package dto;

public class Source {
    private final String id;
    private final String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }
}