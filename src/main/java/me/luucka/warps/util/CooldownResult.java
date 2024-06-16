package me.luucka.warps.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CooldownResult {

	private final boolean hasCooldown;
	private final int remainingTime;

	public boolean hasCooldown() {
		return hasCooldown;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public static CooldownResult of(boolean hasCooldown) {
		return of(hasCooldown, 0);
	}

	public static CooldownResult of(boolean hasCooldown, int remainingTime) {
		return new CooldownResult(hasCooldown, remainingTime);
	}
}
