package net.kwzii.hardcashmod.datagen;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.block.ModBlocks;
import net.kwzii.hardcashmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HardCashMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES).add(
                ModBlocks.SAPPHIRE_ORE.get(),
                Blocks.COPPER_ORE,
                Blocks.IRON_ORE,
                Blocks.GOLD_ORE,
                Blocks.ANCIENT_DEBRIS);
//                .addTag(Tags.Blocks.ORES); // command for all base minecraft ores

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
                ModBlocks.BASIC_MONEY_PRINTER.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                ModBlocks.RAW_SAPPHIRE_BLOCK.get(),
                ModBlocks.SAPPHIRE_ORE.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                ModBlocks.SAPPHIRE_BLOCK.get());

//        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
//                .add();

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.RAW_SAPPHIRE_BLOCK.get(),
                ModBlocks.SAPPHIRE_BLOCK.get(),
                ModBlocks.SAPPHIRE_ORE.get());

//        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.);

//        this.tag(BlockTags.MINEABLE_WITH_HOE).add

//        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add
    }
}
