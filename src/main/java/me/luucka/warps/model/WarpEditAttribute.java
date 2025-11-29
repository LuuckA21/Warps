package me.luucka.warps.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public enum WarpEditAttribute {

	DISPLAYNAME("displayname") {
		@Override
		public void apply(Warp warp, String[] args, Player player) {
			final String newName = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
			warp.setDisplayName(newName);
		}
	},

	LOCATION("location") {
		@Override
		public void apply(Warp warp, String[] args, Player player) {
			warp.setLocation(player.getLocation());
		}
	},

	OWNER("owner") {
		@Override
		public void apply(Warp warp, String[] args, Player player) {
			final String target = args[3];
			UUID uuid;

			try {
				uuid = UUID.fromString(target);
			} catch (Exception ex) {
				OfflinePlayer off = Bukkit.getOfflinePlayerIfCached(target);
				if (off == null)
					throw new IllegalArgumentException("Invalid player/uuid");
				uuid = off.getUniqueId();
			}

			warp.setOwner(uuid);
		}
	},

	PERMISSION_PROTECTED("permission_protected") {
		@Override
		public void apply(Warp warp, String[] args, Player player) {
			warp.setPermissionProtected(Boolean.parseBoolean(args[3]));
		}
	},

	ENABLED("enabled") {
		@Override
		public void apply(Warp warp, String[] args, Player player) {
			warp.setEnabled(Boolean.parseBoolean(args[3]));
		}
	};

	private final String key;

	public abstract void apply(Warp warp, String[] args, Player player);

	public static WarpEditAttribute fromString(String input) {
		for (WarpEditAttribute a : values()) {
			if (a.key.equalsIgnoreCase(input))
				return a;
		}
		return null;
	}
}

