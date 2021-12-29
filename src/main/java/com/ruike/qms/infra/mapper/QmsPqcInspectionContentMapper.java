package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspContentReturnDTO;
import com.ruike.qms.domain.entity.QmsPqcInspectionContent;
import com.ruike.qms.domain.vo.QmsPqcInspectionContentVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检检验项目表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:11
 */
public interface QmsPqcInspectionContentMapper extends BaseMapper<QmsPqcInspectionContent> {

    /**
     * 二级页面检验行查询
     *
     * @param tenantId
     * @param inspectionSchemeId
     * @param tagGroupId
     * @author sanfeng.zhang@hand-china.com 2020/8/12 20:09
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcInspectionContentVO>
     */
    List<QmsPqcInspectionContentVO> selectInspectionLine(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "inspectionSchemeId") String inspectionSchemeId,
                                                         @Param(value = "tagGroupId") String tagGroupId);

    /**
     * 查询检验项目内容
     *
     * @param tenantId
     * @param pqcInspectionContent
     * @return java.util.List<com.ruike.qms.domain.entity.QmsPqcInspectionContent>
     * @author sanfeng.zhang@hand-china.com 2021/3/8 14:59
     */
    List<QmsPqcInspectionContent> queryPqcInspectionContent(@Param("tenantId") Long tenantId, @Param("pqcInspectionContent") QmsPqcInspectionContent pqcInspectionContent);
}
