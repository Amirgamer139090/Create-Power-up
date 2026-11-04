package com.powerup.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeviceTypeTest {

    @Test
    void keepsTheTranslationKey() {
        assertEquals("device_type.create_powerup.example", new DeviceType("device_type.create_powerup.example").translationKey());
    }

    @Test
    void blankKeyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new DeviceType(""));
        assertThrows(IllegalArgumentException.class, () -> new DeviceType("   "));
    }

    @Test
    void nullKeyIsRejected() {
        assertThrows(NullPointerException.class, () -> new DeviceType(null));
    }

    @Test
    void equalityFollowsTheKey() {
        assertEquals(new DeviceType("a"), new DeviceType("a"));
        assertNotEquals(new DeviceType("a"), new DeviceType("b"));
    }
}
