package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsInstructionDocs;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.general.api.dto.MtUserOrganizationDTO;

import java.util.List;


/**
 * 指令单据头表资源库
 *
 * @author taowen.wang@hand-china.com 2021-07-07 17:18:05
 */
public interface WmsInstructionDocsRepository extends BaseRepository<WmsInstructionDocs> {
    /**
     * 工厂编码
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> factoryPermissionQuery(Long tenantId, MtUserOrganizationDTO dto);

    /**
     * 物料编码
     * @param tenantId
     * @param organizationId
     * @return
     */
    List<WmsMaterialDTO> materialPermissionQuery(Long tenantId, String organizationId);

    /**
     * 根据输入查询条件获取生产领退料单头信息
     * @param tenantId
     * @param dto
     * @return
     */
    Page<WmsInstructionDocsDTO> queryHendList(Long tenantId, WmsInstructionDocDTO dto, PageRequest pageRequest);

    /**
     * 根据输入查询条件获取生产领退料单头信息
     * @param tenantId
     * @param instructionDocIds
     * @return
     */
    Page<WmsInstructionIdLineDTO> queryLineList(Long tenantId, String instructionDocIds, PageRequest pageRequest);

    /**
     * 领退料执行实绩明细查看
     * @param tenantId
     * @param instructionIds
     * @return
     */
    Page<WmsInstructionIdAttrDTO> queryLineAttrList(Long tenantId, String instructionIds, PageRequest pageRequest);
}
