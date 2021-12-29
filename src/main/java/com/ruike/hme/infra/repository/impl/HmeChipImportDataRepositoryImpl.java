package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeChipImportDTO;
import com.ruike.hme.api.dto.HmeChipImportDTO2;
import com.ruike.hme.api.dto.HmeChipImportDTO3;
import com.ruike.hme.domain.vo.HmeChipImportVO;
import com.ruike.hme.domain.vo.HmeChipImportVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeChipImportDataMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.domain.repository.HmeChipImportDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 六型芯片导入临时表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
@Component
public class HmeChipImportDataRepositoryImpl extends BaseRepositoryImpl<HmeChipImportData> implements HmeChipImportDataRepository {

    @Autowired
    private HmeChipImportDataMapper hmeChipImportDataMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    @ProcessLovValue
    public Page<HmeChipImportVO> headDataQuery(Long tenantId, HmeChipImportDTO dto, PageRequest pageRequest) {
        Page<HmeChipImportVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeChipImportDataMapper.headDataQuery(tenantId, dto));
        List<String> targetBarcodeList = resultPage.getContent().stream().map(HmeChipImportVO::getTargetBarcode).filter(Objects::nonNull).collect(Collectors.toList());
        Map<String, List<HmeChipImportVO>> targetBarcodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(targetBarcodeList)) {
            List<HmeChipImportVO> importVOList = hmeChipImportDataMapper.queryTargetBarcodeInfoList(tenantId, targetBarcodeList);
            targetBarcodeMap = importVOList.stream().collect(Collectors.groupingBy(vo -> vo.getTargetBarcode()));
        }
        for (HmeChipImportVO hmeChipImportData:resultPage) {
            if(HmeConstants.ConstantValue.NO.equals(dto.getPrintFlag())){
                hmeChipImportData.setPrintFlag("N");
            }else{
                hmeChipImportData.setPrintFlag("Y");
            }
            List<HmeChipImportVO> importVOList = targetBarcodeMap.get(hmeChipImportData.getTargetBarcode());
            if (CollectionUtils.isNotEmpty(importVOList)) {
                hmeChipImportData.setTargetBarcodeId(importVOList.get(0).getTargetBarcodeId());
                hmeChipImportData.setPrimaryUomQty(importVOList.get(0).getPrimaryUomQty());
                hmeChipImportData.setMaterialName(importVOList.get(0).getMaterialName());
                hmeChipImportData.setMaterialCode(importVOList.get(0).getMaterialCode());
                hmeChipImportData.setProductDate(importVOList.get(0).getProductDate());
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeChipImportData> lineDataQuery(Long tenantId, HmeChipImportDTO2 dto, PageRequest pageRequest) {
        Page<HmeChipImportData> resultPage = PageHelper.doPage(pageRequest, () -> hmeChipImportDataMapper.lineDataQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> printPdf(Long tenantId, HmeChipImportDTO3 dto, HttpServletResponse response) {
        List<String> kids = new ArrayList<>();
        List<String> materialLotIds = new ArrayList<>();
        for (HmeChipImportVO hmeChipImportVO:dto.getHeadDataList()) {
            List<HmeChipImportVO2> hmeChipImportDataList = hmeChipImportDataMapper.printDataQuery(tenantId, dto.getPrintFlag(), dto.getWorkNum(), dto.getCreationDateFrom(),
                    dto.getCreationDateTo(), hmeChipImportVO);
            if(CollectionUtils.isNotEmpty(hmeChipImportDataList)){
                List<String> kidList = hmeChipImportDataList.stream().map(HmeChipImportVO2::getKid).collect(Collectors.toList());
                List<String> materialLotIdList = hmeChipImportDataList.stream().map(HmeChipImportVO2::getMaterialLotId).collect(Collectors.toList());
                kids.addAll(kidList);
                materialLotIds.addAll(materialLotIdList);
            }
        }
        //更新是否打印标识
        if(CollectionUtils.isNotEmpty(kids)){
            hmeChipImportDataMapper.updatePrintFlag(tenantId, kids);
        }
        return materialLotIds;
    }
}
