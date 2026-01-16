package com.sik.zak.main;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class SdSpeedTest {

    public static void main(String[] args) throws IOException {

        String DP2 = "%.2f ";

        byte data[] = {0,0,0,5,2,8,3,8,5,6,
                0,1,4,3,2,6,2,0,8,3,
                5,1,9,0,5,5,8,2,4,6,
                3,6,2,8,5,6,7,0,0,0,
                0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,3,3,3,3,3,3,
                7,6,5,4,5,6,7,8,9,8,
                7,8,9,0,2,1,2,3,2,3,
                2,3,4,3,2,4,5,4,3,2,
                3,4,3,2,1,2,3,0,9,8};
        byte eof[] = {0};

        /*
        Change cardPath variable to wherever your card is mounted
         */
        String cardPath = "/Volumes/NO_NAME/";

        String testFilePfx = "testFile";
        String testFileSfx = ".bin";

        List<String> results = new ArrayList<>();

        System.out.println("Testing....");
        int bytesWritten = 0;
        results.add("Small file write test (10 * 10k files)...");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Path file = Paths.get(cardPath + testFilePfx + i + testFileSfx);
            Files.write(file, data);
            for (int j = 0; j < 105; j++) {
                    Files.write(file, data, StandardOpenOption.APPEND);
                    bytesWritten += data.length;
            }
            Files.write(file, eof, StandardOpenOption.WRITE);
            bytesWritten++;
        }

        long endTime = System.currentTimeMillis();
        results.add("Bytes written: " + bytesWritten);
        results.add("Time taken   : " + (endTime - startTime) + "ms");
        float timeSeconds =  (float) (endTime - startTime) /1000;
        results.add("Write Speed  : " + String.format(DP2, (bytesWritten/1048)/timeSeconds) + " Kb/s");

        bytesWritten = 0;
        results.add(" ");
        results.add("Large file write test (10 * 1M files)...");
        startTime = System.currentTimeMillis();
        for (int i = 10; i < 20; i++) {
            Path file = Paths.get(cardPath + testFilePfx + i + testFileSfx);
            Files.write(file, data);
            for (int j = 0; j < 1048; j++) {
                for (int k = 0; k < 10; k++) {
                    Files.write(file, data, StandardOpenOption.APPEND);
                    bytesWritten += data.length;
                }
            }
            Files.write(file, eof, StandardOpenOption.WRITE);
            bytesWritten++;
        }

        endTime = System.currentTimeMillis();
        results.add("Bytes written: " + bytesWritten);
        results.add("Time taken   : " + (endTime - startTime) + "ms");
        timeSeconds =  (float) (endTime - startTime) /1000;
        results.add("Write Speed  : " + String.format(DP2, (bytesWritten/1048)/timeSeconds) + " Kb/s");

        byte readData[] = {};
        int bytesRead = 0;
        results.add(" ");
        results.add("Read test...");
        startTime = System.currentTimeMillis();
        for (int i = 10; i < 20; i++) {
            Path file = Paths.get(cardPath + testFilePfx + i + testFileSfx);
            Files.write(file, data);
            for (int j = 0; j < 1048; j++) {
                for (int k = 0; k < 10; k++) {
                    readData = Files.readAllBytes(file);
                    bytesRead += readData.length;
                }
            }
            readData = eof;
        }

        endTime = System.currentTimeMillis();
        results.add("Bytes read   : " + bytesRead);
        results.add("Time taken   : " + (endTime - startTime) + "ms");
        timeSeconds =  (float) (endTime - startTime) /1000;
        results.add("Write Speed  : " + String.format(DP2, (bytesRead/1048)/timeSeconds) + " Kb/s");

        for (String s : results) {
            System.out.println(s);
        }

        System.out.println("Tidying....");
        for (int i = 0; i < 20; i++) {
            Path file = Paths.get(cardPath + testFilePfx + i + testFileSfx);
            Files.delete(file);
        }

        System.out.println("Writing results....");
        File f = new File(cardPath + "ZTSpeed.txt");
        FileUtils.writeStringToFile(f, "ZT Speed Test results for this card on: " + DateTime.now() + "\n", "UTF-8", true);
        for (String s : results) {
            FileUtils.writeStringToFile(f, s + "\n" , "UTF-8", true);
        }
    }

}