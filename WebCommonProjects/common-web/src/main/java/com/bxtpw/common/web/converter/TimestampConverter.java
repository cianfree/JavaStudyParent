package com.bxtpw.common.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 时间转换器
 * 
 * @author Arvin
 *
 * @time 2013-11-26 下午3:33:35
 * @version 0.1
 * @since 0.1
 */
public class TimestampConverter implements Converter<String, Timestamp> {

    @Override
    public Timestamp convert(String source) {
        if (source == null || "".equals(source.trim())) {
            return null;
        }
        SimpleDateFormat formatter = DateFormatter.getFormatter(source);
        if (null == formatter) {
            throw new RuntimeException("Not suitable data formatter for date: " + source);
        }
        try {
            return new Timestamp(formatter.parse(source).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
