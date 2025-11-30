package me.luucka.warps.command.admin;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.menu.ConfirmationMenu;
import me.luucka.warps.model.Permissions;
import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.settings.Lang;

import java.util.List;

public final class DeleteSubCommand extends WarpAdminSubCommand {

	DeleteSubCommand(final SimpleCommandGroup parent) {
		super(parent, "delete");
		setPermission(Permissions.Command.DELETE);
		setUsage(Lang.component("warp-delete-usage"));
		setDescription(Lang.component("warp-delete-description"));
		setMinArguments(1);
		setMaxArguments(2);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		boolean force = false;
		if (args.length > 1) {
			if ("-f".equalsIgnoreCase(args[1])) {
				force = true;
			}
		}

		final Warp warp = getWarp(warpName);

		if (force) {
			deleteWarp(warp);
			return;
		}

		new ConfirmationMenu(player, confirmed -> {
			if (confirmed) {
				deleteWarp(warp);
			} else {
				tellError(Lang.component("warp-delete-cancel"));
			}
		}).displayTo(player);

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		}
		return NO_COMPLETE;
	}

	private void deleteWarp(final Warp warp) {
		WarpPlugin.getInstance().getWarpManager().delete(warp);
		tellSuccess(Lang.component("warp-delete-success", "warp", warp.getName()));
	}
}
