package Controllers;

import Interfaces.StorageInterface;

/*
 * Used for methods that can be shared between areas.
 */
public class Common {
	private static StorageInterface storage;

	public static void setStorage(StorageInterface storage){
		Common.storage = storage;
	}

}
