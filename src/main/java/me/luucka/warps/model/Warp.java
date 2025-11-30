package me.luucka.warps.model;

import com.google.gson.JsonElement;
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
import org.mineacademy.fo.settings.Lang;

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

	public SimpleComponent getWarpInfo() {
		final SimpleComponent component = SimpleComponent.empty();

		for (final JsonElement element : Lang.dictionary().getAsJsonArray("warp-info-text")) {
			String line = element.getAsString();

			line = line.replace("{warp}", name);
			line = line.replace("{displayname}", displayName);
			line = line.replace("{world}", location.getWorld().getName());
			line = line.replace("{x}", String.valueOf(location.getBlockX()));
			line = line.replace("{y}", String.valueOf(location.getBlockY()));
			line = line.replace("{z}", String.valueOf(location.getBlockZ()));
			line = line.replace("{yaw}", String.valueOf(location.getYaw()));
			line = line.replace("{pitch}", String.valueOf(location.getPitch()));
			line = line.replace("{owner}", owner != null ? owner.toString() : "None");
			line = line.replace("{permission_protected}", String.valueOf(permissionProtected));
			line = line.replace("{enabled}", String.valueOf(enabled));
			line = line.replace("{created}", TimeUtil.getFormattedDate(createdAt));
			line = line.replace("{modified}", TimeUtil.getFormattedDate(lastModified));

			component.append(SimpleComponent.fromMiniAmpersand(line)).append(Component.newline());
		}

		return component;
	}

	//------------------------------------------------------------------------------------------------------------------
	//	Database related methods
	//------------------------------------------------------------------------------------------------------------------

	private void markModified() {
		lastModified = System.currentTimeMillis();
		upsert();
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
