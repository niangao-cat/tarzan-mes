package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeQuantityAnalyzeDocDTO;
import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2;
import com.ruike.hme.domain.vo.MaterialLotWorkOrderVO;
import com.ruike.hme.domain.vo.QualityAnalyzeRepresentationLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质量文件头表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
public interface HmeQuantityAnalyzeDocMapper extends BaseMapper<HmeQuantityAnalyzeDoc> {

    /**
     * 质量文件头表数据查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 14:42:50
     */
    List<HmeQuantityAnalyzeDocVO> quantityAnalyzeDocQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeQuantityAnalyzeDocDTO dto);

    /**
     * 根据物料批Id查询工单号
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 15:19:03
     */
    @Deprecated
    List<String> getWoByMaterialLot(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据头ID查询行表数据
     *
     * @param tenantId 租户ID
     * @param docId    头ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 15:39:05
     */
    List<HmeQuantityAnalyzeDocVO2> quantityAnalyzeLineQuery(@Param("tenantId") Long tenantId, @Param("docId") String docId);

    /**
     * 查询列表
     *
     * @param tenantId tenantId
     * @param query    查询条件
     * @return java.util.List<com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation.Line>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:00:07
     */
    List<QualityAnalyzeRepresentationLineVO> selectList(@Param("tenantId") Long tenantId,
                                                        @Param("query") QualityAnalyzeQuery query);

    /**
     * 根据物料批Id查询工单号
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批
     * @return java.util.List<java.lang.String>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 15:19:03
     */
    List<MaterialLotWorkOrderVO> getMaterialLotWorkOrder(@Param("tenantId") Long tenantId, @Param("ids") List<String> materialLotIds);
}
