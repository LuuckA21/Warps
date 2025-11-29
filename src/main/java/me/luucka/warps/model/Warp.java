package me.luucka.warps.model;

import lombok.Getter;
import me.luucka.warps.database.WarpsTable;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.Row;
import org.mineacademy.fo.database.SimpleResultSet;
import org.mineacademy.fo.database.Table;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.model.Tuple;

import java.sql.SQLException;
import java.util.UUID;

@Getter
public final class Warp extends Row {

	//------------------------------------------------------------------------------------------------------------------
	//	Attributes
	//------------------------------------------------------------------------------------------------------------------
	private final String name;
	private String displayName;

	private Location location;

	private UUID owner;
	private boolean permissionProtected;

	private boolean enabled;
	private final long createdAt;
	private long lastModified;

	//------------------------------------------------------------------------------------------------------------------
	//	Constructors
	//------------------------------------------------------------------------------------------------------------------

	public Warp(SimpleResultSet resultSet) throws SQLException {
//		super(resultSet);
		name = resultSet.getString("Name");
		displayName = resultSet.getString("DisplayName");
		location = resultSet.get("Location", Location.class);
		owner = resultSet.getUniqueId("Owner");
		permissionProtected = resultSet.getBoolean("PermissionProtected");
		enabled = resultSet.getBoolean("Enabled");
		createdAt = resultSet.getLong("CreatedAt");
		lastModified = resultSet.getLong("LastModified");
	}

	public Warp(String name, Location location, Player owner) {
		this.name = name;
		displayName = name;
		this.location = location;
		this.owner = owner.getUniqueId();
		permissionProtected = false;
		createdAt = System.currentTimeMillis();
		lastModified = createdAt;
		enabled = true;
	}

	@Override
	public SerializedMap toMap() {
		return SerializedMap.fromArray(
				"Name", name,
				"DisplayName", displayName,
				"Location", location,
				"Owner", owner,
				"PermissionProtected", permissionProtected,
				"Enabled", enabled,
				"CreatedAt", createdAt,
				"LastModified", lastModified

		);
	}

	//------------------------------------------------------------------------------------------------------------------
	//	Warp related methods
	//------------------------------------------------------------------------------------------------------------------

	// Setters

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
		markModified();
	}

	public void setLocation(final Location location) {
		this.location = location;
		markModified();
	}

	public void setOwner(final UUID owner) {
		this.owner = owner;
		markModified();
	}

	public void setPermissionProtected(final boolean permissionProtected) {
		this.permissionProtected = permissionProtected;
		markModified();
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		markModified();
	}

	// Check

	public boolean canPlayerUse(final Player player) {
		return !permissionProtected || player.hasPermission("warps.warp." + name);
	}

	public void teleport(final Player player) {
		player.teleport(location);
	}

	// Other

	public Component getWarpInfo() {
		final String mini = String.join("\n",
				"<gold><bold>--- <yellow>Warp Info</yellow> ---</bold></gold>",
				"<gray>ID: <white>{id}",
				"<gray>Displayname: <reset>{display}",
				"<gray>Location: <white>{location}",
				"<gray>Owner: <white>{owner}",
				"<gray>Permission protected: <white>{perm}",
				"<gray>Enabled: <white>{enabled}",
				"<gray>Created at: <white>{created}",
				"<gray>Last modified: <white>{modified}"
		);

		final String formattedLocation =
				"<hover:show_text:'<gray>Click to teleport'>" +
						"<click:run_command:'/warp " + name + "'>" +
						"<white>" + location.getWorld().getName() +
						" <gray>(x: <white>" + location.getBlockX() +
						" <gray>, y: <white>" + location.getBlockY() +
						" <gray>, z: <white>" + location.getBlockZ() +
						" <gray>, yaw: <white>" + location.getYaw() +
						" <gray>, pitch: <white>" + location.getPitch() +
						"<gray>)" +
						"</click></hover>";


		return SimpleComponent.fromMiniAmpersand(mini)
				.replaceBracket("id", name)
				.replaceBracket("display", SimpleComponent.fromMiniAmpersand(displayName))
				.replaceBracket("location", SimpleComponent.fromMiniAmpersand(formattedLocation))
				.replaceBracket("owner", owner != null ? owner.toString() : "None")
				.replaceBracket("perm", String.valueOf(permissionProtected))
				.replaceBracket("enabled", String.valueOf(enabled))
				.replaceBracket("created", TimeUtil.getFormattedDate(createdAt))
				.replaceBracket("modified", TimeUtil.getFormattedDate(lastModified))
				.toAdventure();

	}

	//------------------------------------------------------------------------------------------------------------------
	//	Database related methods
	//------------------------------------------------------------------------------------------------------------------

	private void markModified() {
		lastModified = System.currentTimeMillis();
		insertToQueue();
	}

	@Override
	public Table getTable() {
		return WarpsTable.WARPS;
	}

	@Override
	public Tuple<String, Object> getUniqueColumn() {
		return new Tuple<>("Name", name);
	}
}
