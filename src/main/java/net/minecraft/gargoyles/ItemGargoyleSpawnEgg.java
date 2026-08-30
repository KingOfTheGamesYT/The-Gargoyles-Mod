package net.minecraft.gargoyles;

import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGargoyleSpawnEgg extends Item {

    private final int gargoyleType;
    private final int primaryColor;
    private final int secondaryColor;

    public ItemGargoyleSpawnEgg(String name, int gargoyleType, int primaryColor, int secondaryColor) {
        this.gargoyleType = gargoyleType;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;

        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(net.minecraft.creativetab.CreativeTabs.MISC);
        this.setMaxStackSize(64);
    }

    public int getGargoyleType() {
        return this.gargoyleType;
    }

    public int getPrimaryColor() {
        return this.primaryColor;
    }

    public int getSecondaryColor() {
        return this.secondaryColor;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            BlockPos spawnPos = pos.offset(facing);
            EntityGargoyle gargoyle = new EntityGargoyle(world);
            gargoyle.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
            gargoyle.setGargoyleType(this.gargoyleType);

            if (this.gargoyleType != 7) {
                gargoyle.setPlayerCreated(true);
            }

            world.spawnEntity(gargoyle);

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }
}