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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.gargoyles.GConfig;
import net.minecraft.gargoyles.GargoyleBlocks;
import net.minecraft.gargoyles.ModSoundEvents;
import net.minecraft.gargoyles.blocks.BlockPerch;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import net.minecraftforge.common.IPlantable;

public class EntityGargoyle extends EntityIronGolem {
    private static final Set<Block> BLACKLISTEDBLOCKS = Sets.newIdentityHashSet();
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private int clientSideAttackTime;
    private EntityLivingBase targetedEntity;
    private static final DataParameter<Integer> STATE;
    private static final DataParameter<Integer> TARGET_ENTITY;
    private static final DataParameter<Integer> TYPE;
    private static final Predicate<EntityLiving> ATTACKABLE;

    public EntityGargoyle(World p_i1694_1_) {
        super(p_i1694_1_);
        this.enablePersistence();
        this.setSize(0.9F, 2.4F);
        this.tasks.addTask(0, new AIPerch());
        this.tasks.addTask(0, new AIBeamAttack());
        this.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 10, 200));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, ATTACKABLE));
    }

    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        if (entitylivingbaseIn == null) {
            super.setAttackTarget((EntityLivingBase)null);
        } else if (entitylivingbaseIn instanceof EntityLiving && this.getRevengeTarget() == null && !((EntityLiving)entitylivingbaseIn).getCustomNameTag().isEmpty()) {
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

    public float getAttackAnimationScale(float p_175477_1_) {
        return ((float)this.clientSideAttackTime + p_175477_1_) / 80.0F;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(TYPE, 0);
        this.dataManager.register(TARGET_ENTITY, 0);
    }

    public int getState() {
        return (Integer)this.dataManager.get(STATE);
    }

    public void setState(int state) {
        this.dataManager.set(STATE, state);
    }

    public int getGargoyleType() {
        return (Integer)this.dataManager.get(TYPE);
    }

    public void setGargoyleType(int p_82201_1_) {
        this.dataManager.set(TYPE, p_82201_1_);
        switch (p_82201_1_) {
            case 0:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.stoneGargoyleHealth);
                this.setHealth(GConfig.stoneGargoyleHealth);
            case 1:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.sandstoneGargoyleHealth);
                this.setHealth(GConfig.sandstoneGargoyleHealth);
            case 2:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.obsidianGargoyleHealth);
                this.setHealth(GConfig.obsidianGargoyleHealth);
            case 3:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.goldenGargoyleHealth);
                this.setHealth(GConfig.goldenGargoyleHealth);
            case 4:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.ironGargoyleHealth);
                this.setHealth(GConfig.ironGargoyleHealth);
            case 5:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.endStoneGargoyleHealth);
                this.setHealth(GConfig.endStoneGargoyleHealth);
            case 6:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.netherBrickGargoyleHealth);
                this.setHealth(GConfig.netherBrickGargoyleHealth);
            default:
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("GargoyleType", 99)) {
            this.setGargoyleType(tagCompund.getByte("GargoyleType"));
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("GargoyleType", (byte)this.getGargoyleType());
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.gargoyleFollowRange);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.gargoyleMovementSpeed);
    }

    public boolean canAttackClass(Class cls) {
        return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls) ? false : cls != EntityIronGolem.class && cls != EntityGargoyle.class;
    }

    protected void collideWithEntity(Entity p_82167_1_) {
        if (p_82167_1_ instanceof EntityGargoyle && this.getAttackTarget() == null && ((EntityGargoyle)p_82167_1_).getAttackTarget() == null && this.onGround && ((EntityGargoyle)p_82167_1_).onGround && this.getDistanceSq(((EntityGargoyle)p_82167_1_).waypointX, ((EntityGargoyle)p_82167_1_).waypointY, ((EntityGargoyle)p_82167_1_).waypointZ) < (double)4.0F) {
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
        if (this.collidedHorizontally) {
            this.motionY = 0.4;
        }

        if (this.isEntityAlive()) {
            if (this.getTargetedEntity() != null) {
                this.setState(1);
            } else if (this.getVillage() == null && this.world.getBlockState(new BlockPos((int)this.waypointX, (int)this.waypointY, (int)this.waypointZ)).getBlock() != this.getFavoriteBlockToPerch()) {
                this.setState(-1);
            } else {
                this.setState(0);
            }
        }

        BlockPos pos = new BlockPos((int)this.waypointX, (int)this.waypointY, (int)this.waypointZ);
        Block block = this.world.getBlockState(pos).getBlock();
        if (this.getGargoyleType() == 6) {
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, (double)0.0F, (double)0.0F, (double)0.0F, new int[0]);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)this.width, (double)0.0F, (double)0.0F, (double)0.0F, new int[0]);
        }

        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null && this.getDistanceSq((double)this.getVillage().getCenter().getX(), (double)this.getVillage().getCenter().getY(), (double)this.getVillage().getCenter().getZ()) > (double)1024.0F) {
            double d0 = (double)this.getVillage().getCenter().getX() - this.posX;
            double d1 = (double)this.getVillage().getCenter().getZ() - this.posZ;
            double d3 = d0 * d0 + d1 * d1;
            if (this.posY <= (double)this.getVillage().getCenter().getY() + (double)1.0F) {
                this.motionY += 0.6 - this.motionY;
            }

            double d5 = (double)MathHelper.sqrt(d3);
            this.motionX += d0 / d5 * 0.6 - this.motionX;
            this.motionZ += d1 / d5 * 0.6 - this.motionZ;
        }

        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null) {
            List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)(this.getVillage().getCenter().getX() - this.getVillage().getVillageRadius()), (double)this.getVillage().getCenter().getY() - (double)4.0F, (double)(this.getVillage().getCenter().getZ() - this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getX() + this.getVillage().getVillageRadius()), (double)this.getVillage().getCenter().getY() + (double)4.0F, (double)(this.getVillage().getCenter().getZ() + this.getVillage().getVillageRadius())), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));
            if (list != null && !list.isEmpty() && this.rand.nextInt(5) == 0) {
                for(int i = 0; i < list.size(); ++i) {
                    EntityLiving entity = (EntityLiving)list.get(this.rand.nextInt(list.size()));
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass()) && entity instanceof IMob) {
                        this.setAttackTarget(entity);
                    }
                }
            }
        }

        if (!this.world.isRemote && this.getAttackTarget() == null) {
            List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));
            if (list != null && !list.isEmpty() && this.rand.nextInt(GConfig.gargoyleTargetSearchChance) == 0) {
                for(int i = 0; i < list.size(); ++i) {
                    EntityLiving entity = (EntityLiving)list.get(this.rand.nextInt(list.size()));
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass()) && entity instanceof IMob && this.canEntityBeSeen(entity)) {
                        this.setAttackTarget(entity);
                    }
                }
            }
        }

        if (this.getLeashHolder() != null) {
            this.getLookHelper().setLookPositionWithEntity(this.getLeashHolder(), 180.0F, 180.0F);
        }

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
                    this.heal(this.world.getBlockState(pos).getBlock() == this.getFavoriteBlockToPerch() ? GConfig.gargoyleFavoritePerchHeal : GConfig.gargoyleNormalPerchHeal);

                }
            }
        } else {
            this.noClip = false;
        }

        Entity entity = this.getAttackTarget();
        double move = entity != null ? 0.6 + this.rand.nextDouble() * 0.2 : 0.8;
        if (!this.onGround && this.motionY < (double)0.0F) {
            this.motionY *= 0.6;
        }

        if (!this.world.isRemote && entity != null) {
            double d0 = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1;
            if (!this.world.isRemote && this.isEntityAlive() && this.getDistanceSq(entity) <= (double)(entity.width * entity.width + this.width * this.width) + (double)16.0F && (this.ticksExisted + this.getEntityId()) % GConfig.gargoyleAttackCooldown == 0 && this.canEntityBeSeen(entity)) {
                this.attackEntityAsMob(entity);
                this.getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
            }

            if (d3 > (this.getGargoyleType() == 3 ? (double)512.0F : (double)1.0F) && (this.canEntityBeSeen(entity) || this.isEntityInsideOpaqueBlock() || this.posY <= (double)0.0F || this.posY <= entity.posY || this.rand.nextInt(10) == 0)) {
                if (this.posY <= entity.posY + (double)1.0F) {
                    this.motionY += 0.8 - this.motionY;
                }

                double d5 = (double)MathHelper.sqrt(d3);
                this.motionX += d0 / d5 * move - this.motionX;
                this.motionZ += d1 / d5 * move - this.motionZ;
                this.getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
                this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            }
        }

        if (this.getNatureBlock(this.world.getBlockState(pos.up()))) {
            ++this.waypointY;
        }

        if (!this.world.isRemote && (this.getInvalidBlock(this.world.getBlockState(pos)) || this.world.getBlockState(pos.up(1)).getBlock() != Blocks.AIR || this.world.getBlockState(pos.up(2)).getBlock() != Blocks.AIR) || this.world.getBlockState(pos.up(3)).getBlock() != Blocks.AIR) {
            int i = MathHelper.floor(this.posY);
            int i1 = MathHelper.floor(this.posX);
            int j1 = MathHelper.floor(this.posZ);
            boolean flag = false;

            for(int l1 = -8; l1 <= 8; ++l1) {
                for(int i2 = -8; i2 <= 8; ++i2) {
                    for(int j = -8; j <= 8; ++j) {
                        int j2 = i1 + l1;
                        int k = i + j;
                        int l = j1 + i2;
                        IBlockState blockmain = this.world.getBlockState(new BlockPos(j2, k, l));
                        Block block1 = this.world.getBlockState(new BlockPos(j2, k + 1, l)).getBlock();
                        Block block2 = this.world.getBlockState(new BlockPos(j2, k + 2, l)).getBlock();
                        Block block3 = this.world.getBlockState(new BlockPos(j2, k + 3, l)).getBlock();
                        if (this.getNatureBlock(blockmain) && (blockmain.getBlock() == this.getFavoriteBlockToPerch() && this.rand.nextInt(5) == 0 || this.rand.nextInt(600) == 0) && block1 == Blocks.AIR && block2 == Blocks.AIR && block3 == Blocks.AIR) {
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
        switch (this.getGargoyleType()) {
            case 0:
            default:
                return GargoyleBlocks.stoneperch;
            case 1:
                return GargoyleBlocks.sandstoneperch;
            case 2:
                return GargoyleBlocks.obsidianperch;
            case 3:
                return GargoyleBlocks.goldperch;
            case 4:
                return GargoyleBlocks.ironperch;
            case 5:
                return GargoyleBlocks.endstoneperch;
            case 6:
                return GargoyleBlocks.netherbrickperch;
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else if (!source.isExplosion() && source != DamageSource.IN_WALL && source != DamageSource.DROWN && source != DamageSource.ON_FIRE && source != DamageSource.IN_FIRE) {
            if (source.getTrueSource() instanceof EntityLivingBase && this.canAttackClass(source.getTrueSource().getClass())) {
                this.setRevengeTarget((EntityLivingBase)source.getTrueSource());
            }

            return super.attackEntityFrom(source, amount);
        } else {
            return false;
        }
    }

    public boolean attackEntityAsMob(Entity target) {
        this.world.setEntityState(this, (byte) 4);

        double damage;

        switch (this.getGargoyleType()) {
            case 0:
                damage = GConfig.stoneGargoyleDamage;
                break;

            case 1:
                if (this.world.getBiome(this.getPosition()) == Biomes.DESERT
                        || this.world.getBiome(this.getPosition()) == Biomes.DESERT_HILLS) {
                    damage = GConfig.sandstoneGargoyleDesertDamage;
                } else {
                    damage = GConfig.sandstoneGargoyleDamage;
                }
                break;

            case 2:
                damage = GConfig.obsidianGargoyleDamage;
                break;

            case 3:
                damage = GConfig.goldenGargoyleDamage;
                break;

            case 4:
                damage = GConfig.ironGargoyleDamage;
                break;

            case 5:
                damage = GConfig.endStoneGargoyleDamage;
                break;

            case 6:
                damage = GConfig.netherBrickGargoyleDamage;
                break;

            default:
                damage = GConfig.stoneGargoyleDamage;
                break;
        }

        boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) damage);

        if (flag) {
            switch (this.getGargoyleType()) {
                case 0:
                    target.motionY += GConfig.stoneGargoyleKnockUp;
                    break;

                case 1:
                    target.motionY += GConfig.sandstoneGargoyleKnockUp;
                    break;

                case 2:
                    target.motionY += GConfig.obsidianGargoyleKnockUp;
                    break;

                case 3:
                    target.motionY += GConfig.goldenGargoyleKnockUp;
                    break;

                case 4:
                    target.motionY += GConfig.ironGargoyleKnockUp;
                    break;

                case 5:
                    target.motionY += GConfig.endStoneGargoyleKnockUp;
                    break;

                case 6:
                    target.motionY += GConfig.netherBrickGargoyleKnockUp;
                    target.setFire(GConfig.netherBrickGargoyleFireTime);
                    break;
            }
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
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

    private void setTargetedEntity(int entityId) {
        this.dataManager.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return (Integer)this.dataManager.get(TARGET_ENTITY) != 0;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            } else {
                Entity entity = this.world.getEntityByID((Integer)this.dataManager.get(TARGET_ENTITY));
                if (entity instanceof EntityLivingBase) {
                    this.targetedEntity = (EntityLivingBase)entity;
                    return this.targetedEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.getAttackTarget();
        }
    }

    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int j = 1 + this.rand.nextInt(2);
        switch (this.getGargoyleType()) {
            case 0:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.STONE), 1, 0.0F);
                }
                break;
            case 1:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.SANDSTONE), 1, 0.0F);
                }
                break;
            case 2:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.OBSIDIAN), 1, 0.0F);
                }
                break;
            case 3:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1, 0.0F);
                }
                break;
            case 4:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.IRON_BLOCK), 1, 0.0F);
                }
                break;
            case 5:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.END_STONE), 1, 0.0F);
                }
                break;
            case 6:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1, 0.0F);
                }
        }
    }

    static {
        STATE = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);
        TARGET_ENTITY = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);
        TYPE = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);
        ATTACKABLE = new Predicate<EntityLiving>() {
            public boolean apply(@Nullable EntityLiving p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_ instanceof IMob && p_apply_1_.getCustomNameTag().isEmpty() && ((EntityLivingBase)p_apply_1_).attackable();
            }
        };
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

    class AIBeamAttack extends EntityAIBase {
        private EntityGargoyle guardian = EntityGargoyle.this;
        private int tickCounter;

        public AIBeamAttack() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (entitylivingbase.attackable() || entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired());
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            return super.shouldContinueExecuting() && entitylivingbase != null && (entitylivingbase.attackable() || entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired());
        }

        public void startExecuting() {
            this.tickCounter = -10;
            if (this.guardian.getGargoyleType() == 3) {
                this.guardian.getNavigator().clearPath();
                this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
            }

        }

        public void resetTask() {
            this.guardian.setAttackTarget((EntityLivingBase)null);
            this.guardian.setTargetedEntity(0);
        }

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            if (this.guardian.canEntityBeSeen(entitylivingbase)) {
                this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
            }

            if (this.guardian.getGargoyleType() == 3) {
                this.guardian.getNavigator().clearPath();
                this.guardian.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
                if (!this.guardian.canEntityBeSeen(entitylivingbase)) {
                    this.guardian.setAttackTarget((EntityLivingBase)null);
                } else {
                    ++this.tickCounter;
                    this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
                    if (this.tickCounter > 0) {
                        entitylivingbase.attackEntityFrom(DamageSource.MAGIC, (float)(this.guardian.clientSideAttackTime / 80));
                        entitylivingbase.setFire(1 + this.tickCounter);
                        entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.5F, 1.0F + (float)(this.guardian.clientSideAttackTime / 80));
                    }

                    if (this.tickCounter % 20 == 0) {
                        entitylivingbase.hurtResistantTime = 0;
                    }

                    if (this.tickCounter >= 80) {
                        float f = 8.0F;
                        if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                            f += 4.0F;
                        }

                        this.guardian.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + this.guardian.getRNG().nextFloat(), this.guardian.getRNG().nextFloat() * 0.7F + 0.3F);
                        entitylivingbase.world.newExplosion((Entity)null, entitylivingbase.posX, entitylivingbase.posY + (double)1.0F, entitylivingbase.posZ, 1.0F, true, false);
                        entitylivingbase.world.newExplosion(this.guardian, entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.getEyeHeight(), entitylivingbase.posZ, 1.0F, true, false);
                        entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
                        this.guardian.attackEntityAsMob(entitylivingbase);
                        this.guardian.setAttackTarget((EntityLivingBase)null);
                        this.tickCounter = 0;
                        this.resetTask();
                    }
                }

                super.updateTask();
            }

        }
    }

    class AIPerch extends EntityAIBase {
        public AIPerch() {
            this.setMutexBits(7);
        }

        public boolean shouldExecute() {
            IBlockState blockmain = EntityGargoyle.this.world.getBlockState(new BlockPos((int)EntityGargoyle.this.waypointX, (int)EntityGargoyle.this.waypointY, (int)EntityGargoyle.this.waypointZ));
            return EntityGargoyle.this.getNatureBlock(blockmain) && EntityGargoyle.this.getAttackTarget() == null;
        }
    }
}
