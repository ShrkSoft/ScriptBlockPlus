package com.github.yuttyann.scriptblockplus.script.option.chat;

import com.github.yuttyann.scriptblockplus.enums.LogAdmin;
import com.github.yuttyann.scriptblockplus.enums.reflection.PackageType;
import com.github.yuttyann.scriptblockplus.file.config.SBConfig;
import com.github.yuttyann.scriptblockplus.script.option.BaseOption;
import com.github.yuttyann.scriptblockplus.script.option.Option;
import com.github.yuttyann.scriptblockplus.utils.StringUtils;
import com.github.yuttyann.scriptblockplus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ScriptBlockPlus ActionBar オプションクラス
 * @author yuttyann44581
 */
public class ActionBar extends BaseOption {

	public ActionBar() {
		super("actionbar", "@actionbar:");
	}

	@Override
	@NotNull
	public Option newInstance() {
		return new ActionBar();
	}

	@Override
	protected boolean isValid() throws Exception {
		String[] array = StringUtils.split(getOptionValue(), "/");
		String message = StringUtils.setColor(array[0]);

		if (array.length > 1) {
			int stay = Integer.parseInt(array[1]);
			new Task(stay, message).runTaskTimer(getPlugin(), 0, 20);
		} else {
			sendActionBar(message);
		}
		return true;
	}

	private void sendActionBar(@NotNull String message) throws ReflectiveOperationException {
		Optional<Player> value = Optional.ofNullable(getSBPlayer().getPlayer());
		if (!value.isPresent()) {
			return;
		}
		Player player = value.get();
		if (Utils.isCBXXXorLater("1.11")) {
			String command = "title " + player.getName() + " actionbar " + "{\"text\":\"" + message + "\"}";
			LogAdmin.action(player.getWorld(), l -> Bukkit.dispatchCommand(player, command));
		} else if (Utils.isPlatform()) {
			String chatSerializer = "IChatBaseComponent$ChatSerializer";
			Method a = PackageType.NMS.getMethod(chatSerializer, "a", String.class);
			Object component = a.invoke(null, "{\"text\": \"" + message + "\"}");
			Class<?>[] array = { PackageType.NMS.getClass("IChatBaseComponent"), byte.class };
			Constructor<?> packetPlayOutChat = PackageType.NMS.getConstructor("PacketPlayOutChat", array);
			PackageType.sendPacket(player, packetPlayOutChat.newInstance(component, (byte) 2));
		} else {
			String platforms = SBConfig.PLATFORMS.getValue().stream().map(String::valueOf).collect(Collectors.joining(", "));
			throw new UnsupportedOperationException("Unsupported server. | Supported Servers <" + platforms + ">");
		}
	}

	private class Task extends BukkitRunnable {

		private final int stay;
		private final String message;

		private int tick;

		Task(int stay, @NotNull String message) {
			this.tick = 0;
			this.stay = stay;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				if (!getSBPlayer().isOnline() || tick++ >= stay) {
					cancel();
				}
				sendActionBar(isCancelled() ? "" : message);
			} catch (Exception e) {
				cancel();
			}
		}
	}
}