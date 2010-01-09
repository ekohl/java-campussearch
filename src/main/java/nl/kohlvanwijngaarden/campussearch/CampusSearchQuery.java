package nl.kohlvanwijngaarden.campussearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.kohlvanwijngaarden.campussearch.result.CampusSearch;
import nl.kohlvanwijngaarden.campussearch.result.Meta;
import nl.kohlvanwijngaarden.campussearch.result.Result;
import nl.kohlvanwijngaarden.campussearch.result.Status;

import com.thoughtworks.xstream.XStream;

/**
 * A query-class for CampusSearch. See <a href=
 * "http://wiki.student.utwente.nl/nl/wiki.php/CampusSearch_XML_interface for the"
 * > documentation</a> for more information.
 * <p>
 * Typical usage:<br>
 * <code>
 * CampusSearchQuery query = new CampusSearchQuery("Heroes");
 * for(Result result : query)
 *     System.out.println(result);
 * </code>
 * 
 * @author ekohl
 */
public class CampusSearchQuery implements Iterable<Result>, Iterator<Result> {
	/** The protocol used to connect to CampusSearch */
	private static final String PROTOCOL = "http";
	/** The host where CampusSearch can be reached */
	private static final String HOST = "search.student.utwente.nl";
	/** The API search file */
	private static final String SEARCH_API = "/api/search";

	/** The (cached) XStream which parses the XML-result into Java-objects */
	private static XStream xstream;

	/** The result iterator */
	private Iterator<Result> iterator;

	/** The request parameters */
	private final Map<String, String> parameters;

	/**
	 * Constructor
	 * 
	 * @param query
	 *            The search query
	 * 
	 * @see #setQuery(String)
	 */
	public CampusSearchQuery(String query) {
		this.parameters = new HashMap<String, String>();
		setQuery(query);
	}

	/**
	 * Fetches a page
	 * 
	 * @throws IOException
	 *             In case the page couldn't be fetched
	 */
	private void getPageIterator() throws IOException {
		iterator = getResults().iterator();
	}

	/**
	 * Returns the request parameters as they would be sent to CampusSearch.
	 * <p>
	 * This is meant for debugging purposes and cannot be modified.
	 * 
	 * @return the request parameters
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	/**
	 * Returns the results from this query
	 * 
	 * @return the results from this query
	 * @throws IOException
	 *             In case there was a network error
	 */
	public List<Result> getResults() throws IOException {
		InputStream in = null;
		try {
			// Retrieve the result
			URL url = getUrl();
			in = url.openStream();
			CampusSearch result = (CampusSearch) getXStream().fromXML(in);

			// In case there was an exception, throw it
			if (result.getStatus().isError())
				throw new CampusSearchException(url, result.getStatus()
						.getErrorCode());

			// Return the results
			return result.getResults();
		} finally {
			// Clean up
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	/**
	 * Returns the URL generated from this query.
	 * 
	 * @return the URL generated from this query.
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	public URL getUrl() throws MalformedURLException,
			UnsupportedEncodingException {
		StringBuilder params = new StringBuilder(SEARCH_API).append("?");
		for (Map.Entry<String, String> parameter : parameters.entrySet()) {
			params.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
			params.append("=").append(parameter.getValue()).append("&");
		}

		return new URL(PROTOCOL, HOST, params.toString());
	}

	// @Override Java 1.6
	public boolean hasNext() {
		if (iterator == null) {
			try {
				getPageIterator();
			} catch (IOException e) {
				return false;
			}
		} else if (!iterator.hasNext()) {
			// See if we can fetch the next page
			nextPage();
			try {
				getPageIterator();
			} catch (IOException e) {
				// Rewind
				prevPage();
				return false;
			}
		}
		return iterator.hasNext();
	}

	// @Override Java 1.6
	public Iterator<Result> iterator() {
		return this;
	}

	// @Override Java 1.6
	public Result next() {
		return iterator.next();
	}

	/**
	 * Increases the page
	 * 
	 * @see #setPage(int)
	 */
	public void nextPage() {
		String page = this.parameters.get("page");
		setPage(page == null ? 2 : Integer.parseInt(page) + 1);
	}

	/**
	 * Decreases the page
	 * 
	 * @see #setPage(int)
	 */
	public void prevPage() {
		String page = this.parameters.get("page");
		setPage(page == null ? 1 : Integer.parseInt(page) - 1);
	}

	// @Override Java 1.6
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Whether to search only for directories.
	 * 
	 * @param directoriesOnly
	 *            Whether to search only for directories.
	 */
	public void setDirectoriesOnly(boolean directoriesOnly) {
		setParameter("dirsonly", directoriesOnly ? "yes" : "no");
	}

	/**
	 * Sets the maximum number of search results. For example 500
	 * 
	 * @param max
	 *            The maximum number of search results
	 */
	public void setMaxSearchResults(int max) {
		setParameter("n", Integer.toString(max));
	}

	/**
	 * Sets the maximal size for each result. For example 0.5M or 1G.
	 * 
	 * @param maxSize
	 *            The maximal size for each result
	 */
	public void setMaxSize(String maxSize) {
		setParameter("maxsize", maxSize);
	}

	/**
	 * Sets the minimal required size for each result. For example 0.5M or 1G.
	 * 
	 * @param minSize
	 *            The minimal required size for each result
	 */
	public void setMinSize(String minSize) {
		setParameter("minsize", minSize);
	}

	/**
	 * The page number. Each page has 30 results. The first page has 0-29, the
	 * second 30-59, etc.
	 * 
	 * @param page
	 *            The page number
	 */
	public void setPage(int page) {
		setParameter("page", Integer.toString(page));
	}

	/**
	 * Sets the <code>parameter</code> to <code>value</code>.
	 * <p>
	 * <b>Warning</b>: using this method is not recommended and mostly meant for
	 * internal usage. If you set a parameter to an invalid value, this class
	 * can break.
	 * 
	 * @param parameter
	 *            The parameter to set
	 * @param value
	 *            The value to set
	 * @return The old value for <code>parameter</code>
	 */
	public String setParameter(String parameter, String value) {
		return this.parameters.put(parameter, value);
	}

	/**
	 * Sets the search query, for example "Heroes"
	 * 
	 * @param query
	 *            The search query
	 */
	public void setQuery(String query) {
		setParameter("q", query);
	}

	/**
	 * Unsets the given <code>parameter</code>.
	 * 
	 * @param parameter
	 *            The parameter to remove
	 * @return The old value for <code>parameter</code>
	 */
	public String unsetParameter(String parameter) {
		if ("query".equals(parameter))
			throw new IllegalArgumentException("Parameter query is required");
		return this.parameters.remove(parameter);
	}

	/**
	 * Returns an XStream object which can parse the XML-result into
	 * Java-objects.
	 * 
	 * @return The XStream object
	 */
	private static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.alias("campus_search", CampusSearch.class);
			xstream.addImplicitCollection(CampusSearch.class, "results");
			xstream.alias("status", Status.class);
			xstream.alias("meta", Meta.class);
			xstream.alias("result", Result.class);
		}
		return xstream;
	}
}
