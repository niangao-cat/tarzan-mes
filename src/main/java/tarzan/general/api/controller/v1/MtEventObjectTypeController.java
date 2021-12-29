package tarzan.general.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
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
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.MtEventObjectTypeColumnDTO;
import tarzan.general.api.dto.MtEventObjectTypeDTO;
import tarzan.general.app.service.MtEventObjectTypeService;
import tarzan.general.domain.entity.MtEventObjectType;
import tarzan.general.domain.repository.MtEventObjectTypeRepository;
import tarzan.general.domain.vo.MtEventObjectTypeVO;

/**
 * 对象类型定义 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventObjectTypeController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-object-type")
@Api(tags = "MtEventObjectType")
public class MtEventObjectTypeController extends BaseController {

    @Autowired
    private MtEventObjectTypeService service;

    @ApiOperation(value = "UI获取对象类型属性")
    @GetMapping(value = {"/limit-property/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEventObjectType>> queryEventObjectTypeForUi(
                    @PathVariable("organizationId") Long tenantId, MtEventObjectTypeDTO dto,
                    @ApiIgnore @SortDefault(value = MtEventObjectType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEventObjectType>> responseData = new ResponseData<Page<MtEventObjectType>>();
        try {
            responseData.setRows(service.queryEventObjectTypeForUi(tenantId, dto, pageRequest));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存对象类型属性")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectType> saveEventObjectTypeForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventObjectTypeDTO dto) {
        validObject(dto);
        ResponseData<MtEventObjectType> responseData = new ResponseData<MtEventObjectType>();
        try {
            responseData.setRows(service.saveEventObjectTypeForUi(tenantId, dto));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI获取对象类型展示信息")
    @GetMapping(value = {"/query-sql/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectTypeColumnDTO> queryEventObjectTypeQuerySqlForUi(
                    @PathVariable("organizationId") Long tenantId, String objectTypeId) {
        ResponseData<MtEventObjectTypeColumnDTO> responseData = new ResponseData<MtEventObjectTypeColumnDTO>();
        try {
            responseData.setRows(service.queryEventObjectTypeQuerySqlForUi(tenantId, objectTypeId));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除对象类型属性")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Integer> deleteEventObjectTypeForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> objectTypeIdList) {
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        try {
            responseData.setRows(service.deleteEventObjectTypeForUi(tenantId, objectTypeIdList));
        } catch (Exception e) {
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    @Autowired
    private MtEventObjectTypeRepository repository;

    @ApiOperation(value = "objectTypeGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectType> objectTypeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String objectTypeId) {
        ResponseData<MtEventObjectType> result = new ResponseData<MtEventObjectType>();
        try {
            result.setRows(repository.objectTypeGet(tenantId, objectTypeId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitObjectTypeQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitObjectTypeQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventObjectTypeVO dto) {
        ResponseData<List<String>> result = new ResponseData<List<String>>();
        try {
            result.setRows(repository.propertyLimitObjectTypeQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
