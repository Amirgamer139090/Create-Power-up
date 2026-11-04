package com.powerup.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeviceIdentityTest {

    @Test
    void randomIdentitiesAreUnique() {
        Set<DeviceIdentity> seen = new HashSet<>();
        for (int i = 0; i < 1_000; i++) {
            seen.add(DeviceIdentity.random());
        }
        assertEquals(1_000, seen.size());
    }

    @Test
    void toStringIsCanonicalUuidAndRoundTrips() {
        DeviceIdentity identity = DeviceIdentity.random();
        assertEquals(identity.id().toString(), identity.toString());
        assertEquals(identity, DeviceIdentity.parse(identity.toString()));
    }

    @Test
    void parseIsCaseInsensitive() {
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        assertEquals(DeviceIdentity.of(uuid), DeviceIdentity.parse("123E4567-E89B-12D3-A456-426614174000"));
    }

    @Test
    void parseRejectsMalformedText() {
        assertThrows(IllegalArgumentException.class, () -> DeviceIdentity.parse(""));
        assertThrows(IllegalArgumentException.class, () -> DeviceIdentity.parse("not-a-uuid"));
    }

    @Test
    void parseRejectsNonCanonicalShortForm() {
        // UUID.fromString accepts this; DeviceIdentity.parse must not.
        assertThrows(IllegalArgumentException.class, () -> DeviceIdentity.parse("1-1-1-1-1"));
    }

    @Test
    void nullsAreRejected() {
        assertThrows(NullPointerException.class, () -> DeviceIdentity.of(null));
        assertThrows(NullPointerException.class, () -> DeviceIdentity.parse(null));
    }

    @Test
    void equalityFollowsTheUuid() {
        UUID uuid = UUID.randomUUID();
        assertEquals(DeviceIdentity.of(uuid), DeviceIdentity.of(uuid));
        assertEquals(DeviceIdentity.of(uuid).hashCode(), DeviceIdentity.of(uuid).hashCode());
        assertNotEquals(DeviceIdentity.of(uuid), DeviceIdentity.random());
    }
}
