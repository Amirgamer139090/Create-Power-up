package com.powerup.registry;

import com.powerup.PowerUp;
import com.powerup.core.DeviceType;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The {@code create_powerup:device_type} registry, a NeoForge custom registry of {@link DeviceType}s.
 * <p>
 * Being a real registry, other mods can add device types through a {@link DeferredRegister} for
 * {@link #DEVICE_TYPE_KEY}, and the types are looked up by {@code ResourceLocation} like any other
 * registry content. The registry is not synchronized to clients (the default), because device types are
 * resolved by identifier on whichever side needs them; revisit this if a type ever needs to be sent
 * over the network by registry id.
 * <p>
 * The static members are final handles to the registry framework. This class holds no state of its own;
 * types are added with {@link #registerType(String, Supplier)} during mod construction, before the
 * registry event fires.
 */
public final class DeviceRegistry {

    /** The registry key, for use with a {@link DeferredRegister} or a registry lookup. */
    public static final ResourceKey<Registry<DeviceType>> DEVICE_TYPE_KEY =
            ResourceKey.createRegistryKey(PowerUp.id("device_type"));

    private static final DeferredRegister<DeviceType> DEVICE_TYPES =
            DeferredRegister.create(DEVICE_TYPE_KEY, PowerUp.MOD_ID);

    /** The registry itself. */
    public static final Registry<DeviceType> DEVICE_TYPE = DEVICE_TYPES.makeRegistry(builder -> { });

    private DeviceRegistry() {
    }

    /** Hooks this registry up to the mod event bus. Call exactly once from the mod constructor. */
    public static void register(IEventBus modEventBus) {
        DEVICE_TYPES.register(modEventBus);
    }

    /**
     * Adds a device type under {@code create_powerup:<name>}. Must be called during mod construction.
     *
     * @param name    the path of the type's identifier
     * @param factory creates the type when registry content is registered
     */
    public static DeferredHolder<DeviceType, DeviceType> registerType(String name, Supplier<DeviceType> factory) {
        return DEVICE_TYPES.register(name, factory);
    }
}
