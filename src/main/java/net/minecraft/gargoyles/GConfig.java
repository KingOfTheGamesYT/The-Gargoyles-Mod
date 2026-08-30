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
    public static int evilGargoyleAttackCooldown;
    public static int evilGargoyleTargetSearchChance;
    public static int evilGargoyleExperience;

    public static float gargoyleFavoritePerchHeal;
    public static float gargoyleNormalPerchHeal;
    public static int gargoyleAttackCooldown;
    public static int gargoyleTargetSearchChance;
    public static int gargoyleExperience;

    public static float stoneGargoyleHealth;
    public static float stoneGargoyleDamage;
    public static float stoneGargoyleKnockUp;
    public static float stoneGargoyleMovementSpeed;
    public static float stoneGargoyleFollowRange;
    public static float stoneGargoyleKnockbackResistance;

    public static float sandstoneGargoyleHealth;
    public static float sandstoneGargoyleDamage;
    public static float sandstoneGargoyleDesertDamage;
    public static float sandstoneGargoyleKnockUp;
    public static float sandstoneGargoyleMovementSpeed;
    public static float sandstoneGargoyleFollowRange;
    public static float sandstoneGargoyleKnockbackResistance;

    public static float obsidianGargoyleHealth;
    public static float obsidianGargoyleDamage;
    public static float obsidianGargoyleKnockUp;
    public static float obsidianGargoyleMovementSpeed;
    public static float obsidianGargoyleFollowRange;
    public static float obsidianGargoyleKnockbackResistance;

    public static float goldenGargoyleHealth;
    public static float goldenGargoyleDamage;
    public static float goldenGargoyleKnockUp;
    public static float goldenGargoyleMovementSpeed;
    public static float goldenGargoyleFollowRange;
    public static float goldenGargoyleKnockbackResistance;

    public static float ironGargoyleHealth;
    public static float ironGargoyleDamage;
    public static float ironGargoyleKnockUp;
    public static float ironGargoyleMovementSpeed;
    public static float ironGargoyleFollowRange;
    public static float ironGargoyleKnockbackResistance;

    public static float endStoneGargoyleHealth;
    public static float endStoneGargoyleDamage;
    public static float endStoneGargoyleKnockUp;
    public static float endStoneGargoyleMovementSpeed;
    public static float endStoneGargoyleFollowRange;
    public static float endStoneGargoyleKnockbackResistance;

    public static float netheraticGargoyleHealth;
    public static float netheraticGargoyleDamage;
    public static int netheraticGargoyleFireTime;
    public static float netheraticGargoyleKnockUp;
    public static float netheraticGargoyleMovementSpeed;
    public static float netheraticGargoyleFollowRange;
    public static float netheraticGargoyleKnockbackResistance;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        syncConfig();
    }

    public static void syncConfig() {

        cathedralGenerationChance = config.getInt(
                "CathedralGenerationChance",
                "MISC",
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
                "AddVanillaSpawnEggs",
                "MISC",
                true,
                "Adds spawn eggs for vanilla mobs that normally do not have them (Giant, Illusioner, Iron Golem, Snow Golem)."
        );

        evilGargoyleHealth = config.getFloat(
                "EvilGargoyleHealth",
                "Evil Gargoyle",
                50.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Evil Gargoyle."
        );

        evilGargoyleFollowRange = config.getFloat(
                "EvilGargoyleFollowRange",
                "Evil Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Evil Gargoyle."
        );

        evilGargoyleMovementSpeed = config.getFloat(
                "EvilGargoyleMovementSpeed",
                "Evil Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Evil Gargoyle."
        );

        evilGargoyleKnockbackResistance = config.getFloat(
                "EvilGargoyleKnockbackResistance",
                "Evil Gargoyle",
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Evil Gargoyle."
        );

        evilGargoyleMinDamage = config.getFloat(
                "MinDamage",
                "Evil Gargoyle",
                6.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Minimum damage dealt by the Evil Gargoyle."
        );

        evilGargoyleMaxDamage = config.getFloat(
                "MaxDamage",
                "Evil Gargoyle",
                12.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Maximum damage dealt by the Evil Gargoyle."
        );

        evilGargoyleFavoritePerchHeal = config.getFloat(
                "FavoritePerchHeal",
                "Evil Gargoyle",
                2.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on its favorite perch."
        );

        evilGargoyleNormalPerchHeal = config.getFloat(
                "NormalPerchHeal",
                "Evil Gargoyle",
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on any perch."
        );

        evilGargoyleAttackCooldown = config.getInt(
                "AttackCooldown",
                "Evil Gargoyle",
                20,
                1,
                Integer.MAX_VALUE,
                "Ticks between melee attacks. 20 = 1 second."
        );

        evilGargoyleTargetSearchChance = config.getInt(
                "TargetSearchChance",
                "Evil Gargoyle",
                10,
                1,
                Integer.MAX_VALUE,
                "1 in X chance each tick to search for a new target. Lower values make the gargoyle react faster."
        );

        evilGargoyleExperience = config.getInt(
                "EvilGargoyleExperience",
                "Evil Gargoyle",
                10,
                0,
                Integer.MAX_VALUE,
                "Experience dropped by the Evil Gargoyle."
        );

        gargoyleExperience = config.getInt(
                "GGargoyleExperience",
                "Friendly Gargoyle",
                0,
                0,
                Integer.MAX_VALUE,
                "Experience dropped by the Friendly Gargoyle."
        );

        gargoyleAttackCooldown = config.getInt(
                "GAttackCooldown",
                "Friendly Gargoyle",
                20,
                1,
                Integer.MAX_VALUE,
                "Ticks between melee attacks. 20 = 1 second."
        );

        gargoyleTargetSearchChance = config.getInt(
                "GTargetSearchChance",
                "Friendly Gargoyle",
                10,
                1,
                Integer.MAX_VALUE,
                "1 in X chance each tick to search for a new target. Lower values make the gargoyle react faster."
        );

        gargoyleFavoritePerchHeal = config.getFloat(
                "GFavoritePerchHeal",
                "Friendly Gargoyle",
                2.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on its favorite perch."
        );

        gargoyleNormalPerchHeal = config.getFloat(
                "GNormalPerchHeal",
                "Friendly Gargoyle",
                1.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Health restored each heal tick while on any perch."
        );

        stoneGargoyleHealth = config.getFloat(
                "StoneGargoyleHealth",
                "Stone Gargoyle",
                50.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Stone Gargoyle."
        );

        stoneGargoyleDamage = config.getFloat(
                "StoneGargoyleDamage",
                "Stone Gargoyle",
                10.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Stone Gargoyle."
        );

        stoneGargoyleKnockbackResistance = config.getFloat(
                "StoneGargoyleKnockbackResistance",
                "Stone Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Stone Gargoyle."
        );

        stoneGargoyleKnockUp= config.getFloat(
                "StoneGargoyleKnockUp",
                "Stone Gargoyle",
                0.3F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Stone Gargoyle knocks its target upward per hit."
        );

        stoneGargoyleMovementSpeed = config.getFloat(
                "StoneGargoyleMovementSpeed",
                "Stone Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Stone Gargoyle."
        );

        stoneGargoyleFollowRange = config.getFloat(
                "StoneGargoyleFollowRange",
                "Stone Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Stone Gargoyle."
        );

        sandstoneGargoyleHealth = config.getFloat(
                "SandstoneGargoyleHealth",
                "Sandstone Gargoyle",
                30.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Sandstone Gargoyle."
        );

        sandstoneGargoyleDamage = config.getFloat(
                "SandstoneGargoyleDamage",
                "Sandstone Gargoyle",
                6.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Sandstone Gargoyle."
        );

        sandstoneGargoyleDesertDamage = config.getFloat(
                "SandstoneGargoyleDesertDamage",
                "Sandstone Gargoyle",
                12.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Sandstone Gargoyle when in the desert."
        );

        sandstoneGargoyleKnockbackResistance = config.getFloat(
                "SandstoneGargoyleKnockbackResistance",
                "Sandstone Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Sandstone Gargoyle."
        );

        sandstoneGargoyleKnockUp = config.getFloat(
                "SandstoneGargoyleKnockUp",
                "Sandstone Gargoyle",
                0.15F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Sandstone Gargoyle knocks its target upward per hit."
        );

        sandstoneGargoyleMovementSpeed = config.getFloat(
                "SandstoneGargoyleMovementSpeed",
                "Sandstone Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Sandstone Gargoyle."
        );

        sandstoneGargoyleFollowRange = config.getFloat(
                "SandstoneGargoyleFollowRange",
                "Sandstone Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Sandstone Gargoyle."
        );

        obsidianGargoyleHealth = config.getFloat(
                "ObsidianGargoyleHealth",
                "Obsidian Gargoyle",
                200.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Obsidian Gargoyle."
        );

        obsidianGargoyleDamage = config.getFloat(
                "ObsidianGargoyleDamage",
                "Obsidian Gargoyle",
                18.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Obsidian Gargoyle."
        );

        obsidianGargoyleKnockbackResistance = config.getFloat(
                "ObsidianGargoyleKnockbackResistance",
                "Obsidian Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Obsidian Gargoyle."
        );

        obsidianGargoyleKnockUp = config.getFloat(
                "ObsidianGargoyleKnockUp",
                "Obsidian Gargoyle",
                0.5F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Obsidian Gargoyle knocks its target upward per hit."
        );

        obsidianGargoyleMovementSpeed = config.getFloat(
                "ObsidianGargoyleMovementSpeed",
                "Obsidian Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Obsidian Gargoyle."
        );

        obsidianGargoyleFollowRange = config.getFloat(
                "ObsidianGargoyleFollowRange",
                "Obsidian Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Obsidian Gargoyle."
        );

        goldenGargoyleHealth = config.getFloat(
                "GoldenGargoyleHealth",
                "Golden Gargoyle",
                80.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Golden Gargoyle."
        );

        goldenGargoyleDamage = config.getFloat(
                "GoldenGargoyleDamage",
                "Golden Gargoyle",
                20.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Golden Gargoyle."
        );

        goldenGargoyleKnockbackResistance = config.getFloat(
                "GoldenGargoyleKnockbackResistance",
                "Golden Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Golden Gargoyle."
        );

        goldenGargoyleKnockUp = config.getFloat(
                "GoldenGargoyleKnockUp",
                "Golden Gargoyle",
                0.6F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Golden Gargoyle knocks its target upward per hit."
        );

        goldenGargoyleMovementSpeed = config.getFloat(
                "GoldenGargoyleMovementSpeed",
                "Golden Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Golden Gargoyle."
        );

        goldenGargoyleFollowRange = config.getFloat(
                "GoldenGargoyleFollowRange",
                "Golden Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Golden Gargoyle."
        );

        ironGargoyleHealth = config.getFloat(
                "IronGargoyleHealth",
                "Iron Gargoyle",
                100.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Iron Gargoyle."
        );

        ironGargoyleDamage = config.getFloat(
                "IronGargoyleDamage",
                "Iron Gargoyle",
                14.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Iron Gargoyle."
        );

        ironGargoyleKnockbackResistance = config.getFloat(
                "IronGargoyleKnockbackResistance",
                "Iron Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Iron Gargoyle."
        );

        ironGargoyleKnockUp = config.getFloat(
                "IronGargoyleKnockUp",
                "Iron Gargoyle",
                0.4F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Iron Gargoyle knocks its target upward per hit."
        );

        ironGargoyleMovementSpeed = config.getFloat(
                "IronGargoyleMovementSpeed",
                "Iron Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Iron Gargoyle."
        );

        ironGargoyleFollowRange = config.getFloat(
                "IronGargoyleFollowRange",
                "Iron Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Iron Gargoyle."
        );

        endStoneGargoyleHealth = config.getFloat(
                "EndStoneGargoyleHealth",
                "End Stone Gargoyle",
                120.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the End Stone Gargoyle."
        );

        endStoneGargoyleDamage = config.getFloat(
                "EndStoneGargoyleDamage",
                "End Stone Gargoyle",
                26.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the End Stone Gargoyle."
        );

        endStoneGargoyleKnockbackResistance = config.getFloat(
                "EndStoneGargoyleKnockbackResistance",
                "End Stone Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the End Stone Gargoyle."
        );

        endStoneGargoyleKnockUp = config.getFloat(
                "EndStoneGargoyleKnockUp",
                "End Stone Gargoyle",
                0.5F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the End Stone Gargoyle knocks its target upward per hit."
        );

        endStoneGargoyleMovementSpeed = config.getFloat(
                "EndStoneGargoyleMovementSpeed",
                "End Stone Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the End Stone Gargoyle."
        );

        endStoneGargoyleFollowRange = config.getFloat(
                "EndStoneGargoyleFollowRange",
                "End Stone Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the End Stone Gargoyle."
        );

        netheraticGargoyleHealth = config.getFloat(
                "NetheraticGargoyleHealth",
                "Netheratic Gargoyle",
                60.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Maximum health of the Netheratic Gargoyle."
        );

        netheraticGargoyleDamage = config.getFloat(
                "NetheraticGargoyleDamage",
                "Netheratic Gargoyle",
                14.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Damage of the Netheratic Gargoyle."
        );

        netheraticGargoyleKnockbackResistance = config.getFloat(
                "NetheraticGargoyleKnockbackResistance",
                "Netheratic Gargoyle",
                0.0F,
                0.0F,
                Integer.MAX_VALUE,
                "Knockback resistance of the Netheratic Gargoyle."
        );

        netheraticGargoyleFireTime = config.getInt(
                "NetheraticGargoyleFireTime",
                "Netheratic Gargoyle",
                10,
                0,
                Integer.MAX_VALUE,
                "How long the Netheratic Gargoyle sets ppl on fire."
        );

        netheraticGargoyleKnockUp = config.getFloat(
                "NetheraticGargoyleKnockUp",
                "Netheratic Gargoyle",
                0.3F,
                0.0F,
                Integer.MAX_VALUE,
                "How far the Netheratic Gargoyle knocks its target upward per hit."
        );

        netheraticGargoyleMovementSpeed = config.getFloat(
                "NetheraticGargoyleMovementSpeed",
                "Netheratic Gargoyle",
                0.25F,
                0.0F,
                Integer.MAX_VALUE,
                "Movement speed of the Netheratic Gargoyle."
        );

        netheraticGargoyleFollowRange = config.getFloat(
                "NetheraticGargoyleFollowRange",
                "Netheratic Gargoyle",
                24.0F,
                1.0F,
                Integer.MAX_VALUE,
                "Follow range of the Netheratic Gargoyle."
        );

        if (config.hasChanged()) {
            config.save();
        }
    }
}