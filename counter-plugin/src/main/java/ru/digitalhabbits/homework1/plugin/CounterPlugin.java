package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented
        List<String> strings = Arrays
                .asList(text.split("\n").clone());
        int stringsCount = strings.size();
        List<String> words = new ArrayList<>();
        for (String string : strings) {
            words.addAll(
                    Arrays.asList(string.replace("\n", " ").split("\\s"))
            );
        }
        int wordsCount = words.size();
        int lettersCount = text.length();
        return stringsCount + ";" + wordsCount + ";" + lettersCount;
    }
}
