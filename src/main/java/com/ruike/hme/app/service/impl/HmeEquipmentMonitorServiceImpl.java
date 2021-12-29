package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeEquipmentMonitorService;
import com.ruike.hme.domain.repository.HmeEquipmentMonitorRepository;
import com.ruike.hme.domain.vo.*;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备监控平台应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-16 18:44:54
 */
@Service
public class HmeEquipmentMonitorServiceImpl implements HmeEquipmentMonitorService {

    @Autowired
    private HmeEquipmentMonitorRepository hmeEquipmentMonitorRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public List<HmeEquipmentMonitorVO> departmentDataQuery(Long tenantId, String siteId, String areaCategory) {
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工厂"));
        }
        return hmeEquipmentMonitorRepository.departmentDataQuery(tenantId, siteId, areaCategory);
    }

    @Override
    public List<HmeEquipmentMonitorVO2> workshopDataQuery(Long tenantId, String siteId, String departmentId) {
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工厂"));
        }
        if(StringUtils.isEmpty(departmentId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "事业部"));
        }
        return hmeEquipmentMonitorRepository.workshopDataQuery(tenantId, siteId, departmentId);
    }

    @Override
    public List<HmeEquipmentMonitorVO3> prodLineDataQuery(Long tenantId, String siteId, String workshopId) {
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工厂"));
        }
        if(StringUtils.isEmpty(workshopId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "车间"));
        }
        return hmeEquipmentMonitorRepository.prodLineDataQuery(tenantId, siteId, workshopId);
    }

    @Override
    public HmeEquipmentMonitorVO6 equipmentStatusQuery(Long tenantId, String siteId, String prodLineId) {
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "工厂"));
        }
        if(StringUtils.isEmpty(prodLineId)){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        return hmeEquipmentMonitorRepository.equipmentStatusQuery(tenantId, siteId, prodLineId);
    }

    @Override
    public HmeEquipmentMonitorVO12 equipmentDetailQuery(Long tenantId, HmeEquipmentMonitorVO8 dto) {
        return hmeEquipmentMonitorRepository.equipmentDetailQuery(tenantId, dto);
    }

}
