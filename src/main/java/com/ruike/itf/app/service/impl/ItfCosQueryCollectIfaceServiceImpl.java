package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfCosQueryCollectIfaceService;
import com.ruike.itf.infra.mapper.ItfCosQueryCollectIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import tarzan.inventory.domain.repository.MtMaterialLotRepository;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ItfSnQueryCollectIfaceServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:41
 * @Version 1.0
 **/
@Service
@Slf4j
public class ItfCosQueryCollectIfaceServiceImpl implements ItfCosQueryCollectIfaceService {
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private ItfCosQueryCollectIfaceMapper itfCosQueryCollectIfaceMapper;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CosQueryCollectItfReturnDTO invoke(Long tenantId, String materialLotCode) {

        CosQueryCollectItfReturnDTO cosQueryCollectItfReturnDTO = new CosQueryCollectItfReturnDTO();
        if (Strings.isBlank(materialLotCode)) {
            cosQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            cosQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0007", "ITF", "materialLotCode"));
            return cosQueryCollectItfReturnDTO;
        }
        List<CosQueryCollectItfReturnDTO2> cosQueryCollectItfReturnDTO2s = itfCosQueryCollectIfaceMapper.selectMaterialLotByCode(tenantId, materialLotCode);
        if (CollectionUtils.isEmpty(cosQueryCollectItfReturnDTO2s)) {
            cosQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            cosQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0018", "ITF", materialLotCode));
            return cosQueryCollectItfReturnDTO;
        }
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, cosQueryCollectItfReturnDTO2s.get(0).getMaterialLotId())).build());
        if (CollectionUtils.isEmpty(hmeMaterialLotLoads)) {
            cosQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            cosQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0019", "ITF", materialLotCode));
            return cosQueryCollectItfReturnDTO;
        }
        //2021-10-11 09:21 edit by chaonan.hu for peng.zhao 增加是否测试偏振度或是否测试发散角标识逻辑
        CosQueryCollectItfReturnDTO3 cosQueryCollectItfReturnDTO3 = polarizationAndVolatilizationQuery(tenantId, cosQueryCollectItfReturnDTO2s.get(0), hmeMaterialLotLoads);
        if (WmsConstant.KEY_IFACE_STATUS_ERROR.equals(cosQueryCollectItfReturnDTO3.getProcessStatus())) {
            cosQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            cosQueryCollectItfReturnDTO.setProcessMessage(cosQueryCollectItfReturnDTO3.getProcessMessage());
            return cosQueryCollectItfReturnDTO;
        }
        List<CosQueryCollectItfReturnDTO1> cosQueryCollectItfReturnDTO1List = new ArrayList<>();
        List<HmeMaterialLotLoad> materialLotLoadList = cosQueryCollectItfReturnDTO3.getMaterialLotLoadList();
        for (HmeMaterialLotLoad temp :
                hmeMaterialLotLoads) {
            CosQueryCollectItfReturnDTO1 cosQueryCollectItfReturnDTO1 = new CosQueryCollectItfReturnDTO1();
            if (!Objects.isNull(temp.getLoadRow()) && !Objects.isNull(temp.getLoadColumn())) {
                cosQueryCollectItfReturnDTO1.setCosPos((char) (64 + Integer.parseInt(temp.getLoadRow().toString())) + temp.getLoadColumn().toString());
            }
            cosQueryCollectItfReturnDTO1.setCosType(temp.getAttribute1());
            cosQueryCollectItfReturnDTO1.setHotSink(temp.getHotSinkCode());
            cosQueryCollectItfReturnDTO1.setWafer(temp.getAttribute2());
            cosQueryCollectItfReturnDTO1.setLabCode(temp.getAttribute19());
            if (!CollectionUtils.isEmpty(materialLotLoadList)) {
                List<HmeMaterialLotLoad> singleMaterialLotLoad = materialLotLoadList.stream().filter(item -> temp.getMaterialLotLoadId().equals(item.getMaterialLotLoadId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(singleMaterialLotLoad)) {
                    if (HmeConstants.ConstantValue.YES.equals(singleMaterialLotLoad.get(0).getAttribute17())) {
                        cosQueryCollectItfReturnDTO1.setPolarization(HmeConstants.ConstantValue.YES);
                    } else {
                        cosQueryCollectItfReturnDTO1.setPolarization(HmeConstants.ConstantValue.NO);
                    }
                    if (HmeConstants.ConstantValue.YES.equals(singleMaterialLotLoad.get(0).getAttribute18())) {
                        cosQueryCollectItfReturnDTO1.setVolatilization(HmeConstants.ConstantValue.YES);
                    } else {
                        cosQueryCollectItfReturnDTO1.setVolatilization(HmeConstants.ConstantValue.NO);
                    }
                }
            } else {
                if (HmeConstants.ConstantValue.YES.equals(temp.getAttribute17())) {
                    cosQueryCollectItfReturnDTO1.setPolarization(HmeConstants.ConstantValue.YES);
                } else {
                    cosQueryCollectItfReturnDTO1.setPolarization(HmeConstants.ConstantValue.NO);
                }
                if (HmeConstants.ConstantValue.YES.equals(temp.getAttribute18())) {
                    cosQueryCollectItfReturnDTO1.setVolatilization(HmeConstants.ConstantValue.YES);
                } else {
                    cosQueryCollectItfReturnDTO1.setVolatilization(HmeConstants.ConstantValue.NO);
                }
            }
            cosQueryCollectItfReturnDTO1List.add(cosQueryCollectItfReturnDTO1);
        }

        cosQueryCollectItfReturnDTO.setResultList(cosQueryCollectItfReturnDTO1List);
        cosQueryCollectItfReturnDTO.setMaterialLotCode(materialLotCode);
        cosQueryCollectItfReturnDTO.setCosQuantity(cosQueryCollectItfReturnDTO2s.get(0).getPrimaryUomQty());
        cosQueryCollectItfReturnDTO.setCosWo(cosQueryCollectItfReturnDTO2s.get(0).getWorkOrderNum());
        cosQueryCollectItfReturnDTO.setMaterialCode(cosQueryCollectItfReturnDTO2s.get(0).getMaterialCode());
        cosQueryCollectItfReturnDTO.setMaterialDescription(cosQueryCollectItfReturnDTO2s.get(0).getMaterialName());
        cosQueryCollectItfReturnDTO.setWoQuantity(cosQueryCollectItfReturnDTO2s.get(0).getQty());
        cosQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.CONSTANT_Y);
        return cosQueryCollectItfReturnDTO;
    }

    @Override
    public CosQueryCollectItfReturnDTO3 polarizationAndVolatilizationQuery(Long tenantId, CosQueryCollectItfReturnDTO2 dto,
                                                                           List<HmeMaterialLotLoad> hmeMaterialLotLoadList) {
        CosQueryCollectItfReturnDTO3 result = new CosQueryCollectItfReturnDTO3();
        //根据cos类型和wafer查询偏振度和发散角测试结果数据
        List<CosQueryCollectItfReturnDTO4> cosQueryCollectItfReturnDTO4List = new ArrayList<>();
        List<CosQueryCollectItfReturnDTO4> polarizationAndVolatilization = new ArrayList<>();
        cosQueryCollectItfReturnDTO4List.addAll(itfCosQueryCollectIfaceMapper.cosDegreeTestActualQuery(tenantId, dto.getCosType(), dto.getWafer()));
        List<CosQueryCollectItfReturnDTO5> hmeCosDegreeTestActualInsertList = new ArrayList<>();
        Map<String, String> actualIdMap = new HashMap<>();
        //是否包含偏振度
        List<CosQueryCollectItfReturnDTO4> polarizationList = cosQueryCollectItfReturnDTO4List.stream().filter(item -> "POLARIZATION".equals(item.getTestObject())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(polarizationList)) {
            //如果不存在，则根据cos类型和测试对象查询偏振度和发散角良率维护头表数据，进而插入到Hme_cos_degree_test_actual表
            CosQueryCollectItfReturnDTO4 cosQueryCollectItfReturnDTO4 = itfCosQueryCollectIfaceMapper.tagPassRateHeaderQuery(tenantId, dto.getCosType(), "POLARIZATION");
            if (Objects.isNull(cosQueryCollectItfReturnDTO4)) {
                //如果找不到则返回S
                result.setProcessStatus("S");
                return result;
            } else {
                //如果找到，则插入到Hme_cos_degree_test_actual表
                CosQueryCollectItfReturnDTO5 hmeCosDegreeTestActual = new CosQueryCollectItfReturnDTO5();
                hmeCosDegreeTestActual.setTenantId(tenantId);
                hmeCosDegreeTestActual.setCosType(dto.getCosType());
                hmeCosDegreeTestActual.setWafer(dto.getWafer());
                hmeCosDegreeTestActual.setTestObject("POLARIZATION");
                if ("ALL".equals(cosQueryCollectItfReturnDTO4.getTestType())) {
                    hmeCosDegreeTestActual.setTestQty(0L);
                    hmeCosDegreeTestActual.setTargetQty(0L);
                    hmeCosDegreeTestActual.setPriority(0L);
                } else {
                    hmeCosDegreeTestActual.setTestQty(cosQueryCollectItfReturnDTO4.getTestQty());
                    hmeCosDegreeTestActual.setTargetQty(cosQueryCollectItfReturnDTO4.getTestQty());
                }
                hmeCosDegreeTestActual.setTestStatus("TEST");
                hmeCosDegreeTestActualInsertList.add(hmeCosDegreeTestActual);
                cosQueryCollectItfReturnDTO4.setTestQty(hmeCosDegreeTestActual.getTestQty());
                cosQueryCollectItfReturnDTO4.setTargetQty(hmeCosDegreeTestActual.getTargetQty());
                polarizationAndVolatilization.add(cosQueryCollectItfReturnDTO4);
            }
        } else {
            polarizationAndVolatilization.add(polarizationList.get(0));
            actualIdMap.put("POLARIZATION", polarizationList.get(0).getDegreeTestId());
        }
        //是否包含发散角
        List<CosQueryCollectItfReturnDTO4> volatilizationList = cosQueryCollectItfReturnDTO4List.stream().filter(item -> "VOLATILIZATION".equals(item.getTestObject())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(volatilizationList)) {
            //如果不存在，则根据cos类型和测试对象查询偏振度和发散角良率维护头表数据，进而插入到Hme_cos_degree_test_actual表
            CosQueryCollectItfReturnDTO4 cosQueryCollectItfReturnDTO4 = itfCosQueryCollectIfaceMapper.tagPassRateHeaderQuery(tenantId, dto.getCosType(), "VOLATILIZATION");
            if (Objects.isNull(cosQueryCollectItfReturnDTO4)) {
                //如果找不到则返回S
                result.setProcessStatus("S");
                return result;
            } else {
                //如果找到，则插入到Hme_cos_degree_test_actual表
                CosQueryCollectItfReturnDTO5 hmeCosDegreeTestActual = new CosQueryCollectItfReturnDTO5();
                hmeCosDegreeTestActual.setTenantId(tenantId);
                hmeCosDegreeTestActual.setCosType(dto.getCosType());
                hmeCosDegreeTestActual.setWafer(dto.getWafer());
                hmeCosDegreeTestActual.setTestObject("VOLATILIZATION");
                if ("ALL".equals(cosQueryCollectItfReturnDTO4.getTestType())) {
                    hmeCosDegreeTestActual.setTestQty(0L);
                    hmeCosDegreeTestActual.setTargetQty(0L);
                    hmeCosDegreeTestActual.setPriority(0L);
                } else {
                    hmeCosDegreeTestActual.setTestQty(cosQueryCollectItfReturnDTO4.getTestQty());
                    hmeCosDegreeTestActual.setTargetQty(cosQueryCollectItfReturnDTO4.getTestQty());
                }
                hmeCosDegreeTestActual.setTestStatus("TEST");
                hmeCosDegreeTestActualInsertList.add(hmeCosDegreeTestActual);
                cosQueryCollectItfReturnDTO4.setTestQty(hmeCosDegreeTestActual.getTestQty());
                cosQueryCollectItfReturnDTO4.setTargetQty(hmeCosDegreeTestActual.getTargetQty());
                polarizationAndVolatilization.add(cosQueryCollectItfReturnDTO4);
            }
        } else {
            polarizationAndVolatilization.add(volatilizationList.get(0));
            actualIdMap.put("VOLATILIZATION", volatilizationList.get(0).getDegreeTestId());
        }
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date nowDate = new Date();
        if (!CollectionUtils.isEmpty(hmeCosDegreeTestActualInsertList)) {
            //批量插入到hmeCosDegreeTestActual表以及历史表
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_s", hmeCosDegreeTestActualInsertList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_cid_s", hmeCosDegreeTestActualInsertList.size());
            List<String> hisIds = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_his_s", hmeCosDegreeTestActualInsertList.size());
            List<String> hisCids = mtCustomDbRepository.getNextKeys("hme_cos_degree_test_actual_his_cid_s", hmeCosDegreeTestActualInsertList.size());
            int i = 0;
            for (CosQueryCollectItfReturnDTO5 hmeCosDegreeTestActual : hmeCosDegreeTestActualInsertList) {
                hmeCosDegreeTestActual.setDegreeTestId(ids.get(i));
                if ("VOLATILIZATION".equals(hmeCosDegreeTestActual.getTestObject())) {
                    actualIdMap.put("VOLATILIZATION", hmeCosDegreeTestActual.getDegreeTestId());
                } else if ("POLARIZATION".equals(hmeCosDegreeTestActual.getTestObject())) {
                    actualIdMap.put("POLARIZATION", hmeCosDegreeTestActual.getDegreeTestId());
                }
                hmeCosDegreeTestActual.setCid(Long.valueOf(cids.get(i)));
                hmeCosDegreeTestActual.setObjectVersionNumber(1L);
                hmeCosDegreeTestActual.setCreationDate(nowDate);
                hmeCosDegreeTestActual.setCreatedBy(userId);
                hmeCosDegreeTestActual.setLastUpdateDate(nowDate);
                hmeCosDegreeTestActual.setLastUpdatedBy(userId);
                hmeCosDegreeTestActual.setDegreeTestHisId(hisIds.get(i));
                hmeCosDegreeTestActual.setHisCid(Long.valueOf(hisCids.get(i)));
                i++;
            }
            itfCosQueryCollectIfaceMapper.batchInsertHmeCosDegreeTestActual(tenantId, hmeCosDegreeTestActualInsertList);
            itfCosQueryCollectIfaceMapper.batchInsertHmeCosDegreeTestActualHis(tenantId, hmeCosDegreeTestActualInsertList);
        }
