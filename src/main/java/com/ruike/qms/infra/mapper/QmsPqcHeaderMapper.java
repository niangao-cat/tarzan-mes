package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsPqcHeaderDTO4;
import com.ruike.qms.domain.entity.QmsPqcHeader;
import com.ruike.qms.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 巡检单头表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:31
 */
public interface QmsPqcHeaderMapper extends BaseMapper<QmsPqcHeader> {

    List<QmsPqcHeaderVO> prodLineQuery(@Param("tenantId") Long tenantId,@Param("prodLineId") String prodLineId);

    QmsPqcHeaderVO2 processQuery(@Param("tenantId") Long tenantId,@Param("workcellId") String workcellId);

    String getWoIdBySn(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    List<QmsPqcHeaderVO3> pqcListQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcHeaderDTO4 dto);

    List<QmsPqcHeaderVO3> pqcListQueryPda(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcHeaderDTO4 dto,
                                       @Param("dateFrom") Date dateFrom, @Param("dateTo") Date dateTo);

    QmsPqcHeaderVO4 headDataQuery(@Param("tenantId") Long tenantId, @Param("pqcHeaderId") String pqcHeaderId);

    List<QmsPqcHeaderVO5> lineDataQuery(@Param("tenantId") Long tenantId, @Param("pqcHeaderId") String pqcHeaderId);

    List<QmsPqcHeaderVO7> pqcResultQuery(@Param("tenantId") Long tenantId, @Param("pqcLineId") String pqcLineId);

    int maxDetailsNum(@Param("tenantId") Long tenantId, @Param("pqcHeaderId") String pqcHeaderId, @Param("pqcLineId") String pqcLineId);

    List<QmsPqcHeaderVO10> areaLovQuery(@Param("tenantId") Long tenantId, @Param("queryInfo") String queryInfo);

    List<QmsPqcHeaderVO11> workshopLovQuery(@Param("tenantId") Long tenantId, @Param("queryInfo") String queryInfo);

    List<QmsPqcHeaderVO12> prodLineLovQuery(@Param("tenantId") Long tenantId, @Param("queryInfo") String queryInfo);

    /**
     * 依据物料编码查询 EO_ID
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.lang.String
     * @auther wenqiang.yin@hand-china.com 2021/1/26 8:55
    */
    String queryEoIdByMaterialLotCode(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 根据SN查询W0 补充方法
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.lang.String
     * @auther wenqiang.yin@hand-china.com 2021/1/26 9:01
    */
    String getWoIdBySnSupplement(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);
}
