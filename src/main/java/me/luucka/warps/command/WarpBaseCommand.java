package me.luucka.warps.command;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.model.Warp;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public abstract class WarpBaseCommand extends SimpleCommand {

	WarpBaseCommand(String label, List<String> aliases) {
		super(label, aliases);
	}
	
	protected final boolean warpExists(final String name) {
		return WarpPlugin.getInstance().getWarpManager().exists(name);
	}

	protected final Warp getWarp(final String name) {
		final Warp warp = WarpPlugin.getInstance().getWarpManager().get(name);
		checkNotNull(warp, "Warp '" + name + "' not found!");
		return warp;
	}

	protected final List<String> completeLastWordWarpNames() {
		return completeLastWord(WarpPlugin.getInstance().getWarpManager().getWarpNames());
	}
}
