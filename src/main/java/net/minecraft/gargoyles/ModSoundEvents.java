package net.minecraft.gargoyles;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import net.minecraftforge.registries.GameData;

public class ModSoundEvents {
    public static SoundEvent gargoyleLiving;
    public static SoundEvent gargoyleGrunt;
    public static SoundEvent gargoyleDeath;

    public static void registerSounds() {
        gargoyleLiving = registerSound("gargoyleLiving");
        gargoyleGrunt = registerSound("gargoyleGrunt");
        gargoyleDeath = registerSound("gargoyleDeath");
    }

    public static SoundEvent registerSound(String soundName) {
        ResourceLocation soundID = new ResourceLocation("gargoyles", soundName);
        return (SoundEvent)GameData.register_impl((new SoundEvent(soundID)).setRegistryName(soundID));
    }
}