package com.unibz.serendipity.utilities;

import android.content.Context;
import android.content.res.Resources;

import com.unibz.serendipity.R;
import com.unibz.serendipity.Sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Cody on 02.11.15.
 */
public class CSVParser {

    private static final String COMMA_DELIMITER = ",";
    private int fileName;
    private Context context;

    public CSVParser(Context context) {
        this.context = context;
        readCsvFile();

    }

    private void readCsvFile() {

        BufferedReader fileReader = null;
        fileName = R.raw.soundlist;

        try {


            ArrayList<Sound> soundList = new ArrayList<Sound>();

            String line = "";

            //Create the file reader
            Resources res = context.getResources();
            fileReader = new BufferedReader(new InputStreamReader(res.openRawResource(R.raw.soundlist)));

            //Read the CSV file header to skip it

            fileReader.readLine();


            SoundListCreator listCreator = new SoundListCreator();
            //Read the file line by line starting from the second line

            while ((line = fileReader.readLine()) != null) {

                //Get all tokens available in line

                String[] tokens = line.split(COMMA_DELIMITER);

                if (tokens.length > 3) {
                    listCreator.addSound(tokens[0],Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]),tokens[3]);
                }

            }


        }

        catch (Exception e) {

            System.out.println("Error in CsvFileReader !!!");

            e.printStackTrace();

        } finally {

            try {

                fileReader.close();

            } catch (IOException e) {

                System.out.println("Error while closing fileReader !!!");

                e.printStackTrace();

            }

        }



    }

}


