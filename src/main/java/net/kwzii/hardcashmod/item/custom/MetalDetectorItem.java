package net.kwzii.hardcashmod.item.custom;

import net.kwzii.hardcashmod.util.ModTags;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Class for custom Metal Detector item
 * @author Travis Brown
 */
public class MetalDetectorItem extends Item {

    /**
     * Constructor for the metal detector item
     * @param pProperties the properties of the metal detector
     */
    public MetalDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Method to use the metal detector on blocks
     * @param pContext the context of the useOn call
     * @return the InteractionResult of the useOn
     */
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide()) {
            BlockPos posClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();

            for(int i = 0; i <= posClicked.getY() + 64; i++) {
                BlockState state = pContext.getLevel().getBlockState(posClicked.below(i));

                if(isValuableBlock(state)) {
                    player.sendSystemMessage(Component.literal("BEEP BEEP BEEP!"));
                    break;
                }
            }
        }

            // Use item durability
        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    /**
     * Method to give the player the coordinates of the valuable that they found
     * Currently does not get used
     * @param blockPos the block position of the valuable found
     * @param player the player to send the message to
     * @param block the valuable block that was found
     */
    private void outputValuableCoords(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found " + I18n.get(block.getDescriptionId())
                + " at Y = " + blockPos.getY() + "!"));
    }

    /**
     * Method to check if a block is valuable or not
     * @param state the blockState
     * @return boolean true if block has custom METAL_DETECTOR_VALUABLES tag, false otherwise
     */
    private boolean isValuableBlock(BlockState state) {
        return state.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
    }

    /**
     * Method to add custom tool tip to the metal detector item
     * @param pStack the item being hovered over
     * @param pLevel the world instance
     * @param pTooltipComponents the tooltip component to be shown
     * @param pIsAdvanced the ToolTipFlag
     */
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.hardcashmod.metal_detector.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
