package com.powerup.materials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PowerMaterialTest {

    private static final Pattern VALID_ID = Pattern.compile("[a-z][a-z0-9_]*");

    @Test
    void idsAreLowercaseConstantNames() {
        for (PowerMaterial material : PowerMaterial.values()) {
            assertEquals(material.name().toLowerCase(Locale.ROOT), material.id());
        }
    }

    @Test
    void idsAreUniqueAndValidRegistryPaths() {
        Set<String> ids = Arrays.stream(PowerMaterial.values()).map(PowerMaterial::id).collect(Collectors.toSet());
        assertEquals(PowerMaterial.values().length, ids.size());
        for (String id : ids) {
            assertTrue(VALID_ID.matcher(id).matches(), "Invalid registry path: " + id);
        }
    }

    @Test
    void everyMaterialHasACategory() {
        for (PowerMaterial material : PowerMaterial.values()) {
            assertNotNull(material.category(), material.id());
        }
    }

    @Test
    void everyCategoryIsUsed() {
        Set<PowerMaterial.Category> used = EnumSet.noneOf(PowerMaterial.Category.class);
        for (PowerMaterial material : PowerMaterial.values()) {
            used.add(material.category());
        }
        assertEquals(EnumSet.allOf(PowerMaterial.Category.class), used);
    }

    @Test
    void initialMaterialSetIsExactlyAsSpecified() {
        // Guards against accidental removal or renaming; ids are part of the save format.
        List<String> expected = List.of(
                "copper_wire", "insulated_wire", "aluminium_sheet",
                "silicon_bearing_powder", "metallurgical_silicon", "high_purity_silicon", "silicon_ingot",
                "raw_silicon_wafer", "polished_silicon_wafer", "clean_silicon_wafer", "silicon_die", "seed_crystal",
                "optical_glass", "optical_fiber_strand",
                "plastic_sheet", "insulation_material",
                "pcb_substrate", "copper_clad_board", "circuit_board", "photoresist", "etchant",
                "carbon_powder", "cleaning_fluid",
                "ceramic_insulator");
        List<String> actual = Arrays.stream(PowerMaterial.values()).map(PowerMaterial::id).toList();
        assertEquals(expected, actual);
    }

    @Test
    void categoriesMatchTheSpecification() {
        assertEquals(PowerMaterial.Category.METAL, PowerMaterial.COPPER_WIRE.category());
        assertEquals(PowerMaterial.Category.SEMICONDUCTOR, PowerMaterial.SILICON_DIE.category());
        assertEquals(PowerMaterial.Category.OPTICAL, PowerMaterial.OPTICAL_GLASS.category());
        assertEquals(PowerMaterial.Category.POLYMER, PowerMaterial.PLASTIC_SHEET.category());
        assertEquals(PowerMaterial.Category.ELECTRONIC, PowerMaterial.CIRCUIT_BOARD.category());
        assertEquals(PowerMaterial.Category.CHEMICAL, PowerMaterial.CARBON_POWDER.category());
        assertEquals(PowerMaterial.Category.THERMAL, PowerMaterial.CERAMIC_INSULATOR.category());
    }
}
