package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeLogisticsInfoDTO;
import com.ruike.hme.domain.vo.HmeLogisticsInfoVO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;

/**
 * 物流信息表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-08-31 11:06:23
 */
public interface HmeLogisticsInfoService {

    /**
     * 售后接收-物流公司值集数据查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/31 11:17:45
     * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
     */
    List<LovValueDTO> logisticsCompanyQuery(Long tenantId);

    /**
     * 售后接收-物流单号扫描
     *
     * @param tenantId 租户ID
     * @param logisticsNumber 物流单号
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/31 11:34:04
     * @return com.ruike.hme.domain.vo.HmeLogisticsInfoVO
     */
    HmeLogisticsInfoVO scanLogisticsNum(Long tenantId, String logisticsNumber);

    /**
     * 售后接收-确认接收
     * 
     * @param tenantId 租户ID
     * @param dtoList 接收信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/31 11:56:24 
     * @return com.ruike.hme.api.dto.HmeLogisticsInfoDTO
     */
    List<HmeLogisticsInfoDTO> confirmReceive(Long tenantId, List<HmeLogisticsInfoDTO> dtoList);
}
