package net.kwzii.currencymod.datagen;

import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.item.ModItems;
import net.kwzii.currencymod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Mod Item Tag Generator Class
 * @author Travis Brown
 */
public class ModItemTagGenerator extends ItemTagsProvider {
    /**
     * Constructor for Mod Item Generator
     * @param p_275343_ the packoutput
     * @param p_275729_ the provider
     * @param p_275322_ the tag lookup
     * @param existingFileHelper the existing file helper
     */
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CurrencyMod.MOD_ID, existingFileHelper);
    }

    /**
     * Method to add items to custom tags
     * @param provider the provider
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.JARS).add(
                ModItems.EMPTY_JAR.get(),
                ModItems.MAGNETIC_INK.get(),
                ModItems.BLACK_INK.get(),
                ModItems.RED_INK.get(),
                ModItems.BLUE_INK.get(),
                ModItems.GREEN_INK.get(),
                ModItems.PINK_INK.get(),
                ModItems.WHITE_INK.get()
        );

        this.tag(ModTags.Items.BILLS).add(
                ModItems.ONE_DOLLAR_BILL.get(),
                ModItems.TEN_DOLLAR_BILL.get(),
                ModItems.TWENTY_DOLLAR_BILL.get(),
                ModItems.FIFTY_DOLLAR_BILL.get(),
                ModItems.HUNDRED_DOLLAR_BILL.get(),
                ModItems.BLACK_FAKE_MONEY.get(),
                ModItems.WHITE_FAKE_MONEY.get(),
                ModItems.RED_FAKE_MONEY.get(),
                ModItems.BLUE_FAKE_MONEY.get(),
                ModItems.GREEN_FAKE_MONEY.get(),
                ModItems.PINK_FAKE_MONEY.get(),
                ModItems.DARK_RED_FAKE_MONEY.get()
        );

        this.tag(ModTags.Items.PRINTING_PARCHMENT).add(
                Items.PAPER,
                ModItems.BLACK_PAPER.get(),
                ModItems.RED_PAPER.get(),
                ModItems.BLUE_PAPER.get(),
                ModItems.GREEN_PAPER.get(),
                ModItems.PINK_PAPER.get(),
                ModItems.DARK_RED_PAPER.get()
        );

        this.tag(ModTags.Items.STAMPS).add(
                ModItems.MONEY_STAMP.get(),
                ModItems.RECIPE_STAMP.get()
        );

        this.tag(ModTags.Items.INK_CREATING_ITEMS).add(
                // BLACK INK
                Items.INK_SAC,
                Items.BLACK_DYE,
                // RED INK
                Items.POPPY,
                Items.RED_DYE,
                // BLUE INK
                Items.CORNFLOWER,
                Items.BLUE_DYE,
                // GREEN INK
                Items.GREEN_DYE,
                // PINK INK
                Items.PINK_TULIP,
                Items.PEONY,
                Items.PINK_PETALS,
                Items.PINK_DYE,
                // WHITE INK
                Items.LILY_OF_THE_VALLEY,
                Items.WHITE_DYE
                );

        this.tag(ModTags.Items.BLACK_INK_CRAFTING).add(
                Items.INK_SAC,
                Items.BLACK_DYE
        );

        this.tag(ModTags.Items.RED_INK_CRAFTING).add(
                Items.POPPY,
                Items.RED_DYE
        );

        this.tag(ModTags.Items.BLUE_INK_CRAFTING).add(
                Items.CORNFLOWER,
                Items.BLUE_DYE
        );

        this.tag(ModTags.Items.GREEN_INK_CRAFTING).add(
                Items.GREEN_DYE
        );

        this.tag(ModTags.Items.PINK_INK_CRAFTING).add(
                Items.PINK_TULIP,
                Items.PEONY,
                Items.PINK_DYE,
                Items.PINK_PETALS
        );

        this.tag(ModTags.Items.WHITE_INK_CRAFTING).add(
                Items.LILY_OF_THE_VALLEY,
                Items.WHITE_DYE
        );
    }
}
