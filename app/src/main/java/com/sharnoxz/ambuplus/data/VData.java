package com.sharnoxz.ambuplus.data;


import java.util.ArrayList;

public class VData {
    private ArrayList<HData> hData;
    private String title,appDetails,weather;
    private int appIcon,stayHome,coVid;
    private boolean detailsOfApp;

    public VData(ArrayList<HData> hData, String title, String appDetails, int appIcon,int stayHome,int coVid ,String weather, boolean detailsOfApp) {
        this.hData = hData;
        this.title = title;
        this.appDetails = appDetails;
        this.appIcon = appIcon;
        this.stayHome = stayHome;
        this.coVid = coVid;
        this.weather = weather;
        this.detailsOfApp = detailsOfApp;
    }

    public int getCoVid() {
        return coVid;
    }

    public void setCoVid(int coVid) {
        this.coVid = coVid;
    }

    public int getStayHome() {
        return stayHome;
    }

    public void setStayHome(int stayHome) {
        this.stayHome = stayHome;
    }

    public ArrayList<HData> gethData() {
        return hData;
    }

    public void sethData(ArrayList<HData> hData) {
        this.hData = hData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(String appDetails) {
        this.appDetails = appDetails;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public boolean isDetailsOfApp() {
        return detailsOfApp;
    }

    public void setDetailsOfApp(boolean detailsOfApp) {
        this.detailsOfApp = detailsOfApp;
    }
}
