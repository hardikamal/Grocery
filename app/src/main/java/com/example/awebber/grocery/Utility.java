package com.example.awebber.grocery;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.awebber.grocery.data.GroceryContract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by awebber on 4/27/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 * */
public class Utility {

    public static final String TAG ="Utility";
   public Utility(){

    }

    public static String  groceryDetailTableIdentifer(String theTable,String theValue){
    String returnString ="None of the following";
    switch (theTable){
        case GroceryContract.GroceryEntry.TABLE_NAME: {
            returnString = theValue + " is a item "  ;
            break;
        }
        case GroceryContract.CategoryEntry.TABLE_NAME: {
            returnString =  theValue + " is a Category Some " +theValue +" Are" ;
            break;
        }
        case GroceryContract.BrandEntry.TABLE_NAME: {
            returnString = theValue + " is a Brand other products by " +theValue +" Are" ;
            break;
        }
    }
    return returnString;
}

    public  List<String>   LoadTextFile(Context context,int theTextFile){
       List<String> theList = new ArrayList<String>();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = context.getResources().openRawResource(theTextFile);

        BufferedReader reader = null;
        try {
            reader =  new BufferedReader(new InputStreamReader(inputStream));
            String text = null;
            while ((text = reader.readLine()) != null) {
                theList.add(text);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
    return theList;
    }





}