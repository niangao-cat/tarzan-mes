package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosYieldComputeService;
import com.ruike.hme.domain.entity.HmeCosTestPassRate;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeCosYieldComputeRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosYieldComputeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.tarzan.common.domain.util.CollectorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * COS良率计算定时任务应用服务默认实现
 *
 * @author: chaonan.hu@hand-china.com 2021-09-17 11:35:12
 **/
@Service
@Slf4j
public class HmeCosYieldComputeServiceImpl implements HmeCosYieldComputeService {

    @Autowired
    private HmeCosYieldComputeRepository hmeCosYieldComputeRepository;
    @Autowired
    private HmeCosYieldComputeMapper hmeCosYieldComputeMapper;

    @Override
    public void cosYieldComputeJob(Long tenantId) {
        //查询上次Job执行时间
        Date lastJobDate = hmeCosYieldComputeRepository.getLastJobDate(tenantId);
        //查询芯片性能表中最后更新时间大于上次Job执行时间的LoadSequence
        List<String> loadSequenceList = hmeCosYieldComputeMapper.getLoadSequenceByFunctionUpdatedDate(tenantId, lastJobDate);
        if (CollectionUtils.isNotEmpty(loadSequenceList)) {
            //根据LoadSequence集合查询cos类型和wafer的组合
            List<HmeMaterialLotLoad> cosTypeWaferComposeList = hmeCosYieldComputeMapper.getCosTypeWaferComposeByLoadSequence(tenantId, loadSequenceList);
            if (CollectionUtils.isNotEmpty(cosTypeWaferComposeList)) {
                List<String> cosTypeList = cosTypeWaferComposeList.stream().map(HmeMaterialLotLoad::getAttribute1).distinct().collect(Collectors.toList());
                List<String> waferList = cosTypeWaferComposeList.stream().map(HmeMaterialLotLoad::getAttribute2).distinct().collect(Collectors.toList());
                //查询每个COS类型与WAFER的组合下有多少物料批
                HmeCosYieldComputeVO2 hmeCosYieldComputeVO2 = hmeCosYieldComputeRepository.getCosTypeWaferMaterialLotIdMap(
                        tenantId, cosTypeList, waferList, cosTypeWaferComposeList);
                Map<String, List<String>> cosTypeWaferMaterialLotIdMap = hmeCosYieldComputeVO2.getCosTypeWaferMaterialLotIdMap();
                Map<String, String> cosTypeWaferMfFlagMap = hmeCosYieldComputeVO2.getCosTypeWaferMfFlagMap();
                Map<String, String> waferMfFlagMap = hmeCosYieldComputeVO2.getWaferMfFlagMap();
                List<String> materialLotIdList = hmeCosYieldComputeVO2.getMaterialLotIdList();
                if (cosTypeWaferMaterialLotIdMap.isEmpty()) {
                    return;
                }
                //查询物料批的产出数
                List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    List<List<String>> splitMaterialLotIdList = CommonUtils.splitSqlList(materialLotIdList, 1000);
                    for (List<String> splitMaterialLotId : splitMaterialLotIdList) {
                        hmeEoJobSnList.addAll(hmeCosYieldComputeMapper.getMaterialLotSnQty(tenantId, splitMaterialLotId));
                    }
                }
                List<HmeCosYieldComputeVO3> materialLotOutputNumList = new ArrayList<>();
                for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
                    HmeCosYieldComputeVO3 hmeCosYieldComputeVO3 = new HmeCosYieldComputeVO3();
                    hmeCosYieldComputeVO3.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
                    hmeCosYieldComputeVO3.setAttribute6(hmeEoJobSn.getSnQty());
                    materialLotOutputNumList.add(hmeCosYieldComputeVO3);
                }
                //根据COS类型和wafer的组合查询芯片性能表的LoadSequence及A24
                List<HmeCosYieldComputeVO4> loadSequenceA24List = hmeCosYieldComputeMapper.getLoadSequenceByCosTypeWafer(tenantId, cosTypeWaferComposeList);
                //根据COS类型和wafer的组合查询COS测试良率监控头表
                List<HmeCosYieldComputeVO5> hmeCosYieldComputeVO5List = hmeCosYieldComputeMapper.testMonitorHeaderQueryByCosTypeWafer(tenantId, cosTypeWaferComposeList);
                //根据COS类型查询来料良率
                List<HmeCosTestPassRate> inputPassRateList = hmeCosYieldComputeMapper.getInputPassRateByCosType(tenantId, cosTypeList);

                List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList = new ArrayList<>();
                List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList = new ArrayList<>();
                List<HmeCosYieldComputeVO5> monitorRecordInsertDataList = new ArrayList<>();

                for (HmeMaterialLotLoad cosTypeWaferCompose : cosTypeWaferComposeList) {
                    //取出当前cos类型与wafer组合下的产出数
                    String cosType = cosTypeWaferCompose.getAttribute1();
                    String wafer = cosTypeWaferCompose.getAttribute2();
                    String key = cosType + "#" + wafer;
                    List<String> singleMaterialLotIdList = cosTypeWaferMaterialLotIdMap.get(key);
                    if (CollectionUtils.isEmpty(singleMaterialLotIdList)) {
                        continue;
                    }
                    BigDecimal outputNum = materialLotOutputNumList.stream()
                            .filter(item -> singleMaterialLotIdList.contains(item.getMaterialLotId()))
                            .collect(CollectorsUtil.summingBigDecimal(a -> a.getAttribute6()));
                    //取出当前cos类型与wafer组合下的来料良率
                    HmeCosTestPassRate hmeCosTestPassRate = inputPassRateList.stream().filter(item -> cosType.equals(item.getCosType())).collect(Collectors.toList()).get(0);
                    BigDecimal inputPassRate = hmeCosTestPassRate.getInputPassRate();
                    BigDecimal targetPassRate = hmeCosTestPassRate.getTargetPassRate();
                    //相乘计算得到基准数量 S
                    BigDecimal datumQty = outputNum.multiply(inputPassRate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                    //取出当前cos类型与wafer组合下的做过性能测试的LoadSequence
                    List<String> singleLoadSequenceList = loadSequenceA24List.stream()
                            .filter(item -> cosType.equals(item.getAttribute1())
                                    && wafer.equals(item.getAttribute2()))
                            .map(HmeCosYieldComputeVO4::getLoadSequence)
                            .distinct().collect(Collectors.toList());
                    BigDecimal singleLoadSequenceCount = BigDecimal.valueOf(singleLoadSequenceList.size());
                    if (singleLoadSequenceCount.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    if (singleLoadSequenceCount.compareTo(datumQty) >= 0) {
                        //如果LoadSequence的数量大于等于基准数量，则进行良率计算 良率P=A24无值的数量/全部数量
                        long a24IsNullLoadSequenceCount = loadSequenceA24List.stream()
                                .filter(item -> singleLoadSequenceList.contains(item.getLoadSequence())
                                        && StringUtils.isBlank(item.getA24()))
                                .map(HmeCosYieldComputeVO4::getLoadSequence)
                                .distinct().count();
                        BigDecimal yield = BigDecimal.valueOf(a24IsNullLoadSequenceCount).divide(singleLoadSequenceCount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                        //创建单据
                        HmeCosYieldComputeVO6 doc = hmeCosYieldComputeRepository.createDoc(tenantId, targetPassRate, yield, cosType, wafer, hmeCosYieldComputeVO5List, waferMfFlagMap, singleLoadSequenceCount);
                        if (CollectionUtils.isNotEmpty(doc.getMonitorHeaderInsertDataList())) {
                            monitorHeaderInsertDataList.addAll(doc.getMonitorHeaderInsertDataList());
                        }
                        if (CollectionUtils.isNotEmpty(doc.getMonitorRecordInsertDataList())) {
                            monitorRecordInsertDataList.addAll(doc.getMonitorRecordInsertDataList());
                        }
                    } else {
                        //如果LoadSequence的数量小于基准数量，则判断当前cos类型与wafer组合下是否有在制品
                        String mfFlag = cosTypeWaferMfFlagMap.getOrDefault(key, HmeConstants.ConstantValue.NO);
                        if (HmeConstants.ConstantValue.NO.equals(mfFlag)) {
                            //如果没有在制品，则进行良率计算 良率P=A24无值的数量/全部数量
                            long a24IsNullLoadSequenceCount = loadSequenceA24List.stream()
                                    .filter(item -> singleLoadSequenceList.contains(item.getLoadSequence())
                                            && StringUtils.isBlank(item.getA24()))
                                    .map(HmeCosYieldComputeVO4::getLoadSequence)
                                    .distinct().count();
                            BigDecimal yield = BigDecimal.valueOf(a24IsNullLoadSequenceCount).divide(singleLoadSequenceCount, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                            HmeCosYieldComputeVO6 doc = hmeCosYieldComputeRepository.createDoc2(tenantId, yield, cosType, wafer, hmeCosYieldComputeVO5List, singleLoadSequenceCount);
                            if (CollectionUtils.isNotEmpty(doc.getMonitorHeaderInsertDataList())) {
                                monitorHeaderInsertDataList.addAll(doc.getMonitorHeaderInsertDataList());
                            }
                            if (CollectionUtils.isNotEmpty(doc.getMonitorHeaderUpdateDataList())) {
                                monitorHeaderUpdateDataList.addAll(doc.getMonitorHeaderUpdateDataList());
                            }
                        }
                    }
                }

                //批量插入或更新数据
                hmeCosYieldComputeRepository.batchInserUpdateCosTestMonitorData(tenantId, monitorHeaderInsertDataList, monitorHeaderUpdateDataList, monitorRecordInsertDataList);
            }
        }
    }
}
