package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO2;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配送基础数据表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
public interface WmsDistributionBasicDataMapper extends BaseMapper<WmsDistributionBasicData> {

    /**
     * 配送基础数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/23 14:24:27
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionBasicDataVO>
     */
    List<WmsDistributionBasicDataVO> query(@Param("tenantId") Long tenantId,
                                           @Param("dto") WmsDistributionBasicDataDTO2 dto);

    /**
     * 导出配送基础数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author sanfeng.zhang@hand-china.com 2020/9/2 9:17
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionBasicDataVO>
     */
    List<WmsDistributionBasicDataVO> queryDistributionBasic(@Param("tenantId") Long tenantId,
                                                            @Param("dto") WmsDistributionBasicDataDTO2 dto);
}
