package io.tarzan.common.api.controller.v1;

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
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.sys.ResponseData;

@RestController("mtUserController.v1")
@RequestMapping("/v1/{organizationId}/mt-user")
@Api(tags = "MtUser")
public class MtUserController extends BaseController {

    @Autowired
    private MtUserRepository mtUserRepository;

    @ApiOperation(value = "userPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUserInfo> userPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody Long userId) {
        ResponseData<MtUserInfo> result = new ResponseData<MtUserInfo>();
        try {
            result.setRows(mtUserRepository.userPropertyGet(tenantId, userId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
