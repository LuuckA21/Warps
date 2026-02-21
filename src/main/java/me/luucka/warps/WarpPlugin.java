package me.luucka.warps;

import lombok.Getter;
import me.luucka.warps.database.Database;
import me.luucka.warps.manager.UniqueIdNameCacheManager;
import me.luucka.warps.manager.WarpManager;
import me.luucka.warps.setting.MenuSettings;
import org.mineacademy.fo.platform.BukkitPlugin;

public final class WarpPlugin extends BukkitPlugin {

	@Getter
	private WarpManager warpManager;

	@Getter
	private UniqueIdNameCacheManager uniqueIdNameCacheManager;

	@Override
	public String[] getStartupLogo() {
		return new String[]{
				" __    __   ____  ____   ____    _____",
				"|  |__|  | /    ||    \\ |    \\  / ___/",
				"|  |  |  ||  o  ||  D  )|  o  )(   \\_ ",
				"|  |  |  ||     ||    / |   _/  \\__  |",
				"|  `  '  ||  _  ||    \\ |  |    /  \\ |",
				" \\      / |  |  ||  .  \\|  |    \\    |",
				"  \\_/\\_/  |__|__||__|\\_||__|     \\___|",
				" "
		};


	}

	@Override
	protected void onPluginLoad() {
		super.onPluginLoad();
	}

	@Override
	protected void onPluginPreStart() {
	}

	@Override
	protected void onPluginStart() {
		MenuSettings.Storage.load();
		MenuSettings.Storage.createAndDumpToFile();
		uniqueIdNameCacheManager = new UniqueIdNameCacheManager();
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
