package tarzan.order.api.controller.v1;

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
import tarzan.method.domain.vo.MtBomComponentVO19;
import tarzan.method.domain.vo.MtBomComponentVO20;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.vo.MtEoBomVO;
import tarzan.order.domain.vo.MtEoBomVO2;
import tarzan.order.domain.vo.MtEoBomVO3;

/**
 * EO装配清单 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoBomController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-bom")
@Api(tags = "MtEoBom")
public class MtEoBomController extends BaseController {
    @Autowired
    private MtEoBomRepository repository;

    @ApiOperation(value = "eoBomGet")
    @PostMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoBomGet(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoBomGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "attritionLimitEoComponentQtyQuery")
    @PostMapping(value = {"/component-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomComponentVO19>> attritionLimitEoComponentQtyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtBomComponentVO20 dto) {
        ResponseData<List<MtBomComponentVO19>> responseData = new ResponseData<List<MtBomComponentVO19>>();
        try {
            responseData.setRows(this.repository.attritionLimitEoComponentQtyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBomValidate")
    @PostMapping(value = {"/enable-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBomValidate(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.eoBomValidate(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBomUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoBomVO2> eoBomUpdate(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtEoBomVO dto) {

        ResponseData<MtEoBomVO2> responseData = new ResponseData<MtEoBomVO2>();
        try {
            responseData.setRows(repository.eoBomUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBomUpdateValidate")
    @PostMapping(value = {"/modify-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBomUpdateValidate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtEoBomVO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.eoBomUpdateValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBomBatchUpdate")
    @PostMapping(value = {"/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBomBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody MtEoBomVO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.eoBomBatchUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
    @ApiOperation(value = "eoBomBatchGet")
    @PostMapping(value = {"/property/batch/get"},produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBom>> eoBomBatchGet(@PathVariable("organizationId") Long tenantId, @RequestBody List<String> eoIds) {
        ResponseData<List<MtEoBom>> responseData = new ResponseData<List<MtEoBom>>();
        try {
            responseData.setRows(this.repository.eoBomBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
