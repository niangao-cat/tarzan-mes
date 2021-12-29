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
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.api.dto.MtTagGroupObjectHisDTO;
import tarzan.general.app.service.MtTagGroupObjectHisService;
import tarzan.general.domain.entity.MtTagGroupObjectHis;

/**
 * 数据收集组关联对象历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupObjectHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group-object-his")
@Api(tags = "MtTagGroupObjectHis")
public class MtTagGroupObjectHisController extends BaseController {

    @Autowired
    private MtTagGroupObjectHisService service;

    @ApiOperation(value = "UI查询数据收集组关联对象历史")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagGroupObjectHisDTO>> queryTagGroupObjectHisForUi(
                    @PathVariable("organizationId") Long tenantId, MtTagGroupHisDTO2 dto,
                    @ApiIgnore @SortDefault(value = MtTagGroupObjectHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagGroupObjectHisDTO>> responseData = new ResponseData<Page<MtTagGroupObjectHisDTO>>();
        try {
            responseData.setRows(service.queryTagGroupObjectHisForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
