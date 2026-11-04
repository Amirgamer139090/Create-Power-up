package com.powerup.core;

import java.util.Objects;

/**
 * Describes a kind of {@link PowerUpDevice}.
 * <p>
 * Instances are stored in the {@code create_powerup:device_type} registry (see
 * {@code com.powerup.registry.DeviceRegistry}); the registry key is the type's identifier. This record
 * only carries data that does not depend on that identifier, so it stays free of Minecraft types.
 *
 * @param translationKey language key of the type's display name; by convention
 *                       {@code device_type.<namespace>.<path>}
 */
public record DeviceType(String translationKey) {

    public DeviceType {
        Objects.requireNonNull(translationKey, "translationKey");
        if (translationKey.isBlank()) {
            throw new IllegalArgumentException("translationKey must not be blank");
        }
    }
}
