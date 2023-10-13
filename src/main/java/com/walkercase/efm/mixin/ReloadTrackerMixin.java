package com.walkercase.efm.mixin;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ReloadTracker;
import com.mrcrayfish.guns.common.ShootTracker;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.item.Magazine;
import com.walkercase.efm.util.EFMNBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ReloadTracker.class)
public class ReloadTrackerMixin{

    @Final
    @Shadow
    private static Map<Player, ReloadTracker> RELOAD_TRACKER_MAP;

    @Inject(method = "increaseAmmo", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void onReload(Player player, CallbackInfo callback){
        ItemStack stack = player.getMainHandItem();
        Gun gun = ((GunItem)stack.getItem()).getModifiedGun(stack);

        AmmoContext context = Gun.findAmmo(player, gun.getProjectile().getItem());

        if(context.stack().getItem() instanceof Magazine magazine){
            if(magazine.matchesWeapon((GunItem)stack.getItem())){
                ItemStack old = Magazine.setCurrentMagazine(stack, context.stack());
                if(old != null)
                    player.getInventory().add(old);
                player.getInventory().removeItem(context.stack());

                Container container = context.container();
                if (container != null) {
                    container.setChanged();
                }

                stack.setDamageValue(Magazine.getLoadedAmmo(context.stack()).length);

                CompoundTag tagCompound = stack.getOrCreateTag();
                tagCompound.putInt("AmmoCount", Magazine.getLoadedAmmo(context.stack()).length);

                ReloadTracker tracker = (ReloadTracker)RELOAD_TRACKER_MAP.get(player);
                RELOAD_TRACKER_MAP.remove(player);
                ModSyncedDataKeys.RELOADING.setValue(player, false);
            }
        }else{
            ItemStack ammo = context.stack();
            if (!ammo.isEmpty()) {
                int amount = Math.min(ammo.getCount(), gun.getGeneral().getReloadAmount());
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    int maxAmmo = GunEnchantmentHelper.getAmmoCapacity(stack, gun);
                    amount = Math.min(amount, maxAmmo - tag.getInt("AmmoCount"));
                    tag.putInt("AmmoCount", tag.getInt("AmmoCount") + amount);

                    stack.setDamageValue(stack.getDamageValue() + amount);
                }

                ammo.shrink(amount);
                Container container = context.container();
                if (container != null) {
                    container.setChanged();
                }
            }
        }

        ResourceLocation reloadSound = gun.getSounds().getReload();
        if (reloadSound != null) {
            double radius = (Double) Config.SERVER.reloadMaxDistance.get();
            double soundX = player.getX();
            double soundY = player.getY() + 1.0;
            double soundZ = player.getZ();
            S2CMessageGunSound message = new S2CMessageGunSound(reloadSound, SoundSource.PLAYERS, (float)soundX, (float)soundY, (float)soundZ, 1.0F, 1.0F, player.getId(), false, true);
            PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> {
                return LevelLocation.create(player.level, soundX, soundY, soundZ, radius);
            }, message);
        }



        callback.cancel();
    }
}
