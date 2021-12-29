package tarzan.method.api.controller.v1;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtAssembleControlDTO;
import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.repository.MtAssembleControlRepository;
import tarzan.method.domain.vo.MtAssembleControlVO;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssembleControlVO2;

/**
 * 装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@RestController("mtAssembleControlController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-control")
@Api(tags = "MtAssembleControl")
public class MtAssembleControlController extends BaseController {

    @Autowired
    private MtAssembleControlRepository mtAssembleControlRepository;

    @ApiOperation(value = "allAssemblePointMaterialQtyCalculate")
    @PostMapping(value = "/point/material-qty/calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<Map<String, Object>>> allAssemblePointMaterialQtyCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleControlVO dto) {
        ResponseData<List<Map<String, Object>>> responseData = new ResponseData<List<Map<String, Object>>>();
        try {
            responseData.setRows(mtAssembleControlRepository.allAssemblePointMaterialQtyCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleControlUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assembleControlUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtAssembleControlDTO dto,
                                                      @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {

            MtAssembleControl dto1 = new MtAssembleControl();
            BeanUtils.copyProperties(dto, dto1);
            responseData.setRows(this.mtAssembleControlRepository.assembleControlUpdate(tenantId, dto1, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "assembleControlGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assembleControlGet(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtAssembleControlVO1 condition) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtAssembleControlRepository.assembleControlGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleControlPropertyQuery")
    @PostMapping(value = "/property/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleControlVO2>> propertyLimitAssembleControlPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleControlVO2 condition) {
        ResponseData<List<MtAssembleControlVO2>> responseData = new ResponseData<List<MtAssembleControlVO2>>();
        try {
            responseData.setRows(this.mtAssembleControlRepository.propertyLimitAssembleControlPropertyQuery(tenantId,
                            condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
