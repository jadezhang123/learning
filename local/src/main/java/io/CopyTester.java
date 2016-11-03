package io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import test.TimingTester;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by Zhang Junwei on 2016/10/25.
 */
public class CopyTester {
    public static void main(String[] args) {
        TimingTester[] testers = {
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream("F:\\testForLearning\\largeFile1.test"));
                            BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream("F:\\testForLearning\\Tbuffer.test"));
                            byte[] buffer = new byte[1024];
                            int readLength;
                            while ((readLength = bufferedInput.read(buffer))  != -1) {
                                bufferedOutput.write(buffer);
                            }
                            bufferedInput.close();
                            bufferedOutput.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            FileChannel in = new FileInputStream("F:\\testForLearning\\largeFile2.test").getChannel();
                            FileChannel out = new FileOutputStream("F:\\testForLearning\\Tchannel.test").getChannel();
                            in.transferTo(0, in.size(), out);
                            in.close();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("self => stream,stream");
                            FileInputStream fileInputStream = new FileInputStream("F:\\testForLearning\\file3.test");
                            FileOutputStream fileOutputStream = new FileOutputStream("F:\\testForLearning\\Tstream.test");
                            //缓冲区大小会影响效率
                            byte[] buffer = new byte[1024*4];
                            int readLength;
                            while ((readLength = fileInputStream.read(buffer))  != -1) {
                                fileOutputStream.write(buffer);
                            }
                            fileInputStream.close();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("file,stream");
                            File fromFile = new File("F:\\testForLearning\\file1.test");
                            FileOutputStream out = new FileOutputStream("F:\\testForLearning\\Tchannel1.test");
                            //底层实现stream to steam
                            FileUtils.copyFile(fromFile, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("channel => file,file");
                            File fromFile = new File("F:\\testForLearning\\file2.test");
                            File toFile = new File("F:\\testForLearning\\Tchannel2.test");
                            //底层实现 channel to channel
                            FileUtils.copyFile(fromFile, toFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new TimingTester() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("io => file,file");
                            FileInputStream fileInputStream = new FileInputStream("F:\\testForLearning\\file4.test");
                            FileOutputStream fileOutputStream = new FileOutputStream("F:\\testForLearning\\Tio.test");
                            IOUtils.copy(fileInputStream, fileOutputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }};
        /*for (TimingTester tester : testers) {
            tester.execute();
        }*/
        testers[2].execute();
        testers[3].execute();
        testers[4].execute();
        testers[5].execute();

    }


}
