package ru.digitalhabbits.homework1.plugin;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;




public class CounterPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented
        int stringsCount = Arrays
                .asList(text.split("\n").clone())
                .size();
        List<String> words = Arrays
                .asList(text.replace("\n", " ").split("\\s"));
        int wordsCount = words.size();
        int lettersCount = text.length();
//        for(String word: words){
//            lettersCount += word.length();
//        }
        return stringsCount+";"+wordsCount+";"+lettersCount;
    }
}
