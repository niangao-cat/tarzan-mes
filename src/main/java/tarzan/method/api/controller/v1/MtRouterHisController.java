package tarzan.method.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtRouterHisDTO;
import tarzan.method.domain.entity.MtRouterHis;
import tarzan.method.domain.repository.MtRouterHisRepository;

/**
 * 工艺路线历史 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@RestController("mtRouterHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-router-his")
@Api(tags = "MtRouterHis")
public class MtRouterHisController extends BaseController {

    @Autowired
    private MtRouterHisRepository repository;

    @ApiOperation(value = "routerHisCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> routerHisCreate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtRouterHisDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtRouterHis mtRouterHis = new MtRouterHis();
            BeanUtils.copyProperties(dto,mtRouterHis);
            this.repository.routerHisCreate(tenantId, mtRouterHis);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
