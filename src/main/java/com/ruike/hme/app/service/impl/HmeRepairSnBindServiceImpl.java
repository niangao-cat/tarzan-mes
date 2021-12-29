package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.app.service.HmeRepairSnBindService;
import com.ruike.hme.domain.entity.HmeEoRel;
import com.ruike.hme.domain.vo.HmeEoRepairSnVO;
import com.ruike.hme.domain.vo.HmeRepairSnBindVO;
import com.ruike.hme.infra.mapper.HmeEoRelMapper;
import com.ruike.hme.infra.mapper.HmeRepairSnBindMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.infra.mapper.MtEoMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 10:36
 */
@Service
@ImportService(templateCode = "HME.REPAIR_SN_BIND")
public class HmeRepairSnBindServiceImpl implements HmeRepairSnBindService, IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtEoMapper mtEoMapper;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeRepairSnBindMapper hmeRepairSnBindMapper;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoRelMapper hmeEoRelMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            List<HmeRepairSnBindVO> hmeRepairSnBindVOList = new ArrayList<>();
            for (String vo : data) {
                HmeRepairSnBindVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeRepairSnBindVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                hmeRepairSnBindVOList.add(importVO);
            }
            this.handleData(tenantId, hmeRepairSnBindVOList);
        }
        return true;
    }

    private void handleData(Long tenantId, List<HmeRepairSnBindVO> hmeRepairSnBindVOList) {
        // 过滤掉返修SN为空的数据
        List<HmeRepairSnBindVO> filterEoList = hmeRepairSnBindVOList.stream().filter(vo -> StringUtils.isNotBlank(vo.getRepairSn())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(filterEoList)) {
            List<String> eoIdentificationList = filterEoList.stream().map(HmeRepairSnBindVO::getEoIdentification).distinct().collect(Collectors.toList());
            // 不允许对EO多次操作
            if (BigDecimal.valueOf(eoIdentificationList.size()).compareTo(BigDecimal.valueOf(filterEoList.size())) != 0) {
                Map<String, List<HmeRepairSnBindVO>> groupMap = filterEoList.stream().collect(Collectors.groupingBy(HmeRepairSnBindVO::getEoIdentification));
                Optional<Map.Entry<String, List<HmeRepairSnBindVO>>> firstOpt = groupMap.entrySet().stream().filter(vo -> vo.getValue().size() > 1).findFirst();
                throw new MtException("HME_REPAIR_SN_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPAIR_SN_IMPORT_001", "HME", firstOpt.get().getKey()));
            }
            List<String> repairSnList = filterEoList.stream().map(HmeRepairSnBindVO::getRepairSn).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(repairSnList)) {
                eoIdentificationList.addAll(repairSnList);
            }
            List<MtEo> mtEos = mtEoMapper.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_IDENTIFICATION).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andIn(MtEo.FIELD_IDENTIFICATION, eoIdentificationList)).build());
            Map<String, MtEo> mtEoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getIdentification, Function.identity()));
            List<String> eoIdList = mtEos.stream().map(MtEo::getEoId).distinct().collect(Collectors.toList());
            Map<String, String> hmeEoRepairSnVOMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(eoIdList)) {
                List<HmeEoRepairSnVO> hmeEoRepairSnVOList = mtEoMapper.eoRepairSnQuery(tenantId, eoIdList);
                hmeEoRepairSnVOMap = hmeEoRepairSnVOList.stream().collect(Collectors.toMap(HmeEoRepairSnVO::getEoId, HmeEoRepairSnVO::getRepairSn));
            }
            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
//            List<HmeEoRel> updateEoRelList = new ArrayList<>();
            for (HmeRepairSnBindVO hmeRepairSnBindVO : filterEoList) {
                MtEo mtEo = mtEoMap.get(hmeRepairSnBindVO.getEoIdentification());
                String repairSn = hmeEoRepairSnVOMap.getOrDefault(mtEo.getEoId(), "");
                // 如果返修sn相同 则不作任何处理
                if (!repairSn.equals(hmeRepairSnBindVO.getRepairSn())) {
                    MtCommonExtendVO6 commonExtendVO6 = new MtCommonExtendVO6();
                    commonExtendVO6.setKeyId(mtEo.getEoId());

                    List<MtCommonExtendVO5> attrs = new ArrayList<>();
                    MtCommonExtendVO5 extendVO5 = new MtCommonExtendVO5();
                    extendVO5.setAttrName("REWORK_MATERIAL_LOT");
                    extendVO5.setAttrValue(hmeRepairSnBindVO.getRepairSn());
                    attrs.add(extendVO5);
                    commonExtendVO6.setAttrs(attrs);
                    attrPropertyList.add(commonExtendVO6);

                    // 若原eo 绑定过eo关联关系则报错
                    List<HmeEoRel> hmeEoRels = hmeEoRelMapper.selectByCondition(Condition.builder(HmeEoRel.class).select(HmeEoRel.FIELD_EO_REL_ID).andWhere(Sqls.custom()
                            .andEqualTo(HmeEoRel.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoRel.FIELD_EO_ID, mtEo.getEoId())).build());
                    if (CollectionUtils.isNotEmpty(hmeEoRels)) {
                        throw new MtException("HME_REPAIR_SN_IMPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_REPAIR_SN_IMPORT_002", "HME", mtEo.getIdentification()));
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(attrPropertyList)) {
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("HME_REPAIR_SN_IMPORT");
                }});
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_eo_attr", eventId,  attrPropertyList);
            }
        }
    }
}