package com.duplicate.spi;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * 重复数据处理器
 *
 * @author mengzg@xiaoshouyi.com
 * Created on 2017/10/23 17:52
 */
public class DuplicateProcessor {

    private final static String DEFAULT_CHARSET = "gb18130";


    public Multimap<String, Object> processDuplicates(InputStream inputStream, String[] columns) {
        return processDuplicates(inputStream, DEFAULT_CHARSET, columns);
    }

    /**
     * 重复数据处理器
     *
     * @param inputStream 输入数据
     * @param columns     查重依赖列
     */
    public Multimap<String, Object> processDuplicates(InputStream inputStream, String encoding, String[] columns) {
        Multimap<String, Object> multimap = HashMultimap.create();

        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.getFormat().setLineSeparator("\n");
        CsvParser csvParser = new CsvParser(csvParserSettings);
        csvParser.beginParsing(inputStream, encoding);

        parseData(csvParser, columns, multimap);
        return multimap;
    }

    private void parseData(CsvParser parser, String[] columns, Multimap<String, Object> maps) {
        String[] strings = parser.parseNext();
        System.out.println("Titles: " + StringUtils.join(strings, ","));
        Record record;
        while ((record = parser.parseNextRecord()) != null) {
            String[] values = record.getValues(columns);
            String key = StringUtils.join(values, "-");
            maps.put(key, record.getValue("ID", String.class));
        }
    }

    public void outputProcessResult(Multimap<String, Object> multimaps, String[] columns, String outputFile, String encoding) throws IOException {
        CsvWriterSettings settings = new CsvWriterSettings();
        CsvWriter writer = new CsvWriter(new File(outputFile), encoding, settings);
        writer.writeHeaders(ArrayUtils.add(columns, "ID"));
        Iterator<String> keyIt = multimaps.keySet().iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            Collection<Object> objects = multimaps.get(key);
            for (Object obj : objects) {
                writer.writeRow(ArrayUtils.add(key.split("-"), obj));
            }
        }
        writer.flush();
        writer.close();
    }

}
