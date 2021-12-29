package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO3;
import com.ruike.itf.domain.repository.ItfTimeProcessIfaceRepository;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO2;
import com.ruike.itf.infra.mapper.ItfTimeProcessIfaceMapper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:52
 */
@Component
public class ItfTimeProcessIfaceRepositoryImpl implements ItfTimeProcessIfaceRepository {

    @Autowired
    private ItfTimeProcessIfaceMapper itfTimeProcessIfaceMapper;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtEoRepository mtEoRepository;

    @Override
    public List<ItfProcessReturnIfaceVO2> handleProcessReturnData(Long tenantId, List<HmeEoJobTimeSnVO3> lineList) {
        List<ItfProcessReturnIfaceVO2> returnData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lineList)) {
            List<String> workOrderIdList = lineList.stream().map(HmeEoJobTimeSnVO3::getWorkOrderId).distinct().collect(Collectors.toList());
            List<ItfProcessReturnIfaceVO2> woList = itfTimeProcessIfaceMapper.queryWoInfo(tenantId, workOrderIdList);
            Map<String, List<ItfProcessReturnIfaceVO2>> woMap = woList.stream().collect(Collectors.groupingBy(ItfProcessReturnIfaceVO2::getWorkOrderId));
            ItfProcessReturnIfaceVO2 ifaceVO2 = new ItfProcessReturnIfaceVO2();
            List<MtGenStatus> eoStatus = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
            List<LovValueDTO> woTypeList = lovAdapter.queryLovValue("MT.WO_TYPE", tenantId);
            List<String> eoIdList = lineList.stream().map(HmeEoJobTimeSnVO3::getEoId).distinct().collect(Collectors.toList());
            List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID, MtEo.FIELD_STATUS).andWhere(Sqls.custom()
                    .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                    .andIn(MtEo.FIELD_EO_ID, eoIdList)).build());
            Map<String, List<MtEo>> eoMap = mtEoList.stream().collect(Collectors.groupingBy(MtEo::getEoId));
            for (HmeEoJobTimeSnVO3 hmeEoJobTimeSnVO3 : lineList) {
                ifaceVO2.setMaterialLotCode(hmeEoJobTimeSnVO3.getMaterialLotCode());
                ifaceVO2.setSnMaterialCode(hmeEoJobTimeSnVO3.getMaterialCode());
                ifaceVO2.setSnMaterialName(hmeEoJobTimeSnVO3.getMaterialName());
                List<ItfProcessReturnIfaceVO2> itfProcessReturnIfaceVO2s = woMap.get(hmeEoJobTimeSnVO3.getWorkOrderId());
                if (CollectionUtils.isNotEmpty(itfProcessReturnIfaceVO2s)) {
                    ifaceVO2.setSapMaterialCode(itfProcessReturnIfaceVO2s.get(0).getSapMaterialCode());
                    ifaceVO2.setSapMaterialName(itfProcessReturnIfaceVO2s.get(0).getSapMaterialName());
                    ifaceVO2.setWoQuantity(itfProcessReturnIfaceVO2s.get(0).getWoQuantity());
                    ifaceVO2.setWorkOrderType(itfProcessReturnIfaceVO2s.get(0).getWorkOrderType());
                    Optional<LovValueDTO> typeOpt = woTypeList.stream().filter(lov -> StringUtils.equals(ifaceVO2.getWorkOrderType(), lov.getValue())).findFirst();
                    ifaceVO2.setWorkOrderTypeMeaning(typeOpt.isPresent() ? typeOpt.get().getMeaning() : "");
                }
                // eo状态
                List<MtEo> mtEos = eoMap.get(hmeEoJobTimeSnVO3.getEoId());
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    ifaceVO2.setEoStatus(mtEos.get(0).getStatus());
                    Optional<MtGenStatus> firstOpt = eoStatus.stream().filter(es -> StringUtils.equals(es.getStatusCode(), mtEos.get(0).getStatus())).findFirst();
                    ifaceVO2.setEoStatusMeaning(firstOpt.isPresent() ? firstOpt.get().getDescription() : "");
                }
                returnData.add(ifaceVO2);
            }
        }
        return returnData;
    }
}
