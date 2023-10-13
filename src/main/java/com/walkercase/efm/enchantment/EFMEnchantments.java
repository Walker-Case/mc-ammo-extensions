package com.walkercase.efm.enchantment;

import com.walkercase.efm.EFMMain;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EFMEnchantments {
    public static final DeferredRegister<Enchantment> REGISTER;

    public EFMEnchantments() {
    }

    static {
        REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, EFMMain.MODID);

        FRACTURE = REGISTER.register("fracture", FractureGunEnchantment::new);
    }

    public static final RegistryObject<Enchantment> FRACTURE;
}
