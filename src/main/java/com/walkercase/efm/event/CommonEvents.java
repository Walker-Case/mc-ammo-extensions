package com.walkercase.efm.event;

import com.walkercase.efm.EFMMain;
import com.walkercase.efm.entities.EFMEntities;
import com.walkercase.efm.entities.RaiderEntity;
import net.minecraftforge.api.distmarker.Dist;
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
    public static void spawnPlacement(SpawnPlacementRegisterEvent event){
        event.register(EFMEntities.RAIDER_ENTITY.get(), RaiderEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
