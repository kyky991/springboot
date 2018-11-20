package com.zing.boot.fileupload.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class BaseImageController extends BaseController {
    public static void responseIcon(HttpServletRequest request, HttpServletResponse response, String path, String contentType) {
        InputStream is = null;
        OutputStream os = null;
        if (path == null || path.trim().equals("")) {
            is = getDefaultIconStream();
            response.setContentType("image/png");
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
                is = getDefaultIconStream();
                response.setContentType("image/png");
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

    public static InputStream getDefaultIconStream() {
        return BaseImageController.class.getResourceAsStream("/static/toux.png");
    }

    public static String getIconPath(HttpServletRequest request, String savefileName) {
        try {
            return getItemPath(request) + "/user/image?name=" + savefileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIconPath(HttpServletRequest request) {
        try {
            return getItemPath(request) + "/user/image";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getIconPath() {
        return "/user/image?id=";
    }

    public static String getItemPath(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host != null) {
            return request.getScheme() + "://" + host + request.getContextPath();
        }
        String ip = request.getLocalAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "localhost";
        }
        String port = ":" + request.getLocalPort();
        if (port.equals("80")) {
            port = "";
        }
        return request.getScheme() + "://" + ip + port + request.getContextPath();
    }

    public void download(HttpServletRequest req, HttpServletResponse resp, InputStream in, String filename) {
        OutputStream os = null;
        FileInputStream is = null;
        BufferedInputStream bis = null;
        try {
            if (in != null) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = in.available();

                // get file content
                bis = new BufferedInputStream(in);

                // tell the client to allow accept-ranges
                resp.reset();
                resp.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = req.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    resp.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) {  // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p;  // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                resp.setHeader("Content-Length", new Long(contentLength).toString());

                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-")
                            .append(fileLength - 1).append("/")
                            .append(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                }

                resp.setContentType("application/octet-stream");
                if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    filename = URLEncoder.encode(filename, "UTF-8");
                } else {
                    filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
                }
                resp.addHeader("Content-Disposition", "attachment;filename=" + filename);

                OutputStream out = resp.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            } else {
//                if (logger.isDebugEnabled()) {
//                    logger.debug("Error: file " + path + " not found.");
//                }
            }
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
//            logger.error(e.getMessage());
        }
    }

    public void download(HttpServletRequest req, HttpServletResponse resp, String path, String fileName) {
        OutputStream os = null;
        FileInputStream is = null;
        BufferedInputStream bis = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream ins = new FileInputStream(file);
                bis = new BufferedInputStream(ins);

                // tell the client to allow accept-ranges
                resp.reset();
                resp.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = req.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    resp.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) {  // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p;  // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                resp.setHeader("Content-Length", new Long(contentLength).toString());
                resp.setHeader("Yc-Length", new Long(contentLength).toString());
                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-")
                            .append(fileLength - 1).append("/")
                            .append(fileLength).toString();
                    resp.setHeader("Content-Range", contentRange);
                }

                //String fileName = file.getName();
                resp.setContentType("application/octet-stream");
                if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }
                resp.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                resp.setHeader("Yc-filename", fileName);

                OutputStream out = resp.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            } else {
//                if (logger.isDebugEnabled()) {
//                    logger.debug("Error: file " + path + " not found.");
//                }
            }
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
//            logger.error(e.getMessage());
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
