package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
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
                String lookDir = LookDir.getAnimationId(facePacket.lookDir());
                String browPos = BrowPos.getBrowPosFromBlendshapes(facePacket.blendshapes()).toString();
                String mouthState = MouthState.getMouthStateFromBlendshapes(facePacket.blendshapes()).toString();
                String leftEyeLid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), true).toString();
                String rightEyeLid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), false).toString();
                RealFacialExpressionsPlugin.LOGGER.atInfo().log("Brow: " + browPos + " Mouth: " + mouthState + " Right: " + rightEyeLid + " Left: " + leftEyeLid);

//                AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, LookDir.getAnimationId(facePacket.lookDir()), true, store);
                var leftlid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), true);
                var rightlid = EyelidState.getEyeLidStateFromBlendshapes(facePacket.blendshapes(), false);
                var brows = BrowPos.getBrowPosFromBlendshapes(facePacket.blendshapes());
                var mouth = MouthState.getMouthStateFromBlendshapes(facePacket.blendshapes());
                var animName = getAnimName(lookDir, leftlid, rightlid, brows, mouth);
                RealFacialExpressionsPlugin.LOGGER.atInfo().log(animName);
                AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, animName, true, store);
                // another animation in the same spot clobbers previous animation. Is what I expected but am still sad lol.
                //AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, "LeftEyelid50", true, store);
            } catch (InvalidLookDirNameException e) {
                // idk ignore, could potentially send error to the player in future? Idk if that would be helpful though
            }
        }
    }

    private String getAnimName(String lookDir, EyelidState leftEyelid, EyelidState rightEyelid, BrowPos browPos, MouthState mouthState) {
        StringBuilder s = new StringBuilder();
        String sep = "_";
        s.append(lookDir).append(sep);
        s.append(leftEyelid.getLeftAnimId()).append(sep);
        s.append(rightEyelid.getRightAnimId()).append(sep);
        s.append(browPos.getAnimId()).append(sep);
        s.append(mouthState.getAnimId());
        return s.toString();
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(
                Archetype.of(PlayerFaceAnimationComponent.getComponentType()),
                Archetype.of(PlayerRef.getComponentType())
        );
    }
}
