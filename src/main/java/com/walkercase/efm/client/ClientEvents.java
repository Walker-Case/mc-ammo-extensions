package com.walkercase.efm.client;

import com.walkercase.efm.EFMMain;
import com.walkercase.efm.entities.EFMEntities;
import com.walkercase.efm.entities.RaiderEntity;
import com.walkercase.efm.entities.RaiderRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event){
        event.put(EFMEntities.RAIDER_ENTITY.get(), RaiderEntity.getRaiderAttributes().build());
    }

    @SubscribeEvent
    public static void entityRendererEvent(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EFMEntities.RAIDER_ENTITY.get(), RaiderRenderer::new);
    }

}
