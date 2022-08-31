package com.codersworld.rklib.ratings;

public class Bar {
    private int color;
    private int endColor;
    private int raters;
    private String starLabel;
    private int startColor;

    public Bar() {
    }

    Bar(int raters2, int color2) {
        this.raters = raters2;
        this.color = color2;
    }

    Bar(int raters2, int color2, String starLabel2) {
        this.raters = raters2;
        this.color = color2;
        this.starLabel = starLabel2;
    }

    public int getRaters() {
        return this.raters;
    }

    public void setRaters(int raters2) {
        this.raters = raters2;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color2) {
        this.color = color2;
    }

    public String getStarLabel() {
        return this.starLabel;
    }

    public void setStarLabel(String starLabel2) {
        this.starLabel = starLabel2;
    }

    public int getEndColor() {
        return this.endColor;
    }

    public void setEndColor(int endColor2) {
        this.endColor = endColor2;
    }

    public boolean isGradientBar() {
        return this.endColor != 0;
    }

    public int getStartColor() {
        return this.startColor;
    }

    public void setStartColor(int startColor2) {
        this.startColor = startColor2;
    }
}
