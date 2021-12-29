package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEquipmentWkcRelDTO;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 设备工位关系表 管理 API
 *
 * @author han.zhang03@hand-china.com 2020-06-09 11:32:08
 */
@RestController("hmeEquipmentWkcRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-wkc-rel")
@Api(tags = SwaggerApiConfig.HME_EQUIP_WKC_REL)
@Slf4j
public class HmeEquipmentWkcRelController extends BaseController {

    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;

    /**
     * @Description 设备工位关系数据查询
     * @param hmeEquipmentWkcRel
     * @param pageRequest
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>>
     * @Date 2020-06-09 11:37
     * @Author han.zhang
     */
    @ApiOperation(value = "设备工位关系表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/ui", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeEquipmentWkcRel>> list(@PathVariable("organizationId") Long tenantId, HmeEquipmentWkcRelDTO hmeEquipmentWkcRel, @ApiIgnore PageRequest pageRequest) {
        log.info("<====HmeEquipmentWkcRelController-list:{},{}",tenantId, hmeEquipmentWkcRel);
        return Results.success(hmeEquipmentWkcRelRepository.queryBaseData(tenantId,pageRequest, hmeEquipmentWkcRel));
    }

    /**
     * @Description 新增or修改设备工位关系表
     * @param tenantId
     * @param hmeEquipmentWkcRels
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>
     * @Date 2020-06-09 14:11
     * @Author han.zhang
     */
    @ApiOperation(value = "新增or修改设备工位关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/update")
    public ResponseEntity<List<HmeEquipmentWkcRel>> update(@PathVariable("organizationId") Long tenantId, @RequestBody List<HmeEquipmentWkcRel> hmeEquipmentWkcRels) {
        log.info("<====HmeEquipmentWkcRelController-update:{},{}",tenantId, hmeEquipmentWkcRels);
        return Results.success( hmeEquipmentWkcRelRepository.update(tenantId, hmeEquipmentWkcRels));
    }

    @ApiOperation(value = "删除设备工位关系表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/delete")
    public ResponseEntity<?> delete(@PathVariable("organizationId") Long tenantId,@RequestBody HmeEquipmentWkcRel hmeEquipmentWkcRel) {
        log.info("<====HmeEquipmentWkcRelController-delete:{},{}",tenantId, hmeEquipmentWkcRel);
//      SecurityTokenHelper.validToken(hmeEquipmentWkcRel);
        hmeEquipmentWkcRelRepository.deleteByPrimaryKey(hmeEquipmentWkcRel);
        return Results.success();
    }

}
