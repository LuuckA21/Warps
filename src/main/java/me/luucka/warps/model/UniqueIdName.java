package me.luucka.warps.model;

import lombok.Getter;
import me.luucka.warps.database.WarpsTable;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.Row;
import org.mineacademy.fo.database.SimpleResultSet;
import org.mineacademy.fo.database.Table;
import org.mineacademy.fo.model.Tuple;

import java.sql.SQLException;
import java.util.UUID;

@Getter
public final class UniqueIdName extends Row {

	private final UUID uuid;
	private String name;
	private long lastSeen;

	//------------------------------------------------------------------------------------------------------------------
	//	Constructors
	//------------------------------------------------------------------------------------------------------------------

	public UniqueIdName(final SimpleResultSet resultSet) throws SQLException {
//		super(resultSet);
		uuid = resultSet.getUniqueId("UUID");
		name = resultSet.getString("Name");
		lastSeen = resultSet.getLong("LastSeen");
	}

	public UniqueIdName(final UUID uuid, final String name) {
		this.uuid = uuid;
		this.name = name;
		lastSeen = System.currentTimeMillis();
	}

	//------------------------------------------------------------------------------------------------------------------
	//	Database related methods
	//------------------------------------------------------------------------------------------------------------------

	@Override
	public SerializedMap toMap() {
		return SerializedMap.fromArray(
				"UUID", uuid,
				"Name", name,
				"LastSeen", lastSeen
		);
	}

	@Override
	public Table getTable() {
		return WarpsTable.UUID_NAME;
	}

	@Override
	public Tuple<String, Object> getUniqueColumn() {
		return new Tuple<>("UUID", uuid);
	}
}
