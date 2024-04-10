package net.kwzii.hardcashmod.screen;

import net.kwzii.hardcashmod.HardCashMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Mod Menu Registration class
 * @author Travis Brown
 */
public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, HardCashMod.MOD_ID);

    public static final RegistryObject<MenuType<BasicMoneyPrinterMenu>> BASIC_MONEY_PRINTER_MENU =
            registerMenuType("basic_money_printer_menu", BasicMoneyPrinterMenu::new);

    public static final RegistryObject<MenuType<InkJuicerMenu>> INK_JUICER_MENU =
            registerMenuType("ink_juicer_menu", InkJuicerMenu::new);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    /**
     * Method to register an IEventBus with the mod menus
     * @param eventBus IEventBus to be registered
     */
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
