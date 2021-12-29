package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.api.dto.MtAssembleConfirmActualDTO;
import tarzan.actual.api.dto.MtAssembleConfirmActualDTO2;
import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.repository.MtAssembleConfirmActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 装配确认实绩，指示执行作业组件材料的装配和确认情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssembleConfirmActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-confirm-actual")
@Api(tags = "MtAssembleConfirmActual")
public class MtAssembleConfirmActualController extends BaseController {

    @Autowired
    private MtAssembleConfirmActualRepository repository;

    @ApiOperation(value = "propertyLimitAssembleConfirmActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAssembleConfirmActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtAssembleConfirmActual param = new MtAssembleConfirmActual();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.propertyLimitAssembleConfirmActualQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleConfirmActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleConfirmActual> assembleConfirmActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String assembleConfirmActualId) {
        ResponseData<MtAssembleConfirmActual> responseData = new ResponseData<MtAssembleConfirmActual>();
        try {
            responseData.setRows(this.repository.assembleConfirmActualPropertyGet(tenantId, assembleConfirmActualId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleConfirmActualPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActual>> assembleConfirmActualPropertyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> assembleConfirmActualIds) {
        ResponseData<List<MtAssembleConfirmActual>> responseData = new ResponseData<List<MtAssembleConfirmActual>>();
        try {
            responseData.setRows(
                            this.repository.assembleConfirmActualPropertyBatchGet(tenantId, assembleConfirmActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitAssembleActualQuery")
    @PostMapping(value = {"/limit-material/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO3>> materialLimitAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO2 dto) {
        ResponseData<List<MtAssembleConfirmActualVO3>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO3>>();
        try {
            responseData.setRows(this.repository.materialLimitAssembleActualQuery(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitAssembleActualTraceQuery")
    @PostMapping(value = {"/limit-eo/trace/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO13>> eoLimitAssembleActualTraceQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO12 dto) {
        ResponseData<List<MtAssembleConfirmActualVO13>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO13>>();
        try {
            responseData.setRows(this.repository.eoLimitAssembleActualTraceQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoUnconfirmedComponentQuery")
    @PostMapping(value = {"/eo-component-unconfirmed/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO4>> eoUnconfirmedComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<List<MtAssembleConfirmActualVO4>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO4>>();
        try {
            responseData.setRows(this.repository.eoUnconfirmedComponentQuery(tenantId, eoId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBypassedComponentQuery")
    @PostMapping(value = {"/eo-bypassed/component/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO4>> eoBypassedComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO5 dto) {
        ResponseData<List<MtAssembleConfirmActualVO4>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO4>>();
        try {
            responseData.setRows(this.repository.eoBypassedComponentQuery(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentIsConfirmedValidate")
    @PostMapping(value = {"/eo-component-confirmed/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentIsConfirmedValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO6 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentIsConfirmedValidate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembleMaterialSubstituteValidate")
    @PostMapping(value = {"/eo-assemble-material/substitute/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssembleMaterialSubstituteValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAssembleMaterialSubstituteValidate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembleMaterialExcessVerify")
    @PostMapping(value = {"/eo-assemble-material/excess/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssembleMaterialExcessVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO9 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAssembleMaterialExcessVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembleQtyExcessVerify")
    @PostMapping(value = {"/eo-assemble-qty/excess/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAssembleQtyExcessVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO8 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAssembleQtyExcessVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woAssembleQtyExcessVerify")
    @PostMapping(value = {"/wo-assemble-qty/excess/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAssembleQtyExcessVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO8 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAssembleQtyExcessVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleConfirmActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtAssembleConfirmActualVO14> assembleConfirmActualUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualDTO2 dto) {
        ResponseData<MtAssembleConfirmActualVO14> responseData = new ResponseData<MtAssembleConfirmActualVO14>();
        try {
            MtAssembleConfirmActualHis param = new MtAssembleConfirmActualHis();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.assembleConfirmActualUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleConfirm")
    @PostMapping(value = {"/eo-component/confirmation"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleConfirm(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleConfirm(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleConfirmValidate")
    @PostMapping(value = {"/eo-component/confirmation/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleConfirmValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String assembleConfirmActualId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleConfirmValidate(tenantId, assembleConfirmActualId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleConfirmCancel")
    @PostMapping(value = {"/eo-component/confirmation-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleConfirmCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleConfirmCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcBackflushProcess")
    @PostMapping(value = {"/eo-wkc/back-flush/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcBackflushProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO10 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoWkcBackflushProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBomToAssembleConfirmActualCopy")
    @PostMapping(value = {"/eo-bom/duplication"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBomToAssembleConfirmActualCopy(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO11 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoBomToAssembleConfirmActualCopy(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitMaterialLotAssembleActualQuery")
    @PostMapping(value = {"/limit-material-lot/eo/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO16>> eoLimitMaterialLotAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO15 dto) {
        ResponseData<List<MtAssembleConfirmActualVO16>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO16>>();
        try {
            responseData.setRows(this.repository.eoLimitMaterialLotAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleConfirmActualBatchUpdate")
    @PostMapping(value = {"/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO26>> assembleConfirmActualBatchUpdate(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO24 dto) {
        ResponseData<List<MtAssembleConfirmActualVO26>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.assembleConfirmActualBatchUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleConfirmActualPropertyQuery")
    @PostMapping(value = {"/confirm/actual/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO18>> propertyLimitAssembleConfirmActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO17 dto) {
        ResponseData<List<MtAssembleConfirmActualVO18>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO18>>();
        try {
            responseData.setRows(this.repository.propertyLimitAssembleConfirmActualPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitAssembleActualBatchQuery")
    @PostMapping(value = {"/eo/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleConfirmActualVO20>> eoLimitAssembleActualBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleConfirmActualVO19 dto) {
        ResponseData<List<MtAssembleConfirmActualVO20>> responseData =
                        new ResponseData<List<MtAssembleConfirmActualVO20>>();
        try {
            responseData.setRows(this.repository.eoLimitAssembleActualBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcBackflushBatchProcess")
    @PostMapping(value = {"/eo-wkc/back-flush/batch/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcBackflushBatchProcess(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtAssembleConfirmActualVO27 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoWkcBackflushBatchProcess(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
