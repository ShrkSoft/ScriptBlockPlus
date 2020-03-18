package com.github.yuttyann.scriptblockplus.script.option.chat;

import com.github.yuttyann.scriptblockplus.script.option.BaseOption;
import com.github.yuttyann.scriptblockplus.script.option.Option;
import com.github.yuttyann.scriptblockplus.utils.StringUtils;
import com.github.yuttyann.scriptblockplus.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class ToPlayer extends BaseOption {

	public ToPlayer() {
		super("toplayer", "@player ");
	}

	@NotNull
	@Override
	public Option newInstance() {
		return new ToPlayer();
	}

	@Override
	protected boolean isValid() throws Exception {
		Utils.sendMessage(getPlayer(), StringUtils.replaceColor(getOptionValue(), true));
		return true;
	}
}