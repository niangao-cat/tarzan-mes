package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.api.dto.HmeSignInOutRecordDTO8;
import com.ruike.hme.app.service.HmeCosWorkcellExceptionService;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeCosQuantityVO;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2;
import com.ruike.hme.domain.vo.HmeEquipmentVO4;
import com.ruike.hme.infra.mapper.HmeCosWorkcellExceptionMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hpsf.Decimal;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * COS工位加工异常汇总表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 13:05
 */
@Slf4j
@Service
public class HmeCosWorkcellExceptionServiceImpl implements HmeCosWorkcellExceptionService {

    @Autowired
    private HmeCosWorkcellExceptionMapper hmeCosWorkcellExceptionMapper;

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public Page<HmeCosWorkcellExceptionVO> queryList(Long tenantId, HmeCosWorkcellExceptionDTO dto, PageRequest pageRequest) {
        List<HmeCosWorkcellExceptionVO> result = hmeCosWorkcellExceptionMapper.queryList(tenantId, dto);

        Map<HmeCosWorkcellExceptionVO, LongSummaryStatistics> collect = result.stream().collect(
                Collectors.groupingBy(
                        cosWorkcellException -> new HmeCosWorkcellExceptionVO(
                                cosWorkcellException.getWorkOrderNum(),
                                cosWorkcellException.getStatus(),
                                cosWorkcellException.getQty(),
                                cosWorkcellException.getWaferNum(),
                                cosWorkcellException.getCosType(),
                                cosWorkcellException.getDefectCountQuantity(),
                                cosWorkcellException.getDescription(),
                                cosWorkcellException.getRealName(),
                                cosWorkcellException.getWorkcellCode(),
                                cosWorkcellException.getWorkcellName(),
                                cosWorkcellException.getAssetEncoding(),
                                cosWorkcellException.getAssetName(),
                                cosWorkcellException.getMaterialCode(),
                                cosWorkcellException.getMaterialName()
                                ),
                        Collectors.summarizingLong(cosWorkcellException->cosWorkcellException.getDefectCountSum().longValue())
                )
        );

        List<HmeCosWorkcellExceptionVO> hmeCosWorkcellExceptionVOList = new ArrayList<>();
        collect.forEach((k,v) ->{
            k.setDefectCountSum(BigDecimal.valueOf(v.getSum()));
            hmeCosWorkcellExceptionVOList.add(k);
        });

        //计算获取子串开始结束位置
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), hmeCosWorkcellExceptionVOList.size());

        String symbol = "#";
        Map<String , BigDecimal> cosQuantityMap = new HashMap<>();
        Map<String , List<HmeEquipmentVO4>> equipmentMap = new HashMap<>();

        List<HmeCosWorkcellExceptionVO> subCosWorkcellExceptionVOList = hmeCosWorkcellExceptionVOList.subList(fromIndex, toIndex);
        if(CollectionUtils.isNotEmpty(subCosWorkcellExceptionVOList)){
            List<String> workOrderNumList = subCosWorkcellExceptionVOList.stream().map(HmeCosWorkcellExceptionVO::getWorkOrderNum).distinct().collect(Collectors.toList());
            List<String> waferNumList = subCosWorkcellExceptionVOList.stream().map(HmeCosWorkcellExceptionVO::getWaferNum).distinct().collect(Collectors.toList());
            List<String> cosTypeList = subCosWorkcellExceptionVOList.stream().map(HmeCosWorkcellExceptionVO::getCosType).distinct().collect(Collectors.toList());
            List<String> workcellCodeList = subCosWorkcellExceptionVOList.stream().map(HmeCosWorkcellExceptionVO::getWorkcellCode).distinct().collect(Collectors.toList());
            List<String> jobIdList = subCosWorkcellExceptionVOList.stream().map(HmeCosWorkcellExceptionVO::getJobId).distinct().collect(Collectors.toList());

            //批量查询不良总数
            List<HmeCosQuantityVO> hmeCosQuantityVOList = hmeCosWorkcellExceptionMapper.batchQueryQuantity(tenantId,workOrderNumList,waferNumList,cosTypeList,workcellCodeList);
            if(CollectionUtils.isNotEmpty(hmeCosQuantityVOList)){
                cosQuantityMap = hmeCosQuantityVOList.stream().collect(Collectors.toMap(item -> item.getWorkOrderNum() + symbol + item.getWaferNum() + symbol + item.getCosType() + symbol + item.getWorkcellCode(),
                        item -> item.getDefectCountQuantity()));
            }

            //批量查询设备编码、设备描述
            List<HmeEquipmentVO4> hmeEquipmentVO4List = hmeCosWorkcellExceptionMapper.batchQueryEquipment(tenantId,jobIdList);
            if(CollectionUtils.isNotEmpty(hmeEquipmentVO4List)){
                equipmentMap = hmeEquipmentVO4List.stream().collect(Collectors.groupingBy(e -> e.getJobId()));
            }

            String key = null;
            for(HmeCosWorkcellExceptionVO hmeCosWorkcellExceptionVO:subCosWorkcellExceptionVOList) {

                //不良总数
                key = hmeCosWorkcellExceptionVO.getWorkOrderNum() + symbol + hmeCosWorkcellExceptionVO.getWaferNum() + symbol + hmeCosWorkcellExceptionVO.getCosType() + symbol + hmeCosWorkcellExceptionVO.getWorkcellCode();
                hmeCosWorkcellExceptionVO.setDefectCountQuantity(cosQuantityMap.getOrDefault(key , BigDecimal.ZERO));

                //设备编码 设备描述
                StringBuilder assetEncodings = new StringBuilder();
                StringBuilder assetNames = new StringBuilder();
                List<HmeEquipmentVO4> list = equipmentMap.getOrDefault(hmeCosWorkcellExceptionVO.getJobId() , new ArrayList<>());
                if(CollectionUtils.isNotEmpty(list)) {
                    for (HmeEquipmentVO4 hmeEquipment : list) {
                        if (assetEncodings.length() == 0) {
                            assetEncodings.append(hmeEquipment.getAssetEncoding());
                            assetNames.append(hmeEquipment.getAssetName());
                        } else {
                            assetEncodings.append("/" + hmeEquipment.getAssetEncoding());
                            assetNames.append("/" + hmeEquipment.getAssetName());
                        }
                    }
                }
                hmeCosWorkcellExceptionVO.setAssetEncoding(assetEncodings.toString());
                hmeCosWorkcellExceptionVO.setAssetName(assetNames.toString());
            }
        }

        Page<HmeCosWorkcellExceptionVO> resultPage = new Page<>(subCosWorkcellExceptionVOList,
                new PageInfo(pageRequest.getPage(), pageRequest.getSize()),
                hmeCosWorkcellExceptionVOList.size());
        return  resultPage;
    }
}
