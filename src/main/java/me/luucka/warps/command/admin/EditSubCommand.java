package me.luucka.warps.command.admin;

import me.luucka.warps.exception.WarpAttributeException;
import me.luucka.warps.model.Permissions;
import me.luucka.warps.model.Warp;
import me.luucka.warps.model.WarpEditAttribute;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.settings.Lang;

import java.util.ArrayList;
import java.util.List;

public final class EditSubCommand extends WarpAdminSubCommand {

	EditSubCommand(final SimpleCommandGroup parent) {
		super(parent, "edit");
		setPermission(Permissions.Command.EDIT);
		setUsage(Lang.component("warp-edit-usage"));
		setDescription(Lang.component("warp-edit-description"));
		setMinArguments(2);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		final String attribute = args[1];

		final Warp warp = getWarp(warpName);

		final WarpEditAttribute editAttribute = findEnum(WarpEditAttribute.class, attribute, Lang.component("warp-edit-invalid-attribute"));

		try {
			editAttribute.onCommand(warp, args, player);
		} catch (WarpAttributeException e) {
			tellError(e.getErrorMessage());
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		} else if (args.length == 2) {
			return WarpEditAttribute.names();
		}
		return NO_COMPLETE;
	}

	@Override
	protected SimpleComponent getMultilineUsage() {
		final List<SimpleComponent> usages = new ArrayList<>();
		usages.add(Lang.component("warp-edit-usage-displayname"));
		usages.add(Lang.component("warp-edit-usage-location"));
		usages.add(Lang.component("warp-edit-usage-owner"));
		usages.add(Lang.component("warp-edit-usage-permission-protected"));
		usages.add(Lang.component("warp-edit-usage-enabled"));
		return SimpleComponent.join(usages);
	}
}
