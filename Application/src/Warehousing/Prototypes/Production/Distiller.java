package Warehousing.Prototypes.Production;

public class Distiller {
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
}
