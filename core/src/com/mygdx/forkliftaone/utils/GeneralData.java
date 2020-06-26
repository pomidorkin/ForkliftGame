package com.mygdx.forkliftaone.utils;

public class GeneralData {
    private ForkliftData[] allModels;
    private MapData[] allMaps;

    public GeneralData(ForkliftData[] fd, MapData[] md){
        this.allMaps = md;
        allModels = fd;
    };

    public ForkliftData[] getAllModels() {
        return allModels;
    }

    public MapData[] getAllMaps() {
        return allMaps;
    }
}
