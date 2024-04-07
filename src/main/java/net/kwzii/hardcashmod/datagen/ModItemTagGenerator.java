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
                ModItems.MAGNETIC_INK.get());

        this.tag(ModTags.Items.BILLS).add(
                ModItems.DOLLAR_BILL.get());

        this.tag(ModTags.Items.PRINTING_PARCHMENT).add(
                Items.MAP,
                Items.PAPER
        );
    }
}
