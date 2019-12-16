package com;

/**
 * 多线程读取一个文件
 *
 * @author Administrator
 */
class ReadOfThreads extends Thread {
    /**
     * 文件控制单元
     */
    private FileController readFileUnit;
    /**
     * 每个线程所平均分配的可读字节区域大小
     * 对于不可平均分配，有余数时，由最后一个线程读取
     */
    private long threadReadByte;
    /**
     * 当前线程的排序号
     */
    private int levelNumber;
    /**
     * 总线程数
     */
    private int threadNumbers;

    /**
     * @param filePath      待读取文件路径
     * @param threadNumbers 总线程数
     * @param levelNumber   当前线程的排序号
     * @param threadName    当前线程的名字
     */
    ReadOfThreads(String filePath, int threadNumbers, int levelNumber, String threadName) {
//        设置线程名字
        super(threadName);
        this.readFileUnit = new FileController(filePath);
//        文件总字节数
        long fileTotalByte = this.readFileUnit.getFileByte();
//        将总字节数 根据 线程数 平均分配
        this.threadReadByte = fileTotalByte / threadNumbers;
        this.levelNumber = levelNumber;
        this.threadNumbers = threadNumbers;
//        设置每个线程 文件起始读取指针位置
        readFileUnit.setFilePointer(this.threadReadByte * this.levelNumber);
    }

    @Override
    public void run() {
        int length = 0;
        do {
//            每次读取前，指针当前位置
            long currentPostionOfPointer = readFileUnit.getFilePointer();
//            预估每次读取之后  指针新的位置
//            指针新位置 = 指针当前位置 + 缓冲区字节长度
            int newPositionOfPointer = (int) (currentPostionOfPointer + readFileUnit.getBUFFER_SIZE());
//            下一个线程区域起始指针位置
            long startPositionOfNextThread = threadReadByte * (this.levelNumber + 1);
//            指针新位置 与 下一个线程区域起始指针位置 之差
//            判断 读取区域 是否超过自身所分配的区域
            int byteSent = (int) (newPositionOfPointer - startPositionOfNextThread);
//            排除最后一个线程，当前面的线程读取长度 超出 自身线程所分配的区域长度时，
            if (byteSent > 0 && this.threadNumbers - 1 != this.levelNumber) {
//                正确应读长度
                int correctLength = (int) (startPositionOfNextThread - currentPostionOfPointer);
                readFileUnit.readFile(correctLength);
            } else {
                readFileUnit.readFile();
            }
//            对于最后一个线程，文件读取没有值后，退出
            if (readFileUnit.getReadLen() == -1) {
                break;
            }
//            文件每次读取的字节有效长度，进行累加
            length += readFileUnit.getReadLen();
//            排除最后一个线程，当前面的线程读取长度 超出 自身线程所分配的区域长度时
//            代表所分配的区域读取完毕，则退出
            if (byteSent > 0 && this.threadNumbers - 1 != this.levelNumber) {
                break;
            }
        } while (true);
        System.out.println(getName() + " " + length);
    }
}
