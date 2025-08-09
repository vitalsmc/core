package me.kafae.vitalscorev1.items.mace;

import me.kafae.vitalscorev1.items.MaceItem;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class Mace extends MaceItem {


    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getProfileId() {
        return "mace";
    }

    @Override
    public String getDisplay() {
        return "";
    }

    @Override
    public List<String> getLore() {
        return List.of(
                "§7The only mace..."
        );
    }

    @Override
    protected UUID getUUID() {
        return UUID.randomUUID();
    }

    @Override
    public ItemStack getItem(int n) {
        return getItemBase(n);
    }
}
