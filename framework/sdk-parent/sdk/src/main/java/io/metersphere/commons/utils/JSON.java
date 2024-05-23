package io.metersphere.commons.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeFactory typeFactory = objectMapper.getTypeFactory();
    public static final int DEFAULT_MAX_STRING_LEN = Integer.MAX_VALUE;

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 自动检测所有类的全部属性
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 如果一个对象中没有任何的属性，那么在序列化的时候就会报错
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 使用BigDecimal来序列化
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 设置JSON处理字符长度限制
        objectMapper.getFactory()
                .setStreamReadConstraints(StreamReadConstraints.builder().maxStringLength(DEFAULT_MAX_STRING_LEN).build());

        // 处理时间格式
        objectMapper.registerModule(new JavaTimeModule());


    }

    public static String toJSONString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object parseObject(String content) {
        return parseObject(content, Object.class);
    }

    public static <T> T parseObject(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String content, TypeReference<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(InputStream src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List parseArray(String content) {
        return parseArray(content, Object.class);
    }

    public static <T> List<T> parseArray(String content, Class<T> valueType) {
        CollectionType javaType = typeFactory.constructCollectionType(List.class, valueType);
        try {
            return objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String content, TypeReference<T> valueType) {
        try {
            JavaType subType = typeFactory.constructType(valueType);
            CollectionType javaType = typeFactory.constructCollectionType(List.class, subType);
            return objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map parseMap(String jsonObject) {
        try {
            return objectMapper.readValue(jsonObject, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(JSONObject json) {
        Map<String, Object> map = new LinkedHashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value == JSONObject.NULL) {
                value = null;
            }
            map.put(key, value);
        }
        return map;
    }

    public static Object toList(JSONArray array) {
        Object[] list = new Object[array.length()];
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            list[i] = value;
        }
        return list;
    }
}
