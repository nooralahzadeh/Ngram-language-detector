/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.classifier;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instances;

/**
 *
 * @author farhadzn
 */
public class Classify {
        /**
	 * String that stores the text to guess its language.
	 */
	String text;
	/**
	 * Object that stores the instance.
	 */
	Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	FilteredClassifier classifier;

          /**
	 * This method loads the model to be used as classifier.
	 * @param fileName The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + fileName + " =====");
       } 
		catch (Exception e) {
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + fileName);
		}
	}
        /**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public void makeInstance() {
	
		// Create the header
		List attributeList = new ArrayList(2);
	
		
		
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}
	
        /**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
		}		
	}
}
