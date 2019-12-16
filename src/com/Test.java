package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Test {

    public static void main(String[] args) throws IOException {
//        System.out.println(test1.hashCode());
//        Path p1 = Paths.get("./test.png"); // 构造一个Path对象
////        System.out.println(p1);
//        Path p2 = p1.toAbsolutePath(); // 转换为绝对路径
////        System.out.println(p2);
//        Path p3 = p2.normalize(); // 转换为规范路径
////        System.out.println(p3);
//        File f = p3.toFile(); // 转换为File对象
////        fileList(f, 0);
//        Test test = new Test();
        long start = System.currentTimeMillis();
//        test.readOfThreadMode("test.png");
        readAsString(new FileInputStream("test.png"));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private static void readZip() {
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream("test.zip"))) {
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                System.out.println(name);
                if (!entry.isDirectory()) {
                    int n;
                    while ((n = zip.read()) != -1) {
                        System.out.println(n);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = input.read()) != -1) {
            sb.append(n);
        }
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * 列出指定目录下的所有子目录和文件，并按层次打印。
     *
     * @param file  文件或文件夹
     * @param level 当前文件或文件夹的层级，初始为0
     */
    private static void fileList(File file, int level) {
//        针对不同的操作系统
        String separator = File.separator;
        String span = "-";
        if (file.isDirectory()) {
            System.out.println(span.repeat(level) + file.getName() + separator);
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    fileList(subFile, level + 1);
                }
            }
        } else {
            System.out.println(span.repeat(level) + file.getName());
        }
    }

    /**
     * 复制指定文件 到 指定位置
     * <p>
     * 每次读取之前，将旧文件的文件指针设置为新文件的长度
     * 每次写入之前
     * 将新文件的文件指针设置为新文件的长度，预防文件写入中断情况
     * 将新文件的缓冲区设置为旧文件的缓冲区
     * 将新文件的缓冲区实际有效值长度设置为旧文件的缓冲区实际有效值长度
     * <p>
     * 中断之后可继续复制、写入
     *
     * @param oldPath 待复制的文件路径 绝对或相对路径
     * @param newPath 新文件路径 绝对或相对路径
     * @return 复制结果
     */
    private boolean copyFile(String oldPath, String newPath) {
        FileController readFileController = new FileController(oldPath);
        FileController writeFileController = new FileController(newPath);
        do {
//            开始读之前 设置文件指针
            readFileController.setFilePointer(writeFileController.getFileByte());
//            开始读
            readFileController.readFile();
//            开始写之前 将读取结果写入缓冲区
            writeFileController.setBuffer(readFileController.getBuffer());
//            开始写之前 设置缓冲区实际有效值长度
            writeFileController.setReadLen(readFileController.getReadLen());
//            开始写之前 设置文件指针
            writeFileController.setFilePointer(writeFileController.getFileByte());
//            判断 读取结果长度有效 和 写入结果
//            采用短路与模式
        } while (readFileController.getReadLen() != -1 && writeFileController.writeFile());
//        关闭流
        readFileController.closeFile();
        writeFileController.closeFile();
//        检查 原文件 与 新文件 的字节长度一致性
        return readFileController.getFileByte() == writeFileController.getFileByte();
    }

    private void readOfThreadMode(String filePath) {
        int threadNumbers = 4;
        ReadOfThreads[] threads = new ReadOfThreads[threadNumbers];
        for (int i = 0; i < threadNumbers; i++) {
            ReadOfThreads readOfThread = new ReadOfThreads(filePath, threadNumbers, i, "Thread-" + i);
            threads[i] = readOfThread;
            threads[i].start();
        }
        for (ReadOfThreads t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}




