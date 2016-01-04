/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.dataset;

import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import com.farhad.ngram.lang.detector.util.FileTools;
import com.farhad.ngram.lang.detector.util.MyPair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.apache.commons.collections4.map.MultiValueMap;
import weka.core.*;

/**
 *
 * @author farhadzn
 */
public class LanguageProfile {

    final int MIN_TERM_FREQUENCY = 5;
    final int MAX_DOC_FREQUENCY = 10000;

    FileTools filetools = new FileTools();

    public void construct(String path) {
        MultiValueMap corpus = filetools.readFile(path);
        int numClass = corpus.keySet().size();

        Iterator<String> it = corpus.keySet().iterator();

        Map<String, Map> profile = new ConcurrentHashMap<>();
         Map<String, Map> profile_tfidf = new HashMap<>();

        //iterate over each class
        while (it.hasNext()) {

            String theKey = (String) it.next();

            List<String> texts = (List<String>) corpus.get(theKey);

            Map<Integer, Map> ngrams_lang = new HashMap<Integer, Map>();

            Map<String, MyPair> ngrams_profile = new ConcurrentHashMap<>();

            //iterate over each text
            for (int i = 0; i < texts.size(); i++) {

                String text = texts.get(i);

                Map<String, Integer> unigrams = NgramExtractor.gramLength(1).textPadding('_').extractCountedGrams(text);
                Map<String, Integer> bigrams = NgramExtractor.gramLength(2).textPadding('_').extractCountedGrams(text);
                Map<String, Integer> trigrams = NgramExtractor.gramLength(3).textPadding('_').extractCountedGrams(text);
                Map<String, Integer> fourgrams = NgramExtractor.gramLength(4).textPadding('_').extractCountedGrams(text);
                Map<String, Integer> fivegrams = NgramExtractor.gramLength(4).textPadding('_').extractCountedGrams(text);

                unigrams.putAll(bigrams);
                unigrams.putAll(trigrams);
                unigrams.putAll(fourgrams);
                unigrams.putAll(fivegrams);
                ngrams_lang.put(i, unigrams);

            }

            Iterator<Integer> itt = ngrams_lang.keySet().iterator();

            while (itt.hasNext()) {

                Map<String, Integer> ngram = ngrams_lang.get(itt.next());
                Iterator<String> ittt = ngram.keySet().iterator();

                while (ittt.hasNext()) {

                    String ng = ittt.next();

                    if (ngrams_profile.containsKey(ng)) {

                        MyPair pair = ngrams_profile.get(ng);
                        pair.setFirst(pair.getFirst() + ngram.get(ng));
                        pair.setSecond(pair.getSecond() + 1);
                        ngrams_profile.put(ng, pair);

                    } else {

                        MyPair pair = new MyPair(ngram.get(ng), 1);
                        ngrams_profile.put(ng, pair);
                    }
                }
            }

            profile.put(theKey, ngrams_profile);

        }

        //filter based on doc_frequency and term_frequency 
        Iterator<String> p_it = profile.keySet().iterator();

        while (p_it.hasNext()) {
            String lang = p_it.next();

            Map<String, MyPair> ngram = profile.get(lang);

            Iterator<String> ngram_it = ngram.keySet().iterator();
            while (ngram_it.hasNext()) {
                String key = ngram_it.next();
                MyPair freq = ngram.get(key);
                if (freq.getFirst() <= MIN_TERM_FREQUENCY | freq.getSecond() >= MAX_DOC_FREQUENCY) {
                    ngram.remove(key);
                }

            }

        }
        
        //computer the tf-idf for each n-gram 
        p_it = profile.keySet().iterator();
        
        while (p_it.hasNext()) {
            String lang = p_it.next();

            Map<String, MyPair> ngram = profile.get(lang);
            
            int N=ngram.keySet().size();
            
            Iterator<String> ngram_it = ngram.keySet().iterator();
            Map<String, Double> ngram_tfidf=new HashMap<>();
          
            while (ngram_it.hasNext()) {
                
                String key = ngram_it.next();
                MyPair freq = ngram.get(key);
               
                double tf=(double)freq.getFirst()/N;
                        
                double idf=Math.log((double) MAX_DOC_FREQUENCY / freq.getSecond());
                double tfidf=tf*idf;
                ngram_tfidf.put(key, tfidf);

            }
           // profile_tfidf.put(lang, ngram_tfidf);
            
            //write the language profile 
            String filename=lang+".profile";
            saveProfile(filename, ngram_tfidf);
           
        }
        
      
        
        

    }
    
    public void saveProfile(String filename,  Map ngram_tfidf ){
         ObjectOutputStream out;
           
            try {
                out = new ObjectOutputStream(new FileOutputStream(filename));
                out.writeObject(ngram_tfidf);
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LanguageProfile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LanguageProfile.class.getName()).log(Level.SEVERE, null, ex);
            }

        
    }
    
   public   Map<String,Double>  loadProfile(String lang) {
        String filename=lang+".profile";
         Map<String,Double> mapInFile=new HashMap<>();
    try{
        File toRead=new File(filename);
        FileInputStream fis=new FileInputStream(toRead);
        ObjectInputStream ois=new ObjectInputStream(fis);

         mapInFile=(HashMap<String,Double>)ois.readObject();

        ois.close();
        fis.close();
        
     
    }catch(Exception e){}
      return mapInFile;
    }

}
