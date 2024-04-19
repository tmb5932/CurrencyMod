package net.kwzii.currencymod.block.entity;

import net.kwzii.currencymod.item.ModItems;
import net.kwzii.currencymod.screen.StamperMenu;
import net.kwzii.currencymod.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stamper Custom Block Entity
 * @author Travis Brown
 */
public class StamperBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int STAMP_SLOT = 2;
    private static final int INK_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 50;

    /**
     * Constructor for Stamper Block Entity
     * @param pPos the position is it placed in the world
     * @param pBlockState the state of the block when it is placed
     */
    public StamperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STAMPER_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> StamperBlockEntity.this.progress;
                    case 1 -> StamperBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> StamperBlockEntity.this.progress = i1;
                    case 1 -> StamperBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    /**
     * Adds the lazyItemHandler to the getCapability() call
     * @param cap the capability
     * @param side the direction side
     * @return the LazyItemHandler
     * @param <T> capability type
     */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * Adds the lazyItemHandler to the onLoad() call
     */
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    /**
     * Adds the lazyItemHandler to the invalidateCaps() method call
     */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    /**
     * Method to make the inventory contents drop when block broken
     */
    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.currencymod.stamper");
    }

    /**
     * Creates GUI menu for the Stamp block entity
     * @param pContainerId the int container ID
     * @param inv the player inventory
     * @param player the player
     * @return new Basic Money Printer Menu instance
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inv, Player player) {
        return new StamperMenu(pContainerId, inv, this, this.data);
    }

    /**
     * Method to save additional information to server files
     * @param pTag data structure used to store information
     */
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("stamper.inventory", itemHandler.serializeNBT());
        pTag.putInt("stamper.progress", progress);
        super.saveAdditional(pTag);
    }

    /**
     * Loads saved information about block
     * @param pTag data structure used to store information
     */
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("stamper.inventory"));
        progress = pTag.getInt("stamper.progress");
    }

    /**
     * Method that runs every tick
     * Checks if crafting is possible, and if so, starts process of crafting
     * @param pLevel the world instance the tick is happening on
     * @param pPos the block's position
     * @param pState the state of the block
     */
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        ItemStack result = getOutputItem();
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void increaseCraftingProgress() {
        ItemStack ink = this.itemHandler.getStackInSlot(INK_SLOT);
        int damage = 1;

        if (ink.getDamageValue() + damage >= ink.getMaxDamage()) {
            this.itemHandler.extractItem(INK_SLOT, 1, false);
            this.itemHandler.setStackInSlot(INK_SLOT, new ItemStack(ModItems.EMPTY_JAR.get(), 1));
        } else
            ink.setDamageValue(ink.getDamageValue() + damage);
        this.progress++;
    }

    private boolean hasRecipe() {
        boolean hasPaper = this.itemHandler.getStackInSlot(INPUT_SLOT).is(ModTags.Items.PRINTING_PARCHMENT);
        boolean hasStamp = this.itemHandler.getStackInSlot(STAMP_SLOT).is(ModTags.Items.STAMPS);
        boolean hasInk = this.itemHandler.getStackInSlot(INK_SLOT).is(ModTags.Items.JARS)
                && !this.itemHandler.getStackInSlot(INK_SLOT).is(ModItems.EMPTY_JAR.get());

        ItemStack result = getOutputItem();
        return hasPaper && hasStamp && hasInk && compatibleOutputSlot(result.getItem())
                && outputStackHasSpace(result.getCount());
    }

    private boolean outputStackHasSpace(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count
                <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean compatibleOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()
                || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private ItemStack getOutputItem() {
        Item ink = this.itemHandler.getStackInSlot(INK_SLOT).getItem();
        Item paper = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem();
        Item stamp = this.itemHandler.getStackInSlot(STAMP_SLOT).getItem();
        Item result;

        if (stamp == ModItems.MONEY_STAMP.get()) {
            if (paper == ModItems.BLACK_PAPER.get()) {
                result = ModItems.BLACK_FAKE_MONEY.get();
            } else if (paper == Items.PAPER) {
                result = ModItems.WHITE_FAKE_MONEY.get();
            } else if (paper == ModItems.RED_PAPER.get()) {
                result = ModItems.RED_FAKE_MONEY.get();
            } else if (paper == ModItems.BLUE_PAPER.get()) {
                result = ModItems.BLUE_FAKE_MONEY.get();
            } else if (paper == ModItems.GREEN_PAPER.get()) {
                if (ink == ModItems.MAGNETIC_INK.get())
                    result = ModItems.ONE_DOLLAR_BILL.get();
                else
                    result = ModItems.GREEN_FAKE_MONEY.get();
            } else if (paper == ModItems.PINK_PAPER.get()) {
                result = ModItems.PINK_FAKE_MONEY.get();
            } else {
                throw new IllegalStateException("!!! CurrencyMod: Cannot find output item");
            }
        } else if (stamp == ModItems.RECIPE_STAMP.get()) {
            if (paper == ModItems.BLACK_PAPER.get()) {
                result = ModItems.BLACK_RECIPE_PAPER.get();
            } else if (paper == Items.PAPER) {
                result = ModItems.WHITE_RECIPE_PAPER.get();
            } else if (paper == ModItems.RED_PAPER.get()) {
                result = ModItems.RED_RECIPE_PAPER.get();
            } else if (paper == ModItems.BLUE_PAPER.get()) {
                result = ModItems.BLUE_RECIPE_PAPER.get();
            } else if (paper == ModItems.GREEN_PAPER.get()) {
                result = ModItems.GREEN_RECIPE_PAPER.get();
            } else if (paper == ModItems.PINK_PAPER.get()) {
                result = ModItems.PINK_RECIPE_PAPER.get();
            } else {
                throw new IllegalStateException("!!! CurrencyMod: Cannot find output item");
            }
        } else {
            throw new IllegalStateException("!!! CurrencyMod: Cannot find output item");
        }
        return new ItemStack(result);
    }
}
