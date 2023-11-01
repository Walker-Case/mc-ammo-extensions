package com.walkercase.ae.mixin;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.walkercase.ae.item.EnchantmentWrapper;
import com.walkercase.ae.item.Magazine;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GunEnchantmentHelper.class)
public class GunEnchantmentHelperMixin {

    @Inject(method = "getReloadInterval", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getReloadInterval(ItemStack is, CallbackInfoReturnable<Integer> callback){
        if(is != null && is.getItem() instanceof GunItem){

            ItemStack magazine = Magazine.getCurrentMagazineForGun(is);
            if(magazine != null){
                EnchantmentWrapper[] enchantments = ((Magazine)magazine.getItem()).enchantments;
                int interval = 10;
                for(EnchantmentWrapper ench : enchantments){
                    if(ench.enchantment == ModEnchantments.QUICK_HANDS){
                        interval -= 3 * ench.level;
                    }
                }

                callback.setReturnValue(Math.max(interval, 1));
            }
        }
    }

    @Inject(method = "getAmmoCapacity", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void getAmmoCapacity(ItemStack is, Gun modifiedGun, CallbackInfoReturnable<Integer> callback) {
        if(is != null && is.getItem() instanceof GunItem){
            ItemStack magazine = Magazine.getCurrentMagazineForGun(is);
            if(magazine != null){
                callback.setReturnValue(((Magazine)magazine.getItem()).size);
            }
        }
    }
}
