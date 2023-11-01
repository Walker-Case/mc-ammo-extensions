package com.walkercase.ae.client;

import com.mojang.datafixers.util.Either;
import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.ae.AEMain;
import com.walkercase.ae.item.Magazine;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AEMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = AEMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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

}
