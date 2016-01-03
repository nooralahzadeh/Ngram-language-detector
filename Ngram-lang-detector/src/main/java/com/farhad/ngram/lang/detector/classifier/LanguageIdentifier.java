/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.classifier;

import com.farhad.ngram.lang.detector.classifier.Learner;
import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author farhadzn
 */
public class LanguageIdentifier {

    /**
     * String that stores the text to classify
     */
    String text;

    /**
     * Object that stores the instance.
     */
    Instances instances;

    /**
     * Object that stores the classifier.
     */
     // NaiveBayes cModel ; 
    AttributeSelectedClassifier classifier;

    /**
     *
     */
    StringToWordVector filter;
    /**
     *
     * This method loads the text to be classified.
     *
     * @param fileName The name of the file that stores the text.
     */
    public void load(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
            System.out.println("===== Loaded text data: " + fileName + " =====");
            reader.close();
            
            text=text.toLowerCase();
             //replace number with blankspace
            text=text.replaceAll("\\d+", "");
            
            System.out.println(text);
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    /**
     * This method loads the model to be used as classifier.
     *
     * @param fileName The name of the file that stores the model.
     */
    public void loadModel(String fileName) {
        try {
            
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();

            classifier = (AttributeSelectedClassifier) tmp;
          // cModel=(NaiveBayes) tmp;

            in.close();
            System.out.println("===== Loaded model: " + fileName + " =====");
        } catch (Exception e) {
            // Given the cast, a ClassNotFoundException must be caught along with the IOException
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    /**
     * This method creates the instance to be classified, from the text that has
     * been read.
     */
    public void makeInstance() {
        // Create the attributes, class and text
        FastVector fvNominalVal = new FastVector(2);
        fvNominalVal.addElement("EN");
        fvNominalVal.addElement("FR");
        fvNominalVal.addElement("SP");
        Attribute attribute1 = new Attribute("language_class", fvNominalVal);
        Attribute attribute2 = new Attribute("text", (FastVector) null);
        // Create list of instances with one element
        FastVector fvWekaAttributes = new FastVector(2);
        fvWekaAttributes.addElement(attribute1);
        fvWekaAttributes.addElement(attribute2);
        instances = new Instances("language_detection", fvWekaAttributes, 1);
        // Set class index
        instances.setClassIndex(0);
        // Create and add the instance
        Instance instance = new Instance(2);
        instance.setValue(attribute2, text);

        instances.add(instance);
        
        System.out.println("===== Instance created with reference dataset =====");
        System.out.println(instances);
    }

    /* This method performs the classification of the instance.
     * Output is done at the command-line.
     */
    public void classify() {
        try {
//              // Set the tokenizer
	    NGramTokenizer tokenizer = new NGramTokenizer();
	    tokenizer.setNGramMinSize(1);
	    tokenizer.setNGramMaxSize(1);
	    tokenizer.setDelimiters("\\W");
            // Set the filter
            StringToWordVector filter = new StringToWordVector();
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            filter.setTokenizer(tokenizer);

            filter.setInputFormat(instances);
           // filter.setWordsToKeep(1000000);
            filter.setDoNotOperateOnPerClassBasis(true);
            filter.setLowerCaseTokens(true);
            // as far as in learer method the second filter changes the order
//            Reorder  reorder=new Reorder();
//            reorder.setAttributeIndices("2-last,1");
            // Filter the input instances into the output ones
            instances = Filter.useFilter(instances, filter);
        
            
            System.err.println(instances);
            double pred = classifier.classifyInstance(instances.instance(0));
            System.out.println("===== Classified instance =====");
            System.out.println("Language predicted: " + instances.classAttribute().value((int) pred));
        } catch (Exception e) {
            System.out.println("Problem found when classifying the text");
        }
    }
   public void predictLanguageClass(String filename, String Model){
            LanguageIdentifier cls = new LanguageIdentifier();
            cls.load(filename);
            cls.loadModel(Model);
            cls.makeInstance();
            cls.classify();
 
        }
}
