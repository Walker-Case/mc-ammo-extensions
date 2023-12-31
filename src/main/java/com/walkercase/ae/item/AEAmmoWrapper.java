package com.walkercase.ae.item;

import com.mrcrayfish.guns.item.AmmoItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AEAmmoWrapper extends AmmoItem {

    public final ResourceLocation baseAmmo;
    public final EnchantmentWrapper[] enchantments;
    public final ChatFormatting chatColor;
    public AEAmmoWrapper(ResourceLocation baseAmmo, ChatFormatting color, Properties p_41383_) {
        this(baseAmmo, color, null, p_41383_);
    }

    public AEAmmoWrapper(ResourceLocation baseAmmo, ChatFormatting color, EnchantmentWrapper[] enchantments, Properties p_41383_) {
        super(p_41383_);
        this.baseAmmo = baseAmmo;
        this.chatColor = color;
        this.enchantments = enchantments;
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable Level level, List<Component> list, TooltipFlag p_41424_) {
        if(is.getTag() != null && is.getTag().contains("efm")){
            CompoundTag efmTag = is.getTag().getCompound("efm");
            if(efmTag.contains("caliber")){
                list.add(Component.literal("Contains ").append(Component.translatable(efmTag.getString("caliber"))));
            }
        }
    }

    /**
     * Applies ammo enchants to the specified ItemStack.
     * @param is
     * @return
     */
    public ItemStack applyAmmoEnchants(ItemStack is){
        for (EnchantmentWrapper enchant : this.enchantments) {
            is.enchant(enchant.enchantment.get(), enchant.level);
        }

        return is;
    }
}


