package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeEoTestDataRecordMapper;
import com.ruike.hme.infra.mapper.HmeTagMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.FieldNameUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import tarzan.actual.domain.repository.MtDataRecordRepository;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * ??????????????????-???????????? ???????????????
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
@Component
@Slf4j
public class HmeEoJobDataRecordRepositoryImpl extends BaseRepositoryImpl<HmeEoJobDataRecord> implements HmeEoJobDataRecordRepository {

    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtDataRecordRepository mtDataRecordRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeTagDaqAttrRepository hmeTagDaqAttrRepository;
    @Autowired
    private HmeTagMapper hmeTagMapper;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;
    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private HmeEoTestDataRecordRepository hmeEoTestDataRecordRepository;
    @Autowired
    private HmeEoTestDataRecordMapper hmeEoTestDataRecordMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeTagFormulaHeadRepository hmeTagFormulaHeadRepository;

    @Autowired
    private HmeTagFormulaLineRepository hmeTagFormulaLineRepository;

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SYMBOL = "#";

    private static String fetchGroupKey3(String str1, String str2, String str3) {
        return str1 + SYMBOL + str2 + SYMBOL + str3;
    }

    private static String fetchGroupKey4(String str1, String str2, String str3, String str4) {
        return str1 + SYMBOL + str2 + SYMBOL + str3 + SYMBOL + str4;
    }

