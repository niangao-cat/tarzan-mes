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
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtLanguageHelper;
import io.tarzan.common.domain.vo.MtGenStatusVO;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenStatusVO4;
import io.tarzan.common.domain.vo.MtGenStatusVO5;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtRemoteIamService;
import io.tarzan.common.infra.mapper.MtGenStatusMapper;

/**
 * 状态 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtGenStatusRepositoryImpl extends BaseRepositoryImpl<MtGenStatus> implements MtGenStatusRepository {
    private final static String REDIS_GEN_STATUS = "tarzan:gen-status:";
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtGenStatusMapper mtGenStatusMapper;
    @Autowired
    private MtRemoteIamService remoteIamService;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public List<MtGenStatus> groupLimitStatusQuery(Long tenantId, MtGenStatusVO2 condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:groupLimitStatusQuery】"));
        }
        if (StringUtils.isEmpty(condition.getStatusGroup())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:groupLimitStatusQuery】"));
        }
        if (null != condition.getStatusCode()) {
            return Collections.singletonList(getGenStatus(tenantId, condition.getModule(), condition.getStatusGroup(),
                            condition.getStatusCode()));
        } else {
            return getGenStatuz(tenantId, condition.getModule(), condition.getStatusGroup());
        }
    }

    @Override
    public List<String> statusLimitStatusGroupQuery(Long tenantId, MtGenStatusVO condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:statusLimitStatusGroupQuery】"));
        }
        if (StringUtils.isEmpty(condition.getStatusCode()) && StringUtils.isEmpty(condition.getDescription())) {
            throw new MtException("MT_GENERAL_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0005", "GENERAL",
                                            "statusCode", "description", "【API:statusLimitStatusGroupQuery】"));
        }

        List<MtGenStatus> mtGenStatuz = this.mtGenStatusMapper.selectByConditionCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(mtGenStatuz)) {
            return Collections.emptyList();
        }
        return mtGenStatuz.stream().map(MtGenStatus::getStatusGroup).distinct().collect(Collectors.toList());
    }

    @Override
    public List<MtGenStatus> groupLimitDefaultStatusGet(Long tenantId, MtGenStatusVO2 condition) {
        if (StringUtils.isEmpty(condition.getModule())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "module", "【API:groupLimitDefaultStatusGet】"));
        }
        if (StringUtils.isEmpty(condition.getStatusGroup())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:groupLimitDefaultStatusGet】"));
        }
        List<MtGenStatus> mtGenStatusz = this.getGenStatuz(tenantId, condition.getModule(), condition.getStatusGroup());
        if (CollectionUtils.isEmpty(mtGenStatusz)) {
            return Collections.emptyList();
        }
        return mtGenStatusz.stream().filter(c -> "Y".equals(c.getDefaultFlag())).collect(Collectors.toList());
    }

    @Override
    public MtGenStatus getGenStatus(Long tenantId, String module, String group, String code) {
        MtGenStatus mtGenStatus = null;

        String key = REDIS_GEN_STATUS + tenantId + ":" + module + ":" + group + ":" + MtLanguageHelper.language();
        String statusStr = redisHelper.hshGet(key, code);
        if (StringUtils.isNotEmpty(statusStr)) {
            mtGenStatus = redisHelper.fromJson(statusStr, MtGenStatus.class);
        }
        if (null == mtGenStatus) {
            mtGenStatus = new MtGenStatus();
            mtGenStatus.setTenantId(tenantId);
            mtGenStatus.setModule(module);
            mtGenStatus.setStatusGroup(group);
            mtGenStatus.setStatusCode(code);
            mtGenStatus = this.mtGenStatusMapper.selectOne(mtGenStatus);
        }
        return mtGenStatus;
    }

    @Override
    public List<MtGenStatus> getGenStatuz(Long tenantId, String module, String group) {
        List<MtGenStatus> list = new ArrayList<MtGenStatus>();
        String key = REDIS_GEN_STATUS + tenantId + ":" + module + ":" + group + ":" + MtLanguageHelper.language();
        Map<String, String> map = redisHelper.hshGetAll(key);
        MtGenStatus one = null;
        for (String ever : map.values()) {
            if (StringUtils.isNotEmpty(ever)) {
                one = redisHelper.fromJson(ever, MtGenStatus.class);
                if (null != one) {
                    list.add(one);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream()
                            .sorted(Comparator.comparingDouble(
                                            (MtGenStatus c) -> c.getSequence() == null ? 0.0D : c.getSequence()))
                            .collect(Collectors.toList());
        }

        MtGenStatus mtGenStatus = new MtGenStatus();
        mtGenStatus.setTenantId(tenantId);
        mtGenStatus.setModule(module);
        mtGenStatus.setStatusGroup(group);
        return this.mtGenStatusMapper.select(mtGenStatus).stream()
                        .sorted(Comparator.comparingDouble(
                                        (MtGenStatus c) -> c.getSequence() == null ? 0.0D : c.getSequence()))
                        .collect(Collectors.toList());
    }

    @Override
    public void initDataToRedis() {
        List<Language> languages = LanguageHelper.languages();

        for (Language language : languages) {
            String languageCode = language.getCode();

            List<MtGenStatus> mtGenStatuz = this.mtGenStatusMapper.selectAllGenStatus(languageCode);
            Map<String, List<MtGenStatus>> map = mtGenStatuz.stream().collect(Collectors
                            .groupingBy(t -> t.getTenantId() + "::" + t.getModule() + "::" + t.getStatusGroup()));

            for (Map.Entry<String, List<MtGenStatus>> entry : map.entrySet()) {
                String[] array = entry.getKey().split("::");
                String tenantId = array[0];
                String module = array[1];
                String statusGroup = array[2];

                Map<String, String> genStatusMap = entry.getValue().stream()
                                .collect(Collectors.toMap(MtGenStatus::getStatusCode, t -> redisHelper.toJson(t)));

                String key = REDIS_GEN_STATUS + tenantId + ":" + module + ":" + statusGroup + ":" + languageCode;

                redisHelper.hshPutAll(key, genStatusMap);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String genStatusBasicPropertyUpdate(Long tenantId, MtGenStatus dto) {
        String genStatusId = dto.getGenStatusId();

        if (StringUtils.isEmpty(genStatusId)) {
            if (StringUtils.isEmpty(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "module", "【API:getGenStatusBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getStatusGroup())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:getGenStatusBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getStatusCode())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "statusCode", "【API:getGenStatusBasicPropertyUpdate】"));
            }

            MtGenStatus oldGenStatus = new MtGenStatus();
            oldGenStatus.setTenantId(tenantId);
            oldGenStatus.setStatusGroup(dto.getStatusGroup());
            oldGenStatus.setStatusCode(dto.getStatusCode());
            oldGenStatus = mtGenStatusMapper.selectOne(oldGenStatus);
            if (null != oldGenStatus) {
                throw new MtException("MT_GENERAL_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0038", "GENERAL"));
            }

            // 一个状态组下只能一个默认状态
            if ("Y".equals(dto.getDefaultFlag())) {
                MtGenStatus exests = new MtGenStatus();
                exests.setTenantId(tenantId);
                exests.setStatusGroup(dto.getStatusGroup());
                exests.setDefaultFlag("Y");
                List<MtGenStatus> statusList = mtGenStatusMapper.select(exests);
                if (CollectionUtils.isNotEmpty(statusList)) {
                    throw new MtException("MT_GENERAL_0051", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_GENERAL_0051", "GENERAL"));
                }
            }
            Long userId = DetailsHelper.getUserDetails().getUserId();
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setCreatedBy(userId);
            dto.setCreationDate(now);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            self().insertSelective(dto);

            genStatusId = dto.getGenStatusId();

            if (dto.get_tls() == null || dto.get_tls().get("description") == null) {
                String key = REDIS_GEN_STATUS + tenantId + ":" + dto.getModule() + ":" + dto.getStatusGroup() + ":"
                                + MtLanguageHelper.language();
                redisHelper.hshPut(key, dto.getStatusCode(), redisHelper.toJson(dto));
            } else {
                Map<String, String> map = dto.get_tls().get("description");
                dto.set_tls(null);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = REDIS_GEN_STATUS + tenantId + ":" + dto.getModule() + ":" + dto.getStatusGroup() + ":"
                                    + entry.getKey();
                    dto.setDescription(entry.getValue());
                    redisHelper.hshPut(key, dto.getStatusCode(), redisHelper.toJson(dto));
                }
            }


        } else {
            if (null != dto.getModule() && "".equals(dto.getModule())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "module", "【API:getGenStatusBasicPropertyUpdate】"));
            }
            if (null != dto.getStatusGroup() && "".equals(dto.getStatusGroup())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "statusGroup", "【API:getGenStatusBasicPropertyUpdate】"));
            }
            if (null != dto.getStatusCode() && "".equals(dto.getStatusCode())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0001", "GENERAL", "statusCode", "【API:getGenStatusBasicPropertyUpdate】"));
            }

            // 一个状态组下只能一个默认状态
            if ("Y".equals(dto.getDefaultFlag())) {
                MtGenStatus exests = new MtGenStatus();
                exests.setTenantId(tenantId);
                exests.setStatusGroup(dto.getStatusGroup());
                exests.setDefaultFlag("Y");
                List<MtGenStatus> statusList = mtGenStatusMapper.select(exests);
                if (CollectionUtils.isNotEmpty(statusList)) {
                    String finalGenStatusId = genStatusId;
                    List<MtGenStatus> genStatuses =
                                    statusList.stream().filter(t -> !t.getGenStatusId().equals(finalGenStatusId))
                                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(genStatuses)) {
                        throw new MtException("MT_GENERAL_0051", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_GENERAL_0051", "GENERAL"));
                    }
                }
            }

            MtGenStatus oldGenStatus = new MtGenStatus();
            oldGenStatus.setTenantId(tenantId);
            oldGenStatus.setGenStatusId(dto.getGenStatusId());
            oldGenStatus = mtGenStatusMapper.selectOne(oldGenStatus);
            if (null == oldGenStatus) {
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "genStatusId", "【API:getGenStatusBasicPropertyUpdate】"));
            }

            MtGenStatus oldGenStatus1 = new MtGenStatus();
            oldGenStatus1.setTenantId(tenantId);
            oldGenStatus1.setStatusGroup(dto.getStatusGroup());
            oldGenStatus1.setStatusCode(dto.getStatusCode());
            oldGenStatus1 = mtGenStatusMapper.selectOne(oldGenStatus1);
            if (null != oldGenStatus1 && !oldGenStatus1.getGenStatusId().equals(dto.getGenStatusId())) {
                throw new MtException("MT_GENERAL_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0038", "GENERAL"));
            }

            // 获取initialFlag为Y的原始数据(当修改时需判断)
            if ("Y".equals(oldGenStatus.getInitialFlag())) {
                // 校验当前角色是否可以修改initialFlag字段
                String allowUpdateRole = profileClient.getProfileValueByOptions(tenantId,
                                DetailsHelper.getUserDetails().getUserId(), DetailsHelper.getUserDetails().getRoleId(),
                                "GEN_INITIALIZE_UPDATE_ROLE");

                ResponseEntity<List<MtRoleVO>> listMemberRoles = remoteIamService.selfRoles(tenantId);
                List<MtRoleVO> roleList =
                                listMemberRoles != null && listMemberRoles.getBody() != null ? listMemberRoles.getBody()
                                                : new ArrayList<MtRoleVO>();

                List<String> roles = roleList != null
                                ? roleList.stream().map(MtRoleVO::getViewCode).collect(Collectors.toList())
                                : new ArrayList<String>();

                if (StringUtils.isEmpty(allowUpdateRole) || !roles.contains(allowUpdateRole)) {
                    throw new MtException("MT_GENERAL_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0007",
                                                    "GENERAL", allowUpdateRole,
                                                    "【API:getGenStatusBasicPropertyUpdate】"));
                }
            }

            Long userId = DetailsHelper.getUserDetails().getUserId();
            Date now = new Date();
            dto.setTenantId(tenantId);
            dto.setLastUpdateDate(now);
            dto.setLastUpdatedBy(userId);
            dto.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(dto);

            dto.setCreatedBy(oldGenStatus.getCreatedBy());
            dto.setCreationDate(oldGenStatus.getCreationDate());
            dto.setObjectVersionNumber(oldGenStatus.getObjectVersionNumber());
            if (dto.get_tls() == null || dto.get_tls().get("description") == null) {
                String key = REDIS_GEN_STATUS + tenantId + ":" + dto.getModule() + ":" + dto.getStatusGroup() + ":"
                                + MtLanguageHelper.language();
                redisHelper.hshPut(key, dto.getStatusCode(), redisHelper.toJson(dto));
            } else {
                Map<String, String> map = dto.get_tls().get("description");
                dto.set_tls(null);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = REDIS_GEN_STATUS + tenantId + ":" + dto.getModule() + ":" + dto.getStatusGroup() + ":"
                                    + entry.getKey();
                    dto.setDescription(entry.getValue());
                    redisHelper.hshPut(key, dto.getStatusCode(), redisHelper.toJson(dto));
                }
            }
        }
        return genStatusId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeGenStatus(Long tenantId, List<MtGenStatus> list) {
        List<String> sqlList = new ArrayList<String>();
        // 校验获取initialFlag为Y
        if (list.stream().anyMatch(t -> "Y".equals(t.getInitialFlag()))) {
            throw new MtException("MT_GENERAL_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0008", "GENERAL", "【API:removeGenStatus】"));

        }
        list.forEach(dto -> {
            MtGenStatus one = new MtGenStatus();
            one.setTenantId(tenantId);
            one.setGenStatusId(dto.getGenStatusId());
            sqlList.addAll(customDbRepository.getDeleteSql(one));
        });

        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        List<Language> languages = LanguageHelper.languages();
        for (MtGenStatus mtGenStatus : list) {
            for (Language language : languages) {
                String key = REDIS_GEN_STATUS + tenantId + ":" + mtGenStatus.getModule() + ":"
                                + mtGenStatus.getStatusGroup() + ":" + language.getCode();
                redisHelper.hshDelete(key, mtGenStatus.getStatusCode());
            }
        }
    }

    @Override
    public List<MtGenStatusVO5> propertyLimitGenStatusPropertyQuery(Long tenantId, MtGenStatusVO4 dto) {
        return mtGenStatusMapper.selectCondition(tenantId, dto);
    }


}
