package com.walkercase.ae.mixin;

import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.walkercase.ae.item.AEItems;
import com.walkercase.ae.item.Magazine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gun.class)
public class GunMixin {

    @Inject(method = "hasAmmo", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void hasAmmo(ItemStack gunStack, CallbackInfoReturnable<Boolean> callback) {
        ItemStack mag = Magazine.getCurrentMagazineForGun(gunStack);
        if(mag != null && Magazine.getTopBullet(mag) != null)
            callback.setReturnValue(true);
    }

    @Inject(method = "findAmmo", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void findAmmo(Player player, ResourceLocation baseID, CallbackInfoReturnable<AmmoContext> callback) {
        if(AEItems.itemIgnored(player.getMainHandItem().getItem()))
            return;
        for(int i=0;i<player.getInventory().getContainerSize();i++){
            ItemStack is = player.getInventory().getItem(i);
            if(is.getItem() instanceof Magazine magazine){
                if(magazine.matchesWeapon(player.getMainHandItem().getItem())){
                    //If empty, skip
                    if(Magazine.getLoadedAmmo(is).length > 0)
                        callback.setReturnValue(new AmmoContext(is, player.isCreative() ? null : player.getInventory()));
                    return;
                }
            }
        }
        callback.setReturnValue(new AmmoContext(ItemStack.EMPTY, player.isCreative() ? null : player.getInventory()));
    }
}
