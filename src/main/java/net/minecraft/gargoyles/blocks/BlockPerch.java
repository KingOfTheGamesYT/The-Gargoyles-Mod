package net.minecraft.gargoyles.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPerch extends Block {

    public BlockPerch(String blockname, float hardness, float resistance) {
        super(Material.ROCK);

        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(blockname);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8D, 1.0D);
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }
}