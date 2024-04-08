package net.kwzii.hardcashmod.datagen;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.item.ModItems;
import net.kwzii.hardcashmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, HardCashMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.JARS).add(
                ModItems.EMPTY_JAR.get(),
                ModItems.MAGNETIC_INK.get(),
                ModItems.BLACK_INK.get(),
                ModItems.RED_INK.get(),
                ModItems.BLUE_INK.get(),
                ModItems.GREEN_INK.get(),
                ModItems.PINK_INK.get()
        );

        this.tag(ModTags.Items.BILLS).add(
                ModItems.DOLLAR_BILL.get());

        this.tag(ModTags.Items.PRINTING_PARCHMENT).add(
                Items.MAP,
                Items.PAPER
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
                Items.PINK_DYE,
                Items.PINK_PETALS);

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
    }
}
