package com.fsmflying.common.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Data
public class Person {
    private String name;
    private Date birthdate;
    private Long id;
    @JSONField(serialize = false)
    private String password;

    List<Person> children;

    /**
     * 转化为Json对象
     * @return Json对象
     */
    private JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("name",name);
        jsonObject.put("name",name);
        jsonObject.put("name",name);
        jsonObject.put("name",name);
        return jsonObject;
    }

    public Map<String,Object> toMapObject(){
        Map<String,Object> mapObject = new HashMap<>();
        return mapObject;
    }

}
