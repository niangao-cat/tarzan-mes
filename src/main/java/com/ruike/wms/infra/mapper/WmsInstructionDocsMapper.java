package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsInstructionDocDTO;
import com.ruike.wms.api.dto.WmsInstructionDocsDTO;
import com.ruike.wms.api.dto.WmsInstructionIdAttrDTO;
import com.ruike.wms.api.dto.WmsInstructionIdLineDTO;
import com.ruike.wms.domain.entity.WmsInstructionDocs;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指令单据头表Mapper
 *
 * @author taowen.wang@hand-china.com 2021-07-07 17:18:05
 */
public interface WmsInstructionDocsMapper extends BaseMapper<WmsInstructionDocs> {
    /**
     * 生产领退料平台头数据
     * @param tenantId
     * @param wmsInstructionDocDTO
     * @return
     */
    List<WmsInstructionDocsDTO> selectByInstructionDocId(@Param(value = "tenantId") Long tenantId,@Param(value = "dto") WmsInstructionDocDTO wmsInstructionDocDTO,@Param(value = "locatorExe") List<String> locatorExe ,@Param(value = "locatorRe") List<String> locatorRe ,@Param(value = "dmType") List<String> dmType);

    /**
     * 生产领退料平台行数据
     * @param tenantId
     * @param instructionDocIds
     * @return
     */
    List<WmsInstructionIdLineDTO> selectByInstructionLinkDoc(@Param(value = "tenantId") Long tenantId,@Param(value = "instructionDocIds") String instructionDocIds);

    /**
     * 生产领退料平台明细数据
     * @param tenantId
     * @param instructionIds
     * @return
     */
    List<WmsInstructionIdAttrDTO> selectByInstructionAttrDoc(@Param(value = "tenantId") Long tenantId,@Param(value = "instructionIds") String instructionIds);

}
