package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS报废撤回
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 14:39
 */
public interface HmeCosScrapBackMapper {


    /**
     * COS报废撤回-报废数据查询
     *
     * @param tenantId
     * @param backVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosScrapBackVO2>
     * @author sanfeng.zhang@hand-china.com 2021/1/26 14:48
     */
    List<HmeCosScrapBackVO2> queryCosScrap(@Param("tenantId") Long tenantId, @Param("backVO") HmeCosScrapBackVO backVO);

    /**
     * 批量更新装载信息
     *
     * @param tenantId
     * @param userId
     * @param materialLotLoadList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/27 9:22
     */
    void batchLoadUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("materialLotLoadList") List<HmeMaterialLotLoad> materialLotLoadList);

    /**
     * 批量更新报废信息状态为N
     *
     * @param tenantId
     * @param userId
     * @param cosScrapIdList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/27 9:44
     */
    void batchCosScrapUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("cosScrapIdList") List<String> cosScrapIdList);
}
