package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosScrap;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 多层条码拆分
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/8 16:19
 */
public interface HmeManyBarcodeSplitMapper {

    /**
     * 根据扩展名和扩展值查扩展表
     * 
     * @param tenantId
     * @param attrName
     * @param attrValue
     * @author sanfeng.zhang@hand-china.com 2021/3/8 16:34 
     * @return java.lang.Integer
     */
    Integer queryExtendAttrList(@Param("tenantId") Long tenantId, @Param("attrName") String attrName, @Param("attrValue") String attrValue);

    /**
     * 查询外箱下所有条码
     *
     * @param tenantId
     * @param barcode
     * @author sanfeng.zhang@hand-china.com 2021/3/8 17:05
     * @return java.util.List<java.lang.String>
     */
    List<String> queryOutBarcode(@Param("tenantId") Long tenantId, @Param("barcode") String barcode);

}
