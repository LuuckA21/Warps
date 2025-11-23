package me.luucka.warps.command.admin;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.menu.ConfirmationMenu;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;

public final class DeleteSubCommand extends WarpAdminSubCommand {

	DeleteSubCommand(final SimpleCommandGroup parent) {
		super(parent, "delete");
//		this.setPermission(Permissions.Channel.JOIN.replace(".{channel}.{mode}", ""));
		setUsage("<name>");
		setDescription("Delete a warp");
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
			if (args[1].equalsIgnoreCase("force") || args[1].equalsIgnoreCase("-f")) {
				force = true;
			}
		}

		if (!warpExists(warpName)) {
			tellError("Warp '" + warpName + "' do not exists!");
			return;
		}

		if (force) {
			deleteWarp(warpName);
		} else {
			new ConfirmationMenu(player, (confirmed) -> {
				if (confirmed) {
					deleteWarp(warpName);
				} else {
					tellError("Deletion cancelled!");
				}
			}).displayTo(player);
		}
	}

	private void deleteWarp(final String warpName) {
		WarpPlugin.getInstance().getWarpManager().delete(warpName);
		tellSuccess("Warp '" + warpName + "' successfully deleted!");
	}
}
