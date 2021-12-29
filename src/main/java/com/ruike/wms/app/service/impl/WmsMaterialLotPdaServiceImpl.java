package com.ruike.wms.app.service.impl;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruike.wms.api.dto.WmsMaterialLotDTO3;
import com.ruike.wms.api.dto.WmsMaterialLotDTO4;
import com.ruike.wms.app.service.WmsMaterialLotPdaService;
import com.ruike.wms.infra.mapper.WmsMaterialLotPdaMapper;

import java.util.*;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * PDA条码管理应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-03 11:15:27
 */
@Service
public class WmsMaterialLotPdaServiceImpl implements WmsMaterialLotPdaService {

    @Autowired
    private WmsMaterialLotPdaMapper wmsMaterialLotPdaMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public Page<WmsMaterialLotDTO3> selectBarCodeCondition(Long tenantId, WmsMaterialLotDTO4 dto, PageRequest pageRequest) {

        if(StringUtils.isBlank(dto.getMaterialLotCode()) && StringUtils.isBlank(dto.getLocatorCode())
                && StringUtils.isBlank(dto.getMaterialCode()) && StringUtils.isBlank(dto.getLogisticsEquipmentCode())){
            //条码号、货位编码、物料编码、物流器具必输其一,请确认
            throw new MtException("WMS_MATERIAL_LOT_PDA_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_LOT_PDA_001", "WMS"));
        }

        return PageHelper
                .doPageAndSort(pageRequest, () -> wmsMaterialLotPdaMapper.selectBarCodeCondition(tenantId, dto));
    }

}
