package tarzan.iface.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.iface.domain.vo.MtMaterialBasisVO;
import tarzan.iface.domain.vo.MtMaterialBasisVO1;
import tarzan.iface.domain.vo.MtMaterialBasisVO2;

/**
 * 物料业务属性表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:54
 */
@RestController("mtMaterialBasisController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-basis")
@Api(tags = "MtMaterialBasis")
public class MtMaterialBasisController extends BaseController {

    @Autowired
    private MtMaterialBasisRepository repository;

    @ApiOperation("materialBasicPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialBasic> materialBasicPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialBasisVO dto) {
        ResponseData<MtMaterialBasic> responseData = new ResponseData<MtMaterialBasic>();
        try {
            responseData.setRows(
                            this.repository.materialBasicPropertyGet(tenantId, dto.getSiteId(), dto.getMaterialId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitMaterialBasicPropertyQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialBasisVO2>> propertyLimitMaterialBasicPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialBasisVO1 dto) {
        ResponseData<List<MtMaterialBasisVO2>> result = new ResponseData<List<MtMaterialBasisVO2>>();
        try {
            result.setRows(repository.propertyLimitMaterialBasicPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
