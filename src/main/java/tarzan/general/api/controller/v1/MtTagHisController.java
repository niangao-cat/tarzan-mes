package tarzan.general.api.controller.v1;

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
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.MtTagHisDTO;
import tarzan.general.api.dto.MtTagHisDTO1;
import tarzan.general.app.service.MtTagHisService;
import tarzan.general.domain.entity.MtTagHis;

/**
 * 数据收集项历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-his")
@Api(tags = "MtTagHis")
public class MtTagHisController extends BaseController {
    @Autowired
    private MtTagHisService service;

    @ApiOperation(value = "UI查询数据采集项历史信息")
    @GetMapping(value = {"/query/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagHisDTO>> queryTagHistory(@PathVariable("organizationId") Long tenantId,
                                                           @ApiIgnore @SortDefault(value = MtTagHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                           MtTagHisDTO1 dto) {
        ResponseData<Page<MtTagHisDTO>> responseData = new ResponseData<Page<MtTagHisDTO>>();
        try {
            responseData.setRows(service.queryTagHistory(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
