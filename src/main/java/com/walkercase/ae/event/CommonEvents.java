package com.walkercase.ae.event;

import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.ae.AEMain;
import com.walkercase.ae.item.AEItems;
import com.walkercase.ae.item.Magazine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AEMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class CommonEvents {

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event){
        Inventory inv = event.player.getInventory();

        //Set all guns without a magazine to 0 ammo.
        for(int i=0;i<inv.getContainerSize();i++){
            if(inv.getItem(i).getItem() instanceof GunItem gunItem){
                if(!AEItems.itemIgnored(inv.getItem(i).getItem())){
                    if(Magazine.getCurrentMagazineForGun(inv.getItem(i)) == null){
                        gunItem.setDamage(inv.getItem(i), 0);
                        CompoundTag tagCompound = inv.getItem(i).getOrCreateTag();
                        tagCompound.putInt("AmmoCount", 0);
                    }
                }
            }
        }
    }
}
