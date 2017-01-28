package es.udc.fi.dc.fd.tuit;

import java.util.Calendar;


/**
 * The Class CustomTuit.
 */
public class CustomTuit {

	/** The nick user. */
	private String nickUser;

	/** The fecha. */
	private Calendar fecha;

	/** The contenido. */
	private String contenido;

	/**
	 * Instantiates a new custom tuit.
	 */
	public CustomTuit() {
	}

	/**
	 * Instantiates a new custom tuit.
	 *
	 * @param nickUser
	 *            the nick user
	 * @param fecha
	 *            the fecha
	 * @param contenido
	 *            the contenido
	 */
	public CustomTuit(String nickUser, Calendar fecha, String contenido) {
		super();
		this.nickUser = nickUser;
		this.fecha = fecha;
		this.contenido = contenido;
	}

	/**
	 * Gets the nick user.
	 *
	 * @return the nick user
	 */
	public String getNickUser() {
		return nickUser;
	}

	/**
	 * Sets the nick user.
	 *
	 * @param nickUser
	 *            the new nick user
	 */
	public void setNickUser(String nickUser) {
		this.nickUser = nickUser;
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Calendar getFecha() {
		return fecha;
	}

	/**
	 * Sets the fecha.
	 *
	 * @param fecha
	 *            the new fecha
	 */
	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	/**
	 * Gets the contenido.
	 *
	 * @return the contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * Sets the contenido.
	 *
	 * @param contenido
	 *            the new contenido
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
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
		result = prime * result
				+ ((contenido == null) ? 0 : contenido.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result
				+ ((nickUser == null) ? 0 : nickUser.hashCode());
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
		CustomTuit other = (CustomTuit) obj;
		if (contenido == null) {
			if (other.contenido != null) {
				return false;
			}
		} else if (!contenido.equals(other.contenido)) {
			return false;
		}
		if (fecha == null) {
			if (other.fecha != null) {
				return false;
			}
		} else if (!fecha.equals(other.fecha)) {
			return false;
		}
		if (nickUser == null) {
			if (other.nickUser != null) {
				return false;
			}
		} else if (!nickUser.equals(other.nickUser)) {
			return false;
		}
		return true;
	}

}
