package com.powerup.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DeviceStateTest {

    @Test
    void onlyOnlineIsOperational() {
        assertTrue(DeviceState.ONLINE.isOperational());
        assertFalse(DeviceState.OFFLINE.isOperational());
        assertFalse(DeviceState.FAULT.isOperational());
    }

    @Test
    void serializedNamesAreUniqueAndRoundTrip() {
        Set<String> names = Arrays.stream(DeviceState.values())
                .map(DeviceState::serializedName)
                .collect(Collectors.toSet());
        assertEquals(DeviceState.values().length, names.size());
        for (DeviceState state : DeviceState.values()) {
            assertEquals(Optional.of(state), DeviceState.bySerializedName(state.serializedName()));
        }
    }

    @Test
    void serializedNamesAreStable() {
        // These names are part of the save format; changing them breaks existing worlds.
        assertEquals("offline", DeviceState.OFFLINE.serializedName());
        assertEquals("online", DeviceState.ONLINE.serializedName());
        assertEquals("fault", DeviceState.FAULT.serializedName());
    }

    @Test
    void unknownNameIsEmpty() {
        assertEquals(Optional.empty(), DeviceState.bySerializedName("exploded"));
        assertEquals(Optional.empty(), DeviceState.bySerializedName("ONLINE"));
    }

    @Test
    void nullNameIsRejected() {
        assertThrows(NullPointerException.class, () -> DeviceState.bySerializedName(null));
    }
}
