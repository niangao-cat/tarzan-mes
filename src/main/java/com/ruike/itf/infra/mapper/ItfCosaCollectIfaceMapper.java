package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfCosaCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片转移接口表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2021-01-21 14:53:19
 */
public interface ItfCosaCollectIfaceMapper extends BaseMapper<ItfCosaCollectIface> {

    void insertIface(@Param("domains") List<ItfCosaCollectIface> domains);

    void updateIface(@Param("domains") List<ItfCosaCollectIface> domains);

    /**
     * 根据目标条码查询挑选批次
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/26 09:45:58
     * @return java.util.List<java.lang.String>
     */
    List<String> targerSelectionLotQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
