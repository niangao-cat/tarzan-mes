package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.entity.MtAssemblePointControl;
import tarzan.method.domain.repository.MtAssembleControlRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssembleControlVO;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssembleControlVO2;
import tarzan.method.infra.mapper.MtAssembleControlMapper;
import tarzan.method.infra.mapper.MtAssembleGroupMapper;
import tarzan.method.infra.mapper.MtAssemblePointControlMapper;
import tarzan.method.infra.mapper.MtAssemblePointMapper;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationRelVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@Component
public class MtAssembleControlRepositoryImpl extends BaseRepositoryImpl<MtAssembleControl>
                implements MtAssembleControlRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleControlMapper mtAssembleControlMapper;

    @Autowired
    private MtAssembleGroupMapper mtAssembleGroupMapper;

    @Autowired
    private MtAssemblePointMapper mtAssemblePointMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtAssemblePointControlMapper mtAssemblePointControlMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public List<Map<String, Object>> allAssemblePointMaterialQtyCalculate(Long tenantId,
                    MtAssembleControlVO condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:allAssemblePointMaterialQtyCalculate】"));
        }
        if (null == condition.getDemandQty()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "demandQty",
                                            "【API:allAssemblePointMaterialQtyCalculate】"));
        }

        MtAssembleControl mtAssembleControl =
                        this.mtAssembleControlMapper.selectByEnable(tenantId, condition.getAssembleControlId());
        if (null == mtAssembleControl) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:allAssemblePointMaterialQtyCalculate】"));
        }

        if (StringUtils.isNotEmpty(condition.getAssembleGroupId())) {
            MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
            mtAssembleGroup.setTenantId(tenantId);
            mtAssembleGroup.setAssembleGroupId(condition.getAssembleGroupId());
            mtAssembleGroup = this.mtAssembleGroupMapper.selectOne(mtAssembleGroup);
            if (null == mtAssembleGroup || "CLOSED".equals(mtAssembleGroup.getAssembleGroupStatus())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                                "ASSEMBLE_CONTROL", "assembleGroupId",
                                                "【API:allAssemblePointMaterialQtyCalculate】"));
            }
        }

        if (StringUtils.isNotEmpty(condition.getAssemblePointId())) {
            MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
            mtAssemblePoint.setTenantId(tenantId);
            mtAssemblePoint.setAssemblePointId(condition.getAssemblePointId());
            mtAssemblePoint = this.mtAssemblePointMapper.selectOne(mtAssemblePoint);
            if (null == mtAssemblePoint || !"Y".equals(mtAssemblePoint.getEnableFlag())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                                "ASSEMBLE_CONTROL", "assemblePointId",
                                                "【API:allAssemblePointMaterialQtyCalculate】"));
            }
        }

        if (StringUtils.isNotEmpty(condition.getMaterialId())) {
            MtMaterialVO mtMaterialVO =
                            this.mtMaterialRepository.materialPropertyGet(tenantId, condition.getMaterialId());
            if (null == mtMaterialVO || !"Y".equals(mtMaterialVO.getEnableFlag())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                                "ASSEMBLE_CONTROL", "materialId",
                                                "【API:allAssemblePointMaterialQtyCalculate】"));
            }
        }

        if (StringUtils.isNotEmpty(condition.getMaterialLotId())) {
            mtMaterialLotRepository.materialLotEnableValidate(tenantId, condition.getMaterialLotId());
        }

        // 数量不能小于0
        if (BigDecimal.valueOf(condition.getDemandQty()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0015",
                                            "ASSEMBLE_CONTROL", "【API:allAssemblePointMaterialQtyCalculate】"));
        }

        MtAssemblePointControl mtAssemblePointControl = new MtAssemblePointControl();
        mtAssemblePointControl.setAssembleControlId(condition.getAssembleControlId());
        if (StringUtils.isNotEmpty(condition.getMaterialId())) {
            mtAssemblePointControl.setMaterialId(condition.getMaterialId());
        }
        if (null != condition.getMaterialLotId()) {
            mtAssemblePointControl.setMaterialLotId(condition.getMaterialLotId());
        }
        if (StringUtils.isNotEmpty(condition.getAssemblePointId())) {
            mtAssemblePointControl.setAssemblePointId(condition.getAssemblePointId());
        }
        List<MtAssemblePointControl> mtAssemblePointControls =
                        this.mtAssemblePointControlMapper.mySelect(tenantId, mtAssemblePointControl);
        if (CollectionUtils.isEmpty(mtAssemblePointControls)) {
            return Collections.emptyList();
        }

        final List<MtAssemblePointControl> filters = new ArrayList<MtAssemblePointControl>();
        if (StringUtils.isNotEmpty(condition.getAssembleGroupId())) {
            List<Map<String, String>> list = this.mtAssemblePointRepository
                            .assembleGroupLimitEnableAssemblePointQuery(tenantId, condition.getAssembleGroupId());
            if (CollectionUtils.isEmpty(list)) {
                return Collections.emptyList();
            }

            mtAssemblePointControls.stream().forEach(c -> {
                list.stream().forEach(t -> {
                    if (c.getAssemblePointId().equals(t.get("assemblePointId"))) {
                        filters.add(c);
                    }
                });
            });

            if (CollectionUtils.isEmpty(filters)) {
                return Collections.emptyList();
            }
        } else {
            filters.addAll(mtAssemblePointControls);
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        filters.stream().forEach(c -> {
            Map<String, Object> map = new HashMap<String, Object>(0);
            map.put("assemblePointId", c.getAssemblePointId());
            map.put("qty", new BigDecimal(c.getUnitQty().toString())
                            .multiply(new BigDecimal(condition.getDemandQty().toString())).doubleValue());
            result.add(map);
        });
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assembleControlUpdate(Long tenantId, MtAssembleControl dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "objectType", "【API:assembleControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getObjectId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "objectId", "【API:assembleControlUpdate】"));
        }
        if (null == dto.getDateFrom()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "dateFrom", "【API:assembleControlUpdate】"));
        }
        // 新增校验organizationType和organizationId是否同时为空或者同时输入
        if (!(StringUtils.isEmpty(dto.getOrganizationType()) && StringUtils.isEmpty(dto.getOrganizationId())
                        || StringUtils.isNotEmpty(dto.getOrganizationType())
                                        && StringUtils.isNotEmpty(dto.getOrganizationId()))) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0019", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_ASSEMBLE_CONTROL_0019", "ASSEMBLE_CONTROL", "【API:assembleControlUpdate】"));
        }
        MtGenType mtGenType = this.mtGenTypeRepository.getGenType(tenantId, "ASSEMBLE_CONTROL", "ASSEMBLE_CONTROL_TYPE",
                        dto.getObjectType());
        if (null == mtGenType) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "objectType", "【API:assembleControlUpdate】"));
        }

        if (StringUtils.isNotEmpty(dto.getOrganizationType())) {
            mtGenType = this.mtGenTypeRepository.getGenType(tenantId, "ASSEMBLE_CONTROL", "ASSEMBLE_CONTROL_ORG_TYPE",
                            dto.getOrganizationType());
            if (null == mtGenType) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "organizationType", "【API:assembleControlUpdate】"));
            }
        }

        MtAssembleControl mtAssembleControl = new MtAssembleControl();
        mtAssembleControl.setTenantId(tenantId);
        mtAssembleControl.setObjectType(dto.getObjectType());
        mtAssembleControl.setObjectId(dto.getObjectId());
        mtAssembleControl.setOrganizationType(dto.getOrganizationType());
        mtAssembleControl.setOrganizationId(dto.getOrganizationId());
        mtAssembleControl.setReferenceArea(dto.getReferenceArea());
        // 唯一性约束验证
        if (null != mtAssembleControlMapper.selectOne(mtAssembleControl)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0024",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0024",
                                            "ASSEMBLE_CONTROL",
                                            "OBJECT_TYPE,OBJECT_ID,ORGANIZATION_TYPE,ORGANIZATION_ID,REFERENCE_AREA",
                                            "【API:assembleControlUpdate】"));
        }
        mtAssembleControl.setDateFrom(dto.getDateFrom());
        mtAssembleControl.setDateTo(dto.getDateTo());
        MtAssembleControl tmpMtAssembleControl = new MtAssembleControl();
        if (StringUtils.isEmpty(dto.getAssembleControlId())) {
            self().insertSelective(mtAssembleControl);
        } else {
            tmpMtAssembleControl.setTenantId(tenantId);
            tmpMtAssembleControl.setAssembleControlId(dto.getAssembleControlId());
            tmpMtAssembleControl = this.mtAssembleControlMapper.selectOne(tmpMtAssembleControl);
            if (null == tmpMtAssembleControl) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "assembleControlId",
                                                "【API:assembleControlUpdate】"));
            }
            tmpMtAssembleControl.setObjectType(dto.getObjectType());
            tmpMtAssembleControl.setObjectId(dto.getObjectId());
            tmpMtAssembleControl.setOrganizationType(dto.getOrganizationType());
            tmpMtAssembleControl.setOrganizationId(dto.getOrganizationId());
            tmpMtAssembleControl.setReferenceArea(dto.getReferenceArea());
            tmpMtAssembleControl.setDateFrom(dto.getDateFrom());
            tmpMtAssembleControl.setDateTo(dto.getDateTo());
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                tmpMtAssembleControl =
                                (MtAssembleControl) ObjectFieldsHelper.setStringFieldsEmpty(tmpMtAssembleControl);
                self().updateByPrimaryKey(tmpMtAssembleControl);
            } else {
                self().updateByPrimaryKeySelective(tmpMtAssembleControl);
            }
            return tmpMtAssembleControl.getAssembleControlId();
        }
        return mtAssembleControl.getAssembleControlId();
    }

    @Override
    public String assembleControlGet(Long tenantId, MtAssembleControlVO1 condition) {
        if (StringUtils.isEmpty(condition.getObjectType())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "objectType", "【API:assembleControlGet】"));
        }
        if (StringUtils.isEmpty(condition.getObjectId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "objectId", "【API:assembleControlGet】"));
        }

        MtGenType mtGenType = mtGenTypeRepository.getGenType(tenantId, "ASSEMBLE_CONTROL", "ASSEMBLE_CONTROL_TYPE",
                        condition.getObjectType());
        if (null == mtGenType) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "objectType", "【API:assembleControlGet】"));
        }

        if (StringUtils.isNotEmpty(condition.getOrganizationType())) {
            mtGenType = mtGenTypeRepository.getGenType(tenantId, "ASSEMBLE_CONTROL", "ASSEMBLE_CONTROL_ORG_TYPE",
                            condition.getOrganizationType());
            if (null == mtGenType) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "organizationType", "【API:assembleControlGet】"));
            }
        }

        if ((StringUtils.isNotEmpty(condition.getOrganizationType())
                        && StringUtils.isEmpty(condition.getOrganizationId()))
                        || (StringUtils.isEmpty(condition.getOrganizationType())
                                        && StringUtils.isNotEmpty(condition.getOrganizationId()))) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0019", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_ASSEMBLE_CONTROL_0019", "ASSEMBLE_CONTROL", "【API:assembleControlGet】"));
        }

        String referenceArea = (null == condition.getReferenceArea()) ? "" : condition.getReferenceArea();
        String assembleControlId = null;
        if (StringUtils.isEmpty(condition.getOrganizationType())
                        && StringUtils.isEmpty(condition.getOrganizationId())) {
            assembleControlId = queryAssembleControl(tenantId, condition.getObjectType(), condition.getObjectId(), "",
                            "", referenceArea);
            if (StringUtils.isNotEmpty(assembleControlId)) {
                return assembleControlId;
            }

            if ("EO".equals(condition.getObjectType())) {
                String workOrderId = mtEoRepository.eoLimitWoGet(tenantId, condition.getObjectId());
                assembleControlId = queryAssembleControl(tenantId, "WO", workOrderId, "", "", referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }
                MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, condition.getObjectId());
                String materialId = (null == mtEo) ? "" : mtEo.getMaterialId();
                return queryAssembleControl(tenantId, "MATERIAL", materialId, "", "", referenceArea);
            } else if ("WO".equals(condition.getObjectType())) {
                MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, condition.getObjectId());
                String materialId = (null == mtWorkOrder) ? "" : mtWorkOrder.getMaterialId();
                return queryAssembleControl(tenantId, "MATERIAL", materialId, "", "", referenceArea);
            } else {
                return null;
            }
        } else {
            assembleControlId = queryAssembleControl(tenantId, condition.getObjectType(), condition.getObjectId(),
                            condition.getOrganizationType(), condition.getOrganizationId(), referenceArea);
            if (StringUtils.isNotEmpty(assembleControlId)) {
                return assembleControlId;
            }

            if ("EO".equals(condition.getObjectType())) {
                assembleControlId = queryAssembleControlComplex(tenantId, condition.getObjectType(),
                                condition.getObjectId(), condition.getOrganizationType(), condition.getOrganizationId(),
                                referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                String workOrderId = mtEoRepository.eoLimitWoGet(tenantId, condition.getObjectId());

                // 先根据 WO 和 OrganizationType 获取
                assembleControlId = queryAssembleControl(tenantId, "WO", workOrderId, condition.getOrganizationType(),
                                condition.getOrganizationId(), referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                // 获取不到向下获取
                assembleControlId = queryAssembleControlComplex(tenantId, "WO", workOrderId,
                                condition.getOrganizationType(), condition.getOrganizationId(), referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, condition.getObjectId());
                String materialId = (null == mtEo) ? "" : mtEo.getMaterialId();

                // 先根据 MATERIAL 和 OrganizationType 获取
                assembleControlId = queryAssembleControl(tenantId, "MATERIAL", materialId,
                                condition.getOrganizationType(), condition.getOrganizationId(), referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                // 获取不到向下获取
                return queryAssembleControlComplex(tenantId, "MATERIAL", materialId, condition.getOrganizationType(),
                                condition.getOrganizationId(), referenceArea);
            } else if ("WO".equals(condition.getObjectType())) {
                assembleControlId = queryAssembleControlComplex(tenantId, condition.getObjectType(),
                                condition.getObjectId(), condition.getOrganizationType(), condition.getOrganizationId(),
                                referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, condition.getObjectId());
                String materialId = (null == mtWorkOrder) ? "" : mtWorkOrder.getMaterialId();

                // 先根据 MATERIAL 和 OrganizationType 获取
                assembleControlId = queryAssembleControl(tenantId, "MATERIAL", materialId,
                                condition.getOrganizationType(), condition.getOrganizationId(), referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }

                // 获取不到向下获取
                return queryAssembleControlComplex(tenantId, "MATERIAL", materialId, condition.getOrganizationType(),
                                condition.getOrganizationId(), referenceArea);
            } else {
                return queryAssembleControlComplex(tenantId, condition.getObjectType(), condition.getObjectId(),
                                condition.getOrganizationType(), condition.getOrganizationId(), referenceArea);
            }
        }
    }

    @Override
    public List<MtAssembleControlVO2> propertyLimitAssembleControlPropertyQuery(Long tenantId,
                                                                                MtAssembleControlVO2 condition) {
        return mtAssembleControlMapper.selectCondition(tenantId, condition);
    }

    private String queryAssembleControlComplex(Long tenantId, String objectType, String objectId,
                    String organizationType, String organizationId, String referenceArea) {
        String assembleControlId = null;
        if ("WORKCELL".equals(organizationType)) {
            String siteId = querySiteId(tenantId, organizationType, organizationId);

            // 2019/04/22 逻辑变更 先获取父层WORKCELL对应的装配控制信息
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(siteId);
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setOrganizationId(organizationId);
            queryVO.setParentOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            List<MtModOrganizationItemVO> itemVOS =
                            mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, queryVO);
            if (CollectionUtils.isNotEmpty(itemVOS)) {
                for (MtModOrganizationItemVO itemVO : itemVOS) {
                    assembleControlId = queryAssembleControl(tenantId, objectType, objectId, organizationType,
                                    itemVO.getOrganizationId(), referenceArea);
                    if (StringUtils.isNotEmpty(assembleControlId)) {
                        return assembleControlId;
                    }
                }
            }

            String prodLineId = queryProdLineId(tenantId, siteId, organizationType, organizationId);

            /*
             * 2019/04/22 逻辑变更 如基于WKC未找到所属产线，跳过此步骤，直接获取WKC所属站点
             */
            if (StringUtils.isNotEmpty(prodLineId)) {
                assembleControlId = queryAssembleControl(tenantId, objectType, objectId, "PROD_LINE", prodLineId,
                                referenceArea);
                if (StringUtils.isNotEmpty(assembleControlId)) {
                    return assembleControlId;
                }
            }

            assembleControlId = queryAssembleControl(tenantId, objectType, objectId, "SITE", siteId, referenceArea);
            if (StringUtils.isNotEmpty(assembleControlId)) {
                return assembleControlId;
            }
            return queryAssembleControl(tenantId, objectType, objectId, "", "", referenceArea);
        } else if ("PROD_LINE".equals(organizationType)) {
            String siteId = querySiteId(tenantId, organizationType, organizationId);
            assembleControlId = queryAssembleControl(tenantId, objectType, objectId, "SITE", siteId, referenceArea);
            if (StringUtils.isNotEmpty(assembleControlId)) {
                return assembleControlId;
            }
            return queryAssembleControl(tenantId, objectType, objectId, "", "", referenceArea);
        } else {
            return queryAssembleControl(tenantId, objectType, objectId, "", "", referenceArea);
        }
    }

    private String queryProdLineId(Long tenantId, String siteId, String organizationType, String organizationId) {
        MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
        queryVO.setTopSiteId(siteId);
        queryVO.setOrganizationType(organizationType);
        queryVO.setOrganizationId(organizationId);
        queryVO.setParentOrganizationType("PROD_LINE");
        queryVO.setQueryType("");
        List<MtModOrganizationItemVO> itemList =
                        this.mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, queryVO);
        if (CollectionUtils.isEmpty(itemList)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0021",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0021",
                                            "ASSEMBLE_CONTROL", "prodLineId", "【API:assembleControlGet】"));
        }
        if (1 < itemList.size()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0022",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0022",
                                            "ASSEMBLE_CONTROL", "prodLineId", "【API:assembleControlGet】"));
        }
        return itemList.get(0).getOrganizationId();
    }

    private String querySiteId(Long tenantId, String organizationType, String organizationId) {
        MtModOrganizationRelVO relVO = new MtModOrganizationRelVO();
        relVO.setOrganizationType(organizationType);
        relVO.setOrganizationId(organizationId);
        relVO.setSiteType("MANUFACTURING");
        List<String> siteIds = this.mtModOrganizationRelRepository.organizationLimitSiteQuery(tenantId, relVO);
        if (CollectionUtils.isEmpty(siteIds)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0021",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0021",
                                            "ASSEMBLE_CONTROL", "organizationId", "【API:assembleControlGet】"));
        }
        if (1 < siteIds.size()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0022",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0022",
                                            "ASSEMBLE_CONTROL", "organizationId", "【API:assembleControlGet】"));
        }
        return siteIds.get(0);
    }

    private String queryAssembleControl(Long tenantId, String objectType, String objectId, String organizationType,
                    String organizationId, String referenceArea) {
        MtAssembleControl mtAssembleControl = new MtAssembleControl();
        mtAssembleControl.setObjectType(objectType);
        mtAssembleControl.setObjectId(objectId);
        mtAssembleControl.setReferenceArea(referenceArea);
        mtAssembleControl.setOrganizationId(organizationId);
        mtAssembleControl.setOrganizationType(organizationType);
        mtAssembleControl = mtAssembleControlMapper.mySelectOne(tenantId, mtAssembleControl);

        if (null == mtAssembleControl) {
            return null;
        }

        long now = new Date(System.currentTimeMillis()).getTime();
        if (now < mtAssembleControl.getDateFrom().getTime()
                        || (null != mtAssembleControl.getDateTo() && now >= mtAssembleControl.getDateTo().getTime())) {
            return null;
        }
        return mtAssembleControl.getAssembleControlId();
    }

}
