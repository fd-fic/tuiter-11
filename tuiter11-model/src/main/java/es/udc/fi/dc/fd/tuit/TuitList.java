package es.udc.fi.dc.fd.tuit;

import java.util.List;

/**
 * The Class TuitList.
 *
 * @author samu Custom class
 */
public class TuitList {

	/** The tuits. */
	private List<CustomTuit> tuits;

	/**
	 * Instantiates a new tuit list.
	 */
	public TuitList() {
	}

	/**
	 * Instantiates a new tuit list.
	 *
	 * @param tuits
	 *            the tuits
	 */
	public TuitList(List<CustomTuit> tuits) {
		super();
		this.tuits = tuits;
	}

	/**
	 * Gets the tuits.
	 *
	 * @return the tuits
	 */
	public List<CustomTuit> getTuits() {
		return tuits;
	}

	/**
	 * Sets the tuits.
	 *
	 * @param tuits
	 *            the new tuits
	 */
	public void setTuits(List<CustomTuit> tuits) {
		this.tuits = tuits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tuits == null) ? 0 : tuits.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TuitList other = (TuitList) obj;
		if (tuits == null) {
			if (other.tuits != null) {
				return false;
			}
		} else if (!tuits.equals(other.tuits)) {
			return false;
		}
		return true;
	}

}
