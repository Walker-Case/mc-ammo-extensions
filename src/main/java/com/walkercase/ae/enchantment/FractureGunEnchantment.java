package com.walkercase.ae.enchantment;

import com.mrcrayfish.guns.enchantment.EnchantmentTypes;
import com.mrcrayfish.guns.enchantment.GunEnchantment;
import net.minecraft.world.entity.EquipmentSlot;

public class FractureGunEnchantment extends GunEnchantment {
    public FractureGunEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTypes.GUN, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.PROJECTILE);
    }

    public int getMinCost(int level) {
        return 15;
    }

    public int getMaxCost(int level) {
        return super.getMinCost(level) + 30;
    }
}
