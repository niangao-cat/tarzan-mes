package tarzan.general.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import tarzan.general.api.dto.MtTagGroupHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.app.service.MtTagGroupHisService;
import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.repository.MtTagGroupHisRepository;
import tarzan.general.domain.vo.MtTagGroupHisVO;

/**
 * 数据收集组历史表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupHisController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group-his")
@Api(tags = "MtTagGroupHis")
public class MtTagGroupHisController extends BaseController {

    @Autowired
    private MtTagGroupHisService service;

    @Autowired
    private MtTagGroupHisRepository repository;

    @ApiOperation(value = "UI查询数据收集组历史")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagGroupHisDTO>> queryTagGroupHisForUi(@PathVariable("organizationId") Long tenantId,
                                                                      MtTagGroupHisDTO2 dto, @ApiIgnore @SortDefault(value = MtTagGroupHis.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagGroupHisDTO>> responseData = new ResponseData<Page<MtTagGroupHisDTO>>();
        try {
            responseData.setRows(service.queryTagGroupHisForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagGroupLatestHisGet")
    @PostMapping(value = {"/his/get"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagGroupHisVO> tagGroupLatestHisGet(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody String tagGroupId) {
        ResponseData<MtTagGroupHisVO> responseData = new ResponseData<MtTagGroupHisVO>();
        try {
            responseData.setRows(repository.tagGroupLatestHisGet(tenantId, tagGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}
