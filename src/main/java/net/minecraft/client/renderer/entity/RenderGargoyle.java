package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGargoyle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.layers.LayerGargoyleEyes;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGargoyle extends RenderLiving<EntityGargoyle> {
    private static final ResourceLocation stoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle1.png");
    private static final ResourceLocation sandstoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle2.png");
    private static final ResourceLocation obsidianGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle3.png");
    private static final ResourceLocation goldenGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle4.png");
    private static final ResourceLocation ironGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle5.png");
    private static final ResourceLocation endstoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle6.png");
    private static final ResourceLocation nethraticGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle7.png");
    private static final ResourceLocation gargoyleBeamTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle_beam.png");

    public RenderGargoyle(RenderManager renderManager) {
        super(renderManager, new ModelGargoyle(), 0.8F);
        this.addLayer(new LayerGargoyleEyes(this));
    }

    protected ResourceLocation getEntityTexture(EntityGargoyle entity) {
        switch (entity.getGargoyleType()) {
            case 0:
            default:
                return stoneGargoyleTextures;
            case 1:
                return sandstoneGargoyleTextures;
            case 2:
                return obsidianGargoyleTextures;
            case 3:
                return goldenGargoyleTextures;
            case 4:
                return ironGargoyleTextures;
            case 5:
                return endstoneGargoyleTextures;
            case 6:
                return nethraticGargoyleTextures;
        }
    }

    protected void applyRotations(EntityGargoyle p_180588_1_, float p_180588_2_, float p_180588_3_, float p_180588_4_) {
        super.applyRotations(p_180588_1_, p_180588_2_, p_180588_3_, p_180588_4_);
        if (p_180588_1_.getGargoyleType() == 1) {
            GL11.glScalef(0.75F, 1.0F, 0.75F);
        }

        if (p_180588_1_.getGargoyleType() == 2) {
            GL11.glScalef(1.25F, 1.0F, 1.25F);
        }

        if (p_180588_1_.getGargoyleType() == 3) {
            GL11.glScalef(1.1F, 1.0F, 1.1F);
        }

        if (p_180588_1_.getGargoyleType() == 4) {
            GL11.glScalef(1.2F, 1.0F, 1.2F);
        }

        if (p_180588_1_.getGargoyleType() == 5) {
            GL11.glScalef(1.1F, 1.1F, 1.1F);
        }

        if (p_180588_1_.getGargoyleType() == 6) {
            GL11.glScalef(1.05F, 0.95F, 1.05F);
        }

    }

    private Vec3d getPosition(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }

    public void doRender(EntityGargoyle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        EntityLivingBase entitylivingbase = entity.getTargetedEntity();
        if (entity.getGargoyleType() == 3 && entitylivingbase != null) {
            float f = entity.getAttackAnimationScale(partialTicks);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(gargoyleBeamTextures);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            int i1 = 15728880;
            int j1 = i1 % 65536;
            int k1 = i1 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j1, (float)k1);
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
            float f2 = (float)entity.world.getTotalWorldTime() + partialTicks;
            float f3 = f2 * 0.5F % 1.0F;
            float f4 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + f4, (float)z);
            Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase instanceof EntityDragon ? (double)2.5F : (double)entitylivingbase.getEyeHeight(), partialTicks);
            Vec3d vec3d1 = this.getPosition(entity, (double)f4, partialTicks);
            Vec3d vec3d2 = vec3d.subtract(vec3d1);
            double d0 = vec3d2.lengthVector() + (double)1.0F;
            vec3d2 = vec3d2.normalize();
            float f5 = (float)Math.acos(vec3d2.y);
            float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
            GlStateManager.rotate((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            int i = 1;
            double d1 = (double)f2 * 0.05 * (double)-1.5F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            float f7 = f * f;
            int j = 255 + (int)(f7 * 255.0F);
            int k = 255 + (int)(f7 * 255.0F);
            int l = 255 - (int)(f7 * 255.0F);
            double d2 = 0.2;
            double d3 = 0.282;
            double d4 = (double)0.0F + Math.cos(d1 + (double)2.25F) * (double)0.25F;
            double d5 = (double)0.0F + Math.sin(d1 + (double)2.25F) * (double)0.25F;
            double d6 = (double)0.0F + Math.cos(d1 + (Math.PI / 4D)) * (double)0.25F;
            double d7 = (double)0.0F + Math.sin(d1 + (Math.PI / 4D)) * (double)0.25F;
            double d8 = (double)0.0F + Math.cos(d1 + 3.9) * (double)0.25F;
            double d9 = (double)0.0F + Math.sin(d1 + 3.9) * (double)0.25F;
            double d10 = (double)0.0F + Math.cos(d1 + (double)5.5F) * (double)0.25F;
            double d11 = (double)0.0F + Math.sin(d1 + (double)5.5F) * (double)0.25F;
            double d12 = (double)0.0F + Math.cos(d1 + Math.PI) * 0.2;
            double d13 = (double)0.0F + Math.sin(d1 + Math.PI) * 0.2;
            double d14 = (double)0.0F + Math.cos(d1 + (double)0.0F) * 0.2;
            double d15 = (double)0.0F + Math.sin(d1 + (double)0.0F) * 0.2;
            double d16 = (double)0.0F + Math.cos(d1 + (Math.PI / 2D)) * 0.2;
            double d17 = (double)0.0F + Math.sin(d1 + (Math.PI / 2D)) * 0.2;
            double d18 = (double)0.0F + Math.cos(d1 + (Math.PI * 1.5D)) * 0.2;
            double d19 = (double)0.0F + Math.sin(d1 + (Math.PI * 1.5D)) * 0.2;
            double d20 = (double)0.0F;
            double d21 = (double)0.5F;
            double d22 = (double)(-1.0F + f3);
            double d23 = d0 * (double)2.5F + d22;
            bufferbuilder.pos(d12, d0, d13).tex((double)0.5F, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d12, (double)0.0F, d13).tex((double)0.5F, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, (double)0.0F, d15).tex((double)0.0F, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d14, d0, d15).tex((double)0.0F, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, d0, d17).tex((double)0.5F, d23).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d16, (double)0.0F, d17).tex((double)0.5F, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, (double)0.0F, d19).tex((double)0.0F, d22).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d18, d0, d19).tex((double)0.0F, d23).color(j, k, l, 255).endVertex();
            double d24 = (double)0.0F;
            if (entity.ticksExisted % 2 == 0) {
                d24 = (double)0.5F;
            }

            bufferbuilder.pos(d4, d0, d5).tex((double)0.5F, d24 + (double)0.5F).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d6, d0, d7).tex((double)1.0F, d24 + (double)0.5F).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d10, d0, d11).tex((double)1.0F, d24).color(j, k, l, 255).endVertex();
            bufferbuilder.pos(d8, d0, d9).tex((double)0.5F, d24).color(j, k, l, 255).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

    }
}
