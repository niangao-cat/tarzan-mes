package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO;
import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片实验代码录入Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-11-01 11:05:20
 */
public interface HmeChipLabCodeInputMapper {

    /**
     * 根据物料批ID查询物料批装载信息
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/1 11:46:29
     * @return java.util.List.<com.ruike.hme.domain.vo.HmeChipLabCodeInputVO2>
     */
    List<HmeChipLabCodeInputVO2> materialLotLoadListQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 盒子信息查询
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/1 01:50:11
     * @return com.ruike.hme.domain.vo.HmeChipLabCodeInputVO
     */
    HmeChipLabCodeInputVO materialLotInfoQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
