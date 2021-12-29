package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeRepairSnBindRepository;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeEoRepairSnVO;
import com.ruike.hme.domain.vo.HmeRepairSnBindVO;
import com.ruike.hme.infra.mapper.HmeRepairSnBindMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.api.dto.MtEoDTO4;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.infra.mapper.MtEoMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 10:37
 */
@Component
public class HmeRepairSnBindRepositoryImpl implements HmeRepairSnBindRepository {

    @Autowired
    private HmeRepairSnBindMapper hmeRepairSnBindMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;
    @Autowired
    private MtEoMapper mtEoMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public List<HmeRepairSnBindVO> repairSnExport(Long tenantId, MtEoDTO4 dto) {
        // 如果传入当前工序  根据工序查询工位 在hme_eo_job_sn按更新时间倒序 取出EO_ID
        List<String> eoIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            //用户默认站点
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
            eoIdList = hmeSnBindEoRepository.queryEoIdByProcessId(tenantId, dto.getProcessId(), defaultSiteId);
            if (CollectionUtils.isEmpty(eoIdList)) {
                return Collections.EMPTY_LIST;
            }
        }
        dto.setEoIdList(eoIdList);
        List<MtEo> mtEoList = mtEoMapper.eoListForUi(tenantId, dto);
        return this.handleData(tenantId, dto, mtEoList);
    }

    @Override
    public void validateReworkMaterialLot(Long tenantId, String eoId, String reworkMaterialLot) {
        if (StringUtils.isNotBlank(reworkMaterialLot)) {
           List<String> eoIdList = hmeRepairSnBindMapper.queryWorkingEoByOldMaterialLotCode(tenantId, reworkMaterialLot);
            List<String> filterEoIds = eoIdList.stream().filter(vo -> !StringUtils.equals(eoId, vo)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(filterEoIds)) {
                throw new MtException("HME_EO_JOB_REWORK_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_REWORK_0004", "HME"));
            }
        }
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setTableName("mt_eo_attr");
            setAttrName("REWORK_MATERIAL_LOT");
            setKeyId(eoId);
        }});

        String reworkMaterialLotCode = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
        // 校验eo是否进站 进站则不允许绑定其他返修条码
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).select(HmeEoJobSn.FIELD_JOB_ID).andWhere(Sqls.custom()
                .andEqualTo(HmeEoJobSn.FIELD_EO_ID, eoId)
                .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)).build());
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList) && !StringUtils.equals(reworkMaterialLotCode, reworkMaterialLot)) {
            // EO已进站,不允许修改返修条码
            throw new MtException("HME_EO_JOB_REWORK_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_REWORK_0005", "HME"));
        }

    }

    private List<HmeRepairSnBindVO> handleData(Long tenantId, MtEoDTO4 dto, List<MtEo> mtEoList) {
        if (CollectionUtils.isEmpty(mtEoList)) {
            return Collections.EMPTY_LIST;
        }
        // 获取WO
        List<String> woIds = mtEoList.stream().filter(c -> StringUtils.isNotEmpty(c.getWorkOrderId()))
                .map(MtEo::getWorkOrderId).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap = null;
        if (CollectionUtils.isNotEmpty(woIds)) {
            List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woPropertyBatchGet(tenantId, woIds);
            mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, c -> c));
        }
        // 获取物料
        List<String> materialIds = mtEoList.stream().filter(c -> StringUtils.isNotEmpty(c.getMaterialId()))
                .map(MtEo::getMaterialId).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> materialMap = null;
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialBasicInfoBatchGet(tenantId, materialIds);
            materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c));
        }
        // 获取产线
        List<String> prodLineIds = mtEoList.stream().filter(c -> StringUtils.isNotEmpty(c.getProductionLineId()))
                .map(MtEo::getProductionLineId).distinct().collect(Collectors.toList());
        Map<String, MtModProductionLine> productionLineMap = null;
        if (CollectionUtils.isNotEmpty(prodLineIds)) {
            List<MtModProductionLine> mtModProductionLines = mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, prodLineIds);
            productionLineMap = mtModProductionLines.stream()
                    .collect(Collectors.toMap(MtModProductionLine::getProdLineId, c -> c));
        }
        List<String> eoIds = mtEoList.stream().map(MtEo::getEoId).distinct().collect(Collectors.toList());
        //获取eo repairSn 2021/05/24 add by chaonan.hu for fang.pan 增加返修SN列及查询条件
        Map<String, String> eoRepairSnMap = null;
        if (StringUtils.isBlank(dto.getBindSnFlag())) {
            List<HmeEoRepairSnVO> hmeEoRepairSnVOS = mtEoMapper.eoRepairSnQuery(tenantId, eoIds);
            if(CollectionUtil.isNotEmpty(hmeEoRepairSnVOS)){
                eoRepairSnMap = hmeEoRepairSnVOS.stream().collect(Collectors.toMap(HmeEoRepairSnVO::getEoId, HmeEoRepairSnVO::getRepairSn));
            }
        }
        // 返回数据
        List<HmeRepairSnBindVO> list = new ArrayList<>(mtEoList.size());
        Map<String, MtMaterialVO> finalMaterialMap = materialMap;
        Map<String, MtModProductionLine> finalProductionLineMap = productionLineMap;
        Map<String, MtWorkOrder> finalMtWorkOrderMap = mtWorkOrderMap;
        Map<String, String> finalEoRepairSnMap = eoRepairSnMap;
        mtEoList.stream().forEach(c -> {
            HmeRepairSnBindVO hmeRepairSnBindVO = new HmeRepairSnBindVO();
            hmeRepairSnBindVO.setEoId(c.getEoId());
            hmeRepairSnBindVO.setEoNum(c.getEoNum());
            if (StringUtils.isNotEmpty(c.getMaterialId())) {
                hmeRepairSnBindVO.setMaterialId(c.getMaterialId());
                if (MapUtils.isNotEmpty(finalMaterialMap) && finalMaterialMap.get(c.getMaterialId()) != null) {
                    hmeRepairSnBindVO.setMaterialCode(finalMaterialMap.get(c.getMaterialId()).getMaterialCode());
                    hmeRepairSnBindVO.setMaterialName(finalMaterialMap.get(c.getMaterialId()).getMaterialName());
                }
            }
            hmeRepairSnBindVO.setEoIdentification(c.getIdentification());
            if (StringUtils.isNotEmpty(c.getProductionLineId())) {
                hmeRepairSnBindVO.setProductionLineId(c.getProductionLineId());
                if (MapUtils.isNotEmpty(finalProductionLineMap)
                        && finalProductionLineMap.get(c.getProductionLineId()) != null) {
                    hmeRepairSnBindVO.setProductionLineCode(
                            finalProductionLineMap.get(c.getProductionLineId()).getProdLineCode());
                    hmeRepairSnBindVO.setProductionLineName(
                            finalProductionLineMap.get(c.getProductionLineId()).getProdLineName());
                }
            }
            if (StringUtils.isNotEmpty(c.getWorkOrderId())) {
                hmeRepairSnBindVO.setWorkOrderId(c.getWorkOrderId());
                if (MapUtils.isNotEmpty(finalMtWorkOrderMap) && finalMtWorkOrderMap.get(c.getWorkOrderId()) != null) {
                    hmeRepairSnBindVO.setWorkOrderNum(finalMtWorkOrderMap.get(c.getWorkOrderId()).getWorkOrderNum());
                }
            }
            if (StringUtils.isNotBlank(dto.getBindSnFlag())) {
                hmeRepairSnBindVO.setRepairSn(c.getRepairSn());
            } else {
                if (MapUtils.isNotEmpty(finalEoRepairSnMap) && finalEoRepairSnMap.get(c.getEoId()) != null) {
                    hmeRepairSnBindVO.setRepairSn(finalEoRepairSnMap.get(c.getEoId()));
                }
            }
            list.add(hmeRepairSnBindVO);
        });
        return list;
    }
}
