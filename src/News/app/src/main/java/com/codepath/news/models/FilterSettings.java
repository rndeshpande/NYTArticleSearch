package com.codepath.news.models;

/**
 * Created by rdeshpan on 9/20/2017.
 */

public class FilterSettings {

    private String beginDate;
    private int sortSelectedIndex;
    private String sortSelectedText;
    private boolean isCheckedArts;
    private boolean isCheckedFashion;
    private boolean isCheckedSports;

    public FilterSettings() {
    }

    public FilterSettings(String beginDate, int sortSelectedIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
        this.beginDate = beginDate;
        this.sortSelectedIndex = sortSelectedIndex;
        this.sortSelectedText = sortSelectedText;
        this.isCheckedArts = isCheckedArts;
        this.isCheckedFashion = isCheckedFashion;
        this.isCheckedSports = isCheckedSports;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public int getSortSelectedIndex() {
        return sortSelectedIndex;
    }

    public void setSortSelectedIndex(int sortSelectedIndex) {
        this.sortSelectedIndex = sortSelectedIndex;
    }

    public boolean isCheckedArts() {
        return isCheckedArts;
    }

    public void setCheckedArts(boolean checkedArts) {
        isCheckedArts = checkedArts;
    }

    public boolean isCheckedFashion() {
        return isCheckedFashion;
    }

    public void setCheckedFashion(boolean checkedFashion) {
        isCheckedFashion = checkedFashion;
    }

    public boolean isCheckedSports() {
        return isCheckedSports;
    }

    public void setCheckedSports(boolean checkedSports) {
        isCheckedSports = checkedSports;
    }

    public String getSortSelectedText() {
        return sortSelectedText;
    }

    public void setSortSelectedText(String sortSelectedText) {
        this.sortSelectedText = sortSelectedText;
    }
}
