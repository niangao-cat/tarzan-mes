package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtRouterDTO;
import tarzan.method.api.dto.MtRouterDTO2;
import tarzan.method.api.dto.MtRouterStepDTO;
import tarzan.method.app.service.MtRouterService;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@RestController("mtRouterController.v1")
@RequestMapping("/v1/{organizationId}/mt-router")
@Api(tags = "MtRouter")
public class MtRouterController extends BaseController {

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtRouterService service;

    @ApiOperation(value = "UI查询工艺路线列表")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtRouterDTO>> queryRouterListForUi(@PathVariable("organizationId") Long tenantId,
                    MtRouterDTO dto, @ApiIgnore @SortDefault(value = MtRouter.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtRouterDTO>> responseData = new ResponseData<Page<MtRouterDTO>>();
        try {
            responseData.setRows(this.service.queryRouterListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询工艺路线明细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterDTO> queryRouteretailForUi(@PathVariable("organizationId") Long tenantId,
                    String routerId) {
        ResponseData<MtRouterDTO> responseData = new ResponseData<MtRouterDTO>();
        try {
            responseData.setRows(this.service.queryRouteretailForUi(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存工艺路线")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveRouterForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.saveRouterForUi(tenantId, dto));
        } catch (MtException mte) {
            responseData.setRows(mte.getCode());
            responseData.setSuccess(false);
            responseData.setMessage(mte.getMessage());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI弹窗提示后确认保存工艺路线")
    @PostMapping(value = {"/confirm/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> confirmSaveRouterForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.confirmSaveRouterForUi(tenantId, dto));
        } catch (MtException mte) {
            responseData.setRows(mte.getCode());
            responseData.setSuccess(false);
            responseData.setMessage(mte.getMessage());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除工艺路线")
    @PostMapping(value = {"/delete/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Integer> deleteRouterForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> rourterIdList) {
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        try {
            responseData.setRows(service.deleteRouterForUi(tenantId, rourterIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI复制工艺路线")
    @PostMapping(value = {"/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> copyRouterForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.service.copyRouterForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouter> routerGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<MtRouter> responseData = new ResponseData<MtRouter>();
        try {
            responseData.setRows(this.mtRouterRepository.routerGet(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouter>> routerBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> routerIds) {
        ResponseData<List<MtRouter>> responseData = new ResponseData<List<MtRouter>>();
        try {
            responseData.setRows(this.mtRouterRepository.routerBatchGet(tenantId, routerIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerCurrentVersionGet")
    @PostMapping(value = {"/current-version"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerCurrentVersionGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String router) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerCurrentVersionGet(tenantId, router));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerAllVersionQuery")
    @PostMapping(value = {"/all-version"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterVO5>> routerAllVersionQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String router) {
        ResponseData<List<MtRouterVO5>> responseData = new ResponseData<List<MtRouterVO5>>();
        try {
            responseData.setRows(this.mtRouterRepository.routerAllVersionQuery(tenantId, router));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerReleasedFlagVerify")
    @PostMapping(value = {"/released-flag/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerReleasedFlagVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerReleasedFlagValidate(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerAvailabilityVerify")
    @PostMapping(value = {"/available/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerAvailabilityVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtRouterRepository.routerAvailabilityValidate(tenantId, routerId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerTypeQuery")
    @PostMapping(value = {"/router-type/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> routerTypeQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerType) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.mtRouterRepository.routerTypeQuery(tenantId, routerType));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerBomGet")
    @PostMapping(value = {"/bom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerBomGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerBomGet(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerCurrentVersionUpdate")
    @PostMapping(value = {"/current-version/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerCurrentVersionUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtRouterRepository.routerCurrentVersionUpdate(tenantId, routerId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerReleasedFlagUpdate")
    @PostMapping(value = {"/released-flag"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerReleasedFlagUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtRouterRepository.routerReleasedFlagUpdate(tenantId, routerId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerDataVerify")
    @PostMapping(value = {"/data/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerDataVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterVO10 routerData) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtRouterRepository.routerDataValidate(tenantId, routerData);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerCopy")
    @PostMapping(value = {"/duplication"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerCopy(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterVO1 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerCopy(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerAllUpdate")
    @PostMapping(value = {"/all/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerAllUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterVO10 routerData,
                    @RequestParam(name = "copyFlag", defaultValue = "N", required = false) String copyFlag) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerAllUpdate(tenantId, routerData, copyFlag));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "primaryRouterLimitNextStepQuery")
    @PostMapping(value = {"/next-step-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterNextStepVO2>> primaryRouterLimitNextStepQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterStepDTO condition) {
        ResponseData<List<MtRouterNextStepVO2>> responseData = new ResponseData<List<MtRouterNextStepVO2>>();
        try {
            MtRouterStep mtRouterStep = new MtRouterStep();
            BeanUtils.copyProperties(condition, mtRouterStep);
            responseData.setRows(this.mtRouterRepository.primaryRouterLimitNextStepQuery(tenantId, mtRouterStep));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "primaryRouterLimitRouterStepGet")
    @PostMapping(value = {"/router-step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterStepVO8>> primaryRouterLimitRouterStepGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtRouterStepDTO condition) {
        ResponseData<List<MtRouterStepVO8>> responseData = new ResponseData<List<MtRouterStepVO8>>();
        try {
            MtRouterStep mtRouterStep = new MtRouterStep();
            BeanUtils.copyProperties(condition, mtRouterStep);
            responseData.setRows(this.mtRouterRepository.primaryRouterLimitRouterStepGet(tenantId, mtRouterStep));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerAllQuery")
    @PostMapping(value = {"/all-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtRouterVO10> routerAllQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<MtRouterVO10> responseData = new ResponseData<MtRouterVO10>();
        try {
            responseData.setRows(this.mtRouterRepository.routerAllQuery(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerAutoRevisionGet")
    @PostMapping(value = {"/auto-revision"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerAutoRevisionGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String routerId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerAutoRevisionGet(tenantId, routerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "sourceRouterLimitRouterAllUpdate")
    @PostMapping(value = {"/source-router/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> sourceRouterLimitRouterAllUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterVO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.sourceRouterLimitRouterAllUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRelaxedFlowVerify")
    @PostMapping(value = {"/relaxed-flow/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRelaxedFlowVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.eoRelaxedFlowVerify(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerCut")
    @PostMapping(value = {"/cutting"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> routerCut(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterVO9 cutVO) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtRouterRepository.routerCut(tenantId, cutVO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoPrimaryRouterBatchValidate")
    @PostMapping(value = {"/primary-router/batch/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtRouterVO21>> eoPrimaryRouterBatchValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtRouterVO8> dtoList) {
        ResponseData<List<MtRouterVO21>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.mtRouterRepository.eoPrimaryRouterBatchValidate(tenantId, dtoList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
