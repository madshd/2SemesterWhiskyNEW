package Warehousing;

public class Supplier {
    private final String name;
    private final String address;
    private final String description;
    private final String story;

    public Supplier(String name, String address, String description, String story) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.story = story;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getStory() {
        return story;
    }
}
