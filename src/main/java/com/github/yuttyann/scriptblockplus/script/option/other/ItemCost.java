package com.github.yuttyann.scriptblockplus.script.option.other;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.yuttyann.scriptblockplus.file.SBConfig;
import com.github.yuttyann.scriptblockplus.player.PlayerData;
import com.github.yuttyann.scriptblockplus.script.option.BaseOption;
import com.github.yuttyann.scriptblockplus.utils.StringUtils;
import com.github.yuttyann.scriptblockplus.utils.Utils;

public class ItemCost extends BaseOption {

	public static final String KEY_ITEM = PlayerData.createRandomId("ItemCost");

	public ItemCost() {
		super("itemcost", "$item:");
	}

	@Override
	protected boolean isValid() throws Exception {
		String[] array = StringUtils.split(getOptionValue(), " ");
		String[] itemData = StringUtils.split(array[0], ":");
		Material type = Material.getMaterial(itemData[0]);
		short damage = itemData.length > 1 ? Short.parseShort(itemData[1]) : 0;
		int amount = Integer.parseInt(array[1]);
		String create = array.length > 2 ? StringUtils.createString(array, 2) : null;
		String itemName = StringUtils.replaceColorCode(create, false);

		Player player = getPlayer();
		PlayerInventory inventory = player.getInventory();
		ItemStack[] items = copyItems(inventory.getContents());
		int allAmount = 0;
		if (!getScriptRead().hasData(KEY_ITEM)) {
			getScriptRead().setData(KEY_ITEM, copyItems(inventory.getContents()));
		}
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (checkItem(item, itemName, type, damage)) {
				allAmount += item.getAmount();
				items[i] = setAmount(item, allAmount > amount ? allAmount - amount : item.getAmount() - amount);
			}
		}
		if (allAmount < amount) {
			Utils.sendMessage(player, SBConfig.getErrorItemMessage(type, amount, damage, itemName));
			return false;
		}
		inventory.setContents(items);
		return true;
	}

	private ItemStack[] copyItems(ItemStack[] items) {
		ItemStack[] copy = new ItemStack[items.length];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = items[i] == null ? null : items[i].clone();
		}
		return copy;
	}

	private ItemStack setAmount(ItemStack item, int amount) {
		if (amount > 0) {
			item.setAmount(amount);
		} else {
			item = new ItemStack(Material.AIR);
		}
		return item;
	}

	private boolean checkItem(ItemStack item, String itemName, Material type, short damage) {
		if (item == null || item.getType() != type || item.getDurability() != damage) {
			return false;
		}
		return itemName == null || Objects.equals(Utils.getItemName(item, null), itemName);
	}
}