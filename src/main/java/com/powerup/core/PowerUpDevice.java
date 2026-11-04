package com.powerup.core;

/**
 * Contract shared by everything in Create: Power up! that is a "device".
 * <p>
 * The interface is deliberately small and read-only so that it can be implemented by a
 * {@code BlockEntity} (or, later, by an entity) without clashing with the members of those classes;
 * for that reason it uses {@code deviceType()} rather than {@code getType()}.
 *
 * <h2>Expectations for implementers</h2>
 * <ul>
 *   <li>{@link #identity()} is created once with {@link DeviceIdentity#random()}, persisted by the
 *       implementation, and returns the same value for the whole lifetime of the device.</li>
 *   <li>{@link #deviceType()} returns a type that is registered in
 *       {@code com.powerup.registry.DeviceRegistry}.</li>
 *   <li>{@link #state()} may be read on either logical side, but is only changed on the logical server;
 *       a client copy is updated from server data (for example a block entity update packet).</li>
 * </ul>
 */
public interface PowerUpDevice {

    /** The unique identity of this device instance. */
    DeviceIdentity identity();

    /** The kind of device this is. */
    DeviceType deviceType();

    /** The current operating state. */
    DeviceState state();
}
