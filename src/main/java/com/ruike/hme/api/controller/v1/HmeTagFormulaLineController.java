package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeTagFormulaLineService;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.domain.vo.HmeTagFormulaLineVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.repository.HmeTagFormulaLineRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
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
import java.util.stream.Collectors;

/**
 * 数据采集项公式行表 管理 API
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
@RestController("hmeTagFormulaLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-formula-lines")
@Api(tags = SwaggerApiConfig.HME_TAG_FORMULA_LINE)
public class HmeTagFormulaLineController extends BaseController {

    @Autowired
    private HmeTagFormulaLineRepository hmeTagFormulaLineRepository;

    @Autowired
    private HmeTagFormulaLineService hmeTagFormulaLineService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @ApiOperation(value = "创建数据采集项公式行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/tag-formula-line/save-line")
    public ResponseEntity<List<HmeTagFormulaLine>> create(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody List<HmeTagFormulaLine> hmeTagFormulaLines) {
        validList(hmeTagFormulaLines);
        this.verifyData(tenantId, hmeTagFormulaLines, HmeConstants.ConstantValue.NO);
        hmeTagFormulaLineRepository.batchInsertSelective(hmeTagFormulaLines);
        return Results.success(hmeTagFormulaLines);
    }

    @ApiOperation(value = "修改数据采集项公式行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/tag-formula-line/save-line-update")
    public ResponseEntity<List<HmeTagFormulaLine>> update(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody List<HmeTagFormulaLine> hmeTagFormulaLines) {
        //SecurityTokenHelper.validToken(hmeTagFormulaLine);
        this.verifyData(tenantId, hmeTagFormulaLines, HmeConstants.ConstantValue.YES);
        hmeTagFormulaLineRepository.batchUpdateByPrimaryKeySelective(hmeTagFormulaLines);
        return Results.success(hmeTagFormulaLines);
    }

    private void verifyData(Long tenantId, List<HmeTagFormulaLine> hmeTagFormulaLines, String flag){
        //flag N-新增 Y-更新
        if(CollectionUtils.isNotEmpty(hmeTagFormulaLines)){
            if(StringUtils.equals(HmeConstants.ConstantValue.NO, flag)){
                List<String> parameterCodeList = hmeTagFormulaLines.stream().map(HmeTagFormulaLine::getParameterCode).collect(Collectors.toList());

                List<HmeTagFormulaLine> lineList = hmeTagFormulaLineRepository.selectByCondition(Condition.builder(HmeTagFormulaLine.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeTagFormulaLine.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeTagFormulaLine.FIELD_TAG_FORMULA_HEAD_ID, hmeTagFormulaLines.get(0).getTagFormulaHeadId())
                                .andIn(HmeTagFormulaLine.FIELD_PARAMETER_CODE, parameterCodeList)).build());
                if(CollectionUtils.isNotEmpty(lineList)){
                    List<String> codeList = lineList.stream().map(HmeTagFormulaLine::getParameterCode).collect(Collectors.toList());
                    throw new MtException("HME_TAG_FORMULA_HEAD_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TAG_FORMULA_HEAD_002", "HME", StringUtils.join(codeList,",")));
                }
            }
        }else {
            for (HmeTagFormulaLine hmeTagFormulaLine : hmeTagFormulaLines) {
                List<HmeTagFormulaLine> lineList = hmeTagFormulaLineRepository.selectByCondition(Condition.builder(HmeTagFormulaLine.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeTagFormulaLine.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeTagFormulaLine.FIELD_TAG_FORMULA_HEAD_ID, hmeTagFormulaLine.getTagFormulaHeadId())
                                .andEqualTo(HmeTagFormulaLine.FIELD_PARAMETER_CODE, hmeTagFormulaLine.getParameterCode())).build());

                if(CollectionUtils.isNotEmpty(lineList) && StringUtils.equals(lineList.get(0).getTagFormulaLineId(), hmeTagFormulaLine.getTagFormulaLineId())){
                    throw new MtException("HME_TAG_FORMULA_HEAD_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TAG_FORMULA_HEAD_002", "HME", hmeTagFormulaLine.getParameterCode()));
                }
            }
        }
    }

    /**
     * @param tenantId
     * @param tagFormulaHeadId
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/24 14:40
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaLineVO>>
     */
    @ApiOperation(value = "查询公式行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/tag-formula-line/get-line-list")
    public ResponseEntity<Page<HmeTagFormulaLineVO>> getTagLineList(@PathVariable("organizationId") Long tenantId,
                                                                    String tagFormulaHeadId,
                                                                    @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeTagFormulaLineService.getTagLineList(tenantId, tagFormulaHeadId, pageRequest));
    }

    /**
     * 删除数据采集项公式行表
     *
     * @param tenantId
     * @param tagFormulaLineId
     * @author guiming.zhou@hand-china.com 2020/9/25 14:00
     * @return org.springframework.http.ResponseEntity<?>
     */
    @ApiOperation(value = "删除数据采集项公式行表--自定义")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/tag-formula-line/delete-line-list/{tagFormulaLineId}")
    public ResponseEntity<?> deleteHmeTagFormulaLine(@PathVariable(value = "organizationId") Long tenantId,
                                                     @PathVariable String tagFormulaLineId) {
        hmeTagFormulaLineRepository.deleteByPrimaryKey(tagFormulaLineId);
        return Results.success();
    }

}
