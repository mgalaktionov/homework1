package ru.digitalhabbits.homework1.plugin;

import org.jetbrains.annotations.NotNull;
import java.util.*;

class FrequencyDictionary {

    private LinkedHashMap<String,Counter> frequencyDictionary;

    public FrequencyDictionary(String text) {
        var words = splitText(text);
        var dictionary = createDictionary(words);
        this.frequencyDictionary = createFreqDict(dictionary,words);
    }

    private LinkedHashMap<String, Counter> sortDictionary(HashMap<String, Counter> unsortedDict){
        var freqComparator = new Comparator<Map.Entry<String, Counter>>() {
            @Override
            public int compare(Map.Entry<String, Counter> entry1, Map.Entry<String, Counter> entry2) {
                Counter cnt1 = entry1.getValue();
                Counter cnt2 = entry2.getValue();
                return  cnt1.compareTo(cnt2);
            }
        };

        var entryList = new ArrayList<>(unsortedDict.entrySet());
        Collections.sort(entryList,freqComparator);
        var sortedDictionary = new LinkedHashMap<String, Counter>(entryList.size());
        for(var entry: entryList){
            sortedDictionary.put(entry.getKey(),entry.getValue());
        }
        return sortedDictionary;
    }
    private List<String> splitText(String text){
        return List.of(text.trim().split("[\\s.,;!?]+"));

    }

    private HashSet<String> createDictionary(List<String> words){

        HashSet<String> dictionary = new HashSet<>();
        dictionary.addAll(words);
        return dictionary;
    }


    private LinkedHashMap<String, Counter> createFreqDict(HashSet<String> dictionary, List<String> words){
        HashMap<String, Counter> freqDictionary = new HashMap<>();
        for(String word: dictionary){
            freqDictionary.put(word,new Counter());
        }
        for(String word: words){
            if (freqDictionary.containsKey(word)){
                freqDictionary.get(word).increment();
            }
        }

        return sortDictionary(freqDictionary);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (String key: this.frequencyDictionary.keySet()){
            builder.append(key)
                    .append(" ")
                    .append(this.frequencyDictionary.get(key).toString())
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
