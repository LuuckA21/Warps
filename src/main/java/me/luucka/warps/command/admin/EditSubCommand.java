package me.luucka.warps.command.admin;

import me.luucka.warps.exception.WarpAttributeException;
import me.luucka.warps.model.Warp;
import me.luucka.warps.model.WarpEditAttribute;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.List;

public final class EditSubCommand extends WarpAdminSubCommand {

	EditSubCommand(final SimpleCommandGroup parent) {
		super(parent, "edit");
//		this.setPermission(Permissions.Channel.JOIN.replace(".{channel}.{mode}", ""));
		setDescription("Edit a warp");
		setMinArguments(2);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];
		final String attribute = args[1];

		final Warp warp = getWarp(warpName);

		final WarpEditAttribute editAttribute = findEnum(WarpEditAttribute.class, attribute, "Invalid edit attribute!");

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
		final SimpleComponent usage = SimpleComponent.empty();
		for (final WarpEditAttribute value : WarpEditAttribute.values()) {
			usage.append(value.getUsage()).appendMiniAmpersand("\n");
		}
		return usage;
	}
}
