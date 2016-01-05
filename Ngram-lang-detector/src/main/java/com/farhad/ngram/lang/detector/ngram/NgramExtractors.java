/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.ngram;

 
public class NgramExtractors {

    private static final NgramExtractor STANDARD = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(StandardNgramFilter.getInstance())
            .textPadding(' ');
 


    /**
     * The new standard n-gram algorithm.
     */
    public static NgramExtractor standard() {
        return STANDARD;
    }

     

}