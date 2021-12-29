package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosRuleDTO;
import com.ruike.hme.api.dto.HmeCosRuleHeadDto;
import com.ruike.hme.api.dto.HmeCosRuleLogicDTO;
import com.ruike.hme.api.dto.HmeCosRuleTypeDTO;
import com.ruike.hme.app.service.HmeCosRuleHeadService;
import com.ruike.hme.domain.entity.HmeCosRuleHead;
import com.ruike.hme.domain.entity.HmeCosRuleLogic;
import com.ruike.hme.domain.entity.HmeCosRuleType;
import com.ruike.hme.domain.repository.HmeCosRuleLogicRepository;
import com.ruike.hme.domain.repository.HmeCosRuleTypeRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * 芯片规则头表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:46
 */
@RestController("hmeCosRuleHeadController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-rule-heads")
@Api(tags = SwaggerApiConfig.HME_COS_RULE)
@Slf4j
public class HmeCosRuleHeadController extends BaseController {

    @Autowired
    private HmeCosRuleTypeRepository hmeCosRuleTypeRepository;

    @Autowired
    private HmeCosRuleLogicRepository hmeCosRuleLogicRepository;

    @Autowired
    private HmeCosRuleHeadService hmeCosRuleHeadService;

    /**
     *@description 查询芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/11 11:36
     *@param tenantId
     *@param dto
     *@param pageRequest
     *@return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeCosRuleHeadDto>>
     **/
    @ApiOperation(value = "查询芯片规则头表")
    @GetMapping(value = "/cosrule/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ProcessLovValue
    public ResponseEntity<Page<HmeCosRuleHeadDto>> cosRuleQuery(@PathVariable("organizationId") Long tenantId,
                                                                HmeCosRuleDTO dto,
                                                                PageRequest pageRequest) {
        log.info("<====HmeCosRuleHeadController-cosRuleQuery:{},{}",tenantId, dto.getCosRuleCode() );
        Page<HmeCosRuleHeadDto> hmeCosRuleHeadDto = hmeCosRuleHeadService.cosRuleQuery(tenantId, dto, pageRequest);
        return Results.success(hmeCosRuleHeadDto);
    }

    /**
     *@description 新增或修改芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:35
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeCosRuleHead>
     **/
    @ApiOperation(value = "新增或修改芯片规则头表")
    @PostMapping(value = "/cosrule/addandupdate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRuleHead> addAndUpdateCosRule(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody HmeCosRuleHead dto) {
        log.info("<====HmeCosContainerController-addAndUpdateContainer:{},{}",tenantId, dto.getCosRuleId() );
        HmeCosRuleHead hmeCosRuleHead = hmeCosRuleHeadService.addAndUpdateCosRule(tenantId, dto);
        return Results.success(hmeCosRuleHead);
    }

    /**
     *@description 删除芯片规则头表
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 19:42
     *@param tenantId
     *@param hmeCosRuleHead
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "删除芯片规则头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cosrule/delete")
    public ResponseEntity<?> deleteCosRule(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody HmeCosRuleHead hmeCosRuleHead) {
        log.info("<====HmeCosContainerController-deleteCosRule:{},{}",tenantId, hmeCosRuleHead.getCosRuleId() );
        hmeCosRuleHeadService.deleteCosRule(hmeCosRuleHead);
        return Results.success();
    }


    /**
     *@description 获取芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:13
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeCosRuleType>>
     **/
    @ApiOperation(value = "获取芯片规则类型")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/cosruletype/query")
    @ProcessLovValue
    public ResponseEntity<Page<HmeCosRuleTypeDTO>> cosRuleTypeQuery(@PathVariable("organizationId") Long tenantId,
                                                                 String cosRuleId,
                                                                 PageRequest pageRequest) {
        log.info("<====HmeCosContainerController-cosRuleTypeQuery:{},{}",tenantId, cosRuleId );
        Page<HmeCosRuleTypeDTO> hmeCosFunctions = hmeCosRuleHeadService.cosRuleTypeQuery(tenantId, cosRuleId, pageRequest);
        return Results.success(hmeCosFunctions);
    }

    /**
     *@description 新增或修改芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:34
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeCosRuleType>
     **/
    @ApiOperation(value = "新增或修改芯片规则类型")
    @PostMapping(value = "/cosrulerype/addandupdate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRuleType> addAndUpdateCosRuleType(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody HmeCosRuleType dto) {
        log.info("<====HmeCosContainerController-addAndUpdateCosRuleType:{},{}",tenantId, dto.getCosRuleTypeId() );
        HmeCosRuleType hmeCosRuleType = hmeCosRuleHeadService.addAndUpdateCosRuleType(tenantId, dto);
        return Results.success(hmeCosRuleType);
    }

    /**
     *@description 删除芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:41
     *@param tenantId
     *@param hmeCosRuleType
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "删除芯片规则类型")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cosruletype/delete")
    public ResponseEntity<?> deleteCosRuleType(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeCosRuleType hmeCosRuleType) {
        log.info("<====HmeCosContainerController-deleteCosRuleType:{},{}",tenantId, hmeCosRuleType.getCosRuleTypeId() );
        hmeCosRuleTypeRepository.deleteByPrimaryKey(hmeCosRuleType);
        return Results.success();
    }

    /**
     *@description 获取芯片规则逻辑
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:32
     *@param tenantId
     *@param cosRuleId
     *@param pageRequest
     *@return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeCosRuleLogic>>
     **/
    @ApiOperation(value = "获取芯片规则逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/cosrulelogic/query")
    @ProcessLovValue
    public ResponseEntity<Page<HmeCosRuleLogicDTO>> cosRuleLogicQuery(@PathVariable("organizationId") Long tenantId,
                                                                  String cosRuleId,
                                                                  PageRequest pageRequest) {
        log.info("<====HmeCosContainerController-cosRuleLogicQuery:{},{}",tenantId, cosRuleId );
        Page<HmeCosRuleLogicDTO> hmeCosRuleLogic = hmeCosRuleHeadService.cosRuleLogicQuery(tenantId, cosRuleId, pageRequest);
        return Results.success(hmeCosRuleLogic);
    }

    /**
     *@description 新增或修改芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:34
     *@param tenantId
     *@param dto
     *@return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeCosRuleType>
     **/
    @ApiOperation(value = "新增或修改芯片规则逻辑")
    @PostMapping(value = "/cosrulelogic/addandupdate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeCosRuleLogic> addAndUpdateCosRuleLogic(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody HmeCosRuleLogic dto) {
        log.info("<====HmeCosContainerController-addAndUpdateCosRuleLogic:{},{}",tenantId, dto.getCosRuleLogicId() );
        HmeCosRuleLogic hmeCosRuleLogic = hmeCosRuleHeadService.addAndUpdateCosRuleLogic(tenantId, dto);
        return Results.success(hmeCosRuleLogic);
    }

    /**
     *@description 删除芯片规则类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/10 20:41
     *@param tenantId
     *@param hmeCosRuleLogic
     *@return org.springframework.http.ResponseEntity<?>
     **/
    @ApiOperation(value = "删除芯片规则逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/cosruleLogic/delete")
    public ResponseEntity<?> deleteCosRuleLogic(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeCosRuleLogic hmeCosRuleLogic) {
        log.info("<====HmeCosContainerController-deleteCosRuleType:{},{}",tenantId, hmeCosRuleLogic.getCosRuleLogicId() );
        hmeCosRuleLogicRepository.deleteByPrimaryKey(hmeCosRuleLogic);
        return Results.success();
    }
}
