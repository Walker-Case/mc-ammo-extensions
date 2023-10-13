package com.walkercase.efm;

import com.walkercase.efm.client.ClientEvents;
import com.walkercase.efm.command.ExportItemStackCommand;
import com.walkercase.efm.droptable.DropTables;
import com.walkercase.efm.enchantment.EFMEnchantments;
import com.walkercase.efm.entities.EFMEntities;
import com.mojang.logging.LogUtils;
import com.walkercase.efm.event.CommonEvents;
import com.walkercase.efm.event.LevelModEvents;
import com.walkercase.efm.event.MenuEvents;
import com.walkercase.efm.framework.SyncedKeys;
import com.walkercase.efm.item.EFMItems;
import com.walkercase.efm.systems.ShootExtension;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EFMMain.MODID)
public class EFMMain
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "efm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
   // public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path


    public EFMMain()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EFMConfig.commonSpec, "efm-common-config.toml");

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ClientEvents::entityRendererEvent);
        modEventBus.addListener(EFMItems::addCreative);

        MinecraftForge.EVENT_BUS.register(MenuEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonEvents.class);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        MinecraftForge.EVENT_BUS.register(LevelModEvents.class);
        MinecraftForge.EVENT_BUS.register(ShootExtension.class);

        BLOCKS.register(modEventBus);
        EFMItems.REGISTER.register(modEventBus);
        EFMEnchantments.REGISTER.register(modEventBus);
        EFMEntities.REGISTER.register(modEventBus);

        SyncedKeys.registerKeys();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            DropTables.readDropTables();
        });

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    @SubscribeEvent
    public void registerCommandsEvent(RegisterCommandsEvent event){
        ExportItemStackCommand.register(event.getDispatcher());
    }
}
