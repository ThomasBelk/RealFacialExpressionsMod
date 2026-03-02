package net.thomasbelk.real_facial_expressions.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.thomasbelk.real_facial_expressions.FaceSettings;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class PlayerFaceAnimationComponent implements Component<EntityStore> {
    private UUID uniqueId;
    private int ticksSinceLastPacket = 0;
    private FaceSettings faceSettings;


    public PlayerFaceAnimationComponent() {
        uniqueId = UUID.randomUUID();
        faceSettings = new FaceSettings();
    }

    public PlayerFaceAnimationComponent(UUID id, int ticks, FaceSettings faceSet) {
        uniqueId = id;
        ticksSinceLastPacket = ticks;
        faceSettings = faceSet;
    }


    public int getTicksSinceLastPacket() {
        return ticksSinceLastPacket;
    }

    public int getAndIncrementTicksSinceLastPacket() {
        return ++ticksSinceLastPacket;
    }

    public void incrementTicksSinceLastPacket() {
        ticksSinceLastPacket++;
    }

    public void setTicksSinceLastPacket(int ticks) {
        ticksSinceLastPacket = ticks;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID id) { this.uniqueId = id; }

    public FaceSettings getFaceSettings() {
        return this.faceSettings;
    }

    public static final BuilderCodec<PlayerFaceAnimationComponent> CODEC = BuilderCodec
            .builder(PlayerFaceAnimationComponent.class, PlayerFaceAnimationComponent::new)
            .append(
                    new KeyedCodec<>("UniqueId", Codec.UUID_STRING),
                    (data, value) -> data.uniqueId = value,
                    (data) -> data.uniqueId
            ).add()
            .append(new KeyedCodec<>("FaceSettings", FaceSettings.CODEC),
                    (data, value) -> data.faceSettings = value,
                    (data) -> data.faceSettings
            ).add()
            .build();


    private static ComponentType<EntityStore, PlayerFaceAnimationComponent> TYPE;

    public static void setComponentType(ComponentType<EntityStore, PlayerFaceAnimationComponent> type) {
        TYPE = type;
    }

    public static ComponentType<EntityStore, PlayerFaceAnimationComponent> getComponentType() {
        return TYPE;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new PlayerFaceAnimationComponent(this.uniqueId, this.ticksSinceLastPacket, this.faceSettings);
    }

    @Override
    public String toString() {
        return "UUID: " + this.uniqueId.toString() + " | Ticks since last played: " + ticksSinceLastPacket;
    }
}
