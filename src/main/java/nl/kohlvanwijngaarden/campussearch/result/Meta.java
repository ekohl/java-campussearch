package nl.kohlvanwijngaarden.campussearch.result;

import java.net.URL;

/**
 * FIXME: Can this be a map instead of a class?
 * 
 * @author ekohl
 * 
 */
public class Meta {
	private URL imdb_url;
	private int imdb_year;
	private double imdb_rating;
	private URL tvrage_episode_url;

	/**
	 * @return the imdb_url
	 */
	public URL getImdb_url() {
		return imdb_url;
	}

	/**
	 * @return the imdb_year
	 */
	public int getImdb_year() {
		return imdb_year;
	}

	/**
	 * @return the imdb_rating
	 */
	public double getImdb_rating() {
		return imdb_rating;
	}

	/**
	 * @return the tvrage_episode_url
	 */
	public URL getTvrage_episode_url() {
		return tvrage_episode_url;
	}
}
