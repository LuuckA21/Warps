package me.luucka.warps.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.function.Consumer;

public final class ConfirmationMenu extends Menu {

	private final Consumer<Boolean> callback;

	@Position(11)
	private final Button confirButton;

	@Position(15)
	private final Button denyButton;

	public ConfirmationMenu(final Player player, final Consumer<Boolean> callback) {
		this.callback = callback;
		setSize(9 * 3);
		setViewer(player);

		confirButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
				callback.accept(true);
				getViewer().closeInventory();
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.fromMaterial(CompMaterial.GREEN_WOOL).name("Confirm").make();
			}
		};

		denyButton = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
				callback.accept(false);
				getViewer().closeInventory();
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.fromMaterial(CompMaterial.RED_WOOL).name("Cancel").make();
			}
		};
	}

	@Override
	public Menu newInstance() {
		return new ConfirmationMenu(getViewer(), callback);
	}

}
