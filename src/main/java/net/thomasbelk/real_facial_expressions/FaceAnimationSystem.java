package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.enums.*;
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
            @NonNull ArchetypeChunk<EntityStore> archetypeChunk,
            @NonNull Store<EntityStore> store,
            @NonNull CommandBuffer<EntityStore> commandBuffer)
    {
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;

        var playerFaceAnimType = PlayerFaceAnimationComponent.getComponentType();
        var playerFaceAnimComponent = store.getComponent(ref, playerFaceAnimType);
        if (playerFaceAnimComponent == null) return;

        var facePacket = facePacketStore.getFacePackForPlayer(playerRef.getUuid());
        if (facePacket != null && facePacket.lookDir() != null) {
            // anim
            try {
                String lookDir = LookDir.getAnimationId(facePacket.lookDir());
                var leftLid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), true);
                var rightLid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), false);
                var brows = BrowPos.getBrowPosFromBlendshapes(facePacket.blendshapes());
                var mouth = MouthState.getMouthStateFromBlendshapes(facePacket.blendshapes());
                var animName = getAnimName(lookDir, leftLid, rightLid, brows, mouth);

                AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, animName, true, store);
            } catch (InvalidLookDirNameException e) {
                // ignore, could potentially send error to the player in future? Idk if that would be helpful though
            }
        }
    }

    private String getAnimName(String lookDir, EyelidState leftEyelid, EyelidState rightEyelid, BrowPos browPos, MouthState mouthState) {
        String sep = "_";
        return lookDir + sep +
                leftEyelid.getLeftAnimId() + sep +
                rightEyelid.getRightAnimId() + sep +
                browPos.getAnimId() + sep +
                mouthState.getAnimId();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(
                Archetype.of(PlayerFaceAnimationComponent.getComponentType()),
                Archetype.of(PlayerRef.getComponentType())
        );
    }
}
