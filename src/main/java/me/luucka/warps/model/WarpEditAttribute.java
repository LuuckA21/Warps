package me.luucka.warps.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.luucka.warps.WarpPlugin;
import me.luucka.warps.exception.WarpAttributeException;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.Arrays;
import java.util.List;
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
			return SimpleComponent.fromMiniAmpersand("Usage: /warpadmin edit <warp> displayname <new name>");
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			if (args.length < getMinArgs()) {
				throw new WarpAttributeException(getUsage());
			}
			final String newName = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
			warp.setDisplayName(newName);
			Messenger.success(player, commandSuccessMessage(newName));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final String newValue) {
			return SimpleComponent.fromMiniAmpersand("Warp display name changed to '{displayname}'").replaceBracket("displayname", SimpleComponent.fromMiniAmpersand(newValue));
		}
	},

	LOCATION {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return SimpleComponent.fromMiniAmpersand("Usage: /warpadmin edit <warp> location");
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setLocation(player.getLocation());
			Messenger.success(player, commandSuccessMessage(""));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final String newValue) {
			return SimpleComponent.fromMiniAmpersand("Warp location changed to your location");
		}
	},

	OWNER {
		@Override
		public int getMinArgs() {
			return 3;
		}

		@Override
		public SimpleComponent getUsage() {
			return SimpleComponent.fromMiniAmpersand("Usage: /warpadmin edit <warp> owner");
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			if (args.length < getMinArgs()) {
				throw new WarpAttributeException(getUsage());
			}
			final String target = args[2];
			final UUID uuid = WarpPlugin.getInstance().getUniqueIdNameCacheManager().getIdByName(target);
			if (uuid == null) {
				throw new WarpAttributeException("Player '" + target + "' not found!");
			}
			warp.setOwner(uuid);
			Messenger.success(player, commandSuccessMessage(target));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final String newValue) {
			return SimpleComponent.fromMiniAmpersand("Warp owner changed to '{owner}'").replaceBracket("owner", SimpleComponent.fromMiniAmpersand(newValue));
		}
	},

	PERMISSION_PROTECTED {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return SimpleComponent.fromMiniAmpersand("Usage: /warpadmin edit <warp> permission_protected");
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setPermissionProtected(!warp.isPermissionProtected());
			Messenger.success(player, commandSuccessMessage("" + warp.isPermissionProtected()));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final String newValue) {
			return SimpleComponent.fromMiniAmpersand("Warp permission protection changed to '{permission_protected}'").replaceBracket("permission_protected", SimpleComponent.fromMiniAmpersand(newValue));
		}
	},

	ENABLED {
		@Override
		public int getMinArgs() {
			return 2;
		}

		@Override
		public SimpleComponent getUsage() {
			return SimpleComponent.fromMiniAmpersand("Usage: /warpadmin edit <warp> enabled");
		}

		@Override
		public void onCommand(Warp warp, String[] args, Player player) {
			warp.setEnabled(!warp.isEnabled());
			Messenger.success(player, commandSuccessMessage("" + warp.isEnabled()));
		}

		@Override
		public SimpleComponent commandSuccessMessage(final String newValue) {
			return SimpleComponent.fromMiniAmpersand("Warp enabled changed to '{enabled}'").replaceBracket("enabled", newValue);
		}
	};

	public static List<String> names() {
		return Arrays.stream(values())
				.map(att -> att.name().toLowerCase())
				.toList();
	}

	public abstract int getMinArgs();

	public abstract SimpleComponent getUsage();

	public abstract void onCommand(Warp warp, String[] args, Player player);

	public abstract SimpleComponent commandSuccessMessage(String newValue);

}

