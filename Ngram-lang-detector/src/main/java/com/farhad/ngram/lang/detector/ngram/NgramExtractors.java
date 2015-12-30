/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.ngram;

/**
 *
 * @author farhadzn
 */
public class NgramExtractors {

    private static final NgramExtractor STANDARD = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(StandardNgramFilter.getInstance())
            .textPadding(' ');

    private static final NgramExtractor BACKWARDS = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(BackwardsCompatibleNgramFilter.getInstance())
            .textPadding(' ');


    /**
     * The new standard n-gram algorithm.
     */
    public static NgramExtractor standard() {
        return STANDARD;
    }

    /**
     * The old way of doing n-grams.
     */
    public static NgramExtractor backwards() {
        return BACKWARDS;
    }

}