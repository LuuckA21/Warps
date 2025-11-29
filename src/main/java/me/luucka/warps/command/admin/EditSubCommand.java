package me.luucka.warps.command.admin;

import lombok.Getter;
import me.luucka.warps.model.Warp;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;

import java.util.List;

public final class EditSubCommand extends WarpAdminSubCommand {

	EditSubCommand(final SimpleCommandGroup parent) {
		super(parent, "edit");
//		this.setPermission(Permissions.Channel.JOIN.replace(".{channel}.{mode}", ""));
		setUsage("<name> <attribute> <value>");
		setDescription("Edit a warp");
		setMinArguments(2);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Player player = getPlayer();
		final String warpName = args[0];

		final Warp warp = getWarp(warpName);


	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return completeLastWordWarpNames();
		}
		return NO_COMPLETE;
	}
}

@Getter
enum Attribute {
	DISPLAYNAME("displayname"),
	LOCATION("location"),
	OWNER("owner"),
	PERMISSION_PROTECTED("permission_protected"),
	ENABLED("enabled");

	private final String key;

	Attribute(String key) {
		this.key = key;
	}
}
