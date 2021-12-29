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
import tarzan.modeling.api.dto.MtModAreaDTO;
import tarzan.modeling.api.dto.MtModAreaDTO2;
import tarzan.modeling.api.dto.MtModAreaDTO3;
import tarzan.modeling.app.service.MtModAreaService;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.vo.MtModAreaVO1;

/**
 * 区域 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModAreaController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-area")
@Api(tags = "MtModArea")
public class MtModAreaController extends BaseController {

    @Autowired
    private MtModAreaService service;

    @Autowired
    private MtModAreaRepository repository;

    @ApiOperation(value = "UI获取基础区域属性集合")
    @GetMapping(value = "/limit-property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModArea>> queryModAreaForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtModAreaDTO dto, @ApiIgnore @SortDefault(value = MtModArea.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtModArea>> responseData = new ResponseData<List<MtModArea>>();
        try {
            responseData.setRows(this.service.queryModAreaForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI获取基础区域详细信息")
    @GetMapping(value = "/property/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModAreaDTO2> queryModAreaDetailForUi(@PathVariable(value = "organizationId") Long tenantId,
                    String areaId) {
        ResponseData<MtModAreaDTO2> responseData = new ResponseData<MtModAreaDTO2>();
        try {
            responseData.setRows(this.service.queryModAreaDetailForUi(tenantId, areaId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存基础区域属性")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModAreaDTO3> saveModAreaForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModAreaDTO3 dto) {
        validObject(dto);
        ResponseData<MtModAreaDTO3> responseData = new ResponseData<MtModAreaDTO3>();
        try {
            responseData.setRows(this.service.saveModAreaForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "areaBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModArea> areaBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String areaId) {
        ResponseData<MtModArea> responseData = new ResponseData<MtModArea>();
        try {
            responseData.setRows(this.repository.areaBasicPropertyGet(tenantId, areaId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAreaQuery")
    @PostMapping(value = "/property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitAreaQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModAreaVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitAreaQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "areaBasicPropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModArea>> areaBasicPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> areaIds) {
        ResponseData<List<MtModArea>> responseData = new ResponseData<List<MtModArea>>();
        try {
            responseData.setRows(this.repository.areaBasicPropertyBatchGet(tenantId, areaIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "areaBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> areaBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModAreaDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModArea mtModArea = new MtModArea();
            BeanUtils.copyProperties(dto, mtModArea);
            responseData.setRows(this.repository.areaBasicPropertyUpdate(tenantId, mtModArea, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
