package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.MtTagGroupDTO;
import tarzan.general.api.dto.MtTagGroupDTO2;
import tarzan.general.api.dto.MtTagGroupDTO4;
import tarzan.general.api.dto.MtTagGroupDTO5;
import tarzan.general.api.dto.MtTagGroupDTO6;
import tarzan.general.api.dto.MtTagGroupDTO8;
import tarzan.general.app.service.MtTagGroupService;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.vo.MtTagGroupVO1;
import tarzan.general.domain.vo.MtTagGroupVO2;

/**
 * 数据收集组表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group")
@Api(tags = "MtTagGroup")
public class MtTagGroupController extends BaseController {

    @Autowired
    private MtTagGroupService service;

    @ApiOperation(value = "UI查询数据收集组列表信息")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagGroupDTO2>> queryTagGroupListForUi(@PathVariable("organizationId") Long tenantId,
                                                                     MtTagGroupDTO2 dto, @ApiIgnore @SortDefault(value = MtTagGroup.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagGroupDTO2>> responseData = new ResponseData<Page<MtTagGroupDTO2>>();
        try {
            responseData.setRows(service.queryTagGroupListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询数据收集组详细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagGroupDTO4> queryTagGroupDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                                 String tagGroupId) {
        ResponseData<MtTagGroupDTO4> responseData = new ResponseData<MtTagGroupDTO4>();
        try {
            responseData.setRows(service.queryTagGroupDetailForUi(tenantId, tagGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存数据收集组")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveTagGroupForUi(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtTagGroupDTO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveTagGroupForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI复制数据收集组")
    @PostMapping(value = {"/copy/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> copyTagGroupForUi(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtTagGroupDTO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.copyTagGroupForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtTagGroupRepository repository;

    @ApiOperation(value = "tagGroupGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagGroup> tagGroupGet(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody String tagGroupId) {
        ResponseData<MtTagGroup> responseData = new ResponseData<MtTagGroup>();
        try {
            responseData.setRows(repository.tagGroupGet(tenantId, tagGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitTagGroupQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitTagGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtTagGroupDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtTagGroup param = new MtTagGroup();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitTagGroupQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "tagGroupBatchGet")
    @PostMapping(value = {"/batch/info"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtTagGroup>> tagGroupBatchGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody List<String> dto) {
        ResponseData<List<MtTagGroup>> responseData = new ResponseData<List<MtTagGroup>>();
        try {
            responseData.setRows(repository.tagGroupBatchGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagGroupBasicBatchUpdate")
    @PostMapping(value = {"/batch/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> tagGroupBasicBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody List<MtTagGroupDTO8> dto,
                                                       @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(repository.tagGroupBasicBatchUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitTagGroupPropertyQuery")
    @PostMapping(value = {"/tag/group/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtTagGroupVO2>> propertyLimitTagGroupPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtTagGroupVO1 dto) {
        ResponseData<List<MtTagGroupVO2>> result = new ResponseData<List<MtTagGroupVO2>>();
        try {
            result.setRows(repository.propertyLimitTagGroupPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "tagGroupAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> tagGroupAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.tagGroupAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
