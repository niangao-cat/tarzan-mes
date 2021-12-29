package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeModLocatorVO;
import com.ruike.hme.domain.vo.HmePrepareMaterialVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工序作业平台-SN作业应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
public interface HmeEoJobSnService {
    /**
     * 工单号Lov
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWorkOrderVO dto, PageRequest pageRequest);

    /**
     * 预装物料Lov
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmePrepareMaterialVO> materialQuery(Long tenantId, HmeWorkOrderVO dto, PageRequest pageRequest);

    /**
     * 工单号Lov
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeModLocatorVO> locatorLovQuery(Long tenantId, HmeModLocatorVO dto, PageRequest pageRequest);
}
