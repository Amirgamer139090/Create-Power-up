package com.powerup;

import com.mojang.logging.LogUtils;
import com.powerup.core.PowerUpPermissions;
import com.powerup.registry.DeviceRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * Mod entry point for Create: Power up!
 * <p>
 * This class is loaded on both logical sides and must stay free of client-only code; client setup belongs
 * in a separate {@code @Mod(dist = Dist.CLIENT)} class.
 * <p>
 * The value passed to {@link Mod} must match {@code modId} in {@code META-INF/neoforge.mods.toml}.
 */
@Mod(PowerUp.MOD_ID)
public class PowerUp {
    public static final String MOD_ID = "create_powerup";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PowerUp(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        DeviceRegistry.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(PowerUpPermissions::registerNodes);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Create: Power up! common setup");
    }

    /** Creates a {@link ResourceLocation} in this mod's namespace. */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
