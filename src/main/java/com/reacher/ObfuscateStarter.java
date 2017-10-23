package com.reacher;

import com.reacher.obfuscate.ObfuscateContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObfuscateStarter {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入源文件的绝对路径(csv格式): ");
        String inputFilename = scanner.next();

        System.out.print("请输入加密后文件输出的绝对路径(csv格式): ");
        String outputFilename = scanner.next();

        System.out.print("请输入源文件的编码格式: ");
        String encoding = scanner.next();

        List<String> columns = new ArrayList<String>();
        while (true) {
            System.out.print("请输需要被加密的列(输入#结束): ");
            String column = scanner.next();
            if ("#".equals(column)) {
                break;
            }
            columns.add(column);
        }

        ObfuscateContent obfuscateContent = new ObfuscateContent();

        obfuscateContent.obfuscate(inputFilename, outputFilename, columns.toArray(new String[0]), encoding);

        System.out.println("转码完成!");
    }
}
