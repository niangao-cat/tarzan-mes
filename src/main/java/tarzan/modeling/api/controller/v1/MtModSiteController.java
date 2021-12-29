package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
import tarzan.modeling.api.dto.MtModSiteDTO;
import tarzan.modeling.api.dto.MtModSiteDTO2;
import tarzan.modeling.api.dto.MtModSiteDTO3;
import tarzan.modeling.api.dto.MtModSiteDTO4;
import tarzan.modeling.api.dto.MtModSiteDTO5;
import tarzan.modeling.app.service.MtModSiteService;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModSiteVO;
import tarzan.modeling.domain.vo.MtModSiteVO2;
import tarzan.modeling.domain.vo.MtModSiteVO6;

/**
 * 站点 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModSiteController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-site")
@Api(tags = "MtModSite")
public class MtModSiteController extends BaseController {

    @Autowired
    private MtModSiteRepository repository;

    @Autowired
    private MtModSiteService mtModSiteService;

    @ApiOperation("siteBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModSite> siteBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String siteId) {
        ResponseData<MtModSite> responseData = new ResponseData<MtModSite>();
        try {
            responseData.setRows(this.repository.siteBasicPropertyGet(tenantId, siteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitSiteQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitSiteQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModSiteVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitSiteQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("siteBasicPropertyBatchGet")
    @PostMapping(value = "/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModSite>> siteBasicPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> siteIds) {
        ResponseData<List<MtModSite>> responseData = new ResponseData<List<MtModSite>>();
        try {
            responseData.setRows(this.repository.siteBasicPropertyBatchGet(tenantId, siteIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("siteBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> siteBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModSiteDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModSite param = new MtModSite();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.siteBasicPropertyUpdate(tenantId, param, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("站点维护界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModSiteVO2>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtModSiteDTO2 dto, @ApiIgnore @SortDefault(value = MtModSite.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModSiteVO2>> responseData = new ResponseData<Page<MtModSiteVO2>>();
        try {
            responseData.setRows(this.mtModSiteService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("站点维护单条数据显示")
    @GetMapping(value = "/record/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModSiteDTO3> queryInfoForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestParam String siteId) {
        ResponseData<MtModSiteDTO3> responseData = new ResponseData<MtModSiteDTO3>();
        try {
            responseData.setRows(this.mtModSiteService.queryInfoForUi(tenantId, siteId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("站点维护页面保存")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModSiteDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtModSiteService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询用户指定站点类型站点")
    @GetMapping(value = "/limit-user/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModSiteDTO>> queryUserSiteForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                               MtModSiteDTO5 dto) {
        ResponseData<List<MtModSiteDTO>> responseData = new ResponseData<List<MtModSiteDTO>>();
        try {
            responseData.setRows(
                    this.mtModSiteService.queryUserSiteForUi(tenantId, dto.getUserId(), dto.getSiteType()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitSitePropertyQuery")
    @PostMapping(value = "/property-limit/site/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModSiteVO6>> propertyLimitSitePropertyQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModSiteVO6 condition) {
        ResponseData<List<MtModSiteVO6>> responseData = new ResponseData<List<MtModSiteVO6>>();
        try {
            responseData.setRows(this.repository.propertyLimitSitePropertyQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "modSiteAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> modSiteAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.modSiteAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
