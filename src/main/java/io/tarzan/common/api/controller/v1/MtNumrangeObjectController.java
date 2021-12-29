package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectDTO3;
import io.tarzan.common.app.service.MtNumrangeObjectService;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.repository.MtNumrangeObjectRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO1;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO2;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 编码对象属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@RestController("mtNumrangeObjectController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-object")
@Api(tags = "MtNumrangeObject")
public class MtNumrangeObjectController extends BaseController {

    @Autowired
    private MtNumrangeObjectService service;

    @Autowired
    private MtNumrangeObjectRepository repository;


    @ApiOperation(value = "获取编码对象（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeObjectVO>> listNumrangeObjectForUi(@PathVariable("organizationId") Long tenantId,
                                                                          MtNumrangeObjectDTO2 condition,
                                                                          @ApiIgnore @SortDefault(value = MtNumrangeObject.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeObjectVO>> responseData = new ResponseData<Page<MtNumrangeObjectVO>>();
        try {
            responseData.setRows(service.listNumrangeObjectForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新编码对象（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveNumrangeObjectForUi(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtNumrangeObjectDTO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            validObject(dto);
            responseData.setRows(service.saveNumrangeObjectForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "propertyLimitNumrangeObjectQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitNumrangeObjectQuery(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody MtNumrangeObjectDTO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtNumrangeObject param = new MtNumrangeObject();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitNumrangeObjectQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "numrangeObjectGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeObject> numrangeObjectGet(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody String objectId) {
        ResponseData<MtNumrangeObject> responseData = new ResponseData<MtNumrangeObject>();
        try {
            responseData.setRows(repository.numrangeObjectGet(tenantId, objectId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNumrangeObjectPropertyQuery")
    @PostMapping(value = {"/numrange/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNumrangeObjectVO2>> propertyLimitNumrangeObjectPropertyQuery(@PathVariable("organizationId") Long tenantId, @RequestBody MtNumrangeObjectVO1 dto) {
        ResponseData<List<MtNumrangeObjectVO2>> result = new ResponseData<List<MtNumrangeObjectVO2>>();
        try {
            result.setRows(repository.propertyLimitNumrangeObjectPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
