package com.walkercase.efm.item;

import com.mrcrayfish.guns.common.ModTags;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.init.ModItems;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.enchantment.EFMEnchantments;
import com.walkercase.efm.entities.EFMEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EFMItems {
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, EFMMain.MODID);

    public static final RegistryObject<Item> RAIDER_SPAWN_EGG = REGISTER.register("raider_spawn_egg",
            ()-> new ForgeSpawnEggItem(EFMEntities.RAIDER_ENTITY, 0x000000, 0xFF0000, new Item.Properties()));

    /*
            Incendiary
             */
    public static final RegistryObject<Item> BASIC_INC_AMMO = REGISTER.register("basic_inc_ammo", () ->
            new EFMAmmoWrapper(ModItems.BASIC_BULLET.getId(), ChatFormatting.RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1)
                    },
                    new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_INC_AMMO = REGISTER.register("advanced_inc_ammo", () ->
            new EFMAmmoWrapper(ModItems.ADVANCED_AMMO.getId(), ChatFormatting.RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1),
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1)
                    },
                    new Item.Properties()));
    public static final RegistryObject<Item> SHELL_INC_AMMO = REGISTER.register("shell_inc_ammo", () ->
            new EFMAmmoWrapper(ModItems.SHELL.getId(), ChatFormatting.RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1)
                    },
                    new Item.Properties()));
    /*
        Full Metal
         */
    public static final RegistryObject<Item> BASIC_FMJ_AMMO = REGISTER.register("basic_fmj_ammo", () ->
            new EFMAmmoWrapper(ModItems.BASIC_BULLET.getId(), ChatFormatting.GRAY,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 1)
                    },
                    new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_FMJ_AMMO = REGISTER.register("advanced_fmj_ammo", () ->
            new EFMAmmoWrapper(ModItems.ADVANCED_AMMO.getId(), ChatFormatting.GRAY,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2)
                    },
                    new Item.Properties()));
    //Buckshot
    public static final RegistryObject<Item> SHELL_BUCK_AMMO = REGISTER.register("shell_buck_ammo", () ->
            new EFMAmmoWrapper(ModItems.SHELL.getId(), ChatFormatting.GRAY,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 2),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2)
                    },
                    new Item.Properties()));
    /*
        Armor Piercing
         */
    public static final RegistryObject<Item> BASIC_AP_AMMO = REGISTER.register("basic_ap_ammo", () ->
            new EFMAmmoWrapper(ModItems.BASIC_BULLET.getId(), ChatFormatting.GREEN,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2)
                    },
                    new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_AP_AMMO = REGISTER.register("advanced_ap_ammo", () ->
            new EFMAmmoWrapper(ModItems.ADVANCED_AMMO.getId(), ChatFormatting.GREEN,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 2),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2),
                            new EnchantmentWrapper(ModEnchantments.COLLATERAL, 1)
                    },
                    new Item.Properties()));
    //50 cal ball
    public static final RegistryObject<Item> SHELL_FIFTY_AMMO = REGISTER.register("shell_fifty_ammo", () ->
            new EFMAmmoWrapper(ModItems.SHELL.getId(), ChatFormatting.GREEN,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 2),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 4),
                    },
                    new Item.Properties()));
    /*
        Black Tip
         */
    public static final RegistryObject<Item> BASIC_BT_AMMO = REGISTER.register("basic_bt_ammo", () ->
            new EFMAmmoWrapper(ModItems.BASIC_BULLET.getId(), ChatFormatting.DARK_RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1),
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 1),
                    },
                    new Item.Properties()));
    public static final RegistryObject<Item> ADVANCED_BT_AMMO = REGISTER.register("advanced_bt_ammo", () ->
            new EFMAmmoWrapper(ModItems.ADVANCED_AMMO.getId(), ChatFormatting.DARK_RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 1),
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2),
                    },
                    new Item.Properties()));
    //Dragons Breath
    public static final RegistryObject<Item> SHELL_DB_AMMO = REGISTER.register("shell_db_ammo", () ->
            new EFMAmmoWrapper(ModItems.SHELL.getId(), ChatFormatting.DARK_RED,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.ACCELERATOR, 2),
                            new EnchantmentWrapper(ModEnchantments.FIRE_STARTER, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2),
                    },
                    new Item.Properties()));

    /*
    Fracture Ammo
     */
    public static final RegistryObject<Item> BASIC_FRAC_AMMO = REGISTER.register("basic_frac_ammo", () ->
            new EFMAmmoWrapper(ModItems.BASIC_BULLET.getId(), ChatFormatting.AQUA,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(EFMEnchantments.FRACTURE, 1)
                    },
                    new Item.Properties()));

    public static final RegistryObject<Item> ADVANCED_FRAC_AMMO = REGISTER.register("advanced_frac_ammo", () ->
            new EFMAmmoWrapper(ModItems.ADVANCED_AMMO.getId(), ChatFormatting.AQUA,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 2),
                            new EnchantmentWrapper(EFMEnchantments.FRACTURE, 2)
                    },
                    new Item.Properties()));

    public static final RegistryObject<Item> SHELL_FRAC_AMMO = REGISTER.register("shell_frac_ammo", () ->
            new EFMAmmoWrapper(ModItems.SHELL.getId(), ChatFormatting.AQUA,
                    new EnchantmentWrapper[]{
                            new EnchantmentWrapper(EFMEnchantments.FRACTURE, 2),
                            new EnchantmentWrapper(ModEnchantments.COLLATERAL, 1),
                            new EnchantmentWrapper(ModEnchantments.PUNCTURING, 1)
                    },
                    new Item.Properties()));

    public static final RegistryObject<Item> PISTOL_STANDARD_MAG = REGISTER.register("pistol_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.PISTOL, ModItems.MACHINE_PISTOL}, 8, new EnchantmentWrapper[]{}, new Item.Properties()));
    public static final RegistryObject<Item> PISTOL_EXTENDED_MAG = REGISTER.register("pistol_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.PISTOL, ModItems.MACHINE_PISTOL}, 16, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-1)
            }, new Item.Properties()));
    public static final RegistryObject<Item> PISTOL_DRUM_MAG = REGISTER.register("pistol_drum_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.PISTOL, ModItems.MACHINE_PISTOL}, 64, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-6)
            }, new Item.Properties()));

    public static final RegistryObject<Item> PISTOL_FAST_MAG = REGISTER.register("pistol_fast_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.PISTOL, ModItems.MACHINE_PISTOL}, 8, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,3)
            }, new Item.Properties()));

    public static final RegistryObject<Item> SHOTGUN_STANDARD_MAG = REGISTER.register("shotgun_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.SHOTGUN}, 4, new EnchantmentWrapper[]{}, new Item.Properties()));
    public static final RegistryObject<Item> SHOTGUN_EXTENDED_MAG = REGISTER.register("shotgun_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.SHOTGUN}, 8, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-2)
            }, new Item.Properties()));
    public static final RegistryObject<Item> SHOTGUN_DRUM_MAG = REGISTER.register("shotgun_drum_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.SHOTGUN}, 45, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-10)
            }, new Item.Properties()));

    public static final RegistryObject<Item> RIFLE_STANDARD_MAG = REGISTER.register("rifle_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.RIFLE}, 8, new EnchantmentWrapper[]{}, new Item.Properties()));
    public static final RegistryObject<Item> RIFLE_EXTENDED_MAG = REGISTER.register("rifle_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.RIFLE}, 16, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-1)
            }, new Item.Properties()));
    public static final RegistryObject<Item> RIFLE_FAST_MAG = REGISTER.register("rifle_fast_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.RIFLE}, 10, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,2)
            }, new Item.Properties()));

    public static final RegistryObject<Item> AR_STANDARD_MAG = REGISTER.register("ar_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.ASSAULT_RIFLE}, 30, new EnchantmentWrapper[]{}, new Item.Properties()));

    public static final RegistryObject<Item> AR_EXTENDED_MAG = REGISTER.register("ar_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.ASSAULT_RIFLE}, 50, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-3)
            }, new Item.Properties()));
    public static final RegistryObject<Item> AR_DRUM_MAG = REGISTER.register("ar_drum_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.ASSAULT_RIFLE}, 100, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-8)
            }, new Item.Properties()));

    public static final RegistryObject<Item> AR_FAST_MAG = REGISTER.register("ar_fast_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.ASSAULT_RIFLE}, 16, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,3)
            }, new Item.Properties()));

    public static final RegistryObject<Item> HEAVY_RIFLE_STANDARD_MAG = REGISTER.register("heavy_rifle_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.HEAVY_RIFLE}, 5, new EnchantmentWrapper[]{}, new Item.Properties()));

    public static final RegistryObject<Item> HEAVY_RIFLE_EXTENDED_MAG = REGISTER.register("heavy_rifle_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.HEAVY_RIFLE}, 10, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-2)
            }, new Item.Properties()));

    public static final RegistryObject<Item> HEAVY_RIFLE_FAST_MAG = REGISTER.register("heavy_rifle_fast_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.HEAVY_RIFLE}, 6, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,1)
            }, new Item.Properties()));

    public static final RegistryObject<Item> MINIGUN_STANDARD_MAG = REGISTER.register("minigun_standard_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.MINI_GUN}, 256, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-12)
            }, new Item.Properties()));

    public static final RegistryObject<Item> MINIGUN_EXTENDED_MAG = REGISTER.register("minigun_extended_mag",
            () -> new Magazine(new RegistryObject[]{ModItems.MINI_GUN}, 450, new EnchantmentWrapper[]{
                    new EnchantmentWrapper(ModEnchantments.QUICK_HANDS,-15)
            }, new Item.Properties()));

    //public static final RegistryObject<Item> DRUM_MAG = REGISTER.register("drum_mag", () -> new Magazine(new Item.Properties()));
   //public static final RegistryObject<Item> FAST_MAG = REGISTER.register("fast_mag", () -> new Magazine(new Item.Properties()));

    public static void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if (event.getTab() == CreativeModeTabs.SPAWN_EGGS)
            event.accept(RAIDER_SPAWN_EGG);
        if(event.getTab() == CreativeModeTabs.COMBAT){

            event.accept(PISTOL_STANDARD_MAG);
            event.accept(PISTOL_EXTENDED_MAG);
            event.accept(PISTOL_DRUM_MAG);
            event.accept(PISTOL_FAST_MAG);

            event.accept(SHOTGUN_STANDARD_MAG);
            event.accept(SHOTGUN_EXTENDED_MAG);
            event.accept(SHOTGUN_DRUM_MAG);

            event.accept(RIFLE_STANDARD_MAG);
            event.accept(RIFLE_EXTENDED_MAG);
            event.accept(RIFLE_FAST_MAG);

            event.accept(AR_STANDARD_MAG);
            event.accept(AR_DRUM_MAG);
            event.accept(AR_FAST_MAG);
            event.accept(AR_EXTENDED_MAG);

            event.accept(HEAVY_RIFLE_EXTENDED_MAG);
            event.accept(HEAVY_RIFLE_FAST_MAG);
            event.accept(HEAVY_RIFLE_STANDARD_MAG);

            event.accept(MINIGUN_STANDARD_MAG);
            event.accept(MINIGUN_EXTENDED_MAG);

            event.accept(BASIC_AP_AMMO);
            event.accept(ADVANCED_AP_AMMO);
            event.accept(SHELL_FIFTY_AMMO);

            event.accept(BASIC_BT_AMMO);
            event.accept(ADVANCED_BT_AMMO);
            event.accept(SHELL_DB_AMMO);

            event.accept(BASIC_INC_AMMO);
            event.accept(ADVANCED_INC_AMMO);
            event.accept(SHELL_INC_AMMO);

            event.accept(BASIC_FMJ_AMMO);
            event.accept(ADVANCED_FMJ_AMMO);
            event.accept(SHELL_BUCK_AMMO);

            event.accept(BASIC_FRAC_AMMO);
            event.accept(ADVANCED_FRAC_AMMO);
            event.accept(SHELL_FRAC_AMMO);
        }
    }
}
