package me.luucka.warps.menu;

import me.luucka.warps.model.WarpDisk;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompItemFlag;

public final class WarpsListMenu extends MenuPagged<WarpDisk> {

	public WarpsListMenu(Player player) {
		super(WarpDisk.getAll());
		setViewer(player);
	}

	@Override
	protected ItemStack convertToItemStack(WarpDisk warp) {
		return ItemCreator.of(
						warp.getIcon(),
						warp.getDisplayname(),
						warp.getDescription()
				)
				.flags(CompItemFlag.HIDE_ATTRIBUTES)
				.make();
	}

	@Override
	protected void onPageClick(Player player, WarpDisk warp, ClickType click) {
		new WarpMenu(player, warp).displayTo(player);
	}
}
