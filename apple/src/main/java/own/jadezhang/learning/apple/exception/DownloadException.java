package own.jadezhang.learning.apple.exception;

/**
 * Created by Zhang Junwei on 2016/11/3.
 */
public class DownloadException extends Exception {

    public static final String DOWNLOAD_ERROR_CODE = "100";
    public static final String DOWNLOAD_ERROR_STR = "文件下载出错！";

    public static final String FILE_NOT_EXIST_CODE = "101";
    public static final String FILE_NOT_EXIST_STR = "文件不存在！";

    public static final String COMPRESS_ERROR_CODE = "102";
    public static final String COMPRESS_ERROR_STR = "文件打包出错！";

    private String code;

    public DownloadException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
