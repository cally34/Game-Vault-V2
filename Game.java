import java.io.Serializable;

public abstract class Game implements Serializable {
    private String id;       
    private String title;

    public Game(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public abstract String getGameDetails();
    public abstract String toCSV();
}