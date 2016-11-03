package own.jadezhang.learning.apple.utils;

import own.jadezhang.learning.apple.config.Configurations;
import own.jadezhang.learning.apple.config.Range;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhang Junwei on 2016/10/30.
 */
public class IOUtil {
    static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");

    /**
     * According the key, generate a file (if not exist, then create
     * a new file).
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static File getFile(String filename) throws IOException {
        if (filename == null || filename.isEmpty())
            return null;
        String name = filename.replaceAll("/", Matcher.quoteReplacement(File.separator));
        File f = new File(Configurations.getTempPath() + File.separator + name);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    /**
     * Acquired the file.
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static File getTokenedFile(String key) throws IOException {
        if (key == null || key.isEmpty())
            return null;

        File f = new File(Configurations.getTempPath() + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();

        return f;
    }

    public static void storeToken(String key) throws IOException {
        if (key == null || key.isEmpty())
            return;

        File f = new File(Configurations.getTempPath() + File.separator + key);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            f.createNewFile();
    }

    /**
     * close the IO stream.
     *
     * @param stream
     */
    public static void close(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
        }
    }

    /**
     * 获取Range参数
     *
     * @param req
     * @return
     * @throws IOException
     */
    public static Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader(Configurations.CONTENT_RANGE_HEADER);
        Matcher m = RANGE_PATTERN.matcher(range);
        if (m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");

            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);

            return new Range(from, to, size);
        }
        throw new IOException("Illegal Access!");
    }

    /**
     * From the InputStream, write its data to the given file.
     */
    public static long streaming(InputStream in, String key, String fileName) throws IOException {
        OutputStream out = null;
        File f = getTokenedFile(key);
        try {
            out = new FileOutputStream(f);

            int read = 0;
            final byte[] bytes = new byte[Configurations.BUFFER_LENGTH];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } finally {
            close(out);
        }
        /** rename the file * fix the `renameTo` bug */
        File dst = IOUtil.getFile(fileName);
        dst.delete();
        f.renameTo(dst);

        long length = getFile(fileName).length();
        /** if `STREAM_DELETE_FINISH`, then delete it. */
        if (Configurations.isDeleteFinished()) {
            dst.delete();
        }

        return length;
    }
}
