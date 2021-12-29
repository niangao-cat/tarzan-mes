package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeLogisticsInfoDTO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;

import java.util.List;

/**
 * 物流信息表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-31 11:06:23
 */
public interface HmeLogisticsInfoRepository extends BaseRepository<HmeLogisticsInfo> {

    /**
     * 物流公司值集数据查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/31 11:17:45
     * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
     */
    List<LovValueDTO> logisticsCompanyQuery(Long tenantId);

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
