package com.ruike.itf.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentHis;
import com.ruike.hme.domain.repository.HmeEquipmentHisRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import com.ruike.itf.app.service.ItfEquipmentIfaceService;
import com.ruike.itf.domain.entity.ItfEquipmentIface;
import com.ruike.itf.domain.repository.ItfEquipmentIfaceRepository;
import com.ruike.itf.domain.vo.ItfEquipmentReturnVO;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.lov.feign.LovFeignClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.itf.infra.constant.ItfConstant.ConstantValue.YES;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.*;

/**
 * 设备台帐接口表应用服务默认实现
 *
 * @author yonghui.zhu@hand-china.com 2021-01-08 14:11:29
 */
@Service
public class ItfEquipmentIfaceServiceImpl implements ItfEquipmentIfaceService {
    private final ItfEquipmentIfaceRepository itfEquipmentIfaceRepository;
    private final HmeEquipmentRepository hmeEquipmentRepository;
    private final HmeEquipmentMapper hmeEquipmentMapper;
    private final LovFeignClient lovFeignClient;
    private final MtModAreaRepository areaRepository;
    private final HmeEquipmentHisRepository hmeEquipmentHisRepository;
    private final MtEventRepository mtEventRepository;

    public ItfEquipmentIfaceServiceImpl(ItfEquipmentIfaceRepository itfEquipmentIfaceRepository, HmeEquipmentRepository hmeEquipmentRepository, HmeEquipmentMapper hmeEquipmentMapper, LovFeignClient lovFeignClient, MtModAreaRepository areaRepository, HmeEquipmentHisRepository hmeEquipmentHisRepository, MtEventRepository mtEventRepository) {
        this.itfEquipmentIfaceRepository = itfEquipmentIfaceRepository;
        this.hmeEquipmentRepository = hmeEquipmentRepository;
        this.hmeEquipmentMapper = hmeEquipmentMapper;
        this.lovFeignClient = lovFeignClient;
        this.areaRepository = areaRepository;
        this.hmeEquipmentHisRepository = hmeEquipmentHisRepository;
        this.mtEventRepository = mtEventRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<ItfEquipmentReturnVO> invoke(Long tenantId, List<ItfEquipmentIface> list) {
        Set<String> assetEncodingSet = list.stream().map(ItfEquipmentIface::getAssetEncoding).filter(Objects::nonNull).collect(Collectors.toSet());
        List<HmeEquipment> dbList = hmeEquipmentRepository.selectByCondition(Condition.builder(HmeEquipment.class).andWhere(Sqls.custom().andEqualTo(HmeEquipment.FIELD_TENANT_ID, tenantId).
                andIn(HmeEquipment.FIELD_ASSET_ENCODING, assetEncodingSet)).build());
        // 按照assetEncoding是否存在，判断是该新增还是更新
        Map<String, HmeEquipment> assetEncodingMap = dbList.stream().collect(Collectors.toMap(HmeEquipment::getAssetEncoding, Function.identity(), (k1, k2) -> k1));

        // 数据验证
        this.processValidation(tenantId, assetEncodingMap.keySet(), list);

        // 执行导入
        this.processImport(tenantId, assetEncodingMap, list);

        // 数据存接口表
        list.forEach(rec -> rec.setProcessStatus(ERROR.equals(rec.getProcessStatus()) ? ERROR : SUCCESS));
        itfEquipmentIfaceRepository.batchInsertSelective(list);

        // 组装返回数据
        List<ItfEquipmentReturnVO> result = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(ItfEquipmentIface.class, ItfEquipmentReturnVO.class, false);
        list.forEach(rec -> {
            ItfEquipmentReturnVO obj = new ItfEquipmentReturnVO();
            copier.copy(rec, obj, null);
            result.add(obj);
        });

        return result;
    }

    /**
     * 处理验证
     *
     * @param tenantId         租户
     * @param assetEncodingSet 已有数据
     * @param list             接口数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/13 02:04:22
     */
    private void processValidation(Long tenantId, Set<String> assetEncodingSet, List<ItfEquipmentIface> list) {
        // 获取验证数据
        List<LovValueDTO> equipmentTypeList = lovFeignClient.queryLovValue("HME.EQUIPEMNT_TYPE", tenantId);
        List<LovValueDTO> equipmentCategoryList = lovFeignClient.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
        List<LovValueDTO> applyTypeList = lovFeignClient.queryLovValue("HME.APPLY_TYPE", tenantId);
        List<LovValueDTO> yesNoList = lovFeignClient.queryLovValue("WMS.FLAG_YN", tenantId);
        List<LovValueDTO> useFrequencyList = lovFeignClient.queryLovValue("HME.USE_FREQUENCY", tenantId);
        List<LovValueDTO> equipmentStatusList = lovFeignClient.queryLovValue("HME.EQUIPMENT_STATUS", tenantId);
        List<LovValueDTO> equipmentCodeList = lovFeignClient.queryLovValue("HME.EQUIPMENT_CODE", tenantId);
        List<MtModArea> departmentList = areaRepository.select(new MtModArea() {{
            setTenantId(tenantId);
            setEnableFlag(YES);
            setAreaCategory("SYB");
        }});

        Set<String> equipmentTypeSet = equipmentTypeList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> equipmentCategorySet = equipmentCategoryList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> applyTypeSet = applyTypeList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> yesNoSet = yesNoList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> useFrequencySet = useFrequencyList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Set<String> equipmentStatusSet = equipmentStatusList.stream().map(LovValueDTO::getValue).collect(Collectors.toSet());
        Map<String, String> departmentMap = departmentList.stream().collect(Collectors.toMap(MtModArea::getAreaCode, MtModArea::getAreaId, (k1, k2) -> k1));
        Map<String, String> equipmentCodeMap = equipmentCodeList.stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getTag, (k1, k2) -> k1));

