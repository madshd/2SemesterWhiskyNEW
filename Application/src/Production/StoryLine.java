package Production;

import java.time.LocalDate;

public class StoryLine {
    private final LocalDate date;
    private String storyLine;

    public StoryLine(LocalDate date, String storyLine) {
        this.date = date;
        this.storyLine = storyLine;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    @Override
    public String toString() {
        return String.format("%s | %s", date.toString(),storyLine);
    }
}
