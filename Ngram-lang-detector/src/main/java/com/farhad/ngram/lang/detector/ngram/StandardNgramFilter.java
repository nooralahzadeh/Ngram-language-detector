/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.ngram;


/**
 * Filters what is generally not desired.
 * 
 * @author farhadzn
 */
public class StandardNgramFilter implements NgramFilter {

    private static final StandardNgramFilter INSTANCE = new StandardNgramFilter();

    public static NgramFilter getInstance() {
        return INSTANCE;
    }

    private StandardNgramFilter() {
    }

    @Override
    public boolean use(String ngram) {
        switch (ngram.length()) {
            case 1:
                if (ngram.charAt(0)==' ') {
                    return false;
                }
                return true;
            case 2:
                return true;
            case 3:
                if (ngram.charAt(1)==' ') {
                    //middle char is a space
                    return false;
                }
                return true;
            case 4:
                if (ngram.charAt(1)==' ' || ngram.charAt(2)==' ') {
                    //one of the middle chars is a space
                    return false;
                }
                return true;
            default:
                //would need the same check: no space in the middle, border is fine.
                throw new UnsupportedOperationException("Unsupported n-gram length: "+ngram.length());
        }
    }

}