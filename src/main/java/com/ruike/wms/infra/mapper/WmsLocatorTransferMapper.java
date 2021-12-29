package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 库位转移
 * @author: han.zhang
 * @create: 2020/05/08 16:47
 */
public interface WmsLocatorTransferMapper {

    /**
     * @Description 条码查询
     * @param tenantId
     * @param materialLotIdList
     * @return com.ruike.wms.domain.vo.WmsLocatorTransferVO
     * @Date 2020-05-08 17:07
     * @Author han.zhang
     */
    List<WmsLocatorTransferVO> selectMaterialLotInfo(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "materialLotIdList") List<String> materialLotIdList);
}