package net.kwzii.currencymod.item.custom;

import net.kwzii.currencymod.screen.WalletMenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WalletItem extends Item {
    private static final String INV_TAG = "wallet.inventory";
    private static final int INV_SIZE = 5;

    public WalletItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            player.openMenu(new WalletMenuProvider());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    // Method to save the items into the item's NBT data when the menu is closed
    public void saveItemsToNBT(ItemStack stack, ItemStack[] items) {
        CompoundTag tag = stack.getOrCreateTag();
        System.out.println("TAG" + tag);
        CompoundTag itemsTag = new CompoundTag();
        for (int i = 0; i < items.length; i++) {
            if (!items[i].isEmpty()) {
                System.out.println("SAVED " + items[i]);
                CompoundTag itemTag = new CompoundTag();
                items[i].save(itemTag);
                itemsTag.put(String.valueOf(i), itemTag);
            }
        }
        System.out.println("BEFORE TAGISEMPTY:" + tag.isEmpty());
        System.out.println("ITEMTAG: "+ itemsTag);
        tag.put(INV_TAG, itemsTag);
        System.out.println("AFTER TAGISEMPTY:" + tag.isEmpty());
        stack.setTag(tag);
        System.out.println(tag);
    }

    // Method to deserialize the NBT data and retrieve the items when the menu is opened
    public ItemStack[] loadItemsFromNBT(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        System.out.println("LOAD TAG =" + tag);
        if (tag != null && tag.contains(INV_TAG)) {
            System.out.println("CHECKPOINT");
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
        return new ItemStack[INV_SIZE];
    }
}
