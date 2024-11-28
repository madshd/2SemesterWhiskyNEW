package Production;

import java.time.LocalDate;

public class AlcoholPercentage {
    private final LocalDate date;
    private double alcoholPercentage;

    public AlcoholPercentage(LocalDate date, double alcoholPercentage) {
        this.date = date;
        this.alcoholPercentage = alcoholPercentage;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(double alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    @Override
    public String toString() {
        return String.format("%s | Alcohol percentage: %,.2f",date.toString(),alcoholPercentage);
    }
}
