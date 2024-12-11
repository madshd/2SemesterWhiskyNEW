package Production;

import java.io.Serializable;
import java.time.LocalDate;

public class ProductCutInformation implements Serializable {
    private final LocalDate date;
    private String infoLine;

    public ProductCutInformation(LocalDate date, String infoLine) {
        this.date = date;
        this.infoLine = infoLine;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getInfoLine() {
        return infoLine;
    }

    public void setInfoLine(String infoLine) {
        this.infoLine = infoLine;
    }

    @Override
    public String toString() {
        return String.format("%s | %s",date.toString(),infoLine);
    }
}
