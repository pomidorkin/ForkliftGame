package com.mygdx.forkliftaone.utils;

import com.google.gson.Gson;
import com.mygdx.forkliftaone.ForkliftModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessInventory {

    private Gson gson = new Gson();
    private File myFile = new File("inventory.json");
    private File generalData = new File("generalData.json");


    public boolean write(Inventory inv){

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(myFile));
            bw.write(gson.toJson(inv));
            bw.flush();
            bw.close();

            return true;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean write(GeneralData gd){

        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(generalData));
            bw.write(gson.toJson(gd));
            bw.flush();
            bw.close();

            return true;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public Inventory read(){
        BufferedReader bufferedReader = null;

        if (myFile.exists()){
            try {
                bufferedReader = new BufferedReader(new FileReader(myFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Creating new jSon inventory file, where only SMALL forklift is purchased

            List<ForkliftData> fd = new ArrayList<>();
            fd.add(new ForkliftData());
            fd.get(0).setTubes(3);
            fd.get(0).setName(ForkliftModel.ModelName.MODEL_1);
            fd.get(0).setEngine(3);
            fd.get(0).setPrice(new PriceHandler(0, false));
            fd.get(0).setPurchased(true);

            // Map saving
            List<MapData> md = new ArrayList<>();
            md.add(new MapData());
            md.get(0).setName(MapModel.MapName.TEST);
            md.get(0).setPurchased(true);

            SettingsData sd = new SettingsData();
            sd.setMusicVolume(1f);
            sd.setSoundVolume(1f);


            Inventory inv = new Inventory(15000, 0, false, false, fd, md, sd);
            write(inv);
            return inv;
        }

        Gson gson = new Gson();
        Inventory inv = gson.fromJson(bufferedReader, Inventory.class);

        return inv;
    }

    public GeneralData readGeneralData(){
        BufferedReader bufferedReader = null;
        // General data (created for updates)
        if (generalData.exists()){
            try {
                bufferedReader = new BufferedReader(new FileReader(generalData));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            // Creating new jSon inventory file, where only SMALL forklift is purchased

            ForkliftData[] fd;
            fd = new ForkliftData[3];
            fd[0] = new ForkliftData();
            fd[0].setTubes(3);
            fd[0].setName(ForkliftModel.ModelName.MODEL_1);
            fd[0].setEngine(3);
            fd[0].setPrice(new PriceHandler(0, false));
            fd[0].setPurchased(true);

            fd[1] = new ForkliftData();
            fd[1].setTubes(3);
            fd[1].setName(ForkliftModel.ModelName.MODEL_4);
            fd[1].setEngine(3);
            fd[1].setPrice(new PriceHandler(3000, false));
            fd[1].setPurchased(false);

            fd[2] = new ForkliftData();
            fd[2].setTubes(3);
            fd[2].setName(ForkliftModel.ModelName.MODEL_5);
            fd[2].setEngine(3);
            fd[2].setPrice(new PriceHandler(7000, false));
            fd[2].setPurchased(false);

            // Map saving
            MapData[] md;
            md = new MapData[2];
            md[0] = new MapData();
            md[0].setName(MapModel.MapName.CUSTOM);
            md[0].setPrice(new PriceHandler(100, false));
            md[0].setPurchased(false);

            md[1] = new MapData();
            md[1].setName(MapModel.MapName.TEST);
            md[1].setPrice(new PriceHandler(500, false));
            md[1].setPurchased(true);


            GeneralData gd = new GeneralData(fd, md);
            write(gd);
            return gd;
        }
        Gson gson = new Gson();
        GeneralData gd = gson.fromJson(bufferedReader, GeneralData.class);

        return gd;
    }
}