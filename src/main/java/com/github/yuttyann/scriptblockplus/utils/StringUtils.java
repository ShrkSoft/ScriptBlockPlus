package com.github.yuttyann.scriptblockplus.utils;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.text.StrBuilder;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * ScriptBlockPlus StringUtils クラス
 * @author yuttyann44581
 */
public final class StringUtils {

	private static final Random RANDOM = new Random();

	@NotNull
	public static List<String> getScripts(@NotNull String script) throws IllegalArgumentException {
		Validate.notNull(script, "Script cannot be null");
		int length = script.length();
		char[] chars = script.toCharArray();
		if (chars[0] != '[' || chars[length - 1] != ']') {
			return Collections.singletonList(script);
		}
		List<String> result = new ArrayList<>();
		int start = 0, end = 0;
		for (int i = 0, j = 0, k = 0; i < length; i++) {
			if (chars[i] == '[') {
				start++;
				if (j++ == 0) {
					k = i;
				}
			} else if (chars[i] == ']') {
				end++;
				if (--j == 0) {
					result.add(script.substring(k + 1, i));
				}
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
		return result.toArray(new String[0]);
	}

	@Nullable
	public static String replace(@Nullable String source, char search, char replace) {
		return isEmpty(source) ? source : source.replace(search, replace);
	}

	@NotNull
	public static String setColor(@Nullable String source) {
		source = isEmpty(source) ? "" : ChatColor.translateAlternateColorCodes('&', source);
		return replace(source, "§rc", ChatColor.getByChar(Integer.toHexString(RANDOM.nextInt(16))));
	}

	@NotNull
	public static List<String> setListColor(List<String> list) {
		list = new ArrayList<>(list);
		for (int i = 0; i < list.size(); i++) {
			list.set(i, setColor(list.get(i)));
		}
		return list;
	}

	@NotNull
	public static String getColors(@NotNull String source) {
		char[] chars = source.toCharArray();
		StringBuilder builder = new StringBuilder(chars.length);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != '§' || (i + 1) >= chars.length) {
				continue;
			}
			if (ChatColor.getByChar(Character.toLowerCase(chars[i + 1])) != null) {
				builder.append(chars[i++]).append(chars[i]);
			}
		}
		return builder.toString();
	}

	@NotNull
	public static String replace(@Nullable String source, @NotNull String search, @Nullable Object replace) {
		if (isEmpty(source)) {
			return "";
		}
		int start = 0;
		int end = source.indexOf(search, start);
		if (end == -1) {
			return source;
		}
		String value = replace == null ? "" : replace.toString();
		int searchLength = search.length();
		int replaceLength = Math.max(source.length() - value.length(), 0);
		StrBuilder builder = new StrBuilder(source.length() + replaceLength);
		while (end != -1) {
			builder.append(source.substring(start, end)).append(value);
			end = source.indexOf(search, start = end + searchLength);
		}
		return builder.append(source.substring(start)).toString();
	}

	@NotNull
	public static String createString(@NotNull String[] args, int start) {
		StrBuilder builder = new StrBuilder();
		for (int i = start; i < args.length; i++) {
			builder.append(args[i]).append(i == (args.length - 1) ? "" : " ");
		}
		return builder.toString();
	}

	@NotNull
	public static String removeStart(@NotNull String source, @NotNull String prefix) {
		return isNotEmpty(prefix) ? (startsWith(source, prefix) ? source.substring(prefix.length()) : source) : source;
	}

	public static boolean startsWith(@Nullable String source, @NotNull String prefix) {
		return isNotEmpty(source) && source.startsWith(prefix);
	}

	public static boolean isNotEmpty(@Nullable String source) {
		return !isEmpty(source);
	}

	public static boolean isNotEmpty(@NotNull String[] sources) {
		return !isEmpty(sources);
	}

	public static boolean isEmpty(@Nullable String source) {
		return source == null || source.length() == 0;
	}

	public static boolean isEmpty(@NotNull String[] sources) {
		return StreamUtils.anyMatch(sources, StringUtils::isEmpty);
	}
}