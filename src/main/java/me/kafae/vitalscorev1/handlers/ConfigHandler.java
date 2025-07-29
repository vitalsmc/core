package me.kafae.vitalscorev1.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHandler {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("vitals/core");
    private Config config = new Config();

    // getters
    public Path getPath() {
        return PATH;
    }

    // base config
    public static class Config {
        private int pearlCD = 1200; // as ticks
        private boolean allowNethArmorAndSword = false;
        private boolean allowEnd = false;
        private boolean isTesting = false;

        // getters
        public int getPearlCD() {
            return pearlCD;
        }

        public boolean getAllowNethArmorAndSword() {
            return allowNethArmorAndSword;
        }

        public boolean getAllowEnd() {
            return allowEnd;
        }

        public boolean getIsTesting() {
            return isTesting;
        }

        // setters
        public void setPearlCD(int cd) {
            pearlCD = cd;
        }

        public void setAllowNethArmorAndSword(boolean a) {
            allowNethArmorAndSword = a;
        }

        public void setAllowEnd(boolean a) {
            allowEnd = a;
        }
    }

    // getters
    public Config getConfig() {
        return config;
    }

    // load config
    public void loadConfig() {
        File f = PATH.resolve("config.json").toFile();
        if (f.exists()) {
            try {
                config = gson.fromJson(Files.readString(f.toPath()), Config.class);
            } catch (IOException ignored) {
                config = new Config();
            }
        } else {
            config = new Config();
        }
    }

    // save config
    public void saveConfig() {
        File f = PATH.resolve("config.json").toFile();
        try {
            Files.writeString(f.toPath(), gson.toJson(config));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // update config
    public void updateConfig() {
        saveConfig();
        loadConfig();
    }

}
