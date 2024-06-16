package me.luucka.warps.command;

import me.luucka.warps.model.WarpDisk;
import me.luucka.warps.util.CooldownResult;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;
import java.util.Optional;

@AutoRegister
public final class WarpCommand extends SimpleCommand {

	public WarpCommand() {
		super("warp");
		setDescription("Teleport to selected warp");
		setUsage("<name>");
		setMinArguments(1);
//		if (true) {
//			setPermission(null);
//		}
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();
		String warpName = args[0];

		Optional<WarpDisk> warp = WarpDisk.find(warpName);
		if (!warp.isPresent()) {
			tellError("Warp not found!");
			return;
		}

		WarpDisk warpDisk = warp.get();
		if (!warpDisk.getActive()) {
			tellError("Warp is not active");
			return;
		}
		if (!warpDisk.checkPerm(player)) {
			tellError("You do not have permission to use this warp");
			return;
		}

		CooldownResult cooldown = warpDisk.handleCooldown(player);
		if (cooldown.hasCooldown()) {
			tellError("&cWait {duration} second(s) before using this warp again.".replace("{duration}", String.valueOf(cooldown.getRemainingTime())));
			return;
		}

		warpDisk.teleport(player);
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
