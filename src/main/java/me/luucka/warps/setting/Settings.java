package me.luucka.warps.setting;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.CommonCore;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.model.DatabaseType;
import org.mineacademy.fo.platform.Platform;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.sql.SQLException;

public final class Settings extends SimpleSettings {

	/**
	 * Settings for MySQL
	 * <p>
	 * For security reasons, no sensitive information is stored here.
	 */
	public static class Database {

		public static DatabaseType TYPE;

		private static void init() {
			final File databaseYml = FileUtil.extract("database.yml");
			final YamlConfig databaseConfig = YamlConfig.fromFile(databaseYml);

			databaseConfig.setDefaults(YamlConfig.fromInternalPath("database.yml"));

			setPathPrefix("Database");

			boolean save = false;

			if (isSet("Type")) {
				databaseConfig.set("Type", get("Type", DatabaseType.class));

				save = true;
			}

			if (isSet("Host"))
				databaseConfig.set("Host", getString("Host"));

			if (isSet("Database"))
				databaseConfig.set("Database", getString("Database"));

			if (isSet("User"))
				databaseConfig.set("User", getString("User"));

			if (isSet("Password"))
				databaseConfig.set("Password", getString("Password"));

			if (isSet("Line"))
				databaseConfig.set("Line", getString("Line"));

			if (save)
				Common.log("Migrated 'Database' section from settings.yml to database.yml. Please check.");

			TYPE = databaseConfig.get("Type", DatabaseType.class);
			final String HOST = databaseConfig.getString("Host");
			final String DATABASE = databaseConfig.getString("Database");
			final String USER = databaseConfig.getString("User");
			final String PASSWORD = databaseConfig.getString("Password");
			final String LINE = databaseConfig.getString("Line");

			databaseConfig.save();

			boolean remoteFailed = false;

			if (TYPE == DatabaseType.REMOTE) {
				if (Platform.getCustomServerName().equals("server"))
					CommonCore.logFramed(true,
							"&fERROR: &cRemote database requires the",
							"Server_Name key in proxy.yml to be set!");
				else {
					CommonCore.log("", "Connecting to remote " + TYPE.getDriver() + " database...");
					final String address = LINE.replace("{driver}", TYPE.getDriver()).replace("{host}", HOST).replace("{database}", DATABASE);

					try {
						me.luucka.warps.database.Database.getInstance().connect(address, USER, PASSWORD);

					} catch (final Throwable t) {
						if (t instanceof SQLException && t.getMessage() != null && t.getMessage().contains("invalid database address")) {
							Common.warning("Invalid database address: " + address + ", falling back to local.");

							t.printStackTrace();

						} else
							CommonCore.error(t, "Error connecting to remote database, falling back to local.");

						remoteFailed = true;
					}
				}
			}

			if (TYPE == DatabaseType.LOCAL || remoteFailed)
				me.luucka.warps.database.Database.getInstance().connect("jdbc:sqlite:" + FileUtil.getFile("sqlite.db").getPath());
		}

		public static boolean isRemote() {
			return TYPE.isRemote();
		}
	}

}
