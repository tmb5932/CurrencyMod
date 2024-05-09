package net.kwzii.currencymod;

import com.mojang.logging.LogUtils;
import net.kwzii.currencymod.block.ModBlocks;
import net.kwzii.currencymod.block.entity.ModBlockEntities;
import net.kwzii.currencymod.item.ModCreativeModTabs;
import net.kwzii.currencymod.item.ModItems;
import net.kwzii.currencymod.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main Class for Currency Mod
 * @author Travis Brown
 */
@Mod(CurrencyMod.MOD_ID)
public class CurrencyMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "currencymod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for CurrencyMod
     */
    public CurrencyMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    /**
     * Common Setup
     * @param event the FML Common Setup Event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    /**
     * Method to add blocks to the creative menus
     * @param event the Creative Mod Tab Content Event
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    /**
     * Method called when server starts
     * @param event Server Starting Event
     */
    @SubscribeEvent     // You can use SubscribeEvent and let the Event Bus discover methods to call
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Currency Mod Started");
    }

    /**
     * Method for Client Mod Events
     */
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    public static class ClientModEvents {

        /**
         * The on Client Startup call
         * @param event FML Client Setup Event
         */
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Registers Menus with the screens
            MenuScreens.register(ModMenuTypes.BASIC_MONEY_PRINTER_MENU.get(), BasicMoneyPrinterScreen::new);
            MenuScreens.register(ModMenuTypes.INK_JUICER_MENU.get(), InkJuicerScreen::new);
            MenuScreens.register(ModMenuTypes.STAMPER_MENU.get(), StamperScreen::new);
            MenuScreens.register(ModMenuTypes.WALLET_MENU.get(), WalletScreen::new);
            MenuScreens.register(ModMenuTypes.ENHANCEMENT_STATION_MENU.get(), EnhancementStationScreen::new);

            // Sets render of object to translucent so opaque things will be seen through
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.INK_JUICER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENHANCEMENT_STATION.get(), RenderType.translucent());
        }
    }
}
