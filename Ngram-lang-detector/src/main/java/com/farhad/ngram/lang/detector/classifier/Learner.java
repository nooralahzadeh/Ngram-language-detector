/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.classifier;

import com.farhad.ngram.lang.detector.dataset.Dataset;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.associations.tertius.IndividualInstances;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 *
 * @author farhadzn
 */
public class Learner {

    /**
     * Object that stores training data.
     */
    Instances trainData;

    /**
     * Object that stores the filter
     */
    public static StringToWordVector filter;

    /**
     * Object that stores the classifier
     */
    FilteredClassifier classifier;

    public Learner(String path) {
        Dataset dataset = new Dataset();
        trainData = dataset.construct(path);
    }


    /*index the inputInstances string features using the StringToWordVector filter.
     */
    public void index() {

        try {
            // Set the tokenizer
            WordTokenizer tokenizer = new WordTokenizer();

            tokenizer.setDelimiters(" ");

            // Set the filter
            filter = new StringToWordVector();
           
            filter.setAttributeIndices("last");
            filter.setTokenizer(tokenizer);
            filter.setInputFormat(trainData);
            filter.setWordsToKeep(1000000);
            filter.setDoNotOperateOnPerClassBasis(true);
            filter.setLowerCaseTokens(true);

            // Filter the input instances into the output ones
            trainData = Filter.useFilter(trainData, filter);
            System.out.println("=====First  Filtering dataset done =====");
            //System.err.println(trainData );
            //Attribute selection
            AttributeSelection attributeSelection = new AttributeSelection();
            InfoGainAttributeEval ev = new InfoGainAttributeEval();
            
            trainData.setClassIndex(0);
            Ranker ranker = new Ranker();
            ranker.setStartSet("1-last");
            ranker.setNumToSelect(Math.min(300, trainData.numAttributes() - 1));
            attributeSelection.setEvaluator(ev);
            attributeSelection.setSearch(ranker);
            
            attributeSelection.setInputFormat(trainData);

            trainData = Filter.useFilter(trainData, attributeSelection);
            
            System.out.println("=====Second Filtering dataset done =====");
            
        } catch (Exception e) {
            System.out.println("Problem found when training");
        }

    }

    public void evaluate()  {
	try {
        index();
	trainData.setClassIndex(0);
        classifier = new FilteredClassifier();
        SMO smo = new SMO();
            smo.setFilterType(new SelectedTag(SMO.FILTER_NONE, SMO.TAGS_FILTER));
            RBFKernel rbf = new RBFKernel();
            rbf.setGamma(0.5);
            smo.setKernel(rbf);
            smo.setC(1.5);
            classifier.setClassifier(smo);
        //classifier.setClassifier(new NaiveBayes());
       
        Evaluation eval = new Evaluation(trainData);
        eval.crossValidateModel(classifier, trainData, 5, new Random(1));
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());
        System.out.println("===== Evaluating (training) dataset done =====");
	
                }
		catch (Exception e) {
			System.out.println("Problem found when evaluating");
                }
    }
                
    public void learn()  {
        try {
            
            trainData.setClassIndex(0);
           
            classifier = new FilteredClassifier();
            
            SMO smo = new SMO();
            smo.setFilterType(new SelectedTag(SMO.FILTER_NONE, SMO.TAGS_FILTER));
            RBFKernel rbf = new RBFKernel();
            rbf.setGamma(0.5);
            smo.setKernel(rbf);
            smo.setC(1.5);
            classifier.setClassifier(smo);
            //classifier.setClassifier(new NaiveBayes());
            classifier.buildClassifier(trainData);
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
        } 
		catch (IOException e) {
			System.out.println("Problem found when writing: " + fileName);
		}
	}
   /**
	 * Save an instances object into an ARFF file.
	 * @param fileName The name of the file to be saved.
	 */	
   public void saveARFF(String fileName) {
	
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(fileName));
			writer.print(trainData);
			System.out.println("===== Saved dataset: " + fileName + " =====");
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Problem found when writing: " + fileName);
		}
	}
   /**
	 * Loads an ARFF file into an instances object.
	 * @param fileName The name of the file to be loaded.
	 */
   public void loadARFF(String fileName) {
	
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			System.out.println("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
		}
                
	}

}
