package com.sergeikostin.simpletodo.util;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergei.kostin on 7/23/17.
 */

public class DataPersistUtil {

    public static List<String> readItems( Context context ){
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            return new ArrayList<>( FileUtils.readLines( todoFile ) );
        } catch(IOException e){
             return new ArrayList<>(  );
        }
    }

    public static void writeItems(Context context, List<String> items){
        File filesDir = context.getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines( todoFile, items ) ;
        } catch(IOException e){
            Toast.makeText( context, "Error saving items to file", Toast.LENGTH_SHORT ).show();
        }
    }


}
