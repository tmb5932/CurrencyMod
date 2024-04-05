package net.kwzii.hardcashmod.item;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.item.custom.DiceItem;
import net.kwzii.hardcashmod.item.custom.DoubleDiceItem;
import net.kwzii.hardcashmod.item.custom.FuelItem;
import net.kwzii.hardcashmod.item.custom.MetalDetectorItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

    public static final RegistryObject<Item> MAGNETIC_INK = ITEMS.register("magnetic_ink",
            () -> new Item(new Item.Properties().durability(100)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
