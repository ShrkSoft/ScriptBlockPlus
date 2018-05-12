package com.github.yuttyann.scriptblockplus.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.yuttyann.scriptblockplus.ScriptBlock;
import com.github.yuttyann.scriptblockplus.enums.ScriptType;
import com.github.yuttyann.scriptblockplus.file.yaml.YamlConfig;
import com.github.yuttyann.scriptblockplus.utils.StreamUtils;
import com.github.yuttyann.scriptblockplus.utils.StringUtils;

public final class Files {

	private static final Map<String, YamlConfig> FILES = new HashMap<String, YamlConfig>();

	private static String defaultLanguage;

	public static void reload() {
		FILES.put("config", loadFile("config.yml", true));
		SBConfig.reloadConfig();

		FILES.put("lang", loadLang("lang_{code}.yml", "lang"));
		SBConfig.reloadLang();

		StreamUtils.forEach(ScriptType.values(), s -> loadScript(s));
	}

	public static Map<String, YamlConfig> getFiles() {
		return Collections.unmodifiableMap(FILES);
	}

	public static YamlConfig getConfig() {
		return FILES.get("config");
	}

	public static YamlConfig getLang() {
		return FILES.get("lang");
	}

	public static YamlConfig getScriptFile(ScriptType scriptType) {
		return scriptType == null ? null : FILES.get(scriptType.getType());
	}

	private static void loadScript(ScriptType scriptType) {
		String type = scriptType.getType();
		FILES.put(type, loadFile("scripts/" + type + ".yml", false));
	}

	private static YamlConfig loadFile(String filePath, boolean isCopyFile) {
		return YamlConfig.load(ScriptBlock.getInstance(), filePath, isCopyFile);
	}

	private static YamlConfig loadLang(String filePath, String dirPath) {
		String language = SBConfig.getLanguage();
		if (StringUtils.isEmpty(language) || "default".equalsIgnoreCase(language)) {
			language = getDefaultLanguage();
		}
		return new Lang(ScriptBlock.getInstance(), language).load(filePath, dirPath);
	}

	private static String getDefaultLanguage() {
		return defaultLanguage == null ? defaultLanguage = Locale.getDefault().getLanguage() : defaultLanguage;
	}
}