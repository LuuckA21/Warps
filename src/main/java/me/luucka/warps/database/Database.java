package me.luucka.warps.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.database.SimpleDatabase;
import org.mineacademy.fo.database.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor

public final class Database extends SimpleDatabase {

	/**
	 * The database instance.
	 */
	@Getter
	private static final Database instance = new Database();

	/**
	 * The map of UUID to player name
	 */
	private final Map<UUID, String> uniqueIdToName = new HashMap<>();

	/**
	 * The map of player name to UUID
	 */
	private final Map<String, UUID> nameToUniqueId = new HashMap<>();

	@Override
	protected void onConnected() {
	}

	@Override
	public Table[] getTables() {
		return WarpsTable.values();
	}

//	public PlayerCache getCache(final Player player) {
//		final Table table = KitForgeTable.PLAYERS;
//
//		try (final PreparedStatement statement = prepareStatement("SELECT * FROM " + table.getName() + " WHERE UUID = ?")) {
//			statement.setString(1, player.getUniqueId().toString());
//
//			try (final ResultSet resultSet = statement.executeQuery()) {
//				if (resultSet.next()) {
//					return new PlayerCache(SimpleResultSet.wrap(table, resultSet));
//				}
//			}
//
//		} catch (final Throwable t) {
//			CommonCore.error(t, "Error getting cache for player " + player.getName());
//		}
//
//		return null;
//	}
}
