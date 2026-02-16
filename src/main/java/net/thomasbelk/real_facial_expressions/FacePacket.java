package net.thomasbelk.real_facial_expressions;

import java.util.Map;
import java.util.UUID;

public record FacePacket(
        UUID faceId,
        String lookDir,
        Map<String, Float> blendshapes
) {
}
