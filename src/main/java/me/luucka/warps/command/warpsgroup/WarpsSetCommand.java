package me.luucka.warps.command.warpsgroup;

import me.luucka.warps.model.WarpDisk;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;
import java.util.Optional;

public class WarpsSetCommand extends SimpleSubCommand {

	public WarpsSetCommand(SimpleCommandGroup parent) {
		super(parent, "set");
		setDescription("Set warp or update the location.");
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
			warp.get().setLocation(player.getLocation());
			tellSuccess("Warp updated!");
		} else {
			WarpDisk.create(warpName, player.getLocation());
			tellSuccess("Warp created!");
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
