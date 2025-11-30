package me.luucka.warps.command;

import io.papermc.paper.dialog.Dialog;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class DummyCommand extends WarpBaseCommand {

	public DummyCommand() {
		super("dummy");
	}

	@Override
	protected void onCommand() {
		checkConsole();

//		Dialog dialog = Dialog.create(builder -> builder.empty()
//				.base(DialogBase.builder(Component.text("Title")).build())
//				.type(DialogType.notice())
//		);
		audience.showDialog(Dialog.SERVER_LINKS);
	}
}
