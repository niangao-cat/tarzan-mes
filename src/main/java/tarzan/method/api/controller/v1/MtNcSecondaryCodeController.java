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
import tarzan.method.app.service.MtNcSecondaryCodeService;
import tarzan.method.domain.entity.MtNcSecondaryCode;
import tarzan.method.domain.repository.MtNcSecondaryCodeRepository;

/**
 * 次级不良代码 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@RestController("mtNcSecondaryCodeController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-secondary-code")
@Api(tags = "MtNcSecondaryCode")
public class MtNcSecondaryCodeController extends BaseController {

    @Autowired
    private MtNcSecondaryCodeService service;

    @ApiOperation(value = "UI删除次级不良代码")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeSecondaryCodeForUi(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody String ncSecondaryCodeId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeSecondaryCodeForUi(tenantId, ncSecondaryCodeId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtNcSecondaryCodeRepository repository;

    @ApiOperation(value = "ncCodeLimitRequiredSecondaryCodeQuery")
    @PostMapping(value = {"/limit-code/close-required/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcSecondaryCode>> ncCodeLimitRequiredSecondaryCodeQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String ncCodeId) {
        ResponseData<List<MtNcSecondaryCode>> responseData = new ResponseData<List<MtNcSecondaryCode>>();
        try {
            responseData.setRows(repository.ncCodeLimitRequiredSecondaryCodeQuery(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "ncSecondaryCodeQuery")
    @PostMapping(value = {"/limit-code/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcSecondaryCode>> ncSecondaryCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody String ncCodeId) {
        ResponseData<List<MtNcSecondaryCode>> responseData = new ResponseData<List<MtNcSecondaryCode>>();
        try {
            responseData.setRows(repository.ncSecondaryCodeQuery(tenantId, ncCodeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
