package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PlayerJoinLeaveSystem extends RefSystem<EntityStore> {

    public PlayerJoinLeaveSystem() {
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
        var id = FacePacketStore.INSTANCE.getNewFaceId();
        if (playerFaceAnimComponent != null) {
            id = playerFaceAnimComponent.getUniqueId();
        } else {
            var p = new PlayerFaceAnimationComponent();
            p.setUniqueId(id);
            commandBuffer.addComponent(ref, playerFaceAnimType, p);
        }

        RealFacialExpressionsPlugin.LOGGER.atInfo().log(playerRef.getUsername() + "'s FaceId = " + id.toString());
        FacePacketStore.INSTANCE.addOrUpdatePlayerFaceId(playerRef.getUuid(), id);
    }

    @Override
    public void onEntityRemove(@NonNull Ref<EntityStore> ref, @NonNull RemoveReason removeReason, @NonNull Store<EntityStore> store, @NonNull CommandBuffer<EntityStore> commandBuffer) {
        if (removeReason != RemoveReason.UNLOAD) return;

        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;

        var playerId = playerRef.getUuid();
        var faceId = FacePacketStore.INSTANCE.getFaceId(playerId);
        var animationComponent = store.getComponent(ref, PlayerFaceAnimationComponent.getComponentType());
        if (animationComponent != null) {
            animationComponent.setUniqueId(faceId);
        }
        FacePacketStore.INSTANCE.removePlayer(playerId);
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Archetype.of(PlayerRef.getComponentType());
    }
}
