package net.minecraft.gargoyles;

import net.minecraft.entity.monster.EntityEvilGargoyle;
import net.minecraft.entity.monster.EntityGargoyle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class RenderTheGargoyle {
    private static int id = 0;

    public static void registerEntity() {
        String entityName1 = "gargoyle";
        String entityName2 = "gargoyle_evil";
        createEntity(EntityGargoyle.class, entityName1);
        createEntity(EntityEvilGargoyle.class, entityName2);
        createEgg(entityName1, 11316396, 10526880);
        createEgg(entityName2, 10526880, 6579300);
        createVanillaEgg("giant", 44975, 7969893);
        createVanillaEgg("illusion_illager", 1267859, 9804699);
        createVanillaEgg("snowman", 15663103, 14913565);
        createVanillaEgg("villager_golem", 13288125, 13680304);
    }

    public static void createEntity(Class entityClass, String entityName) {
        ResourceLocation resource = new ResourceLocation("gargoyles", entityName);
        EntityRegistry.registerModEntity(resource, entityClass, entityName, ++id, Gargoyles.modInstance, 256, 1, true);
    }

    private static void createVanillaEgg(String entityName, int solidColor, int spotColor) {
        EntityRegistry.registerEgg(new ResourceLocation(entityName), solidColor, spotColor);
    }

    private static void createEgg(String entityName, int solidColor, int spotColor) {
        EntityRegistry.registerEgg(new ResourceLocation("gargoyles", entityName), solidColor, spotColor);
    }
}
