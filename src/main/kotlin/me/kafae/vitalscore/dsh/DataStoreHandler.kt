package me.kafae.vitalscore.dsh

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.nio.file.Files
import java.nio.file.Path
import me.kafae.vitalscore.Main

class DataStoreHandler {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    val dir: Path = Path.of("vitals/core/player")

    data class Profile(
        var rm: Float = 1f,
    )

    fun isThere(uid: String): Boolean {
        return dir.resolve("${uid}.json").toFile().exists()
    }

    fun new(uid: String) {
        Main.profiles[uid] = Profile()
    }

    fun load(uid: String): Profile {
        val f = dir.resolve("${uid}.json").toFile()
        return if (f.exists()) {
            try {
                gson.fromJson(f.readText(), Profile::class.java)?: Profile()
            } catch (e: Exception) {
                Profile()
            }
        } else {
            Profile()
        }
    }

    fun loadAll(): Boolean {
        Files.list(dir).forEach { path ->
            if (path.toString().endsWith(".json")) {
                val uid = path.fileName.toString().removeSuffix(".json")
                val pd = load(uid)
                Main.profiles[uid] = pd
            }
        }
        return true
    }

    fun save(uid: String): Boolean {
        val f = dir.resolve("${uid}.json").toFile()
        f.writeText(gson.toJson(Main.profiles[uid]))
        Main.profiles.remove(uid)
        return true
    }

    fun savewomemdump(uid: String): Boolean {
        val f = dir.resolve("${uid}.json").toFile()
        f.writeText(gson.toJson(Main.profiles[uid]))
        return true
    }

    fun saveAll(): Boolean {
        Main.profiles.forEach { (uid, pd) ->
            val f = dir.resolve("${uid}.json").toFile()
            f.writeText(gson.toJson(pd))
        }
        return true
    }
}