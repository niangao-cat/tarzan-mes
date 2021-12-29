package tarzan.general.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.general.api.dto.MtTagGroupObjectDTO4;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.entity.MtTagGroupObjectHis;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtTagGroupObjectVO;
import tarzan.general.domain.vo.MtTagGroupObjectVO2;
import tarzan.general.domain.vo.MtTagGroupObjectVO3;
import tarzan.general.infra.mapper.MtTagGroupObjectMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 数据收集组关联对象表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Component
public class MtTagGroupObjectRepositoryImpl extends BaseRepositoryImpl<MtTagGroupObject>
        implements MtTagGroupObjectRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagGroupObjectMapper mtTagGroupObjectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Override
    public MtTagGroupObject tagGroupObjectGet(Long tenantId, String tagGroupObjectId) {
        if (StringUtils.isEmpty(tagGroupObjectId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupObjectId", "【API:tagGroupObjectGet】"));
        }

        MtTagGroupObject object = new MtTagGroupObject();
        object.setTenantId(tenantId);
        object.setTagGroupObjectId(tagGroupObjectId);
        return mtTagGroupObjectMapper.selectOne(object);
    }

    @Override
    public List<MtTagGroupObject> tagGroupObjectBatchGet(Long tenantId, List<String> tagGroupObjectIdList) {
        if (CollectionUtils.isNotEmpty(tagGroupObjectIdList)) {
            return mtTagGroupObjectMapper.selectByIdList(tenantId, tagGroupObjectIdList);
        }
        return null;
    }

    @Override
    public List<String> propertyLimitTagGroupObjectQuery(Long tenantId, MtTagGroupObject mtTagGroupObject) {
        if (ObjectFieldsHelper.isAllFieldNull(mtTagGroupObject)) {
            throw new MtException("MT_GENERAL_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0014", "GENERAL", "【API:propertyLimitTagGroupObjectQuery】"));
        }
        return mtTagGroupObjectMapper.selectForEmptyString(tenantId, mtTagGroupObject).stream()
                .map(MtTagGroupObject::getTagGroupObjectId).collect(Collectors.toList());
    }

    @Override
    public List<String> propertyLimitTagGroupObjectBatchQuery(Long tenantId, List<String> tagGroupIdList,
                                                              MtTagGroupObject mtTagGroupObject) {

        return mtTagGroupObjectMapper.propertyLimitTagGroupObjectBatchQuery(tenantId, tagGroupIdList, mtTagGroupObject)
                .stream().map(MtTagGroupObject::getTagGroupObjectId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long tagGroupObjectBatchUpdate(Long tenantId, List<MtTagGroupObjectVO> dto, String fullUpdate) {

        // 必输校验
        if (CollectionUtils.isEmpty(dto) || dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getTagGroupCode()))) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "tagGroupCode", "【API:tagGroupObjectBatchUpdate】"));
        }

        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("TAG_GROUP_OBJECT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 获取默认站点Id
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setOrganizationType("SITE");
        mtUserOrganization.setUserId(userId);
        mtUserOrganization = mtUserOrganizationRepository.userDefaultOrganizationGet(tenantId, mtUserOrganization);

        Date now = new Date(System.currentTimeMillis());
        List<String> sqlList = new ArrayList<>();

        // 1.判断新增还是更新
        MtTagGroupObject mtTagGroupObject;
        Boolean isUpdate = true;

        // 批量获取数据
        List<String> tagGroupCdoes =
                dto.stream().map(MtTagGroupObjectVO::getTagGroupCode).distinct().collect(Collectors.toList());
        List<MtTagGroup> mtTagGroups = mtTagGroupRepository.selectTagGroupByTagGroupCodes(tenantId, tagGroupCdoes);
        Map<String, MtTagGroup> mtTagGroupMap = null;
        if (CollectionUtils.isNotEmpty(mtTagGroups)) {
            mtTagGroupMap = mtTagGroups.stream().collect(Collectors.toMap(t -> t.getTagGroupCode(), t -> t));
        }
        List<MtTagGroupObject> mtTagGroupObjects = mtTagGroupObjectMapper.selectBytagGroupIdList(tenantId,
                mtTagGroups.stream().map(MtTagGroup::getTagGroupId).collect(Collectors.toList()));
        Map<String, List<MtTagGroupObject>> mtTagGroupObjectMap = null;
        if (CollectionUtils.isNotEmpty(mtTagGroupObjects)) {
            mtTagGroupObjectMap =
                    mtTagGroupObjects.stream().collect(Collectors.groupingBy(MtTagGroupObject::getTagGroupId));
        }
        // 物料
        List<String> materialCode = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getMaterialCode()))
                .map(MtTagGroupObjectVO::getMaterialCode).distinct().collect(Collectors.toList());
        Map<String, MtMaterial> mtMaterialMap = null;
        if (CollectionUtils.isNotEmpty(materialCode)) {
            List<MtMaterial> mtMaterials = mtMaterialRepository.queryMaterialByCode(tenantId, materialCode);
            if (CollectionUtils.isNotEmpty(mtMaterials)) {
                mtMaterialMap = mtMaterials.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, t -> t));
            }
        }

        // 工艺
        List<String> operationName = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getOperationName()))
                .map(MtTagGroupObjectVO::getOperationName).distinct().collect(Collectors.toList());
        Map<String, List<MtOperation>> mtOperationMap = null;
        if (CollectionUtils.isNotEmpty(operationName) && mtUserOrganization != null
                && StringUtils.isNotEmpty(mtUserOrganization.getUserOrganizationId())) {
            List<MtOperation> mtOperations = mtOperationRepository.operationCurrentVersionQuery(tenantId, operationName,
                    mtUserOrganization.getUserOrganizationId());
            if (CollectionUtils.isNotEmpty(mtOperations)) {
                mtOperationMap = mtOperations.stream().collect(Collectors.groupingBy(MtOperation::getOperationName));
            }
        }

        // 工艺路线
        List<String> routerName = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getRouterName()))
                .map(MtTagGroupObjectVO::getRouterName).distinct().collect(Collectors.toList());

        // 工艺路线步骤
        List<String> routerStepName = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getRouterStepName()))
                .map(MtTagGroupObjectVO::getRouterStepName).distinct().collect(Collectors.toList());

        Map<String, List<MtRouter>> routerNameMap = null;
        Map<String, List<MtRouterStep>> mtRouterStepsMap = null;
        if (CollectionUtils.isNotEmpty(routerName)) {
            List<MtRouter> mtRouters = mtRouterRepository.routerCurrentVersionQuery(tenantId, routerName);
            if (CollectionUtils.isNotEmpty(mtRouters)) {
                routerNameMap = mtRouters.stream().collect(Collectors.groupingBy(MtRouter::getRouterName));
                if (CollectionUtils.isNotEmpty(routerStepName)) {
                    List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.stepNameLimitRouterStepQuery(tenantId,
                            mtRouters.stream().map(MtRouter::getRouterId).collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                        mtRouterStepsMap = mtRouterSteps.stream()
                                .collect(Collectors.groupingBy(MtRouterStep::getStepName));
                    }
                }
            }
        }

        // 工作单元
        List<String> workCellCode = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getWorkcellCode()))
                .map(MtTagGroupObjectVO::getWorkcellCode).distinct().collect(Collectors.toList());
        Map<String, MtModWorkcell> mtModWorkcellMap = null;
        if (CollectionUtils.isNotEmpty(workCellCode)) {
            List<MtModWorkcell> mtModWorkcells =
                    mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, workCellCode);
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                mtModWorkcellMap = mtModWorkcells.stream()
                        .collect(Collectors.toMap(MtModWorkcell::getWorkcellCode, t -> t));
            }
        }
        // 生产指令
        List<String> workOrderNum = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getWorkOrderNum()))
                .map(MtTagGroupObjectVO::getWorkOrderNum).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap = null;
        if (CollectionUtils.isNotEmpty(workOrderNum)) {
            List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, workOrderNum);
            if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderNum, t -> t));
            }
        }
        // 执行作业
        List<String> eoNum = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getEoNum()))
                .map(MtTagGroupObjectVO::getEoNum).distinct().collect(Collectors.toList());
        Map<String, MtEo> mtEoMap = null;
        if (CollectionUtils.isNotEmpty(eoNum)) {
            List<MtEo> mtEos = mtEoRepository.numberLimitEoQuery(tenantId, eoNum);
            if (CollectionUtils.isNotEmpty(mtEos)) {
                mtEoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoNum, t -> t));
            }
        }
        // 不良代码
        List<String> ncCode = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getNcCode()))
                .map(MtTagGroupObjectVO::getNcCode).distinct().collect(Collectors.toList());
        Map<String, MtNcCode> mtNcCodeMap = null;
        if (CollectionUtils.isNotEmpty(ncCode)) {
            List<MtNcCode> mtNcCodes = mtNcCodeRepository.ncCodeByMcCodeQuery(tenantId, ncCode);
            if (CollectionUtils.isNotEmpty(mtNcCodes)) {
                mtNcCodeMap = mtNcCodes.stream().collect(Collectors.toMap(MtNcCode::getNcCode, t -> t));
            }
        }

        // 装配清单
        List<String> bomName = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getBomName()))
                .map(MtTagGroupObjectVO::getBomName).distinct().collect(Collectors.toList());
        Map<String, List<MtBom>> mtBomMap = null;
        Map<String, List<MtBomComponent>> mtBomComponentMap = null;
        if (CollectionUtils.isNotEmpty(bomName)) {
            List<MtBom> mtBoms = mtBomRepository.bomLimitBomNameQuery(tenantId, bomName);
            if (CollectionUtils.isNotEmpty(mtBoms)) {
                mtBomMap = mtBoms.stream().collect(Collectors.groupingBy(MtBom::getBomName));
                List<MtBomComponent> mtBomComponents = mtBomComponentRepository.selectBomComponentByBomIds(tenantId,
                        mtBoms.stream().map(MtBom::getBomId).collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                    mtBomComponentMap =
                            mtBomComponents.stream().collect(Collectors.groupingBy(MtBomComponent::getBomId));
                }
            }
        }
        // 装配清单行
        Map<String, MtMaterial> mtBomComMap = null;
        List<String> bomComCode = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getBomComponentCode()))
                .map(MtTagGroupObjectVO::getBomComponentCode).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(bomComCode)) {
            List<MtMaterial> mtMaterials = mtMaterialRepository.queryMaterialByCode(tenantId, bomComCode);
            if (CollectionUtils.isNotEmpty(mtMaterials)) {
                mtBomComMap = mtMaterials.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, t -> t));
            }
        }

        // 物料批
        List<String> materialLotCode = dto.stream().filter(t -> StringUtils.isNotEmpty(t.getMaterialLotCode()))
                .map(MtTagGroupObjectVO::getMaterialLotCode).distinct().collect(Collectors.toList());
        Map<String, MtMaterialLot> mtMaterialLotMap = null;
        if (CollectionUtils.isNotEmpty(materialLotCode)) {
            List<MtMaterialLot> mtMaterialLots =
                    mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, materialLotCode);
            if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
                mtMaterialLotMap = mtMaterialLots.stream()
                        .collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode, t -> t));
            }
        }
        Long result = 0L;

        // 获取Cid、Id
        List<String> tagGroupObjectIds = this.customDbRepository.getNextKeys("mt_tag_group_object_s", dto.size());
        List<String> tagGroupObjectCids = this.customDbRepository.getNextKeys("mt_tag_group_object_cid_s", dto.size());
        List<String> tagGroupObjectHisIds =
                this.customDbRepository.getNextKeys("mt_tag_group_object_his_s", dto.size());
        List<String> tagGroupObjectHisCids =
                this.customDbRepository.getNextKeys("mt_tag_group_object_his_cid_s", dto.size());
        for (MtTagGroupObjectVO c : dto) {
            if (MapUtils.isEmpty(mtTagGroupMap) || mtTagGroupMap.get(c.getTagGroupCode()) == null) {
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "tagGroupCode", "【API:tagGroupObjectBatchUpdate】"));
            }
            String tagGroupId = mtTagGroupMap.get(c.getTagGroupCode()).getTagGroupId();
            if (MapUtils.isEmpty(mtTagGroupObjectMap) || CollectionUtils.isEmpty(mtTagGroupObjectMap.get(tagGroupId))) {
                // 新增
                isUpdate = false;
                mtTagGroupObject = new MtTagGroupObject();
                mtTagGroupObject.setTagGroupId(tagGroupId);
            } else {
                mtTagGroupObject = mtTagGroupObjectMap.get(tagGroupId).get(0);
            }

            // 数据ID转换
            // 物料Id
            if (StringUtils.isNotEmpty(c.getMaterialCode())) {
                if (MapUtils.isEmpty(mtMaterialMap) || mtMaterialMap.get(c.getMaterialCode()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "materialCode", "【API:tagGroupObjectBatchUpdate】"));

                }
                mtTagGroupObject.setMaterialId(mtMaterialMap.get(c.getMaterialCode()).getMaterialId());
            }

            // 工艺Id
            if (StringUtils.isNotEmpty(c.getOperationName()) && mtUserOrganization != null) {
                if (MapUtils.isEmpty(mtOperationMap)
                        || CollectionUtils.isEmpty(mtOperationMap.get(c.getOperationName()))) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "operationName", "【API:tagGroupObjectBatchUpdate】"));
                }
                mtTagGroupObject.setOperationId(mtOperationMap.get(c.getOperationName()).get(0).getOperationId());
            }

            // 工艺路线Id
            if (StringUtils.isNotEmpty(c.getRouterName())) {
                if (MapUtils.isEmpty(routerNameMap) || CollectionUtils.isEmpty(routerNameMap.get(c.getRouterName()))) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "routerName", "【API:tagGroupObjectBatchUpdate】"));
                }
                String routerId = routerNameMap.get(c.getRouterName()).get(0).getRouterId();
                mtTagGroupObject.setRouterId(routerId);

                // 工艺路线步骤ID
                if (StringUtils.isNotEmpty(c.getRouterStepName())) {
                    if (MapUtils.isEmpty(mtRouterStepsMap)
                            || CollectionUtils.isEmpty(mtRouterStepsMap.get(c.getRouterStepName()))) {
                        throw new MtException("MT_GENERAL_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                        "GENERAL", "routerStepName",
                                        "【API:tagGroupObjectBatchUpdate】"));
                    }
                    Optional<MtRouterStep> first = mtRouterStepsMap.get(c.getRouterStepName()).stream()
                            .filter(t -> routerId.equalsIgnoreCase(t.getRouterId())).findFirst();
                    if (!first.isPresent()) {
                        throw new MtException("MT_GENERAL_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                        "GENERAL", "routerStepName",
                                        "【API:tagGroupObjectBatchUpdate】"));
                    }
                    mtTagGroupObject.setRouterStepId(first.get().getRouterStepId());
                }

            }
            // 工作单元
            if (StringUtils.isNotEmpty(c.getWorkcellCode())) {
                if (MapUtils.isEmpty(mtModWorkcellMap) || mtModWorkcellMap.get(c.getWorkcellCode()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "workcellCode", "【API:tagGroupObjectBatchUpdate】"));

                }
                mtTagGroupObject.setWorkcellId(mtModWorkcellMap.get(c.getWorkcellCode()).getWorkcellId());
            }

            // 生产指令ID
            if (StringUtils.isNotEmpty(c.getWorkOrderNum())) {
                if (MapUtils.isEmpty(mtWorkOrderMap) || mtWorkOrderMap.get(c.getWorkOrderNum()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "workOrderNum", "【API:tagGroupObjectBatchUpdate】"));

                }
                mtTagGroupObject.setWorkOrderId(mtWorkOrderMap.get(c.getWorkOrderNum()).getWorkOrderId());
            }

            // 执行作业ID
            if (StringUtils.isNotEmpty(c.getEoNum())) {
                if (MapUtils.isEmpty(mtEoMap) || mtEoMap.get(c.getEoNum()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "workOrderNum", "【API:tagGroupObjectBatchUpdate】"));

                }
                mtTagGroupObject.setEoId(mtEoMap.get(c.getEoNum()).getEoId());
            }

            // 不良代码ID
            if (StringUtils.isNotEmpty(c.getNcCode())) {
                if (MapUtils.isEmpty(mtNcCodeMap) || mtNcCodeMap.get(c.getNcCode()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "ncCode", "【API:tagGroupObjectBatchUpdate】"));

                }
                mtTagGroupObject.setNcCodeId(mtNcCodeMap.get(c.getNcCode()).getNcCodeId());
            }

            // 装配清单ID
            if (StringUtils.isNotEmpty(c.getBomName())) {
                if (MapUtils.isEmpty(mtBomMap) || CollectionUtils.isEmpty(mtBomMap.get(c.getBomName()))) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "bomName", "【API:tagGroupObjectBatchUpdate】"));


                }
                String bomId = mtBomMap.get(c.getBomName()).get(0).getBomId();
                mtTagGroupObject.setBomId(bomId);
                // 装配清单行ID
                if (StringUtils.isNotEmpty(c.getBomComponentCode()) && StringUtils.isNotEmpty(bomId)) {
                    if (CollectionUtils.isEmpty(mtBomComponentMap.get(bomId)) || MapUtils.isEmpty(mtBomComMap)
                            || mtBomComMap.get(c.getBomComponentCode()) == null) {
                        throw new MtException("MT_GENERAL_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                        "GENERAL", "bomComponentCode",
                                        "【API:tagGroupObjectBatchUpdate】"));
                    }
                    String materialId = mtBomComMap.get(c.getBomComponentCode()).getMaterialId();
                    Optional<MtBomComponent> first = mtBomComponentMap.get(bomId).stream()
                            .filter(t -> materialId.equalsIgnoreCase(t.getMaterialId())).findFirst();
                    if (!first.isPresent()) {
                        throw new MtException("MT_GENERAL_0037",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                        "GENERAL", "bomComponentCode",
                                        "【API:tagGroupObjectBatchUpdate】"));
                    }

                    mtTagGroupObject.setBomComponentId(first.get().getBomComponentId());
                }
            }
            // 物料批ID:将参数materialLotCode调用API{propertyLimitMaterialLotQuery}转为materialLotId
            if (StringUtils.isNotEmpty(c.getMaterialLotCode())) {
                if (MapUtils.isEmpty(mtMaterialLotMap) || mtMaterialLotMap.get(c.getMaterialLotCode()) == null) {
                    throw new MtException("MT_GENERAL_0037",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0037",
                                    "GENERAL", "materialLotCode", "【API:tagGroupObjectBatchUpdate】"));
                }
                mtTagGroupObject.setMaterialLotId(mtMaterialLotMap.get(c.getMaterialLotCode()).getMaterialLotId());
            }
            if (isUpdate) {
                mtTagGroupObject.setTenantId(tenantId);
                mtTagGroupObject.setLastUpdatedBy(userId);
                mtTagGroupObject.setLastUpdateDate(now);
                mtTagGroupObject.setCid(Long.valueOf(tagGroupObjectCids.get(result.intValue())));

                MtTagGroupObjectHis mtTagGroupObjectHis = new MtTagGroupObjectHis();
                BeanUtils.copyProperties(mtTagGroupObject, mtTagGroupObjectHis);
                mtTagGroupObjectHis.setEventId(eventId);
                mtTagGroupObjectHis.setTenantId(tenantId);
                mtTagGroupObjectHis.setCid(Long.valueOf(tagGroupObjectHisCids.get(result.intValue())));
                mtTagGroupObjectHis.setTagGroupObjectHisId(tagGroupObjectHisIds.get(result.intValue()));
                if ("Y".equals(fullUpdate)) {
                    sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupObjectHis));
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtTagGroupObject));
                } else {
                    sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupObjectHis));
                    sqlList.addAll(customDbRepository.getUpdateSql(mtTagGroupObject));
                }
                result += 1L;

            } else {
                mtTagGroupObject.setTenantId(tenantId);
                mtTagGroupObject.setCreatedBy(userId);
                mtTagGroupObject.setLastUpdatedBy(userId);
                mtTagGroupObject.setLastUpdateDate(now);
                mtTagGroupObject.setCreationDate(now);
                mtTagGroupObject.setTagGroupObjectId(tagGroupObjectIds.get(result.intValue()));
                mtTagGroupObject.setCid(Long.valueOf(tagGroupObjectCids.get(result.intValue())));
                sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupObject));

                MtTagGroupObjectHis mtTagGroupObjectHis = new MtTagGroupObjectHis();
                BeanUtils.copyProperties(mtTagGroupObject, mtTagGroupObjectHis);
                mtTagGroupObjectHis.setEventId(eventId);
                mtTagGroupObjectHis.setTenantId(tenantId);
                mtTagGroupObjectHis.setCid(Long.valueOf(tagGroupObjectHisCids.get(result.intValue())));
                mtTagGroupObjectHis.setTagGroupObjectHisId(tagGroupObjectHisIds.get(result.intValue()));

                sqlList.addAll(customDbRepository.getInsertSql(mtTagGroupObjectHis));
                result += 1L;
            }
        }
        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return result;
    }

    @Override
    public List<String> objectLimitTagGroupQuery(Long tenantId, MtTagGroupObjectVO2 dto) {
        // 第一步，校验输入参数合规性
        // 校验输入参数不可全部为空
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getOperationId())
                && StringUtils.isEmpty(dto.getRouterId()) && StringUtils.isEmpty(dto.getRouterStepId())
                && StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getWorkOrderId())
                && StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getNcCodeId())
                && StringUtils.isEmpty(dto.getBomId()) && StringUtils.isEmpty(dto.getBomComponentId())
                && StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("MT_DATA_RECORD_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0002", "DATA_RECORD", "【API:objectLimitTagGroupQuery】"));
        }
        // 第二步，根据输入参数在表MT_TAG_GROUP_OBJECT中进行匹配，将匹配到的所有TAG_GROUP_ID进行输出
        if (MtBaseConstants.NO.equals(dto.getFuzzyMatchingFlag()) || StringUtils.isEmpty(dto.getFuzzyMatchingFlag())) {
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                dto.setMaterialId("");
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                dto.setOperationId("");
            }
            if (StringUtils.isEmpty(dto.getRouterId())) {
                dto.setRouterId("");
            }
            if (StringUtils.isEmpty(dto.getRouterStepId())) {
                dto.setRouterStepId("");
            }
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                dto.setWorkcellId("");
            }
            if (StringUtils.isEmpty(dto.getWorkOrderId())) {
                dto.setWorkOrderId("");
            }
            if (StringUtils.isEmpty(dto.getEoId())) {
                dto.setEoId("");
            }
            if (StringUtils.isEmpty(dto.getNcCodeId())) {
                dto.setNcCodeId("");
            }
            if (StringUtils.isEmpty(dto.getBomId())) {
                dto.setBomId("");
            }
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                dto.setBomComponentId("");
            }
            if (StringUtils.isEmpty(dto.getMaterialLotId())) {
                dto.setMaterialLotId("");
            }
        }
        return mtTagGroupObjectMapper.objectLimitTagGroupQuery(tenantId, dto);
    }

    @Override
    public Page<MtTagGroupObjectDTO3> productionVersionQuery(Long tenantId, MtTagGroupObjectDTO4 dto, PageRequest pageRequest) {
        Page<MtTagGroupObjectDTO3> result = PageHelper.doPageAndSort(pageRequest, () -> mtTagGroupObjectMapper.productionVersionQuery(tenantId, dto));
        return result;
    }
}
