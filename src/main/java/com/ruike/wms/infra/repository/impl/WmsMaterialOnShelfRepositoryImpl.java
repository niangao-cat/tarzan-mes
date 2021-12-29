package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.repository.WmsMaterialOnShelfRepository;
import com.ruike.wms.infra.mapper.WmsMaterialOnShelfMapper;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-06-09 14:59
 */
@Component
public class WmsMaterialOnShelfRepositoryImpl implements WmsMaterialOnShelfRepository {

    @Autowired
    private WmsMaterialOnShelfMapper wmsMaterialOnShelfMapper;

    @Override
    @ProcessLovValue
    public List<WmsMaterialOnShelfBarCodeDTO> selectMaterialLotCondition(Long tenantId, List<String> materialLotIds) {
        return wmsMaterialOnShelfMapper.selectMaterialLotCondition(tenantId, materialLotIds);
    }
}
