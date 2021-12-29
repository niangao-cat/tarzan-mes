package tarzan.general.api.controller.v1;

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
import tarzan.general.api.dto.MtEventObjectColumnDTO;
import tarzan.general.app.service.MtEventObjectColumnService;
import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.repository.MtEventObjectColumnRepository;
import tarzan.general.domain.vo.MtEventObjectColumnVO;

/**
 * 对象列定义 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@RestController("mtEventObjectColumnController.v1")
@RequestMapping("/v1/{organizationId}/mt-event-object-column")
@Api(tags = "MtEventObjectColumn")
public class MtEventObjectColumnController extends BaseController {

    @Autowired
    private MtEventObjectColumnService service;

    @ApiOperation(value = "UI根据对象Id获取对象列")
    @GetMapping(value = {"/limit-property/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEventObjectColumn>> queryEventObjectColumnByEventObjectType(
                    @PathVariable("organizationId") Long tenantId, String objectTypeId,
                    @ApiIgnore @SortDefault(value = MtEventObjectColumn.FIELD_LINE_NUMBER,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtEventObjectColumn>> responseData = new ResponseData<List<MtEventObjectColumn>>();
        try {
            responseData.setRows(
                            service.queryEventObjectColumnByEventObjectTypeForUi(tenantId, objectTypeId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存对象列")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectColumnDTO> saveEventObjectColumn(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventObjectColumnDTO dto) {
        validObject(dto);
        ResponseData<MtEventObjectColumnDTO> responseData = new ResponseData<MtEventObjectColumnDTO>();
        try {
            responseData.setRows(service.saveEventObjectColumnForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI删除对象列")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Integer> deleteEventObjectColumnForUi(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> eventObjectColumnIdList) {
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        try {
            responseData.setRows(service.deleteEventObjectColumnForUi(tenantId, eventObjectColumnIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @Autowired
    private MtEventObjectColumnRepository repository;

    @ApiOperation(value = "propertyLimitObjectColumnQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitObjectColumnQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtEventObjectColumnVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.propertyLimitObjectColumnQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "objectColumnGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEventObjectColumn> objectColumnGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String objectColumnId) {
        ResponseData<MtEventObjectColumn> result = new ResponseData<MtEventObjectColumn>();
        try {
            result.setRows(repository.objectColumnGet(tenantId, objectColumnId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
