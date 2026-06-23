package sgdss;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataStore {
    private final Path storagePath;
    private AppState state;

    public DataStore() {
        this(Paths.get("data", "sgdss.dat"));
    }

    public DataStore(Path storagePath) {
        this.storagePath = storagePath;
    }

    public AppState loadOrCreate() {
        try {
            if (Files.exists(storagePath) && Files.size(storagePath) > 0) {
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(storagePath))) {
                    state = (AppState) in.readObject();
                    return state;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load existing state, creating fresh one: " + e.getMessage());
        }
        state = DemoData.bootstrap();
        save();
        return state;
    }

    public synchronized void save() {
        try {
            Files.createDirectories(storagePath.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(storagePath))) {
                out.writeObject(state);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not save application data", e);
        }
    }

    public AppState getState() {
        if (state == null) state = loadOrCreate();
        return state;
    }
}
