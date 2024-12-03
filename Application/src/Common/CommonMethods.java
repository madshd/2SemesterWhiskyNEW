package Common;

import java.time.LocalDate;

public abstract class CommonMethods {

    /**
     * Includes start and end date
     * @param date
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isDateBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate) || date.isBefore(endDate));
    }
}
