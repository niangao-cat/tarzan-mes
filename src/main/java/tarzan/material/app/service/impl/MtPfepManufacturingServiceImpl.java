package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.config.ExecutorConfig;
import tarzan.material.api.dto.MtPfepManufacturingDTO;
import tarzan.material.api.dto.MtPfepManufacturingDTO2;
import tarzan.material.api.dto.MtPfepManufacturingDTO3;
import tarzan.material.app.service.MtPfepManufacturingService;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO2;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.material.infra.mapper.MtPfepManufacturingMapper;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.vo.MtBomVO7;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * 物料生产属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Service
public class MtPfepManufacturingServiceImpl extends BaseServiceImpl<MtPfepManufacturing>
                implements MtPfepManufacturingService {
    private static final Logger logger = LoggerFactory.getLogger(MtPfepManufacturingServiceImpl.class);
    private static final String MT_PFEP_MANUFACTURING_ATTR = "mt_pfep_manufacturing_attr";

    @Autowired
    private MtPfepManufacturingMapper mapper;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtThreadPoolRepository mtThreadPoolRepository;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private ExecutorConfig executorConfig;

    @Override
    public Page<MtPfepManufacturingVO> listForUi(Long tenantId, String materialId, PageRequest pageRequest) {
        Page<MtPfepManufacturing> base =
                        PageHelper.doPage(pageRequest, () -> mapper.selectByMaterialIdForUi(tenantId, materialId));
        Page<MtPfepManufacturingVO> result = new Page<MtPfepManufacturingVO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());
        // 定义线程
        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        // 并行流获取id列表
        List<String> areaIds = base.parallelStream().filter(t -> "AREA".equals(t.getOrganizationType()))
                        .map(MtPfepManufacturing::getOrganizationId).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());

        List<String> productionLineIds =
                        base.parallelStream().filter(t -> "PRODUCTIONLINE".equals(t.getOrganizationType()))
                                        .map(MtPfepManufacturing::getOrganizationId).filter(StringUtils::isNotEmpty)
                                        .distinct().collect(Collectors.toList());

        List<String> workcellIds = base.parallelStream().filter(t -> "WORKCELL".equals(t.getOrganizationType()))
                        .map(MtPfepManufacturing::getOrganizationId).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());

        List<String> bomIds = base.parallelStream().map(MtPfepManufacturing::getDefaultBomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        List<String> routerIds = base.parallelStream().map(MtPfepManufacturing::getDefaultRoutingId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        // 根据物料站点id获取物料Id和站点id
        List<String> materialSiteIds = base.parallelStream().map(MtPfepManufacturing::getMaterialSiteId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, String> materialIds = new HashMap<>(0);
        Map<String, String> siteIds = new HashMap<>(0);

        if (CollectionUtils.isNotEmpty(materialSiteIds)) {
            List<MtMaterialSite> mtMaterialSites =
                            mtMaterialSiteRepository.queryByMaterialSiteId(tenantId, materialSiteIds);
            if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
                materialIds = mtMaterialSites.stream().collect(
                                Collectors.toMap(MtMaterialSite::getMaterialSiteId, MtMaterialSite::getMaterialId));
                siteIds = mtMaterialSites.stream().collect(
                                Collectors.toMap(MtMaterialSite::getMaterialSiteId, MtMaterialSite::getSiteId));
            }
        }


        // 获取的数据信息
        List<MtMaterialVO> materialVOS = new ArrayList<>();

        List<MtModSite> mtModSites = new ArrayList<>();

        List<MtModArea> mtModAreas = new ArrayList<>();

        List<MtModProductionLine> mtModProductionLines = new ArrayList<>();

        List<MtModWorkcell> mtModWorkcells = new ArrayList<>();

        List<MtBomVO7> mtBomVO7s = new ArrayList<>();

        List<MtRouter> mtRouters = new ArrayList<>();

        try {
            // 获取物料信息
            Future<List<MtMaterialVO>> materialFuture = mtThreadPoolRepository.getMaterialFuture(poolExecutor, tenantId,
                            new ArrayList<>(materialIds.values()));
            // 获取站点信息
            Future<List<MtModSite>> modSiteFuture = mtThreadPoolRepository.getModSiteFuture(poolExecutor, tenantId,
                            new ArrayList<>(siteIds.values()));
            // 获取区域信息
            Future<List<MtModArea>> modAreaFuture =
                            mtThreadPoolRepository.getModAreaFuture(poolExecutor, tenantId, areaIds);
            // 获取生产线信息
            Future<List<MtModProductionLine>> modProductionLineFuture = mtThreadPoolRepository
                            .getModProductionLineFuture(poolExecutor, tenantId, productionLineIds);
            // 获取工作单元信息
            Future<List<MtModWorkcell>> modWorkcellFuture =
                            mtThreadPoolRepository.getModWorkcellFuture(poolExecutor, tenantId, workcellIds);
            // 获取装配清单信息
            Future<List<MtBomVO7>> bomFuture = mtThreadPoolRepository.getBomFuture(poolExecutor, tenantId, bomIds);

            // 获取工艺路线信息
            Future<List<MtRouter>> routerFuture =
                            mtThreadPoolRepository.getRouterFuture(poolExecutor, tenantId, routerIds);

            materialVOS = materialFuture.get();
            mtModSites = modSiteFuture.get();
            mtModAreas = modAreaFuture.get();
            mtModProductionLines = modProductionLineFuture.get();
            mtModWorkcells = modWorkcellFuture.get();
            mtBomVO7s = bomFuture.get();
            mtRouters = routerFuture.get();

        } catch (Exception e) {
            logger.error("listForUi error", e);
        }
        // 组装物料Map
        Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(materialVOS)) {
            materialMap = materialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
        }
        // 组装站点Map
        Map<String, String> siteNames = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModSites)) {
            siteNames = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, MtModSite::getSiteName));
        }

        // 组装区域Map
        Map<String, String> areaNames = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModAreas)) {
            areaNames = mtModAreas.stream().collect(Collectors.toMap(MtModArea::getAreaId, MtModArea::getAreaName));
        }

        // 组装生产线Map
        Map<String, String> productionLineNames = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModProductionLines)) {
            productionLineNames = mtModProductionLines.stream().collect(
                            Collectors.toMap(MtModProductionLine::getProdLineId, MtModProductionLine::getProdLineName));
        }
        // 组装工作单元Map
        Map<String, String> workCellNames = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
            workCellNames = mtModWorkcells.stream()
                            .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getWorkcellName));
        }

        // 组装装配清单Map
        Map<String, MtBomVO7> bomMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
            bomMap = mtBomVO7s.stream().collect(Collectors.toMap(MtBomVO7::getBomId, t -> t));
        }


        Map<String, MtRouter> routerMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtRouters)) {
            routerMap = mtRouters.stream().collect(Collectors.toMap(MtRouter::getRouterId, t -> t));
        }

        // 组装数据
        List<MtPfepManufacturingVO> list = Collections.synchronizedList(new ArrayList<>());
        Map<String, MtMaterialVO> finalMaterialMap = materialMap;
        Map<String, String> finalMaterialIds = materialIds;
        Map<String, String> finalSiteNames = siteNames;
        Map<String, String> finalSiteIds = siteIds;
        Map<String, String> finalAreaNames = areaNames;
        Map<String, String> finalProductionLineNames = productionLineNames;
        Map<String, String> finalWorkCellNames = workCellNames;
        Map<String, MtBomVO7> finalBomMap = bomMap;
        Map<String, MtRouter> finalRouterMap = routerMap;
        base.parallelStream().forEach(t -> {
            MtPfepManufacturingVO vo = new MtPfepManufacturingVO();
            MtMaterialVO materialVO = finalMaterialMap.get(finalMaterialIds.get(t.getMaterialSiteId()));

            vo.setMaterialCode(null == materialVO ? null : materialVO.getMaterialCode());
            vo.setMaterialName(null == materialVO ? null : materialVO.getMaterialName());

            vo.setSiteName(finalSiteNames.get(finalSiteIds.get(t.getMaterialSiteId())));
            if ("AREA".equals(t.getOrganizationType())) {
                vo.setAreaName(finalAreaNames.get(t.getOrganizationId()));
            }
            if ("PRODUCTIONLINE".equals(t.getOrganizationType())) {
                vo.setProdLineName(finalProductionLineNames.get(t.getOrganizationId()));
            }
            if ("WORKCELL".equals(t.getOrganizationType())) {
                vo.setWorkcellName(finalWorkCellNames.get(t.getOrganizationId()));
            }
            vo.setEnableFlag(t.getEnableFlag());
            vo.setKid(t.getPfepManufacturingId());
            vo.setKeyType("material");
            vo.setCategoryCode("");
            vo.setCategoryDesc("");

            vo.setDefaultBomId(t.getDefaultBomId());
            MtBomVO7 mtBomVO7 = finalBomMap.get(t.getDefaultBomId());
            vo.setDefaultBomName(null == mtBomVO7 ? null : mtBomVO7.getBomName());
            vo.setDefaultBomRevision(null == mtBomVO7 ? null : mtBomVO7.getRevision());

            MtRouter mtRouter = finalRouterMap.get(t.getDefaultRoutingId());
            vo.setDefaultRoutingId(t.getDefaultRoutingId());
            vo.setDefaultRoutingName(null == mtRouter ? null : mtRouter.getRouterName());
            vo.setDefaultRoutingRevision(null == mtRouter ? null : mtRouter.getRevision());

            vo.setIssueControlType(t.getIssueControlType());
            vo.setIssueControlQty(t.getIssueControlQty());
            vo.setCompleteControlType(t.getCompleteControlType());
            vo.setCompleteControlQty(t.getCompleteControlQty());
            vo.setAttritionControlType(t.getAttritionControlType());
            vo.setAttritionControlQty(t.getAttritionControlQty());
            vo.setOperationAssembleFlag(t.getOperationAssembleFlag());
            list.add(vo);
        });
        list.sort(Comparator.comparingDouble((MtPfepManufacturingVO t) -> Double
                        .valueOf(StringUtils.isEmpty(t.getKid()) ? "0" : t.getKid())));
        result.setContent(list);
        return result;
    }

    @Override
    public MtPfepManufacturingVO2 detailForUi(Long tenantId, String kid) {
        MtPfepManufacturingVO2 vo = mapper.selectByIdCustomForUi(tenantId, kid);
        if (vo != null && StringUtils.isNotEmpty(vo.getKid())) {
            vo.setPfepAttrList(mtExtendSettingsService.attrQuery(tenantId, vo.getKid(), MT_PFEP_MANUFACTURING_ATTR));
        }

        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtPfepManufacturingDTO3 saveMtPfepManufacturingForUi(Long tenantId, MtPfepManufacturingDTO dto) {
        // 基础属性赋值
        MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
        BeanUtils.copyProperties(dto, mtPfepManufacturing);
        mtPfepManufacturing.setTenantId(tenantId);

        // 获取站点关系ID
        MtMaterialSite site = new MtMaterialSite();
        site.setTenantId(tenantId);
        site.setMaterialId(dto.getMaterialId());
        site.setSiteId(dto.getSiteId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, site);
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_MATERIAL_0074",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0074", "MATERIAL", ""));
        }
        mtPfepManufacturing.setMaterialSiteId(materialSiteId);
        // 主键赋值
        if (StringUtils.isEmpty(dto.getKid())) {
            // 新增时需要校验唯一性
            MtPfepManufacturing temp = new MtPfepManufacturing();
            temp.setTenantId(tenantId);
            temp.setMaterialSiteId(mtPfepManufacturing.getMaterialSiteId());
            temp.setOrganizationType(mtPfepManufacturing.getOrganizationType());
            temp.setOrganizationId(mtPfepManufacturing.getOrganizationId());
            temp = mapper.selectOne(temp);
            if (temp != null && StringUtils.isNotEmpty(temp.getPfepManufacturingId())) {
                throw new MtException("MT_MATERIAL_0061",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0061", "MATERIAL"));
            }
            // 校验通过时直接保存
            insertSelective(mtPfepManufacturing);
        } else {
            mtPfepManufacturing.setPfepManufacturingId(dto.getKid());
            updateByPrimaryKey(mtPfepManufacturing);
        }

        MtPfepManufacturingDTO3 result = new MtPfepManufacturingDTO3();
        result.setKid(mtPfepManufacturing.getPfepManufacturingId());
        result.setType("material");

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtPfepManufacturingDTO3 copyPfepManufacturingForUi(Long tenantId, MtPfepManufacturingDTO2 dto) {
        // 获取站点关系
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setTenantId(tenantId);
        mtMaterialSite.setMaterialId(dto.getSourceMaterialId());
        mtMaterialSite.setSiteId(dto.getSourceSiteId());
        mtMaterialSite = mtMaterialSiteMapper.selectOne(mtMaterialSite);

        if (mtMaterialSite == null) {
            // 目标物料与站点关系不存在，请确认
            throw new MtException("MT_BOM_0022", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_BOM_0022",
                            "BOM", "API【saveMtPfepInventory】"));
        }
        // 获取来源属性
        MtPfepManufacturing pfepManufacturing = new MtPfepManufacturing();
        pfepManufacturing.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
        pfepManufacturing.setOrganizationId(dto.getSourceOrgId());
        pfepManufacturing.setOrganizationType(dto.getSourceOrgType());
        pfepManufacturing.setTenantId(tenantId);

        // 目标属性赋值
        MtPfepManufacturing targetPfepManufacturing = mapper.selectOne(pfepManufacturing);
        if (targetPfepManufacturing == null) {
            throw new MtException("MT_MATERIAL_0085",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0085", "MATERIAL"));
        }

        MtPfepManufacturingDTO saveDTO = new MtPfepManufacturingDTO();
        BeanUtils.copyProperties(targetPfepManufacturing, saveDTO);
        saveDTO.setMaterialId(dto.getTargetMaterialId());
        saveDTO.setSiteId(dto.getTargetSiteId());
        saveDTO.setOrganizationId(dto.getTargetOrgId());
        saveDTO.setOrganizationType(dto.getTargetOrgType());
        // 获取来源扩展属性
        List<MtExtendAttrDTO> list = mtExtendSettingsService.attrQuery(tenantId,
                        targetPfepManufacturing.getPfepManufacturingId(), MT_PFEP_MANUFACTURING_ATTR);
        List<MtExtendAttrDTO3> extendList = new ArrayList<>();
        list.forEach(e -> {
            // 写入扩展字段
            MtExtendAttrDTO3 dto3 = new MtExtendAttrDTO3();
            dto3.setAttrName(e.getAttrName());
            dto3.setAttrValue(e.getAttrValue());
            extendList.add(dto3);
        });
        saveDTO.setMtPfepManufacturingAttrs(extendList);
        // 保存
        return saveMtPfepManufacturingForUi(tenantId, saveDTO);
    }

}