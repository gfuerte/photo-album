package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Serial implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "dat";
	public static final String storeFile = "data.dat";

	private List<User> users;

	public Serial() {
		this.users = new ArrayList<>();
	}

	public List<User> getUserList() {
		return this.users;
	}

	public static void serialize(Serial serial) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(serial);
	}

	public static Serial deserialize() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		Serial serial = (Serial) ois.readObject();
		ois.close();
		return serial;
	}
}
