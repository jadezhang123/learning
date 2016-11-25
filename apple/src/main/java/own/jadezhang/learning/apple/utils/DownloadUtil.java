package own.jadezhang.learning.apple.utils;

import org.apache.commons.io.IOUtils;
import own.jadezhang.common.domain.BaseDomain;
import own.jadezhang.common.exception.BizException;
import own.jadezhang.learning.apple.config.Configurations;
import own.jadezhang.learning.apple.exception.DownloadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件下载工具类
 * Created by Zhang Junwei on 2016/11/3.
 */
public class DownloadUtil {


    /**
     * @param request
     * @param response
     * @param filePath
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        download(request, response, file);
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file) {
        if (file == null || !file.exists()) {
            responseOnError(response, DownloadException.FILE_NOT_EXIST_STR);
            return;
        }
        setResponseHeader(request, response, file, file.getName());
        doDownload(file, response);
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file, FilenameGenerator fileNameGenerator) {
        if (file == null || !file.exists()) {
            responseOnError(response, DownloadException.FILE_NOT_EXIST_STR);
            return;
        }
        String fileName = fileNameGenerator != null ? fileNameGenerator.generateFilename(file) : file.getName();
        setResponseHeader(request, response, file, fileName);
        doDownload(file, response);
    }

    /**
     * @param request
     * @param response
     * @param files
     */
    public static void downloadAfterCompress(HttpServletRequest request, HttpServletResponse response, File[] files) {
        try {
            String compressedFilePath = compressFiles(files, null);
            File compressedFile = new File(compressedFilePath);

            download(request, response, compressedFile);

            compressedFile.delete();
        } catch (DownloadException e) {
            responseOnError(response, e.getMessage());
        }
    }

    public static void downloadAfterCompress(HttpServletRequest request, HttpServletResponse response, File[] files, FilenameGenerator fileNameGenerator) {
        try {
            String compressedFilePath = compressFiles(files, fileNameGenerator);

            File compressedFile = new File(compressedFilePath);

            String filename;
            if (fileNameGenerator != null) {
                filename = fileNameGenerator.generateFilename(compressedFile);
            } else {
                filename = compressedFile.getName();
            }

            setResponseHeader(request, response, compressedFile, filename);
            download(request, response, compressedFile, fileNameGenerator);
            compressedFile.delete();
        } catch (DownloadException e) {
            responseOnError(response, e.getMessage());
        }
    }

    private static void responseOnError(HttpServletResponse response, String errorMsg) {
        try {
            response.setContentType("text/html;charset=utf-8");
            OutputStream out = response.getOutputStream();
            out.write(("<script>alert('" + errorMsg + "');window.close();</script>").getBytes("utf-8"));
            IOUtils.closeQuietly(out);
        } catch (IOException e) {
            throw new BizException(DownloadException.DOWNLOAD_ERROR_CODE, DownloadException.COMPRESS_ERROR_STR, e);
        }
    }

    private static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, File file, String fileName) {
        try {
            response.setContentType(Files.probeContentType(file.toPath()));
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            }
        } catch (IOException e) {
            throw new BizException(DownloadException.DOWNLOAD_ERROR_CODE, DownloadException.DOWNLOAD_ERROR_STR, e);
        }
    }

    private static void doDownload(File file, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
        } catch (IOException e) {
            throw new BizException(DownloadException.DOWNLOAD_ERROR_CODE, DownloadException.DOWNLOAD_ERROR_STR, e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    private static String compressFiles(File[] files, FilenameGenerator fileNameGenerator) throws DownloadException {
        String compressedFilePath = Configurations.getTempPath() + File.separator + System.currentTimeMillis() + "-temp.zip";
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(compressedFilePath));
            for (int index = 0; index < files.length; index++) {
                File file = files[index];
                if (file == null || !file.exists()) {
                    throw new DownloadException(DownloadException.FILE_NOT_EXIST_CODE, DownloadException.FILE_NOT_EXIST_STR);
                }
                if (!file.isFile()) {
                    continue;
                }
                String filename ;
                if (fileNameGenerator != null) {
                    filename = fileNameGenerator.generateFilename(file, index);
                } else {
                    filename = file.getName();
                }

                out.putNextEntry(new ZipEntry(filename));
                in = new FileInputStream(file);
                IOUtils.copy(in, out);
                out.closeEntry();
                IOUtils.closeQuietly(in);
            }
            IOUtils.closeQuietly(out);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DownloadException(DownloadException.COMPRESS_ERROR_CODE, DownloadException.COMPRESS_ERROR_STR);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return compressedFilePath;
    }

    /*private static final void zip(File directory, File base, ZipOutputStream out) throws IOException {
        File[] files;
        if (directory.isDirectory()) {
            files = directory.listFiles();
        } else {
            files = new File[1];
            files[0] = directory;
        }

        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, out);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
            }
        }
    }*/

    private static List<File> traverseFile(File[] files) {
        List<File> allFiles = new ArrayList<File>();
        for (File file : files) {
            if (file.isFile()) {
                allFiles.add(file);
            } else if (file.isDirectory()) {
                allFiles.addAll(traverseFile(file.listFiles()));
            }
        }
        return allFiles;
    }

    /**
     * 下载文件的名称生成器
     */
    public static abstract class FilenameGenerator {
        /**
         * 生成下载的文件显示的名称
         * @param file 当前待下载的文件
         * @return
         */
        public abstract String generateFilename(File file);

        /**
         * 生成下载的文件显示的名称，用于生成打包文件子文件名
         * @param file  当前待打包的文件
         * @param index 打包文件中子文件的索引
         * @return
         */
        public abstract String generateFilename(File file, int index);
    }

    /**
     * 文件名生成适配器，选择需要方法进行覆写，主要适用于单文件下载，此时需要覆写第一个方法
     */
    public static class FileNameGeneratorAdapter extends FilenameGenerator{

        @Override
        public String generateFilename(File file) {
            return file.getName();
        }

        @Override
        public String generateFilename(File file, int index) {
            return file.getName();
        }
    }

}
