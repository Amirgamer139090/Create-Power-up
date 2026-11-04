package com.powerup.materials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Checks that the hand-written resource files parse as JSON and agree with {@link PowerMaterial}. Gradle runs
 * tests with the project directory as working directory, so the sources are read from
 * {@code src/main/resources}. Gson is expected on the test classpath through the NeoForge dependency.
 */
class MaterialResourcesTest {

    private static final String NS = "create_powerup";
    private static final Path RESOURCES = Path.of("src/main/resources");
    private static final Path ASSETS = RESOURCES.resolve("assets/" + NS);
    private static final Path DATA = RESOURCES.resolve("data");

    private static JsonObject json(Path path) throws IOException {
        assertTrue(Files.isRegularFile(path), "Missing file: " + path);
        JsonElement element = JsonParser.parseString(Files.readString(path));
        assertTrue(element.isJsonObject(), "Not a JSON object: " + path);
        return element.getAsJsonObject();
    }

    @Test
    void everyJsonResourceIsValidJson() throws IOException {
        int count = 0;
        try (Stream<Path> files = Files.walk(RESOURCES)) {
            for (Path path : (Iterable<Path>) files.filter(p -> p.toString().endsWith(".json"))::iterator) {
                json(path);
                count++;
            }
        }
        assertTrue(count > PowerMaterial.values().length, "Suspiciously few JSON resources: " + count);
    }

    @Test
    void everyMaterialHasModelTextureAndTranslation() throws IOException {
        JsonObject lang = json(ASSETS.resolve("lang/en_us.json"));
        for (PowerMaterial material : PowerMaterial.values()) {
            String id = material.id();

            JsonObject model = json(ASSETS.resolve("models/item/" + id + ".json"));
            assertEquals("minecraft:item/generated", model.get("parent").getAsString(), id);
            String texture = model.getAsJsonObject("textures").get("layer0").getAsString();
            assertEquals(NS + ":item/" + id, texture);
            assertValidPng(ASSETS.resolve("textures/item/" + id + ".png"));

            String key = "item." + NS + "." + id;
            assertTrue(lang.has(key), "Missing translation: " + key);
            assertFalse(lang.get(key).getAsString().isBlank(), "Blank translation: " + key);
        }
    }

    @Test
    void noUnexpectedItemModelsOrTranslations() throws IOException {
        Set<String> expectedModels = new HashSet<>();
        Set<String> expectedKeys = new HashSet<>();
        for (PowerMaterial material : PowerMaterial.values()) {
            expectedModels.add(material.id() + ".json");
            expectedKeys.add("item." + NS + "." + material.id());
        }
        expectedModels.add("quartz_ore.json");
        expectedKeys.add("block." + NS + ".quartz_ore");
        expectedKeys.add("itemGroup." + NS);

        Set<String> models = new HashSet<>();
        try (Stream<Path> files = Files.list(ASSETS.resolve("models/item"))) {
            files.forEach(path -> models.add(path.getFileName().toString()));
        }
        assertEquals(expectedModels, models);
        assertEquals(expectedKeys, json(ASSETS.resolve("lang/en_us.json")).keySet());
    }

    @Test
    void quartzOreModelsAndBlockstateAreConsistent() throws IOException {
        String modelId = NS + ":block/quartz_ore";

        JsonObject blockstate = json(ASSETS.resolve("blockstates/quartz_ore.json"));
        assertEquals(modelId, blockstate.getAsJsonObject("variants").getAsJsonObject("").get("model").getAsString());

        JsonObject blockModel = json(ASSETS.resolve("models/block/quartz_ore.json"));
        assertEquals("minecraft:block/cube_all", blockModel.get("parent").getAsString());
        assertEquals(modelId, blockModel.getAsJsonObject("textures").get("all").getAsString());
        assertValidPng(ASSETS.resolve("textures/block/quartz_ore.png"));

        JsonObject itemModel = json(ASSETS.resolve("models/item/quartz_ore.json"));
        assertEquals(modelId, itemModel.get("parent").getAsString());
    }

    @Test
    void quartzOreLootTableHasSilkTouchAndFortuneBranches() throws IOException {
        JsonObject loot = json(DATA.resolve(NS + "/loot_table/blocks/quartz_ore.json"));
        assertEquals("minecraft:block", loot.get("type").getAsString());

        JsonObject entry = loot.getAsJsonArray("pools").get(0).getAsJsonObject()
                .getAsJsonArray("entries").get(0).getAsJsonObject();
        assertEquals("minecraft:alternatives", entry.get("type").getAsString());
        JsonArray children = entry.getAsJsonArray("children");
        assertEquals(2, children.size());

        JsonObject silk = children.get(0).getAsJsonObject();
        assertEquals(NS + ":quartz_ore", silk.get("name").getAsString());
        assertTrue(silk.toString().contains("minecraft:silk_touch"), "First branch must require Silk Touch");

        JsonObject drop = children.get(1).getAsJsonObject();
        assertEquals("minecraft:quartz", drop.get("name").getAsString());
        assertTrue(drop.toString().contains("minecraft:fortune"), "Second branch must apply Fortune");
        assertTrue(drop.toString().contains("minecraft:ore_drops"));
    }

    @Test
    void quartzOreIsMineableWithAStonePickaxe() throws IOException {
        for (String tag : new String[] {"mineable/pickaxe", "needs_stone_tool"}) {
            JsonObject json = json(DATA.resolve("minecraft/tags/block/" + tag + ".json"));
            assertFalse(json.get("replace").getAsBoolean(), "Tag must not replace vanilla entries: " + tag);
            assertTrue(json.getAsJsonArray("values").contains(JsonParser.parseString("\"" + NS + ":quartz_ore\"")),
                    "Tag " + tag + " must contain quartz_ore");
        }
    }

    private static void assertValidPng(Path path) throws IOException {
        assertTrue(Files.isRegularFile(path), "Missing texture: " + path + " (run tools/generate_placeholder_textures.py)");
        byte[] bytes = Files.readAllBytes(path);
        assertTrue(bytes.length > 33, "Texture too small to be a PNG: " + path);
        byte[] signature = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
        for (int i = 0; i < signature.length; i++) {
            assertEquals(signature[i], bytes[i], "Not a PNG file: " + path);
        }
        ByteBuffer header = ByteBuffer.wrap(bytes);
        int width = header.getInt(16);
        int height = header.getInt(20);
        assertTrue(width > 0 && width == height, "Texture must be square: " + path);
    }
}
