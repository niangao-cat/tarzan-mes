package tarzan.method.api.controller.v1;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtNcGroupDTO;
import tarzan.method.api.dto.MtNcGroupDTO2;
import tarzan.method.api.dto.MtNcGroupDTO4;
import tarzan.method.app.service.MtNcGroupService;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.repository.MtNcGroupRepository;
import tarzan.method.domain.vo.MtNcGroupVO;
import tarzan.method.domain.vo.MtNcGroupVO1;

/**
 * 不良代码组 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@RestController("mtNcGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-nc-group")
@Api(tags = "MtNcGroup")
public class MtNcGroupController extends BaseController {

    @Autowired
    private MtNcGroupService service;

    @ApiOperation(value = "UI查询不良代码组")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNcGroupDTO>> queryNcGroupListForUi(@PathVariable("organizationId") Long tenantId,
                                                                  MtNcGroupDTO dto, @ApiIgnore @SortDefault(value = MtNcGroup.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNcGroupDTO>> responseData = new ResponseData<Page<MtNcGroupDTO>>();
        try {
            responseData.setRows(service.queryNcGroupListForUi(tenantId, dto, pageRequest));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI根据不良代码组Id查询不良代码组详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcGroupDTO2> queryNcGroupDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                               String ncGroupId) {
        ResponseData<MtNcGroupDTO2> responseData = new ResponseData<MtNcGroupDTO2>();
        try {
            responseData.setRows(service.queryNcGroupDetailForUi(tenantId, ncGroupId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存不良代码组")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveNcGroupForUi(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtNcGroupDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto.getMtNcGroup());
        if (CollectionUtils.isNotEmpty(dto.getMtNcSecondaryCodeList())) {
            validObject(dto.getMtNcSecondaryCodeList());
        }
        if (CollectionUtils.isNotEmpty(dto.getMtNcValidOperList())) {
            validObject(dto.getMtNcValidOperList());
        }
        try {
            responseData.setRows(service.saveNcGroupForUi(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtNcGroupRepository repository;

    @ApiOperation(value = "ncGroupPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNcGroup> ncGroupPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody String ncGroupId) {
        ResponseData<MtNcGroup> responseData = new ResponseData<MtNcGroup>();
        try {
            responseData.setRows(repository.ncGroupPropertyGet(tenantId, ncGroupId));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNcGroupPropertyQuery")
    @PostMapping(value = {"/nc/group/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNcGroupVO1>> propertyLimitNcGroupPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtNcGroupVO dto) {
        ResponseData<List<MtNcGroupVO1>> result = new ResponseData<List<MtNcGroupVO1>>();
        try {
            result.setRows(repository.propertyLimitNcGroupPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "ncGroupAttrPropertyUpdate")
    @PostMapping(value = {"/property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> ncGroupAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.ncGroupAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
