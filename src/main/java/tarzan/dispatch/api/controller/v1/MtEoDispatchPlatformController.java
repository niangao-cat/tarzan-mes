package tarzan.dispatch.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import springfox.documentation.annotations.ApiIgnore;
import tarzan.dispatch.api.dto.*;
import tarzan.dispatch.app.service.MtEoDispatchPlatformService;
import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.method.domain.entity.MtOperation;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * @author : MrZ
 * @date : 2019-12-03 17:40
 **/
@RestController("mtEoDispatchPlatformController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-dispatch-platform")
@Api(tags = "MtEoDispatchPlatform")
public class MtEoDispatchPlatformController extends BaseController {
    @Autowired
    private MtEoDispatchPlatformService eoDispatchPlatformService;

    @ApiOperation(value = "调度平台(获取用户默认站点,前台)")
    @GetMapping(value = "/user-default-site/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> defaultSiteUi(@PathVariable("organizationId") Long tenantId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.defaultSiteUi(tenantId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(获取用户有权限的生产线,前台)")
    @GetMapping(value = "/user-prod-line/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModProductionLine>> userProdLineUi(@PathVariable("organizationId") Long tenantId) {
        ResponseData<List<MtModProductionLine>> responseData = new ResponseData<List<MtModProductionLine>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.userProdLineUi(tenantId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(根据生产线获取工艺,前台)")
    @GetMapping(value = "/operation/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtOperation>> operationByProdLineUi(@PathVariable("organizationId") Long tenantId,
                    MtEoDispatchPlatformDTO11 dto) {
        ResponseData<List<MtOperation>> responseData = new ResponseData<List<MtOperation>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.operationByProdLineUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(获取调度范围,WKC范围,前台)")
    @GetMapping(value = "/wkc-dispatch-range/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchPlatformDTO4>> wkcDispatchRangeUi(
                    @PathVariable("organizationId") Long tenantId, MtEoDispatchPlatformDTO3 dto) {
        ResponseData<List<MtEoDispatchPlatformDTO4>> responseData = new ResponseData<List<MtEoDispatchPlatformDTO4>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.wkcDispatchRangeUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(列表信息,前台)")
    @GetMapping(value = "/table-info/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoDispatchPlatformDTO2>> dispatchPlatformTableUi(
                    @PathVariable("organizationId") Long tenantId, MtEoDispatchPlatformDTO dto,
                    @ApiIgnore @SortDefault(value = MtEoDispatchProcess.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEoDispatchPlatformDTO2>> responseData = new ResponseData<Page<MtEoDispatchPlatformDTO2>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchPlatformTableUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(子列表信息,前台)")
    @GetMapping(value = "/sub-table-info/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchPlatformDTO10>> dispatchPlatformSubTableUi(
                    @PathVariable("organizationId") Long tenantId, MtEoDispatchPlatformDTO9 dto) {
        ResponseData<List<MtEoDispatchPlatformDTO10>> responseData =
                        new ResponseData<List<MtEoDispatchPlatformDTO10>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchPlatformSubTableUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(已调度子列表信息,前台)")
    @PostMapping(value = "/scheduled-sub-table-info/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchPlatformDTO19>> scheduledSubTableUi(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchPlatformDTO18 dto) {
        ResponseData<List<MtEoDispatchPlatformDTO19>> responseData =
                        new ResponseData<List<MtEoDispatchPlatformDTO19>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.scheduledSubTableUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(已调度子表格顺序调整,前台)")
    @PostMapping(value = "/scheduled-sub-table/reorder/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> scheduledSubTableReorderUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtEoDispatchPlatformDTO17> dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.eoDispatchPlatformService.scheduledSubTableReorderUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(已调度执行作业，查询一天的所有WKC,前台)")
    @PostMapping(value = "/chart-info/one-day/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoDispatchPlatformDTO7>> dispatchPlatformChartDayUi(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtEoDispatchPlatformDTO6 dto) {
        ResponseData<List<MtEoDispatchPlatformDTO7>> responseData = new ResponseData<List<MtEoDispatchPlatformDTO7>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchPlatformChartDayUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(已调度执行作业，查询其中某一个图标信息,前台)")
    @PostMapping(value = "/chart-info/one-chart/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchPlatformDTO7> dispatchPlatformChartUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchPlatformDTO5 dto) {
        ResponseData<MtEoDispatchPlatformDTO7> responseData = new ResponseData<MtEoDispatchPlatformDTO7>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchPlatformChartUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(可选的WKC信息LOV,前台)")
    @GetMapping(value = "/wkc/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtModWorkcell>> wkcLovUi(@PathVariable("organizationId") Long tenantId,
                    MtEoDispatchPlatformDTO3 condition,
                    @ApiIgnore @SortDefault(value = MtModWorkcell.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(eoDispatchPlatformService.wkcLovUi(tenantId, condition, pageRequest));
    }

    @ApiOperation(value = "调度平台(获取班次信息下拉框,前台)")
    @PostMapping(value = "/shift-code/combo-box/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> shiftCodeComboBoxUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchPlatformDTO12 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.shiftCodeComboBoxUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(执行调度逻辑,前台)")
    @PostMapping(value = "/dispatch/confirm/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchPlatformDTO14> dispatchConfirmUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchPlatformDTO13 dto) {
        ResponseData<MtEoDispatchPlatformDTO14> responseData = new ResponseData<MtEoDispatchPlatformDTO14>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchConfirmUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(执行调度发布逻辑,前台)")
    @PostMapping(value = "/dispatch/release/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> dispatchReleaseUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchPlatformDTO15 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.eoDispatchPlatformService.dispatchReleaseUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "调度平台(执行调度撤销逻辑,前台)")
    @PostMapping(value = "/dispatch/revoke/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDispatchPlatformDTO14> dispatchRevokeUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEoDispatchPlatformDTO16 dto) {
        ResponseData<MtEoDispatchPlatformDTO14> responseData = new ResponseData<MtEoDispatchPlatformDTO14>();
        try {
            responseData.setRows(this.eoDispatchPlatformService.dispatchRevokeUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
