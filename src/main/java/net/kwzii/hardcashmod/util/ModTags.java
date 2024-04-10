package net.kwzii.hardcashmod.util;

import net.kwzii.hardcashmod.HardCashMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Custom Mod Tags class
 * @author Travis Brown
 */
public class ModTags {

    /**
     * Custom Mod Tags for Blocks
     */
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");

        /**
         * Method to create a new custom tag for blocks
         * @param name the name of the tag
         * @return the TagKey
         */
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(HardCashMod.MOD_ID, name));
        }
    }

    /**
     * Custom Mod Tags for Items
     */
    public static class Items {
        public static final TagKey<Item> BILLS = tag("cash_bills");
        public static final TagKey<Item> JARS = tag("ink_jars");
        public static final TagKey<Item> PRINTING_PARCHMENT = tag("printing_parchment");

        public static final TagKey<Item> INK_CREATING_ITEMS = tag("ink_crafting");
        public static final TagKey<Item> BLACK_INK_CRAFTING = tag("black_ink_crafting");
        public static final TagKey<Item> RED_INK_CRAFTING = tag("red_ink_crafting");
        public static final TagKey<Item> BLUE_INK_CRAFTING = tag("blue_ink_crafting");
        public static final TagKey<Item> GREEN_INK_CRAFTING = tag("green_ink_crafting");
        public static final TagKey<Item> PINK_INK_CRAFTING = tag("pink_ink_crafting");

        /**
         * Method to create a new custom tag for items
         * @param name the name of the tag
         * @return the TagKey
         */
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(HardCashMod.MOD_ID, name));
        }
    }
}
