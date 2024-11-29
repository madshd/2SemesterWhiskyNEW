import Interfaces.StorageInterface;
import Storage.Storage;
import Storage.initStorage;

public class App {
	public static void main(String[] args) {
		StorageInterface storage = new Storage();
		// Controllers.Production.setStorage(storage);
		Controllers.Warehousing.setStorage(storage);
		Controllers.BatchArea.setStorage(storage);
		// Controllers.Common.setStorage(storage);
		// initStorage.loadProduction();
		initStorage.loadWarehousing();

		System.out.println("LAUNCHING LABELTALES");
		GUI.LaunchPad.Launch.main(args);

	}
}
