package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;
import com.hypixel.hytale.protocol.ServerCameraSettings;

/**
 * A debug command that looks at the front of the player
 */
public class ViewFrontCommand extends AbstractPlayerCommand {
    private static final String NAME = "front";
    private static final String DESC = "Changes camera view to look at character.";
    public ViewFrontCommand() {
        super(NAME, DESC);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, createServerCameraSettings()));
    }

    public static ServerCameraSettings createServerCameraSettings() {
        ServerCameraSettings cameraSettings = new ServerCameraSettings();
        cameraSettings.isFirstPerson = false;
        cameraSettings.displayCursor = false;
        cameraSettings.sendMouseMotion = false;

        cameraSettings.positionLerpSpeed = 0.2f;
        cameraSettings.rotationLerpSpeed = 0.2f;
        cameraSettings.distance = 2.0f;

        cameraSettings.rotationType = RotationType.AttachedToPlusOffset;
        cameraSettings.movementForceRotationType = MovementForceRotationType.AttachedToHead;
        cameraSettings.eyeOffset = true;
        cameraSettings.positionDistanceOffsetType = PositionDistanceOffsetType.DistanceOffset;
        cameraSettings.rotationOffset = new Direction((float) Math.PI, 0, 0);
//        cameraSettings.planeNormal = new Vector3f(0.0f, 1.0f, 0.0f);
        return cameraSettings;
    }
}