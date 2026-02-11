package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PlayerJoinSystem extends RefSystem<EntityStore> {
    FacePacketStore facePacketStore;

    public PlayerJoinSystem(FacePacketStore facePacketStore) {
        this.facePacketStore = facePacketStore;
    }

    @Override
    public void onEntityAdded(
            @NonNull Ref<EntityStore> ref,
            @NonNull AddReason addReason,
            @NonNull Store<EntityStore> store,
            @NonNull CommandBuffer<EntityStore> commandBuffer)
    {
        if (addReason != AddReason.LOAD) return;

        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;

        var playerFaceAnimType = PlayerFaceAnimationComponent.getComponentType();
        var playerFaceAnimComponent = store.getComponent(ref, playerFaceAnimType);

        if (playerFaceAnimComponent != null) {
            playerRef.sendMessage(Message.raw(
                    "Already has component: " + playerFaceAnimComponent.toString()
            ));
        } else {
            var p = new PlayerFaceAnimationComponent();
            commandBuffer.addComponent(ref, playerFaceAnimType, p);
            playerRef.sendMessage(Message.raw(
                    "Added component: " + p.toString()
            ));
        }

        // assign the player a faceId
        // this line feels a bit odd... look into refactoring
        facePacketStore.addOrUpdatePlayerFaceId(playerRef.getUuid(), facePacketStore.getNewFaceId());
    }

    @Override
    public void onEntityRemove(@NonNull Ref<EntityStore> ref, @NonNull RemoveReason removeReason, @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        if (removeReason != RemoveReason.UNLOAD) return;

        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;
        facePacketStore.removePlayer(playerRef.getUuid());
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Archetype.of(PlayerRef.getComponentType());
    }
}
