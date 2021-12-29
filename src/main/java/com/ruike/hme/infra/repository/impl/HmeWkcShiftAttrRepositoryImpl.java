package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeWkcShiftAttrMapper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;
import com.ruike.hme.domain.repository.HmeWkcShiftAttrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 班组交接事项记录表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-31 11:00:48
 */
@Component
public class HmeWkcShiftAttrRepositoryImpl extends BaseRepositoryImpl<HmeWkcShiftAttr> implements HmeWkcShiftAttrRepository {

    @Autowired
    private HmeWkcShiftAttrMapper hmeWkcShiftAttrMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeWkcShiftAttr createOrUpdate(Long tenantId, HmeWkcShiftAttr dto) {
        if(StringUtils.isNotEmpty(dto.getAttrId())){
            //更新
            hmeWkcShiftAttrMapper.updateByPrimaryKeySelective(dto);
        }else{
            //新建
            dto.setTenantId(tenantId);
            this.insertSelective(dto);
        }
        return dto;
    }
}
