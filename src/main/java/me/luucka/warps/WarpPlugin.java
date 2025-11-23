package me.luucka.warps;

import lombok.Getter;
import me.luucka.warps.database.Database;
import me.luucka.warps.manager.WarpManager;
import org.mineacademy.fo.platform.BukkitPlugin;

public final class WarpPlugin extends BukkitPlugin {

	@Getter
	private WarpManager warpManager;

//	@Override
//	public String[] getStartupLogo() {
//		return new String[]{
//				" __  ___  __  .___________. _______   ______   .______        _______  _______",
//				"|  |/  / |  | |           ||   ____| /  __  \\  |   _  \\      /  _____||   ____|",
//				"|  '  /  |  | `---|  |----`|  |__   |  |  |  | |  |_)  |    |  |  __  |  |__   ",
//				"|    <   |  |     |  |     |   __|  |  |  |  | |      /     |  | |_ | |   __|",
//				"|  .  \\  |  |     |  |     |  |     |  `--'  | |  |\\  \\----.|  |__| | |  |____ ",
//				"|__|\\__\\ |__|     |__|     |__|      \\______/  | _| `._____| \\______| |_______|"
//		};
//	}

	@Override
	protected void onPluginLoad() {
		super.onPluginLoad();
	}

	@Override
	protected void onPluginPreStart() {
		super.onPluginPreStart();
	}

	@Override
	protected void onPluginStart() {
		warpManager = new WarpManager();
	}

	@Override
	protected void onPluginPreReload() {
		Database.getInstance().disconnect();
	}

	@Override
	protected void onPluginReload() {
	}

	public static WarpPlugin getInstance() {
		return (WarpPlugin) BukkitPlugin.getInstance();
	}

	@Override
	public int getFoundedYear() {
		return 2025;
	}
}
