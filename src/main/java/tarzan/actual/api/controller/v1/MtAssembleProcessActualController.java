package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtAssembleProcessActual;
import tarzan.actual.domain.repository.MtAssembleProcessActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 装配过程实绩，记录每一次执行作业的材料明细装配记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssembleProcessActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-process-actual")
@Api(tags = "MtAssembleProcessActual")
public class MtAssembleProcessActualController extends BaseController {

    @Autowired
    private MtAssembleProcessActualRepository repository;

    @ApiOperation(value = "assembleProcessActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleProcessActual> assembleProcessActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String assembleProcessActualId) {
        ResponseData<MtAssembleProcessActual> responseData = new ResponseData<MtAssembleProcessActual>();
        try {
            responseData.setRows(this.repository.assembleProcessActualPropertyGet(tenantId, assembleProcessActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleProcessActualPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleProcessActual>> assembleProcessActualPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assembleProcessActualIds) {
        ResponseData<List<MtAssembleProcessActual>> responseData = new ResponseData<List<MtAssembleProcessActual>>();
        try {
            responseData.setRows(
                            this.repository.assembleProcessActualPropertyBatchGet(tenantId, assembleProcessActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleProcessActualQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssembleProcessActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitAssembleProcessActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitAssembleActualQuery")
    @PostMapping(value = {"/component-limit/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleProcessActualVO3>> componentLimitAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO2 dto) {
        ResponseData<List<MtAssembleProcessActualVO3>> responseData =
                        new ResponseData<List<MtAssembleProcessActualVO3>>();
        try {
            responseData.setRows(this.repository.componentLimitAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitComponentAssembleActualQuery")
    @PostMapping(value = {"/event-limit/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleProcessActual>> eventLimitComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO4 dto) {
        ResponseData<List<MtAssembleProcessActual>> responseData = new ResponseData<List<MtAssembleProcessActual>>();
        try {
            responseData.setRows(this.repository.eventLimitComponentAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleProcessActualCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleProcessActualVO6> assembleProcessActualCreate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO1 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtAssembleProcessActualVO6> responseData = new ResponseData<MtAssembleProcessActualVO6>();
        try {
            responseData.setRows(this.repository.assembleProcessActualCreate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualAssembleProcess")
    @PostMapping(value = {"/eo-component/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleProcessActualVO6> eoComponentActualAssembleProcess(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<MtAssembleProcessActualVO6> responseData = new ResponseData<MtAssembleProcessActualVO6>();
        try {
            responseData.setRows(this.repository.eoComponentActualAssembleProcess(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualAssembleCancelProcess")
    @PostMapping(value = {"/eo-component/cancellation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualAssembleCancelProcess(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentActualAssembleCancelProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentAssembleProcess")
    @PostMapping(value = {"/component/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleProcessActualVO6> componentAssembleProcess(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<MtAssembleProcessActualVO6> responseData = new ResponseData<MtAssembleProcessActualVO6>();
        try {
            responseData.setRows(this.repository.componentAssembleProcess(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentAssembleCancelProcess")
    @PostMapping(value = {"/component/cancellation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> componentAssembleCancelProcess(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.componentAssembleCancelProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualScrapProcess")
    @PostMapping(value = {"/eo-component/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualScrapProcess(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtAssembleProcessActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentActualScrapProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualScrapCancelProcess")
    @PostMapping(value = {"/eo-component/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualScrapCancelProcess(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtAssembleProcessActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentActualScrapCancelProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentScrapProcess")
    @PostMapping(value = {"/component/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> componentScrapProcess(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtAssembleProcessActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.componentScrapProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentScrapCancelProcess")
    @PostMapping(value = {"/component/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> componentScrapCancelProcess(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtAssembleProcessActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.componentScrapCancelProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualRemoveProcess")
    @PostMapping(value = {"/eo-component/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualRemoveProcess(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentActualRemoveProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentRemoveProcess")
    @PostMapping(value = {"/component/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> componentRemoveProcess(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.componentRemoveProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssemblePointComponentAssembleProcess")
    @PostMapping(value = {"/eo-component/point/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssemblePointComponentAssembleProcess(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAssemblePointComponentAssembleProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembleGroupComponentAssembleProcess")
    @PostMapping(value = {"/eo-components/group/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssembleGroupComponentAssembleProcess(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtAssembleProcessActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAssembleGroupComponentAssembleProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleProActAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> assembleProActAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.assembleProActAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "assembleProcessActualBatchCreate")
    @PostMapping(value = {"/batch-add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleProcessActualVO13>> assembleProcessActualBatchCreate(@PathVariable("organizationId") Long tenantId,
                                                                                            @RequestBody MtAssembleProcessActualVO12 dto) {
        ResponseData<List<MtAssembleProcessActualVO13>> result = new ResponseData<>();
        try {
            result.setRows(repository.assembleProcessActualBatchCreate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "componentAssembleBatchProcess")
    @PostMapping(value = {"/component/batch/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> componentAssembleBatchProcess(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO16 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.componentAssembleBatchProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualAssembleBatchProcess")
    @PostMapping(value = {"/eo-component/batch-process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleProcessActualVO6>> eoComponentActualAssembleBatchProcess(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO9 dto) {
        ResponseData<List<MtAssembleProcessActualVO6>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoComponentActualAssembleBatchProcess(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembleLimitWoBatchAssemble")
    @PostMapping(value = {"/eo-component/batch/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssembleLimitWoBatchAssemble(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleProcessActualVO18 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            repository.eoAssembleLimitWoBatchAssemble(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}
