package com.work.control;

import com.work.file.Read;
import com.work.file.Write;
import com.work.model.Data;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;


public class Control extends Thread {

    private boolean dataReady;
    private Data data;

    private TextArea textArea;

    private ArrayList<Byte> bytesRiesived;
    private LinkedList<Integer> listIntegers = new LinkedList<>();

    public Control(TextArea textArea) {
        bytesRiesived = new ArrayList<>();
        this.textArea = textArea;
    }

    public boolean isDataReady() {
        return dataReady;
    }

    private void setDataReady(boolean dataReady) {
        this.dataReady = dataReady;
    }

    public Data getData() {
        return data;
    }

    private void setData(Data data) {
        this.data = data;
    }

    public void addToList(LinkedList<Byte> addBytes) {
        setDataReady(false);
        bytesRiesived.addAll(addBytes);
        //System.out.println("add to list = " + bytesRiesived.toString());
        parseArray(bytesRiesived);

        //parseByTest(bytesRiesived);
        //Write.writing("notes3.txt",listIntegers);
        delete();

    }

    private void parseArray(ArrayList<Byte> bytesRiesived) {
        System.out.println("received bytes " + bytesRiesived.size());
        if (bytesRiesived.size() >= 15) {

           /* data.setIdHigh(fromBinToInt(dataToBin(bytesRiesived.get(0))));
            data.setIdLow(fromBinToInt(dataToBin(bytesRiesived.get(2))
                    + dataToBin(bytesRiesived.get(1))));*/
           for(int i = 3; i < bytesRiesived.size(); i+=12) {
               int j = i;
               Data data = new Data();
               LocalDate localDate = LocalDate.of(fromBinToInt(dataToBin(bytesRiesived.get(i+3))
                               + dataToBin(bytesRiesived.get(i+2))),
                       fromBinToInt(dataToBin(bytesRiesived.get(i+1))), fromBinToInt(dataToBin(bytesRiesived.get(i))));
               data.setDate(localDate);
               LocalTime localTime = LocalTime.of(fromBinToInt(dataToBin(bytesRiesived.get(i+4))),
                       fromBinToInt(dataToBin(bytesRiesived.get(i+5))));
               data.setTime(localTime);
               data.setDiary(fromBinToInt(dataToBin(bytesRiesived.get(i+6))));
               data.setEat(fromBinToInt(dataToBin(bytesRiesived.get(i+7))));
               data.setGlucose(fromBinToInt(dataToBin(bytesRiesived.get(i+9)) + dataToBin(bytesRiesived.get(i+8))));
               data.setHct(fromBinToInt(dataToBin(bytesRiesived.get(i+11)) + dataToBin(bytesRiesived.get(i+10))));
               Platform.runLater(() -> textArea.appendText(data.toString()));
               Write.writing(data);
               setDataReady(true);
               setData(data);
           }
        }
//        for (byte byteReceived : bytesRiesived) {
//            listIntegers.add(fromBinToInt(dataToBin(byteReceived)));
//        }
//        System.out.println("listIntegers : " + listIntegers);

    }


    public void parseByTest(LinkedList<Byte> bytesRiesived) {
        for (int i = 0; i < bytesRiesived.size(); i++) {
            if (bytesRiesived.get(i) == 92) {
                bytesRiesived.remove(i);
            }
            if (bytesRiesived.get(i) == -59) {
                bytesRiesived.remove(i);
                i -= 2;

            }
        }
    }

    private String dataToBin(byte data) {
        String bs = null;
        if (data >= 0) {
            bs = Integer.toBinaryString(data);
            while (bs.length() < 8) {
                bs = "0" + bs;
            }
        } else {
            bs = Integer.toBinaryString(data);
            bs = bs.substring(24, 32);
        }
        //System.out.println(bs);
        return bs;

    }

    private int fromBinToInt(String binaryString) {
        int decDigit = 0;
        char[] binStringToArray = binaryString.toCharArray(); // Преобразуем строку в массив символов
        //System.out.println(Arrays.toString(binStringToArray));
        if (binStringToArray[0] != '1') {
            int j = 0;
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                decDigit += Character.getNumericValue(binStringToArray[i]) * Math.pow(2, j);
                j++;
            }
        } else {
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                binStringToArray[i] = binStringToArray[i] == '1' ? '0' : '1';

            }
            int j = 0;
            for (int i = binStringToArray.length - 1; i >= 0; i--) {
                decDigit += Character.getNumericValue(binStringToArray[i]) * Math.pow(2, j);
                j++;

            }
            decDigit = (decDigit + 1) * (-1);
        }
        //System.out.println(Arrays.toString(binStringToArray));

        // System.out.println(decDigit);
        return decDigit;
    }


    public void delete() {
        bytesRiesived.removeAll(bytesRiesived);
        listIntegers.clear();
    }
}