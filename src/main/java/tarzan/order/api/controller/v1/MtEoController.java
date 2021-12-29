package tarzan.order.api.controller.v1;

import java.util.Arrays;
import java.util.List;

import com.ruike.hme.app.service.HmeSnBindEoService;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.order.api.dto.MtEoBomDTO;
import tarzan.order.api.dto.MtEoBomDTO4;
import tarzan.order.api.dto.MtEoDTO;
import tarzan.order.api.dto.MtEoDTO10;
import tarzan.order.api.dto.MtEoDTO11;
import tarzan.order.api.dto.MtEoDTO12;
import tarzan.order.api.dto.MtEoDTO3;
import tarzan.order.api.dto.MtEoDTO4;
import tarzan.order.api.dto.MtEoDTO5;
import tarzan.order.api.dto.MtEoDTO6;
import tarzan.order.api.dto.MtEoDTO7;
import tarzan.order.api.dto.MtEoDTO8;
import tarzan.order.api.dto.MtEoDTO9;
import tarzan.order.api.dto.MtEoRouterDTO;
import tarzan.order.api.dto.MtEoRouterDTO2;
import tarzan.order.api.dto.MtEoRouterDTO3;
import tarzan.order.api.dto.MtEoRouterDTO5;
import tarzan.order.api.dto.MtEoRouterDTO6;
import tarzan.order.api.dto.MtEoRouterDTO7;
import tarzan.order.api.dto.MtEoRouterDTO8;
import tarzan.order.app.service.MtEoService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;


