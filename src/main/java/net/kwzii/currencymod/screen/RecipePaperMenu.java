package net.kwzii.currencymod.screen;

import net.kwzii.currencymod.item.ModItems;
import net.kwzii.currencymod.item.custom.RecipePaperItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class RecipePaperMenu extends AbstractContainerMenu {
    private final Container data;
    private final ItemStack itemStack;
    private final ItemStack[] items;
    private final double[] sliderVals;

    public RecipePaperMenu(int id, Inventory inv, FriendlyByteBuf friendlyByteBuf) {
        this(id, inv);
    }

    public RecipePaperMenu(int pContainerId, Inventory inv) {
        super(ModMenuTypes.RECIPE_PAPER_MENU.get(), pContainerId);
        checkContainerSize(inv, 5);
        this.data = new SimpleContainer(8);
        itemStack = inv.player.getItemInHand(InteractionHand.MAIN_HAND);

        items = ((RecipePaperItem) itemStack.getItem()).loadItemsFromNBT(itemStack);
        sliderVals = ((RecipePaperItem) itemStack.getItem()).loadSlidersFromNBT(itemStack);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        for (int i = 0; i < 4; i++) {   // Create the 4 ingredients slots
            addSlot(new Slot(data, i, 59 + i * 19, 33)).set(items[i]); // todo: get correct x,y
        }
        // Create the result slot
        addSlot(new Slot(data, 4, 114, 76)).set(items[4]); // todo: get correct x,y
    }

    private static final int HOTBAR_SLOT_COUNT = 9; // 9 slots on hot bar
    private static final int PLAYER_INVENTORY_SLOT_COUNT = 9 * 3;   // 9 COLUMNS x 3 ROWS of base inventory
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_SLOT_COUNT;
    private static final int TE_SLOT_COUNT = 5; // Num of custom slots in menu

    /**
     * Method to pickup/interact with item in inventory
     * Method makes it so the wallet cannot be moved or altered while its menu is open
     *
     * @param pSlotId    the slot that was clicked
     * @param pButton    the button it was pressed with
     * @param pClickType the click type
     * @param pPlayer    the player
     */
    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        if (pSlotId > 0 && pSlotId < VANILLA_SLOT_COUNT && slots.get(pSlotId).hasItem() && slots.get(pSlotId).getItem().is(ModItems.WALLET.get())) {
            return;
        }
        super.clicked(pSlotId, pButton, pClickType, pPlayer);

    }

    /**
     * Method to allow for shift clicking an item and moving it around the inventory
     *
     * @param player    the player
     * @param slotIndex the slot being moved
     * @return the ItemStack
     */
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack;
        Slot slot = this.slots.get(slotIndex);

        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;
        ItemStack slotStack = slot.getItem();
        itemstack = slotStack.copy();

        if (slotIndex < TE_INVENTORY_FIRST_SLOT_INDEX) {
            // Transfer from player inventory to custom slots
            if (!this.moveItemStackTo(slotStack, TE_INVENTORY_FIRST_SLOT_INDEX, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Transfer from custom slots to player inventory
            if (!this.moveItemStackTo(slotStack, 0, VANILLA_SLOT_COUNT, true)) {
                return ItemStack.EMPTY;
            }
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (slotStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, slotStack);
        return itemstack;
    }

    /**
     * Method to add the players inventory to the Printer menu
     * @param playerInventory the players inventory to be added
     */
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    /**
     * Method to add the players inventory to the Printer menu
     * @param playerInventory the players inventory to be added
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    /**
     * Checks for validity of whether the menu is still valid to keep open
     * Doesn't really have a good way of checking when it's an item.
     * For blocks, it's a distance thing, but for items they are always going be in your hand
     * @param pPlayer the player
     * @return true
     */
    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    /**
     * Method to determine what should be done when menu is closed
     * Saves the items that are in the recipe paper
     * @param pPlayer the player
     */
    @Override
    public void removed(Player pPlayer) {
        for (int i = 0; i < TE_SLOT_COUNT; i++) {
            items[i] = slots.get(TE_INVENTORY_FIRST_SLOT_INDEX+i).getItem();
        }

        super.removed(pPlayer);
        ((RecipePaperItem) itemStack.getItem()).saveItemsToNBT(itemStack, items);
        saveSliderData(sliderVals);
    }

    public double getSliderData(int i) {
        return sliderVals[i];
    }

    public void setSliderData(int i, double val) {
        sliderVals[i] = val;
    }

    public void saveSliderData(double[] i) {
        ((RecipePaperItem) itemStack.getItem()).saveSlidersToNBT(itemStack, i);
    }
}
