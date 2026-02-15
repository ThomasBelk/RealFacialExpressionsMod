package net.thomasbelk.real_facial_expressions;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FacePacketStore {
    /**
     * Map of faceId to FacePacket
     */
    private final Map<UUID, FacePacket> latestPackets = new ConcurrentHashMap<UUID, FacePacket>();
    /**
     * Map of playerId to faceId
     */
    private final Map<UUID, UUID> playerIdToFaceId = new ConcurrentHashMap<UUID, UUID>();
    /**
     * Set of current active faceIds. Is used to help validate packets.
     */
    private final Set<UUID> activeFaceIds = ConcurrentHashMap.newKeySet();

    /**
     * Puts a FacePacket into the store
     * @param faceId the id shared is shared with the companion app.
     * @param facePacket the packet
     */
    public void putPacket(UUID faceId, FacePacket facePacket) {
        latestPackets.put(faceId, facePacket);
    }

    public void addOrUpdatePlayerFaceId(UUID playerId, UUID faceId) {
        var currentFaceId = playerIdToFaceId.get(playerId);
        if (currentFaceId != null) {
            activeFaceIds.remove(currentFaceId);
            // handle case where the player is changing their faceId and a packet already exists under old faceId
            var latestPacketFromPlayer = latestPackets.get(currentFaceId);
            if (latestPacketFromPlayer != null && latestPackets.remove(currentFaceId, latestPacketFromPlayer)) {
                latestPackets.put(faceId, latestPacketFromPlayer);
            }
        }
        playerIdToFaceId.put(playerId, faceId);
        // while the add realistically is not necessary if we use the getNewFaceId(), it is probably safer just to leave it
        activeFaceIds.add(faceId);
    }

    public void removePlayer(UUID playerId) {
        var faceIdToRemove = playerIdToFaceId.get(playerId);
        if (faceIdToRemove != null) {
            latestPackets.remove(faceIdToRemove);
            activeFaceIds.remove(faceIdToRemove);
        }
    }

    public boolean hasValidFaceId(UUID faceId) {
        if (faceId == null) return false;
        return activeFaceIds.contains(faceId);
    }

    public FacePacket getFacePackForPlayer(UUID playerId) {
        var faceId = playerIdToFaceId.get(playerId);
        if (faceId == null) return null;
        return latestPackets.get(faceId);
    }

    @Nullable
    public UUID getFaceId(UUID playerId) {
        return playerIdToFaceId.get(playerId);
    }

    public UUID getNewFaceId() {
        UUID newId;
        do {
            newId = UUID.randomUUID();
        } while (!activeFaceIds.add(newId));
        return newId;
    }
}