        Date nowDate = DateUtil.date();
        list.forEach(rec -> {
            StringBuilder messageBuilder = new StringBuilder();
            boolean existsFlag = assetEncodingSet.contains(rec.getAssetEncoding());
            // 若新增时为给值则默认可用
            rec.setEquipmentStatus(!existsFlag && StringUtils.isBlank(rec.getEquipmentStatus()) ? "KY" : rec.getEquipmentStatus());
            if (StringUtils.isBlank(rec.getAssetEncoding())) {
                messageBuilder.append("资产编码字段，不可为空！");
            }
            messageBuilder.append(!((existsFlag && StringUtils.isBlank(rec.getEquipmentType())) || equipmentTypeSet.contains(rec.getEquipmentType())) ? "设备类型不存在!" : "");
            messageBuilder.append(!((existsFlag && StringUtils.isBlank(rec.getEquipmentCategory())) || equipmentCategorySet.contains(rec.getEquipmentCategory())) ? "设备类别不存在!" : "");
            messageBuilder.append(StringUtils.isNotBlank(rec.getApplyType()) && !applyTypeSet.contains(rec.getApplyType()) ? "应用类型不存在!" : "");
            messageBuilder.append(!((existsFlag && StringUtils.isBlank(rec.getMeasureFlag())) || yesNoSet.contains(rec.getMeasureFlag())) ? "计量类型不存在!" : "");
            messageBuilder.append(!((existsFlag && StringUtils.isBlank(rec.getFrequency())) || useFrequencySet.contains(rec.getFrequency())) ? "使用频次类型不存在!" : "");
            messageBuilder.append(!((existsFlag && StringUtils.isBlank(rec.getEquipmentStatus())) || equipmentStatusSet.contains(rec.getEquipmentStatus())) ? "设备状态不存在!" : "");
            if (StringUtils.isNotBlank(rec.getBusinessId())) {
                if (!departmentMap.containsKey(rec.getBusinessId())) {
                    messageBuilder.append("保管部门不存在!");
                } else {
                    rec.setBusinessId(departmentMap.get(rec.getBusinessId()));
                }
            }
            // 新增时验证台账类型 获取第1位和第5位
            if (!existsFlag) {
                String acctType = String.valueOf(rec.getAssetEncoding().charAt(0)) + rec.getAssetEncoding().charAt(5);
                if (!equipmentCodeMap.containsKey(acctType)) {
                    messageBuilder.append("台账类型获取失败! ");
                } else {
                    rec.setAccountType(equipmentCodeMap.get(acctType));
                }
            }

            // 若存在错误消息，则为有错的数据，其他默认为新建状态
            if (messageBuilder.length() > 0) {
                rec.setProcessMessage(messageBuilder.toString());
                rec.setProcessStatus(ERROR);
            } else {
                rec.setProcessStatus(NEW);
                rec.setAssetClass("MACHINE");
            }
            rec.setTenantId(tenantId);
            rec.setProcessDate(nowDate);
        });
    }

    /**
     * 处理导入
     *
     * @param tenantId         租户
     * @param assetEncodingMap 已有数据
     * @param list             接口数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/13 02:04:22
     */
    private void processImport(Long tenantId, Map<String, HmeEquipment> assetEncodingMap, List<ItfEquipmentIface> list) {
        // 忽略所有弹性域字段
        List<HmeEquipment> insertList = new ArrayList<>();
        List<HmeEquipment> updateList = new ArrayList<>();
        List<HmeEquipmentHis> insertHisList = new ArrayList<>();
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("HME_EQUIPMENT_MODIFED");
        }});
        list.forEach(rec -> {
            if (!ERROR.equals(rec.getProcessStatus())) {
                boolean updateFlag = assetEncodingMap.containsKey(rec.getAssetEncoding());
                HmeEquipment hmeEquipment = rec.toEntity(updateFlag);
                hmeEquipment.setTenantId(tenantId);
                if (updateFlag) {
                    // 20210804 add by sanfeng.zhang 增加历史记录
                    HmeEquipment hmeEquipmentObj = assetEncodingMap.get(hmeEquipment.getAssetEncoding());
                    hmeEquipment.setEquipmentId(hmeEquipmentObj.getEquipmentId());
                    HmeEquipmentHis hmeEquipmentHis = new HmeEquipmentHis();
                    BeanUtils.copyProperties(hmeEquipment, hmeEquipmentHis);
                    hmeEquipmentHis.setEventId(eventId);
                    hmeEquipmentHis.setAssetName(hmeEquipmentObj.getAssetName());
                    hmeEquipmentHis.setAssetClass(hmeEquipmentObj.getAssetClass());
                    insertHisList.add(hmeEquipmentHis);
                    updateList.add(hmeEquipment);
                } else {
                    insertList.add(hmeEquipment);
                }
            }
        });
        // 批量新增或者更新
        if (CollectionUtils.isNotEmpty(insertList)) {
            hmeEquipmentRepository.batchInsertSelective(insertList);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            updateList.forEach(hmeEquipmentMapper::updateByPrimaryKeySelective);
        }
        if (CollectionUtils.isNotEmpty(insertHisList)) {
            hmeEquipmentHisRepository.batchInsertSelective(insertHisList);
        }
    }

}
