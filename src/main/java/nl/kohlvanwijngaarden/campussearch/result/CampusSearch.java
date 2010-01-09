package nl.kohlvanwijngaarden.campussearch.result;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author ekohl
 * 
 */
@XStreamAlias("campus_search")
public class CampusSearch {
	/** The status of the search */
	private Status status;
	/** The results */
	@XStreamImplicit
	private List<Result> results;

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the results
	 */
	public List<Result> getResults() {
		return results;
	}
}
