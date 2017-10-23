package com.duplicate.spi;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.InputStream;

/**
 * 重复数据处理器
 *
 * @author mengzg@xiaoshouyi.com
 * Created on 2017/10/23 17:52
 */
public class DuplicateProcessor {

    /**
     * 重复数据处理器
     *
     * @param inputStream    输入数据
     * @param columns        查重依赖列
     * @param outputFilename 输出文件
     */
    public void processDuplicates(InputStream inputStream, String[] columns, String outputFilename) {
        Multimap<String, String> multimap = HashMultimap.create();

        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.getFormat().setLineSeparator("\n");
        CsvParser csvParser = new CsvParser(csvParserSettings);
        csvParser.beginParsing(inputStream);

        Record record = csvParser.parseNextRecord();
        String[] values = record.getValues(columns);

    }

}
