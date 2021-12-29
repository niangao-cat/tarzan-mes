package tarzan.inventory.api.controller.v1;

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
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO1;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO2;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO3;

/**
 * 物料批历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@RestController("mtMaterialLotHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-lot-his")
@Api(tags = "MtMaterialLotHis")
public class MtMaterialLotHisController extends BaseController {
    @Autowired
    private MtMaterialLotHisRepository repository;

    @ApiOperation(value = "requestLimitMaterialLotHisQuery")
    @PostMapping(value = {"/limit-request/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotHis>> requestLimitMaterialLotHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialLotHisVO1 dto) {
        ResponseData<List<MtMaterialLotHis>> responseData = new ResponseData<List<MtMaterialLotHis>>();
        try {
            responseData.setRows(this.repository.requestLimitMaterialLotHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitMaterialLotHisQuery")
    @PostMapping(value = {"/limit-event/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotHis>> eventLimitMaterialLotHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String eventId) {
        ResponseData<List<MtMaterialLotHis>> responseData = new ResponseData<List<MtMaterialLotHis>>();
        try {
            responseData.setRows(this.repository.eventLimitMaterialLotHisQuery(tenantId, eventId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitMaterialLotHisBatchQuery")
    @PostMapping(value = {"/limit-event-list/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotHis>> eventLimitMaterialLotHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtMaterialLotHis>> responseData = new ResponseData<List<MtMaterialLotHis>>();
        try {
            responseData.setRows(this.repository.eventLimitMaterialLotHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotHisQuery")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotHis>> materialLotHisQuery(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtMaterialLotHisVO2 dto) {
        ResponseData<List<MtMaterialLotHis>> responseData = new ResponseData<List<MtMaterialLotHis>>();
        try {
            responseData.setRows(this.repository.materialLotHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLatestHisGet")
    @PostMapping(value = {"/material/lot/latest/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLotHisVO3> materialLotLatestHisGet(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody String materialLotId) {
        ResponseData<MtMaterialLotHisVO3> responseData = new ResponseData<MtMaterialLotHisVO3>();
        try {
            responseData.setRows(this.repository.materialLotLatestHisGet(tenantId, materialLotId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
