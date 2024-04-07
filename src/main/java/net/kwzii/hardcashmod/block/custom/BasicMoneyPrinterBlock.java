package net.kwzii.hardcashmod.block.custom;

import net.kwzii.hardcashmod.block.entity.BasicMoneyPrinterBlockEntity;
import net.kwzii.hardcashmod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BasicMoneyPrinterBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    public BasicMoneyPrinterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {

        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof BasicMoneyPrinterBlockEntity) {
                ((BasicMoneyPrinterBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof BasicMoneyPrinterBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (BasicMoneyPrinterBlockEntity)entity, pPos); // ONLY WORKS IN 1.20.1, NOT NEWER!!
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BasicMoneyPrinterBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.BASIC_MONEY_PRINTER_BE.get(),
                (level, blockPos, blockState, basicMoneyPrinterBlockEntity) -> basicMoneyPrinterBlockEntity.tick(level, blockPos, blockState));
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            System.out.println("NEIGHBORS CHANGED");
            if (blockEntity instanceof BasicMoneyPrinterBlockEntity) {
                BasicMoneyPrinterBlockEntity printerEntity = (BasicMoneyPrinterBlockEntity) blockEntity;
                System.out.println("FOUND PRINTER");

                BlockEntity hopperBlockEntity = pLevel.getBlockEntity(pNeighborPos);
                if (hopperBlockEntity instanceof HopperBlockEntity) {
                    HopperBlockEntity hopper = (HopperBlockEntity) hopperBlockEntity;
                    System.out.println("FOUND HOPPER");
                    for (int i = 0; i < hopper.getContainerSize(); i++) {
                        boolean reset = false;
                        ItemStack extractedStack = hopper.getItem(0);
                        System.out.println("FOR LOOP: " + hopper.isEmpty());
                        System.out.println(extractedStack);
                        while (!extractedStack.isEmpty()) {
                            System.out.println("WHILE LOOPING");
                            printerEntity.insertItem(extractedStack, pPos, pNeighborPos);
                            hopper.removeItem(i, 1);
                            for (int k = 0; k < i; k++) {
                                if (!hopper.getItem(k).isEmpty()) {
                                    i = k;
                                    reset = true;
                                    break;
                                }
                            }
                            if (reset) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
