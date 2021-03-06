package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeSsnInspectLineMapper;
import com.ruike.hme.infra.mapper.HmeSsnInspectResultHeaderMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.infra.mapper.MtTagGroupAssignMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????????????? ???????????????
 *
 * @author sanfeng.zhang@hand-china 2021-02-04 14:51:27
 */
@Component
public class HmeSsnInspectResultHeaderRepositoryImpl extends BaseRepositoryImpl<HmeSsnInspectResultHeader> implements HmeSsnInspectResultHeaderRepository {

    @Autowired
    private HmeSsnInspectResultHeaderMapper hmeSsnInspectResultHeaderMapper;
    @Autowired
    private HmeSsnInspectHeaderRepository hmeSsnInspectHeaderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;
    @Autowired
    private HmeSsnInspectResultLineRepository hmeSsnInspectResultLineRepository;
    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;
    @Autowired
    private MtTagGroupAssignMapper mtTagGroupAssignMapper;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private HmeSsnInspectLineMapper hmeSsnInspectLineMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Override
    public HmeSsnInspectResultVO4 workcellScan(Long tenantId, HmeEoJobSnDTO dto) {
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, dto);
        HmeSsnInspectResultVO4 resultVO4 = new HmeSsnInspectResultVO4();
        BeanUtils.copyProperties(hmeEoJobSnVO4, resultVO4);
        if (CollectionUtils.isNotEmpty(resultVO4.getOperationIdList())) {
            MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(resultVO4.getOperationIdList().get(0));
            resultVO4.setOperationName(mtOperation != null ? mtOperation.getOperationName() : "");
        }
        return resultVO4;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeSsnInspectResultVO2> querySsnInspectTag(Long tenantId, HmeSsnInspectResultVO resultVO) {
        // ????????????????????????
        if (StringUtils.isBlank(resultVO.getStandardSnCode())) {
            throw new MtException("HME_SSN_INSPECT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_006", "HME"));
        }
        // ????????????????????????
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.SSN_WORK_WAY", tenantId);
        // ??????
        // ??????????????????????????????
        List<HmeSsnInspectHeader> hmeSsnInspectHeaderList = hmeSsnInspectResultHeaderMapper.querySsnInspectHeader(tenantId, resultVO);
        // ??????????????????????????????
        if (CollectionUtils.isEmpty(hmeSsnInspectHeaderList)) {
            Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "1")).findFirst();
            // ??????????????????
            String materialCode = "";
            if (CollectionUtils.isNotEmpty(resultVO.getMaterialIdList())) {
                List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, resultVO.getMaterialIdList());
                List<String> materialCodeList = mtMaterialVOS.stream().map(MtMaterialVO::getMaterialCode).distinct().collect(Collectors.toList());
                materialCode = StringUtils.join(materialCodeList, ",");
            }
            throw new MtException("HME_SSN_INSPECT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_001", "HME", materialCode, resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
        }
        // ?????????????????? ???????????????????????????????????? ?????????
        if (CollectionUtils.isNotEmpty(resultVO.getMaterialIdList())) {
            List<String> materialIdList = hmeSsnInspectHeaderList.stream().map(HmeSsnInspectHeader::getMaterialId).distinct().collect(Collectors.toList());
            Optional<String> filterOpt = resultVO.getMaterialIdList().stream().filter(mid -> !materialIdList.contains(mid)).findFirst();
            if (filterOpt.isPresent()) {
                // ??????????????????????????? ??????????????????
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(filterOpt.get());
                Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO.getWorkWay())).findFirst();
                throw new MtException("HME_SSN_INSPECT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_001", "HME", mtMaterial.getMaterialCode(), resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
        }
        // ???????????????2 ????????????1???????????????????????????OK
        if (StringUtils.equals(resultVO.getWorkWay(), "2")) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(resultVO.getWkcShiftId());
            // 20210309 add by sanfeng.zhang for tianyang.xie ???????????? ???????????? ?????? ???????????? ???????????????1
            List<HmeSsnInspectResultHeader> hmeSsnInspectResultHeaderList = hmeSsnInspectResultHeaderMapper.querySsnInspectResultHeaderTwo(tenantId, resultVO.getWorkcellId(), "1", DateUtil.date2String(new Date(), "yyyy-MM-dd"), mtWkcShift.getShiftCode());
            Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "1")).findFirst();
            // ????????? ?????????
            if (CollectionUtils.isEmpty(hmeSsnInspectResultHeaderList)) {
                List<String> materialIdList = resultVO.getMaterialIdList();
                MtMaterial mtMaterial = null;
                if (CollectionUtils.isNotEmpty(materialIdList)) {
                    mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialIdList.get(0));
                }
                throw new MtException("HME_SSN_INSPECT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_002", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
            // ??????????????? ????????????????????? ????????????OK?????????
            Map<String, List<HmeSsnInspectResultHeader>> resultHeaderMap = hmeSsnInspectResultHeaderList.stream().collect(Collectors.groupingBy(rh -> StringUtils.isNotBlank(rh.getMaterialId()) ? rh.getMaterialId() : "-1"));
            for (Map.Entry<String, List<HmeSsnInspectResultHeader>> resultHeaderEntry : resultHeaderMap.entrySet()) {
                List<HmeSsnInspectResultHeader> resultHeaderList = resultHeaderEntry.getValue();
                Optional<HmeSsnInspectResultHeader> resultHeaderFirst = resultHeaderList.stream().sorted(Comparator.comparing(HmeSsnInspectResultHeader::getCreationDate).reversed()).findFirst();
                String result = resultHeaderFirst.isPresent() ? resultHeaderFirst.get().getResult() : "";
                if (!HmeConstants.ConstantValue.OK.equals(result)) {
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(resultHeaderEntry.getKey());
                    throw new MtException("HME_SSN_INSPECT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_002", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
                }
            }
        }
        // ???????????? ????????????????????? ?????????????????????????????????????????????????????????
        // ???????????????
        List<String> tagCodeList = new ArrayList<>();
        Map<String, List<HmeSsnInspectHeader>> ssnInspectHeaderMap = hmeSsnInspectHeaderList.stream().collect(Collectors.groupingBy(hl -> StringUtils.isNotBlank(hl.getMaterialId()) ? hl.getMaterialId() : "-1"));
        for (Map.Entry<String, List<HmeSsnInspectHeader>> ssnInspectHeaderEntry : ssnInspectHeaderMap.entrySet()) {
            List<HmeSsnInspectHeader> inspectHeaderList = ssnInspectHeaderEntry.getValue();
            Optional<HmeSsnInspectHeader> ssnInspectHeaderOpt = inspectHeaderList.stream().sorted(Comparator.comparing(HmeSsnInspectHeader::getLastUpdateDate).reversed()).findFirst();
            List<HmeSsnInspectResultVO2> resultVO2List = hmeSsnInspectResultHeaderMapper.querySsnInspectTag(tenantId, ssnInspectHeaderOpt.get().getSsnInspectHeaderId());
            if (CollectionUtils.isEmpty(resultVO2List)) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(ssnInspectHeaderEntry.getKey());
                Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO.getWorkWay())).findFirst();
                throw new MtException("HME_SSN_INSPECT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_003", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
            List<String> tagCodes = resultVO2List.stream().map(HmeSsnInspectResultVO2::getTagCode).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tagCodeList)) {
                if (tagCodes.size() != tagCodeList.size() || !tagCodeList.containsAll(tagCodes)) {
                    throw new MtException("HME_SSN_INSPECT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_011", "HME"));
                }
            } else {
                tagCodeList.addAll(tagCodes);
            }
        }
        // ??????????????????????????? ?????????????????????????????????
        List<HmeSsnInspectHeader> lastUpdateHeader = hmeSsnInspectHeaderList.stream().sorted(Comparator.comparing(HmeSsnInspectHeader::getLastUpdateDate).reversed()).collect(Collectors.toList());
        List<HmeSsnInspectResultVO2> resultVO2List = hmeSsnInspectResultHeaderMapper.querySsnInspectTag(tenantId, lastUpdateHeader.get(0).getSsnInspectHeaderId());
        if (CollectionUtils.isEmpty(resultVO2List)) {
            // ??????????????????
            String materialCode = "";
            if (CollectionUtils.isNotEmpty(resultVO.getMaterialIdList())) {
                List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, resultVO.getMaterialIdList());
                List<String> materialCodeList = mtMaterialVOS.stream().map(MtMaterialVO::getMaterialCode).distinct().collect(Collectors.toList());
                materialCode = StringUtils.join(materialCodeList, ",");
            }
            Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO.getWorkWay())).findFirst();
            throw new MtException("HME_SSN_INSPECT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_003", "HME", materialCode, resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
        }
        // ????????????????????????????????????????????? ???????????????
        List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtEo.FIELD_IDENTIFICATION, resultVO.getStandardSnCode())).build());
        if (CollectionUtils.isEmpty(mtEoList)) {
            throw new MtException("HME_SSN_INSPECT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_008", "HME", resultVO.getStandardSnCode()));
        }
        // ?????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(resultVO.getStandardSnCode());
        }});
        if (mtMaterialLot == null) {
            throw new MtException("HME_SSN_INSPECT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_007", "HME", resultVO.getStandardSnCode()));
        }
        // ??????????????????
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setKeyId(mtMaterialLot.getMaterialLotId());
            setAttrName("REWORK_FLAG");
        }});
        String reworkFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : HmeConstants.ConstantValue.NO;
        List<HmeEoJobSn> hmeEoJobSnList = hmeSsnInspectResultHeaderMapper.querySiteInList(tenantId, resultVO, mtEoList.get(0).getEoId(), reworkFlag);
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            Date currentDate = CommonUtils.currentTimeGet();
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            // ???????????????????????????????????????jobId
            List<String> jobIdList = new ArrayList<>();
            // ??????????????????hme_eo_job_sn???hme_eo_job_data_record?????????
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(currentDate);
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setWorkcellId(resultVO.getWorkcellId());
            hmeEoJobSn.setOperationId(resultVO.getOperationId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setEoId(mtEoList.get(0).getEoId());
            hmeEoJobSn.setReworkFlag(reworkFlag);
            hmeEoJobSn.setJobType("SSN_PROCESS");
            hmeEoJobSn.setAttribute3(resultVO.getCosType());
            hmeEoJobSn.setAttribute4(resultVO.getWorkWay());
            Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, resultVO.getWorkcellId(), mtEoList.get(0).getEoId(), mtMaterialLot.getMaterialLotId(), reworkFlag, "SSN_PROCESS", resultVO.getOperationId());
            if (CollectionUtils.isEmpty(resultVO.getMaterialIdList())) {
                hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
                hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
                jobIdList.add(hmeEoJobSn.getJobId());
            } else {
                for (String materialId : resultVO.getMaterialIdList()) {
                    hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
                    hmeEoJobSn.setSnMaterialId(materialId);
                    hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
                    jobIdList.add(hmeEoJobSn.getJobId());
                    maxEoStepNum++;
                }
            }
            for (HmeSsnInspectResultVO2 hmeSsnInspectResultVO2 : resultVO2List) {
                List<String> JobRecordIdList = new ArrayList<>();
                for (String jobId : jobIdList) {
                    HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
                    hmeEoJobDataRecord.setTenantId(tenantId);
                    hmeEoJobDataRecord.setJobId(jobId);
                    hmeEoJobDataRecord.setWorkcellId(resultVO.getWorkcellId());
                    hmeEoJobDataRecord.setEoId(mtEoList.get(0).getEoId());
                    hmeEoJobDataRecord.setTagId(hmeSsnInspectResultVO2.getTagId());
                    hmeEoJobDataRecord.setGroupPurpose("SSN");
                    hmeEoJobDataRecord.setMaximalValue(hmeSsnInspectResultVO2.getMaximalValue());
                    hmeEoJobDataRecord.setMinimumValue(hmeSsnInspectResultVO2.getMinimumValue());
                    hmeEoJobDataRecordRepository.insertSelective(hmeEoJobDataRecord);

                    JobRecordIdList.add(hmeEoJobDataRecord.getJobRecordId());
                }
                // ?????? ????????????
                hmeSsnInspectResultVO2.setInspectResult("");
                hmeSsnInspectResultVO2.setJobRecordIdList(JobRecordIdList);
            }
        } else {
            // ????????????????????? ???????????? ??????????????????????????????
            if (CollectionUtils.isEmpty(resultVO.getMaterialIdList())) {
                if (hmeEoJobSnList.size() > 1) {
                    throw new MtException("HME_SSN_INSPECT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_009", "HME", resultVO.getStandardSnCode(), ""));
                }
            } else {
                if (Integer.valueOf(hmeEoJobSnList.size()).compareTo(resultVO.getMaterialIdList().size()) != 0) {
                    HmeEoJobSn hmeEoJobSn = hmeEoJobSnList.get(0);
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobSn.getSnMaterialId());
                    throw new MtException("HME_SSN_INSPECT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_009", "HME", resultVO.getStandardSnCode(), mtMaterial != null ? mtMaterial.getMaterialCode() : ""));
                }
            }
            // ???????????????????????????????????????jobId
            String materialId = StringUtils.isNotBlank(lastUpdateHeader.get(0).getMaterialId()) ? lastUpdateHeader.get(0).getMaterialId() : "";
            List<HmeEoJobSn> lastEoJobSnList = hmeEoJobSnList.stream().filter(ejs -> {
                String snMaterialId = StringUtils.isNotBlank(ejs.getSnMaterialId()) ? ejs.getSnMaterialId() : "";
                return StringUtils.equals(materialId, snMaterialId);
            }).collect(Collectors.toList());
            // ?????????????????? ??????????????? ???????????????????????????????????????
            List<HmeSsnInspectResultVO2> hmeSsnInspectResultVO2List = new ArrayList<>();
            List<String> tagList = resultVO2List.stream().map(HmeSsnInspectResultVO2::getTagId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tagList)) {
                List<HmeEoJobDataRecord> recordList = hmeEoJobDataRecordRepository.selectByCondition(Condition.builder(HmeEoJobDataRecord.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobDataRecord.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobDataRecord.FIELD_JOB_ID, hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList()))
                        .andIn(HmeEoJobDataRecord.FIELD_TAG_ID, tagList)).build());
                // ????????? ??????jobId ???????????????
                List<HmeEoJobDataRecord> lastRecordList = recordList.stream().filter(record -> StringUtils.equals(record.getJobId(), lastEoJobSnList.get(0).getJobId())).collect(Collectors.toList());
                for (HmeEoJobDataRecord hmeEoJobDataRecord : lastRecordList) {
                    Optional<HmeSsnInspectResultVO2> lineOpt = resultVO2List.stream().filter(dto -> StringUtils.equals(dto.getTagId(), hmeEoJobDataRecord.getTagId())).findFirst();
                    if (lineOpt.isPresent()) {
                        HmeSsnInspectResultVO2 resultVO2 = lineOpt.get();
                        resultVO2.setInspectResult(hmeEoJobDataRecord.getResult());
                        resultVO2.setJobRecordId(hmeEoJobDataRecord.getJobRecordId());
                        // ????????????????????? ???????????????jobRecord?????????
                        List<HmeEoJobDataRecord> filterList = recordList.stream().filter(record -> StringUtils.equals(record.getTagId(), hmeEoJobDataRecord.getTagId())).collect(Collectors.toList());
                        resultVO2.setJobRecordIdList(filterList.stream().map(HmeEoJobDataRecord::getJobRecordId).collect(Collectors.toList()));
                        hmeSsnInspectResultVO2List.add(resultVO2);
                    }
                }
            }
            resultVO2List = hmeSsnInspectResultVO2List.stream().sorted(Comparator.comparing(line -> line.getSequence())).collect(Collectors.toList());
        }
        return resultVO2List;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeSsnInspectResultVO2 saveSsnInspectResult(Long tenantId, HmeSsnInspectResultVO2 resultVO2) {
        if (CollectionUtils.isEmpty(resultVO2.getJobRecordIdList())) {
            throw new MtException("HME_SSN_INSPECT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_010", "HME"));
        }
        List<HmeEoJobDataRecord> recordList = hmeEoJobDataRecordRepository.selectByCondition(Condition.builder(HmeEoJobDataRecord.class).andWhere(Sqls.custom()
                .andEqualTo(HmeEoJobDataRecord.FIELD_TENANT_ID, tenantId)
                .andIn(HmeEoJobDataRecord.FIELD_JOB_RECORD_ID, resultVO2.getJobRecordIdList())).build());
        if (CollectionUtils.isEmpty(recordList)) {
            throw new MtException("HME_SSN_INSPECT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_010", "HME"));
        }
        for (HmeEoJobDataRecord hmeEoJobDataRecord : recordList) {
            hmeEoJobDataRecord.setResult(resultVO2.getInspectResult());
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(hmeEoJobDataRecord);
        }
        return resultVO2;
    }

    private void singleSubmit(Long tenantId, String materialId,Long userId,HmeSsnInspectResultVO3 resultVO3, String jobId) {
        // ?????????????????? ?????????????????????????????? ?????????????????? ?????????????????????????????????
        List<HmeSsnInspectResultVO2> inspectTagList = resultVO3.getInspectTagList();
        // ??????????????????
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
        // ????????????????????????
        List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.SSN_WORK_WAY", tenantId);
        if (StringUtils.isNotBlank(materialId)) {
            // ??????????????????????????????
            HmeSsnInspectResultVO resultVO = new HmeSsnInspectResultVO();
            BeanUtils.copyProperties(resultVO3, resultVO);
            resultVO.setMaterialIdList(Collections.singletonList(materialId));
            List<HmeSsnInspectHeader> hmeSsnInspectHeaderList = hmeSsnInspectResultHeaderMapper.querySsnInspectHeader(tenantId, resultVO);
            // ??????????????????????????????
            if (CollectionUtils.isEmpty(hmeSsnInspectHeaderList)) {
                // ??????????????????????????? ??????????????????
                Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO.getWorkWay())).findFirst();
                throw new MtException("HME_SSN_INSPECT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_001", "HME", mtMaterial.getMaterialCode(), resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
            Optional<HmeSsnInspectHeader> ssnInspectHeaderOpt = hmeSsnInspectHeaderList.stream().sorted(Comparator.comparing(HmeSsnInspectHeader::getLastUpdateDate).reversed()).findFirst();
            List<HmeSsnInspectResultVO2> resultVO2List = hmeSsnInspectResultHeaderMapper.querySsnInspectTag(tenantId, ssnInspectHeaderOpt.get().getSsnInspectHeaderId());
            if (CollectionUtils.isEmpty(resultVO2List)) {
                Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO.getWorkWay())).findFirst();
                throw new MtException("HME_SSN_INSPECT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_003", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
            // ??????????????????????????????  ????????????
            // ??????jobid???????????????????????????
            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.select(new HmeEoJobDataRecord() {{
                setTenantId(tenantId);
                setJobId(jobId);
            }});
            // ???hme_eo_job_data_record?????????????????? ??????????????????
            List<HmeSsnInspectResultVO2> returnResultList = new ArrayList<>();
            for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList) {
                Optional<HmeSsnInspectResultVO2> resultOpt = resultVO2List.stream().filter(vo2 -> StringUtils.equals(vo2.getTagId(), hmeEoJobDataRecord.getTagId())).findFirst();
                if (resultOpt.isPresent()) {
                    Optional<HmeSsnInspectResultVO2> tagOpt = resultVO3.getInspectTagList().stream().filter(tag -> StringUtils.equals(tag.getTagId(), hmeEoJobDataRecord.getTagId())).findFirst();
                    hmeEoJobDataRecord.setResult(tagOpt.isPresent() ? tagOpt.get().getInspectResult() : "");
                    HmeSsnInspectResultVO2 resultVO2 = resultOpt.get();
                    resultVO2.setInspectResult(tagOpt.isPresent() ? tagOpt.get().getInspectResult() : "");
                    returnResultList.add(resultVO2);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                hmeEoJobDataRecordMapper.batchUpdateResult(tenantId, userId, hmeEoJobDataRecordList);
            }
            inspectTagList = returnResultList;
        }

        // ?????????
        HmeSsnInspectResultHeader hmeSsnInspectResultHeader = new HmeSsnInspectResultHeader();
        hmeSsnInspectResultHeader.setTenantId(tenantId);
        hmeSsnInspectResultHeader.setStandardSnCode(resultVO3.getStandardSnCode());
        hmeSsnInspectResultHeader.setMaterialId(materialId);
        hmeSsnInspectResultHeader.setCosType(resultVO3.getCosType());
        hmeSsnInspectResultHeader.setWorkWay(resultVO3.getWorkWay());
        hmeSsnInspectResultHeader.setWorkcellId(resultVO3.getWorkcellId());
        // ???????????????OK???OK?????????NG
        Optional<HmeSsnInspectResultVO2> resultOpt = inspectTagList.stream().filter(vo3 -> {
            if (StringUtils.equals(HmeConstants.ConstantValue.YES, vo3.getJudgeFlag())) {
                BigDecimal inspectValue = BigDecimal.valueOf(Double.valueOf(vo3.getInspectResult()));
                // ???????????????Y ???????????????????????????????????????????????????????????????OK????????????NG
                // ??????????????????????????????????????????OK
                if (vo3.getMinimumValue() == null && vo3.getMaximalValue() == null) {
                    return false;
                } else if (inspectValue.compareTo(vo3.getMinimumValue()) >= 0 && inspectValue.compareTo(vo3.getMaximalValue()) <= 0) {
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }).findFirst();
        hmeSsnInspectResultHeader.setResult(resultOpt.isPresent() ? HmeConstants.ConstantValue.NG : HmeConstants.ConstantValue.OK);
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(resultVO3.getWkcShiftId());
        hmeSsnInspectResultHeader.setShiftCode(mtWkcShift.getShiftCode());
        LocalDate localDate = LocalDate.now();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        hmeSsnInspectResultHeader.setShiftDate(Date.from(zonedDateTime.toInstant()));
        self().insertSelective(hmeSsnInspectResultHeader);

        // ?????????
        for (HmeSsnInspectResultVO2 hmeSsnInspectResultVO2 : inspectTagList) {
            HmeSsnInspectResultLine hmeSsnInspectResultLine = new HmeSsnInspectResultLine();
            hmeSsnInspectResultLine.setTenantId(tenantId);
            hmeSsnInspectResultLine.setSsnInspectResultHeaderId(hmeSsnInspectResultHeader.getSsnInspectResultHeaderId());
            hmeSsnInspectResultLine.setSequence(Long.valueOf(hmeSsnInspectResultVO2.getSequence()));
            hmeSsnInspectResultLine.setTagId(hmeSsnInspectResultVO2.getTagId());
            hmeSsnInspectResultLine.setMinimumValue(hmeSsnInspectResultVO2.getMinimumValue());
            hmeSsnInspectResultLine.setMaximalValue(hmeSsnInspectResultVO2.getMaximalValue());
            BigDecimal inspectValue = BigDecimal.valueOf(Double.valueOf(hmeSsnInspectResultVO2.getInspectResult()));
            hmeSsnInspectResultLine.setInspectValue(inspectValue);
            if (StringUtils.equals(HmeConstants.ConstantValue.YES, hmeSsnInspectResultVO2.getJudgeFlag())) {
                // ???????????????Y ???????????????????????????????????????????????????????????????OK????????????NG
                if(hmeSsnInspectResultVO2.getMinimumValue() == null && hmeSsnInspectResultVO2.getMaximalValue() == null) {
                    // ??????????????????????????????????????????OK
                    hmeSsnInspectResultLine.setResult(HmeConstants.ConstantValue.OK);
                } else if (inspectValue.compareTo(hmeSsnInspectResultVO2.getMinimumValue()) >= 0 && inspectValue.compareTo(hmeSsnInspectResultVO2.getMaximalValue()) <= 0) {
                    hmeSsnInspectResultLine.setResult(HmeConstants.ConstantValue.OK);
                } else {
                    hmeSsnInspectResultLine.setResult(HmeConstants.ConstantValue.NG);
                }
            } else {
                // ?????? ??????OK
                hmeSsnInspectResultLine.setResult(HmeConstants.ConstantValue.OK);
            }
            hmeSsnInspectResultLineRepository.insertSelective(hmeSsnInspectResultLine);

            // COS????????????????????????????????????????????????Y
            if (HmeConstants.ConstantValue.YES.equals(hmeSsnInspectResultVO2.getCosCoupleFlag()) && HmeConstants.ConstantValue.YES.equals(hmeSsnInspectResultVO2.getCoupleFlag())) {
                Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO3.getWorkWay())).findFirst();
                throw new MtException("HME_SSN_INSPECT_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_012", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO3.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
            }
            if (HmeConstants.ConstantValue.YES.equals(hmeSsnInspectResultVO2.getCosCoupleFlag())) {
                // COS???????????????Y ??????COS???????????????????????????
                // ?????????????????????????????????
                List<HmeSsnInspectDetail> hmeSsnInspectDetailList = hmeSsnInspectDetailRepository.select(new HmeSsnInspectDetail() {{
                    setTenantId(tenantId);
                    setSsnInspectLineId(hmeSsnInspectResultVO2.getSsnInspectLineId());
                }});
                if (CollectionUtils.isEmpty(hmeSsnInspectDetailList)) {
                    Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO3.getWorkWay())).findFirst();
                    throw new MtException("HME_SSN_INSPECT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_005", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO3.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
                }
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setTableName("mt_tag_attr");
                    setKeyId(hmeSsnInspectResultVO2.getTagId());
                    setAttrName("TAG_TYPE");
                }});
                // ??????TAG_TYPE
                String tagType = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                List<String> tagGroupIdList = hmeSsnInspectDetailList.stream().map(HmeSsnInspectDetail::getTagGroupId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                    // ??????TAG_TYPE?????????????????? ?????????????????????????????????????????????
                    List<MtTagGroupAssign> tagGroupAssignList = hmeSsnInspectResultHeaderMapper.queryTagGroupAssign(tenantId, tagGroupIdList, hmeSsnInspectResultVO2.getCosPos(), tagType);
                    for (MtTagGroupAssign mtTagGroupAssign : tagGroupAssignList) {
                        // ??????????????? ????????????
                        BigDecimal allowDiffer = hmeSsnInspectResultVO2.getAllowDiffer() == null ? BigDecimal.ZERO : hmeSsnInspectResultVO2.getAllowDiffer();
                        BigDecimal minValue = inspectValue != null ? inspectValue.subtract(allowDiffer) : BigDecimal.ZERO.subtract(allowDiffer);
                        mtTagGroupAssign.setMinimumValue(minValue.doubleValue());
                        BigDecimal maxValue = inspectValue != null ? inspectValue.add(allowDiffer) : BigDecimal.ZERO.add(allowDiffer);
                        mtTagGroupAssign.setMaximalValue(maxValue.doubleValue());
                        mtTagGroupAssignMapper.updateByPrimaryKeySelective(mtTagGroupAssign);
                    }
                }
            }

            // ???????????????????????????
            if (StringUtils.equals(HmeConstants.ConstantValue.YES, hmeSsnInspectResultVO2.getCoupleFlag())) {
                // ????????????????????????Y ????????????
                // ?????????????????????????????????
                List<HmeSsnInspectDetail> hmeSsnInspectDetailList = hmeSsnInspectDetailRepository.select(new HmeSsnInspectDetail() {{
                    setTenantId(tenantId);
                    setSsnInspectLineId(hmeSsnInspectResultVO2.getSsnInspectLineId());
                }});
                if (CollectionUtils.isEmpty(hmeSsnInspectDetailList)) {
                    Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), resultVO3.getWorkWay())).findFirst();
                    throw new MtException("HME_SSN_INSPECT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_005", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO3.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
                }
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setTableName("mt_tag_attr");
                    setKeyId(hmeSsnInspectResultVO2.getTagId());
                    setAttrName("TAG_TYPE");
                }});
                // ??????TAG_TYPE
                String tagType = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                List<String> tagGroupIdList = hmeSsnInspectDetailList.stream().map(HmeSsnInspectDetail::getTagGroupId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                    // ??????TAG_TYPE?????????????????? ?????????????????????????????????????????????
                    List<MtTagGroupAssign> tagGroupAssignList = hmeSsnInspectResultHeaderMapper.queryTagGroupAssign(tenantId, tagGroupIdList, null, tagType);
                    for (MtTagGroupAssign mtTagGroupAssign : tagGroupAssignList) {
                        // ??????????????? ????????????
                        BigDecimal allowDiffer = hmeSsnInspectResultVO2.getAllowDiffer() == null ? BigDecimal.ZERO : hmeSsnInspectResultVO2.getAllowDiffer();
                        BigDecimal minValue = inspectValue != null ? inspectValue.subtract(allowDiffer) : BigDecimal.ZERO.subtract(allowDiffer);
                        mtTagGroupAssign.setMinimumValue(minValue.doubleValue());
                        BigDecimal maxValue = inspectValue != null ? inspectValue.add(allowDiffer) : BigDecimal.ZERO.add(allowDiffer);
                        mtTagGroupAssign.setMaximalValue(maxValue.doubleValue());
                        mtTagGroupAssignMapper.updateByPrimaryKeySelective(mtTagGroupAssign);
                    }
                }
            }

            // ??????????????????2???????????????????????????3????????????
            if (StringUtils.equals("2", resultVO3.getWorkWay())) {
                // ??????????????????????????????
                Sqls custom = Sqls.custom();
                custom.andEqualTo(HmeSsnInspectHeader.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeSsnInspectHeader.FIELD_STANDARD_SN_CODE, resultVO3.getStandardSnCode())
                        .andEqualTo(HmeSsnInspectHeader.FIELD_WORKCELL_ID, resultVO3.getWorkcellId())
                        .andEqualTo(HmeSsnInspectHeader.FIELD_WORK_WAY, "3")
                        .andEqualTo(HmeSsnInspectHeader.FIELD_MATERIAL_ID, materialId);

                if (StringUtils.isNotBlank(resultVO3.getCosType())) {
                    custom.andEqualTo(HmeSsnInspectHeader.FIELD_COS_TYPE, resultVO3.getCosType());
                } else {
                    custom.andIsNull(HmeSsnInspectHeader.FIELD_COS_TYPE);
                }
                List<HmeSsnInspectHeader> hmeSsnInspectHeaderList = hmeSsnInspectHeaderRepository.selectByCondition(Condition.builder(HmeSsnInspectHeader.class).andWhere(custom).build());
                if (CollectionUtils.isNotEmpty(hmeSsnInspectHeaderList)) {
                    List<HmeSsnInspectResultVO2> resultVO2List = hmeSsnInspectResultHeaderMapper.querySsnInspectTag(tenantId, hmeSsnInspectHeaderList.get(0).getSsnInspectHeaderId());
                    if (CollectionUtils.isEmpty(resultVO2List)) {
                        Optional<LovValueDTO> lovOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "3")).findFirst();
                        throw new MtException("HME_SSN_INSPECT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_003", "HME", mtMaterial != null ? mtMaterial.getMaterialCode() : "", resultVO3.getCosType(), lovOpt.isPresent() ? lovOpt.get().getMeaning() : ""));
                    }
                    List<String> tagIdList = resultVO2List.stream().map(HmeSsnInspectResultVO2::getTagId).distinct().collect(Collectors.toList());
                    MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_tag_attr");
                    List<MtExtendVO5> attrList = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("TAG_TYPE");
                    attrList.add(mtExtendVO5);
                    mtExtendVO1.setAttrs(attrList);
                    mtExtendVO1.setKeyIdList(tagIdList);
                    List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    List<MtTagGroupAssign> tagGroupAssignList = new ArrayList<>();
                    for (HmeSsnInspectResultVO2 ssnInspectResultVO2 : resultVO2List) {
                        if (StringUtils.equals(ssnInspectResultVO2.getTagId(), hmeSsnInspectResultVO2.getTagId())) {
                            Optional<MtExtendAttrVO1> tagTypeOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getKeyId(), ssnInspectResultVO2.getTagId()) && StringUtils.equals(attr.getAttrName(), "TAG_TYPE")).findFirst();
                            // ?????????????????????????????????
                            List<HmeSsnInspectDetail> hmeSsnInspectDetailList = hmeSsnInspectDetailRepository.select(new HmeSsnInspectDetail() {{
                                setTenantId(tenantId);
                                setSsnInspectLineId(ssnInspectResultVO2.getSsnInspectLineId());
                            }});
                            BigDecimal allowDiffer = hmeSsnInspectResultVO2.getCheckAllowDiffer() == null ? BigDecimal.ZERO : hmeSsnInspectResultVO2.getCheckAllowDiffer();
                            BigDecimal minValue = inspectValue != null ? inspectValue.subtract(allowDiffer) : BigDecimal.ZERO.subtract(allowDiffer);
                            BigDecimal maxValue = inspectValue != null ? inspectValue.add(allowDiffer) : BigDecimal.ZERO.add(allowDiffer);

                            if (CollectionUtils.isNotEmpty(hmeSsnInspectDetailList)) {
                                List<String> tagGroupIdList = hmeSsnInspectDetailList.stream().map(HmeSsnInspectDetail::getTagGroupId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                                    List<MtTagGroupAssign> assignList = new ArrayList<>();
                                    if (HmeConstants.ConstantValue.YES.equals(ssnInspectResultVO2.getCosCoupleFlag())) {
                                        assignList = hmeSsnInspectResultHeaderMapper.queryTagGroupAssign(tenantId, tagGroupIdList, ssnInspectResultVO2.getCosPos(), tagTypeOpt.get().getAttrValue());
                                    } else if (HmeConstants.ConstantValue.YES.equals(ssnInspectResultVO2.getCoupleFlag())) {
                                        assignList = hmeSsnInspectResultHeaderMapper.queryTagGroupAssign(tenantId, tagGroupIdList, null, tagTypeOpt.get().getAttrValue());
                                    }
                                    if (CollectionUtils.isNotEmpty(assignList)) {
                                        for (MtTagGroupAssign mtTagGroupAssign : assignList) {
                                            // ??????????????? ????????????
                                            mtTagGroupAssign.setMinimumValue(minValue.doubleValue());
                                            mtTagGroupAssign.setMaximalValue(maxValue.doubleValue());
                                        }
                                        tagGroupAssignList.addAll(assignList);
                                    }
                                }
                            }
                            // ??????????????????3???????????????
                            HmeSsnInspectLine hmeSsnInspectLine = new HmeSsnInspectLine();
                            hmeSsnInspectLine.setSsnInspectLineId(ssnInspectResultVO2.getSsnInspectLineId());
                            hmeSsnInspectLine.setMinimumValue(minValue);
                            hmeSsnInspectLine.setMaximalValue(maxValue);
                            hmeSsnInspectLineMapper.updateByPrimaryKeySelective(hmeSsnInspectLine);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(tagGroupAssignList)) {
                        hmeSsnInspectResultHeaderMapper.myBatchUpdate(tenantId, userId, tagGroupAssignList);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ssnInspectResultSubmit(Long tenantId, HmeSsnInspectResultVO3 resultVO3) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        // ??????????????? ???????????????
        Optional<HmeSsnInspectResultVO2> resultOpt = resultVO3.getInspectTagList().stream().filter(vo3 -> StringUtils.isBlank(vo3.getInspectResult())).findFirst();
        if (resultOpt.isPresent()) {
            throw new MtException("HME_SSN_INSPECT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_004", "HME"));
        }
        // ????????????????????? ??????
        List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtEo.FIELD_IDENTIFICATION, resultVO3.getStandardSnCode())).build());
        if (CollectionUtils.isEmpty(mtEoList)) {
            throw new MtException("HME_SSN_INSPECT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_008", "HME", resultVO3.getStandardSnCode()));
        }
        // ?????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(resultVO3.getStandardSnCode());
        }});
        if (mtMaterialLot == null) {
            throw new MtException("HME_SSN_INSPECT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SSN_INSPECT_007", "HME", resultVO3.getStandardSnCode()));
        }
        // ??????????????????
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_material_lot_attr");
            setKeyId(mtMaterialLot.getMaterialLotId());
            setAttrName("REWORK_FLAG");
        }});
        String reworkFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : HmeConstants.ConstantValue.NO;
        HmeSsnInspectResultVO resultVO = new HmeSsnInspectResultVO();
        BeanUtils.copyProperties(resultVO3, resultVO);
        List<HmeEoJobSn> hmeEoJobSnList = hmeSsnInspectResultHeaderMapper.querySiteInList(tenantId, resultVO, mtEoList.get(0).getEoId(), reworkFlag);
        for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
            hmeEoJobSn.setSiteOutDate(new Date());
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
        }
        // ????????????
        if (CollectionUtils.isNotEmpty(resultVO3.getMaterialIdList())) {
            for (String materialId : resultVO3.getMaterialIdList()) {
                // ????????????????????????id ??????????????????
                Optional<HmeEoJobSn> eoJobSnOpt = hmeEoJobSnList.stream().filter(ejs -> StringUtils.equals(ejs.getSnMaterialId(), materialId)).findFirst();
                singleSubmit(tenantId, materialId, userId, resultVO3, eoJobSnOpt.get().getJobId());
            }
        } else {
            singleSubmit(tenantId, null, userId, resultVO3, "");
        }
    }
}
