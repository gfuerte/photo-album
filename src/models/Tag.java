package models;

import java.io.Serializable;

/**
 * @author Greg Fuerte
 * @author Aries Regalado
 */
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	
	public Tag(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Return this tag's key.
	 * @return String value of key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets this tag's key to parameter.
	 * @param key Desired key to be set.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns this tag's value.
	 * @return String value of value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Return this tag's value to parameter.
	 * @param value Desired value to be set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
