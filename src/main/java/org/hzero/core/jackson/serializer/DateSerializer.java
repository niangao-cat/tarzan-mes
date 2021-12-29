package org.hzero.core.jackson.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.jackson.JacksonConstant;
import org.hzero.core.jackson.annotation.IgnoreTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 根据用户的时区序列化时间
 *
 * @author zijin.liang
 */
public class DateSerializer extends JsonSerializer<Date> implements ContextualSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);
    private boolean ignoreTimeZone = false;
    private String dateFormat = JacksonConstant.DEFAULT_DATE_FORMAT;

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(dateFormat);
        try {
            if (ignoreTimeZone) {
                jsonGenerator.writeString(dateFormatGmt.format(date));
            } else {
                CustomUserDetails details = DetailsHelper.getUserDetails();
                if (details != null && details.getTimeZone() != null) {
                    dateFormatGmt.setTimeZone(TimeZone.getTimeZone(details.getTimeZone()));
                }
                jsonGenerator.writeString(dateFormatGmt.format(date));
            }
        } catch (Exception e) {
            LOGGER.warn("date format error : {}", e);
            jsonGenerator.writeNull();
        }
    }


    private DateSerializer setIgnoreTimeZone(boolean ignoreTimeZone) {
        this.ignoreTimeZone = ignoreTimeZone;
        return this;
    }

    private DateSerializer setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        DateSerializer dateSerializer = new DateSerializer();
        if (null != property) {
            ignoreTimeZone = property.getMember().hasAnnotation(IgnoreTimeZone.class);
            dateSerializer.setIgnoreTimeZone(ignoreTimeZone);

            if (property.getMember().hasAnnotation(JsonFormat.class)) {
                JsonFormat jsonFormat = property.getMember().getAnnotation(JsonFormat.class);
                dateFormat = StringUtils.isEmpty(jsonFormat.pattern()) ? JacksonConstant.DEFAULT_DATE_FORMAT
                                : jsonFormat.pattern();
                dateSerializer.setDateFormat(dateFormat);
            }
        }
        return dateSerializer;
    }
}
