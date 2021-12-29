package tarzan.actual.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO3;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO4;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;

/**
 * 指令实绩明细表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtInstructionActualDetailRepositoryImpl extends BaseRepositoryImpl<MtInstructionActualDetail>
                implements MtInstructionActualDetailRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String instructionActualDetailCreate(Long tenantId, MtInstructionActualDetail dto) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(dto.getActualId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "actualId", "【API:instructionActualDetailCreate】"));

        }
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "materialLotId", "【API:instructionActualDetailCreate】"));

        }
        if (StringUtils.isEmpty(dto.getUomId())) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "uomId", "【API:instructionActualDetailCreate】"));

        }
        if (dto.getActualQty() == null) {
            throw new MtException("MT_INSTRUCTION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0001", "INSTRUCTION", "actualQty", "【API:instructionActualDetailCreate】"));

        }

        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setTenantId(tenantId);
        mtInstructionActualDetail.setActualId(dto.getActualId());
        mtInstructionActualDetail.setMaterialLotId(dto.getMaterialLotId());
        mtInstructionActualDetail = this.mtInstructionActualDetailMapper.selectOne(mtInstructionActualDetail);
        if (null != mtInstructionActualDetail) {
            throw new MtException("MT_INSTRUCTION_0038",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0038",
                                            "INSTRUCTION", "mt_instruction_actual_detail", "actualId,materialLotId",
                                            "【API:instructionActualDetailCreate】"));
        }

        dto.setTenantId(tenantId);
        self().insertSelective(dto);
        // 输出参数actualDetailId
        return dto.getActualDetailId();

    }

    @Override
    public List<MtInstructionActualDetailVO> propertyLimitInstructionActualDetailQuery(Long tenantId,
                    MtInstructionActualDetail detail) {
        return mtInstructionActualDetailMapper.propertyLimitInstructionActualDetailQuery(tenantId, detail);
    }

    @Override
    public List<MtInstructionActualDetail> instructionLimitActualDetailQuery(Long tenantId, String instructionId) {
        if (StringUtils.isEmpty(instructionId)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionId", "【API:instructionLimitActualDetailQuery】"));
        }

        List<MtInstructionActual> mtInstructionActuals =
                        mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, instructionId);
        if (CollectionUtils.isEmpty(mtInstructionActuals)) {
            return Collections.emptyList();
        }

        List<String> actualIds = mtInstructionActuals.stream().map(MtInstructionActual::getActualId)
                        .collect(Collectors.toList());
        return instructionActualLimitDetailBatchQuery(tenantId, actualIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void instructionActualDetailBatchDelete(Long tenantId, List<String> instructionActualDetailIdList) {
        mtInstructionActualDetailMapper.batchDelete(tenantId, instructionActualDetailIdList);
    }

    @Override
    public List<MtInstructionActualDetail> instructionActualLimitDetailBatchQuery(Long tenantId,
                    List<String> actualIds) {
        return this.mtInstructionActualDetailMapper.selectByIdsCustom(tenantId, actualIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void instructActDetailAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "keyId", "【API:instructActDetailAttrPropertyUpdate】"));
        }
        // 获取主表数据
        MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
        actualDetail.setTenantId(tenantId);
        actualDetail.setActualDetailId(dto.getKeyId());
        actualDetail = mtInstructionActualDetailMapper.selectOne(actualDetail);
        if (actualDetail == null || StringUtils.isEmpty(actualDetail.getActualDetailId())) {
            throw new MtException("MT_INSTRUCTION_0055",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0055",
                                            "INSTRUCTION", "keyId:" + dto.getKeyId(), "mt_instruction_actual_detail",
                                            "【API:instructActDetailAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruct_act_detail_attr", dto.getKeyId(),
                        dto.getEventId(), dto.getAttrs());
    }

    @Override
    public List<MtInstructionActualDetailVO2> instructionLimitActualDetailBatchQuery(Long tenantId,
                    List<String> instructionIdList) {
        if (CollectionUtils.isEmpty(instructionIdList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "instructionIdList",
                                            "【API:instructionLimitActualDetailBatchQuery】"));
        }
        return mtInstructionActualDetailMapper.selectByMyCondition(tenantId, instructionIdList);
    }

    /**
     * instructionActualDetailBatchCreate-指令实绩明细批量创建
     *
     * @author chuang.yang
     * @date 2020/1/14
     * @param tenantId
     * @param actualDetailMessageList
     * @return java.util.List<tarzan.actual.domain.vo.MtInstructionActualDetailVO3>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtInstructionActualDetailVO3> instructionActualDetailBatchCreate(Long tenantId,
                    List<MtInstructionActualDetailVO4> actualDetailMessageList) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(actualDetailMessageList)) {
            throw new MtException("MT_INSTRUCTION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                            "INSTRUCTION", "actualDetailMessageList",
                                            "【API:instructionActualDetailBatchCreate】"));
        }

        for (MtInstructionActualDetailVO4 detail : actualDetailMessageList) {
            if (StringUtils.isEmpty(detail.getActualId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "actualId", "【API:instructionActualDetailBatchCreate】"));
            }
            if (StringUtils.isEmpty(detail.getMaterialLotId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "materialLotId",
                                                "【API:instructionActualDetailBatchCreate】"));
            }
            if (StringUtils.isEmpty(detail.getUomId())) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "uomId", "【API:instructionActualDetailBatchCreate】"));
            }
            if (detail.getActualQty() == null) {
                throw new MtException("MT_INSTRUCTION_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INSTRUCTION_0001",
                                                "INSTRUCTION", "actualQty",
                                                "【API:instructionActualDetailBatchCreate】"));
            }
        }

        List<String> sqlList = new ArrayList<>(actualDetailMessageList.size());
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        // 批量获取新增数据的ID和CID
        List<String> instructionActualDetailIdS =
                        customDbRepository.getNextKeys("mt_instruction_actual_detail_s", actualDetailMessageList.size());
        List<String> instructionActualDetailCidS = customDbRepository.getNextKeys("mt_instruction_actual_detail_cid_s",
                        actualDetailMessageList.size());

        Map<String, List<String>> actualDetailMap = new HashMap<>();

        // 整理批量执行参数
        for (MtInstructionActualDetailVO4 actualDetail : actualDetailMessageList) {
            MtInstructionActualDetail inserData = new MtInstructionActualDetail();
            inserData.setActualId(actualDetail.getActualId());
            inserData.setMaterialLotId(actualDetail.getMaterialLotId());
            inserData.setActualQty(actualDetail.getActualQty());
            inserData.setContainerId(actualDetail.getContainerId());
            inserData.setFromLocatorId(actualDetail.getFromLocatorId());
            inserData.setToLocatorId(actualDetail.getToLocatorId());
            inserData.setUomId(actualDetail.getUomId());

            String detailId = instructionActualDetailIdS.remove(0);
            inserData.setActualDetailId(detailId);
            inserData.setTenantId(tenantId);
            inserData.setCid(Long.valueOf(instructionActualDetailCidS.remove(0)));
            inserData.setCreatedBy(userId);
            inserData.setCreationDate(now);
            inserData.setLastUpdateDate(now);
            inserData.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getInsertSql(inserData));

            // 记录实绩id和实际明细id关系
            List<String> exitsDetailIds = actualDetailMap.remove(actualDetail.getActualId());
            List<String> detailIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(exitsDetailIds)) {
                detailIds.addAll(exitsDetailIds);
            }

            if (CollectionUtils.isEmpty(detailIds)) {
                actualDetailMap.put(actualDetail.getActualId(), Arrays.asList(detailId));
            } else {
                detailIds.add(detailId);
                actualDetailMap.put(actualDetail.getActualId(), detailIds);
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        List<MtInstructionActualDetailVO3> resultList = new ArrayList<>(actualDetailMessageList.size());
        for (Map.Entry<String, List<String>> entry : actualDetailMap.entrySet()) {
            MtInstructionActualDetailVO3 result = new MtInstructionActualDetailVO3();
            result.setActualId(entry.getKey());
            result.setActualDetailIds(entry.getValue());
            resultList.add(result);
        }

        return resultList;
    }
}
