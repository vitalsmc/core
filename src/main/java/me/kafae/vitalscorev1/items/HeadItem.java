package me.kafae.vitalscorev1.items;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class HeadItem {

    private final String line = "§7-------------------------";

    protected abstract String getId();
    protected abstract UUID getUUID();
    protected abstract String getDisplay();
    protected abstract List<String> getLore();
    protected abstract String getTexture();

    public ItemStack getItem(Integer n) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD, n);

        // set name
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(getDisplay()));

        // set lore
        List<Text> flore = new ArrayList<>();
        flore.add(Text.literal(line));
        flore.add(Text.literal(" "));

        for (String s : getLore()) {
            flore.add(Text.literal("§7" + s));
        }

        flore.add(Text.literal(" "));
        flore.add(Text.literal("§7ID: " + getId()));

        stack.set(DataComponentTypes.LORE, new LoreComponent(flore));

        // game profile
        GameProfile prof = new GameProfile(getUUID(), "headitem");

        // add texture
        prof.getProperties().put("textures", new Property("textures", getTexture()));

        // profile component
        ProfileComponent comp = new ProfileComponent(prof);

        // set profile
        stack.set(DataComponentTypes.PROFILE, comp);

        return stack;
    }

}
