package net.minecraft.gargoyles;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void registerRenders() {
    }

    public void preInit(FMLPreInitializationEvent e) {
        GargoyleBlocks.init();
        this.registerRenderThings();
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    public void registerRenderThings() {
    }
}