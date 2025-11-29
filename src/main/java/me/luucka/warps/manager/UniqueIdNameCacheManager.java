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
