package com.zing.boot.fileupload.controller;

import com.zing.boot.fileupload.common.FileUtil;
import com.zing.boot.fileupload.vo.FileSaveInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;


public class BaseUploadController extends BaseImageController {

    /**
     * @param guid     临时文件名
     * @param md5value 客户端生成md5值
     * @param chunks   分块数
     * @param chunk    分块序号
     * @param name     上传文件名
     * @param file     文件本身
     * @return
     */
    public static FileSaveInfo fileUpload(String guid,
                                          String md5value,
                                          String chunks,
                                          String chunk,
                                          String name,
                                          MultipartFile file, String uploadFolderPath) {
        String fileName;
        int temp = guid.lastIndexOf(".");
        if (temp != -1) {
            guid = guid.substring(0, temp);
        }
        try {
            int index;

            //合并文件保存的路径
            String mergePath = uploadFolderPath + File.separator + guid + "/";
            String ext = "";
            if (name.lastIndexOf(".") != -1) {
                ext = name.substring(name.lastIndexOf("."));
            }
            //判断文件是否分块
            if (chunks != null && chunk != null && !chunks.equals("1")) {
                index = Integer.parseInt(chunk);
                fileName = String.valueOf(index) + ext;
                // 将文件分块保存到临时文件夹里，便于之后的合并文件
                FileUtil.saveFile(mergePath, fileName, file);
                // 验证所有分块是否上传成功，成功的话进行合并
                FileSaveInfo saveInfo = FileUtil.uploaded(md5value, guid, chunk, chunks, uploadFolderPath, fileName, ext);
                //合并成功的话
                return saveInfo;
            } else {
                fileName = guid + ext;
                //上传文件没有分块的话就直接保存
                FileUtil.saveFile(uploadFolderPath + File.separator, fileName, file);
                FileSaveInfo saveInfo = new FileSaveInfo();
                saveInfo.setName(fileName);
                saveInfo.setFix(ext);
                saveInfo.setSize(file.getSize());
                saveInfo.setContentType(file.getContentType());
                saveInfo.setSaveName(fileName);
                saveInfo.setPath(uploadFolderPath + File.separator + fileName);
                saveInfo.setRelativePath(FileUtil.relativePath(FileUtil.getSave_path(), saveInfo.getPath()));
                saveInfo.setMd5(md5value);
                return saveInfo;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


    public static void responseIcon(HttpServletRequest request, HttpServletResponse response, String path, String contentType, InputStream defaultStream) {
        InputStream is = null;
        OutputStream os = null;
        if (path == null || path.trim().equals("")) {
            is = defaultStream;
            response.setContentType(contentType);
        } else {
            File file = new File(path);
            if (file.exists()) {
                int temp = path.lastIndexOf(".");
                String fixName = path.substring(temp + 1, path.length());
                response.setContentType(contentType != null ? contentType : "image/" + fixName);
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException e) {

                }
            } else {
                is = defaultStream;
                response.setContentType(contentType);
            }
        }
        try {
            os = response.getOutputStream();
            int count = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected void copyRange(InputStream istream, OutputStream ostream,
                             long start, long end) throws IOException {

        long skipped = 0;
        skipped = istream.skip(start);

        if (skipped < start) {
            throw new IOException("skip fail: skipped=" + Long.valueOf(skipped)
                    + ", start=" + Long.valueOf(start));
        }

        long bytesToRead = end - start + 1;

        byte buffer[] = new byte[2048];
        int len = buffer.length;
        while ((bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = istream.read(buffer);
                if (bytesToRead >= len) {
                    ostream.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    ostream.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                len = -1;
                throw e;
            }
            if (len < buffer.length)
                break;
        }

    }

    public void responseInputstream(HttpServletRequest request, HttpServletResponse response, String path, String contentType) {
        File downloadFile = new File(path); // 要下载的文件
        long fileLength = downloadFile.length();// 记录文件大小
        long pastLength = 0;// 记录已下载文件大小
        long toLength = 0;// 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
        long contentLength = 0;// 客户端请求的字节总量
        String rangeBytes = "";// 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容

        // ETag header
        // The ETag is contentLength + lastModified
        response.setHeader("ETag",
                "W/\"" + fileLength + "-" + downloadFile.lastModified() + "\"");
        // Last-Modified header
        response.setHeader("Last-Modified",
                new Date(downloadFile.lastModified()).toString());

        if (request.getHeader("Range") != null) {// 客户端请求的下载的文件块的开始字节
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//            log.info("request.getHeader(\"Range\")="
//                    + request.getHeader("Range"));
            rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
            if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {// bytes=969998336-
                rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                pastLength = Long.parseLong(rangeBytes.trim());
                toLength = fileLength - 1;
            } else {// bytes=1275856879-1275877358
                String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                String temp2 = rangeBytes.substring(
                        rangeBytes.indexOf('-') + 1, rangeBytes.length());
                // bytes=1275856879-1275877358，从第 1275856879个字节开始下载
                pastLength = Long.parseLong(temp0.trim());
                // bytes=1275856879-1275877358，到第 1275877358 个字节结束
                toLength = Long.parseLong(temp2);
            }
        } else {// 从开始进行下载
            toLength = fileLength - 1;
        }
        // 客户端请求的是1275856879-1275877358 之间的字节
        contentLength = toLength - pastLength + 1;
        if (contentLength < Integer.MAX_VALUE) {
            response.setContentLength((int) contentLength);
        } else {
            // Set the content-length as String to be able to use a long
            response.setHeader("content-length", "" + contentLength);
        }
        if (null != contentType) {
            response.setContentType(contentType);
        }

        // 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
        response.setHeader("Accept-Ranges", "bytes");
        // 必须先设置content-length再设置header
        response.addHeader("Content-Range", "bytes " + pastLength + "-"
                + toLength + "/" + fileLength);

        response.setBufferSize(2048);

        InputStream istream = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            istream = new BufferedInputStream(
                    new FileInputStream(downloadFile), 2048);
            try {
                copyRange(istream, os, pastLength, toLength);
            } catch (IOException ie) {
                /**
                 * 在写数据的时候， 对于 ClientAbortException 之类的异常，
                 * 是因为客户端取消了下载，而服务器端继续向浏览器写入数据时， 抛出这个异常，这个是正常的。
                 * 尤其是对于迅雷这种吸血的客户端软件， 明明已经有一个线程在读取 bytes=1275856879-1275877358，
                 * 如果短时间内没有读取完毕，迅雷会再启第二个、第三个。。。线程来读取相同的字节段， 直到有一个线程读取完毕，迅雷会 KILL
                 * 掉其他正在下载同一字节段的线程， 强行中止字节读出，造成服务器抛 ClientAbortException。
                 * 所以，我们忽略这种异常
                 */
                // ignore
            }
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
        } finally {
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e) {
                    // log.error(e.getMessage(), e);
                }
            }
        }
    }
}
