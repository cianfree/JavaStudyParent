package com.bxtpw.common.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换器
 * 
 * @author 夏集球
 * 
 * @time 2015年5月7日 上午11:38:23
 * @version 0.1
 * @since 0.1
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (source == null || "".equals(source.trim())) {
            return null;
        }
        SimpleDateFormat formatter = DateFormatter.getFormatter(source);
        if (null == formatter) {
            throw new RuntimeException("Not suitable data formatter for date: " + source);
        }
        try {
            return formatter.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
