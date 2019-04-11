package com.fsmflying.common.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

//import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * @author fangming
 * @date 2019-04-11 17:56:01
 * @version 1.0.0
 */
@Data
public class CsdnUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * (long)用户id,默认值为None
     */
    private Long id;

    /**
     * (String)登陆用户名，长度128,默认值为None
     */
    private String username;

    /**
     * (String)登陆密码，长度128,默认值为None
     */
    private String password;

    /**
     * (String)用户昵称,默认值为None
     */
    private String nickname;

    /**
     * (String)注册邮箱，长度128,默认值为None
     */
    private String email;

    /**
     * (long)地区编号,默认值为None
     */
    private String regionId;

    /**
     * (int)性别(0:未知，默认值)(1:男)(2:女),默认值为0
     */
    private Integer sex;

    /**
     * (Date)出生日期,默认值为None
     */
    private Date birthDate;

    /**
     * (String)密码保护问题，长度128,默认值为None
     */
    private String passwordQuestion;

    /**
     * (String)密码保护答案，长度128,默认值为None
     */
    private String passwordAnswer;

    /**
     * (int)账号状态(0:账号正常)(1:账号已经被禁用，即账号不能再用),默认值为0
     */
    private Integer status;

    /**
     * (Date)账号开始锁定时间,默认值为None
     */
    private Date disableStartTime;

    /**
     * (int)账号锁定时长,默认值为0
     */
    private Integer disableMinutes;

    /**
     * (Date)创建时间,默认值为CURRENT_TIMESTAMP
     */
    private Date createTime;

    /**
     * (int)是否已经被删除，默认值为0，表示逻辑上未被删除，即逻辑上存在，当值为1时，表示账号已经被删除，即已经被注销,默认值为0
     */
    private Integer deleted;

    /**
     * 转化为json对象
     */
    @SuppressWarnings("all")
    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        jsonObject.put("nickname",nickname);
        jsonObject.put("email",email);
        jsonObject.put("regionId",regionId);
        jsonObject.put("sex",sex);
        jsonObject.put("birthDate",birthDate);
        jsonObject.put("passwordQuestion",passwordQuestion);
        jsonObject.put("passwordAnswer",passwordAnswer);
        jsonObject.put("status",status);
        jsonObject.put("disableStartTime",disableStartTime);
        jsonObject.put("disableMinutes",disableMinutes);
        jsonObject.put("createTime",createTime);
        jsonObject.put("deleted",deleted);
        return jsonObject;
    }

    /**
     * 解析jsonObject为[CsdnUser]对象
     */
    public static CsdnUser parse(JSONObject jsonObject) {
        CsdnUser target = new CsdnUser();
        if (jsonObject.containsKey("id")) {
            target.setId(jsonObject.getLong("id"));
        }
        if (jsonObject.containsKey("username")) {
            target.setUsername(jsonObject.getString("username"));
        }
        if (jsonObject.containsKey("password")) {
            target.setPassword(jsonObject.getString("password"));
        }
        if (jsonObject.containsKey("nickname")) {
            target.setNickname(jsonObject.getString("nickname"));
        }
        if (jsonObject.containsKey("email")) {
            target.setEmail(jsonObject.getString("email"));
        }
        if (jsonObject.containsKey("regionId")) {
            target.setRegionId(jsonObject.getString("regionId"));
        }
        if (jsonObject.containsKey("sex")) {
            target.setSex(jsonObject.getInteger("sex"));
        }
        if (jsonObject.containsKey("birthDate")) {
            target.setBirthDate(jsonObject.getDate("birthDate"));
        }
        if (jsonObject.containsKey("passwordQuestion")) {
            target.setPasswordQuestion(jsonObject.getString("passwordQuestion"));
        }
        if (jsonObject.containsKey("passwordAnswer")) {
            target.setPasswordAnswer(jsonObject.getString("passwordAnswer"));
        }
        if (jsonObject.containsKey("status")) {
            target.setStatus(jsonObject.getInteger("status"));
        }
        if (jsonObject.containsKey("disableStartTime")) {
            target.setDisableStartTime(jsonObject.getDate("disableStartTime"));
        }
        if (jsonObject.containsKey("disableMinutes")) {
            target.setDisableMinutes(jsonObject.getInteger("disableMinutes"));
        }
        if (jsonObject.containsKey("createTime")) {
            target.setCreateTime(jsonObject.getDate("createTime"));
        }
        if (jsonObject.containsKey("deleted")) {
            target.setDeleted(jsonObject.getInteger("deleted"));
        }
        return target;
    }

    /**
     * 解析json为[CsdnUser]对象
     */
    public static CsdnUser parse(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        return parse(jsonObject);
    }

    /**
     * 解析json为[CsdnUser]列表
     */
    public static List<CsdnUser> parseList(String json) {
        List<CsdnUser> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(parse(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    /**
     * 转化为Map对象
     */
    @SuppressWarnings("all")
    public Map<String,Object> toMapObject(){
        Map<String,Object> mapObject = new HashMap<>();
        mapObject.put("id",id);
        mapObject.put("username",username);
        mapObject.put("password",password);
        mapObject.put("nickname",nickname);
        mapObject.put("email",email);
        mapObject.put("regionId",regionId);
        mapObject.put("sex",sex);
        mapObject.put("birthDate",birthDate);
        mapObject.put("passwordQuestion",passwordQuestion);
        mapObject.put("passwordAnswer",passwordAnswer);
        mapObject.put("status",status);
        mapObject.put("disableStartTime",disableStartTime);
        mapObject.put("disableMinutes",disableMinutes);
        mapObject.put("createTime",createTime);
        mapObject.put("deleted",deleted);
        return mapObject;
    }
}