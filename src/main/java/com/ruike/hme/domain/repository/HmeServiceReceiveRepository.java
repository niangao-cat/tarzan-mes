package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO2;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 营销服务部接收拆箱登记表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
public interface HmeServiceReceiveRepository extends BaseRepository<HmeServiceReceive> {

    /**
     * 扫描物流单号
     *
     * @param tenantId 租户ID
     * @param logisticsNumber 物流单号
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 14:45:15
     * @return com.ruike.hme.domain.vo.HmeServiceReceiveVO
     */
    HmeServiceReceiveVO scanlogisticsNumber(Long tenantId, String logisticsNumber);

    /**
     * 确认保存
     *
     * @param tenantId 租户ID
     * @param dto 保存数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 16:13:53
     * @return com.ruike.hme.api.dto.HmeServiceReceiveDTO2
     */
    HmeServiceReceiveDTO2 confirm(Long tenantId, HmeServiceReceiveDTO2 dto);
}
