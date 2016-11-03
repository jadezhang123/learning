package own.jadezhang.learning.apple.utils;

import org.apache.commons.io.IOUtils;
import own.jadezhang.learning.apple.config.Configurations;
import own.jadezhang.learning.apple.exception.DownloadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
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
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath) throws IOException {
        File file = new File(filePath);
        download(request, response, file);
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        if (file == null || !file.exists()) {
            responseOnError(response, DownloadException.FILE_NOT_EXIST_STR);
            return;
        }
        setResponseHeader(request, response, file, file.getName());
        doDownload(file, response.getOutputStream());
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file, FilenameGenerator fileNameGenerator)throws IOException{
        if (file == null || !file.exists()) {
            responseOnError(response, DownloadException.FILE_NOT_EXIST_STR);
            return;
        }
        String fileName =fileNameGenerator !=null ? fileNameGenerator.generateFilename(file) : file.getName();
        setResponseHeader(request, response, file, fileName);
        doDownload(file, response.getOutputStream());
    }

    /**
     * @param request
     * @param response
     * @param files
     */
    public static void downloadAfterCompress(HttpServletRequest request, HttpServletResponse response, File[] files) throws IOException {
        String compressedFilePath = null;
        try {
            compressedFilePath = compressFiles(files);
        }catch (DownloadException e) {
            responseOnError(response, e.getMessage());
            return;
        }
        File compressedFile = new File(compressedFilePath);

        download(request, response, compressedFile);

        compressedFile.delete();
    }

    public static void downloadAfterCompress(HttpServletRequest request, HttpServletResponse response, File[] files, FilenameGenerator fileNameGenerator) throws IOException {
        String compressedFilePath = null;
        try {
            compressedFilePath = compressFiles(files);
        } catch (DownloadException e) {
            responseOnError(response, e.getMessage());
            return;
        }
        File compressedFile = new File(compressedFilePath);

        String filename;
        if (fileNameGenerator != null) {
            filename = fileNameGenerator.generateFilename(compressedFile);
        }else {
            filename = compressedFile.getName();
        }

        setResponseHeader(request, response, compressedFile, filename);
        download(request, response, compressedFile, fileNameGenerator);
        compressedFile.delete();
    }

    private static void responseOnError(HttpServletResponse response, String errorMsg) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        OutputStream out = response.getOutputStream();
        out.write(("<script>alert('"+errorMsg+"');window.close();</script>").getBytes("utf-8"));
        IOUtils.closeQuietly(out);
    }

    private static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, File file, String fileName) throws IOException {
        response.setContentType(Files.probeContentType(file.toPath()));
        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        } else {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        }
    }

    private static void doDownload(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        IOUtils.copy(in, out);
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);
    }

    private static String compressFiles(File[] files) throws DownloadException {
        String compressedFilePath = Configurations.getTempPath() + File.separator + System.currentTimeMillis() + "-temp.zip";
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(compressedFilePath));
            for (File file : files) {
                if (file == null || !file.exists()) {
                    throw new DownloadException(DownloadException.FILE_NOT_EXIST_CODE, DownloadException.FILE_NOT_EXIST_STR);
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
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            throw new DownloadException(DownloadException.COMPRESS_ERROR_CODE, DownloadException.COMPRESS_ERROR_STR);
        }
        return compressedFilePath;
    }

    /**
     * 下载文件的名称生成器
     */
    public static abstract class FilenameGenerator{
        /**
         * 生成下载的文件在前端显示的名称
         * @param file 当前待下载的文件
         * @return
         */
        public abstract String generateFilename(File file);
    }
}
