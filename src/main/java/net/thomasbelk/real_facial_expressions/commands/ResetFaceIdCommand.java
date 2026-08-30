package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.ui.FaceIdUI;
import net.thomasbelk.real_facial_expressions.FacePacketStore;
import net.thomasbelk.real_facial_expressions.components.PlayerFaceAnimationComponent;
import javax.annotation.Nonnull;

public class ResetFaceIdCommand extends AbstractPlayerCommand {
    public ResetFaceIdCommand() {
        super("resetFaceId", "Resets the player's faceId");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        resetFaceIdForPlayer(store, ref, playerRef);
    }

    static void resetFaceIdForPlayer(@Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        FacePacketStore.INSTANCE.removePlayer(playerRef.getUuid());
        var id = FacePacketStore.INSTANCE.getNewFaceId();
        FacePacketStore.INSTANCE.addOrUpdatePlayerFaceId(playerRef.getUuid(), id);
        var animComponent = store.getComponent(ref, PlayerFaceAnimationComponent.getComponentType());
        if (animComponent != null) {
            animComponent.setUniqueId(id);
        }

        FaceIdUI page = new FaceIdUI(playerRef, id);

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
