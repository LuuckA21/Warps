package me.luucka.warps.command.admin;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.model.Permissions;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.settings.Lang;

public final class CreateSubCommand extends WarpAdminSubCommand {

	CreateSubCommand(final SimpleCommandGroup parent) {
		super(parent, "create");
		setPermission(Permissions.Command.CREATE);
		setUsage(Lang.component("warp-create-usage"));
		setDescription(Lang.component("warp-create-description"));
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];

		checkBoolean(!warpExists(warpName), Lang.component("warp-already-exists", "warp", warpName));

		WarpPlugin.getInstance().getWarpManager().create(warpName, player.getLocation(), player);
		tellSuccess(Lang.component("warp-create-success", "warp", warpName));
	}
}
