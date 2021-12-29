package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeDispositionFunctionDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDetailDTO;
import com.ruike.hme.app.service.HmeDispositionService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;
import tarzan.method.domain.entity.MtDispositionFunction;

import java.util.List;

/**
 * 处置方法与处置组功能 管理 API
 *
 * @author quan.luo@hand-china.com 2020-11-24 10:16:47
 */
@RestController("hmeDispositionController.v1")
@RequestMapping("/v1/{organizationId}/hme-disposition")
@Api(tags = SwaggerApiConfig.HME_DISPOSITION)
public class HmeDispositionController extends BaseController {

    @Autowired
    private HmeDispositionService hmeDispositionService;

    @ApiOperation(value = "处置方法列表查询")
    @GetMapping(value = {"/function-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<HmeDispositionFunctionDTO>> dispositionQuery(@PathVariable("organizationId") Long tenantId,
                                                                          PageRequest pageRequest,
                                                                          HmeDispositionFunctionDTO hmeDispositionFunctionDTO) {
        ResponseData<Page<HmeDispositionFunctionDTO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.queryFunctionByCondition(tenantId, pageRequest, hmeDispositionFunctionDTO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法类型下拉框查询")
    @GetMapping(value = {"/function-type-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenType>> functionTypeQuery(@PathVariable("organizationId") Long tenantId) {
        ResponseData<List<MtGenType>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.functionTypeQuery(tenantId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法新增")
    @PostMapping(value = {"/function-save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtDispositionFunction> functionSave(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtDispositionFunction mtDispositionFunction) {
        ResponseData<MtDispositionFunction> responseData = new ResponseData<>();
        mtDispositionFunction.setTenantId(tenantId);
        validObject(mtDispositionFunction);
        try {
            responseData.setRows(hmeDispositionService.functionSave(tenantId, mtDispositionFunction));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法删除")
    @DeleteMapping(value = {"/function-del"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> functionDel(@PathVariable("organizationId") Long tenantId,
                                                  @RequestParam List<String> dispositionFunctionIdList) {
        ResponseData<List<String>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.functionDel(tenantId, dispositionFunctionIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法组列表查询")
    @GetMapping(value = {"/group-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<HmeDispositionGroupDTO>> groupQuery(@PathVariable("organizationId") Long tenantId,
                                                                 PageRequest pageRequest,
                                                                 HmeDispositionGroupDTO hmeDispositionGroupDTO) {
        ResponseData<Page<HmeDispositionGroupDTO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.queryGroupByCondition(tenantId, pageRequest, hmeDispositionGroupDTO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法组详情查询")
    @GetMapping(value = {"/group-detail-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<HmeDispositionFunctionDTO>> groupDetailQuery(@PathVariable("organizationId") Long tenantId,
                                                                          PageRequest pageRequest,
                                                                          @ApiParam(value = "维护组id", required = true)
                                                                          @RequestParam String dispositionGroupId) {
        ResponseData<Page<HmeDispositionFunctionDTO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.groupDetailQuery(tenantId, pageRequest, dispositionGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法组保存或修改")
    @PostMapping(value = {"/group-save-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> groupSaveOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody HmeDispositionGroupDetailDTO hmeDispositionGroupDetailDTO) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.groupSaveOrUpdate(tenantId, hmeDispositionGroupDetailDTO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法关系数据删除")
    @DeleteMapping(value = {"/group-member-del"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseData<String> groupMemberDel(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeDispositionFunctionDTO hmeDispositionFunctionDTO) {
        ResponseData<String> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.groupMemberDel(tenantId, hmeDispositionFunctionDTO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "处置方法组删除")
    @DeleteMapping(value = {"/group-del"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseData<List<String>> groupDel(@PathVariable("organizationId") Long tenantId,
                                               @RequestParam List<String> dispositionGroupIdList) {
        ResponseData<List<String>> responseData = new ResponseData<>();
        try {
            responseData.setRows(hmeDispositionService.groupDel(tenantId, dispositionGroupIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
