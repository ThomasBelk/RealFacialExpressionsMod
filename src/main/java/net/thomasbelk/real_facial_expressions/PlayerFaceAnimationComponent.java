package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

// do I even need a component at this point lol? I guess to store the time, but UUID is also store in the packet store...
public class PlayerFaceAnimationComponent implements Component<EntityStore> {
    private UUID uniqueId;
    private int ticksSinceLastPacket = 0;


    public PlayerFaceAnimationComponent() {
        uniqueId = UUID.randomUUID();
    }

    public PlayerFaceAnimationComponent(UUID id, int ticks) {
        uniqueId = id;
        ticksSinceLastPacket = ticks;
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

    public UUID resetUniqueId() {
        uniqueId = UUID.randomUUID();
        return uniqueId;
    }

    public static final BuilderCodec<PlayerFaceAnimationComponent> CODEC = BuilderCodec
            .builder(PlayerFaceAnimationComponent.class, PlayerFaceAnimationComponent::new)
            .append(
                    new KeyedCodec<>("UniqueId", Codec.UUID_STRING),
                    (data, value) -> data.uniqueId = value,
                    (data) -> data.uniqueId
            ).add().build();


    private static ComponentType<EntityStore, PlayerFaceAnimationComponent> TYPE;

    public static void setComponentType(ComponentType<EntityStore, PlayerFaceAnimationComponent> type) {
        TYPE = type;
    }

    public static ComponentType<EntityStore, PlayerFaceAnimationComponent> getComponentType() {
        return TYPE;
    }

    @Override
    public @Nullable Component<EntityStore> clone() {
        return new PlayerFaceAnimationComponent(this.uniqueId, this.ticksSinceLastPacket);
    }

    @Override
    public String toString() {
        return "UUID: " + this.uniqueId.toString() + " | Ticks since last played: " + ticksSinceLastPacket;
    }
}
