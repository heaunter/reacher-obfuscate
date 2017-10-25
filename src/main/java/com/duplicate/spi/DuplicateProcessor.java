package com.duplicate.spi;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

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
        Record record;
        while ((record = parser.parseNextRecord()) != null) {
            String[] values = record.getValues(columns);
            String key = StringUtils.join(values, "-");
            maps.put(key, record.getValue("ID", String.class));
        }
    }

    public void outputProcessResult(Multimap<String, Object> multimaps, String outputFile) {

    }

}
