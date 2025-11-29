package me.luucka.warps.command;

import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Collections;
import java.util.List;

@AutoRegister
public final class WarpCommand extends WarpBaseCommand {

	public WarpCommand() {
		super("warp", Collections.singletonList("w"));
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		final Warp warp = getWarp(warpName);

		checkNotNull(warp, "Warp '" + warpName + "' not found!");
		checkBoolean(warp.isEnabled(), "Warp '" + warpName + "' is disabled!");
		checkBoolean(warp.canPlayerUse(player), String.format("You do not have permission to use warp '%s'.", warpName));

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
