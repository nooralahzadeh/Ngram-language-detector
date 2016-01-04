/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.dataset;

import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author fnoorala
 */
public class DocumentProfile {

    String text;

    public void load(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }

            reader.close();

            text = text.toLowerCase();
            //replace number with blankspace
            text = text.replaceAll("\\p{Punct}+", "");
            text = text.replaceAll("\\d+", "");
            System.out.println(text);
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    public Map<String, Double> toVector(String text) {
        Map<String, Double> vector = new HashMap<>();
        Map<String, Integer> unigrams = NgramExtractor.gramLength(1).textPadding('_').extractCountedGrams(text);
        Map<String, Integer> bigrams = NgramExtractor.gramLength(2).textPadding('_').extractCountedGrams(text);
        Map<String, Integer> trigrams = NgramExtractor.gramLength(3).textPadding('_').extractCountedGrams(text);
        Map<String, Integer> fourgrams = NgramExtractor.gramLength(4).textPadding('_').extractCountedGrams(text);
        Map<String, Integer> fivegrams = NgramExtractor.gramLength(4).textPadding('_').extractCountedGrams(text);

        unigrams.putAll(bigrams);
        unigrams.putAll(trigrams);
        unigrams.putAll(fourgrams);
        unigrams.putAll(fivegrams);

        Iterator<String> it = unigrams.keySet().iterator();

        while (it.hasNext()) {
            int N = unigrams.keySet().size();
            double f = (double) unigrams.get(it) / N;
            vector.put(it.next(), f);
        }
        return vector;

    }

}
