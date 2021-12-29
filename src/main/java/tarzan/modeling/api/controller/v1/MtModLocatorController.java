package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import tarzan.modeling.api.dto.*;
import tarzan.modeling.app.service.MtModLocatorService;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.*;

/**
 * 库位 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModLocatorController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-locator")
@Api(tags = "MtModLocator")
public class MtModLocatorController extends BaseController {

    @Autowired
    private MtModLocatorService mtModLocatorService;
    @Autowired
    private MtModLocatorRepository repository;

    @ApiOperation("新增&保存（前台）")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveModLocatorForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO5 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(this.mtModLocatorService.saveModLocatorForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("获取库位列表（前台）")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorVO6>> listModLocatorForUi(
                    @PathVariable(value = "organizationId") Long tenantId, MtModLocatorVO5 condition,
                    @ApiIgnore @SortDefault(value = MtModLocator.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtModLocatorVO6>> responseData = new ResponseData<List<MtModLocatorVO6>>();
        try {
            responseData.setRows(this.mtModLocatorService.listModLocatorForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("获取库位详情（前台）")
    @GetMapping(value = "/detail/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModLocatorVO3> detailModLocatorForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestParam(required = true) String locatorId) {
        ResponseData<MtModLocatorVO3> responseData = new ResponseData<MtModLocatorVO3>();
        try {
            responseData.setRows(this.mtModLocatorService.detailModLocatorForUi(tenantId, locatorId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModLocator> locatorBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String locatorId) {
        ResponseData<MtModLocator> responseData = new ResponseData<MtModLocator>();
        try {
            responseData.setRows(this.repository.locatorBasicPropertyGet(tenantId, locatorId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("subLocatorQuery")
    @PostMapping(value = "/sub-locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> subLocatorQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(dto.getLocatorId());
            mtModLocatorVO9.setQueryType(dto.getQueryType());
            responseData.setRows(this.repository.subLocatorQuery(tenantId, mtModLocatorVO9));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorGroupLimitLocatorQuery")
    @PostMapping(value = "/limit-group", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> locatorGroupLimitLocatorQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorDTO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.locatorGroupLimitLocatorQuery(tenantId, dto.getLocatorGroupId(),
                            dto.getQueryType()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("parentLocatorQuery")
    @PostMapping(value = "/parent-locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> parentLocatorQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.parentLocatorQuery(tenantId, dto.getLocatorId(), dto.getQueryType()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorLimitLocatorGroupGet")
    @PostMapping(value = "/limit-locator/group", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorLimitLocatorGroupGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String locatorId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.locatorLimitLocatorGroupGet(tenantId, locatorId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorRelVerify")
    @PostMapping(value = "/locator-rel-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorRelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(
                            this.repository.locatorRelVerify(tenantId, dto.getLocatorId(), dto.getParentLocatorId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitLocatorQuery")
    @PostMapping(value = "/limit-locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitLocatorQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitLocatorQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("locatorRelDelete")
    @PostMapping(value = "/remove", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> locatorRelDelete(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtModLocator param = new MtModLocator();
            BeanUtils.copyProperties(dto, param);
            this.repository.locatorRelDelete(tenantId, param);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorBasicPropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocator>> locatorBasicPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> locatorIds) {
        ResponseData<List<MtModLocator>> responseData = new ResponseData<List<MtModLocator>>();
        try {
            responseData.setRows(this.repository.locatorBasicPropertyBatchGet(tenantId, locatorIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorRecordOnhandVerify")
    @PostMapping(value = "/record-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> locatorRecordOnhandVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String locatorId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.locatorRecordOnhandVerify(tenantId, locatorId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorLimitInventoryCategoryLocatorGet")
    @PostMapping(value = "/category", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorLimitInventoryCategoryLocatorGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String locatorId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.locatorLimitInventoryCategoryLocatorGet(tenantId, locatorId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorTransferOnhandUpdateVerify")
    @PostMapping(value = "/transfer-save-verify", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> locatorTransferOnhandUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.locatorTransferOnhandUpdateVerify(tenantId, dto.getSourceLocatorId(),
                            dto.getTargetLocatorId());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorBasicPropertyUpdate")
    @PostMapping(value = "/basic/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> locatorBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModLocatorDTO7 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModLocator param = new MtModLocator();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.locatorBasicPropertyUpdate(tenantId, param, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorListLimitInvCategoryLocatorGet")
    @PostMapping(value = "/category/batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorVO15>> locatorListLimitInvCategoryLocatorGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> locatorIds) {

        ResponseData<List<MtModLocatorVO15>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.locatorListLimitInvCategoryLocatorGet(tenantId, locatorIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorListLimitSubLocatorQuery")
    @PostMapping(value = "/batch-sub-locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModLocatorVO14>> locatorListLimitSubLocatorQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModLocatorVO13 vo13) {
        ResponseData<List<MtModLocatorVO14>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.locatorListLimitSubLocatorQuery(tenantId, vo13));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
