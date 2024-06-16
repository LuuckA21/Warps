package me.luucka.warps;

import me.luucka.warps.model.WarpDisk;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public class Warps extends SimplePlugin {

	@Override
	protected void onPluginStart() {

	}

	@Override
	protected void onReloadablesStart() {
		WarpDisk.init();
	}

	@Override
	protected void onPluginStop() {
		Common.cancelTasks();
	}
}
