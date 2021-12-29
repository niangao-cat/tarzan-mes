package tarzan.method.api.controller.v1;

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
import tarzan.method.app.service.MtNcValidOperService;
import tarzan.method.domain.entity.MtNcValidOper;
import tarzan.method.domain.repository.MtNcValidOperRepository;
import tarzan.method.domain.vo.MtNcValidOperVO1;

/**
 * 不良代码工艺分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@RestController("mtNcValidOperController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-valid-oper")
@Api(tags = "MtNcValidOper")
public class MtNcValidOperController extends BaseController {

    @Autowired
    private MtNcValidOperService service;

    @ApiOperation(value = "UI删除不良代码工艺")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeNcValidOperForUi(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody String ncValidOperId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeNcValidOperForUi(tenantId, ncValidOperId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @Autowired
    private MtNcValidOperRepository repository;

    @ApiOperation(value = "ncCodeOperationValidate")
    @PostMapping(value = {"/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> ncCodeOperationValidate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtNcValidOperVO1 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(repository.ncCodeOperationValidate(tenantId, dto));


        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncValidOperationQuery")
    @PostMapping(value = {"/limit-code/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcValidOper>> ncValidOperationQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody String ncCodeId) {
        ResponseData<List<MtNcValidOper>> responseData = new ResponseData<List<MtNcValidOper>>();
        try {
            responseData.setRows(repository.ncValidOperationQuery(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "operationValidNcQuery")
    @PostMapping(value = {"/limit-operation/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcValidOper>> operationValidNcQuery(@PathVariable("organizationId") Long tenantId,
                                                                   @RequestBody String operationId) {
        ResponseData<List<MtNcValidOper>> responseData = new ResponseData<List<MtNcValidOper>>();
        try {
            responseData.setRows(repository.operationValidNcQuery(tenantId, operationId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
