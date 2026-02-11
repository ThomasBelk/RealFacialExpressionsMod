package net.thomasbelk.real_facial_expressions.look;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.AnimationSlot;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.AnimationUtils;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

public class LookCommand extends AbstractPlayerCommand {
    private static final String NAME = "look";
    private static final String DESC = "Lets you look in a specified direction. Directions include: TODO";
    private final DefaultArg<String> lookDir;


    public LookCommand() {
        super(NAME, DESC);
        this.lookDir = this.withDefaultArg("lookdir", "The direction the animation should look", ArgTypes.STRING, LookDir.Center.toString(), "Look to the center from the player character's perspective");
    }

    @Override
    protected void execute(@NonNull CommandContext commandContext, @NonNull Store<EntityStore> store, @NonNull Ref<EntityStore> ref, @NonNull PlayerRef playerRef, @NonNull World world) {
        try {
            AnimationUtils.playAnimation(ref, AnimationSlot.Face, null, LookDir.getAnimationId(this.lookDir.get(commandContext)), true, store);
        } catch (InvalidLookDirNameException e) {
            playerRef.sendMessage(Message.raw(e.getMessage()));
        }

    }
}
