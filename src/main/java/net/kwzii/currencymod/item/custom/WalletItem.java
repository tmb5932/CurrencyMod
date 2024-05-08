package net.kwzii.currencymod.item.custom;

import net.kwzii.currencymod.screen.WalletMenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Arrays;

/**
 * Class for Custom Wallet Item that opens GUI
 * @author Travis Brown
 */
public class WalletItem extends Item {
    private static final String INV_TAG = "wallet.inventory";
    private static final int INV_SIZE = 5;

    /**
     * Constructor for Wallet Item
     * @param pProperties the block properties
     */
    public WalletItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    /**
     * The use method, that is called everytime the item is used with this item
     * @param world the world
     * @param player the player who clicked
     * @param hand the hand clicked with
     * @return the interaction result of the click
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            player.openMenu(new WalletMenuProvider());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    //
    /**
     * Method to save the items in the wallets menu into the wallet's NBT data when the menu is closed
     * @param stack the wallet instance that the data will be stored to
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
}
