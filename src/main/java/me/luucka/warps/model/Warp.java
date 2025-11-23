package me.luucka.warps.model;

import lombok.Getter;
import me.luucka.warps.database.WarpsTable;
import org.bukkit.Location;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.Row;
import org.mineacademy.fo.database.SimpleResultSet;
import org.mineacademy.fo.database.Table;
import org.mineacademy.fo.model.Tuple;

import java.sql.SQLException;

@Getter
public final class Warp extends Row {

	private final String name;
	private String displayName;

	private final Location location;

	@Override
	public SerializedMap toMap() {
		return SerializedMap.fromArray(
				"Name", name,
				"DisplayName", displayName,
				"Location", location
		);
	}

	public Warp(SimpleResultSet resultSet) throws SQLException {
//		super(resultSet);
		name = resultSet.getString("Name");
		displayName = resultSet.getString("DisplayName");
		location = resultSet.get("Location", Location.class);
	}

	public Warp(String name, Location location) {
		this.name = name;
		this.location = location;
	}

	public void insert() {
		insertToQueue();
	}

	public void update() {
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
