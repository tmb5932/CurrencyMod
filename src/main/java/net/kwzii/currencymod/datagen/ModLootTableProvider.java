package net.kwzii.currencymod.datagen;

import net.kwzii.currencymod.datagen.loot.ModBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

/**
 * Mod Loot Table Provider
 * @author Travis Brown
 */
public class ModLootTableProvider {
    /**
     * Method to create the loot tables of custom blocks
     * @param output the PackOutput
     * @return the loot table provider of the ModBlockLottTables class
     */
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
