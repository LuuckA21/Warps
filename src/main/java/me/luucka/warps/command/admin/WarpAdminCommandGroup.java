package me.luucka.warps.command.admin;

import lombok.Getter;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.DebugSubCommand;
import org.mineacademy.fo.command.DumpLocaleSubCommand;
import org.mineacademy.fo.command.ReloadSubCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

@AutoRegister
public final class WarpAdminCommandGroup extends SimpleCommandGroup {

	@Getter
	private final static SimpleCommandGroup instance = new WarpAdminCommandGroup();

	private WarpAdminCommandGroup() {
		super("warpadmin/wadmin/wa");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new CreateSubCommand(this));
		registerSubcommand(new DeleteSubCommand(this));
		registerSubcommand(new DebugSubCommand());
		registerSubcommand(new DumpLocaleSubCommand());
		registerSubcommand(new ReloadSubCommand());
	}
}
