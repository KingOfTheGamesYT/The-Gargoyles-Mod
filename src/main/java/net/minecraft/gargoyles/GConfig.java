package net.minecraft.gargoyles;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class GConfig {

    private static Configuration config;

    public static int cathedralGenerationChance;
    public static boolean addVanillaSpawnEggs;

    public static float evilGargoyleHealth;
    public static float evilGargoyleFollowRange;
    public static float evilGargoyleMovementSpeed;
    public static float evilGargoyleKnockbackResistance;
    public static float evilGargoyleMinDamage;
    public static float evilGargoyleMaxDamage;
    public static float evilGargoyleFavoritePerchHeal;
    public static float evilGargoyleNormalPerchHeal;
    public static float evilGargoyleAttackCooldown;
    public static int evilGargoyleTargetSearchChance;
    public static int evilGargoyleExperience;

    public static float gargoyleFollowRange;
    public static float gargoyleMovementSpeed;
    public static float gargoyleFavoritePerchHeal;
    public static float gargoyleNormalPerchHeal;
    public static float gargoyleAttackCooldown;
    public static int gargoyleTargetSearchChance;

    public static float stoneGargoyleHealth;
    public static float sandstoneGargoyleHealth;
    public static float obsidianGargoyleHealth;
    public static float goldenGargoyleHealth;
    public static float ironGargoyleHealth;
    public static float endStoneGargoyleHealth;
    public static float netherBrickGargoyleHealth;

    public static float stoneGargoyleDamage;
    public static float sandstoneGargoyleDamage;
    public static float sandstoneGargoyleDesertDamage;
    public static float obsidianGargoyleDamage;
    public static float goldenGargoyleDamage;
    public static float ironGargoyleDamage;
    public static float endStoneGargoyleDamage;
    public static float netherBrickGargoyleDamage;
    public static int netherBrickGargoyleFireTime;

    public static float stoneGargoyleKnockUp;
    public static float sandstoneGargoyleKnockUp;
    public static float obsidianGargoyleKnockUp;
    public static float goldenGargoyleKnockUp;
    public static float ironGargoyleKnockUp;
    public static float endStoneGargoyleKnockUp;
    public static float netherBrickGargoyleKnockUp;

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

        addVanillaSpawnEggs = config.getBoolean(
                "addVanillaSpawnEggs",
                Configuration.CATEGORY_GENERAL,
                true,
                "Adds spawn eggs for vanilla mobs that normally do not have them (Giant, Illusioner, Iron Golem, Snow Golem)."
        );

        evilGargoyleHealth = config.getFloat(
                "EvilGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                50.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Evil Gargoyle."
        );

        evilGargoyleFollowRange = config.getFloat(
                "EvilGargoyleFollowRange",
                Configuration.CATEGORY_GENERAL,
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Evil Gargoyle."
        );

        evilGargoyleMovementSpeed = config.getFloat(
                "EvilGargoyleMovementSpeed",
                Configuration.CATEGORY_GENERAL,
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Evil Gargoyle."
        );

        evilGargoyleKnockbackResistance = config.getFloat(
                "EvilGargoyleKnockbackResistance",
                Configuration.CATEGORY_GENERAL,
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Evil Gargoyle."
        );

        evilGargoyleMinDamage = config.getFloat(
                "MinDamage",
                Configuration.CATEGORY_GENERAL,
                6.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Minimum damage dealt by the Evil Gargoyle."
        );

        evilGargoyleMaxDamage = config.getFloat(
                "MaxDamage",
                Configuration.CATEGORY_GENERAL,
                12.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Maximum damage dealt by the Evil Gargoyle."
        );

        evilGargoyleFavoritePerchHeal = config.getFloat(
                "FavoritePerchHeal",
                Configuration.CATEGORY_GENERAL,
                2.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on its favorite perch."
        );

        evilGargoyleNormalPerchHeal = config.getFloat(
                "NormalPerchHeal",
                Configuration.CATEGORY_GENERAL,
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on any perch."
        );

        evilGargoyleAttackCooldown = config.getInt(
                "AttackCooldown",
                Configuration.CATEGORY_GENERAL,
                20,
                1,
                Integer.MAX_VALUE,
                "Ticks between melee attacks. 20 = 1 second."
        );

        evilGargoyleTargetSearchChance = config.getInt(
                "TargetSearchChance",
                Configuration.CATEGORY_GENERAL,
                10,
                1,
                Integer.MAX_VALUE,
                "1 in X chance each tick to search for a new target. Lower values make the gargoyle react faster."
        );

        evilGargoyleExperience = config.getInt(
                "evilGargoyleExperience",
                Configuration.CATEGORY_GENERAL,
                10,
                0,
                Integer.MAX_VALUE,
                "Experience dropped by the Evil Gargoyle."
        );

        gargoyleAttackCooldown = config.getInt(
                "GAttackCooldown",
                Configuration.CATEGORY_GENERAL,
                20,
                1,
                Integer.MAX_VALUE,
                "Ticks between melee attacks. 20 = 1 second."
        );

        gargoyleFollowRange = config.getFloat(
                "GargoyleFollowRange",
                Configuration.CATEGORY_GENERAL,
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Normal Gargoyle."
        );

        gargoyleMovementSpeed = config.getFloat(
                "GargoyleMovementSpeed",
                Configuration.CATEGORY_GENERAL,
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Normal Gargoyle."
        );

        gargoyleTargetSearchChance = config.getInt(
                "GTargetSearchChance",
                Configuration.CATEGORY_GENERAL,
                10,
                1,
                Integer.MAX_VALUE,
                "1 in X chance each tick to search for a new target. Lower values make the gargoyle react faster."
        );

        gargoyleFavoritePerchHeal = config.getFloat(
                "GFavoritePerchHeal",
                Configuration.CATEGORY_GENERAL,
                2.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on its favorite perch."
        );

        gargoyleNormalPerchHeal = config.getFloat(
                "GNormalPerchHeal",
                Configuration.CATEGORY_GENERAL,
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on any perch."
        );

        stoneGargoyleHealth = config.getFloat(
                "StoneGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                50.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Stone Gargoyle."
        );

        sandstoneGargoyleHealth = config.getFloat(
                "SandstoneGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                30.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Sandstone Gargoyle."
        );

        obsidianGargoyleHealth = config.getFloat(
                "ObsidianGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                200.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Obsidian Gargoyle."
        );

        goldenGargoyleHealth = config.getFloat(
                "GoldGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                80.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Gold Gargoyle."
        );

        ironGargoyleHealth = config.getFloat(
                "IronGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                100.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Iron Gargoyle."
        );

        endStoneGargoyleHealth = config.getFloat(
                "EndStoneGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                120.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the End Stone Gargoyle."
        );

        netherBrickGargoyleHealth = config.getFloat(
                "NetherBrickGargoyleHealth",
                Configuration.CATEGORY_GENERAL,
                60.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Nether Brick Gargoyle."
        );

        stoneGargoyleDamage = config.getFloat(
                "StoneGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                10.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Stone Gargoyle."
        );

        sandstoneGargoyleDamage = config.getFloat(
                "SandstoneGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                6.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Sandstone Gargoyle."
        );

        sandstoneGargoyleDesertDamage = config.getFloat(
                "SandstoneGargoyleDesertDamage",
                Configuration.CATEGORY_GENERAL,
                12.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Sandstone Gargoyle when in the desert."
        );

        obsidianGargoyleDamage = config.getFloat(
                "ObsidianGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                18.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Obsidian Gargoyle."
        );

        goldenGargoyleDamage = config.getFloat(
                "GoldGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                20.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Gold Gargoyle."
        );

        ironGargoyleDamage = config.getFloat(
                "IronGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                14.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Iron Gargoyle."
        );

        endStoneGargoyleDamage = config.getFloat(
                "EndStoneGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                26.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the End Stone Gargoyle."
        );

        netherBrickGargoyleDamage = config.getFloat(
                "NetherBrickGargoyleDamage",
                Configuration.CATEGORY_GENERAL,
                14.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Nether Brick Gargoyle."
        );

        netherBrickGargoyleFireTime = config.getInt(
                "NetherBrickGargoyleFireTime",
                Configuration.CATEGORY_GENERAL,
                10,
                0,
                Integer.MAX_VALUE,
                "How long the Nether Brick Gargoyle sets ppl on fire."
        );

        stoneGargoyleKnockUp= config.getFloat(
                "StoneGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.3F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Stone Gargoyle knocks its target upward per hit."
        );

        sandstoneGargoyleKnockUp = config.getFloat(
                "SandstoneGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.15F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Sandstone Gargoyle knocks its target upward per hit."
        );

        obsidianGargoyleKnockUp = config.getFloat(
                "ObsidianGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.5F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Obsidian Gargoyle knocks its target upward per hit."
        );

        goldenGargoyleKnockUp = config.getFloat(
                "GoldGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.6F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Gold Gargoyle knocks its target upward per hit."
        );

        ironGargoyleKnockUp = config.getFloat(
                "IronGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.4F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Iron Gargoyle knocks its target upward per hit."
        );

        endStoneGargoyleKnockUp = config.getFloat(
                "EndStoneGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.5F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the End Stone Gargoyle knocks its target upward per hit."
        );

        netherBrickGargoyleKnockUp = config.getFloat(
                "NetherBrickGargoyleKnockUp",
                Configuration.CATEGORY_GENERAL,
                0.3F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Nether Brick Gargoyle knocks its target upward per hit."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}