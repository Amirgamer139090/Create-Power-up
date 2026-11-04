package com.powerup.core;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Coarse operating state of a {@link PowerUpDevice}.
 * <p>
 * The state is owned by the logical server. Clients may display it, but must only change it in response
 * to data received from the server. The {@link #serializedName() serialized names} are part of the save
 * format and must not be renamed once released.
 */
public enum DeviceState {
    /** The device is not running, for example because it has no power. */
    OFFLINE(false),
    /** The device is running normally. */
    ONLINE(true),
    /** The device stopped because of an error and needs attention. */
    FAULT(false);

    private final boolean operational;

    DeviceState(boolean operational) {
        this.operational = operational;
    }

    /** Returns {@code true} if a device in this state performs its function. */
    public boolean isOperational() {
        return operational;
    }

    /** Returns the stable name used when persisting or synchronizing this state. */
    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /** Looks up a state by its {@link #serializedName()}; empty if the name is unknown. */
    public static Optional<DeviceState> bySerializedName(String name) {
        Objects.requireNonNull(name, "name");
        for (DeviceState state : values()) {
            if (state.serializedName().equals(name)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
