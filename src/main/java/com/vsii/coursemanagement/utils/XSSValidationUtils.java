package com.vsii.coursemanagement.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONException;
import org.cloudinary.json.JSONObject;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class XSSValidationUtils {

    public final Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.\\/?\\s]*$", Pattern.CASE_INSENSITIVE);


    public static boolean isValidURL(String uri, List<String> skipWords) {
        AtomicBoolean flag= new AtomicBoolean(false);
         String[]  urls=uri.split("\\/");

        Arrays.stream(urls).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
            String val=String.valueOf(url);
            System.out.println("value:"+val);
           // if(skipWords.stream().anyMatch(val::equalsIgnoreCase)){
                if(skipWords.stream().anyMatch(p->val.toLowerCase().contains(p.toLowerCase()))){
                System.out.println("bad char found!!!!!");
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(val);
            if (!matcher.matches()) {
                System.out.println("Invalid char found!!!!!");
                flag.set(true);
            }else{
                System.out.println("valid char found: "+val);
            }
        });
        return !flag.get();
    }

    public static boolean isValidRequestParam(String param, List<String> skipWords) {
        AtomicBoolean flag= new AtomicBoolean(false);
        String[]  paramList=param.split("&");

        Arrays.stream(paramList).filter(e->!StringUtils.isEmpty(e)).forEach(url->{
            String val=String.valueOf(url);
            System.out.println("value:"+val);
            if(skipWords.stream().anyMatch(val::equalsIgnoreCase)){
                System.out.println("bad char found!!!!!");
                flag.set(true);
            }
            Matcher matcher = pattern.matcher(val);
            if (!matcher.matches()) {
                System.out.println("Invalid char found!!!!!");
                flag.set(true);
            }else{
                System.out.println("valid char found: "+val);
            }
        });
        return !flag.get();
    }


    public static boolean isValidURLPattern(String uri, List<String> skipWords) {
        AtomicBoolean flag= new AtomicBoolean(false);
        String[]  urls=uri.split("\\/");

        try {
            Arrays.stream(urls).filter(e -> !StringUtils.isEmpty(e)).forEach(url -> {
                String val = String.valueOf(url);
                Map<String, Object> mapping = jsonToMap(new JSONObject(val));
                System.out.println("Map; " + mapping);
                mapping.forEach((key, value) -> {
                    //   System.out.println("key  "+key+"  value:"+value);
                    if (skipWords.stream().anyMatch(String.valueOf(value)::equalsIgnoreCase)) {
                        System.out.println("bad char found!!!!!");
                        flag.set(true);
                    }
                    Matcher matcher = pattern.matcher(String.valueOf(value));
                    if (!matcher.matches()) {
                        System.out.println(key + "  : Invalid char found!!!!!");
                        flag.set(true);
                    } else {
                        System.out.println("valid char found: " + String.valueOf(value));
                    }
                });

            });
        }catch(Exception ex){
            flag.set(true);
        }
        return !flag.get();
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json,retMap);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object, Map<String, Object> map) throws JSONException {


        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

         //   System.out.println("key  "+key+"  value:"+ value);

            if(value instanceof JSONArray) {
                value = toList(key,(JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value,map);
            }else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static List<Object> toList(String key,JSONArray array,Map<String, Object> map ) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList(key,(JSONArray) value,map);
            }else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value,map);
            }else{
                String mapValue=String.valueOf(value);
                if(map.containsKey(key)){
                    mapValue+=","+String.valueOf(map.get(key));
                }
                map.put(key, mapValue);
            }
            list.add(value);
        }
        return list;
    }


    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
