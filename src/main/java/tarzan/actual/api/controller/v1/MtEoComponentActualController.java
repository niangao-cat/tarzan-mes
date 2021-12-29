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
import tarzan.actual.api.dto.MtEoComponentActualDTO;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业组件装配实绩，记录执行作业物料和组件实际装配情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoComponentActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-component-actual")
@Api(tags = "MtEoComponentActual")
public class MtEoComponentActualController extends BaseController {

    @Autowired
    private MtEoComponentActualRepository repository;

    @ApiOperation(value = "eoComponentReferencePointQuery")
    @PostMapping(value = {"/reference-point"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO1>> eoComponentReferencePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO dto) {
        ResponseData<List<MtEoComponentActualVO1>> responseData = new ResponseData<List<MtEoComponentActualVO1>>();
        try {
            responseData.setRows(this.repository.eoComponentReferencePointQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualUpdate")
    @PostMapping(value = {"/actual/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtEoComponentActualHis param = new MtEoComponentActualHis();
            BeanUtils.copyProperties(dto, param);
            this.repository.eoComponentActualUpdate(tenantId, param);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleLocatorGet")
    @PostMapping(value = {"/locator/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoComponentAssembleLocatorGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoComponentAssembleLocatorGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentScrap")
    @PostMapping(value = {"/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentScrap(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentScrap(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentScrapCancel")
    @PostMapping(value = {"/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentScrapCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentScrapCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentRemove")
    @PostMapping(value = {"/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentRemove(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentRemove(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentRemoveVerify")
    @PostMapping(value = {"/remove/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentRemoveVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO7 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentRemoveVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssemble")
    @PostMapping(value = {"/assemble"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssemble(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssemble(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleCancel")
    @PostMapping(value = {"/assemble-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO11 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentIsAssembledValidate")
    @PostMapping(value = {"/already-assemble/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentIsAssembledValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO6 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentIsAssembledValidate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentSubstituteQuery")
    @PostMapping(value = {"/substitution/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO8>> eoComponentSubstituteQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO2 dto) {
        ResponseData<List<MtEoComponentActualVO8>> responseData = new ResponseData<List<MtEoComponentActualVO8>>();
        try {
            responseData.setRows(this.repository.eoComponentSubstituteQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO9 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoComponentAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoComponentActual> eoComponentAssembleActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eoComponentActualId) {
        ResponseData<MtEoComponentActual> responseData = new ResponseData<MtEoComponentActual>();
        try {
            responseData.setRows(this.repository.eoComponentAssembleActualPropertyGet(tenantId, eoComponentActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitEoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-component/assemble/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActual>> componentLimitEoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO10 dto) {
        ResponseData<List<MtEoComponentActual>> responseData = new ResponseData<List<MtEoComponentActual>>();
        try {
            responseData.setRows(this.repository.componentLimitEoComponentAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitEoComponentScrapActualQuery")
    @PostMapping(value = {"/limit-component/scrap/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActual>> componentLimitEoComponentScrapActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO10 dto) {
        ResponseData<List<MtEoComponentActual>> responseData = new ResponseData<List<MtEoComponentActual>>();
        try {
            responseData.setRows(this.repository.componentLimitEoComponentScrapActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitEoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-material/assemble/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActual>> materialLimitEoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO9 dto) {
        ResponseData<List<MtEoComponentActual>> responseData = new ResponseData<List<MtEoComponentActual>>();
        try {
            responseData.setRows(this.repository.materialLimitEoComponentAssembleActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitEoComponentScrapActualQuery")
    @PostMapping(value = {"/limit-material/scrap/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActual>> materialLimitEoComponentScrapActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO9 dto) {
        ResponseData<List<MtEoComponentActual>> responseData = new ResponseData<List<MtEoComponentActual>>();
        try {
            responseData.setRows(this.repository.materialLimitEoComponentScrapActualQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoUnassembledComponentQuery")
    @PostMapping(value = {"/unassemble-component/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO13>> eoUnassembledComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO12 dto) {
        ResponseData<List<MtEoComponentActualVO13>> responseData = new ResponseData<List<MtEoComponentActualVO13>>();
        try {
            responseData.setRows(this.repository.eoUnassembledComponentQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssemblePeriodGet")
    @PostMapping(value = {"/period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoComponentActualVO15> eoComponentAssemblePeriodGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO14 dto) {
        ResponseData<MtEoComponentActualVO15> responseData = new ResponseData<MtEoComponentActualVO15>();
        try {
            responseData.setRows(this.repository.eoComponentAssemblePeriodGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembledSubstituteMaterialQuery")
    @PostMapping(value = {"/eo-assembled/substitute-material/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO16>> eoAssembledSubstituteMaterialQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO6 dto) {
        ResponseData<List<MtEoComponentActualVO16>> responseData = new ResponseData<List<MtEoComponentActualVO16>>();
        try {
            responseData.setRows(this.repository.eoAssembledSubstituteMaterialQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAssembledExcessMaterialQuery")
    @PostMapping(value = {"/eo-assembled/excess-material/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO17>> eoAssembledExcessMaterialQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO6 dto) {
        ResponseData<List<MtEoComponentActualVO17>> responseData = new ResponseData<List<MtEoComponentActualVO17>>();
        try {
            responseData.setRows(this.repository.eoAssembledExcessMaterialQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoMaterialLimitComponentQuery")
    @PostMapping(value = {"/limit-material/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoMaterialLimitComponentQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO6 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoMaterialLimitComponentQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentMerge")
    @PostMapping(value = {"/merge"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentMerge(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO18 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentMerge(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentSplit")
    @PostMapping(value = {"/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentSplit(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO19 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentSplit(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentUpdateVerify")
    @PostMapping(value = {"/save/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentUpdateVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO20 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoComponentActualVO24> eoComponentUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO21 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtEoComponentActualVO24> responseData = new ResponseData<MtEoComponentActualVO24>();
        try {
            responseData.setRows(this.repository.eoComponentUpdate(tenantId, dto, fullUpdate));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleBypass")
    @PostMapping(value = {"/assemble/bypass"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleBypass(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO23 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleBypass(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleBypassCancel")
    @PostMapping(value = {"/assemble/bypass-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentAssembleBypassCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO22 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentAssembleBypassCancel(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAllComponentIsConfirmedValidate")
    @PostMapping(value = {"/assemble-finished/confirmation/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAllComponentIsConfirmedValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAllComponentIsConfirmedValidate(tenantId, eoId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoComponentActualPropertyQuery")
    @PostMapping(value = {"/limit-property/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO25>> propertyLimitEoComponentActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoComponentActualVO9 dto) {
        ResponseData<List<MtEoComponentActualVO25>> responseData = new ResponseData<List<MtEoComponentActualVO25>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoComponentActualPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentActualBatchUpdate")
    @PostMapping(value = {"/component-actual/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentActualBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO26 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentActualBatchUpdate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentBatchAssemble")
    @PostMapping(value = {"/batch-assemble"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentBatchAssemble(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoComponentActualVO31 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoComponentBatchAssemble(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentAssembleLocatorBatchGet")
    @PostMapping(value = {"/locator/batch/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoComponentActualVO33>> eoComponentAssembleLocatorBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoComponentActualVO> inputList) {
        ResponseData<List<MtEoComponentActualVO33>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoComponentAssembleLocatorBatchGet(tenantId, inputList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
