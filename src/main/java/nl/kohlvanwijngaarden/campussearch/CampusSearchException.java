package nl.kohlvanwijngaarden.campussearch;

import java.io.IOException;
import java.net.URL;

/**
 * @author ekohl
 * 
 */
public class CampusSearchException extends IOException {
	/** Generated serial */
	private static final long serialVersionUID = -4748128527084638069L;
	/** The URL that triggered the exception */
	private final URL url;

	/**
	 * Constructor
	 * 
	 * @param url
	 *            The URL that triggered the error
	 * @param message
	 *            The error message
	 */
	public CampusSearchException(final URL url, final String message) {
		super(message);
		this.url = url;
	}

	/**
	 * Returns the URL that triggered the error
	 * 
	 * @return the URL that triggered the error
	 */
	public URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return url + ": " + getMessage();
	}
}
