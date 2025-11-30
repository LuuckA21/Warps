package me.luucka.warps.exception;

import lombok.Getter;
import org.mineacademy.fo.model.SimpleComponent;

@Getter
public final class WarpAttributeException extends RuntimeException {

	private final SimpleComponent errorMessage;

	public WarpAttributeException(final SimpleComponent errorMessage) {
		super(errorMessage.toPlain());
		this.errorMessage = errorMessage;
	}

	public WarpAttributeException(final String errorMessage) {
		super(errorMessage);
		this.errorMessage = SimpleComponent.fromMiniAmpersand(errorMessage);
	}
}
