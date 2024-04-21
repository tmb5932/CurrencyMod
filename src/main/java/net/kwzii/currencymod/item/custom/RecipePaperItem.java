package net.kwzii.currencymod.item.custom;

import net.kwzii.currencymod.item.entity.RecipePaperItemEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class RecipePaperItem extends Item {


    public RecipePaperItem(Properties pProperties) {
        super(pProperties.stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {

//            pPlayer.openMenu(new recipePaperMenu());

//            Entity entity = pPlayer.getItemInHand(pUsedHand).getEntityRepresentation();
////            if(pPlayer.getItemInHand(pUsedHand).getItem() instanceof RecipePaperItem) {
////                Entity itemEntity = pPlayer.getItemInHand(pUsedHand).getEntityRepresentation();
//                if (entity instanceof RecipePaperItemEntity recipePaperEntity) {
////                    RecipePaperItemEntity recipePaperItemEntity = (RecipePaperItemEntity) itemEntity;
//                    pPlayer.openMenu(recipePaperEntity);
////                    NetworkHooks.openScreen((ServerPlayer) pPlayer, recipePaperItemEntity);
//                } else {
//                    throw new IllegalStateException("RECIPE PAPER entity representation is missing or not of the expected type!");
//                }
////            } else {
////                throw new IllegalStateException("RECIPE PAPER item is missing or not of the expected type!");
////            }

            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        } else
            return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
