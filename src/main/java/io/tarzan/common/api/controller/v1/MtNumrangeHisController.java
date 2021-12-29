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
import io.swagger.annotations.ApiParam;
import io.tarzan.common.api.dto.MtNumrangeHisDTO;
import io.tarzan.common.app.service.MtNumrangeHisService;
import io.tarzan.common.domain.entity.MtNumrangeHis;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtNumrangeHisVO;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 号码段定义历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@RestController("mtNumrangeHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-his")
@Api(tags = "MtNumrangeHis")
public class MtNumrangeHisController extends BaseController {

    @Autowired
    private MtNumrangeHisService service;

    @ApiOperation(value = "号码段修改历史查询(前台)")
    @GetMapping(value = {"/his-list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeHisVO>> numrangeHisQueryForUi(
            @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
            MtNumrangeHisDTO dto, @ApiIgnore @SortDefault(value = MtNumrangeHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeHisVO>> result = new ResponseData<Page<MtNumrangeHisVO>>();
        try {
            result.setRows(service.numrangeHisQueryForUi(tenantId, dto, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
