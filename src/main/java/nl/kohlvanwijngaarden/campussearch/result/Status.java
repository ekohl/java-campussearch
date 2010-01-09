package nl.kohlvanwijngaarden.campussearch.result;

/**
 * 
 * @author ekohl
 * 
 */
public class Status {
	/** The status message */
	private String status_msg;
	/** The error code */
	private String error_code;

	/**
	 * @return the status_msg
	 */
	public String getStatusMsg() {
		return status_msg;
	}

	/**
	 * @return the error_code
	 */
	public String getErrorCode() {
		return error_code;
	}

	/**
	 * Returns whether an error occurred
	 * 
	 * @return whether an error occurred
	 */
	public boolean isError() {
		return "error".equals(status_msg);
	}
	
	@Override
	public String toString() {
		return isError() ? status_msg + ": " + error_code : status_msg;
	}
}
