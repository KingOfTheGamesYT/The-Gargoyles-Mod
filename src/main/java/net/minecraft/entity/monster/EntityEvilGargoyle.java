package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.gargoyles.GargoyleBlocks;
import net.minecraft.gargoyles.ModSoundEvents;
import net.minecraft.gargoyles.blocks.BlockPerch;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEvilGargoyle extends EntityMob {
    private static final Set<Block> BLACKLISTEDBLOCKS = Sets.newIdentityHashSet();
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private int attackTimer;
    private static final Predicate<EntityLivingBase> ATTACKABLE = new Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
            return p_apply_1_ != null && !(p_apply_1_ instanceof IMob) && p_apply_1_.attackable();
        }
    };

    public EntityEvilGargoyle(World p_i1694_1_) {
        super(p_i1694_1_);
        this.enablePersistence();
        this.setSize(0.9F, 2.4F);
        this.experienceValue = 10;
        this.tasks.addTask(0, new AIPerch());
        this.tasks.addTask(1, new EntityAIAttackMelee(this, (double)1.0F, true));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, false, ATTACKABLE));
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        if (entitylivingbaseIn == null) {
            super.setAttackTarget((EntityLivingBase)null);
        } else {
            super.setAttackTarget(entitylivingbaseIn);
        }

    }

    protected ResourceLocation getLootTable() {
        return null;
    }

    public float getEyeHeight() {
        return !this.onGround ? 1.4F : (this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - (double)0.5F), MathHelper.floor(this.posZ))).getBlock() == this.getFavoriteBlockToPerch() ? 0.875F + this.rotationPitch / 40.0F : 2.1F);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)50.0F);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue((double)24.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.25F);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue((double)1.0F);
    }

    public boolean canAttackClass(Class cls) {
        return cls != EntityEvilGargoyle.class;
    }

    protected void collideWithEntity(Entity p_82167_1_) {
        if (p_82167_1_ instanceof EntityEvilGargoyle && this.getAttackTarget() == null && ((EntityEvilGargoyle)p_82167_1_).getAttackTarget() == null && this.onGround && ((EntityEvilGargoyle)p_82167_1_).onGround && this.getDistanceSq(((EntityEvilGargoyle)p_82167_1_).waypointX, ((EntityEvilGargoyle)p_82167_1_).waypointY, ((EntityEvilGargoyle)p_82167_1_).waypointZ) < (double)4.0F) {
            ++this.waypointY;
            this.noClip = false;
        }

        super.collideWithEntity(p_82167_1_);
    }

    public int getMaxFallHeight() {
        return 256;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.ticksExisted < 20) {
            this.getNavigator().clearPath();
        }

        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.collidedHorizontally) {
            this.motionY = 0.4;
        }

        BlockPos pos = new BlockPos((int)this.waypointX, (int)this.waypointY, (int)this.waypointZ);
        Block block = this.world.getBlockState(pos).getBlock();
        this.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, (double)0.0F, (double)0.0F, (double)0.0F, new int[0]);
        if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
            this.setAttackTarget((EntityLivingBase)null);
        }

        if (this.getAttackTarget() == null && this.getNatureBlock(this.world.getBlockState(pos))) {
            double d0 = this.waypointX - this.posX;
            double d1 = this.waypointY + (double)1.0F - this.posY;
            double d2 = this.waypointZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 == (double)4.0F) {
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead += 180.0F;
            }

            if (d3 > (double)3.0F) {
                double d5 = (double)MathHelper.sqrt(d3);
                this.motionX += d0 / d5 * (double)0.75F - this.motionX;
                this.motionY += d1 / d5 * (double)0.75F - this.motionY;
                this.motionZ += d2 / d5 * (double)0.75F - this.motionZ;
                this.getLookHelper().setLookPosition(this.waypointX, this.waypointY, this.waypointZ, 180.0F, 0.0F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
                this.noClip = true;
            } else {
                this.getNavigator().clearPath();
                this.setLocationAndAngles(this.waypointX + (double)0.5F, this.waypointY + (block instanceof BlockPerch ? 0.8 : (double)1.0F), this.waypointZ + (double)0.5F, this.rotationYawHead, 40.0F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
                this.noClip = false;
                this.extinguish();
                if ((this.ticksExisted + this.getEntityId()) % (this.world.getBlockState(pos).getBlock() == this.getFavoriteBlockToPerch() ? 20 : 40) == 0) {
                    this.heal(this.world.getBlockState(pos).getBlock() == this.getFavoriteBlockToPerch() ? 2.0F : 1.0F);
                }
            }
        } else {
            this.noClip = false;
        }

        Entity entity = this.getAttackTarget();
        double move = entity != null ? 0.4 + this.rand.nextDouble() * 0.2 : 0.6;
        if (!this.onGround && this.motionY < (double)0.0F) {
            this.motionY *= 0.6;
        }

        if (!this.world.isRemote && entity != null) {
            double d0 = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1;
            if (!this.world.isRemote && this.isEntityAlive() && this.getDistanceSq(entity) <= (double)(entity.width * entity.width + this.width * this.width) + (double)16.0F && (this.ticksExisted + this.getEntityId()) % 20 == 0 && this.canEntityBeSeen(entity)) {
                this.attackEntityAsMob(entity);
                this.getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
            }

            if (d3 > (double)1.0F && (this.canEntityBeSeen(entity) || this.isEntityInsideOpaqueBlock() || this.posY <= (double)0.0F || this.posY <= entity.posY || this.rand.nextInt(10) == 0)) {
                if (this.posY <= entity.posY + (double)0.5F) {
                    this.motionY += 0.6 - this.motionY;
                }

                double d5 = (double)MathHelper.sqrt(d3);
                this.motionX += d0 / d5 * move - this.motionX * move;
                this.motionZ += d1 / d5 * move - this.motionZ * move;
                this.getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            }
        }

        if (!this.world.isRemote && this.getAttackTarget() == null) {
            List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));
            if (list != null && !list.isEmpty() && this.rand.nextInt(10) == 0) {
                for(int i = 0; i < list.size(); ++i) {
                    EntityLiving entity1 = (EntityLiving)list.get(this.rand.nextInt(list.size()));
                    if (this.isEntityAlive() && entity1.isEntityAlive() && this.canAttackClass(entity1.getClass()) && !(entity1 instanceof IMob)) {
                        this.setAttackTarget(entity1);
                    }
                }
            }
        }

        if (this.getNatureBlock(this.world.getBlockState(pos.up()))) {
            ++this.waypointY;
        }

        if (this.ticksExisted > 100 && !this.world.isRemote && (this.getInvalidBlock(this.world.getBlockState(pos)) || this.world.getBlockState(pos.up(1)).getBlock() != Blocks.AIR || this.world.getBlockState(pos.up(2)).getBlock() != Blocks.AIR) || this.world.getBlockState(pos.up(3)).getBlock() != Blocks.AIR) {
            int i = MathHelper.floor(this.posY);
            int i1 = MathHelper.floor(this.posX);
            int j1 = MathHelper.floor(this.posZ);
            boolean flag = false;

            for(int l1 = -2; l1 <= 2; ++l1) {
                for(int i2 = -2; i2 <= 2; ++i2) {
                    for(int j = -2; j <= 2; ++j) {
                        int j2 = i1 + l1;
                        int k = i + j;
                        int l = j1 + i2;
                        IBlockState blockmain = this.world.getBlockState(new BlockPos(j2, k, l));
                        Block block1 = this.world.getBlockState(new BlockPos(j2, k + 1, l)).getBlock();
                        Block block2 = this.world.getBlockState(new BlockPos(j2, k + 2, l)).getBlock();
                        Block block3 = this.world.getBlockState(new BlockPos(j2, k + 3, l)).getBlock();
                        if (this.getNatureBlock(blockmain) && blockmain.getBlock() == this.getFavoriteBlockToPerch() && this.rand.nextInt(5) == 0 && block1 == Blocks.AIR && block2 == Blocks.AIR && block3 == Blocks.AIR) {
                            this.waypointX = (double)j2;
                            this.waypointY = (double)k;
                            this.waypointZ = (double)l;
                        }
                    }
                }
            }
        }

    }

    public boolean getNatureBlock(IBlockState state) {
        return (state.getMaterial().isOpaque() || state.getBlock() == this.getFavoriteBlockToPerch()) && state.getBlock().isTopSolid(state) && !(state.getBlock() instanceof IPlantable) && !(state.getBlock() instanceof IGrowable) && !BLACKLISTEDBLOCKS.contains(state.getBlock());
    }

    public boolean getInvalidBlock(IBlockState state) {
        return !state.getMaterial().isOpaque() || state.getBlock() != this.getFavoriteBlockToPerch() || !state.getBlock().isTopSolid(state) || state.getBlock() instanceof IPlantable || state.getBlock() instanceof IGrowable || BLACKLISTEDBLOCKS.contains(state.getBlock());
    }

    public Block getFavoriteBlockToPerch() {
        return GargoyleBlocks.stoneperch;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (source != DamageSource.IN_WALL && source != DamageSource.DROWN && source != DamageSource.ON_FIRE && source != DamageSource.IN_FIRE) {
            if (source.getTrueSource() instanceof EntityLivingBase && this.canAttackClass(source.getTrueSource().getClass())) {
                this.setRevengeTarget((EntityLivingBase)source.getTrueSource());
            }

            return super.attackEntityFrom(source, amount);
        } else {
            return false;
        }
    }

    public boolean attackEntityAsMob(Entity p_70652_1_) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 6.0F + this.rand.nextFloat() * 6.0F);
        if (flag) {
            p_70652_1_.motionY += 0.3;
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleStatusUpdate(id);
        }

    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    protected SoundEvent getAmbientSound() {
        return this.getNatureBlock(this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - (double)0.5F), MathHelper.floor(this.posZ)))) ? null : ModSoundEvents.gargoyleLiving;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.gargoyleGrunt;
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.gargoyleDeath;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return false;
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int j = 2 + this.rand.nextInt(3);

        for(int k = 0; k < j; ++k) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.STONE), 1, 0.0F);
        }

    }

    static {
        BLACKLISTEDBLOCKS.add(Blocks.AIR);
        BLACKLISTEDBLOCKS.add(Blocks.GRASS_PATH);
        BLACKLISTEDBLOCKS.add(Blocks.BEDROCK);
        BLACKLISTEDBLOCKS.add(Blocks.BARRIER);
        BLACKLISTEDBLOCKS.add(Blocks.COMMAND_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.CHAIN_COMMAND_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.REPEATING_COMMAND_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.STRUCTURE_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.GRASS);
        BLACKLISTEDBLOCKS.add(Blocks.DIRT);
        BLACKLISTEDBLOCKS.add(Blocks.MYCELIUM);
        BLACKLISTEDBLOCKS.add(Blocks.STONE);
        BLACKLISTEDBLOCKS.add(Blocks.END_STONE);
        BLACKLISTEDBLOCKS.add(Blocks.SANDSTONE);
        BLACKLISTEDBLOCKS.add(Blocks.SAND);
        BLACKLISTEDBLOCKS.add(Blocks.GRAVEL);
        BLACKLISTEDBLOCKS.add(Blocks.TNT);
        BLACKLISTEDBLOCKS.add(Blocks.CACTUS);
        BLACKLISTEDBLOCKS.add(Blocks.CLAY);
        BLACKLISTEDBLOCKS.add(Blocks.PUMPKIN);
        BLACKLISTEDBLOCKS.add(Blocks.MELON_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.NETHERRACK);
        BLACKLISTEDBLOCKS.add(Blocks.SOUL_SAND);
        BLACKLISTEDBLOCKS.add(Blocks.LOG);
        BLACKLISTEDBLOCKS.add(Blocks.LOG2);
        BLACKLISTEDBLOCKS.add(Blocks.LEAVES);
        BLACKLISTEDBLOCKS.add(Blocks.LEAVES2);
        BLACKLISTEDBLOCKS.add(Blocks.COAL_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.COAL_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.IRON_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.IRON_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.LAPIS_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.LAPIS_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.REDSTONE_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.LIT_REDSTONE_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.REDSTONE_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.GOLD_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.GOLD_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.DIAMOND_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.DIAMOND_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.EMERALD_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.EMERALD_BLOCK);
    }

    class AIPerch extends EntityAIBase {
        public AIPerch() {
            this.setMutexBits(7);
        }

        public boolean shouldExecute() {
            IBlockState blockmain = EntityEvilGargoyle.this.world.getBlockState(new BlockPos((int)EntityEvilGargoyle.this.waypointX, (int)EntityEvilGargoyle.this.waypointY, (int)EntityEvilGargoyle.this.waypointZ));
            return EntityEvilGargoyle.this.getNatureBlock(blockmain) && EntityEvilGargoyle.this.getAttackTarget() == null;
        }
    }
}
