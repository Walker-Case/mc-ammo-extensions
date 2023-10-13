package com.walkercase.efm.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.util.EFMNBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class Magazine extends Item {
    public final RegistryObject<GunItem> companionWeapon;
    public final int size;
    public Magazine(RegistryObject<GunItem> companionWeapon, int size, Properties properties) {
        super(properties.durability(size));

        this.size = size;
        this.companionWeapon = companionWeapon;
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable Level level, List<Component> list, TooltipFlag p_41424_) {
        Item[] ammo = Magazine.getLoadedAmmo(is);
        MutableComponent base = Component.empty();
        if(ammo.length > 0){
            for(int i=0;i<ammo.length;i++){
                ChatFormatting formatting = ChatFormatting.WHITE;
                if(ammo[i] instanceof EFMAmmoWrapper wrapper){
                    formatting = wrapper.chatColor;
                }
                if(i % 20==0){
                    list.add(base);
                    base = Component.empty();
                }
                base.append(Component.literal("|").withStyle(formatting));
            }
            if(!StringUtils.isEmpty(base.getString())){
                list.add(base);
            }
        }else{
            list.add(Component.literal("Empty"));
        }
    }

    public static Item[] getLoadedAmmo(ItemStack is){
        JsonArray arr = getJsonLoadedAmmo(is);
        Item[] items = new Item[arr.size()];
        for(int i=0;i<arr.size();i++){
            items[i] = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(arr.get(i).getAsString()));
        }
        return items;
    }

    public static Item getTopBullet(ItemStack is){
        JsonArray arr = getJsonLoadedAmmo(is);
        if(!arr.isEmpty()){
            return (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(arr.get(0).getAsString()));
        }
        return null;
    }

    public static boolean putBullet(ItemStack is, Item bullet){
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(bullet);

        JsonArray arr = getJsonLoadedAmmo(is);
        if(arr.size() < is.getMaxDamage()){
            arr.add(key.toString());
            updateLoadedAmmo(is, arr);
            return true;
        }

        return false;
    }

    public static Item takeTopBullet(ItemStack is){
        JsonArray arr = getJsonLoadedAmmo(is);
        if(!arr.isEmpty()){
            Item item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(arr.remove(0).getAsString()));
            updateLoadedAmmo(is, arr);
            return item;
        }
        return null;
    }

    /**
     * Sets the current gun magazine and returns the old one if any existed.
     * @param gun
     * @param magazine
     * @return
     */
    public static ItemStack setCurrentMagazine(ItemStack gun, ItemStack magazine){
        CompoundTag efm = EFMNBTHelper.getModMainTagForItemStack(gun);
        ItemStack old = getCurrentMagazineForGun(gun);

        efm.put("current-magazine", magazine.serializeNBT());
        return old;
    }

    /**
     * Returns the current magazine stored in the gun.
     * @param is
     * @return
     */
    public static ItemStack getCurrentMagazineForGun(ItemStack is){
        CompoundTag efm = EFMNBTHelper.getModMainTagForItemStack(is);
        if(efm.contains("current-magazine")){
            return ItemStack.of(efm.getCompound("current-magazine"));
        }

        return null;
    }

    /**
     * Updates the currently loaded ammo in the ItemStack nbt to the new JsonArray.
     * @param is
     * @param newArray
     */
    private static void updateLoadedAmmo(ItemStack is, JsonArray newArray){
        CompoundTag efm = EFMNBTHelper.getModMainTagForItemStack(is);
        efm.putString("loaded-ammo", newArray.toString());
    }

    private static JsonArray getJsonLoadedAmmo(ItemStack is){
        CompoundTag efm = EFMNBTHelper.getModMainTagForItemStack(is);
        String loadedAmmo = efm.getString("loaded-ammo");
        return StringUtils.isEmpty(loadedAmmo) ? new JsonArray() : JsonParser.parseString(loadedAmmo).getAsJsonArray();
    }
}
