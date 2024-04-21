package net.kwzii.currencymod.datagen;

import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

/**
 * Mod Item datagen class
 * @author Travis Brown
 */
public class ModItemModelProvider extends ItemModelProvider {
    /**
     * Constructor for mod item data gen
     * @param output PackOutput
     * @param existingFileHelper the existing file helper
     */
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CurrencyMod.MOD_ID, existingFileHelper);
    }

    /**
     * Method to register the custom mod item models
     */
    @Override
    protected void registerModels() {
        simpleItem(ModItems.MAXWELL);

        simpleItem(ModItems.DICE);
        simpleItem(ModItems.DOUBLE_DICE);

        simpleItem(ModItems.EMPTY_JAR);
        simpleItem(ModItems.MAGNETIC_INK);
        simpleItem(ModItems.BLACK_INK);
        simpleItem(ModItems.WHITE_INK);
        simpleItem(ModItems.RED_INK);
        simpleItem(ModItems.BLUE_INK);
        simpleItem(ModItems.GREEN_INK);
        simpleItem(ModItems.PINK_INK);

        simpleItem(ModItems.BLACK_PAPER);
        simpleItem(ModItems.RED_PAPER);
        simpleItem(ModItems.BLUE_PAPER);
        simpleItem(ModItems.GREEN_PAPER);
        simpleItem(ModItems.PINK_PAPER);
        simpleItem(ModItems.DARK_RED_PAPER);

        simpleItem(ModItems.MONEY_STAMP);
        simpleItem(ModItems.RECIPE_STAMP);

        simpleItem(ModItems.RAW_SAPPHIRE);
        simpleItem(ModItems.SAPPHIRE);

        simpleItem(ModItems.METAL_DETECTOR);

        simpleItem(ModItems.ONE_DOLLAR_BILL);
        simpleItem(ModItems.TEN_DOLLAR_BILL);
        simpleItem(ModItems.TWENTY_DOLLAR_BILL);
        simpleItem(ModItems.FIFTY_DOLLAR_BILL);
        simpleItem(ModItems.HUNDRED_DOLLAR_BILL);

        simpleItem(ModItems.BLACK_FAKE_MONEY);
        simpleItem(ModItems.WHITE_FAKE_MONEY);
        simpleItem(ModItems.RED_FAKE_MONEY);
        simpleItem(ModItems.BLUE_FAKE_MONEY);
        simpleItem(ModItems.GREEN_FAKE_MONEY);
        simpleItem(ModItems.PINK_FAKE_MONEY);
        simpleItem(ModItems.DARK_RED_FAKE_MONEY);

        simpleItem(ModItems.BLACK_RECIPE_PAPER);
        simpleItem(ModItems.WHITE_RECIPE_PAPER);
        simpleItem(ModItems.RED_RECIPE_PAPER);
        simpleItem(ModItems.BLUE_RECIPE_PAPER);
        simpleItem(ModItems.GREEN_RECIPE_PAPER);
        simpleItem(ModItems.PINK_RECIPE_PAPER);
        simpleItem(ModItems.DARK_RED_RECIPE_PAPER);

        simpleItem(ModItems.WALLET);
        // Creates Item models for blocks that have a non data gen block state and model
        withExistingParent("basic_money_printer", modLoc("block/basic_money_printer"));
        withExistingParent("ink_juicer", modLoc("block/ink_juicers/ink_juicer0"));
        withExistingParent("stamper", modLoc("block/stamper"));
    }

    /**
     * Method to create a simple item
     * @param item the custom mod item to be created
     * @return the ItemModelBuilder
     */
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CurrencyMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
