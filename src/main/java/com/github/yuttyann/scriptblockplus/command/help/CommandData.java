package com.github.yuttyann.scriptblockplus.command.help;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.yuttyann.scriptblockplus.enums.Permission;

public class CommandData {

	private String message;
	private List<String> permissions;
	private boolean isHelp;

	public CommandData() {
		this(null, null, true);
	}

	public CommandData(boolean isHelp) {
		this(null, null, isHelp);
	}

	public CommandData(Object permission) {
		this(null, permission, true);
	}

	public CommandData(String message, boolean isHelp) {
		this(message, null, isHelp);
	}

	public CommandData(String message, Object permission) {
		this(message, permission, true);
	}

	public CommandData(String message, Object permission, boolean isHelp) {
		setMessage(message);
		if (permission != null) {
			addPermissions(permission);
		}
		setHelp(isHelp);
	}

	public CommandData setMessage(String message) {
		this.message = message;
		return this;
	}

	public CommandData addPermissions(Object... args) {
		if (permissions == null) {
			permissions = new ArrayList<String>();
		}
		for (Object permission : args) {
			permissions.add(
				permission instanceof Permission
				? ((Permission) permission).getNode()
				: permission.toString()
			);
		}
		return this;
	}

	public CommandData setHelp(boolean isHelp) {
		this.isHelp = isHelp;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public boolean hasPermission(CommandSender sender) {
		boolean has = false;
		for (String permission : permissions) {
			if (!has && Permission.has(permission, sender)) {
				has = true;
			}
		}
		return has;
	}

	public boolean isHelp() {
		return isHelp;
	}

	public boolean hasMessage() {
		return message != null;
	}

	public boolean hasPermission() {
		return permissions != null && !permissions.isEmpty();
	}
}