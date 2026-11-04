package com.powerup.materials;

import java.util.Locale;

/**
 * The simple material items of PowerMaterials (see {@code POWER_MATERIALS.md}).
 * <p>
 * This enum is the single source of truth for these items: {@code PowerUpItems} registers one item per
 * constant, the creative tab lists them, and the unit tests and the placeholder texture script derive their
 * expectations from it. It has no Minecraft dependency, so it can be unit tested without the game.
 * <p>
 * The {@link #id() id} is the registry path of the item and part of the save format; do not rename a
 * constant once released.
 * <p>
 * Constants have the form {@code NAME(Category.X)} separated by commas and ended by a semicolon, with
 * only {@code //} comments between them. {@code tools/generate_placeholder_textures.py} parses this
 * block strictly and fails loudly on anything else.
 */
public enum PowerMaterial {
    // Metals and conductors
    COPPER_WIRE(Category.METAL),
    INSULATED_WIRE(Category.METAL),
    ALUMINIUM_SHEET(Category.METAL),

    // Semiconductor chain
    SILICON_BEARING_POWDER(Category.SEMICONDUCTOR),
    METALLURGICAL_SILICON(Category.SEMICONDUCTOR),
    HIGH_PURITY_SILICON(Category.SEMICONDUCTOR),
    SILICON_INGOT(Category.SEMICONDUCTOR),
    RAW_SILICON_WAFER(Category.SEMICONDUCTOR),
    POLISHED_SILICON_WAFER(Category.SEMICONDUCTOR),
    CLEAN_SILICON_WAFER(Category.SEMICONDUCTOR),
    SILICON_DIE(Category.SEMICONDUCTOR),
    SEED_CRYSTAL(Category.SEMICONDUCTOR),

    // Optical
    OPTICAL_GLASS(Category.OPTICAL),
    OPTICAL_FIBER_STRAND(Category.OPTICAL),

    // Polymers
    PLASTIC_SHEET(Category.POLYMER),
    INSULATION_MATERIAL(Category.POLYMER),

    // Electronic materials
    PCB_SUBSTRATE(Category.ELECTRONIC),
    COPPER_CLAD_BOARD(Category.ELECTRONIC),
    CIRCUIT_BOARD(Category.ELECTRONIC),
    PHOTORESIST(Category.ELECTRONIC),
    ETCHANT(Category.ELECTRONIC),

    // Chemicals
    CARBON_POWDER(Category.CHEMICAL),
    CLEANING_FLUID(Category.CHEMICAL),

    // Thermal
    CERAMIC_INSULATOR(Category.THERMAL);

    /** Material groups, following section 2 of {@code POWER_MATERIALS.md}. */
    public enum Category {
        METAL,
        SEMICONDUCTOR,
        OPTICAL,
        POLYMER,
        ELECTRONIC,
        CHEMICAL,
        THERMAL
    }

    private final Category category;
    private final String id;

    PowerMaterial(Category category) {
        this.category = category;
        this.id = name().toLowerCase(Locale.ROOT);
    }

    /** The registry path of this material's item, for example {@code copper_wire}. */
    public String id() {
        return id;
    }

    /** The group this material belongs to. */
    public Category category() {
        return category;
    }
}