    /**
     * ????????????
     *
     * @param sqlList  ?????????
     * @param splitNum ????????????
     * @return ????????????
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, int splitNum) {

        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobDataRecordVO> inSiteScan(Long tenantId, HmeEoJobSnVO2 dto) {
        Long startDate = System.currentTimeMillis();
        //??????????????????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //V20200813 modify by penglin.sui for zhenyong.ban ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // 0.?????? + ?????? + ???????????????LAB??????1.??????+??????+?????????2.??????+??????+???????????????3.??????+?????????+???????????????4.??????+???????????????+????????????
        //V20201106 modify by penglin.sui for zhenyong.ban DATA???GENERAL??????????????????????????????????????????
        //V20211126 modify by penglin.sui for hui.ma ??????LAB??????????????????????????????DATA??????
        List<String> businessTypeList = new ArrayList<>();
        List<HmeTagVO> hmeTagVOList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getLabCode())) {
            businessTypeList.add("LAB");
            startDate = System.currentTimeMillis();
            hmeTagVOList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn2(tenantId, dto.getOperationId(), dto.getSnMaterialId(), businessTypeList , dto.getLabCode());
            log.info("=================================>inSiteScan-LabQuery????????????"+(System.currentTimeMillis() - startDate)+ "ms");
        }
        if(CollectionUtils.isEmpty(hmeTagVOList)) {
            businessTypeList.clear();
            businessTypeList.add("DATA");
            startDate = System.currentTimeMillis();
            hmeTagVOList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getSnMaterialId(), dto.getProductionVersion(), businessTypeList);
            log.info("=================================>inSiteScan-DATA-1????????????"+(System.currentTimeMillis() - startDate)+ "ms");
            if (CollectionUtils.isEmpty(hmeTagVOList)) {
                //2
                startDate = System.currentTimeMillis();
                hmeTagVOList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getSnMaterialId(), "", businessTypeList);
                log.info("=================================>inSiteScan-DATA-2????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                if (CollectionUtils.isEmpty(hmeTagVOList)) {
                    //3
                    startDate = System.currentTimeMillis();
                    hmeTagVOList = hmeTagMapper.operationItemTypeLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getItemType(), businessTypeList);
                    log.info("=================================>inSiteScan-DATA-3????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                    if (CollectionUtils.isEmpty(hmeTagVOList)) {
                        //4
                        startDate = System.currentTimeMillis();
                        hmeTagVOList = hmeTagMapper.operationLimitForEoJobSn(tenantId, dto.getOperationId(), businessTypeList);
                        log.info("=================================>inSiteScan-DATA-4????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                    }
                }
            }
        }

        List<String> businessTypeList2 = new ArrayList<>();
        businessTypeList2.add("GENERAL");
        startDate = System.currentTimeMillis();
        List<HmeTagVO> hmeTagVOList2 = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getSnMaterialId(), dto.getProductionVersion(), businessTypeList2);
        log.info("=================================>inSiteScan-GENERAL-1????????????"+(System.currentTimeMillis() - startDate)+ "ms");
        if (CollectionUtils.isEmpty(hmeTagVOList2)) {
            //2
            startDate = System.currentTimeMillis();
            hmeTagVOList2 = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getSnMaterialId(), "", businessTypeList2);
            log.info("=================================>inSiteScan-GENERAL-2????????????"+(System.currentTimeMillis() - startDate)+ "ms");
            if (CollectionUtils.isEmpty(hmeTagVOList2)) {
                //3
                startDate = System.currentTimeMillis();
                hmeTagVOList2 = hmeTagMapper.operationItemTypeLimitForEoJobSn(tenantId, dto.getOperationId(), dto.getItemType(), businessTypeList2);
                log.info("=================================>inSiteScan-GENERAL-3????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                if (CollectionUtils.isEmpty(hmeTagVOList2)) {
                    //4
                    startDate = System.currentTimeMillis();
                    hmeTagVOList2 = hmeTagMapper.operationLimitForEoJobSn(tenantId, dto.getOperationId(), businessTypeList2);
                    log.info("=================================>inSiteScan-GENERAL-4????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                }
            }
        }

        if (CollectionUtils.isNotEmpty(hmeTagVOList2)) {
            hmeTagVOList.addAll(hmeTagVOList2);
        }

        hmeTagVOList = hmeTagVOList.stream().sorted(Comparator.comparing(HmeTagVO::getValueType)).collect(Collectors.toList());
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<HmeEoJobDataRecord>();
        List<String> ids = this.customDbRepository.getNextKeys("hme_eo_job_data_record_s", hmeTagVOList.size());
        List<String> cIds = this.customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", hmeTagVOList.size());
        int count = 0;
        for (HmeTagVO tagVO : hmeTagVOList) {
            HmeEoJobDataRecord hmeEoJobDataRecord = HmeEoJobDataRecord.builder()
                    .tenantId(tenantId)
                    .jobRecordId(ids.get(count))
                    .jobId(dto.getJobId())
                    .workcellId(dto.getWorkcellId())
                    .eoId(dto.getEoId())
                    .tagGroupId(tagVO.getTagGroupId())
                    .tagId(tagVO.getTagId())
                    .minimumValue(tagVO.getMinimumValue())
                    .maximalValue(tagVO.getMaximalValue())
                    .groupPurpose(tagVO.getGroupPurpose())
                    .result("")
                    .dataRecordId("")
                    .isSupplement("0")
                    .cid(Long.valueOf(cIds.get(count)))
                    .build();
            hmeEoJobDataRecord.setObjectVersionNumber(1L);
            hmeEoJobDataRecord.setCreationDate(new Date());
            hmeEoJobDataRecord.setCreatedBy(userId);
            hmeEoJobDataRecord.setLastUpdatedBy(userId);
            hmeEoJobDataRecord.setLastUpdateDate(new Date());
            hmeEoJobDataRecordList.add(hmeEoJobDataRecord);
            count++;
        }
        //??????????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
            List<List<HmeEoJobDataRecord>> splitSqlList = splitSqlList(hmeEoJobDataRecordList, 200);
            startDate = System.currentTimeMillis();
            for (List<HmeEoJobDataRecord> domains : splitSqlList) {
                hmeEoJobDataRecordMapper.batchInsertDataRecord("hme_eo_job_data_record", domains);
            }
            log.info("=================================>inSiteScan-??????????????????????????????"+(System.currentTimeMillis() - startDate)+ "ms");
            //???????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.10.21
            //V20201209 modify by penglin.sui ?????????????????????????????????????????????????????????SN???????????????
            if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                    || !HmeConstants.ConstantValue.YES.equals(dto.getBatchProcessSnScanFlag())
                    || (HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(dto.getCodeType()))) {
                List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(ids)){
                    startDate = System.currentTimeMillis();
                    hmeEoJobDataRecordVOList = hmeEoJobDataRecordMapper.queryEoJobDataRecord(tenantId, "", "", ids);
                    log.info("=================================>inSiteScan-queryEoJobDataRecord????????????"+(System.currentTimeMillis() - startDate)+ "ms");
                }
                return hmeEoJobDataRecordVOList;
            } else {
                return new ArrayList<HmeEoJobDataRecordVO>();
            }
        }
        log.info("=================================>inSiteScan????????????"+(System.currentTimeMillis() - startDate)+ "ms");
        return new ArrayList<HmeEoJobDataRecordVO>();
    }

    @Override
    public List<HmeEoJobDataRecordVO> eoJobDataRecordQuery(Long tenantId, HmeEoJobMaterialVO2 dto) {
//        HmeEoJobDataRecord jobDataRecord = new HmeEoJobDataRecord();
//        jobDataRecord.setTenantId(tenantId);
//        jobDataRecord.setWorkcellId(dto.getWorkcellId());
//        jobDataRecord.setJobId(dto.getJobId());
//        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = this.select(jobDataRecord);
//        if(hmeEoJobDataRecordList != null){
//            if(hmeEoJobDataRecordList.size() > 0){
//                hmeEoJobDataRecordList.forEach(t -> {
//                        t.setIsSupplement("1".equals(t.getIsSupplement()) ? "1" : "0");
//                });
//            }
//        }

        if(StringUtils.isBlank(dto.getJobId())){
            //?????????????????????,???????????????????????????
            throw new MtException("HME_EO_JOB_SN_194", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_194", "HME"));
        }
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getJobId())) {

            //????????????????????????jobId
            if(dto.getJobId().indexOf(",") >= 0){
                List<String> jobIdList = Arrays.asList(dto.getJobId().split(","));
                List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList2 = hmeEoJobDataRecordMapper.queryEoJobDataRecord2(tenantId, dto.getWorkcellId(), jobIdList);

                //??????????????????????????????????????????????????????
                if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList2)){
                    log.debug("<========????????????????????? begin========>");
                    Map<String, List<HmeEoJobDataRecordVO>> collect = hmeEoJobDataRecordVOList2.stream().collect(Collectors.groupingBy(HmeEoJobDataRecordVO::getTagId));
                    log.debug("<========????????????????????? end========>");
                    for(Map.Entry<String, List<HmeEoJobDataRecordVO>> entry : collect.entrySet()){
                        if (entry.getValue().size() == jobIdList.size()){
                            HmeEoJobDataRecordVO hmeEoJobDataRecordVO = entry.getValue().get(0);
                            for (HmeEoJobDataRecordVO a : entry.getValue()
                                 ) {
                                if (!StringUtils.equals(a.getResult(), hmeEoJobDataRecordVO.getResult())){
                                    hmeEoJobDataRecordVO.setResult("");
                                    break;
                                }
                            }
                            hmeEoJobDataRecordVOList.add(hmeEoJobDataRecordVO);
                        }
                    }
                    log.debug("<========??????????????????????????? end========>");
                    //????????????map????????????????????????????????????
//                    Map<String, Integer> map = new HashMap<>();
//                    for(HmeEoJobDataRecordVO hmeEoJobDataRecordVO : hmeEoJobDataRecordVOList2){
//                        if(map.containsKey(hmeEoJobDataRecordVO.getTagId())){
//                            //?????????????????????????????????????????????????????????1
//                            map.put(hmeEoJobDataRecordVO.getTagId(), map.get(hmeEoJobDataRecordVO.getTagId()).intValue() + 1);
//                        }else{
//                            map.put(hmeEoJobDataRecordVO.getTagId(), 1);
//                        }
//                    }
//
//                    for(Map.Entry<String, Integer> entry : map.entrySet()){
//                        if(entry.getValue() == jobIdList.size()){
//                            List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList3 = hmeEoJobDataRecordVOList2.stream().filter(item -> item.getTagId().equals(entry.getKey()))
//                                    .collect(Collectors.toList());
//                            String result = Strings.EMPTY;
//                            HmeEoJobDataRecordVO hmeEoJobDataRecordVO = hmeEoJobDataRecordVOList3.get(0);
//                            for(int i = 0 ; i < hmeEoJobDataRecordVOList3.size() ; i++){
//                                hmeEoJobDataRecordVO = hmeEoJobDataRecordVOList3.get(i);
//                                if(i == 0){
//                                    result = hmeEoJobDataRecordVO.getResult();
//                                }else {
//                                    if (!result.equals(hmeEoJobDataRecordVO.getResult())){
//                                        hmeEoJobDataRecordVO.setResult("");
//                                        break;
//                                    }
//                                }
//                            }
//                            hmeEoJobDataRecordVOList.add(hmeEoJobDataRecordVO);
//                        }
//                    }
                }
            }else{
                hmeEoJobDataRecordVOList = hmeEoJobDataRecordMapper.queryEoJobDataRecord(tenantId, dto.getWorkcellId(), dto.getJobId(), null);
            }
        }
//        for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList) {
//            HmeEoJobDataRecordVO jobDataRecordVO = new HmeEoJobDataRecordVO();
//            jobDataRecordVO.setIsSupplement(hmeEoJobDataRecord.getIsSupplement());
//            MtTag mtTag = mtTagRepository.tagGet(tenantId, hmeEoJobDataRecord.getTagId());
//            jobDataRecordVO.setResultType(mtTag.getValueType());
//            jobDataRecordVO.setTagGroupId(hmeEoJobDataRecord.getTagGroupId());
//            jobDataRecordVO.setTagId(mtTag.getTagId());
//            jobDataRecordVO.setTagCode(mtTag.getTagCode());
//            jobDataRecordVO.setTagDescription(mtTag.getTagDescription());
//
//            MtExtendSettings extendAttr = new MtExtendSettings();
//            extendAttr.setAttrName("TAG_TYPE");
//            List<MtExtendAttrVO> tagTypeAttr = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
//                    "mt_tag_attr", "TAG_ID", mtTag.getTagId(),
//                    Collections.singletonList(extendAttr));
//            if (CollectionUtils.isNotEmpty(tagTypeAttr)) {
//                jobDataRecordVO.setTagType(tagTypeAttr.get(0).getAttrValue());
//            }
//
//            // ??????????????????????????????
//            HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
//            hmeTagDaqAttrParam.setTenantId(tenantId);
//            hmeTagDaqAttrParam.setTagId(mtTag.getTagId());
//            HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
//            if (Objects.nonNull(hmeTagDaqAttr)) {
//                jobDataRecordVO.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
//                jobDataRecordVO.setValueField(hmeTagDaqAttr.getValueField());
//                jobDataRecordVO.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
//                jobDataRecordVO.setCond1Value(hmeTagDaqAttr.getCond1Value());
//                jobDataRecordVO.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
//                jobDataRecordVO.setCond2Value(hmeTagDaqAttr.getCond2Value());
//            }
//
//            MtTagGroupAssign mtTagGroupAssignParam = new MtTagGroupAssign();
//            mtTagGroupAssignParam.setTenantId(tenantId);
//            mtTagGroupAssignParam.setTagId(mtTag.getTagId());
//            mtTagGroupAssignParam.setTagGroupId(hmeEoJobDataRecord.getTagGroupId());
//            MtTagGroupAssign mtTagGroupAssign = mtTagGroupAssignRepository.selectOne(mtTagGroupAssignParam);
//            if (Objects.nonNull(mtTagGroupAssign)) {
//                jobDataRecordVO.setSerialNumber(mtTagGroupAssign.getSerialNumber());
//                jobDataRecordVO.setValueAllowMissing(mtTagGroupAssign.getValueAllowMissing());
//            }
//            else{
//                jobDataRecordVO.setSerialNumber(-99d);
//            }
//
//            //????????????
//            MtUom mtUomPara = new MtUom();
//            mtUomPara.setUomId(mtTag.getUnit());
//            MtUom mtUom = mtUomRepository.selectOne(mtUomPara);
//            if(Objects.nonNull(mtUom)){
//                jobDataRecordVO.setUomCode(mtUom.getUomCode());
//                jobDataRecordVO.setUomName(mtUom.getUomName());
//            }
//
//            BeanUtils.copyProperties(hmeEoJobDataRecord, jobDataRecordVO);
//            hmeEoJobDataRecordVOList.add(jobDataRecordVO);
//        }
//
//        hmeEoJobDataRecordVOList = hmeEoJobDataRecordVOList.stream()
//                .sorted(Comparator.comparing(HmeEoJobDataRecordVO::getIsSupplement)
//                        .thenComparing(HmeEoJobDataRecordVO::getResultType)
//                        .thenComparing(HmeEoJobDataRecordVO::getSerialNumber)).collect(Collectors.toList());

        return hmeEoJobDataRecordVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobDataRecordVO materialScan(Long tenantId, HmeEoJobDataRecordVO dto) {
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<>();
        Map<String,HmeEoJobSn> hmeEoJobSnMap = new HashMap<>();
        if (HmeConstants.ConstantValue.YES.equals(dto.getBatchFlag())) {
//            for (String jobId : dto.getJobIdList()) {
//                List<HmeEoJobDataRecord> oneJobDataRecordList = this.selectByCondition(Condition.builder(HmeEoJobDataRecord.class)
//                        .andWhere(Sqls.custom()
//                                .andEqualTo(HmeEoJobDataRecord.FIELD_TENANT_ID, tenantId)
//                                .andEqualTo(HmeEoJobDataRecord.FIELD_WORKCELL_ID, dto.getWorkcellId())
//                                .andEqualTo(HmeEoJobDataRecord.FIELD_JOB_ID, jobId)
//                                .andEqualTo(HmeEoJobDataRecord.FIELD_TAG_GROUP_ID, dto.getTagGroupId())
//                                .andEqualTo(HmeEoJobDataRecord.FIELD_TAG_ID, dto.getTagId()))
//                        .build());
//                hmeEoJobDataRecordList.addAll(oneJobDataRecordList);
//            }
            //V20210430 modify by penglin.sui ?????????????????????
            hmeEoJobDataRecordList = this.selectByCondition(Condition.builder(HmeEoJobDataRecord.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeEoJobDataRecord.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobDataRecord.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andIn(HmeEoJobDataRecord.FIELD_JOB_ID, dto.getJobIdList())
                            .andEqualTo(HmeEoJobDataRecord.FIELD_TAG_GROUP_ID, dto.getTagGroupId())
                            .andEqualTo(HmeEoJobDataRecord.FIELD_TAG_ID, dto.getTagId()))
                    .build());

            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom()
                            .andIn(HmeEoJobDataRecord.FIELD_JOB_ID, dto.getJobIdList()))
                    .build());
            hmeEoJobSnMap = hmeEoJobSnList.stream().collect(Collectors.toMap(item -> item.getJobId(), t -> t));
        } else {
            HmeEoJobDataRecord param = new HmeEoJobDataRecord();
            param.setJobRecordId(dto.getJobRecordId());
            param.setTenantId(tenantId);
            HmeEoJobDataRecord hmeEoJobDataRecord = this.selectByPrimaryKey(param);
            hmeEoJobDataRecordList.add(hmeEoJobDataRecord);

            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(hmeEoJobDataRecord.getJobId());
            hmeEoJobSnMap.put(hmeEoJobSn.getJobId() , hmeEoJobSn);
        }
        // ????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_TAG_RECORD_CREATE");
        // ????????????
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<HmeEoTestDataRecord> hmeEoTestDataRecordList = new ArrayList<>();
        List<String> updateEoJobDataRecordSqlList = new ArrayList<>();
        List<String> cidS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)){
            cidS = customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", hmeEoJobDataRecordList.size());
        }
        int count = 0;
        for (HmeEoJobDataRecord dataRecord : hmeEoJobDataRecordList) {

            /* ?????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.21
            MtDataRecordVO mtDataRecordVO = new MtDataRecordVO();
            mtDataRecordVO.setEoId(dataRecord.getEoId());
            mtDataRecordVO.setWorkcellId(dataRecord.getWorkcellId());
            mtDataRecordVO.setTagGroupId(dataRecord.getTagGroupId());
            mtDataRecordVO.setTagId(dataRecord.getTagId());
            mtDataRecordVO.setTagValue(dto.getResult());
            mtDataRecordVO.setEventId(eventId);

            MtTagGroupObject tagGroupObjectParam = new MtTagGroupObject();
            tagGroupObjectParam.setTenantId(tenantId);
            tagGroupObjectParam.setTagGroupId(dataRecord.getTagGroupId());
            List<MtTagGroupObject> mtTagGroupObjects = mtTagGroupObjectRepository.select(tagGroupObjectParam);
            if (CollectionUtils.isNotEmpty(mtTagGroupObjects)) {
                MtTagGroupObject mtTagGroupObject = mtTagGroupObjects.get(0);
                mtDataRecordVO.setOperationId(mtTagGroupObject.getOperationId());
            }
            HmeRouterStepVO nearStepVO =
                    hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dataRecord.getEoId());
            if (Objects.nonNull(nearStepVO)) {
                mtDataRecordVO.setEoStepActualId(nearStepVO.getEoStepActualId());
            }
            MtDataRecordVO6 mtDataRecordVo6 = mtDataRecordRepository.dataRecordAndHisCreate(tenantId, mtDataRecordVO);

            dataRecord.setDataRecordId(mtDataRecordVo6.getDataRecordId());*/
            dataRecord.setResult(dto.getResult());
            dataRecord.setRemark(dto.getRemark());
            dataRecord.setLastUpdatedBy(userId);
            dataRecord.setLastUpdateDate(CommonUtils.currentTimeGet());
            dataRecord.setCid(Long.valueOf(cidS.get(count++)));
            updateEoJobDataRecordSqlList.addAll(customDbRepository.getUpdateSql(dataRecord));
//            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(dataRecord);
            BeanUtils.copyProperties(dataRecord, dto);

