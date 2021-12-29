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
import tarzan.actual.domain.repository.MtAssembleGroupActualHisRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO;
import tarzan.actual.domain.vo.MtAssembleGroupActualHisVO2;

/**
 * 装配组实绩历史,记录装配组所有安装位置历史记录 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@RestController("mtAssembleGroupActualHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-group-actual-his")
@Api(tags = "MtAssembleGroupActualHis")
public class MtAssembleGroupActualHisController extends BaseController {

    @Autowired
    private MtAssembleGroupActualHisRepository repository;

    @ApiOperation(value = "assembleGroupActualHisQuery")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroupActualHisVO2>> assembleGroupActualHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupActualHisVO condition) {
        ResponseData<List<MtAssembleGroupActualHisVO2>> responseData =
                        new ResponseData<List<MtAssembleGroupActualHisVO2>>();
        try {
            responseData.setRows(this.repository.assembleGroupActualHisQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
