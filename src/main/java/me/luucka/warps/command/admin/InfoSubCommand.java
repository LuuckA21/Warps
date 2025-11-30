package me.luucka.warps.command.admin;

import me.luucka.warps.model.Warp;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.settings.Lang;

import java.util.List;

public final class InfoSubCommand extends WarpAdminSubCommand {

	InfoSubCommand(final SimpleCommandGroup parent) {
		super(parent, "info");
		setUsage(Lang.component("warp-info-usage"));
		setDescription(Lang.component("warp-info-description"));
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final String warpName = args[0];

		final Warp warp = getWarp(warpName);
		tell(warp.getWarpInfo());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		}
		return NO_COMPLETE;
	}
}
