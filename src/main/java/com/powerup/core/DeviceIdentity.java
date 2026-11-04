package com.powerup.core;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable, globally unique identity of a single {@link PowerUpDevice} instance.
 * <p>
 * An identity is assigned once, when a device is first created, and must then be persisted by the
 * device and never change. It deliberately does not encode a position or a dimension, so a device
 * keeps its identity when it is moved or when the world layout changes.
 * <p>
 * This type is side-neutral and has no Minecraft dependency; persist it through its {@link #id()}.
 *
 * @param id the underlying UUID, never {@code null}
 */
public record DeviceIdentity(UUID id) {

    public DeviceIdentity {
        Objects.requireNonNull(id, "id");
    }

    /** Creates a new, random identity. Call this once per device, when the device is first created. */
    public static DeviceIdentity random() {
        return new DeviceIdentity(UUID.randomUUID());
    }

    /** Wraps an existing UUID, for example one that was read back from persistent data. */
    public static DeviceIdentity of(UUID id) {
        return new DeviceIdentity(id);
    }

    /**
     * Parses the canonical text form produced by {@link #toString()} (case-insensitive).
     * <p>
     * This is stricter than {@link UUID#fromString(String)}, which also accepts shortened forms such as
     * {@code 1-1-1-1-1}.
     *
     * @throws IllegalArgumentException if {@code text} is not a canonical UUID string
     */
    public static DeviceIdentity parse(String text) {
        Objects.requireNonNull(text, "text");
        final UUID parsed;
        try {
            parsed = UUID.fromString(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not a valid device identity: '" + text + "'", e);
        }
        if (!parsed.toString().equalsIgnoreCase(text)) {
            throw new IllegalArgumentException("Not a canonical device identity: '" + text + "'");
        }
        return new DeviceIdentity(parsed);
    }

    /** Returns the canonical lowercase UUID text, suitable for logs and for {@link #parse(String)}. */
    @Override
    public String toString() {
        return id.toString();
    }
}
