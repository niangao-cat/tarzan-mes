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
import tarzan.order.domain.entity.MtEoRouterHis;
import tarzan.order.domain.repository.MtEoRouterHisRepository;
import tarzan.order.domain.vo.MtEoRouterHisVO;

/**
 * EO工艺路线历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoRouterHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-router-his")
@Api(tags = "MtEoRouterHis")
public class MtEoRouterHisController extends BaseController {
    @Autowired
    private MtEoRouterHisRepository repository;

    @ApiOperation(value = "eoRouterHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterHis>> eoRouterHisQuery(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtEoRouterHisVO condition) {
        ResponseData<List<MtEoRouterHis>> responseData = new ResponseData<List<MtEoRouterHis>>();
        try {
            responseData.setRows(this.repository.eoRouterHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
