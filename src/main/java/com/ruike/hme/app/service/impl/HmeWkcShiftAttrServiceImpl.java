package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeWkcShiftAttrService;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;
import com.ruike.hme.domain.repository.HmeWkcShiftAttrRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 班组交接事项记录表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-31 11:00:48
 */
@Service
public class HmeWkcShiftAttrServiceImpl implements HmeWkcShiftAttrService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeWkcShiftAttrRepository hmeWkcShiftAttrRepository;

    @Override
    public HmeWkcShiftAttr createOrUpdate(Long tenantId, HmeWkcShiftAttr dto) {
        if(StringUtils.isEmpty(dto.getWkcShiftId())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "班次"));
        }
        if(StringUtils.isEmpty(dto.getRemark())){
            throw new MtException("HME_SHIFT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SHIFT_004", "HME"));
        }
        return hmeWkcShiftAttrRepository.createOrUpdate(tenantId, dto);
    }
}
