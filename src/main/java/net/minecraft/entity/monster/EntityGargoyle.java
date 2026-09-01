package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.gargoyles.GConfig;
import net.minecraft.gargoyles.RegistryHandler;
import net.minecraft.gargoyles.blocks.BlockPerch;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
    private UUID creatorUUID;

    public EntityGargoyle(World world) {
        super(world);
        this.enablePersistence();
        this.setSize(0.9F, 2.4F);
        this.tasks.addTask(0, new AIPerch());
        this.tasks.addTask(0, new AIBeamAttack());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, ATTACKABLE));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, (double)1.0F, true));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn) {
        if (entitylivingbaseIn == null) {
            super.setAttackTarget(null);
            return;
        }

        if (this.canAttackEntity(entitylivingbaseIn)) {
            super.setAttackTarget(entitylivingbaseIn);
        } else {
            super.setAttackTarget(null);
        }
    }

    protected ResourceLocation getLootTable() {
        return null;
    }

    public float getEyeHeight() {
        return !this.onGround ? 1.4F : (this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - (double)0.5F), MathHelper.floor(this.posZ))).getBlock() == this.getFavoriteBlockToPerch() ? 0.875F + this.rotationPitch / 40.0F : 2.1F);
    }

    public float getAttackAnimationScale(float partialTicks) {
        return ((float)this.clientSideAttackTime + partialTicks) / 80.0F;
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

    //Only here for initialization. Will be replaced by the values in setGargoyleType below
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(7);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(7);
    }

    public int getGargoyleType() {
        return (Integer)this.dataManager.get(TYPE);
    }

    public void setGargoyleType(int type) {
        this.dataManager.set(TYPE, type);
        switch (type) {
            case 0:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.stoneGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.stoneGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.stoneGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.stoneGargoyleKnockbackResistance);
                break;
            case 1:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.sandstoneGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.sandstoneGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.sandstoneGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.sandstoneGargoyleKnockbackResistance);
                break;
            case 2:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.obsidianGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.obsidianGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.obsidianGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.obsidianGargoyleKnockbackResistance);
                break;
            case 3:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.goldenGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.goldenGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.goldenGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.goldenGargoyleKnockbackResistance);
                break;
            case 4:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.ironGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.ironGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.ironGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.ironGargoyleKnockbackResistance);
                break;
            case 5:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.endStoneGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.endStoneGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.endStoneGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.endStoneGargoyleKnockbackResistance);
                break;
            case 6:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.netheraticGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.netheraticGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.netheraticGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.netheraticGargoyleKnockbackResistance);
                break;
            case 7:
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(GConfig.evilGargoyleHealth);
                this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(GConfig.evilGargoyleFollowRange);
                this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(GConfig.evilGargoyleMovementSpeed);
                this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(GConfig.evilGargoyleKnockbackResistance);
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("GargoyleType", 99)) {
            this.setGargoyleType(compound.getByte("GargoyleType"));
        }

        if (compound.hasUniqueId("CreatorUUID")) {
            this.creatorUUID = compound.getUniqueId("CreatorUUID");
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("GargoyleType", (byte)this.getGargoyleType());
        if (this.creatorUUID != null) {
            compound.setUniqueId("CreatorUUID", this.creatorUUID);
        }
    }

    public boolean canAttackClass(Class entityClass) {
        if (EntityGargoyle.class.isAssignableFrom(entityClass)) {
            return true;
        }

        if (this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(entityClass)) {
            return false;
        }

        return entityClass != EntityIronGolem.class;
    }

    public boolean canAttackEntity(EntityLivingBase target) {
        if (target == null || target == this || !target.isEntityAlive()) {
            return false;
        }

        //Evil gargoyles can never attack another evil gargoyle.
        if (target instanceof EntityGargoyle) {
            EntityGargoyle other = (EntityGargoyle) target;

            if (other.getGargoyleType() == 7) {
                return this.getGargoyleType() != 7;
            }

            //Evil gargoyles can attack every gargoyle (except their type) including player made ones.
            if (this.getGargoyleType() == 7) {
                return true;
            }

            //Normal gargoyles don't attack other non evil gargoyles.
            return false;
        }

        if (this.getGargoyleType() == 7 && target instanceof EntityPlayer) {
            return true;
        }

        //A normal gargoyle does not attack its creator.
        if (this.isCreator(target)) {
            return false;
        }

        //Normal gargoyles don't attack players.
        if (this.isPlayerCreated() && target instanceof EntityPlayer && this.getGargoyleType() != 7) {
            return false;
        }

        //Normal gargoyles only target hostile mobs.
        return target instanceof IMob;
    }

    protected void collideWithEntity(Entity entity) {
        if (entity instanceof EntityGargoyle && this.getAttackTarget() == null && ((EntityGargoyle)entity).getAttackTarget() == null && this.onGround && ((EntityGargoyle)entity).onGround && this.getDistanceSq(((EntityGargoyle)entity).waypointX, ((EntityGargoyle)entity).waypointY, ((EntityGargoyle)entity).waypointZ) < (double)4.0F) {
            ++this.waypointY;
            this.noClip = false;
        }

        super.collideWithEntity(entity);
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
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass())) {
                        this.setAttackTarget(entity);
                    }
                }
            }
        }

        int targetSearchChance = this.getGargoyleType() == 7
                ? GConfig.evilGargoyleTargetSearchChance
                : GConfig.gargoyleTargetSearchChance;

        if (!this.world.isRemote && this.getAttackTarget() == null) {
            List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));
            if (list != null && !list.isEmpty() && this.rand.nextInt(targetSearchChance) == 0) {
                for(int i = 0; i < list.size(); ++i) {
                    EntityLiving entity = (EntityLiving)list.get(this.rand.nextInt(list.size()));
                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass()) && this.canEntityBeSeen(entity)) {
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
                boolean favoritePerch = block == this.getFavoriteBlockToPerch();

                if ((this.ticksExisted + this.getEntityId()) % (favoritePerch ? 20 : 40) == 0) {
                    this.heal(this.getPerchHealAmount(favoritePerch));
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

            int attackCooldown = this.getGargoyleType() == 7
                    ? GConfig.evilGargoyleAttackCooldown
                    : GConfig.gargoyleAttackCooldown;

            if (this.isEntityAlive()
                    && this.getDistanceSq(entity) <= (double)(entity.width * entity.width + this.width * this.width) + 16.0D
                    && (this.ticksExisted + this.getEntityId()) % attackCooldown == 0
                    && this.canEntityBeSeen(entity)) {

                this.attackEntityAsMob(entity);
                this.getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
            }

            if (d3 > (this.getGargoyleType() == 3 ? 512.0D : 1.0D)
                    && (this.canEntityBeSeen(entity)
                    || this.isEntityInsideOpaqueBlock()
                    || this.posY <= 0.0D
                    || this.posY <= entity.posY
                    || this.rand.nextInt(10) == 0)) {

                if (this.posY <= entity.posY + (this.getGargoyleType() == 7 ? 0.5D : 1.0D)) {
                    this.motionY += (this.getGargoyleType() == 7 ? 0.6D : 0.8D) - this.motionY;
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

    private float getPerchHealAmount(boolean favoritePerch) {
        if (this.getGargoyleType() == 7) {
            return favoritePerch ? GConfig.evilGargoyleFavoritePerchHeal : GConfig.evilGargoyleNormalPerchHeal;
        }
        return favoritePerch ? GConfig.gargoyleFavoritePerchHeal : GConfig.gargoyleNormalPerchHeal;
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
                return RegistryHandler.stoneperch;
            case 1:
                return RegistryHandler.sandstoneperch;
            case 2:
                return RegistryHandler.obsidianperch;
            case 3:
                return RegistryHandler.goldperch;
            case 4:
                return RegistryHandler.ironperch;
            case 5:
                return RegistryHandler.endstoneperch;
            case 6:
                return RegistryHandler.netherbrickperch;
            case 7:
                return RegistryHandler.stoneperch;
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
                if (this.world.getBiome(this.getPosition()) == Biomes.DESERT || this.world.getBiome(this.getPosition()) == Biomes.DESERT_HILLS) {
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
                damage = GConfig.netheraticGargoyleDamage;
                break;
            case 7:
                damage = GConfig.evilGargoyleMinDamage;

                if (GConfig.evilGargoyleMaxDamage > GConfig.evilGargoyleMinDamage) {
                    damage += this.rand.nextFloat() * (GConfig.evilGargoyleMaxDamage - GConfig.evilGargoyleMinDamage);
                }
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
                    target.motionY += GConfig.netheraticGargoyleKnockUp;
                    target.setFire(GConfig.netheraticGargoyleFireTime);
                    break;
            }
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return this.getNatureBlock(this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - (double)0.5F), MathHelper.floor(this.posZ)))) ? null : RegistryHandler.GARGOYLE_LIVING;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return RegistryHandler.GARGOYLE_GRUNT;
    }

    protected SoundEvent getDeathSound() {
        return RegistryHandler.GARGOYLE_DEATH;
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
                break;
            case 7:
                for(int k = 0; k < j; ++k) {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.STONE), 1, 0.0F);
                }
        }
    }

    static {
        STATE = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);
        TARGET_ENTITY = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);
        TYPE = EntityDataManager.createKey(EntityGargoyle.class, DataSerializers.VARINT);

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
        private EntityGargoyle gargoyle = EntityGargoyle.this;
        private int tickCounter;

        public AIBeamAttack() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.gargoyle.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (entitylivingbase.attackable() || entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired());
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.gargoyle.getAttackTarget();
            return super.shouldContinueExecuting() && entitylivingbase != null && (entitylivingbase.attackable() || entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired());
        }

        public void startExecuting() {
            this.tickCounter = -10;
            if (this.gargoyle.getGargoyleType() == 3) {
                this.gargoyle.getNavigator().clearPath();
                this.gargoyle.getLookHelper().setLookPositionWithEntity(this.gargoyle.getAttackTarget(), 90.0F, 90.0F);
            }
        }

        public void resetTask() {
            this.gargoyle.setAttackTarget((EntityLivingBase)null);
            this.gargoyle.setTargetedEntity(0);
        }

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.gargoyle.getAttackTarget();
            if (this.gargoyle.canEntityBeSeen(entitylivingbase)) {
                this.gargoyle.setTargetedEntity(this.gargoyle.getAttackTarget().getEntityId());
            }

            if (this.gargoyle.getGargoyleType() == 3) {
                this.gargoyle.getNavigator().clearPath();
                this.gargoyle.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
                if (!this.gargoyle.canEntityBeSeen(entitylivingbase)) {
                    this.gargoyle.setAttackTarget((EntityLivingBase)null);
                } else {
                    ++this.tickCounter;
                    this.gargoyle.setTargetedEntity(this.gargoyle.getAttackTarget().getEntityId());
                    if (this.tickCounter > 0) {
                        entitylivingbase.attackEntityFrom(DamageSource.MAGIC, (float)(this.gargoyle.clientSideAttackTime / 80));
                        entitylivingbase.setFire(1 + this.tickCounter);
                        entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.5F, 1.0F + (float)(this.gargoyle.clientSideAttackTime / 80));
                    }

                    if (this.tickCounter % 20 == 0) {
                        entitylivingbase.hurtResistantTime = 0;
                    }

                    if (this.tickCounter >= 80) {
                        float f = 8.0F;
                        if (this.gargoyle.world.getDifficulty() == EnumDifficulty.HARD) {
                            f += 4.0F;
                        }

                        this.gargoyle.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + this.gargoyle.getRNG().nextFloat(), this.gargoyle.getRNG().nextFloat() * 0.7F + 0.3F);
                        entitylivingbase.world.newExplosion((Entity)null, entitylivingbase.posX, entitylivingbase.posY + (double)1.0F, entitylivingbase.posZ, 1.0F, true, false);
                        entitylivingbase.world.newExplosion(this.gargoyle, entitylivingbase.posX, entitylivingbase.posY + (double)entitylivingbase.getEyeHeight(), entitylivingbase.posZ, 1.0F, true, false);
                        entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.gargoyle, this.gargoyle), f);
                        this.gargoyle.attackEntityAsMob(entitylivingbase);
                        this.gargoyle.setAttackTarget((EntityLivingBase)null);
                        this.tickCounter = 0;
                        this.resetTask();
                    }
                }
                super.updateTask();
            }
        }
    }

    private final Predicate<EntityLivingBase> ATTACKABLE = new Predicate<EntityLivingBase>() {
        @Override
        public boolean apply(@Nullable EntityLivingBase entity) {
            if (entity == null || !entity.attackable()) {
                return false;
            }

            if (EntityGargoyle.this.getGargoyleType() == 7 && entity instanceof EntityPlayer || entity instanceof EntityGargoyle) {
                return true;
            }

            return entity instanceof IMob && entity.getCustomNameTag().isEmpty();
        }
    };

    class AIPerch extends EntityAIBase {
        public AIPerch() {
            this.setMutexBits(7);
        }

        public boolean shouldExecute() {
            IBlockState blockmain = EntityGargoyle.this.world.getBlockState(new BlockPos((int)EntityGargoyle.this.waypointX, (int)EntityGargoyle.this.waypointY, (int)EntityGargoyle.this.waypointZ));
            return EntityGargoyle.this.getNatureBlock(blockmain) && EntityGargoyle.this.getAttackTarget() == null;
        }
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return this.getGargoyleType() == 7 ? GConfig.evilGargoyleExperience : GConfig.gargoyleExperience;
    }

    public void setCreator(EntityPlayer player) {
        this.creatorUUID = player.getUniqueID();
        this.setPlayerCreated(true);
    }

    @Nullable
    public UUID getCreatorUUID() {
        return this.creatorUUID;
    }

    public boolean isCreator(EntityLivingBase entity) {
        return entity != null && this.creatorUUID != null && this.creatorUUID.equals(entity.getUniqueID());
    }
}