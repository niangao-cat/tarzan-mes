package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.method.api.dto.MtSubstepDTO;
import tarzan.method.api.dto.MtSubstepDTO2;
import tarzan.method.app.service.MtSubstepService;
import tarzan.method.domain.entity.MtSubstep;
import tarzan.method.domain.repository.MtSubstepRepository;

/**
 * 子步骤 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
@RestController("mtSubstepController.v1")
@RequestMapping("/v1/{organizationId}/mt-substep")
@Api(tags = "MtSubstep")
public class MtSubstepController extends BaseController {

    @Autowired
    private MtSubstepRepository repository;

    @Autowired
    private MtSubstepService service;

    @ApiOperation(value = "UI查询子步骤")
    @GetMapping(value = {"/property/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtSubstepDTO2>> querySubstepForUi(@PathVariable("organizationId") Long tenantId,
                                                               MtSubstepDTO dto, @ApiIgnore @SortDefault(value = MtSubstep.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtSubstepDTO2>> responseData = new ResponseData<List<MtSubstepDTO2>>();
        try {
            responseData.setRows(service.querySubstepForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存子步骤")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveSubstepForUi(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtSubstepDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveSubstepForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除子步骤")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeSubstepForUi(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody String substepId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeSubstepForUi(tenantId, substepId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "substepGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtSubstep> substepGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String substepId) {
        ResponseData<MtSubstep> responseData = new ResponseData<MtSubstep>();
        try {
            responseData.setRows(this.repository.substepGet(tenantId, substepId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "substepBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtSubstep>> substepBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> substepIds) {
        ResponseData<List<MtSubstep>> responseData = new ResponseData<List<MtSubstep>>();
        try {
            responseData.setRows(this.repository.substepBatchGet(tenantId, substepIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
