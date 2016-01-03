/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.classifier;

import com.farhad.ngram.lang.detector.dataset.Dataset;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;

import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author farhadzn
 */
public class Learner {

     //NaiveBayes cModel;
    /**
     * Object that stores training data.
     */
    Instances train;
    Instances test;
    
    AttributeSelectedClassifier classifier;
    /**
     * Object that stores the filter
     */
    public static StringToWordVector filter;

    
    //AttributeSelectedClassifier selector;

    public Learner(String path) {
        Dataset dataset = new Dataset();
        String path_for_train=path+"langid.collection.test.txt";
        //String path_for_test=path+"langid.collection.test.txt";
        train = dataset.construct(path_for_train);
        //test=dataset.construct(path_for_test);
    }

    public Learner(){
        
    }

    /*index the inputInstances string features using the StringToWordVector filter.
     */
    public void index() {
        try {
            // Set the tokenizer
	    NGramTokenizer tokenizer = new NGramTokenizer();
	    tokenizer.setNGramMinSize(1);
	    tokenizer.setNGramMaxSize(1);
	    tokenizer.setDelimiters("\\W");
            // Set the filter
            StringToWordVector filter = new StringToWordVector();
            filter = new StringToWordVector();
            filter.setAttributeIndices("last");
            filter.setTokenizer(tokenizer);
            filter.setMinTermFreq(2);
            filter.setInputFormat(train);
            filter.setWordsToKeep(1000000);
            filter.setDoNotOperateOnPerClassBasis(true);
            filter.setLowerCaseTokens(true);
            
            // Filter the input instances into the output ones
            train = Filter.useFilter(train, filter);
            System.out.println("=====First  Filtering dataset done =====");
            saveARFF("/Users/farhadzn/Desktop/Hannover/first-training.arff", train);
//            test=Filter.useFilter(test,filter); 
//            saveARFF("/Users/farhadzn/Desktop/Hannover/firts-test.arff",train);
            //Attribute selection
//            AttributeSelection attributeSelection = new AttributeSelection();
//            InfoGainAttributeEval ev = new InfoGainAttributeEval();
//            
//            train.setClassIndex(0);
//            Ranker ranker = new Ranker();
//           
//           
//            ranker.setNumToSelect(Math.min(10000, train.numAttributes() - 1));
//            ranker.setThreshold(0.2);
//            attributeSelection.setEvaluator(ev);
//            attributeSelection.setSearch(ranker);   
//            attributeSelection.setInputFormat(train);
//
//            train = Filter.useFilter(train, attributeSelection);
            
//            System.out.println("=====Second Filtering dataset done =====");
           // saveARFF("/Users/farhadzn/Desktop/Hannover/attSelection-training.arff");
        } catch (Exception e) {
            System.out.println("Problem found when training");
        }

    }

    public void evaluate() {
        try {
            index();
            //loadARFF("/Users/farhadzn/Desktop/Hannover/first-training.arff");
            //resample if needed 
//            train = train.resample(new Random(42));  
//            //split to 70:30 learn and test set 
//            double percent = 70.0; 
//            int trainSize = (int) Math.round(train.numInstances() * percent / 100);
//            int testSize = train.numInstances() - trainSize; 
//            Instances trainSet = new Instances(train, 0, trainSize); 
//            Instances testSet= new Instances(train, trainSize, testSize);
//            trainSet.setClassIndex(0);
//            testSet.setClassIndex(0);
            classifier = new AttributeSelectedClassifier();
            CfsSubsetEval eval = new CfsSubsetEval();
            BestFirst search=new BestFirst();
            classifier.setClassifier(new NaiveBayes());
            classifier.setEvaluator(eval);
            classifier.setSearch(search);
          
            Evaluation evaluation = new Evaluation(train);
            evaluation.crossValidateModel(classifier, train, 5, new Random(1));
            System.out.println(evaluation.toSummaryString());
            System.out.println(evaluation.toClassDetailsString());
            System.out.println("===== Evaluating (training) dataset done =====");
        } catch (Exception e) {
            System.out.println("Problem found when evaluating");
        }
    }

    public void learn() {
       
        try {
           index();
            //loadARFF("/Users/farhadzn/Desktop/Hannover/attSelection-training.arff");
            classifier = new AttributeSelectedClassifier();
            CfsSubsetEval eval = new CfsSubsetEval();
            BestFirst search=new BestFirst();
            classifier.setClassifier(new NaiveBayes());
            classifier.setEvaluator(eval);
            classifier.setSearch(search);
             classifier.buildClassifier(train);
        System.out.println(classifier);
            System.out.println("===== Training on  (training) dataset done =====");
            
    
        } catch (Exception ex) {
            Logger.getLogger(Learner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    /* This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
     * @param fileName The name of the file that will store the trained model.  */
    public void saveModel(String fileName) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
            System.out.println("===== Saved model: " + fileName + " =====");
        } catch (IOException e) {
            System.out.println("Problem found when writing: " + fileName);
        }
    }

    /**
     * Save an instances object into an ARFF file.
     *
     * @param fileName The name of the file to be saved.
     */
    public void saveARFF(String fileName, Instances instances) {

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            writer.print(instances);
            
            System.out.println("===== Saved dataset: " + fileName + " =====");
            writer.close();
        } catch (IOException e) {
            System.out.println("Problem found when writing: " + fileName);
        }
    }

    /**
     * Loads an ARFF file into an instances object.
     *
     * @param fileName The name of the file to be loaded.
     */
    public void loadARFF(String fileName) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            ArffReader arff = new ArffReader(reader);
            train = arff.getData();
            System.out.println("===== Loaded dataset: " + fileName + " =====");
            //System.err.println(train);
            reader.close();
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }

    }

}
