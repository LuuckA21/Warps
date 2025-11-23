package me.luucka.warps.command;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;
import java.util.List;

@AutoRegister
public final class WarpCommand extends SimpleCommand {

	public WarpCommand() {
		super("warp", Collections.singletonList("w"));
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		final Warp warp = WarpPlugin.getInstance().getWarpManager().get(warpName);
		if (warp == null) {
			tellError("Warp '" + warpName + "' not found!");
			return;
		}

		player.teleport(warp.getLocation());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return WarpPlugin.getInstance().getWarpManager().getWarpNames();
		}
		return NO_COMPLETE;
	}
}
