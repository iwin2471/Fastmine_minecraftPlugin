package kim.younjune.mineFast;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MineListener implements Listener {

    /**
     * handheld item
     */
    enum Tool {
        Axe, PickAxe, Shovel, Not
    }

    /**
     * what is current block type
     */
    enum BlockEnum {
        Ore,
        Log,
        Leaf,
        Other
    }

    /**
     * which direction?
     */
    BlockFace[] blockfaces = {
            BlockFace.NORTH,
            BlockFace.NORTH_EAST,
            BlockFace.NORTH_WEST,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH_WEST,
            BlockFace.WEST,
            BlockFace.UP,
            BlockFace.DOWN
    };
    Player _player;


    /**
     * when block has been mined
     *
     * @param event
     */
    @EventHandler
    public void onMine(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        Block block = event.getBlock();
        _player = event.getPlayer();

        Material originalBlockType = block.getType();
        BlockEnum enumBlokType = checkBlockType(originalBlockType);

        if (enumBlokType == BlockEnum.Other) {
            return;
        }

        if (checkTool(getTool().getType()) != Tool.Not) {
            breakBlocks(block, block.getWorld());
        }

        event.setCancelled(true);
    }

    private BlockEnum checkBlockType(Material block) {
        String sBlockData = block.toString();
        if (sBlockData.contains("LOG"))
            return BlockEnum.Log;
        else if (sBlockData.contains("ORE"))
            return BlockEnum.Ore;
        else if (sBlockData.contains("LEAF"))
            return BlockEnum.Leaf;

        return BlockEnum.Other;
    }

    /**
     * break the block
     *
     * @param block
     * @param world
     * @return
     */
    public boolean breakBlocks(Block block, World world) {
        shearchAndBreak(block.getType(), block, BlockFace.SELF);
        return true;
    }

    private void shearchAndBreak(Material originalBlock, Block currentBlock, BlockFace direction) {
        BlockFace oppositeFace = direction.getOppositeFace();
        Block blockToCheck;
        BlockEnum blockType;
        ItemStack handHeldItem = getTool();

        if (currentBlock.getType().equals(originalBlock)) {
            currentBlock.breakNaturally(handHeldItem);
        }

        for (BlockFace bf : blockfaces) {
            if (bf == oppositeFace)
                continue;
            blockToCheck = currentBlock.getRelative(bf);
            blockType = checkBlockType(blockToCheck.getType());

            if (blockType == BlockEnum.Other)
                continue;

            shearchAndBreak(originalBlock, blockToCheck, bf);
        }
    }

    public ItemStack getTool() {
        return _player.getInventory().getItemInMainHand();
    }

    public Tool checkTool(Material item) {
        switch (item) {
            case WOODEN_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
                return Tool.Axe;

            case IRON_PICKAXE:
            case WOODEN_PICKAXE:
            case DIAMOND_PICKAXE:
            case GOLDEN_PICKAXE:
                return Tool.PickAxe;
            case GOLDEN_SHOVEL:
            case IRON_SHOVEL:
            case WOODEN_SHOVEL:
            case DIAMOND_SHOVEL:
                return Tool.Shovel;
            default:
                return Tool.Not;
        }
    }

}
