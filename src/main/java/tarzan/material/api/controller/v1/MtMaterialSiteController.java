package tarzan.material.api.controller.v1;

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
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendVO10;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.material.api.dto.MtMaterialSiteDTO;
import tarzan.material.api.dto.MtMaterialSiteDTO2;
import tarzan.material.api.dto.MtMaterialSiteDTO3;
import tarzan.material.app.service.MtMaterialSiteService;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MaterialSiteVO;
import tarzan.material.domain.vo.MtMaterialSiteVO;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;

/**
 * 物料站点分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@RestController("mtMaterialSiteController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-site")
@Api(tags = "MtMaterialSite")
public class MtMaterialSiteController extends BaseController {

    @Autowired
    private MtMaterialSiteRepository repository;
    @Autowired
    private MtMaterialSiteService mtMaterialSiteService;

    @ApiOperation(value = "materialSiteLimitRelationGet")
    @PostMapping(value = {"/material-site-limit"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialSiteLimitRelationGet(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtMaterialSiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtMaterialSite param = new MtMaterialSite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialSiteLimitRelationGet(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "relationLimitMaterialSiteGet")
    @PostMapping(value = "/relation-limit", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialSite> relationLimitMaterialSiteGet(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody String materialSiteId) {
        ResponseData<MtMaterialSite> result = new ResponseData<MtMaterialSite>();
        try {
            result.setRows(repository.relationLimitMaterialSiteGet(tenantId, materialSiteId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "siteLimitMaterialQuery")
    @PostMapping(value = {"/limit-site"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> siteLimitMaterialQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtMaterialSiteDTO2 dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            MtMaterialSite param = new MtMaterialSite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.siteLimitMaterialQuery(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialLimitSiteQuery")
    @PostMapping(value = {"/limit-material"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> materialLimitSiteQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtMaterialSiteDTO3 dto) {

        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            MtMaterialSite param = new MtMaterialSite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialLimitSiteQuery(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialSitePfepExistValidate")
    @PostMapping(value = {"/pfep/exist/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialSitePfepExistValidate(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtMaterialSiteDTO dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            MtMaterialSite param = new MtMaterialSite();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialSitePfepExistValidate(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation("物料维护站点查询(前台)")
    @GetMapping(value = "/site/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MaterialSiteVO>> materialSiteForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable(value = "organizationId") Long tenantId,
                    @ApiParam(value = "物料ID", required = true) @RequestParam String materialId,
                    @ApiIgnore @SortDefault(value = MtMaterialSite.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MaterialSiteVO>> responseData = new ResponseData<Page<MaterialSiteVO>>();
        try {
            responseData.setRows(mtMaterialSiteService.selectMaterialSiteById(tenantId, materialId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除物料站点信息(前台)")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialSiteDeleteForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料站点id集合") @RequestBody List<String> materialSiteIds) {

        ResponseData<Void> result = new ResponseData<Void>();
        try {
            mtMaterialSiteService.materialSiteDelete(tenantId, materialSiteIds);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialSiteAssign")
    @PostMapping(value = {"/site/assign"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialSiteAssign(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "物料站点") @RequestBody MtMaterialSiteVO vo) {

        ResponseData<String> result = new ResponseData<String>();
        try {
            result.setRows(repository.materialSiteAssign(tenantId, vo));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialSiteLimitRelationBatchGet")
    @PostMapping(value = {"/material/site/limit/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialSiteVO4>> materialSiteLimitRelationBatchGet(
            @PathVariable("organizationId") Long tenantId,
            @RequestBody List<MtMaterialSiteVO3> materialSiteIds) {
        ResponseData<List<MtMaterialSiteVO4>> result = new ResponseData<>();
        try {
            result.setRows(repository.materialSiteLimitRelationBatchGet(tenantId, materialSiteIds));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialSiteAttrPropertyUpdate")
    @PostMapping(value = {"/attr-table-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialSiteAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtExtendVO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            this.repository.materialSiteAttrPropertyUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
