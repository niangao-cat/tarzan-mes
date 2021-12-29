package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeTagImportVO;
import com.ruike.hme.domain.vo.HmeTagImportVO2;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendSettingsVO2;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.app.service.MtTagService;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.material.domain.entity.MtUom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 数据收集项-批量导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 9:38
 */
@ImportService(templateCode = "HME.TAG_IMPORT")
public class HmeTagImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagService mtTagService;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private LovAdapter lovAdapter;


    private static final String TABLENAME = "mt_tag_attr";

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            List<HmeTagImportVO2> tagImportVO2List = new ArrayList<>();

            //V20211129 modify by penglin.sui 将值集查询提取到循环外
            //数据收集方式处理
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.TAG_COLLECTION_METHOD", tenantId);
            //数据类型处理
            List<LovValueDTO> valTypeList = lovAdapter.queryLovValue("HME.TAG_VALUE_TYPE", tenantId);
            for (String vo : data) {
                HmeTagImportVO2 tagVo = new HmeTagImportVO2();
                HmeTagImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeTagImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                Boolean flag = false;
                for (HmeTagImportVO2 vo1 : tagImportVO2List){
                    if(StringUtils.equals(importVO.getTagCode(),vo1.getTagCode())) {
                        flag = true;
                        // 校验导入方式
                        if (!StringUtils.equals(importVO.getImportMethod(), vo1.getImportMethod())) {
                            throw new MtException("HME_EXCEL_IMPORT_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_026", "HME", importVO.getTagCode()));
                        }
                        //增加扩展属性
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        if (StringUtils.isNotBlank(importVO.getAttrName())) {
                            List<MtExtendSettingsVO2> vo2List = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLENAME, "Y");
                            for (MtExtendSettingsVO2 mtExtendSettingsVO2 : vo2List) {
                                if (StringUtils.equals(mtExtendSettingsVO2.getAttrMeaning(), importVO.getAttrName())) {
                                    mtExtendVO5.setAttrName(mtExtendSettingsVO2.getAttrName());
                                    break;
                                }
                            }
                        }
                        mtExtendVO5.setAttrValue(importVO.getAttrValue());
                        //校验唯一 编码 扩展字段
                        if(StringUtils.equals(importVO.getAttrName(),vo1.getAttrName()) && StringUtils.equals(importVO.getAttrValue(),vo1.getAttrValue())){
                            throw new MtException("HME_EXCEL_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_006", "HME"));
                        }

                        if (StringUtils.isNotBlank(mtExtendVO5.getAttrName())) {
                            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(vo1.getAttrList())) {
                                mtExtendVO5List.addAll(vo1.getAttrList());
                            }
                            mtExtendVO5List.add(mtExtendVO5);
                            vo1.setAttrList(mtExtendVO5List);
                        }
                        break;
                    }
                }

                if(flag){
                    continue;
                }

                if(importVO.getTenantId() != null){
                    tenantId = importVO.getTenantId();
                }

                String collectionMethod = "";
                for (LovValueDTO lovValueDTO : lovValueDTOS) {
                    if(StringUtils.equals(lovValueDTO.getMeaning(),importVO.getCollectionMethod())){
                        collectionMethod = lovValueDTO.getValue();
                        break;
                    }
                }
                importVO.setCollectionMethod(collectionMethod);

                String valueType = "";
                for (LovValueDTO valueDTO : valTypeList) {
                    if(StringUtils.equals(valueDTO.getMeaning(),importVO.getValueType())){
                        valueType = valueDTO.getValue();
                        break;
                    }
                }
                importVO.setValueType(valueType);

                //检验单位
                MtUom mtUom = hmeSnBindEoRepository.queryMtUomByUomCode(tenantId, importVO.getUnit());
                importVO.setUnit(mtUom != null ? mtUom.getUomId() : "");

                //处理数据
                BeanUtils.copyProperties(importVO, tagVo);
                tagVo.setEnableFlag(importVO.getEnableFlag());
                tagVo.setProcessFlag(importVO.getProcessFlag());
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                if(StringUtils.isNotBlank(importVO.getAttrName())){
                    List<MtExtendSettingsVO2> vo2List = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLENAME, "Y");
                    for (MtExtendSettingsVO2 mtExtendSettingsVO2 : vo2List) {
                        if(StringUtils.equals(mtExtendSettingsVO2.getAttrMeaning(),importVO.getAttrName())){
                            mtExtendVO5.setAttrName(mtExtendSettingsVO2.getAttrName());
                            break;
                        }
                    }
                }
                mtExtendVO5.setAttrValue(importVO.getAttrValue());
                if (StringUtils.isNotBlank(mtExtendVO5.getAttrName()) && StringUtils.isNotBlank(mtExtendVO5.getAttrValue())) {
                    List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                    mtExtendVO5List.add(mtExtendVO5);
                    tagVo.setAttrList(mtExtendVO5List);
                }
                tagImportVO2List.add(tagVo);
            }
            // 保存数据项及扩展字段
            this.saveTagAndExtend(tenantId, tagImportVO2List);
        }
        return true;
    }

    private void saveTagAndExtend(Long tenantId, List<HmeTagImportVO2> tagImportVO2List) {
        if (CollectionUtils.isNotEmpty(tagImportVO2List)) {
            for (HmeTagImportVO2 hmeTagImportVO2 : tagImportVO2List) {
                if (StringUtils.equals(hmeTagImportVO2.getImportMethod(), "INCREASE")) {
                    // 新增 若数据项已存在则报错
                    List<MtTag> tagList = mtTagRepository.select(new MtTag() {{
                        setTagCode(hmeTagImportVO2.getTagCode());
                        setTenantId(tenantId);
                    }});
                    if (CollectionUtils.isNotEmpty(tagList)) {
                        throw new MtException("HME_EXCEL_IMPORT_027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_027", "HME", hmeTagImportVO2.getTagCode()));
                    }
                    MtTagDTO mtTagDTO = new MtTagDTO();
                    BeanUtils.copyProperties(hmeTagImportVO2, mtTagDTO);
                    String tagId = mtTagService.tagSaveForUi(tenantId, mtTagDTO);
                    List<MtExtendVO5> mtExtendVO9List = CollectionUtils.isNotEmpty(hmeTagImportVO2.getAttrList()) ? hmeTagImportVO2.getAttrList() : new ArrayList<>();
                    // 增加过程数据标识
                    if (StringUtils.isNotBlank(hmeTagImportVO2.getProcessFlag())) {
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        mtExtendVO5.setAttrName("PROCESS_FLAG");
                        mtExtendVO5.setAttrValue(hmeTagImportVO2.getProcessFlag());
                        mtExtendVO9List.add(mtExtendVO5);
                    }
                    // 默认普通类型
                    if (StringUtils.isBlank(hmeTagImportVO2.getImportType())) {
                        hmeTagImportVO2.setImportType("NORMAL");
                    }
                    if (!StringUtils.equals(hmeTagImportVO2.getImportType(), "NORMAL")) {
                        // 非普通的 校验 新增数据项必须要排序码且有值
                        Optional<MtExtendVO5> orderKeyOpt = mtExtendVO9List.stream().filter(dto -> StringUtils.equals(dto.getAttrName(), "ORDER_KEY")).findFirst();
                        if (!orderKeyOpt.isPresent() || StringUtils.isBlank(orderKeyOpt.get().getAttrValue())) {
                            throw new MtException("HME_EXCEL_IMPORT_034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_034", "HME", mtTagDTO.getTagCode()));
                        }
                    }
                    //扩展表
                    if (CollectionUtils.isNotEmpty(mtExtendVO9List)) {
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, TABLENAME, tagId, "", mtExtendVO9List);
                    }
                } else if (StringUtils.equals(hmeTagImportVO2.getImportMethod(), "UPDATE")) {
                    // 更新 若数据项不存在则报错
                    MtTag mtTag = mtTagRepository.selectOne(new MtTag() {{
                        setTagCode(hmeTagImportVO2.getTagCode());
                        setTenantId(tenantId);
                    }});
                    if (mtTag == null) {
                        throw new MtException("HME_EXCEL_IMPORT_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_028", "HME", hmeTagImportVO2.getTagCode()));
                    }
                    MtTagDTO mtTagDTO = new MtTagDTO();
                    BeanUtils.copyProperties(hmeTagImportVO2, mtTagDTO);
                    mtTagDTO.setTagId(mtTag.getTagId());
                    mtTagService.tagSaveForUi(tenantId, mtTagDTO);
                    List<MtExtendVO5> mtExtendVO9List = CollectionUtils.isNotEmpty(hmeTagImportVO2.getAttrList()) ? hmeTagImportVO2.getAttrList() : new ArrayList<>();
                    // 增加过程数据标识
                    if (StringUtils.isNotBlank(hmeTagImportVO2.getProcessFlag())) {
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        mtExtendVO5.setAttrName("PROCESS_FLAG");
                        mtExtendVO5.setAttrValue(hmeTagImportVO2.getProcessFlag());
                        mtExtendVO9List.add(mtExtendVO5);
                    }
                    //扩展表
                    if (CollectionUtils.isNotEmpty(mtExtendVO9List)) {
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, TABLENAME, mtTag.getTagId(), "", mtExtendVO9List);
                    }
                }
            }
        }
    }
}
