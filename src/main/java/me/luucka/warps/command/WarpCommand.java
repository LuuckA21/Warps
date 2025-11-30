package me.luucka.warps.command;

import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.settings.Lang;

import java.util.List;

@AutoRegister
public final class WarpCommand extends WarpBaseCommand {

	public WarpCommand() {
		super("warp");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		final Warp warp = getWarp(warpName);

		checkBoolean(warp.isEnabled(), Lang.component("warp-disable", "warp", warpName));
		checkBoolean(warp.canPlayerUse(player), Lang.component("warp-player-no-permission", "warp", warpName));

		warp.teleport(player);
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		}
		return NO_COMPLETE;
	}
}
