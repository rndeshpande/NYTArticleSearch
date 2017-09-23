package com.codepath.news.models;

import org.parceler.Parcel;

/**
 * Created by rdeshpan on 9/20/2017.
 */
@Parcel
public class FilterSettings {

    public int beginYear;
    public int beginMonth;
    public int beginDay;
    public int sortSelectedIndex;
    public String sortSelectedText;
    public boolean isCheckedArts;
    public boolean isCheckedFashion;
    public boolean isCheckedSports;

    public FilterSettings() {
    }

    public FilterSettings(int beginYear, int beginMonth, int beginDay, int sortSelectedIndex, String sortSelectedText, boolean isCheckedArts, boolean isCheckedFashion, boolean isCheckedSports) {
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

    public void setSortSelectedText(String sortSelectedText) {
        this.sortSelectedText = sortSelectedText;
    }

    public int getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }

    public int getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(int beginMonth) {
        this.beginMonth = beginMonth;
    }

    public int getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(int beginDay) {
        this.beginDay = beginDay;
    }
}
