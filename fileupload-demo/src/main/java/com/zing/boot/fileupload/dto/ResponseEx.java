package com.zing.boot.fileupload.dto;

import com.zing.boot.fileupload.config.ErrorCode;
import com.zing.boot.fileupload.config.SuccessCode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseEx {
    private String code;
    private String message;
    private String type;
    private Object data;

    public ResponseEx() {
        this.code = SuccessCode.SUCCESS + "";
        this.message = "处理成功";
        this.data = new HashMap<>();
        this.type = "";
    }

    public ResponseEx(Page page) {
        this.code = SuccessCode.SUCCESS + "";
        this.message = "处理成功";
        this.data = new ResponsePageList(page);
        this.type = "";
    }

    public ResponseEx(String code, String message, String type, Object data) {
        this.code = code;
        this.message = message;
        this.type = type;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseEx error(String message) {
        this.setCode(ErrorCode.ERROR + "");
        this.setMessage(message);
        this.data = new HashMap<>();
        this.type = "";
        return this;
    }

    public ResponseEx error(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
        this.data = new HashMap<>();
        this.type = "";
        return this;
    }

    public ResponseEx error(int code, String message) {
        this.setCode(code + "");
        this.setMessage(message);
        this.data = new HashMap<>();
        this.type = "";
        return this;
    }

    public ResponseEx success(String message) {
        this.setCode(SuccessCode.SUCCESS + "");
        this.setMessage(message);
        this.data = new HashMap<>();
        this.type = "";
        return this;
    }

    public ResponseEx data(Object data) {
        this.data = data;
        this.type = "";
        this.setMessage("处理成功");
        this.setCode(SuccessCode.SUCCESS + "");
        return this;
    }

    public static void main(String[] args) {
        String str = "[{\"knowcateId\":1,\"level\":1},{\"knowcateId\":2,\"level\":2},{\"knowcateId\":3,\"level\":3}]";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> res = objectMapper.readValue(str, new TypeReference<List<Map<String, Object>>>() {
            });
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
