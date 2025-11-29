package me.luucka.warps.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class JoinListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
	}

}
