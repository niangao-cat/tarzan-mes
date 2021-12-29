package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspectionSchemeHeadDTO;
import com.ruike.qms.api.dto.QmsMisHeadReturnDTO;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料检验计划Mapper
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:43
 */
public interface QmsMaterialInspSchemeMapper extends BaseMapper<QmsMaterialInspScheme> {
    /**
     * @Description 头信息查询
     * @param tenantId
     * @param headDTO 查询条件
     * @return java.util.List<com.ruike.qms.api.dto.QmsMisHeadReturnDTO>
     * @Date 2020-04-22 10:14
     * @Author han.zhang
     */
    List<QmsMisHeadReturnDTO> selectHeadList(@Param("tenantId") Long tenantId, @Param("dto") QmsMaterialInspectionSchemeHeadDTO headDTO);

    /**
     * 查询物料检验计划
     *
     * @param tenantId      租户id
     * @param dto           参数
     * @author sanfeng.zhang@hand-china.com 2020/9/3 9:51
     * @return com.ruike.qms.domain.entity.QmsMaterialInspScheme
     */
    QmsMaterialInspScheme queryOneMaterialInspectionScheme(@Param("tenantId") Long tenantId, @Param("dto")  QmsMaterialInspScheme dto);
}
