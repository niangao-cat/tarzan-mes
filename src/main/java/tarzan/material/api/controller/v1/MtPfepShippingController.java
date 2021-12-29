package tarzan.material.api.controller.v1;

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
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.repository.MtPfepShippingRepository;

/**
 * 物料发运属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepShippingController.v1")
@RequestMapping("/v1/{organizationId}/mt-pfep-shipping")
@Api(tags = "MtPfepShipping")
public class MtPfepShippingController extends BaseController {

    @Autowired
    private MtPfepShippingRepository mtPfepShippingRepository;

    @ApiOperation(value = "pfepShippingAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> pfepShippingAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.mtPfepShippingRepository.pfepShippingAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
