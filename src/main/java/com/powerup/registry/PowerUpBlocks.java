package com.powerup.registry;

import com.powerup.PowerUp;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block registrations of Create: Power up!.
 * <p>
 * Machine blocks are added together with their block entities in later phases.
 */
public final class PowerUpBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PowerUp.MOD_ID);

    /**
     * Quartz ore, the start of the silicon chain. It drops vanilla quartz (Fortune and Silk Touch apply, see
     * the loot table). World generation is not part of this phase.
     */
    public static final DeferredBlock<DropExperienceBlock> QUARTZ_ORE = BLOCKS.register("quartz_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));

    private PowerUpBlocks() {
    }

    /** Hooks the block register up to the mod event bus. Call exactly once from the mod constructor. */
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
