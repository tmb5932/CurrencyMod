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
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
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
            System.out.println("COUNT IS AT -> " + i);
            if (!items[i].isEmpty()) {
                System.out.println(items[i]);
                CompoundTag itemTag = new CompoundTag();
                items[i].save(itemTag);
                itemsTag.put(String.valueOf(i), itemTag);
            }
        }

        tag.put(INV_TAG, itemsTag);
        stack.setTag(tag);
        System.out.println("SECONDARY FULL TAG SAVED:" + stack.getTag());
    }

    /**
     * Method to deserialize the NBT data and retrieve the items when the menu is opened
     * @param stack the recipe paper instance
     * @return the ItemStack array of items stored
     */
    public ItemStack[] loadItemsFromNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        System.out.println((tag == null) + "TRUTH FACTOR 1?");
        if (tag != null)
            System.out.println((tag.contains(INV_TAG)) + "TRUTH FACTOR 2?");


        if (tag != null && tag.contains(INV_TAG)) {
            CompoundTag itemsTag = tag.getCompound(INV_TAG);
            ItemStack[] items = new ItemStack[INV_SIZE+3];
            for (int i = 0; i < items.length; i++) {
                System.out.println("LOAD COUNT IS AT -> " + i);
                if (itemsTag.contains(String.valueOf(i))) {
                    CompoundTag itemTag = itemsTag.getCompound(String.valueOf(i));
                    items[i] = ItemStack.of(itemTag);
                    System.out.println(items[i]);
                } else {
                    items[i] = ItemStack.EMPTY;
                }
            }
            System.out.println(Arrays.toString(items));
            return items;
        }
        System.out.println("LOADED ITEMS");
        ItemStack[] result = new ItemStack[INV_SIZE+3];
        Arrays.fill(result, ItemStack.EMPTY);
        return result;
    }

    public double[] loadSlidersFromNBT(ItemStack stack) {
        if (!stack.isEmpty()) {
            CompoundTag tag = stack.getTag();
            double[] result = new double[3];
            System.out.println("THE TAG ON START :" + tag);

            if (tag != null && tag.contains(SLIDERS_TAG)) {
                CompoundTag slidersTag = tag.getCompound(SLIDERS_TAG);
                if (slidersTag.contains("HeatVal")) {
                    result[0] = slidersTag.getDouble("HeatVal");
                }
                if (slidersTag.contains("AeroVal"))
                    result[1] = slidersTag.getDouble("AeroVal");
                if (slidersTag.contains("PurifVal"))
                    result[2] = slidersTag.getDouble("PurifVal");
            } else {
                System.out.println("No Recipe Paper Tag available");
            }
            System.out.println("THE SLIDER VALUES ARE OFFICIALLY " + Arrays.toString(result));
            return result;
        }
        System.out.println("FAILED LOADING SLIDERS");
        double[] result = new double[3];
        Arrays.fill(result, 0.0D);
        return result;
    }
}