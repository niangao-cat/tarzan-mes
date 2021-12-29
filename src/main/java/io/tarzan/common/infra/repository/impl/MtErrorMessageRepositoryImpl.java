package io.tarzan.common.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.entity.MtErrorMessage;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.util.ReplaceUtil;
import io.tarzan.common.domain.vo.MtErrorMessageVO2;
import io.tarzan.common.infra.mapper.MtErrorMessageMapper;

/**
 * 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtErrorMessageRepositoryImpl extends BaseRepositoryImpl<MtErrorMessage>
                implements MtErrorMessageRepository {
    private final static String REDIS_ERROR_MESSAGE = "tarzan:error-message:";

    @Autowired
    private MtErrorMessageMapper mtErrorMessageMapper;

    @Autowired
    private RedisHelper redisHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtCustomDbRepository customDbRepository;


    @Override
    public String getErrorMessageWithModule(Long tenantId, String code, String module, String... args) {
        String key = REDIS_ERROR_MESSAGE + tenantId + ":" + module + ":" + MtLanguageHelper.language();
        String message = redisHelper.hshGet(key, code);

        if (StringUtils.isEmpty(message)) {
            MtErrorMessage one = new MtErrorMessage();
            one.setTenantId(tenantId);
            one.setModule(module);
            one.setMessageCode(code);
            one = mtErrorMessageMapper.selectOne(one);
            if (null != one) {
                message = one.getMessage();
            }
        }

        if (StringUtils.isEmpty(message)) {
            return code + "";
        }

        if (ArrayUtils.isNotEmpty(args)) {
            message = ReplaceUtil.replace(message, args);
        }
        return message;
    }

    @Override
    public List<String> messageLimitMessageCodeQuery(Long tenantId, String module, String message) {
        if (StringUtils.isEmpty(module)) {
            throw new MtException("MT_GENERAL_0001", self().getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                            "GENERAL", "module", "【API:messageLimitMessageCodeQuery】"));
        }
        if (StringUtils.isEmpty(message)) {
            throw new MtException("MT_GENERAL_0001", self().getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                            "GENERAL", "message", "【API:messageLimitMessageCodeQuery】"));
        }

        MtErrorMessage mtErrorMessage = new MtErrorMessage();
        mtErrorMessage.setTenantId(tenantId);
        mtErrorMessage.setModule(module);
        mtErrorMessage.setMessage(message);
        Criteria criteria = new Criteria(mtErrorMessage);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtErrorMessage.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtErrorMessage.FIELD_MODULE, Comparison.EQUAL));
        whereFields.add(new WhereField(MtErrorMessage.FIELD_MESSAGE, Comparison.LIKE));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtErrorMessage> mtErrorMessages = this.mtErrorMessageMapper.selectOptional(mtErrorMessage, criteria);
        if (CollectionUtils.isEmpty(mtErrorMessages)) {
            return Collections.emptyList();
        }

        return mtErrorMessages.stream().map(MtErrorMessage::getMessageCode).distinct().collect(toList());
    }

    @Override
    public String messageCodeLimitMessageGet(Long tenantId, String module, String messageCode) {
        if (StringUtils.isEmpty(module)) {
            throw new MtException("MT_GENERAL_0001", self().getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                            "GENERAL", "module", "【API:messageCodeLimitMessageGet】"));
        }
        if (StringUtils.isEmpty(messageCode)) {
            throw new MtException("MT_GENERAL_0001", self().getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                            "GENERAL", "messageCode", "【API:messageCodeLimitMessageGet】"));
        }

        String key = REDIS_ERROR_MESSAGE + tenantId + ":" + module + ":" + MtLanguageHelper.language();
        String message = redisHelper.hshGet(key, messageCode);
        if (StringUtils.isEmpty(message)) {
            MtErrorMessage one = new MtErrorMessage();
            one.setTenantId(tenantId);
            one.setModule(module);
            one.setMessageCode(messageCode);
            one = mtErrorMessageMapper.selectOne(one);
            if (one != null) {
                message = one.getMessage();
            }
        }

        return message;
    }

    @Override
    public void initDataToRedis() {
        List<Language> languages = LanguageHelper.languages();

        for (Language language : languages) {
            String languageCode = language.getCode();

            List<MtErrorMessage> mtErrorMessages = this.mtErrorMessageMapper.selectAllErrorMessage(languageCode);
            Map<String, List<MtErrorMessage>> map = mtErrorMessages.stream()
                            .collect(Collectors.groupingBy(t -> t.getTenantId() + "::" + t.getModule()));

            for (Map.Entry<String, List<MtErrorMessage>> entry : map.entrySet()) {
                String[] array = entry.getKey().split("::");
                String tenantId = array[0];
                String module = array[1];

                Map<String, String> messageMap = entry.getValue().stream()
                                .collect(Collectors.toMap(MtErrorMessage::getMessageCode, MtErrorMessage::getMessage));

                String key = REDIS_ERROR_MESSAGE + tenantId + ":" + module + ":" + languageCode;
                redisHelper.hshPutAll(key, messageMap);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String messageBasicPropertyUpdate(Long tenantId, MtErrorMessage dto) {
        String messageId = dto.getMessageId();

        if (StringUtils.isEmpty(messageId)) {
            if (StringUtils.isEmpty(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                "GENERAL", "module", "【API:messageBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getMessageCode())) {
                throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                "GENERAL", "messageCode", "【API:messageBasicPropertyUpdate】"));
            }

            MtErrorMessage oldMessage = new MtErrorMessage();
            oldMessage.setTenantId(tenantId);
            oldMessage.setMessageCode(dto.getMessageCode());
            oldMessage = mtErrorMessageMapper.selectOne(oldMessage);
            if (null != oldMessage) {
                throw new MtException("MT_GENERAL_0004", getErrorMessageWithModule(tenantId, "MT_GENERAL_0004",
                                "GENERAL", "messageCode", "【API:messageBasicPropertyUpdate】"));
            }

            Long userId = DetailsHelper.getUserDetails().getUserId();
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            self().insertSelective(dto);

            messageId = dto.getMessageId();

            if (dto.get_tls() == null || dto.get_tls().get("message") == null) {
                String key = REDIS_ERROR_MESSAGE + tenantId + ":" + dto.getModule() + ":" + MtLanguageHelper.language();
                redisHelper.hshPut(key, dto.getMessageCode(), dto.getMessage());
            } else {
                Map<String, String> map = dto.get_tls().get("message");
                dto.set_tls(null);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = REDIS_ERROR_MESSAGE + tenantId + ":" + dto.getModule() + ":" + entry.getKey();
                    dto.setMessage(entry.getValue());
                    redisHelper.hshPut(key, dto.getMessageCode(), dto.getMessage());
                }
            }

        } else {
            if (null != dto.getModule() && "".equals(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                "GENERAL", "module", "【API:messageBasicPropertyUpdate】"));
            }
            if (null != dto.getMessageCode() && "".equals(dto.getMessageCode())) {
                throw new MtException("MT_GENERAL_0001", getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
                                "GENERAL", "messageCode", "【API:messageBasicPropertyUpdate】"));
            }

            MtErrorMessage oldMessage = new MtErrorMessage();
            oldMessage.setTenantId(tenantId);
            oldMessage.setMessageId(messageId);
            oldMessage = mtErrorMessageMapper.selectOne(oldMessage);
            if (null == oldMessage) {
                throw new MtException("MT_GENERAL_0037", getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                "GENERAL", "messageId", "【API:messageBasicPropertyUpdate】"));
            }

            MtErrorMessage oldMessage1 = new MtErrorMessage();
            oldMessage1.setTenantId(tenantId);
            oldMessage1.setMessageCode(dto.getMessageCode());
            oldMessage1 = mtErrorMessageMapper.selectOne(oldMessage1);
            if (null != oldMessage1 && !oldMessage1.getMessageId().equals(dto.getMessageId())) {
                throw new MtException("MT_GENERAL_0004", getErrorMessageWithModule(tenantId, "MT_GENERAL_0004",
                                "GENERAL", "messageCode", "【API:messageBasicPropertyUpdate】"));
            }

            Long userId = DetailsHelper.getUserDetails().getUserId();
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            dto.setTenantId(tenantId);

            self().updateByPrimaryKeySelective(dto);

            dto.setCreatedBy(oldMessage.getCreatedBy());
            dto.setCreationDate(oldMessage.getCreationDate());
            dto.setObjectVersionNumber(oldMessage.getObjectVersionNumber());

            if (dto.get_tls() == null || dto.get_tls().get("message") == null) {
                String key = REDIS_ERROR_MESSAGE + tenantId + ":" + dto.getModule() + ":" + LanguageHelper.language();
                redisHelper.hshPut(key, dto.getMessageCode(), dto.getMessage());
            } else {
                Map<String, String> map = dto.get_tls().get("message");
                dto.set_tls(null);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = REDIS_ERROR_MESSAGE + tenantId + ":" + dto.getModule() + ":" + entry.getKey();
                    dto.setMessage(entry.getValue());
                    redisHelper.hshPut(key, dto.getMessageCode(), dto.getMessage());
                }
            }
        }
        return messageId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeMessage(Long tenantId, List<MtErrorMessageVO2> list) {
        List<String> sqlList = new ArrayList<String>();
        for (MtErrorMessageVO2 vo : list) {
            MtErrorMessage one = new MtErrorMessage();
            one.setTenantId(tenantId);
            one.setMessageId(vo.getMessageId());
            sqlList.addAll(customDbRepository.getDeleteSql(one));
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        List<Language> languages = LanguageHelper.languages();
        for (MtErrorMessageVO2 message : list) {
            for (Language language : languages) {
                String key = REDIS_ERROR_MESSAGE + tenantId + ":" + message.getModule() + ":" + language.getCode();
                redisHelper.hshDelete(key, message.getMessageCode());
            }
        }
    }
}
