package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEvilGargoyle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ModelEvilGargoyle extends ModelBase {
    public ModelRenderer Torso;
    public ModelRenderer Body;
    public ModelRenderer LeftLeg;
    public ModelRenderer RightLeg;
    public ModelRenderer Head;
    public ModelRenderer RightArm;
    public ModelRenderer LeftArm;
    public ModelRenderer RightWing1;
    public ModelRenderer LeftWing1;
    public ModelRenderer RightHorn;
    public ModelRenderer LeftHorn;
    public ModelRenderer Nose;
    public ModelRenderer RightArmShoulder;
    public ModelRenderer LeftArm_1;
    public ModelRenderer RightWing2;
    public ModelRenderer RightWingSkin1;
    public ModelRenderer RightWingSkin2;
    public ModelRenderer LeftWing2;
    public ModelRenderer LeftWingSkin1;
    public ModelRenderer LeftWingSkin2;

    public ModelEvilGargoyle() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.RightArm = new ModelRenderer(this, 68, 0);
        this.RightArm.setRotationPoint(-9.0F, -7.0F, 0.0F);
        this.RightArm.addBox(-5.0F, -2.0F, -3.0F, 6, 20, 6, 0.0F);
        this.LeftArm = new ModelRenderer(this, 68, 0);
        this.LeftArm.mirror = true;
        this.LeftArm.setRotationPoint(9.0F, -7.0F, 0.0F);
        this.LeftArm.addBox(-1.0F, -2.0F, -3.0F, 6, 20, 6, 0.0F);
        this.RightWing1 = new ModelRenderer(this, 0, 47);
        this.RightWing1.setRotationPoint(-6.0F, -8.0F, 3.0F);
        this.RightWing1.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.setRotateAngle(this.RightWing1, 0.34906584F, 0.34906584F, 0.34906584F);
        this.RightArmShoulder = new ModelRenderer(this, 68, 0);
        this.RightArmShoulder.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArmShoulder.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(this.RightArmShoulder, -0.0F, ((float)Math.PI / 3F), ((float)Math.PI / 3F));
        this.LeftArm_1 = new ModelRenderer(this, 68, 0);
        this.LeftArm_1.mirror = true;
        this.LeftArm_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftArm_1.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(this.LeftArm_1, 0.0F, (-(float)Math.PI / 3F), (-(float)Math.PI / 3F));
        this.LeftWingSkin2 = new ModelRenderer(this, 0, 61);
        this.LeftWingSkin2.mirror = true;
        this.LeftWingSkin2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftWingSkin2.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.Torso = new ModelRenderer(this, 0, 36);
        this.Torso.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.Torso.addBox(-5.0F, -5.0F, -3.0F, 10, 5, 6, 0.5F);
        this.RightWingSkin2 = new ModelRenderer(this, 0, 61);
        this.RightWingSkin2.mirror = true;
        this.RightWingSkin2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightWingSkin2.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.LeftLeg = new ModelRenderer(this, 46, 0);
        this.LeftLeg.mirror = true;
        this.LeftLeg.setRotationPoint(5.0F, 0.0F, 0.0F);
        this.LeftLeg.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 5, 0.0F);
        this.RightWing2 = new ModelRenderer(this, 0, 47);
        this.RightWing2.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.RightWing2.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.setRotateAngle(this.RightWing2, 0.0F, 0.0F, -0.17453292F);
        this.LeftHorn = new ModelRenderer(this, 0, 0);
        this.LeftHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftHorn.addBox(1.0F, -12.0F, -3.0F, 2, 4, 2, 0.0F);
        this.RightWingSkin1 = new ModelRenderer(this, 0, 53);
        this.RightWingSkin1.mirror = true;
        this.RightWingSkin1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightWingSkin1.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.RightLeg = new ModelRenderer(this, 46, 0);
        this.RightLeg.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.RightLeg.addBox(-3.0F, 0.0F, -3.0F, 6, 14, 5, 0.0F);
        this.Nose = new ModelRenderer(this, 0, 0);
        this.Nose.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Nose.addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, 0.0F);
        this.LeftWingSkin1 = new ModelRenderer(this, 0, 53);
        this.LeftWingSkin1.mirror = true;
        this.LeftWingSkin1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftWingSkin1.addBox(-12.0F, 0.0F, 0.0F, 12, 8, 0, 0.0F);
        this.Body = new ModelRenderer(this, 0, 16);
        this.Body.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.Body.addBox(-9.0F, -10.0F, -5.0F, 18, 10, 10, 0.0F);
        this.RightHorn = new ModelRenderer(this, 0, 0);
        this.RightHorn.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightHorn.addBox(-3.0F, -12.0F, -3.0F, 2, 4, 2, 0.0F);
        this.LeftWing1 = new ModelRenderer(this, 0, 47);
        this.LeftWing1.mirror = true;
        this.LeftWing1.setRotationPoint(6.0F, -8.0F, 3.0F);
        this.LeftWing1.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.setRotateAngle(this.LeftWing1, -0.34906584F, 2.7925267F, -0.34906584F);
        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, -10.0F, -2.0F);
        this.Head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.LeftWing2 = new ModelRenderer(this, 0, 47);
        this.LeftWing2.mirror = true;
        this.LeftWing2.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.LeftWing2.addBox(-12.0F, -1.5F, -1.5F, 12, 3, 3, 0.0F);
        this.setRotateAngle(this.LeftWing2, 0.0F, 0.0F, -0.17453292F);
        this.Body.addChild(this.RightArm);
        this.Body.addChild(this.LeftArm);
        this.Body.addChild(this.RightWing1);
        this.RightArm.addChild(this.RightArmShoulder);
        this.LeftArm.addChild(this.LeftArm_1);
        this.LeftWing2.addChild(this.LeftWingSkin2);
        this.RightWing2.addChild(this.RightWingSkin2);
        this.Torso.addChild(this.LeftLeg);
        this.RightWing1.addChild(this.RightWing2);
        this.Head.addChild(this.LeftHorn);
        this.RightWing1.addChild(this.RightWingSkin1);
        this.Torso.addChild(this.RightLeg);
        this.Head.addChild(this.Nose);
        this.LeftWing1.addChild(this.LeftWingSkin1);
        this.Torso.addChild(this.Body);
        this.Head.addChild(this.RightHorn);
        this.Body.addChild(this.LeftWing1);
        this.Body.addChild(this.Head);
        this.LeftWing1.addChild(this.LeftWing2);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Torso.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        EntityEvilGargoyle entityirongolem = (EntityEvilGargoyle)p_78086_1_;
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        this.Torso.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.setRotateAngle(this.RightWing1, 0.35F, 0.35F, 0.35F);
        this.setRotateAngle(this.LeftWing1, -0.35F, 2.8F, -0.35F);
        this.setRotateAngle(this.RightWing2, 0.0F, 0.0F, -0.175F);
        this.setRotateAngle(this.LeftWing2, 0.0F, 0.0F, -0.175F);
        float f6 = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
        this.Head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.Head.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
        this.RightArm.rotateAngleZ = 0.0F;
        this.LeftArm.rotateAngleZ = 0.0F;
        this.RightArm.rotateAngleX = 0.0F;
        this.LeftArm.rotateAngleX = 0.0F;
        this.RightLeg.rotateAngleX = -1.5F * this.triangleWave(p_78087_1_, 13.0F) * p_78087_2_;
        this.LeftLeg.rotateAngleX = 1.5F * this.triangleWave(p_78087_1_, 13.0F) * p_78087_2_;
        EntityEvilGargoyle entityirongolem = (EntityEvilGargoyle)p_78087_7_;
        int i = entityirongolem.getAttackTimer();
        boolean flag = entityirongolem.world.getBlockState(new BlockPos(MathHelper.floor(entityirongolem.posX), MathHelper.floor(entityirongolem.getEntityBoundingBox().minY - (double)0.5F), MathHelper.floor(entityirongolem.posZ))).getBlock() == entityirongolem.getFavoriteBlockToPerch();
        if (i > 0) {
            this.RightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - p_78087_6_, 10.0F);
            this.LeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - p_78087_6_, 10.0F);
        } else if (entityirongolem.onGround) {
            this.RightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(p_78087_1_, 13.0F)) * p_78087_2_;
            this.LeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(p_78087_1_, 13.0F)) * p_78087_2_;
        }

        this.RightLeg.rotateAngleY = 0.0F;
        this.LeftLeg.rotateAngleY = 0.0F;
        if (!entityirongolem.onGround) {
            ModelRenderer var10000 = this.RightWing1;
            var10000.rotateAngleY += MathHelper.cos(p_78087_3_ * 0.4F) * 0.5F;
            var10000 = this.LeftWing1;
            var10000.rotateAngleY -= MathHelper.cos(p_78087_3_ * 0.4F) * 0.5F;
            var10000 = this.RightWing2;
            var10000.rotateAngleY += MathHelper.cos(p_78087_3_ * 0.4F - 1.5F);
            var10000 = this.LeftWing2;
            var10000.rotateAngleY -= MathHelper.cos(p_78087_3_ * 0.4F - 1.5F);
            --this.Head.rotateAngleX;
            var10000 = this.RightArm;
            var10000.rotateAngleX -= 2.5F - (f6 * 1.2F - f7 * 0.4F);
            var10000 = this.LeftArm;
            var10000.rotateAngleX -= 2.5F - (f6 * 1.2F - f7 * 0.4F);
            this.RightLeg.rotateAngleX = 0.0F;
            this.LeftLeg.rotateAngleX = 0.0F;
        }

        if (flag) {
            ModelRenderer var18 = this.RightWing1;
            var18.rotateAngleY += 0.5F;
            var18 = this.LeftWing1;
            var18.rotateAngleY -= 0.5F;
            var18 = this.RightWing2;
            var18.rotateAngleY += -1.5F;
            var18 = this.LeftWing2;
            var18.rotateAngleY -= -1.5F;
            this.RightArm.rotateAngleX = -0.75F;
            this.LeftArm.rotateAngleX = -0.75F;
            var18 = this.RightArm;
            var18.rotateAngleZ -= 0.5F;
            var18 = this.LeftArm;
            var18.rotateAngleZ += 0.5F;
            this.RightLeg.rotateAngleX = -0.8F;
            this.LeftLeg.rotateAngleX = -0.8F;
            this.Torso.rotationPointY = 16.0F;
            this.Torso.rotateAngleX = 1.25F;
            --this.Head.rotateAngleX;
        } else {
            this.Torso.rotateAngleX = !entityirongolem.onGround ? 1.25F : 0.0F;
        }

    }

    private float triangleWave(float p_78172_1_, float p_78172_2_) {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}
