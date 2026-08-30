package net.minecraft.gargoyles.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.gargoyles.RegistryHandler;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMagicPumpkin extends BlockHorizontal {
    private BlockPattern stoneGargoylePattern;
    private BlockPattern sandstoneGargoylePattern;
    private BlockPattern obsidianGargoylePattern;
    private BlockPattern goldenGargoylePattern;
    private BlockPattern ironGargoylePattern;
    private BlockPattern endstoneGargoylePattern;
    private BlockPattern netheraticGargoylePattern;

    public BlockMagicPumpkin() {
        super(Material.GOURD, MapColor.ADOBE);
        this.setUnlocalizedName("magic_pumpkin");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGargoyle(worldIn, pos);
    }

    public boolean canDispenserPlace(World worldIn, BlockPos pos) {
        return this.getStoneGargoylePattern().match(worldIn, pos) != null || this.getSandstoneGargoylePattern().match(worldIn, pos) != null || this.getObsidianGargoylePattern().match(worldIn, pos) != null || this.getGoldenGargoylePattern().match(worldIn, pos) != null || this.getIronGargoylePattern().match(worldIn, pos) != null || this.getEndstoneGargoylePattern().match(worldIn, pos) != null || this.getNetheraticGargoylePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGargoyle(World worldIn, BlockPos pos) {
        BlockPattern pattern;
        BlockPattern.PatternHelper patternHelper;

        pattern = this.getStoneGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 0);
            return;
        }

        pattern = this.getSandstoneGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 1);
            return;
        }

        pattern = this.getObsidianGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 2);
            return;
        }

        pattern = this.getGoldenGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 3);
            return;
        }

        pattern = this.getIronGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 4);
            return;
        }

        pattern = this.getEndstoneGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 5);
            return;
        }

        pattern = this.getNetheraticGargoylePattern();
        patternHelper = pattern.match(worldIn, pos);
        if (patternHelper != null) {
            spawnGargoyle(worldIn, pattern, patternHelper, 6);
        }
    }

    private void spawnGargoyle(World worldIn, BlockPattern pattern, BlockPattern.PatternHelper patternHelper, int gargoyleType)
    {
        //Remove the structure
        for (int x = 0; x < pattern.getPalmLength(); ++x) {
            for (int y = 0; y < pattern.getThumbLength(); ++y) {
                worldIn.setBlockState(patternHelper.translateOffset(x, y, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
            }
        }

        //Create gargoyle
        EntityGargoyle entityGargoyle = new EntityGargoyle(worldIn);
        entityGargoyle.setGargoyleType(gargoyleType);
        entityGargoyle.setPlayerCreated(true);

        BlockPos spawnPos = patternHelper.translateOffset(1, 2, 0).getPos();
        entityGargoyle.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.05D, spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
        worldIn.spawnEntity(entityGargoyle);

        //Trigger summoned entity advancement
        for (EntityPlayerMP player : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entityGargoyle.getEntityBoundingBox().grow(5.0D))) {

            CriteriaTriggers.SUMMONED_ENTITY.trigger(player,entityGargoyle);
        }

        for (int i = 0; i < 120; ++i) {
            worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, spawnPos.getX() + worldIn.rand.nextDouble(), spawnPos.getY() + worldIn.rand.nextDouble() * 2.5D, spawnPos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
        }

        //Notify neighbors
        for (int x = 0; x < pattern.getPalmLength(); ++x) {
            for (int y = 0; y < pattern.getThumbLength(); ++y) {
                BlockWorldState blockState = patternHelper.translateOffset(x, y, 0);

                worldIn.notifyNeighborsRespectDebug(blockState.getPos(), Blocks.AIR, false);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    protected BlockPattern getStoneGargoylePattern() {
        if (this.stoneGargoylePattern == null) {
            this.stoneGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "SIS", "~R~"}).where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK))).where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.STONE))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.stoneGargoylePattern;
    }

    protected BlockPattern getSandstoneGargoylePattern() {
        if (this.sandstoneGargoylePattern == null) {
            this.sandstoneGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "SSS", "~R~"}).where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK))).where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SANDSTONE))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.sandstoneGargoylePattern;
    }

    protected BlockPattern getObsidianGargoylePattern() {
        if (this.obsidianGargoylePattern == null) {
            this.obsidianGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "ODO", "~D~"}).where('D', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK))).where('O', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.OBSIDIAN))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.obsidianGargoylePattern;
    }

    protected BlockPattern getGoldenGargoylePattern() {
        if (this.goldenGargoylePattern == null) {
            this.goldenGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "GDG", "~D~"}).where('D', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK))).where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.goldenGargoylePattern;
    }

    protected BlockPattern getIronGargoylePattern() {
        if (this.ironGargoylePattern == null) {
            this.ironGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "IGI", "~R~"}).where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK))).where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.ironGargoylePattern;
    }

    protected BlockPattern getEndstoneGargoylePattern() {
        if (this.endstoneGargoylePattern == null) {
            this.endstoneGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "IGI", "~R~"}).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK))).where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_STONE))).where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
        }

        return this.endstoneGargoylePattern;
    }

    protected BlockPattern getNetheraticGargoylePattern() {
        if (this.netheraticGargoylePattern == null) {
            this.netheraticGargoylePattern = FactoryBlockPattern.start().aisle(new String[]{"~^~", "IGI", "~R~"}).where('R', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.REDSTONE_BLOCK))).where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.NETHER_BRICK))).where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(RegistryHandler.magic_pumpkin))).build();
        }

        return this.netheraticGargoylePattern;
    }
}