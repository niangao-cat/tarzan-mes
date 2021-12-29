package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO;
import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * @Author tong.li
 * @Date 2020/4/24 12:49
 * @Version 1.0
 */
public interface WmsCheckedWaitGroudingService {

    /**
     * 已收待上架看板 数据查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return : java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO>
     * @author tong.li 2020/4/24 12:59
     */
    List<WmsCheckedWaitGroudingDTO> queryTaskData(Long tenantId, PageRequest pageRequest);

    /**
     * 30天物料上架量查询
     *
     * @param tenantId    1
     * @param pageRequest 2
     * @return java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2>
     * @author tong.li 2020/4/26 16:19
     */
    List<WmsCheckedWaitGroudingDTO2> materialStoragedNumQuery(Long tenantId, PageRequest pageRequest);

    /**
     * 全年收货量和上架时长趋势图数据查询
     *
     * @param tenantId    1
     * @param pageRequest 2
     * @return java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2>
     * @author tong.li 2020/4/26 16:19
     */
    List<WmsCheckedWaitGroudingDTO2> trendDataQuery(Long tenantId, PageRequest pageRequest);

}
