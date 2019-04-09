package com.fsmflying.common.fastjson;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

public class Startup {

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();

    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        Person person=new Person();
        jsonObject.put("name",person.getName());
        return jsonObject;

    }

    @Data
    public static class Person{
        private String name;
    }
}
