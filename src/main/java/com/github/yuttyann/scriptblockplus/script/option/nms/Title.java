package com.github.yuttyann.scriptblockplus.script.option.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.github.yuttyann.scriptblockplus.enums.PackageType;
import com.github.yuttyann.scriptblockplus.script.option.BaseOption;
import com.github.yuttyann.scriptblockplus.utils.StringUtils;
import com.github.yuttyann.scriptblockplus.utils.Utils;

public class Title extends BaseOption {

	private static final Class<?>[] TITLE_PARAMS;
	private static final Class<?>[] TIMES_PARAMS;

	static{
		Class<?> titleActionClass = null;
		Class<?> iChatBaseComponentClass = null;
		if (Utils.isCB18orLater()) {
			try {
				if (Utils.isCB183orLater()) {
					titleActionClass = PackageType.NMS.getClass("PacketPlayOutTitle$EnumTitleAction");
				} else {
					titleActionClass = PackageType.NMS.getClass("EnumTitleAction");
				}
				iChatBaseComponentClass = PackageType.NMS.getClass("IChatBaseComponent");
			} catch (ClassNotFoundException e) {}
			TITLE_PARAMS = new Class<?>[]{titleActionClass, iChatBaseComponentClass};
			TIMES_PARAMS = new Class<?>[]{titleActionClass, iChatBaseComponentClass, int.class, int.class, int.class};
		} else {
			TITLE_PARAMS = null;
			TIMES_PARAMS = null;
		}
	}

	public Title() {
		super("title", "@title:");
	}

	@Override
	public boolean isValid() throws Exception {
		if (!Utils.isCB18orLater()) {
			throw new UnsupportedOperationException();
		}
		String[] array = StringUtils.split(getOptionValue(), "/");
		String title = StringUtils.replaceColorCode(array[0], true);
		String subtitle = StringUtils.replaceColorCode(array[1], true);
		int fadeIn = 10;
		int stay = 40;
		int fadeOut = 10;
		if (array.length == 3) {
			String[] times = StringUtils.split(array[2], "-");
			if (times.length == 3) {
				fadeIn = Integer.parseInt(times[0]);
				stay = Integer.parseInt(times[1]);
				fadeOut = Integer.parseInt(times[2]);
			}
		}
		sendTitle(getPlayer(), title, subtitle, fadeIn, stay, fadeOut);
		return false;
	}

	private void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) throws ReflectiveOperationException {
		Class<?> titleActionClass;
		if (Utils.isCB183orLater()) {
			titleActionClass = PackageType.NMS.getClass("PacketPlayOutTitle$EnumTitleAction");
		} else {
			titleActionClass = PackageType.NMS.getClass("EnumTitleAction");
		}
		if (title != null || subtitle != null) {
			setTime(titleActionClass, player, fadeIn, stay, fadeOut);
		}
		Constructor<?> packetConstructor = PackageType.NMS.getConstructor("PacketPlayOutTitle", TITLE_PARAMS);
		String chatSerializer;
		if (Utils.isCB183orLater()) {
			chatSerializer = "IChatBaseComponent$ChatSerializer";
		} else {
			chatSerializer = "ChatSerializer";
		}
		if (title != null) {
			title = StringUtils.replaceColorCode(title, true);
			Object enumTITLE = NMSHelper.getEnumField(titleActionClass, "TITLE");
			Method a = PackageType.NMS.getMethod(chatSerializer, "a", NMSHelper.STRING_PARAM);
			Object component = a.invoke(null, "{\"text\": \"" + title + "\"}");
			Object packetPlayOutTitle = packetConstructor.newInstance(enumTITLE, component);
			NMSHelper.sendPacket(player, packetPlayOutTitle);
		}
		if (subtitle != null) {
			subtitle = StringUtils.replaceColorCode(subtitle, true);
			Object enumSUBTITLE = NMSHelper.getEnumField(titleActionClass, "SUBTITLE");
			Method a = PackageType.NMS.getMethod(chatSerializer, "a", NMSHelper.STRING_PARAM);
			Object component = a.invoke(null, "{\"text\": \"" + subtitle + "\"}");
			Object packetPlayOutTitle = packetConstructor.newInstance(enumSUBTITLE, component);
			NMSHelper.sendPacket(player, packetPlayOutTitle);
		}
	}

	private void setTime(Class<?> titleActionClass, Player player, int fadeIn, int stay, int fadeOut) throws ReflectiveOperationException {
		Constructor<?> packetConstructor = PackageType.NMS.getConstructor("PacketPlayOutTitle", TIMES_PARAMS);
		Object enumTIMES = NMSHelper.getEnumField(titleActionClass, "TIMES");
		Object packetPlayOutTimes = packetConstructor.newInstance(enumTIMES, null, fadeIn, stay, fadeOut);
		NMSHelper.sendPacket(player, packetPlayOutTimes);
	}
}