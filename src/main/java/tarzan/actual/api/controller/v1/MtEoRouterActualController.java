package tarzan.actual.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.vo.*;

/**
 * EO工艺路线实绩 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtEoRouterActualController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-router-actual")
@Api(tags = "MtEoRouterActual")
public class MtEoRouterActualController extends BaseController {

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @ApiOperation(value = "eoOperationLimitCurrentRouterStepGet")
    @PostMapping(value = {"/limit-operation/current/step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO1> eoOperationLimitCurrentRouterStepGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualVO2 vo) {
        ResponseData<MtEoRouterActualVO1> responseData = new ResponseData<MtEoRouterActualVO1>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoOperationLimitCurrentRouterStepGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcLimitCurrentStepWkcActualGet")
    @PostMapping(value = {"/limit-wkc/current/step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO4> eoWkcLimitCurrentStepWkcActualGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualVO3 vo) {
        ResponseData<MtEoRouterActualVO4> responseData = new ResponseData<MtEoRouterActualVO4>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoWkcLimitCurrentStepWkcActualGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterProductionResultAndHisUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO28> eoRouterProductionResultAndHisUpdate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualVO6 vo) {
        ResponseData<MtEoRouterActualVO28> responseData = new ResponseData<MtEoRouterActualVO28>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterProductionResultAndHisUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterActualAndHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO28> eoRouterActualAndHisCreate(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody MtEoRouterActualVO7 vo) {
        ResponseData<MtEoRouterActualVO28> responseData = new ResponseData<MtEoRouterActualVO28>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterActualAndHisCreate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActual> eoRouterPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String eoRouterActualId) {
        ResponseData<MtEoRouterActual> responseData = new ResponseData<MtEoRouterActual>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterPropertyGet(tenantId, eoRouterActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterProductionResultGet")
    @PostMapping(value = {"/production"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO8> eoRouterProductionResultGet(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody String eoRouterActualId) {
        ResponseData<MtEoRouterActualVO8> responseData = new ResponseData<MtEoRouterActualVO8>();
        try {
            responseData.setRows(
                            this.mtEoRouterActualRepository.eoRouterProductionResultGet(tenantId, eoRouterActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepTypeValidate")
    @PostMapping(value = {"/step-type/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO10> eoStepTypeValidate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtEoRouterActualVO9 vo) {

        ResponseData<MtEoRouterActualVO10> responseData = new ResponseData<MtEoRouterActualVO10>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoStepTypeValidate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStepTypeBatchValidate")
    @PostMapping(value = {"/step-type/batch/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualVO49>> eoStepTypeBatchValidate(
            @PathVariable("organizationId") Long tenantId, @RequestBody List<MtEoRouterActualVO9> vo) {
        ResponseData<List<MtEoRouterActualVO49>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoStepTypeBatchValidate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoReturnStepGet")
    @PostMapping(value = {"/return/step"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO11> eoReturnStepGet(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtEoRouterActualVO9 vo) {
        ResponseData<MtEoRouterActualVO11> responseData = new ResponseData<MtEoRouterActualVO11>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoReturnStepGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterActualStatusGenerate")
    @PostMapping(value = {"/status"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRouterActualStatusGenerate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String eoRouterActualId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(
                            this.mtEoRouterActualRepository.eoRouterActualStatusGenerate(tenantId, eoRouterActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterIsSubRouterValidate")
    @PostMapping(value = {"/sub-route/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRouterIsSubRouterValidate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtEoRouterActualVO12 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterIsSubRouterValidate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterMoveIn")
    @PostMapping(value = {"/move-in"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO14> eoRouterMoveIn(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtEoRouterActualVO13 vo) {
        ResponseData<MtEoRouterActualVO14> responseData = new ResponseData<MtEoRouterActualVO14>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterMoveIn(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterMoveInCancel")
    @PostMapping(value = {"/move-in-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoRouterMoveInCancel(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtEoRouterActualVO16 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoRouterMoveInCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "queueProcess")
    @PostMapping(value = {"/order/queue-up"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> queueProcess(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody MtEoRouterActualVO15 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.queueProcess(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "queueProcessCancel")
    @PostMapping(value = {"/order/queue-up-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> queueProcessCancel(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtEoRouterActualVO16 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.queueProcessCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualCopy")
    @PostMapping(value = {"/duplication"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoWkcAndStepActualCopy(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtEoRouterActualVO25 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualCopy(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualBatchCopy")
    @PostMapping(value = {"/batch-duplication"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoWkcAndStepActualBatchCopy(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtEoRouterActualVO17 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualBatchCopy(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoLimitWkcAndStepActualBatchCopy")
    @PostMapping(value = {"/batch/copy"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoLimitWkcAndStepActualBatchCopy(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtEoRouterActualVO33 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.mtEoRouterActualRepository.eoLimitWkcAndStepActualBatchCopy(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoWkcAndStepActualSplitVerify")
    @PostMapping(value = {"/split/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepActualSplitVerify(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtEoRouterActualVO18 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualSplitVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualMergeVerify")
    @PostMapping(value = {"/merge/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepActualMergeVerify(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualMergeVerify(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoRouterActualQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEoRouterActualQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtEoRouterActualVO20 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.propertyLimitEoRouterActualQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualSplit")
    @PostMapping(value = {"/wkc-step/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepActualSplit(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtEoRouterActualVO21 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualSplit(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepActualMerge")
    @PostMapping(value = {"/wkc-step/merge"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepActualMerge(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtEoRouterActualVO22 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepActualMerge(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepScrapped")
    @PostMapping(value = {"/wkc-step/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepScrapped(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepScrapped(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepScrappedCancel")
    @PostMapping(value = {"/wkc-step/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepScrappedCancel(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepScrappedCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "scrappedProcess")
    @PostMapping(value = {"/order/scrap"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> scrappedProcess(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.scrappedProcess(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "scrappedProcessCancel")
    @PostMapping(value = {"/order/scrap-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> scrappedProcessCancel(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.scrappedProcessCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "coproductEoActualCreate")
    @PostMapping(value = {"/co-production/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> coproductEoActualCreate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtEoRouterActualVO21 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.coproductEoActualCreate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepWorking")
    @PostMapping(value = {"/wkc-step/working"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepWorking(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepMoving")
    @PostMapping(value = {"/wkc-step/moving"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepMoving(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtEoRouterActualVO27 moveVO) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepMoving(tenantId, moveVO);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepWorkingCancel")
    @PostMapping(value = {"/wkc-step/working-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepWorkingCancel(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepWorkingCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWorkingProcess")
    @PostMapping(value = {"/working/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWorkingProcess(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWorkingProcess(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWorkingProcessCancel")
    @PostMapping(value = {"/working/process-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWorkingProcessCancel(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWorkingProcessCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepCompletePending")
    @PostMapping(value = {"/wkc-step/completion/pending"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepCompletePending(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepCompletePending(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepCompletePendingCancel")
    @PostMapping(value = {"/wkc-step/completion/pending-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepCompletePendingCancel(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepCompletePendingCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepComplete")
    @PostMapping(value = {"/wkc-step/completion"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepComplete(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepComplete(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoWkcAndStepCompleteCancel")
    @PostMapping(value = {"/wkc-step/completion-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepCompleteCancel(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepCompleteCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterCompletedValidate")
    @PostMapping(value = {"/completion/finished/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoRouterCompletedValidate(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody MtEoRouterActualVO14 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterCompletedValidate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterComplete")
    @PostMapping(value = {"/completion"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO24> eoRouterComplete(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<MtEoRouterActualVO24> responseData = new ResponseData<MtEoRouterActualVO24>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterComplete(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterCompleteCancel")
    @PostMapping(value = {"/completion-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO26> eoRouterCompleteCancel(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<MtEoRouterActualVO26> responseData = new ResponseData<MtEoRouterActualVO26>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterCompleteCancel(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "completeProcess")
    @PostMapping(value = {"/completion-finish"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO32> completeProcess(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<MtEoRouterActualVO32> responseData = new ResponseData<MtEoRouterActualVO32>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.completeProcess(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "completeProcessCancel")
    @PostMapping(value = {"/completion-finish-cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> completeProcessCancel(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtEoRouterActualVO19 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.mtEoRouterActualRepository.completeProcessCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoRouterActualPropertyQuery")
    @PostMapping(value = {"/limit-eo-router/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualVO30>> propertyLimitEoRouterActualPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualVO29 dto) {
        ResponseData<List<MtEoRouterActualVO30>> responseData = new ResponseData<List<MtEoRouterActualVO30>>();
        try {
            responseData.setRows(
                            this.mtEoRouterActualRepository.propertyLimitEoRouterActualPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "routerStepTypeGet")
    @PostMapping(value = {"/type/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO31> routerStepTypeGet(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String routerStepId) {
        ResponseData<MtEoRouterActualVO31> responseData = new ResponseData<MtEoRouterActualVO31>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.routerStepTypeGet(tenantId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterPropertyBatchGet")
    @PostMapping(value = {"/property/batch-get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActual>> eoRouterPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                         @RequestBody List<String> eoRouterActualIds) {
        ResponseData<List<MtEoRouterActual>> responseData = new ResponseData<List<MtEoRouterActual>>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterPropertyBatchGet(tenantId, eoRouterActualIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoCompleteProcess")
    @PostMapping(value = {"/eo-completion-process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoRouterActualVO32> eoCompleteProcess(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtEoRouterActualVO36 vo) {
        ResponseData<MtEoRouterActualVO32> responseData = new ResponseData<MtEoRouterActualVO32>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoCompleteProcess(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRouterProductionActualBatchUpdate")
    @PostMapping(value = {"/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualVO43>> eoRouterProductionActualBatchUpdate(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtEoRouterActualVO38 vo) {
        ResponseData<List<MtEoRouterActualVO43>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.eoRouterProductionActualBatchUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoWkcAndStepBatchWorking")
    @PostMapping(value = {"/wkc-step/batch/working"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoWkcAndStepBatchWorking(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtEoRouterActualVO52 dto) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.mtEoRouterActualRepository.eoWkcAndStepBatchWorking(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "queueBatchProcess")
    @PostMapping(value = {"/batch-queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> queueBatchProcess(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtEoRouterActualVO41 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.mtEoRouterActualRepository.queueBatchProcess(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "completeBatchProcess")
    @PostMapping(value = {"/complete/process/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterActualVO55>> completeBatchProcess(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody MtEoRouterActualVO39 vo) {
        ResponseData<List<MtEoRouterActualVO55>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.mtEoRouterActualRepository.completeBatchProcess(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
