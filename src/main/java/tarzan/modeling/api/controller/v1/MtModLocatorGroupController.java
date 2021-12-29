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
import tarzan.modeling.api.dto.MtModLocatorGroupDTO;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO2;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO3;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO4;
import tarzan.modeling.app.service.MtModLocatorGroupService;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.repository.MtModLocatorGroupRepository;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO2;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO3;

/**
 * 库位组 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModLocatorGroupController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-locator-group")
@Api(tags = "MtModLocatorGroup")
public class MtModLocatorGroupController extends BaseController {

    @Autowired
    private MtModLocatorGroupRepository repository;

    @Autowired
    private MtModLocatorGroupService mtModLocatorGroupService;

    @ApiOperation("locatorGroupBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModLocatorGroup> locatorGroupBasicPropertyGet(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody String locatorGroupId) {
        ResponseData<MtModLocatorGroup> responseData = new ResponseData<MtModLocatorGroup>();
        try {
            responseData.setRows(this.repository.locatorGroupBasicPropertyGet(tenantId, locatorGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitLocatorGroupQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitLocatorGroupQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorGroupVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitLocatorGroupQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("locatorGroupBasicPropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorGroup>> locatorGroupBasicPropertyBatchGet(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> locatorGroupIds) {
        ResponseData<List<MtModLocatorGroup>> responseData = new ResponseData<List<MtModLocatorGroup>>();
        try {
            responseData.setRows(this.repository.locatorGroupBasicPropertyBatchGet(tenantId, locatorGroupIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorGroupBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorGroupBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                                                                @RequestBody MtModLocatorGroupDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModLocatorGroup param = new MtModLocatorGroup();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.locatorGroupBasicPropertyUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI库位组界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModLocatorGroupVO2>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                               MtModLocatorGroupDTO2 dto, @ApiIgnore @SortDefault(value = MtModLocatorGroup.FIELD_CREATION_DATE,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModLocatorGroupVO2>> responseData = new ResponseData<Page<MtModLocatorGroupVO2>>();
        try {
            responseData.setRows(this.mtModLocatorGroupService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI库位组单行查询")
    @GetMapping(value = "/one/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModLocatorGroupDTO4> oneForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                        @RequestParam String locatorGroupId) {
        ResponseData<MtModLocatorGroupDTO4> responseData = new ResponseData<MtModLocatorGroupDTO4>();
        try {
            responseData.setRows(this.mtModLocatorGroupService.oneForUi(tenantId, locatorGroupId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI库位组维护页面保存")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                                          @RequestBody MtModLocatorGroupDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtModLocatorGroupService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI库位组维护页面保存(同时保存扩展属性)")
    @PostMapping(value = "/save-with-attr/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveWithAttrForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtModLocatorGroupDTO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtModLocatorGroupService.saveWithAttrForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitLocatorGroupPropertyQuery")
    @PostMapping(value = "/locator/group/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorGroupVO3>> propertyLimitLocatorGroupPropertyQuery(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorGroupVO3 dto) {
        ResponseData<List<MtModLocatorGroupVO3>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.propertyLimitLocatorGroupPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "modLocatorGroupAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> modLocatorGroupAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.modLocatorGroupAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
