package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeAfterSalesRepair;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * cos冻结转移
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/17 14:48
 */
public interface HmeCosFreezeTransferMapper {

    /**
     * 查询冻结的装载位置个数
     *
     * @param tenantId
     * @param materialLotId
     * @return java.lang.Integer
     * @author sanfeng.zhang@hand-china.com 2021/3/18 14:54
     */
    Integer queryFreezeMaterialLotLoad(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

}
