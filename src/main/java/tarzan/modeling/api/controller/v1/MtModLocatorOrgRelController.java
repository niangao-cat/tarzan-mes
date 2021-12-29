package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.modeling.api.dto.MtModLocatorOrgRelDTO;
import tarzan.modeling.api.dto.MtModLocatorOrgRelDTO2;
import tarzan.modeling.api.dto.MtModLocatorOrgRelDTO4;
import tarzan.modeling.domain.entity.MtModLocatorOrgRel;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

/**
 * 组织与库位结构关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModLocatorOrgRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-locator-org-rel")
@Api(tags = "MtModLocatorOrgRel")
public class MtModLocatorOrgRelController extends BaseController {

    @Autowired
    private MtModLocatorOrgRelRepository repository;

    @ApiOperation("locatorOrganizationRelVerify")
    @PostMapping(value = "/organization-type-rel-verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorOrganizationRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                                                             @RequestBody MtModLocatorOrgRelDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.locatorOrganizationRelVerify(tenantId, dto.getLocatorId(),
                    dto.getParentOrganizationType(), dto.getParentOrganizationId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setRows("N");
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("organizationLimitLocatorQuery")
    @PostMapping(value = "/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorOrgRelVO>> organizationLimitLocatorQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorOrgRelDTO2 dto) {
        ResponseData<List<MtModLocatorOrgRelVO>> responseData = new ResponseData<List<MtModLocatorOrgRelVO>>();
        try {
            responseData.setRows(this.repository.organizationLimitLocatorQuery(tenantId, dto.getOrganizationType(),
                    String.valueOf(tenantId), dto.getQueryType()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("organizationLimitLocatorAllQuery")
    @PostMapping(value = "/all/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> organizationLimitLocatorAllQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorOrgRelVO3 queryVO) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.organizationLimitLocatorAllQuery(tenantId, queryVO));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorLimitOrganizationQuery")
    @PostMapping(value = "/limit-type")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorOrgRelVO3>> locatorLimitOrganizationQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorOrgRelVO2 dto) {
        ResponseData<List<MtModLocatorOrgRelVO3>> responseData = new ResponseData<List<MtModLocatorOrgRelVO3>>();
        try {
            responseData.setRows(this.repository.locatorLimitOrganizationQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorOrganizationRelDelete")
    @PostMapping(value = "/remove")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> locatorOrganizationRelDelete(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody MtModLocatorOrgRelDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtModLocatorOrgRel param = new MtModLocatorOrgRel();
            BeanUtils.copyProperties(dto, param);
            this.repository.locatorOrganizationRelDelete(tenantId, param);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
