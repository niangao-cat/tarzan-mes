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
import tarzan.actual.api.dto.MtWoComponentActualDTO;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.method.domain.vo.MtBomSubstituteVO7;

/**
 * 生产订单组件装配实绩，记录生产订单物料和组件实际装配情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtWorkOrderComponentActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-component-actual")
@Api(tags = "MtWorkOrderComponentActual")
public class MtWorkOrderComponentActualController extends BaseController {

    @Autowired
    private MtWorkOrderComponentActualRepository repository;

    @ApiOperation(value = "woComponentAssembleLocatorGet")
    @PostMapping(value = {"/assemble-locator/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woComponentAssembleLocatorGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO18 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.woComponentAssembleLocatorGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentSubstituteQuery")
    @PostMapping(value = {"/substitute"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSubstituteVO7>> woComponentSubstituteQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO17 dto) {

        ResponseData<List<MtBomSubstituteVO7>> responseData = new ResponseData<List<MtBomSubstituteVO7>>();
        try {
            responseData.setRows(repository.woComponentSubstituteQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentIsAssembledVerify")
    @PostMapping(value = {"/is-assembled/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentIsAssembledVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO27 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentIsAssembledVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentAssemble")
    @PostMapping(value = {"/assemble"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentAssemble(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO1 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentAssemble(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentActualUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentActualUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO2 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentActualUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentRemoveVerify")
    @PostMapping(value = {"/remove/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentRemoveVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentRemoveVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitWoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-material-assemble/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO4>> materialLimitWoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO19 dto) {

        ResponseData<List<MtWoComponentActualVO4>> responseData = new ResponseData<List<MtWoComponentActualVO4>>();
        try {
            List<MtWoComponentActualVO4> rows = repository.materialLimitWoComponentAssembleActualQuery(tenantId, dto);
            responseData.setRows(rows);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitWoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-component-assemble/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO4>> componentLimitWoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO5 dto) {

        ResponseData<List<MtWoComponentActualVO4>> responseData = new ResponseData<List<MtWoComponentActualVO4>>();
        try {
            List<MtWoComponentActualVO4> rows = repository.componentLimitWoComponentAssembleActualQuery(tenantId, dto);
            responseData.setRows(rows);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woUnassembledComponentQuery")
    @PostMapping(value = {"/unassemble-component"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO6>> woUnassembledComponentQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO20 dto) {

        ResponseData<List<MtWoComponentActualVO6>> responseData = new ResponseData<List<MtWoComponentActualVO6>>();
        try {
            List<MtWoComponentActualVO6> rows = repository.woUnassembledComponentQuery(tenantId, dto);
            responseData.setRows(rows);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentReferencePointQuery")
    @PostMapping(value = {"/component-reference-point"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO21>> woComponentReferencePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO18 dto) {

        ResponseData<List<MtWoComponentActualVO21>> responseData = new ResponseData<List<MtWoComponentActualVO21>>();
        try {
            List<MtWoComponentActualVO21> rows = repository.woComponentReferencePointQuery(tenantId, dto);
            responseData.setRows(rows);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentAssembleActualPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWoComponentActualVO4> woComponentAssembleActualPropertyGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String workOrderComponentActualId) {

        ResponseData<MtWoComponentActualVO4> responseData = new ResponseData<MtWoComponentActualVO4>();
        try {
            MtWoComponentActualVO4 v4 =
                            repository.woComponentAssembleActualPropertyGet(tenantId, workOrderComponentActualId);
            if (v4 != null) {
                responseData.setRows(v4);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentRemove")
    @PostMapping(value = {"/remove"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentRemove(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO1 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentRemove(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentScrap")
    @PostMapping(value = {"/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentScrap(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO8 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentScrap(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitWoComponentAssembleActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitWoComponentAssembleActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO9 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            List<String> ls = repository.propertyLimitWoComponentAssembleActualQuery(tenantId, dto);
            if (ls != null) {
                responseData.setRows(ls);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woAssembledExcessMaterialQuery")
    @PostMapping(value = {"/assembled-excess-material"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO10>> woAssembledExcessMaterialQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO22 dto) {

        ResponseData<List<MtWoComponentActualVO10>> responseData = new ResponseData<List<MtWoComponentActualVO10>>();
        try {
            List<MtWoComponentActualVO10> ls = repository.woAssembledExcessMaterialQuery(tenantId, dto);
            if (ls != null) {
                responseData.setRows(ls);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woAssembledSubstituteMaterialQuery")
    @PostMapping(value = {"/assembled-substitute-material"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO11>> woAssembledSubstituteMaterialQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO23 dto) {

        ResponseData<List<MtWoComponentActualVO11>> responseData = new ResponseData<List<MtWoComponentActualVO11>>();
        try {
            List<MtWoComponentActualVO11> ls = repository.woAssembledSubstituteMaterialQuery(tenantId, dto);
            if (ls != null) {
                responseData.setRows(ls);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentScrapCancel")
    @PostMapping(value = {"/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentScrapCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO8 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentScrapCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentAssembleCancel")
    @PostMapping(value = {"/assemble-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentAssembleCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualDTO dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtWoComponentActualVO12 dto1 = new MtWoComponentActualVO12();
            BeanUtils.copyProperties(dto, dto1);

            repository.woComponentAssembleCancel(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitWoComponentScrapActualQuery")
    @PostMapping(value = {"/limit-scrap-actual"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO4>> componentLimitWoComponentScrapActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO5 dto) {

        ResponseData<List<MtWoComponentActualVO4>> responseData = new ResponseData<List<MtWoComponentActualVO4>>();
        try {
            List<MtWoComponentActualVO4> ls = repository.componentLimitWoComponentScrapActualQuery(tenantId, dto);
            if (ls != null) {
                responseData.setRows(ls);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLimitWoComponentScrapActualQuery")
    @PostMapping(value = {"/limit-material-scrap-actual"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWoComponentActualVO4>> materialLimitWoComponentScrapActualQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO19 dto) {

        ResponseData<List<MtWoComponentActualVO4>> responseData = new ResponseData<List<MtWoComponentActualVO4>>();
        try {
            List<MtWoComponentActualVO4> ls = repository.materialLimitWoComponentScrapActualQuery(tenantId, dto);
            if (ls != null) {
                responseData.setRows(ls);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentAssemblePeriodGet")
    @PostMapping(value = {"/assemble-period"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWoComponentActualVO13> woComponentAssemblePeriodGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtWoComponentActualVO24 dto) {

        ResponseData<MtWoComponentActualVO13> responseData = new ResponseData<MtWoComponentActualVO13>();
        try {
            responseData.setRows(repository.woComponentAssemblePeriodGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentSplit")
    @PostMapping(value = {"/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentSplit(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentSplit(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentMerge")
    @PostMapping(value = {"/merge"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentMerge(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO15 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentMerge(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentUpdateVerify")
    @PostMapping(value = {"/save/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentUpdateVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO25 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentActualBatchUpdate")
    @PostMapping(value = {"batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentActualBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtWoComponentActualVO29 dto) {

        ResponseData<Void> responseData = new ResponseData<>();
        try {
            repository.woComponentActualBatchUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentBatchAssemble")
    @PostMapping(value = {"/batch-assemble"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentBatchAssemble(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtWoComponentActualVO33 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.woComponentBatchAssemble(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woComponentUpdate")
    @PostMapping(value = {"/component/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComponentUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtWoComponentActualVO16 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.woComponentUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
