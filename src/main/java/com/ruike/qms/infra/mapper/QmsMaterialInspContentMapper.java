package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspContentReturnDTO;
import com.ruike.qms.domain.entity.QmsMaterialInspContent;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料检验项目表Mapper
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:42
 */
public interface QmsMaterialInspContentMapper extends BaseMapper<QmsMaterialInspContent> {
    /**
     * @Description 二级页面检验行查询
     * @param tenantId
     * @param inspectionSchemeId
     * @param tagGroupId
     * @return java.util.List<com.ruike.qms.api.dto.QmsMaterialInspContentReturnDTO>
     * @Date 2020-05-13 14:54
     * @Author han.zhang
     */
    List<QmsMaterialInspContentReturnDTO> selectInspectionLine(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "inspectionSchemeId") String inspectionSchemeId,
                                                               @Param(value = "tagGroupId") String tagGroupId);
}
