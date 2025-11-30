package me.luucka.warps.command.admin;

import lombok.Getter;
import me.luucka.warps.model.Permissions;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.*;

@AutoRegister
public final class WarpAdminCommandGroup extends SimpleCommandGroup {

	@Getter
	private final static SimpleCommandGroup instance = new WarpAdminCommandGroup();

	private WarpAdminCommandGroup() {
		super("warpadmin/wadmin");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CreateSubCommand(this));
		registerSubcommand(new DeleteSubCommand(this));
		registerSubcommand(new InfoSubCommand(this));
		registerSubcommand(new EditSubCommand(this));
		registerSubcommand(new DebugSubCommand());
		registerSubcommand(new DumpLocaleSubCommand());
		registerSubcommand(new ReloadSubCommand());
		registerSubcommand(new PermsSubCommand(Permissions.class));
	}
}
