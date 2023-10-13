package com.walkercase.efm.mixin;

import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.efm.item.Magazine;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getMaxDamage", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void getMaxDamage( CallbackInfoReturnable<Integer> callback) {
        ItemStack is = ((ItemStack)(Object)this);
        if(is != null && is.getItem() instanceof GunItem){
            ItemStack magazine = Magazine.getCurrentMagazineForGun(is);
            if(magazine != null){
                callback.setReturnValue(((Magazine)magazine.getItem()).size);
            }
        }
    }

}
