package tarzan.material.api.controller.v1;

import java.util.List;

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
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.api.dto.MtPfepDistributionDTO1;
import tarzan.material.api.dto.MtPfepDistributionDTO2;
import tarzan.material.api.dto.MtPfepDistributionDTO3;
import tarzan.material.api.dto.MtPfepDistributionDTO4;
import tarzan.material.api.dto.MtPfepDistributionDTO5;
import tarzan.material.api.dto.MtPfepDistributionDTO6;
import tarzan.material.api.dto.MtPfepDistributionDTO7;
import tarzan.material.app.service.MtPfepDistributionService;
import tarzan.material.domain.repository.MtPfepDistributionRepository;
import tarzan.material.domain.vo.MtPfepDistributionVO;
import tarzan.material.domain.vo.MtPfepDistributionVO2;
import tarzan.material.domain.vo.MtPfepDistributionVO3;

/**
 * 物料配送属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@RestController("mtPfepDistributionController.v1")
@RequestMapping(value = {"/v1/{organizationId}/mt-pfep-distribution"})
@Api(tags = "MtPfepDistribution")
public class MtPfepDistributionController extends BaseController {

    @Autowired
    private MtPfepDistributionRepository repository;

    @Autowired
    private MtPfepDistributionService service;

    @ApiOperation(value = "pfepDistributionAttrPropertyUpdate")
    @PostMapping(value = {"/attr/table/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> pfepDistributionAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.pfepDistributionAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialDistributionPfepQuery")
    @PostMapping(value = {"/pfep/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPfepDistributionVO2>> materialDistributionPfepQuery(
            @PathVariable("organizationId") Long tenantId, @RequestBody MtPfepDistributionVO3 dto) {
        ResponseData<List<MtPfepDistributionVO2>> result = new ResponseData<List<MtPfepDistributionVO2>>();
        try {
            result.setRows(repository.materialDistributionPfepQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialPfepDistributionUpdate")
    @PostMapping(value = {"/pfep/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialPfepDistributionUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtPfepDistributionVO dto) {
        ResponseData<String> result = new ResponseData<>();
        try {
            result.setRows(repository.materialPfepDistributionUpdate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "物料配送属性查询（前台）")
    @GetMapping(value = {"/pfep/query/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtPfepDistributionDTO4>> materialPfepDistributionQueryForUi(
            @PathVariable("organizationId") Long tenantId, MtPfepDistributionDTO1 dto,
            PageRequest pageRequest) {
        ResponseData<Page<MtPfepDistributionDTO4>> responseData = new ResponseData<Page<MtPfepDistributionDTO4>>();
        try {
            responseData.setRows(service.materialPfepDistributionQueryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "物料配送属性保存（前台）)")
    @PostMapping(value = "/pfep/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialPfepDistributionSaveForUi(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtPfepDistributionDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.materialPfepDistributionSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "物料配送属性复制（前台）)")
    @PostMapping(value = "/pfep/copy/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepDistributionDTO7> materialPfepDistributionCopyForUi(@PathVariable("organizationId") Long tenantId,
                                                                                  @RequestBody MtPfepDistributionDTO3 dto) {
        ResponseData<MtPfepDistributionDTO7> responseData = new ResponseData<MtPfepDistributionDTO7>();
        try {
            responseData.setRows(this.service.materialPfepDistributionCopyForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "物料配送属性明細查詢（前台）)")
    @GetMapping(value = "/pfep/details/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPfepDistributionDTO6> materialPfepDistributionDetailsForUi(
            @PathVariable("organizationId") Long tenantId, MtPfepDistributionDTO5 dto) {
        ResponseData<MtPfepDistributionDTO6> responseData = new ResponseData<MtPfepDistributionDTO6>();
        try {
            responseData.setRows(this.service.materialPfepDistributionDetailsForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
