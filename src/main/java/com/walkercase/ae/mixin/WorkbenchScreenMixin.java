package com.walkercase.ae.mixin;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.screen.WorkbenchScreen;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ReloadTracker;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.walkercase.ae.item.AEItems;
import com.walkercase.ae.item.Magazine;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(WorkbenchScreen.class)
public class WorkbenchScreenMixin{
   @Shadow
   private List<WorkbenchScreen.Tab> tabs = new ArrayList<>();

    @Inject(method = "createTabs", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void createTabs(NonNullList<WorkbenchRecipe> recipes, CallbackInfo ci) {
        List<WorkbenchRecipe> magazines = new ArrayList<>();

        for(WorkbenchRecipe recipe : recipes)
        {
            ItemStack output = recipe.getItem();
            if(output.getItem() instanceof Magazine){
                magazines.add(recipe);
            }
        }

        if(!magazines.isEmpty()){
           // tabs.add(new Tab(new ItemStack(AEItems.AR_STANDARD_MAG.get()), "magazines", magazines));
        }
    }
}
