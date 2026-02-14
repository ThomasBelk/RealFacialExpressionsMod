package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.look.InvalidLookDirNameException;
import net.thomasbelk.real_facial_expressions.look.LookDir;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FaceAnimationSystem extends EntityTickingSystem<EntityStore> {
    FacePacketStore facePacketStore;

    public FaceAnimationSystem(FacePacketStore facePacketStore) {
        this.facePacketStore = facePacketStore;
    }

    @Override
    public void tick(
            float dt,
            int index,
            @NonNull ArchetypeChunk archetypeChunk,
            @NonNull Store store,
            @NonNull CommandBuffer commandBuffer)
    {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        PlayerRef playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;

        var playerFaceAnimType = PlayerFaceAnimationComponent.getComponentType();
        var playerFaceAnimComponent = (PlayerFaceAnimationComponent) store.getComponent(ref, playerFaceAnimType);
        if (playerFaceAnimComponent == null) return;

        var facePacket = facePacketStore.getFacePackForPlayer(playerRef.getUuid());
        if (facePacket != null && facePacket.lookDir() != null) {
            // anim
            try {
                AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, LookDir.getAnimationId(facePacket.lookDir()), true, store);
            } catch (InvalidLookDirNameException e) {
                // idk ignore
            }
        } else if ((playerFaceAnimComponent.getAndIncrementTicksSinceLastPacket() + 1) % 100 == 0) {
            playerRef.sendMessage(Message.raw(Integer.toString(playerFaceAnimComponent.getAndIncrementTicksSinceLastPacket())));
        }

    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(
                Archetype.of(PlayerFaceAnimationComponent.getComponentType()),
                Archetype.of(PlayerRef.getComponentType())
        );
    }
}
