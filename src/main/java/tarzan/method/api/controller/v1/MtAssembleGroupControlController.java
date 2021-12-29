package tarzan.method.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.method.api.dto.MtAssembleGroupControlDTO;
import tarzan.method.domain.entity.MtAssembleGroupControl;
import tarzan.method.domain.repository.MtAssembleGroupControlRepository;
import tarzan.method.domain.vo.MtAssembleGroupControlVO;
import tarzan.method.domain.vo.MtAssembleGroupControlVO1;
import tarzan.method.domain.vo.MtAssembleGroupControlVO2;

/**
 * 装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@RestController("mtAssembleGroupControlController.v1")
@RequestMapping("/v1/{organizationId}/mt-assemble-group-control")
@Api(tags = "MtAssembleGroupControl")
public class MtAssembleGroupControlController extends BaseController {

    @Autowired
    private MtAssembleGroupControlRepository mtAssembleGroupControlRepository;

    @ApiOperation(value = "wkcLimitAvailableAssembleGroupQuery")
    @PostMapping(value = "/limit-wkc/available/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> wkcLimitAvailableAssembleGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody MtAssembleGroupControlVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(
                            this.mtAssembleGroupControlRepository.wkcLimitAvailableAssembleGroupQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "assembleGroupControlUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> assembleGroupControlUpdate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtAssembleGroupControlDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtAssembleGroupControl param = new MtAssembleGroupControl();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.mtAssembleGroupControlRepository.assembleGroupControlUpdate(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitAssembleGroupControlPropertyQuery")
    @PostMapping(value = "/limit-property/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtAssembleGroupControlVO2>> propertyLimitAssembleGroupControlPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtAssembleGroupControlVO1 dto) {
        ResponseData<List<MtAssembleGroupControlVO2>> responseData =
                        new ResponseData<List<MtAssembleGroupControlVO2>>();
        try {
            responseData.setRows(this.mtAssembleGroupControlRepository
                            .propertyLimitAssembleGroupControlPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
