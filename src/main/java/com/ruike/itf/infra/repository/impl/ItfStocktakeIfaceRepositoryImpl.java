package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentHis;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.repository.HmeEquipmentHisRepository;
import com.ruike.itf.domain.repository.ItfStocktakeIfaceRepository;
import com.ruike.itf.domain.vo.ItfStocktakeVO;
import com.ruike.itf.domain.vo.ItfStocktakeVO2;
import com.ruike.itf.domain.vo.ItfStocktakeVO3;
import com.ruike.itf.domain.vo.ItfStocktakeVO4;
import com.ruike.itf.infra.mapper.ItfStocktakeIfaceMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 18:28
 */
@Component
public class ItfStocktakeIfaceRepositoryImpl implements ItfStocktakeIfaceRepository {

    @Autowired
    private ItfStocktakeIfaceMapper itfStocktakeIfaceMapper;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEquipmentHisRepository hmeEquipmentHisRepository;

    @Override
    @ProcessLovValue
    public List<ItfStocktakeVO> queryStocktakeList(Long tenantId) {
        // 查询状态NEW和GOING的盘点单信息
        List<ItfStocktakeVO> itfStocktakeVOList = itfStocktakeIfaceMapper.queryStocktakeList(tenantId);
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.USE_FREQUENCY", tenantId);
        for (ItfStocktakeVO itfStocktakeVO : itfStocktakeVOList) {
            if (CollectionUtils.isNotEmpty(itfStocktakeVO.getEquipmentList())) {
                itfStocktakeVO.getEquipmentList().forEach(eq -> {
                    Optional<LovValueDTO> firstOpt = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), eq.getFrequency())).findFirst();
                    if (firstOpt.isPresent()) {
                        eq.setEquipmentStatusMeaning(firstOpt.get().getMeaning());
                    }
                });
            }
        }
        return itfStocktakeVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfStocktakeVO3> updateStocktakeList(List<ItfStocktakeVO> stocktakeVOList) {
        List<ItfStocktakeVO3> headerList = new ArrayList<>();
        // 更新盘点单
        if (CollectionUtils.isNotEmpty(stocktakeVOList)) {
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME_STOCKTAKE_STATUS", stocktakeVOList.get(0).getTenantId());
            List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.USE_FREQUENCY", stocktakeVOList.get(0).getTenantId());
            Long tenantId = stocktakeVOList.get(0).getTenantId();
            List<String> stocktakeNumList = stocktakeVOList.stream().map(ItfStocktakeVO::getStocktakeNum).distinct().collect(Collectors.toList());
            List<HmeEquipmentStocktakeDoc> hmeEquipmentStocktakeDocs = itfStocktakeIfaceMapper.batchQueryStocktakeDocByStocktakeNums(tenantId, stocktakeNumList);
            Map<String, List<HmeEquipmentStocktakeDoc>> hmeEquipmentStocktakeDocMap = hmeEquipmentStocktakeDocs.stream().collect(Collectors.groupingBy(HmeEquipmentStocktakeDoc::getStocktakeNum));
            List<HmeEquipmentStocktakeDoc> equipmentStocktakeDocList = new ArrayList<>();
            for (ItfStocktakeVO itfStocktakeVO : stocktakeVOList) {
                Optional<LovValueDTO> firstOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), itfStocktakeVO.getStocktakeStatus())).findFirst();
                ItfStocktakeVO3 itfStocktakeVO3 = new ItfStocktakeVO3();
                itfStocktakeVO3.setTenantId(itfStocktakeVO.getTenantId());
                itfStocktakeVO3.setStocktakeNum(itfStocktakeVO.getStocktakeNum());
                itfStocktakeVO3.setProcessFlag("S");
                if (!firstOpt.isPresent()) {
                    // 不在值集内 返回错误消息
                    itfStocktakeVO3.setProcessMessage("盘点单状态错误！");
                    itfStocktakeVO3.setProcessFlag("E");
                } else {
                    List<ItfStocktakeVO4> itfStocktakeVO4s = this.handleStocktakeEquipmentList(itfStocktakeVO.getEquipmentList(), lovValueList);
                    // 设备及盘点实绩处理失败 则盘点单也失败
                    List<ItfStocktakeVO4> equipmentList = itfStocktakeVO4s.stream().filter(vo -> StringUtils.equals(vo.getProcessFlag(), "E")).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(equipmentList)) {
                        itfStocktakeVO3.setProcessFlag("E");
                        // 拼接消息
                        StringBuffer sb = new StringBuffer();
                        List<String> processMsgList = equipmentList.stream().map(eq -> {
                            return eq.getAssetEncoding() + "#" + eq.getProcessMessage();
                        }).collect(Collectors.toList());
                        itfStocktakeVO3.setProcessMessage(StringUtils.join(processMsgList, ","));
                    } else {
                        // 更新盘点单信息
                        List<HmeEquipmentStocktakeDoc> hmeEquipmentStocktakeDocList = hmeEquipmentStocktakeDocMap.get(itfStocktakeVO.getStocktakeNum());
                        if (CollectionUtils.isNotEmpty(hmeEquipmentStocktakeDocList)) {
                            HmeEquipmentStocktakeDoc hmeEquipmentStocktakeDoc = new HmeEquipmentStocktakeDoc();
                            hmeEquipmentStocktakeDoc.setStocktakeId(hmeEquipmentStocktakeDocList.get(0).getStocktakeId());
                            hmeEquipmentStocktakeDoc.setStocktakeStatus(itfStocktakeVO.getStocktakeStatus());
                            equipmentStocktakeDocList.add(hmeEquipmentStocktakeDoc);
                        }
                    }
                    itfStocktakeVO3.setEquipmentList(itfStocktakeVO4s);
                }
                headerList.add(itfStocktakeVO3);
            }
            if (CollectionUtils.isNotEmpty(equipmentStocktakeDocList)) {
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                itfStocktakeIfaceMapper.batchUpdateEquipmentStocktakeDoc(tenantId, userId, equipmentStocktakeDocList);
            }
        }
        return headerList;
    }

    private List<ItfStocktakeVO4> handleStocktakeEquipmentList(List<ItfStocktakeVO2> equipmentList, List<LovValueDTO> lovValueList) {
        List<ItfStocktakeVO4> stocktakeVO4List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(equipmentList)) {
            // 校验使用频次、盘点标识（是/否）及盘点时间
            for (ItfStocktakeVO2 itfStocktakeVO2 : equipmentList) {
                ItfStocktakeVO4 itfStocktakeVO4 = new ItfStocktakeVO4();
                itfStocktakeVO4.setAssetEncoding(itfStocktakeVO2.getAssetEncoding());
                itfStocktakeVO4.setStocktakeNum(itfStocktakeVO2.getStocktakeNum());
                itfStocktakeVO4.setTenantId(itfStocktakeVO2.getTenantId());
                itfStocktakeVO4.setProcessFlag("S");
                if (StringUtils.isBlank(itfStocktakeVO2.getEquipmentStatusMeaning())) {
                    itfStocktakeVO4.setProcessMessage("使用频次不能为空");
                    itfStocktakeVO4.setProcessFlag("E");
                } else {
                    Optional<LovValueDTO> firstOpt = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getMeaning(), itfStocktakeVO2.getEquipmentStatusMeaning())).findFirst();
                    if (!firstOpt.isPresent()) {
                        itfStocktakeVO4.setProcessMessage("使用频次值有误");
                        itfStocktakeVO4.setProcessFlag("E");
                    } else {
                        itfStocktakeVO2.setFrequency(firstOpt.get().getValue());
                    }
                }
                if (StringUtils.isBlank(itfStocktakeVO2.getStocktakeFlag())) {
                    itfStocktakeVO4.setProcessMessage("盘点标识不能为空");
                    itfStocktakeVO4.setProcessFlag("E");
                } else {
                    if (!StringCommonUtils.contains(itfStocktakeVO2.getStocktakeFlag(), "Y", "N")) {
                        itfStocktakeVO4.setProcessMessage("盘点标识值有误");
                        itfStocktakeVO4.setProcessFlag("E");
                    }
                }
                if (StringUtils.isBlank(itfStocktakeVO2.getStocktakeDate())) {
                    itfStocktakeVO4.setProcessMessage("盘点时间不能为空");
                    itfStocktakeVO4.setProcessFlag("E");
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        sdf.parse(itfStocktakeVO2.getStocktakeDate());
                    } catch (ParseException e) {
                        itfStocktakeVO4.setProcessMessage("盘点时间格式有误");
                        itfStocktakeVO4.setProcessFlag("E");
                    }
                }
                stocktakeVO4List.add(itfStocktakeVO4);
            }
            Optional<ItfStocktakeVO4> errorOpt = stocktakeVO4List.stream().filter(vo -> StringUtils.equals("E", vo.getProcessFlag())).findFirst();
            if (!errorOpt.isPresent()) {
                Long tenantId = equipmentList.get(0).getTenantId();
                Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
                // 校验全部通过 则更新设备及盘点实绩信息
                List<String> assetEncodingList = equipmentList.stream().map(ItfStocktakeVO2::getAssetEncoding).distinct().collect(Collectors.toList());
                List<String> stocktakeNumList = equipmentList.stream().map(ItfStocktakeVO2::getStocktakeNum).distinct().collect(Collectors.toList());
                List<HmeEquipment> hmeEquipmentList = itfStocktakeIfaceMapper.batchQueryEquipmentByAssetEncodings(tenantId, assetEncodingList);
                Map<String, List<HmeEquipment>> hmeEquipmentMap = hmeEquipmentList.stream().collect(Collectors.groupingBy(HmeEquipment::getAssetEncoding));
                List<HmeEquipmentStocktakeDoc> hmeEquipmentStocktakeDocs = itfStocktakeIfaceMapper.batchQueryStocktakeDocByStocktakeNums(tenantId, stocktakeNumList);
                Map<String, List<HmeEquipmentStocktakeDoc>> hmeEquipmentStocktakeDocMap = hmeEquipmentStocktakeDocs.stream().collect(Collectors.groupingBy(HmeEquipmentStocktakeDoc::getStocktakeNum));
                equipmentList.forEach(eq -> {
                    List<HmeEquipment> hmeEquipments = hmeEquipmentMap.get(eq.getAssetEncoding());
                    if (CollectionUtils.isNotEmpty(hmeEquipments)) {
                        eq.setEquipmentId(hmeEquipments.get(0).getEquipmentId());
                        eq.setAssetEncoding(hmeEquipments.get(0).getAssetEncoding());
                        eq.setAssetName(hmeEquipments.get(0).getAssetName());
                        eq.setAssetClass(hmeEquipments.get(0).getAssetClass());
                    }
                    List<HmeEquipmentStocktakeDoc> hmeEquipmentStocktakeDocList = hmeEquipmentStocktakeDocMap.get(eq.getStocktakeNum());
                    if (CollectionUtils.isNotEmpty(hmeEquipmentStocktakeDocList)) {
                        eq.setStocktakeId(hmeEquipmentStocktakeDocList.get(0).getStocktakeId());
                    }
                });
                try {
                    //20210812 add by sanfeng.zhang for peng.zhao 增加设备修改历史
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventTypeCode("HME_EQUIPMENT_MODIFED");
                    }});
                    List<HmeEquipmentHis> hmeEquipmentHisList = new ArrayList<>();
                    equipmentList.forEach(equipment -> {
                        HmeEquipmentHis hmeEquipmentHis = new HmeEquipmentHis();
                        hmeEquipmentHis.setTenantId(tenantId);
                        hmeEquipmentHis.setEquipmentId(equipment.getEquipmentId());
                        hmeEquipmentHis.setAssetEncoding(equipment.getAssetEncoding());
                        hmeEquipmentHis.setAssetName(equipment.getAssetName());
                        hmeEquipmentHis.setAssetClass(equipment.getAssetClass());
                        hmeEquipmentHis.setFrequency(equipment.getFrequency());
                        hmeEquipmentHis.setLocation(equipment.getLocation());
                        hmeEquipmentHis.setEventId(eventId);
                        hmeEquipmentHisList.add(hmeEquipmentHis);
                    });
                    // 记录设备更新历史
                    if (CollectionUtils.isNotEmpty(hmeEquipmentHisList)) {
                        hmeEquipmentHisRepository.batchInsertSelective(hmeEquipmentHisList);
                    }

                    // 更新设备
                    itfStocktakeIfaceMapper.batchUpdateEquipment(tenantId, userId, equipmentList);
                    // 更新盘点实绩
                    itfStocktakeIfaceMapper.batchUpdateStocktake(tenantId, userId, equipmentList);
                } catch (Exception e) {
                    // 批量更新报错 则更新返回消息
                    stocktakeVO4List.stream().forEach(vo -> {
                        vo.setProcessFlag("E");
                        vo.setProcessMessage(e.getMessage());
                    });
                }
            }
        }
        return stocktakeVO4List;
    }
}
