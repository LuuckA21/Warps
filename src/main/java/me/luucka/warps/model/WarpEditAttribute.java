package me.luucka.warps.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.luucka.warps.WarpPlugin;
import me.luucka.warps.exception.WarpAttributeException;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.settings.Lang;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public enum WarpEditAttribute {

	DISPLAYNAME {
		@Override
		public int getMinArgs() {
			return 3;
		}

		@Override
		public SimpleComponent getUsage() {
			return Lang.component("warp-edit-usage-displayname", LABEL_SUBLABEL);
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			if (args.length < getMinArgs()) {
				throw new WarpAttributeException(getUsage());
			}
			final String newName = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
			warp.setDisplayName(newName);
			Messenger.success(player, commandSuccessMessage(warp, newName));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final Warp warp, final String newValue) {
			return Lang.component("warp-edit-displayname", "warp", warp.getName(), "displayname", newValue);
		}
	},

	LOCATION {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return Lang.component("warp-edit-usage-location", LABEL_SUBLABEL);
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setLocation(player.getLocation());
			Messenger.success(player, commandSuccessMessage(warp, ""));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final Warp warp, final String newValue) {
			return Lang.component("warp-edit-location", "warp", warp.getName());
		}
	},

	OWNER {
		@Override
		public int getMinArgs() {
			return 3;
		}

		@Override
		public SimpleComponent getUsage() {
			return Lang.component("warp-edit-usage-owner", LABEL_SUBLABEL);
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			if (args.length < getMinArgs()) {
				throw new WarpAttributeException(getUsage());
			}
			final String target = args[2];
			final UUID uuid = WarpPlugin.getInstance().getUniqueIdNameCacheManager().getIdByName(target);
			if (uuid == null) {
				throw new WarpAttributeException(Lang.component("player-not-found", "player", target));
			}
			warp.setOwner(uuid);
			Messenger.success(player, commandSuccessMessage(warp, target));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final Warp warp, final String newValue) {
			return Lang.component("warp-edit-owner", "warp", warp.getName(), "owner", newValue);
		}
	},

	PERMISSION_PROTECTED {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return Lang.component("warp-edit-usage-permission-protected", LABEL_SUBLABEL);
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setPermissionProtected(!warp.isPermissionProtected());
			Messenger.success(player, commandSuccessMessage(warp, "" + warp.isPermissionProtected()));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final Warp warp, final String newValue) {
			return Lang.component("warp-edit-permission-protected", "warp", warp.getName(), "state", newValue);
		}
	},

	ENABLED {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return Lang.component("warp-edit-usage-enabled", LABEL_SUBLABEL);
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setEnabled(!warp.isEnabled());
			Messenger.success(player, commandSuccessMessage(warp, "" + warp.isEnabled()));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final Warp warp, final String newValue) {
			return Lang.component("warp-edit-enabled", "warp", warp.getName(), "state", newValue);
		}
	};

	public static List<String> names() {
		return Arrays.stream(values())
				.map(att -> att.name().toLowerCase())
				.toList();
	}

	private static final Map<String, Object> LABEL_SUBLABEL = Map.of(
			"label", "warpadmin",
			"sublabel", "edit"
	);

	public abstract int getMinArgs();

	public abstract SimpleComponent getUsage();

	public abstract void onCommand(Warp warp, String[] args, Player player);

	public abstract SimpleComponent commandSuccessMessage(Warp warp, String newValue);

}

