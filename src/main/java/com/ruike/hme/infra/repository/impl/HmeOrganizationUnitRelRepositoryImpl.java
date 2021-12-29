package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.domain.vo.HmeOrganizationUnitVO;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import com.ruike.hme.infra.mapper.HmeOrganizationUnitRelMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOrganizationUnitRel;
import com.ruike.hme.domain.repository.HmeOrganizationUnitRelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 组织职能关系表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-28 10:43:15
 */
@Component
public class HmeOrganizationUnitRelRepositoryImpl extends BaseRepositoryImpl<HmeOrganizationUnitRel> implements HmeOrganizationUnitRelRepository {

    private static final String ORGANIZATIONTYPE = "WORKCELL";

    @Autowired
    private HmeOrganizationUnitRelMapper hmeOrganizationUnitRelMapper;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public void saveOrganizationUnitRel(Long tenantId, HmeOrganizationUnitRel unitRel) {
        if(StringUtils.isNotBlank(unitRel.getOrganizationId()) && unitRel.getUnitId() != null){
            HmeOrganizationUnitRel hmeOrganizationUnitRel = hmeOrganizationUnitRelMapper.queryOrganizationUnitByOrganization(unitRel.getOrganizationId(), ORGANIZATIONTYPE);
            if(hmeOrganizationUnitRel != null){
                //更新
                hmeOrganizationUnitRel.setUnitId(unitRel.getUnitId());
                hmeOrganizationUnitRelMapper.updateByPrimaryKeySelective(hmeOrganizationUnitRel);
            }else {
                //新建
                unitRel.setOrganizationType(ORGANIZATIONTYPE);
                self().insertSelective(unitRel);
            }
        }
    }

    @Override
    public HmeOrganizationUnitVO queryOrganizationUnitRel(Long tenantId,String organizationId) {
        HmeOrganizationUnitVO unitVO = new HmeOrganizationUnitVO();
        HmeOrganizationUnitRel hmeOrganizationUnitRel = hmeOrganizationUnitRelMapper.queryOrganizationUnitByOrganization(organizationId, ORGANIZATIONTYPE);
        if(hmeOrganizationUnitRel != null){
            BeanUtils.copyProperties(hmeOrganizationUnitRel,unitVO);
            ResponseEntity<HmeHzeroPlatformUnitDTO> unitsInfo = hmeHzeroPlatformFeignClient.getUnitsInfo(tenantId, hmeOrganizationUnitRel.getUnitId().toString());
            if(unitsInfo.getBody() != null){
                unitVO.setUnitName(unitsInfo.getBody().getUnitName());
            }
        }
        return unitVO;
    }


}
