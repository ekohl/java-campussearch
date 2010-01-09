package nl.kohlvanwijngaarden.campussearch.result;

/**
 * 
 * @author ekohl
 */
public class Result {
	private String dir;
	private String full_path;
	private String ip;
	private Meta meta;
	private String name;
	private String netbios_name;
	private boolean online;
	private int rank;
	private long size;
	private String type;

	/**
	 * Returns the directory
	 * 
	 * @return the directory
	 */
	public String getDirectory() {
		return dir;
	}

	/**
	 * Returns the full path
	 * 
	 * @return the full path
	 */
	public String getFullPath() {
		return full_path;
	}

	public String getIP() {
		return ip;
	}

	/**
	 * Returns the meta data from this result
	 * 
	 * @return the meta data from this result
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * Returns the name of the result
	 * 
	 * @return the name of the result
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the netbios name hosting the result
	 * 
	 * @return the netbios name hosting the result
	 */
	public String getNetbiosName() {
		return netbios_name;
	}

	/**
	 * Returns the ranking
	 * 
	 * @return the ranking
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Returns the size in bytes
	 * 
	 * @return the size in bytes
	 */
	public long getSize() {
		return size;
	}

	/**
	 * The type of result (e.g. directory or file)
	 * 
	 * @return The type of result
	 */
	public String getType() {
		return type;
	}

	/**
	 * Whether the result is online or not
	 * 
	 * @return whether the result is online or not
	 */
	public boolean isOnline() {
		return online;
	}

	@Override
	public String toString() {
		return full_path;
	}
}