            // ??????????????????????????????
//            HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
//            hmeTagDaqAttrParam.setTenantId(tenantId);
//            hmeTagDaqAttrParam.setTagId(dataRecord.getTagId());
//            HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
//            if (Objects.nonNull(hmeTagDaqAttr)) {
//                dto.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
//                dto.setValueField(hmeTagDaqAttr.getValueField());
//                dto.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
//                dto.setCond1Value(hmeTagDaqAttr.getCond1Value());
//                dto.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
//                dto.setCond2Value(hmeTagDaqAttr.getCond2Value());
//            }

            //V20200920 modify by penglin.sui for tianyan.xie ????????????hme_eo_test_data_record?????????
            HmeEoTestDataRecord hmeEoTestDataRecordPara = new HmeEoTestDataRecord();
            hmeEoTestDataRecordPara.setTenantId(tenantId);
            hmeEoTestDataRecordPara.setEoId(dto.getEoId());
            hmeEoTestDataRecordPara.setMaterialLotId(hmeEoJobSnMap.get(dataRecord.getJobId()).getMaterialLotId());
            hmeEoTestDataRecordPara.setOperationId(dto.getOperationId());
            hmeEoTestDataRecordPara.setTagGroupId(dataRecord.getTagGroupId());
            hmeEoTestDataRecordPara.setTagId(dataRecord.getTagId());
            hmeEoTestDataRecordPara.setReworkFlag(StringUtils.isBlank(dto.getReworkFlag()) ? HmeConstants.ConstantValue.NO : dto.getReworkFlag());
            hmeEoTestDataRecordPara.setMinValue(dataRecord.getMinimumValue());
            hmeEoTestDataRecordPara.setMaxValue(dataRecord.getMaximalValue());
            hmeEoTestDataRecordPara.setResult(dto.getResult());
            hmeEoTestDataRecordList.add(hmeEoTestDataRecordPara);
//            hmeEoTestDataRecordRepository.saveTestDataRecord(tenantId, hmeEoTestDataRecordPara);
        }

        if (CollectionUtils.isNotEmpty(updateEoJobDataRecordSqlList)) {
            jdbcTemplate.batchUpdate(updateEoJobDataRecordSqlList.toArray(new String[updateEoJobDataRecordSqlList.size()]));

            // ??????????????????????????????
            HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
            hmeTagDaqAttrParam.setTenantId(tenantId);
            hmeTagDaqAttrParam.setTagId(hmeEoJobDataRecordList.get(hmeEoJobDataRecordList.size() - 1).getTagId());
            HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
            if (Objects.nonNull(hmeTagDaqAttr)) {
                dto.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
                dto.setValueField(hmeTagDaqAttr.getValueField());
                dto.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
                dto.setCond1Value(hmeTagDaqAttr.getCond1Value());
                dto.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
                dto.setCond2Value(hmeTagDaqAttr.getCond2Value());
            }

            hmeEoTestDataRecordRepository.batchSaveTestDataRecord(tenantId, hmeEoTestDataRecordList);
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobDataRecordVO> batchSave(Long tenantId, List<HmeEoJobDataRecordVO> dtoList) {
        List<HmeEoJobDataRecord> insertDataRecordList = new ArrayList<HmeEoJobDataRecord>();
        //List<HmeEoJobDataRecord> updateDataRecordList = new ArrayList<HmeEoJobDataRecord>();
        Map<Integer, Integer> insertIndexMap = new HashMap<>();
        int index = -1;
        System.out.println("===========================begin==========================:" + new Date());
        for (HmeEoJobDataRecordVO jobDataRecord : dtoList) {
            index++;
            System.out.println("===========================existsJobDataRecord begin==========================:" + new Date());
            HmeEoJobDataRecord dataRecordParam = new HmeEoJobDataRecord();
            dataRecordParam.setTenantId(tenantId);
            dataRecordParam.setWorkcellId(jobDataRecord.getWorkcellId());
            dataRecordParam.setJobId(jobDataRecord.getJobId());
            dataRecordParam.setTagGroupId(jobDataRecord.getTagGroupId());
            dataRecordParam.setTagId(jobDataRecord.getTagId());
            HmeEoJobDataRecord existsJobDataRecord = this.selectOne(dataRecordParam);
            System.out.println("===========================existsJobDataRecord end==========================:" + new Date());
            if (Objects.isNull(existsJobDataRecord)) {
                HmeEoJobDataRecord hmeEoJobDataRecord = HmeEoJobDataRecord.builder()
                        .tenantId(tenantId)
                        .jobId(jobDataRecord.getJobId())
                        .workcellId(jobDataRecord.getWorkcellId())
                        .eoId(jobDataRecord.getEoId())
                        .tagGroupId(jobDataRecord.getTagGroupId())
                        .tagId(jobDataRecord.getTagId())
                        .minimumValue(jobDataRecord.getMinimumValue())
                        .maximalValue(jobDataRecord.getMaximalValue())
                        .groupPurpose(jobDataRecord.getGroupPurpose())
                        .isSupplement(jobDataRecord.getIsSupplement())
                        .result(jobDataRecord.getResult())
                        .build();
                insertDataRecordList.add(hmeEoJobDataRecord);
                insertIndexMap.put(insertDataRecordList.size() - 1, index);
            } else {

                if (StringUtils.isBlank(jobDataRecord.getJobRecordId())) {
                    //????????????????????????????????????
                    throw new MtException("HME_EO_JOB_DATA_RECORD_001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_DATA_RECORD_001", "HME"));
                }

                jobDataRecord.setJobRecordId(existsJobDataRecord.getJobRecordId());
                //updateDataRecordList.add(jobDataRecord);
                System.out.println("===========================updateByPrimaryKeySelective begin==========================:" + new Date());
                System.out.println("===========================jobDataRecord.getObjectVersionNumber()==========================:" + jobDataRecord.getObjectVersionNumber());
                hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(jobDataRecord);
                System.out.println("===========================updateByPrimaryKeySelective end==========================:" + new Date());
            }

            // ??????????????????????????????
            HmeTagDaqAttr hmeTagDaqAttrParam = new HmeTagDaqAttr();
            hmeTagDaqAttrParam.setTenantId(tenantId);
            hmeTagDaqAttrParam.setTagId(jobDataRecord.getTagId());
            HmeTagDaqAttr hmeTagDaqAttr = hmeTagDaqAttrRepository.selectOne(hmeTagDaqAttrParam);
            if (Objects.nonNull(hmeTagDaqAttr)) {
                jobDataRecord.setEquipmentCategory(hmeTagDaqAttr.getEquipmentCategory());
                jobDataRecord.setValueField(hmeTagDaqAttr.getValueField());
                jobDataRecord.setLimitCond1(hmeTagDaqAttr.getLimitCond1());
                jobDataRecord.setCond1Value(hmeTagDaqAttr.getCond1Value());
                jobDataRecord.setLimitCond2(hmeTagDaqAttr.getLimitCond2());
                jobDataRecord.setCond2Value(hmeTagDaqAttr.getCond2Value());
            }

            //V20200920 modify by penglin.sui for tianyan.xie ????????????hme_eo_test_data_record?????????
            HmeEoTestDataRecord hmeEoTestDataRecordPara = new HmeEoTestDataRecord();
            hmeEoTestDataRecordPara.setEoId(jobDataRecord.getEoId());
            hmeEoTestDataRecordPara.setMaterialLotId(jobDataRecord.getMaterialLotId());
            hmeEoTestDataRecordPara.setOperationId(jobDataRecord.getOperationId());
            hmeEoTestDataRecordPara.setTagGroupId(jobDataRecord.getTagGroupId());
            hmeEoTestDataRecordPara.setTagId(jobDataRecord.getTagId());
            hmeEoTestDataRecordPara.setReworkFlag(StringUtils.isBlank(jobDataRecord.getReworkFlag()) ? HmeConstants.ConstantValue.NO : jobDataRecord.getReworkFlag());
            hmeEoTestDataRecordPara.setMinValue(jobDataRecord.getMinimumValue());
            hmeEoTestDataRecordPara.setMaxValue(jobDataRecord.getMaximalValue());
            hmeEoTestDataRecordPara.setResult(jobDataRecord.getResult());
            hmeEoTestDataRecordRepository.saveTestDataRecord(tenantId, hmeEoTestDataRecordPara);
        }

        if (CollectionUtils.isNotEmpty(insertDataRecordList)) {
            System.out.println("===========================batchInsertSelective begin==========================:" + new Date());
            self().batchInsertSelective(insertDataRecordList);
            System.out.println("===========================batchInsertSelective end==========================:" + new Date());
            //??????????????????
            for (Map.Entry<Integer, Integer> entry : insertIndexMap.entrySet()) {
                dtoList.get(entry.getValue()).setJobRecordId(insertDataRecordList.get(entry.getKey()).getJobRecordId());
            }
        }
//        if(CollectionUtils.isNotEmpty(updateDataRecordList)){
//            System.out.println("===========================batchUpdateOptional begin==========================:" + new Date());
//            self().batchUpdateByPrimaryKeySelective(updateDataRecordList);
//            System.out.println("===========================batchUpdateOptional begin==========================:" + new Date());
//        }
        System.out.println("===========================end==========================:" + new Date());

        return dtoList;
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO
     * @Description ????????????????????????
     * @author yuchao.wang
     * @date 2020/9/22 11:47
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobDataRecordVO calculationFormulaData(Long tenantId, HmeEoJobDataRecordVO dto) {
        if (StringUtils.isEmpty(dto.getJobRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "JobRecordId"));
        }
        if (StringUtils.isEmpty(dto.getTagId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "TagId"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "EoId"));
        }

        //?????????????????????????????????OperationId+TagGroupId+TagId
        HmeTagFormulaHead headQuery = new HmeTagFormulaHead();
        headQuery.setTenantId(tenantId);
        headQuery.setOperationId(dto.getOperationId());
        headQuery.setTagGroupId(dto.getTagGroupId());
        headQuery.setTagId(dto.getTagId());
        List<HmeTagFormulaHead> headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
        if (StringUtils.isBlank(dto.getOperationId()) || StringUtils.isBlank(dto.getTagGroupId()) || CollectionUtils.isEmpty(headList)
                || Objects.isNull(headList.get(0)) || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
            //???????????????TagGroupId+TagId
            headQuery.setOperationId(null);
            headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
            if (StringUtils.isBlank(dto.getTagGroupId()) || CollectionUtils.isEmpty(headList)
                    || Objects.isNull(headList.get(0)) || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                //???????????????OperationId+TagId
                headQuery.setTagGroupId(null);
                headQuery.setOperationId(dto.getOperationId());
                headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
                if (CollectionUtils.isEmpty(headList) || Objects.isNull(headList.get(0))
                        || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                    //???????????????TagId
                    headQuery.setOperationId(null);
                    headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
                    if (CollectionUtils.isEmpty(headList) || Objects.isNull(headList.get(0))
                            || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "???????????????????????????"));
                    }
                }
            }
        }
        HmeTagFormulaHead tagFormulaHead = headList.get(0);

        //?????????????????????
        HmeTagFormulaLine lineQuery = new HmeTagFormulaLine();
        lineQuery.setTenantId(tenantId);
        lineQuery.setTagFormulaHeadId(tagFormulaHead.getTagFormulaHeadId());
        List<HmeTagFormulaLine> lineList = hmeTagFormulaLineRepository.select(lineQuery);
        if (CollectionUtils.isEmpty(lineList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "???????????????????????????"));
        }




        String eoId = dto.getEoId();
        String oldEoId = "";
        Boolean eoOfRepairWorkOrder = this.isEoOfRepairWorkOrder(tenantId, dto.getEoId());
        if (eoOfRepairWorkOrder) {
           oldEoId = hmeEoJobDataRecordMapper.queryOldEoByEoId(tenantId, eoId);
        }
        boolean reflectorNc = hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId());

        //V20210115 modify by penglin.sui for peng.zhao ????????????????????????

        //?????????????????????
        Map<String, List<HmeEoJobDataRecordVO5>> blankOperationDataRecordMap = new HashMap<>();
        List<HmeTagFormulaLine> blankOperationList = lineList.stream().filter(item -> StringUtils.isBlank(item.getOperationId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(blankOperationList)) {
            List<String> tagIdList = blankOperationList.stream().map(HmeTagFormulaLine::getTagId).distinct().collect(Collectors.toList());
            List<String> tagGroupIdList = blankOperationList.stream().map(HmeTagFormulaLine::getTagGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithoutOperationId(tenantId, eoId, tagIdList, tagGroupIdList);
            // ????????? ?????????????????? ?????????????????????????????????
            if (reflectorNc && eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                List<HmeEoJobDataRecordVO5> oldEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithoutOperationId(tenantId, oldEoId, tagIdList, tagGroupIdList);
                if (CollectionUtils.isNotEmpty(oldEoJobDataRecordList)) {
                    hmeEoJobDataRecordList.addAll(oldEoJobDataRecordList);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                for (HmeEoJobDataRecordVO5 hmeEoJobDataRecordVO5 : hmeEoJobDataRecordList
                ) {
                    if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                        if (!tagGroupIdList.contains(hmeEoJobDataRecordVO5.getTagGroupId())) {
                            hmeEoJobDataRecordVO5.setTagGroupId(null);
                        }
                    } else {
                        hmeEoJobDataRecordVO5.setTagGroupId(null);
                    }
                }
                blankOperationDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(item -> fetchGroupKey3(item.getEoId(), item.getTagId(), item.getTagGroupId())));
            }
        }
        //????????????????????????
        Map<String, List<HmeEoJobDataRecordVO5>> notBlankOperationDataRecordMap = new HashMap<>();
        List<HmeTagFormulaLine> notBlankOperationList = lineList.stream().filter(item -> StringUtils.isNotBlank(item.getOperationId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notBlankOperationList)) {
            List<String> tagIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getTagId).distinct().collect(Collectors.toList());
            List<String> tagGroupIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getTagGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> operationIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getOperationId).distinct().collect(Collectors.toList());
            List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithOperationId(tenantId, eoId, tagIdList, tagGroupIdList, operationIdList);
            // ????????? ?????????????????? ?????????????????????????????????
            if (reflectorNc && eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                List<HmeEoJobDataRecordVO5> oldEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithOperationId(tenantId, oldEoId, tagIdList, tagGroupIdList, operationIdList);
                if (CollectionUtils.isNotEmpty(oldEoJobDataRecordList)) {
                    hmeEoJobDataRecordList.addAll(oldEoJobDataRecordList);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                for (HmeEoJobDataRecordVO5 hmeEoJobDataRecordVO5 : hmeEoJobDataRecordList
                ) {
                    if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                        if (!tagGroupIdList.contains(hmeEoJobDataRecordVO5.getTagGroupId())) {
                            hmeEoJobDataRecordVO5.setTagGroupId(null);
                        }
                    } else {
                        hmeEoJobDataRecordVO5.setTagGroupId(null);
                    }
                }
                notBlankOperationDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(item -> fetchGroupKey4(item.getEoId(), item.getTagId(), item.getTagGroupId(), item.getOperationId())));
            }
        }
        String formula = tagFormulaHead.getFormula();
        // ????????? ???????????? ???????????????a/b?????? ???a-?????????????????????EO??? b-???????????? ?????????eo???
        String paramCodeOne = "";
        String paramCodeTwo = "";
        if (formula.contains("/")) {
            String[] split = formula.split("/");
            paramCodeOne = split[0];
            paramCodeTwo = split[1];
        }
        //?????????????????????
        Scope scope = new Scope();
        for (HmeTagFormulaLine tagFormulaLine : lineList) {
            //
            String result = "";
            //????????????????????????JobId??????????????????????????????
            if (StringUtils.isNotBlank(tagFormulaLine.getOperationId())) {
//                result = hmeEoJobDataRecordMapper.queryResultWithOperationId(tenantId, dto.getEoId(), tagFormulaLine);
                List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordVO5 = notBlankOperationDataRecordMap.getOrDefault(fetchGroupKey4(eoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId(), tagFormulaLine.getOperationId()), new ArrayList<>());
                if (reflectorNc && eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                    if (tagFormulaLine.getParameterCode().equals(paramCodeTwo)) {
                        hmeEoJobDataRecordVO5 = notBlankOperationDataRecordMap.getOrDefault(fetchGroupKey4(oldEoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId(), tagFormulaLine.getOperationId()), new ArrayList<>());
                    }
                }
                if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVO5)) {
                    hmeEoJobDataRecordVO5 = hmeEoJobDataRecordVO5.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO5::getLastUpdateDate).reversed())
                            .collect(Collectors.toList());
                    result = hmeEoJobDataRecordVO5.get(0).getResult();
                }
            } else {
//                result = hmeEoJobDataRecordMapper.queryResultWithoutOperationId(tenantId, dto.getEoId(), tagFormulaLine);
                List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordVO5 = blankOperationDataRecordMap.getOrDefault(fetchGroupKey3(eoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId()), new ArrayList<>());
                if (reflectorNc && eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                    if (tagFormulaLine.getParameterCode().equals(paramCodeTwo)) {
                        hmeEoJobDataRecordVO5 = blankOperationDataRecordMap.getOrDefault(fetchGroupKey3(oldEoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId()), new ArrayList<>());
                    }
                }
                if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVO5)) {
                    hmeEoJobDataRecordVO5 = hmeEoJobDataRecordVO5.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO5::getLastUpdateDate).reversed())
                            .collect(Collectors.toList());
                    result = hmeEoJobDataRecordVO5.get(0).getResult();
                }
            }

            //???????????????
            if (StringUtils.isBlank(result)) {
                MtTag mtTag = mtTagRepository.tagGet(tenantId, tagFormulaLine.getTagId());
                String tagCode = "";
                if (Objects.nonNull(mtTag)) {
                    tagCode = mtTag.getTagCode();
                }
                throw new MtException("HME_EO_JOB_DATA_RECORD_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_DATA_RECORD_002", "HME", tagCode));
            } else {
                //?????????Double
                try {
                    double res = Double.parseDouble(result);
                } catch (Exception e) {
                    log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData ???????????????????????? ", e);
                    MtTag mtTag = mtTagRepository.tagGet(tenantId, tagFormulaLine.getTagId());
                    String tagCode = "";
                    if (Objects.nonNull(mtTag)) {
                        tagCode = mtTag.getTagCode();
                    }
                    throw new MtException("HME_EO_JOB_DATA_RECORD_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_DATA_RECORD_004", "HME", tagCode));
                }
            }

            //?????????????????????
            scope.getVariable(tagFormulaLine.getParameterCode()).setValue(Double.parseDouble(result));
        }

        //????????????
        try {
            Expression expr2 = Parser.parse(tagFormulaHead.getFormula(), scope);
            double formulaValue = expr2.evaluate();
            dto.setResult(String.format("%.4f", formulaValue));
        } catch (Exception e) {
            log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData ?????????{}???????????????{}??????????????? ", tagFormulaHead.getFormula(), scope.getLocalVariables());
            log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData " + e);
            throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_DATA_RECORD_003", "HME", tagFormulaHead.getFormula()));
        }

        //???????????????????????????
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
        HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
        eoJobDataRecord.setJobRecordId(dto.getJobRecordId());
        eoJobDataRecord.setResult(dto.getResult());
        eoJobDataRecord.setLastUpdateDate(new Date());
        eoJobDataRecord.setLastUpdatedBy(userId);
        hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(eoJobDataRecord);

        return dto;
    }

    private Boolean isEoOfRepairWorkOrder(Long tenantId, String eoId) {
        Boolean flag = false;
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(eoId);
        if (mtEo != null) {
            List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId);
            List<String> repairWoTypeList = lovValue.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
            if (repairWoTypeList.contains(mtWorkOrder.getWorkOrderType())) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobDataRecordVO> batchCalculationFormulaData(Long tenantId, List<HmeEoJobDataRecordVO> dtoList) {
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(dtoList)){
            dtoList = dtoList.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO::getSerialNumber))
                    .collect(Collectors.toList());
        }
        for (HmeEoJobDataRecordVO hmeEoJobDataRecordVO: dtoList
             ) {
            HmeEoJobDataRecordVO hmeEoJobDataRecordVO1 = calculationFormulaData(tenantId,hmeEoJobDataRecordVO);
            hmeEoJobDataRecordVOList.add(hmeEoJobDataRecordVO1);
        }
        return hmeEoJobDataRecordVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobDataRecordVO> batchCalculationFormulaDataForRepair(Long tenantId, List<HmeEoJobDataRecordVO> dtoList) {
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dtoList)) {
            return hmeEoJobDataRecordVOList;
        }
        // ??????????????????
        validateNotBlankParams(tenantId, dtoList);
        dtoList = dtoList.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO::getSerialNumber))
                .collect(Collectors.toList());
        int count = 0;
        int dtoListSize = dtoList.size();
        // ?????????????????????
        while(CollectionUtils.isNotEmpty(dtoList)) {
            HmeEoJobDataRecordVO dto = dtoList.get(0);
            boolean breakFlag = false;
            if (count >= dtoListSize && HmeConstants.ConstantValue.YES.equals(dto.getValueAllowMissing())) {
                hmeEoJobDataRecordVOList.add(dto);
                dtoList.remove(dto);
                count ++;
                continue;
            }

            //?????????????????????????????????OperationId+TagGroupId+TagId
            HmeTagFormulaHead headQuery = new HmeTagFormulaHead();
            headQuery.setTenantId(tenantId);
            headQuery.setOperationId(dto.getOperationId());
            headQuery.setTagGroupId(dto.getTagGroupId());
            headQuery.setTagId(dto.getTagId());
            List<HmeTagFormulaHead> headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
            if (StringUtils.isBlank(dto.getOperationId()) || StringUtils.isBlank(dto.getTagGroupId()) || CollectionUtils.isEmpty(headList)
                    || Objects.isNull(headList.get(0)) || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                //???????????????TagGroupId+TagId
                headQuery.setOperationId(null);
                headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
                if (StringUtils.isBlank(dto.getTagGroupId()) || CollectionUtils.isEmpty(headList)
                        || Objects.isNull(headList.get(0)) || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                    //???????????????OperationId+TagId
                    headQuery.setTagGroupId(null);
                    headQuery.setOperationId(dto.getOperationId());
                    headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
                    if (CollectionUtils.isEmpty(headList) || Objects.isNull(headList.get(0))
                            || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                        //???????????????TagId
                        headQuery.setOperationId(null);
                        headList = hmeTagFormulaHeadRepository.selectHeadForCalculation(headQuery);
                        if (CollectionUtils.isEmpty(headList) || Objects.isNull(headList.get(0))
                                || StringUtils.isBlank(headList.get(0).getTagFormulaHeadId())) {
                            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_CHIP_TRANSFER_013", "HME", "???????????????????????????"));
                        }
                    }
                }
            }
            HmeTagFormulaHead tagFormulaHead = headList.get(0);

            //?????????????????????
            HmeTagFormulaLine lineQuery = new HmeTagFormulaLine();
            lineQuery.setTenantId(tenantId);
            lineQuery.setTagFormulaHeadId(tagFormulaHead.getTagFormulaHeadId());
            List<HmeTagFormulaLine> lineList = hmeTagFormulaLineRepository.select(lineQuery);
            if (CollectionUtils.isEmpty(lineList)) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "???????????????????????????"));
            }

            // ??????????????????  ???????????????????????? ??????????????????eo????????????
            String eoId = dto.getEoId();
            String oldEoId = "";
            Boolean eoOfRepairWorkOrder = this.isEoOfRepairWorkOrder(tenantId, dto.getEoId());
            if (eoOfRepairWorkOrder) {
                oldEoId = hmeEoJobDataRecordMapper.queryOldEoByEoId(tenantId, eoId);
            }
