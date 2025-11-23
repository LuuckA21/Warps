package me.luucka.warps.database;

import lombok.Getter;
import me.luucka.warps.model.Warp;
import org.mineacademy.fo.database.Row;
import org.mineacademy.fo.database.SimpleDatabase;
import org.mineacademy.fo.database.Table;

@Getter
public enum WarpsTable implements Table {

	WARPS("warps", "Warps_Warp", Warp.class) {
		@Override
		public void onTableCreate(final SimpleDatabase.TableCreator creator) {
			creator
					.addNotNull("Name", "varchar(64)")
					.add("DisplayName", "text")
					.add("Location", "text")
					.addDefault("LastModified", "bigint(20)", "NULL")
					.setPrimaryColumn("Name");
		}
	};

	private final String key;
	private final String name;
	private final Class<? extends Row> rowClass;

	WarpsTable(final String key, final String name, final Class<? extends Row> rowClass) {
		this.key = key;
		this.name = name;
		this.rowClass = rowClass;
	}

	@Override
	public SimpleDatabase getDatabase() {
		return Database.getInstance();
	}


}
