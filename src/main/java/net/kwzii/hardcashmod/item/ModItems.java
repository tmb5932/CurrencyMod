package net.kwzii.hardcashmod.item;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.item.custom.DiceItem;
import net.kwzii.hardcashmod.item.custom.DoubleDiceItem;
import net.kwzii.hardcashmod.item.custom.FuelItem;
import net.kwzii.hardcashmod.item.custom.MetalDetectorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Class of all custom items added
 * @author Travis Brown
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HardCashMod.MOD_ID);

    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SAPPHIRE = ITEMS.register("raw_sapphire",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAXWELL = ITEMS.register("maxwell",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DOLLAR_BILL = ITEMS.register("dollar_bill",
            () -> new FuelItem(new Item.Properties(), 400));

    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));

    public static final RegistryObject<Item> DICE = ITEMS.register("dice",
            () -> new DiceItem(new Item.Properties()));

    public static final RegistryObject<Item> DOUBLE_DICE = ITEMS.register("double_dice",
            () -> new DoubleDiceItem(new Item.Properties()));

    public static final RegistryObject<Item> EMPTY_JAR = ITEMS.register("empty_jar",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MAGNETIC_INK = ITEMS.register("magnetic_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> BLACK_INK = ITEMS.register("black_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> RED_INK = ITEMS.register("red_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> BLUE_INK = ITEMS.register("blue_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> GREEN_INK = ITEMS.register("green_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> PINK_INK = ITEMS.register("pink_ink",
            () -> new Item(new Item.Properties().durability(750)));

    public static final RegistryObject<Item> BLACK_PAPER = ITEMS.register("black_paper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RED_PAPER = ITEMS.register("red_paper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> BLUE_PAPER = ITEMS.register("blue_paper",
        () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> GREEN_PAPER = ITEMS.register("green_paper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> PINK_PAPER = ITEMS.register("pink_paper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> DARK_RED_PAPER = ITEMS.register("dark_red_paper",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * Registers an IEventBus with the mod items
     * @param eventBus the IEventBus to be registered
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
