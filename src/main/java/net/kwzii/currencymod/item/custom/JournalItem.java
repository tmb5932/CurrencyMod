package net.kwzii.currencymod.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class JournalItem extends WritableBookItem {
    public JournalItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        System.out.println("USED");
        return super.use(pLevel, pPlayer, pHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        pContext.getPlayer().openItemGui(pContext.getItemInHand() , pContext.getHand());
        return super.useOn(pContext);
    }
}
