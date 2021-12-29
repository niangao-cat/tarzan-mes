package tarzan.dispatch.api.controller.v1;

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
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO1;
import tarzan.dispatch.app.service.MtOperationWkcDispatchRelService;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.*;

/**
 * 工艺和工作单元调度关系表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
@RestController("mtOperationWkcDispatchRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-operation-wkc-dispatch-rel")
@Api(tags = "MtOperationWkcDispatchRel")
public class MtOperationWkcDispatchRelController extends BaseController {

    @Autowired
    private MtOperationWkcDispatchRelRepository repository;

    @Autowired
    private MtOperationWkcDispatchRelService service;

    @ApiOperation(value = "UI查询工艺与工作单元关系列表信息")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtOpWkcDispatchRelDTO1>> queryOpWkcDispatchRelListForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    MtOpWkcDispatchRelDTO dto,
                    @ApiIgnore @SortDefault(value = MtOperationWkcDispatchRel.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtOpWkcDispatchRelDTO1>> responseData = new ResponseData<Page<MtOpWkcDispatchRelDTO1>>();
        try {
            responseData.setRows(service.queryOpWkcDispatchRelListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询工艺与工作单元关系详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtOpWkcDispatchRelDTO1> queryOpWkcDispatchRelDetailForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "工艺与工作单元关系ID", required = true) @RequestParam(
                                    name = "operationWkcDispatchRelId") String operationWkcDispatchRelId) {
        ResponseData<MtOpWkcDispatchRelDTO1> responseData = new ResponseData<MtOpWkcDispatchRelDTO1>();
        try {
            responseData.setRows(service.queryOpWkcDispatchRelDetailForUi(tenantId, operationWkcDispatchRelId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺与工作单元关系")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveOpWkcDispatchRelForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.saveOpWkcDispatchRelForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除工艺与工作单元关系")
    @PostMapping(value = {"/delete/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> deleteOpWkcDispatchRelForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> operationWkcDispatchRelIds) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(service.deleteOpWkcDispatchRelForUi(tenantId, operationWkcDispatchRelIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    

    @ApiOperation(value = "operationShiftLimitAvailableWorkcellQuery")
    @PostMapping(value = "/available/limit-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOpWkcDispatchRelVO2>> operationShiftLimitAvailableWorkcellQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO1 dto) {
        ResponseData<List<MtOpWkcDispatchRelVO2>> responseData = new ResponseData<List<MtOpWkcDispatchRelVO2>>();
        try {
            responseData.setRows(this.repository.operationShiftLimitAvailableWorkcellQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationShiftLimitHighestPriorityWorkcellGet")
    @PostMapping(value = "/highest/limit-shift", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationShiftLimitHighestPriorityWorkcellGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO1 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.operationShiftLimitHighestPriorityWorkcellGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitAvailableWorkcellQuery")
    @PostMapping(value = "/available/limit-operation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOpWkcDispatchRelVO2>> operationLimitAvailableWorkcellQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO3 dto) {
        ResponseData<List<MtOpWkcDispatchRelVO2>> responseData = new ResponseData<List<MtOpWkcDispatchRelVO2>>();
        try {
            responseData.setRows(repository.operationLimitAvailableWorkcellQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitHighestPriorityWkcGet")
    @PostMapping(value = "/highest/limit-operation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationLimitHighestPriorityWkcGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelVO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.operationLimitHighestPriorityWkcGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitAvailableOperationQuery")
    @PostMapping(value = "/available/operation/limit-wkc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOpWkcDispatchRelVO6>> wkcLimitAvailableOperationQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO7 dto) {
        ResponseData<List<MtOpWkcDispatchRelVO6>> responseData = new ResponseData<List<MtOpWkcDispatchRelVO6>>();
        try {
            responseData.setRows(this.repository.wkcLimitAvailableOperationQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "wkcLimitAvailableOperationBatchQuery")
    @PostMapping(value = "/available/operation/limit-batch-wkc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOpWkcDispatchRelVO10>> wkcLimitAvailableOperationBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO9 dto) {
        ResponseData<List<MtOpWkcDispatchRelVO10>> responseData = new ResponseData<List<MtOpWkcDispatchRelVO10>>();
        try {
            responseData.setRows(this.repository.wkcLimitAvailableOperationBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationLimitWkcUniqueValidate")
    @PostMapping(value = "/wkc/unique-validate/limit-operation", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> operationLimitWkcUniqueValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.operationLimitWkcUniqueValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "uniqueWorkcellEoAutoDispatch")
    @PostMapping(value = "/unique-wkc/dispatch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> uniqueWorkcellEoAutoDispatch(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelVO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.uniqueWorkcellEoAutoDispatch(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationWkcRelUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationWkcRelUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelVO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.operationWkcRelUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationWkcRelDelete")
    @PostMapping(value = "/remove", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> operationWkcRelDelete(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtOpWkcDispatchRelVO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.operationWkcRelDelete(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitOperationWkcQuery")
    @PostMapping(value = "/property/limit/wkc-operation/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOpWkcDispatchRelVO8>> propertyLimitOperationWkcQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtOpWkcDispatchRelVO5 dto,
                    @RequestParam(name = "fuzzyQueryFlag", defaultValue = "N",
                                    required = false) String fuzzyQueryFlag) {
        ResponseData<List<MtOpWkcDispatchRelVO8>> responseData = new ResponseData<List<MtOpWkcDispatchRelVO8>>();
        try {
            responseData.setRows(this.repository.propertyLimitOperationWkcQuery(tenantId, dto, fuzzyQueryFlag));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
