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
import springfox.documentation.annotations.ApiIgnore;
import tarzan.modeling.api.dto.MtModEnterpriseDTO;
import tarzan.modeling.api.dto.MtModEnterpriseDTO2;
import tarzan.modeling.app.service.MtModEnterpriseService;
import tarzan.modeling.domain.entity.MtModEnterprise;
import tarzan.modeling.domain.repository.MtModEnterpriseRepository;
import tarzan.modeling.domain.vo.MtModEnterpriseVO;

/**
 * 企业 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModEnterpriseController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-enterprise")
@Api(tags = "MtModEnterprise")
public class MtModEnterpriseController extends BaseController {

    @Autowired
    private MtModEnterpriseRepository repository;

    @Autowired
    private MtModEnterpriseService mtModEnterpriseService;

    @ApiOperation("enterpriseBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModEnterprise> enterpriseBasicPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String enterpriseId) {
        ResponseData<MtModEnterprise> responseData = new ResponseData<MtModEnterprise>();
        try {
            responseData.setRows(this.repository.enterpriseBasicPropertyGet(tenantId, enterpriseId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("propertyLimitEnterpriseQuery")
    @PostMapping(value = "limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitEnterpriseQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtModEnterpriseVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitEnterpriseQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("enterpriseBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> enterpriseBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModEnterpriseDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModEnterprise param = new MtModEnterprise();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.enterpriseBasicPropertyUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI企业维护界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModEnterprise>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtModEnterpriseDTO2 dto, @ApiIgnore @SortDefault(value = MtModEnterprise.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModEnterprise>> responseData = new ResponseData<Page<MtModEnterprise>>();
        try {
            responseData.setRows(this.mtModEnterpriseService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI企业单行查询")
    @GetMapping(value = "/one/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModEnterprise> oneForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestParam String enterpriseId) {
        ResponseData<MtModEnterprise> responseData = new ResponseData<MtModEnterprise>();
        try {
            responseData.setRows(this.mtModEnterpriseService.oneForUi(tenantId, enterpriseId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI企业维护界面维护")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModEnterpriseDTO> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModEnterpriseDTO dto) {
        ResponseData<MtModEnterpriseDTO> responseData = new ResponseData<MtModEnterpriseDTO>();
        try {
            responseData.setRows(this.mtModEnterpriseService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
