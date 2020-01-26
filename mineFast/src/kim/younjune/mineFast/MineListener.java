package kim.younjune.mineFast;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

public class MineListener implements Listener {

	@EventHandler
	public void onMine(BlockBreakEvent event) {
		Location blockLocation = event.getBlock().getLocation();
		Player p = event.getPlayer();
		Location eyeLocation = p.getEyeLocation(); // get player eyeLocation
		p.sendMessage(blockLocation.getBlock().getBlockData().getAsString()); // send block name
		p.sendMessage("block Location:" + blockLocation.toString()); // send block location
		p.sendMessage("eye Location:" + eyeLocation.toString());

		if (blockLocation.equals(eyeLocation)) { // blockLocation and eyelocation aren't same need fix
			p.sendMessage("equal");
			for (int i = 0; i < 8; i++) {
				Location newLocation = blockLocation.add(1, 0, 0);
				Block bloc = newLocation.getBlock();
				bloc.setType(Material.AIR);
			}
		}

		for (int i = 0; i < 8; i++) {
			Location newLocation = blockLocation.add(1, 0, 0);
			Block bloc = newLocation.getBlock();
			bloc.setType(Material.AIR);
		}
		// replace 8 block to air

	}
}
