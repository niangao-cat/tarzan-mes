package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeTagGroupAssignImportVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.api.dto.MtTagGroupAssignDTO;
import tarzan.general.app.service.MtTagGroupAssignHisService;
import tarzan.general.app.service.MtTagGroupAssignService;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.mapper.MtTagGroupAssignMapper;
import tarzan.material.domain.entity.MtUom;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据收集组收集项-批量导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 9:46
 */
@ImportService(templateCode = "HME.TAG_GROUP_DATA")
public class HmeTagGroupAssignImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;


    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtTagGroupAssignHisService mtTagGroupAssignHisService;

    @Autowired
    private MtTagGroupAssignMapper mtTagGroupAssignMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            List<HmeTagGroupAssignImportVO> assignImportVOList = new ArrayList<>();
            for (String vo : data) {
                HmeTagGroupAssignImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeTagGroupAssignImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                if (importVO.getTenantId() != null) {
                    tenantId = importVO.getTenantId();
                }

                //校验数据
                if (StringUtils.isBlank(importVO.getTagGroupId())) {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME", String.valueOf(index + 1), "数据收集组"));
                }

                Condition condition = new Condition(MtTagGroup.class);
                condition.and().andEqualTo("tagGroupCode",importVO.getTagGroupId());
                List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectByCondition(condition);
                if(CollectionUtils.isEmpty(mtTagGroups)){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME", String.valueOf(index + 1), importVO.getTagGroupId()));
                }
                importVO.setTagGroupCode(importVO.getTagGroupId());
                importVO.setTagGroupId(mtTagGroups.get(0).getTagGroupId());


                //序号
                if (importVO.getSerialNumber() == null) {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME", String.valueOf(index + 1), "序号"));
                }
                // 导入类型
                if (StringUtils.isBlank(importVO.getImportType())) {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME", String.valueOf(index + 1), "导入类型"));
                }

                //数据收集方式处理
                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.TAG_COLLECTION_METHOD", tenantId);
                String collectionMethod = "";
                for (LovValueDTO lovValueDTO : lovValueDTOS) {
                    if (StringUtils.equals(lovValueDTO.getMeaning(), importVO.getCollectionMethod())) {
                        collectionMethod = lovValueDTO.getValue();
                        break;
                    }
                }
                importVO.setCollectionMethod(collectionMethod);

                //收集项编码
                importVO.setTagCode(importVO.getTagId());
                if(StringUtils.isNotBlank(importVO.getTagId())) {
                    Condition tagCondition = new Condition(MtTag.class);
                    tagCondition.and().andEqualTo("tagCode", importVO.getTagId());
                    List<MtTag> mtTagList = mtTagRepository.selectByCondition(tagCondition);
                    if (CollectionUtils.isEmpty(mtTagList)) {
                        throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_001", "HME", String.valueOf(index + 1), importVO.getTagId()));
                    }
                    importVO.setTagId(mtTagList.get(0).getTagId());
                }

                //检验单位
                MtUom mtUom = hmeSnBindEoRepository.queryMtUomByUomCode(tenantId, importVO.getUnit());
                importVO.setUnit(mtUom != null ? mtUom.getUomId() : "");
                assignImportVOList.add(importVO);
            }
            Map<String,List<HmeTagGroupAssignImportVO>> assignMap = new HashMap<>();
            assignMap = assignImportVOList.stream().collect(Collectors.groupingBy(item -> item.getTagGroupId()));

            for (Map.Entry<String,List<HmeTagGroupAssignImportVO>> entry : assignMap.entrySet()) {
                // 查询数据组下数据项
                MtTagGroupAssign queryTagGroupAssign = new MtTagGroupAssign();
                queryTagGroupAssign.setTenantId(tenantId);
                queryTagGroupAssign.setTagGroupId(entry.getKey());
                List<MtTagGroupAssign> originTagGroupAssignList = mtTagGroupAssignRepository.select(queryTagGroupAssign);

                List<HmeTagGroupAssignImportVO> importVOList = entry.getValue();
                // 保存及更新的集合
                List<MtTagGroupAssignDTO> assignDTOList = new ArrayList<>();
                // 解绑的集合
                List<MtTagGroupAssign> untieAssignList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(importVOList)) {
                    for (HmeTagGroupAssignImportVO hmeTagGroupAssignImportVO : importVOList) {
                        MtTagGroupAssignDTO assignDTO = new MtTagGroupAssignDTO();
                        BeanUtils.copyProperties(hmeTagGroupAssignImportVO, assignDTO);
                        // 数据项不能重复
                        List<MtTagGroupAssignDTO> collect = assignDTOList.stream().filter(f -> StringUtils.equals(f.getTagId(), assignDTO.getTagId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(collect)) {
                            throw new MtException("HME_EXCEL_IMPORT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_008", "HME"));
                        }
                        // 序号不能重复
                        List<MtTagGroupAssignDTO> serialNumberList = assignDTOList.stream().filter(f -> f.getSerialNumber().compareTo(assignDTO.getSerialNumber()) == 0).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(serialNumberList)) {
                            throw new MtException("MT_GENERAL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0012", "GENERAL", "数据收集组, 序号"));
                        }
                        // 查询数据项重复
                        Optional<MtTagGroupAssign> tagGroupAssignFirst = originTagGroupAssignList.stream().filter(tg -> StringUtils.equals(tg.getTagId(), hmeTagGroupAssignImportVO.getTagId())).findFirst();
                        // 查询序号重复的
                        Optional<MtTagGroupAssign> serialNumberOpt = originTagGroupAssignList.stream().filter(tg -> tg.getSerialNumber().compareTo(hmeTagGroupAssignImportVO.getSerialNumber()) == 0).findFirst();

                        if (StringUtils.equals("NEW", hmeTagGroupAssignImportVO.getImportType())) {
                            // 新增 关联存在就报错
                            if (tagGroupAssignFirst.isPresent()) {
                                throw new MtException("HME_EXCEL_IMPORT_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_032", "HME", hmeTagGroupAssignImportVO.getTagGroupCode(), hmeTagGroupAssignImportVO.getTagCode()));
                            }
                            // 新增 序号重复也报错
                            if (serialNumberOpt.isPresent()) {
                                throw new MtException("HME_EXCEL_IMPORT_035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_035", "HME", hmeTagGroupAssignImportVO.getTagGroupCode(), hmeTagGroupAssignImportVO.getSerialNumber().toString()));
                            }
                            assignDTOList.add(assignDTO);
                        } else if (StringUtils.equals("UPDATE", hmeTagGroupAssignImportVO.getImportType())) {
                            // 更新 关联不存在就报错
                            if (!tagGroupAssignFirst.isPresent()) {
                                throw new MtException("HME_EXCEL_IMPORT_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_033", "HME", hmeTagGroupAssignImportVO.getTagGroupCode(), hmeTagGroupAssignImportVO.getTagCode()));
                            }
                            assignDTO.setTagGroupAssignId(tagGroupAssignFirst.get().getTagGroupAssignId());
                            if (serialNumberOpt.isPresent() && !StringUtils.equals(serialNumberOpt.get().getTagId(), hmeTagGroupAssignImportVO.getTagId())) {
                                throw new MtException("HME_EXCEL_IMPORT_035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_035", "HME", hmeTagGroupAssignImportVO.getTagGroupCode(), hmeTagGroupAssignImportVO.getSerialNumber().toString()));
                            }
                            assignDTOList.add(assignDTO);
                        } else if (StringUtils.equals("UNTIE", hmeTagGroupAssignImportVO.getImportType())) {
                            // 解绑 关联不存在则报错
                            if (!tagGroupAssignFirst.isPresent()) {
                                throw new MtException("HME_EXCEL_IMPORT_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_033", "HME", hmeTagGroupAssignImportVO.getTagGroupCode(), hmeTagGroupAssignImportVO.getTagCode()));
                            }
                            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
                            mtTagGroupAssign.setTagGroupAssignId(tagGroupAssignFirst.get().getTagGroupAssignId());
                            untieAssignList.add(mtTagGroupAssign);
                        }
                    }
                }
                // 保存及更新数据
                this.saveTagGroupAssign(tenantId, entry.getKey(),
                        assignDTOList);
                // 解绑数据
                this.untieTagGroupAssign(tenantId, untieAssignList);
            }
        }
        return true;
    }

    private void saveTagGroupAssign(Long tenantId, String tagGroupId, List<MtTagGroupAssignDTO> assignDTOList) {
        if (CollectionUtils.isNotEmpty(assignDTOList)) {
            // record history
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("TAG_GROUP_ASSIGN");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            for (MtTagGroupAssignDTO dto : assignDTOList) {
                MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
                BeanUtils.copyProperties(dto, mtTagGroupAssign);
                mtTagGroupAssign.setTenantId(tenantId);
                mtTagGroupAssign.setTagGroupId(tagGroupId);
                if (StringUtils.isNotBlank(mtTagGroupAssign.getTagGroupAssignId())) {
                    // 更新数据
                    // modify by sanfeng.zhang for peng.zhao 非必输数据 为空也更新
                    mtTagGroupAssignMapper.myUpdateByPrimaryKey(tenantId, mtTagGroupAssign, userId);
                } else {
                    // 插入数据
                    mtTagGroupAssignRepository.insertSelective(mtTagGroupAssign);
                }
                // 记录历史
                mtTagGroupAssignHisService.saveTagGroupAssignHis(tenantId, eventId, mtTagGroupAssign);
            }
        }
    }

    private void untieTagGroupAssign(Long tenantId, List<MtTagGroupAssign> untieAssignList) {
        if (CollectionUtils.isNotEmpty(untieAssignList)) {
            mtTagGroupAssignRepository.batchDeleteByPrimaryKey(untieAssignList);
        }
    }
}
