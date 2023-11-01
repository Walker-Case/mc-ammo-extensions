package com.walkercase.ae.enchantment;

import com.walkercase.ae.AEMain;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AEEnchantments {
    public static final DeferredRegister<Enchantment> REGISTER;

    public AEEnchantments() {
    }

    static {
        REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AEMain.MODID);

        FRACTURE = REGISTER.register("fracture", FractureGunEnchantment::new);
    }

    public static final RegistryObject<Enchantment> FRACTURE;
}
