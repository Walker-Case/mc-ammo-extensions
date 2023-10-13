package com.walkercase.efm.client;

import com.mojang.datafixers.util.Either;
import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.entities.EFMEntities;
import com.walkercase.efm.entities.RaiderEntity;
import com.walkercase.efm.entities.RaiderRenderer;
import com.walkercase.efm.item.Magazine;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    static class MainEventBus{
        @SubscribeEvent
        public static void tooltipRenderEvent(RenderTooltipEvent.GatherComponents event){
            ItemStack is = event.getItemStack();

            if(is.getItem() instanceof GunItem){
                ItemStack magazine = Magazine.getCurrentMagazineForGun(is);
                if(magazine != null){
                    event.getTooltipElements().remove(1);
                    event.getTooltipElements().remove(2);
                    event.getTooltipElements().remove(3);

                    Magazine.getTooltipAmmoTicker(magazine).forEach(x->{
                        event.getTooltipElements().add(2, Either.left(x));
                    });
                }
            }
        }
    }


    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event){
        event.put(EFMEntities.RAIDER_ENTITY.get(), RaiderEntity.getRaiderAttributes().build());
    }

    @SubscribeEvent
    public static void entityRendererEvent(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EFMEntities.RAIDER_ENTITY.get(), RaiderRenderer::new);
    }

}
