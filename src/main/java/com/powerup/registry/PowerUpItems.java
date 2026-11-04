package com.powerup.registry;

import com.powerup.PowerUp;
import com.powerup.materials.PowerMaterial;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registrations of Create: Power up!: the block items and one plain item per {@link PowerMaterial}.
 */
public final class PowerUpItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PowerUp.MOD_ID);

    /** Block item of {@link PowerUpBlocks#QUARTZ_ORE}. */
    public static final DeferredItem<BlockItem> QUARTZ_ORE = ITEMS.register("quartz_ore",
            () -> new BlockItem(PowerUpBlocks.QUARTZ_ORE.get(), new Item.Properties()));

    private static final Map<PowerMaterial, DeferredItem<Item>> MATERIALS = registerMaterials();

    private PowerUpItems() {
    }

    private static Map<PowerMaterial, DeferredItem<Item>> registerMaterials() {
        Map<PowerMaterial, DeferredItem<Item>> items = new EnumMap<>(PowerMaterial.class);
        for (PowerMaterial material : PowerMaterial.values()) {
            items.put(material, ITEMS.register(material.id(), () -> new Item(new Item.Properties())));
        }
        return Collections.unmodifiableMap(items);
    }

    /** Returns the registered item of a material. */
    public static DeferredItem<Item> material(PowerMaterial material) {
        return MATERIALS.get(material);
    }

    /** Offers every item of this mod to a creative tab, in registration order. */
    static void acceptAll(CreativeModeTab.Output output) {
        ITEMS.getEntries().forEach(entry -> output.accept(entry.get()));
    }

    /** Hooks the item register up to the mod event bus. Call exactly once from the mod constructor. */
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
