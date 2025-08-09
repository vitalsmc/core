package me.kafae.vitalscorev1.items;

import com.mojang.authlib.GameProfile;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MaceItem implements VitalsItem {

    public abstract String getProfileId();
    public abstract List<String> getLore();
    protected abstract UUID getUUID();

    protected ItemStack getItemBase(Integer n) {
        ItemStack stack = new ItemStack(Items.MACE, n);
        stack.set(DataComponentTypes.LORE, new LoreComponent(new ArrayList<Text>() {{
            getLore().forEach(s -> {
                add(Text.literal(s));
            });
        }}));
        GameProfile prof = new GameProfile(getUUID(), getProfileId());
        ProfileComponent comp = new ProfileComponent(prof);
        stack.set(DataComponentTypes.PROFILE, comp);
        return stack;
    }

}
