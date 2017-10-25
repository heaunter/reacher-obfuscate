package com.duplicate;

import com.duplicate.spi.DuplicateProcessor;
import com.google.common.collect.Multimap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 重复处理启动类
 * Created by mengzg<mengzg@xiaoshouyi.com> on 2017/10/25 9:41
 */
public class DuplicateProcessStarter {


    /*
    数据处理完后输出MUltiMap里的状况。
    比如，一共处理了多少records，MultiMap里有多少key（就是Unique线索数），MultiMap里有多少entry对应的list只有一个entry（无重复）；
    把他们删掉后输出剩下的（有重复）的records。
    输出可以有以下columns：输入的columns（电话+公司），id。*/
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入源文件的绝对路径(csv格式): ");
        String inputFilename = scanner.next();

        System.out.print("请输入需要查重的列名(请以都#隔开): ");
        String columnsInput = scanner.next();
        String[] columns = columnsInput.split("#");

        System.out.print("请输入输出文件的绝对路径(csv格式): ");
        String outputFilename = scanner.next();

        System.out.print("请输入源文件的编码格式: ");
        String encoding = scanner.next();

        InputStream stream = new FileInputStream(inputFilename);

        DuplicateProcessor duplicateProcessor = new DuplicateProcessor();
        Multimap<String, Object> multimap = duplicateProcessor.processDuplicates(stream, encoding, columns);
        System.out.println("处理后的数据集合：" + multimap.asMap());
        System.out.println("总数据条数：" + multimap.values().size());
        System.out.println("unique线索条数：" + multimap.keySet().size());

        int uniqueSingle = 0;

        Iterator<String> keyIt = multimap.keySet().iterator();
        while (keyIt.hasNext()) {
            String key = keyIt.next();
            Collection<Object> objects = multimap.get(key);
            if (objects.size() == 1) {
                ++uniqueSingle;
                keyIt.remove();
            }
        }
        System.out.println("single线索条数：" + uniqueSingle);

        duplicateProcessor.outputProcessResult(multimap, outputFilename, encoding);
        System.out.println("文件输出到：" + outputFilename);

    }
}
