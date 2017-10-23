package com.reacher.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;

public class CSVFileUtil {

    public static CSVReader reader(String filename, String encoding) throws Exception {
        return new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding)));
    }

    public static CSVWriter writer(String filename, String encoding) throws Exception {
        return new CSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), encoding)));
    }

    public static void close(Closeable ...files) throws Exception {
        if (null == files || 0 == files.length) {
            return;
        }
        for (Closeable file : files) {
            if (file instanceof Writer) {
                ((Writer) file).flush();
            }
            file.close();
        }
    }
}
