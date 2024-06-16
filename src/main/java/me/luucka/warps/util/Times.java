package me.luucka.warps.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Times {

	private final long value;
	private final TimeUnit unit;

	public static Times of(String stringTime) {
		Pattern pattern = Pattern.compile("\\d+[smhd]");
		Matcher matcher = pattern.matcher(stringTime);

		if (!matcher.matches()) {
			return new Times(0, TimeUnit.SECONDS);
		}

		int value = Integer.parseInt(matcher.group(1));
		char unit = matcher.group(2).charAt(0);

		TimeUnit timeUnit;
		switch (unit) {
			case 's':
				timeUnit = TimeUnit.SECONDS;
				break;
			case 'm':
				timeUnit = TimeUnit.MINUTES;
				break;
			case 'h':
				timeUnit = TimeUnit.HOURS;
				break;
			case 'd':
				timeUnit = TimeUnit.DAYS;
				break;
			default:
				value = 0;
				timeUnit = TimeUnit.SECONDS;
		}

		return new Times(value, timeUnit);
	}
}
