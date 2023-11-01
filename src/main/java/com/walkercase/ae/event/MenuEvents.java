package com.walkercase.ae.event;

import com.walkercase.ae.AEMain;
import com.walkercase.ae.item.AEAmmoWrapper;
import com.walkercase.ae.item.Magazine;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AEMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class MenuEvents {
    @SubscribeEvent
    public static void handleReloadMagInInventory(ItemStackedOnOtherEvent event){
        ItemStack carried = event.getCarriedItem();
        ItemStack inSlot = event.getStackedOnItem();

        if(carried.getItem() instanceof AEAmmoWrapper wrapper){
            if(inSlot.getItem() instanceof Magazine magazine){
                //If magazine gun ammo and wrapper ammo are equal.
                if(magazine.matchesAmmo(wrapper.baseAmmo)){

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
