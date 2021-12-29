package tarzan.general.api.controller.v1;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.general.api.dto.MtTagGroupObjectDTO;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.general.api.dto.MtTagGroupObjectDTO4;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.vo.MtTagGroupObjectVO;
import tarzan.general.domain.vo.MtTagGroupObjectVO2;

/**
 * 数据收集组关联对象表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagGroupObjectController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-group-object")
@Api(tags = "MtTagGroupObject")
public class MtTagGroupObjectController extends BaseController {

    @Autowired
    private MtTagGroupObjectRepository repository;

    @ApiOperation(value = "tagGroupObjectGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagGroupObject> tagGroupObjectGet(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String tagGroupObjectId) {
        ResponseData<MtTagGroupObject> responseData = new ResponseData<MtTagGroupObject>();
        try {
            responseData.setRows(repository.tagGroupObjectGet(tenantId, tagGroupObjectId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitTagGroupObjectQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitTagGroupObjectQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtTagGroupObjectDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtTagGroupObject param = new MtTagGroupObject();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitTagGroupObjectQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "tagGroupObjectBatchUpdate")
    @PostMapping(value = {"/batch-save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Long> tagGroupObjectBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody List<MtTagGroupObjectVO> vo,
                                                        @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<Long> responseData = new ResponseData<Long>();
        try {
            responseData.setRows(repository.tagGroupObjectBatchUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "objectLimitTagGroupQuery")
    @PostMapping(value = {"/limit/object/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> objectLimitTagGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtTagGroupObjectVO2 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.objectLimitTagGroupQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "生产版本LOV")
    @GetMapping(value = {"/production/version/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtTagGroupObjectDTO3>> productionVersionQuery(@PathVariable("organizationId") Long tenantId,
                                                                             MtTagGroupObjectDTO4 dto, PageRequest pageRequest){
        return Results.success(repository.productionVersionQuery(tenantId, dto, pageRequest));
    }
}
