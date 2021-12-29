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
import org.springframework.web.bind.annotation.RestController;

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
import tarzan.material.api.dto.MtUomDTO;
import tarzan.material.api.dto.MtUomDTO2;
import tarzan.material.api.dto.MtUomDTO3;
import tarzan.material.api.dto.MtUomDTO4;
import tarzan.material.api.dto.MtUomDTO5;
import tarzan.material.api.dto.MtUomDTO6;
import tarzan.material.app.service.MtUomService;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.material.domain.vo.MtUomVO2;
import tarzan.material.domain.vo.MtUomVO3;
import tarzan.material.domain.vo.MtUomVO4;
import tarzan.material.domain.vo.MtUomVO5;

/**
 * 单位 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
@RestController("mtUomController.v1")
@RequestMapping("/v1/{organizationId}/mt-uom")
@Api(tags = "MtUom")
public class MtUomController extends BaseController {

    @Autowired
    private MtUomService service;

    @ApiOperation(value = "UI获取单位属性信息集合")
    @GetMapping(value = "/limit-property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtUom>> queryUomForUi(@PathVariable("organizationId") Long tenantId, MtUomDTO5 dto,
                                                   @ApiIgnore @SortDefault(value = MtUom.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtUom>> responseData = new ResponseData<Page<MtUom>>();
        try {
            responseData.setRows(this.service.queryUomForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI保存单位属性信息")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUom> saveUomForUi(@PathVariable("organizationId") Long tenantId, @RequestBody MtUomDTO6 dto) {
        validObject(dto);
        ResponseData<MtUom> responseData = new ResponseData<MtUom>();
        try {
            responseData.setRows(this.service.saveUomForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @Autowired
    private MtUomRepository repository;

    @ApiOperation(value = "uomPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUomVO> uomPropertyGet(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody String uomId) {

        ResponseData<MtUomVO> responseData = new ResponseData<MtUomVO>();
        try {
            responseData.setRows(this.repository.uomPropertyGet(tenantId, uomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitUomQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUomVO>> propertyLimitUomQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtUomDTO dto) {
        ResponseData<List<MtUomVO>> result = new ResponseData<List<MtUomVO>>();
        try {
            MtUomVO param = new MtUomVO();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.propertyLimitUomQuery(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "uomDecimalProcess")
    @PostMapping(value = {"/decimal/process-uom"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUomVO1> uomDecimalProcess(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtUomDTO2 dto) {
        ResponseData<MtUomVO1> result = new ResponseData<MtUomVO1>();
        try {
            MtUomVO1 param = new MtUomVO1();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.uomDecimalProcess(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "uomConversion")
    @PostMapping(value = {"/conversion"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUomVO1> uomConversion(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtUomDTO3 dto) {
        ResponseData<MtUomVO1> result = new ResponseData<MtUomVO1>();
        try {
            MtUomVO1 param = new MtUomVO1();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.uomConversion(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "primaryUomGet")
    @PostMapping(value = "/primary-uom", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUomVO2> primaryUomGet(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody String uomId) {
        ResponseData<MtUomVO2> result = new ResponseData<MtUomVO2>();
        try {
            result.setRows(repository.primaryUomGet(tenantId, uomId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "sameTypeUomQuery")
    @PostMapping(value = "/uom/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUom>> sameTypeUomQuery(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody String sourceUomId) {
        ResponseData<List<MtUom>> responseData = new ResponseData<List<MtUom>>();
        try {
            responseData.setRows(this.repository.sameTypeUomQuery(tenantId, sourceUomId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialUomConversion")
    @PostMapping(value = {"/material-uom/conversion"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtUomVO3> materialUomConversion(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtUomDTO4 dto) {
        ResponseData<MtUomVO3> result = new ResponseData<MtUomVO3>();
        try {
            MtUomVO3 param = new MtUomVO3();
            BeanUtils.copyProperties(dto, param);
            result.setRows(repository.materialUomConversion(tenantId, param));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "uomPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUomVO>> uomPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody List<String> uomIds) {

        ResponseData<List<MtUomVO>> responseData = new ResponseData<List<MtUomVO>>();
        try {
            responseData.setRows(this.repository.uomPropertyBatchGet(tenantId, uomIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitUomPropertyQuery")
    @PostMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtUomVO5>> propertyLimitUomPropertyQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtUomVO4 dto) {
        ResponseData<List<MtUomVO5>> result = new ResponseData<List<MtUomVO5>>();
        try {
            result.setRows(repository.propertyLimitUomPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "uomAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property-update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> uomAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtExtendVO10 dto) {
        ResponseData<String> result = new ResponseData<String>();
        try {
            repository.uomAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
