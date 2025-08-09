package me.kafae.vitalscorev1.items;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface VitalsItem {

    String getId();
    String getProfileId();
    String getDisplay();
    List<String> getLore();

    ItemStack getItem(int n);

}
