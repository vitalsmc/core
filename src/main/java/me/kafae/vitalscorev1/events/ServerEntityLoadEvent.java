package me.kafae.vitalscorev1.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;

public class ServerEntityLoadEvent {

    // register
    public void register() {
        ServerEntityEvents.ENTITY_LOAD.register(this::onServerEntityLoad);
    }

    // event logic
    private void onServerEntityLoad(Entity e, ServerWorld w) {
        if (e instanceof ItemEntity) {
            if (((ItemEntity) e).getStack().getComponents().contains(DataComponentTypes.PROFILE)) {
                e.setGlowing(true);
                e.setInvulnerable(true);
            }
        }
    }

}
