package com.ruike.wms.app.service;

import java.util.List;

import com.ruike.wms.api.dto.WmsDistDemandQueryDTO;
import com.ruike.wms.api.dto.WmsDistributionDocCreateDTO;
import com.ruike.wms.domain.vo.WmsDistributionDemandVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 配送需求表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
public interface WmsDistributionDemandService {

    /**
     * 根据条件查询列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页条件
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemand>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 04:22:18
     */
    Page<WmsDistributionDemandVO> selectListByCondition(Long tenantId, WmsDistDemandQueryDTO dto, PageRequest pageRequest);

    /**
     * 配送单生成
     *
     * @param tenantId 租户
     * @param dto      需求
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/1 09:47:55
     */
    void distributionDocCreate(Long tenantId, WmsDistributionDocCreateDTO dto);

    /**
     * 配送单生成方式校验
     *
     * @param tenantId
     * @param demandList
     * @return String
     */
    WmsDistributionDocCreateDTO distributionDocCheck(Long tenantId, List<WmsDistributionDemandVO> demandList);

}
