import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VaultManager {
    private Map<String, Game> vault = new HashMap<>();

    public void insertGame(Game game) throws Exception {
        if (vault.containsKey(game.getId())) {
            throw new Exception("Error: ID " + game.getId() + " is already in use.");
        }
        vault.put(game.getId(), game);
    }

    public void updateGame(String id, String newTitle, String newType) throws Exception {
        if (!vault.containsKey(id)) {
            throw new Exception("Missing Record: ID " + id + " not found.");
        }
        Game g = vault.get(id);
        g.setTitle(newTitle);
        if (g instanceof PCGame) {
            ((PCGame) g).setType(newType);
        }
    }

    public void deleteGame(String id) throws Exception {
        if (!vault.containsKey(id)) {
            throw new Exception("Missing Record: ID " + id + " not found.");
        }
        vault.remove(id);
    }

    public Set<String> getExistingIds() {
        return vault.keySet();
    }

    public String getAllGamesDisplay() {
        if (vault.isEmpty()) return "The vault is empty.";
        StringBuilder sb = new StringBuilder();
        for (Game g : vault.values()) {
            sb.append(g.getGameDetails()).append("\n");
        }
        return sb.toString();
    }

    public void saveToFile(File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Game g : vault.values()) {
                bw.write(g.toCSV());
                bw.newLine();
            }
        }
    }

    public void loadFromFile(File file) throws IOException {
        vault.clear(); 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    vault.put(parts[0], new PCGame(parts[0], parts[1], parts[2]));
                }
            }
        }
    }
}