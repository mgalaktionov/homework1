package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented
        var strings = Arrays.asList(text.split("\n").clone());
        var stringsCount = strings.size();
        var wordsCount = strings.stream()
                .map(string -> string.replaceAll("\n", "\\s"))
                .map(formStr -> List.of(formStr.split("\\s")))
                .mapToLong(List::size)
                .sum();
        var charCount = text.length();
        return stringsCount + ";" + wordsCount + ";" + charCount;
    }
}
