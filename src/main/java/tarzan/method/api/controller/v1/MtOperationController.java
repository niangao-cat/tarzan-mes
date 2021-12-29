package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.*;
import tarzan.method.app.service.MtOperationService;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.vo.MtOperationVO;
import tarzan.method.domain.vo.MtOperationVO1;
import tarzan.method.domain.vo.MtOperationVO2;
import tarzan.method.domain.vo.MtOperationVO3;

/**
 * 工序 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@RestController("mtOperationController.v1")
@RequestMapping("/v1/{organizationId}/mt-operation")
@Api(tags = "MtOperation")
public class MtOperationController extends BaseController {

    @Autowired
    private MtOperationService service;

    @Autowired
    private MtOperationRepository repository;

    @ApiOperation(value = "UI查询工艺列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtOperationDTO4>> queryOperationListForUi(@PathVariable("organizationId") Long tenantId,
                                                                       MtOperationDTO3 dto, @ApiIgnore @SortDefault(value = MtOperation.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtOperationDTO4>> responseData = new ResponseData<Page<MtOperationDTO4>>();
        try {
            responseData.setRows(service.queryOperationListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询工艺详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtOperationDTO4> queryOperationDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                                   String operationId) {
        ResponseData<MtOperationDTO4> responseData = new ResponseData<MtOperationDTO4>();
        try {
            responseData.setRows(service.queryOperationDetailForUi(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺信息")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveOperationForUi(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtOperationDTO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveOperationForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtOperation> operationGet(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody String operationId) {
        ResponseData<MtOperation> responseData = new ResponseData<MtOperation>();
        try {
            responseData.setRows(repository.operationGet(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOperation>> operationBatchGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody List<String> operationIds) {
        ResponseData<List<MtOperation>> responseData = new ResponseData<List<MtOperation>>();
        try {
            responseData.setRows(repository.operationBatchGet(tenantId, operationIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationCurrentVersionGet")
    @PostMapping(value = {"/current/version"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationCurrentVersionGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtOperationDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(
                    repository.operationCurrentVersionGet(tenantId, dto.getOperationName(), dto.getSiteId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationAllVersionQuery")
    @PostMapping(value = {"/all/version"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationAllVersionQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtOperationDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(
                    repository.operationAllVersionQuery(tenantId, dto.getOperationType(), dto.getSiteId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationTypeQuery")
    @PostMapping(value = {"/limit-type/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationTypeQuery(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtOperationDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.operationTypeQuery(tenantId, dto.getOperationType(), dto.getSiteId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationSpecialRouterGet")
    @PostMapping(value = {"/special/router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationSpecialRouterGet(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String operationId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.operationSpecialRouterGet(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationAvailabilityValidate")
    @PostMapping(value = {"/available/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationAvailabilityValidate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String operationId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.operationAvailabilityValidate(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationCurrentVersionUpdate")
    @PostMapping(value = {"/current/version/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationCurrentVersionUpdate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String operationId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.operationCurrentVersionUpdate(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitOperationPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOperationVO1>> propertyLimitOperationPropertyQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtOperationVO dto) {
        ResponseData<List<MtOperationVO1>> result = new ResponseData<List<MtOperationVO1>>();
        try {
            result.setRows(repository.propertyLimitOperationPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("operationAttrPropertyUpdate")
    @PostMapping(value = "/attr/property/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> operationAttrPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                          @RequestBody MtExtendVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.operationAttrPropertyUpdate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitOperationQuery")
    @PostMapping(value = {"/limit-property/base/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitOperationQuery(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtOperationVO2 vo) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.propertyLimitOperationQuery(tenantId, vo));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitOperationBatchQuery")
    @PostMapping(value = {"/limit-batch-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitOperationBatchQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtOperationVO3 vo) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.propertyLimitOperationBatchQuery(tenantId, vo));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
