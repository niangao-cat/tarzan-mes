package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeCosYieldComputeRepository;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO2;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO5;
import com.ruike.hme.domain.vo.HmeCosYieldComputeVO6;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosYieldComputeMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.vo.MtNumrangeVO11;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * COS良率计算定时任务资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-17 11:35:12
 */
@Component
public class HmeCosYieldComputeRepositoryImpl implements HmeCosYieldComputeRepository {

    @Autowired
    private HmeCosYieldComputeMapper hmeCosYieldComputeMapper;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Override
    public Date getLastJobDate(Long tenantId) {
        Date lastJobDate = hmeCosYieldComputeMapper.getLastJobDate(tenantId);
        Date nowDate = new Date();
        if (Objects.isNull(lastJobDate)) {
            //如果为空，则代表是第一次跑job，插入一条指定数据
            hmeCosYieldComputeMapper.insertJobDate(tenantId, nowDate);
            lastJobDate = nowDate;
        } else {
            //如果不为空，则更新job执行时间
            hmeCosYieldComputeMapper.updateJobDate(tenantId, nowDate);
        }
        return lastJobDate;
    }

    @Override
    public HmeCosYieldComputeVO2 getCosTypeWaferMaterialLotIdMap(Long tenantId, List<String> cosTypeList, List<String> waferList,
                                                                 List<HmeMaterialLotLoad> cosTypeWaferComposeList) {
        HmeCosYieldComputeVO2 result = new HmeCosYieldComputeVO2();
        Map<String, List<String>> cosTypeWaferMaterialLotIdMap = new HashMap<>();
        Map<String, String> cosTypeWaferMfFlagMap = new HashMap<>();
        Map<String, String> waferMfFlagMap = new HashMap<>();
        List<String> materialLotIdList = new ArrayList<>();
        //waferMaterialLotList 存储的是Wafer满足要求的物料批 先根据wafer找条码，是因为wafer的区分度高
        List<HmeCosYieldComputeVO> waferMaterialLotList = hmeCosYieldComputeMapper.getMaterialLotIdByAttr(tenantId, "WAFER_NUM", waferList);
        if (CollectionUtils.isNotEmpty(waferMaterialLotList)) {
            //enableFlagYMaterialLotIdList 存储的是有效性为Y的物料批，主要用于后面两种情况筛选是否存在在制品物料批
            List<String> enableFlagYMaterialLotIdList = waferMaterialLotList.stream().filter(item -> "Y".equals(item.getEnableFlag())).map(HmeCosYieldComputeVO::getMaterialLotId).collect(Collectors.toList());
            List<String> waferMaterialLotIdList = waferMaterialLotList.stream().map(HmeCosYieldComputeVO::getMaterialLotId).collect(Collectors.toList());
            //waferBegin3MaterialLotIdList 存储的是满足wafer要求的物料编码以3开头的物料批
            List<String> waferBegin3MaterialLotIdList = waferMaterialLotList.stream().filter(item -> item.getMaterialCode().startsWith("3")).map(HmeCosYieldComputeVO::getMaterialLotId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(waferBegin3MaterialLotIdList)) {
                //cosTypeMaterialLotList 存储的是以3开头的满足wafer要求的物料批，也满足cosType要求的物料批
                List<HmeCosYieldComputeVO> cosTypeMaterialLotList = hmeCosYieldComputeMapper.getCosTypeMaterialLotId(tenantId, "COS_TYPE", cosTypeList, waferBegin3MaterialLotIdList);
                if (CollectionUtils.isNotEmpty(cosTypeMaterialLotList)) {
                    List<String> waferNotBegin3MaterialLotIdList = new ArrayList<>();
                    waferNotBegin3MaterialLotIdList.addAll(waferMaterialLotIdList);
                    waferNotBegin3MaterialLotIdList.removeAll(waferBegin3MaterialLotIdList);
                    //cosTypeMaterialLotList2  存储的是满足wafer要求的编码不以3开头的物料批
                    List<HmeCosYieldComputeVO> cosTypeMaterialLotList2 = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(waferNotBegin3MaterialLotIdList)){
                        cosTypeMaterialLotList2.addAll(hmeCosYieldComputeMapper.getCosTypeMaterialLotId(tenantId, "COS_TYPE", cosTypeList, waferNotBegin3MaterialLotIdList));
                    }
                    //再加上以3开头的物料批，最终得到的是满足wafer要求，同时满足cosType要求的物料批
                    cosTypeMaterialLotList2.addAll(cosTypeMaterialLotList);
                    List<String> allMaterialLotIdList = cosTypeMaterialLotList2.stream().map(HmeCosYieldComputeVO::getMaterialLotId).collect(Collectors.toList());
                    //mfFlagMaterialLotIdList 存储的是上一步的所有物料批中在制品标识为Y的物料批
                    List<String> mfFlagMaterialLotIdList = hmeCosYieldComputeMapper.getMfFlagMaterialLotId(tenantId, allMaterialLotIdList);
                    //waferMfFlagMaterialLotList 存储的是所有wafer下物料批中在制品标识为Y的物料批
                    List<String> waferMfFlagMaterialLotList = hmeCosYieldComputeMapper.getMfFlagMaterialLotId(tenantId, allMaterialLotIdList);
                    for (HmeMaterialLotLoad cosTypeWaferCompose : cosTypeWaferComposeList) {
                        //查询当前cosType+wafer组合下是否存在以3开头的物料批中
                        String cosType = cosTypeWaferCompose.getAttribute1();
                        String wafer = cosTypeWaferCompose.getAttribute2();
                        List<String> waferMaterialLotId = waferMaterialLotList.stream()
                                .filter(item -> wafer.equals(item.getAttrValue()))
                                .map(HmeCosYieldComputeVO::getMaterialLotId)
                                .collect(Collectors.toList());
                        List<String> cosTypeMaterialLotId = cosTypeMaterialLotList.stream()
                                .filter(item -> cosType.equals(item.getAttrValue()))
                                .map(HmeCosYieldComputeVO::getMaterialLotId)
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(cosTypeMaterialLotId) && CollectionUtils.isNotEmpty(waferMaterialLotId)) {
                            cosTypeMaterialLotId.retainAll(waferMaterialLotId);
                            if (CollectionUtils.isNotEmpty(cosTypeMaterialLotId)) {
                                String key = cosType + "#" + wafer;
                                cosTypeWaferMaterialLotIdMap.put(key, cosTypeMaterialLotId);
                                materialLotIdList.addAll(cosTypeMaterialLotId);
                                //查询当前cosType+wafer组合下的所有物料批中是否存在有效的在制品
                                if (CollectionUtils.isNotEmpty(mfFlagMaterialLotIdList) && CollectionUtils.isNotEmpty(enableFlagYMaterialLotIdList)) {
                                    List<String> waferMaterialLotId2 = waferMaterialLotList.stream()
                                            .filter(item -> wafer.equals(item.getAttrValue()))
                                            .map(HmeCosYieldComputeVO::getMaterialLotId)
                                            .collect(Collectors.toList());
                                    List<String> cosTypeMaterialLotId2 = cosTypeMaterialLotList2.stream()
                                            .filter(item -> cosType.equals(item.getAttrValue()))
                                            .map(HmeCosYieldComputeVO::getMaterialLotId)
                                            .collect(Collectors.toList());
                                    cosTypeMaterialLotId2.retainAll(waferMaterialLotId2);
                                    cosTypeMaterialLotId2.retainAll(mfFlagMaterialLotIdList);
                                    cosTypeMaterialLotId2.retainAll(enableFlagYMaterialLotIdList);
                                    if (CollectionUtils.isNotEmpty(cosTypeMaterialLotId2)) {
                                        cosTypeWaferMfFlagMap.put(key, HmeConstants.ConstantValue.YES);
                                    }else{
                                        cosTypeWaferMfFlagMap.put(key, HmeConstants.ConstantValue.NO);
                                    }
                                }else {
                                    cosTypeWaferMfFlagMap.put(key, HmeConstants.ConstantValue.NO);
                                }
                                //查询当前wafer下的所有物料批中是否存在在制品
                                String waferMfFlag = waferMfFlagMap.get(wafer);
                                if (StringUtils.isBlank(waferMfFlag)) {
                                    if(CollectionUtils.isNotEmpty(waferMfFlagMaterialLotList) && CollectionUtils.isNotEmpty(enableFlagYMaterialLotIdList)){
                                        List<String> waferMaterialLotId2 = waferMaterialLotList.stream()
                                                .filter(item -> wafer.equals(item.getAttrValue()))
                                                .map(HmeCosYieldComputeVO::getMaterialLotId)
                                                .collect(Collectors.toList());
                                        waferMaterialLotId2.retainAll(waferMfFlagMaterialLotList);
                                        waferMaterialLotId2.retainAll(enableFlagYMaterialLotIdList);
                                        if(CollectionUtils.isNotEmpty(waferMaterialLotId2)){
                                            waferMfFlagMap.put(wafer, HmeConstants.ConstantValue.YES);
                                        }else {
                                            waferMfFlagMap.put(wafer, HmeConstants.ConstantValue.NO);
                                        }
                                    }else {
                                        waferMfFlagMap.put(wafer, HmeConstants.ConstantValue.NO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        result.setCosTypeWaferMaterialLotIdMap(cosTypeWaferMaterialLotIdMap);
        result.setCosTypeWaferMfFlagMap(cosTypeWaferMfFlagMap);
        result.setMaterialLotIdList(materialLotIdList);
        result.setWaferMfFlagMap(waferMfFlagMap);
        return result;
    }

    @Override
    public HmeCosYieldComputeVO6 createDoc(Long tenantId, BigDecimal targetPassRate, BigDecimal yield, String cosType, String wafer,
                                           List<HmeCosYieldComputeVO5> hmeCosYieldComputeVO5List, Map<String, String> waferMfFlagMap, BigDecimal singleLoadSequenceCount) {
        HmeCosYieldComputeVO6 result = new HmeCosYieldComputeVO6();
        List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList = new ArrayList<>();
        List<HmeCosYieldComputeVO5> monitorRecordInsertDataList = new ArrayList<>();

        //根据cos、wafer查询hme_cos_test_monitor_header表数据
        List<HmeCosYieldComputeVO5> singleCosYieldComputeVO5List = hmeCosYieldComputeVO5List.stream()
                .filter(item -> cosType.equals(item.getCosType()) && wafer.equals(item.getWafer()))
                .sorted(Comparator.comparing(HmeCosYieldComputeVO5::getCreationDate))
                .collect(Collectors.toList());
        if (yield.compareTo(targetPassRate) < 0) {
            //如果计算值小于目标良率，则不合格，根据cosType+wafet取出COS测试良率监控头表数据
            if (CollectionUtils.isEmpty(singleCosYieldComputeVO5List)) {
                //若无数据，则插入hme_cos_test_monitor_header表一条数据
                HmeCosYieldComputeVO5 monitorHeaderInsertData = new HmeCosYieldComputeVO5();
                monitorHeaderInsertData.setTenantId(tenantId);
                monitorHeaderInsertData.setDocStatus(HmeConstants.ConstantValue.NG);
                monitorHeaderInsertData.setCheckStatus("WAIT_CHECK");
                monitorHeaderInsertData.setCosType(cosType);
                monitorHeaderInsertData.setWafer(wafer);
                monitorHeaderInsertData.setTestPassRate(yield);
                monitorHeaderInsertData.setTestQty(singleLoadSequenceCount.longValue());
                monitorHeaderInsertDataList.add(monitorHeaderInsertData);
            } else {
                //若有数据，则插入hme_cos_test_monitor_record表一条数据
                HmeCosYieldComputeVO5 hmeCosYieldComputeVO5 = singleCosYieldComputeVO5List.get(0);
                HmeCosYieldComputeVO5 monitorRecordInsertData = new HmeCosYieldComputeVO5();
                monitorRecordInsertData.setTenantId(tenantId);
                monitorRecordInsertData.setCosMonitorHeaderId(hmeCosYieldComputeVO5.getCosMonitorHeaderId());
                monitorRecordInsertData.setMonitorDocNum(hmeCosYieldComputeVO5.getMonitorDocNum());
                monitorRecordInsertData.setDocStatus(hmeCosYieldComputeVO5.getDocStatus());
                monitorRecordInsertData.setCheckStatus(hmeCosYieldComputeVO5.getCheckStatus());
                monitorRecordInsertData.setCosType(cosType);
                monitorRecordInsertData.setWafer(wafer);
                monitorRecordInsertData.setTestPassRate(yield);
                monitorRecordInsertData.setTestQty(singleLoadSequenceCount.longValue());
                monitorRecordInsertDataList.add(monitorRecordInsertData);
            }
        } else {
            //如果计算值大于等于目标良率，则合格，根据cosType+wafet取出COS测试良率监控头表数据
            if (CollectionUtils.isEmpty(singleCosYieldComputeVO5List)) {
                //若无数据，则插入hme_cos_test_monitor_header表一条数据
                HmeCosYieldComputeVO5 monitorHeaderInsertData = new HmeCosYieldComputeVO5();
                monitorHeaderInsertData.setTenantId(tenantId);
                monitorHeaderInsertData.setDocStatus(HmeConstants.ConstantValue.OK);
                monitorHeaderInsertData.setCheckStatus("WAIT_CHECK");
                monitorHeaderInsertData.setCosType(cosType);
                monitorHeaderInsertData.setWafer(wafer);
                monitorHeaderInsertData.setTestPassRate(yield);
                monitorHeaderInsertData.setTestQty(singleLoadSequenceCount.longValue());
                monitorHeaderInsertDataList.add(monitorHeaderInsertData);
            } else {
                //若有数据，则判断最早的那条数据，单据状态是否为OK
                HmeCosYieldComputeVO5 hmeCosYieldComputeVO5 = singleCosYieldComputeVO5List.get(0);
                if (HmeConstants.ConstantValue.OK.equals(hmeCosYieldComputeVO5.getDocStatus())) {
                    //如果为ok，则插入hme_cos_test_monitor_record表一条数据
                    HmeCosYieldComputeVO5 monitorRecordInsertData = new HmeCosYieldComputeVO5();
                    monitorRecordInsertData.setTenantId(tenantId);
                    monitorRecordInsertData.setCosMonitorHeaderId(hmeCosYieldComputeVO5.getCosMonitorHeaderId());
                    monitorRecordInsertData.setMonitorDocNum(hmeCosYieldComputeVO5.getMonitorDocNum());
                    monitorRecordInsertData.setDocStatus(hmeCosYieldComputeVO5.getDocStatus());
                    monitorRecordInsertData.setCheckStatus(hmeCosYieldComputeVO5.getCheckStatus());
                    monitorRecordInsertData.setCosType(cosType);
                    monitorRecordInsertData.setWafer(wafer);
                    monitorRecordInsertData.setTestPassRate(yield);
                    monitorRecordInsertData.setTestQty(singleLoadSequenceCount.longValue());
                    monitorRecordInsertDataList.add(monitorRecordInsertData);
                } else if (HmeConstants.ConstantValue.NG.equals(hmeCosYieldComputeVO5.getDocStatus())) {
                    //如果为NG,则判断该wafer是含有在制品
                    String waferMfFlag = waferMfFlagMap.getOrDefault(wafer, HmeConstants.ConstantValue.NO);
                    if(HmeConstants.ConstantValue.NO.equals(waferMfFlag)){
                        //若该wafer无在制品，则插入hme_cos_test_monitor_header表一条数据
                        HmeCosYieldComputeVO5 monitorHeaderInsertData = new HmeCosYieldComputeVO5();
                        monitorHeaderInsertData.setTenantId(tenantId);
                        monitorHeaderInsertData.setDocStatus(HmeConstants.ConstantValue.OK);
                        monitorHeaderInsertData.setCheckStatus("WAIT_CHECK");
                        monitorHeaderInsertData.setCosType(cosType);
                        monitorHeaderInsertData.setWafer(wafer);
                        monitorHeaderInsertData.setTestPassRate(yield);
                        monitorHeaderInsertData.setTestQty(singleLoadSequenceCount.longValue());
                        monitorHeaderInsertDataList.add(monitorHeaderInsertData);
                    }else {
                        //若该wafer有在制品，则插入hme_cos_test_monitor_record表一条数据
                        HmeCosYieldComputeVO5 monitorRecordInsertData = new HmeCosYieldComputeVO5();
                        monitorRecordInsertData.setTenantId(tenantId);
                        monitorRecordInsertData.setCosMonitorHeaderId(hmeCosYieldComputeVO5.getCosMonitorHeaderId());
                        monitorRecordInsertData.setMonitorDocNum(hmeCosYieldComputeVO5.getMonitorDocNum());
                        monitorRecordInsertData.setDocStatus(HmeConstants.ConstantValue.OK);
                        monitorRecordInsertData.setCheckStatus(hmeCosYieldComputeVO5.getCheckStatus());
                        monitorRecordInsertData.setCosType(cosType);
                        monitorRecordInsertData.setWafer(wafer);
                        monitorRecordInsertData.setTestPassRate(yield);
                        monitorRecordInsertData.setTestQty(singleLoadSequenceCount.longValue());
                        monitorRecordInsertDataList.add(monitorRecordInsertData);
                    }
                }
            }
        }
        result.setMonitorHeaderInsertDataList(monitorHeaderInsertDataList);
        result.setMonitorRecordInsertDataList(monitorRecordInsertDataList);
        return result;
    }

    @Override
    public HmeCosYieldComputeVO6 createDoc2(Long tenantId, BigDecimal yield, String cosType, String wafer,
                                            List<HmeCosYieldComputeVO5> hmeCosYieldComputeVO5List, BigDecimal singleLoadSequenceCount) {
        HmeCosYieldComputeVO6 result = new HmeCosYieldComputeVO6();
        List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList = new ArrayList<>();
        List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList = new ArrayList<>();

        //根据cos、wafer查询hme_cos_test_monitor_header表数据
        List<HmeCosYieldComputeVO5> singleCosYieldComputeVO5List = hmeCosYieldComputeVO5List.stream()
                .filter(item -> cosType.equals(item.getCosType()) && wafer.equals(item.getWafer()))
                .sorted(Comparator.comparing(HmeCosYieldComputeVO5::getCreationDate))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(singleCosYieldComputeVO5List)){
            //如果找不到数据，则插入hme_cos_test_monitor_header表一条数据
            HmeCosYieldComputeVO5 monitorHeaderInsertData = new HmeCosYieldComputeVO5();
            monitorHeaderInsertData.setTenantId(tenantId);
            monitorHeaderInsertData.setDocStatus("UNKNOWN");
            monitorHeaderInsertData.setCheckStatus("WAIT_CHECK");
            monitorHeaderInsertData.setCosType(cosType);
            monitorHeaderInsertData.setWafer(wafer);
            monitorHeaderInsertData.setTestPassRate(yield);
            monitorHeaderInsertData.setTestQty(singleLoadSequenceCount.longValue());
            monitorHeaderInsertDataList.add(monitorHeaderInsertData);
        }else {
            //如果找到，则更新第一条数据的test_pass_rate  业务指定正常情况下此种场景只会找到一条数据
            HmeCosYieldComputeVO5 hmeCosYieldComputeVO5 = singleCosYieldComputeVO5List.get(0);
            hmeCosYieldComputeVO5.setTestPassRate(yield);
            hmeCosYieldComputeVO5.setTestQty(singleLoadSequenceCount.longValue());
            monitorHeaderUpdateDataList.add(hmeCosYieldComputeVO5);
        }
        result.setMonitorHeaderInsertDataList(monitorHeaderInsertDataList);
        result.setMonitorHeaderUpdateDataList(monitorHeaderUpdateDataList);
        return result;
    }

    @Override
    public void batchInserUpdateCosTestMonitorData(Long tenantId, List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList,
                                                   List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList, List<HmeCosYieldComputeVO5> monitorRecordInsertDataList) {
        Date nowDate = new Date();
        if(CollectionUtils.isNotEmpty(monitorHeaderInsertDataList)){
            int size = monitorHeaderInsertDataList.size();
            //批量插入COS测试良率监控头表
            MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
            mtNumrangeVO9.setObjectCode("COS_TEST_DOC");
            mtNumrangeVO9.setObjectNumFlag("Y");
            mtNumrangeVO9.setNumQty(Long.valueOf(size));
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
            List<String> numberList = mtNumrangeVO8.getNumberList();

            List<String> ids = mtCustomDbRepository.getNextKeys("hme_cos_test_monitor_header_s", size);
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_cos_test_monitor_header_cid_s", size);
            int i = 0;
            for (HmeCosYieldComputeVO5 monitorHeaderInsertData:monitorHeaderInsertDataList) {
                monitorHeaderInsertData.setCosMonitorHeaderId(ids.get(i));
                monitorHeaderInsertData.setMonitorDocNum(numberList.get(i));
                monitorHeaderInsertData.setCid(Long.valueOf(cids.get(i)));
                monitorHeaderInsertData.setObjectVersionNumber(1L);
                monitorHeaderInsertData.setCreationDate(nowDate);
                monitorHeaderInsertData.setCreatedBy(-1L);
                monitorHeaderInsertData.setLastUpdateDate(nowDate);
                monitorHeaderInsertData.setLastUpdatedBy(-1L);
                i++;
            }
            hmeCosYieldComputeMapper.batchInsertCosTestMonitorHeader(tenantId, monitorHeaderInsertDataList);
        }
        if(CollectionUtils.isNotEmpty(monitorHeaderUpdateDataList)){
            //批量更新COS测试良率监控头表
            hmeCosYieldComputeMapper.batchUpdateCosTestMonitorHeader(tenantId, monitorHeaderUpdateDataList);
        }
        if(CollectionUtils.isNotEmpty(monitorRecordInsertDataList)){
            int size = monitorRecordInsertDataList.size();
            //批量插入COS测试良率监控记录表
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_cos_test_monitor_record_s", size);
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_cos_test_monitor_record_cid_s", size);
            int i = 0;
            for (HmeCosYieldComputeVO5 monitorRecordInsertData:monitorRecordInsertDataList) {
                monitorRecordInsertData.setCosMonitorRecordId(ids.get(i));
                monitorRecordInsertData.setCid(Long.valueOf(cids.get(i)));
                monitorRecordInsertData.setObjectVersionNumber(1L);
                monitorRecordInsertData.setCreationDate(nowDate);
                monitorRecordInsertData.setCreatedBy(-1L);
                monitorRecordInsertData.setLastUpdateDate(nowDate);
                monitorRecordInsertData.setLastUpdatedBy(-1L);
                i++;
            }
            hmeCosYieldComputeMapper.batchInsertCosTestMonitorRecord(tenantId, monitorRecordInsertDataList);
        }
    }
}
