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
import tarzan.general.api.dto.MtTagGroupAssignHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.app.service.MtTagGroupAssignHisService;
import tarzan.general.domain.entity.MtTagGroupAssignHis;

/**
 * 数据收集项分配收集组历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupAssignHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group-assign-his")
@Api(tags = "MtTagGroupAssignHis")
public class MtTagGroupAssignHisController extends BaseController {

    @Autowired
    private MtTagGroupAssignHisService service;

    @ApiOperation(value = "UI查询数据收集组分配数据项历史")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagGroupAssignHisDTO>> queryTagGroupAssignHisForUi(
                    @PathVariable("organizationId") Long tenantId, MtTagGroupHisDTO2 dto,
                    @ApiIgnore @SortDefault(value = MtTagGroupAssignHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagGroupAssignHisDTO>> responseData = new ResponseData<Page<MtTagGroupAssignHisDTO>>();
        try {
            responseData.setRows(service.queryTagGroupAssignHisForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
