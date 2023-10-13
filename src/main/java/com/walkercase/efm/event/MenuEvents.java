package com.walkercase.efm.event;

import com.walkercase.efm.EFMMain;
import com.walkercase.efm.item.EFMAmmoWrapper;
import com.walkercase.efm.item.Magazine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class MenuEvents {
    @SubscribeEvent
    public static void handleReloadMagInInventory(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack inSlot = event.getStackedOnItem();

        if(carried.getItem() instanceof EFMAmmoWrapper wrapper){
            if(inSlot.getItem() instanceof Magazine magazine){
                //If magazine gun ammo and wrapper ammo are equal.
                ResourceLocation magazineGunAmmo = magazine.companionWeapon.get().getGun().getProjectile().getItem();

                if(magazineGunAmmo.compareTo(wrapper.baseAmmo) == 0){

                    //If the ammo can be placed in the magazine
                    if(Magazine.putBullet(event.getStackedOnItem(), event.getCarriedItem().getItem())){
                        carried.setCount(carried.getCount()-1);

                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
