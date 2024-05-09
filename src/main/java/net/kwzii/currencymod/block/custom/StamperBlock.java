package net.kwzii.currencymod.block.custom;

import net.kwzii.currencymod.block.entity.ModBlockEntities;
import net.kwzii.currencymod.block.entity.StamperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

/**
 * Custom Stamper Block class
 * @author Travis Brown
 */
public class StamperBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    // TODO: Maybe add ability to copy a enchantment book, that uses special ink? EXP ink?
    public StamperBlock(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Overrides getShape to give a proper hitbox for the stamper block
     * @param pState the block state
     * @param pLevel the world instance
     * @param pPos the block position
     * @param pContext the collision context of the shape
     * @return VoxelShape custom-made hitbox for the stamper block entity
     */
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    /**
     * Override method getRenderShape to return the custom stamper block model
     * @param pState the block state
     * @return the RenderShape.MODEL
     */
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    // Facing Methods
    /**
     * Override method to get the placement state for the block
     * @param pContext the block placement context
     * @return the block state it should be placed in
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    /**
     * Overrides the rotate block method
     * @param state the block state
     * @param level the level
     * @param pos the block position
     * @param direction the direction to rotate
     * @return the direction to set the block
     */
    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }
    /**
     * Method to set the mirror placement value
     * @param pState the block state
     * @param pMirror the mirror direction
     * @return the direction for the block to be placed when mirror placing
     */
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    /**
     * Method to add the FACING direction to the block pBuilder
     * @param pBuilder the state definition builder of the block and the block state
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    /**
     * Override method onRemove to drop the items inside the stamper when broken
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
            if (blockEntity instanceof StamperBlockEntity) {
                ((StamperBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }
    /**
     * The interaction method to open stamper block GUI when right-clicked
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
            if(entity instanceof StamperBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (StamperBlockEntity)entity, pPos); // ONLY WORKS IN 1.20.1, NOT NEWER!!
            } else {
                throw new IllegalStateException("STAMPER Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
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

        return createTickerHelper(pBlockEntityType, ModBlockEntities.STAMPER_BE.get(),
                (level, blockPos, blockState, pBlockEntity) -> pBlockEntity.tick(level, blockPos, blockState));
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
        return new StamperBlockEntity(blockPos, blockState);
    }
}
