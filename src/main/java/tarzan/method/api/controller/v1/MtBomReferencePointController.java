package tarzan.method.api.controller.v1;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtBomReferencePointDTO;
import tarzan.method.app.service.MtBomReferencePointService;
import tarzan.method.domain.entity.MtBomReferencePoint;
import tarzan.method.domain.repository.MtBomReferencePointRepository;
import tarzan.method.domain.vo.MtBomReferencePointVO;
import tarzan.method.domain.vo.MtBomReferencePointVO2;
import tarzan.method.domain.vo.MtBomReferencePointVO3;

/**
 * 装配清单行参考点关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomReferencePointController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-reference-point")
@Api(tags = "MtBomReferencePoint")
public class MtBomReferencePointController extends BaseController {

    @Autowired
    private MtBomReferencePointService service;
    
    @Autowired
    private MtBomReferencePointRepository repository;

    @ApiOperation(value = "获取装配清单组件行的参考点列表（前台）")
    @GetMapping(value = {"/list/by/component/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePointDTO>> listbomReferencePointUi(@PathVariable("organizationId") Long tenantId,
                    @ApiParam("主键") @RequestParam("bomComponentId") String bomComponentId) {
        ResponseData<List<MtBomReferencePointDTO>> responseData = new ResponseData<List<MtBomReferencePointDTO>>();
        try {
            responseData.setRows(this.service.listbomReferencePointUi(tenantId,
                            bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
    @ApiOperation(value = "保存装配清单组件行的参考点（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveReferencePointForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomReferencePointDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.saveReferencePointForUi(tenantId,dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
    @ApiOperation(value = "bomReferencePointBasicGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtBomReferencePoint> bomReferencePointBasicGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomReferencePointId) {
        ResponseData<MtBomReferencePoint> responseData = new ResponseData<MtBomReferencePoint>();
        try {
            responseData.setRows(this.repository.bomReferencePointBasicGet(tenantId,
                            bomReferencePointId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "componentLimitBomReferencePointQuery")
    @PostMapping(value = {"/limit-component"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<Map<String, String>>> componentLimitBomReferencePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String bomComponentId) {
        ResponseData<List<Map<String, String>>> responseData = new ResponseData<List<Map<String, String>>>();
        try {
            responseData.setRows(this.repository.componentLimitBomReferencePointQuery(tenantId,
                            bomComponentId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitBomReferencePointQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePointVO2>> propertyLimitBomReferencePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomReferencePointVO condition) {
        ResponseData<List<MtBomReferencePointVO2>> responseData = new ResponseData<List<MtBomReferencePointVO2>>();
        try {
            responseData.setRows(this.repository.propertyLimitBomReferencePointQuery(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomReferencePointBasicBatchGet")
    @PostMapping(value = {"/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePoint>> bomReferencePointBasicBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> bomReferencePointIds) {
        ResponseData<List<MtBomReferencePoint>> responseData = new ResponseData<List<MtBomReferencePoint>>();
        try {
            responseData.setRows(this.repository.bomReferencePointBasicBatchGet(tenantId,
                            bomReferencePointIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomComponentReferencePointQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomReferencePoint>> bomComponentReferencePointQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomReferencePointVO3 dto) {
        ResponseData<List<MtBomReferencePoint>> responseData = new ResponseData<List<MtBomReferencePoint>>();
        try {
            responseData.setRows(this.repository.bomComponentReferencePointQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomReferencePointUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> bomReferencePointUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomReferencePointDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {

            MtBomReferencePoint dto1 = new MtBomReferencePoint();
            BeanUtils.copyProperties(dto, dto1);

            responseData.setRows(this.repository.bomReferencePointUpdate(tenantId, dto1));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());   
        }
        return responseData;
    }
}
