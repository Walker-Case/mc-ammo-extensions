package com.walkercase.ae.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mrcrayfish.guns.item.GunItem;
import com.walkercase.ae.util.AENBTHelper;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Magazine extends Item {
    private final RegistryObject[] companionWeapons;
    public final int size;
    public final EnchantmentWrapper[] enchantments;
    public Magazine(RegistryObject[] companionWeapons, int size, EnchantmentWrapper[] enchantments, Properties properties) {
        super(properties.durability(size));

        this.size = size;
        this.companionWeapons = companionWeapons;
        this.enchantments = enchantments;
    }

    /**
     * Returns true if the provided gun matches the provided magazine.
     * @param item GunItem
     * @return true/false
     */
    public boolean matchesWeapon(Item item){
        return matchesWeapon(ForgeRegistries.ITEMS.getKey(item));
    }

    /**
     * Returns true if the provided gun matches the provided magazine.
     * @param resourceLocation Gun
     * @return true/false
     */
    public boolean matchesWeapon(ResourceLocation resourceLocation){
        for(RegistryObject ro : companionWeapons){
            if(ro.getId().compareTo(resourceLocation) == 0)
                return true;
        }
        return false;
    }

    /**
     * Returns true if the provided resourceLocation matches the magazine type.
     * @param resourceLocation ResourceLocation
     * @return true/false
     */
    public boolean matchesAmmo(ResourceLocation resourceLocation){
        for(RegistryObject ro : companionWeapons){
            GunItem gun = (GunItem)ro.get();
            ResourceLocation a = gun.getGun().getProjectile().getItem();
            if(a.compareTo(resourceLocation) == 0)
                return true;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable Level level, List<Component> list, TooltipFlag p_41424_) {
        list.addAll(getTooltipAmmoTicker(is));
    }

    public static Collection<? extends Component> getTooltipAmmoTicker(ItemStack is){
        Item[] ammo = Magazine.getLoadedAmmo(is);
        ArrayList<Component> list = new ArrayList<Component>();
        MutableComponent base = Component.empty();
        if(ammo.length > 0){
            for(int i=0;i<ammo.length;i++){
                ChatFormatting formatting = ChatFormatting.WHITE;
                if(ammo[i] instanceof AEAmmoWrapper wrapper){
                    formatting = wrapper.chatColor;
                }
                if(i % 40==0){
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

        return list;
    }

    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public int getBarWidth(ItemStack stack) {
        return (int)(13.0 * ((double)Magazine.getLoadedAmmo(stack).length / (double)this.size));
    }

    public int getBarColor(ItemStack stack) {
        return (Integer) Objects.requireNonNull(ChatFormatting.YELLOW.getColor());
    }

    /**
     * Returns an array of all of the loaded ammo.
     * @param is ItemStack Magazine
     * @return
     */
    public static Item[] getLoadedAmmo(ItemStack is){
        JsonArray arr = getJsonLoadedAmmo(is);
        Item[] items = new Item[arr.size()];
        for(int i=0;i<arr.size();i++){
            items[i] = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(arr.get(i).getAsString()));
        }
        return items;
    }

    /**
     * Returns the top bullet in the magazine.
     * @param is ItemStack Magazine
     * @return
     */
    public static Item getTopBullet(ItemStack is){
        JsonArray arr = getJsonLoadedAmmo(is);
        if(!arr.isEmpty()){
            return (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(arr.get(0).getAsString()));
        }
        return null;
    }

    /**
     * Put a bullet into the top of the magazine ItemStack.
     * @param is ItemStack Magazine
     * @param bullet Item Ammo
     * @return true if successful
     */
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

    /**
     * Attempts to take the first bullet in the magazine and return it.
     * @param is ItemStack Magazine
     * @return Item or null if unable.
     */
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
     * @param gun ItemStack GunItem
     * @param magazine ItemStack Magazine
     * @return
     */
    public static ItemStack setCurrentMagazine(ItemStack gun, ItemStack magazine){
        CompoundTag efm = AENBTHelper.getModMainTagForItemStack(gun);
        ItemStack old = getCurrentMagazineForGun(gun);

        efm.put("current-magazine", magazine.serializeNBT());
        return old;
    }

    /**
     * Returns the current magazine stored in the gun.
     * @param is ItemStack GunItem
     * @return ItemStack Magazine
     */
    public static ItemStack getCurrentMagazineForGun(ItemStack is){
        CompoundTag efm = AENBTHelper.getModMainTagForItemStack(is);
        if(efm.contains("current-magazine")){
            return ItemStack.of(efm.getCompound("current-magazine"));
        }

        return null;
    }

    /**
     * Updates the currently loaded ammo in the ItemStack nbt to the new JsonArray.
     * @param is ItemStack Magazine
     * @param newArray JsonArray from NBT Tag
     */
    private static void updateLoadedAmmo(ItemStack is, JsonArray newArray){
        CompoundTag efm = AENBTHelper.getModMainTagForItemStack(is);
        efm.putString("loaded-ammo", newArray.toString());
    }

    /**
     * Returns the raw JsonArray of the loaded-ammo NBT tag.
     * @param is ItemStack Magazine
     * @return
     */
    private static JsonArray getJsonLoadedAmmo(ItemStack is){
        CompoundTag efm = AENBTHelper.getModMainTagForItemStack(is);
        String loadedAmmo = efm.getString("loaded-ammo");
        return StringUtils.isEmpty(loadedAmmo) ? new JsonArray() : JsonParser.parseString(loadedAmmo).getAsJsonArray();
    }
}
