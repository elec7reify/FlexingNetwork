package com.flexingstudios.flexingnetwork.api.util;

public class MathUtil {
    public static float pitchNormalizer(float pitch) {
        pitch %= 360.0F;
        if (pitch >= 180.0F) {
            pitch -= 360.0F;
        }
        if (pitch < -180.0F) {
            pitch += 360.0F;
        }
        return pitch;
    }
}
