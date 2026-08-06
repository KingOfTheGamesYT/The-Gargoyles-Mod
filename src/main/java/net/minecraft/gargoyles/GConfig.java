package net.minecraft.gargoyles;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class GConfig {

    private static Configuration config;

    public static int cathedralGenerationChance;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        syncConfig();
    }

    public static void syncConfig() {

        cathedralGenerationChance = config.getInt(
                "CathedralGenerationChance",
                Configuration.CATEGORY_GENERAL,
                10,
                0,
                Integer.MAX_VALUE,
                "Examples.\n" +
                        "0 = never generate.\n" +
                        "1 = every eligible chunk.\n" +
                        "2 = about half of eligible chunks.\n" +
                        "10 = about 1 in 10 chunks.\n" +
                        "100 = about 1 in 100 chunks."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}