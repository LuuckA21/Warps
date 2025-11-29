package me.luucka.warps.database;

import lombok.Getter;
import me.luucka.warps.model.UniqueIdName;
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
					.add("Owner", "varchar(36)")
					.add("PermissionProtected", "boolean")
					.add("Enabled", "boolean")
					.add("CreatedAt", "bigint(20)")
					.addDefault("LastModified", "bigint(20)", "NULL")
					.setPrimaryColumn("Name");
		}
	},

	UUID_NAME("uuid_name", "Warps_Uuid_Name", UniqueIdName.class) {
		@Override
		public void onTableCreate(final SimpleDatabase.TableCreator creator) {
			creator
					.addNotNull("UUID", "varchar(36)")
					.add("Name", "text")
					.addDefault("LastSeen", "bigint(20)", "NULL")
					.setPrimaryColumn("UUID");
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
