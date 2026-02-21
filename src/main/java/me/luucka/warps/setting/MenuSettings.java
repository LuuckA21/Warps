package me.luucka.warps.setting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.CommonCore;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.ValidCore;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.platform.Platform;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;
import java.nio.file.StandardOpenOption;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MenuSettings {

	private static final MenuSettings instance = new MenuSettings();

	private JsonObject dictionary;

	private Map<String, String> cache;

	public static final class Storage {

		public static File createAndDumpToFile() {
			return dumpToFile0(true);
		}

		public static File updateFileIfExists() {
			return dumpToFile0(false);
		}

		private static File dumpToFile0(final boolean createFileIfNotExists) {
			final String path = "menu/" + SimpleSettings.LOCALE + ".json";
			final File localFile = FileUtil.getFile(path);

			if (!localFile.exists()) {
				if (createFileIfNotExists) {
					FileUtil.createIfNotExists(path);
				} else {
					return localFile;
				}
			}

			JsonObject localJson;

			try {
				localJson = CommonCore.GSON.fromJson(String.join("\n", FileUtil.readLinesFromFile(localFile)), JsonObject.class);

			} catch (final JsonSyntaxException ex) {
				throw new FoException("Invalid JSON in " + localFile + " file. Use services like https://jsonformatter.org/ to correct it. Error: " + ex.getMessage(), false);
			}

			if (localJson == null)
				localJson = new JsonObject();

			// First, remove local keys that no longer exist in our dictionary
			for (final Map.Entry<String, JsonElement> entry : localJson.entrySet()) {
				final String key = entry.getKey();

				if (!instance.dictionary.has(key)) {
					CommonCore.log("Removing unused key '" + key + "' from locale file " + localFile);

					localJson.remove(key);
				}
			}

			// Then, add new keys to the local file
			for (final Map.Entry<String, JsonElement> entry : instance.dictionary.entrySet()) {
				final String key = entry.getKey();

				if (!localJson.has(key)) {
					CommonCore.log("Adding new key '" + key + "' from locale file " + localFile);

					localJson.add(key, instance.dictionary.get(key));
				}
			}

			// Trick to sort keys.
			final String unsortedDump = CommonCore.GSON_PRETTY.toJson(localJson);
			final Map<String, Object> map = CommonCore.GSON.fromJson(unsortedDump, TreeMap.class);

			FileUtil.write(localFile, Arrays.asList(CommonCore.GSON_PRETTY.toJson(map)), StandardOpenOption.TRUNCATE_EXISTING);

			return localFile;
		}

		public static void load() {
			final String englishLangTag = Locale.US.getLanguage() + "_" + Locale.US.getCountry();
			final boolean isEnglish = SimpleSettings.LOCALE.equals("en_US");

			List<String> content;
			final JsonObject dictionary = new JsonObject();

			// Set early to make dumpLocale work to update old files
			instance.dictionary = dictionary;

			// Plugin-specific, in jar
			{
				// Optional
				content = FileUtil.readLinesFromInternalPath("menu/" + englishLangTag + ".json");
				putToDictionary(dictionary, content);

				if (!isEnglish) {

					// Base overlay must be set when using non-English locale
					ValidCore.checkNotNull(content, "When using non-English locale (" + SimpleSettings.LOCALE + "), the base overlay en_US.json must exists in " + Platform.getPlugin().getName());

					content = FileUtil.readLinesFromInternalPath("menu/" + SimpleSettings.LOCALE + ".json");

					if (content != null)
						putToDictionary(dictionary, content);

					else
						CommonCore.warning("No such localization: " + SimpleSettings.LOCALE + " in plugin's jar, using keys from the disk file or from the default English locale for keys that are missing.");
				}
			}

			// On disk
			{
				// Start with base locale as overlay
				content = FileUtil.readLinesFromFile("menu/" + englishLangTag + ".json");

				if (content != null)
					try {
						putToDictionary(dictionary, content);
					} catch (final JsonSyntaxException ex) {
						CommonCore.warning("Invalid syntax in localization file " + englishLangTag + ". Use services like https://jsonformatter.org/ to correct it. Error: " + ex.getMessage());
					}

				if (!isEnglish) {
					content = FileUtil.readLinesFromFile("menu/" + SimpleSettings.LOCALE + ".json");

					if (content != null)
						try {
							putToDictionary(dictionary, content);

						} catch (final JsonSyntaxException ex) {
							CommonCore.warning("Invalid syntax in localization file " + SimpleSettings.LOCALE + ". Use services like https://jsonformatter.org/ to correct it. Error: " + ex.getMessage());
						}
				}
			}

			// At last, update the dictionary on disk if the file exists
			updateFileIfExists();

			// Cache all the keys for maximum performance
			final Map<String, String> cache = new HashMap<>();

			for (final Map.Entry<String, JsonElement> entry : dictionary.entrySet()) {
				final String key = entry.getKey();
				final JsonElement value = dictionary.get(key);

				if (value.isJsonPrimitive()) {
					String string = value.getAsString();

					if (string.isEmpty())
						string = "none";

					if (key.startsWith("prefix-") && "none".equals(string)) {
						// ignore
					} else {

						cache.put(key, string);
					}
				}

				// else if it it is array, join with \n
				else if (value.isJsonArray()) {
					final JsonArray array = value.getAsJsonArray();

					final List<String> plainList = new ArrayList<>();

					for (final JsonElement element : array)
						if (element.isJsonPrimitive()) {
							String string = element.getAsString();

							// Need to do this now because components merge using \n and it wont work in sending them
							if (string.startsWith("<center>"))
								string = ChatUtil.center(string.substring(8).trim());

							plainList.add(string);

						} else {
							ValidCore.checkBoolean(element != null && !element.isJsonNull(), "Missing element in array for lang key " + key + "! Make sure to remove ',' at the end of the list");

							CommonCore.warning("Invalid element in array for lang key " + key + ": " + element + ", only Strings and primitives are supported");
						}

					cache.put(key, String.join("\n", plainList));

				} else {
					ValidCore.checkBoolean(value != null && !value.isJsonNull(), "Missing element for lang key " + key + ", check for trailing commas");

					CommonCore.warning("Invalid element for lang key " + key + ": " + value + ", only Strings, primitives and arrays are supported");
				}
			}

			instance.cache = cache;
		}

		/*
		 * Helper method to turn the lines content into a single dump, parse to JSON and
		 * put the keys into the dictionary.
		 */
		private static void putToDictionary(final JsonObject dictionary, final List<String> content) {
			if (content != null && !content.isEmpty()) {
				final JsonObject json = CommonCore.GSON.fromJson(String.join("\n", content), JsonObject.class);

				for (final Map.Entry<String, JsonElement> entry : json.entrySet()) {
					final String key = entry.getKey();

					dictionary.add(key, json.get(key));
				}
			}
		}

	}
}
