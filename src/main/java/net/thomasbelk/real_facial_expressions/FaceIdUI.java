package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class FaceIdUI extends InteractiveCustomUIPage<FaceIdUI.CloseEventData> {
    private UUID faceId;
    public FaceIdUI(PlayerRef playerRef, UUID faceId) {
        super(playerRef, CustomPageLifetime.CanDismiss, CloseEventData.CODEC);
        this.faceId = faceId;
    }

    @Override
    public void build(@NonNull Ref<EntityStore> ref, @NonNull UICommandBuilder cmd, @NonNull UIEventBuilder uiEventBuilder, @NonNull Store<EntityStore> store) {
        cmd.append("Pages/FaceIdPage.ui");

        cmd.set("#FaceId.Value", faceId.toString());
    }

    public static class CloseEventData {
        public static final BuilderCodec<CloseEventData> CODEC =
                BuilderCodec.builder(CloseEventData.class, CloseEventData::new).build();
    }
}
