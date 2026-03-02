package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
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

        var page = new FaceSettingsUI(playerRef, playerFaceData);

        player.getPageManager().openCustomPage(ref, store, page);
    }
}