//            boolean reflectorNc = hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId());

            //?????????????????????
            Map<String, List<HmeEoJobDataRecordVO5>> blankOperationDataRecordMap = new HashMap<>();
            List<HmeTagFormulaLine> blankOperationList = lineList.stream().filter(item -> StringUtils.isBlank(item.getOperationId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(blankOperationList)) {
                List<String> tagIdList = blankOperationList.stream().map(HmeTagFormulaLine::getTagId).distinct().collect(Collectors.toList());
                List<String> tagGroupIdList = blankOperationList.stream().map(HmeTagFormulaLine::getTagGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithoutOperationId(tenantId, eoId, tagIdList, tagGroupIdList);
                // ????????? ?????????????????? ?????????????????????????????????
                if (eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                    List<HmeEoJobDataRecordVO5> oldEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithoutOperationId(tenantId, oldEoId, tagIdList, tagGroupIdList);
                    if (CollectionUtils.isNotEmpty(oldEoJobDataRecordList)) {
                        hmeEoJobDataRecordList.addAll(oldEoJobDataRecordList);
                    }
                }
                if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                    for (HmeEoJobDataRecordVO5 hmeEoJobDataRecordVO5 : hmeEoJobDataRecordList
                    ) {
                        if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                            if (!tagGroupIdList.contains(hmeEoJobDataRecordVO5.getTagGroupId())) {
                                hmeEoJobDataRecordVO5.setTagGroupId(null);
                            }
                        } else {
                            hmeEoJobDataRecordVO5.setTagGroupId(null);
                        }
                    }
                    blankOperationDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(item -> fetchGroupKey3(item.getEoId(), item.getTagId(), item.getTagGroupId())));
                }
            }
            //????????????????????????
            Map<String, List<HmeEoJobDataRecordVO5>> notBlankOperationDataRecordMap = new HashMap<>();
            List<HmeTagFormulaLine> notBlankOperationList = lineList.stream().filter(item -> StringUtils.isNotBlank(item.getOperationId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(notBlankOperationList)) {
                List<String> tagIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getTagId).distinct().collect(Collectors.toList());
                List<String> tagGroupIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getTagGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                List<String> operationIdList = notBlankOperationList.stream().map(HmeTagFormulaLine::getOperationId).distinct().collect(Collectors.toList());
                List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithOperationId(tenantId, eoId, tagIdList, tagGroupIdList, operationIdList);
                // ???????????? ?????????????????????????????????
                if (eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                    List<HmeEoJobDataRecordVO5> oldEoJobDataRecordList = hmeEoJobDataRecordMapper.batchQueryResultWithOperationId(tenantId, oldEoId, tagIdList, tagGroupIdList, operationIdList);
                    if (CollectionUtils.isNotEmpty(oldEoJobDataRecordList)) {
                        hmeEoJobDataRecordList.addAll(oldEoJobDataRecordList);
                    }
                }
                if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                    for (HmeEoJobDataRecordVO5 hmeEoJobDataRecordVO5 : hmeEoJobDataRecordList
                    ) {
                        if (CollectionUtils.isNotEmpty(tagGroupIdList)) {
                            if (!tagGroupIdList.contains(hmeEoJobDataRecordVO5.getTagGroupId())) {
                                hmeEoJobDataRecordVO5.setTagGroupId(null);
                            }
                        } else {
                            hmeEoJobDataRecordVO5.setTagGroupId(null);
                        }
                    }
                    notBlankOperationDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(item -> fetchGroupKey4(item.getEoId(), item.getTagId(), item.getTagGroupId(), item.getOperationId())));
                }
            }
            String formula = tagFormulaHead.getFormula();
            //?????????????????????
            Scope scope = new Scope();
            for (HmeTagFormulaLine tagFormulaLine : lineList) {
                //
                String result = "";
                //????????????????????????JobId??????????????????????????????
                if (StringUtils.isNotBlank(tagFormulaLine.getOperationId())) {
//                result = hmeEoJobDataRecordMapper.queryResultWithOperationId(tenantId, dto.getEoId(), tagFormulaLine);
                    List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordVO5 = notBlankOperationDataRecordMap.getOrDefault(fetchGroupKey4(eoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId(), tagFormulaLine.getOperationId()), new ArrayList<>());
                    if (eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                        List<HmeEoJobDataRecordVO5> oldRecordList = notBlankOperationDataRecordMap.getOrDefault(fetchGroupKey4(oldEoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId(), tagFormulaLine.getOperationId()), new ArrayList<>());
                        if (CollectionUtils.isNotEmpty(oldRecordList)) {
                            hmeEoJobDataRecordVO5.addAll(oldRecordList);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVO5)) {
                        hmeEoJobDataRecordVO5 = hmeEoJobDataRecordVO5.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO5::getLastUpdateDate).reversed())
                                .collect(Collectors.toList());
                        result = hmeEoJobDataRecordVO5.get(0).getResult();
                    }
                } else {
//                result = hmeEoJobDataRecordMapper.queryResultWithoutOperationId(tenantId, dto.getEoId(), tagFormulaLine);
                    List<HmeEoJobDataRecordVO5> hmeEoJobDataRecordVO5 = blankOperationDataRecordMap.getOrDefault(fetchGroupKey3(eoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId()), new ArrayList<>());
                    if (eoOfRepairWorkOrder && StringUtils.isNotBlank(oldEoId)) {
                        List<HmeEoJobDataRecordVO5> oldRecordList = blankOperationDataRecordMap.getOrDefault(fetchGroupKey3(oldEoId, tagFormulaLine.getTagId(), tagFormulaLine.getTagGroupId()), new ArrayList<>());
                        if (CollectionUtils.isNotEmpty(oldRecordList)) {
                            hmeEoJobDataRecordVO5.addAll(oldRecordList);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVO5)) {
                        hmeEoJobDataRecordVO5 = hmeEoJobDataRecordVO5.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO5::getLastUpdateDate).reversed())
                                .collect(Collectors.toList());
                        result = hmeEoJobDataRecordVO5.get(0).getResult();
                    }
                }

                // ??????????????? ?????????????????? ???????????????????????????
                if (StringUtils.isBlank(result)) {
                    breakFlag = true;
                    // ????????? ???????????????????????? ??????
                    if (count >= dtoListSize - 1 && HmeConstants.ConstantValue.NO.equals(dto.getValueAllowMissing())) {
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, tagFormulaLine.getTagId());
                        String tagCode = "";
                        if (Objects.nonNull(mtTag)) {
                            tagCode = mtTag.getTagCode();
                        }
                        throw new MtException("HME_EO_JOB_DATA_RECORD_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_DATA_RECORD_002", "HME", tagCode));
                    }
                    break;
                } else {
                    //?????????Double
                    try {
                        double res = Double.parseDouble(result);
                    } catch (Exception e) {
                        log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData ???????????????????????? ", e);
                        MtTag mtTag = mtTagRepository.tagGet(tenantId, tagFormulaLine.getTagId());
                        String tagCode = "";
                        if (Objects.nonNull(mtTag)) {
                            tagCode = mtTag.getTagCode();
                        }
                        throw new MtException("HME_EO_JOB_DATA_RECORD_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_DATA_RECORD_004", "HME", tagCode));
                    }
                }

                //?????????????????????
                scope.getVariable(tagFormulaLine.getParameterCode()).setValue(Double.parseDouble(result));
            }
            // ?????????????????????
            if (breakFlag) {
                dtoList.remove(dto);
                dtoList.add(dto);
                count ++;
                continue;
            }

            //????????????
            try {
                Expression expr2 = Parser.parse(tagFormulaHead.getFormula(), scope);
                double formulaValue = expr2.evaluate();
                dto.setResult(String.format("%.4f", formulaValue));
            } catch (Exception e) {
                log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData ?????????{}???????????????{}??????????????? ", tagFormulaHead.getFormula(), scope.getLocalVariables());
                log.error("<===== HmeEoJobDataRecordRepositoryImpl.calculationFormulaData " + e);
                throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_DATA_RECORD_003", "HME", tagFormulaHead.getFormula()));
            }

            //???????????????????????????
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
            HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
            eoJobDataRecord.setJobRecordId(dto.getJobRecordId());
            eoJobDataRecord.setResult(dto.getResult());
            eoJobDataRecord.setLastUpdateDate(new Date());
            eoJobDataRecord.setLastUpdatedBy(userId);
            hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(eoJobDataRecord);

            hmeEoJobDataRecordVOList.add(dto);
            dtoList.remove(dto);
            count ++;
        }
        return hmeEoJobDataRecordVOList;
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     * @Description ???????????????????????????????????????
     * @author yuchao.wang
     * @date 2020/9/30 15:48
     */
    @Override
    public List<HmeEoJobDataRecordVO> queryResultForFirstProcess(Long tenantId, HmeEoJobDataCalculationResultDTO dto) {
        //????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "EoId"));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "SiteId"));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "MaterialLotId"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "OperationId"));
        }
        if (CollectionUtils.isEmpty(dto.getEoJobDataRecordVOList())) {
            return new ArrayList<HmeEoJobDataRecordVO>();
        }

        //????????????????????????
        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("ALIAS");
        List<MtExtendAttrVO> tagTypeAttr = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_operation_attr", "OPERATION_ID", dto.getOperationId(),
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isEmpty(tagTypeAttr) || Objects.isNull(tagTypeAttr.get(0))
                || StringUtils.isBlank(tagTypeAttr.get(0).getAttrValue())) {
            //???????????????????????????????????????
            throw new MtException("HME_EO_JOB_DATA_RECORD_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_DATA_RECORD_005", "HME"));
        }
        String alias = tagTypeAttr.get(0).getAttrValue();

        List<String> currentList = new ArrayList<>();
        List<HmeEoJobDataRecordVO> eoJobDataRecordVOList = dto.getEoJobDataRecordVOList();
        eoJobDataRecordVOList.forEach(item -> {
            if (StringUtils.isEmpty(item.getJobRecordId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "JobRecordId"));
            }
            if (StringUtils.isEmpty(item.getTagId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "TagId"));
            }
            if (StringUtils.isEmpty(item.getTagCode())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "TagCode"));
            }

            //??????TagCode???????????????
            String[] split = item.getTagCode().replace(alias + "-", "").split("-");
            if (split.length == 3) {
                String current = "";
                for (String str : split) {
                    if (Pattern.matches("^(\\d)+A$", str)) {
                        current = str.replace("A", "");
                        currentList.add(current);
                    }
                }
            }
        });
        if(CollectionUtils.isEmpty(currentList)){
            return eoJobDataRecordVOList;
        }

        //????????????HME.FIRST_PROCESS_TAG
        Map<String, String> firstProcessTagMap = new HashMap<String, String>();
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.FIRST_PROCESS_TAG", tenantId);
        if (CollectionUtils.isNotEmpty(lovValueDTOS)) {
            lovValueDTOS.forEach(item -> firstProcessTagMap.put(item.getValue(), item.getMeaning()));
        }

        //???????????????????????????
        List<HmeCosFunctionVO> cosFunctionVOList = new ArrayList<>();
        if(HmeConstants.ConstantValue.YES.equals(dto.getUseSourceFlag())) {
            cosFunctionVOList = hmeEoJobDataRecordMapper.queryCosFunctions(tenantId, dto.getEoId(), dto.getSiteId(), currentList);
        }else {
            cosFunctionVOList = hmeEoJobDataRecordMapper.queryCosFunctionMaterials(tenantId, dto.getEoId(), dto.getSiteId(), currentList);
            // 20211018 add by sanfeng.zhang for yiming.zhang ?????????????????? ????????????????????? ??????????????? ?????????????????????
            if (CollectionUtils.isEmpty(cosFunctionVOList) && HmeConstants.ConstantValue.YES.equals(dto.getIsEquipmentFirstProcess())) {
                cosFunctionVOList = hmeEoJobDataRecordMapper.queryCosFunctions(tenantId, dto.getEoId(), dto.getSiteId(), currentList);
            }
        }
        if (CollectionUtils.isEmpty(cosFunctionVOList)) {
//            return eoJobDataRecordVOList;
            //???????????????????????????,?????????
            throw new MtException("HME_EO_JOB_FIRST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_FIRST_003", "HME"));
        }

        //????????????????????????Map key???Current-COSxx
        // 20210326 modify by sanfeng.zhang for peng.zhao COS???????????????????????????attribute2
        String tmpCurrent = cosFunctionVOList.get(0).getCurrent();
        Map<String, HmeCosFunctionVO> resultMap = new HashMap<String, HmeCosFunctionVO>();
        for (HmeCosFunctionVO item : cosFunctionVOList) {
            if (!tmpCurrent.equals(item.getCurrent())) {
                tmpCurrent = item.getCurrent();
            }
            item.setCosNum(item.getFacCosPos());
            resultMap.put(tmpCurrent + "-" + item.getCosNum(), item);
        }

        //?????????????????????
        List<HmeEoJobDataRecord> jobDataRecordList = new ArrayList<>();
        for (HmeEoJobDataRecordVO eoJobDataRecordVO : eoJobDataRecordVOList) {
            //???????????????????????????????????????
            if (!eoJobDataRecordVO.getTagCode().startsWith(alias + "-")) {
                continue;
            }

            //??????TagCode???????????????
            String[] split = eoJobDataRecordVO.getTagCode().replace(alias + "-", "").split("-");
            if (split.length != 3) {
                continue;
            }
            String current = "";
            String cosNum = "";
            String firstProcessTag = "";
            for (String str : split) {
                if (Pattern.matches("^COS(\\d)+$", str)) {
                    cosNum = str;
                } else if (Pattern.matches("^(\\d)+A$", str)) {
                    current = str.replace("A", "");
                } else {
                    firstProcessTag = str;
                }
            }

            //????????????????????????????????????????????????
            if (resultMap.containsKey(current + "-" + cosNum) && firstProcessTagMap.containsKey(firstProcessTag)) {
                //????????????????????????????????????????????????
                try {
                    HmeCosFunctionVO vo = resultMap.get(current + "-" + cosNum);
                    String columnName = FieldNameUtils.underline2Camel(firstProcessTagMap.get(firstProcessTag), true);
                    Field field = HmeCosFunctionVO.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    Object result = field.get(vo);
                    eoJobDataRecordVO.setResult(Objects.isNull(result) ? "" : result.toString());
                } catch (Exception e) {
                    log.error("<===== HmeEoJobDataRecordRepositoryImpl.queryResultForFirstProcess ????????????["
                            + firstProcessTagMap.get(firstProcessTag) + "]????????? ", e);
                    //????????????${1}?????????,?????????!
                    throw new MtException("HME_EO_JOB_FIRST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_FIRST_004", "HME"));
                }
            } else {
                //???????????????????????????,?????????
                throw new MtException("HME_EO_JOB_FIRST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_FIRST_003", "HME"));
            }

            //???????????????????????????
            HmeEoJobDataRecord eoJobDataRecord = new HmeEoJobDataRecord();
            eoJobDataRecord.setJobRecordId(eoJobDataRecordVO.getJobRecordId());
            eoJobDataRecord.setResult(eoJobDataRecordVO.getResult());
            jobDataRecordList.add(eoJobDataRecord);


            //??????hme_eo_test_data_record?????????
            HmeEoTestDataRecord hmeEoTestDataRecordPara = new HmeEoTestDataRecord();
            hmeEoTestDataRecordPara.setEoId(dto.getEoId());
            hmeEoTestDataRecordPara.setMaterialLotId(dto.getMaterialLotId());
            hmeEoTestDataRecordPara.setReworkFlag(StringUtils.isBlank(dto.getReworkFlag()) ? HmeConstants.ConstantValue.NO : dto.getReworkFlag());
            hmeEoTestDataRecordPara.setOperationId(dto.getOperationId());
            hmeEoTestDataRecordPara.setTagGroupId(eoJobDataRecordVO.getTagGroupId());
            hmeEoTestDataRecordPara.setTagId(eoJobDataRecordVO.getTagId());
            hmeEoTestDataRecordPara.setMinValue(eoJobDataRecordVO.getMinimumValue());
            hmeEoTestDataRecordPara.setMaxValue(eoJobDataRecordVO.getMaximalValue());
            hmeEoTestDataRecordPara.setResult(eoJobDataRecordVO.getResult());
            hmeEoTestDataRecordRepository.saveTestDataRecord(tenantId, hmeEoTestDataRecordPara);
        }

        //?????????????????????
        if (CollectionUtils.isNotEmpty(jobDataRecordList)) {
            batchUpdateResult(tenantId, jobDataRecordList);
        }
        return eoJobDataRecordVOList;
    }

    /**
     * @param tenantId       ??????ID
     * @param dataRecordList ??????
     * @return void
     * @Description ?????????????????????
     * @author yuchao.wang
     * @date 2020/9/30 17:06
     */
    @Override
    public void batchUpdateResult(Long tenantId, List<HmeEoJobDataRecord> dataRecordList) {
        int batchNum = 500;
        Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId()))
                ? DetailsHelper.getUserDetails().getUserId() : -1L;
        List<List<HmeEoJobDataRecord>> list = InterfaceUtils.splitSqlList(dataRecordList, batchNum);
        list.forEach(item -> hmeEoJobDataRecordMapper.batchUpdateResult(tenantId, userId, item));
    }

    /**
     * @param tenantId                ??????ID
     * @param dataRecordInitParamList ??????
     * @return void
     * @Description ???????????????????????????
     * @author yuchao.wang
     * @date 2020/11/23 17:23
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInitEoJobDataRecord(Long tenantId, List<HmeEoJobSnVO2> dataRecordInitParamList) {
        //??????????????????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date currDate = new Date();

        //????????????tag
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = new ArrayList<HmeEoJobDataRecord>();
        for (HmeEoJobSnVO2 dataRecordInitParam : dataRecordInitParamList) {
            //?????????DATA
            List<String> businessTypeList = new ArrayList<>();
            businessTypeList.add("DATA");

            //1.??????+??????+?????????2.??????+??????+???????????????3.??????+?????????+???????????????4.??????+???????????????+????????????
            List<HmeTagVO> tagList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getSnMaterialId(), dataRecordInitParam.getProductionVersion(), businessTypeList);
            if (CollectionUtils.isEmpty(tagList)) {
                //2
                tagList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getSnMaterialId(), "", businessTypeList);
                if (CollectionUtils.isEmpty(tagList)) {
                    //3
                    tagList = hmeTagMapper.operationItemTypeLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getItemType(), businessTypeList);
                    if (CollectionUtils.isEmpty(tagList)) {
                        //4
                        tagList = hmeTagMapper.operationLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), businessTypeList);
                    }
                }
            }

            //?????????GENERAL
            businessTypeList.clear();
            businessTypeList.add("GENERAL");
            List<HmeTagVO> hmeGeneralTagList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getSnMaterialId(), dataRecordInitParam.getProductionVersion(), businessTypeList);
            if (CollectionUtils.isEmpty(hmeGeneralTagList)) {
                //2
                hmeGeneralTagList = hmeTagMapper.operationMaterialVersionLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getSnMaterialId(), "", businessTypeList);
                if (CollectionUtils.isEmpty(hmeGeneralTagList)) {
                    //3
                    hmeGeneralTagList = hmeTagMapper.operationItemTypeLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), dataRecordInitParam.getItemType(), businessTypeList);
                    if (CollectionUtils.isEmpty(hmeGeneralTagList)) {
                        //4
                        hmeGeneralTagList = hmeTagMapper.operationLimitForEoJobSn(tenantId, dataRecordInitParam.getOperationId(), businessTypeList);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(hmeGeneralTagList)) {
                tagList.addAll(hmeGeneralTagList);
            }


            if (CollectionUtils.isNotEmpty(tagList)) {
                for (HmeTagVO tagVO : tagList) {
                    HmeEoJobDataRecord hmeEoJobDataRecord = HmeEoJobDataRecord.builder()
                            .tenantId(tenantId)
                            .jobId(dataRecordInitParam.getJobId())
                            .workcellId(dataRecordInitParam.getWorkcellId())
                            .eoId(dataRecordInitParam.getEoId())
                            .tagGroupId(tagVO.getTagGroupId())
                            .tagId(tagVO.getTagId())
                            .minimumValue(tagVO.getMinimumValue())
                            .maximalValue(tagVO.getMaximalValue())
                            .groupPurpose(tagVO.getGroupPurpose())
                            .result("")
                            .dataRecordId("")
                            .isSupplement("0")
                            .build();
                    hmeEoJobDataRecord.setObjectVersionNumber(1L);
                    hmeEoJobDataRecord.setCreationDate(currDate);
                    hmeEoJobDataRecord.setCreatedBy(userId);
                    hmeEoJobDataRecord.setLastUpdatedBy(userId);
                    hmeEoJobDataRecord.setLastUpdateDate(currDate);
                    hmeEoJobDataRecordList.add(hmeEoJobDataRecord);
                }
            }
        }

        if (CollectionUtils.isEmpty(hmeEoJobDataRecordList)) {
            return;
        }

        List<String> ids = this.customDbRepository.getNextKeys("hme_eo_job_data_record_s", hmeEoJobDataRecordList.size());
        List<String> cIds = this.customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", hmeEoJobDataRecordList.size());
        int count = 0;
        for (HmeEoJobDataRecord eoJobDataRecord : hmeEoJobDataRecordList) {
            eoJobDataRecord.setJobRecordId(ids.get(count));
            eoJobDataRecord.setCid(Long.valueOf(cIds.get(count)));
            count++;
        }

        //??????????????????
        List<List<HmeEoJobDataRecord>> splitSqlList = splitSqlList(hmeEoJobDataRecordList, 200);
        for (List<HmeEoJobDataRecord> domains : splitSqlList) {
            hmeEoJobDataRecordMapper.batchInsertDataRecord("hme_eo_job_data_record", domains);
        }
    }

    /**
     * @param tenantId          ??????ID
     * @param jobDataRecordList ???????????????ID
     * @return void
     * @Description ?????????????????????
     * @author yuchao.wang
     * @date 2020/12/7 17:20
     */
    @Override
    public void deleteDataRecordById(Long tenantId, List<String> jobDataRecordList) {
        if (CollectionUtils.isEmpty(jobDataRecordList)) {
            return;
        }

        //???????????????????????????
        List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(jobDataRecordList, 500);
        for (List<String> domains : splitSqlList) {
            hmeEoJobDataRecordMapper.deleteDataRecordById(domains);
        }
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO
     * @Description ???????????????????????????????????????
     * @author yuchao.wang
     * @date 2020/12/18 14:01
     */
    @Override
    public HmeEoJobDataRecordVO resultSaveForSingleProcess(Long tenantId, HmeEoJobDataRecordVO dto) {
        //????????????
        if (StringUtils.isBlank(dto.getJobRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "JobRecordId"));
        }
        if (!HmeConstants.ConstantValue.YES.equals(dto.getValueAllowMissing())
                && StringUtils.isBlank(dto.getResult()) && StringUtils.isBlank(dto.getRemark())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "???????????????"));
        }

        //????????????????????????????????????
        SecurityTokenHelper.close();
        HmeEoJobDataRecord param = new HmeEoJobDataRecord();
        param.setJobRecordId(dto.getJobRecordId());
        HmeEoJobDataRecord exitEoJobDataRecord = this.selectByPrimaryKey(param);
        if (Objects.isNull(exitEoJobDataRecord) || StringUtils.isBlank(exitEoJobDataRecord.getJobRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "?????????????????????"));
        }
        HmeEoJobDataRecord update = new HmeEoJobDataRecord();
        update.setJobRecordId(dto.getJobRecordId());
        update.setResult(dto.getResult());
        update.setRemark(dto.getRemark());
        hmeEoJobDataRecordMapper.updateByPrimaryKeySelective(update);

        //???????????????????????????-????????????????????????????????????????????????
        HmeEoJobDataRecordVO4 eoJobDataRecordVO4 = hmeEoJobDataRecordMapper.queryTagInfoByTagId(tenantId, exitEoJobDataRecord.getTagId());
        if (Objects.isNull(eoJobDataRecordVO4) || StringUtils.isBlank(eoJobDataRecordVO4.getTagId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "???????????????"));
        }

        //??????hme_eo_test_data_record?????????
        if (HmeConstants.ConstantValue.YES.equals(eoJobDataRecordVO4.getProcessFlag())) {
            String reworkFlag = StringUtils.isBlank(dto.getReworkFlag()) ? HmeConstants.ConstantValue.NO : dto.getReworkFlag();
            //????????????????????????
            SecurityTokenHelper.close();
            List<HmeEoTestDataRecord> hmeEoTestDataRecordList = hmeEoTestDataRecordRepository.selectByCondition(Condition.builder(HmeEoTestDataRecord.class)
                    .select(HmeEoTestDataRecord.FIELD_TEST_ID)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoTestDataRecord.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoTestDataRecord.FIELD_EO_ID, dto.getEoId())
                            .andEqualTo(HmeEoTestDataRecord.FIELD_TAG_ID, exitEoJobDataRecord.getTagId())
                            .andEqualTo(HmeEoTestDataRecord.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotId())
                            .andEqualTo(HmeEoTestDataRecord.FIELD_OPERATION_ID, dto.getOperationId())
                            .andEqualTo(HmeEoTestDataRecord.FIELD_TAG_GROUP_ID, exitEoJobDataRecord.getTagGroupId())
                            .andEqualTo(HmeEoTestDataRecord.FIELD_REWORK_FLAG, reworkFlag)
                    ).build());

            //???????????? ????????????
            if (CollectionUtils.isNotEmpty(hmeEoTestDataRecordList)) {
                for (HmeEoTestDataRecord hmeEoTestDataRecord : hmeEoTestDataRecordList) {
                    HmeEoTestDataRecord updateTestRecord = new HmeEoTestDataRecord();
                    updateTestRecord.setTestId(hmeEoTestDataRecord.getTestId());
                    updateTestRecord.setMinValue(exitEoJobDataRecord.getMinimumValue());
                    updateTestRecord.setMaxValue(exitEoJobDataRecord.getMaximalValue());
                    if (StringUtils.isNotBlank(eoJobDataRecordVO4.getStandard())) {
                        updateTestRecord.setStandardValue(new BigDecimal(eoJobDataRecordVO4.getStandard()));
                    }
                    updateTestRecord.setResult(dto.getResult());
                    hmeEoTestDataRecordMapper.updateByPrimaryKeySelective(updateTestRecord);
                }
            } else {
                HmeEoTestDataRecord insertTestRecord = new HmeEoTestDataRecord();
                insertTestRecord.setTenantId(tenantId);
                insertTestRecord.setEoId(dto.getEoId());
                insertTestRecord.setMaterialLotId(dto.getMaterialLotId());
                insertTestRecord.setOperationId(dto.getOperationId());
                insertTestRecord.setTagGroupId(exitEoJobDataRecord.getTagGroupId());
                insertTestRecord.setTagId(exitEoJobDataRecord.getTagId());
                insertTestRecord.setReworkFlag(reworkFlag);
                insertTestRecord.setMinValue(exitEoJobDataRecord.getMinimumValue());
                insertTestRecord.setMaxValue(exitEoJobDataRecord.getMaximalValue());
                if (StringUtils.isNotBlank(eoJobDataRecordVO4.getStandard())) {
                    insertTestRecord.setStandardValue(new BigDecimal(eoJobDataRecordVO4.getStandard()));
                }
                insertTestRecord.setResult(dto.getResult());
                hmeEoTestDataRecordRepository.insertSelective(insertTestRecord);
            }
        }

        //??????????????????
        exitEoJobDataRecord.setResult(dto.getResult());
        exitEoJobDataRecord.setRemark(dto.getRemark());
        BeanUtils.copyProperties(exitEoJobDataRecord, dto);
        dto.setEquipmentCategory(eoJobDataRecordVO4.getEquipmentCategory());
        dto.setValueField(eoJobDataRecordVO4.getValueField());
        dto.setLimitCond1(eoJobDataRecordVO4.getLimitCond1());
        dto.setCond1Value(eoJobDataRecordVO4.getCond1Value());
        dto.setLimitCond2(eoJobDataRecordVO4.getLimitCond2());
        dto.setCond2Value(eoJobDataRecordVO4.getCond2Value());
        return dto;
    }

    @Override
    public Page<HmeEoJobDataRecordDetailVO> detailListGet(Long tenantId, String snMaterialId, String operationId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeEoJobDataRecordMapper.selectDetailList(tenantId, snMaterialId, operationId));
    }

    /**
     *
     * @Description ????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/22 16:39
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param jobId jobId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     *
     */
    @Override
    public List<HmeEoJobDataRecord> queryForNcRecordValidate(Long tenantId, String workcellId, String jobId) {
        return hmeEoJobDataRecordMapper.queryForNcRecordValidate(tenantId, workcellId, jobId);
    }

    @Override
    public Map<String, HmeEoJobDataRecordVO2> queryNcRecord(Long tenantId, List<String> jobIdList) {
        List<HmeEoJobDataRecordVO2> hmeEoJobDataRecordVO2List = hmeEoJobDataRecordMapper.queryNcRecord(tenantId,jobIdList);
        Map<String, HmeEoJobDataRecordVO2> resultMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordVO2List)){
            Map<String,List<HmeEoJobDataRecordVO2>> hmeEoJobDataRecordVO2Map = hmeEoJobDataRecordVO2List.stream().collect(Collectors.groupingBy(e -> e.getJobId()));
            for(Map.Entry<String, List<HmeEoJobDataRecordVO2>> entry : hmeEoJobDataRecordVO2Map.entrySet()){
                if(CollectionUtils.isNotEmpty(entry.getValue())) {
                    List<HmeEoJobDataRecordVO2> ncRecordList = entry.getValue().stream().filter(item -> item.getTagCode().contains("NCRECORD")).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(ncRecordList)){
                        //V20210519 modify by penglin.sui for peng.zhao ??????????????????
                        resultMap.put(entry.getKey() , ncRecordList.get(0));
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     *
     * @Description ????????????????????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/25 17:43
     * @param tenantId ??????ID
     * @param workcellId ??????ID
     * @param jobIdList jobIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     *
     */
    @Override
    public List<HmeEoJobDataRecord> batchQueryForNcRecordValidate(Long tenantId, String workcellId, List<String> jobIdList) {
        return hmeEoJobDataRecordMapper.batchQueryForNcRecordValidate(tenantId, workcellId, jobIdList);
    }

    private void validateNotBlankParams(Long tenantId, List<HmeEoJobDataRecordVO> dtoList) {
        dtoList.forEach(dto -> {
            if (StringUtils.isEmpty(dto.getJobRecordId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "JobRecordId"));
            }
            if (StringUtils.isEmpty(dto.getTagId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "TagId"));
            }
            if (StringUtils.isEmpty(dto.getEoId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "EoId"));
            }
        });
    }

}
