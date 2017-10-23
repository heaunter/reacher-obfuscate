package com.reacher.utils;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import java.io.*;

public class CsvFileUtil {

    public static CsvParser getParser(String filename, String encoding) throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        CsvParser parser = new CsvParser(settings);
        parser.beginParsing(new InputStreamReader(new FileInputStream(filename), encoding));
        return parser;
    }

    public static CsvWriter getWriter(String filename, String encoding) throws Exception {
        CsvWriterSettings settings = new CsvWriterSettings();
        settings.getFormat().setQuote('\'');
        settings.getFormat().setQuoteEscape('\'');
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filename), encoding);
        return new CsvWriter(outputStreamWriter, settings);
    }

    public static void close(Closeable... files) throws Exception {
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
