package own.jadezhang.learning.apple.controller.upload;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import own.jadezhang.learning.apple.config.Configurations;
import own.jadezhang.learning.apple.config.Range;
import own.jadezhang.learning.apple.exception.StreamException;
import own.jadezhang.learning.apple.utils.DownloadUtil;
import own.jadezhang.learning.apple.utils.IOUtil;
import own.jadezhang.learning.apple.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang Junwei on 2016/10/30.
 */
@Controller
@RequestMapping("/file/upload")
public class FileUploadController {
    static final String FILE_NAME_FIELD = "name";
    static final String FILE_SIZE_FIELD = "size";
    static final String TOKEN_FIELD = "token";
    static final String SERVER_FIELD = "server";
    static final String SUCCESS = "success";
    static final String MESSAGE = "message";
    static final int BUFFER_LENGTH = 10240;
    static final String START_FIELD = "start";

    @ResponseBody
    @RequestMapping(value = "/getToken")
    public Map<String, Object> getToken(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = req.getParameter(FILE_NAME_FIELD);
        String size = req.getParameter(FILE_SIZE_FIELD);
        String token = TokenUtil.generateToken(name, size);
        /** TODO: save the token. */

        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put(TOKEN_FIELD, token);
        responseMap.put(SUCCESS, true);
        responseMap.put(SERVER_FIELD, Configurations.getCrossServer());
        responseMap.put(MESSAGE, "");
        return responseMap;
    }

    @ResponseBody
    @RequestMapping(value = "/streamUpload", method = RequestMethod.GET)
    public Map<String, Object> streamUploadByGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final String token = req.getParameter(TOKEN_FIELD);
        final String size = req.getParameter(FILE_SIZE_FIELD);
        final String fileName = req.getParameter(FILE_NAME_FIELD);
        Map<String, Object> responseMap = new HashMap<String, Object>();

        long start = 0;
        boolean success = true;
        String message = "";
        try {
            File f = IOUtil.getTokenedFile(token);
            start = f.length();
        } catch (FileNotFoundException fne) {
            message = "Error: " + fne.getMessage();
            success = false;
        } finally {
            if (success) {
                responseMap.put(START_FIELD, start);
            }
            responseMap.put(SUCCESS, success);
            responseMap.put(MESSAGE, message);
            responseMap.put(TOKEN_FIELD, token);
        }

