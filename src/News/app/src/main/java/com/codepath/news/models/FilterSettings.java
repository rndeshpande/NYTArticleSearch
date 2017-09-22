package com.codepath.news.models;

import org.parceler.Parcel;

/**
 * Created by rdeshpan on 9/20/2017.
 */
@Parcel
public class FilterSettings {

    private String beginYear;
    private String beginMonth;
    private String beginDay;
    private int sortSelectedIndex;
    private String sortSelectedText;
    private boolean isCheckedArts;
    private boolean isCheckedFashion;
    private boolean isCheckedSports;

    public FilterSettings() {
    }

    public FilterSettings(String beginYear, String beginMonth, String beginDay, int sortSelectedIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
        this.beginYear = beginYear;
        this.beginMonth = beginMonth;
        this.beginDay = beginDay;
        this.sortSelectedIndex = sortSelectedIndex;
        this.sortSelectedText = sortSelectedText;
        this.isCheckedArts = isCheckedArts;
        this.isCheckedFashion = isCheckedFashion;
        this.isCheckedSports = isCheckedSports;
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

    public void setSortSelectedText(String sortSelectedText) { this.sortSelectedText = sortSelectedText; }

    public String getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(String beginYear) {
        this.beginYear = beginYear;
    }

    public String getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(String beginMonth) {
        this.beginMonth = beginMonth;
    }

    public String getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(String beginDay) {
        this.beginDay = beginDay;
    }
}