/**
 * 执行作业【执行作业需求和实绩拆分开】 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@RestController("mtEoController.v1")
@RequestMapping("/v1/{organizationId}/mt-eo")
@Api(tags = "MtEo")
public class MtEoController extends BaseController {
    @Autowired
    private MtEoRepository repository;

    @Autowired
    private MtEoService service;

    @Autowired
    private HmeSnBindEoService hmeSnBindEoService;


    @ApiOperation(value = "eoUpdate")
    @PostMapping(value = {"/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoVO29> eoUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoDTO dto,
                                           @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtEoVO29> responseData = new ResponseData<MtEoVO29>();
        try {
            MtEoVO vo = new MtEoVO();
            BeanUtils.copyProperties(dto, vo);
            responseData.setRows(this.repository.eoUpdate(tenantId, vo, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEo> eoPropertyGet(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<MtEo> responseData = new ResponseData<MtEo>();
        try {
            responseData.setRows(this.repository.eoPropertyGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEo>> eoPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody List<String> eoIds) {
        ResponseData<List<MtEo>> responseData = new ResponseData<List<MtEo>>();
        try {
            responseData.setRows(this.repository.eoPropertyBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitWoGet")
    @PostMapping(value = {"/limit-wo"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoLimitWoGet(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoLimitWoGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woLimitEoQuery")
    @PostMapping(value = {"/limit-eo"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> woLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtEoVO2 vo) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.woLimitEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "numberLimitEoGet")
    @PostMapping(value = {"/limit-num"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> numberLimitEoGet(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody String eoNum) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.numberLimitEoGet(tenantId, eoNum));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusUpdate")
    @PostMapping(value = {"/update-status"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoStatusUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO3 vo) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoStatusUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusValidate")
    @PostMapping(value = {"/status-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> eoStatusValidate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtEoVO4 vo) {
        ResponseData<Boolean> responseData = new ResponseData<Boolean>();
        try {
            responseData.setSuccess(this.repository.eoStatusValidate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusAvailableValidate")
    @PostMapping(value = {"/status-available-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Boolean> eoStatusAvailableValidate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody String eoId) {
        ResponseData<Boolean> responseData = new ResponseData<Boolean>();
        try {
            responseData.setSuccess(this.repository.eoStatusAvailableValidate(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoMaterialVerify")
    @PostMapping(value = {"/material-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoMaterialVerify(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoMaterialVerify(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomRouterLimitEoQuery")
    @PostMapping(value = {"/limit-bom-router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> bomRouterLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtEoVO5 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.bomRouterLimitEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "eoNextNumberGet")
    @PostMapping(value = {"/next/number"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO5> eoNextNumberGet(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtEoVO32 dto) {
        ResponseData<MtNumrangeVO5> responseData = new ResponseData<MtNumrangeVO5>();
        try {
            responseData.setRows(this.repository.eoNextNumberGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoValidateVerifyUpdate")
    @PostMapping(value = {"/validate-verify/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoValidateVerifyUpdate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtEoVO16 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoValidateVerifyUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woLimitEoCreate")
    @PostMapping(value = {"/add/limit-wo"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> woLimitEoCreate(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtEoVO6 vo) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            String eoId = hmeSnBindEoService.woLimitEoCreate(tenantId, vo);
            responseData.setRows(eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "planTimeLimitEoQuery")
    @PostMapping(value = {"/limit-plan-time"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> planTimeLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtEoVO7 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.planTimeLimitEoQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "releaseEoLimitWoQtyUpdateVerify")
    @PostMapping(value = {"/qty/update-verify/limit-wo-qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> releaseEoLimitWoQtyUpdateVerify(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtEoVO8 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.releaseEoLimitWoQtyUpdateVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoSort")
    @PostMapping(value = {"/sort"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoSort(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody MtEoVO9 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoSort(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusUpdateVerify")
    @PostMapping(value = {"/status-change-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStatusUpdateVerify(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtEoDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStatusUpdateVerify(tenantId, dto.getEoId(), dto.getTargetStatus());
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoHoldVerify")
    @PostMapping(value = {"/hold-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoHoldVerify(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoHoldVerify(tenantId, eoId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoCloseVerify")
    @PostMapping(value = {"/close-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoCloseVerify(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoCloseVerify(tenantId, eoId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoReleaseVerify")
    @PostMapping(value = {"/release-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoReleaseVerify(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoReleaseVerify(tenantId, eoId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoPreProductValidate")
    @PostMapping(value = {"/preproduct-validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoPreProductValidate(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoPreProductValidate(tenantId, eoId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoHold")
    @PostMapping(value = {"/hold"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoHold(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoHold(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoHoldCancel")
    @PostMapping(value = {"/hold/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoHoldCancel(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO28 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoHoldCancel(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoClose")
    @PostMapping(value = {"/close"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoClose(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoClose(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoCloseCancel")
    @PostMapping(value = {"/close/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoCloseCancel(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoCloseCancel(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusComplete")
    @PostMapping(value = {"/status/complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStatusComplete(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody MtEoVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStatusComplete(tenantId, vo);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoCompleteVerify")
    @PostMapping(value = {"/complete-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoCompleteVerify(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody MtEoVO11 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoCompleteVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusCompleteVerify")
    @PostMapping(value = {"/status-complete-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStatusCompleteVerify(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStatusCompleteVerify(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoQtyUpdateVerify")
    @PostMapping(value = {"/qty/update-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoQtyUpdateVerify(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody MtEoVO12 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoQtyUpdateVerify(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAbandonVerify")
    @PostMapping(value = {"/abandon/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAbandonVerify(@PathVariable("organizationId") Long tenantId, @RequestBody String eoId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAbandonVerify(tenantId, eoId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAbandon")
    @PostMapping(value = {"/abandon"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAbandon(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO10 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoAbandon(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoQtyUpdate")
    @PostMapping(value = {"/qty/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoQtyUpdate(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO13 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoQtyUpdate(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "bomRouterLimitUniqueEoValidate")
    @PostMapping(value = {"/unique-validate/limit-bom-router"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> bomRouterLimitUniqueEoValidate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtEoVO17 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.bomRouterLimitUniqueEoValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "woLimitEoBatchCreate")
    @PostMapping(value = {"/batch/add/limit-wo"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> woLimitEoBatchCreate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtEoVO14 vo) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.woLimitEoBatchCreate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComplete")
    @PostMapping(value = {"/complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComplete(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO15 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComplete(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoCompleteCancel")
    @PostMapping(value = {"/complete/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoCompleteCancel(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody MtEoVO15 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoCompleteCancel(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoRelease")
    @PostMapping(value = {"/release"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoRelease(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO18 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoRelease(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoComponentQtyQuery")
    @PostMapping(value = {"/component/qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO20>> eoComponentQtyQuery(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtEoVO19 vo) {
        ResponseData<List<MtEoVO20>> responseData = new ResponseData<List<MtEoVO20>>();
        try {
            responseData.setRows(this.repository.eoComponentQtyQuery(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoReleaseAndStepQueue")
    @PostMapping(value = {"/release/step/queue"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoReleaseAndStepQueue(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtEoVO18 vo) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoReleaseAndStepQueue(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoOperationAssembleFlagGet")
    @PostMapping(value = {"/operation/assemble-flag"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoOperationAssembleFlagGet(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody String eoId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoOperationAssembleFlagGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoMerge")
    @PostMapping(value = {"/merge"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoMerge(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO22 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoMerge(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoMergeVerify")
    @PostMapping(value = {"/merge-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoMergeVerify(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO22 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoMergeVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoSplitVerify")
    @PostMapping(value = {"/split-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoSplitVerify(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO23 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoSplitVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoSplit")
    @PostMapping(value = {"/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoSplit(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO24 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.eoSplit(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "coproductEoCreate")
    @PostMapping(value = {"/coproduct/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoVO29> coproductEoCreate(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtEoVO25 dto) {
        ResponseData<MtEoVO29> responseData = new ResponseData<MtEoVO29>();
        try {
            responseData.setRows(this.repository.coproductEoCreate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoIdentify")
    @PostMapping(value = {"/identify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoIdentify(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody String identification) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoIdentify(tenantId, identification));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusCompleteCancel")
    @PostMapping(value = {"/status/complete/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStatusCompleteCancel(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtEoVO18 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoStatusCompleteCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "attrLimitEoQuery")
    @PostMapping(value = {"/limit-attr"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrLimitEoQuery(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody MtEoAttrVO1 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.attrLimitEoQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitAttrQuery")
    @PostMapping(value = {"/attr-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> eoLimitAttrQuery(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtEoAttrVO2 dto) {
        ResponseData<List<MtExtendAttrVO>> responseData = new ResponseData<List<MtExtendAttrVO>>();
        try {
            responseData.setRows(this.repository.eoLimitAttrQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoLimitAttrUpdate")
    @PostMapping(value = {"/save/limit-attr"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoLimitAttrUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtEoAttrVO3 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoLimitAttrUpdate(tenantId, dto));
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitEoAttrHisBatchQuery")
    @PostMapping(value = {"/attr/list/limit-even"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoAttrHisVO1>> eventLimitEoAttrHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtEoAttrHisVO1>> responseData = new ResponseData<List<MtEoAttrHisVO1>>();
        try {
            responseData.setRows(this.repository.eventLimitEoAttrHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAttrHisQuery")
    @PostMapping(value = {"/attr-his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoAttrHisVO1>> eoAttrHisQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtEoAttrHisVO2 dto) {
        ResponseData<List<MtEoAttrHisVO1>> responseData = new ResponseData<List<MtEoAttrHisVO1>>();
        try {
            responseData.setRows(this.repository.eoAttrHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitEoPropertyQuery")
    @PostMapping(value = {"/limit-property/eo"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO31>> propertyLimitEoPropertyQuery(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody MtEoVO30 dto) {
        ResponseData<List<MtEoVO31>> responseData = new ResponseData<List<MtEoVO31>>();
        try {
            responseData.setRows(this.repository.propertyLimitEoPropertyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAttrPropertyUpdate")
    @PostMapping(value = {"/attr-property/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.eoAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "eoComponentBatchSplit")
    @PostMapping(value = {"/eo-component/batch/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoComponentBatchSplit(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody MtEoVO34 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.eoComponentBatchSplit(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBatchNumberGet")
    @PostMapping(value = {"/eo/batch/number/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO8> eoBatchNumberGet(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtEoVO36 dto) {
        ResponseData<MtNumrangeVO8> responseData = new ResponseData<MtNumrangeVO8>();
        try {
            responseData.setRows(this.repository.eoBatchNumberGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBatchUpdate")
    @PostMapping(value = {"/eo/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO29>> eoBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                      @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate,
                                                      @RequestBody MtEoVO39 dto) {
        ResponseData<List<MtEoVO29>> responseData = new ResponseData<List<MtEoVO29>>();
        try {
            responseData.setRows(this.repository.eoBatchUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoAverageSplit")
    @PostMapping(value = {"/eo/average/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> eoAverageSplit(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtEoVO40 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.eoAverageSplit(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询执行作业列表")
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoDTO5>> eoListForUi(@PathVariable(value = "organizationId") Long tenantId, MtEoDTO4 dto,
                                                    @ApiIgnore @SortDefault(value = MtEo.FIELD_EO_NUM,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtEoDTO5>> responseData = new ResponseData<>();
        try {
            dto.setWorkOrderNumList(StringUtils.isNotBlank(dto.getWorkOrderNum()) ? Arrays.asList(StringUtils.split(dto.getWorkOrderNum(), ",")) : null);
            responseData.setRows(this.service.eoListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI查询执行作业明细信息")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDTO6> queryEoDetailForUi(@PathVariable("organizationId") Long tenantId, String eoId) {
        ResponseData<MtEoDTO6> responseData = new ResponseData<MtEoDTO6>();
        try {
            responseData.setRows(this.service.queryEoDetailForUi(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI执行作业明细信息保存")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoSaveForUi(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoDTO6 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.eoSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询工艺路线步骤列表")
    @GetMapping(value = "/router-step-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoRouterDTO5>> eoRouterStepListForUi(
                    @PathVariable(value = "organizationId") Long tenantId, String routerId,
                    @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoRouterDTO5>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.eoRouterStepListForUi(tenantId, routerId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询装配清单列表")
    @GetMapping(value = "/bom-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoBomDTO>> eoBomListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                         MtEoBomDTO4 dto, @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoBomDTO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.eoBomListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询步骤实绩列表")
    @GetMapping(value = "/router-step-actual-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoRouterDTO>> eoRouterStepActualListForUi(
                    @PathVariable(value = "organizationId") Long tenantId, MtEoRouterDTO7 dto,
                    @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoRouterDTO>> responseData = new ResponseData<Page<MtEoRouterDTO>>();
        try {
            responseData.setRows(this.service.eoRouterStepActualListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询不良实绩列表")
    @GetMapping(value = "/nc-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoRouterDTO2>> eoNcActualListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                                  String eoStepActualId, @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoRouterDTO2>> responseData = new ResponseData<Page<MtEoRouterDTO2>>();
        try {
            responseData.setRows(this.service.eoNcActualListForUi(tenantId, eoStepActualId, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询数据收集组列表")
    @GetMapping(value = "/tag-group-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoRouterDTO6>> eoTagGroupActualListForUi(
                    @PathVariable(value = "organizationId") Long tenantId, String eoStepActualId) {
        ResponseData<List<MtEoRouterDTO6>> responseData = new ResponseData<List<MtEoRouterDTO6>>();
        try {
            responseData.setRows(this.service.eoTagGroupActualListForUi(tenantId, eoStepActualId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询数据收集项列表")
    @GetMapping(value = "/tag-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoRouterDTO3>> eoTagActualListForUi(
                    @PathVariable(value = "organizationId") Long tenantId, MtEoRouterDTO8 dto,
                    @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoRouterDTO3>> responseData = new ResponseData<Page<MtEoRouterDTO3>>();
        try {
            responseData.setRows(this.service.eoTagActualListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询执行作业关系列表")
    @GetMapping(value = "/relation-list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtEoDTO7>> eoRelationListForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                            MtEoDTO12 dto, @ApiIgnore PageRequest pageRequest) {
        ResponseData<Page<MtEoDTO7>> responseData = new ResponseData<Page<MtEoDTO7>>();
        try {
            responseData.setRows(this.service.eoRelationListForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI查询执行作业下达完成数量")
    @GetMapping(value = "/release-complete-qty/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtEoDTO9> eoRelationCompleteQtyForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                             String eoId) {
        ResponseData<MtEoDTO9> responseData = new ResponseData<MtEoDTO9>();
        try {
            responseData.setRows(this.service.eoRelationCompleteQtyForUi(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI执行作业拆分")
    @PostMapping(value = {"/split/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoSplitForUi(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoDTO8 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.eoSplitForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "UI执行作业合并")
    @PostMapping(value = {"/merge/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> eoMergeForUi(@PathVariable("organizationId") Long tenantId,
                                             @RequestBody MtEoDTO10 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.service.eoMergeForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("UI执行作业状态变更")
    @PostMapping(value = "/status/update/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoStatusUpdateForUi(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody MtEoDTO11 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.service.eoStatusUpdateForUi(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eoKittingQtyCalculate")
    @PostMapping(value = "/kitting/qty/calculate", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> eoKittingQtyCalculate(@PathVariable(value = "organizationId") Long tenantId,
                                                      @RequestBody MtEoVO42 dto) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.eoKittingQtyCalculate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("eoCurrentQuantityGet")
    @PostMapping(value = "/quantity/get", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> eoCurrentQuantityGet(@PathVariable(value = "organizationId") Long tenantId,
                                                     @RequestBody String eoId) {
        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.eoCurrentQuantityGet(tenantId, eoId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoOperationAssembleFlagBatchGet")
    @PostMapping(value = {"/operation/assemble-flag/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO49>> eoOperationAssembleFlagBatchGet(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody List<String> eoIds) {
        ResponseData<List<MtEoVO49>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoOperationAssembleFlagBatchGet(tenantId, eoIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoStatusBatchUpdate")
    @PostMapping(value = {"/batch-update-status"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO29>> eoStatusBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody MtEoVO44 vo) {
        ResponseData<List<MtEoVO29>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoStatusBatchUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBatchRelease")
    @PostMapping(value = {"/release/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBatchRelease(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO50 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoBatchRelease(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBasicBatchUpdate")
    @PostMapping(value = {"/basic-update/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtEoVO29>> eoBasicBatchUpdate(@PathVariable("organizationId") Long tenantId,
                                                           @RequestBody MtEoVO46 vo) {
        ResponseData<List<MtEoVO29>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.eoBasicBatchUpdate(tenantId, vo));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoReleaseAndStepBatchQueue")
    @PostMapping(value = {"/release/step/queue/batch"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoReleaseAndStepBatchQueue(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody MtEoVO50 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoReleaseAndStepBatchQueue(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "eoBatchComplete")
    @PostMapping(value = {"/batch/complete"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> eoBatchComplete(@PathVariable("organizationId") Long tenantId, @RequestBody MtEoVO47 vo) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.eoBatchComplete(tenantId, vo);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
