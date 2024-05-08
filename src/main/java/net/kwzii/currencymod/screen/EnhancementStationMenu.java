package net.kwzii.currencymod.screen;

import net.kwzii.currencymod.block.ModBlocks;
import net.kwzii.currencymod.block.entity.EnhancementStationBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class EnhancementStationMenu extends AbstractContainerMenu {
    public final EnhancementStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    /**
     * Constructor for the Enhancement Station Menu
     * Calls to other constructor
     */
    public EnhancementStationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    /**
     * Constructor for Enhancement Station Menu
     * Creates menu, calls methods to add the player inv and hotbar, and registers item slots in the menu
     * @param pContainerId the Container ID
     * @param inv the player inventory
     * @param entity the block entity
     * @param data the container data
     */
    public EnhancementStationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.ENHANCEMENT_STATION_MENU.get(), pContainerId);
        checkContainerSize(inv, 5);
        blockEntity = ((EnhancementStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 60, 8));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 84, 8));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 108, 8));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 132, 8));
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 134, 60) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });

        addDataSlots(data);
    }

    /**
     * Method to check if the enhancement station is progressing
     * Used to see if progress bar needs to be rendered
     * @return true if progress is greater than 0
     */
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    /**
     * Getter for the heat slider value
     * @return the value of the heat slider
     */
    public int getHeatValue() {
        return data.get(2);
    }

    /**
     * Getter for the aeration slider value
     * @return the value of the aeration slider
     */
    public int getAeratValue() {
        return data.get(3);
    }

    /**
     * Getter for the purification slider value
     * @return the value of the purification slider
     */
    public int getPurifValue() {
        return data.get(4);
    }

    /**
     * Method to get the current progress in % so the rendered progress arrow is accurate
     * @return % progress completed
     */
    public int getScaledProgress() {
        int progress = this.data.get(0);        // Progress
        int maxProgress = this.data.get(1);     // Max Progress
        int progressArrowSize = 29;     // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    // The quickMoveStack function allows for shift clicking items
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
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!

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
                pPlayer, ModBlocks.ENHANCEMENT_STATION.get()); // todo: revert this
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
}
