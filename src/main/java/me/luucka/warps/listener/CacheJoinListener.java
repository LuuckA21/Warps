package me.luucka.warps.listener;

import me.luucka.warps.WarpPlugin;
import me.luucka.warps.model.UniqueIdName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class CacheJoinListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final UniqueIdName uniqueIdName = new UniqueIdName(player);
		uniqueIdName.upsert();
		WarpPlugin.getInstance().getUniqueIdNameCacheManager().addToCache(uniqueIdName);

	}

}
