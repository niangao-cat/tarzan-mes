package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsOqcHeader;
import com.ruike.qms.domain.vo.QmsOqcHeaderVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 出库检头表Mapper
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:10
 */
public interface QmsOqcHeaderMapper extends BaseMapper<QmsOqcHeader> {

    /**
     *
     * @Description 查询最近一条检验数据
     *
     * @author yuchao.wang
     * @date 2020/8/28 19:59
     * @param tenantId 租户ID
     * @param materialLotCode 物料批条码
     * @return com.ruike.qms.domain.entity.QmsOqcHeader
     *
     */
    QmsOqcHeader queryLastOqcData(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     *
     * @Description 查询OQC检验头行明细数据
     *
     * @author yuchao.wang
     * @date 2020/8/28 20:09
     * @param oqcHeaderId 头ID
     * @return com.ruike.qms.domain.vo.QmsOqcHeaderVO
     *
     */
    QmsOqcHeaderVO queryOqcDataByHeadId(@Param("oqcHeaderId") String oqcHeaderId);

    /**
     *
     * @Description 根据物料批编码查询条码扫描基础信息
     *
     * @author yuchao.wang
     * @date 2020/8/28 22:30
     * @param tenantId 租户ID
     * @param materialLotCode 物料批编码
     * @return com.ruike.qms.domain.vo.QmsOqcHeaderVO
     *
     */
    QmsOqcHeaderVO queryBaseDataForOqc(@Param("tenantId") Long tenantId,
                                       @Param("materialLotCode") String materialLotCode);

    /**
     *
     * @Description 查询是否有进行中的检验单
     *
     * @author yuchao.wang
     * @date 2020/8/29 11:22
     * @param tenantId 租户ID
     * @param materialLotCode 物料批条码
     * @return java.lang.Integer
     *
     */
    Integer checkProcessing(@Param("tenantId") Long tenantId,
                            @Param("materialLotCode") String materialLotCode);

    /**
     *
     * @Description 根据条码查询检验计划ID
     *
     * @author yuchao.wang
     * @date 2020/8/29 13:45
     * @param tenantId 租户ID
     * @param materialLotCode 物料批条码
     * @return java.lang.String
     *
     */
    String queryInspectionSchemeIdByBarcode(@Param("tenantId") Long tenantId,
                                            @Param("materialLotCode") String materialLotCode);

}
