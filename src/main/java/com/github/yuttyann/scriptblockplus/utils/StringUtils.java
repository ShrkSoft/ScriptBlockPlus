package com.github.yuttyann.scriptblockplus.utils;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class StringUtils {

	private static final Random RANDOM = new Random();

	@NotNull
	public static List<String> getScripts(@NotNull String script) throws IllegalArgumentException {
		Validate.notNull(script, "Script cannot be null");
		int length = script.length();
		char[] chars = script.toCharArray();
		if (chars[0] != '[' || chars[length - 1] != ']') {
			return Arrays.asList(script);
		}
		List<String> result = new ArrayList<>();
		int start = 0, end = 0;
		for (int i = 0, j = 0, k = 0; i < length; i++) {
			switch (chars[i]) {
			case '[':
				start++;
				if (j++ == 0) {
					k = i;
				}
				continue;
			case ']':
				end++;
				if (--j == 0) {
					result.add(script.substring(k + 1, i));
				}
				continue;
			default:
			}
		}
		if (start != end) {
			throw new IllegalArgumentException("Failed to load the script");
		}
		return result;
	}

	@NotNull
	public static String[] split(@NotNull String source, @NotNull String delimiter) {
		int start = 0;
		int end = source.indexOf(delimiter, start);
		List<String> result = new LinkedList<>();
		while (end != -1) {
			result.add(source.substring(start, end));
			start = end + delimiter.length();
			end = source.indexOf(delimiter, start);
		}
		result.add(source.substring(start));
		return result.toArray(new String[result.size()]);
	}

	@Nullable
	public static String replace(@Nullable String source, char search, char replace) {
		return isEmpty(source) ? source : source.replace(search, replace);
	}

	@NotNull
	public static String replaceColor(@Nullable String source, boolean randomColor) {
		if (randomColor) {
			ChatColor color = ChatColor.getByChar(Integer.toHexString(RANDOM.nextInt(16)));
			source = replace(source, "&rc", color::toString);
		}
		return isEmpty(source) ? "" : ChatColor.translateAlternateColorCodes('&', source);
	}

	@NotNull
	public static String replace(@Nullable String source, @NotNull String search, @Nullable String replace) {
		return replace(source, search, () -> replace == null ? "" : replace);
	}

	@NotNull
	public static String replace(@Nullable String source, @NotNull String search, @NotNull ReplaceValue replace) {
		if (isEmpty(source)) {
			return "";
		}
		int start = 0;
		int end = source.indexOf(search, start);
		if (end == -1) {
			return source;
		}
		int searchLength = search.length();
		int replaceLength = source.length() - replace.length();
		replaceLength = Math.max(replaceLength, 0);
		StrBuilder builder = new StrBuilder(source.length() + replaceLength);
		while (end != -1) {
			builder.append(source.substring(start, end)).append(replace.asString());
			start = end + searchLength;
			end = source.indexOf(search, start);
		}
		builder.append(source.substring(start));
		return builder.toString();
	}

	public interface ReplaceValue {

		@Nullable
		Object value();

		@NotNull
		default String asString() {
			Object obj = value();
			return obj == null ? "" : obj.toString();
		}

		default int length() {
			return asString().length();
		}
	}

	@NotNull
	public static String createString(@NotNull String[] args, int start) {
		StrBuilder builder = new StrBuilder();
		for (int i = start; i < args.length; i++) {
			builder.append(args[i]).append(i == (args.length - 1) ? "" : " ");
		}
		return builder.toString();
	}

	@Nullable
	public static String removeStart(@Nullable String source, @Nullable  String prefix) {
		return isNotEmpty(prefix) ? (startsWith(source, prefix) ? source.substring(prefix.length()) : source) : source;
	}

	public static boolean startsWith(@Nullable String source, @NotNull String prefix) {
		return isNotEmpty(source) && source.startsWith(prefix);
	}

	public static boolean isNotEmpty(@Nullable String source) {
		return !isEmpty(source);
	}

	public static boolean isNotEmpty(@Nullable String[] sources) {
		return !isEmpty(sources);
	}

	public static boolean isEmpty(@Nullable String source) {
		return source == null || source.length() == 0;
	}

	public static boolean isEmpty(@Nullable String[] sources) {
		return StreamUtils.anyMatch(sources, StringUtils::isEmpty);
	}
}