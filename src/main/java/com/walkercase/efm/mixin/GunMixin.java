package com.walkercase.efm.mixin;

import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.item.EFMAmmoWrapper;
import com.walkercase.efm.item.Magazine;
import com.walkercase.efm.util.EFMNBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.security.auth.callback.Callback;

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
        for(int i=0;i<player.getInventory().getContainerSize();i++){
            ItemStack is = player.getInventory().getItem(i);
            if(is.getItem() instanceof Magazine magazine){
                if(magazine.companionWeapon.get() == player.getMainHandItem().getItem()){
                    callback.setReturnValue(new AmmoContext(is, player.isCreative() ? null : player.getInventory()));
                    return;
                }
            }

            if(is.getItem() instanceof EFMAmmoWrapper ammo){
                //If equal
                if(ammo.baseAmmo.compareTo(baseID) == 0){
                    callback.setReturnValue(new AmmoContext(is, player.isCreative() ? null : player.getInventory()));
                    return;
                }
            }
        }
    }
}
