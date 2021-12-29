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
 * 工序作业平台-时效投料 资源库实现
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
     * 拆分数据
     *
     * @param sqlList  源数据
     * @param splitNum 拆分数量
     * @return 拆分数据
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
        // 物料信息
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

        //获取组件版本
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

        //获取用户信息
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
            //V20201023 modify by penglin.sui 批量更新
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

        // 物料信息
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
            // 非预装平台需匹配wkc组件清单物料
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // 获取当前wkc工艺对应的组件清单
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
                        //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
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
                // 非预装平台需匹配wkc组件清单物料
                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobTimeMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
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

            //获取组件版本
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
     * @Description 初始化时效物料投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:53
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param availableTime 有效时间
     * @param componentQty 组件数量
     * @param dto  初始化参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTimeMaterialWithoutQuery(Long tenantId, String materialId, String availableTime, BigDecimal componentQty, HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobTimeMaterialRepositoryImpl.initTimeMaterialWithoutQuery tenantId=[{}],materialId=[{}]" +
                ",availableTime=[{}],componentQty=[{}],dto=[{}]", tenantId, materialId, availableTime, componentQty, dto);

        //获取用户信息
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

            //V20201023 modify by penglin.sui 批量更新
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

        //预先查出组件清单 modify by yuchao.wang at 2020.9.23
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
            //预装作业平台要筛选物料 add by yuchao.wang for lu.bai at 2020.9.17
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

                        //V20200930 modify by pengli.sui for lu.bai 替代料物料也要显示
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

        //批量查询remainQty add by yuchao.wang for lu.bai at 2020.9.14
        Map<String, BigDecimal> releaseQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials = hmeEoJobSnLotMaterialRepository.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())).build());
            if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterials)) {
                //V20201003 modify by penglin.sui for lu.bai 排除退料的数据
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

                //工位扫描时不校验替代料 modify by yuchao.wang for lu.bai at 2020.12.15
                if (!YES.equals(dto.getIsWorkcellQuery())) {
                    //投料数量为0时，校验是否替代料，替代料才显示
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

                // 物料信息
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
                //如果是工位查询不去筛选显示所有物料
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    if (CollectionUtils.isNotEmpty(woComponentList)) {
                        //预装根据WO获取组件需求数量 add by yuchao.wang for lu.bai at 2020.9.14
                        List<MtWorkOrderVO8> componentQtyList = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(componentQtyList)) {
                            //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
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
                            //获取EO下组件需求数量 add by yuchao.wang for lu.bai at 2020.9.14
                            List<MtEoVO20> componentQtyList = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(componentQtyList)) {
                                //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
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
                    //V20200921 modify by penglin.sui for lu.bai 不在组件清单中且不是替代料的不显示
                    if (HmeConstants.ConstantValue.NO.equals(isInComponentFlag) && !hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), dto, woComponentList, mtEoVO20List)) {
                        continue;
                    }
                }
                String releaseQtyKey = jobTimeMaterialVO.getMaterialId() + "-" + StringUtils.trimToEmpty(jobTimeMaterialVO.getProductionVersion());
                jobTimeMaterialVO.setReleaseQty(releaseQtyMap.getOrDefault(releaseQtyKey, BigDecimal.ZERO));
                jobTimeMaterialVO.setLineNumber(sortedMap.getOrDefault(jobTimeMaterialVO.getMaterialId(), 99999L));

                //获取组件版本
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

        //按照LineNumber升序排序 modify by yuchao.wang for lu.bai at 2020.9.25
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
            // 当前条码无效, 请确认
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());

        //V20201016 modify by penglin.sui for lu.bai 新增物料类型校验
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
        if(!HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
            //物料生产类型【${1}】与当前扫描框的物料类型不符,请检查!
            throw new MtException("HME_EO_JOB_SN_116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_116", "HME", materialType));
        }

        //V20201008 modify by penglin.sui for lu.bai 条码不能绑定多个工位
        String workcellCode = hmeEoJobTimeMaterialMapper.queryHaveBindWorkcell(tenantId,dto.getMaterialLotId(),dto.getWorkcellId());

        if(StringUtils.isNotBlank(workcellCode)){
            // 当前条码已与工位【${1}】绑定
            throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_110", "HME",workcellCode ));
        }

        //V20200831 modify by penglin.sui for jiao.chen 判断条码是否绑定当前工位
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
//                        // 条码号【${1}】不为OK状态,请核实所录入条码
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        } else {
            // 非预装平台需匹配wkc组件清单物料
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // 获取当前wkc工艺对应的组件清单
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
//                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
//                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(mtMaterialLot.getMaterialId())).findFirst();
//                    if (!componentOptional.isPresent()) {
//                        // 条码号【${1}】不为OK状态,请核实所录入条码
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        }

        //V20200831 modify by penglin.sui for jiao.chen 条码质量状态为OK
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // 条码号【${1}】不为OK状态,请核实所录入条码
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        HmeEoJobTimeMaterial param = new HmeEoJobTimeMaterial();
        param.setTenantId(tenantId);
        param.setWorkcellId(dto.getWorkcellId());
        param.setMaterialId(mtMaterialLot.getMaterialId());
        List<HmeEoJobTimeMaterial> scanMaterialList = hmeEoJobTimeMaterialMapper.select(param);

        if (CollectionUtils.isEmpty(scanMaterialList)) {
            // 当前条码无法匹配投料物料,请确认
//            throw new MtException("HME_EO_JOB_SN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_015", "HME"));
            //V20200925 modify by penglin.sui for lu.bai 判断物料是否满足替代关系
            if(!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId,mtMaterialLot.getMaterialId(),dto,woComponentList,mtEoVO20List)
                    && !StringUtils.equals(dto.getPfType(), HmeConstants.PfType.REWORK)){
                //所扫描条码物料与装配清单不匹配且非可替代物料,请检查
                throw new MtException("HME_EO_JOB_SN_078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_078", "HME"));
            }
        }

        // 校验条码是否有效
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            //条码不为有效条码.${1}
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "【API:materialLotEnableValidate】"));
        }

        //如果是预装查WO组件 直接从前台拿woId，否则查EO组件 modify by yuchao.wang for lu.bai at 2020.9.22
        Map<String, Double> componentQtyMap = new HashMap<>();
        Map<String, String> bomComponentIdMap = new HashMap<>();
        if(!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (Objects.isNull(mtEo)) {
                // 不存在此SN号对应的EO
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }
            dto.setWorkOrderId(mtEo.getWorkOrderId());

            // 查询匹配当前工位下wkc工艺组件清单匹配的批次物料
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

        //库位校验
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //单位校验
        hmeEoJobSnLotMaterialRepository.CheckUom(tenantId,mtMaterialLot.getPrimaryUomId(),mtMaterialLot.getMaterialId());

        String bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");

        //销售订单校验
        hmeEoJobSnLotMaterialRepository.CheckSoNum(tenantId,dto.getWorkOrderId(),dto.getMaterialLotId(),bomComponentId);

        //在制品校验
        hmeEoJobSnLotMaterialRepository.CheckMaterialAttr(tenantId,dto.getMaterialLotId());

        //获取条码扩展表信息
        MtExtendSettings productionVersionAttr = new MtExtendSettings();
        productionVersionAttr.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(),
                Collections.singletonList(productionVersionAttr));
        String productionVersion = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
        }

        // 如果是计划外投料，需要先插时效物料表
        //预装没有计划外投料 modify by yuchao.wang for lu.bai at 2020.9.22
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
//                //匹配当前工位下wkc工艺组件清单匹配的批次物料
//                if (bomComponentIdMap.containsKey(mtMaterialLot.getMaterialId())) {
//                    //获取BOM组件扩展表信息
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

        //V20200831 modify by penglin.sui for jiao.chen 判断工位 + 条码是否存在，不存在则新增，存在则更新数量
        HmeEoJobTimeMaterial hmeEoJobTimeMaterialPara = new HmeEoJobTimeMaterial();
        hmeEoJobTimeMaterialPara.setTenantId(tenantId);
        hmeEoJobTimeMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobTimeMaterialPara.setMaterialId(mtMaterialLot.getMaterialId());
        hmeEoJobTimeMaterialPara.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        HmeEoJobTimeMaterial existLotMaterial = this.selectOne(hmeEoJobTimeMaterialPara);

        HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
        String virtualFlag = HmeConstants.ConstantValue.NO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            //获取BOM组件扩展表信息
            MtExtendSettings virtualFlagAttr = new MtExtendSettings();
            virtualFlagAttr.setAttrName("lineAttribute8");
            List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(virtualFlagAttr));
            if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
            }
        }

        //V20200921 modify by penglin.sui for lu.bai 新增availableTime获取逻辑
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(mtMaterialLot.getSiteId());
        materialSiteParam.setMaterialId(mtMaterialLot.getMaterialId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,
                materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // 未找到匹配的物料站点信息
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }
        MtExtendVO materialSiteExtend = new MtExtendVO();
        materialSiteExtend.setTableName("mt_material_site_attr");
        materialSiteExtend.setKeyId(materialSiteId);
        List<MtExtendAttrVO> materialSiteExtendAttr =
                mtExtendSettingsRepository.attrPropertyQuery(tenantId, materialSiteExtend);
        // 物料站点扩展字段：时效
        List<MtExtendAttrVO> mtAvailableTimeAttr = materialSiteExtendAttr.stream()
                .filter(result -> "attribute9".equals(result.getAttrName()))
                .collect(Collectors.toList());
        String availableTime = "0";
        if(CollectionUtils.isNotEmpty(mtAvailableTimeAttr)){
            availableTime = mtAvailableTimeAttr.get(0).getAttrValue();
        }

        if(Objects.isNull(existLotMaterial)){
            //工位 + 物料 + 条码不存在，再判断 工位 + 物料 + 条码为空是否存在
            List<HmeEoJobTimeMaterial> existLotMaterialList = hmeEoJobTimeMaterialMapper.qqueryEoJobTimeMaterial2(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
            if(CollectionUtils.isNotEmpty(existLotMaterialList)){
                //更新material_lot_id 、release_qty、、availableTime
                BeanUtils.copyProperties(existLotMaterialList.get(0),hmeEoJobTimeMaterial);
                hmeEoJobTimeMaterial.setMaterialLotId(dto.getMaterialLotId());

                //匹配当前工位下wkc工艺组件清单匹配的批次物料
                hmeEoJobTimeMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(productionVersion);
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setVirtualFlag(virtualFlag);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
            }else {
                //新增数据
                // 匹配当前工位下wkc工艺组件清单匹配的批次物料
                hmeEoJobTimeMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobTimeMaterial.setTenantId(tenantId);
                hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobTimeMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                // 初始化为不投料
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
            //更新release_qty、availableTime
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
            //获取组件扩展表信息
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
                    //条码物料版本【${1}】与组件需求版本【${2}】不一致
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
                    // 更新时效条码的启用日期
                    MtExtendVO5 enableDateAttr = new MtExtendVO5();
                    enableDateAttr.setAttrName("ENABLE_DATE");
                    enableDateAttr.setAttrValue(currentTimeStr);
                    mtExtendVO5List.add(enableDateAttr);
                    // 更新时效条码的截止日期
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
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
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
            //V20200910 modify by penglin.sui for jioa.chen 校验是否存在多笔工位 + 物料关系 , 多笔则删除当前工位+ 物料 + 条码数据，否则 清空条码字段
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

        //预装查询时把materialId替换成snMaterialId,用于筛选虚拟组件下的物料 modify by yuchao.wang at 2020.9.24
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            dto.setMaterialId(dto.getSnMaterialId());
        }
        //如果jobId为空则为工位解绑 modify by yuchao.wang for lu.bai at 2020.12.15
        if (StringUtils.isBlank(dto.getJobId())) {
            dto.setIsWorkcellQuery(YES);
        }
        return matchedJobTimeMaterialQuery(tenantId, dto, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobTimeMaterialVO updateIsReleased(Long tenantId, HmeEoJobTimeMaterialVO dto) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
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
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
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
