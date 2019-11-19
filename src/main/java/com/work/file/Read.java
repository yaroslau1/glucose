package com.work.file;

import com.work.model.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Read {

    public String reading(String path) {
        String string = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ( (s = br.readLine()) != null) {
                if (string == null) {
                    string = s;
                } else {
                    string += s;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return string;
    }

    public static List<Data> reading(){
        List<Data> records = null;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("notes3.txt")))
        {
            records = (ArrayList<Data>) objectInputStream.readObject();
            System.out.println(records.toString());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return records;
    }
}