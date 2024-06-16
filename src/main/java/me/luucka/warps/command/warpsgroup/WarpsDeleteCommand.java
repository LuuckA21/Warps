package me.luucka.warps.command.warpsgroup;

import me.luucka.warps.model.WarpDisk;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;
import java.util.Optional;

public class WarpsDeleteCommand extends SimpleSubCommand {

	public WarpsDeleteCommand(SimpleCommandGroup parent) {
		super(parent, "delete|del");
		setDescription("Delete a warp.");
		setUsage("<name>");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		String warpName = args[0];

		Optional<WarpDisk> warp = WarpDisk.find(warpName);
		if (warp.isPresent()) {
			warp.get().delete();
			tellSuccess("Warp deleted!");
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
