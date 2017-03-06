package com.github.search.simple.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Json 工具类
 *
 * @author Arvin
 * @time 2017/1/9 21:16
 */
public class JsonUtils {

    /** 日志 */
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);

    /** Default Object Mapper */
    private static ObjectMapper defaultMapper = new ObjectMapper();

    /** 忽略未知字段的Mapper */
    private static ObjectMapper ignoreUnknownFieldMapper = new ObjectMapper();

    /** 初始化 */
    static {
        // 忽略未知字段
        ignoreUnknownFieldMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 使用默认的Mapper将指定对象转换成Json
     *
     * @param obj 对象
     */
    public static String toJson(Object obj) {
        return toJson(obj, defaultMapper);
    }

    /**
     * 将指定对象转换成Json
     *
     * @param obj    对象
     * @param mapper mapper
     */
    public static String toJson(Object obj, ObjectMapper mapper) {
        try {
            String json;
            if (obj == null) {
                json = null;
            } else {
                json = mapper.writeValueAsString(obj);
            }
            return json;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * 将Json转换成对象.
     *
     * @param json         json
     * @param requiredType 返回类型
     */
    public static <T> T toObject(String json, Class<T> requiredType) {
        return toObject(json, requiredType, true);
    }

    /**
     * 将Json转换成对象.
     *
     * @param json json字符串
     * @param requiredType 结果类型
     * @param ignoreUnknownField 是否忽略不存在的字段?
     * @return
     */
    public static <T> T toObject(String json, Class<T> requiredType, boolean ignoreUnknownField) {
        try {
            if (json == null || json.length() == 0) {
                return null;
            } else {
                if (ignoreUnknownField) {
                    return ignoreUnknownFieldMapper.readValue(json, requiredType);
                } else {
                    return defaultMapper.readValue(json, requiredType);
                }
            }
        } catch (Exception e) {
            logger.warn("message:" + e.getMessage() + " json:" + json);
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
