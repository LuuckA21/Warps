package me.luucka.warps.command.admin;

import me.luucka.warps.WarpPlugin;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;

public final class CreateSubCommand extends WarpAdminSubCommand {

	CreateSubCommand(final SimpleCommandGroup parent) {
		super(parent, "create");
//		this.setPermission(Permissions.Channel.JOIN.replace(".{channel}.{mode}", ""));
		setUsage("<name>");
		setDescription("Creates a new warp");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];

		checkBoolean(!warpExists(warpName), "Warp '" + warpName + "' already exists!");

		WarpPlugin.getInstance().getWarpManager().create(warpName, player.getLocation(), player);
		tellSuccess("Warp '" + warpName + "' created!");
	}
}
