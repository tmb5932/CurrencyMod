package net.kwzii.currencymod.item.custom;

import net.kwzii.currencymod.screen.RecipePaperMenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Arrays;

/**
 * Class for Custom Recipe Paper Item that opens GUI
 * @author Travis Brown
 */
public class RecipePaperItem extends Item {
    private static final String INV_TAG = "recipe_paper.inventory";
    private static final String SLIDERS_TAG = "recipe_paper.sliders";
    private static final int INV_SIZE = 5;

    /**
     * Constructor for Recipe Paper Item
     * @param pProperties the block properties
     */
    public RecipePaperItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    /**
     * The use method, that is called everytime the item is used with this item
     * @param pLevel the world
     * @param pPlayer the player who clicked
     * @param pUsedHand the hand clicked with
     * @return the interaction result of the click
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            pPlayer.openMenu(new RecipePaperMenuProvider());
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        } else
            return super.use(pLevel, pPlayer, pUsedHand);
    }

    /**
     * Method to save the items in the recipe paper menu into the recipe paper's NBT data when the menu is closed
     * @param stack the recipe paper instance that the data will be stored to
     * @param items the items being stored
     */
    public void saveItemsToNBT(ItemStack stack, ItemStack[] items) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag itemsTag = new CompoundTag();
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                items[i].save(itemTag);
                itemsTag.put(String.valueOf(i), itemTag);
            }
        }
        tag.put(INV_TAG, itemsTag);
        stack.setTag(tag);
    }

    /**
     * Method to deserialize the NBT data and retrieve the items when the menu is opened
     * @param stack the wallet instance
     * @return the ItemStack array of items stored
     */
    public ItemStack[] loadItemsFromNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(INV_TAG)) {
            CompoundTag itemsTag = tag.getCompound(INV_TAG);
            ItemStack[] items = new ItemStack[INV_SIZE];
            for (int i = 0; i < items.length; i++) {
                if (itemsTag.contains(String.valueOf(i))) {
                    CompoundTag itemTag = itemsTag.getCompound(String.valueOf(i));
                    items[i] = ItemStack.of(itemTag);
                } else {
                    items[i] = ItemStack.EMPTY;
                }
            }
            return items;
        }

        ItemStack[] result = new ItemStack[INV_SIZE];
        Arrays.fill(result, ItemStack.EMPTY);
        return result;
    }

    public void saveSlidersToNBT(ItemStack stack, double[] values) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag sliderTag = new CompoundTag();
        System.out.println("NBT:" + Arrays.toString(values));
        for (int i = 0; i < values.length; i++) {
            sliderTag.putDouble(String.valueOf(i), values[i]);
        }
        tag.put(SLIDERS_TAG, sliderTag);
        System.out.println(tag);
        stack.setTag(tag);
    }

    public double[] loadSlidersFromNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(SLIDERS_TAG)) {
            CompoundTag sliderTag = tag.getCompound(SLIDERS_TAG);
            double[] values = new double[3];
            for (int i = 0; i < values.length; i++) {
                if (sliderTag.contains(String.valueOf(i))) {
                    CompoundTag itemTag = sliderTag.getCompound(String.valueOf(i));
                    values[i] = itemTag.getDouble(String.valueOf(i));
                } else {
                    values[i] = 0;
                }
            }
            return values;
        }

        double[] result = new double[3];
        Arrays.fill(result, 0.0);
        return result;
    }
}