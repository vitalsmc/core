package me.kafae.vitalscorev1.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DataHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("vitals/core/player");
    public Map<String, Profile> profiles = new HashMap<>();

    // getters
    public Path getPath() {
        return PATH;
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

    public void loadProfile(String uuid) {
        File f = PATH.resolve(uuid + ".json").toFile();
        if (f.exists()) {
            try {
                profiles.put(uuid, gson.fromJson(Files.readString(f.toPath()), Profile.class));
            } catch (Exception ignored) {
                profiles.put(uuid, new Profile());
            }
        } else {
            profiles.put(uuid, new Profile());
        }
    }

    public void saveProfile(String uuid) {
        saveProfile(uuid, false);
    }

    public void saveProfile(String uuid, Boolean dump) {
        File f = PATH.resolve(uuid + ".json").toFile();
        try {
            Files.writeString(f.toPath(), gson.toJson(profiles.get(uuid)));
            if (dump) {
                profiles.remove(uuid);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
