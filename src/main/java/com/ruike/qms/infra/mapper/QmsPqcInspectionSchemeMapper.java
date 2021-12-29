package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspectionSchemeHeadDTO;
import com.ruike.qms.api.dto.QmsMisHeadReturnDTO;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检检验计划Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
public interface QmsPqcInspectionSchemeMapper extends BaseMapper<QmsPqcInspectionScheme> {

    /**
     * 头信息查询
     *
     * @param tenantId
     * @param headDTO 查询条件
     * @author sanfeng.zhang@hand-china.com 2020/8/12 18:20
     * @return java.util.List<com.ruike.qms.api.dto.QmsMisHeadReturnDTO>
     */
    List<QmsMisHeadReturnDTO> selectHeadList(@Param("tenantId") Long tenantId, @Param("dto") QmsMaterialInspectionSchemeHeadDTO headDTO);

    /**
     * 查询计划
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/8/15 16:08
     * @return com.ruike.qms.domain.entity.QmsPqcInspectionScheme
     */
    QmsPqcInspectionScheme queryOnePqcInspectionScheme(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcInspectionScheme dto);
}
