import Controllers.Production;
import Interfaces.StorageInterface;
import Storage.Storage;
import Storage.initStorage;
import Production.TestProduction;

public class App {
	public static void main(String[] args) {
		StorageInterface storage = new Storage();
		Controllers.Production.setStorage(storage);
		Controllers.Warehousing.setStorage(storage);
		Controllers.Common.setStorage(storage);
		Controllers.BatchArea.setStorage(storage);
		initStorage.loadProduction();
	    //initStorage.loadWarehousing();

		initStorage.loadBatchArea();
		// TestProduction.firstTest();

		System.out.println("LAUNCHING LABELTALES");
		GUI.LaunchPad.Launch.main(args);

	}
}
