package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtBomSiteAssignDTO;
import tarzan.method.api.dto.MtBomSiteAssignDTO2;
import tarzan.method.app.service.MtBomSiteAssignService;
import tarzan.method.domain.entity.MtBomSiteAssign;
import tarzan.method.domain.repository.MtBomSiteAssignRepository;
import tarzan.method.domain.vo.MtBomSiteAssignVO;
import tarzan.method.domain.vo.MtBomSiteAssignVO2;

/**
 * 装配清单站点分配 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@RestController("mtBomSiteAssignController.v1")
@RequestMapping("/v1/{organizationId}/mt-bom-site-assign")
@Api(tags = "MtBomSiteAssign")
public class MtBomSiteAssignController extends BaseController {

    @Autowired
    private MtBomSiteAssignService service;

    @Autowired
    private MtBomSiteAssignRepository repository;

    @ApiOperation(value = "bomLimitEnableSiteQuery")
    @PostMapping(value = {"/enable/site/limit-bom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> bomLimitEnableSiteQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String bomId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.bomLimitEnableSiteQuery(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "siteLimitBomQuery")
    @PostMapping(value = {"/limit-site"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> siteLimitBomQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String siteId) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.siteLimitBomQuery(tenantId, siteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomSiteAssign")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomSiteAssign(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSiteAssignDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtBomSiteAssign condition = new MtBomSiteAssign();
            BeanUtils.copyProperties(dto, condition);

            repository.bomSiteAssign(tenantId, condition);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "分配站点查询（前台）")
    @GetMapping(value = {"/list/by/bom/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSiteAssignVO>> listSiteByBomForUi(@PathVariable("organizationId") Long tenantId,
                    @ApiParam("装配清单行主键") @RequestParam(name = "bomId") String bomId) {
        ResponseData<List<MtBomSiteAssignVO>> responseData = new ResponseData<List<MtBomSiteAssignVO>>();
        try {
            responseData.setRows(service.listBomSiteAssignUi(tenantId, bomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "分配站点查询（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveBomSiteAssignForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSiteAssignDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.saveBomSiteAssignForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomListLimitSiteQuery")
    @PostMapping(value = {"/limit-bom/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtBomSiteAssignVO2>> bomListLimitSiteQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtBomSiteAssignDTO2 dto) {
        ResponseData<List<MtBomSiteAssignVO2>> responseData = new ResponseData<>();
        try {
            responseData.setRows(repository.bomListLimitSiteQuery(tenantId, dto.getBomIds(), dto.getEnableFlag()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
