package own.jadezhang.learning.apple.utils;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
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

    private static final ArchiveStreamFactory archiveStreamFactory = null;

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
            String compressedFilePath = compressFiles(files);
            File compressedFile = new File(compressedFilePath);

            download(request, response, compressedFile);

            compressedFile.delete();
        } catch (DownloadException e) {
            responseOnError(response, e.getMessage());
        }
    }

    public static void downloadAfterCompress(HttpServletRequest request, HttpServletResponse response, File[] files, FilenameGenerator fileNameGenerator) {
        try {

            String compressedFilePath = compressFiles(files);

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

    private static String compressFiles(File[] files) throws DownloadException {
        String compressedFilePath = Configurations.getTempPath() + File.separator + System.currentTimeMillis() + "-temp.zip";
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(compressedFilePath));
            List<File> allFiles = traverseFile(files);
            for (File file : allFiles) {
                if (file == null || !file.exists()) {
                    throw new DownloadException(DownloadException.FILE_NOT_EXIST_CODE, DownloadException.FILE_NOT_EXIST_STR);
                }
                if (!file.isFile()) {
                    continue;
                }
                out.putNextEntry(new ZipEntry(file.getName()));
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
         * 生成下载的文件在前端显示的名称
         *
         * @param file 当前待下载的文件
         * @return
         */
        public abstract String generateFilename(File file);
    }
}
