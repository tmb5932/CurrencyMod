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

/**
 * Custom Double Dice item
 * @author Travis Brown
 */
public class DoubleDiceItem extends Item {
    Random rand = new Random();

    /**
     * Constructor for double dice item
     * @param pProperties the properties of the dice
     */
    public DoubleDiceItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Method to let dice be rolled
     * @param pLevel the world instance
     * @param pPlayer the placer using the dice
     * @param pUsedHand the hand the dice is used in
     * @return the InteractionResult of the use
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()) {
            sendRollMessage(pPlayer);
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    /**
     * Method to mark the dice as an unenchantable item (don't think this works unfortunately)
     * @param pStack
     * @return
     */
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    /**
     * Method to get a random number 1-6 for what the dice rolls
     * @return the int roll value
     */
    public int getRoll() {
        return rand.nextInt(1,7);  // Random number 1 to 6
    }

    /**
     * Method to send the roll message to the player
     * @param pPlayer the player who rolled the dice
     */
    public void sendRollMessage(Player pPlayer) {
        int roll1 = getRoll();
        int roll2 = getRoll();
        int total = roll1 + roll2;

        pPlayer.sendSystemMessage(Component.literal(pPlayer.getScoreboardName() + " rolled a " + roll1
                + " and " + roll2 + "! (" + total + ")"));
    }

    /**
     * Method to set the tool tip of the dice item
     * @param pStack the item that is being hovered over
     * @param pLevel the world instance
     * @param pTooltipComponents the component that displays the tool tip
     * @param pIsAdvanced tool tip flag
     */
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Double dice? Double down!"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
