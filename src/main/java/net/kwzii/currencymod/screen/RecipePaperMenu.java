package net.kwzii.currencymod.screen;

import net.kwzii.currencymod.item.entity.RecipePaperItemEntity;
import net.kwzii.currencymod.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class RecipePaperMenu extends AbstractContainerMenu {
    public final RecipePaperItemEntity itemEntity;
    private final ContainerData data;
    private final Level level;
    private final double xCoord;
    private final double yCoord;
    private final double zCoord;

    public RecipePaperMenu(int id, Inventory inv, FriendlyByteBuf friendlyByteBuf) {
        this(id, inv, inv.player.getItemInHand(InteractionHand.MAIN_HAND).getEntityRepresentation(), new SimpleContainerData(3));
    }

    public RecipePaperMenu(int pContainerId, Inventory inv, Entity entity, ContainerData data) {
        super(ModMenuTypes.RECIPE_PAPER_MENU.get(), pContainerId);
        checkContainerSize(inv, 5);
        this.data = data;
        this.level = inv.player.level();
        this.itemEntity = (RecipePaperItemEntity) entity;
        xCoord = inv.player.getX();
        yCoord = inv.player.getY();
        zCoord = inv.player.getZ();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.itemEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 59, 33) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getCount() == 1;
                }
            });
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 77, 33) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getCount() == 1;
                }
            }); // todo : do these (x,y)'s
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 97, 33) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getCount() == 1;
                }
            });
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 118, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.getCount() == 1;
                }
            });
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 114, 76) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(Items.POTION);
                }
            });
        });

        addDataSlots(data);
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
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!
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
     * Method to check if player is still close enough to see container
     * @param player the player
     * @return true if close enough to container
     */
    @Override
    public boolean stillValid(Player player) {
        // Check if the player is within a certain distance from the container
        if (player.distanceToSqr(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) > 15) {
            return false; // Player is too far away
        }

        return true;
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
