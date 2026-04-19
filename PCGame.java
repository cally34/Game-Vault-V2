public class PCGame extends Game {
    private String type; // Now "Physical" or "Digital"

    public PCGame(String id, String title, String type) {
        super(id, title);
        this.type = type;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String getGameDetails() {
        return "ID: " + getId() + " | Title: " + getTitle() + " | Type: " + type;
    }

    @Override
    public String toCSV() {
        return getId() + "," + getTitle() + "," + type;
    }
}