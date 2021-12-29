package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.entity.HmeFacYkHis;
import com.ruike.hme.domain.repository.HmeFacYkHisRepository;
import com.ruike.hme.domain.repository.HmeFacYkRepository;
import com.ruike.hme.domain.vo.HmeFacYkImportVO;
import com.ruike.hme.infra.mapper.HmeFacYkMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 11:23
 */
@ImportService(templateCode = "HME.FAC_YK")
public class HmeFacYkImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeFacYkRepository hmeFacYkRepository;
    @Autowired
    private HmeFacYkMapper hmeFacYkMapper;
    @Autowired
    private HmeFacYkHisRepository hmeFacYkHisRepository;
    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeFacYkImportVO> hmeFacYkImportVOList = new ArrayList<>();
            for (String vo : data) {
                HmeFacYkImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeFacYkImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 物料编码
                if (StringUtils.isBlank(importVO.getMaterialCode())) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "物料编码"));
                }
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
                if (CollectionUtils.isEmpty(mtMaterialList)) {
                    throw new MtException("HME_FACYK_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_002", "HME", importVO.getMaterialCode()));
                }
                importVO.setMaterialId(mtMaterialList.get(0).getMaterialId());
                // 芯片类型
                if (StringUtils.isBlank(importVO.getCosType())) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "芯片类型"));
                }
                // FAC物料编码
                if (StringUtils.isBlank(importVO.getFacMaterialCode())) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "FAC物料编码"));
                }
                List<MtMaterial> facMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getFacMaterialCode()));
                if (CollectionUtils.isEmpty(facMaterialList)) {
                    throw new MtException("HME_FACYK_IMPORT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_003", "HME", importVO.getFacMaterialCode()));
                }
                importVO.setFacMaterialId(facMaterialList.get(0).getMaterialId());
                // 工位编码
                if (StringUtils.isBlank(importVO.getWorkcellCode())) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "工位编码"));
                }
                List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importVO.getWorkcellCode()));
                if (CollectionUtils.isEmpty(workcellList)) {
                    throw new MtException("HME_FACYK_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_004", "HME", importVO.getWorkcellCode()));
                }
                importVO.setWorkcellId(workcellList.get(0).getWorkcellId());
                // 标准值
                if (importVO.getStandardValue() == null) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "标准值"));
                }
                // 允差
                if (importVO.getAllowDiffer() == null) {
                    throw new MtException("HME_FACYK_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_001", "HME", "允差"));
                }
                // 导入数据
                hmeFacYkImportVOList.add(importVO);
            }
            this.batchImportFacYk(tenantId, hmeFacYkImportVOList);
        }
        return true;
    }

    /**
     * 批量导入数据
     *
     * @param tenantId
     * @param hmeFacYkImportVOList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/7 14:18
     */
    private void batchImportFacYk(Long tenantId, List<HmeFacYkImportVO> hmeFacYkImportVOList) {
        if (CollectionUtils.isNotEmpty(hmeFacYkImportVOList)) {
            // 物料编码+芯片类型+FAC物料编码+工位编码分组
            Map<String, List<HmeFacYkImportVO>> hmeFacYkImportMap = hmeFacYkImportVOList.stream().collect(Collectors.groupingBy(fy -> spliceStr(fy)));
            List<HmeFacYk> insertList = new ArrayList<>();
            List<HmeFacYk> updateList = new ArrayList<>();
            List<HmeFacYkHis> updateHisList = new ArrayList<>();
            for (Map.Entry<String, List<HmeFacYkImportVO>> facYkEntry : hmeFacYkImportMap.entrySet()) {
                List<HmeFacYkImportVO> value = facYkEntry.getValue();
                if (value.size() > 1) {
                    throw new MtException("HME_FACYK_IMPORT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_FACYK_IMPORT_005", "HME", value.get(0).getMaterialCode(), value.get(0).getCosType(), value.get(0).getFacMaterialCode(), value.get(0).getWorkcellCode()));
                }
                HmeFacYkImportVO hmeFacYkImportVO = value.get(0);
                HmeFacYk hmeFacYk = new HmeFacYk();
                BeanUtils.copyProperties(hmeFacYkImportVO, hmeFacYk);

                List<HmeFacYk> hmeFacYkList = hmeFacYkRepository.select(new HmeFacYk() {{
                    setTenantId(tenantId);
                    setMaterialId(hmeFacYk.getMaterialId());
                    setCosType(hmeFacYk.getCosType());
                    setFacMaterialId(hmeFacYk.getFacMaterialId());
                    setWorkcellId(hmeFacYk.getWorkcellId());
                }});
                if (CollectionUtils.isNotEmpty(hmeFacYkList)) {
                    hmeFacYk.setFacYkId(hmeFacYkList.get(0).getFacYkId());
                    updateList.add(hmeFacYk);
                    // 记录更新历史
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventTypeCode("HME_FAC-Y_MODIFIED");
                    }});
                    HmeFacYkHis hmeFacYkHis = new HmeFacYkHis();
                    BeanUtils.copyProperties(hmeFacYk, hmeFacYkHis);
                    hmeFacYkHis.setEventId(eventId);
                    hmeFacYkHis.setTenantId(tenantId);
                    updateHisList.add(hmeFacYkHis);
                } else {
                    hmeFacYk.setTenantId(tenantId);
                    insertList.add(hmeFacYk);
                }
            }
            if (CollectionUtils.isNotEmpty(updateList)) {
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                List<List<HmeFacYk>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeFacYk> domains : splitSqlList) {
                    hmeFacYkMapper.myBatchUpdate(tenantId, userId, domains);
                }
                // 记录历史
                List<List<HmeFacYkHis>> splitHisSqlList = InterfaceUtils.splitSqlList(updateHisList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeFacYkHis> domains : splitHisSqlList) {
                    hmeFacYkHisRepository.batchInsert(domains);
                }
            }
            if (CollectionUtils.isNotEmpty(insertList)) {
                List<List<HmeFacYk>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeFacYk> domains : splitSqlList) {
                    hmeFacYkRepository.batchInsert(domains);
                }
            }
        }
    }

    private String spliceStr(HmeFacYkImportVO importVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(importVO.getMaterialCode());
        sb.append(importVO.getCosType());
        sb.append(importVO.getFacMaterialCode());
        sb.append(importVO.getWorkcellCode());
        return sb.toString();
    }
}
