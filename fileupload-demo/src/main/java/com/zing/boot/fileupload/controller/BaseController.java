package com.zing.boot.fileupload.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    public List<Map<String, Object>> parseResultToMapList(List<Object[]> objects, String... mapNames) {
        if (objects == null) {
            return null;
        }
        List<Map<String, Object>> resList = new ArrayList<>();
        for (Object[] objs : objects) {
            int objsize = objs.length;
            Map<String, Object> resMap = new HashMap<>();
            for (int x = 0; x < mapNames.length; x++) {
                if (x <= objsize) {
                    resMap.put(mapNames[x], objs[x]);
                }
            }
            resList.add(resMap);
        }
        return resList;
    }
}
