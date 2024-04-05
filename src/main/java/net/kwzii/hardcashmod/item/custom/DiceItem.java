package net.kwzii.hardcashmod.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class DiceItem extends Item {
    Random rand = new Random();
    public DiceItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()) {
            sendRollMessage(pPlayer);
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    public int getRoll() {
        return rand.nextInt(1,7);  // Random number 1 to 6
    }

    public void sendRollMessage(Player pPlayer) {
        int roll = getRoll();
        pPlayer.sendSystemMessage(Component.literal(pPlayer.getScoreboardName() + " rolled a " + roll + "!"));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Winners don't give up when faced with failure..."));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
