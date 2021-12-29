package org.hzero.core.jackson.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
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
 * 根据用户的时区反序列化时间
 *
 * @author zijin.liang
 */
public class DateDeserializer extends JsonDeserializer<Date> implements ContextualDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateDeserializer.class);
    private boolean ignoreTimeZone = false;
    private String dateFormat = JacksonConstant.DEFAULT_DATE_FORMAT;

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat(dateFormat);
            if (ignoreTimeZone) {
                return dateFormatGmt.parse(jsonParser.getValueAsString());
            }
            CustomUserDetails details = DetailsHelper.getUserDetails();
            if (details != null && details.getTimeZone() != null) {
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone(details.getTimeZone()));
            }
            return dateFormatGmt.parse(jsonParser.getValueAsString());
        } catch (Exception e) {
            LOGGER.warn("date format error : {}", e);
            return null;
        }
    }

    private DateDeserializer setIgnoreTimeZone(boolean ignoreTimeZone) {
        this.ignoreTimeZone = ignoreTimeZone;
        return this;
    }

    private DateDeserializer setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        DateDeserializer dateDeserializer = new DateDeserializer();
        dateDeserializer.setIgnoreTimeZone(property.getMember().hasAnnotation(IgnoreTimeZone.class));

        if (property.getMember().hasAnnotation(JsonFormat.class)) {
            JsonFormat jsonFormat = property.getMember().getAnnotation(JsonFormat.class);
            dateFormat = StringUtils.isEmpty(jsonFormat.pattern()) ? JacksonConstant.DEFAULT_DATE_FORMAT
                            : jsonFormat.pattern();
            dateDeserializer.setDateFormat(dateFormat);
        }
        return dateDeserializer;
    }

}
