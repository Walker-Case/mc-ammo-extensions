package com.walkercase.ae.mixin;

import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.ae.item.AEItems;
import com.walkercase.ae.item.Magazine;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    //https://fabricmc.net/wiki/tutorial:mixin_injects
    @Inject(method = "getMaxDamage", at = @At(value = "HEAD", target = "Lnet/minecraft/world/item/ItemStack;getMaxDamage(I)V"), cancellable = true)
    public void getMaxDamage( CallbackInfoReturnable<Integer> callback) {
        ItemStack is = ((ItemStack)(Object)this);
        if(is != null && is.getItem() instanceof GunItem){
            ItemStack magazine = Magazine.getCurrentMagazineForGun(is);
            if(!AEItems.itemIgnored(is.getItem()) && magazine != null){
                callback.setReturnValue(((Magazine)magazine.getItem()).size);
            }
        }
    }

}
