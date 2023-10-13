package com.walkercase.efm.event;

import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.entities.EFMEntities;
import com.walkercase.efm.entities.RaiderEntity;
import com.walkercase.efm.item.Magazine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class CommonEvents {
    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event){
        event.put(EFMEntities.RAIDER_ENTITY.get(), RaiderEntity.getRaiderAttributes().build());
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event){
        Inventory inv = event.player.getInventory();

        //Set all guns without a magazine to 0 ammo.
        for(int i=0;i<inv.getContainerSize();i++){
            if(inv.getItem(i).getItem() instanceof GunItem gunItem){
                if(Magazine.getCurrentMagazineForGun(inv.getItem(i)) == null){
                    gunItem.setDamage(inv.getItem(i), 0);
                    CompoundTag tagCompound = inv.getItem(i).getOrCreateTag();
                    tagCompound.putInt("AmmoCount", 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void spawnPlacement(SpawnPlacementRegisterEvent event){
        event.register(EFMEntities.RAIDER_ENTITY.get(), RaiderEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
