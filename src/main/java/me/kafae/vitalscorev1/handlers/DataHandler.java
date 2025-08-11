package me.kafae.vitalscorev1.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.kafae.vitalscorev1.Main;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("vitals/core/player");
    private static final Path BACKUP_PATH = Path.of("vitals/core/backup/player");
    public Map<String, Profile> profiles = new ConcurrentHashMap<>();

    // getters
    public Path getPath() {
        return PATH;
    }
    public Path getBackupPath() {
        return BACKUP_PATH;
    }

    // default profile
    public static class Profile {
        private float rm = 1f;

        // getters
        public float getRm() {
            return rm;
        }

        // setters
        public void setRm(Float f) {
            rm = Math.round(f * 10) / 10f;
        }
    }

    public void loadProfile(ServerPlayerEntity p) {
        String uuid = p.getUuid().toString();
        File f = PATH.resolve(uuid + ".json").toFile();
        if (f.exists()) {
            try {
                profiles.put(uuid, gson.fromJson(Files.readString(f.toPath()), Profile.class));
                Main.getLogger().info("Successfully loaded data of player of UUID %s".formatted(p.getUuid().toString()));
            } catch (Exception e) {
                try {
                    profiles.put(uuid, gson.fromJson(Files.readString(BACKUP_PATH.resolve(uuid + ".json")), Profile.class));
                    Main.getLogger().warn("Data of player of UUID %s is null/corrupted, retrieving from backup instead".formatted(p.getUuid().toString()));
                } catch (Exception ee) {
                    p.networkHandler.disconnect(Text.literal("Cannot retrieve backup file, please contact server staff"));
                    Main.getLogger().error("Backup data of player of UUID %s is null/corrupted".formatted(p.getUuid().toString()));
                }
            }
        } else {
            profiles.put(uuid, new Profile());
            Main.getLogger().warn("Data of player of UUID %s not found, creating a new one".formatted(p.getUuid().toString()));
        }
    }

    public void saveProfile(String uuid) {
        saveProfile(uuid, false);
    }

    public void saveProfile(String uuid, Boolean backup) {
        try {
            if (backup) {
                File f = BACKUP_PATH.resolve(uuid + ".json").toFile();
                Files.writeString(f.toPath(), gson.toJson(profiles.get(uuid)));
            } else {
                File f = PATH.resolve(uuid + ".json").toFile();
                Files.writeString(f.toPath(), gson.toJson(profiles.get(uuid)));
                profiles.remove(uuid);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
