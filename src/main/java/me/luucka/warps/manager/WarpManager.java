package me.luucka.warps.manager;

import me.luucka.warps.database.Database;
import me.luucka.warps.database.WarpsTable;
import me.luucka.warps.model.Warp;
import org.bukkit.Location;
import org.mineacademy.fo.debug.Debugger;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class WarpManager {

	private final Map<String, Warp> loadedWarps = new ConcurrentHashMap<>();

	public WarpManager() {
		loadAll();
	}

	private String normalizeId(String id) {
		return id.toLowerCase(Locale.ROOT).trim();
	}

	public Warp create(final String id, final Location location) {
		final String key = normalizeId(id);

		if (loadedWarps.containsKey(key)) {
			return null;
		}

		final Warp warp = new Warp(key, location);
		warp.insert();
		loadedWarps.put(key, warp);
		return warp;
	}

	public boolean exists(final String id) {
		if (id == null) return false;
		return loadedWarps.containsKey(normalizeId(id));
	}

	public void delete(final String id) {
		if (id == null) return;
		final String key = normalizeId(id);
		if (loadedWarps.containsKey(key)) {
			loadedWarps.get(key).delete();
			loadedWarps.remove(key);
		}
	}

	public Warp get(final String id) {
		if (id == null) return null;
		return loadedWarps.get(normalizeId(id));
	}

	public List<String> getWarpNames() {
		return loadedWarps.keySet().stream().toList();
	}

	public void loadAll() {
		Debugger.debug("database", "Loading Warps from database...");
		loadedWarps.clear();
		final List<Warp> warps = Database.getInstance().getRows(WarpsTable.WARPS);
		warps.forEach(warp -> {
			Debugger.debug("database", "Warp '" + warp.getName() + "' loaded.");
			loadedWarps.put(warp.getName(), warp);
		});
	}
}
