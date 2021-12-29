package io.tarzan.common.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.vo.MtGenTypeVO;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtRemoteIamService;
import io.tarzan.common.infra.mapper.MtGenTypeMapper;

/**
 * 类型 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtGenTypeRepositoryImpl extends BaseRepositoryImpl<MtGenType> implements MtGenTypeRepository {
    private final static String REDIS_GEN_TYPE = "tarzan:gen-type:";
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtGenTypeMapper mtGenTypeMapper;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private MtRemoteIamService remoteIamService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public List<MtGenType> groupLimitTypeQuery(Long tenantId, MtGenTypeVO2 condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:groupLimitTypeQuery】"));
        }
        if (StringUtils.isEmpty(condition.getTypeGroup())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:groupLimitTypeQuery】"));
        }
        if (null != condition.getTypeCode()) {
            return Collections.singletonList(this.getGenType(tenantId, condition.getModule(), condition.getTypeGroup(),
                            condition.getTypeCode()));
        } else {
            return this.getGenTypes(tenantId, condition.getModule(), condition.getTypeGroup());
        }
    }

    @Override
    public List<String> typeLimitTypeGroupQuery(Long tenantId, MtGenTypeVO condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:typeLimitTypeGroupQuery】"));
        }
        if (StringUtils.isEmpty(condition.getTypeCode()) && StringUtils.isEmpty(condition.getDescription())) {
            throw new MtException("MT_GENERAL_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0005", "GENERAL",
                                            "statusCode", "description", "【API:typeLimitTypeGroupQuery】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeMapper.selectByConditionCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            return Collections.emptyList();
        }
        return mtGenTypes.stream().map(MtGenType::getTypeGroup).distinct().collect(Collectors.toList());
    }

    @Override
    public List<MtGenType> groupLimitDefaultTypeGet(Long tenantId, MtGenTypeVO2 condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:groupLimitDefaultTypeGet】"));
        }
        if (StringUtils.isEmpty(condition.getTypeGroup())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:groupLimitDefaultTypeGet】"));
        }
        List<MtGenType> mtGenTypes = this.getGenTypes(tenantId, condition.getModule(), condition.getTypeGroup());
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            return Collections.emptyList();
        }
        return mtGenTypes.stream().filter(c -> "Y".equals(c.getDefaultFlag())).collect(Collectors.toList());
    }

    @Override
    public MtGenType getGenType(Long tenantId, String module, String group, String code) {
        MtGenType mtGenType = null;

        String key = REDIS_GEN_TYPE + tenantId + ":" + module + ":" + group + ":" + MtLanguageHelper.language();
        String typeStr = redisHelper.hshGet(key, code);
        if (StringUtils.isNotEmpty(typeStr)) {
            mtGenType = redisHelper.fromJson(typeStr, MtGenType.class);
        }

        if (null == mtGenType) {
            mtGenType = new MtGenType();
            mtGenType.setTenantId(tenantId);
            mtGenType.setModule(module);
            mtGenType.setTypeGroup(group);
            mtGenType.setTypeCode(code);
            mtGenType = this.mtGenTypeMapper.selectOne(mtGenType);
        }
        return mtGenType;
    }

    @Override
    public List<MtGenType> getGenTypes(Long tenantId, String module, String group) {
        List<MtGenType> list = new ArrayList<MtGenType>();
        String key = REDIS_GEN_TYPE + tenantId + ":" + module + ":" + group + ":" + MtLanguageHelper.language();
        Map<String, String> map = redisHelper.hshGetAll(key);
        MtGenType one = null;
        for (String ever : map.values()) {
            if (StringUtils.isNotEmpty(ever)) {
                one = redisHelper.fromJson(ever, MtGenType.class);
                if (null != one) {
                    list.add(one);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream()
                            .sorted(Comparator.comparingDouble(
                                            (MtGenType c) -> c.getSequence() == null ? 0.0D : c.getSequence()))
                            .collect(Collectors.toList());
        }

        MtGenType mtGenType = new MtGenType();
        mtGenType.setTenantId(tenantId);
        mtGenType.setModule(module);
        mtGenType.setTypeGroup(group);
        return this.mtGenTypeMapper.select(mtGenType).stream()
                        .sorted(Comparator.comparingDouble(
                                        (MtGenType t) -> t.getSequence() == null ? 0.0D : t.getSequence()))
                        .collect(Collectors.toList());
    }

    @Override
    public void initDataToRedis() {
        List<Language> languages = LanguageHelper.languages();

        for (Language language : languages) {
            String languageCode = language.getCode();

            List<MtGenType> mtGenTypes = this.mtGenTypeMapper.selectAllGenTypes(languageCode);
            Map<String, List<MtGenType>> map = mtGenTypes.stream().collect(Collectors
                            .groupingBy(t -> t.getTenantId() + "::" + t.getModule() + "::" + t.getTypeGroup()));

            for (Map.Entry<String, List<MtGenType>> entry : map.entrySet()) {
                String[] array = entry.getKey().split("::");
                String tenantId = array[0];
                String module = array[1];
                String typeGroup = array[2];

                Map<String, String> genTypeMap = entry.getValue().stream()
                                .collect(Collectors.toMap(MtGenType::getTypeCode, t -> redisHelper.toJson(t)));

                String key = REDIS_GEN_TYPE + tenantId + ":" + module + ":" + typeGroup + ":" + languageCode;

                redisHelper.hshPutAll(key, genTypeMap);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String genTypeBasicPropertyUpdate(Long tenantId, MtGenType dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        String genTypeId = dto.getGenTypeId();
        dto.setTenantId(tenantId);
        dto.setLastUpdateDate(now);
        dto.setLastUpdatedBy(userId);

        if (StringUtils.isEmpty(genTypeId)) {
            if (StringUtils.isEmpty(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "module", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getTypeGroup())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "typeGroup", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getTypeCode())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "typeCode", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (null == dto.getSequence() || dto.getSequence() < 0) {
                throw new MtException("MT_GENERAL_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0052", "GENERAL", "【API:getGenTypeBasicPropertyUpdate】"));
            }
        } else {
            if (null != dto.getModule() && "".equals(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "module", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (null != dto.getTypeGroup() && "".equals(dto.getTypeGroup())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "typeGroup", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (null != dto.getTypeCode() && "".equals(dto.getTypeCode())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "typeCode", "【API:getGenTypeBasicPropertyUpdate】"));
            }
            if (null != dto.getSequence() && dto.getSequence() < 0) {
                throw new MtException("MT_GENERAL_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0052", "GENERAL", "【API:getGenTypeBasicPropertyUpdate】"));
            }
        }

        MtGenType queryGenType = new MtGenType();
        queryGenType.setTenantId(tenantId);
        queryGenType.setTypeGroup(dto.getTypeGroup());
        List<MtGenType> originGenTypeList = mtGenTypeMapper.select(queryGenType);
        if (CollectionUtils.isEmpty(originGenTypeList)) {
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            self().insertSelective(dto);

            genTypeId = dto.getGenTypeId();
        } else {
            if (StringUtils.isEmpty(genTypeId)) {
                if ("Y".equals(dto.getDefaultFlag())
                                && originGenTypeList.stream().anyMatch(g -> "Y".equals(g.getDefaultFlag()))) {
                    throw new MtException("MT_GENERAL_0049", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0049", "GENERAL"));
                }

                if (originGenTypeList.stream().anyMatch(g -> g.getTypeCode().equals(dto.getTypeCode()))) {
                    throw new MtException("MT_GENERAL_0050", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0050", "GENERAL"));
                }
                dto.setCreatedBy(userId);
                dto.setCreationDate(now);
                self().insertSelective(dto);
            } else {
                String finalGenTypeId = genTypeId;
                if ("Y".equals(dto.getDefaultFlag())
                                && originGenTypeList.stream().filter(g -> !finalGenTypeId.equals(g.getGenTypeId()))
                                                .anyMatch(g -> "Y".equals(g.getDefaultFlag()))) {
                    throw new MtException("MT_GENERAL_0049", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0049", "GENERAL"));
                }

                MtGenType originGenType = originGenTypeList.stream()
                                .filter(g -> g.getGenTypeId().equals(dto.getGenTypeId())).findAny()
                                .orElseThrow(() -> new MtException("MT_GENERAL_0037",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_GENERAL_0037", "GENERAL", "genTypeId",
                                                                "【API:getGenTypeBasicPropertyUpdate】")));
                if (!originGenType.getTypeCode().equals(dto.getTypeCode())) {
                    throw new MtException("MT_GENERAL_0050", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0050", "GENERAL"));
                }

                // 获取initialFlag为Y的原始数据(当修改时需判断)
                if ("Y".equals(originGenType.getInitialFlag())) {
                    // 校验当前角色是否可以修改initialFlag字段
                    String allowUpdateRole = profileClient.getProfileValueByOptions(tenantId,
                                    DetailsHelper.getUserDetails().getUserId(),
                                    DetailsHelper.getUserDetails().getRoleId(), "GEN_INITIALIZE_UPDATE_ROLE");


                    ResponseEntity<List<MtRoleVO>> listMemberRoles = remoteIamService.selfRoles(tenantId);
                    List<MtRoleVO> roleList =
                                    listMemberRoles == null ? new ArrayList<MtRoleVO>() : listMemberRoles.getBody();

                    List<String> roles = roleList == null ? new ArrayList<String>()
                                    : roleList.stream().map(MtRoleVO::getViewCode).collect(Collectors.toList());

                    if (StringUtils.isEmpty(allowUpdateRole) || !roles.contains(allowUpdateRole)) {
                        throw new MtException("MT_GENERAL_0007",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0007",
                                                        "GENERAL", allowUpdateRole,
                                                        "【API:getGenTypeBasicPropertyUpdate】"));
                    }
                }
                self().updateByPrimaryKeySelective(dto);
                dto.setCreatedBy(originGenType.getCreatedBy());
                dto.setCreationDate(originGenType.getCreationDate());
                dto.setObjectVersionNumber(originGenType.getObjectVersionNumber());
            }

        }

        // flush redis
        if (dto.get_tls() == null || dto.get_tls().get("description") == null) {
            String key = REDIS_GEN_TYPE + tenantId + ":" + dto.getModule() + ":" + dto.getTypeGroup() + ":"
                            + MtLanguageHelper.language();
            redisHelper.hshPut(key, dto.getTypeCode(), redisHelper.toJson(dto));
        } else {
            Map<String, String> map = dto.get_tls().get("description");
            dto.set_tls(null);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = REDIS_GEN_TYPE + tenantId + ":" + dto.getModule() + ":" + dto.getTypeGroup() + ":"
                                + entry.getKey();
                dto.setDescription(entry.getValue());
                redisHelper.hshPut(key, dto.getTypeCode(), redisHelper.toJson(dto));
            }
        }

        return genTypeId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeGenType(Long tenantId, List<MtGenType> list) {
        List<String> sqlList = new ArrayList<String>();
        // 校验获取initialFlag为Y
        if (list.stream().anyMatch(t -> "Y".equals(t.getInitialFlag()))) {
            throw new MtException("MT_GENERAL_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0008", "GENERAL", "【API:removeGenType】"));

        }
        list.forEach(dto -> {
            MtGenType one = new MtGenType();
            one.setTenantId(tenantId);
            one.setGenTypeId(dto.getGenTypeId());
            sqlList.addAll(customDbRepository.getDeleteSql(one));
        });

        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        List<Language> languages = LanguageHelper.languages();
        for (MtGenType mtGenType : list) {
            for (Language language : languages) {
                String key = REDIS_GEN_TYPE + tenantId + ":" + mtGenType.getModule() + ":" + mtGenType.getTypeGroup()
                                + ":" + language.getCode();
                redisHelper.hshDelete(key, mtGenType.getTypeCode());
            }
        }
    }

    @Override
    public List<MtGenTypeVO8> propertyLimitGenTypePropertyQuery(Long tenantId, MtGenTypeVO7 dto) {
        return mtGenTypeMapper.selectCondition(tenantId, dto);
    }

}
