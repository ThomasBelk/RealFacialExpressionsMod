package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.RealFacialExpressionsPlugin;
import net.thomasbelk.real_facial_expressions.components.PlayerFaceAnimationComponent;
import net.thomasbelk.real_facial_expressions.ui.FaceSettingsUI;
import org.jspecify.annotations.NonNull;

public class FaceSettingsCommand extends AbstractPlayerCommand {
    private static final String NAME = "settings";
    private static final String DESC = "Adjust the detection thresholds that determine when facial expressions are triggered, such as identifying when your eyes are closed or your mouth is open.";

    public FaceSettingsCommand() {
        super(NAME, DESC);
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        var playerAnimComponent = store.getComponent(ref, PlayerFaceAnimationComponent.getComponentType());
        if (playerAnimComponent == null) return;
        var playerFaceData = playerAnimComponent.getFaceSettings();
        if (playerFaceData == null) return;
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        var position = playerRef.getTransform().getPosition();
        var rotation = new com.hypixel.hytale.math.vector.Vector3f(0f, 0f, 0f);

        // Am I crazy or should this use CommandBuffer? How do I get the Command Buffer? It works though.
        // doing this to make sure we can always see players face when we enter the menu to tweak expressions.
        Teleport teleport = Teleport.createForPlayer(position, rotation);
        store.addComponent(ref, Teleport.getComponentType(), teleport);



//        var transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
//        if (transformComponent != null) {
//            var temp = new TransformComponent();
//            temp.setRotation(new com.hypixel.hytale.math.vector.Vector3f(0f,0f,0f));
//            temp.setPosition(transformComponent.getTransform().getPosition());
//            assert playerRef.getHolder() != null;
//            playerRef.getHolder().putComponent(TransformComponent.getComponentType(), temp);
//           transformComponent.getTransform().setRotation(new com.hypixel.hytale.math.vector.Vector3f(0f, 0f, 0f));
//            RealFacialExpressionsPlugin.LOGGER.atInfo().log(transformComponent.toString());
//        }
//
//        playerRef.updatePosition(store.getExternalData().getWorld(), transform, new com.hypixel.hytale.math.vector.Vector3f(0f,0f,0f));
        RealFacialExpressionsPlugin.LOGGER.atInfo().log(playerRef.getHeadRotation().toString());
        playerRef.getPacketHandler().writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, createServerCameraSettings()));


        var page = new FaceSettingsUI(playerRef, playerFaceData);

        player.getPageManager().openCustomPage(ref, store, page);
    }

    public static ServerCameraSettings createServerCameraSettings() {
        ServerCameraSettings cameraSettings = new ServerCameraSettings();
        cameraSettings.isFirstPerson = false;
        cameraSettings.displayCursor = false;
        cameraSettings.sendMouseMotion = false;

        cameraSettings.positionLerpSpeed = 0.2f;
        cameraSettings.rotationLerpSpeed = 0.2f;
        cameraSettings.distance = 1.0f;

        cameraSettings.rotationType = RotationType.AttachedToPlusOffset;
        cameraSettings.movementForceRotationType = MovementForceRotationType.AttachedToHead;
        cameraSettings.eyeOffset = true;
        cameraSettings.positionDistanceOffsetType = PositionDistanceOffsetType.DistanceOffset;
        cameraSettings.positionOffset = new Position(0f, 0f, -0.25f);
        cameraSettings.rotationOffset = new Direction((float) Math.PI, 0, 0);
        cameraSettings.planeNormal = new Vector3f(0.0f, 1.0f, 0.0f);
        return cameraSettings;
    }
}
