package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.api.dto.HmeProcessNcHeaderDTO;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.domain.vo.HmeProcessNcVO;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO2;
import com.ruike.hme.infra.mapper.HmeProcessNcHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.repository.HmeProcessNcHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工序不良头表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Component
public class HmeProcessNcHeaderRepositoryImpl extends BaseRepositoryImpl<HmeProcessNcHeader> implements HmeProcessNcHeaderRepository {

    @Autowired
    private HmeProcessNcHeaderMapper hmeProcessNcHeaderMapper;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<HmeProcessNcHeaderVO> selectProcessHeader(Long tenantId, PageRequest pageRequest, HmeProcessNcHeaderDTO hmeProcessNcHeaderDTO) {
        Page<HmeProcessNcHeaderVO> page = PageHelper.doPage(pageRequest, () -> hmeProcessNcHeaderMapper.selectProcessHeader(tenantId, hmeProcessNcHeaderDTO));
        // 查询状态信息
        List<MtGenType> mtGenTypeList = mtGenTypeRepository.select(new MtGenType() {{
            setTenantId(tenantId);
            setModule("GENERAL");
            setTypeGroup("TAG_GROUP_STATUS");
        }});
        for (HmeProcessNcHeaderVO hmeProcessNcHeaderVO : page.getContent()) {
            Optional<MtGenType> typeOpt = mtGenTypeList.stream().filter(tagGroupStatus -> StringUtils.equals(tagGroupStatus.getTypeCode(), hmeProcessNcHeaderVO.getStatus())).findFirst();
            hmeProcessNcHeaderVO.setStatusMeaning(typeOpt.isPresent() ? typeOpt.get().getDescription() : "");
        }
        return page;
    }

    @Override
    public List<HmeProcessNcVO> processNcExport(Long tenantId, HmeProcessNcHeaderDTO dto) {
        List<HmeProcessNcVO> resultList = hmeProcessNcHeaderMapper.processNcExport(tenantId, dto);
        if(CollectionUtil.isNotEmpty(resultList)){
            // 查询状态信息
            List<MtGenType> mtGenTypeList = mtGenTypeRepository.select(new MtGenType() {{
                setTenantId(tenantId);
                setModule("GENERAL");
                setTypeGroup("TAG_GROUP_STATUS");
            }});
            for (HmeProcessNcVO hmeProcessNcVO:resultList) {
                Optional<MtGenType> typeOpt = mtGenTypeList.stream().filter(tagGroupStatus -> StringUtils.equals(tagGroupStatus.getTypeCode(), hmeProcessNcVO.getStatus())).findFirst();
                hmeProcessNcVO.setStatusMeaning(typeOpt.isPresent() ? typeOpt.get().getDescription() : "");
            }
        }
        return resultList;
    }

