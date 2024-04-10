package net.kwzii.hardcashmod.block.custom;

import net.kwzii.hardcashmod.block.entity.InkJuicerBlockEntity;
import net.kwzii.hardcashmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * Custom Ink Juicer block class
 * @author Travis Brown
 */
public class InkJuicerBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 19, 13);

    /**
     * Creator for Ink Juicer block
     * @param pProperties the block properties[
     */
    public InkJuicerBlock(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Overrides getShape to give a proper hitbox for the ink juicer block
     * @param pState the block state
     * @param pLevel the world instance
     * @param pPos the block position
     * @param pContext the collision context of the shape
     * @return VoxelShape custom-made hitbox for the juicer block entity
     */
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    /**
     * Override method getRenderShape to return the custom ink juicer block model
     * @param pState the block state
     * @return the RenderShape.MODEL
     */
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    /**
     * Override method onRemove to drop the items inside the juicer when broken
     * @param pState the block state
     * @param pLevel the world instance
     * @param pPos the block position
     * @param pNewState the new block state
     * @param pMovedByPiston boolean if moved by piston
     */
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InkJuicerBlockEntity) {
                ((InkJuicerBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    /**
     * The interaction method to open ink juicer block GUI when right-clicked
     * @param pState the block state
     * @param pLevel the world instance
     * @param pPos the block position
     * @param pPlayer the player who right-clicked
     * @param pHand the hand right-clicked with
     * @param pHit The block hit result
     * @return Interaction result if the interaction was successful
     */
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof InkJuicerBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (InkJuicerBlockEntity)entity, pPos); // ONLY WORKS IN 1.20.1, NOT NEWER!!
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    /**
     * The block entity creator
     * @param blockPos the block position
     * @param blockState the block state
     * @return the BlockEntity that is created
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InkJuicerBlockEntity(blockPos, blockState);
    }

    /**
     * Method that creates a new tickHelper for the tick method in the block entity
     * @param pLevel the world instance
     * @param pState the block state
     * @param pBlockEntityType the block entity type
     * @return the TickerHelper
     * @param <T> The block entity
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.INK_JUICE_BE.get(),
                (level, blockPos, blockState, pBlockEntity) -> pBlockEntity.tick(level, blockPos, blockState));
    }

}
