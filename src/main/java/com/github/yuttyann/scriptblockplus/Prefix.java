package com.github.yuttyann.scriptblockplus;

public class Prefix {

	protected static final String COMMAND = "@command ";
	protected static final String BYPASS = "@bypass ";
	protected static final String PLAYER = "@player ";
	protected static final String SAY = "@say ";
	protected static final String PERM = "@perm:";
	protected static final String PERM_ADD = "@permADD:";
	protected static final String PERM_REMOVE = "@permREMOVE:";
	protected static final String GROUP = "@group:";
	protected static final String GROUP_ADD = "@groupADD:";
	protected static final String GROUP_REMOVE = "@groupREMOVE:";
	protected static final String AMOUNT = "@amount:";
	protected static final String DELAY = "@delay:";
	protected static final String COOLDOWN = "@cooldown:";
	protected static final String COST = "$cost:";
	protected static final String ITEM = "$item:";
	protected static final String[] prefixs = getPrefixs(15);

	private static String[] getPrefixs(int max) {
		String[] prefixs = new String[max];
		for (int i = 0; i < max; i++) {
			switch (i) {
			case 0:
				prefixs[i] = COMMAND;
				break;
			case 1:
				prefixs[i] = BYPASS;
				break;
			case 2:
				prefixs[i] = PLAYER;
				break;
			case 3:
				prefixs[i] = SAY;
				break;
			case 4:
				prefixs[i] = PERM;
				break;
			case 5:
				prefixs[i] = PERM_ADD;
				break;
			case 6:
				prefixs[i] = PERM_ADD;
				break;
			case 7:
				prefixs[i] = GROUP;
				break;
			case 8:
				prefixs[i] = GROUP_ADD;
				break;
			case 9:
				prefixs[i] = GROUP_REMOVE;
				break;
			case 10:
				prefixs[i] = AMOUNT;
				break;
			case 11:
				prefixs[i] = DELAY;
				break;
			case 12:
				prefixs[i] = COOLDOWN;
				break;
			case 13:
				prefixs[i] = COST;
				break;
			case 14:
				prefixs[i] = ITEM;
				break;
			default:
				return prefixs;
			}
		}
		return prefixs;
	}
}
