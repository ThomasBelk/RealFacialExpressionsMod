package net.thomasbelk.real_facial_expressions.enums;

import net.thomasbelk.real_facial_expressions.FaceSettings;

import java.util.Map;

public enum EyelidState {
    Open("Open", "LEyelidO", "REyelidO"),
    HalfOpen("Half Open", "LEyelidHO", "REyelidHO"),
    Closed("Closed", "LEyelidC", "REyelidC");

    private final String name;
    private final String animNameLeft;
    private final String animNameRight;

    EyelidState(String name, String animNameLeft, String animNameRight) {
        this.name = name;
        this.animNameLeft = animNameLeft;
        this.animNameRight = animNameRight;
    }

    public static EyelidState getEyeLidStateFromBlendshapes(
            Map<String, Float> blendshapes,
            float closedThreshold,
            float halfOpenThreshold,
            boolean isLeftEye
    ) {
        if (blendshapes == null) {
            return EyelidState.Open;
        }
        float blink;
        if (isLeftEye) {
            blink = blendshapes.getOrDefault("eyeBlinkLeft", 0f);
        } else {
            blink = blendshapes.getOrDefault("eyeBlinkRight", 0f);
        }

        if (blink >= closedThreshold) {
            return EyelidState.Closed;
        } else if (blink >= halfOpenThreshold) {
            return EyelidState.HalfOpen;
        } else {
            return EyelidState.Open;
        }
    }

    public static EyelidState getEyeLidStateFromBlendshapes(
            Map<String, Float> blendshapes, boolean isLeftEye
    ) {
        return getEyeLidStateFromBlendshapes(
                blendshapes,
                FaceSettings.DEFAULT_EYE_LID_CLOSED_THRESHOLD,
                FaceSettings.DEFAULT_EYE_LID_HALF_OPEN_THRESHOLD,
                isLeftEye
        );
    }

    public static EyelidState getEyeLidStateFromBlendshapes(
            Map<String, Float> blendshapes, boolean isLeftEye, FaceSettings faceSettings
    ) {
        return getEyeLidStateFromBlendshapes(
                blendshapes,
                faceSettings.getEyeLidClosedThreshold(),
                faceSettings.getEyeLidHalfOpenThreshold(),
                isLeftEye
        );
    }

    public String getLeftAnimId() {
        return this.animNameLeft;
    }

    public String getRightAnimId() {
        return this.animNameRight;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
