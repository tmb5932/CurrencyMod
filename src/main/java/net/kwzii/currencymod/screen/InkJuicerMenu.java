package net.kwzii.currencymod.screen;

import net.kwzii.currencymod.block.ModBlocks;
import net.kwzii.currencymod.block.entity.InkJuicerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Ink Juicer Menu Class
 * @author Travis Brown
 */
public class InkJuicerMenu extends AbstractContainerMenu {
    public final InkJuicerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    /**
     * Constructor for the Ink Juicer Menu
     * Calls to other constructor
     */
    public InkJuicerMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    /**
     * Constructor for Ink Juicer Menu
     * Creates the menu, adds the player inventory and hotbar, and adds the item slots
     * @param pContainerId the container ID
     * @param inv the player inventory
     * @param entity the block entity
     * @param data the container data
     */
    public InkJuicerMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.INK_JUICER_MENU.get(), pContainerId);
        checkContainerSize(inv,2);
        blockEntity = ((InkJuicerBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 48, 16));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 48, 52));
        });

        addDataSlots(data);
    }

    /**
     * Method to see if the block entity has input progress
     * @return true if the block entities input progress is greater than 0
     */
    public boolean isJuicing() {
        return data.get(0) > 0;
    }

    /**
     * Method to see if the block entity has output progress
     * @return true if the block entities output progress is greater than 0
     */
    public boolean isPouring() {
        return data.get(2) > 0;
    }

    /**
     * Method to get the current input progress in % so the rendered progress arrow is accurate
     * @return % input progress completed
     */
    public int getScaledInputProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 22; // Length of arrow in pixels

        return maxProgress != 0 && progress != 0 ? (progress * progressArrowSize) / maxProgress : 0;
    }

    /**
     * Method to get the current output progress in % so the rendered progress arrow is accurate
     * @return % output progress completed
     */
    public int getScaledOutputProgress() {
        int progress = this.data.get(2);
        int maxProgress = this.data.get(3);
        int progressArrowSize = 22; // Length of arrow in pixels

        return maxProgress != 0 && progress != 0 ? (progress * progressArrowSize) / maxProgress : 0;
    }

    /**
     * Method to get the current liquid stored in % so the rendered liquid storage tank is accurate
     * @return % current liquid stored completed
     */
    public int getScaledLiquid() {
        int currentLiquid = this.data.get(4);
        int maxLiquid = this.data.get(5);
        int liquidContainerSize = 52; // Height of liquid storage in pixels

        return maxLiquid != 0 && currentLiquid != 0 ? (currentLiquid * liquidContainerSize) / maxLiquid : 0;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    /**
     * Checks for validity
     * @param pPlayer the player
     * @return true if stillValid call is true, false otherwise
     */
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.INK_JUICER.get());
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
     * Method to get the color the stored liquid should be
     * Used by screen to render the liquid tank the right color
     * Gets the stored current liquid type and returns that color
     * @return color hexcode for the liquid to be rendered as
     */
    public int getInkColor() {
        switch(data.get(6)) {
            case 0 -> { return 0x8b8b8b; }      // No Ink
            case 1 -> { return 0xFF000000; }    // Black
            case 2 -> { return 0xFFFFFFFF; }    // White
            case 3 -> { return 0xFFFF0000; }    // Red
            case 4 -> { return 0xFF0000FF; }    // Blue
            case 5 -> { return 0xFF00FF00; }    // Green
            case 6 -> { return 0xFFFF69B4; }    // Pink
            case 7 -> { return 0xFF200000; }    // Metallic Red
            default -> { return 0x8b8b8b; }     // Grey
        }
    }
}
