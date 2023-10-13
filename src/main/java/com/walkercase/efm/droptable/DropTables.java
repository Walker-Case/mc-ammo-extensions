package com.walkercase.efm.droptable;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.walkercase.efm.exception.UnkownTableException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class DropTables {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Random random = new Random();

    private static HashMap<String, DropTableItem> dropTablePrefabs = new HashMap<String, DropTableItem>();
    private static ArrayList<DropTable> dropTables = new ArrayList<DropTable>();

    /**
     * Returns one guaranteed drop for the specified container.
     * @param containerName
     * @return
     * @throws UnkownTableException
     */
    public static ItemStack getGuaranteedItemStack(String containerName) throws UnkownTableException {
        ArrayList<DropTable> tables = getDropTablesByName(containerName);

            DropTable table = tables.get(random.nextInt(tables.size()));
            float f = random.nextFloat();

            Optional<Map.Entry<String, Float>> optional = table.dropTable.entrySet().stream().filter(x->{
                return x.getValue() >= f;
            }).findAny();

            if(!optional.isEmpty()) {
                return dropTablePrefabs.get(optional.get().getKey()).createItemStack();
            }else {
                try{
                    String key = table.dropTable.entrySet().stream().findAny().get().getKey();
                    return dropTablePrefabs.get(key).createItemStack();
                }catch(Exception e){
                    LOGGER.error("Error", e);
                }

                return new ItemStack(Items.DIAMOND_SWORD);
            }
    }

    /**
     * Returns the item drops as specified by the container file.
     * @param containerName
     * @param count
     * @return
     * @throws UnkownTableException
     */
    public static List<ItemStack> getItemStacksForContainer(String containerName, int count) throws UnkownTableException {
      ArrayList<ItemStack> loot = new ArrayList<ItemStack>();

      ArrayList<DropTable> tables = getDropTablesByName(containerName);

      for(int i=0;i<count;i++){
          DropTable table = tables.get(random.nextInt(tables.size()));
          float f = random.nextFloat();

          Optional<Map.Entry<String, Float>> optional = table.dropTable.entrySet().stream().filter(x->{
              return x.getValue() >= f;
          }).findAny();

          if(!optional.isEmpty())
              loot.add(dropTablePrefabs.get(optional.get().getKey()).createItemStack());
          else
              loot.add(new ItemStack(Blocks.AIR));
      }

      return loot;
    }

    /**
     * Returns a list of drop tables for the specified container.
     * @param containerName
     * @return
     */
    private static ArrayList<DropTable> getDropTablesByName(String containerName) throws UnkownTableException {
        ArrayList<DropTable> tables = new ArrayList<DropTable>();
        dropTables.forEach(dropTable->{
            if(dropTable.containers.contains(containerName)){
                tables.add(dropTable);
            }
        });

        if(tables == null || tables.size() == 0)
            throw new UnkownTableException(containerName);

        return tables;
    }

    /**
     * Reads drop table prefabs and tables from disk.
     */
    public static void readDropTables(){
        loadItemStackPrefabs();
        loadDropTableFiles();
    }

    /**
     * Loads the item stack prefabs.
     */
    private static void loadItemStackPrefabs(){
        File itemStacks = new File("config/eft/drop-tables/prefabs");
        for(File f : itemStacks.listFiles()){
            if(f.isFile()){
                LOGGER.debug("Parsing " + f.getAbsoluteFile());
                try {
                    DropTableItem di = new DropTableItem();
                    JsonElement json = JsonParser.parseString(Files.readString(f.toPath()));
                    JsonObject jsonObj = json.getAsJsonObject();

                    if(!jsonObj.has("id")){
                        LOGGER.error("ItemStack must have id! file={}", f.getAbsoluteFile());
                        return;
                    }

                    di.base = getItemFromKey(jsonObj.get("id").getAsString());

                    LOGGER.info("Loaded item from prefab {}", di.base);

                    if(!jsonObj.has("count")){
                        LOGGER.error("ItemStack must have count! file={}", f.getAbsoluteFile());
                        return;
                    }
                    JsonObject count = jsonObj.get("count").getAsJsonObject();
                    di.minCount = count.get("min").getAsInt();
                    di.maxCount = count.get("max").getAsInt();

                    if(jsonObj.has("damage")){
                        JsonObject damage = jsonObj.get("damage").getAsJsonObject();
                        di.minDamage = damage.get("min").getAsInt();
                        di.maxDamage = damage.get("max").getAsInt();
                    }

                    if(jsonObj.has("enchantments")){
                        JsonObject enchantments = jsonObj.get("enchantments").getAsJsonObject();
                        di.maxEnchantments = enchantments.get("max-enchantments").getAsInt();
                        di.enchantmentWeight = enchantments.get("enchantment-weight").getAsFloat();

                        ArrayList<DropTableEnchantment> enchantList = new ArrayList<DropTableEnchantment>();
                        enchantments.get("possibilities").getAsJsonArray().forEach(e->{
                            JsonObject enchantObj = e.getAsJsonObject();

                            DropTableEnchantment dte = new DropTableEnchantment();
                            dte.enchant = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantObj.get("name").getAsString()));
                            dte.maxLevel = enchantObj.get("max-level").getAsInt();
                            dte.weight = enchantObj.get("weight").getAsFloat();

                            enchantList.add(dte);
                        });
                        di.enchantList = enchantList;
                    }

                    if(jsonObj.has("cgm")){
                        JsonObject cgm = jsonObj.getAsJsonObject("cgm");
                        if(cgm.has("attachments")){
                            HashMap<CgmWeaponAttachmentType, List<String>> weaponAttachments = new HashMap<CgmWeaponAttachmentType, List<String>>();
                            JsonObject attachments = cgm.getAsJsonObject("attachments");

                            if(attachments.has("barrel")){
                                if(!weaponAttachments.containsKey(CgmWeaponAttachmentType.barrel))
                                    weaponAttachments.put(CgmWeaponAttachmentType.barrel, new ArrayList<String>());

                                attachments.getAsJsonArray("barrel").forEach(x->{
                                    weaponAttachments.get(CgmWeaponAttachmentType.barrel).add(x.getAsString());
                                });
                            }

                            if(attachments.has("scope")){
                                if(!weaponAttachments.containsKey(CgmWeaponAttachmentType.scope))
                                    weaponAttachments.put(CgmWeaponAttachmentType.scope, new ArrayList<String>());

                                attachments.getAsJsonArray("scope").forEach(x->{
                                    weaponAttachments.get(CgmWeaponAttachmentType.scope).add(x.getAsString());
                                });
                            }

                            if(attachments.has("stock")){
                                if(!weaponAttachments.containsKey(CgmWeaponAttachmentType.stock))
                                    weaponAttachments.put(CgmWeaponAttachmentType.stock, new ArrayList<String>());

                                attachments.getAsJsonArray("stock").forEach(x->{
                                    weaponAttachments.get(CgmWeaponAttachmentType.stock).add(x.getAsString());
                                });
                            }

                            if(attachments.has("under-barrel")){
                                if(!weaponAttachments.containsKey(CgmWeaponAttachmentType.under_barrel))
                                    weaponAttachments.put(CgmWeaponAttachmentType.under_barrel, new ArrayList<String>());

                                attachments.getAsJsonArray("under-barrel").forEach(x->{
                                    weaponAttachments.get(CgmWeaponAttachmentType.under_barrel).add(x.getAsString());
                                });
                            }
                            di.weaponAttachments = weaponAttachments;
                        }
                    }

                    dropTablePrefabs.put(f.getName(), di);
                    LOGGER.info("Found drop table itemstack prefab {} at {}", di.base.toString(), f.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }

    /**
     * Returns the Item object from registry for the provided key.
     * @param id
     * @return
     */
    private static Item getItemFromKey(String id){
        ResourceLocation baseKey = new ResourceLocation(id);
        if(ForgeRegistries.ITEMS.containsKey(baseKey))
            return ForgeRegistries.ITEMS.getValue(baseKey).asItem();
        else
            return ForgeRegistries.BLOCKS.getValue(baseKey).asItem();
    }

    /**
     * Loads the drop table files.
     */
    private static void loadDropTableFiles(){
        File dropTablesF = new File("config/eft/drop-tables");
        for(File f : dropTablesF.listFiles()){
            if(f.isFile()){
                LOGGER.debug("Found drop-table " + f.toString());
                try {
                    DropTable dropTable = new DropTable();

                    JsonElement json = JsonParser.parseString(Files.readString(f.toPath()));
                    JsonObject dropTableObj = json.getAsJsonObject();
                    List<String> containers = new ArrayList<String>();
                    dropTableObj.get("containers").getAsJsonArray().forEach(x->{
                        containers.add(x.getAsString());
                    });
                    dropTable.containers = containers;

                    JsonArray items = dropTableObj.get("items").getAsJsonArray();
                    items.forEach(g->{
                        JsonObject item = g.getAsJsonObject();
                        dropTable.dropTable.put(item.get("prefab").getAsString(), item.get("rarity").getAsFloat());
                    });

                    HashMap<String, Float> result = dropTable.dropTable.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                    dropTable.dropTable = result;

                    dropTables.add(dropTable);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

/**
 * Contains information on what containers certain items can "drop" for.
 */
class DropTable{
    public List<String> containers;
    public HashMap<String, Float> dropTable = new HashMap<String, Float>();
}

/**
 * Contains information on each specific item prefab.
 */
class DropTableItem{
    private static Random rand = new Random();

    public Item base;
    public int minCount = 1, maxCount = 1;
    public int minDamage = 0, maxDamage = 0;

    public int maxEnchantments = 0;
    public float enchantmentWeight = 0;

    public List<DropTableEnchantment> enchantList;

    public HashMap<CgmWeaponAttachmentType, List<String>> weaponAttachments;

    public ItemStack createItemStack(){
        ItemStack is = new ItemStack(base, maxCount == 1 ? 1 : rand.nextInt(minCount, maxCount));
        CompoundTag nbt = is.getOrCreateTag();
        Random random = new Random();

        if(maxDamage != 0){
            if(maxDamage == -1) {
                //ExampleMod.LOGGER.info("Max Damage {}", is.getMaxDamage());
                //is.setDamageValue(rand.nextInt(minDamage, is.getMaxDamage()));
            }else
                is.setDamageValue(maxDamage == 0 ? 0 : rand.nextInt(minDamage, maxDamage));
        }

        if(enchantList != null){
            int numOfEnchants = getWeighedNumber(maxEnchantments, enchantmentWeight);

            ArrayList<DropTableEnchantment> toUse = new ArrayList<DropTableEnchantment>();
            for(int i=0;i<numOfEnchants;i++){
                DropTableEnchantment dte = enchantList.get(rand.nextInt(enchantList.size()));
                if(!toUse.contains(dte)) {
                    toUse.add(dte);
                    int weighedLevel = getWeighedNumber(dte.maxLevel, dte.weight);
                    is.enchant(dte.enchant, weighedLevel == 0 ? 1 : weighedLevel);
                }
            }
        }

        if(weaponAttachments != null && weaponAttachments.size() > 0){
            CompoundTag attachmentsTag = getOrCreateTag(nbt, "Attachments");


            if(random.nextBoolean() && weaponAttachments.containsKey(CgmWeaponAttachmentType.barrel)){
                CompoundTag barrelTag = getOrCreateTag(attachmentsTag, "Barrel");

                List<String> barrel = weaponAttachments.get(CgmWeaponAttachmentType.barrel);

                String randomBarrel = barrel.get(random.nextInt(barrel.size()));

                barrelTag.putString("id", randomBarrel);
                barrelTag.putByte("Count", (byte)1);
            }

            if(random.nextBoolean() && weaponAttachments.containsKey(CgmWeaponAttachmentType.scope)){
                CompoundTag scopeTag = getOrCreateTag(attachmentsTag, "Scope");

                List<String> scope = weaponAttachments.get(CgmWeaponAttachmentType.scope);
                String randomScope = scope.get(random.nextInt(scope.size()));

                scopeTag.putString("id", randomScope);
                scopeTag.putByte("Count", (byte)1);
            }

            if(random.nextBoolean() && weaponAttachments.containsKey(CgmWeaponAttachmentType.stock)){
                CompoundTag stockTag = getOrCreateTag(attachmentsTag, "Stock");

                List<String> stock = weaponAttachments.get(CgmWeaponAttachmentType.stock);
                String randomStock = stock.get(random.nextInt(stock.size()));

                stockTag.putString("id", randomStock);
                stockTag.putByte("Count", (byte)1);
            }

            if(random.nextBoolean() && weaponAttachments.containsKey(CgmWeaponAttachmentType.under_barrel)){
                CompoundTag ubTag = getOrCreateTag(attachmentsTag, "Under_Barrel");

                List<String> ub = weaponAttachments.get(CgmWeaponAttachmentType.under_barrel);
                String randomUb = ub.get(random.nextInt(ub.size()));

                ubTag.putString("id", randomUb);
                ubTag.putByte("Count", (byte)1);
            }
        }

        return is;
    }

    /**
     * Returns or creates the specified tag.
     * @param tag
     * @param newTag
     * @return
     */
    private static CompoundTag getOrCreateTag(CompoundTag tag, String newTag){
        if(!tag.contains(newTag)){
            tag.put(newTag, new CompoundTag());
        }
        return tag.getCompound(newTag);
    }

    private static int getWeighedNumber(int max, float weight){
        float randFloat = rand.nextFloat();
        for(int i=max;i>0;i--){
            if(weight/i >= randFloat){
                return i;
            }
        }
        return 0;
    }
}

/**
 * Weapon attachment enum used for MrCrayfish's Gun mod.
 */
enum CgmWeaponAttachmentType{
    barrel, scope, stock, under_barrel
}

/**
 * Contains information regarding item prefab enchantments.
 */
class DropTableEnchantment{
    public Enchantment enchant;
    public int maxLevel = 1;
    public float weight  = 0;
}
