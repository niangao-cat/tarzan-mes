package com.ruike.hme.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeProcessNcImportDTO;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.repository.HmeProcessNcDetailRepository;
import com.ruike.hme.domain.repository.HmeProcessNcHeaderRepository;
import com.ruike.hme.domain.repository.HmeProcessNcLineRepository;
import com.ruike.hme.domain.vo.HmeTagGroupAssignImportVO;
import com.ruike.hme.infra.mapper.HmeProcessNcDetailMapper;
import com.ruike.hme.infra.mapper.HmeProcessNcHeaderMapper;
import com.ruike.hme.infra.mapper.HmeProcessNcLineMapper;
import com.ruike.itf.domain.entity.ItfFsmCollectIface;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 工序不良判定标准维护批量导入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/12 14:46
 */
@ImportService(templateCode = "HME.PROCESS_NC_IMPORT")
public class HmeProcessNcImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private HmeProcessNcLineRepository hmeProcessNcLineRepository;

    @Autowired
    private HmeProcessNcLineMapper hmeProcessNcLineMapper;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private HmeProcessNcHeaderMapper hmeProcessNcHeaderMapper;

    @Autowired
    private HmeProcessNcDetailRepository hmeProcessNcDetailRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private HmeProcessNcDetailMapper hmeProcessNcDetailMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doImport(List<String> data) {
        //状态
        MtGenStatus mtGenStatus = new MtGenStatus();
        mtGenStatus.setModule("GENERAL");
        mtGenStatus.setStatusGroup("TAG_GROUP_STATUS");
        List<MtGenStatus> mtGenStatusList = mtGenStatusRepository.select(mtGenStatus);
        List<String> collect = mtGenStatusList.stream().map(MtGenStatus::getDescription).collect(Collectors.toList());
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        List<HmeProcessNcImportDTO> processNcImportList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeProcessNcImportDTO importDTO = null;
                try {
                    importDTO = objectMapper.readValue(vo, HmeProcessNcImportDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                if (importDTO.getTenantId() != null) {
                    tenantId = importDTO.getTenantId();
                }

                //物料
                List<MtMaterial> mtMaterialList = mtMaterialRepository.select(MtMaterial.FIELD_MATERIAL_CODE, importDTO.getMaterialCode());
                if (CollectionUtil.isEmpty(mtMaterialList)) {
                    throw new MtException("HME_MATERIAL_00001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_MATERIAL_00001", "HME", importDTO.getMaterialCode()));
                }
                importDTO.setMaterialId(mtMaterialList.get(0).getMaterialId());
                //工艺
                List<MtOperation> operationList = mtOperationRepository.select(MtOperation.FIELD_OPERATION_NAME, importDTO.getOperationName());
                if (CollectionUtil.isNotEmpty(operationList)) {
                    importDTO.setOperationId(operationList.get(0).getOperationId());
                }
                //数据项编码
                if (StringUtils.isBlank(importDTO.getTagCode())) {
                    throw new MtException("HME_TAGCODE_00001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TAGCODE_00001", "HME", importDTO.getTagCode()));
                }
                List<MtTag> mtTagList = mtTagRepository.select(MtTag.FIELD_TAG_CODE, importDTO.getTagCode());
                if (CollectionUtil.isEmpty(mtTagList)) {
                    throw new MtException("HME_TAGCODE_00001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TAGCODE_00001", "HME", importDTO.getTagCode()));
                }
                importDTO.setTagId(mtTagList.get(0).getTagId());
                //数据组编码
                if (StringUtils.isNotBlank(importDTO.getTagGroupCode())) {
                    List<MtTagGroup> mtTagGroupList = mtTagGroupRepository.select(MtTagGroup.FIELD_TAG_GROUP_CODE, importDTO.getTagGroupCode());
                    if (CollectionUtil.isEmpty(mtTagGroupList)) {
                        throw new MtException("HME_TAGGROUP_00001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_TAGGROUP_00001", "HME", importDTO.getTagGroupCode()));
                    }
                    importDTO.setTagGroupId(mtTagGroupList.get(0).getTagGroupId());
                }

                //不良编码
                List<MtNcCode> mtNcCodeList = new ArrayList<>();
                if (StringUtils.isNotBlank(importDTO.getNcCode())) {
                    mtNcCodeList = mtNcCodeRepository.select(MtNcCode.FIELD_NC_CODE, importDTO.getNcCode());
                    importDTO.setNcCodeId(mtNcCodeList.get(0).getNcCodeId());
                }
                //状态status不存在，报错：未定义的状态!
                if (!collect.contains(importDTO.getStatus())) {
                    throw new MtException("HME_GENSTATUS_00001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_GENSTATUS_00001", "HME", importDTO.getStatus()));
                }
                for (MtGenStatus genStatus : mtGenStatusList) {
                    if (genStatus.getDescription().equals(importDTO.getStatus())) {
                        importDTO.setStatusCode(genStatus.getStatusCode());
                    }
                }

                // 工序 有值 则需要存在
                if (StringUtils.isNotBlank(importDTO.getWorkcellCode())) {
                    List<MtModWorkcell> workcellList = mtModWorkcellRepository.selectByCondition(Condition.builder(MtModWorkcell.class).andWhere(Sqls.custom()
                            .andEqualTo(MtModWorkcell.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtModWorkcell.FIELD_WORKCELL_CODE, importDTO.getWorkcellCode())).build());
                    if (CollectionUtils.isEmpty(workcellList)) {
                        throw new MtException("HME_PROCESS_NC_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_PROCESS_NC_IMPORT_001", "HME", importDTO.getWorkcellCode()));
                    }
                    importDTO.setProcessId(workcellList.get(0).getWorkcellId());
                }
                processNcImportList.add(importDTO);
            }
            this.handleSaveProcessNc(tenantId, processNcImportList, mtGenStatusList);
        }
        return true;
    }

    /**
     * 处理头 行 明细
     *
     * @param tenantId
     * @param processNcImportList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/29 16:44
     */
    private void handleSaveProcessNc(Long tenantId, List<HmeProcessNcImportDTO> processNcImportList, List<MtGenStatus> mtGenStatusList) {
        // 根据物料 产品代码 COS型号 芯片组合 工艺编码 分组 即根据头分组
        Map<String, List<HmeProcessNcImportDTO>> headerMap = processNcImportList.stream().collect(Collectors.groupingBy(header -> spliceHeaderStr(header),LinkedHashMap::new, Collectors.toList()));
        // 批量新增或更新
        List<HmeProcessNcHeader> updateHeaderList = new ArrayList<>();
        List<HmeProcessNcLine> updateLineList = new ArrayList<>();
        List<HmeProcessNcDetail> updateDetailList = new ArrayList<>();

        for (Map.Entry<String, List<HmeProcessNcImportDTO>> headerListEntry : headerMap.entrySet()) {
            List<HmeProcessNcImportDTO> valueList = headerListEntry.getValue();
            // 同一个工序不良头的导入方式要一致
            List<String> importTypeList = valueList.stream().map(HmeProcessNcImportDTO::getImportType).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (importTypeList.size() > 1) {
                throw new MtException("HME_EXCEL_IMPORT_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_045", "HME"));
            }
            List<HmeProcessNcHeader> hmeProcessNcHeaderList = hmeProcessNcHeaderMapper.queryProcessNcHeader(tenantId, valueList.get(0));
            HmeProcessNcImportDTO hmeProcessNcHeaderDto = valueList.get(0);
            // 头信息
            HmeProcessNcHeader hmeProcessNcHeader = new HmeProcessNcHeader();
            hmeProcessNcHeader.setTenantId(tenantId);
            hmeProcessNcHeader.setMaterialId(hmeProcessNcHeaderDto.getMaterialId());
            hmeProcessNcHeader.setProductCode(hmeProcessNcHeaderDto.getProductCode());
            hmeProcessNcHeader.setCosModel(hmeProcessNcHeaderDto.getCosModel());
            hmeProcessNcHeader.setChipCombination(hmeProcessNcHeaderDto.getChipCombination());
            hmeProcessNcHeader.setOperationId(hmeProcessNcHeaderDto.getOperationId());
            hmeProcessNcHeader.setWorkcellId(hmeProcessNcHeaderDto.getProcessId());
            hmeProcessNcHeader.setStatus(hmeProcessNcHeaderDto.getStatusCode());

            // 根据行 数据项 数据收集组及编码分组
            Map<String, List<HmeProcessNcImportDTO>> lineMap = valueList.stream().collect(Collectors.groupingBy(header -> spliceListStr(header), LinkedHashMap::new, Collectors.toList()));
            if ("INCREASE".equals(hmeProcessNcHeaderDto.getImportType())) {
                if (CollectionUtils.isEmpty(hmeProcessNcHeaderList)) {
                    // 新增 头 行 明细
                    hmeProcessNcHeaderRepository.insertSelective(hmeProcessNcHeader);
                    for (Map.Entry<String, List<HmeProcessNcImportDTO>> lineListEntry : lineMap.entrySet()) {
                        List<HmeProcessNcImportDTO> detailList = lineListEntry.getValue();
                        HmeProcessNcImportDTO hmeProcessNcLineDto = detailList.get(0);
                        // 插入行信息
                        HmeProcessNcLine hmeProcessNcLine = new HmeProcessNcLine();
                        hmeProcessNcLine.setTenantId(tenantId);
                        hmeProcessNcLine.setTagId(hmeProcessNcLineDto.getTagId());
                        hmeProcessNcLine.setTagGroupId(hmeProcessNcLineDto.getTagGroupId());
                        hmeProcessNcLine.setStandardCode(hmeProcessNcLineDto.getDataStandardCode());
                        hmeProcessNcLine.setPriority(hmeProcessNcLineDto.getPriority());
                        hmeProcessNcLine.setHeaderId(hmeProcessNcHeader.getHeaderId());
                        hmeProcessNcLineRepository.insertSelective(hmeProcessNcLine);

                        for (HmeProcessNcImportDTO hmeProcessNcImportDTO : detailList) {
                            // 明细
                            HmeProcessNcDetail hmeProcessNcDetail = new HmeProcessNcDetail();
                            hmeProcessNcDetail.setTenantId(tenantId);
                            hmeProcessNcDetail.setStandardCode(hmeProcessNcImportDTO.getStandardCode());
                            hmeProcessNcDetail.setMinValue(hmeProcessNcImportDTO.getMinValue());
                            hmeProcessNcDetail.setMaxValue(hmeProcessNcImportDTO.getMaxValue());
                            hmeProcessNcDetail.setNcCodeId(hmeProcessNcImportDTO.getNcCodeId());
                            hmeProcessNcDetail.setLineId(hmeProcessNcLine.getLineId());
                            hmeProcessNcDetail.setHeaderId(hmeProcessNcHeader.getHeaderId());
                            hmeProcessNcDetailRepository.insertSelective(hmeProcessNcDetail);
                        }
                    }
                } else {
                    for (Map.Entry<String, List<HmeProcessNcImportDTO>> lineListEntry : lineMap.entrySet()) {
                        // 头存在 行、明细不存在则新增
                        List<HmeProcessNcImportDTO> detailList = lineListEntry.getValue();
                        HmeProcessNcImportDTO hmeProcessNcLineDto = detailList.get(0);
                        HmeProcessNcLine hmeProcessNcLine = new HmeProcessNcLine();
                        hmeProcessNcLine.setTenantId(tenantId);
                        hmeProcessNcLine.setTagId(hmeProcessNcLineDto.getTagId());
                        hmeProcessNcLine.setTagGroupId(hmeProcessNcLineDto.getTagGroupId());
                        hmeProcessNcLine.setStandardCode(hmeProcessNcLineDto.getDataStandardCode());
                        hmeProcessNcLine.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                        List<HmeProcessNcLine> hmeProcessNcLineList = hmeProcessNcLineMapper.queryProcessNcLineList(tenantId, hmeProcessNcLine);

                        if (CollectionUtils.isEmpty(hmeProcessNcLineList)) {
                            // 插入行、明细
                            hmeProcessNcLine.setPriority(hmeProcessNcLineDto.getPriority());
                            hmeProcessNcLineRepository.insertSelective(hmeProcessNcLine);
                            for (HmeProcessNcImportDTO hmeProcessNcImportDTO : detailList) {
                                HmeProcessNcDetail hmeProcessNcDetail = new HmeProcessNcDetail();
                                hmeProcessNcDetail.setTenantId(tenantId);
                                hmeProcessNcDetail.setStandardCode(hmeProcessNcImportDTO.getStandardCode());
                                hmeProcessNcDetail.setMinValue(hmeProcessNcImportDTO.getMinValue());
                                hmeProcessNcDetail.setMaxValue(hmeProcessNcImportDTO.getMaxValue());
                                hmeProcessNcDetail.setNcCodeId(hmeProcessNcImportDTO.getNcCodeId());
                                hmeProcessNcDetail.setLineId(hmeProcessNcLine.getLineId());
                                hmeProcessNcDetail.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                                hmeProcessNcDetailRepository.insertSelective(hmeProcessNcDetail);
                            }
                        } else {
                            // 删除明细 覆盖之前的
                            hmeProcessNcDetailMapper.deleteByLine(tenantId, hmeProcessNcLineList.get(0));
                            for (HmeProcessNcImportDTO hmeProcessNcImportDTO : detailList) {
                                HmeProcessNcDetail hmeProcessNcDetail = new HmeProcessNcDetail();
                                hmeProcessNcDetail.setTenantId(tenantId);
                                hmeProcessNcDetail.setStandardCode(hmeProcessNcImportDTO.getStandardCode());
                                hmeProcessNcDetail.setMinValue(hmeProcessNcImportDTO.getMinValue());
                                hmeProcessNcDetail.setMaxValue(hmeProcessNcImportDTO.getMaxValue());
                                hmeProcessNcDetail.setNcCodeId(hmeProcessNcImportDTO.getNcCodeId());
                                hmeProcessNcDetail.setLineId(hmeProcessNcLineList.get(0).getLineId());
                                hmeProcessNcDetail.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                                hmeProcessNcDetailRepository.insertSelective(hmeProcessNcDetail);
                            }
                        }
                    }
                }
            } else if ("UPDATE".equals(hmeProcessNcHeaderDto.getImportType())) {
                // 修改
                //校验数据
                if (CollectionUtil.isEmpty(hmeProcessNcHeaderList)) {
                    //产品信息不存在,请检查!
                    throw new MtException("HME_PRODUCT_00003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_PRODUCT_00003", "HME", hmeProcessNcHeaderDto.getMaterialCode(), hmeProcessNcHeaderDto.getProductCode(), hmeProcessNcHeaderDto.getCosModel(), hmeProcessNcHeaderDto.getChipCombination()));
                } else {
                    hmeProcessNcHeader.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                    updateHeaderList.add(hmeProcessNcHeader);
                    for (Map.Entry<String, List<HmeProcessNcImportDTO>> lineListEntry : lineMap.entrySet()) {
                        List<HmeProcessNcImportDTO> detailList = lineListEntry.getValue();
                        HmeProcessNcImportDTO hmeProcessNcLineDto = detailList.get(0);
                        HmeProcessNcLine hmeProcessNcLine = new HmeProcessNcLine();
                        hmeProcessNcLine.setTenantId(tenantId);
                        hmeProcessNcLine.setTagId(hmeProcessNcLineDto.getTagId());
                        hmeProcessNcLine.setTagGroupId(hmeProcessNcLineDto.getTagGroupId());
                        hmeProcessNcLine.setStandardCode(hmeProcessNcLineDto.getDataStandardCode());
                        hmeProcessNcLine.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                        List<HmeProcessNcLine> hmeProcessNcLineList = hmeProcessNcLineMapper.queryProcessNcLineList(tenantId, hmeProcessNcLine);
                        if (CollectionUtils.isEmpty(hmeProcessNcLineList)) {
                            //产品【material_code：product_code：cos_model：chip_combination】的【tag_code】数据项不存在,请检查！
                            throw new MtException("HME_PRODUCT_00002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_PRODUCT_00002", "HME", hmeProcessNcLineDto.getMaterialCode(), hmeProcessNcLineDto.getProductCode(), hmeProcessNcLineDto.getCosModel(), hmeProcessNcLineDto.getChipCombination(), hmeProcessNcLineDto.getTagCode()));
                        }
                        hmeProcessNcLine.setPriority(hmeProcessNcLineDto.getPriority());
                        hmeProcessNcLine.setLineId(hmeProcessNcLineList.get(0).getLineId());
                        updateLineList.add(hmeProcessNcLine);
                        for (HmeProcessNcImportDTO hmeProcessNcImportDTO : detailList) {
                            HmeProcessNcDetail hmeProcessNcDetail = new HmeProcessNcDetail();
                            hmeProcessNcDetail.setTenantId(tenantId);
                            hmeProcessNcDetail.setStandardCode(hmeProcessNcImportDTO.getStandardCode());
                            hmeProcessNcDetail.setLineId(hmeProcessNcLineList.get(0).getLineId());
                            List<HmeProcessNcDetail> hmeProcessNcDetailList = hmeProcessNcDetailRepository.select(hmeProcessNcDetail);
                            hmeProcessNcDetail.setMinValue(hmeProcessNcImportDTO.getMinValue());
                            hmeProcessNcDetail.setMaxValue(hmeProcessNcImportDTO.getMaxValue());
                            hmeProcessNcDetail.setNcCodeId(hmeProcessNcImportDTO.getNcCodeId());
                            hmeProcessNcDetail.setHeaderId(hmeProcessNcHeaderList.get(0).getHeaderId());
                            if (CollectionUtils.isEmpty(hmeProcessNcDetailList)) {
                                hmeProcessNcDetailRepository.insertSelective(hmeProcessNcDetail);
                            } else {
                                hmeProcessNcDetail.setDetailId(hmeProcessNcDetailList.get(0).getDetailId());
                                updateDetailList.add(hmeProcessNcDetail);
                            }
                        }
                    }
                }
            }
        }
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        if (CollectionUtils.isNotEmpty(updateHeaderList)) {
            List<List<HmeProcessNcHeader>> splitSqlList = InterfaceUtils.splitSqlList(updateHeaderList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeProcessNcHeader> domains : splitSqlList) {
                hmeProcessNcHeaderMapper.batchHeaderUpdate(tenantId, userId, domains);
            }
        }

        if (CollectionUtils.isNotEmpty(updateLineList)) {
            List<List<HmeProcessNcLine>> splitSqlList = InterfaceUtils.splitSqlList(updateLineList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeProcessNcLine> domains : splitSqlList) {
                hmeProcessNcLineMapper.batchHeaderUpdate(tenantId, userId, domains);
            }
        }

        if (CollectionUtils.isNotEmpty(updateDetailList)) {
            List<List<HmeProcessNcDetail>> splitSqlList = InterfaceUtils.splitSqlList(updateDetailList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeProcessNcDetail> domains : splitSqlList) {
                hmeProcessNcDetailMapper.batchHeaderUpdate(tenantId, userId, domains);
            }
        }
    }

    /**
     * 拼接物料 产品代码 COS型号 芯片组合 工艺编码
     *
     * @param processNcImportDTO
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/3/29 17:08
     */
    private String spliceHeaderStr(HmeProcessNcImportDTO processNcImportDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(processNcImportDTO.getMaterialId());
        sb.append(processNcImportDTO.getProductCode());
        sb.append(processNcImportDTO.getCosModel());
        sb.append(processNcImportDTO.getChipCombination());
        sb.append(processNcImportDTO.getOperationId());
        return sb.toString();
    }

    /**
     * 拼接数据项 数据收集组 编码
     *
     * @param processNcImportDTO
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/3/29 17:08
     */
    private String spliceListStr(HmeProcessNcImportDTO processNcImportDTO) {
        StringBuffer sb = new StringBuffer();
        sb.append(processNcImportDTO.getTagId());
        sb.append(processNcImportDTO.getTagGroupId());
        sb.append(processNcImportDTO.getDataStandardCode());
        return sb.toString();
    }
}
