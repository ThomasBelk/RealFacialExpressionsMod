package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.FaceIdUI;
import net.thomasbelk.real_facial_expressions.FacePacketStore;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class DisplayFaceIdCommand extends AbstractPlayerCommand {
    public DisplayFaceIdCommand() {
        super("faceId", "Displays faceId to player.", false);
    }

    @Override
    protected void execute(
            @NonNull CommandContext commandContext,
            @NonNull Store<EntityStore> store,
            @NonNull Ref<EntityStore> ref,
            @NonNull PlayerRef playerRef,
            @NonNull World world
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;
        UUID faceId = FacePacketStore.INSTANCE.getFaceId(playerRef.getUuid());
        if (faceId == null) {
            ResetFaceIdCommand.resetFaceIdForPlayer(store, ref, playerRef);
            return;
        }
        FaceIdUI page = new FaceIdUI(playerRef, faceId);

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
