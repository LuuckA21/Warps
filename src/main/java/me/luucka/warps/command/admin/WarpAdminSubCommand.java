package me.luucka.warps.command.admin;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.model.Warp;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.Lang;

import java.util.List;

public abstract class WarpAdminSubCommand extends SimpleSubCommand {


	WarpAdminSubCommand(final SimpleCommandGroup parent, final String sublabel) {
		super(parent, sublabel);
	}

	protected final boolean warpExists(final String name) {
		return WarpPlugin.getInstance().getWarpManager().exists(name);
	}

	protected final Warp getWarp(final String name) {
		final Warp warp = WarpPlugin.getInstance().getWarpManager().get(name);
		checkNotNull(warp, Lang.component("warp-not-found", "warp", name));
		return warp;
	}

	protected final List<String> completeLastWordWarpNames() {
		return completeLastWord(WarpPlugin.getInstance().getWarpManager().getWarpNames());
	}
}
