package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Serial implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "dat";
	public static final String storeFile = "data.dat";

	private List<User> users;

	public Serial() {
		this.users = new ArrayList<>();
	}

	/**
	 * Returns every registered user in the application.
	 * @return Arraylist value of all Users.
	 */
	public List<User> getUserList() {
		return this.users;
	}

	/**
	 * Writes to data.dat of all objects in the application.
	 * @param serial Serial parameter that is to be written.
	 * @throws IOException Exception if input is null.
	 */
	public static void serialize(Serial serial) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile))) {
			oos.writeObject(serial);
		}
	}

	/**
	 * Reads data.dat of all objects in the application.
	 * @return Returns Serial object of all objects in the application.
	 * @throws IOException Exception if null.
	 * @throws ClassNotFoundException Exception if file does not exist.
	 */
	public static Serial deserialize() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Serial serial = (Serial) ois.readObject();
		ois.close();
		return serial;
	}
}