        return responseMap;
    }

    @ResponseBody
    @RequestMapping(value = "/streamUpload", method = RequestMethod.POST)
    public Map<String, Object> streamUploadByPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final String token = req.getParameter(TOKEN_FIELD);
        final String fileName = req.getParameter(FILE_NAME_FIELD);
        Range range = IOUtil.parseRange(req);
        OutputStream out = null;
        InputStream content = null;

        /** TODO: validate your token. */

        Map<String, Object> responseMap = new HashMap<String, Object>();

        long start = 0;
        boolean success = true;
        String message = "";
        File savedFile = IOUtil.getTokenedFile(token);
        try {
            if (savedFile.length() != range.getFrom()) {
                /** drop this uploaded data */
                throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
            }
            IOUtils.copy(req.getInputStream(), new FileOutputStream(savedFile, true));
            start = savedFile.length();
        } catch (StreamException se) {
            success = StreamException.ERROR_FILE_RANGE_START == se.getCode();
            message = "Code: " + se.getCode();
        } catch (FileNotFoundException fne) {
            message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
            success = false;
        } catch (IOException io) {
            message = "IO Error: " + io.getMessage();
            success = false;
        } finally {
            IOUtil.close(out);
            IOUtil.close(content);

            /** rename the file */
            /*if (range.getSize() == start) {
                *//** fix the `renameTo` bug *//*
                File dst = IoUtil.getFile(fileName);
                dst.delete();
                f.renameTo(dst);
                System.out.println("TK: `" + token + "`, NE: `" + fileName + "`");

                *//** if `STREAM_DELETE_FINISH`, then delete it. *//*
            }*/

            if (success) {
                responseMap.put(START_FIELD, start);
            }
            responseMap.put(SUCCESS, success);
            responseMap.put(MESSAGE, message);
            responseMap.put(TOKEN_FIELD, token);
        }

        return responseMap;
    }

    @RequestMapping(value = "/formUpload", method = RequestMethod.POST)
    public void formUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            final PrintWriter writer = resp.getWriter();
            writer.println("ERROR: It's not Multipart form.");
            IOUtil.close(writer);
        }

        long start = 0;
        boolean success = true;
        String message = "";

        ServletFileUpload upload = new ServletFileUpload();
        InputStream in = null;
        String token = null;
        try {
            FileItemIterator iter = upload.getItemIterator(req);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                in = item.openStream();
                if (item.isFormField()) {
                    String value = Streams.asString(in);
                    if (TOKEN_FIELD.equals(name)) {
                        token = value;
                        /** TODO: validate your token. */
                    }
                    System.out.println(name + ":" + value);
                } else {
                    String fileName = item.getName();
                    start = IOUtil.streaming(in, token, fileName);
                }
            }
        } catch (FileUploadException fne) {
            success = false;
            message = "Error: " + fne.getLocalizedMessage();
        } finally {
            if (success) {
                jsonObject.put(START_FIELD, start);
            }
            jsonObject.put(SUCCESS, success);
            jsonObject.put(MESSAGE, message);
            jsonObject.put(TOKEN_FIELD, token);
            IOUtils.closeQuietly(in);
            //IOUtil.close(in);
        }
        resp.getWriter().write(jsonObject.toString());
    }

    /**
     * 根据token从临时文件中删除
     *
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteOne")
    public Map<String, Object> deleteOne(String token) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            File file = IOUtil.getTokenedFile(token);
            if (file.exists()) {
                file.delete();
            }
            responseMap.put(SUCCESS, true);
        } catch (IOException e) {
            e.printStackTrace();
            responseMap.put(SUCCESS, false);
        }
        return responseMap;
    }

    /**
     * 取消已经上传的文件
     *
     * @param excels
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteUploaded")
    public Map<String, Object> deleteUploaded(@RequestBody List<JSONObject> excels) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        try {
            for (JSONObject excel : excels) {
                File file = IOUtil.getTokenedFile(excel.getString("token"));
                if (file.exists()) {
                    file.delete();
                }
            }
            responseMap.put(SUCCESS, true);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put(SUCCESS, false);
        }
        return responseMap;
    }

    /**
     * 根据token从临时文件中在前端显示
     *
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/viewFile")
    public void viewImg(HttpServletRequest request, HttpServletResponse response, String token, String ext) {
        try {
            if (StringUtils.isBlank(ext)) {
                ext = "";
            }
            String fileRepository = Configurations.getFileRepository(token + ext);
            //先从正式文件仓库中获取文件
            File file = new File(fileRepository);
            if (!file.exists()) {
                //文件仓库中没有则从临时文件中获取
                file = IOUtil.getTokenedFile(token);
            }
            if (!file.exists()) {
                return;
            }
            FileInputStream fis = new FileInputStream(file);
            int size = fis.available(); //得到文件大小
            byte data[] = new byte[size];
            fis.read(data);  //读数据
            fis.close();
            URL u = new URL("file:///" + file.getPath());
            response.setContentType(u.openConnection().getContentType());
//            response.setContentType("image/gif"); //设置返回的文件类型
            //兼容火狐
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                response.setHeader("Content-disposition", "attachment;filename=" + new String(token.getBytes(), "ISO8859-1"));
            } else {
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(token, "UTF-8"));
            }
            OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/webUploader")
    public void webUpload(@RequestParam("file") MultipartFile uploadedFile) {
        FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
        String fileExt = StringUtils.substringAfterLast(uploadedFile.getOriginalFilename(), ".");
        try {
            File savedFile = new File(Configurations.getFileRepository(Configurations.generateRelativePath(fileExt)));
            if (!savedFile.getParentFile().exists()) {
                savedFile.getParentFile().mkdirs();
            }
            uploadedFile.transferTo(savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //DownloadUtil.download(request, response, "");
       /* File file = new File("F:\\document\\杂件\\社保名单汇总-张俊伟.xlsx");
        DownloadUtil.download(request, response, file, new DownloadUtil.FilenameGenerator() {
            @Override
            public String generateFilename(File file) {
                return CommonUtil.makeUUID()+ "." + FilenameUtils.getExtension(file.getName());
            }
        });*/

    /*    File[] files = new File[]{
                new File("D:\\chart.xls"),
                new File("D:\\学生体质健康记录卡.doc"),
                new File("D:\\十九楼平面布置图1027.pdf")};*/
        File[] files = new File[]{};
        DownloadUtil.downloadAfterCompress(request, response, files, new DownloadUtil.FilenameGenerator() {
            @Override
            public String generateFilename(File file) {
                return "打包文件（新闻)."+FilenameUtils.getExtension(file.getName());
            }

            @Override
            public String generateFilename(File file, int index) {
                return "打包子文件（"+index+"）."+FilenameUtils.getExtension(file.getName());
            }
        });
    }
}
