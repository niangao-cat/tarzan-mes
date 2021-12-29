package tarzan.order.api.controller.v1;


import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.order.api.dto.MtWorkOrderRelDTO;
import tarzan.order.api.dto.MtWorkOrderRelDTO2;
import tarzan.order.api.dto.MtWorkOrderRelDTO3;
import tarzan.order.api.dto.MtWorkOrderRelDTO4;
import tarzan.order.domain.entity.MtWorkOrderRel;
import tarzan.order.domain.repository.MtWorkOrderRelRepository;
import tarzan.order.domain.vo.MtWorkOrderRelVO;

/**
 * 生产指令关系,标识生产指令的父子关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@RestController("mtWorkOrderRelController.v1")
@RequestMapping("/v1/{organizationId}/mt-work-order-rel")
@Api(tags = "MtWorkOrderRel")
public class MtWorkOrderRelController extends BaseController {

    @Autowired
    private MtWorkOrderRelRepository repository;

    @ApiOperation("woRelDelete")
    @PostMapping("/remove")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRelDelete(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);
            this.repository.woRelDelete(tenantId, rel);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelParentQuery")
    @PostMapping("/parent")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderRelVO>> woRelParentQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO2 dto) {
        ResponseData<List<MtWorkOrderRelVO>> responseData = new ResponseData<List<MtWorkOrderRelVO>>();
        try {

            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);
            responseData.setRows(this.repository.woRelParentQuery(tenantId, rel));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelChildQuery")
    @PostMapping("/child")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderRelVO>> woRelChildQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO3 dto) {
        ResponseData<List<MtWorkOrderRelVO>> responseData = new ResponseData<List<MtWorkOrderRelVO>>();
        try {
            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);
            responseData.setRows(this.repository.woRelSubQuery(tenantId, rel));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelTreeQuery")
    @PostMapping("/all")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtWorkOrderRelVO>> woRelTreeQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO4 dto) {
        ResponseData<List<MtWorkOrderRelVO>> responseData = new ResponseData<List<MtWorkOrderRelVO>>();
        try {

            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);

            responseData.setRows(this.repository.woRelTreeQuery(tenantId, rel));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelLimitChildQtyUpdate")
    @PostMapping("/child/qty/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRelLimitChildQtyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {

            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);

            this.repository.woRelLimitChildQtyUpdate(tenantId, rel);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woRelStatusLimitChildQtyUpdate")
    @PostMapping("/child/add-qty/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woRelStatusLimitChildQtyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);
            this.repository.woRelStatusLimitChildQtyUpdate(tenantId, rel);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("woLimitChildRelDelete")
    @PostMapping("/child/remove")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> woLimitChildRelDelete(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtWorkOrderRelDTO4 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            MtWorkOrderRel rel = new MtWorkOrderRel();
            BeanUtils.copyProperties(dto, rel);
            this.repository.woLimitSubRelDelete(tenantId, rel);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
