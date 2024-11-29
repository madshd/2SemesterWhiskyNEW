package Interfaces;

import Warehousing.Warehouse;

public interface WarehousingObserver {
   void update(Warehouse warehouse, String changeDetails);
}
