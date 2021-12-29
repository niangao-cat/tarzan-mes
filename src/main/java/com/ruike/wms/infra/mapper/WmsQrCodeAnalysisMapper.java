package com.ruike.wms.infra.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng 2020-08-24 16:02
 */
public interface WmsQrCodeAnalysisMapper {

    /**
     * 查询条码号是否存在于扩展表
     *
     * @author jiangling.zheng@hand-china.com 2020-08-24 16:02
     * @param tenantId
     * @param attrValue
     * @return
     */
    List<String> selectMaterialLotId(@Param("tenantId") Long tenantId,
                                     @Param("attrValue") String attrValue);

}
