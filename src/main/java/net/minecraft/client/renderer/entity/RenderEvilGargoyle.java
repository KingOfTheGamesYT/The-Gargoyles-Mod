package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEvilGargoyle;
import net.minecraft.client.renderer.entity.layers.LayerEvilGargoyleEyes;
import net.minecraft.entity.monster.EntityEvilGargoyle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvilGargoyle extends RenderLiving<EntityEvilGargoyle> {
    private static final ResourceLocation stoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle8.png");

    public RenderEvilGargoyle(RenderManager renderManager) {
        super(renderManager, new ModelEvilGargoyle(), 0.8F);
        this.addLayer(new LayerEvilGargoyleEyes(this));
    }

    protected ResourceLocation getEntityTexture(EntityEvilGargoyle entity) {
        return stoneGargoyleTextures;
    }
}