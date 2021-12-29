package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeChipImportDTO;
import com.ruike.hme.api.dto.HmeChipImportDTO2;
import com.ruike.hme.api.dto.HmeChipImportDTO3;
import com.ruike.hme.app.service.HmeChipImportDataService;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.domain.repository.HmeChipImportDataRepository;
import com.ruike.hme.domain.vo.HmeChipImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.app.service.WmsMaterialLotPrintService;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 六型芯片导入临时表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
@Service
public class HmeChipImportDataServiceImpl implements HmeChipImportDataService {

    @Autowired
    private HmeChipImportDataRepository hmeChipImportDataRepository;
    @Autowired
    private WmsMaterialLotPrintService wmsMaterialLotPrintService;

    @Override
    public Page<HmeChipImportVO> headDataQuery(Long tenantId, HmeChipImportDTO dto, PageRequest pageRequest) {
        if(StringUtils.isBlank(dto.getPrintFlag())){
            dto.setPrintFlag(HmeConstants.ConstantValue.NO);
        }
        return hmeChipImportDataRepository.headDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public Page<HmeChipImportData> lineDataQuery(Long tenantId, HmeChipImportDTO2 dto, PageRequest pageRequest) {
        if(StringUtils.isBlank(dto.getPrintFlag())){
            dto.setPrintFlag(HmeConstants.ConstantValue.NO);
        }
        return hmeChipImportDataRepository.lineDataQuery(tenantId, dto, pageRequest);
    }

    @Override
    public void printPdf(Long tenantId, HmeChipImportDTO3 dto, HttpServletResponse response) throws Exception {
        if(StringUtils.isBlank(dto.getPrintFlag())){
            dto.setPrintFlag(HmeConstants.ConstantValue.NO);
        }
        List<String> materialLotIdList = hmeChipImportDataRepository.printPdf(tenantId, dto, response);
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            wmsMaterialLotPrintService.multiplePrint(tenantId, materialLotIdList, response);
        }
    }
}
