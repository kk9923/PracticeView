package com.kx.admin.day19.view;

public class ParallaxTag {
    private float translationXIn;
    private float translationXOut;
    private float translationYIn;
    private float translationYOut;
    private float translationZIn;
    private float translationZOut;

    public float getTranslationXIn() {
        return translationXIn;
    }

    public void setTranslationXIn(float translationXIn) {
        this.translationXIn = translationXIn;
    }

    public float getTranslationXOut() {
        return translationXOut;
    }

    public void setTranslationXOut(float translationXOut) {
        this.translationXOut = translationXOut;
    }

    public float getTranslationYIn() {
        return translationYIn;
    }

    public void setTranslationYIn(float translationYIn) {
        this.translationYIn = translationYIn;
    }

    public float getTranslationYOut() {
        return translationYOut;
    }

    public void setTranslationYOut(float translationYOut) {
        this.translationYOut = translationYOut;
    }

    public float getTranslationZIn() {
        return translationZIn;
    }

    public void setTranslationZIn(float translationZIn) {
        this.translationZIn = translationZIn;
    }

    public float getTranslationZOut() {
        return translationZOut;
    }

    public void setTranslationZOut(float translationZOut) {
        this.translationZOut = translationZOut;
    }

    @Override
    public String toString() {
        return "translationXIn->"+translationXIn+" translationXOut->"+translationXOut
                +" translationYIn->"+translationYIn+" translationYOut->"+translationYOut;
    }
}
