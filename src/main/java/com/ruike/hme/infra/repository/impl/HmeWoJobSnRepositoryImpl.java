package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWoJobSn;
import com.ruike.hme.domain.repository.HmeWoJobSnRepository;
import com.ruike.hme.infra.mapper.HmeWoJobSnMapper;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * wo工艺作业记录表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
@Component
public class HmeWoJobSnRepositoryImpl extends BaseRepositoryImpl<HmeWoJobSn> implements HmeWoJobSnRepository {

    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;

    @Autowired
    private MtUserClient userClient;

    /**
     *@description 获取工单数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/12 16:28
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>
     * @param tenantId
     * @param dto*/
    @Override
    public List<HmeWoJobSnReturnDTO> workList(Long tenantId, HmeWoJobSnDTO dto) {
        List<HmeWoJobSnReturnDTO> hmeWoJobSnReturnDTOS = hmeWoJobSnMapper.workList(tenantId, dto);
        for (HmeWoJobSnReturnDTO vo:
        hmeWoJobSnReturnDTOS) {
            //获取创建人
            if (StringUtils.isNotBlank(vo.getCreatedBy())) {
                vo.setRealName(userClient.userInfoGet(tenantId, Long.valueOf(vo.getCreatedBy())).getRealName());
            }
        }
        return hmeWoJobSnReturnDTOS;
    }

    @Override
    public List<HmeWoJobSnReturnDTO4> workDetails(Long tenantId, HmeWoJobSnDTO3 dto) {
        List<HmeWoJobSnReturnDTO4> hmeWoJobSnReturnDTO4s=hmeWoJobSnMapper.workDetails(tenantId,dto);

        return hmeWoJobSnReturnDTO4s;
    }

    /**
     *@description 查询工单组件
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/24 9:51
     *@param tenantId
     *@param workOrderId
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>
     **/
    @Override
    public List<HmeWoJobSnReturnDTO6> component(Long tenantId, String workOrderId) {
        List<HmeWoJobSnReturnDTO6> hmeWoJobSnReturnDTO6s=hmeWoJobSnMapper.component(tenantId,workOrderId);
        return hmeWoJobSnReturnDTO6s;
    }

}
