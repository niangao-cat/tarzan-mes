package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.modeling.api.dto.MtModProdLineDispatchOperDTO;
import tarzan.modeling.app.service.MtModProdLineDispatchOperService;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;
import tarzan.modeling.domain.repository.MtModProdLineDispatchOperRepository;
import tarzan.modeling.domain.vo.MtModProdLineDispatchOperVO1;

/**
 * 生产线调度指定工艺 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModProdLineDispatchOperController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-prod-line-dispatch-oper")
@Api(tags = "MtModProdLineDispatchOper")
public class MtModProdLineDispatchOperController extends BaseController {

    @Autowired
    private MtModProdLineDispatchOperService service;
    
    @Autowired
    private MtModProdLineDispatchOperRepository repository;

    @ApiOperation("UI根据生产线获取指定的调度工艺")
    @GetMapping(value = "/limit-dispatch/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModProdLineDispatchOper>> prodLineIdLimitDispatchOperationQueryForUi(
                    @PathVariable(value = "organizationId") Long tenantId, String prodLineId,
                    @ApiIgnore @SortDefault(value = MtModProdLineDispatchOper.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtModProdLineDispatchOper>> responseData =
                        new ResponseData<List<MtModProdLineDispatchOper>>();
        try {
            responseData.setRows(service.prodLineIdLimitDispatchOperationQueryForUi(tenantId, prodLineId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI保存生产线指定的调度工艺")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModProdLineDispatchOperDTO> saveModProdLineDispatchOperForUi(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProdLineDispatchOperDTO dto) {
        ResponseData<MtModProdLineDispatchOperDTO> responseData = new ResponseData<MtModProdLineDispatchOperDTO>();
        try {
            responseData.setRows(service.saveModProdLineDispatchOperForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI删除生产线指定的调度工艺")
    @PostMapping(value = "/remove/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Integer> deleteModProdLineDispatchOperForUi(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> modProdLineDispatchOperIdList) {
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        try {
            responseData.setRows(service.deleteModProdLineDispatchOperForUi(tenantId, modProdLineDispatchOperIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("prodLineLimitDispatchOperationQuery")
    @PostMapping(value = "/limit-dispatch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> prodLineLimitDispatchOperationQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String prodLineId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.prodLineLimitDispatchOperationQuery(tenantId, prodLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("operationLimitProdLineQuery")
    @PostMapping(value = "/prod-line", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> operationLimitProdLineQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String operationId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.operationLimitProdLineQuery(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineDispatchOperationVerify")
    @PostMapping(value = "/verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> prodLineDispatchOperationVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProdLineDispatchOperVO1 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.prodLineDispatchOperationValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
