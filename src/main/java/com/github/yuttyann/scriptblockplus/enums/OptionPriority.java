package com.github.yuttyann.scriptblockplus.enums;

import com.github.yuttyann.scriptblockplus.script.option.chat.Console;
import com.github.yuttyann.scriptblockplus.script.option.other.Amount;
import com.github.yuttyann.scriptblockplus.script.option.other.Calculation;
import com.github.yuttyann.scriptblockplus.script.option.other.Execute;
import com.github.yuttyann.scriptblockplus.script.option.other.ScriptAction;
import com.github.yuttyann.scriptblockplus.script.option.time.Delay;
import com.github.yuttyann.scriptblockplus.script.option.vault.MoneyCost;
import com.github.yuttyann.scriptblockplus.script.option.vault.PermRemove;
import org.jetbrains.annotations.NotNull;

/**
 * ScriptBlockPlus OptionPriority 列挙型
 * @author yuttyann44581
 */
public enum OptionPriority {

    /**
     * 優先位置: 最後尾
     */
    LAST(new Amount().getSyntax()),

    /**
     * 優先位置: {@link Execute} の後
     */
    LOWEST(new Execute().getSyntax()),

    /**
     * 優先位置: {@link Console} の後
     */
    LOW(new Console().getSyntax()),

    /**
     * 優先位置: {@link PermRemove} の後
     */
    NORMAL(new PermRemove().getSyntax()),

    /**
     * 優先位置: {@link MoneyCost} の後
     */
    HIGH(new MoneyCost().getSyntax()),

    /**
     * 優先位置: {@link Delay} の後
     */
    VERY_HIGH(new Delay().getSyntax()),

    /**
     * 優先位置: {@link Calculation} の後
     */
    HIGHEST(new Calculation().getSyntax()),

    /**
     * 優先位置: 先頭
     */
    TOP(new ScriptAction().getSyntax());

    private final String syntax;

    private OptionPriority(@NotNull String syntax) {
        this.syntax = syntax;
    }

    @NotNull
    public String getSyntax() {
        return syntax;
    }
}