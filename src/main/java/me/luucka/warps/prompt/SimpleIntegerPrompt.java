package me.luucka.warps.prompt;

import lombok.*;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;

import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
public class SimpleIntegerPrompt extends SimplePrompt {

	@Setter(value = AccessLevel.PROTECTED)
	private String question = null;

	@Setter(value = AccessLevel.PROTECTED)
	private Consumer<Integer> successAction;

	public SimpleIntegerPrompt(String question) {
		this(question, null);
	}

	public SimpleIntegerPrompt(boolean openMenu) {
		super(openMenu);
	}

	@Override
	protected String getPrompt(final ConversationContext ctx) {
		Valid.checkNotNull(question, "Please either call setQuestion or override getPrompt");

		return question;
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
		return "Please insert a whole number. Got: '{input}'".replace("{input}", invalidInput);
	}

	@Override
	protected Prompt acceptValidatedInput(@NonNull final ConversationContext context, @NonNull final String input) {
		int integer = Integer.parseInt(input);

		if (successAction != null)
			successAction.accept(integer);

		else
			onValidatedInput(context, input);

		return Prompt.END_OF_CONVERSATION;
	}

	protected void onValidatedInput(ConversationContext context, String input) {
	}

	public static void show(final Player player, final String question, final Consumer<Integer> successAction) {
		new SimpleIntegerPrompt(question, successAction).show(player);
	}
}
