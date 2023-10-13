package com.walkercase.efm.item;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

public class EFMAmmoEnchant{
    public RegistryObject<Enchantment> enchantment;
    public int level;

    public EFMAmmoEnchant(RegistryObject<Enchantment> enchantment, int i) {
        this.enchantment = enchantment;
        this.level = i;
    }
}