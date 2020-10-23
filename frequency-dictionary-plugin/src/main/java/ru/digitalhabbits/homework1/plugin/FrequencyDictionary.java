package ru.digitalhabbits.homework1.plugin;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

class FrequencyDictionary {
    private String text;
    private HashMap<String,Counter> frequencyDictionary;
    private LinkedHashMap<String,Counter> sortedfrequencyDictionary;

    public FrequencyDictionary(String text) {
        this.text = text;
        this.frequencyDictionary = createFreqDict(createDictionary(splitText()),splitText());
        this.sortedfrequencyDictionary = sortDictionary();
    }

    private LinkedHashMap<String, Counter> sortDictionary(){
        Comparator<Map.Entry<String, Counter>> freqComparator = new Comparator<Map.Entry<String, Counter>>() {
            @Override
            public int compare(Map.Entry<String, Counter> entryt1, Map.Entry<String, Counter> entryt2) {
                Counter cnt1 = entryt1.getValue();
                Counter cnt2 = entryt2.getValue();
                return  cnt1.compareTo(cnt2);
            }
        };

        List<Map.Entry<String, Counter>> entryList = new ArrayList<>(frequencyDictionary.entrySet());
        Collections.sort(entryList,freqComparator);
        LinkedHashMap<String, Counter> sortedDictionary = new LinkedHashMap<>(entryList.size());
        for(Map.Entry<String, Counter> entry: entryList){
            sortedDictionary.put(entry.getKey(),entry.getValue());
        }
        return sortedDictionary;
    }
    private List<String> splitText(){
        return Arrays.asList(this.text.trim().split("[\\s.,;!?]+"));

    }

    private HashSet<String> createDictionary(List<String> words){

        HashSet<String> dictionary = new HashSet<>();
        dictionary.addAll(words);
        return dictionary;
    }


    private HashMap<String, Counter> createFreqDict(HashSet<String> dictionary, List<String> words){
        HashMap<String, Counter> freqDictionary = new HashMap<>();
        for(String word: dictionary){
            freqDictionary.put(word,new Counter());
        }
        for(String word: words){
            if (freqDictionary.containsKey(word)){
                freqDictionary.get(word).increment();
            }
        }

        return freqDictionary;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (String key: this.sortedfrequencyDictionary.keySet()){
            builder.append(key)
                    .append(" ")
                    .append(this.sortedfrequencyDictionary.get(key).toString())
                    .append("\n");
        }
        return builder.toString();
    }

    private static class Counter{
        private int count;

        public Counter() {
            this.count = 0;
        }

        public void increment(){
            this.count++;
        }

        public int getCount() {
            return count;
        }

        @Override
        public String toString(){
            return Integer.toString(this.count);
        }

        public int compareTo(@NotNull Counter cnt) {
            if(this.count == cnt.getCount()){
                return 0;
            }else if(this.count > cnt.getCount()){
                return -1;
            }
            return 1;
        }
    }
}
