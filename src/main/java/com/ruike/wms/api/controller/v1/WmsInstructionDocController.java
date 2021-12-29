package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.repository.WmsInstructionDocsRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.general.api.dto.MtUserOrganizationDTO;

import java.util.List;

/**
 * 指令单据头表 管理 API
 *
 * @author taowen.wang@hand-china.com 2021-07-07 17:18:05
 */
@RestController("instructionDocController.v1" )
@RequestMapping("/v1/{organizationId}/instruction-docs")
@Api(tags = "wmsInstructionDoc")
@Slf4j
public class WmsInstructionDocController extends BaseController {
    @Autowired
    private WmsInstructionDocsRepository wmsInstructionDocsRepository;


    @ApiOperation(value = "factoryPermissionQuery")
    @PostMapping(value = {"/factory"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<String>> factoryPermissionQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtUserOrganizationDTO dto) {
        List<String> siteCodes = wmsInstructionDocsRepository.factoryPermissionQuery(tenantId, dto);
        log.info("<====WmsInstructionDocController-factoryPermissionQuery:{},{}", tenantId, dto);
        return Results.success(siteCodes);
    }


    @ApiOperation(value = "materialPermissionQuery")
    @PostMapping(value = {"/material"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsMaterialDTO>> materialPermissionQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody String organizationId) {
        List<WmsMaterialDTO> wmsMaterialDTOS = wmsInstructionDocsRepository.materialPermissionQuery(tenantId, organizationId);
        log.info("<====WmsInstructionDocController-materialPermissionQuery:{},{}", tenantId, organizationId);
        return Results.success(wmsMaterialDTOS);
    }



    @ApiOperation(value = "生产领退料单查询")
    @GetMapping(value = "/query/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsInstructionDocsDTO>> queryHendList(@PathVariable("organizationId") Long tenantId,
                                                                 WmsInstructionDocDTO dto,
                                                                 PageRequest pageRequest) {
        dto.initParam();
        Page<WmsInstructionDocsDTO> wmsInstructionDocsDTOS = wmsInstructionDocsRepository.queryHendList(tenantId, dto, pageRequest);
        log.info("<====WmsInstructionDocController-queryList:{},{}", tenantId, dto);
        return Results.success(wmsInstructionDocsDTOS);
    }

    @ApiOperation(value = "领退料单头获取领退料单行明细")
    @GetMapping(value = "/query/line/{instructionDocIds}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsInstructionIdLineDTO>> queryLinkList(@PathVariable("organizationId") Long tenantId,
                                                                       @PathVariable("instructionDocIds") String instructionDocIds,
                                                                       PageRequest pageRequest) {
        Page<WmsInstructionIdLineDTO> wmsInstructionIdLineDTOS = wmsInstructionDocsRepository.queryLineList(tenantId, instructionDocIds, pageRequest);
        log.info("<====WmsInstructionDocController-queryLineList:{},{}", tenantId, instructionDocIds);
        return Results.success(wmsInstructionIdLineDTOS);
    }

    @ApiOperation(value = "领退料执行实绩明细查看")
    @GetMapping(value = "/query/attr/{instructionIds}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsInstructionIdAttrDTO>> queryLinkAttrList(@PathVariable("organizationId") Long tenantId,
                                                                           @PathVariable("instructionIds") String instructionIds,
                                                                           PageRequest pageRequest) {
        Page<WmsInstructionIdAttrDTO> wmsInstructionIdAttrDTOS = wmsInstructionDocsRepository.queryLineAttrList(tenantId, instructionIds, pageRequest);
        log.info("<====WmsInstructionDocController-queryLineAttrList:{},{}", tenantId, instructionIds);
        return Results.success(wmsInstructionIdAttrDTOS);
    }
}
