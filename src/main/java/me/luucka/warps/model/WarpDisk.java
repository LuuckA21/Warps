package me.luucka.warps.model;

import lombok.Getter;
import me.luucka.warps.util.CooldownResult;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.SerializeUtil;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class WarpDisk extends YamlConfig {

	private static final ConfigItems<WarpDisk> LOADED_WARP_DISK = ConfigItems.fromFolder("warps", WarpDisk.class);

	private final ExpiringMap<UUID, Long> cooldownMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).variableExpiration().build();

	private boolean isNewWarp = false;

	// Mandatory fields
	@Getter
	private Location location;

	// Optional fields
	@Getter
	private String displayname;

	@Getter
	private List<String> description;

	@Getter
	private CompMaterial icon;

	@Getter
	private Boolean active;

	@Getter
	private String permission;

	@Getter
	private String cooldownPermission;

	@Getter
	private Integer cooldownSeconds;

	private WarpDisk(String warp) {
		loadConfiguration(NO_DEFAULT, "warps/" + warp + ".yml");
	}

	public WarpDisk(String warp, Location location) {
		isNewWarp = true;

		// Mandatory fields
		this.location = location;

		// Optional fields
		displayname = warp;
		description = Arrays.asList("Description", "of the", "warp");
		icon = CompMaterial.GRASS_BLOCK;
		active = true;
		permission = "warps.warp." + warp;
		cooldownPermission = permission + ".bypasscooldown";
		cooldownSeconds = 0;

		setHeader(
				Common.configLine(),
				"Location is formatted like this:",
				"    <world> <x> <y> <z> <yaw> <pitch>",
				Common.configLine() + "\n"
		);

		loadConfiguration(NO_DEFAULT, "warps/" + warp + ".yml");
	}

	public void setLocation(Location location) {
		this.location = location;
		save();
	}

	public World getWorld() {
		return location.getWorld();
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
		save();
	}

	public void setDescription(List<String> description) {
		this.description = description;
		save();
	}

	public void setIcon(CompMaterial icon) {
		this.icon = icon;
		save();
	}

	public void setActive(Boolean active) {
		this.active = active;
		save();
	}

	public void setPermission(String permission) {
		this.permission = permission;
		save();
	}

	public void setCooldownPermission(String cooldownPermission) {
		this.cooldownPermission = cooldownPermission;
		save();
	}

	public void setCooldownSeconds(Integer cooldownSeconds) {
		this.cooldownSeconds = cooldownSeconds;
		cooldownMap.setExpiration(cooldownSeconds, TimeUnit.SECONDS);
		save();
	}

	//	----------------------------------------------------------------------------------------------------------------

	public boolean checkPerm(Player player) {
		return player.hasPermission(permission);
	}

	public CooldownResult handleCooldown(Player player) {
		if (cooldownSeconds <= 0) return CooldownResult.of(false);

		if (player.hasPermission(cooldownPermission)) {
			return CooldownResult.of(false);
		}

		final long lastRun = cooldownMap.getOrDefault(player.getUniqueId(), 0L);
		final long difference = (System.currentTimeMillis() - lastRun) / 1000;

		if (difference < cooldownSeconds) {
			return CooldownResult.of(true, (int) (cooldownSeconds - difference + 1));
		}

		cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
		return CooldownResult.of(false);
	}

	public void teleport(Player player) {
		player.teleport(location);
	}

//	--------------------------------------------------------------------------------------------------------------------

	@Override
	protected void onLoad() {
		if (isNewWarp) {
			save();
		} else {
			// Mandatory fields
			location = SerializeUtil.deserializeLocationD(getString("Location"));

			// Optional fields
			displayname = getString("Displayname");
			description = getStringList("Description");
			icon = getMaterial("Icon");
			active = getBoolean("Active");
			permission = getString("Permission");
			cooldownPermission = getString("Cooldown_Permission");
			cooldownSeconds = getInteger("Cooldown_Seconds");
			cooldownMap.setExpiration(cooldownSeconds, TimeUnit.SECONDS);
		}
	}

	@Override
	protected void onSave() {
		// Mandatory fields
		set("Location", SerializeUtil.serializeLocD(location));

		// Optional fields
		set("Displayname", displayname);
		set("Description", description);
		set("Icon", icon);
		set("Active", active);
		set("Permission", permission);
		set("Cooldown_Permission", cooldownPermission);
		set("Cooldown_Seconds", cooldownSeconds);
	}

	public void delete() {
		WarpDisk.delete(this);
	}

	public static void init() {
		LOADED_WARP_DISK.loadItems();
	}

	public static void create(String warp, Location location) {
		LOADED_WARP_DISK.loadOrCreateItem(warp, () -> new WarpDisk(warp, location));
	}

	public static void delete(WarpDisk warp) {
		LOADED_WARP_DISK.removeItem(warp);
	}

	public static Optional<WarpDisk> find(String warp) {
		return Optional.ofNullable(LOADED_WARP_DISK.findItem(warp));
	}

	public static List<WarpDisk> getAll() {
		return LOADED_WARP_DISK.getItems();
	}

	public static Set<String> getAllNames() {
		return LOADED_WARP_DISK.getItemNames();
	}
}
