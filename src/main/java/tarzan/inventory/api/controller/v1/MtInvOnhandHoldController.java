package tarzan.inventory.api.controller.v1;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.inventory.api.dto.MtInvOnhandHoldDTO;
import tarzan.inventory.api.dto.MtInvOnhandHoldDTO2;
import tarzan.inventory.api.dto.MtInvOnhandHoldDTO3;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.app.service.MtInvOnhandHoldService;
import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.repository.MtInvOnhandHoldRepository;
import tarzan.inventory.domain.vo.*;

import java.util.List;

/**
 * 库存保留量 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@RestController("mtInvOnhandHoldController.v1")
@RequestMapping("/v1/{organizationId}/mt-inv-onhand-hold")
@Api(tags = "MtInvOnhandHold")
public class MtInvOnhandHoldController extends BaseController {

    @Autowired
    private MtInvOnhandHoldService service;

    @Autowired
    private MtInvOnhandHoldRepository repository;

    @ApiOperation("UI获取库存保留信息")
    @PostMapping("/limit-property/list/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtInvOnhandQuantityDTO>> queryInventoryHoldQuantityForUi(
                    @PathVariable(value = "organizationId") Long tenantId,@RequestBody MtInvOnhandHoldVO3 holdVO,
                    @ApiIgnore @SortDefault(value = MtInvOnhandHold.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {

        ResponseData<Page<MtInvOnhandQuantityDTO>> result = new ResponseData<Page<MtInvOnhandQuantityDTO>>();
        try {
            result.setRows(service.queryInventoryHoldQuantityForUi(tenantId, holdVO, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("onhandReserveGet")
    @PostMapping("/reserve-detail")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInvOnhandHold> onhandReserveGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String onhandHoldId) {

        ResponseData<MtInvOnhandHold> result = new ResponseData<MtInvOnhandHold>();
        try {
            result.setRows(repository.onhandReserveGet(tenantId, onhandHoldId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("onhandReserveBatchGet")
    @PostMapping("/reserve/detail/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandHold>> onhandReserveBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> onhandHoldIds) {

        ResponseData<List<MtInvOnhandHold>> responseData = new ResponseData<List<MtInvOnhandHold>>();
        try {
            responseData.setRows(repository.onhandReserveBatchGet(tenantId, onhandHoldIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitOnhandReserveQuery")
    @PostMapping("/limit-property/reserve")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitOnhandReserveQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInvOnhandHoldVO3 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.propertyLimitOnhandReserveQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("onhandReserveCreateVerify")
    @PostMapping("/reserve/add/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveCreateVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveCreateVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveCreate")
    @PostMapping("/reserve/add")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveCreate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO2 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveCreate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("onhandReserveReleaseVerify")
    @PostMapping("/reserve/release/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveReleaseVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO4 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveReleaseVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveRelease")
    @PostMapping("/reserve/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveRelease(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO5 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveRelease(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveAvailableVerify")
    @PostMapping("/reserve/available/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveAvailableVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveAvailableVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveUse")
    @PostMapping("/reserve/use/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveUse(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldVO6 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.onhandReserveUse(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveUseProcess")
    @PostMapping("/reserve/use/process/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveUseProcess(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldDTO dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtInvOnhandHoldVO7 dto1 = new MtInvOnhandHoldVO7();
            BeanUtils.copyProperties(dto, dto1);

            repository.onhandReserveUseProcess(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveCreateProcess")
    @PostMapping("/reserve/create/process/add")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveCreateProcess(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldDTO2 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtInvOnhandHoldVO8 dto1 = new MtInvOnhandHoldVO8();
            BeanUtils.copyProperties(dto, dto1);

            repository.onhandReserveCreateProcess(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("onhandReserveReleaseProcess")
    @PostMapping("/reserve/release/process")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> onhandReserveReleaseProcess(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtInvOnhandHoldDTO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtInvOnhandHoldVO9 dto1 = new MtInvOnhandHoldVO9();
            BeanUtils.copyProperties(dto, dto1);

            repository.onhandReserveReleaseProcess(tenantId, dto1);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }



}
