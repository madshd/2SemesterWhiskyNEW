package Production;

import java.io.Serializable;

public class Distiller implements Serializable {
    private final String name;
    private final String initials;
    private String story;

    public Distiller(String name, String initials, String story) {
        this.name = name;
        this.initials = initials;
        this.story = story;
    }

    public String getName() {
        return name;
    }

    public String getInitials() {
        return initials;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\t Initials: %s",name,initials);
    }
}
