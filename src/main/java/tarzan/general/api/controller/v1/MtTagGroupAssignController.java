package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import tarzan.general.api.dto.MtTagGroupAssignDTO;
import tarzan.general.api.dto.MtTagGroupAssignDTO2;
import tarzan.general.app.service.MtTagGroupAssignService;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.vo.MtTagGroupAssignVO;

/**
 * 数据收集项分配收集组表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupAssignController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group-assign")
@Api(tags = "MtTagGroupAssign")
public class MtTagGroupAssignController extends BaseController {

    @Autowired
    private MtTagGroupAssignService service;

    @ApiOperation(value = "UI查询数据收集组分配的数据收集项")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagGroupAssignDTO>> queryTagGroupAssignForUi(
                    @PathVariable("organizationId") Long tenantId, MtTagGroupAssignDTO2 dto,
                    @ApiIgnore @SortDefault(value = MtTagGroupAssign.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagGroupAssignDTO>> responseData = new ResponseData<Page<MtTagGroupAssignDTO>>();
        try {
            responseData.setRows(service.queryTagGroupAssignForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除数据收集组分配数据项")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> removeTagGroupAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody String tagGroupAssignId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            service.removeTagGroupAssignForUi(tenantId, tagGroupAssignId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtTagGroupAssignRepository repository;

    @ApiOperation(value = "tagGroupAssignGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagGroupAssign> tagGroupAssignGet(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String tagGroupAssignId) {
        ResponseData<MtTagGroupAssign> responseData = new ResponseData<MtTagGroupAssign>();
        try {
            responseData.setRows(this.repository.tagGroupAssignGet(tenantId, tagGroupAssignId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagGroupAssignBatchUpdate")
    @PostMapping(value = {"/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> tagGroupAssignBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody List<MtTagGroupAssignVO> tagGroupAssignList,
                                                        @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(this.repository.tagGroupAssignBatchUpdate(tenantId, tagGroupAssignList, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagGroupAssignBatchUpdate")
    @PostMapping(value = {"/property/limit"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitTagGroupAssignQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtTagGroupAssign dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitTagGroupAssignQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
