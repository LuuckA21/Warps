package me.luucka.warps.manager;

import me.luucka.warps.database.Database;
import me.luucka.warps.database.WarpsTable;
import me.luucka.warps.model.UniqueIdName;
import org.mineacademy.fo.database.SimpleDatabase;
import org.mineacademy.fo.debug.Debugger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UniqueIdNameCacheManager {

	private final Map<UUID, String> uniqueIdName = new HashMap<>();

	private final Map<String, UUID> nameUniqueId = new HashMap<>();

	public UniqueIdNameCacheManager() {
		loadCache();
	}

	public void addToCache(final UniqueIdName uniqueIdName) {
		this.uniqueIdName.put(uniqueIdName.getUuid(), uniqueIdName.getName());
		nameUniqueId.put(uniqueIdName.getName(), uniqueIdName.getUuid());
	}

	public String getNameById(final UUID uuid) {
		String playerName = uniqueIdName.get(uuid);
		if (playerName == null) {
			final UniqueIdName idName = loadCacheFromDatabaseById(uuid);
			if (idName == null) return null;
			playerName = idName.getName();
		}
		return playerName;
	}

	public UniqueIdName loadCacheFromDatabaseById(final UUID playerId) {
		final UniqueIdName uuidToName = Database.getInstance().getRowWhere(
				WarpsTable.UUID_NAME,
				SimpleDatabase.Where.builder().equals("UUID", String.valueOf(playerId))
		);
		if (uuidToName == null) return null;
		addToCache(uuidToName);
		return uuidToName;
	}

	public UUID getIdByName(final String name) {
		UUID uuid = nameUniqueId.get(name);
		if (uuid == null) {
			final UniqueIdName idName = loadCacheFromDatabaseByName(name);
			if (idName == null) return null;
			uuid = idName.getUuid();
		}
		return uuid;
	}

	public UniqueIdName loadCacheFromDatabaseByName(final String playerName) {
		final UniqueIdName uuidToName = Database.getInstance().getRowWhere(
				WarpsTable.UUID_NAME,
				SimpleDatabase.Where.builder().equals("Name", playerName)
		);
		if (uuidToName == null) return null;
		addToCache(uuidToName);
		return uuidToName;
	}

	public List<String> getAllNames() {
		return uniqueIdName.values().stream().toList();
	}

	private void loadCache() {
		Debugger.debug("database", "Loading UniqueIdName from database...");
		uniqueIdName.clear();
		nameUniqueId.clear();
		final List<UniqueIdName> uniqueIdNames = Database.getInstance()
				.getRowsWhere(
						WarpsTable.UUID_NAME,
						SimpleDatabase.Where.builder().greaterThan("LastSeen", lastSeenThreshold())
				);
		uniqueIdNames.forEach(idName -> {
			uniqueIdName.put(idName.getUuid(), idName.getName());
			nameUniqueId.put(idName.getName(), idName.getUuid());
		});
	}

	private long lastSeenThreshold() {
		long now = System.currentTimeMillis();
		long fifteenDaysMillis = 15L * 24 * 60 * 60 * 1000;
		return now - fifteenDaysMillis;
	}
}
