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

public abstract class HeadItem implements VitalsItem {

    private static final String line = "§7-------------------------";

    public abstract String getId();
    protected abstract UUID getUUID();
    public abstract String getProfileId();
    public abstract String getDisplay();
    public abstract List<String> getLore();
    protected abstract String getTexture();

    protected ItemStack getItemBase(Integer n) {
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
        GameProfile prof = new GameProfile(getUUID(), getProfileId());

        // add texture
        prof.getProperties().put("textures", new Property("textures", getTexture()));

        // profile component
        ProfileComponent comp = new ProfileComponent(prof);

        // set profile
        stack.set(DataComponentTypes.PROFILE, comp);

        // make unstackable
        stack.set(DataComponentTypes.MAX_STACK_SIZE, 1);

        return stack;
    }

}
