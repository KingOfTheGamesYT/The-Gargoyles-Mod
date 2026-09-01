package net.minecraft.gargoyles;

import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.SoundEvents;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

@Mod(modid = "gargoyles", name = "Gargoyles Mod", version = "1.0.2", acceptedMinecraftVersions = "[1.12.2]")
public class Gargoyles {
    public static final String MODID = "gargoyles";
    @SidedProxy(clientSide = "net.minecraft.gargoyles.ClientProxy", serverSide = "net.minecraft.gargoyles.CommonProxy")
    public static CommonProxy proxy;
    @Metadata
    public static ModMetadata meta;
    @Instance("gargoyles")
    public static Gargoyles modInstance;
    public static WorldGen gen = new WorldGen();

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        EntityParrot.registerMimicSound(EntityGargoyle.class, RegistryHandler.GARGOYLE_LIVING);
        EntityParrot.registerMimicSound(EntityIronGolem.class, SoundEvents.ENTITY_IRONGOLEM_HURT);
        EntityParrot.registerMimicSound(EntitySnowman.class, SoundEvents.ENTITY_SNOWMAN_HURT);
        File configFile = new File(e.getModConfigurationDirectory(), MODID + ".cfg");
        GConfig.init(configFile);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        GameRegistry.registerWorldGenerator(gen, 10000);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}