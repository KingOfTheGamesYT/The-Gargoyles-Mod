package net.minecraft.gargoyles;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.gargoyles.blocks.BlockMagicPumpkin;
import net.minecraft.gargoyles.blocks.BlockPerch;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

public class RegistryHandler {
    public static Block magic_pumpkin;
    public static Block stoneperch;
    public static Block sandstoneperch;
    public static Block obsidianperch;
    public static Block goldperch;
    public static Block ironperch;
    public static Block endstoneperch;
    public static Block netherbrickperch;

    public static Item STONE_GARGOYLE_EGG;
    public static Item SANDSTONE_GARGOYLE_EGG;
    public static Item OBSIDIAN_GARGOYLE_EGG;
    public static Item GOLDEN_GARGOYLE_EGG;
    public static Item IRON_GARGOYLE_EGG;
    public static Item END_STONE_GARGOYLE_EGG;
    public static Item NETHERATIC_GARGOYLE_EGG;
    public static Item EVIL_GARGOYLE_EGG;

    private static int entityId = 0;

    public static SoundEvent GARGOYLE_LIVING;
    public static SoundEvent GARGOYLE_GRUNT;
    public static SoundEvent GARGOYLE_DEATH;

    public static void init() {
        //Blocks
        magic_pumpkin = registerBlock(new BlockMagicPumpkin());
        stoneperch = registerBlock(new BlockPerch("stoneperch", 1.5F, 10.0F));
        sandstoneperch = registerBlock(new BlockPerch("sandstoneperch", 0.8F, 5.0F));
        obsidianperch = registerBlock(new BlockPerch("obsidianperch", 50.0F, 2000.0F));
        goldperch = registerBlock(new BlockPerch("goldperch", 3.0F, 10.0F));
        ironperch = registerBlock(new BlockPerch("ironperch", 5.0F, 10.0F));
        endstoneperch = registerBlock(new BlockPerch("endstoneperch", 3.0F, 15.0F));
        netherbrickperch = registerBlock(new BlockPerch("netherbrickperch", 2.0F, 10.0F));

        //Spawn Eggs
        STONE_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("stone_gargoyle_spawn_egg", 0, 11316396, 10526880));
        SANDSTONE_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("sandstone_gargoyle_spawn_egg", 1, 14596231, 10526880));
        OBSIDIAN_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("obsidian_gargoyle_spawn_egg", 2, 1315860, 6579300));
        GOLDEN_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("golden_gargoyle_spawn_egg", 3, 16766720, 10526880));
        IRON_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("iron_gargoyle_spawn_egg", 4, 10526880, 11316396));
        END_STONE_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("end_stone_gargoyle_spawn_egg", 5, 16777215, 10526880));
        NETHERATIC_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("netheratic_gargoyle_spawn_egg", 6, 0x3A2020,0x6B3838));
        EVIL_GARGOYLE_EGG = registerItem(new ItemGargoyleSpawnEgg("evil_gargoyle_spawn_egg", 7, 10526880, 6579300));
        if (GConfig.addVanillaSpawnEggs) {
            createVanillaEgg("giant", 44975, 7969893);
            createVanillaEgg("illusion_illager", 1267859, 9804699);
            createVanillaEgg("snowman", 15663103, 14913565);
            createVanillaEgg("villager_golem", 13288125, 13680304);
        }

        // Sounds
        GARGOYLE_LIVING = registerSound("gargoyleLiving");
        GARGOYLE_GRUNT = registerSound("gargoyleGrunt");
        GARGOYLE_DEATH = registerSound("gargoyleDeath");

        //Entities
        registerEntity(EntityGargoyle.class, "gargoyle");
    }

    private static Block registerBlock(Block block) {
        GameData.register_impl(block.setRegistryName(block.getUnlocalizedName().substring(5)));
        GameData.register_impl(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    private static Item registerItem(Item item) {
        GameData.register_impl(item);
        return item;
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        //Blocks
        registerRender(magic_pumpkin);
        registerRender(stoneperch);
        registerRender(sandstoneperch);
        registerRender(obsidianperch);
        registerRender(goldperch);
        registerRender(ironperch);
        registerRender(endstoneperch);
        registerRender(netherbrickperch);

        //Spawn Eggs
        registerRender(STONE_GARGOYLE_EGG);
        registerRender(SANDSTONE_GARGOYLE_EGG);
        registerRender(OBSIDIAN_GARGOYLE_EGG);
        registerRender(GOLDEN_GARGOYLE_EGG);
        registerRender(IRON_GARGOYLE_EGG);
        registerRender(END_STONE_GARGOYLE_EGG);
        registerRender(NETHERATIC_GARGOYLE_EGG);
        registerRender(EVIL_GARGOYLE_EGG);
        registerItemColors();

    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("gargoyles:" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block) {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("gargoyles:" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemColors() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
                new IItemColor() {
                    @Override
                    public int colorMultiplier(ItemStack stack, int tintIndex) {
                        ItemGargoyleSpawnEgg egg = (ItemGargoyleSpawnEgg) stack.getItem();

                        if (tintIndex == 0) {
                            return egg.getPrimaryColor();
                        }

                        if (tintIndex == 1) {
                            return egg.getSecondaryColor();
                        }

                        return 0xFFFFFF;
                    }
                },
                STONE_GARGOYLE_EGG,
                SANDSTONE_GARGOYLE_EGG,
                OBSIDIAN_GARGOYLE_EGG,
                GOLDEN_GARGOYLE_EGG,
                IRON_GARGOYLE_EGG,
                END_STONE_GARGOYLE_EGG,
                NETHERATIC_GARGOYLE_EGG,
                EVIL_GARGOYLE_EGG
        );
    }

    static void registerEntity(Class<? extends Entity> entityClass, String entityName) {
        ResourceLocation resource = new ResourceLocation("gargoyles", entityName);
        EntityRegistry.registerModEntity(resource, entityClass, entityName, ++entityId, Gargoyles.modInstance, 256, 1, true);
    }

    private static void createVanillaEgg(String entityName, int solidColor, int spotColor) {
        EntityRegistry.registerEgg(new ResourceLocation(entityName), solidColor, spotColor);
    }

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation("gargoyles", soundName);

        return (SoundEvent) GameData.register_impl(
                new SoundEvent(soundID).setRegistryName(soundID)
        );
    }
}