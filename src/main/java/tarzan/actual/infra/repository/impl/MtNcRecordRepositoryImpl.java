package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.entity.MtNcRecordHis;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtNcIncidentRepository;
import tarzan.actual.domain.repository.MtNcRecordHisRepository;
import tarzan.actual.domain.repository.MtNcRecordRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtEoStepActualVO1;
import tarzan.actual.domain.vo.MtNcIncidentVO2;
import tarzan.actual.domain.vo.MtNcRecordVO1;
import tarzan.actual.domain.vo.MtNcRecordVO2;
import tarzan.actual.domain.vo.MtNcRecordVO3;
import tarzan.actual.domain.vo.MtNcRecordVO4;
import tarzan.actual.domain.vo.MtNcRecordVO5;
import tarzan.actual.domain.vo.MtNcRecordVO6;
import tarzan.actual.domain.vo.MtNcRecordVO7;
import tarzan.actual.domain.vo.MtNcRecordVO8;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.infra.mapper.MtNcRecordMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.MtDispositionFunction;
import tarzan.method.domain.entity.MtDispositionGroupMember;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.entity.MtNcSecondaryCode;
import tarzan.method.domain.repository.MtDispositionFunctionRepository;
import tarzan.method.domain.repository.MtDispositionGroupMemberRepository;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtNcSecondaryCodeRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 不良代码记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
@Component
public class MtNcRecordRepositoryImpl extends BaseRepositoryImpl<MtNcRecord> implements MtNcRecordRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtDispositionGroupMemberRepository mtDispositionGroupMemberRepo;
    @Autowired
    private MtDispositionFunctionRepository mtDispositionFunctionRepo;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private MtNcSecondaryCodeRepository mtNcSecondaryCodeRepository;
    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;
    @Autowired
    private MtNcRecordHisRepository mtNcRecordHisRepository;

    @Autowired
    private MtNcRecordMapper mtNcRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String ncEventCreate(Long tenantId, MtNcRecordVO6 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventTypeCode", "【API:ncEventCreate】"));
        }

        Date shiftDate = null;
        String shiftCode = "";

        // 2. 获取当前班次
        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
            if (mtWkcShiftVO3 != null) {
                shiftCode = mtWkcShiftVO3.getShiftCode();
                shiftDate = mtWkcShiftVO3.getShiftDate();
            }
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(shiftDate);
        eventCreateVO.setShiftCode(shiftCode);
        return mtEventRepository.eventCreate(tenantId, eventCreateVO);
    }

    @Override
    public MtNcRecord ncRecordPropertyGet(Long tenantId, String ncRecordId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordPropertyGet】"));
        }

        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setNcRecordId(ncRecordId);
        return mtNcRecordMapper.selectOne(mtNcRecord);
    }

    @Override
    public String ncRecordSourceEoStepActualGet(Long tenantId, String ncRecordId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordSourceEoStepActualGet】"));
        }

        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setNcRecordId(ncRecordId);
        mtNcRecord = mtNcRecordMapper.selectOne(mtNcRecord);

        return mtNcRecord == null ? "" : mtNcRecord.getEoStepActualId();
    }

    @Override
    public MtNcRecordVO5 ncDispositionGroupGet(Long tenantId, String ncRecordId) {
        MtNcRecordVO5 v1 = null;
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncDispositionGroupGet】"));

        }

        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setNcRecordId(ncRecordId);
        mtNcRecord.setNcRecordId(ncRecordId);
        mtNcRecord = self().selectOne(mtNcRecord);
        if (mtNcRecord != null) {
            v1 = new MtNcRecordVO5();
            v1.setDispositionGroupId(mtNcRecord.getDispositionGroupId());
            v1.setDispositionRouterId(mtNcRecord.getDispositionRouterId());
        }
        return v1;
    }

    @Override
    public String eoNcRecordAllClosedValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "eoId", "【API:eoNcRecordAllClosedValidate】"));

        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setEoId(eoId);
        mtNcRecord.setClosureRequiredFlag("Y");
        mtNcRecord.setNcStatus("OPEN");
        List<MtNcRecord> list = mtNcRecordMapper.select(mtNcRecord);

        return CollectionUtils.isEmpty(list) ? "Y" : "N";
    }

    @Override
    public String eoStepNcRecordAllClosedValidate(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "eoStepActualId", "【API:eoStepNcRecordAllClosedValidate】"));

        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setEoStepActualId(eoStepActualId);
        mtNcRecord.setClosureRequiredFlag("Y");
        mtNcRecord.setNcStatus("OPEN");
        List<MtNcRecord> list = mtNcRecordMapper.select(mtNcRecord);

        return CollectionUtils.isEmpty(list) ? "Y" : "N";
    }

    @Override
    public MtNcRecord eoIncidentAndCodeLimitNcRecordGet(Long tenantId, MtNcRecord dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eoId", "【API:eoIncidentAndCodeLimitNcRecordGet】"));
        }
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncCodeId", "【API:eoIncidentAndCodeLimitNcRecordGet】"));
        }
        if (StringUtils.isEmpty(dto.getNcIncidentId())) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001", "NC_RECORD",
                                            "ncIncidentId", "【API:eoIncidentAndCodeLimitNcRecordGet】"));
        }

        dto.setTenantId(tenantId);
        return mtNcRecordMapper.selectOne(dto);
    }

    @Override
    public List<MtNcRecord> eoLimitNcRecordQuery(Long tenantId, String eoId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eoId", "【API:eoLimitNcRecordQuery】"));
        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setEoId(eoId);
        return mtNcRecordMapper.select(mtNcRecord);
    }

    @Override
    public List<MtNcRecord> incidentLimitNcRecordQuery(Long tenantId, String ncIncidentId) {
        if (StringUtils.isEmpty(ncIncidentId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncIncidentId", "【API:incidentLimitNcRecordQuery】"));

        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setNcIncidentId(ncIncidentId);
        return mtNcRecordMapper.select(mtNcRecord);
    }

    @Override
    public void ncRecordSecondaryCodeClosedValidate(Long tenantId, String ncRecordId) {
        // 验证参数有效性
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001", "NC_RECORD",
                                            "ncRecordId", "【API:ncRecordSecondaryCodeClosedValidate】"));
        }

        // 1. 获取不良记录
        MtNcRecord mtNcRecord = ncRecordPropertyGet(tenantId, ncRecordId);
        if (mtNcRecord == null || StringUtils.isEmpty(mtNcRecord.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0003",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0003", "NC_RECORD",
                                            "ncRecordId", "【API:ncRecordSecondaryCodeClosedValidate】"));
        }

        // 2. 获取所有次级代码并验证是否存在
        List<MtNcSecondaryCode> mtNcSecondaryCodeList = mtNcSecondaryCodeRepository
                        .ncCodeLimitRequiredSecondaryCodeQuery(tenantId, mtNcRecord.getNcCodeId());
        if (CollectionUtils.isNotEmpty(mtNcSecondaryCodeList)) {
            for (MtNcSecondaryCode mtNcSecondaryCode : mtNcSecondaryCodeList) {
                MtNcRecord queryNcRecord = new MtNcRecord();
                queryNcRecord.setTenantId(tenantId);
                queryNcRecord.setNcCodeId(mtNcSecondaryCode.getNcCodeId());
                queryNcRecord.setEoId(mtNcRecord.getEoId());
                queryNcRecord.setParentNcRecordId(ncRecordId);
                if (CollectionUtils.isEmpty(mtNcRecordMapper.select(queryNcRecord))) {
                    throw new MtException("MT_NC_RECORD_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_NC_RECORD_0005", "NC_RECORD", "【API:ncRecordSecondaryCodeClosedValidate】"));
                }
            }
        }
    }

    @Override
    public List<MtNcRecord> propertyLimitNcRecordQuery(Long tenantId, MtNcRecord dto) {
        Criteria criteria = new Criteria(dto);
        criteria.where(new WhereField(MtNcRecord.FIELD_TENANT_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_NC_RECORD_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_SITE_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_EO_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_PARENT_NC_RECORD_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_USER_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_SEQUENCE, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_NC_INCIDENT_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_DATE_TIME, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_QTY, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_DEFECT_COUNT, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_COMMENTS, Comparison.LIKE));
        criteria.where(new WhereField(MtNcRecord.FIELD_NC_CODE_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_NC_TYPE, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_COMPONENT_MATERIAL_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_MATERIAL_LOT_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_REFERENCE_POINT, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_EO_STEP_ACTUAL_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_ROUTER_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_OPERATION_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_ROOT_CAUSE_OPERATION_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_WORKCELL_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_ROOT_CAUSE_WORKCELL_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_NC_STATUS, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_CONFIRMED_STATUS, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_CONFIRMED_DATE_TIME, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_CLOSURE_REQUIRED_FLAG, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_CLOSED_DATE_TIME, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_CLOSED_USER_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_DISPOSITION_DONE_FLAG, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_DISPOSITION_GROUP_ID, Comparison.EQUAL));
        criteria.where(new WhereField(MtNcRecord.FIELD_DISPOSITION_ROUTER_ID, Comparison.EQUAL));
        return self().selectOptional(dto, criteria);
    }

    @Override
    public String eoMaxNcLimitValidate(Long tenantId, MtNcRecordVO1 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eoId", "【API:eoMaxNcLimitValidate】"));

        }
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncCodeId", "【API:eoMaxNcLimitValidate】"));
        }

        // 1.获取属性[P1]maxNcLimit
        MtNcCode mtNcCode = mtNcCodeRepository.ncCodeGet(tenantId, dto.getNcCodeId());
        if (mtNcCode == null || mtNcCode.getMaxNcLimit() == null) {
            return "N";
        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setEoId(dto.getEoId());
        mtNcRecord.setNcCodeId(dto.getNcCodeId());
        int count = mtNcRecordMapper.eoMaxNcLimitCount(tenantId, mtNcRecord);

        return BigDecimal.valueOf(count).compareTo(new BigDecimal(mtNcCode.getMaxNcLimit().toString())) < 0 ? "N" : "Y";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtNcRecordVO8 ncRecordAndHisUpdate(Long tenantId, MtNcRecordVO7 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordAndHisUpdate】"));

        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventId", "【API:ncRecordAndHisUpdate】"));
        }

        // 2. 更新不良记录
        // 2.1. 查询不良记录
        MtNcRecord mtNcRecord = ncRecordPropertyGet(tenantId, dto.getNcRecordId());
        if (mtNcRecord == null || StringUtils.isEmpty(mtNcRecord.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0003", "NC_RECORD", "ncRecordId", "【API:ncRecordAndHisUpdate】"));
        }

        Date now = new Date();

        // 2.2. 更新赋值
        if (dto.getQty() != null) {
            mtNcRecord.setQty(dto.getQty());
        }
        if (dto.getDefectCount() != null) {
            mtNcRecord.setDefectCount(dto.getDefectCount());
        }
        if (dto.getComments() != null) {
            mtNcRecord.setComments(dto.getComments());
        }
        if (dto.getComponentMaterialId() != null) {
            mtNcRecord.setComponentMaterialId(dto.getComponentMaterialId());
        }
        if (dto.getMaterialLotId() != null) {
            mtNcRecord.setMaterialLotId(dto.getMaterialLotId());
        }
        if (dto.getReferencePoint() != null) {
            mtNcRecord.setReferencePoint(dto.getReferencePoint());
        }
        if (StringUtils.isNotEmpty(dto.getEoStepActualId())) {
            mtNcRecord.setEoStepActualId(dto.getEoStepActualId());

            // 获取工艺路线及工艺
            MtEoStepActualVO1 mtEoStepActualVO1 =
                            mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
            if (mtEoStepActualVO1 != null) {
                mtNcRecord.setRouterId(mtEoStepActualVO1.getRouterId());
                mtNcRecord.setOperationId(mtEoStepActualVO1.getOperationId());
            }
        }
        if (dto.getRootCauseOperationId() != null) {
            mtNcRecord.setRootCauseOperationId(dto.getRootCauseOperationId());
        }
        if (dto.getWorkcellId() != null) {
            mtNcRecord.setWorkcellId(dto.getWorkcellId());
        }
        if (dto.getRootCaiseWorkcellId() != null) {
            mtNcRecord.setRootCauseWorkcellId(dto.getRootCaiseWorkcellId());
        }
        if (dto.getNcStatus() != null) {
            mtNcRecord.setNcStatus(dto.getNcStatus());
        }
        if (StringUtils.isNotEmpty(dto.getVerifiedStatus())) {
            mtNcRecord.setConfirmedStatus(dto.getVerifiedStatus());
            mtNcRecord.setConfirmedDateTime(now);
        }
        if ("CLOSED".equals(dto.getNcStatus())) {
            mtNcRecord.setClosedDateTime(now);
            mtNcRecord.setClosedUserId(DetailsHelper.getUserDetails().getUserId());
        }
        if (dto.getDispositionDoneFlag() != null) {
            mtNcRecord.setDispositionDoneFlag(dto.getDispositionDoneFlag());
        }
        if (dto.getDispositionGroupId() != null) {
            mtNcRecord.setDispositionGroupId(dto.getDispositionGroupId());
        }
        if (dto.getDispositionRouter() != null) {
            mtNcRecord.setDispositionRouterId(dto.getDispositionGroupId());
        }

        mtNcRecord.setTenantId(tenantId);
        self().updateByPrimaryKeySelective(mtNcRecord);

        // 3. 生成工艺路线步骤实绩记史
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(dto.getEventId());
        mtNcRecordHis.setTenantId(tenantId);
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);

        MtNcRecordVO8 result = new MtNcRecordVO8();
        result.setNcRecordId(mtNcRecord.getNcRecordId());
        result.setNcRecordHisId(mtNcRecordHis.getNcRecordHisId());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String ncRecordAndIncidentClose(Long tenantId, MtNcRecordVO2 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordAndIncidentClose】"));
        }
        // 1.【第一步】关闭eventId=P3
        String eventId = ncRecordClose(tenantId, dto);
        // 2.【第二步】验证是否自动关闭不良事故ncCodeId=P1,ncIncidentId=P2
        MtNcRecord mtNcRecord = ncRecordPropertyGet(tenantId, dto.getNcRecordId());

        String flag = mtNcCodeRepository.ncCodeIncidentAutoCloseValidate(tenantId, mtNcRecord.getNcCodeId());

        if (!"Y".equals(flag)) {
            return eventId;
        }
        // 3.【第三步】
        MtNcIncidentVO2 mtNcIncidentVO2 = new MtNcIncidentVO2();
        mtNcIncidentVO2.setEventId(eventId);
        mtNcIncidentVO2.setNcIncidentId(mtNcRecord.getNcIncidentId());
        mtNcIncidentVO2.setStatus("CLOSED");
        mtNcIncidentRepository.ncIncidentAndHisUpdate(tenantId, mtNcIncidentVO2);
        return eventId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtNcRecordVO8 ncRecordAndHisCreate(Long tenantId, MtNcRecordVO3 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eoId", "【API:ncRecordAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getNcIncidentId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncIncidentId", "【API:ncRecordAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncCodeId", "【API:ncRecordAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eventId", "【API:ncRecordAndHisCreate】"));
        }

        // 验证是否需要组件
        if (StringUtils.isEmpty(dto.getComponentMaterialId())) {
            String flag = mtNcCodeRepository.ncCodeComponentRequiredValidate(tenantId, dto.getNcCodeId());
            if (MtBaseConstants.YES.equals(flag)) {
                throw new MtException("MT_NC_RECORD_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_NC_RECORD_0002", "NC_RECORD", "【API:ncRecordAndHisCreate】"));
            }

        }
        // 【第一步】获取相关参数
        // 1.1获取站点及物料 siteId=[P1]，materialId=[P5]
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null) {// 为空存空
            throw new MtException("MT_NC_RECORD_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0003", "NC_RECORD", "eoId", "【API:ncRecordAndHisCreate】"));
        }
        // 1.2获取不良代码类型及处置组 ncType=[P2]，dispositionGroupId=[P7]
        MtNcCode mtNcCode = mtNcCodeRepository.ncCodePropertyGet(tenantId, dto.getNcCodeId());
        if (mtNcCode == null) {
            throw new MtException("MT_NC_RECORD_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0003", "NC_RECORD", "ncCodeId", "【API:ncRecordAndHisCreate】"));
        }
        // 1.3routerId=[P3],operationId=[P4]
        // 新增逻辑当eoStepActualId未输入时,不调用stepActualLimitEoAndRouterGet
        MtEoStepActualVO1 mtEoStepActualVO1 = null;
        if (StringUtils.isNotBlank(dto.getEoStepActualId())) {
            mtEoStepActualVO1 =
                            mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        }

        if (mtEoStepActualVO1 == null) {
            mtEoStepActualVO1 = new MtEoStepActualVO1();
        }
        // 1.4获取是否需要关闭标识 [P6]
        String closeRequiredFlag = mtNcCodeRepository.ncCodeClosedRequiredValidate(tenantId, dto.getNcCodeId());

        // 1.5 根据当前EO获取最大值+1（初始为1）
        Long sequence = mtNcRecordMapper.getMaxSequence(tenantId, dto.getEoId());
        if (sequence == null) {
            sequence = Long.valueOf(1L);
        }
        sequence++;
        // 【第二步】生成不良记录

        MtNcRecord checkNcRecord = new MtNcRecord();
        checkNcRecord.setTenantId(tenantId);
        checkNcRecord.setEoId(dto.getEoId());
        checkNcRecord.setNcIncidentId(dto.getNcIncidentId());
        checkNcRecord.setNcCodeId(dto.getNcCodeId());
        checkNcRecord = this.mtNcRecordMapper.selectOne(checkNcRecord);
        if (null != checkNcRecord) {
            throw new MtException("MT_NC_RECORD_0006", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0006", "NC_RECORD", "【API:ncRecordAndHisCreate】"));
        }

        MtNcRecord mtNcRecord = new MtNcRecord();
        BeanUtils.copyProperties(dto, mtNcRecord);
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setSiteId(mtEo.getSiteId());
        mtNcRecord.setSequence(sequence);
        mtNcRecord.setDateTime(new Date());
        mtNcRecord.setNcType(mtNcCode.getNcType());
        mtNcRecord.setRouterId(mtEoStepActualVO1.getRouterId());
        mtNcRecord.setOperationId(mtEoStepActualVO1.getOperationId());
        mtNcRecord.setMaterialId(mtEo.getMaterialId());
        mtNcRecord.setNcStatus("OPEN");
        mtNcRecord.setConfirmedStatus("N");
        mtNcRecord.setClosureRequiredFlag(closeRequiredFlag);
        mtNcRecord.setDispositionDoneFlag("N");
        mtNcRecord.setDispositionGroupId(mtNcCode.getDispositionGroupId());
        self().insertSelective(mtNcRecord);

        // 【第三步】生成工艺路线步骤实绩记史
        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        BeanUtils.copyProperties(mtNcRecord, mtNcRecordHis);
        mtNcRecordHis.setEventId(dto.getEventId());
        mtNcRecordHis.setTenantId(tenantId);
        mtNcRecordHisRepository.insertSelective(mtNcRecordHis);

        MtNcRecordVO8 result = new MtNcRecordVO8();
        result.setNcRecordId(mtNcRecord.getNcRecordId());
        result.setNcRecordHisId(mtNcRecordHis.getNcRecordHisId());
        return result;
    }

    @Override
    public String ncRecordCloseVerify(Long tenantId, String ncRecordId) {
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordCloseVerify】"));

        }
        try {
            ncRecordSecondaryCodeClosedValidate(tenantId, ncRecordId);
        } catch (MtException e) {
            return "N";
        }
        MtNcRecord mtNcRecord = ncRecordPropertyGet(tenantId, ncRecordId);

        if (mtNcRecord == null) {
            return "N";
        }

        String flag = mtNcCodeRepository.ncCodeConfirmRequiredValidate(tenantId, mtNcRecord.getNcCodeId());

        return "Y".equals(flag) && "N".equals(mtNcRecord.getConfirmedStatus()) ? "N" : "Y";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void ncRecordCloseProcess(Long tenantId, MtNcRecordVO2 dto) {
        if (StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordCloseProcess】"));

        }
        // Step1 关闭不良记录
        String eventId = ncRecordAndIncidentClose(tenantId, dto);

        // Step2 验证是否需要关闭父代码
        MtNcRecord mtNcRecord = ncRecordPropertyGet(tenantId, dto.getNcRecordId());

        if (mtNcRecord == null || StringUtils.isEmpty(mtNcRecord.getNcCodeId())) {
            throw new MtException("MT_NC_RECORD_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0003", "NC_RECORD", "ncCodeId", "【API:ncRecordCloseProcess】"));


        } else if (StringUtils.isEmpty(mtNcRecord.getParentNcRecordId())) {
            return;
        }
        String verifyResult = mtNcCodeRepository.ncCodePrimaryCodeAutoCloseValidate(tenantId, mtNcRecord.getNcCodeId());
        if (!"Y".equals(verifyResult)) {
            return;
        }

        // Step3关闭父代码
        dto.setNcRecordId(mtNcRecord.getParentNcRecordId());
        dto.setParentEventId(eventId);
        ncRecordAndIncidentClose(tenantId, dto);
    }

    @Override
    public String ncIncidentCloseVerify(Long tenantId, String ncIncidentId) {
        if (StringUtils.isEmpty(ncIncidentId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncIncidentId", "【API:ncIncidentCloseVerify】"));
        }

        MtNcRecord dto = new MtNcRecord();
        dto.setTenantId(tenantId);
        dto.setNcIncidentId(ncIncidentId);
        dto.setClosureRequiredFlag("Y");
        dto.setNcStatus("OPEN");
        List<MtNcRecord> mtNcRecords = this.mtNcRecordMapper.select(dto);

        return CollectionUtils.isEmpty(mtNcRecords) ? "Y" : "N";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String ncRecordClose(Long tenantId, MtNcRecordVO2 dto) {
        if (StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordClose】"));
        }

        // Step1创建事件
        MtNcRecordVO6 eventVO = new MtNcRecordVO6();
        BeanUtils.copyProperties(dto, eventVO);
        eventVO.setEventTypeCode("NC_RECORD_CLOSE");
        String eventId = ncEventCreate(tenantId, eventVO);

        // Step2取消
        MtNcRecordVO7 mtNcRecordUpdateVO = new MtNcRecordVO7();
        mtNcRecordUpdateVO.setNcRecordId(dto.getNcRecordId());
        mtNcRecordUpdateVO.setNcStatus("CLOSED");
        mtNcRecordUpdateVO.setEventId(eventId);
        ncRecordAndHisUpdate(tenantId, mtNcRecordUpdateVO);
        return eventId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void ncRecordConfirm(Long tenantId, MtNcRecordVO2 dto) {
        if (StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordConfirm】"));
        }

        // Step1创建事件
        MtNcRecordVO6 eventVO = new MtNcRecordVO6();
        BeanUtils.copyProperties(dto, eventVO);
        eventVO.setEventTypeCode("NC_RECORD_CONFIRM");
        String eventId = ncEventCreate(tenantId, eventVO);

        // Step2复核
        MtNcRecordVO7 mtNcRecordUpdateVO = new MtNcRecordVO7();
        mtNcRecordUpdateVO.setNcRecordId(dto.getNcRecordId());
        mtNcRecordUpdateVO.setVerifiedStatus("Y");
        mtNcRecordUpdateVO.setEventId(eventId);
        ncRecordAndHisUpdate(tenantId, mtNcRecordUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void ncRecordCancel(Long tenantId, MtNcRecordVO2 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getNcRecordId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordCancel】"));
        }

        // 【第一步】创建事件
        MtNcRecordVO6 mtNcRecordEventVO = new MtNcRecordVO6();
        BeanUtils.copyProperties(dto, mtNcRecordEventVO);
        mtNcRecordEventVO.setEventTypeCode("NC_RECORD_CANCEL");

        // P1=eventId
        String eventId = ncEventCreate(tenantId, mtNcRecordEventVO);

        // 【第二步】取消
        MtNcRecordVO7 mtNcRecordUpdateVO = new MtNcRecordVO7();
        mtNcRecordUpdateVO.setNcRecordId(dto.getNcRecordId());
        mtNcRecordUpdateVO.setNcStatus("CANCEL");
        mtNcRecordUpdateVO.setEventId(eventId);
        ncRecordAndHisUpdate(tenantId, mtNcRecordUpdateVO);
    }

    @Override
    public List<MtNcRecord> parentNcCodeLimitNcRecordGet(Long tenantId, String parentNcRecordId) {
        if (StringUtils.isEmpty(parentNcRecordId)) {
            throw new MtException("MT_NC_RECORD_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_RECORD_0001", "NC_RECORD",
                                            "parentNcRecordId", "【API:parentNcCodeLimitNcRecordGet】"));
        }
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setParentNcRecordId(parentNcRecordId);
        return mtNcRecordMapper.select(mtNcRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> ncRecordCreateProcess(Long tenantId, MtNcRecordVO4 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "eoId", "【API:ncRecordCreateProcess】"));
        }
        if (StringUtils.isEmpty(dto.getNcIncidentId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncIncidentId", "【API:ncRecordCreateProcess】"));
        }
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncCodeId", "【API:ncRecordCreateProcess】"));
        }

        // 1. 创建事件
        MtNcRecordVO6 mtNcRecordEventVO = new MtNcRecordVO6();
        mtNcRecordEventVO.setEventTypeCode("NC_RECORD_CREATE");
        mtNcRecordEventVO.setWorkcellId(dto.getWorkcellId());
        mtNcRecordEventVO.setParentEventId(dto.getParentEventId());
        mtNcRecordEventVO.setEventRequestId(dto.getEventRequestId());
        String eventId = ncEventCreate(tenantId, mtNcRecordEventVO);

        // 2. 不良记录创建
        MtNcRecordVO3 mtNcRecordVO3 = new MtNcRecordVO3();
        BeanUtils.copyProperties(dto, mtNcRecordVO3);
        mtNcRecordVO3.setEventId(eventId);

        MtNcRecordVO8 mtNcRecordVO8 = ncRecordAndHisCreate(tenantId, mtNcRecordVO3);

        String ncRecordId = mtNcRecordVO8.getNcRecordId();

        List<String> resultList = new ArrayList<>();

        // 3. 获取处置方法
        MtNcRecordVO5 mtNcRecordVO5 = ncDispositionGroupGet(tenantId, ncRecordId);
        if (mtNcRecordVO5 != null && StringUtils.isNotEmpty(mtNcRecordVO5.getDispositionGroupId())) {
            List<MtDispositionGroupMember> mtDispositionGroupMemberList = mtDispositionGroupMemberRepo
                            .dispositionGroupMemberQuery(tenantId, mtNcRecordVO5.getDispositionGroupId());
            if (CollectionUtils.isNotEmpty(mtDispositionGroupMemberList)) {
                for (MtDispositionGroupMember temp : mtDispositionGroupMemberList) {
                    // 4. 执行处置方法
                    MtDispositionFunction mtDispositionFunction = mtDispositionFunctionRepo
                                    .dispositionFunctionPropertyGet(tenantId, temp.getDispositionFunctionId());
                    if (mtDispositionFunction != null) {
                        resultList.add(mtDispositionFunction.getDispositionFunction());
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    public List<MtNcRecord> eoStepActualLimitNcRecordQuery(Long tenantId, String eoStepActualId) {
        MtNcRecord mtNcRecord = new MtNcRecord();
        mtNcRecord.setTenantId(tenantId);
        mtNcRecord.setEoStepActualId(eoStepActualId);
        return mtNcRecordMapper.select(mtNcRecord);
    }
}
