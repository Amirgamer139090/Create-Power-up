package com.powerup.registry;

import com.powerup.PowerUp;
import com.powerup.materials.PowerMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative tab registrations of Create: Power up!.
 */
public final class PowerUpCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PowerUp.MOD_ID);

    /** The main tab. Its title key is {@code itemGroup.create_powerup}. */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + PowerUp.MOD_ID))
                    .icon(() -> new ItemStack(PowerUpItems.material(PowerMaterial.CIRCUIT_BOARD).get()))
                    .displayItems((parameters, output) -> PowerUpItems.acceptAll(output))
                    .build());

    private PowerUpCreativeTabs() {
    }

    /** Hooks the tab register up to the mod event bus. Call exactly once from the mod constructor. */
    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
