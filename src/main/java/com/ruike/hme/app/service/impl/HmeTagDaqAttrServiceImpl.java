package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeTagDaqAttrDTO;
import com.ruike.hme.app.service.HmeTagDaqAttrService;
import com.ruike.hme.domain.entity.HmeTagDaqAttr;
import com.ruike.hme.domain.repository.HmeTagDaqAttrRepository;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovDTO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据项数据采集扩展属性表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 09:52:44
 */
@Service
public class HmeTagDaqAttrServiceImpl implements HmeTagDaqAttrService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeTagDaqAttrRepository hmeTagDaqAttrRepository;

    @Override
    public Page<LovValueDTO> dataCollectionLovQuery(Long tenantId, HmeTagDaqAttrDTO dto, PageRequest pageRequest) {
        if(StringUtils.isEmpty(dto.getEquipmentCategory())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME","设备类别"));
        }
        return hmeTagDaqAttrRepository.dataCollectionLovQuery(tenantId, dto, pageRequest);
    }

    @Override
    public HmeTagDaqAttr createOrUpdate(Long tenantId, HmeTagDaqAttr dto) {
        if(StringUtils.isEmpty(dto.getEquipmentCategory())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME","设备类别"));
        }
        if(StringUtils.isEmpty(dto.getValueField())){
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME","取值字段"));
        }
        if(StringUtils.isNotEmpty(dto.getLimitCond1()) && StringUtils.isEmpty(dto.getCond1Value())){
            throw new MtException("HME_TAG_DAQ_ATTR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_DAQ_ATTR_0001", "HME","条件1限制值"));
        }
        if(StringUtils.isNotEmpty(dto.getLimitCond2()) && StringUtils.isEmpty(dto.getCond2Value())){
            throw new MtException("HME_TAG_DAQ_ATTR_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TAG_DAQ_ATTR_0001", "HME","条件2限制值"));
        }
        return hmeTagDaqAttrRepository.createOrUpdate(tenantId, dto);
    }

    @Override
    public HmeTagDaqAttrVO2 query(Long tenantId, String tagId) {
        if(StringUtils.isEmpty(tagId)){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","tagId"));
        }
        return hmeTagDaqAttrRepository.query(tenantId, tagId);
    }
}
