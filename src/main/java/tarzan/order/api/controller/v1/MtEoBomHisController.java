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
import tarzan.order.domain.entity.MtEoBomHis;
import tarzan.order.domain.repository.MtEoBomHisRepository;
import tarzan.order.domain.vo.MtEoBomHisVO;

/**
 * EO装配清单历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoBomHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo-bom-his")
@Api(tags = "MtEoBomHis")
public class MtEoBomHisController extends BaseController {
    @Autowired
    private MtEoBomHisRepository repository;

    @ApiOperation(value = "eoBomHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoBomHis>> eoBomHisQuery(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtEoBomHisVO condition) {
        ResponseData<List<MtEoBomHis>> responseData = new ResponseData<List<MtEoBomHis>>();
        try {
            responseData.setRows(this.repository.eoBomHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
