package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.HmeSsnInspectDetailRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectHeaderHisRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectLineHisRepository;
import com.ruike.hme.domain.repository.HmeSsnInspectLineRepository;
import com.ruike.hme.domain.vo.HmeSsnInspectImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeSsnInspectHeaderMapper;
import com.ruike.hme.infra.mapper.HmeSsnInspectLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/8 11:26
 */
@ImportService(templateCode = "HME.SSN_INSPECT")
public class HmeSsnInspectImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;
    @Autowired
    private HmeSsnInspectHeaderMapper hmeSsnInspectHeaderMapper;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeSsnInspectLineRepository hmeSsnInspectLineRepository;
    @Autowired
    private HmeSsnInspectLineMapper hmeSsnInspectLineMapper;
    @Autowired
    private HmeSsnInspectDetailRepository hmeSsnInspectDetailRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private HmeSsnInspectHeaderHisRepository hmeSsnInspectHeaderHisRepository;
    @Autowired
    private HmeSsnInspectLineHisRepository hmeSsnInspectLineHisRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeSsnInspectImportVO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeSsnInspectImportVO importDTO = null;
                try {
                    importDTO = objectMapper.readValue(vo, HmeSsnInspectImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 标准件编码
                if (StringUtils.isBlank(importDTO.getStandardSnCode())) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_001", "HME", "标准件编码"));
                }
                // 物料编码
                if (StringUtils.isBlank(importDTO.getMaterialCode())) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_001", "HME", "物料编码"));
                }
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importDTO.getMaterialCode()));
                if (CollectionUtils.isEmpty(mtMaterialList)) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_002", "HME", importDTO.getMaterialCode()));
                }
                importDTO.setMaterialId(mtMaterialList.get(0).getMaterialId());
                // 序号
                if (importDTO.getSequence() == null) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_001", "HME", "序号"));
                }
                // 检验项
                if (StringUtils.isBlank(importDTO.getTagCode())) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_001", "HME", "检验项编码"));
                }
                List<MtTag> tagList = mtTagRepository.selectByCodeList(tenantId, Collections.singletonList(importDTO.getTagCode()));
                if (CollectionUtils.isEmpty(tagList)) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_003", "HME", importDTO.getTagCode()));
                }
                importDTO.setTagId(tagList.get(0).getTagId());
                // 耦合项目组编码
                if (StringUtils.isNotBlank(importDTO.getTagGroupCode())) {
                    List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectTagGroupByTagGroupCodes(tenantId, Collections.singletonList(importDTO.getTagGroupCode()));
                    if (CollectionUtils.isEmpty(mtTagGroups)) {
                        throw new MtException("HME_SSN_INSPECT_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_IMPORT_004", "HME", importDTO.getTagGroupCode()));
                    }
                    importDTO.setTagGroupId(mtTagGroups.get(0).getTagGroupId());
                }
                // 影响耦合标识为Y时耦合允差不能为空
                if (HmeConstants.ConstantValue.YES.equals(importDTO.getCoupleFlag())) {
                    if (importDTO.getAllowDiffer() == null) {
                        throw new MtException("HME_SSN_INSPECT_IMPORT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_IMPORT_005", "HME", importDTO.getStandardSnCode()));
                    }
                }
                // COS耦合标志为Y时耦合cos位置和耦合允差不能为空
                if (HmeConstants.ConstantValue.YES.equals(importDTO.getCosCoupleFlag())) {
                    if (importDTO.getCosPos() == null || importDTO.getAllowDiffer() == null) {
                        throw new MtException("HME_SSN_INSPECT_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_IMPORT_006", "HME", importDTO.getStandardSnCode()));
                    }
                }
                if (StringUtils.isNotBlank(importDTO.getWorkcellCode())) {
                    List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importDTO.getWorkcellCode()));
                    if (CollectionUtils.isEmpty(workcellList)) {
                        throw new MtException("HME_SSN_INSPECT_IMPORT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_IMPORT_007", "HME", importDTO.getWorkcellCode()));
                    }
                    importDTO.setWorkcellId(workcellList.get(0).getWorkcellId());
                }
                importVOList.add(importDTO);
            }
            this.handleSsnInspect(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 保存或更新标准件检验
     *
     * @param tenantId
     * @param importVOList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/8 17:17
     */
    private void handleSsnInspect(Long tenantId, List<HmeSsnInspectImportVO> importVOList) {
        // 标准件编码+物料编码+芯片类型+工作方式+工位编码 分组
        Map<String, List<HmeSsnInspectImportVO>> headerMap = importVOList.stream().collect(Collectors.groupingBy(vo -> spliceStr(vo)));
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        Date nowDate = CommonUtils.currentTimeGet();

        List<HmeSsnInspectHeader> insertHeaderList = new ArrayList<>();
        List<HmeSsnInspectLine> insertLineList = new ArrayList<>();
        List<HmeSsnInspectDetail> insertDetailList = new ArrayList<>();
        List<HmeSsnInspectHeaderHis> insertHeaderHisList = new ArrayList<>();
        List<HmeSsnInspectLineHis> insertLineHisList = new ArrayList<>();

        List<HmeSsnInspectHeader> updateHeaderList = new ArrayList<>();
        List<HmeSsnInspectLine> updateLineList = new ArrayList<>();
        for (Map.Entry<String, List<HmeSsnInspectImportVO>> headerEntry : headerMap.entrySet()) {
            List<HmeSsnInspectImportVO> value = headerEntry.getValue();
            HmeSsnInspectImportVO importHeaderDTO = value.get(0);
            List<String> importTypeList = value.stream().map(HmeSsnInspectImportVO::getImportType).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (importTypeList.size() > 1) {
                throw new MtException("HME_SSN_INSPECT_IMPORT_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SSN_INSPECT_IMPORT_012", "HME", importHeaderDTO.getStandardSnCode(), importHeaderDTO.getMaterialCode(), importHeaderDTO.getCosType(), importHeaderDTO.getWorkWay(), importHeaderDTO.getWorkcellCode()));
            }
            // 标准件编码+物料编码+芯片类型+工作方式+工位编码 查找头
            List<HmeSsnInspectHeader> inspectHeaderList = hmeSsnInspectHeaderMapper.querySsnInspectHeader(tenantId, importHeaderDTO);
            if ("INCREASE".equals(importHeaderDTO.getImportType())) {
                if (CollectionUtils.isNotEmpty(inspectHeaderList)) {
                    List<HmeSsnInspectLine> ssnInspectLineList = hmeSsnInspectLineRepository.select(new HmeSsnInspectLine() {{
                        setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                    }});
                    Map<String, List<HmeSsnInspectImportVO>> lineListMap = value.stream().collect(Collectors.groupingBy(HmeSsnInspectImportVO::getTagId));
                    for (Map.Entry<String, List<HmeSsnInspectImportVO>> lineListEntry : lineListMap.entrySet()) {
                        // 头存在 行重复则报错
                        List<HmeSsnInspectLine> hmeSsnInspectLineList = ssnInspectLineList.stream().filter(line -> StringUtils.equals(line.getTagId(), lineListEntry.getKey())).collect(Collectors.toList());
                        List<HmeSsnInspectImportVO> detailsList = lineListEntry.getValue();
                        if (CollectionUtils.isNotEmpty(hmeSsnInspectLineList)) {
                            // 行存在  明细为空时 报行重复  不为空  则校验 明细是否重复
                            for (HmeSsnInspectImportVO hmeSsnInspectDetails : detailsList) {
                                if (StringUtils.isBlank(hmeSsnInspectDetails.getTagGroupCode())) {
                                    throw new MtException("HME_SSN_INSPECT_IMPORT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_SSN_INSPECT_IMPORT_010", "HME", hmeSsnInspectDetails.getStandardSnCode(), hmeSsnInspectDetails.getMaterialCode(), hmeSsnInspectDetails.getCosType(), hmeSsnInspectDetails.getWorkWay(), hmeSsnInspectDetails.getWorkcellCode(), hmeSsnInspectDetails.getTagCode()));
                                } else {
                                    List<HmeSsnInspectDetail> inspectDetailList = hmeSsnInspectDetailRepository.select(new HmeSsnInspectDetail() {{
                                        setSsnInspectLineId(hmeSsnInspectLineList.get(0).getSsnInspectLineId());
                                        setTagGroupId(hmeSsnInspectDetails.getTagGroupId());
                                    }});
                                    if (CollectionUtils.isNotEmpty(inspectDetailList)) {
                                        throw new MtException("HME_SSN_INSPECT_IMPORT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_SSN_INSPECT_IMPORT_011", "HME", hmeSsnInspectDetails.getStandardSnCode(), hmeSsnInspectDetails.getMaterialCode(), hmeSsnInspectDetails.getCosType(), hmeSsnInspectDetails.getWorkWay(), hmeSsnInspectDetails.getWorkcellCode(), hmeSsnInspectDetails.getTagCode(), hmeSsnInspectDetails.getTagGroupCode()));
                                    } else {
                                        // 新增明细
                                        HmeSsnInspectDetail hmeSsnInspectDetail = new HmeSsnInspectDetail();
                                        hmeSsnInspectDetail.setTagGroupId(hmeSsnInspectDetails.getTagGroupId());
                                        hmeSsnInspectDetail.setSsnInspectLineId(hmeSsnInspectLineList.get(0).getSsnInspectLineId());
                                        hmeSsnInspectDetail.setTenantId(tenantId);
                                        insertDetailList.add(hmeSsnInspectDetail);
                                    }
                                }
                            }
                        } else {
                            // 新增行、明细
                            String lineId = customSequence.getNextKey("hme_ssn_inspect_line_s");
                            String lineCid = customSequence.getNextKey("hme_ssn_inspect_line_cid_s");
                            HmeSsnInspectLine hmeSsnInspectLine = new HmeSsnInspectLine();
                            BeanUtils.copyProperties(detailsList.get(0), hmeSsnInspectLine);
                            hmeSsnInspectLine.setSsnInspectLineId(lineId);
                            hmeSsnInspectLine.setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                            hmeSsnInspectLine.setTenantId(tenantId);
                            hmeSsnInspectLine.setCid(Long.valueOf(lineCid));
                            hmeSsnInspectLine.setLastUpdatedBy(userId);
                            hmeSsnInspectLine.setLastUpdateDate(nowDate);
                            hmeSsnInspectLine.setCreationDate(nowDate);
                            hmeSsnInspectLine.setCreatedBy(userId);
                            hmeSsnInspectLine.setObjectVersionNumber(1L);
                            insertLineList.add(hmeSsnInspectLine);
                            // 明细 有值则新增
                            for (HmeSsnInspectImportVO hmeSsnInspectImportVO : detailsList) {
                                if (StringUtils.isNotBlank(hmeSsnInspectImportVO.getTagGroupId())) {
                                    HmeSsnInspectDetail hmeSsnInspectDetail = new HmeSsnInspectDetail();
                                    hmeSsnInspectDetail.setTagGroupId(hmeSsnInspectImportVO.getTagGroupId());
                                    hmeSsnInspectDetail.setSsnInspectLineId(lineId);
                                    hmeSsnInspectDetail.setTenantId(tenantId);
                                    insertDetailList.add(hmeSsnInspectDetail);
                                }
                            }
                        }
                    }
                } else {
                    String headerId = customSequence.getNextKey("hme_ssn_inspect_header_s");
                    String headerCid = customSequence.getNextKey("hme_ssn_inspect_header_cid_s");
                    HmeSsnInspectHeader hmeSsnInspectHeader = new HmeSsnInspectHeader();
                    hmeSsnInspectHeader.setSsnInspectHeaderId(headerId);
                    hmeSsnInspectHeader.setWorkcellId(importHeaderDTO.getWorkcellId());
                    hmeSsnInspectHeader.setEnableFlag(importHeaderDTO.getEnableFlag());
                    hmeSsnInspectHeader.setCosType(importHeaderDTO.getCosType());
                    hmeSsnInspectHeader.setMaterialId(importHeaderDTO.getMaterialId());
                    hmeSsnInspectHeader.setStandardSnCode(importHeaderDTO.getStandardSnCode());
                    hmeSsnInspectHeader.setWorkWay(importHeaderDTO.getWorkWay());
                    hmeSsnInspectHeader.setTenantId(tenantId);
                    hmeSsnInspectHeader.setCid(Long.valueOf(headerCid));
                    hmeSsnInspectHeader.setLastUpdatedBy(userId);
                    hmeSsnInspectHeader.setLastUpdateDate(nowDate);
                    hmeSsnInspectHeader.setCreationDate(nowDate);
                    hmeSsnInspectHeader.setCreatedBy(userId);
                    hmeSsnInspectHeader.setObjectVersionNumber(1L);
                    insertHeaderList.add(hmeSsnInspectHeader);
                    Map<String, List<HmeSsnInspectImportVO>> lineListMap = value.stream().collect(Collectors.groupingBy(HmeSsnInspectImportVO::getTagId));
                    for (Map.Entry<String, List<HmeSsnInspectImportVO>> lineListEntry : lineListMap.entrySet()) {
                        List<HmeSsnInspectImportVO> detailsList = lineListEntry.getValue();
                        // 行
                        String lineId = customSequence.getNextKey("hme_ssn_inspect_line_s");
                        String lineCid = customSequence.getNextKey("hme_ssn_inspect_line_cid_s");
                        HmeSsnInspectLine hmeSsnInspectLine = new HmeSsnInspectLine();
                        BeanUtils.copyProperties(detailsList.get(0), hmeSsnInspectLine);
                        hmeSsnInspectLine.setSsnInspectLineId(lineId);
                        hmeSsnInspectLine.setSsnInspectHeaderId(headerId);
                        hmeSsnInspectLine.setTenantId(tenantId);
                        hmeSsnInspectLine.setCid(Long.valueOf(lineCid));
                        hmeSsnInspectLine.setLastUpdatedBy(userId);
                        hmeSsnInspectLine.setLastUpdateDate(nowDate);
                        hmeSsnInspectLine.setCreationDate(nowDate);
                        hmeSsnInspectLine.setCreatedBy(userId);
                        hmeSsnInspectLine.setObjectVersionNumber(1L);
                        insertLineList.add(hmeSsnInspectLine);
                        for (HmeSsnInspectImportVO details : detailsList) {
                            // 明细
                            if (StringUtils.isNotBlank(details.getTagGroupId())) {
                                HmeSsnInspectDetail hmeSsnInspectDetail = new HmeSsnInspectDetail();
                                hmeSsnInspectDetail.setTagGroupId(details.getTagGroupId());
                                hmeSsnInspectDetail.setSsnInspectLineId(lineId);
                                hmeSsnInspectDetail.setTenantId(tenantId);
                                insertDetailList.add(hmeSsnInspectDetail);
                            }
                        }
                    }
                }
            } else if ("UPDATE".equals(importHeaderDTO.getImportType())) {
                // 头不存在 则报错
                if (CollectionUtils.isEmpty(inspectHeaderList)) {
                    throw new MtException("HME_SSN_INSPECT_IMPORT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SSN_INSPECT_IMPORT_008", "HME", importHeaderDTO.getStandardSnCode(), importHeaderDTO.getMaterialCode(), importHeaderDTO.getCosType(), importHeaderDTO.getWorkWay(), importHeaderDTO.getWorkcellCode()));
                }
                List<HmeSsnInspectLine> ssnInspectLineList = hmeSsnInspectLineRepository.select(new HmeSsnInspectLine() {{
                    setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                }});
                // 更新头 行 明细 头及行更新记录历史
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("HME_SSN_INSPEC_HEAD_MODIFIED");
                }});
                String lineEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("HME_SSN_INSPEC_LINE_MODIFIED");
                }});

                // 头
                HmeSsnInspectHeader hmeSsnInspectHeader = new HmeSsnInspectHeader();
                BeanUtils.copyProperties(importHeaderDTO, hmeSsnInspectHeader);
                hmeSsnInspectHeader.setSsnInspectHeaderId(inspectHeaderList.get(0).getSsnInspectHeaderId());
                updateHeaderList.add(hmeSsnInspectHeader);

                // 头历史
                HmeSsnInspectHeaderHis headerHis = new HmeSsnInspectHeaderHis();
                BeanUtils.copyProperties(hmeSsnInspectHeader, headerHis);
                headerHis.setEventId(eventId);
                insertHeaderHisList.add(headerHis);

                Map<String, List<HmeSsnInspectImportVO>> lineListMap = value.stream().collect(Collectors.groupingBy(HmeSsnInspectImportVO::getTagId));
                for (Map.Entry<String, List<HmeSsnInspectImportVO>> lineListEntry : lineListMap.entrySet()) {
                    List<HmeSsnInspectImportVO> detailsList = lineListEntry.getValue();
                    // 行不存在也报错
                    List<HmeSsnInspectLine> hmeSsnInspectLineList = ssnInspectLineList.stream().filter(line -> StringUtils.equals(line.getTagId(), lineListEntry.getKey())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(hmeSsnInspectLineList)) {
                        throw new MtException("HME_SSN_INSPECT_IMPORT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SSN_INSPECT_IMPORT_009", "HME", detailsList.get(0).getStandardSnCode(), detailsList.get(0).getTagCode()));
                    }
                    // 行
                    HmeSsnInspectLine hmeSsnInspectLine = new HmeSsnInspectLine();
                    BeanUtils.copyProperties(detailsList.get(0), hmeSsnInspectLine);
                    hmeSsnInspectLine.setSsnInspectLineId(hmeSsnInspectLineList.get(0).getSsnInspectLineId());
                    hmeSsnInspectLine.setSsnInspectHeaderId(hmeSsnInspectLineList.get(0).getSsnInspectHeaderId());
                    updateLineList.add(hmeSsnInspectLine);

                    // 行记录
                    HmeSsnInspectLineHis hmeSsnInspectLineHis = new HmeSsnInspectLineHis();
                    BeanUtils.copyProperties(hmeSsnInspectLine, hmeSsnInspectLineHis);
                    hmeSsnInspectLineHis.setEventId(lineEventId);
                    insertLineHisList.add(hmeSsnInspectLineHis);
                }
            }
        }
        // 新增头
        if (CollectionUtils.isNotEmpty(insertHeaderList)) {
            List<List<HmeSsnInspectHeader>> splitSqlList = InterfaceUtils.splitSqlList(insertHeaderList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectHeader> domains : splitSqlList) {
                hmeSsnInspectHeaderMapper.mtBatchInsert(domains);
            }
        }
        // 更新头 和新增历史
        if (CollectionUtils.isNotEmpty(updateHeaderList)) {
            List<List<HmeSsnInspectHeader>> splitSqlList = InterfaceUtils.splitSqlList(updateHeaderList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectHeader> domains : splitSqlList) {
                hmeSsnInspectHeaderMapper.mtBatchUpdate(tenantId, userId, domains);
            }
        }
        if (CollectionUtils.isNotEmpty(insertHeaderHisList)) {
            List<List<HmeSsnInspectHeaderHis>> splitSqlList = InterfaceUtils.splitSqlList(insertHeaderHisList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectHeaderHis> domains : splitSqlList) {
                hmeSsnInspectHeaderHisRepository.batchInsertSelective(domains);
            }
        }
        // 新增头
        if (CollectionUtils.isNotEmpty(insertLineList)) {
            List<List<HmeSsnInspectLine>> splitSqlList = InterfaceUtils.splitSqlList(insertLineList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectLine> domains : splitSqlList) {
                hmeSsnInspectLineMapper.mtBatchInsert(domains);
            }
        }
        // 更新行 及新增历史
        if (CollectionUtils.isNotEmpty(updateLineList)) {
            List<List<HmeSsnInspectLine>> splitSqlList = InterfaceUtils.splitSqlList(updateLineList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectLine> domains : splitSqlList) {
                hmeSsnInspectLineMapper.mtBatchUpdate(tenantId, userId, domains);
            }
        }
        if (CollectionUtils.isNotEmpty(insertLineHisList)) {
            List<List<HmeSsnInspectLineHis>> splitSqlList = InterfaceUtils.splitSqlList(insertLineHisList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectLineHis> domains : splitSqlList) {
                hmeSsnInspectLineHisRepository.batchInsertSelective(domains);
            }
        }
        // 新增行明细
        if (CollectionUtils.isNotEmpty(insertDetailList)) {
            List<List<HmeSsnInspectDetail>> splitSqlList = InterfaceUtils.splitSqlList(insertDetailList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeSsnInspectDetail> domains : splitSqlList) {
                hmeSsnInspectDetailRepository.batchInsertSelective(domains);
            }
        }
    }

    /**
     * 拼接标准件编码+物料编码+芯片类型+工作方式+工位编码
     *
     * @param vo
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/4/8 17:28
     */
    private String spliceStr(HmeSsnInspectImportVO vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getStandardSnCode());
        sb.append(vo.getMaterialCode());
        sb.append(vo.getCosType());
        sb.append(vo.getWorkWay());
        sb.append(vo.getWorkcellCode());
        return sb.toString();
    }
}
