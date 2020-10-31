package ru.digitalhabbits.homework1.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class FrequencyDictionary {

    private static final String SPLITTER = "[\\s.,;!?]+";
    private final Map<String, Counter> freqDict;

    public FrequencyDictionary(String text) {
        var words = splitText(text);
        var dictionary = getVocabulary(words);
        this.freqDict = createFreqDict(dictionary, words);
    }

    private List<String> splitText(String text) {
        return List.of(text.trim().split(SPLITTER));
    }

    private HashSet<String> getVocabulary(List<String> words) {
        return new HashSet<>(words);
    }

    private Map<String, Counter> sortDictionary(HashMap<String, Counter> unsortedDict) {

        return unsortedDict
                .entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry<String, Counter>::getKey,
                        Map.Entry<String, Counter>::getValue
                        )
                );
    }

    private Map<String, Counter> createFreqDict(HashSet<String> dictionary, List<String> words) {
        HashMap<String, Counter> freqDictionary = new HashMap<>();
        for (String word : dictionary) {
            freqDictionary.put(word, new Counter());
        }

        for (String word : words) {
            if (freqDictionary.containsKey(word)) {
                freqDictionary.get(word).increment();
            }
        }

        return sortDictionary(freqDictionary);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Counter> entry : this.freqDict.entrySet()) {
            builder.append(entry.getKey())
                    .append(" ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return builder.toString();
    }

    private static class Counter {
        private int count;

        public Counter() {
            this.count = 0;
        }

        public void increment() {
            this.count++;
        }

        @Override
        public String toString() {
            return Integer.toString(this.count);
        }

    }
}
