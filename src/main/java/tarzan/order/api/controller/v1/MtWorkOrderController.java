package tarzan.order.api.controller.v1;

import java.util.List;

import com.ruike.hme.app.service.HmeSnBindEoService;
import org.apache.commons.lang3.StringUtils;
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
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.vo.MtBomComponentVO18;
import tarzan.method.domain.vo.MtBomComponentVO19;
import tarzan.order.api.dto.*;
import tarzan.order.app.service.MtWorkOrderService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;

/**
 * 生产指令 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@RestController("mtWorkOrderController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order")
@Api(tags = "MtWorkOrder")
public class MtWorkOrderController extends BaseController {

    @Autowired
    private MtWorkOrderRepository repository;

    @Autowired
    private MtWorkOrderService service;

    @Autowired
    private HmeSnBindEoService hmeSnBindEoService;

    @ApiOperation("woPropertyGet")
    @PostMapping("/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrder> woPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody String workOrderId) {
        ResponseData<MtWorkOrder> responseData = new ResponseData<MtWorkOrder>();
        try {
            responseData.setRows(this.repository.woPropertyGet(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woPropertyBatchGet")
    @PostMapping("/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrder>> woPropertyBatchGet(@PathVariable(value = "organizationId") Long tenantId,
                                                              @RequestBody List<String> workOrderIds) {
        ResponseData<List<MtWorkOrder>> responseData = new ResponseData<List<MtWorkOrder>>();
        try {
            responseData.setRows(this.repository.woPropertyBatchGet(tenantId, workOrderIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitWoQuery")
    @PostMapping("/limit-wo")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitWoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody MtWorkOrderVO21 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitWoQuery(tenantId, dto));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation("numberLimitWoGet")
    @PostMapping("/limit-wo-num")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> numberLimitWoGet(@PathVariable(value = "organizationId") Long tenantId,
                                                 @RequestBody String workOrderNum) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.numberLimitWoGet(tenantId, workOrderNum));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("bomRouterLimitWoQuery")
    @PostMapping("/limit-bom-router")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> bomRouterLimitWoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody MtWorkOrderVO17 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.bomRouterLimitWoQuery(tenantId, dto));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation("planTimeLimitWoQuery")
    @PostMapping("/limit-plantime")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> planTimeLimitWoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody MtWorkOrderVO20 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.planTimeLimitWoQuery(tenantId, dto));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woDefaultLocationGet")
    @PostMapping("/default-location")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woDefaultLocationGet(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody String workOrderId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.woDefaultLocationGet(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woCompleteControlGet")
    @PostMapping("/complete-control")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO19> woCompleteControlGet(@PathVariable(value = "organizationId") Long tenantId,
                                                              @RequestBody String workOrderId) {
        ResponseData<MtWorkOrderVO19> responseData = new ResponseData<MtWorkOrderVO19>();
        try {
            responseData.setRows(this.repository.woCompleteControlGet(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woMaterialValidate")
    @PostMapping("/material-validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woMaterialValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                 @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woMaterialValidate(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woBomValidate")
    @PostMapping("/bom-validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woBomValidate(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woBomValidate(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woRouterValidate")
    @PostMapping("/router-validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRouterValidate(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woRouterValidate(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woRouterBomMatchValidate")
    @PostMapping("/router-bom-match-validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRouterBomMatchValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woRouterBomMatchValidate(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woStatusUpdate")
    @PostMapping("/status/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO28> woStatusUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                        @RequestBody MtWorkOrderVO25 dto) {
        ResponseData<MtWorkOrderVO28> result = new ResponseData<MtWorkOrderVO28>();
        try {
            result.setRows(this.repository.woStatusUpdate(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woReleaseVerify")
    @PostMapping("/release-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woReleaseVerify(@PathVariable(value = "organizationId") Long tenantId,
                                              @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woReleaseVerify(tenantId, workOrderId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelease")
    @PostMapping("/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRelease(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody MtWorkOrderVO23 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woRelease(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woStatusVerify")
    @PostMapping("/status-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> woStatusVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                @RequestBody MtWorkOrderDTO dto) {
        ResponseData<Boolean> result = new ResponseData<Boolean>();
        try {
            result.setSuccess(
                    this.repository.woStatusVerify(tenantId, dto.getWorkOrderId(), dto.getDemandStatusList()));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woValidateVerifyUpdate")
    @PostMapping("/update-validate-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO28> woValidateVerifyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                                @RequestBody MtWorkOrderDTO2 dto) {
        ResponseData<MtWorkOrderVO28> result = new ResponseData<MtWorkOrderVO28>();
        try {
            result.setRows(this.repository.woValidateVerifyUpdate(tenantId, dto.getWorkOrderId(), dto.getEventId()));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("attritionLimitWoComponentQtyQuery")
    @PostMapping("/component-qty/limit-attrition")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO19>> attritionLimitWoComponentQtyQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtBomComponentVO18 dto) {
        ResponseData<List<MtBomComponentVO19>> responseData = new ResponseData<List<MtBomComponentVO19>>();
        try {
            responseData.setRows(this.repository.attritionLimitWoComponentQtyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woBomUpdate")
    @PostMapping("/bom/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woBomUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderDTO3 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(this.repository.woBomUpdate(tenantId, dto.getWorkOrderId(), dto.getBomId(),
                    dto.getEventId()));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woRouterUpdate")
    @PostMapping("/router/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO28> woRouterUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                        @RequestBody MtWorkOrderDTO4 dto) {
        ResponseData<MtWorkOrderVO28> result = new ResponseData<MtWorkOrderVO28>();
        try {
            result.setRows(this.repository.woRouterUpdate(tenantId, dto.getWorkOrderId(), dto.getRouterId(),
                    dto.getEventId()));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woQtyUpdate")
    @PostMapping("/qty/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO28> woQtyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody MtWorkOrderVO22 dto) {
        ResponseData<MtWorkOrderVO28> result = new ResponseData<MtWorkOrderVO28>();
        try {
            result.setRows(this.repository.woQtyUpdate(tenantId, dto));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO28> woUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtWorkOrderVO condition,
                                                  @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtWorkOrderVO28> responseData = new ResponseData<MtWorkOrderVO28>();
        try {
            responseData.setRows(this.repository.woUpdate(tenantId, condition, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("woBatchUpdate")
    @PostMapping("/batch/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO28>> woBatchUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                             @RequestBody MtWorkOrderVO37 condition,
                                                             @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<List<MtWorkOrderVO28>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woBatchUpdate(tenantId, condition, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("woSort")
    @PostMapping("/sort")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> woSort(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody MtWorkOrderVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.woSort(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woStatusUpdateVerify")
    @PostMapping("/status-change-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woStatusUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody MtWorkOrderDTO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woStatusUpdateVerify(tenantId, dto.getWorkOrderId(), dto.getTargetStatus());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woHoldVerify")
    @PostMapping("/hold-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woHoldVerify(@PathVariable(value = "organizationId") Long tenantId,
                                           @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woHoldVerify(tenantId, workOrderId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woCloseVerify")
    @PostMapping("/close-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woCloseVerify(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woCloseVerify(tenantId, workOrderId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woStatusComplete")
    @PostMapping("/status/complete")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woStatusComplete(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody MtWorkOrderVO2 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woStatusComplete(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woClose")
    @PostMapping("/close")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woClose(@PathVariable(value = "organizationId") Long tenantId,
                                      @RequestBody MtWorkOrderVO2 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woClose(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woCloseCancel")
    @PostMapping("/close/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woCloseCancel(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderVO2 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woCloseCancel(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woHold")
    @PostMapping("/hold")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woHold(@PathVariable(value = "organizationId") Long tenantId,
                                     @RequestBody MtWorkOrderVO2 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woHold(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woHoldCancel")
    @PostMapping("/hold/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woHoldCancel(@PathVariable(value = "organizationId") Long tenantId,
                                           @RequestBody MtWorkOrderVO15 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woHoldCancel(tenantId, dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woPreProductValidate")
    @PostMapping("/preproduct-validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woPreProductValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woPreProductValidate(tenantId, workOrderId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("statusLimitWoQtyUpdateVerify")
    @PostMapping("/qty-update/status-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> statusLimitWoQtyUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.statusLimitWoQtyUpdateVerify(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woStatusCompleteVerify")
    @PostMapping("/status-complete-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woStatusCompleteVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woStatusCompleteVerify(tenantId, workOrderId);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woCompleteControlLimitEoCreateVerify")
    @PostMapping("/created/complete-control-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woCompleteControlLimitEoCreateVerify(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtWorkOrderVO3 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woCompleteControlLimitEoCreateVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woCompleteVerify")
    @PostMapping("/complete/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woCompleteVerify(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody MtWorkOrderVO29 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woCompleteVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woLimitEoCreateVerify")
    @PostMapping("/eo-add-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woLimitEoCreateVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                    @RequestBody MtWorkOrderVO3 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woLimitEoCreateVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoClose")
    @PostMapping("/wo-and-eo/close")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoClose(@PathVariable(value = "organizationId") Long tenantId,
                                           @RequestBody MtWorkOrderVO5 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoClose(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woCompleteCancel")
    @PostMapping("/complete/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woCompleteCancel(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody MtWorkOrderVO4 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woCompleteCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woComplete")
    @PostMapping("/complete")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woComplete(@PathVariable(value = "organizationId") Long tenantId,
                                         @RequestBody MtWorkOrderVO4 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woComplete(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoCloseCancel")
    @PostMapping("/wo-and-eo/close/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoCloseCancel(@PathVariable(value = "organizationId") Long tenantId,
                                                 @RequestBody MtWorkOrderVO5 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoCloseCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("releaseWoLimitWoQtyUpdateVerify")
    @PostMapping("/released-qty/qty-update-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> releaseWoLimitWoQtyUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                              @RequestBody MtWorkOrderVO6 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.releaseWoLimitWoQtyUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoQtyUpdate")
    @PostMapping("/wo-and-eo-qty/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoQtyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody MtWorkOrderVO22 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoQtyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("bomRouterLimitUniqueWoValidate")
    @PostMapping("/bom-or-router/unique-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomRouterLimitUniqueWoValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                             @RequestBody MtWorkOrderVO17 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.bomRouterLimitUniqueWoValidate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("woRelLimitSubWoAndEoQtyUpdate")
    @PostMapping("/sub-qty/update/limit-rel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRelLimitSubWoAndEoQtyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody MtWorkOrderVO24 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            this.repository.woRelLimitSubWoAndEoQtyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("bomLimitWoCreate")
    @PostMapping("/add/limit-bom")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> bomLimitWoCreate(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody String workOrderId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.bomLimitWoCreate(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woComponentQtyQuery")
    @PostMapping("/component-qty")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO8>> woComponentQtyQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                  @RequestBody MtWorkOrderVO7 dto) {
        ResponseData<List<MtWorkOrderVO8>> responseData = new ResponseData<List<MtWorkOrderVO8>>();
        try {
            responseData.setRows(this.repository.woComponentQtyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woMaterialLimitComponentQuery")
    @PostMapping("/component/limit-material-wo")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> woMaterialLimitComponentQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtWorkOrderVO27 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.woMaterialLimitComponentQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woOperationAssembleFlagGet")
    @PostMapping("/operation/assemble-flag")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woOperationAssembleFlagGet(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody String workOrderId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.woOperationAssembleFlagGet(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woNextNumberGet")
    @PostMapping("/next-number")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO5> woNextNumberGet(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody MtWorkOrderVO33 mtWorkOrderVO33) {
        ResponseData<MtNumrangeVO5> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woNextNumberGet(tenantId, mtWorkOrderVO33));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woMerge")
    @PostMapping("/merge")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woMerge(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody MtWorkOrderVO9 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            String woId = this.repository.woMerge(tenantId, dto);
            if (StringUtils.isNotEmpty(woId)) {
                responseData.setRows(woId);
            }

        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woMergeVerify")
    @PostMapping("/merge-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woMergeVerify(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderVO10 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woMergeVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woSplitVerify")
    @PostMapping("/split-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woSplitVerify(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderVO11 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woSplitVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woSplit")
    @PostMapping("/split")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woSplit(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody MtWorkOrderVO13 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.woSplit(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woRouterUpdateVerify")
    @PostMapping("/router-change-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRouterUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody MtWorkOrderVO12 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woRouterUpdateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("wobomUpdateValidate")
    @PostMapping("/bom-update-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> wobomUpdateValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtWorkOrderDTO6 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.wobomUpdateValidate(tenantId, dto.getWorkOrderId(), dto.getBomId());
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoBomUpdate")
    @PostMapping("/wo-and-wo-bom/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoBomUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                               @RequestBody MtWorkOrderVO14 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoBomUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAbandon")
    @PostMapping("/abandon")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAbandon(@PathVariable(value = "organizationId") Long tenantId,
                                        @RequestBody MtWorkOrderVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAbandon(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoHold")
    @PostMapping("/wo-and-eo/hold")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoHold(@PathVariable(value = "organizationId") Long tenantId,
                                          @RequestBody MtWorkOrderVO2 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoHold(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAndEoHoldCancel")
    @PostMapping("/wo-and-eo/hold/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAndEoHoldCancel(@PathVariable(value = "organizationId") Long tenantId,
                                                @RequestBody MtWorkOrderVO15 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAndEoHoldCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woStatusCompleteCancel")
    @PostMapping("/complete-status/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woStatusCompleteCancel(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody MtWorkOrderVO5 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woStatusCompleteCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("materialLimitWoUpdate")
    @PostMapping("/update/limit-material")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLimitWoUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                    @RequestBody MtWorkOrderEventVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLimitWoUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woLimitAttrQuery")
    @PostMapping("/attr/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> woLimitAttrQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               @RequestBody MtWorkOrderAttrVO2 dto) {
        ResponseData<List<MtExtendAttrVO>> responseData = new ResponseData<List<MtExtendAttrVO>>();
        try {
            responseData.setRows(this.repository.woLimitAttrQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("attrLimitWoQuery")
    @PostMapping("/limit-attr")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrLimitWoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody MtWorkOrderAttrVO1 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.attrLimitWoQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woLimitAttrUpdate")
    @PostMapping("/attr/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woLimitAttrUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                @RequestBody MtWorkOrderAttrVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woLimitAttrUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("woAttrHisQuery")
    @PostMapping("/attr/his")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderAttrHisVO2>> woAttrHisQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtWorkOrderAttrHisVO dto) {
        ResponseData<List<MtWorkOrderAttrHisVO2>> responseData = new ResponseData<List<MtWorkOrderAttrHisVO2>>();
        try {
            responseData.setRows(this.repository.woAttrHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("eventLimitWoAttrHisBatchQuery")
    @PostMapping("/attr/his/limit-event")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderAttrHisVO2>> eventLimitWoAttrHisBatchQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtWorkOrderAttrHisVO2>> responseData = new ResponseData<List<MtWorkOrderAttrHisVO2>>();
        try {
            responseData.setRows(this.repository.eventLimitWoAttrHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation("propertyLimitWoPropertyQuery")
    @PostMapping("/wo/limit-property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO31>> propertyLimitWoPropertyQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtWorkOrderVO30 dto) {
        ResponseData<List<MtWorkOrderVO31>> responseData = new ResponseData<List<MtWorkOrderVO31>>();
        try {
            responseData.setRows(this.repository.propertyLimitWoPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }


    @ApiOperation("woPriorityLimitNextWoQuery")
    @PostMapping(value = "/limit-priority/next", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> woPriorityLimitNextWoQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                 @RequestBody String workOrderId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.woPriorityLimitNextWoQuery(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woAttrPropertyUpdate")
    @PostMapping(value = "/attr/property/update", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woAttrPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                   @RequestBody MtWorkOrderVO32 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.woAttrPropertyUpdate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("woBatchNumberGet")
    @PostMapping(value = "/batch/number/get", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO8> woAttrPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody MtWorkOrderVO35 vo) {
        ResponseData<MtNumrangeVO8> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woBatchNumberGet(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woKittingQtyCalculate")
    @PostMapping(value = "/kitting/qty/calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> woKittingQtyCalculate(@PathVariable(value = "organizationId") Long tenantId,
                                                      @RequestBody MtWorkOrderVO45 vo) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.woKittingQtyCalculate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令列表")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtWorkOrderVO39>> woListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                           MtWorkOrderVO38 dto, @ApiIgnore @SortDefault(value = MtWorkOrder.FIELD_WORK_ORDER_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtWorkOrderVO39>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.woListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令详情")
    @GetMapping(value = "/detail/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO40> woDetailForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                       @ApiParam(value = "生产指令ID", required = true) @RequestParam String workOrderId) {
        ResponseData<MtWorkOrderVO40> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.woDetailForUi(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI工艺路线列表")
    @GetMapping(value = "/router/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO41>> routerListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                               @ApiParam(value = "生产指令ID", required = true) @RequestParam String workOrderId,
                                                               @ApiParam(value = "工艺路线ID", required = true) @RequestParam String routerId,
                                                               @ApiIgnore @SortDefault(value = MtRouterStep.FIELD_SEQUENCE,
                                                                       direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtWorkOrderVO41>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.routerListForUi(tenantId, workOrderId, routerId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI工艺路线子步骤列表")
    @GetMapping(value = "/sub-router-step/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO41>> subRouterStepListForUi(
            @PathVariable(value = "organizationId") Long tenantId,
            @ApiParam(value = "生产指令ID", required = true) @RequestParam String workOrderId,
            @ApiParam(value = "工艺路线步骤ID", required = true) @RequestParam String routerStepId) {
        ResponseData<List<MtWorkOrderVO41>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.subRouterStepListForUi(tenantId, workOrderId, routerStepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI装配清单列表")
    @GetMapping(value = "/bom/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtWorkOrderVO42>> bomListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                            MtWorkOrderVO47 dto, @ApiIgnore @SortDefault(value = MtRouterStep.FIELD_SEQUENCE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtWorkOrderVO42>> responseData = new ResponseData<Page<MtWorkOrderVO42>>();
        try {
            responseData.setRows(this.service.bomListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UIwo清单列表")
    @GetMapping(value = "/eo/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO51>> eoListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                           @ApiIgnore @SortDefault(value = MtEo.FIELD_EO_NUM,
                                                                   direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                           MtWorkOrderVO46 dto) {
        ResponseData<List<MtWorkOrderVO51>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.eoListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令新增&更新")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woSaveForUi(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderVO48 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.woSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令状态变更")
    @PostMapping(value = "/status/update/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woStatusUpdateForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtWorkOrderVO52 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.service.woStatusUpdateForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("UI创建EO")
    @PostMapping(value = "/eo/create/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoCreateForUi(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody MtWorkOrderVO50 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        validObject(dto);
        try {
            hmeSnBindEoService.eoCreateForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令关系列表")
    @GetMapping(value = "/wo/rel/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtWorkOrderVO53>> woRelListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                              @ApiIgnore @SortDefault(value = MtWorkOrder.FIELD_WORK_ORDER_NUM,
                                                                      direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                              MtWorkOrderVO54 dto) {
        ResponseData<Page<MtWorkOrderVO53>> responseData = new ResponseData<Page<MtWorkOrderVO53>>();
        try {
            responseData.setRows(this.service.woRelListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令拆分")
    @PostMapping(value = "/wo/split/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woSplitForUi(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody MtWorkOrderVO55 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.woSplitForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令合并")
    @PostMapping(value = "/wo/merge/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woMergeForUi(@PathVariable(value = "organizationId") Long tenantId,
                                             @RequestBody MtWorkOrderVO56 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.woMergeForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI生产指令数量获取")
    @GetMapping(value = "/wo/qty/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtWorkOrderVO57> woQtyForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                    @ApiParam(value = "生产指令ID", required = true) @RequestParam String workOrderId) {
        ResponseData<MtWorkOrderVO57> responseData = new ResponseData<MtWorkOrderVO57>();
        try {
            responseData.setRows(this.service.woQtyForUi(tenantId, workOrderId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UIbom分解")
    @PostMapping(value = "/bom/split/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomSplitForUi(@PathVariable(value = "organizationId") Long tenantId,
                                            @RequestBody String workOrderId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.service.bomSplitForUi(tenantId, workOrderId);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woMaterialLimitComponentBatchQuery")
    @PostMapping(value = "/component/limit-material-wo-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderVO68>> woMaterialLimitComponentBatchQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtWorkOrderVO27> vo) {
        ResponseData<List<MtWorkOrderVO68>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.woMaterialLimitComponentBatchQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woLimitWorkNUmQuery")
    @PostMapping("/wo-num")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrder>> woLimitWorkNUmQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                               @RequestBody List<String> workOrderNum) {
        ResponseData<List<MtWorkOrder>> responseData = new ResponseData<List<MtWorkOrder>>();
        try {
            responseData.setRows(this.repository.woLimitWorkNUmQuery(tenantId, workOrderNum));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("selectByWipEntityId")
    @PostMapping("/wip-id")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrder>> selectByWipEntityId(@PathVariable(value = "organizationId") Long tenantId,
                                                               @RequestBody List<String> wipEntityIds) {
        ResponseData<List<MtWorkOrder>> responseData = new ResponseData<List<MtWorkOrder>>();
        try {
            responseData.setRows(this.repository.selectByWipEntityId(tenantId, wipEntityIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
