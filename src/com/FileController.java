package com;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileController {
    private File file;
    private boolean fileExists;
    private String parentFile;
    private String suffix;
    private String fileName;
    private RandomAccessFile fileOutput = null;
    private final int BUFFER_SIZE = 1024;//1kb
    private byte[] buffer = new byte[BUFFER_SIZE];
    private int readLen;

    /**
     * 构造函数
     */
    public FileController(String pathName) {
        file = new File(pathName);
        fileExists = file.exists();
        fileName = file.getName();
        parentFile = file.getParent();
        if (file.isDirectory()) {
            System.out.println("传入的路径必须是文件");
        } else {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
    }

    /**
     * 获取文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取文件指针
     */
    public long getFilePointer() {
        try {
            fileOutput = fileOutput == null ? new RandomAccessFile(file, "rw") : fileOutput;
            return fileOutput.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置文件指针
     */
    public void setFilePointer(long filePointer) {
        try {
            fileOutput = fileOutput == null ? new RandomAccessFile(file, "rw") : fileOutput;
            fileOutput.seek(filePointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件读取的字节数
     */
    public int getReadLen() {
        return readLen;
    }

    /**
     * 设置文件读取的字节数
     */
    public void setReadLen(int readLen) {
        this.readLen = readLen;
    }

    /**
     * 获取文件后缀
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 获取文件的父目录路径
     *
     * @return 父目录路径字符串格式
     */
    public String getParentFile() {
        return parentFile;
    }

    /**
     * 获取文件大小
     */
    public long getFileByte() {
        return file.length();
    }

    /**
     * 写入 缓冲区
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    /**
     * 获取缓冲区 内容
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * 创建文件
     *
     * @param mustCreate 是否必须新建
     * @return 新建结果
     */
    public boolean createFile(boolean mustCreate) {
        boolean createResult = false;
        try {
            if (!fileExists || mustCreate) {
                if (fileExists) {
                    if (!file.delete()) {
                        return createResult;
                    }
                }
                createResult = file.createNewFile();
            } else {
                createResult = fileExists;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createResult;
    }

    /**
     * 写入文件
     */
    public boolean writeFile() {
        try {
            fileOutput = fileOutput == null ? new RandomAccessFile(file, "rw") : fileOutput;
            fileOutput.write(getBuffer(), 0, getReadLen());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件
     */
    public void readFile() {
        try {
            fileOutput = fileOutput == null ? new RandomAccessFile(file, "rw") : fileOutput;
            setReadLen(fileOutput.read(getBuffer()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭文件流
     */
    public void closeFile() {
        try {
            if (fileOutput != null) {
                fileOutput.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
