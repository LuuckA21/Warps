package me.luucka.warps.model;

import org.mineacademy.fo.command.annotation.Permission;
import org.mineacademy.fo.command.annotation.PermissionGroup;

public final class Permissions {

	@PermissionGroup("Permissions for main commands.")
	public static final class Command {

		@Permission("Create a new Warp")
		public static final String CREATE = "warps.command.create";

		@Permission("Delete a Warp")
		public static final String DELETE = "warps.command.delete";

		@Permission("Edit Warp's attributes")
		public static final String EDIT = "warps.command.edit";

		@Permission("Show Warp's info")
		public static final String INFO = "warps.command.info";
	}
}
