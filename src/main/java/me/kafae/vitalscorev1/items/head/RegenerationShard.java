package me.kafae.vitalscorev1.items.head;

import me.kafae.vitalscorev1.items.HeadItem;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class RegenerationShard extends HeadItem {

    @Override
    public String getId() {
        return "regeneration_shard";
    }

    @Override
    public String getProfileId() {
        return "regenshard";
    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @Override
    public String getDisplay() {
        return "§d§ke§r §cRegeneration Shard §r§d§ke";
    }

    @Override
    public List<String> getLore() {
        return List.of(
                "§6§lRight Click: §4§lConsume",
                "Gain §c+0.1x§r §7regeneration",
                "multiplier."
        );
    }

    @Override
    public ItemStack getItem(int n) {
        return getItemBase(n);
    }

    @Override
    public String getTexture() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ViM2Q3MWNkNWFjOTFkOGJkMWQ2MWZjZWI1NTU3YjllMTljMGI5YTU0ZGMyNzIwZGZmZDVmYmNjMTIyN2ZjMCJ9fX0=";
    }

}
