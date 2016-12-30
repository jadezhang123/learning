package own.jadezhang.learning.apple.config;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import own.jadezhang.common.domain.common.OSinfo;
import own.jadezhang.common.utils.CommonUtil;
import own.jadezhang.learning.apple.utils.IDUtil;
import own.jadezhang.learning.apple.utils.IOUtil;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * read the configurations from file `config.properties`.
 */
public class Configurations {
	public static final String CONTENT_RANGE_HEADER = "content-range";
	public static final int BUFFER_LENGTH = 1024 * 1024 * 10;
	static final String CONFIG_FILE = "config"+File.separator+"stream-config.properties";
	private static Properties properties = null;

	static {
		new Configurations();
	}

	private Configurations() {
		init();
		System.out.println("[NOTICE] File Repository Path ≥≥≥ " + getTempPath());
	}

	void init() {
		try {
			ClassLoader loader = Configurations.class.getClassLoader();
			InputStream in = loader.getResourceAsStream(CONFIG_FILE);
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			System.err.println("reading `" + CONFIG_FILE + "` error!" + e);
		}
	}

	public static String getConfig(String key) {
		return getConfig(key, null);
	}

	public static String getConfig(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static int getConfig(String key, int defaultValue) {
		String val = getConfig(key);
		int setting = 0;
		try {
			setting = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			setting = defaultValue;
		}
		return setting;
	}

	/**
	 * 获取文件临时存放目录
	 * @return
	 */
	public static String getTempPath() {
		String val = getConfig("STREAM_TEMP_PATH");
        if (StringUtils.isEmpty(val)) {
            val = System.getProperty("java.io.tmpdir", File.separator +"tmp"+File.separator +"fileRepository");
        }
		return val;
	}

	/**
	 * 获取文件正式存放路径（仓库）
	 * @relativePath 相对路径
	 * @return
	 */
	public static String getFileRepository(String relativePath) {
		String val = getConfig("STREAM_FILE_REPOSITORY");
		if (val == null || val.isEmpty()) {
			val = System.getProperty("user.dir", File.separator + "usr" + File.separator
					+ "dangan-repository");
		}
		if(StringUtils.isNotBlank(relativePath)){
			val += File.separator + relativePath;
		}
		val += File.separator;
		val = FilenameUtils.normalize(val);
		if(OSinfo.isWindows() && !relativePath.contains(":")){
			val = "D:\\" + val;
		}
		return val;
	}

	public static String generateRelativePath(Date date) {
		return DateFormatUtils.format(date,"yyyy-MM-dd").replace("-",File.separator);
	}

	/**
	 * 拷贝临时文件到正式
	 * @param token
	 * @param fileType
	 * @return
	 */
	public static String copyTempToRepository(String token,String fileType){
		String relativePath = Configurations.generateRelativePath(new Date())
				+ File.separator + IDUtil.makeUUID() + "." + fileType;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			//把文件copy正式文件夹
			File fromFile = IOUtil.getTokenedFile(token);
			File toFile = new File(Configurations.getFileRepository(relativePath));
			if (!toFile.getParentFile().exists()) {
				toFile.getParentFile().mkdirs();
			}
			inputStream = new FileInputStream(fromFile);
			outputStream = new FileOutputStream(toFile);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
			//删除临时文件
			/*Path path = Paths.get(fromFile.toURI());
			boolean delete = Files.deleteIfExists(path);*/
			boolean delete = fromFile.delete();
			System.out.println(delete);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
		return relativePath;
	}

	/**
	 * 生成文件的相对文件名
	 * @param fileExt 文件后缀名
	 * @return
     */
	public static String generateRelativePath(String fileExt){
		return DateFormatUtils.format(new Date(),"yyyy-MM-dd").replace("-",File.separator) + File.separator + CommonUtil.makeUUID() + "." + fileExt;
	}

	public static String getCrossServer() {
		//String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
		return getConfig("STREAM_CROSS_SERVER");
	}
	
	public static String getCrossOrigins() {
		return getConfig("STREAM_CROSS_ORIGIN");
	}
	
	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(getConfig(key));
	}
	
	public static boolean isDeleteFinished() {
		return getBoolean("STREAM_DELETE_FINISH");
	}
	
	public static boolean isCrossed() {
		return getBoolean("STREAM_IS_CROSS");
	}
}
