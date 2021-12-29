package io.tarzan.common.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.api.dto.MtNumrangeAssignHisDTO;
import io.tarzan.common.app.service.MtNumrangeAssignHisService;
import io.tarzan.common.domain.entity.MtNumrangeAssignHis;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtNumrangeAssignHisVO;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 号码段分配历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@RestController("mtNumrangeAssignHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-assign-his")
@Api(tags = "MtNumrangeAssignHis")
public class MtNumrangeAssignHisController extends BaseController {

    @Autowired
    private MtNumrangeAssignHisService service;

    @ApiOperation(value = "获取号码段分配历史列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeAssignHisVO>> listNumrangeAssignHisForUi(
                    @PathVariable("organizationId") Long tenantId, MtNumrangeAssignHisDTO condition,
                    @ApiIgnore @SortDefault(value = MtNumrangeAssignHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeAssignHisVO>> responseData = new ResponseData<Page<MtNumrangeAssignHisVO>>();
        try {
            responseData.setRows(service.listNumrangeAssignHisForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
