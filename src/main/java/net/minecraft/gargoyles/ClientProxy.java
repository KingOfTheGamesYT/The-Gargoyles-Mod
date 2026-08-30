package net.minecraft.gargoyles;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderGargoyle;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGargoyle;

import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        RegistryHandler.registerRenders();
    }

    public void registerRenderThings() {
        registerGargoyle(EntityGargoyle.class);
    }

    public static void registerGargoyle(Class<? extends EntityGargoyle> mob) {
        RenderingRegistry.registerEntityRenderingHandler(mob, new IRenderFactory() {
            public Render<? super EntityGargoyle> createRenderFor(RenderManager manager) {
                return new RenderGargoyle(manager);
            }
        });
    }
}