package net.kwzii.currencymod.item;

import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.item.custom.*;
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
            DeferredRegister.create(ForgeRegistries.ITEMS, CurrencyMod.MOD_ID);

    // Maxwell Item :)
    public static final RegistryObject<Item> MAXWELL = ITEMS.register("maxwell",
            () -> new Item(new Item.Properties()));

    // Sapphire Items
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_SAPPHIRE = ITEMS.register("raw_sapphire",
            () -> new Item(new Item.Properties()));

    // Currency Items
    public static final RegistryObject<Item> ONE_DOLLAR_BILL = ITEMS.register("one_dollar_bill",
            () -> new DollarBillItem(new Item.Properties(), 1));

    public static final RegistryObject<Item> TEN_DOLLAR_BILL = ITEMS.register("ten_dollar_bill",
            () -> new DollarBillItem(new Item.Properties(), 10));

    public static final RegistryObject<Item> TWENTY_DOLLAR_BILL = ITEMS.register("twenty_dollar_bill",
            () -> new DollarBillItem(new Item.Properties(), 20));

    public static final RegistryObject<Item> FIFTY_DOLLAR_BILL = ITEMS.register("fifty_dollar_bill",
            () -> new DollarBillItem(new Item.Properties(), 50));

    public static final RegistryObject<Item> HUNDRED_DOLLAR_BILL = ITEMS.register("hundred_dollar_bill",
            () -> new DollarBillItem(new Item.Properties(), 100));

    // Ore Searching Item
    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));

    // Gambling Items
    public static final RegistryObject<Item> DICE = ITEMS.register("dice",
            () -> new DiceItem(new Item.Properties()));

    public static final RegistryObject<Item> DOUBLE_DICE = ITEMS.register("double_dice",
            () -> new DoubleDiceItem(new Item.Properties()));

    // Ink Jar Items
    public static final RegistryObject<Item> EMPTY_JAR = ITEMS.register("empty_jar",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MAGNETIC_INK = ITEMS.register("magnetic_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> BLACK_INK = ITEMS.register("black_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> RED_INK = ITEMS.register("red_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> BLUE_INK = ITEMS.register("blue_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> GREEN_INK = ITEMS.register("green_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> PINK_INK = ITEMS.register("pink_ink",
            () -> new Item(new Item.Properties().durability(800)));

    public static final RegistryObject<Item> WHITE_INK = ITEMS.register("white_ink",
            () -> new Item(new Item.Properties().durability(800)));

    // Paper Items
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

    // Stamp Items
    public static final RegistryObject<Item> RECIPE_STAMP = ITEMS.register("recipe_stamp",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MONEY_STAMP = ITEMS.register("money_stamp",
            () -> new Item(new Item.Properties().stacksTo(1)));

    // Recipe Paper Items


    /**
     * Registers an IEventBus with the mod items
     * @param eventBus the IEventBus to be registered
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
