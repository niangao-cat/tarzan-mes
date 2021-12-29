package tarzan.actual.api.controller.v1;

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
import tarzan.actual.domain.repository.MtAssemblePointActualHisRepository;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO;
import tarzan.actual.domain.vo.MtAssemblePointActualHisVO1;

/**
 * 装配点实绩历史，记录装配组下装配点实际装配物料和数量变更记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssemblePointActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-point-actual-his")
@Api(tags = "MtAssemblePointActualHis")
public class MtAssemblePointActualHisController extends BaseController {

    @Autowired
    private MtAssemblePointActualHisRepository repository;

    @ApiOperation(value = "assemblePointActualHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssemblePointActualHisVO1>> assemblePointActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssemblePointActualHisVO condition) {
        ResponseData<List<MtAssemblePointActualHisVO1>> responseData =
                        new ResponseData<List<MtAssemblePointActualHisVO1>>();
        try {
            responseData.setRows(this.repository.assemblePointActualHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