//        //判断testQty是否有等于0的 如果有，则取装载表所有数据
//        long count = polarizationAndVolatilization.stream().filter(item -> item.getTestQty() == 0).count();
//        if (count == 0) {
//            //如果没有等于0的，则需要确定最终的test_qty
//            for (CosQueryCollectItfReturnDTO4 cosQueryCollectItfReturnDTO4 : polarizationAndVolatilization) {
//                if ("POLARIZATION".equals(cosQueryCollectItfReturnDTO4.getTestObject())) {
//                    Long attribute17Count = itfCosQueryCollectIfaceMapper.countAttribute17Y(tenantId, dto.getCosType(), dto.getWafer());
//                    cosQueryCollectItfReturnDTO4.setTestQty(cosQueryCollectItfReturnDTO4.getTestQty() - attribute17Count);
//                } else if ("VOLATILIZATION".equals(cosQueryCollectItfReturnDTO4.getTestObject())) {
//                    Long attribute18Count = itfCosQueryCollectIfaceMapper.countAttribute18Y(tenantId, dto.getCosType(), dto.getWafer());
//                    cosQueryCollectItfReturnDTO4.setTestQty(cosQueryCollectItfReturnDTO4.getTestQty() - attribute18Count);
//                }
//            }
//            polarizationAndVolatilization = polarizationAndVolatilization.stream().sorted(Comparator.comparing(CosQueryCollectItfReturnDTO4::getTestQty).reversed()).collect(Collectors.toList());
//            Long testQty = polarizationAndVolatilization.get(0).getTestQty();
//            if (testQty > 0) {
//                hmeMaterialLotLoadList = itfCosQueryCollectIfaceMapper.materialLotLoadQuery(tenantId, dto.getMaterialLotId(), testQty);
//            }
//        }
        //更新装载表数据
        if (!CollectionUtils.isEmpty(hmeMaterialLotLoadList)) {
            List<String> cidS = mtCustomDbRepository.getNextKeys("hme_material_lot_load_cid_s", hmeMaterialLotLoadList.size());
            List<String> sqlList = new ArrayList<>();
            int i = 0;
            for (CosQueryCollectItfReturnDTO4 cosQueryCollectItfReturnDTO4 : polarizationAndVolatilization) {
                Long testQty = cosQueryCollectItfReturnDTO4.getTestQty();
                if (testQty == 0) {
                    for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
                        if ("POLARIZATION".equals(cosQueryCollectItfReturnDTO4.getTestObject())) {
                            hmeMaterialLotLoad.setAttribute17("Y");
                        } else {
                            hmeMaterialLotLoad.setAttribute18("Y");
                        }

                    }
                } else {
                    Long targetQty = cosQueryCollectItfReturnDTO4.getTargetQty();
                    if (targetQty != 0) {
                        List<HmeMaterialLotLoad> attribute17NotYLoadList = hmeMaterialLotLoadList.stream().filter(item -> !"Y".equals(item.getAttribute17())).collect(Collectors.toList());
                        int attribute17NotYCount = 0;
                        if(!CollectionUtils.isEmpty(attribute17NotYLoadList)){
                            attribute17NotYCount = attribute17NotYLoadList.size();
                        }
                        if (attribute17NotYLoadList.size() >= targetQty) {
                            for (int j = 0; j < targetQty; j++) {
                                HmeMaterialLotLoad hmeMaterialLotLoad = attribute17NotYLoadList.get(j);
                                if ("POLARIZATION".equals(cosQueryCollectItfReturnDTO4.getTestObject())) {
                                    hmeMaterialLotLoad.setAttribute17("Y");
                                } else {
                                    hmeMaterialLotLoad.setAttribute18("Y");
                                }
                            }
                            String polarizationDegreeTestId = actualIdMap.get(cosQueryCollectItfReturnDTO4.getTestObject());
                            itfCosQueryCollectIfaceMapper.updateTargetQty(tenantId, polarizationDegreeTestId, 0L, userId);
                            CosQueryCollectItfReturnDTO5 cosQueryCollectItfReturnDTO5 = itfCosQueryCollectIfaceMapper.hmeCosDegreeTestActualQuery(tenantId, polarizationDegreeTestId);
                            String id = mtCustomDbRepository.getNextKey("hme_cos_degree_test_actual_his_s");
                            String cid = mtCustomDbRepository.getNextKey("hme_cos_degree_test_actual_his_cid_s");
                            cosQueryCollectItfReturnDTO5.setTargetQty(0L);
                            cosQueryCollectItfReturnDTO5.setDegreeTestHisId(id);
                            cosQueryCollectItfReturnDTO5.setHisCid(Long.valueOf(cid));
                            cosQueryCollectItfReturnDTO5.setObjectVersionNumber(1L);
                            cosQueryCollectItfReturnDTO5.setCreationDate(nowDate);
                            cosQueryCollectItfReturnDTO5.setCreatedBy(userId);
                            cosQueryCollectItfReturnDTO5.setLastUpdateDate(nowDate);
                            cosQueryCollectItfReturnDTO5.setLastUpdatedBy(userId);
                            List<CosQueryCollectItfReturnDTO5> insertList = new ArrayList<>();
                            insertList.add(cosQueryCollectItfReturnDTO5);
                            itfCosQueryCollectIfaceMapper.batchInsertHmeCosDegreeTestActualHis(tenantId, insertList);
                        } else {
                            for (HmeMaterialLotLoad hmeMaterialLotLoad : attribute17NotYLoadList) {
                                if ("POLARIZATION".equals(cosQueryCollectItfReturnDTO4.getTestObject())) {
                                    hmeMaterialLotLoad.setAttribute17("Y");
                                } else {
                                    hmeMaterialLotLoad.setAttribute18("Y");
                                }
                            }
                            String polarizationDegreeTestId = actualIdMap.get(cosQueryCollectItfReturnDTO4.getTestObject());
                            Long finalTargetQty = targetQty - attribute17NotYCount;
                            itfCosQueryCollectIfaceMapper.updateTargetQty(tenantId, polarizationDegreeTestId, finalTargetQty, userId);
                            CosQueryCollectItfReturnDTO5 cosQueryCollectItfReturnDTO5 = itfCosQueryCollectIfaceMapper.hmeCosDegreeTestActualQuery(tenantId, polarizationDegreeTestId);
                            String id = mtCustomDbRepository.getNextKey("hme_cos_degree_test_actual_his_s");
                            String cid = mtCustomDbRepository.getNextKey("hme_cos_degree_test_actual_his_cid_s");
                            cosQueryCollectItfReturnDTO5.setTargetQty(finalTargetQty);
                            cosQueryCollectItfReturnDTO5.setDegreeTestHisId(id);
                            cosQueryCollectItfReturnDTO5.setHisCid(Long.valueOf(cid));
                            cosQueryCollectItfReturnDTO5.setObjectVersionNumber(1L);
                            cosQueryCollectItfReturnDTO5.setCreationDate(nowDate);
                            cosQueryCollectItfReturnDTO5.setCreatedBy(userId);
                            cosQueryCollectItfReturnDTO5.setLastUpdateDate(nowDate);
                            cosQueryCollectItfReturnDTO5.setLastUpdatedBy(userId);
                            List<CosQueryCollectItfReturnDTO5> insertList = new ArrayList<>();
                            insertList.add(cosQueryCollectItfReturnDTO5);
                            itfCosQueryCollectIfaceMapper.batchInsertHmeCosDegreeTestActualHis(tenantId, insertList);
                        }
                    }
                }
            }
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
                hmeMaterialLotLoad.setCid(Long.valueOf(cidS.get(i)));
                hmeMaterialLotLoad.setObjectVersionNumber(hmeMaterialLotLoad.getObjectVersionNumber() + 1);
                hmeMaterialLotLoad.setLastUpdatedBy(userId);
                hmeMaterialLotLoad.setLastUpdateDate(nowDate);
                i++;
                sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeMaterialLotLoad));
            }
            if (!CollectionUtils.isEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
            result.setProcessStatus("S");
            result.setMaterialLotLoadList(hmeMaterialLotLoadList);
        } else {
            result.setProcessStatus("S");
        }
        return result;
    }

}
