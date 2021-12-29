package tarzan.inventory.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotChangeHistory;
import tarzan.inventory.domain.repository.MtMaterialLotChangeHistoryRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO1;
import tarzan.inventory.infra.mapper.MtMaterialLotChangeHistoryMapper;

/**
 * 物料批变更历史，记录物料批拆分合并的变更情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@Component
public class MtMaterialLotChangeHistoryRepositoryImpl extends BaseRepositoryImpl<MtMaterialLotChangeHistory>
                implements MtMaterialLotChangeHistoryRepository {
    @Autowired
    private MtMaterialLotChangeHistoryMapper mtMaterialLotChangeHistoryMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialLotRepository materialLotRepository;

    @Override
    public List<MtMaterialCategoryHisVO> sourceMaterialLotQuery(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialLotId", "【API：sourceMaterialLotQuery】"));
        }

        MtMaterialLotChangeHistory history = new MtMaterialLotChangeHistory();
        history.setTenantId(tenantId);
        history.setMaterialLotId(materialLotId);
        List<MtMaterialLotChangeHistory> historyList = this.mtMaterialLotChangeHistoryMapper.select(history);
        if (CollectionUtils.isEmpty(historyList)) {
            return Collections.emptyList();
        }

        List<MtMaterialCategoryHisVO> resultList = new ArrayList<>();
        historyList.stream().forEach(t -> {
            MtMaterialCategoryHisVO hisVO = new MtMaterialCategoryHisVO();
            BeanUtils.copyProperties(t, hisVO);
            resultList.add(hisVO);
        });

        return resultList;
    }

    @Override
    public List<MtMaterialCategoryHisVO1> materialLotLimitChangeHistoryQuery(Long tenantId, String materialLotId) {
        if (StringUtils.isEmpty(materialLotId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId",
                                            "【API：materialLotLimitChangeHistoryQuery】"));
        }

        // Step2将物料批作为拆分合并结果循环获取拆分合并来源
        List<MtMaterialLotChangeHistory> historyList = new ArrayList<>();
        historyList.addAll(getSourceMaterialLot(tenantId, materialLotId, "UP"));

        // Step3将物料批作为拆分合并来源循环获取拆分合并目标
        historyList.addAll(getSourceMaterialLot(tenantId, materialLotId, "DOWN"));
        List<MtMaterialCategoryHisVO1> resultList = new ArrayList<>();
        historyList.stream().forEach(t -> {
            MtMaterialCategoryHisVO1 hisVO1 = new MtMaterialCategoryHisVO1();
            BeanUtils.copyProperties(t, hisVO1);
            hisVO1.setTargetMaterialLotId(t.getMaterialLotId());
            resultList.add(hisVO1);
        });
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String materialLotChangeHistoryCreate(Long tenantId, MtMaterialLotChangeHistory dto) {
        // 第一步：判断输入参数是否合规
        if (dto == null || StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "materialLotId", "【API：materialLotChangeHistoryCreate】"));
        }
        if (StringUtils.isEmpty(dto.getSourceMaterialLotId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "sourceMaterialLotId",
                                            "【API：materialLotChangeHistoryCreate】"));
        }
        if (StringUtils.isEmpty(dto.getReason())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "reason", "【API：materialLotChangeHistoryCreate】"));
        }
        if (dto.getSequence() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "sequence", "【API：materialLotChangeHistoryCreate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：materialLotChangeHistoryCreate】"));
        }

        if (dto.getTrxQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "trxQty", "【API：materialLotChangeHistoryCreate】"));
        }
        if (dto.getSourceTrxQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "sourceTrxQty", "【API：materialLotChangeHistoryCreate】"));
        }

        // 2.第二步，根据输入参数向表MT_MATERIAL_LOT_CHANGE_HISTORY中插入数据
        dto.setTenantId(tenantId);

        MtMaterialLot materialLot =
                        materialLotRepository.materialLotPropertyGet(tenantId, dto.getSourceMaterialLotId());
        if (materialLot != null) {
            dto.setMaterialLotHisId(materialLot.getLatestHisId());
        } else {
            dto.setMaterialLotHisId("");
        }
        self().insertSelective(dto);

        return dto.getMaterialLotChangeHistoryId();
    }

    @Override
    public List<MtMaterialCategoryHisVO1> materialLotLimitSourceIdQuery(Long tenantId, String materialId) {

        MtMaterialLotChangeHistory history = new MtMaterialLotChangeHistory();
        history.setTenantId(tenantId);
        history.setMaterialLotId(materialId);
        List<MtMaterialLotChangeHistory> histories = mtMaterialLotChangeHistoryMapper.select(history);
        if (CollectionUtils.isEmpty(histories)) {
            return Collections.emptyList();
        }

        List<String> eventIds = histories.stream().filter(t -> StringUtils.isNotEmpty(t.getEventId()))
                        .map(MtMaterialLotChangeHistory::getEventId).distinct().collect(Collectors.toList());
        List<MtEventVO1> events = mtEventRepository.eventBatchGet(tenantId, eventIds);

        Map<String, Date> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(events)) {
            map = events.stream().collect(Collectors.toMap(MtEventVO1::getEventId, MtEventVO1::getEventTime));
        }


        Map<String, Date> finalMap = map;
        List<MtMaterialCategoryHisVO1> collect = histories.stream().map(t -> {
            MtMaterialCategoryHisVO1 hisVO1 = new MtMaterialCategoryHisVO1();
            hisVO1.setTargetMaterialLotId(t.getMaterialLotId());
            hisVO1.setSourceMaterialLotId(t.getSourceMaterialLotId());
            hisVO1.setEventId(t.getEventId());
            hisVO1.setEventTime(finalMap.get(t.getEventId()));
            hisVO1.setReason(t.getReason());
            hisVO1.setSequence(t.getSequence());
            hisVO1.setSourceTrxQty(t.getSourceTrxQty());
            hisVO1.setTrxQty(t.getTrxQty());
            return hisVO1;
        }).collect(Collectors.toList());
        return collect;
    }

    /***
     * MT_MATERIAL_LOT_CHANGE_HISTORY中获取数据
     */
    private List<MtMaterialLotChangeHistory> getSourceMaterialLot(Long tenantId, String materialLotId, String dir) {
        MtMaterialLotChangeHistory history = new MtMaterialLotChangeHistory();
        if ("UP".equals(dir)) {
            history.setMaterialLotId(materialLotId);
        } else {
            history.setSourceMaterialLotId(materialLotId);
        }
        List<MtMaterialLotChangeHistory> historyList = new ArrayList<>();
        history.setTenantId(tenantId);
        List<MtMaterialLotChangeHistory> temp = this.mtMaterialLotChangeHistoryMapper.select(history);
        historyList.addAll(temp);
        if (CollectionUtils.isEmpty(temp)) {
            return temp;
        }

        for (MtMaterialLotChangeHistory t : temp) {
            List<MtMaterialLotChangeHistory> list;
            if ("UP".equals(dir)) {
                list = getSourceMaterialLot(tenantId, t.getSourceMaterialLotId(), dir);
            } else {
                list = getSourceMaterialLot(tenantId, t.getMaterialLotId(), dir);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                historyList.addAll(list);
            }
        }

        return historyList;
    }

}
