package kim.younjune.mineFast;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{
	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		super.onEnable();
		
		PluginManager pm = getServer().getPluginManager(); //get pluginManager
		MineListener ml = new MineListener();
		
		pm.registerEvents(ml, this); //register event listener
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
}
