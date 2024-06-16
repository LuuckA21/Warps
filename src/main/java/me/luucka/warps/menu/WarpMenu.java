package me.luucka.warps.menu;

import me.luucka.warps.model.WarpDisk;
import me.luucka.warps.prompt.SimpleCompMaterialPrompt;
import me.luucka.warps.prompt.SimpleIntegerPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.conversation.SimpleStringPrompt;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonRemove;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class WarpMenu extends Menu {

	private final WarpDisk warpDisk;

	private final Button buttonLocation;
	private final Button buttonDisplayname;
	private final Button buttonDescription;
	private final Button buttonIcon;
	private final Button buttonActive;
	private final Button buttonPermission;
	private final Button buttonCooldownPermission;
	private final Button buttonCooldownSeconds;

	private final Button buttonDelete;

	public WarpMenu(Player player, WarpDisk warpDisk) {
		this.warpDisk = warpDisk;
		setViewer(player);
		setTitle("Warp: " + warpDisk.getDisplayname());
		setSize(9 * 6);
		setSlotNumbersVisible();

		//--------------------------------------------------------------------------------------------------------------
		// Button location
		buttonLocation = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				if (click == ClickType.LEFT) {
					warpDisk.setLocation(player.getLocation());
				} else {
					warpDisk.teleport(player);
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.MAP,
								"Location",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current: {x} | {y} | {z}",
												"",
												"&7(Left click)",
												"&6Update warp",
												"",
												"&7(Right click)",
												"&aTeleport to warp"
										),
										"x", warpDisk.getLocation().getBlockX(),
										"y", warpDisk.getLocation().getBlockY(),
										"z", warpDisk.getLocation().getBlockZ())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button displayname
		buttonDisplayname = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleStringPrompt.show(player, "Write new displayname", warpDisk::setDisplayname);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.NAME_TAG,
								"Displayname",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{displayname}",
												"",
												"&7Click to update"
										),
										"displayname", warpDisk.getDisplayname())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button description
		buttonDescription = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleStringPrompt.show(player, "Write new description (split line with '_')", lines -> warpDisk.setDescription(Arrays.stream(lines.split("_")).collect(Collectors.toList())));
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.BOOK,
								"Description",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{description}",
												"",
												"&7Click to update"
										),
										"description", warpDisk.getDescription())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button icon
		buttonIcon = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleCompMaterialPrompt.show(player, "Write new icon (must be a material)", warpDisk::setIcon);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								warpDisk.getIcon(),
								"Icon",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{icon}",
												"",
												"&7Click to update"
										),
										"icon", warpDisk.getIcon())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button active
		buttonActive = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				warpDisk.setActive(!warpDisk.getActive());
				restartMenu();
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								warpDisk.getActive() ? CompMaterial.GREEN_WOOL : CompMaterial.RED_WOOL,
								warpDisk.getActive() ? "Active" : "Inactive",
								warpDisk.getActive() ?
										Arrays.asList(
												"",
												"Click to set",
												"&4INACTIVE"
										) :
										Arrays.asList(
												"",
												"Click to set",
												"&aACTIVE"
										)
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button permission
		buttonPermission = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleStringPrompt.show(player, "Write new permission", warpDisk::setPermission);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.TRIPWIRE_HOOK,
								"Permission",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{permission}",
												"",
												"&7Click to update"
										),
										"permission", warpDisk.getPermission())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button cooldown permission
		buttonCooldownPermission = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleStringPrompt.show(player, "Write new cooldown permission", warpDisk::setCooldownPermission);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.COMPASS,
								"Cooldown permission",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{permission}",
												"",
												"&7Click to update"
										),
										"permission", warpDisk.getCooldownPermission())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button cooldown seconds
		buttonCooldownSeconds = new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				SimpleIntegerPrompt.show(player, "Write new cooldown seconds", warpDisk::setCooldownSeconds);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(
								CompMaterial.CLOCK,
								"Cooldown seconds",
								Replacer.replaceArray(Arrays.asList(
												"",
												"Current:",
												"{seconds}",
												"",
												"&7Click to update"
										),
										"seconds", warpDisk.getCooldownSeconds())
						)
						.make();
			}
		};

		//--------------------------------------------------------------------------------------------------------------
		// Button delete
		buttonDelete = new ButtonRemove(this, "warp", warpDisk.getName(), warpDisk::delete);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		if (10 == slot) {
			return buttonLocation.getItem();
		} else if (12 == slot) {
			return buttonDisplayname.getItem();
		} else if (14 == slot) {
			return buttonDescription.getItem();
		} else if (16 == slot) {
			return buttonIcon.getItem();
		} else if (28 == slot) {
			return buttonActive.getItem();
		} else if (30 == slot) {
			return buttonPermission.getItem();
		} else if (32 == slot) {
			return buttonCooldownPermission.getItem();
		} else if (34 == slot) {
			return buttonCooldownSeconds.getItem();
		}

		if (9 * 6 - 1 == slot) {
			return buttonDelete.getItem();
		}

		return NO_ITEM;
	}

	@Override
	public Menu newInstance() {
		return new WarpMenu(getViewer(), warpDisk);
	}
}
