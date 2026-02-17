package net.thomasbelk.real_facial_expressions.enums;

import java.util.Map;

public enum EyelidState {
    Open("Open", "LEyelidO", "REyelidO"),
    HalfOpen("Half Open", "LEyelidHO", "REyelidHO"),
    Closed("Closed", "LEyelidC", "REyelidC");

    private final String name;
    private final String animNameLeft;
    private final String animNameRight;
    private static final float CLOSED_THRESHOLD = 0.4f;
    private static final float HALF_OPEN_THRESHOLD = 0.25f;

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
                CLOSED_THRESHOLD,
                HALF_OPEN_THRESHOLD,
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
