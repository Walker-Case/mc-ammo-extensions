package com.walkercase.efm.entities;

import com.walkercase.efm.EFMMain;
import com.walkercase.efm.enchantment.FractureGunEnchantment;
import com.walkercase.efm.item.EFMItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EFMEntities {
    public static final DeferredRegister<EntityType<?>> REGISTER;

    public EFMEntities() {
    }

    static {
        REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EFMMain.MODID);


        RAIDER_ENTITY = REGISTER.register("raider",
                () -> EntityType.Builder.of(RaiderEntity::new, MobCategory.MISC).sized(1.0f, 2.0f)
                        .build(new ResourceLocation(EFMMain.MODID, "raider").toString()));
    }



    public static final RegistryObject<EntityType<RaiderEntity>> RAIDER_ENTITY;
}
