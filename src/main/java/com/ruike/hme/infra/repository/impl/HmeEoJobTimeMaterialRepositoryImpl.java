package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.api.dto.HmeEoJobLotMaterialDTO;
import com.ruike.hme.api.dto.HmeEoJobTimeMaterialDTO;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.HmePrepareMaterialVO;
import com.ruike.hme.infra.constant.HmeConstants;

import com.ruike.hme.infra.mapper.HmeEoJobSnLotMaterialMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;
import com.ruike.wms.api.dto.WmsMaterialLotQryDTO;
import com.ruike.wms.api.dto.WmsMaterialLotQryResultDTO;
import com.ruike.wms.infra.mapper.WmsMaterialLotMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;
import com.ruike.hme.domain.repository.HmeEoJobTimeMaterialRepository;
import com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;
import com.ruike.hme.infra.mapper.HmeEoJobTimeMaterialMapper;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * ??????????????????-???????????? ???????????????
 *
 * @author liyuan.lv@hand-china.com 2020-03-22 17:08:55
 */
@Slf4j
@Component
public class HmeEoJobTimeMaterialRepositoryImpl extends BaseRepositoryImpl<HmeEoJobTimeMaterial>
        implements HmeEoJobTimeMaterialRepository {
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private WmsMaterialLotMapper wmsMaterialLotMapper;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

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
    public HmeEoJobTimeMaterialVO initTimeMaterial(Long tenantId,
                                                   String bomComponentId,
                                                   String materialId,
                                                   String availableTime,
                                                   BigDecimal componentQty,
                                                   HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.initTimeMaterial tenantId=[{}],bomComponentId=[{}],materialId=[{}]" +
                ",availableTime=[{}],componentQty=[{}],dto=[{}]", tenantId, bomComponentId, materialId, availableTime, componentQty, dto);
        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterial.setTenantId(tenantId);
        hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterial.setMaterialId(materialId);

        HmeEoJobTimeMaterial currentTimeMaterial = this.selectOne(hmeEoJobTimeMaterial);

        HmeEoJobTimeMaterialVO jobTimeMaterialVO = new HmeEoJobTimeMaterialVO();
        if (Objects.isNull(currentTimeMaterial)) {
            hmeEoJobTimeMaterial.setWorkDate(dto.getSiteInDate());
            hmeEoJobTimeMaterial.setShiftId(dto.getShiftId());
            hmeEoJobTimeMaterial.setReleaseQty(componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty()));
            hmeEoJobTimeMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobTimeMaterial);

            BeanUtils.copyProperties(hmeEoJobTimeMaterial, jobTimeMaterialVO);
        }else{
            currentTimeMaterial.setReleaseQty(componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty()));
            hmeEoJobTimeMaterialMapper.updateByPrimaryKey(currentTimeMaterial);

            BeanUtils.copyProperties(currentTimeMaterial, jobTimeMaterialVO);
        }
        // ????????????
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobTimeMaterial.getMaterialId());
        jobTimeMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
        jobTimeMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
        jobTimeMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
        jobTimeMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
        if(componentQty.compareTo(BigDecimal.ZERO) > 0) {
            jobTimeMaterialVO.setWkcMatchedFlag(YES);
        }
        jobTimeMaterialVO.setRemainQty(Objects.isNull(jobTimeMaterialVO.getRemainQty()) ? BigDecimal.ZERO : jobTimeMaterialVO.getRemainQty());
        //jobTimeMaterialVO.setReleaseQty(BigDecimal.ZERO);
        jobTimeMaterialVO.setComponentQty(Objects.isNull(componentQty) ? 0D : componentQty.doubleValue());

        //??????????????????
        if(StringUtils.isNotBlank(bomComponentId)) {
            MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
            lineAttribute7Attr.setAttrName("lineAttribute7");
            List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(lineAttribute7Attr));
            if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
                if (StringUtils.isNotBlank(mtBomExtendAttrVOList.get(0).getAttrValue())) {
                    jobTimeMaterialVO.setBomComponentVersion(mtBomExtendAttrVOList.get(0).getAttrValue());
                }
            }
        }

        return jobTimeMaterialVO;
    }

    @Override
    public List<HmeEoJobTimeMaterialVO> initTimeMaterialList(Long tenantId,
                                                             String materialId,
                                                             String availableTime,
                                                             BigDecimal componentQty,
                                                             HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.initTimeMaterialList tenantId=[{}],materialId=[{}]" +
                ",availableTime=[{}],componentQty=[{}],dto=[{}]", tenantId, materialId, availableTime, componentQty, dto);

        //??????????????????
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        List<HmeEoJobTimeMaterialVO> jobTimeMaterialVOList = new ArrayList<HmeEoJobTimeMaterialVO>();
        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterial.setTenantId(tenantId);
        hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterial.setMaterialId(materialId);
        List<HmeEoJobTimeMaterial> currentTimeMaterialList = this.select(hmeEoJobTimeMaterial);
        HmeEoJobTimeMaterialVO jobTimeMaterialVO2 = new HmeEoJobTimeMaterialVO();
        BigDecimal releaseQty = componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty());
        if(CollectionUtils.isNotEmpty(currentTimeMaterialList)){
            for (HmeEoJobTimeMaterial hmeEoJobTimeMaterial2:currentTimeMaterialList
                 ) {
                hmeEoJobTimeMaterial2.setReleaseQty(releaseQty);
                BeanUtils.copyProperties(hmeEoJobTimeMaterial2, jobTimeMaterialVO2);
                jobTimeMaterialVOList.add(jobTimeMaterialVO2);
            }
            //V20201023 modify by penglin.sui ????????????
            if (CollectionUtils.isNotEmpty(jobTimeMaterialVOList)) {
                List<List<HmeEoJobTimeMaterialVO>> splitSqlList = splitSqlList(jobTimeMaterialVOList, 200);
                for (List<HmeEoJobTimeMaterialVO> domains : splitSqlList) {
                    hmeEoJobTimeMaterialMapper.batchUpdateTimeMaterial(userId,domains);
                }
            }
        }else{
            hmeEoJobTimeMaterial.setWorkDate(dto.getSiteInDate());
            hmeEoJobTimeMaterial.setShiftId(dto.getShiftId());
            hmeEoJobTimeMaterial.setReleaseQty(releaseQty);
            hmeEoJobTimeMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobTimeMaterial);
            BeanUtils.copyProperties(hmeEoJobTimeMaterial, jobTimeMaterialVO2);
            jobTimeMaterialVOList.add(jobTimeMaterialVO2);
        }

        // ????????????
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobTimeMaterial.getMaterialId());

        List<MtWorkOrderVO8> woComponentList = new ArrayList<>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<>();
        List<String> bomComponentIdList = new ArrayList<>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                mtWorkOrderVO7.setRouterStepId(dto.getEoStepId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).collect(Collectors.toList());
                }
            }
        } else {
            // ????????????????????????wkc??????????????????
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // ????????????wkc???????????????????????????
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                    bomComponentIdList = mtEoVO20List.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                }
            }
        }
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)){
            List<String> attrNameList = new ArrayList<>();
            attrNameList.add("lineAttribute7");
            mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
        }
        for (HmeEoJobTimeMaterialVO jobTimeMaterialVO:jobTimeMaterialVOList
             ) {
            jobTimeMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
            jobTimeMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
            jobTimeMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
            jobTimeMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
            jobTimeMaterialVO.setReleaseQtyChangeFlag(YES);

            String bomComponentId = "";
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobTimeMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        //????????????0????????????N modify by yuchao.wang for lu.bai at 2020.11.10
                        if (!YES.equals(dto.getReworkFlag()) && componentOptional.get().getPerQty() > 0) {
                            jobTimeMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                        }
                        if (BigDecimal.valueOf(componentOptional.get().getPerQty()).compareTo(BigDecimal.ZERO) > 0) {
                            jobTimeMaterialVO.setWkcMatchedFlag(YES);
                        }
                        bomComponentId = componentOptional.get().getBomComponentId();
                    }
                }
            } else {
                // ????????????????????????wkc??????????????????
                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobTimeMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        //????????????0????????????N modify by yuchao.wang for lu.bai at 2020.11.10
                        if (!YES.equals(dto.getReworkFlag()) && componentOptional.get().getPreQty() > 0) {
                            jobTimeMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                        }
                        if (BigDecimal.valueOf(componentOptional.get().getPreQty()).compareTo(BigDecimal.ZERO) > 0) {
                            jobTimeMaterialVO.setWkcMatchedFlag(YES);
                        }
                        bomComponentId = componentOptional.get().getBomComponentId();
                    }
                }
            }

            //??????????????????
            if(StringUtils.isNotBlank(bomComponentId) && CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                String bomComponentId2 = bomComponentId;
                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(bomComponentId2)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                        jobTimeMaterialVO.setBomComponentVersion(mtExtendAttrVO1List2.get(0).getAttrValue());
                    }
                }
            }
        }
        return jobTimeMaterialVOList;
    }

    /**
     *
     * @Description ?????????????????????????????????
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:53
     * @param tenantId ??????ID
     * @param materialId ??????ID
     * @param availableTime ????????????
     * @param componentQty ????????????
     * @param dto  ???????????????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTimeMaterialWithoutQuery(Long tenantId, String materialId, String availableTime, BigDecimal componentQty, HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.initTimeMaterialWithoutQuery tenantId=[{}],materialId=[{}]" +
                ",availableTime=[{}],componentQty=[{}],dto=[{}]", tenantId, materialId, availableTime, componentQty, dto);

        //??????????????????
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterial.setTenantId(tenantId);
        hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterial.setMaterialId(materialId);
        List<HmeEoJobTimeMaterial> currentTimeMaterialList = this.select(hmeEoJobTimeMaterial);

        BigDecimal releaseQty = componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty());

        if (CollectionUtils.isNotEmpty(currentTimeMaterialList)) {
            List<HmeEoJobTimeMaterialVO> insertList = new ArrayList<HmeEoJobTimeMaterialVO>();
            for (HmeEoJobTimeMaterial hmeEoJobTimeMaterial2 : currentTimeMaterialList) {
                hmeEoJobTimeMaterial2.setReleaseQty(releaseQty);
                HmeEoJobTimeMaterialVO jobTimeMaterialVO2 = new HmeEoJobTimeMaterialVO();
                BeanUtils.copyProperties(hmeEoJobTimeMaterial2, jobTimeMaterialVO2);
                insertList.add(jobTimeMaterialVO2);
            }

            //V20201023 modify by penglin.sui ????????????
            if (CollectionUtils.isNotEmpty(insertList)) {
                List<List<HmeEoJobTimeMaterialVO>> splitSqlList = splitSqlList(insertList, 200);
                for (List<HmeEoJobTimeMaterialVO> domains : splitSqlList) {
                    hmeEoJobTimeMaterialMapper.batchUpdateTimeMaterial(userId, domains);
                }
            }
        } else {
            hmeEoJobTimeMaterial.setWorkDate(dto.getSiteInDate());
            hmeEoJobTimeMaterial.setShiftId(dto.getShiftId());
            hmeEoJobTimeMaterial.setReleaseQty(releaseQty);
            hmeEoJobTimeMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobTimeMaterial);
        }
    }

    @Override
    public List<HmeEoJobTimeMaterialVO> matchedJobTimeMaterialQuery(Long tenantId,
                                                                    HmeEoJobMaterialVO dto,
                                                                    List<HmeEoJobTimeMaterial> timeMaterialList) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.matchedJobTimeMaterialQuery tenantId=[{}]," +
                "dto=[{}],timeMaterialList=[{}]", tenantId, dto, timeMaterialList);
        List<HmeEoJobTimeMaterialVO> resultVOList = new ArrayList<>();
        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = new ArrayList<>();;
        List<HmeEoJobTimeMaterial> eoJobTimeMaterials = new ArrayList<HmeEoJobTimeMaterial>();
        Map<String, Long> sortedMap = new HashMap<String, Long>();

        if (CollectionUtils.isNotEmpty(timeMaterialList)) {
            eoJobTimeMaterials = timeMaterialList;
        } else {
            HmeEoJobTimeMaterial jobLotMaterialParam = new HmeEoJobTimeMaterial();
            jobLotMaterialParam.setTenantId(tenantId);
            jobLotMaterialParam.setWorkcellId(dto.getWorkcellId());
            if (YES.equals(dto.getIsWorkcellQuery())) {
                eoJobTimeMaterials = hmeEoJobTimeMaterialMapper.queryEoJobTimeMaterialOfNotNullMaterialLot(tenantId, jobLotMaterialParam);
            } else {
                eoJobTimeMaterials = hmeEoJobTimeMaterialMapper.queryEoJobTimeMaterial(tenantId, jobLotMaterialParam);
            }
        }

        //???????????????????????? modify by yuchao.wang at 2020.9.23
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        if (!YES.equals(dto.getIsWorkcellQuery())) {
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
            } else {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtEoVO19 mtEoVO19 = new MtEoVO19();
                    mtEoVO19.setEoId(dto.getEoId());
                    mtEoVO19.setOperationId(dto.getOperationId());
                    mtEoVO19.setRouterStepId(dto.getEoStepId());
                    mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                }
            }
        }

        HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
        hmeEoJobMaterialVO.setJobType(dto.getJobType());
        hmeEoJobMaterialVO.setEoId(dto.getEoId());
        hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());

        if(CollectionUtils.isNotEmpty(eoJobTimeMaterials)) {
            //????????????????????????????????? add by yuchao.wang for lu.bai at 2020.9.17
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && StringUtils.isNotBlank(dto.getMaterialId())) {
                MtMaterial material = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                if(Objects.nonNull(material)) {
                    List<HmePrepareMaterialVO> jobTimeMaterialList = hmeEoJobSnMapper.prepareEoJobTimeMaterialQuery(tenantId,
                            dto.getSiteId(), dto.getWorkOrderId(), material.getMaterialCode());
                    if (CollectionUtils.isNotEmpty(jobTimeMaterialList)) {
                        List<String> materialIdList = new ArrayList<>();
                        jobTimeMaterialList.forEach(item -> {
                            sortedMap.put(item.getMaterialId(), item.getLineNumber());
                            materialIdList.add(item.getMaterialId());
                        });

                        //V20200930 modify by pengli.sui for lu.bai ???????????????????????????
                        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList2 = eoJobTimeMaterials.stream().filter(item -> !materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList2)){
                            for (HmeEoJobTimeMaterial hmeEoJobTimeMaterial:hmeEoJobTimeMaterialList2
                            ) {
                                if(hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId,material.getMaterialId(),hmeEoJobMaterialVO,null,null)){
                                    materialIdList.add(hmeEoJobTimeMaterial.getMaterialId());
                                }
                            }
                        }

                        hmeEoJobTimeMaterialList = eoJobTimeMaterials.stream().filter(item -> materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                    }
                }
            } else {
                hmeEoJobTimeMaterialList = eoJobTimeMaterials;

                if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                    for (MtEoVO20 mtEoVO20:mtEoVO20List
                    ) {
                        sortedMap.put(mtEoVO20.getMaterialId(), mtEoVO20.getSequence());
                    }
                }
            }
        }

        //????????????remainQty add by yuchao.wang for lu.bai at 2020.9.14
        Map<String, BigDecimal> releaseQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials = hmeEoJobSnLotMaterialRepository.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())).build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterials)) {
                //V20201003 modify by penglin.sui for lu.bai ?????????????????????
                List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials2 = hmeEoJobSnLotMaterials.stream().filter(item -> item.getReleaseQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                hmeEoJobSnLotMaterials2.forEach(item -> {
                    String key = item.getMaterialId() + "-" + StringUtils.trimToEmpty(item.getProductionVersion());
                    if (releaseQtyMap.containsKey(key)) {
                        releaseQtyMap.put(key, releaseQtyMap.get(key).add(item.getReleaseQty()));
                    } else {
                        releaseQtyMap.put(key, item.getReleaseQty());
                    }
                });
            }

            String isInComponentFlag = HmeConstants.ConstantValue.NO;
            for (HmeEoJobTimeMaterial material : hmeEoJobTimeMaterialList) {

                //????????????????????????????????? modify by yuchao.wang for lu.bai at 2020.12.15
                if (!YES.equals(dto.getIsWorkcellQuery())) {
                    //???????????????0????????????????????????????????????????????????
                    if (Objects.isNull(material.getReleaseQty()) || material.getReleaseQty().compareTo(BigDecimal.ZERO) == 0) {
                        if (!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), hmeEoJobMaterialVO, null, null)) {
                            continue;
                        }
                    }
                }

                isInComponentFlag = HmeConstants.ConstantValue.NO;
                HmeEoJobTimeMaterialVO jobTimeMaterialVO = new HmeEoJobTimeMaterialVO();
                BeanUtils.copyProperties(material, jobTimeMaterialVO);
                jobTimeMaterialVO.setComponentQty(0.0);
                jobTimeMaterialVO.setRemainQty(BigDecimal.ZERO);

                // ????????????
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                jobTimeMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
                jobTimeMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
                jobTimeMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
                if (StringUtils.isNotBlank(material.getMaterialLotId())) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                    jobTimeMaterialVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                    jobTimeMaterialVO.setRemainQty(Objects.isNull(mtMaterialLot.getPrimaryUomQty()) ? BigDecimal.ZERO : new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                }

                jobTimeMaterialVO.setReleaseQtyChangeFlag(YES);
                jobTimeMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);

                String bomComponentId = "";
                BigDecimal sumQty = BigDecimal.ZERO;
                //???????????????????????????????????????????????????
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    if (CollectionUtils.isNotEmpty(woComponentList)) {
                        //????????????WO???????????????????????? add by yuchao.wang for lu.bai at 2020.9.14
                        List<MtWorkOrderVO8> componentQtyList = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(componentQtyList)) {
                            //????????????0????????????N modify by yuchao.wang for lu.bai at 2020.11.10
                            sumQty = BigDecimal.valueOf(componentQtyList.stream().mapToDouble(MtWorkOrderVO8::getPerQty).sum());
                            if (!YES.equals(dto.getReworkFlag()) && sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                jobTimeMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                            }
                            jobTimeMaterialVO.setComponentQty(sumQty.multiply(dto.getPrepareQty()).doubleValue());
                            if(sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                jobTimeMaterialVO.setWkcMatchedFlag(YES);
                            }
                            isInComponentFlag = YES;
                            bomComponentId = componentQtyList.get(0).getBomComponentId();
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(dto.getOperationId())) {
                        if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                            //??????EO????????????????????? add by yuchao.wang for lu.bai at 2020.9.14
                            List<MtEoVO20> componentQtyList = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(componentQtyList)) {
                                //????????????0????????????N modify by yuchao.wang for lu.bai at 2020.11.10
                                sumQty = BigDecimal.valueOf(componentQtyList.stream().mapToDouble(MtEoVO20::getComponentQty).sum());
                                if (!YES.equals(dto.getReworkFlag()) && sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                    jobTimeMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                                }
                                jobTimeMaterialVO.setComponentQty(sumQty.doubleValue());
                                if(sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                    jobTimeMaterialVO.setWkcMatchedFlag(YES);
                                }
                                isInComponentFlag = YES;
                                bomComponentId = componentQtyList.get(0).getBomComponentId();
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(bomComponentId)) {
                    if (hmeEoJobSnRepository.checkVirtualComponent(tenantId, bomComponentId) &&
                            !HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        continue;
                    }
                }
                if (!YES.equals(dto.getIsWorkcellQuery())) {
                    //V20200921 modify by penglin.sui for lu.bai ???????????????????????????????????????????????????
                    if (HmeConstants.ConstantValue.NO.equals(isInComponentFlag) && !hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), dto, woComponentList, mtEoVO20List)) {
                        continue;
                    }
                }
                String releaseQtyKey = jobTimeMaterialVO.getMaterialId() + "-" + StringUtils.trimToEmpty(jobTimeMaterialVO.getProductionVersion());
                jobTimeMaterialVO.setReleaseQty(releaseQtyMap.getOrDefault(releaseQtyKey, BigDecimal.ZERO));
                jobTimeMaterialVO.setLineNumber(sortedMap.getOrDefault(jobTimeMaterialVO.getMaterialId(), 99999L));

                //??????????????????
                if(StringUtils.isNotBlank(bomComponentId)) {
                    MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
                    lineAttribute7Attr.setAttrName("lineAttribute7");
                    List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                            Collections.singletonList(lineAttribute7Attr));
                    if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
                        if (StringUtils.isNotBlank(mtBomExtendAttrVOList.get(0).getAttrValue())) {
                            jobTimeMaterialVO.setBomComponentVersion(mtBomExtendAttrVOList.get(0).getAttrValue());
                        }
                    }
                }
                resultVOList.add(jobTimeMaterialVO);
            }
        }

        //??????LineNumber???????????? modify by yuchao.wang for lu.bai at 2020.9.25
        if (CollectionUtils.isNotEmpty(resultVOList)) {
            resultVOList = resultVOList.stream().sorted(
                    Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList());
        }

        return CollectionUtils.isNotEmpty(resultVOList) ? resultVOList : new ArrayList<HmeEoJobTimeMaterialVO>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobTimeMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.releaseScan tenantId=[{}],dto=[{}]", tenantId, dto);
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

        //V20201016 modify by penglin.sui for lu.bai ????????????????????????
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
        if(!HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
            //?????????????????????${1}??????????????????????????????????????????,?????????!
            throw new MtException("HME_EO_JOB_SN_116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_116", "HME", materialType));
        }

        //V20201008 modify by penglin.sui for lu.bai ??????????????????????????????
        String workcellCode = hmeEoJobTimeMaterialMapper.queryHaveBindWorkcell(tenantId,dto.getMaterialLotId(),dto.getWorkcellId());

        if(StringUtils.isNotBlank(workcellCode)){
            // ???????????????????????????${1}?????????
            throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_110", "HME",workcellCode ));
        }

        //V20200831 modify by penglin.sui for jiao.chen ????????????????????????????????????
        int count = hmeEoJobTimeMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobTimeMaterial.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobTimeMaterial.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobTimeMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_ID, mtMaterialLot.getMaterialId())
                        .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotId()))
                .build());
        if(count > 0){
            List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = new ArrayList<HmeEoJobTimeMaterialVO>();
            hmeEoJobTimeMaterialVOList.add(new HmeEoJobTimeMaterialVO(){{
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                setDeleteFlag("Y");
                setWorkcellId(dto.getWorkcellId());
                setMaterialId(mtMaterialLot.getMaterialId());
            }});
            return hmeEoJobTimeMaterialVOList;
        }
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
//                if (CollectionUtils.isNotEmpty(woComponentList)) {
//                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(mtMaterialLot.getMaterialId())).findFirst();
//                    if (!componentOptional.isPresent()) {
//                        // ????????????${1}?????????OK??????,????????????????????????
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        } else {
            // ????????????????????????wkc??????????????????
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // ????????????wkc???????????????????????????
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
//                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
//                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(mtMaterialLot.getMaterialId())).findFirst();
//                    if (!componentOptional.isPresent()) {
//                        // ????????????${1}?????????OK??????,????????????????????????
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        }

        //V20200831 modify by penglin.sui for jiao.chen ?????????????????????OK
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // ????????????${1}?????????OK??????,????????????????????????
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        HmeEoJobTimeMaterial param = new HmeEoJobTimeMaterial();
        param.setTenantId(tenantId);
        param.setWorkcellId(dto.getWorkcellId());
        param.setMaterialId(mtMaterialLot.getMaterialId());
        List<HmeEoJobTimeMaterial> scanMaterialList = hmeEoJobTimeMaterialMapper.select(param);

        if (CollectionUtils.isEmpty(scanMaterialList)) {
            // ????????????????????????????????????,?????????
//            throw new MtException("HME_EO_JOB_SN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_015", "HME"));
            //V20200925 modify by penglin.sui for lu.bai ????????????????????????????????????
            if(!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId,mtMaterialLot.getMaterialId(),dto,woComponentList,mtEoVO20List)
                    && !StringUtils.equals(dto.getPfType(), HmeConstants.PfType.REWORK)){
                //??????????????????????????????????????????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_078", "HME"));
            }
        }

        // ????????????????????????
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            //????????????????????????.${1}
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "???API:materialLotEnableValidate???"));
        }

        //??????????????????WO?????? ??????????????????woId????????????EO?????? modify by yuchao.wang for lu.bai at 2020.9.22
        Map<String, Double> componentQtyMap = new HashMap<>();
        Map<String, String> bomComponentIdMap = new HashMap<>();
        if(!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (Objects.isNull(mtEo)) {
                // ????????????SN????????????EO
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }
            dto.setWorkOrderId(mtEo.getWorkOrderId());

            // ???????????????????????????wkc???????????????????????????????????????
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                mtEoVO20List.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getComponentQty());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });
            }
        } else {
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderVO7.setOperationId(dto.getOperationId());
            if(CollectionUtils.isNotEmpty(woComponentList)){
                woComponentList.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getPerQty() * dto.getPrepareQty().doubleValue());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });
            }
        }

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //????????????
        hmeEoJobSnLotMaterialRepository.CheckUom(tenantId,mtMaterialLot.getPrimaryUomId(),mtMaterialLot.getMaterialId());

        String bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");

        //??????????????????
        hmeEoJobSnLotMaterialRepository.CheckSoNum(tenantId,dto.getWorkOrderId(),dto.getMaterialLotId(),bomComponentId);

        //???????????????
        hmeEoJobSnLotMaterialRepository.CheckMaterialAttr(tenantId,dto.getMaterialLotId());

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
//        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())
//                && StringUtils.isNotBlank(dto.getBydMaterialId()) && StringUtils.isBlank(dto.getJobMaterialId())) {
//            HmeEoJobSnVO2 initParam = new HmeEoJobSnVO2();
//            initParam.setWorkcellId(dto.getWorkcellId());
//            initParam.setJobId(dto.getJobId());
//            initParam.setEoId(dto.getEoId());
//            initParam.setSnMaterialId(dto.getSnMaterialId());
//            initParam.setBydMaterialId(dto.getBydMaterialId());
//            initParam.setLocatorId(mtMaterialLot.getLocatorId());
//            initParam.setLotCode(mtMaterialLot.getLot());
//            initParam.setProductionVersion(productionVersion);
//            String virtualFlag = HmeConstants.ConstantValue.NO;
//            if (!bomComponentIdMap.isEmpty()) {
//                //?????????????????????wkc???????????????????????????????????????
//                if (bomComponentIdMap.containsKey(mtMaterialLot.getMaterialId())) {
//                    //??????BOM?????????????????????
//                    MtExtendSettings virtualFlagAttr = new MtExtendSettings();
//                    virtualFlagAttr.setAttrName("lineAttribute8");
//                    List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
//                            "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIdMap.get(mtMaterialLot.getMaterialId()),
//                            Collections.singletonList(virtualFlagAttr));
//                    if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
//                        virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
//                    }
//                }
//            }
//            initParam.setVirtualFlag(StringUtils.isBlank(virtualFlag) ? HmeConstants.ConstantValue.NO : virtualFlag);
//            initTimeMaterial(tenantId, mtMaterialLot.getMaterialId(), dto.getAvailableTime(), dto.getReleaseQty(), initParam);
//        }

        //V20200831 modify by penglin.sui for jiao.chen ???????????? + ???????????????????????????????????????????????????????????????
        HmeEoJobTimeMaterial hmeEoJobTimeMaterialPara = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterialPara.setTenantId(tenantId);
        hmeEoJobTimeMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterialPara.setMaterialId(mtMaterialLot.getMaterialId());
        hmeEoJobTimeMaterialPara.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        HmeEoJobTimeMaterial existLotMaterial = this.selectOne(hmeEoJobTimeMaterialPara);

        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        String virtualFlag = HmeConstants.ConstantValue.NO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            //??????BOM?????????????????????
            MtExtendSettings virtualFlagAttr = new MtExtendSettings();
            virtualFlagAttr.setAttrName("lineAttribute8");
            List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(virtualFlagAttr));
            if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
            }
        }

        //V20200921 modify by penglin.sui for lu.bai ??????availableTime????????????
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(mtMaterialLot.getSiteId());
        materialSiteParam.setMaterialId(mtMaterialLot.getMaterialId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,
                materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }
        MtExtendVO materialSiteExtend = new MtExtendVO();
        materialSiteExtend.setTableName("mt_material_site_attr");
        materialSiteExtend.setKeyId(materialSiteId);
        List<MtExtendAttrVO> materialSiteExtendAttr =
                mtExtendSettingsRepository.attrPropertyQuery(tenantId, materialSiteExtend);
        // ?????????????????????????????????
        List<MtExtendAttrVO> mtAvailableTimeAttr = materialSiteExtendAttr.stream()
                .filter(result -> "attribute9".equals(result.getAttrName()))
                .collect(Collectors.toList());
        String availableTime = "0";
        if(CollectionUtils.isNotEmpty(mtAvailableTimeAttr)){
            availableTime = mtAvailableTimeAttr.get(0).getAttrValue();
        }

        if(Objects.isNull(existLotMaterial)){
            //?????? + ?????? + ??????????????????????????? ?????? + ?????? + ????????????????????????
            List<HmeEoJobTimeMaterial> existLotMaterialList = hmeEoJobTimeMaterialMapper.qqueryEoJobTimeMaterial2(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
            if(CollectionUtils.isNotEmpty(existLotMaterialList)){
                //??????material_lot_id ???release_qty??????availableTime
                BeanUtils.copyProperties(existLotMaterialList.get(0),hmeEoJobTimeMaterial);
                hmeEoJobTimeMaterial.setMaterialLotId(dto.getMaterialLotId());

                //?????????????????????wkc???????????????????????????????????????
                hmeEoJobTimeMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(productionVersion);
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
            }else {
                //????????????
                // ?????????????????????wkc???????????????????????????????????????
                hmeEoJobTimeMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobTimeMaterial.setTenantId(tenantId);
                hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobTimeMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                // ?????????????????????
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                hmeEoJobTimeMaterial.setBydMaterialId(dto.getBydMaterialId());
                hmeEoJobTimeMaterial.setMaterialLotId(dto.getMaterialLotId());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setVirtualFlag(StringUtils.isBlank(virtualFlag) ? HmeConstants.ConstantValue.NO : virtualFlag);
                hmeEoJobTimeMaterial.setProductionVersion(productionVersion);
                hmeEoJobTimeMaterial.setWorkDate(new Date(System.currentTimeMillis()));
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
                self().insertSelective(hmeEoJobTimeMaterial);
            }
        }else{
            //??????release_qty???availableTime
            BeanUtils.copyProperties(existLotMaterial,hmeEoJobTimeMaterial);
            hmeEoJobTimeMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
            hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
            hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
            hmeEoJobTimeMaterial.setProductionVersion(productionVersion);
            hmeEoJobTimeMaterial.setAvailableTime(availableTime);
            hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
            if(componentQtyMap.containsKey(mtMaterialLot.getMaterialId())){
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
            }
        }

        if(StringUtils.isNotBlank(bomComponentId)) {
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
            if(StringUtils.isNotBlank(bomVersion)){
                if (!productionVersion.equals(bomVersion)){
                    //?????????????????????${1}???????????????????????????${2}????????????
                    throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_065", "HME", productionVersion, bomVersion));
                }
            }
        }

        HmeEoJobTimeMaterial jobLotParam = new HmeEoJobTimeMaterial();
        jobLotParam.setTenantId(tenantId);
        jobLotParam.setWorkcellId(dto.getWorkcellId());
        jobLotParam.setMaterialId(mtMaterialLot.getMaterialId());
        jobLotParam.setMaterialLotId(dto.getMaterialLotId());
        //List<HmeEoJobTimeMaterial> currentMaterialList = hmeEoJobTimeMaterialMapper.select(jobLotParam);
        List<HmeEoJobTimeMaterial> currentMaterialList = hmeEoJobTimeMaterialMapper.queryEoJobTimeMaterial(tenantId,jobLotParam);

        WmsMaterialLotQryDTO materialLotQryDTO = new WmsMaterialLotQryDTO();
        materialLotQryDTO.setMaterialId(mtMaterialLot.getMaterialId());
        materialLotQryDTO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        List<WmsMaterialLotQryResultDTO> wmsMaterialLotQryResultDTOS = wmsMaterialLotMapper.selectBarCodeCondition(tenantId,materialLotQryDTO);

        if(CollectionUtils.isNotEmpty(currentMaterialList)) {
            currentMaterialList.forEach(timeMaterial -> {
                String deadLineDateStr;
                if (StringUtils.isNotBlank(wmsMaterialLotQryResultDTOS.get(0).getDeadlineDate())) {
                    deadLineDateStr = wmsMaterialLotQryResultDTOS.get(0).getDeadlineDate();
                } else {
                    Date dateTime = new Date();
                    String currentTimeStr = DateUtil.format(dateTime, BaseConstants.Pattern.DATETIME);
                    String availableTime2 = timeMaterial.getAvailableTime();

                    DateTime newDate3 = DateUtil.offsetMinute(dateTime, Integer.parseInt(availableTime2));
                    deadLineDateStr = newDate3.toString(BaseConstants.Pattern.DATETIME);

                    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_UPDATE");
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventRequestId(eventRequestId);
                        setEventTypeCode("MATERIAL_LOT_UPDATE");
                    }});

                    List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                    // ?????????????????????????????????
                    MtExtendVO5 enableDateAttr = new MtExtendVO5();
                    enableDateAttr.setAttrName("ENABLE_DATE");
                    enableDateAttr.setAttrValue(currentTimeStr);
                    mtExtendVO5List.add(enableDateAttr);
                    // ?????????????????????????????????
                    MtExtendVO5 deadLineDateAttr = new MtExtendVO5();
                    deadLineDateAttr.setAttrName("DEADLINE_DATE");
                    deadLineDateAttr.setAttrValue(deadLineDateStr);
                    mtExtendVO5List.add(deadLineDateAttr);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                        setKeyId(dto.getMaterialLotId());
                        setEventId(eventId);
                        setAttrs(mtExtendVO5List);
                    }});
                }

//            timeMaterial.setMaterialLotId(dto.getMaterialLotId());
                timeMaterial.setDeadLineDate(deadLineDateStr);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(timeMaterial);
            });
        }

        return matchedJobTimeMaterialQuery(tenantId, dto, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobTimeMaterialVO> deleteTimeMaterial(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.deleteTimeMaterial tenantId=[{}],dto=[{}]", tenantId, dto);
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobTimeMaterial hmeEoJobTimeMaterialPara = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterialPara.setTenantId(tenantId);
        hmeEoJobTimeMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterialPara.setMaterialId(dto.getMaterialId());
        hmeEoJobTimeMaterialPara.setMaterialLotId(dto.getMaterialLotId());
        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = self().selectOne(hmeEoJobTimeMaterialPara);
        if(Objects.nonNull(hmeEoJobTimeMaterial)){
            //V20200910 modify by penglin.sui for jioa.chen ?????????????????????????????? + ???????????? , ???????????????????????????+ ?????? + ????????????????????? ??????????????????
            List<HmeEoJobTimeMaterial> existLotMaterialList = this.selectByCondition(Condition.builder(HmeEoJobTimeMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobTimeMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobTimeMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_ID, dto.getMaterialId()))
                    .build());
            log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.deleteTimeMaterial existLotMaterialList.size=[{}],tenantId=[{}]," +
                    "workcellId=[{}],materialId=[{}]", existLotMaterialList.size(), tenantId, dto.getWorkcellId(), dto.getMaterialId());
            if(existLotMaterialList.size() > 1){
                hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(hmeEoJobTimeMaterial.getJobMaterialId());
            }else {
                HmeEoJobTimeMaterial hmeEoJobTimeMaterialUpdate = new HmeEoJobTimeMaterial();
                hmeEoJobTimeMaterialUpdate.setJobMaterialId(hmeEoJobTimeMaterial.getJobMaterialId());
                hmeEoJobTimeMaterialUpdate.setMaterialLotId("");
                hmeEoJobTimeMaterialUpdate.setIsReleased(HmeConstants.ConstantValue.ZERO);
                hmeEoJobTimeMaterialUpdate.setLocatorId("");
                hmeEoJobTimeMaterialUpdate.setLotCode("");
                hmeEoJobTimeMaterialUpdate.setDeadLineDate("");
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterialUpdate);
            }
        }

        //??????????????????materialId?????????snMaterialId,???????????????????????????????????? modify by yuchao.wang at 2020.9.24
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            dto.setMaterialId(dto.getSnMaterialId());
        }
        //??????jobId???????????????????????? modify by yuchao.wang for lu.bai at 2020.12.15
        if (StringUtils.isBlank(dto.getJobId())) {
            dto.setIsWorkcellQuery(YES);
        }
        return matchedJobTimeMaterialQuery(tenantId, dto, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobTimeMaterialVO updateIsReleased(Long tenantId, HmeEoJobTimeMaterialVO dto) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobTimeMaterial updateHmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        updateHmeEoJobTimeMaterial.setJobMaterialId(dto.getJobMaterialId());
        updateHmeEoJobTimeMaterial.setIsReleased(dto.getIsReleased());
        hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(updateHmeEoJobTimeMaterial);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobTimeMaterialVO> batchUpdateIsReleased(Long tenantId, List<HmeEoJobTimeMaterialVO> dtoList) {
        if(CollectionUtils.isNotEmpty(dtoList)){
            for (HmeEoJobTimeMaterialVO dto : dtoList
                 ) {
                updateIsReleased(tenantId , dto);
            }
        }
        return dtoList;
    }

    @Override
    public HmeEoJobTimeMaterialVO updateReleaseQty(Long tenantId, HmeEoJobTimeMaterialVO dto) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        HmeEoJobTimeMaterial updateHmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        updateHmeEoJobTimeMaterial.setJobMaterialId(dto.getJobMaterialId());
        updateHmeEoJobTimeMaterial.setReleaseQty(dto.getReleaseQty());
        hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(updateHmeEoJobTimeMaterial);
        return dto;
    }
}
