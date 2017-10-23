package com.reacher.obfuscate;

import com.reacher.utils.CsvFileUtil;
import com.reacher.utils.MD5Util;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObfuscateContent {

    private static final String ENCODING = "utf-8";

    /**
     * @param inputFilename  源文件
     * @param outputFilename 加密后生产的文件
     * @param columns        需要被加密的列
     * @throws Exception
     */
    public void obfuscate(String inputFilename, String outputFilename, String[] columns) throws Exception {
        this.obfuscate(inputFilename, outputFilename, columns, ENCODING);
    }

    /**
     * @param inputFilename  源文件
     * @param outputFilename 加密后生产的文件
     * @param columns        需要被加密的列
     * @param encoding       文件编码
     * @throws Exception
     */
    public void obfuscate(String inputFilename, String outputFilename, String[] columns, String encoding) throws Exception {

        CsvParser parser = CsvFileUtil.getParser(inputFilename, encoding);

        //获取表头
        String[] datas = parser.parseNext();
        Map<String, Integer> titleToIndexMap = new HashMap<String, Integer>();
        for (int i = 0; i < datas.length; ++i) {
            titleToIndexMap.put(datas[i], i);
        }
        String validate = this.validate(titleToIndexMap, columns);
        if (null != validate) {
            throw new Exception("要转码的列[" + validate + "]不存在");
        }
        CsvWriter writer = CsvFileUtil.getWriter(outputFilename, encoding);
        writer.writeRow(datas);//写入表头

        CsvWriter obfuscateMappings = CsvFileUtil.getWriter(outputFilename.replaceAll("\\.csv", "") + "-mappings.csv", encoding);
        obfuscateMappings.writeRow(this.buildMappingsTitle(columns));//写表头

        while ((datas = parser.parseNext()) != null) {
            obfuscateMappings.writeRow(obfuscate(datas, columns, titleToIndexMap));
            writer.writeRow(datas);
        }
        parser.stopParsing();
        writer.close();
        obfuscateMappings.close();

    }

    /**
     * @param datas           原始数据
     * @param columns         需要被加密的列
     * @param titleToIndexMap 文件每列对应的下标
     * @throws Exception
     */
    /**
     *
     * @param datas           原始数据
     * @param columns         需要被加密的列
     * @param titleToIndexMap 文件每列对应的下标
     * @return                原始数据和加密后数据生成的列
     * @throws Exception
     */
    private String[] obfuscate(String[] datas, String[] columns, Map<String, Integer> titleToIndexMap) throws Exception {
        List<String> temps = new ArrayList<String>();
        for (String column: columns) {
            temps.add(datas[titleToIndexMap.get(column)]);
            if (null != datas[titleToIndexMap.get(column)] && 0 < datas[titleToIndexMap.get(column)].length()) {
                datas[titleToIndexMap.get(column)] = MD5Util.md5(datas[titleToIndexMap.get(column)]);
            }
            temps.add(datas[titleToIndexMap.get(column)]);
        }
        return temps.toArray(new String[0]);
    }

    private String validate(Map<String, Integer> titleToIndexMap, String[] columns) {
        for (String column: columns) {
            if (!titleToIndexMap.containsKey(column)) {
                return column;
            }
        }
        return null;
    }

    private String[] buildMappingsTitle(String[] columns) {
        String[] titles = new String[columns.length * 2];
        int index = 0;
        for (String column: columns) {
            titles[index++] = column;
            titles[index++] = "obfuscate";
        }

        return titles;
    }
}
