package net.minecraft.gargoyles;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.gargoyles.blocks.BlockMagicPumpkin;
import net.minecraft.gargoyles.blocks.BlockPerch;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

public class GargoyleBlocks {
    public static Block magic_pumpkin;
    public static Block stoneperch;
    public static Block sandstoneperch;
    public static Block obsidianperch;
    public static Block goldperch;
    public static Block ironperch;
    public static Block endstoneperch;
    public static Block netherbrickperch;

    public static void init() {
        magic_pumpkin = new BlockMagicPumpkin();
        stoneperch = new BlockPerch("stoneperch", 1.5F, 10.0F);
        sandstoneperch = new BlockPerch("sandstoneperch", 0.8F, 5.0F);
        obsidianperch = new BlockPerch("obsidianperch", 50.0F, 2000.0F);
        goldperch = new BlockPerch("goldperch", 3.0F, 10.0F);
        ironperch = new BlockPerch("ironperch", 5.0F, 10.0F);
        endstoneperch = new BlockPerch("endstoneperch", 3.0F, 15.0F);
        netherbrickperch = new BlockPerch("netherbrickperch", 2.0F, 10.0F);
        registerBlock(magic_pumpkin);
        registerBlock(stoneperch);
        registerBlock(sandstoneperch);
        registerBlock(obsidianperch);
        registerBlock(goldperch);
        registerBlock(ironperch);
        registerBlock(endstoneperch);
        registerBlock(netherbrickperch);
    }

    private static void registerBlock(Block block) {
        GameData.register_impl(block.setRegistryName(block.getUnlocalizedName().substring(5)));
        GameData.register_impl((new ItemBlock(block)).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        registerRender(magic_pumpkin);
        registerRender(stoneperch);
        registerRender(sandstoneperch);
        registerRender(obsidianperch);
        registerRender(goldperch);
        registerRender(ironperch);
        registerRender(endstoneperch);
        registerRender(netherbrickperch);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block) {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("gargoyles:" + item.getUnlocalizedName().substring(5), "inventory"));
    }
}
