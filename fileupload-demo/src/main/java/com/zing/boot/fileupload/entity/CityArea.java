package com.zing.boot.fileupload.entity;

import com.zing.boot.fileupload.entity.base.TemporalEntity;

import javax.persistence.Entity;

@Entity
public class CityArea extends TemporalEntity {

    //地区名称
    private String name;

    //行政编码
    private String code;

    private String prevCode;

    //地区类型
    private short type;

    //全称
    private String allName;

    public CityArea() {

    }

    public CityArea(String name, String code, short type, String allName) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.allName = allName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getPrevCode() {
        return prevCode;
    }

    public void setPrevCode(String prevCode) {
        this.prevCode = prevCode;
    }

    public String validateCode(String areaCode, int areaType) {
        if (areaCode.trim().length() == 2 && areaType == 1) {
            areaCode = areaCode + "0000000000";
        } else if (areaCode.trim().length() == 4 && areaType == 2) {
            areaCode = areaCode + "00000000";
        } else if (areaCode.trim().length() == 6 && areaType == 3) {
            areaCode = areaCode + "000000";
        } else if (areaCode.trim().length() == 9 && areaType == 4) {
            areaCode = areaCode + "000";
        }
        return areaCode;
    }

    //转化为简化区域代码
    public static String parseSimpleCode(String code) {
        //验证字符串格式及处理前置零
        code = String.valueOf(Long.parseLong(code));
        //字符串末尾零的个数
        int zeroCount = 0;
        for (int x = code.length() - 1; x >= 0; x--) {
            char c = code.charAt(x);
            if (c == '0') {
                zeroCount++;
            } else {
                break;
            }
        }
        //字符串长度
        int length = code.length();
        if (length == 4) {//四位编码
            code = zeroCount >= 2 ? code.substring(0, 2) : code;
        } else if (length == 6) {//六位编码
            code = zeroCount >= 4 ? code.substring(0, 2) : zeroCount >= 2 ? code.substring(0, 4) : code;
        } else if (length == 9) {//九位编码
            code = zeroCount >= 7 ? code.substring(0, 2) : zeroCount >= 5 ? code.substring(0, 4) : zeroCount >= 3 ? code.substring(0, 6) : code;
        } else if (length == 12) {//十二位编码
            code = zeroCount >= 10 ? code.substring(0, 2) : zeroCount >= 8 ? code.substring(0, 4) : zeroCount >= 6 ? code.substring(0, 6) : zeroCount >= 3 ? code.substring(0, 9) : code;
        }
        return code;
    }

    //转化为十二位行政编码
    public static String parseLongCode(String code) {
        long num = Long.parseLong(code);
        if (num == 0) {
            return "0";
        } else {
            int length = code.length();
            if (length == 2 || length == 4 || length == 6 || length == 9 || length == 12) {
                //补零
                StringBuffer zeroBuffer = new StringBuffer();
                zeroBuffer.append(code);
                for (int x = 0; x < 12 - length; x++) {
                    zeroBuffer.append("0");
                }
                code = zeroBuffer.toString();
            } else {
                throw new RuntimeException("行政编码格式异常,行政编码格式不正确");
            }
        }
        return code;
    }
}
