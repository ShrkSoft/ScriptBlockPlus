package com.github.yuttyann.scriptblockplus.option;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.yuttyann.scriptblockplus.ScriptBlock;
import com.github.yuttyann.scriptblockplus.enums.ScriptType;
import com.github.yuttyann.scriptblockplus.manager.MapManager;

public class Cooldown {

	private int second;
	private ScriptBlock plugin;
	private MapManager mapManager;

	public Cooldown(ScriptBlock plugin, String second) {
		this.plugin = plugin;
		this.second = Integer.parseInt(second);
		this.mapManager = plugin.getMapManager();
	}

	public int getSecond() {
		return second;
	}

	public int[] get(ScriptType scriptType, String fullCoords, UUID uuid) {
		Map<String, Map<UUID, int[]>> cooldown = mapManager.getCooldownScripts().get(scriptType);
		if (cooldown == null) {
			return new int[]{0, 0, 0};
		}
		Map<UUID, int[]> params = cooldown.get(fullCoords);
		if (params != null && params.containsKey(uuid)) {
			return params.get(uuid);
		}
		return new int[]{0, 0, 0};
	}

	public void put(ScriptType scriptType, String fullCoords, UUID uuid, int[] params) {
		Map<String, Map<UUID, int[]>> cooldown = mapManager.getCooldownScripts().get(scriptType);
		if (cooldown == null) {
			cooldown = new HashMap<String, Map<UUID,int[]>>();
		}
		Map<UUID, int[]> params2 = cooldown.get(fullCoords);
		if (params2 == null) {
			params2 = new HashMap<UUID, int[]>();
		}
		params2.put(uuid, params);
		cooldown.put(fullCoords, params2);
		mapManager.getCooldownScripts().put(scriptType, cooldown);
	}

	public void remove(ScriptType scriptType, String fullCoords, UUID uuid) {
		Map<String, Map<UUID, int[]>> cooldown = mapManager.getCooldownScripts().get(scriptType);
		if (cooldown == null) {
			return;
		}
		Map<UUID, int[]> params = cooldown.get(fullCoords);
		if (params != null && params.containsKey(uuid)) {
			params.remove(uuid);
			cooldown.put(fullCoords, params);
			mapManager.getCooldownScripts().put(scriptType, cooldown);
		}
	}

	public boolean contains(ScriptType scriptType, String fullCoords, UUID uuid) {
		Map<String, Map<UUID, int[]>> cooldown = mapManager.getCooldownScripts().get(scriptType);
		if (cooldown == null) {
			return false;
		}
		Map<UUID, int[]> params = cooldown.get(fullCoords);
		return params != null && params.containsKey(uuid);
	}

	public void run(final ScriptType scriptType, final UUID uuid, final String fullCoords) {
		put(scriptType, fullCoords, uuid, calcParams(get(scriptType, fullCoords, uuid), second));
		new BukkitRunnable() {
			int second = getSecond();
			int[] params = new int[3];
			@Override
			public void run() {
				if (second <= 0) {
					remove(scriptType, fullCoords, uuid);
					cancel();
				} else {
					put(scriptType, fullCoords, uuid, calcParams(params, second));
					second--;
				}
			}
		}.runTaskTimer(plugin, 0, 20);
	}

	private int[] calcParams(int[] params, int second) {
		params[0] = second / 3600;
		params[1] = second % 3600 / 60;
		params[2] = second % 3600 % 60;
		return params;
	}
}