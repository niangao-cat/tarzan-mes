package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWoJobSn;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * wo工艺作业记录表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-12 10:38:22
 */
public interface HmeWoJobSnRepository extends BaseRepository<HmeWoJobSn> {


    /**
     *@description 获取工单数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/12 16:27
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO>
     * @param tenantId
     * @param dto*/
    List<HmeWoJobSnReturnDTO> workList(Long tenantId, HmeWoJobSnDTO dto);


    List<HmeWoJobSnReturnDTO4> workDetails(Long tenantId, HmeWoJobSnDTO3 dto);

    /**
     *@description 查询工单组件
     *@author wenzhang.yu@hand-china.com
     *@date 2020/9/24 9:51
     *@param tenantId
     *@param workOrderId
     *@return java.util.List<com.ruike.hme.api.dto.HmeWoJobSnReturnDTO6>
     **/
    List<HmeWoJobSnReturnDTO6> component(Long tenantId, String workOrderId);
}
