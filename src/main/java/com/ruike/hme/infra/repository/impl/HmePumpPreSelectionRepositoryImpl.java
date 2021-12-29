package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmePumpPreSelectionDTO4;
import com.ruike.hme.api.dto.HmePumpPreSelectionDTO5;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmePumpSelectionDetails;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmePumpPreSelectionMapper;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmePumpPreSelection;
import com.ruike.hme.domain.repository.HmePumpPreSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.vo.MtBomVO3;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 泵浦源预筛选基础表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@Component
@Slf4j
public class HmePumpPreSelectionRepositoryImpl extends BaseRepositoryImpl<HmePumpPreSelection> implements HmePumpPreSelectionRepository {

    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private HmePumpPreSelectionMapper hmePumpPreSelectionMapper;
    @Autowired
    private MtBomRepository mtBomRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtContainerMapper mtContainerMapper;

    @Override
    public List<String> getMaterialLotByContainer(Long tenantId, MtContainer mtContainer, String defaultStorageLocatorId) {
        //容器可用性校验
        mtContainerRepository.containerAvailableValidate(tenantId, mtContainer.getContainerId());
        //容器库位与当前工位的工段的默认存储库位是否一致
        if (!defaultStorageLocatorId.equals(mtContainer.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtContainer.getLocatorId());
            throw new MtException("HME_PUMP_SELECTION_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_007", "HME", mtModLocator.getLocatorCode()));
        }
        //调用API{containerLimitMaterialLotQuery}
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(mtContainer.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        List<MtContLoadDtlVO4> mtContLoadDtlVO4List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);
        if (CollectionUtils.isEmpty(mtContLoadDtlVO4List)) {
            throw new MtException("HME_PUMP_SELECTION_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_001", "HME"));
        }
        List<String> materialLotIdList = mtContLoadDtlVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        return materialLotIdList;
    }

    @Override
    public HmePumpPreSelectionVO6 pumpPreSelectionConfirmBarcodeVerify(Long tenantId, HmePumpPreSelectionDTO4 dto) {
        List<String> materialLotCodeList = dto.getMaterialLotCodeList();
        //根据条码查询条码信息
        List<HmePumpPreSelectionVO4> materialLotList = hmePumpPreSelectionMapper.materialLotInfoQueryByCode(tenantId, materialLotCodeList);
        //如果有条码的有效性不为Y则报错
        List<HmePumpPreSelectionVO4> enableFlagErrorList = materialLotList.stream().filter(item ->
                !HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(enableFlagErrorList)) {
            throw new MtException("MT_MATERIAL_TFR_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0003", "HME", enableFlagErrorList.get(0).getMaterialLotCode()));
        }
        //如果有条码的质量状态不为OK则报错
        List<HmePumpPreSelectionVO4> qualityStatusErrorList = materialLotList.stream().filter(item ->
                !HmeConstants.ConstantValue.OK.equals(item.getQualityStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(qualityStatusErrorList)) {
            throw new MtException("HME_PUMP_SELECTION_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_011", "HME", qualityStatusErrorList.get(0).getMaterialLotCode()));
        }
        //如果有条码的货位不为登录工位的上层工段下的默认存储库位则报错
        List<HmePumpPreSelectionVO4> locatorErrorList = materialLotList.stream().filter(item ->
                !dto.getDefaultStorageLocatorId().equals(item.getLocatorId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(locatorErrorList)) {
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorErrorList.get(0).getLocatorId());
            throw new MtException("HME_PUMP_SELECTION_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_012", "HME", locatorErrorList.get(0).getMaterialLotCode(), mtModLocator.getLocatorCode()));
        }
        //条码是否已装载校验
        List<String> materialLotIdList = materialLotList.stream().map(HmePumpPreSelectionVO4::getMaterialLotId).collect(Collectors.toList());
        String errorMaterialLotCode = hmePumpPreSelectionMapper.materialLotLoadedQuery2(tenantId, materialLotIdList);
        if (StringUtils.isNotBlank(errorMaterialLotCode)) {
            throw new MtException("HME_PUMP_SELECTION_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_0015", "HME", errorMaterialLotCode));
        }
        List<HmePumpPreSelectionVO4> materialLotContainerList = new ArrayList<>();
        for (HmePumpPreSelectionVO4 materialLot : materialLotList) {
            //调用API{objectLimitLoadingContainerQuery}查询条码是否装载在容器中，如果在容器中则先卸载
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
            mtContLoadDtlVO5.setLoadObjectId(materialLot.getMaterialLotId());
            mtContLoadDtlVO5.setTopLevelFlag(HmeConstants.ConstantValue.YES);
            List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
            if (CollectionUtils.isNotEmpty(containerIdList)) {
                MtContainer mtContainer = mtContainerRepository.containerPropertyGet(tenantId, containerIdList.get(0));
                //校验现装载的容器的货位是否为登录工位的上层工段下的默认存储库位
                if (!mtContainer.getLocatorId().equals(dto.getDefaultStorageLocatorId())) {
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtContainer.getLocatorId());
                    throw new MtException("HME_PUMP_SELECTION_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_PUMP_SELECTION_013", "HME", materialLot.getMaterialLotCode(), mtContainer.getContainerCode(), mtModLocator.getLocatorCode()));
                }
                HmePumpPreSelectionVO4 hmePumpPreSelectionVO4 = new HmePumpPreSelectionVO4();
                hmePumpPreSelectionVO4.setMaterialLotId(materialLot.getMaterialLotId());
                hmePumpPreSelectionVO4.setContainerId(mtContainer.getContainerId());
                materialLotContainerList.add(hmePumpPreSelectionVO4);
            }
        }
        HmePumpPreSelectionVO6 result = new HmePumpPreSelectionVO6();
        result.setContainerUnloadList(materialLotContainerList);
        result.setMaterialLotIdList(materialLotIdList);
        return result;
    }

    @Override
    public MtContainer pumpPreSelectionConfirmContainerVerify(Long tenantId, HmePumpPreSelectionDTO4 dto,
                                                              List<String> materialLotIdList) {
        MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
            setTenantId(tenantId);
            setContainerCode(dto.getNewContainerCode());
        }});
        //目标容器存在校验
        if (Objects.isNull(mtContainer)) {
            throw new MtException("HME_PUMP_SELECTION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_009", "HME", dto.getNewContainerCode()));
        }
        //2021-09-09 edit by chaonan.hu for wenxin.zhang 在校验容器可用性之前，把容器的货位更新为默认存储货位
        if(!dto.getDefaultStorageLocatorId().equals(mtContainer.getLocatorId())){
            mtContainer.setLocatorId(dto.getDefaultStorageLocatorId());
            mtContainerMapper.updateByPrimaryKeySelective(mtContainer);
        }
        //容器可用性校验
        mtContainerRepository.containerAvailableValidate(tenantId, mtContainer.getContainerId());
        //容器装载验证
        List<MtContainerVO9> mtContainerVO9List = new ArrayList<>();
        for (String materialLotId : materialLotIdList) {
            MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
            mtContainerVO9.setContainerId(mtContainer.getContainerId());
            mtContainerVO9.setLoadObjectType("MATERIAL_LOT");
            mtContainerVO9.setLoadObjectId(materialLotId);
            mtContainerVO9List.add(mtContainerVO9);
        }
        mtContainerRepository.containerLoadBatchVerify(tenantId, mtContainerVO9List);
        //容器是否装载校验
        mtContainerLoadDetailRepository.containerIsEmptyValidate(tenantId, mtContainer.getContainerId());
        return mtContainer;
    }

    @Override
    public HmePumpPreSelectionVO14 getAllMaterialByMainMaterial(Long tenantId, HmePumpPreSelectionDTO5 dto) {
        List<String> materialIdList = new ArrayList<>();
        //根据物料编码、版本号查询唯一的bomId
        String bomId = hmePumpPreSelectionMapper.getBomIdByMaterialRevision(tenantId, dto);
        if (StringUtils.isBlank(bomId)) {
            //如果找不到bom，则报错物料【${1}】找不到版本号【${2}】的装配清单
            throw new MtException("HME_PUMP_SELECTION_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_020", "HME", dto.getMaterialCode(), dto.getRevision()));
        }
        //调用API{bomAvailableVerify}校验BOM可用性
        MtBomVO3 mtBomVO3 = new MtBomVO3();
        mtBomVO3.setBomId(bomId);
        mtBomVO3.setBomType("MATERIAL");
        mtBomVO3.setSiteId(dto.getSiteId());
        mtBomRepository.bomAvailableVerify(tenantId, mtBomVO3);
        //根据bomId查询当前时间在生效时间和失效时间之内的Bom组件物料
        List<String> bomComponentMaterialIdList = hmePumpPreSelectionMapper.getBomComponentMaterialByBomId(tenantId, bomId);
        if (CollectionUtils.isEmpty(bomComponentMaterialIdList)) {
            //如果找不到Bom组件物料，则报错物料【${1}】的装配清单【${2}】版本号不存在有效组件行
            throw new MtException("HME_PUMP_SELECTION_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_015", "HME", dto.getMaterialCode(), dto.getRevision()));
        }
        materialIdList.addAll(bomComponentMaterialIdList);
        //根据Bom组件物料集合查询替代组
        List<WmsMaterialSubstituteRel> subStituteGroupList = hmePumpPreSelectionMapper.getSubStituteGroupByMainMaterial(tenantId, bomComponentMaterialIdList);
        if (CollectionUtils.isNotEmpty(subStituteGroupList)) {
            //对上一步查到的替代组集合根据主物料分组，如果主物料下找到多个替代组则报错当前全局替代料【${1】存在多个替代组,请检查。
            Map<String, List<WmsMaterialSubstituteRel>> mainMaterialMap = subStituteGroupList.stream().collect(Collectors.groupingBy(WmsMaterialSubstituteRel::getMainMaterialId));
            for (Map.Entry<String, List<WmsMaterialSubstituteRel>> entry : mainMaterialMap.entrySet()) {
                List<WmsMaterialSubstituteRel> valueList = entry.getValue().stream().distinct().collect(Collectors.toList());
                if (valueList.size() > 1) {
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(entry.getKey());
                    throw new MtException("HME_PUMP_SELECTION_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_PUMP_SELECTION_016", "HME", mtMaterial.getMaterialCode()));
                }
            }
            //根据替代组查询组下的全局替代料
            List<String> substituteGroupIdList = subStituteGroupList.stream().map(WmsMaterialSubstituteRel::getSubstituteGroup).distinct().collect(Collectors.toList());
            List<String> subStituteMaterial = hmePumpPreSelectionMapper.getMaterialBySubStituteGroup(tenantId, substituteGroupIdList);
            materialIdList.addAll(subStituteMaterial);
        }
        materialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
        HmePumpPreSelectionVO14 result = new HmePumpPreSelectionVO14();
        result.setBomId(bomId);
        result.setMaterialId(materialIdList);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO8 eoJobDataRecordVerify(Long tenantId, List<String> materialLotIdList,
                                                        List<HmePumpPreSelectionVO7> pumpFilterRuleLineList, long qty) {
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        //查询值集HME_PUMP_SOURCE_WKC中维护的值-工艺编码
        List<LovValueDTO> pumpSourceWkcLov = lovAdapter.queryLovValue("HME_PUMP_SOURCE_WKC", tenantId);
        if (CollectionUtils.isNotEmpty(pumpSourceWkcLov)) {
            List<String> valueList = pumpSourceWkcLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(valueList)) {
                //根据工艺编码查询工位
                List<String> workcellIdList = hmePumpPreSelectionMapper.getWorkcellByOperation(tenantId, valueList);
                if (CollectionUtils.isNotEmpty(workcellIdList)) {
                    //根据物料批、工位查询已出站数据
                    hmeEoJobSnList = hmePumpPreSelectionMapper.eoJobSnDataQuery(tenantId, materialLotIdList, workcellIdList);
                }
            }
        }
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            //如果根据筛选池中的条码未找到已出站数据，则报错
            throw new MtException("HME_PUMP_SELECTION_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_021", "HME"));
        }
        List<String> jobIdList = new ArrayList<>();
        //materialLotJobMap存储的是此时筛选池中的条码一一对应的jobId，key为jobId value为物料批Id
        Map<String, String> materialLotJobMap = new HashMap<>();
        //已出站数据根据物料批分组，如果一个物料批下对应多个jobId，则取更新时间最近的一条
        Map<String, List<HmeEoJobSn>> eoJonSnMap = hmeEoJobSnList.stream().collect(Collectors.groupingBy(HmeEoJobSn::getMaterialLotId));
        for (Map.Entry<String, List<HmeEoJobSn>> entry : eoJonSnMap.entrySet()) {
            List<HmeEoJobSn> singleEoJobSnList = entry.getValue();
            if (singleEoJobSnList.size() > 1) {
                singleEoJobSnList = singleEoJobSnList.stream().sorted(Comparator.comparing(HmeEoJobSn::getLastUpdateDate).reversed()).collect(Collectors.toList());
            }
            jobIdList.add(singleEoJobSnList.get(0).getJobId());
            materialLotJobMap.put(singleEoJobSnList.get(0).getJobId(), entry.getKey());
        }
        List<String> tagIdList = new ArrayList<>();
        //获取筛选规则行中类型为MULTIPLE、SINGLE、SUM的筛选规则行的tagId
        List<String> calculateTypeList = new ArrayList<>();
        calculateTypeList.add("MULTIPLE");
        calculateTypeList.add("SINGLE");
        calculateTypeList.add("SUM");
        List<HmePumpPreSelectionVO7> pumpFilterRuleLineList2 = pumpFilterRuleLineList.stream().filter(item ->
                calculateTypeList.contains(item.getCalculateType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(pumpFilterRuleLineList2)) {
            tagIdList = pumpFilterRuleLineList2.stream().map(HmePumpPreSelectionVO7::getTagId).distinct().collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(tagIdList)) {
            //如果找不到筛选规则行的tagId,则报错匹配筛选规则数据项的条码数不足,本次筛选无结果
            throw new MtException("HME_PUMP_SELECTION_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_018", "HME"));
        }
        materialLotIdList = new ArrayList<>();
        Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap = new HashMap<>();
        List<HmePumpPreSelectionVO11> materialLotTagResultList = new ArrayList<>();
        //根据jobId、tagId查询结果不为空的数据采集项,查询结果并根据jobId分组
        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmePumpPreSelectionMapper.eoJobDataRecordQuery(tenantId, jobIdList, tagIdList);
        if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
            List<HmePumpPreSelectionVO9> eoJobDataRecordList = new ArrayList<>();
            try {
                for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList) {
                    HmePumpPreSelectionVO9 hmePumpPreSelectionVO9 = new HmePumpPreSelectionVO9();
                    hmePumpPreSelectionVO9.setJobId(hmeEoJobDataRecord.getJobId());
                    hmePumpPreSelectionVO9.setTagId(hmeEoJobDataRecord.getTagId());
                    hmePumpPreSelectionVO9.setResult(new BigDecimal(hmeEoJobDataRecord.getResult()));
                    eoJobDataRecordList.add(hmePumpPreSelectionVO9);
                }
            } catch (Exception ex) {
                throw new MtException("HME_PUMP_SELECTION_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_022", "HME"));
            }
            //eoJobDataRecordMap存储的是jobId所对应的数据采集项
            Map<String, List<HmePumpPreSelectionVO9>> eoJobDataRecordMap = eoJobDataRecordList.stream().collect(Collectors.groupingBy(HmePumpPreSelectionVO9::getJobId));
            for (Map.Entry<String, List<HmePumpPreSelectionVO9>> entry : eoJobDataRecordMap.entrySet()) {
                String jobId = entry.getKey();
                List<HmePumpPreSelectionVO9> singleEoJobDataRecordList = entry.getValue();
                List<String> singleTagIdList = singleEoJobDataRecordList.stream().map(HmePumpPreSelectionVO9::getTagId).distinct().collect(Collectors.toList());
                //只用当每个jobId下查询到的tagId集合与上面筛选规则行的tagId集合一致时，才代表jobId通过校验，进而jobId背后的条码通过校验
                if (singleTagIdList.size() == tagIdList.size()) {
                    String materialLotId = materialLotJobMap.get(jobId);
                    materialLotIdList.add(materialLotId);
                    materialLotJobDataRecordMap.put(materialLotId, singleEoJobDataRecordList);
                    for (HmePumpPreSelectionVO9 hmePumpPreSelectionVO9 : singleEoJobDataRecordList) {
                        HmePumpPreSelectionVO11 hmePumpPreSelectionVO11 = new HmePumpPreSelectionVO11();
                        hmePumpPreSelectionVO11.setMaterialLotId(materialLotId);
                        hmePumpPreSelectionVO11.setTagId(hmePumpPreSelectionVO9.getTagId());
                        hmePumpPreSelectionVO11.setResult(hmePumpPreSelectionVO9.getResult());
                        materialLotTagResultList.add(hmePumpPreSelectionVO11);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(materialLotIdList) || materialLotIdList.size() < qty) {
            //如果最终在筛选池中没找到符合要求的条码则报错匹配筛选规则数据项的条码数不足,本次筛选无结果
            throw new MtException("HME_PUMP_SELECTION_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_018", "HME"));
        }
        HmePumpPreSelectionVO8 result = new HmePumpPreSelectionVO8();
        result.setMaterialLotIdList(materialLotIdList);
        result.setMaterialLotJobDataRecordMap(materialLotJobDataRecordMap);
        result.setMaterialLotTagResultList(materialLotTagResultList);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO13 pumpFilterRuleLineQuery(Long tenantId, String materialId) {
        HmePumpPreSelectionVO13 result = new HmePumpPreSelectionVO13();
        List<HmePumpPreSelectionVO7> pumpFilterRuleLineList = hmePumpPreSelectionMapper.getPumpFilterRuleLineByMaterial(tenantId, materialId);
        if (CollectionUtils.isEmpty(pumpFilterRuleLineList)) {
            //如果找不到则报错筛选规则没有维护筛选条件
            throw new MtException("HME_EO_JOB_SN_225", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_225", "HME"));
        }
        //2021-10-25 14:31 edit by chaonan.hu for hui.gu 如果找到的筛选规则行的最小值或最大值为空，则赋值为值集维护的默认值
        List<HmePumpPreSelectionVO7> minMaxValueNullRuleLineList = pumpFilterRuleLineList.stream().filter(item -> Objects.isNull(item.getMinValue()) || Objects.isNull(item.getMaxValue())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(minMaxValueNullRuleLineList)){
            List<LovValueDTO> pumpMaxMinDefaultValueList = lovAdapter.queryLovValue("HME.PUMP_MAX_MIN_DEFAULT_VALUE", tenantId);
            List<String> minList = pumpMaxMinDefaultValueList.stream().filter(item -> "MIN".equals(item.getValue())).map(LovValueDTO::getMeaning).collect(Collectors.toList());
            BigDecimal min = new BigDecimal(minList.get(0));
            List<String> maxList = pumpMaxMinDefaultValueList.stream().filter(item -> "MAX".equals(item.getValue())).map(LovValueDTO::getMeaning).collect(Collectors.toList());
            BigDecimal max = new BigDecimal(maxList.get(0));
            for (HmePumpPreSelectionVO7 minMaxValueNullRuleLine:minMaxValueNullRuleLineList) {
                if(Objects.isNull(minMaxValueNullRuleLine.getMinValue())){
                    minMaxValueNullRuleLine.setMinValue(min);
                }
                if(Objects.isNull(minMaxValueNullRuleLine.getMaxValue())){
                    minMaxValueNullRuleLine.setMaxValue(max);
                }
            }
        }
        //如果筛选规则行中存在计算类型为SINGLE的行,则需保证这些行的tagId必须是同一个，且行的个数要和筛选规则头的泵浦源个数相等
        List<HmePumpPreSelectionVO7> singleList = pumpFilterRuleLineList.stream().filter(item -> "SINGLE".equals(item.getCalculateType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(singleList)) {
            BigDecimal qty = pumpFilterRuleLineList.get(0).getQty();
            if (singleList.size() != qty.longValue()) {
                //此物料筛选规则行下,计算类型为【单个符合】行数与泵浦源需求数不相等,请检查
                throw new MtException("HME_PUMP_SELECTION_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_024", "HME"));
            }
            List<String> tagIdList = singleList.stream().map(HmePumpPreSelectionVO7::getTagId).distinct().collect(Collectors.toList());
            if (tagIdList.size() > 1) {
                //此物料筛选规则行下,计算类型为【单个符合】存在多个不同的数据项,请检查
                throw new MtException("HME_PUMP_SELECTION_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_025", "HME"));
            }
        }
        List<HmePumpPreSelectionVO7> sumList = pumpFilterRuleLineList.stream().filter(item -> "SUM".equals(item.getCalculateType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sumList)) {
            //报错筛选规则中缺少“求和”类型的数据项
            throw new MtException("HME_PUMP_SELECTION_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_030", "HME"));
        }
        sumList = sumList.stream().sorted(Comparator.comparing(HmePumpPreSelectionVO7::getSequence)).collect(Collectors.toList());
        //如果Sequence最小的SUM计算类型的筛选规则行的Priority不为ASCEND或DESCEND,则报错
        if (!"ASCEND".equals(sumList.get(0).getPriority()) && !"DESCEND".equals(sumList.get(0).getPriority())) {
            MtTag mtTag = mtTagRepository.selectByPrimaryKey(sumList.get(0).getTagId());
            throw new MtException("HME_PUMP_SELECTION_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PUMP_SELECTION_010", "HME", mtTag.getTagCode()));
        }
        //查询筛选规则行中是否存在计算类型为CALCULATION、参数代码为WPE的行
        List<HmePumpPreSelectionVO7> wpeList = pumpFilterRuleLineList.stream().filter(item -> "CALCULATION".equals(item.getCalculateType())
                                                            && "WPE".equals(item.getParameterCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(wpeList)) {
            //letterTagIdMap WPE类型计算公式的字母与SUM类型数据项的对应关系 用于算法最后校验WPE
            Map<String, String> letterTagIdMap = new HashMap<>();
            HmePumpPreSelectionVO7 wpeLine = wpeList.get(0);
            result.setWpeLine(wpeLine);
            //获取计算公式中不同的字母
            String formula = wpeLine.getFormula();
            List<String> letterList = new ArrayList<>();
            for (int i = 0; i < formula.length(); i++) {
                char letter = formula.charAt(i);
                if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')) {
                    letterList.add(String.valueOf(letter));
                }
            }
            letterList = letterList.stream().distinct().collect(Collectors.toList());
            //根据每个字母作为参数代码、计算类型为SUM查到到对应唯一的tagId
            for (String letter : letterList) {
                List<HmePumpPreSelectionVO7> letterSumList = sumList.stream().filter(item -> letter.equals(item.getParameterCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(letterSumList)) {
                    //如果找不到，则报错计算公式【${1}】找不到参数代码【${2}】
                    throw new MtException("HME_EO_JOB_SN_229", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_229", "HME", formula, letter));
                }
                letterTagIdMap.put(letter, letterSumList.get(0).getTagId());
            }
            result.setLetterTagIdMap(letterTagIdMap);
        }
        //返回结果
        result.setPumpFilterRuleLineList(pumpFilterRuleLineList);
        return result;
    }

    @Override
    public HmePumpPreSelectionVO8 multipleSinglePumpFilterRuleLineVerify(Long tenantId, List<String> materialLotIdList,
                                                                         List<HmePumpPreSelectionVO7> pumpFilterRuleLineList,
                                                                         Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap,
                                                                         Long qty) {
        List<HmePumpPreSelectionVO7> multipleList = pumpFilterRuleLineList.stream().filter(item -> "MULTIPLE".equals(item.getCalculateType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(multipleList)) {
            //errorMaterialLotIdList记录未通过校验的物料批ID
            List<String> errorMaterialLotIdList = new ArrayList<>();
            for (String materialLotId : materialLotIdList) {
                List<HmePumpPreSelectionVO9> hmeEoJobDataRecordList = materialLotJobDataRecordMap.get(materialLotId);
                Map<String, List<HmePumpPreSelectionVO9>> tagJobDataRecordMap = hmeEoJobDataRecordList.stream().collect(Collectors.groupingBy(HmePumpPreSelectionVO9::getTagId));
                for (HmePumpPreSelectionVO7 hmePumpPreSelectionVO7 : multipleList) {
                    boolean continueFlag = false;
                    String tagId = hmePumpPreSelectionVO7.getTagId();
                    //正常情况下这里拿到的tagJobDataRecordList里只会有一条数据，因为是根据条码(实际上是其所对应的唯一jobId)+tagId查的数据采集项
                    List<HmePumpPreSelectionVO9> tagJobDataRecordList = tagJobDataRecordMap.get(tagId);
                    for (HmePumpPreSelectionVO9 tagJobDataRecord : tagJobDataRecordList) {
                        BigDecimal result = tagJobDataRecord.getResult();
                        if (result.compareTo(hmePumpPreSelectionVO7.getMinValue()) < 0 || result.compareTo(hmePumpPreSelectionVO7.getMaxValue()) > 0) {
                            errorMaterialLotIdList.add(materialLotId);
                            continueFlag = true;
                            continue;
                        }
                    }
                    if (continueFlag) {
                        continue;
                    }
                }
            }
            materialLotIdList.removeAll(errorMaterialLotIdList);
            if (CollectionUtils.isEmpty(materialLotIdList) || materialLotIdList.size() < qty) {
                //如果经过MULTIPLE类型筛选规则行校验后，筛选池中无条码则报错类型为【多个符合】的条码数不足,本次筛选无结果
                throw new MtException("HME_PUMP_SELECTION_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_019", "HME"));
            }
        }
        List<HmePumpPreSelectionVO7> singleList = pumpFilterRuleLineList.stream().filter(item -> "SINGLE".equals(item.getCalculateType())).collect(Collectors.toList());
        List<List<String>> materialLotGroupList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(singleList)) {
            //hmePumpPreSelectionVO10List存储的是通过SINGLE类型校验的条码是落在哪个筛选规则行的
            List<HmePumpPreSelectionVO10> hmePumpPreSelectionVO10List = new ArrayList<>();
            //这里的tagIdList在前面获取筛选规则行时已校验过，保证都是同一个tagId
            List<String> tagIdList = singleList.stream().map(HmePumpPreSelectionVO7::getTagId).distinct().collect(Collectors.toList());
            //errorMaterialLotIdList记录未通过校验的物料批ID
            List<String> errorMaterialLotIdList = new ArrayList<>();
            for (String materialLotId : materialLotIdList) {
                //根据物料批ID拿到对应的数据采集项
                List<HmePumpPreSelectionVO9> hmeEoJobDataRecordList = materialLotJobDataRecordMap.get(materialLotId);
                /*
                 *  从数据采集项中筛选次tagId的采集项数据，只要采集项数据的result存在任意一个筛选规则行的范围之内，此result即可合格
                 *  只用当条码下的所有result合格时，此条码才通过校验
                 *  正常情况下这里拿到的singleEoJobDataRecord里只会有一条数据，因为是根据条码(实际上是其所对应的唯一jobId)+tagId查的数据采集项
                 */
                List<HmePumpPreSelectionVO9> singleEoJobDataRecord = hmeEoJobDataRecordList.stream().filter(item -> tagIdList.get(0).equals(item.getTagId())).collect(Collectors.toList());
                int resultSuccessCount = 0;
                for (HmePumpPreSelectionVO9 hmePumpPreSelectionVO9 : singleEoJobDataRecord) {
                    BigDecimal result = hmePumpPreSelectionVO9.getResult();
                    for (HmePumpPreSelectionVO7 hmePumpPreSelectionVO7 : singleList) {
                        if (result.compareTo(hmePumpPreSelectionVO7.getMinValue()) >= 0
                                && result.compareTo(hmePumpPreSelectionVO7.getMaxValue()) <= 0) {
                            resultSuccessCount++;
                            HmePumpPreSelectionVO10 hmePumpPreSelectionVO10 = new HmePumpPreSelectionVO10();
                            hmePumpPreSelectionVO10.setMaterialLotId(materialLotId);
                            hmePumpPreSelectionVO10.setRuleLineId(hmePumpPreSelectionVO7.getRuleLineId());
                            hmePumpPreSelectionVO10List.add(hmePumpPreSelectionVO10);
                            break;
                        }
                    }
                }
                if (resultSuccessCount != singleEoJobDataRecord.size()) {
                    errorMaterialLotIdList.add(materialLotId);
                }
            }
            materialLotIdList.removeAll(errorMaterialLotIdList);
            if (CollectionUtils.isEmpty(materialLotIdList) || materialLotIdList.size() < qty) {
                //如果经过SINGLE类型筛选规则行校验后，筛选池中无条码则报错类型为【单个符合】的条码数不足,本次筛选无结果
                throw new MtException("HME_PUMP_SELECTION_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_023", "HME"));
            }
            if (hmePumpPreSelectionVO10List.size() != materialLotIdList.size()) {
                //如果不等则报错同一作业记录里存在多个相同的数据采集项,请检查
                throw new MtException("HME_PUMP_SELECTION_027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_027", "HME"));
            }
            //获取条码属于哪个子集中，如果最终子集的个数小于泵浦源个数则报错
            Map<String, List<HmePumpPreSelectionVO10>> ruleLineMaterialLotMap = hmePumpPreSelectionVO10List.stream().collect(Collectors.groupingBy(HmePumpPreSelectionVO10::getRuleLineId));
            for (Map.Entry<String, List<HmePumpPreSelectionVO10>> singleRuleLineMaterialLot : ruleLineMaterialLotMap.entrySet()) {
                materialLotGroupList.add(singleRuleLineMaterialLot.getValue().stream().map(HmePumpPreSelectionVO10::getMaterialLotId).collect(Collectors.toList()));
            }
            if(CollectionUtils.isEmpty(materialLotGroupList) || materialLotGroupList.size() < qty){
                //存在【单个符合】范围区间内的条码数为零,本次筛选无结果；
                throw new MtException("HME_PUMP_SELECTION_032 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_SELECTION_032 ", "HME"));
            }
        }
        HmePumpPreSelectionVO8 result = new HmePumpPreSelectionVO8();
        result.setMaterialLotIdList(materialLotIdList);
        result.setMaterialLotGroupList(materialLotGroupList);
        return result;
    }

    @Override
    public List<List<String>> pumpFilterRuleSumLineAscend(Long tenantId, List<String> materialLotIdList, List<HmePumpPreSelectionVO7> sumList,
                                                          BigDecimal qty, long qtyLong, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                                          List<List<String>> materialLotGroupList, Map<String, String> letterTagIdMap, HmePumpPreSelectionVO7 wpeLine) {
        HmePumpPreSelectionVO7 sequenceMinSumLine = sumList.get(0);
        String tagId = sequenceMinSumLine.getTagId();
        BigDecimal minValue = sequenceMinSumLine.getMinValue();
        //minAverageValue为minValue除以筛选规则头的泵浦源个数，四舍五入 保留6位小数
        BigDecimal minAverageValue = minValue.divide(qty, 6, RoundingMode.HALF_UP);
        /*
         * 根据目前筛选池中的条码+最小行的tagId取到result,并计算出result-minAverageValue的差值
         * 结果记录在集合filterPoolMaterialLotSumTagResultDiffMinList中
         * 因为做while循环时只会减去条码，所以此时我获取到前筛选池中的条码的result与minAverageValue的差值，下次循环时直接从集合中取即可
         */
        List<HmePumpPreSelectionVO11> filterPoolMaterialLotSumTagResultList = materialLotTagResultList.stream()
                .filter(item -> item.getTagId().equals(tagId)
                        && materialLotIdList.contains(item.getMaterialLotId()))
                .collect(Collectors.toList());
        List<HmePumpPreSelectionVO12> filterPoolMaterialLotSumTagResultDiffMinList = new ArrayList<>();
        for (HmePumpPreSelectionVO11 hmePumpPreSelectionVO11 : filterPoolMaterialLotSumTagResultList) {
            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = new HmePumpPreSelectionVO12();
            hmePumpPreSelectionVO12.setMaterialLotId(hmePumpPreSelectionVO11.getMaterialLotId());
            hmePumpPreSelectionVO12.setResult(hmePumpPreSelectionVO11.getResult());
            hmePumpPreSelectionVO12.setDiffMinAverageValue(hmePumpPreSelectionVO11.getResult().subtract(minAverageValue));
            filterPoolMaterialLotSumTagResultDiffMinList.add(hmePumpPreSelectionVO12);
        }
        //只要筛选池中还有条码且条码个数大于等于泵浦源个数，循环就继续，循环体内会去掉条码
        List<List<String>> finalMaterialLotList = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(materialLotIdList) && materialLotIdList.size() >= qtyLong ) {
            List<String> currentMaterialLotIdList = new ArrayList<>();
            currentMaterialLotIdList.addAll(materialLotIdList);
            List<List<String>> currentMaterialLotGroupList = materialLotGroupList;
            //取出当前筛选池中条码的result与minAverageValue的差值，并按照差值从小到大的排序，此时差值最小的条码即为此次循环的基点条码
            List<HmePumpPreSelectionVO12> currentPoolMaterialLotSumTagResultDiffMinList = filterPoolMaterialLotSumTagResultDiffMinList.stream()
                    .filter(item -> currentMaterialLotIdList.contains(item.getMaterialLotId()))
                    .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMinAverageValue))
                    .collect(Collectors.toList());
            HmePumpPreSelectionVO12 basicPointPumpPreSelectionVO12 = currentPoolMaterialLotSumTagResultDiffMinList.get(0);
            //basicPointMaterialLotId为基点条码ID
            String basicPointMaterialLotId = basicPointPumpPreSelectionVO12.getMaterialLotId();
            //temporaryMaterialLotList为此次循环要确定的临时条码组合
            List<String> temporaryMaterialLotList = new ArrayList<String>(qty.intValue());
            temporaryMaterialLotList.add(basicPointMaterialLotId);
            //diffMinAverageValueSum 临时条码组合中条码的result与minAverageValue的差值之和
            BigDecimal diffMinAverageValueSum = basicPointPumpPreSelectionVO12.getDiffMinAverageValue();
            List<String> removeMaterialLotIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(currentMaterialLotGroupList)) {
                //如果多个子集集合不为空，则接下来组合其他条码时则不考虑与基点条码同属一个子集下的所有条码
                Iterator<List<String>> materialLotGroupIterator = currentMaterialLotGroupList.iterator();
                while (materialLotGroupIterator.hasNext()) {
                    List<String> next = materialLotGroupIterator.next();
                    if (next.contains(basicPointMaterialLotId)) {
                        removeMaterialLotIdList.addAll(next);
                        break;
                    }
                }
            } else {
                //如果多个子集集合为空,则只不再考虑此条码
                removeMaterialLotIdList.add(basicPointMaterialLotId);
            }
            //n为此次组合中确定的条码个数，当n与泵浦源个数相等时代表此时已找到一个临时条码组合
            long n = temporaryMaterialLotList.size();
            while (n < qtyLong) {
                //注意剩余物料批含义是当前筛选池中所有的条码-removeMaterialLotIdList里的所有条码
                List<String> surplusMaterialLotIdList = currentMaterialLotIdList.stream().filter(item -> !removeMaterialLotIdList.contains(item)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(surplusMaterialLotIdList)){
                    materialLotIdList.remove(basicPointMaterialLotId);
                    n = qtyLong + 1;
                    continue;
                }
                //temporaryMaterialLotId为此次要确定好的临时组合条码
                String temporaryMaterialLotId = null;
                //如果n<泵浦源个数
                if (qtyLong - n > 1) {
                    //如果泵浦源个数-n大于1，说明此时临时条码组合至少还差2个条码
                    if (diffMinAverageValueSum.compareTo(BigDecimal.ZERO) >= 0) {
                        //如果差值和大于等于0则取出剩余物料批中差值最小的物料批，
                        List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMinList.stream()
                                .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId()))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMinAverageValue))
                                .collect(Collectors.toList());
                        HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                        diffMinAverageValueSum = diffMinAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMinAverageValue());
                        temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                        temporaryMaterialLotList.add(temporaryMaterialLotId);
                        n++;
                    } else {
                        //如果差值和小于0则取出的条码需为剩余物料批中当前差值和+本身差值>=0且本身差值最小的物料批
                        BigDecimal currentDiffMinAverageValueSum = diffMinAverageValueSum;
                        List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMinList.stream()
                                .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId())
                                        && (currentDiffMinAverageValueSum.add(item.getDiffMinAverageValue()).compareTo(BigDecimal.ZERO) >= 0))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMinAverageValue))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(hmePumpPreSelectionVO12List)) {
                            //如果可以找到，则直接取就好
                            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                            diffMinAverageValueSum = diffMinAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMinAverageValue());
                            temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                            temporaryMaterialLotList.add(temporaryMaterialLotId);
                        } else {
                            //如果找不到，则取剩余物料批中差值最大的物料批
                            List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List2 = filterPoolMaterialLotSumTagResultDiffMinList.stream()
                                    .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId()))
                                    .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMinAverageValue).reversed())
                                    .collect(Collectors.toList());
                            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List2.get(0);
                            diffMinAverageValueSum = diffMinAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMinAverageValue());
                            temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                            temporaryMaterialLotList.add(temporaryMaterialLotId);
                        }
                        n++;
                    }
                    //新添加的组合条码也要加入到removeMaterialLotIdList
                    if (CollectionUtils.isNotEmpty(currentMaterialLotGroupList)) {
                        //如果多个子集集合不为空，则接下来组合其他条码时则不考虑与基点条码同属一个子集下的所有条码
                        Iterator<List<String>> materialLotGroupIterator = currentMaterialLotGroupList.iterator();
                        while (materialLotGroupIterator.hasNext()) {
                            List<String> next = materialLotGroupIterator.next();
                            if (next.contains(temporaryMaterialLotId)) {
                                removeMaterialLotIdList.addAll(next);
                                break;
                            }
                        }
                    } else {
                        //如果多个子集集合为空,则只不再考虑此条码
                        removeMaterialLotIdList.add(temporaryMaterialLotId);
                    }
                } else {
                    //如果泵浦源个数-n小于等于1，实际上只会等于1，则取出的条码需为剩余物料批中当前差值和+本身差值>=0且本身差值最小的物料批
                    BigDecimal currentDiffMinAverageValueSum = diffMinAverageValueSum;
                    List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMinList.stream()
                            .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId())
                                    && (currentDiffMinAverageValueSum.add(item.getDiffMinAverageValue()).compareTo(BigDecimal.ZERO) >= 0))
                            .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMinAverageValue))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(hmePumpPreSelectionVO12List)) {
                        //如果可以找到，判断加上找到条码的临时条码组合的结果和是否在此tagId下的min_value和max_value中
                        HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                        diffMinAverageValueSum = diffMinAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMinAverageValue());
                        temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                        temporaryMaterialLotList.add(temporaryMaterialLotId);
                        List<String> temporaryMaterialLotListCopy= temporaryMaterialLotList;
                        BigDecimal temporaryResultSum = materialLotTagResultList.stream()
                                .filter(item -> temporaryMaterialLotListCopy.contains(item.getMaterialLotId())
                                        && tagId.equals(item.getTagId()))
                                .collect(CollectorsUtil.summingBigDecimal(HmePumpPreSelectionVO11::getResult));
                        n++;
                        if (temporaryResultSum.compareTo(sequenceMinSumLine.getMinValue()) >= 0
                                && temporaryResultSum.compareTo(sequenceMinSumLine.getMaxValue()) <= 0) {
                            //进入到第10步，进行其他数据项的校验
                            temporaryMaterialLotList = this.temporaryMaterialLotOtherTagDisplace(tenantId, temporaryMaterialLotList,
                                    materialLotGroupList, materialLotTagResultList, sumList, currentMaterialLotIdList);
                            if (CollectionUtils.isNotEmpty(temporaryMaterialLotList) && temporaryMaterialLotList.size() == qtyLong) {
                                //继续进行WPE校验
                                if(Objects.nonNull(wpeLine)){
                                    temporaryMaterialLotList = this.temporaryMaterialLotWpeDisplace(tenantId, temporaryMaterialLotList, materialLotGroupList,
                                            materialLotTagResultList, sumList, currentMaterialLotIdList, wpeLine, letterTagIdMap);
                                }
                                if (CollectionUtils.isNotEmpty(temporaryMaterialLotList) && temporaryMaterialLotList.size() == qtyLong) {
                                    //此时才通过所有校验，找到一套条码组合，破坏内层循环条件，使之退出内层循环，继续外层循环
                                    finalMaterialLotList.add(temporaryMaterialLotList);
                                    materialLotIdList.removeAll(temporaryMaterialLotList);
                                    n = qtyLong + 1;
                                } else {
                                    //如果没有通过WPE校验，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                                    materialLotIdList.remove(basicPointMaterialLotId);
                                    n = qtyLong + 1;
                                }
                            } else {
                                //如果没有通过数据项的校验，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                                materialLotIdList.remove(basicPointMaterialLotId);
                                n = qtyLong + 1;
                            }
                        } else {
                            //如果没有找到临时条码组合，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                            materialLotIdList.remove(basicPointMaterialLotId);
                            n = qtyLong + 1;
                        }
                    }else {
                        //如果没有找到临时条码组合，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                        materialLotIdList.remove(basicPointMaterialLotId);
                        n = qtyLong + 1;
                    }
                }
            }
        }
        return finalMaterialLotList;
    }

    @Override
    public List<List<String>> pumpFilterRuleSumLineDescend(Long tenantId, List<String> materialLotIdList, List<HmePumpPreSelectionVO7> sumList,
                                                           BigDecimal qty, long qtyLong, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                                           List<List<String>> materialLotGroupList, Map<String, String> letterTagIdMap, HmePumpPreSelectionVO7 wpeLine) {
        HmePumpPreSelectionVO7 sequenceMinSumLine = sumList.get(0);
        String tagId = sequenceMinSumLine.getTagId();
        BigDecimal maxValue = sequenceMinSumLine.getMaxValue();
        //maxAverageValue为maxValue除以筛选规则头的泵浦源个数，四舍五入 保留6位小数
        BigDecimal maxAverageValue = maxValue.divide(qty, 6, RoundingMode.HALF_UP);
        /*
         * 根据目前筛选池中的条码+最小行的tagId取到result,并计算出result-maxAverageValue的差值
         * 结果记录在集合filterPoolMaterialLotSumTagResultDiffMaxList中
         * 因为做while循环时只会减去条码，所以此时我获取到前筛选池中的条码的result与maxAverageValue的差值，下次循环时直接从集合中取即可
         */
        List<HmePumpPreSelectionVO11> filterPoolMaterialLotSumTagResultList = materialLotTagResultList.stream()
                .filter(item -> item.getTagId().equals(tagId)
                        && materialLotIdList.contains(item.getMaterialLotId()))
                .collect(Collectors.toList());
        List<HmePumpPreSelectionVO12> filterPoolMaterialLotSumTagResultDiffMaxList = new ArrayList<>();
        for (HmePumpPreSelectionVO11 hmePumpPreSelectionVO11 : filterPoolMaterialLotSumTagResultList) {
            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = new HmePumpPreSelectionVO12();
            hmePumpPreSelectionVO12.setMaterialLotId(hmePumpPreSelectionVO11.getMaterialLotId());
            hmePumpPreSelectionVO12.setResult(hmePumpPreSelectionVO11.getResult());
            hmePumpPreSelectionVO12.setDiffMaxAverageValue(hmePumpPreSelectionVO11.getResult().subtract(maxAverageValue));
            filterPoolMaterialLotSumTagResultDiffMaxList.add(hmePumpPreSelectionVO12);
        }
        //只要筛选池中还有条码且条码个数大于等于泵浦源个数，循环就继续，循环体内会去掉条码
        List<List<String>> finalMaterialLotList = new ArrayList<>();
        while (CollectionUtils.isNotEmpty(materialLotIdList) && materialLotIdList.size() >= qtyLong) {
            List<String> currentMaterialLotIdList = materialLotIdList;
            List<List<String>> currentMaterialLotGroupList = materialLotGroupList;
            //取出当前筛选池中条码的result与maxAverageValue的差值，并按照差值从大到小的排序，此时差值最大的条码即为此次循环的基点条码
            List<HmePumpPreSelectionVO12> currentPoolMaterialLotSumTagResultDiffMaxList = filterPoolMaterialLotSumTagResultDiffMaxList.stream()
                    .filter(item -> currentMaterialLotIdList.contains(item.getMaterialLotId()))
                    .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMaxAverageValue).reversed())
                    .collect(Collectors.toList());
            HmePumpPreSelectionVO12 basicPointPumpPreSelectionVO12 = currentPoolMaterialLotSumTagResultDiffMaxList.get(0);
            //basicPointMaterialLotId为基点条码ID
            String basicPointMaterialLotId = basicPointPumpPreSelectionVO12.getMaterialLotId();
            //temporaryMaterialLotList为此次循环要确定的临时条码组合
            List<String> temporaryMaterialLotList = new ArrayList<String>(qty.intValue());
            temporaryMaterialLotList.add(basicPointMaterialLotId);
            //diffMaxAverageValueSum 临时条码组合中条码的result与maxAverageValue的差值之和
            BigDecimal diffMaxAverageValueSum = basicPointPumpPreSelectionVO12.getDiffMaxAverageValue();
            List<String> removeMaterialLotIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(currentMaterialLotGroupList)) {
                //如果多个子集集合不为空，则接下来组合其他条码时则不考虑与基点条码同属一个子集下的所有条码
                Iterator<List<String>> materialLotGroupIterator = currentMaterialLotGroupList.iterator();
                while (materialLotGroupIterator.hasNext()) {
                    List<String> next = materialLotGroupIterator.next();
                    if (next.contains(basicPointMaterialLotId)) {
                        removeMaterialLotIdList.addAll(next);
                        break;
                    }
                }
            } else {
                //如果多个子集集合为空,则只不再考虑此条码
                removeMaterialLotIdList.add(basicPointMaterialLotId);
            }
            //n为此次组合中确定的条码个数，当n与泵浦源个数相等时代表此时已找到一个临时条码组合
            long n = temporaryMaterialLotList.size();
            while (n < qtyLong) {
                //注意剩余物料批含义是当前筛选池中所有的条码-removeMaterialLotIdList里的所有条码
                List<String> surplusMaterialLotIdList = currentMaterialLotIdList.stream().filter(item -> !removeMaterialLotIdList.contains(item)).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(surplusMaterialLotIdList)){
                    materialLotIdList.remove(basicPointMaterialLotId);
                    n = qtyLong + 1;
                    continue;
                }
                //temporaryMaterialLotId为此次要确定好的临时组合条码
                String temporaryMaterialLotId = null;
                //如果n<泵浦源个数
                if (qtyLong - n > 1) {
                    //如果泵浦源个数-n大于1，说明此时临时条码组合至少还差2个条码
                    if (diffMaxAverageValueSum.compareTo(BigDecimal.ZERO) > 0) {
                        //如果差值和大于0则取出剩余物料批中当前差值和+本身差值<=0且本身差值最大的物料批
                        BigDecimal currentDiffMaxAverageValueSum = diffMaxAverageValueSum;
                        List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMaxList.stream()
                                .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId())
                                        && (currentDiffMaxAverageValueSum.add(item.getDiffMaxAverageValue()).compareTo(BigDecimal.ZERO) <= 0))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMaxAverageValue).reversed())
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(hmePumpPreSelectionVO12List)) {
                            //如果可以找到，则直接取就好
                            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                            diffMaxAverageValueSum = diffMaxAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMaxAverageValue());
                            temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                            temporaryMaterialLotList.add(temporaryMaterialLotId);
                        } else {
                            //如果找不到，则取剩余物料批中差值最小的物料批
                            List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List2 = filterPoolMaterialLotSumTagResultDiffMaxList.stream()
                                    .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId()))
                                    .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMaxAverageValue))
                                    .collect(Collectors.toList());
                            HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List2.get(0);
                            diffMaxAverageValueSum = diffMaxAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMaxAverageValue());
                            temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                            temporaryMaterialLotList.add(temporaryMaterialLotId);
                        }
                    } else {
                        //如果差值和小于等于0则取出剩余物料批中差值最大的物料批，
                        List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMaxList.stream()
                                .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId()))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMaxAverageValue).reversed())
                                .collect(Collectors.toList());
                        HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                        diffMaxAverageValueSum = diffMaxAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMaxAverageValue());
                        temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                        temporaryMaterialLotList.add(temporaryMaterialLotId);
                    }
                    n++;
                    //新添加的组合条码也要加入到removeMaterialLotIdList
                    if (CollectionUtils.isNotEmpty(currentMaterialLotGroupList)) {
                        //如果多个子集集合不为空，则接下来组合其他条码时则不考虑与基点条码同属一个子集下的所有条码
                        Iterator<List<String>> materialLotGroupIterator = currentMaterialLotGroupList.iterator();
                        while (materialLotGroupIterator.hasNext()) {
                            List<String> next = materialLotGroupIterator.next();
                            if (next.contains(temporaryMaterialLotId)) {
                                removeMaterialLotIdList.addAll(next);
                                break;
                            }
                        }
                    } else {
                        //如果多个子集集合为空,则只不再考虑此条码
                        removeMaterialLotIdList.add(temporaryMaterialLotId);
                    }
                } else {
                    //如果泵浦源个数-n小于等于1，实际上只会等于1，则取出的条码需为剩余物料批中当前差值和+本身差值<0且本身差值最大的物料批
                    BigDecimal currentDiffMaxAverageValueSum = diffMaxAverageValueSum;
                    List<HmePumpPreSelectionVO12> hmePumpPreSelectionVO12List = filterPoolMaterialLotSumTagResultDiffMaxList.stream()
                            .filter(item -> surplusMaterialLotIdList.contains(item.getMaterialLotId())
                                    && (currentDiffMaxAverageValueSum.add(item.getDiffMaxAverageValue()).compareTo(BigDecimal.ZERO) < 0))
                            .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getDiffMaxAverageValue).reversed())
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(hmePumpPreSelectionVO12List)) {
                        //如果可以找到，判断加上找到条码的临时条码组合的结果和是否在此tagId下的min_value和max_value中
                        HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = hmePumpPreSelectionVO12List.get(0);
                        diffMaxAverageValueSum = diffMaxAverageValueSum.add(hmePumpPreSelectionVO12.getDiffMaxAverageValue());
                        temporaryMaterialLotId = hmePumpPreSelectionVO12.getMaterialLotId();
                        temporaryMaterialLotList.add(temporaryMaterialLotId);
                        List<String> temporaryMaterialLotListCopy= temporaryMaterialLotList;
                        BigDecimal temporaryResultSum = materialLotTagResultList.stream()
                                .filter(item -> temporaryMaterialLotListCopy.contains(item.getMaterialLotId())
                                        && tagId.equals(item.getTagId()))
                                .collect(CollectorsUtil.summingBigDecimal(HmePumpPreSelectionVO11::getResult));
                        n++;
                        if (temporaryResultSum.compareTo(sequenceMinSumLine.getMinValue()) >= 0
                                && temporaryResultSum.compareTo(sequenceMinSumLine.getMaxValue()) <= 0) {
                            //进入到第10步，进行其他数据项的校验
                            temporaryMaterialLotList = this.temporaryMaterialLotOtherTagDisplace(tenantId, temporaryMaterialLotList,
                                    materialLotGroupList, materialLotTagResultList, sumList, currentMaterialLotIdList);
                            if (CollectionUtils.isNotEmpty(temporaryMaterialLotList) && temporaryMaterialLotList.size() == qtyLong) {
                                //继续进行WPE校验
                                if(Objects.nonNull(wpeLine)){
                                    temporaryMaterialLotList = this.temporaryMaterialLotWpeDisplace(tenantId, temporaryMaterialLotList, materialLotGroupList,
                                            materialLotTagResultList, sumList, currentMaterialLotIdList, wpeLine, letterTagIdMap);
                                }
                                if (CollectionUtils.isNotEmpty(temporaryMaterialLotList) && temporaryMaterialLotList.size() == qtyLong) {
                                    //此时才通过所有校验，找到一套条码组合，破坏内层循环条件，使之退出内层循环，继续外层循环
                                    finalMaterialLotList.add(temporaryMaterialLotList);
                                    materialLotIdList.removeAll(temporaryMaterialLotList);
                                    n = qtyLong + 1;
                                } else {
                                    //如果没有通过WPE校验，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                                    materialLotIdList.remove(basicPointMaterialLotId);
                                    n = qtyLong + 1;
                                }
                            } else {
                                //如果没有通过数据项的校验，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                                materialLotIdList.remove(basicPointMaterialLotId);
                                n = qtyLong + 1;
                            }
                        } else {
                            //如果没有找到临时条码组合，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                            materialLotIdList.remove(basicPointMaterialLotId);
                            n = qtyLong + 1;
                        }
                    }else {
                        //如果没有找到临时条码组合，则n赋值为qtyLong + 1,破坏内层循环条件，使之退出内层循环，继续外层循环
                        materialLotIdList.remove(basicPointMaterialLotId);
                        n = qtyLong + 1;
                    }
                }
            }
        }
        return finalMaterialLotList;
    }

    @Override
    public List<String> temporaryMaterialLotOtherTagDisplace(Long tenantId, List<String> temporaryMaterialLotList,
                                                             List<List<String>> materialLotGroupList, List<HmePumpPreSelectionVO11> materialLotTagResultList,
                                                             List<HmePumpPreSelectionVO7> sumList, List<String> currentMaterialLotIdList) {
        /*
         * 因为SUM类型的筛选规则行的第一个tagId,我们的临时条码组合已经满足，故此时从排好序的第二个tagId开始
         * 如果临时条码组合满足当前循环的tagId，则继续循环下一tagId
         * 如果临时条码组合不满足当前循环的tagId,则我们需从置换池中取出满足置换要求的条码，有则置换，无则临时条码组合失败
         * 置换池的概念：如果之前获取的materialLotGroupList不为空，临时条码落在其中哪个子集中，那么这个子集中除了临时条码之外的所有条码构成置换池
         *               如果之前获取的materialLotGroupList为空，则筛选池中除了临时条码之外的所有条码构成置换池
         * 核心点在于：如何快速高效地从置换池中取出满足置换要求的条码进行置换
         *             置换时除了要满足当前循环tagId的最小值与最大值，还不能破坏之前已通过校验的tagId
         */
        List<String> passTagIdList = new ArrayList<>(sumList.size());
        passTagIdList.add(sumList.get(0).getTagId());
        for (int i = 1; i < sumList.size(); i++) {
            //currentSequenceMinSumLine 当前循环的SUM类型筛选规则行
            HmePumpPreSelectionVO7 currentSequenceMinSumLine = sumList.get(i);
            String currentTagId = currentSequenceMinSumLine.getTagId();
            //取出临时条码组合中的条码所对应的当前数据项ID所对应的result,进而求和
            List<HmePumpPreSelectionVO11> temporaryMaterialLotResultList = materialLotTagResultList.stream()
                    .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                            && currentTagId.equals(item.getTagId()))
                    .sorted(Comparator.comparing(HmePumpPreSelectionVO11::getResult))
                    .collect(Collectors.toList());
            BigDecimal resultSum = temporaryMaterialLotResultList.stream().collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
            if (resultSum.compareTo(currentSequenceMinSumLine.getMinValue()) < 0) {
                //如果resulr之和小于最小值，则取出result最小的临时条码,先将此条码从临时条码组合中移除出去
                String removeMaterialLotId = temporaryMaterialLotResultList.get(0).getMaterialLotId();
                temporaryMaterialLotList.remove(removeMaterialLotId);
                //寻找可置换的条码
                List<String> displaceMaterialLot = getDisplaceMaterialLot(tenantId, temporaryMaterialLotList, materialLotGroupList, materialLotTagResultList,
                        sumList, currentMaterialLotIdList, currentSequenceMinSumLine, removeMaterialLotId, resultSum, passTagIdList);
                if (CollectionUtils.isEmpty(displaceMaterialLot)) {
                    //如果找不到可置换条码，则传入的临时条码组合失败且无可置换条码，结束
                    return null;
                }
                //如果找到则取出可置换条码集合中在当前最外层遍历的tagId下result最大的那一个条码来置换
                List<HmePumpPreSelectionVO11> displaceMaterialLotResult = materialLotTagResultList.stream()
                        .filter(item -> displaceMaterialLot.contains(item.getMaterialLotId())
                                && currentTagId.equals(item.getTagId()))
                        .sorted(Comparator.comparing(HmePumpPreSelectionVO11::getResult).reversed())
                        .collect(Collectors.toList());
                temporaryMaterialLotList.add(displaceMaterialLotResult.get(0).getMaterialLotId());
            }else if(resultSum.compareTo(currentSequenceMinSumLine.getMaxValue()) > 0){
                //如果resulr之和大于最大值，则取出result最大的临时条码,先将此条码从临时条码组合中移除出去
                String removeMaterialLotId = temporaryMaterialLotResultList.get(temporaryMaterialLotResultList.size() - 1).getMaterialLotId();
                temporaryMaterialLotList.remove(removeMaterialLotId);
                //寻找可置换的条码
                List<String> displaceMaterialLot = getDisplaceMaterialLot(tenantId, temporaryMaterialLotList, materialLotGroupList, materialLotTagResultList,
                        sumList, currentMaterialLotIdList, currentSequenceMinSumLine, removeMaterialLotId, resultSum, passTagIdList);
                if (CollectionUtils.isEmpty(displaceMaterialLot)) {
                    //如果找不到可置换条码，则传入的临时条码组合失败且无可置换条码，结束
                    return null;
                }
                //如果找到则取出可置换条码集合中在当前最外层遍历的tagId下result最小的那一个条码来置换
                List<HmePumpPreSelectionVO11> displaceMaterialLotResult = materialLotTagResultList.stream()
                        .filter(item -> displaceMaterialLot.contains(item.getMaterialLotId())
                                && currentTagId.equals(item.getTagId()))
                        .sorted(Comparator.comparing(HmePumpPreSelectionVO11::getResult))
                        .collect(Collectors.toList());
                temporaryMaterialLotList.add(displaceMaterialLotResult.get(0).getMaterialLotId());
            }
            passTagIdList.add(currentTagId);
        }
        return temporaryMaterialLotList;
    }

    @Override
    public List<String> getDisplaceMaterialLot(Long tenantId, List<String> temporaryMaterialLotList, List<List<String>> materialLotGroupList,
                                               List<HmePumpPreSelectionVO11> materialLotTagResultList, List<HmePumpPreSelectionVO7> sumList,
                                               List<String> currentMaterialLotIdList, HmePumpPreSelectionVO7 currentSequenceMinSumLine,
                                               String removeMaterialLotId, BigDecimal resultSum, List<String> passTagIdList) {
        //currentSequenceMinSumLine 当前循环的SUM类型筛选规则行
        String currentTagId = currentSequenceMinSumLine.getTagId();
        BigDecimal minValue = currentSequenceMinSumLine.getMinValue();
        BigDecimal maxValue = currentSequenceMinSumLine.getMaxValue();
        //确定移除出去的条码的置换池
        List<String> replacePool = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLotGroupList)) {
            for (List<String> materialLotGroup : materialLotGroupList) {
                if (materialLotGroup.contains(removeMaterialLotId)) {
                    replacePool.addAll(materialLotGroup);
                    replacePool.remove(removeMaterialLotId);
                    break;
                }
            }
        } else {
            replacePool.addAll(currentMaterialLotIdList);
            replacePool.removeAll(temporaryMaterialLotList);
            replacePool.remove(removeMaterialLotId);
        }
        if (CollectionUtils.isEmpty(replacePool)) {
            return null;
        }
        List<String> currentReplacePool = replacePool;
        //取出临时条码结果集合中，要移除条码的result，进而取出要满足当前tag的最小result与最大result
        List<HmePumpPreSelectionVO11> removeMaterialLotResultList = materialLotTagResultList.stream()
                .filter(item -> removeMaterialLotId.equals(item.getMaterialLotId())
                        && currentTagId.equals(item.getTagId()))
                .collect(Collectors.toList());
        BigDecimal removeMaterialLotResult = removeMaterialLotResultList.get(0).getResult();
        //最小result =  当前tag的minValue - 当前不满足要求的resultSum + 不满足要求条码的result
        BigDecimal minResult = minValue.subtract(resultSum).add(removeMaterialLotResult);
        //最大result =  当前tag的maxValue - 当前不满足要求的resultSum + 不满足要求条码的result
        BigDecimal maxResult = maxValue.subtract(resultSum).add(removeMaterialLotResult);
        //passCurrentTagMaterialLotList 筛选出置换池中满足当前tag要求的条码
        List<HmePumpPreSelectionVO11> passCurrentTagMaterialLotList = materialLotTagResultList.stream()
                .filter(item -> currentReplacePool.contains(item.getMaterialLotId()) && currentTagId.equals(item.getTagId())
                        && minResult.compareTo(item.getResult()) <= 0 && maxResult.compareTo(item.getResult()) >= 0)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(passCurrentTagMaterialLotList)) {
            //如果可以找到，则再去校验找到的条码是否满足之前通过校验的tagId
            //temporaryReplaceMaterialLotIdList 临时的通过当前循环的tagId的可置换条码集合，进而取出这些条码在之前tagId下的result
            List<String> temporaryReplaceMaterialLotIdList = passCurrentTagMaterialLotList.stream().map(HmePumpPreSelectionVO11::getMaterialLotId).collect(Collectors.toList());
            //errorTemporaryReplaceMaterialLotList 临时可置换条码集合中不满足以前tag的条码集合
            List<String> errorTemporaryReplaceMaterialLotList = new ArrayList<>();
            for (String temporaryReplaceMaterialLotId : temporaryReplaceMaterialLotIdList) {
                //循环每一个临时可置换条码，判断是否满足之前tag的最小值和最大值
                for (String passTagId : passTagIdList) {
                    //取出以前tag的筛选规则行，对于同一头下的SUM类型的筛选规则行来说，tag唯一
                    List<HmePumpPreSelectionVO7> passTagSumLineList = sumList.stream().filter(item -> passTagId.equals(item.getTagId())).collect(Collectors.toList());
                    HmePumpPreSelectionVO7 passSumLine = passTagSumLineList.get(0);
                    //取出当前临时条码组合中剩下的条码在以前tag下的result之和，进而取出当前遍历的临时可置换条码需满足的最小结果与最大结果
                    BigDecimal passTagSum = materialLotTagResultList.stream()
                            .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                                    && passTagId.equals(item.getTagId()))
                            .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
                    BigDecimal minPassTagResult = passSumLine.getMinValue().subtract(passTagSum);
                    BigDecimal maxPassTagResult = passSumLine.getMaxValue().subtract(passTagSum);
                    //取出当前遍历的临时可置换条码在以前tag下的result
                    List<HmePumpPreSelectionVO11> temporaryReplaceMaterialLotPassTagResultList = materialLotTagResultList.stream()
                            .filter(item -> temporaryReplaceMaterialLotId.equals(item.getMaterialLotId())
                                    && passTagId.equals(item.getTagId()))
                            .collect(Collectors.toList());
                    BigDecimal temporaryReplaceMaterialLotPassTagResult = temporaryReplaceMaterialLotPassTagResultList.get(0).getResult();
                    if (temporaryReplaceMaterialLotPassTagResult.compareTo(minPassTagResult) < 0
                            || temporaryReplaceMaterialLotPassTagResult.compareTo(maxPassTagResult) > 0) {
                        //如果result小于最小result或者大于最大result，则不满足当前遍历的tag 退出内层循环 并记录到errorTemporaryReplaceMaterialLotList
                        errorTemporaryReplaceMaterialLotList.add(temporaryReplaceMaterialLotId);
                        break;
                    }
                }
            }
            //得到最终满足以前tag的临时可置换条码集合
            temporaryReplaceMaterialLotIdList.removeAll(errorTemporaryReplaceMaterialLotList);
            if (CollectionUtils.isEmpty(temporaryReplaceMaterialLotIdList)) {
                //如果得不到,则传入的临时条码组合失败且无可置换条码，结束
                return null;
            } else {
                //返回满足要求的可置换条码集合
                return temporaryReplaceMaterialLotIdList;
            }
        } else {
            //如果找不到,则传入的临时条码组合失败且无可置换条码，结束
            return null;
        }
    }

    @Override
    public List<String> temporaryMaterialLotWpeDisplace(Long tenantId, List<String> temporaryMaterialLotList, List<List<String>> materialLotGroupList,
                                                        List<HmePumpPreSelectionVO11> materialLotTagResultList, List<HmePumpPreSelectionVO7> sumList,
                                                        List<String> currentMaterialLotIdList, HmePumpPreSelectionVO7 wpeLine, Map<String, String> letterTagIdMap) {
        //WPE的计算公式定死为p/(v*c)，其中c为常数
        String formula = wpeLine.getFormula();
        BigDecimal minValue = wpeLine.getMinValue();
        BigDecimal maxValue = wpeLine.getMaxValue();
        //创建公式键值对
        Scope scope = new Scope();
        String pTagId = letterTagIdMap.get("P");
        String vTagId = letterTagIdMap.get("V");
        for (Map.Entry<String, String> entry : letterTagIdMap.entrySet()) {
            //letter WPE计算公式组成的字母
            String letter = entry.getKey();
            //tagId  字母所对应的采集项
            String tagId = entry.getValue();
            //根据临时条码组合中的条码+tagId取出result之和,即为字母的实际值
            BigDecimal letterResultSum = materialLotTagResultList.stream()
                    .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                            && tagId.equals(item.getTagId()))
                    .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
            //添加参数键值对
            scope.getVariable(letter).setValue(letterResultSum.doubleValue());
        }
        //计算公式
        Double formulaValue = null;
        try {
            Expression expr2 = Parser.parse(formula, scope);
            formulaValue = expr2.evaluate();
        } catch (Exception e) {
            log.error("<===== 泵浦源预筛选WPE 公式【{}】，参数【{}】计算错误 ", formula, scope.getLocalVariables());
            log.error("<===== 泵浦源预筛选WPE " + e);
            throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_DATA_RECORD_003", "HME", formula));
        }
        if (formulaValue < minValue.doubleValue()) {
            //公式计算值小于最小值，则计算出临时条码组合中p/v最小的值移除出去
            BigDecimal pvMinResult = null;
            String removeMaterialLotId = null;
            for (String temporaryMaterialLot : temporaryMaterialLotList) {
                List<HmePumpPreSelectionVO11> temporaryMaterialLotPResult = materialLotTagResultList.stream()
                        .filter(item -> temporaryMaterialLot.equals(item.getMaterialLotId())
                                && pTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal pResult = temporaryMaterialLotPResult.get(0).getResult();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotVResult = materialLotTagResultList.stream()
                        .filter(item -> temporaryMaterialLot.equals(item.getMaterialLotId())
                                && vTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal vResult = temporaryMaterialLotVResult.get(0).getResult();
                BigDecimal currentPvResult = pResult.divide(vResult, 6, BigDecimal.ROUND_HALF_UP);
                if (Objects.isNull(pvMinResult) || currentPvResult.compareTo(pvMinResult) < 0) {
                    pvMinResult = currentPvResult;
                    removeMaterialLotId = temporaryMaterialLot;
                }
            }
            //根据要移除出去的条码确定好置换池
            List<String> replacePool = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(materialLotGroupList)) {
                for (List<String> materialLotGroup : materialLotGroupList) {
                    if (materialLotGroup.contains(removeMaterialLotId)) {
                        replacePool.addAll(materialLotGroup);
                        replacePool.remove(removeMaterialLotId);
                        break;
                    }
                }
            } else {
                replacePool.addAll(currentMaterialLotIdList);
                replacePool.removeAll(temporaryMaterialLotList);
            }
            temporaryMaterialLotList.remove(removeMaterialLotId);
            //置换池中的条码先初步筛选一下，要想最终满足置换条码，首先单个条码的p/v值必须要比上面最小的pvMinResult大
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            }
            Iterator<String> iterator = replacePool.iterator();
            List<HmePumpPreSelectionVO12> pvResultList = new ArrayList<>();
            while (iterator.hasNext()) {
                String replacePoolMaterialLot = iterator.next();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotPResult = materialLotTagResultList.stream()
                        .filter(item -> replacePoolMaterialLot.equals(item.getMaterialLotId())
                                && pTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal pResult = temporaryMaterialLotPResult.get(0).getResult();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotVResult = materialLotTagResultList.stream()
                        .filter(item -> replacePoolMaterialLot.equals(item.getMaterialLotId())
                                && vTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal vResult = temporaryMaterialLotVResult.get(0).getResult();
                BigDecimal currentPvResult = pResult.divide(vResult, 6, BigDecimal.ROUND_HALF_UP);
                HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = new HmePumpPreSelectionVO12();
                hmePumpPreSelectionVO12.setMaterialLotId(replacePoolMaterialLot);
                hmePumpPreSelectionVO12.setPvResult(currentPvResult);
                pvResultList.add(hmePumpPreSelectionVO12);
                if (currentPvResult.compareTo(pvMinResult) <= 0) {
                    iterator.remove();
                }
            }
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            }
            List<String> errorMaterialLotList = new ArrayList<>();
            for (String replacePoolMaterialLot : replacePool) {
                scope = new Scope();
                //先将当前循环的置换池条码加到临时条码组合中，判断是否满足置换要求，最终在移除出去
                temporaryMaterialLotList.add(replacePoolMaterialLot);
                for (Map.Entry<String, String> entry : letterTagIdMap.entrySet()) {
                    //letter WPE计算公式组成的字母
                    String letter = entry.getKey();
                    //tagId  字母所对应的采集项
                    String tagId = entry.getValue();
                    //根据临时条码组合中的条码+tagId取出result之和,即为字母的实际值
                    BigDecimal letterResultSum = materialLotTagResultList.stream()
                            .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                                    && tagId.equals(item.getTagId()))
                            .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
                    //添加参数键值对
                    scope.getVariable(letter).setValue(letterResultSum.doubleValue());
                }
                //计算公式
                Double currentFormulaValue = null;
                try {
                    Expression expr2 = Parser.parse(formula, scope);
                    currentFormulaValue = expr2.evaluate();
                } catch (Exception e) {
                    log.error("<===== 泵浦源预筛选WPE2 公式【{}】，参数【{}】计算错误 ", formula, scope.getLocalVariables());
                    log.error("<===== 泵浦源预筛选WPE2 " + e);
                    throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_DATA_RECORD_003", "HME", formula));
                }
                if (currentFormulaValue.compareTo(minValue.doubleValue()) >= 0 &&
                        currentFormulaValue.compareTo(maxValue.doubleValue()) <= 0) {
                    //只有当前的公式计算值在最小值和最大值之间，再去判断当前的临时条码组合是否满足之前所有SUM类型的筛选规则要求
                    for (HmePumpPreSelectionVO7 sumLine : sumList) {
                        BigDecimal resultSum = materialLotTagResultList.stream()
                                .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                                        && sumLine.getTagId().equals(item.getTagId()))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO11::getResult))
                                .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
                        if (resultSum.compareTo(sumLine.getMinValue()) < 0 || resultSum.compareTo(sumLine.getMaxValue()) > 0) {
                            errorMaterialLotList.add(replacePoolMaterialLot);
                            break;
                        }
                    }
                } else {
                    errorMaterialLotList.add(replacePoolMaterialLot);
                }
                temporaryMaterialLotList.remove(replacePoolMaterialLot);
            }
            replacePool.removeAll(errorMaterialLotList);
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            } else {
                List<String> finalReplacePool = replacePool;
                //如果找到的有可置换条码，则从中取出p/v最大的条码去置换
                List<HmePumpPreSelectionVO12> replacePoolSortedList = pvResultList.stream()
                        .filter(item -> finalReplacePool.contains(item.getMaterialLotId()))
                        .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getPvResult).reversed())
                        .collect(Collectors.toList());
                temporaryMaterialLotList.add(replacePoolSortedList.get(0).getMaterialLotId());
                return temporaryMaterialLotList;
            }
        } else if (formulaValue > maxValue.doubleValue()) {
            //公式计算值大于最小值，则计算出临时条码组合中p/v最大的值移除出去
            BigDecimal pvMaxResult = null;
            String removeMaterialLotId = null;
            for (String temporaryMaterialLot : temporaryMaterialLotList) {
                List<HmePumpPreSelectionVO11> temporaryMaterialLotPResult = materialLotTagResultList.stream()
                        .filter(item -> temporaryMaterialLot.equals(item.getMaterialLotId())
                                && pTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal pResult = temporaryMaterialLotPResult.get(0).getResult();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotVResult = materialLotTagResultList.stream()
                        .filter(item -> temporaryMaterialLot.equals(item.getMaterialLotId())
                                && vTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal vResult = temporaryMaterialLotVResult.get(0).getResult();
                BigDecimal currentPvResult = pResult.divide(vResult, 6, BigDecimal.ROUND_HALF_UP);
                if (Objects.isNull(pvMaxResult) || currentPvResult.compareTo(pvMaxResult) > 0) {
                    pvMaxResult = currentPvResult;
                    removeMaterialLotId = temporaryMaterialLot;
                }
            }
            //根据要移除出去的条码确定好置换池
            List<String> replacePool = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(materialLotGroupList)) {
                for (List<String> materialLotGroup : materialLotGroupList) {
                    if (materialLotGroup.contains(removeMaterialLotId)) {
                        replacePool.addAll(materialLotGroup);
                        replacePool.remove(removeMaterialLotId);
                        break;
                    }
                }
            } else {
                replacePool.addAll(currentMaterialLotIdList);
                replacePool.removeAll(temporaryMaterialLotList);
            }
            temporaryMaterialLotList.remove(removeMaterialLotId);
            //置换池中的条码先初步筛选一下，要想最终满足置换条码，首先单个条码的p/v值必须要比上面最大的pvMinResult小
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            }
            Iterator<String> iterator = replacePool.iterator();
            List<HmePumpPreSelectionVO12> pvResultList = new ArrayList<>();
            while (iterator.hasNext()) {
                String replacePoolMaterialLot = iterator.next();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotPResult = materialLotTagResultList.stream()
                        .filter(item -> replacePoolMaterialLot.equals(item.getMaterialLotId())
                                && pTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal pResult = temporaryMaterialLotPResult.get(0).getResult();
                List<HmePumpPreSelectionVO11> temporaryMaterialLotVResult = materialLotTagResultList.stream()
                        .filter(item -> replacePoolMaterialLot.equals(item.getMaterialLotId())
                                && vTagId.equals(item.getTagId()))
                        .collect(Collectors.toList());
                BigDecimal vResult = temporaryMaterialLotVResult.get(0).getResult();
                BigDecimal currentPvResult = pResult.divide(vResult, 6, BigDecimal.ROUND_HALF_UP);
                HmePumpPreSelectionVO12 hmePumpPreSelectionVO12 = new HmePumpPreSelectionVO12();
                hmePumpPreSelectionVO12.setMaterialLotId(replacePoolMaterialLot);
                hmePumpPreSelectionVO12.setPvResult(currentPvResult);
                pvResultList.add(hmePumpPreSelectionVO12);
                if (currentPvResult.compareTo(pvMaxResult) >= 0) {
                    iterator.remove();
                }
            }
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            }
            List<String> errorMaterialLotList = new ArrayList<>();
            for (String replacePoolMaterialLot : replacePool) {
                scope = new Scope();
                //先将当前循环的置换池条码加到临时条码组合中，判断是否满足置换要求，最终在移除出去
                temporaryMaterialLotList.add(replacePoolMaterialLot);
                for (Map.Entry<String, String> entry : letterTagIdMap.entrySet()) {
                    //letter WPE计算公式组成的字母
                    String letter = entry.getKey();
                    //tagId  字母所对应的采集项
                    String tagId = entry.getValue();
                    //根据临时条码组合中的条码+tagId取出result之和,即为字母的实际值
                    BigDecimal letterResultSum = materialLotTagResultList.stream()
                            .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                                    && tagId.equals(item.getTagId()))
                            .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
                    //添加参数键值对
                    scope.getVariable(letter).setValue(letterResultSum.doubleValue());
                }
                //计算公式
                Double currentFormulaValue = null;
                try {
                    Expression expr2 = Parser.parse(formula, scope);
                    currentFormulaValue = expr2.evaluate();
                } catch (Exception e) {
                    log.error("<===== 泵浦源预筛选WPE2max 公式【{}】，参数【{}】计算错误 ", formula, scope.getLocalVariables());
                    log.error("<===== 泵浦源预筛选WPE2max " + e);
                    throw new MtException("HME_EO_JOB_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_DATA_RECORD_003", "HME", formula));
                }
                if (currentFormulaValue.compareTo(minValue.doubleValue()) >= 0 &&
                        currentFormulaValue.compareTo(maxValue.doubleValue()) <= 0) {
                    //只有当前的公式计算值在最小值和最大值之间，再去判断当前的临时条码组合是否满足之前所有SUM类型的筛选规则要求
                    for (HmePumpPreSelectionVO7 sumLine : sumList) {
                        BigDecimal resultSum = materialLotTagResultList.stream()
                                .filter(item -> temporaryMaterialLotList.contains(item.getMaterialLotId())
                                        && sumLine.getTagId().equals(item.getTagId()))
                                .sorted(Comparator.comparing(HmePumpPreSelectionVO11::getResult))
                                .collect(CollectorsUtil.summingBigDecimal(item -> item.getResult()));
                        if (resultSum.compareTo(sumLine.getMinValue()) < 0 || resultSum.compareTo(sumLine.getMaxValue()) > 0) {
                            errorMaterialLotList.add(replacePoolMaterialLot);
                            break;
                        }
                    }
                } else {
                    errorMaterialLotList.add(replacePoolMaterialLot);
                }
                temporaryMaterialLotList.remove(replacePoolMaterialLot);
            }
            replacePool.removeAll(errorMaterialLotList);
            if (CollectionUtils.isEmpty(replacePool)) {
                return null;
            } else {
                List<String> finalReplacePool = replacePool;
                //如果找到的有可置换条码，则从中取出p/v最小的条码去置换
                List<HmePumpPreSelectionVO12> replacePoolSortedList = pvResultList.stream()
                        .filter(item -> finalReplacePool.contains(item.getMaterialLotId()))
                        .sorted(Comparator.comparing(HmePumpPreSelectionVO12::getPvResult))
                        .collect(Collectors.toList());
                temporaryMaterialLotList.add(replacePoolSortedList.get(0).getMaterialLotId());
                return temporaryMaterialLotList;
            }
        }
        return temporaryMaterialLotList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePumpPreSelectionVO15 insertPumpPreSelectionData(Long tenantId, HmePumpPreSelectionDTO5 dto, 
                                                              List<List<String>> finalMaterialLotList, String bomId, 
                                                              String headId, long qty) {
        int setsNum = finalMaterialLotList.size();
        //生成挑选批次
        String selectionLot = null;
        if(StringUtils.isNotBlank(dto.getSelectionLot())){
            selectionLot = dto.getSelectionLot();
        }else {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
            String[] split = mtModWorkcell.getWorkcellCode().split("-");
            String subWorkcellCode = split[split.length - 1];
            MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
            mtNumrange.setObjectCode("PUMP_PICK_BATCH");
            List<String> incomingValueList = new ArrayList<>();
            incomingValueList.add(subWorkcellCode);
            mtNumrange.setIncomingValueList(incomingValueList);
            selectionLot = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
        }
        //新增头
        HmePumpPreSelection hmePumpPreSelection = new HmePumpPreSelection();
        hmePumpPreSelection.setTenantId(tenantId);
        hmePumpPreSelection.setSiteId(dto.getSiteId());
        hmePumpPreSelection.setWorkcellId(dto.getWorkcellId());
        hmePumpPreSelection.setSelectionLot(selectionLot);
        hmePumpPreSelection.setSetsNum(Long.valueOf(setsNum));
        hmePumpPreSelection.setCombMaterialId(dto.getMaterialId());
        hmePumpPreSelection.setBomId(bomId);
        hmePumpPreSelection.setStatus("NEW");
        hmePumpPreSelection.setRuleHeadId(headId);
        hmePumpPreSelection.setPumpQty(qty);
        this.insertSelective(hmePumpPreSelection);
        //新增行
        Long selectionOrder = 1L;
        List<HmePumpPreSelectionVO4> pumpMaterialLotInfoList = dto.getPumpMaterialLotInfoList();
        int lineSize = 0;
        for (List<String> singleMaterialLotList:finalMaterialLotList) {
            lineSize = lineSize + singleMaterialLotList.size();
        }
        List<String> ids = mtCustomDbRepository.getNextKeys("hme_pump_selection_details_s", lineSize);
        List<String> cids = mtCustomDbRepository.getNextKeys("hme_pump_selection_details_cid_s", lineSize);
        List<String> sqlList = new ArrayList<>();
        Integer indexNum = 0;
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date currentDate = new Date();
        Map<String, Long> materialLotSelectionOrderMap = new HashMap<>();
        for (List<String> singleMaterialLotList:finalMaterialLotList) {
            for (String materialLot:singleMaterialLotList) {
                HmePumpSelectionDetails hmePumpSelectionDetails = new HmePumpSelectionDetails();
                hmePumpSelectionDetails.setTenantId(tenantId);
                hmePumpSelectionDetails.setPumpPreSelectionId(hmePumpPreSelection.getPumpPreSelectionId());
                hmePumpSelectionDetails.setSelectionOrder(selectionOrder);
                materialLotSelectionOrderMap.put(materialLot, selectionOrder);
                hmePumpSelectionDetails.setMaterialLotId(materialLot);
                List<HmePumpPreSelectionVO4> materialLotInfo = pumpMaterialLotInfoList.stream()
                        .filter(item -> materialLot.equals(item.getMaterialLotId()))
                        .collect(Collectors.toList());
                hmePumpSelectionDetails.setMaterialId(materialLotInfo.get(0).getMaterialId());
                List<HmePumpPreSelectionVO4> containerId = materialLotInfo.stream()
                        .filter(item -> StringUtils.isNotBlank(item.getContainerId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(containerId)){
                    hmePumpSelectionDetails.setOldContainerId(containerId.get(0).getContainerId());
                }
                hmePumpSelectionDetails.setStatus("NEW");
                hmePumpSelectionDetails.setPumpSelectionDetailsId(ids.get(indexNum));
                hmePumpSelectionDetails.setCid(Long.valueOf(cids.get(indexNum)));
                hmePumpSelectionDetails.setObjectVersionNumber(1L);
                hmePumpSelectionDetails.setCreationDate(currentDate);
                hmePumpSelectionDetails.setCreatedBy(userId);
                hmePumpSelectionDetails.setLastUpdatedBy(userId);
                hmePumpSelectionDetails.setLastUpdateDate(currentDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmePumpSelectionDetails));
                indexNum++;
            }
            selectionOrder++;
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        //返回结果
        HmePumpPreSelectionVO15 result = new HmePumpPreSelectionVO15();
        result.setSetsNum(setsNum);
        result.setSelectionLot(selectionLot);
        for (HmePumpPreSelectionVO4 pumpMaterialLotInfo:pumpMaterialLotInfoList) {
            Long pumpSelectionOrder = materialLotSelectionOrderMap.get(pumpMaterialLotInfo.getMaterialLotId());
            if(Objects.nonNull(pumpSelectionOrder)){
                pumpMaterialLotInfo.setGroupNum(pumpSelectionOrder);
            }
        }
        result.setPumpMaterialLotInfoList(pumpMaterialLotInfoList);
        return result;
    }
}
