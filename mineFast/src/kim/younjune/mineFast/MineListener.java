package kim.younjune.mineFast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class MineListener implements Listener {
	enum Tool {
		Axe, PickAxe, Shovel, Not
	}

	enum BlockEnum {
		Ore, Log, Other
	}

	BlockFace[] blockfaces = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN };
	Player _player;

	@EventHandler
	public void onMine(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		Block block = event.getBlock();
		Player p = event.getPlayer();
		_player = p;
		PlayerInventory playerInventory = p.getInventory();

		Material originalBlockType = block.getType();
		BlockEnum enumBlokType = checkBlockType(originalBlockType);

		if (enumBlokType == BlockEnum.Other) {
			return;
		}

		breakBlocks(block, p, block.getWorld());

		event.setCancelled(true);

	}

	private BlockEnum checkBlockType(Material block) {
		String sBlockData = block.toString();
		if (sBlockData.contains("LOG"))
			return BlockEnum.Log;
		else if (sBlockData.contains("ORE"))
			return BlockEnum.Ore;

		return BlockEnum.Other;
//		switch(block) {
//		case ACACIA_LOG:
//		case DARK_OAK_LOG:
//		case BIRCH_LOG:
//		case JUNGLE_LOG:
//		case OAK_LOG:
//		case SPRUCE_LOG:
//		case STRIPPED_ACACIA_LOG:
//		case STRIPPED_BIRCH_LOG:
//		case STRIPPED_DARK_OAK_LOG:
//		case STRIPPED_JUNGLE_LOG:
//		case STRIPPED_OAK_LOG:
//		case STRIPPED_SPRUCE_LOG:
//			return BlockEnum.Log;
//		case COAL_ORE:
//		case DIAMOND_ORE:
//		case EMERALD_ORE:
//		case GOLD_ORE:
//		case IRON_ORE:
//		case LAPIS_ORE:
//		case NETHER_QUARTZ_ORE:
//		case REDSTONE_ORE:
//		default:
//			return BlockEnum.Other;
//		}
	}

	public boolean breakBlocks(Block block, Player player, World world) {
		List<Block> blocks = new LinkedList<Block>();

		getBlocksToBreak(block, block, blocks, BlockFace.SELF);

		breakBloks(block, blocks, world, player);

		return true;
	}

	private void getBlocksToBreak(Block originalBlock, Block currentBlock, List<Block> blocks, BlockFace direction) {
		BlockFace oppositeFace = BlockFace.SELF;
		
		if (!blocks.contains(currentBlock)) {
			blocks.add(currentBlock);
		}
		
		
		switch (direction) {
		case NORTH: 
			oppositeFace = BlockFace.SOUTH;
			break;
		case NORTH_EAST: 
			oppositeFace = BlockFace.SOUTH_WEST;
			break;
		case NORTH_WEST: 
			oppositeFace = BlockFace.SOUTH_EAST;
			break;
		case EAST:
			oppositeFace = BlockFace.WEST;
			break;
		case SOUTH:
			oppositeFace = BlockFace.NORTH;
			break;
		case SOUTH_EAST:
			oppositeFace = BlockFace.NORTH_WEST;
			break;
		case SOUTH_WEST: 
			oppositeFace = BlockFace.NORTH_EAST;
			break;
		case WEST: 
			oppositeFace = BlockFace.EAST;
			break;
		case UP: 
			oppositeFace = BlockFace.DOWN;
			break;
		case DOWN:
			oppositeFace = BlockFace.UP;
			break;
		}

		for (BlockFace bf : blockfaces) {
			if(bf == oppositeFace) continue;
			Block block = currentBlock.getRelative(bf);
			if(originalBlock.getType().equals(block.getType())) 
				getBlocksToBreak(originalBlock, currentBlock.getRelative(bf), blocks, bf);
		}
	}

	public boolean breaksTool(Player player, ItemStack item, Material block) {
		if (item != null) {
			Tool currentTool = isTool(item.getType());
			if (currentTool != Tool.Not) {
				Damageable damage = (Damageable) item.getItemMeta();
				int currentDamage = damage.getDamage();
				currentDamage += calculateDamege(currentTool, block);

				if (currentDamage >= item.getType().getMaxDurability()) {
					return true;
				}
				damage.setDamage(currentDamage);

				((Damageable) item.getItemMeta()).setDamage(currentDamage);
			}
		}
		return false;
	}

	public void breakBloks(Block block, List<Block> blocks, World world, Player player) {
		Material drop = block.getType();
		ItemStack item = new ItemStack(drop, 1);
		PlayerInventory playerInventory = player.getInventory();

		for (int counter = 0; counter < blocks.size(); counter++) {

			block = blocks.get(counter);
			block.setType(Material.AIR);
			world.dropItem(block.getLocation(), item);

			if (breaksTool(player, playerInventory.getItemInMainHand(), block.getType())) {
				int itemSlot = playerInventory.getHeldItemSlot();
				playerInventory.clear(itemSlot);
			}
		}
	}

	private int calculateDamege(Tool tool, Material block) {

		return 1;
	}

	public Tool isTool(Material item) {
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
