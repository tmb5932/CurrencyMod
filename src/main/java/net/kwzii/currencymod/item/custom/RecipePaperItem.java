package net.kwzii.currencymod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RecipePaperItem extends Item {


    public RecipePaperItem(Properties pProperties) {
        super(pProperties.stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide() && pUsedHand == InteractionHand.MAIN_HAND) {
            // todo: make it open the menu
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
//        return super.use(pLevel, pPlayer, pUsedHand); // todo look if this should be here
    }
}
