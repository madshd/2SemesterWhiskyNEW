package Warehousing;

import Interfaces.WarehousingObserver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LoggerObserver implements WarehousingObserver {
    private static List<String> logs = new ArrayList<>();

    @Override
    public void update(Warehouse warehouse, String changeDetails) {
        String logEntry = "[" + LocalDateTime.of(LocalDate.now(), LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute())) + "]" + " Log: [" + warehouse.getName() + "] - " + changeDetails;
        logs.add(0, logEntry);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public static List<String> getLogsByWarehouse(Warehouse warehouse) {
        List<String> logsByWarehouse = new ArrayList<>();
        for (String log : logs) {
            if (log.contains("[" + warehouse.getName() + "]")) {
                logsByWarehouse.add(log);
            }
        }
        return logsByWarehouse;
    }

    public void clearLogs() {
        logs.clear();
    }
}