    /**
     *
     * @Description 查询工序不良头行明细信息
     *
     * @author yuchao.wang
     * @date 2021/1/22 17:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialId 物料ID
     * @param productCode 产品编码
     * @param cosModel cos类型
     * @param chipCombination 芯片组合
     * @return com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2
     *
     */
    @Override
    public HmeProcessNcHeaderVO2 queryProcessNcInfoForNcRecordValidate(Long tenantId, String operationId, String materialId, String productCode, String cosModel, String chipCombination) {
        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderMapper
                .queryProcessNcInfoForNcRecordValidate(tenantId, operationId, materialId, productCode, cosModel,"");
        if (CollectionUtils.isNotEmpty(processNcInfoList)) {
            List<HmeProcessNcHeaderVO2> processNcInfoList2 = processNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getChipCombination())
                    && item.getChipCombination().equals(chipCombination)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(processNcInfoList2)){
                return processNcInfoList2.get(0);
            }
            processNcInfoList2 = processNcInfoList.stream().filter(item -> StringUtils.isBlank(item.getChipCombination()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(processNcInfoList2)){
                return processNcInfoList2.get(0);
            }
            return null;
        }
        return null;
    }

    /**
     *
     * @Description 批量查询工序不良头行明细信息 key:materialId+productCode+cosModel
     *
     * @author yuchao.wang
     * @date 2021/1/25 18:52
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    @Override
    public Map<String, HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForNcRecordValidate(Long tenantId, String operationId, List<String> materialIdList) {
        Map<String, HmeProcessNcHeaderVO2> processNcInfoMap = new HashMap<String, HmeProcessNcHeaderVO2>();
        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderMapper
                .batchQueryProcessNcInfoForNcRecordValidate(tenantId, operationId, materialIdList);
        if (CollectionUtils.isNotEmpty(processNcInfoList)) {
            processNcInfoList.forEach(item ->
                    processNcInfoMap.put(item.getMaterialId() + "," + item.getProductCode() + "," + item.getCosModel() + "," + item.getChipCombination(), item));
        }
        return processNcInfoMap;
    }

    /**
     *
     * @Description 查询工序不良头行明细信息-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 11:28
     * @param tenantId 租户ID
     * @param materialId 产品ID
     * @param stationId 工序ID
     * @param cosModel 芯片类型
     * @param materialLotCode 出站SN
     * @return com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2
     *
     */
    @Override
    public HmeProcessNcHeaderVO2 queryProcessNcInfoForAgeingNcRecordValidate(Long tenantId, String materialId, String stationId, String cosModel, String materialLotCode, String operationId) {
        //V20210607 modify by penglin.sui for peng.zhao 先根据【material_id】查，若有多笔：则根据【material_id】+【cos_model】，若唯一则取此数据；若有多笔，继续根据【material_id】+【cos_model】+【workcell_id】查，若唯一则取此数据，若多比，则报错HME_EO_JOB_SN_202
        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderMapper
                .queryProcessNcInfoForAgeingNcRecordValidate(tenantId, materialId, "", "", operationId);

        if (CollectionUtils.isNotEmpty(processNcInfoList)) {

            List<String> headIdList = processNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

            if(headIdList.size() > 1) {

                processNcInfoList = processNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getCosModel()) && item.getCosModel().equals(cosModel)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(processNcInfoList)){
                    // 	此SN【${1}】查询到多条判定标准,请检查!
                    throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME", materialLotCode));
                }

                headIdList = processNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                if (headIdList.size() > 1) {
                    processNcInfoList = processNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getStationId()) && item.getStationId().equals(stationId)).collect(Collectors.toList());

                    if(CollectionUtils.isNotEmpty(processNcInfoList)){
                        headIdList = processNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());
                    }

                    if (CollectionUtils.isEmpty(processNcInfoList) || headIdList.size() > 1) {
                        // 	此SN【${1}】查询到多条判定标准,请检查!
                        throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME", materialLotCode));
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(processNcInfoList)) {
            return processNcInfoList.get(0);
        }
        return null;
    }

    /**
     *
     * @Description 批量查询工序不良头行明细信息-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 14:30
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    @Override
    public List<HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForAgeingNcRecordValidate(Long tenantId, String operationId, List<String> materialIdList) {
        Map<String, HmeProcessNcHeaderVO2> processNcInfoMap = new HashMap<String, HmeProcessNcHeaderVO2>();
        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderMapper
                .batchQueryProcessNcInfoForAgeingNcRecordValidate(tenantId, operationId,materialIdList);
        return processNcInfoList;
    }

    @Override
    public HmeProcessNcHeaderVO2 queryProcessNcInfoForReflectorNcRecordValidate(Long tenantId, String operationId, String materialId, String cosModel, String chipCombination) {
        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderMapper
                .queryProcessNcInfoForReflectorNcRecordValidate(tenantId, operationId, materialId, cosModel,"");
        if (CollectionUtils.isNotEmpty(processNcInfoList)) {
            List<HmeProcessNcHeaderVO2> processNcInfoList2 = processNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getChipCombination())
                    && item.getChipCombination().equals(chipCombination)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(processNcInfoList2)){
                return processNcInfoList2.get(0);
            }
            processNcInfoList2 = processNcInfoList.stream().filter(item -> StringUtils.isBlank(item.getChipCombination()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(processNcInfoList2)){
                return processNcInfoList2.get(0);
            }
            return null;
        }
        return null;
    }
}
