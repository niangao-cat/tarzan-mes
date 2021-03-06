package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnLotMaterialMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.util.MtBaseConstants;

import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ruike.hme.infra.mapper.HmeEoJobMaterialMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtAssembleProcessActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO5;
import tarzan.actual.domain.vo.MtEoComponentActualVO;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import com.ruike.hme.domain.repository.HmeEoJobMaterialRepository;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * ??????????????????-?????? ???????????????
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:41:23
 */
@Component
@Slf4j
public class HmeEoJobMaterialRepositoryImpl extends BaseRepositoryImpl<HmeEoJobMaterial>
        implements HmeEoJobMaterialRepository {
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;
    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    /**
     * ????????????
     *
     * @param sqlList  ?????????
     * @param splitNum ????????????
     * @return ????????????
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, int splitNum) {

        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    @Override
    public List<HmeEoJobMaterialVO> initJobMaterial(Long tenantId, String materialId, Boolean isSplitLine,
                                                    BigDecimal componentQty, String bomComponentId, HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.initJobMaterial tenantId=[{}],bomComponentId=[{}],materialId=[{}]" +
                ",componentQty=[{}],isSplitLine=[{}],dto=[{}]", tenantId, bomComponentId, materialId, componentQty, isSplitLine, dto);

        //??????????????????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        List<HmeEoJobMaterialVO> resultVOList = new ArrayList<>();

        List<HmeEoJobMaterial> materialSubList = new ArrayList<>();
        // ???????????????SN????????????????????????????????????????????????
        if (isSplitLine && componentQty.doubleValue() % 1 != 0) {
            // ??????????????????,???????????????????????????
            throw new MtException("HME_EO_JOB_SN_006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_006", "HME"));
        }
        //V20200826 modify by penglin.sui for tianyang.xie ???????????? <= 0 ??????????????????
        if (componentQty.compareTo(BigDecimal.ZERO) <= 0) {
            return resultVOList;
        }
        // ??????????????????????????????????????????????????????????????????
        int componentQtyInt = componentQty.intValue();
        int jobMaterialCount = componentQtyInt;

        //????????????????????????????????????
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            jobMaterialCount = (int) (componentQtyInt * dto.getPrepareQty().doubleValue());
        }

        if (isSplitLine) {
            List<String> ids = this.customDbRepository.getNextKeys("hme_eo_job_material_s", jobMaterialCount);
            List<String> cIds = this.customDbRepository.getNextKeys("hme_eo_job_material_cid_s", jobMaterialCount);
            int count = 0;
            for (int i = 0; i < jobMaterialCount; i++) {
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setTenantId(tenantId);
                hmeEoJobMaterial.setJobMaterialId(ids.get(count));
                hmeEoJobMaterial.setJobId(dto.getJobId());
                hmeEoJobMaterial.setEoId(dto.getEoId());
                hmeEoJobMaterial.setBomComponentId(bomComponentId);
                hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterial.setMaterialId(materialId);
                // ?????????????????????
                hmeEoJobMaterial.setIsReleased(0);
                hmeEoJobMaterial.setReleaseQty(BigDecimal.ONE);
                hmeEoJobMaterial.setCid(Long.valueOf(cIds.get(count)));
                hmeEoJobMaterial.setObjectVersionNumber(1L);
                hmeEoJobMaterial.setCreationDate(new Date());
                hmeEoJobMaterial.setCreatedBy(userId);
                hmeEoJobMaterial.setLastUpdatedBy(userId);
                hmeEoJobMaterial.setLastUpdateDate(new Date());
                materialSubList.add(hmeEoJobMaterial);
                count++;
            }
            //self().batchInsertSelective(materialSubList);
            //??????????????????
            if (CollectionUtils.isNotEmpty(materialSubList)) {
                List<List<HmeEoJobMaterial>> splitSqlList = splitSqlList(materialSubList, 200);
                for (List<HmeEoJobMaterial> domains : splitSqlList) {
                    hmeEoJobMaterialMapper.batchInsertJobMaterial("hme_eo_job_material", domains);
                }
            }
        } else {
            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setJobId(dto.getJobId());
            hmeEoJobMaterial.setEoId(dto.getEoId());
            hmeEoJobMaterial.setBomComponentId(bomComponentId);
            hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
            hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterial.setMaterialId(materialId);
            // ?????????????????????
            hmeEoJobMaterial.setIsReleased(0);
            hmeEoJobMaterial.setReleaseQty(componentQty);
            hmeEoJobMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobMaterial);
            materialSubList.add(hmeEoJobMaterial);
        }

        //V20201023 modify by penglin.sui ????????????????????????
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialId);

        //??????????????????
        String bomComponentVersion = "";
        MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
        lineAttribute7Attr.setAttrName("lineAttribute7");
        List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                Collections.singletonList(lineAttribute7Attr));
        if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
            if (StringUtils.isNotBlank(mtBomExtendAttrVOList.get(0).getAttrValue())) {
                bomComponentVersion = mtBomExtendAttrVOList.get(0).getAttrValue();
            }
        }

        for (HmeEoJobMaterial jobMaterial : materialSubList) {
            HmeEoJobMaterialVO jobMaterialVO = new HmeEoJobMaterialVO();
            BeanUtils.copyProperties(jobMaterial, jobMaterialVO);

            jobMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
            jobMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
            jobMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());

            jobMaterialVO.setBomComponentVersion(bomComponentVersion);
            jobMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
            if (jobMaterial.getReleaseQty().compareTo(BigDecimal.ZERO) > 0) {
                jobMaterialVO.setWkcMatchedFlag(YES);
            }
            resultVOList.add(jobMaterialVO);
        }
        return resultVOList;
    }

    /**
     *
     * @Description ?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:26
     * @param tenantId       ??????ID
     * @param materialId     ??????ID
     * @param componentQty   ????????????
     * @param bomComponentId bom??????ID
     * @param dto            ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initJobMaterialWithoutQuery(Long tenantId, String materialId, boolean isSplitLine, BigDecimal componentQty, String bomComponentId, HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.initJobMaterialWithoutQuery tenantId=[{}],bomComponentId=[{}],materialId=[{}]" +
                ",componentQty=[{}],isSplitLine=[{}],dto=[{}]", tenantId, bomComponentId, materialId, componentQty, isSplitLine, dto);

        //??????????????????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        // ???????????????SN????????????????????????????????????????????????
        if (isSplitLine && componentQty.doubleValue() % 1 != 0) {
            // ??????????????????,???????????????????????????
            throw new MtException("HME_EO_JOB_SN_006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_006", "HME"));
        }
        //V20200826 modify by penglin.sui for tianyang.xie ???????????? <= 0 ??????????????????
        if (componentQty.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        // ??????????????????????????????????????????????????????????????????
        int componentQtyInt = componentQty.intValue();
        int jobMaterialCount = componentQtyInt;

        //????????????????????????????????????
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            jobMaterialCount = (int) (componentQtyInt * dto.getPrepareQty().doubleValue());
        }

        if (isSplitLine) {
            List<HmeEoJobMaterial> insertList = new ArrayList<>();
            List<String> ids = this.customDbRepository.getNextKeys("hme_eo_job_material_s", jobMaterialCount);
            List<String> cIds = this.customDbRepository.getNextKeys("hme_eo_job_material_cid_s", jobMaterialCount);
            int count = 0;
            for (int i = 0; i < jobMaterialCount; i++) {
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setTenantId(tenantId);
                hmeEoJobMaterial.setJobMaterialId(ids.get(count));
                hmeEoJobMaterial.setJobId(dto.getJobId());
                hmeEoJobMaterial.setEoId(dto.getEoId());
                hmeEoJobMaterial.setBomComponentId(bomComponentId);
                hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterial.setMaterialId(materialId);
                // ?????????????????????
                hmeEoJobMaterial.setIsReleased(0);
                hmeEoJobMaterial.setReleaseQty(BigDecimal.ONE);
                hmeEoJobMaterial.setCid(Long.valueOf(cIds.get(count)));
                hmeEoJobMaterial.setObjectVersionNumber(1L);
                hmeEoJobMaterial.setCreationDate(new Date());
                hmeEoJobMaterial.setCreatedBy(userId);
                hmeEoJobMaterial.setLastUpdatedBy(userId);
                hmeEoJobMaterial.setLastUpdateDate(new Date());
                insertList.add(hmeEoJobMaterial);
                count++;
            }

            //??????????????????
            if (CollectionUtils.isNotEmpty(insertList)) {
                List<List<HmeEoJobMaterial>> splitSqlList = splitSqlList(insertList, 200);
                for (List<HmeEoJobMaterial> domains : splitSqlList) {
                    hmeEoJobMaterialMapper.batchInsertJobMaterial("hme_eo_job_material", domains);
                }
            }
        } else {
            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setJobId(dto.getJobId());
            hmeEoJobMaterial.setEoId(dto.getEoId());
            hmeEoJobMaterial.setBomComponentId(bomComponentId);
            hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
            hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterial.setMaterialId(materialId);
            // ?????????????????????
            hmeEoJobMaterial.setIsReleased(0);
            hmeEoJobMaterial.setReleaseQty(componentQty);
            hmeEoJobMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobMaterial);
        }
    }

    @Override
    public List<HmeEoJobMaterialVO> jobSnLimitJobMaterialQuery(Long tenantId, HmeEoJobMaterialVO2 dto) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.jobSnLimitJobMaterialQuery tenantId=[{}],dto=[{}]", tenantId, dto);
        List<HmeEoJobMaterial> eoJobMaterials = hmeEoJobMaterialMapper.selectJobMaterial(tenantId, dto.getJobId(), dto.getWorkcellId());

        Map<String, Long> sortedMap = new HashMap<String, Long>();
        List<HmeEoJobMaterial> hmeEoJobMaterialList = new ArrayList<>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        if (CollectionUtils.isNotEmpty(eoJobMaterials)) {
            //????????????????????????????????? add by yuchao.wang for lu.bai at 2020.9.17
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtMaterial material = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                if (Objects.nonNull(material)) {
                    List<HmePrepareMaterialVO> prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery(tenantId,
                            dto.getSiteId(), dto.getWorkOrderId(), material.getMaterialCode());
                    if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                        List<String> materialIdList = new ArrayList<>();
                        prepareMaterialVOList.forEach(item -> {
                            sortedMap.put(item.getMaterialId(), item.getLineNumber());
                            materialIdList.add(item.getMaterialId());
                        });

                        //V20200929 modify by pengli.sui for lu.bai ???????????????????????????
                        List<HmeEoJobMaterial> hmeEoJobMaterialList2 = eoJobMaterials.stream().filter(item -> !materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList2)) {
                            HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
                            hmeEoJobMaterialVO.setJobType(dto.getJobType());
                            hmeEoJobMaterialVO.setEoId(dto.getEoId());
                            hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
                            hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());
                            for (HmeEoJobMaterial hmeEoJobMaterial : hmeEoJobMaterialList2
                            ) {
                                if (hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), hmeEoJobMaterialVO, null, null)) {
                                    materialIdList.add(hmeEoJobMaterial.getMaterialId());
                                }
                            }
                        }

                        hmeEoJobMaterialList = eoJobMaterials.stream().filter(item -> materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                    }
                }
            } else {
                hmeEoJobMaterialList = eoJobMaterials;

                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                    for (MtEoVO20 mtEoVO20 : mtEoVO20List
                    ) {
                        sortedMap.put(mtEoVO20.getMaterialId(), mtEoVO20.getSequence());
                    }
                }
            }
        }

        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
            //???????????????????????????
            List<MtWorkOrderVO8> woComponentList = new ArrayList<>();
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
            } else {
                // ????????????????????????wkc??????????????????
                if (CollectionUtils.isEmpty(mtEoVO20List)) {
                    if (StringUtils.isNotBlank(dto.getOperationId())) {
                        MtEoVO19 mtEoVO19 = new MtEoVO19();
                        mtEoVO19.setEoId(dto.getEoId());
                        mtEoVO19.setOperationId(dto.getOperationId());
                        mtEoVO19.setRouterStepId(dto.getEoStepId());
                        // ????????????wkc???????????????????????????
                        mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                    }
                }
            }

            for (HmeEoJobMaterial material : hmeEoJobMaterialList) {
                if (StringUtils.isNotBlank(material.getBomComponentId())) {
                    if (hmeEoJobSnRepository.checkVirtualComponent(tenantId, material.getBomComponentId()) &&
                            !HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        continue;
                    }
                }
                HmeEoJobMaterialVO jobMaterialVO = new HmeEoJobMaterialVO();
                BeanUtils.copyProperties(material, jobMaterialVO);
                // ??????????????????
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                jobMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
                jobMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
                jobMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
                if (StringUtils.isNotBlank(material.getMaterialLotId())) {
                    MtMaterialLot mtMaterialLot =
                            mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                    jobMaterialVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());

                }
                jobMaterialVO.setLineNumber(sortedMap.getOrDefault(jobMaterialVO.getMaterialId(), 99999L));

                //??????????????????
                MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
                lineAttribute7Attr.setAttrName("lineAttribute7");
                List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        "mt_bom_component_attr", "BOM_COMPONENT_ID", material.getBomComponentId(),
                        Collections.singletonList(lineAttribute7Attr));
                if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
                    if (StringUtils.isNotBlank(mtBomExtendAttrVOList.get(0).getAttrValue())) {
                        jobMaterialVO.setBomComponentVersion(mtBomExtendAttrVOList.get(0).getAttrValue());
                    }
                }
                jobMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    if (CollectionUtils.isNotEmpty(woComponentList)) {
                        Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobMaterialVO.getMaterialId())).findFirst();
                        if (componentOptional.isPresent()) {
                            if (BigDecimal.valueOf(componentOptional.get().getPerQty()).compareTo(BigDecimal.ZERO) > 0) {
                                jobMaterialVO.setWkcMatchedFlag(YES);
                            }
                        }
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                        Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobMaterialVO.getMaterialId())).findFirst();
                        if (componentOptional.isPresent()) {
                            if (BigDecimal.valueOf(componentOptional.get().getPreQty()).compareTo(BigDecimal.ZERO) > 0) {
                                jobMaterialVO.setWkcMatchedFlag(YES);
                            }
                        }
                    }
                }

                hmeEoJobMaterialVOList.add(jobMaterialVO);
            }
        }

        //??????LineNumber???????????? modify by yuchao.wang for lu.bai at 2020.9.25
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialVOList)) {
            hmeEoJobMaterialVOList = hmeEoJobMaterialVOList.stream().sorted(
                    Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList());
        }

        return CollectionUtils.isNotEmpty(hmeEoJobMaterialVOList) ? hmeEoJobMaterialVOList : new ArrayList<HmeEoJobMaterialVO>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.releaseScan tenantId=[{}],dto=[{}]", tenantId, dto);
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            // ??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        MtMaterialLotVO3 materialLotParam = new MtMaterialLotVO3();
        materialLotParam.setMaterialLotCode(dto.getMaterialLotCode());
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotParam);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            dto.setMaterialLotId(materialLotIds.get(0));
        } else {
            // ??????????????????, ?????????
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
        //???????????????
        String substitsuteFlag = StringUtils.isBlank(dto.getSubstituteFlag()) ? HmeConstants.ConstantValue.NO : dto.getSubstituteFlag();
        if (HmeConstants.ConstantValue.NO.equals(substitsuteFlag)) {
            if (!mtMaterialLot.getMaterialId().equals(dto.getMaterialId())) {
                // ?????????????????????????????????????????????,?????????!
                throw new MtException("HME_NC_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0001", "HME"));
            }
        }

        //V20201016 modify by penglin.sui for lu.bai ????????????????????????
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId, dto.getSiteId(), mtMaterialLot.getMaterialId());
        if (!HmeConstants.MaterialTypeCode.SN.equals(materialType)) {
            //?????????????????????${1}??????????????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_116", "HME", materialType));
        }

        int count = hmeEoJobMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobMaterial.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, mtMaterialLot.getMaterialId())
                        .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_LOT_CODE, dto.getMaterialLotCode())
                        .andEqualTo(HmeEoJobMaterial.FIELD_JOB_ID, dto.getJobId()))
                .build());
        log.info("====================count====================:" + count);
        if (count > 0) {
            HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
            hmeEoJobMaterialVO.setJobType(dto.getJobType());
            hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterialVO.setDeleteFlag(YES);
            hmeEoJobMaterialVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobMaterialVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeEoJobMaterialVO.setJobId(dto.getJobId());
            hmeEoJobMaterialVO.setEoId(dto.getEoId());
            hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
            hmeEoJobMaterialVO.setRouterStepId(dto.getEoStepId());
            List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = new ArrayList<HmeEoJobMaterialVO>();
            hmeEoJobMaterialVOList.add(hmeEoJobMaterialVO);
            return hmeEoJobMaterialVOList;
        }

        //V20200829 modify by penglin.sui for jiao.chen ?????????????????????OK
        if (!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())) {
            // ????????????${1}?????????OK??????,????????????????????????
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            // ???????????????????????????wkc???????????????????????????????????????
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            log.info("====================dto.getEoId()====================:" + dto.getEoId());
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        } else {
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderVO7.setOperationId(dto.getOperationId());
            woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
        }

        if (YES.equals(substitsuteFlag)) {
//            List<String> componentMaterialIdList = new ArrayList<String>();
//            if(CollectionUtils.isNotEmpty(mtEoVO20List)) {
//                componentMaterialIdList = mtEoVO20List.stream().map(MtEoVO20::getMaterialId).collect(Collectors.toList());
//            }else if(CollectionUtils.isNotEmpty(woComponentList)) {
//                componentMaterialIdList = woComponentList.stream().map(MtWorkOrderVO8::getMaterialId).collect(Collectors.toList());
//            }
//            if (CollectionUtils.isNotEmpty(componentMaterialIdList)) {
//                if (componentMaterialIdList.contains(mtMaterialLot.getMaterialId())) {
//                    //???????????????????????????????????????,?????????????????????????????????????????????????????????
//                    throw new MtException("HME_EO_JOB_SN_079", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_EO_JOB_SN_079", "HME"));
//                }
//            }

            log.info("====================??????????????? begin====================");
            if (!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, mtMaterialLot.getMaterialId(), dto, woComponentList, mtEoVO20List)
                    && !StringUtils.equals(dto.getPfType(), HmeConstants.PfType.REWORK)) {
                //??????????????????????????????????????????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_078", "HME"));
            }
            log.info("====================??????????????? end====================");
        }

        // ????????????????????????
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            //????????????????????????.${1}
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotEnableValidate???"));
        }

        //V20200923 modify by penglin.sui for lu.bai ???????????????????????????????????????
        if (HmeConstants.ConstantValue.NO.equals(substitsuteFlag)) {
            if (dto.getReleaseQty().compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) != 0) {
                // ????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_074", "HME"));
            }
        }

        // ????????????????????????????????????????????????????????????
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(dto.getSiteId());
        materialSiteParam.setMaterialId(mtMaterialLot.getMaterialId());
        String materialSiteId =
                mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }
        MtExtendVO materialSiteExtend = new MtExtendVO();
        materialSiteExtend.setTableName("mt_material_site_attr");
        materialSiteExtend.setKeyId(materialSiteId);
        List<MtExtendAttrVO> materialSiteExtendAttr = mtExtendSettingsRepository.attrPropertyQuery(tenantId, materialSiteExtend);
        List<MtExtendAttrVO> mtLotTypeAttr = materialSiteExtendAttr.stream()
                .filter(result -> "attribute14".equals(result.getAttrName()))
                .collect(Collectors.toList());
        // ??????????????????????????????????????????????????????????????????SN??????????????????????????????,?????????????????????SN?????????
        String attrValue = HmeConstants.ConstantValue.SN;
        if (CollectionUtils.isNotEmpty(mtLotTypeAttr)) {
            attrValue = mtLotTypeAttr.get(0).getAttrValue();
        }

        if (!HmeConstants.ConstantValue.SN.equals(attrValue)) {
            //?????????${1}????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_072", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_072", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        if (HmeConstants.ConstantValue.SN.equals(attrValue)) {
            if (dto.getSnNum().equals(dto.getMaterialLotCode())) {
                // ??????????????????????????????????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_047", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_047", "HME"));
            }

            List<String> materialLotCodeList = hmeEoJobMaterialMapper.selectMaterialLotBindMaterialLot(tenantId, dto.getMaterialLotCode(), dto.getJobMaterialId());
            if (CollectionUtils.isNotEmpty(materialLotCodeList)) {
                //???????????????????????????SN??????${1}???
                //throw new MtException("HME_EO_JOB_SN_076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                //        "HME_EO_JOB_SN_076", "HME", String.join(",", materialLotCodeList)));
                HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
                hmeEoJobMaterialVO.setJobType(dto.getJobType());
                hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterialVO.setDeleteFlag(YES);
                hmeEoJobMaterialVO.setMaterialLotCode(materialLotCodeList.get(0));
                hmeEoJobMaterialVO.setJobId(dto.getJobId());
                hmeEoJobMaterialVO.setEoId(dto.getEoId());
                hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
                hmeEoJobMaterialVO.setRouterStepId(dto.getEoStepId());
                List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = new ArrayList<HmeEoJobMaterialVO>();
                hmeEoJobMaterialVOList.add(hmeEoJobMaterialVO);
                return hmeEoJobMaterialVOList;
            }
        }

        //?????????????????????????????????woId modify by yuchao.wang for fang.pang at 2020.9.14
        //??????????????????WO??????????????????EO?????? modify by yuchao.wang for lu.bai at 2020.9.16
        Map<String, Double> componentQtyMap = new HashMap<>();
        Map<String, String> bomComponentIdMap = new HashMap<>();
        String virtualFlag = "";
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (Objects.isNull(mtEo)) {
                // ????????????SN????????????EO
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }
            dto.setWorkOrderId(mtEo.getWorkOrderId());
            if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                mtEoVO20List.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getComponentQty());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });

                //?????????????????????wkc???????????????????????????????????????
                List<MtEoVO20> mtEoVO21List = mtEoVO20List.stream().filter(s -> s.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtEoVO21List)) {
                    //??????BOM?????????????????????
                    MtExtendSettings virtualFlagAttr = new MtExtendSettings();
                    virtualFlagAttr.setAttrName("lineAttribute8");
                    List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_bom_component_attr", "BOM_COMPONENT_ID", mtEoVO21List.get(0).getBomComponentId(),
                            Collections.singletonList(virtualFlagAttr));
                    if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                        virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
                    }
                }
            }
        } else {
            if (CollectionUtils.isNotEmpty(woComponentList)) {
                woComponentList.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getPerQty() * dto.getPrepareQty().doubleValue());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });

                //?????????????????????wkc???????????????????????????????????????
                List<MtWorkOrderVO8> mtEoVO21List = woComponentList.stream().filter(s -> s.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtEoVO21List)) {
                    //??????BOM?????????????????????
                    MtExtendSettings virtualFlagAttr = new MtExtendSettings();
                    virtualFlagAttr.setAttrName("lineAttribute8");
                    List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_bom_component_attr", "BOM_COMPONENT_ID", mtEoVO21List.get(0).getBomComponentId(),
                            Collections.singletonList(virtualFlagAttr));
                    if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                        virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
                    }
                }
            }
        }

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckUom(tenantId, mtMaterialLot.getPrimaryUomId(), mtMaterialLot.getMaterialId());

        String bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");

        //??????????????????
        hmeEoJobSnLotMaterialRepository.CheckSoNum(tenantId, dto.getWorkOrderId(), dto.getMaterialLotId(), bomComponentId);

        //???????????????
        hmeEoJobSnLotMaterialRepository.CheckMaterialAttr(tenantId, dto.getMaterialLotId());

        //???????????????????????????
        MtExtendSettings productionVersionAttr = new MtExtendSettings();
        productionVersionAttr.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(),
                Collections.singletonList(productionVersionAttr));
        String productionVersion = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
        }

        // ??????????????????????????????????????????????????????
        //??????????????????????????? modify by yuchao.wang for lu.bai at 2020.9.22
//        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) &&
//                StringUtils.isNotBlank(dto.getBydMaterialId()) && StringUtils.isBlank(dto.getJobMaterialId())) {
//            HmeEoJobSnVO2 initParam = new HmeEoJobSnVO2();
//            initParam.setWorkcellId(dto.getWorkcellId());
//            initParam.setJobId(dto.getJobId());
//            initParam.setEoId(dto.getEoId());
//            initParam.setSnMaterialId(dto.getSnMaterialId());
//            initParam.setVirtualFlag(virtualFlag);
//            List<HmeEoJobMaterialVO> bydMaterial = initJobMaterial(tenantId, dto.getMaterialId(), false,
//                    BigDecimal.ONE, null, initParam);
//            if (CollectionUtils.isNotEmpty(bydMaterial)) {
//                dto.setJobMaterialId(bydMaterial.get(0).getJobMaterialId());
//                dto.setMaterialCode(bydMaterial.get(0).getMaterialCode());
//                dto.setMaterialName(bydMaterial.get(0).getMaterialName());
//                dto.setPrimaryUomCode(bydMaterial.get(0).getPrimaryUomCode());
//            }
//        }

        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();

        //V20200831 modify by penglin.sui for lu.bai ???????????? + ?????? +???????????????????????????????????????????????????????????????
//        HmeEoJobMaterial hmeEoJobMaterialPara = new HmeEoJobMaterial();
//        hmeEoJobMaterialPara.setTenantId(tenantId);
//        hmeEoJobMaterialPara.setWorkcellId(dto.getWorkcellId());
//        hmeEoJobMaterialPara.setMaterialId(dto.getMaterialId());
//        hmeEoJobMaterialPara.setMaterialLotCode(dto.getMaterialLotCode());
//        HmeEoJobMaterial existMaterial = this.selectOne(hmeEoJobMaterialPara);

//        if(Objects.isNull(existMaterial)){
//            //V20200831 modify by penglin.sui for lu.bai ???????????? + ?????? +???????????? ?????????????????????????????????????????????????????????
//            List<HmeEoJobMaterial> existMaterialList = this.selectByCondition(Condition.builder(HmeEoJobMaterial.class)
//                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
//                            .andIsNull(HmeEoJobMaterial.FIELD_MATERIAL_LOT_CODE)
//                            .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
//                            .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, dto.getMaterialId())).build());
//            if(CollectionUtils.isEmpty(existMaterialList)){
//                //??????
//                hmeEoJobMaterial.setTenantId(tenantId);
//                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
//                hmeEoJobMaterial.setMaterialId(dto.getMaterialId());
//                hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
//                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
//                hmeEoJobMaterial.setBydMaterialId(dto.getBydMaterialId());
//                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
//                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
//                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
//                hmeEoJobMaterial.setProductionVersion(productionVersion);
//                bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");
//                hmeEoJobMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
//                hmeEoJobMaterial.setVirtualFlag(virtualFlag);
//                hmeEoJobMaterialMapper.insertSelective(hmeEoJobMaterial);
//            }else{
//                //??????material_lot_id ???release_qty
//                BeanUtils.copyProperties(existMaterialList.get(0),hmeEoJobMaterial);
//                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
//                bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");
//                hmeEoJobMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
//                if(componentQtyMap.containsKey(mtMaterialLot.getMaterialId())){
//                    hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
//                }
//            }
//        }else{
//            //??????release_qty
//            BeanUtils.copyProperties(existMaterial,hmeEoJobMaterial);
//            bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");
//            hmeEoJobMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
//            if(componentQtyMap.containsKey(mtMaterialLot.getMaterialId())){
//                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
//            }
//        }

        HmeEoJobMaterial existMaterial = this.selectByPrimaryKey(dto.getJobMaterialId());
        if (Objects.isNull(existMaterial)) {
            //V20200831 modify by penglin.sui for lu.bai ???????????? + ?????? +???????????? ?????????????????????????????????????????????????????????
            //??????
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterial.setMaterialId(mtMaterialLot.getMaterialId());
            hmeEoJobMaterial.setSnMaterialId(dto.getSnMaterialId());
            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterial.setBydMaterialId(dto.getBydMaterialId());
            hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
            hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
            hmeEoJobMaterial.setProductionVersion(productionVersion);
            if (HmeConstants.ConstantValue.NO.equals(substitsuteFlag)) {
                hmeEoJobMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
            } else {
                hmeEoJobMaterial.setReleaseQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            }
            hmeEoJobMaterial.setVirtualFlag(virtualFlag);
            hmeEoJobMaterial.setJobId(dto.getJobId());
            hmeEoJobMaterial.setEoId(dto.getEoId());
            hmeEoJobMaterial.setBomComponentId(bomComponentId);
            this.insertSelective(hmeEoJobMaterial);
            dto.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
        } else {
            //??????material_lot_id ???release_qty
            BeanUtils.copyProperties(existMaterial, hmeEoJobMaterial);
            hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
            hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
            hmeEoJobMaterial.setProductionVersion(productionVersion);
            hmeEoJobMaterial.setVirtualFlag(virtualFlag);
            if (componentQtyMap.containsKey(mtMaterialLot.getMaterialId())) {
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
            }
        }

        if (StringUtils.isNotBlank(bomComponentId)) {
            //???????????????????????????
            MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
            lineAttribute7Attr.setAttrName("lineAttribute7");
            List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(lineAttribute7Attr));
            String bomVersion = "";
            if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
                bomVersion = mtBomExtendAttrVOList.get(0).getAttrValue();
            }

            if (StringUtils.isNotBlank(bomVersion)) {
                if (!productionVersion.equals(bomVersion)) {
                    //?????????????????????${1}???????????????????????????${2}????????????
                    throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_065", "HME", productionVersion, bomVersion));
                }
            }
            dto.setBomComponentVersion(bomVersion);
        }

//        BeanUtils.copyProperties(hmeEoJobMaterial, dto);
//        if (StringUtils.isBlank(dto.getMaterialName())) {
//            MtMaterialVO mtMaterialVO =
//                    mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
//            dto.setMaterialCode(mtMaterialVO.getMaterialCode());
//            dto.setMaterialName(mtMaterialVO.getMaterialName());
//            dto.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
//        }

        HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
        hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
        hmeEoJobMaterialVO2.setJobId(dto.getJobId());
        hmeEoJobMaterialVO2.setMaterialId(dto.getSnMaterialId());
        hmeEoJobMaterialVO2.setJobType(dto.getJobType());
        hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
        hmeEoJobMaterialVO2.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobMaterialVO2.setEoId(dto.getEoId());
        hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO2.setEoStepId(dto.getEoStepId());
        log.info("====================?????? begin====================:" + hmeEoJobMaterialVO2.getEoId());
        return jobSnLimitJobMaterialQuery(tenantId, hmeEoJobMaterialVO2);
    }

    @Override
    public void materialOutSite(Long tenantId, List<HmeEoJobMaterial> dtoList) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.materialOutSite tenantId=[{}],dtoList=[{}]", tenantId, dtoList);
        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dtoList.get(0).getJobId());

        for (HmeEoJobMaterial material : dtoList) {
            MtBomComponent bomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, material.getBomComponentId());
            // ?????????????????????????????????
            if (!"PHANTOM".equals(bomComponent.getBomComponentType())) {
                MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                // ????????????ID
                assembleProcessVo5.setEoId(material.getEoId());
                assembleProcessVo5.setMaterialId(material.getMaterialId());
                String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, material.getEoId());
                assembleProcessVo5.setRouterId(eoRouterId);
                // ??????????????????????????????1
                assembleProcessVo5.setTrxAssembleQty(Double.valueOf(material.getReleaseQty().toString()));
                assembleProcessVo5.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                assembleProcessVo5.setWorkcellId(material.getWorkcellId());
                assembleProcessVo5.setEventRequestId(eventRequestId);

                if (hmeEoJobSn.getShiftId() != null) {
                    MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());

                    assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
                    assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());
                }

                MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                mtEoComponentVO.setEoId(material.getEoId());
                mtEoComponentVO.setMaterialId(material.getMaterialId());
                mtEoComponentVO.setOperationId(hmeEoJobSn.getOperationId());

                // ??????????????????
                String locatorGet = mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                if (StringUtils.isBlank(locatorGet)) {
                    // ????????????BOM?????????????????????
                    throw new MtException("HME_EO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_014", "HME"));
                }
                assembleProcessVo5.setLocatorId(locatorGet);
                if (StringUtils.isNotBlank(material.getBomComponentId())) {
                    assembleProcessVo5.setBomComponentId(material.getBomComponentId());
                } else {
                    assembleProcessVo5.setAssembleExcessFlag(YES);
                }
                assembleProcessVo5.setRouterStepId(hmeEoJobSn.getEoStepId());
                mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);
            /*
             * 2020.08.01????????????????????????????????????
            if (StringUtils.isNotBlank(dto.getMaterialLotId())) {
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());

                MtMaterialLotVO1 mtMaterialLotVo1 = new MtMaterialLotVO1();
                mtMaterialLotVo1.setMaterialLotId(dto.getMaterialLotId());
                mtMaterialLotVo1.setPrimaryUomId(mtMaterialVO.getPrimaryUomId());
                // ??????????????????????????????1
                mtMaterialLotVo1.setTrxPrimaryUomQty(dto.getReleaseQty().doubleValue());

                mtMaterialLotVo1.setEventRequestId(eventRequestId);

                if (StringUtils.isNotEmpty(mtMaterialVO.getSecondaryUomId())) {
                    mtMaterialLotVo1.setSecondaryUomId(mtMaterialVO.getSecondaryUomId());
                    mtMaterialLotVo1.setTrxSecondaryUomQty(0.0D);
                }
                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVo1);
            }
            */
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobMaterialVO> deleteMaterial(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobMaterialRepositoryImpl.deleteMaterial tenantId=[{}],dto=[{}]", tenantId, dto);
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        //V20201002 modify by penglin.sui for lu.bai ????????????????????????????????????????????????
        Boolean inComponentFlag = false;
        Boolean deleteFlag = false;
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            // ???????????????????????????wkc???????????????????????????????????????
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        } else {
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderVO7.setOperationId(dto.getOperationId());
            woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
        }
        List<String> componentMaterialIdList = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
            componentMaterialIdList = mtEoVO20List.stream().map(MtEoVO20::getMaterialId).collect(Collectors.toList());
        } else if (CollectionUtils.isNotEmpty(woComponentList)) {
            componentMaterialIdList = woComponentList.stream().map(MtWorkOrderVO8::getMaterialId).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(componentMaterialIdList)) {
            if (componentMaterialIdList.contains(dto.getMaterialId())) {
                inComponentFlag = true;
            }
        }
        if (!inComponentFlag) {
            if (hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, dto.getMaterialId(), dto, woComponentList, mtEoVO20List)) {
                deleteFlag = true;
            }
        }

        HmeEoJobMaterial hmeEoJobMaterialPara = new HmeEoJobMaterial();
        hmeEoJobMaterialPara.setTenantId(tenantId);
        hmeEoJobMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobMaterialPara.setMaterialId(dto.getMaterialId());
        hmeEoJobMaterialPara.setMaterialLotCode(dto.getMaterialLotCode());
        HmeEoJobMaterial hmeEoJobMaterial = self().selectOne(hmeEoJobMaterialPara);
        if (Objects.nonNull(hmeEoJobMaterial)) {

            //V20200925 modify by penglin.sui for lu.bai ?????????????????????????????????
            if (HmeConstants.ConstantValue.ONE.equals(hmeEoJobMaterial.getIsIssued())) {
                //???????????????${1}????????????,????????????????????????
                throw new MtException("HME_EO_JOB_SN_077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_077", "HME", dto.getMaterialLotCode()));
            }
            if (deleteFlag) {
                hmeEoJobMaterialMapper.deleteByPrimaryKey(hmeEoJobMaterial.getJobMaterialId());
            } else {
                HmeEoJobMaterial hmeEoJobLotMaterialUpdate = new HmeEoJobMaterial();
                hmeEoJobLotMaterialUpdate.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
                hmeEoJobLotMaterialUpdate.setMaterialLotId("");
                hmeEoJobLotMaterialUpdate.setMaterialLotCode("");
                hmeEoJobLotMaterialUpdate.setIsReleased(HmeConstants.ConstantValue.ZERO);
                hmeEoJobLotMaterialUpdate.setLocatorId("");
                hmeEoJobLotMaterialUpdate.setLotCode("");
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterialUpdate);
            }
        }

        //SN????????????????????????
        HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
        hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
        hmeEoJobMaterialVO2.setJobId(dto.getJobId());
        hmeEoJobMaterialVO2.setMaterialId(dto.getSnMaterialId());
        hmeEoJobMaterialVO2.setJobType(dto.getJobType());
        hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
        hmeEoJobMaterialVO2.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobMaterialVO2.setEoId(dto.getEoId());
        hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO2.setEoStepId(dto.getEoStepId());
        return jobSnLimitJobMaterialQuery(tenantId, hmeEoJobMaterialVO2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobMaterialVO updateIsReleased(Long tenantId, HmeEoJobMaterialVO dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if (StringUtils.isBlank(dto.getMaterialLotId())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobMaterial updateHmeEoJobMaterial = new HmeEoJobMaterial();
        updateHmeEoJobMaterial.setJobMaterialId(dto.getJobMaterialId());
        updateHmeEoJobMaterial.setIsReleased(dto.getIsReleased());
        hmeEoJobMaterialMapper.updateByPrimaryKeySelective(updateHmeEoJobMaterial);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobMaterialVO> batchUpdateIsReleased(Long tenantId, List<HmeEoJobMaterialVO> dtoList) {
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (HmeEoJobMaterialVO dto : dtoList
            ) {
                updateIsReleased(tenantId, dto);
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobMaterialVO> deleteReleaseMaterial(Long tenantId, HmeEoJobMaterialVO dto) {
        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOS = new ArrayList<>();
        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOs = hmeEoJobMaterialMapper.selectAllMaterialLotBindMaterialLot(tenantId, dto.getMaterialLotCode(), dto.getJobMaterialId());
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialVOs)) {
            HmeEoJobMaterialVO hmeEoJobMaterialVO = hmeEoJobMaterialVOs.get(0);
            hmeEoJobMaterialVO.setMaterialId(dto.getMaterialId());
            hmeEoJobMaterialVO.setMaterialCode(dto.getMaterialCode());
            hmeEoJobMaterialVO.setMaterialName(dto.getMaterialName());
            hmeEoJobMaterialVO.setPrimaryUomCode(dto.getPrimaryUomCode());
            hmeEoJobMaterialVO.setReleaseQty(dto.getReleaseQty());
            hmeEoJobMaterialVO.setLineNumber(dto.getLineNumber());
            hmeEoJobMaterialVO.setMaterialLotCode(dto.getMaterialLotCode());
            deleteMaterial(tenantId, hmeEoJobMaterialVO);
            hmeEoJobMaterialVOS = releaseScan(tenantId, dto);
        }
        return hmeEoJobMaterialVOS;
    }

}
