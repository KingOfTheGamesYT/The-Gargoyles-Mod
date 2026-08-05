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
                "1 in X chance per chunk. 0 disables cathedral generation."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}