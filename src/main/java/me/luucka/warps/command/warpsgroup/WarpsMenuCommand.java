package me.luucka.warps.command.warpsgroup;

import me.luucka.warps.menu.WarpMenu;
import me.luucka.warps.model.WarpDisk;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;
import java.util.Optional;

public class WarpsMenuCommand extends SimpleSubCommand {

	public WarpsMenuCommand(SimpleCommandGroup parent) {
		super(parent, "menu");
		setDescription("Open warp menu.");
		setUsage("<name>");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();
		String warpName = args[0];

		Optional<WarpDisk> warp = WarpDisk.find(warpName);
		if (warp.isPresent()) {
			new WarpMenu(player, warp.get()).displayTo(player);
		} else {
			tellError("Warp not found!");
		}
	}

	@Override
	protected List<String> tabComplete() {
		checkConsole();

		if (args.length == 1) {
			return completeLastWord(WarpDisk.getAllNames());
		}

		return NO_COMPLETE;
	}
}
