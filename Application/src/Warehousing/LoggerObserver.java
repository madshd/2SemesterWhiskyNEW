package Warehousing;

import Interfaces.WarehousingObserver;

import java.util.ArrayList;
import java.util.List;

public class LoggerObserver implements WarehousingObserver {
    private List<String> logs = new ArrayList<>();

    @Override
    public void update(Warehouse warehouse, String changeDetails) {
        String logEntry = "Log: [" + warehouse.getName() + "] - " + changeDetails;
        logs.add(logEntry);
        System.out.println(logEntry);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs); // Return a copy to preserve encapsulation
    }

    public void clearLogs() {
        logs.clear();
    }
}