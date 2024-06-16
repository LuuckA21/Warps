package me.luucka.warps.command.warpsgroup;

import me.luucka.warps.menu.WarpsListMenu;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class WarpsListCommand extends SimpleSubCommand {

	public WarpsListCommand(SimpleCommandGroup parent) {
		super(parent, "list");
		setDescription("Open warp menu list.");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();

		new WarpsListMenu(player).displayTo(player);
	}
}
