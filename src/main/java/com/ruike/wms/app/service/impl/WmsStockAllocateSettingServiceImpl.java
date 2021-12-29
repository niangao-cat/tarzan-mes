package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsStockAllocateSettingDTO;
import com.ruike.wms.app.service.WmsMaterialLotService;
import com.ruike.wms.app.service.WmsStockAllocateSettingService;
import com.ruike.wms.domain.entity.WmsStockAllocateSetting;
import com.ruike.wms.domain.repository.WmsStockAllocateSettingRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsStockAllocateSettingMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.modeling.domain.entity.MtModSite;

import java.util.Objects;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

/**
 * 库存调拨审核设置表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 17:21:32
 */
@Service
public class WmsStockAllocateSettingServiceImpl extends BaseServiceImpl<WmsStockAllocateSetting> implements WmsStockAllocateSettingService {

    @Autowired
    private WmsStockAllocateSettingMapper settingMapper;

    @Autowired
    private WmsStockAllocateSettingRepository settingRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public Page<WmsStockAllocateSettingDTO> listStockSettingForUi(Long tenantId, PageRequest pageRequest, WmsStockAllocateSettingDTO dto) {
        return PageHelper.doPage(pageRequest, () -> settingMapper.selectByConditionForUi(tenantId, dto));
    }

    @Override
    public String saveStockSettingForUi(Long tenantId, WmsStockAllocateSettingDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        // 空值校验
        if (StringUtils.isEmpty(dto.getFromLocatorId())) {
            throw new MtException("WMS_STOCK_SETTING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_STOCK_SETTING_0001", WmsConstant.ConstantValue.WMS, "fromLocatorId"));
        }
        if (StringUtils.isEmpty(dto.getToLocatorId())) {
            throw new MtException("WMS_STOCK_SETTING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_STOCK_SETTING_0001", WmsConstant.ConstantValue.WMS, "toLocatorId"));
        }
        if (StringUtils.isEmpty(dto.getApproveSetting())) {
            throw new MtException("WMS_STOCK_SETTING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_STOCK_SETTING_0001", WmsConstant.ConstantValue.WMS, "approveSetting"));
        }
        // 唯一性校验
        WmsStockAllocateSetting setting = new WmsStockAllocateSetting();
        if (StringUtils.isNotEmpty(dto.getAllocateSettingId())) {
            WmsStockAllocateSetting oldSetting = settingRepository.selectByPrimaryKey(dto.getAllocateSettingId());
            WmsCommonUtils.copyPropertiesIgnoreNullAndTableFields(oldSetting, setting);
        }
        BeanUtils.copyProperties(dto, setting);
        setting.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(setting.getAllocateSettingId())) {
            settingMapper.updateByPrimaryKey(setting);
        } else {
            insertSelective(setting);
        }
        return setting.getAllocateSettingId();
    }
}
