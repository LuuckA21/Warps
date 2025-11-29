package me.luucka.warps.command.admin;

import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;

import java.util.List;

public final class InfoSubCommand extends WarpAdminSubCommand {

	InfoSubCommand(final SimpleCommandGroup parent) {
		super(parent, "info");
//		this.setPermission(Permissions.Channel.JOIN.replace(".{channel}.{mode}", ""));
		setUsage("<name>");
		setDescription("Displays information about a warp");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];

		final Warp warp = getWarp(warpName);
		player.sendMessage(warp.getWarpInfo());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		}
		return NO_COMPLETE;
	}
}
