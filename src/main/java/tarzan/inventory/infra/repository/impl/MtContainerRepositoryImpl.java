package tarzan.inventory.infra.repository.impl;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import io.tarzan.common.domain.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrHisVO;
import io.tarzan.common.domain.vo.MtExtendAttrHisVO2;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.repository.MtHoldActualDetailRepository;
import tarzan.actual.domain.repository.MtHoldActualRepository;
import tarzan.actual.domain.vo.MtHoldActualDetailVO2;
import tarzan.actual.domain.vo.MtHoldActualDetailVO4;
import tarzan.actual.domain.vo.MtHoldActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.api.dto.MtContainerDTO3;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerHis;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerHisRepository;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtContainerLoadDetailMapper;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 容器，一个具体的容器并记录容器的业务属性，包括容器装载实物所有者、预留对象、位置状态等，提供执行作业、物料批、容器的装载结构 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Component
public class MtContainerRepositoryImpl extends BaseRepositoryImpl<MtContainer> implements MtContainerRepository {
    private final static List<String> LOAD_OBJECT_TYPE =
                    Collections.unmodifiableList(Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER"));

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtContainerMapper mtContainerMapper;

    @Autowired
    private MtContainerLoadDetailMapper mtContainerLoadDetailMapper;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtHoldActualRepository mtHoldActualRepository;

    @Autowired
    private MtHoldActualDetailRepository mtHoldActualDetailRepository;

    @Autowired
    private MtContainerHisRepository mtContainerHisRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;


    @Override
    public List<String> locatorLimitContainerQuery(Long tenantId, MtContainerVO dto) {
        // Step 1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "locatorId", "【API:locatorLimitContainerQuery】"));
        }

        // Step2获取需获取容器的库位mlocatorId列表
        List<String> mlocatorId = new ArrayList<>();
        mlocatorId.add(dto.getLocatorId());
        if (!"N".equals(dto.getSubLocatorFlag())) {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(dto.getLocatorId());
            mtModLocatorVO9.setQueryType("ALL");
            List<String> subLocatorId = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            mlocatorId.addAll(subLocatorId);
        }

        // Step3获取库位下所有容器
        MtContainerVO1 mtContainerVO1 = new MtContainerVO1();
        mtContainerVO1.setLocatorIds(mlocatorId);
        mtContainerVO1.setEnableFlag(dto.getEnableFlag());
        List<MtContainer> containers = mtContainerMapper.selectLocatorLimitQuery(tenantId, mtContainerVO1);

        List<String> containerIds = containers.stream().map(MtContainer::getContainerId).collect(Collectors.toList());

        // Step 4根据输入参数判断并返回结果
        if (StringUtils.isEmpty(dto.getEmptyFlag())) {
            return containerIds;
        }

        List<String> resultList = new ArrayList<>();

        MtContainerLoadDetail mtContainerLoadDetail = null;
        for (String containerId : containerIds) {
            mtContainerLoadDetail = new MtContainerLoadDetail();
            mtContainerLoadDetail.setContainerId(containerId);
            mtContainerLoadDetail.setTenantId(tenantId);
            List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper.select(mtContainerLoadDetail);

            if ("Y".equals(dto.getEmptyFlag()) && CollectionUtils.isEmpty(loadDetails)) {
                resultList.add(containerId);
            } else if (!"Y".equals(dto.getEmptyFlag()) && CollectionUtils.isNotEmpty(loadDetails)) {
                resultList.add(containerId);
            }
        }
        return resultList;
    }

    @Override
    public List<String> locatorLimitMaterialLotQuery(Long tenantId, MtContainerVO dto) {
        // Step 1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "locatorId", "【API:locatorLimitMaterialLotQuery】"));
        }

        // Step2获取需获取物料批的库位mlocatorId列表
        List<String> mlocatorId = new ArrayList<>();
        mlocatorId.add(dto.getLocatorId());
        if (!"N".equals(dto.getSubLocatorFlag())) {
            MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
            mtModLocatorVO9.setLocatorId(dto.getLocatorId());
            mtModLocatorVO9.setQueryType("ALL");
            List<String> subLocatorId = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
            mlocatorId.addAll(subLocatorId);
        }

        // Step3获取库位下物料批materialLotId
        List<MtMaterialLot> materialLots = new ArrayList<>();
        MtMaterialLot mtMaterialLot = null;
        for (String t : mlocatorId) {
            mtMaterialLot = new MtMaterialLot();
            if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
                mtMaterialLot.setEnableFlag(dto.getEnableFlag());
            }
            mtMaterialLot.setLocatorId(t);
            mtMaterialLot.setTenantId(tenantId);
            materialLots.addAll(mtMaterialLotMapper.select(mtMaterialLot));
        }
        return materialLots.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList());
    }

    @Override
    public void containerLoadingObjectValidate(Long tenantId, MtContainerVO19 dto) {
        // Step 1-a判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerLoadingObjectValidate】"));

        }
        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectId", "【API:containerLoadingObjectValidate】"));

        }

        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectType", "【API:containerLoadingObjectValidate】"));

        }
        // 1-b
        if (!loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerLoadingObjectValidate】"));

        }

        // Step 2判断对象是否直接装装载在指定容器中
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectType(dto.getLoadObjectType());
        mtContLoadDtlVO5.setLoadObjectId(dto.getLoadObjectId());
        mtContLoadDtlVO5.setTopLevelFlag("N");
        List<String> containerIds =
                        mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isEmpty(containerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0050", "MATERIAL_LOT", "【API:containerLoadingObjectValidate】"));
        }

        if (containerIds.contains(dto.getContainerId())) {
            return;
        }

        // Step3-a根据输入参数判断是否需要继续判断其他层级容器的装载情况
        if (!"Y".equals(dto.getAllLevelFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0050", "MATERIAL_LOT", "【API:containerLoadingObjectValidate】"));
        }

        if ("Y".equals(dto.getAllLevelFlag())) {
            getUntilContainerId(tenantId, containerIds, dto.getContainerId());
        }
    }


    @Override
    public void containerLoadingObjectBatchValidate(Long tenantId, List<MtContainerVO19> dto) {
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        // 第一步，判断输入参数是否合规
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerList",
                                            "【API:containerLoadingObjectBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId",
                                            "【API:containerLoadingObjectBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getLoadObjectId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectId",
                                            "【API:containerLoadingObjectBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getLoadObjectType()))) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectType",
                                            "【API:containerLoadingObjectBatchValidate】"));
        }

        // 判断输入参数loadObjectType输入值是否在值集[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]范围内
        if (dto.stream().anyMatch(t -> !loadObjectTypes.contains(t.getLoadObjectType()))) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerLoadingObjectBatchValidate】"));

        }

        // Step 2判断对象是否直接装装载在指定容器中
        List<MtContainerVO35> voList = dto.stream().map(t -> {
            MtContainerVO35 vo35 = new MtContainerVO35();
            vo35.setLoadObjectId(t.getLoadObjectId());
            vo35.setLoadObjectType(t.getLoadObjectType());
            vo35.setTopLevelFlag(MtBaseConstants.NO);
            return vo35;
        }).collect(Collectors.toList());
        List<MtContainerVO36> containerVO36s =
                        mtContainerRepository.objectLimitLoadingContainerBatchQuery(tenantId, voList);

        if (CollectionUtils.isEmpty(containerVO36s)
                        || containerVO36s.stream().anyMatch(t -> CollectionUtils.isEmpty(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0050", "MATERIAL_LOT", "【API:containerLoadingObjectBatchValidate】"));
        }


        for (int i = 0; i < dto.size(); i++) {
            MtContainerVO19 container = dto.get(i);

            MtContainerVO36 vo36 = containerVO36s.get(i);

            if (vo36.getContainerId().contains(container.getContainerId())) {
                continue;
            } else {
                // 第三步，根据输入参数判断是否需要继续判断其他层级容器的装载情况
                if (!MtBaseConstants.YES.equals(container.getAllLevelFlag())) {
                    throw new MtException("MT_MATERIAL_LOT_0050",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0050",
                                                    "MATERIAL_LOT", "【API:containerLoadingObjectBatchValidate】"));
                } else {
                    batchGetUntilContainerId(tenantId, vo36.getContainerId(), container.getContainerId());
                }
            }

        }
    }

    @Override
    public void containerMaxLoadWeightExcessBatchVerify(Long tenantId, List<MtContainerDTO3> dtos) {
        // 1.判断输入参数是否合规
        Long containerIdCount =
                        dtos.stream().map(MtContainerDTO3::getContainerId).filter(t -> StringUtils.isEmpty(t)).count();
        if (containerIdCount > 0) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId",
                                            "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }

        List<String> typeList = dtos.stream().map(MtContainerDTO3::getLoadObjectType)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(typeList)) {
            // 类型判断
            for (String type : typeList) {
                if (!LOAD_OBJECT_TYPE.contains(type)) {
                    throw new MtException("MT_MATERIAL_LOT_0003",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                                    "MATERIAL_LOT", "loadObjectType",
                                                    "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                                    "【API:containerMaxLoadWeightExcessBatchVerify】"));
                }
            }
        }

        // LoadObjectType与loadObjectId不能同时输入
        for (MtContainerDTO3 dto : dtos) {

            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isEmpty(dto.getLoadObjectId())) {
                throw new MtException("MT_MATERIAL_LOT_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                                "【API:containerMaxLoadWeightExcessBatchVerify】"));
            }
        }

        List<String> isConReLoadObjectId = dtos.stream().filter(
                        t -> StringUtils.isNotEmpty(t.getLoadObjectType()) && "CONTAINER".equals(t.getLoadObjectType()))
                        .map(MtContainerDTO3::getLoadObjectId).collect(Collectors.toList());

        List<String> isMatReLoadObjectId = dtos.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getLoadObjectType())
                                        && "MATERIAL_LOT".equals(t.getLoadObjectType()))
                        .map(MtContainerDTO3::getLoadObjectId).collect(Collectors.toList());
        int maSize = isMatReLoadObjectId.stream().distinct().collect(Collectors.toList()).size();
        int conSize = isConReLoadObjectId.stream().distinct().collect(Collectors.toList()).size();

        if (CollectionUtils.isNotEmpty(isConReLoadObjectId) && isConReLoadObjectId.size() != conSize) {
            throw new MtException("MT_MATERIAL_LOT_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0106", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }
        if (CollectionUtils.isNotEmpty(isMatReLoadObjectId) && isMatReLoadObjectId.size() != maSize) {
            throw new MtException("MT_MATERIAL_LOT_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0106", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }

        // 2.据输入参数containerId调用API{containerPropertyBatchGet }获取容器对应的容器类型containerTypeId
        List<String> containerIds = dtos.stream().map(MtContainerDTO3::getContainerId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        List<MtContainer> containerList = containerPropertyBatchGet(tenantId, containerIds);

        if (CollectionUtils.isEmpty(containerList)) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));

        }

        // 为第六步数据做准备 建立containerid与containerTypeId关系
        Map<String, MtContainer> containerIdAndTypeIdMap =
                        containerList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

        // 获取containerTypeIds
        List<String> containerTypeIds = containerList.stream().map(MtContainer::getContainerTypeId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        // 3.获取容器对应容器类型的容量限制
        List<MtContainerType> mtContainerTypes =
                        mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIds);
        if (CollectionUtils.isEmpty(mtContainerTypes)) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }

        // 如果MaxLoadWeight全部为空，通过校验
        long notEmptyCount = mtContainerTypes.stream().map(MtContainerType::getMaxLoadWeight)
                        .filter(t -> StringUtils.isNotEmpty(String.valueOf(t))).count();
        if (notEmptyCount == 0) {
            return;
        }

        // 分组，为第六步准备数据
        Map<String, MtContainerType> containerTypeMap =
                        mtContainerTypes.stream().collect(Collectors.toMap(t -> t.getContainerTypeId(), t -> t));

        // 4.获取容器内所有已装载物料及物料数量
        List<MtContLoadDtlVO> paramIn = dtos.stream().map(m -> {
            MtContLoadDtlVO one = new MtContLoadDtlVO();
            one.setContainerId(m.getContainerId());
            one.setAllLevelFlag("Y");
            return one;
        }).distinct().collect(Collectors.toList());
        List<MtContLoadDtlVO21> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, paramIn);

        Map<String, List<String>> containerIdAndMaterialLotId4Map = new HashMap<>();
        Map<String, List<String>> loadContainerId4Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(loadDetails)) {
            Map<String, List<MtContLoadDtlVO6>> detailListMap = loadDetails.stream()
                            .collect(Collectors.toMap(t -> t.getContainerId(), t -> t.getLoadDetailList()));

            for (String container : containerIds) {
                List<MtContLoadDtlVO6> detailList = detailListMap.get(container);

                // 处理获取到的结果
                if (CollectionUtils.isNotEmpty(detailList)) {
                    List<String> tempMaterialLotIds = detailList.stream()
                                    .filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                                    .map(MtContLoadDtlVO6::getLoadObjectId).distinct().collect(Collectors.toList());

                    List<String> tempContainerIds = detailList.stream()
                                    .filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                                    .map(MtContLoadDtlVO6::getLoadObjectId).distinct().collect(Collectors.toList());

                    // 为第六步数据做准备
                    loadContainerId4Map.put(container, tempContainerIds);
                    containerIdAndMaterialLotId4Map.put(container, tempMaterialLotIds);
                }

            }
        }

        // 5.若loadObjectType有输入，获取当前装载对象对应的物料和数量
        MyHashMap<String, List<String>> containerIdAndMaterialLotId5Map = new MyHashMap<>();
        MyHashMap<String, List<String>> loadContainerId5Map = new MyHashMap<>();
        for (MtContainerDTO3 dto : dtos) {
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && "MATERIAL_LOT".equals(dto.getLoadObjectType())) {
                containerIdAndMaterialLotId5Map.put(dto.getContainerId(), Arrays.asList(dto.getLoadObjectId()));
            }
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && "CONTAINER".equals(dto.getLoadObjectType())) {
                loadContainerId5Map.put(dto.getContainerId(), Arrays.asList(dto.getLoadObjectId()));
            }
        }

        List<String> tempContainerIds = dtos.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getLoadObjectType())
                                        && StringUtils.isNotEmpty(t.getLoadObjectId())
                                        && "CONTAINER".equals(t.getLoadObjectType()))
                        .map(MtContainerDTO3::getLoadObjectId).distinct().collect(Collectors.toList());
        MyHashMap<String, List<String>> containerAndLoadObjectMap = new MyHashMap();
        for (MtContainerDTO3 dto : dtos) {
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isNotEmpty(dto.getLoadObjectId())
                            && "CONTAINER".equals(dto.getLoadObjectType())) {
                containerAndLoadObjectMap.put(dto.getLoadObjectId(), Arrays.asList(dto.getContainerId()));
            }
        }

        // 调用API{ containerLimitObjectBatchQuery }获取装载容器内所有装载的物料批
        if (CollectionUtils.isNotEmpty(tempContainerIds)) {
            List<MtContLoadDtlVO> param = tempContainerIds.stream().map(t -> {
                MtContLoadDtlVO loadDto = new MtContLoadDtlVO();
                loadDto.setContainerId(t);
                loadDto.setAllLevelFlag("Y");
                return loadDto;
            }).collect(Collectors.toList());

            List<MtContLoadDtlVO21> eoExistQtyList =
                            mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, param);
            if (CollectionUtils.isNotEmpty(eoExistQtyList)) {
                Map<String, MtContLoadDtlVO21> tempContainerIdMap =
                                eoExistQtyList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));
                for (String tempContainerId : tempContainerIds) {
                    MtContLoadDtlVO21 mtContLoadDtlVO21 = tempContainerIdMap.get(tempContainerId);
                    if (null != mtContLoadDtlVO21) {
                        List<MtContLoadDtlVO6> loadDetailList = mtContLoadDtlVO21.getLoadDetailList();

                        if (CollectionUtils.isNotEmpty(loadDetailList)) {
                            List<String> tempContainerIdList = loadDetailList.stream()
                                            .filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                                            .map(MtContLoadDtlVO6::getLoadObjectId).distinct()
                                            .collect(Collectors.toList());

                            List<String> tempMaterialLotIds = loadDetailList.stream()
                                            .filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                                            .map(MtContLoadDtlVO6::getLoadObjectId).distinct()
                                            .collect(Collectors.toList());
                            List<String> containerId = containerAndLoadObjectMap.get(tempContainerId);
                            // 为第六步数据做准备 前面把所有的CONTAINER类型的put进去了
                            // 5-b 若获取不到数据，获取：欲装载容器：load_containerId = loadObjectId
                            for (String id : containerId) {
                                containerIdAndMaterialLotId5Map.put(id, tempMaterialLotIds);
                                loadContainerId5Map.put(id, tempContainerIdList);
                            }
                        }
                    }
                }
            }
        }

        Map<String, List<MtContainerDTO3>> dtosMap =
                        dtos.stream().collect(Collectors.groupingBy(t -> t.getContainerId()));

        for (String containerId : containerIds) {
            // 6判断装载是否超出容器容量限制
            BigDecimal containerWeight = BigDecimal.ZERO;
            List<String> containerIdList = new ArrayList<>();
            // 按相同输入参数containerId分组
            List<String> loadContainerId = new ArrayList<>();
            List<String> containerId4s = loadContainerId4Map.get(containerId);
            List<String> containerId5s = loadContainerId5Map.get(containerId);
            if (CollectionUtils.isNotEmpty(containerId4s)) {
                loadContainerId.addAll(containerId4s);
            }
            if (CollectionUtils.isNotEmpty(containerId5s)) {
                loadContainerId.addAll(containerId5s);
            }

            loadContainerId = loadContainerId.stream().filter(t -> StringUtils.isNotEmpty(t)).distinct()
                            .collect(Collectors.toList());
            // 6-a
            if (!loadContainerId.stream().allMatch(t -> containerId.equals(t))) {
                containerIdList.addAll(loadContainerId.stream().filter(t -> !containerId.equals(t)).distinct()
                                .collect(Collectors.toList()));
                List<MtContainer> mtContainers = containerPropertyBatchGet(tenantId, containerIdList);

                List<String> containerTypeIdList = mtContainers.stream().map(MtContainer::getContainerTypeId)
                                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

                MtContainer container = containerIdAndTypeIdMap.get(containerId);
                MtContainerType mtContainerType =
                                containerTypeMap.get(null == container ? null : container.getContainerTypeId());

                if (CollectionUtils.isNotEmpty(containerTypeIdList)) {
                    List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository
                                    .containerTypePropertyBatchGet(tenantId, containerTypeIdList);
                    if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                        for (MtContainerType type : mtContainerTypeList) {
                            // MtContainerType mtContainerType = containerTypeMap.get(type.getContainerTypeId());
                            if (null != mtContainerType) {
                                // 若获取到weightUomId为空 或者若获取到weightUomId不为空，且weightUomId = 第三步3-a-ii获取的weightUomId
                                if (StringUtils.isEmpty(type.getWeightUomId()) || (StringUtils
                                                .isNotEmpty(type.getWeightUomId())
                                                && type.getWeightUomId().equals(mtContainerType.getWeightUomId()))) {
                                    containerWeight = containerWeight.add(type.getWeight() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(type.getWeight()));
                                } else if (StringUtils.isNotEmpty(type.getWeightUomId())
                                                && !type.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                                    MtUomVO1 mtUomVO1 = new MtUomVO1();
                                    mtUomVO1.setSourceUomId(type.getWeightUomId());
                                    mtUomVO1.setSourceValue(type.getWeight());
                                    mtUomVO1.setTargetUomId(mtContainerType.getWeightUomId());
                                    mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                                    if (mtUomVO1 != null) {
                                        containerWeight = containerWeight.add(mtUomVO1.getTargetValue() == null
                                                        ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6-b 需要在循环里面，不然后面不好得到 3-a-ii mtContainerType.getWeightUomId()
            MtContainer container = containerIdAndTypeIdMap.get(containerId);
            MtContainerType mtContainerType =
                            containerTypeMap.get(null == container ? null : container.getContainerTypeId());

            List<String> materialLotIds = containerIdAndMaterialLotId4Map.get(containerId);
            List<MtContainerDTO3> dto3s = dtosMap.get(containerId);
            List<String> loadObjectIds = dto3s.stream().map(MtContainerDTO3::getLoadObjectId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

            if (CollectionUtils.isEmpty(materialLotIds)) {
                materialLotIds = new ArrayList<>();
            }
            if (CollectionUtils.isNotEmpty(loadObjectIds)) {
                // 这里获取第五步containerId对应的materialLotIds
                // for (String loadObjectId : loadObjectIds) {
                List<String> materialLotIdList = containerIdAndMaterialLotId5Map.get(containerId);
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    materialLotIds.addAll(materialLotIdList);
                }
                // }

            }

            materialLotIds = materialLotIds.stream().filter(t -> StringUtils.isNotEmpty(t)).distinct()
                            .collect(Collectors.toList());
            BigDecimal materialWeight = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(materialLotIds)) {

                List<MtMaterialLot> lots =
                                mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
                Map<String, Map<String, BigDecimal>> resultMap = lots.stream().collect(Collectors.groupingBy(
                                MtMaterialLot::getMaterialId,
                                Collectors.groupingBy(MtMaterialLot::getPrimaryUomId, CollectorsUtil.summingBigDecimal(
                                                c -> BigDecimal.valueOf(c.getPrimaryUomQty() == null ? 0.0D
                                                                : c.getPrimaryUomQty())))));

                // 根据所有获取结果按照物料+主单位汇总
                List<MtMaterialLot> lots1 = new ArrayList<>();
                resultMap.entrySet().forEach(t -> {
                    t.getValue().entrySet().forEach(tt -> {
                        MtMaterialLot lot = new MtMaterialLot();
                        lot.setMaterialId(t.getKey());
                        lot.setPrimaryUomId(tt.getKey());
                        lot.setPrimaryUomQty(tt.getValue().doubleValue());
                        lots1.add(lot);
                    });
                });

                // 根据获取到的汇总结果获取物料主单位和物料重量
                List<String> materialIds = lots1.stream().map(MtMaterialLot::getMaterialId).distinct()
                                .collect(Collectors.toList());
                List<MtMaterialVO> materialVOist = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);

                if (CollectionUtils.isNotEmpty(materialVOist)) {
                    for (MtMaterialVO m : materialVOist) {
                        if (StringUtils.isEmpty(m.getWeightUomId())) {
                            m.setWeightUomId(mtContainerType.getWeightUomId());
                        }

                        BigDecimal weight = m.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(m.getWeight());
                        List<MtMaterialLot> lost2 =
                                        lots1.stream().filter(t -> t.getMaterialId().equals(m.getMaterialId()))
                                                        .collect(Collectors.toList());

                        // 基于汇总结果每条数据循环进行重量求和计算
                        for (MtMaterialLot s : lost2) {
                            if (s.getPrimaryUomId().equals(m.getPrimaryUomId())
                                            && weight.compareTo(BigDecimal.ZERO) != 0
                                            && m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {

                                materialWeight = materialWeight.add(weight.multiply(
                                                BigDecimal.valueOf(s.getPrimaryUomQty() == null ? Double.valueOf(0.0D)
                                                                : s.getPrimaryUomQty())));

                            } else if (s.getPrimaryUomId().equals(m.getPrimaryUomId())
                                            && weight.compareTo(BigDecimal.ZERO) != 0
                                            && !m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                                MtUomVO1 transferUomVO = new MtUomVO1();
                                transferUomVO.setSourceUomId(m.getWeightUomId());
                                transferUomVO.setSourceValue(new BigDecimal(s.getPrimaryUomQty().toString())
                                                .multiply(new BigDecimal(m.getWeight().toString())).doubleValue());
                                transferUomVO.setTargetUomId(mtContainerType.getWeightUomId());
                                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                                if (null != transferUomVO) {
                                    materialWeight = materialWeight.add(BigDecimal.valueOf(
                                                    transferUomVO.getTargetValue() == null ? Double.valueOf(0.0D)
                                                                    : transferUomVO.getTargetValue()));
                                }
                            } else if (!s.getPrimaryUomId().equals(m.getPrimaryUomId())
                                            && weight.compareTo(BigDecimal.ZERO) != 0) {
                                MtUomVO1 mtUomVO1 = new MtUomVO1();
                                mtUomVO1.setSourceUomId(s.getPrimaryUomId());
                                mtUomVO1.setSourceValue(s.getPrimaryUomQty());
                                mtUomVO1.setTargetUomId(m.getPrimaryUomId());
                                mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);

                                if (m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                                    materialWeight = materialWeight.add(BigDecimal.valueOf(m.getWeight())
                                                    .multiply(BigDecimal.valueOf(mtUomVO1.getTargetValue())));
                                } else {
                                    MtUomVO1 uomVO1 = new MtUomVO1();
                                    uomVO1.setSourceUomId(m.getWeightUomId());
                                    uomVO1.setSourceValue(BigDecimal.valueOf(uomVO1.getTargetValue())
                                                    .multiply(BigDecimal.valueOf(m.getWeight())).doubleValue());
                                    uomVO1.setTargetUomId(mtContainerType.getWeightUomId());
                                    uomVO1 = mtUomRepository.uomConversion(tenantId, uomVO1);
                                    materialWeight = materialWeight.add(BigDecimal.valueOf(uomVO1.getTargetValue()));
                                }

                            }
                        }
                    }
                }
            }

            // 6-c
            BigDecimal weight = containerWeight.add(materialWeight);
            if (weight.compareTo(BigDecimal.valueOf(mtContainerType.getMaxLoadWeight())) == 1) {
                throw new MtException("MT_MATERIAL_LOT_0049",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0049",
                                                "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
            }

        }

    }

    @Override
    public void containerMixedAllowBatchVerify(Long tenantId, List<MtContainerVO21> dtos) {
        // 1.判断输入参数是否合规
        Long containerIdCount =
                        dtos.stream().map(MtContainerVO21::getContainerId).filter(t -> StringUtils.isEmpty(t)).count();
        if (containerIdCount > 0) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerMixedAllowBatchVerify】"));
        }

        List<String> typeList = dtos.stream().map(MtContainerVO21::getLoadObjectType)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(typeList)) {
            // 类型判断
            for (String type : typeList) {
                if (!LOAD_OBJECT_TYPE.contains(type)) {
                    throw new MtException("MT_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0003", "MATERIAL_LOT", "loadObjectType",
                                    "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]", "【API:containerMixedAllowBatchVerify】"));
                }
            }
        }

        // LoadObjectType与loadObjectId不能同时输入
        for (MtContainerVO21 dto : dtos) {
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isEmpty(dto.getLoadObjectId())) {
                throw new MtException("MT_MATERIAL_LOT_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                                "【API:containerMixedAllowBatchVerify】"));
            }
        }

        // 2.获取容器对应的容器类型
        List<String> containerIds =
                        dtos.stream().map(MtContainerVO21::getContainerId).distinct().collect(Collectors.toList());
        List<MtContainer> containerList = containerPropertyBatchGet(tenantId, containerIds);

        // 建立containerId与typeId关系
        Map<String, MtContainer> containerIdMap =
                        containerList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

        if (CollectionUtils.isEmpty(containerList) || containerList.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerMixedAllowBatchVerify】"));

        }

        // 获取containerTypeIds
        List<String> containerTypeIds = containerList.stream().map(MtContainer::getContainerTypeId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        // 3.获取容器对应容器类型的混合装载限制
        Map<String, MtContainerType> containerTypeIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(containerTypeIds)) {
            List<MtContainerType> mtContainerTypeList =
                            mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIds);

            if (CollectionUtils.isEmpty(mtContainerTypeList)) {
                throw new MtException("MT_MATERIAL_LOT_0025",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0025",
                                                "MATERIAL_LOT", "【API:containerMixedAllowBatchVerify】"));

            }

            // 通过containerTypeId关联对象
            containerTypeIdMap =
                            mtContainerTypeList.stream().collect(Collectors.toMap(t -> t.getContainerTypeId(), t -> t));


            // 获取容器类型的混合装载空值
            for (MtContainerType type : mtContainerTypeList) {
                if (StringUtils.isEmpty(type.getMixedMaterialFlag()) && StringUtils.isEmpty(type.getMixedEoFlag())
                                && StringUtils.isEmpty(type.getMixedWoFlag())
                                && StringUtils.isEmpty(type.getMixedOwnerFlag())
                                || "Y".equals(type.getMixedMaterialFlag()) && "Y".equals(type.getMixedEoFlag())
                                                && "Y".equals(type.getMixedWoFlag())
                                                && "Y".equals(type.getMixedOwnerFlag())) {
                    return;
                }
            }
        }

        // 4.获取容器内所有已装载物料批列表
        // List<String> loadMaterialLotId = new ArrayList<>();
        Map<String, List<String>> materialLotIdMap = new HashMap<>();

        List<MtContLoadDtlVO> param = dtos.stream().map(t -> {
            MtContLoadDtlVO one = new MtContLoadDtlVO();
            one.setContainerId(t.getContainerId());
            one.setLoadObjectType("MATERIAL_LOT");
            one.setAllLevelFlag("Y");
            return one;
        }).distinct().collect(Collectors.toList());
        List<MtContLoadDtlVO21> loadDtlVO21s =
                        mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, param);

        // 获取 容器内装载物料批列表
        if (CollectionUtils.isNotEmpty(loadDtlVO21s)) {
            for (MtContLoadDtlVO21 loadDtlVO21 : loadDtlVO21s) {
                List<MtContLoadDtlVO6> loadDetailList = loadDtlVO21.getLoadDetailList();
                if (CollectionUtils.isNotEmpty(loadDetailList)) {
                    List<String> materialLotIds = loadDetailList.stream().map(MtContLoadDtlVO6::getLoadObjectId)
                                    .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
                    // loadMaterialLotId.addAll(materialLotIds);

                    // 为第七步数据做准备
                    materialLotIdMap.put(loadDtlVO21.getContainerId(), materialLotIds);
                }
            }
        }

        // 5.获取容器内所有已装载的执行作业列表
        // List<String> loadEoId = new ArrayList<>();
        Map<String, List<String>> containerIdStep5EoIdsMap = new HashMap<>();

        List<MtContLoadDtlVO> paramEo = dtos.stream().map(t -> {
            MtContLoadDtlVO one = new MtContLoadDtlVO();
            one.setContainerId(t.getContainerId());
            one.setLoadObjectType("EO");
            one.setAllLevelFlag("Y");
            return one;
        }).distinct().collect(Collectors.toList());
        List<MtContLoadDtlVO21> loadEoDtlVO21s =
                        mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, paramEo);

        if (CollectionUtils.isNotEmpty(loadEoDtlVO21s)) {
            for (MtContLoadDtlVO21 loadDtlVO21 : loadEoDtlVO21s) {
                List<MtContLoadDtlVO6> loadDetailList = loadDtlVO21.getLoadDetailList();
                if (CollectionUtils.isNotEmpty(loadDetailList)) {
                    List<String> eoIds = loadDetailList.stream().map(MtContLoadDtlVO6::getLoadObjectId)
                                    .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

                    containerIdStep5EoIdsMap.put(loadDtlVO21.getContainerId(), eoIds);
                    // loadEoId.addAll(eoIds);
                }
            }
        }

        // 6.若loadObjectType有输入，获取当前装载对象对应的物料和数量：
        MyHashMap<String, List<String>> containerIdAndMaterialIdsMap = new MyHashMap<>();
        MyHashMap<String, List<String>> containerIdStep6EoIdsMap = new MyHashMap<>();

        // 为第七步数据做准备
        for (MtContainerVO21 dto : dtos) {
            if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
                containerIdAndMaterialIdsMap.put(dto.getContainerId(), Arrays.asList(dto.getLoadObjectId()));
            }
            if ("EO".equals(dto.getLoadObjectType())) {
                containerIdStep6EoIdsMap.put(dto.getContainerId(), Arrays.asList(dto.getLoadObjectId()));
            }
        }

        List<String> tempContainerIds = dtos.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                        .map(MtContainerVO21::getLoadObjectId).distinct().collect(Collectors.toList());

        MyHashMap<String, List<String>> containerAndLoadObjectMap = new MyHashMap();
        for (MtContainerVO21 dto : dtos) {
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isNotEmpty(dto.getLoadObjectId())
                            && "CONTAINER".equals(dto.getLoadObjectType())) {
                containerAndLoadObjectMap.put(dto.getLoadObjectId(), Arrays.asList(dto.getContainerId()));
            }
        }
        // 这里全查出来再去分类
        if (CollectionUtils.isNotEmpty(tempContainerIds)) {
            if (CollectionUtils.isNotEmpty(tempContainerIds)) {
                List<MtContLoadDtlVO> paramContainer = tempContainerIds.stream().map(t -> {
                    MtContLoadDtlVO loadDto = new MtContLoadDtlVO();
                    loadDto.setContainerId(t);
                    loadDto.setAllLevelFlag("Y");
                    return loadDto;
                }).distinct().collect(Collectors.toList());

                List<MtContLoadDtlVO21> eoExistQtyList = mtContainerLoadDetailRepository
                                .containerLimitObjectBatchQuery(tenantId, paramContainer);
                if (CollectionUtils.isNotEmpty(eoExistQtyList)) {
                    Map<String, MtContLoadDtlVO21> tempContainerIdMap =
                                    eoExistQtyList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));
                    for (String tempContainerId : tempContainerIds) {
                        MtContLoadDtlVO21 mtContLoadDtlVO21 = tempContainerIdMap.get(tempContainerId);
                        if (null != mtContLoadDtlVO21) {
                            List<MtContLoadDtlVO6> loadDetailList = mtContLoadDtlVO21.getLoadDetailList();
                            if (CollectionUtils.isNotEmpty(loadDetailList)) {
                                List<String> tempMaterialLotIdList = loadDetailList.stream()
                                                .filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                                                .map(MtContLoadDtlVO6::getLoadObjectId).distinct()
                                                .collect(Collectors.toList());

                                List<String> tempEoIdList =
                                                loadDetailList.stream().filter(t -> "EO".equals(t.getLoadObjectType()))
                                                                .map(MtContLoadDtlVO6::getLoadObjectId).distinct()
                                                                .collect(Collectors.toList());
                                List<String> containerId = containerAndLoadObjectMap.get(tempContainerId);
                                for (String id : containerId) {
                                    // 为第七步数据做准备
                                    containerIdAndMaterialIdsMap.put(id, tempMaterialLotIdList);
                                    containerIdStep6EoIdsMap.put(id, tempEoIdList);
                                }

                            }

                        }
                    }
                }
            }
        }

        // 7.判断装载是否超出容器混合装载限制
        for (MtContainerVO21 dto : dtos) {
            List<String> eoId5s = containerIdStep5EoIdsMap.get(dto.getContainerId());

            List<String> eoId6s = new ArrayList<>();
            if (StringUtils.isNotEmpty(dto.getLoadObjectType())) {
                eoId6s = containerIdStep6EoIdsMap.get(dto.getContainerId());
            }

            // 7-a
            List<String> loadMaterialLotIdList = materialLotIdMap.get(dto.getContainerId());
            if (CollectionUtils.isEmpty(loadMaterialLotIdList)) {
                loadMaterialLotIdList = new ArrayList<>();
            }
            if (StringUtils.isNotEmpty(dto.getLoadObjectType())) {
                List<String> materialLotIds = containerIdAndMaterialIdsMap.get(dto.getContainerId());
                if (CollectionUtils.isNotEmpty(materialLotIds)) {
                    loadMaterialLotIdList.addAll(materialLotIds);
                }
            }

            List<MtMaterialLot> lots = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(loadMaterialLotIdList)) {
                lots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, loadMaterialLotIdList);
            }

            // 7-b
            List<String> temp = lots.stream().filter(t -> t.getEoId() != null).map(MtMaterialLot::getEoId).distinct()
                            .collect(Collectors.toList());

            List<String> materialIds = lots.stream().filter(t -> t.getMaterialId() != null)
                            .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());

            // 从第三步取出数据
            MtContainer container = containerIdMap.get(dto.getContainerId());
            MtContainerType type = containerTypeIdMap.get(null == container ? null : container.getContainerTypeId());

            if (StringUtils.isNotEmpty(type.getMixedEoFlag()) && "N".equals(type.getMixedEoFlag())) {

                List<String> loadEoId7b = new ArrayList<>();
                // 取并集
                if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && CollectionUtils.isNotEmpty(eoId6s)) {
                    loadEoId7b.addAll(eoId6s);
                }
                if (CollectionUtils.isNotEmpty(temp)) {
                    loadEoId7b.addAll(temp);
                }
                if (CollectionUtils.isNotEmpty(eoId5s)) {
                    loadEoId7b.addAll(eoId5s);
                }

                // 去重
                if (CollectionUtils.isNotEmpty(loadEoId7b)) {
                    loadEoId7b = loadEoId7b.stream().distinct().collect(Collectors.toList());
                }

                // 结果大于1则报错，说明存在多个eo
                if (loadEoId7b.size() > 1) {
                    throw new MtException("MT_MATERIAL_LOT_0044",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0044",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowBatchVerify】"));
                }
            }

            // 7-c
            List<String> loadEoId7c = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(temp)) {
                loadEoId7c.addAll(temp);
            }
            if (CollectionUtils.isNotEmpty(eoId5s)) {
                loadEoId7c.addAll(eoId5s);
            }
            if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && CollectionUtils.isNotEmpty(eoId6s)) {
                loadEoId7c.addAll(eoId6s);
            }


            // 拼接所有eoId的EO信息（比较woId时用）
            // List<MtEo> nonQtyEos = new ArrayList<>();
            // if (CollectionUtils.isNotEmpty(containerIdStep5EoIdsMap.get(dto.getContainerId()))) {
            // nonQtyEos = mtEoRepository.eoPropertyBatchGet(tenantId,
            // containerIdStep5EoIdsMap.get(dto.getContainerId()));
            // }

            // 只有loadEoId的EO信息
            List<MtEo> loadEos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(loadEoId7c)) {
                loadEos = mtEoRepository.eoPropertyBatchGet(tenantId, loadEoId7c);
            }

            // 7-d-i 判断容器是否允许混合装载物料：
            List<String> materialIdTemp = loadEos.stream().filter(t -> t.getMaterialId() != null)
                            .map(MtEo::getMaterialId).distinct().collect(Collectors.toList());

            if (StringUtils.isNotEmpty(type.getMixedMaterialFlag()) && "N".equals(type.getMixedMaterialFlag())) {
                List<String> loadEoId7d = new ArrayList<>();
                // 取并集
                if (CollectionUtils.isNotEmpty(materialIds)) {
                    loadEoId7d.addAll(materialIds);
                }
                if (CollectionUtils.isNotEmpty(materialIdTemp)) {
                    loadEoId7d.addAll(materialIdTemp);
                }

                // 去重
                if (CollectionUtils.isNotEmpty(loadEoId7d)) {
                    loadEoId7d = loadEoId7d.stream().distinct().collect(Collectors.toList());
                }

                // 结果大于1则报错，说明存在多个material
                if (loadEoId7d.size() > 1) {
                    throw new MtException("MT_MATERIAL_LOT_0045",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0045",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowBatchVerify】"));
                }
            }

            // 7-d-ii 判断容器是否允许混合装载不同所有者的实物
            if (StringUtils.isNotEmpty(type.getMixedOwnerFlag()) && "N".equals(type.getMixedOwnerFlag())) {
                if ((CollectionUtils.isNotEmpty(eoId5s) || CollectionUtils.isNotEmpty(eoId6s))
                                && CollectionUtils.isNotEmpty(lots)
                                && lots.stream().anyMatch(t -> StringUtils.isNotEmpty(t.getOwnerType())
                                                || StringUtils.isNotEmpty(t.getOwnerId()))) {
                    throw new MtException("MT_MATERIAL_LOT_0046",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0046",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowBatchVerify】"));
                }
                if (CollectionUtils.isEmpty(eoId5s) && CollectionUtils.isEmpty(eoId6s)
                                && CollectionUtils.isNotEmpty(lots)) {
                    // 判断7-a所有结果中ownerType、ownerId全部一致
                    List<String> temp1 = lots.stream().map(MtMaterialLot::getOwnerType).distinct()
                                    .collect(Collectors.toList());
                    List<String> temp2 = lots.stream().map(MtMaterialLot::getOwnerId).distinct()
                                    .collect(Collectors.toList());
                    if (temp1.size() > 1 || temp2.size() > 1) {
                        throw new MtException("MT_MATERIAL_LOT_0046",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0046", "MATERIAL_LOT",
                                                        "【API：containerMixedAllowBatchVerify】"));
                    }
                }
            }

            // 7-d-iii判断容器是否允许混合装载不同生产指令的实物
            if (StringUtils.isNotEmpty(type.getMixedWoFlag()) && "N".equals(type.getMixedWoFlag())) {
                List<String> eoId7a = lots.stream().map(MtMaterialLot::getEoId).distinct().collect(Collectors.toList());

                if (eoId7a.size() > 1 && eoId7a.stream().anyMatch(t -> StringUtils.isEmpty(t))) {
                    throw new MtException("MT_MATERIAL_LOT_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0047",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowBatchVerify】"));
                }
                if (CollectionUtils.isNotEmpty(eoId7a) && eoId7a.stream().allMatch(t -> StringUtils.isEmpty(t))
                                && CollectionUtils.isEmpty(eoId5s) && CollectionUtils.isEmpty(eoId6s)) {
                    return;
                }
                if (CollectionUtils.isNotEmpty(eoId7a) && eoId7a.stream().allMatch(t -> StringUtils.isEmpty(t))
                                && (CollectionUtils.isNotEmpty(eoId5s) || CollectionUtils.isNotEmpty(eoId6s))) {
                    throw new MtException("MT_MATERIAL_LOT_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0047",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowBatchVerify】"));
                }
                if (CollectionUtils.isNotEmpty(eoId7a) && eoId7a.stream().allMatch(t -> StringUtils.isNotEmpty(t))) {
                    List<String> isSame =
                                    loadEos.stream().map(MtEo::getWorkOrderId).distinct().collect(Collectors.toList());
                    if (isSame.size() > 1) {
                        throw new MtException("MT_MATERIAL_LOT_0047",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0047", "MATERIAL_LOT",
                                                        "【API：containerMixedAllowBatchVerify】"));
                    }
                }
            }

        }
    }

    @Override
    public void containerLoadBatchVerify(Long tenantId, List<MtContainerVO9> dtos) {
        // 1.验证参数
        for (MtContainerVO9 dto : dtos) {
            // a
            if (StringUtils.isEmpty(dto.getContainerId()) || StringUtils.isEmpty(dto.getLoadObjectId())
                            || StringUtils.isEmpty(dto.getLoadObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "containerId、loadObjectType、loadObjectId",
                                                "【API:containerLoadBatchVerify】"));
            }

            // b
            if (!LOAD_OBJECT_TYPE.contains(dto.getLoadObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                                "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                                "【API:containerLoadBatchVerify】"));
            }

            // c
            if ("EO".equals(dto.getLoadObjectType()) && null == dto.getTrxLoadQty()) {
                throw new MtException("MT_MATERIAL_LOT_0071",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0071",
                                                "MATERIAL_LOT", "loadObjectType = EO", "trxLoadQty",
                                                "【API:containerLoadBatchVerify】"));
            }

        }

        List<String> isConReLoadObjectId = dtos.stream().filter(
                        t -> StringUtils.isNotEmpty(t.getLoadObjectType()) && "CONTAINER".equals(t.getLoadObjectType()))
                        .map(MtContainerVO9::getLoadObjectId).collect(Collectors.toList());

        List<String> isMatReLoadObjectId = dtos.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getLoadObjectType())
                                        && "MATERIAL_LOT".equals(t.getLoadObjectType()))
                        .map(MtContainerVO9::getLoadObjectId).collect(Collectors.toList());
        int maSize = isMatReLoadObjectId.stream().distinct().collect(Collectors.toList()).size();
        int conSize = isConReLoadObjectId.stream().distinct().collect(Collectors.toList()).size();

        if (CollectionUtils.isNotEmpty(isConReLoadObjectId) && isConReLoadObjectId.size() != conSize) {
            throw new MtException("MT_MATERIAL_LOT_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0106", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }
        if (CollectionUtils.isNotEmpty(isMatReLoadObjectId) && isMatReLoadObjectId.size() != maSize) {
            throw new MtException("MT_MATERIAL_LOT_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0106", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessBatchVerify】"));
        }

        // d判断欲装载容器是否可用
        List<String> containerIds =
                        dtos.stream().map(MtContainerVO9::getContainerId).distinct().collect(Collectors.toList());
        containerAvailableBatchValidate(tenantId, containerIds);

        // 上面验证不通过会报错，走到这一步说明验证通过
        List<MtContainer> containerList = containerPropertyBatchGet(tenantId, containerIds);

        if (CollectionUtils.isEmpty(containerList) || containerList.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0083", "MATERIAL_LOT", "containerId", "【API:containerLoadBatchVerify】"));
        }

        // 获取容器的预留信息reservedFlag、reservedObjectType、reservedObjectId和容器的容器类别containerTypeId
        Map<String, MtContainer> containerMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(containerList)) {
            containerMap = containerList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));
        }

        // e-i 判断装载对象是否存在且有效
        List<String> eoIds = dtos.stream().filter(t -> "EO".equals(t.getLoadObjectType()))
                        .map(MtContainerVO9::getLoadObjectId).collect(Collectors.toList());
        List<MtEo> mtEos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(eoIds)) {
            mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isEmpty(mtEos) || mtEos.size() != eoIds.size()) {
                throw new MtException("MT_MATERIAL_LOT_0052", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0052", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
            }
        }

        // e-ii
        List<String> materialLotIds = dtos.stream().filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                        .map(MtContainerVO9::getLoadObjectId).collect(Collectors.toList());
        Map<String, MtMaterialLot> mtMaterialLotMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            List<MtMaterialLot> mtMaterialLots =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            if (CollectionUtils.isEmpty(mtMaterialLots) || materialLotIds.size() != mtMaterialLots.size()
                            || mtMaterialLots.stream().filter(t -> !"Y".equals(t.getEnableFlag()))
                                            .collect(Collectors.toList()).size() > 0) {
                throw new MtException("MT_MATERIAL_LOT_0052", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0052", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
            }
            // 若获取数据不为空，且获取数据enableFlag = Y
            // 获取物料批数据预留信息reservedFlag、reservedObjectType、reservedObjectId
            mtMaterialLotMap = mtMaterialLots.stream().collect(Collectors.toMap(t -> t.getMaterialLotId(), t -> t));

            for (MtContainerVO9 dto : dtos) {
                MtContainer container = containerMap.get(dto.getContainerId());
                MtMaterialLot mtMaterialLot = mtMaterialLotMap.get(dto.getLoadObjectId());

                String reservedFlag = container.getReservedFlag();
                String reservedObjectType = container.getReservedObjectType();
                String reservedObjectId = container.getReservedObjectId();

                if (null != mtMaterialLot) {
                    if ("Y".equals(reservedFlag)) {
                        if (!StringHelper.isSame(reservedFlag, mtMaterialLot.getReservedFlag())
                                        || !StringHelper.isSame(reservedObjectType,
                                                        mtMaterialLot.getReservedObjectType())
                                        || !StringHelper.isSame(reservedObjectId,
                                                        mtMaterialLot.getReservedObjectId())) {
                            throw new MtException("MT_MATERIAL_LOT_0054",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0054", "MATERIAL_LOT",
                                                            "【API:containerLoadBatchVerify】"));
                        }
                    }
                }
            }
        }
        // e-iii
        List<String> containerIde3s = dtos.stream().filter(t -> "CONTAINER".equalsIgnoreCase(t.getLoadObjectType()))
                        .map(MtContainerVO9::getLoadObjectId).collect(Collectors.toList());
        Map<String, MtContainer> containerE3Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(containerIde3s)) {
            containerAvailableBatchValidate(tenantId, containerIde3s);

            // 验证通过
            List<MtContainer> containerE3List = containerPropertyBatchGet(tenantId, containerIde3s);
            if (CollectionUtils.isEmpty(containerE3List) || containerIde3s.size() != containerE3List.size()) {
                throw new MtException("MT_MATERIAL_LOT_0052", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0052", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
            }
            containerE3Map = containerE3List.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

            for (MtContainerVO9 dto : dtos) {
                MtContainer container = containerMap.get(dto.getContainerId());
                MtContainer mtContainerE3 = containerE3Map.get(dto.getLoadObjectId());

                String reservedFlag = container.getReservedFlag();
                String reservedObjectType = container.getReservedObjectType();
                String reservedObjectId = container.getReservedObjectId();

                if (null != mtContainerE3) {
                    if ("Y".equals(reservedFlag)) {
                        if (!StringHelper.isSame(reservedFlag, mtContainerE3.getReservedFlag())
                                        || !StringHelper.isSame(reservedObjectType,
                                                        mtContainerE3.getReservedObjectType())
                                        || !StringHelper.isSame(reservedObjectId,
                                                        mtContainerE3.getReservedObjectId())) {
                            throw new MtException("MT_MATERIAL_LOT_0054",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0054", "MATERIAL_LOT",
                                                            "【API:containerLoadBatchVerify】"));
                        }
                    }
                }
            }
        }

        // 2判断装载对象和容器的库位是否一致
        List<MtContainerVO9> noEoDtos =
                        dtos.stream().filter(t -> !"EO".equals(t.getLoadObjectType())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(noEoDtos)) {
            List<String> noEoContainerIds =
                            noEoDtos.stream().map(MtContainerVO9::getContainerId).collect(Collectors.toList());

            // b-i. 根据输入参数containerId在表MT_CONTAINER中限制CONTAINER_ID字段获取库位ID locatorId
            List<MtContainer> containerNoEoAllList = containerPropertyBatchGet(tenantId, noEoContainerIds);
            Map<String, MtContainer> containerNoEoIdMap =
                            containerNoEoAllList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

            for (MtContainerVO9 noEoDto : noEoDtos) {
                MtContainer container = containerNoEoIdMap.get(noEoDto.getContainerId());
                String locatorId = container.getLocatorId();

                if ("CONTAINER".equals(noEoDto.getLoadObjectType())) {
                    // 数据库默认为locatorId‘’
                    MtContainer mtContainerE3 = containerE3Map.get(noEoDto.getLoadObjectId());
                    if (!locatorId.equals(mtContainerE3.getLocatorId())) {
                        throw new MtException("MT_MATERIAL_LOT_0088",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0088", "MATERIAL_LOT",
                                                        "【API:containerLoadBatchVerify】"));
                    }
                }
                if ("MATERIAL_LOT".equals(noEoDto.getLoadObjectType())) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotMap.get(noEoDto.getLoadObjectId());
                    if (!locatorId.equals(mtMaterialLot.getLocatorId())) {
                        throw new MtException("MT_MATERIAL_LOT_0088",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0088", "MATERIAL_LOT",
                                                        "【API:containerLoadBatchVerify】"));
                    }
                }
            }
        }

        // 3.判断装载对象是否已被装载于容器中
        // 3-a-i
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtContLoadDtlVO20> mtContLoadDtlVO20s =
                            mtContainerLoadDetailRepository.eoLimitLoadingEoQtyBatchGet(tenantId, eoIds);
            Map<String, MtContLoadDtlVO20> loadEoListMap =
                            mtContLoadDtlVO20s.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));
            // 3-a-ii
            Map<String, MtEo> eoListMap = mtEos.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

            List<MtContainerVO9> hasEoDtos =
                            dtos.stream().filter(t -> "EO".equals(t.getLoadObjectType())).collect(Collectors.toList());
            for (MtContainerVO9 hasEoDto : hasEoDtos) {
                MtContLoadDtlVO20 mtContLoadDtlVO20 = loadEoListMap.get(hasEoDto.getLoadObjectId());
                MtEo mtEo = eoListMap.get(hasEoDto.getLoadObjectId());

                double result = BigDecimal.valueOf(mtContLoadDtlVO20.getLoadQty())
                                .add(BigDecimal.valueOf(hasEoDto.getTrxLoadQty()))
                                .subtract(BigDecimal.valueOf(mtEo.getQty())).doubleValue();
                if (result > 0D) {
                    throw new MtException("MT_MATERIAL_LOT_0068",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0068",
                                                    "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
                }
            }
        }
        // 3-b-i
        for (MtContainerVO9 noEoDto : noEoDtos) {
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectType(noEoDto.getLoadObjectType());
            mtContLoadDtlVO5.setLoadObjectId(noEoDto.getLoadObjectId());
            List<String> tempContainerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId,
                            mtContLoadDtlVO5);
            tempContainerIds = tempContainerIds.stream().filter(t -> StringUtils.isNotEmpty(t))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tempContainerIds)) {
                throw new MtException("MT_MATERIAL_LOT_0039", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0039", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
            }
        }

        // 4若装载对象为容器，判断是否存在嵌套装载
        List<MtContainerVO9> hasLoadSelf = dtos.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType())
                        && t.getContainerId().equals(t.getLoadObjectId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hasLoadSelf)) {
            throw new MtException("MT_MATERIAL_LOT_0087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0087", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
        }

        // 4-ii
        List<MtContainerVO19> vo19s = new ArrayList<>();
        for (MtContainerVO9 mtContainerVO9 : dtos.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                        .collect(Collectors.toList())) {
            MtContainerVO19 vo19 = new MtContainerVO19();
            vo19.setContainerId(mtContainerVO9.getLoadObjectId());
            vo19.setLoadObjectType("CONTAINER");
            vo19.setLoadObjectId(mtContainerVO9.getContainerId());
            vo19.setAllLevelFlag("Y");
            vo19s.add(vo19);
        }
        boolean isPass = true;
        try {
            containerLoadingObjectBatchValidate(tenantId, vo19s);
        } catch (Exception ex) {
            isPass = false;
        }

        if (isPass) {
            throw new MtException("MT_MATERIAL_LOT_0107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0107", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));
        }

        // 5获取容器对应容器类型的启用位置管理标识
        List<String> containerTypeIds = containerList.stream().map(MtContainer::getContainerTypeId).distinct()
                        .collect(Collectors.toList());

        List<MtContainerType> mtContainerTypeList =
                        mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIds);
        if (CollectionUtils.isEmpty(mtContainerTypeList)) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerLoadBatchVerify】"));

        }

        List<MtContainerVO6> vo6s = new ArrayList<>();
        List<MtContLoadDtlVO18> vo18s = new ArrayList<>();
        List<MtContainerDTO3> dto3s = new ArrayList<>();
        List<MtContainerVO21> vo21s = new ArrayList<>();
        List<MtContainerVO37> vo37s = new ArrayList<>();
        for (MtContainerVO9 dto : dtos) {
            MtContainer container = containerMap.get(dto.getContainerId());
            for (MtContainerType type : mtContainerTypeList) {
                if (type.getContainerTypeId().equals(container.getContainerTypeId())) {
                    if ("Y".equals(type.getLocationEnabledFlag())) {
                        // 6. 针对启用位置管理的容器验证容器位置是否为空
                        if (dto.getLocationRow() == null || dto.getLocationColumn() == null) {
                            throw new MtException("MT_MATERIAL_LOT_0001",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT",
                                                            "locationRow、locationColumn",
                                                            "【API:containerLoadBatchVerify】"));
                        }

                        // 最后调用API{containerLocationIsEmptyBatchValidate}获取验证结果
                        MtContainerVO6 mtContainerVO6 = new MtContainerVO6();
                        mtContainerVO6.setContainerId(dto.getContainerId());
                        mtContainerVO6.setLocationRow(dto.getLocationRow());
                        mtContainerVO6.setLocationColumn(dto.getLocationColumn());
                        vo6s.add(mtContainerVO6);
                    }

                    // 7.验证此次装载是否满足包装等级要求
                    if (StringUtils.isNotEmpty(type.getPackingLevel())) {
                        MtContLoadDtlVO18 vo18 = new MtContLoadDtlVO18();
                        vo18.setContainerId(dto.getContainerId());
                        vo18.setLoadObjectType(dto.getLoadObjectType());
                        vo18s.add(vo18);
                        // 最后调用API{containerPackingLevelBatchVerify}获取验证结果
                    }

                    // 8验证此次装载是否满足重量要求
                    if (null != type.getMaxLoadWeight()) {
                        MtContainerDTO3 dto3 = new MtContainerDTO3();
                        dto3.setContainerId(dto.getContainerId());
                        dto3.setLoadObjectType(dto.getLoadObjectType());
                        dto3.setLoadObjectId(dto.getLoadObjectId());
                        dto3s.add(dto3);
                        // 最后调用API{containerMaxLoadWeightExcessBatchVerify}获取验证结果
                    }

                    // 9.验证此次装载是否满足混合装载要求
                    if ("N".equals(type.getMixedMaterialFlag()) || "N".equals(type.getMixedEoFlag())
                                    || "N".equals(type.getMixedWoFlag()) || "N".equals(type.getMixedOwnerFlag())) {
                        // 调用API{ containerMixedAllowVerify }获取验证结果
                        MtContainerVO21 mtContainerVO21 = new MtContainerVO21();
                        mtContainerVO21.setContainerId(dto.getContainerId());
                        mtContainerVO21.setLoadObjectType(dto.getLoadObjectType());
                        mtContainerVO21.setLoadObjectId(dto.getLoadObjectId());
                        vo21s.add(mtContainerVO21);
                        // 最后调用API{ containerMixedAllowBatchVerify }获取验证结果
                    }

                    // 10.验证此次装载是否满足容器容量要求
                    if (null != type.getCapacityQty()) {
                        MtContainerVO37 vo37 = new MtContainerVO37();
                        vo37.setContainerId(dto.getContainerId());
                        vo37.setLoadObjectType(dto.getLoadObjectType());
                        vo37.setLoadObjectId(dto.getLoadObjectId());
                        vo37.setLoadQty(dto.getTrxLoadQty());
                        vo37s.add(vo37);
                        // 最后调用API{ containerCapacityExcessBatchVerify }获取验证结果
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(vo6s)) {
            containerLocationIsEmptyBatchValidate(tenantId, vo6s);
        }
        if (CollectionUtils.isNotEmpty(vo18s)) {
            containerPackingLevelBatchVerify(tenantId, vo18s);
        }
        if (CollectionUtils.isNotEmpty(dto3s)) {
            containerMaxLoadWeightExcessBatchVerify(tenantId, dto3s);
        }
        if (CollectionUtils.isNotEmpty(vo21s)) {
            containerMixedAllowBatchVerify(tenantId, vo21s);
        }
        if (CollectionUtils.isNotEmpty(vo37s)) {
            containerCapacityExcessBatchVerify(tenantId, vo37s);
        }

    }

    private void getUntilContainerId(Long tenantId, List<String> containerIds, String dtoContainerId) {
        for (String containerId : containerIds) {
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectId(containerId);
            mtContLoadDtlVO5.setLoadObjectType("CONTAINER");
            mtContLoadDtlVO5.setTopLevelFlag("N");
            List<String> superContainerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId,
                            mtContLoadDtlVO5);
            if (CollectionUtils.isEmpty(superContainerIds)) {
                throw new MtException("MT_MATERIAL_LOT_0051",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0051",
                                                "MATERIAL_LOT", "【API:containerLoadingObjectValidate】"));
            }
            if (superContainerIds.contains(dtoContainerId)) {
                return;
            }
            getUntilContainerId(tenantId, superContainerIds, dtoContainerId);
        }
    }

    private void batchGetUntilContainerId(Long tenantId, List<String> containerIds, String dtoContainerId) {
        List<MtContainerVO35> voList = containerIds.stream().map(t -> {
            MtContainerVO35 vo35 = new MtContainerVO35();
            vo35.setLoadObjectId(t);
            vo35.setLoadObjectType("CONTAINER");
            vo35.setTopLevelFlag(MtBaseConstants.NO);
            return vo35;
        }).collect(Collectors.toList());
        List<MtContainerVO36> superContainers =
                        mtContainerRepository.objectLimitLoadingContainerBatchQuery(tenantId, voList);

        if (CollectionUtils.isEmpty(superContainers)
                        || superContainers.stream().anyMatch(t -> CollectionUtils.isEmpty(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0051", "MATERIAL_LOT", "【API:containerLoadingObjectBatchValidate】"));
        }
        for (MtContainerVO36 superContainer : superContainers) {
            List<String> superContainerIds = superContainer.getContainerId();
            if (superContainerIds.contains(dtoContainerId)) {
                return;
            }
            batchGetUntilContainerId(tenantId, superContainerIds, dtoContainerId);
        }
    }

    /**
     * containerPropertyGet-获取容器属性
     *
     * @param tenantId
     * @param containerId
     * @author sen.luo
     * @date 2019/4/3
     */
    @Override
    public MtContainer containerPropertyGet(Long tenantId, String containerId) {
        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerPropertyGet】"));
        }

        MtContainer mtContainer = new MtContainer();
        mtContainer.setContainerId(containerId);
        mtContainer.setTenantId(tenantId);
        mtContainer = mtContainerMapper.selectOne(mtContainer);
        return mtContainer;
    }

    /**
     * containerStatusUpdateVerify-容器状态变更验证
     *
     * @param tenantId
     * @param containerId
     * @param targetStatus
     * @author sen.luo
     * @date 2019/4/3
     */
    @Override
    public void containerStatusUpdateVerify(Long tenantId, String containerId, String targetStatus) {
        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerStatusUpdateVerify】"));
        }

        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("MATERIAL_LOT");
        mtGenStatusVO2.setStatusGroup("CONTAINER_STATUS");
        List<MtGenStatus> bomStatus = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        List<String> status = bomStatus.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());

        if (!status.contains(targetStatus)) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", targetStatus, status.toString(),
                                            "【API:containerStatusUpdateVerify】"));
        }

        MtContainer mtContainer = containerPropertyGet(tenantId, containerId);

        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerStatusUpdateVerify】"));
        }

        if ("NEW".equals(targetStatus)) {
            throw new MtException("MT_MATERIAL_LOT_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0034", "MATERIAL_LOT", "【API:containerStatusUpdateVerify】"));
        } else if ("CANRELEASE".equals(targetStatus)) {
            if (!"NEW".equals(mtContainer.getStatus())) {
                throw new MtException("MT_MATERIAL_LOT_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0035", "MATERIAL_LOT", "【API:containerStatusUpdateVerify】"));
            }
        } else if ("HOLD".equals(targetStatus)) {
            if (!"CANRELEASE".equals(mtContainer.getStatus())) {
                throw new MtException("MT_MATERIAL_LOT_0036", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0036", "MATERIAL_LOT", "【API:containerStatusUpdateVerify】"));
            }
        } else if ("ABANDON".equals(targetStatus) && !"CANRELEASE".equals(mtContainer.getStatus())) {
            throw new MtException("MT_MATERIAL_LOT_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0037", "MATERIAL_LOT", "【API:containerStatusUpdateVerify】"));
        }
    }

    /**
     * containerAndMaterialLotReserve-容器和容器内物料批预留
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerAndMaterialLotReserve(Long tenantId, MtContainerVO8 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerAndMaterialLotReserve】"));
        }

        if (StringUtils.isEmpty(dto.getReservedObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectType",
                                            "【API:containerAndMaterialLotReserve】"));
        }

        if (StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectId",
                                            "【API:containerAndMaterialLotReserve】"));
        }

        // 2. 获取容器属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerAndMaterialLotReserve】"));
        }

        // 3. 获取容器内所有的物料批
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag("Y");
        List<MtContLoadDtlVO4> detailVO4List =
                        mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);

        for (MtContLoadDtlVO4 mtContLoadDtlVO4 : detailVO4List) {
            try {
                MtMaterialLotVO6 mtMaterialLotVO6 = new MtMaterialLotVO6();
                mtMaterialLotVO6.setMaterialLotId(mtContLoadDtlVO4.getMaterialLotId());
                mtMaterialLotVO6.setReservedObjectType(dto.getReservedObjectType());
                mtMaterialLotVO6.setReservedObjectId(dto.getReservedObjectId());
                this.mtMaterialLotRepository.materialLotReserveVerify(tenantId, mtMaterialLotVO6);
            } catch (Exception e) {
                continue;
            }

            MtMaterialLotVO7 mtMaterialLotVO7 = new MtMaterialLotVO7();
            mtMaterialLotVO7.setMaterialLotId(mtContLoadDtlVO4.getMaterialLotId());
            mtMaterialLotVO7.setReservedObjectType(dto.getReservedObjectType());
            mtMaterialLotVO7.setReservedObjectId(dto.getReservedObjectId());
            mtMaterialLotVO7.setWorkcellId(dto.getWorkcellId());
            mtMaterialLotVO7.setParentEventId(dto.getParentEventId());
            mtMaterialLotVO7.setEventRequestId(dto.getEventRequestId());
            mtMaterialLotVO7.setShiftDate(dto.getShiftDate());
            mtMaterialLotVO7.setShiftCode(dto.getShiftCode());
            this.mtMaterialLotRepository.materialLotReserve(tenantId, mtMaterialLotVO7);
        }

        // 5. 容器预留事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_RESERVE");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(mtContainer.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 6. 对容器进行预留
        mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag("Y");
        List<MtContLoadDtlVO3> mtContLoadDtlVO3s =
                        this.mtContainerLoadDetailRepository.containerLimitContainerQuery(tenantId, mtContLoadDtlVO10);
        List<String> containerIds = mtContLoadDtlVO3s.stream().map(MtContLoadDtlVO3::getLoadContainerId)
                        .collect(Collectors.toList());
        containerIds.add(dto.getContainerId());

        for (String containerId : containerIds) {
            try {
                MtContainerVO10 mtContainerVO10 = new MtContainerVO10();
                mtContainerVO10.setContainerId(containerId);
                mtContainerVO10.setReservedObjectType(dto.getReservedObjectType());
                mtContainerVO10.setReservedObjectId(dto.getReservedObjectId());
                containerReserveVerify(tenantId, mtContainerVO10);
            } catch (Exception ex) {
                continue;
            }

            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setContainerId(containerId);
            mtContainerVO12.setReservedObjectType(dto.getReservedObjectType());
            mtContainerVO12.setReservedObjectId(dto.getReservedObjectId());
            mtContainerVO12.setReservedFlag("Y");
            mtContainerVO12.setEventId(eventId);
            containerUpdate(tenantId, mtContainerVO12, "N");
        }
    }

    /**
     * containerAndMaterialLotReserveCancel-容器和容器内物料批预留取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerAndMaterialLotReserveCancel(Long tenantId, MtContainerVO14 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId",
                                            "【API:containerAndMaterialLotReserveCancel】"));
        }

        // 2. 获取容器属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerAndMaterialLotReserveCancel】"));
        }

        // 3. 获取容器内所有的物料批
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag("Y");

        List<MtContLoadDtlVO4> detailVO4List =
                        mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);


        // 4. 生成容器预留取消事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_RESERVE_CANCEL");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(mtContainer.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 6. 容器进行预留取消
        // 6.1. 获取容器内装载的所有的容器
        mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag("Y");
        List<MtContLoadDtlVO3> mtContLoadDtlVO3s =
                        this.mtContainerLoadDetailRepository.containerLimitContainerQuery(tenantId, mtContLoadDtlVO10);
        List<String> containerIds = mtContLoadDtlVO3s.stream().map(MtContLoadDtlVO3::getLoadContainerId)
                        .collect(Collectors.toList());
        containerIds.add(dto.getContainerId());

        // 6.2. 对容器进行预留取消
        for (String containerId : containerIds) {
            MtContainer loadContainer = containerPropertyGet(tenantId, containerId);
            if (null == loadContainer || !"Y".equals(loadContainer.getReservedFlag())) {
                continue;
            }

            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setContainerId(containerId);
            mtContainerVO12.setReservedObjectType("");
            mtContainerVO12.setReservedObjectId("");
            mtContainerVO12.setReservedFlag("N");
            mtContainerVO12.setEventId(eventId);
            containerUpdate(tenantId, mtContainerVO12, "N");
        }

        // 7. 针对容器下的物料批进行预留取消
        for (MtContLoadDtlVO4 detailVO4 : detailVO4List) {
            try {
                this.mtMaterialLotRepository.materialLotReserveCancelVerify(tenantId, detailVO4.getMaterialLotId());
            } catch (Exception e) {
                continue;
            }

            MtMaterialLotVO7 mtMaterialLotVO7 = new MtMaterialLotVO7();
            mtMaterialLotVO7.setMaterialLotId(detailVO4.getMaterialLotId());
            mtMaterialLotVO7.setWorkcellId(dto.getWorkcellId());
            mtMaterialLotVO7.setParentEventId(dto.getParentEventId());
            mtMaterialLotVO7.setEventRequestId(dto.getEventRequestId());
            mtMaterialLotVO7.setShiftDate(dto.getShiftDate());
            mtMaterialLotVO7.setShiftCode(dto.getShiftCode());
            this.mtMaterialLotRepository.materialLotReserveCancel(tenantId, mtMaterialLotVO7);
        }
    }

    @Override
    public void containerPackingLevelVerify(Long tenantId, MtContainerVO3 dto) {
        // Step1判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerPackingLevelVerify】"));
        }
        // 1-b
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerPackingLevelVerify】"));
        }

        // Step2据输入参数containerId调用API{ containerPropertyGet }获取容器对应的容器类型containerTypeId
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerPackingLevelVerify】"));
        }

        // Step3获取容器对应容器类型的包装等级要求
        MtContainerType mtContainerType =
                        mtContainerTypeRepository.containerTypePropertyGet(tenantId, mtContainer.getContainerTypeId());
        if (mtContainerType == null) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerPackingLevelVerify】"));
        }
        if (StringUtils.isEmpty(mtContainerType.getPackingLevel())) {
            return;
        }

        // Step4获取容器内所有已装载对象列表
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        mtConLoadDetailVO.setContainerId(dto.getContainerId());

        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        List<String> typeList =
                        loadDetails.stream().map(MtContLoadDtlVO6::getLoadObjectType).collect(Collectors.toList());
        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            if (CollectionUtils.isEmpty(loadDetails)) {
                return;
            }
            if (typeList.stream().allMatch(t -> mtContainerType.getPackingLevel().equals(t))) {
                return;
            } else {
                throw new MtException("MT_MATERIAL_LOT_0048", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0048", "MATERIAL_LOT", "【API:containerPackingLevelVerify】"));
            }
        } else {
            if (CollectionUtils.isEmpty(loadDetails)) {
                if (mtContainerType.getPackingLevel().equals(dto.getLoadObjectType())) {
                    return;
                } else {
                    throw new MtException("MT_MATERIAL_LOT_0048",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0048",
                                                    "MATERIAL_LOT", "【API:containerPackingLevelVerify】"));
                }
            } else {
                if (mtContainerType.getPackingLevel().equals(dto.getLoadObjectType())
                                && typeList.stream().allMatch(t -> mtContainerType.getPackingLevel().equals(t))) {
                    return;
                } else {
                    throw new MtException("MT_MATERIAL_LOT_0048",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0048",
                                                    "MATERIAL_LOT", "【API:containerPackingLevelVerify】"));
                }
            }
        }
    }

    /**
     * containerLimitContainerTypeGet-获取指定容器的容器类型
     *
     * @param tenantId
     * @param containerId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public String containerLimitContainerTypeGet(Long tenantId, String containerId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerLimitContainerTypeGet】"));
        }

        MtContainer mtContainer = new MtContainer();
        mtContainer.setTenantId(tenantId);
        mtContainer.setContainerId(containerId);
        mtContainer = mtContainerMapper.selectOne(mtContainer);
        if (mtContainer != null) {
            return mtContainer.getContainerTypeId();
        }
        return null;
    }

    @Override
    public void containerLoadVerify(Long tenantId, MtContainerVO9 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerLoadVerify】"));
        }

        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectType", "【API:containerLoadVerify】"));
        }

        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", "【API:containerLoadVerify】"));
        }

        // 判断传入状态是否符合规则
        List<String> types = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (!types.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0003", "MATERIAL_LOT", types.toString(), "【API:containerLoadVerify】"));
        }
        if ("EO".equals(dto.getLoadObjectType()) && dto.getTrxLoadQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0071",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0071",
                                            "MATERIAL_LOT", "loadObjectType = EO", "trxLoadQty",
                                            "【API:containerLoadVerify】"));
        }

        // 验证容器可用性验证结果
        containerAvailableValidate(tenantId, dto.getContainerId());

        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (null == mtContainer) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerLoadVerify】"));
        }

        String reservedFlag = mtContainer.getReservedFlag();
        String reservedObjectType = mtContainer.getReservedObjectType();
        String reservedObjectId = mtContainer.getReservedObjectId();
        String containerTypeId = mtContainer.getContainerTypeId();

        if ("EO".equals(dto.getLoadObjectType())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getLoadObjectId());
            if (null == mtEo) {
                throw new MtException("MT_MATERIAL_LOT_0052", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0052", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }

            // 判断执行作业已装载数量和欲装载数量是否超过执行作业数量
            Double eoLoadQty = mtContainerLoadDetailRepository.eoLimitLoadingEoQtyGet(tenantId, dto.getLoadObjectId());

            // 若已装载执行作业数量eo_loadQty+本次装载执行作业数量trxLoadQty＞执行作业数量qty 则返回报错
            if (BigDecimal.valueOf(eoLoadQty).add(BigDecimal.valueOf(dto.getTrxLoadQty()))
                            .compareTo(BigDecimal.valueOf(mtEo.getQty())) > 0) {
                throw new MtException("MT_MATERIAL_LOT_0068", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0068", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }
        } else if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
            MtMaterialLot mtMaterialLot =
                            this.mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getLoadObjectId());
            if (null == mtMaterialLot || !"Y".equals(mtMaterialLot.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_LOT_0052", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0052", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }

            if ("Y".equals(reservedFlag)) {
                if (!StringHelper.isSame(reservedFlag, mtMaterialLot.getReservedFlag())
                                || !StringHelper.isSame(reservedObjectType, mtMaterialLot.getReservedObjectType())
                                || !StringHelper.isSame(reservedObjectId, mtMaterialLot.getReservedObjectId())) {
                    throw new MtException("MT_MATERIAL_LOT_0054", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0054", "MATERIAL_LOT", "【API:containerLoadVerify】"));
                }
            }
        } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
            containerAvailableValidate(tenantId, dto.getLoadObjectId());
            MtContainer container = containerPropertyGet(tenantId, dto.getLoadObjectId());
            if (container == null) {
                throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }

            if ("Y".equals(reservedFlag)) {
                if (!StringHelper.isSame(reservedFlag, container.getReservedFlag())
                                || !StringHelper.isSame(reservedObjectType, container.getReservedObjectType())
                                || !StringHelper.isSame(reservedObjectId, container.getReservedObjectId())) {
                    throw new MtException("MT_MATERIAL_LOT_0054", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0054", "MATERIAL_LOT", "【API:containerLoadVerify】"));
                }
            }
        }

        if (!"EO".equals(dto.getLoadObjectType())) {
            // 判断装载对象和容器的库位是否一致
            String containerLocatorId = mtContainer.getLocatorId() == null ? "" : mtContainer.getLocatorId();
            if ("CONTAINER".equals(dto.getLoadObjectType())) {
                MtContainer loadContainer = containerPropertyGet(tenantId, dto.getLoadObjectId());
                if (loadContainer == null || !containerLocatorId.equals(loadContainer.getLocatorId())) {
                    throw new MtException("MT_MATERIAL_LOT_0088", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0088", "MATERIAL_LOT", "【API:containerLoadVerify】"));
                }
            }

            if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
                MtMaterialLot loadMaterialLot =
                                mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getLoadObjectId());
                if (loadMaterialLot == null || !containerLocatorId.equals(loadMaterialLot.getLocatorId())) {
                    throw new MtException("MT_MATERIAL_LOT_0088", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0088", "MATERIAL_LOT", "【API:containerLoadVerify】"));
                }
            }

            // 判断装载对象是否已被装载于容器中
            MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
            mtContLoadDtlVO5.setLoadObjectType(dto.getLoadObjectType());
            mtContLoadDtlVO5.setLoadObjectId(dto.getLoadObjectId());
            List<String> loadingContainerIds = this.mtContainerLoadDetailRepository
                            .objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
            if (CollectionUtils.isNotEmpty(loadingContainerIds)) {
                throw new MtException("MT_MATERIAL_LOT_0039", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0039", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }
        }

        // 3. 若装载对象为容器，判断是否存在嵌套装载
        if ("CONTAINER".equals(dto.getLoadObjectType())) {
            if (dto.getLoadObjectId().equals(dto.getContainerId())) {
                throw new MtException("MT_MATERIAL_LOT_0087", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0087", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }
            MtContainerVO2 mtContainerVO2 = new MtContainerVO2();
            mtContainerVO2.setContainerId(dto.getLoadObjectId());
            mtContainerVO2.setLoadObjectType("CONTAINER");
            mtContainerVO2.setLoadObjectId(dto.getContainerId());
            mtContainerVO2.setAllLevelFlag("Y");

            boolean isPass = true;
            try {
                MtContainerVO19 mtContainerVO19 = new MtContainerVO19();
                BeanUtils.copyProperties(mtContainerVO2, mtContainerVO19);
                containerLoadingObjectValidate(tenantId, mtContainerVO19);
            } catch (Exception ex) {
                isPass = false;
            }

            if (isPass) {
                throw new MtException("MT_MATERIAL_LOT_0053", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0053", "MATERIAL_LOT", "【API:containerLoadVerify】"));
            }
        }

        // 4. 获取容器对应容器类型的启用位置管理标识
        MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId);
        if (null == mtContainerType) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerLoadVerify】"));
        }

        if ("Y".equals(mtContainerType.getLocationEnabledFlag())) {
            // 5. 针对启用位置管理的容器验证容器位置是否为空
            if (dto.getLocationRow() == null) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "locationRow", "【API:containerLoadVerify】"));
            }
            if (dto.getLocationColumn() == null) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "locationColumn", "【API:containerLoadVerify】"));
            }

            // 调用API{containerLocationIsEmptyValidate}获取验证结果
            MtContainerVO6 mtContainerVO6 = new MtContainerVO6();
            mtContainerVO6.setContainerId(dto.getContainerId());
            mtContainerVO6.setLocationRow(dto.getLocationRow());
            mtContainerVO6.setLocationColumn(dto.getLocationColumn());
            containerLocationIsEmptyValidate(tenantId, mtContainerVO6);
        }

        // 6. 验证此次装载是否满足包装等级要求
        if (StringUtils.isNotEmpty(mtContainerType.getPackingLevel())) {
            MtContainerVO3 mtContainerVO3 = new MtContainerVO3();
            mtContainerVO3.setContainerId(dto.getContainerId());
            mtContainerVO3.setLoadObjectType(dto.getLoadObjectType());
            containerPackingLevelVerify(tenantId, mtContainerVO3);
        }

        // 7. 验证此次装载是否满足重量要求
        if (null != mtContainerType.getMaxLoadWeight()) {
            MtContainerDTO3 mtContainerDTO = new MtContainerDTO3();
            mtContainerDTO.setContainerId(dto.getContainerId());
            mtContainerDTO.setLoadObjectType(dto.getLoadObjectType());
            mtContainerDTO.setLoadObjectId(dto.getLoadObjectId());
            containerMaxLoadWeightExcessVerify(tenantId, mtContainerDTO);
        }

        // 8. 验证此次装载是否满足混合装载要求
        if ("N".equals(mtContainerType.getMixedMaterialFlag()) || "N".equals(mtContainerType.getMixedEoFlag())
                        || "N".equals(mtContainerType.getMixedWoFlag())
                        || "N".equals(mtContainerType.getMixedOwnerFlag())) {
            // 调用API{ containerMixedAllowVerify }获取验证结果
            MtContainerVO21 mtContainerVO21 = new MtContainerVO21();
            mtContainerVO21.setContainerId(dto.getContainerId());
            mtContainerVO21.setLoadObjectType(dto.getLoadObjectType());
            mtContainerVO21.setLoadObjectId(dto.getLoadObjectId());
            containerMixedAllowVerify(tenantId, mtContainerVO21);
        }

        // 9. 验证此次装载是否满足容器容量要求
        if (null != mtContainerType.getCapacityQty()) {
            MtContainerVO2 mtContainerVO2 = new MtContainerVO2();
            mtContainerVO2.setContainerId(dto.getContainerId());
            mtContainerVO2.setLoadObjectType(dto.getLoadObjectType());
            mtContainerVO2.setLoadObjectId(dto.getLoadObjectId());
            mtContainerVO2.setLoadQty(dto.getTrxLoadQty());
            containerCapacityExcessVerify(tenantId, mtContainerVO2);
        }
    }

    /**
     * containerAvailableValidate-容器可用性验证
     *
     * @param tenantId
     * @param containerId
     * @author sen.luo
     * @date 2019/4/3
     */
    @Override
    public void containerAvailableValidate(Long tenantId, String containerId) {
        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerAvailableValidate】"));
        }
        MtContainer mtContainer = containerPropertyGet(tenantId, containerId);

        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerAvailableValidate】"));
        }
        if (!"CANRELEASE".equals(mtContainer.getStatus())) {
            throw new MtException("MT_MATERIAL_LOT_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0033", "MATERIAL_LOT", "【API:containerAvailableValidate】"));
        }
    }

    @Override
    public void containerMaxLoadWeightExcessVerify(Long tenantId, MtContainerDTO3 dto) {
        // Step1判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerMaxLoadWeightExcessVerify】"));
        }
        // 1-b
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerMaxLoadWeightExcessVerify】"));

        }
        // 1-c
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                            "【API:containerMaxLoadWeightExcessVerify】"));

        }
        // Step2据输入参数containerId调用API{containerPropertyGet }获取容器对应的容器类型containerTypeId
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessVerify】"));

        }

        // Step3获取容器对应容器类型的容量限制
        MtContainerType mtContainerType =
                        mtContainerTypeRepository.containerTypePropertyGet(tenantId, mtContainer.getContainerTypeId());
        if (mtContainerType == null) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessVerify】"));
        }
        if (mtContainerType.getMaxLoadWeight() == null) {
            return;
        }

        // Step4获取容器内所有已装载物料及物料数量
        MtContLoadDtlVO detailVO = new MtContLoadDtlVO();
        detailVO.setContainerId(dto.getContainerId());
        detailVO.setAllLevelFlag("Y");
        List<MtContLoadDtlVO6> detailVO4List =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, detailVO);


        // Step5 若loadObjectType有输入，获取当前装载对象对应的物料和数量
        List<MtContLoadDtlVO6> detailVO6List2;
        List<String> loadMaterialLotId = new ArrayList<>();
        List<String> loadContainerId = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(detailVO4List)) {
            loadMaterialLotId.addAll(detailVO4List.stream().filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                            .map(MtContLoadDtlVO6::getLoadObjectId).distinct().collect(Collectors.toList()));
            loadContainerId.addAll(detailVO4List.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                            .map(MtContLoadDtlVO6::getLoadObjectId).distinct().collect(Collectors.toList()));
        }

        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && "MATERIAL_LOT".equals(dto.getLoadObjectType())) {
            loadMaterialLotId.add(dto.getLoadObjectId());
        }
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && "CONTAINER".equals(dto.getLoadObjectType())) {
            detailVO.setContainerId(dto.getLoadObjectId());
            loadContainerId.add(dto.getLoadObjectId());
            detailVO6List2 = mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, detailVO);
            if (CollectionUtils.isNotEmpty(detailVO6List2)) {
                loadMaterialLotId.addAll(detailVO6List2.stream()
                                .filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType()))
                                .map(MtContLoadDtlVO6::getLoadObjectId).distinct().collect(Collectors.toList()));
                loadContainerId.addAll(detailVO6List2.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType()))
                                .map(MtContLoadDtlVO6::getContainerId).distinct().collect(Collectors.toList()));
            }
        }

        // Step6判断装载是否超出容器容量限制
        BigDecimal containerWeight = BigDecimal.ZERO;
        List<String> containerIds = new ArrayList<>();
        // 6-a
        if (!loadContainerId.stream().allMatch(t -> dto.getContainerId().equals(t))) {
            containerIds.addAll(loadContainerId.stream().filter(t -> !dto.getContainerId().equals(t)).distinct()
                            .collect(Collectors.toList()));
            List<MtContainer> mtContainers = containerPropertyBatchGet(tenantId, containerIds);
            for (MtContainer t : mtContainers) {
                MtContainerType type =
                                mtContainerTypeRepository.containerTypePropertyGet(tenantId, t.getContainerTypeId());
                if (type != null) {
                    if (StringUtils.isEmpty(type.getWeightUomId()) || (StringUtils.isNotEmpty(type.getWeightUomId())
                                    && type.getWeightUomId().equals(mtContainerType.getWeightUomId()))) {
                        containerWeight = containerWeight.add(type.getWeight() == null ? BigDecimal.ZERO
                                        : BigDecimal.valueOf(type.getWeight()));
                    } else if (StringUtils.isNotEmpty(type.getWeightUomId())
                                    && !type.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                        MtUomVO1 mtUomVO1 = new MtUomVO1();
                        mtUomVO1.setSourceUomId(type.getWeightUomId());
                        mtUomVO1.setSourceValue(type.getWeight());
                        mtUomVO1.setTargetUomId(mtContainerType.getWeightUomId());
                        mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                        if (mtUomVO1 != null) {
                            containerWeight = containerWeight.add(mtUomVO1.getTargetValue() == null ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(mtUomVO1.getTargetValue()));
                        }
                    }
                }
            }
        }

        // 6-b
        BigDecimal materialWeight = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(loadMaterialLotId)) {
            List<MtMaterialLot> lots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, loadMaterialLotId);
            Map<String, Map<String, BigDecimal>> resultMap = lots.stream().collect(Collectors.groupingBy(
                            MtMaterialLot::getMaterialId,
                            Collectors.groupingBy(MtMaterialLot::getPrimaryUomId,
                                            CollectorsUtil.summingBigDecimal(
                                                            c -> BigDecimal.valueOf(c.getPrimaryUomQty() == null ? 0.0D
                                                                            : c.getPrimaryUomQty())))));

            List<MtMaterialLot> lots1 = new ArrayList<>();
            resultMap.entrySet().forEach(t -> {
                t.getValue().entrySet().forEach(tt -> {
                    MtMaterialLot lot = new MtMaterialLot();
                    lot.setMaterialId(t.getKey());
                    lot.setPrimaryUomId(tt.getKey());
                    lot.setPrimaryUomQty(tt.getValue().doubleValue());
                    lots1.add(lot);
                });
            });

            List<String> materialIds =
                            lots1.stream().map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
            List<MtMaterialVO> materialVOist = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);

            if (CollectionUtils.isNotEmpty(materialVOist)) {
                for (MtMaterialVO m : materialVOist) {
                    if (StringUtils.isEmpty(m.getWeightUomId())) {
                        m.setWeightUomId(mtContainerType.getWeightUomId());
                    }

                    BigDecimal weight = m.getWeight() == null ? BigDecimal.ZERO : BigDecimal.valueOf(m.getWeight());
                    List<MtMaterialLot> lost2 = lots1.stream().filter(t -> t.getMaterialId().equals(m.getMaterialId()))
                                    .collect(Collectors.toList());

                    for (MtMaterialLot s : lost2) {
                        if (s.getPrimaryUomId().equals(m.getPrimaryUomId()) && weight.compareTo(BigDecimal.ZERO) != 0
                                        && m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {

                            materialWeight = materialWeight.add(weight.multiply(
                                            BigDecimal.valueOf(s.getPrimaryUomQty() == null ? Double.valueOf(0.0D)
                                                            : s.getPrimaryUomQty())));

                        } else if (s.getPrimaryUomId().equals(m.getPrimaryUomId())
                                        && weight.compareTo(BigDecimal.ZERO) != 0
                                        && !m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                            MtUomVO1 transferUomVO = new MtUomVO1();
                            transferUomVO.setSourceUomId(m.getWeightUomId());
                            transferUomVO.setSourceValue(new BigDecimal(s.getPrimaryUomQty().toString())
                                            .multiply(new BigDecimal(m.getWeight().toString())).doubleValue());
                            transferUomVO.setTargetUomId(mtContainerType.getWeightUomId());
                            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);

                            if (null != transferUomVO) {
                                materialWeight = materialWeight.add(BigDecimal
                                                .valueOf(transferUomVO.getTargetValue() == null ? Double.valueOf(0.0D)
                                                                : transferUomVO.getTargetValue()));
                            }
                        } else if (!s.getPrimaryUomId().equals(m.getPrimaryUomId())
                                        && weight.compareTo(BigDecimal.ZERO) != 0) {
                            MtUomVO1 mtUomVO1 = new MtUomVO1();
                            mtUomVO1.setSourceUomId(s.getPrimaryUomId());
                            mtUomVO1.setSourceValue(s.getPrimaryUomQty());
                            mtUomVO1.setTargetUomId(m.getPrimaryUomId());
                            mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);

                            if (m.getWeightUomId().equals(mtContainerType.getWeightUomId())) {
                                materialWeight = materialWeight.add(BigDecimal.valueOf(m.getWeight())
                                                .multiply(BigDecimal.valueOf(mtUomVO1.getTargetValue())));
                            } else {
                                MtUomVO1 uomVO1 = new MtUomVO1();
                                uomVO1.setSourceUomId(m.getWeightUomId());
                                uomVO1.setSourceValue(BigDecimal.valueOf(uomVO1.getTargetValue())
                                                .multiply(BigDecimal.valueOf(m.getWeight())).doubleValue());
                                uomVO1.setTargetUomId(mtContainerType.getWeightUomId());
                                uomVO1 = mtUomRepository.uomConversion(tenantId, uomVO1);
                                materialWeight = materialWeight.add(BigDecimal.valueOf(uomVO1.getTargetValue()));
                            }

                        }
                    }
                }
            }
        }

        // 6-c
        BigDecimal weight = containerWeight.add(materialWeight);
        if (weight.compareTo(BigDecimal.valueOf(mtContainerType.getMaxLoadWeight())) == 1) {
            throw new MtException("MT_MATERIAL_LOT_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0049", "MATERIAL_LOT", "【API:containerMaxLoadWeightExcessVerify】"));
        }
    }

    @Override
    public List<MtContainer> containerPropertyBatchGet(Long tenantId, List<String> containerIds) {
        if (CollectionUtils.isEmpty(containerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerPropertyBatchGet】"));

        }
        return mtContainerMapper.selectByContainerIds(tenantId, containerIds);
    }

    /**
     * containerReserveVerify-容器预留验证
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    @Override
    public void containerReserveVerify(Long tenantId, MtContainerVO10 dto) {
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerReserveVerify】"));
        }

        if (StringUtils.isEmpty(dto.getReservedObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectType", "【API:containerReserveVerify】"));
        }

        if (StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "reservedObjectId", "【API:containerReserveVerify】"));
        }

        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());

        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerReserveVerify】"));
        }

        if ("Y".equals(mtContainer.getReservedFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0058", "MATERIAL_LOT", "【API:containerReserveVerify】"));
        }

        containerAvailableValidate(tenantId, dto.getContainerId());

        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        mtConLoadDetailVO.setContainerId(dto.getContainerId());
        mtConLoadDetailVO.setAllLevelFlag("Y");
        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        if (CollectionUtils.isEmpty(loadDetails)) {
            return;
        }

        for (MtContLoadDtlVO6 mld : loadDetails) {
            if ("EO".equals(mld.getLoadObjectType())) {
                continue;
            } else if ("MATERIAL_LOT".equals(mld.getLoadObjectType())) {
                MtMaterialLot lot =
                                this.mtMaterialLotRepository.materialLotPropertyGet(tenantId, mld.getLoadObjectId());
                // 确认过，取不到数据按不一致处理
                if (lot == null) {
                    throw new MtException("MT_MATERIAL_LOT_0059",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0059",
                                                    "MATERIAL_LOT", "【MATERIAL_LOT：" + mld.getLoadObjectId() + "/null】",
                                                    "【API:containerReserveVerify】"));
                }

                if ("Y".equals(lot.getReservedFlag()) && (!StringHelper.isSame(lot.getReservedObjectType(),
                                dto.getReservedObjectType())
                                || !StringHelper.isSame(lot.getReservedObjectId(), dto.getReservedObjectId()))) {
                    throw new MtException("MT_MATERIAL_LOT_0059",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0059",
                                                    "MATERIAL_LOT",
                                                    mld.getLoadObjectId() + "/" + lot.getMaterialLotCode(),
                                                    "【API:containerReserveVerify】"));
                }
            } else if ("CONTAINER".equals(mld.getLoadObjectType())) {
                MtContainer container = containerPropertyGet(tenantId, mld.getLoadObjectId());
                if (container == null) {
                    throw new MtException("MT_MATERIAL_LOT_0059",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0059",
                                                    "MATERIAL_LOT", "【MATERIAL_LOT：" + mld.getLoadObjectId() + "/null】",
                                                    "【API:containerReserveVerify】"));
                }
                if ("Y".equals(container.getReservedFlag()) && (!StringHelper.isSame(container.getReservedObjectType(),
                                dto.getReservedObjectType())
                                || !StringHelper.isSame(container.getReservedObjectId(), dto.getReservedObjectId()))) {
                    throw new MtException("MT_MATERIAL_LOT_0059",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0059",
                                                    "MATERIAL_LOT",
                                                    mld.getLoadObjectId() + "/" + container.getContainerCode(),
                                                    "【API:containerReserveVerify】"));
                }
            }
        }
    }

    /**
     * containerLoad-容器装载
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerLoad(Long tenantId, MtContainerVO24 dto) {
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLoad】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectType", "【API：containerLoad】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", "【API：containerLoad】"));
        }

        List<String> types = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");

        if (!types.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", types.toString(), "【API：containerLoad】"));
        }
        if (dto.getTrxLoadQty() != null) {
            if (BigDecimal.valueOf(dto.getTrxLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                throw new MtException("MT_MATERIAL_LOT_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                                "MATERIAL_LOT", "trxLoadQty ", "【API：containerLoad】"));
            }
        }

        // 2. 获取容器装载事件
        String eventTypeCode = null;
        if ("EO".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_EO_LOAD";
        } else if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_MATERIAL_LOT_LOAD";
        } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_LOAD";
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 新增容器装载实绩
        MtContLoadDtlVO8 mtContLoadDtlVO8 = new MtContLoadDtlVO8();
        mtContLoadDtlVO8.setContainerId(dto.getContainerId());
        mtContLoadDtlVO8.setLocationRow(dto.getLocationRow());
        mtContLoadDtlVO8.setLocationColumn(dto.getLocationColumn());
        mtContLoadDtlVO8.setLoadObjectType(dto.getLoadObjectType());
        mtContLoadDtlVO8.setLoadObjectId(dto.getLoadObjectId());
        mtContLoadDtlVO8.setTrxLoadQty(dto.getTrxLoadQty());
        mtContLoadDtlVO8.setLoadEoStepActualId(dto.getLoadEoStepActualId());
        mtContLoadDtlVO8.setEventId(eventId);
        this.mtContainerLoadDetailRepository.containerLoadDetailUpdate(tenantId, mtContLoadDtlVO8);

        // 4. 更新容器数据
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setLastLoadTime(new Date());
        mtContainerVO12.setEventId(eventId);
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    /**
     * containerUnload-容器卸载
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerUnload(Long tenantId, MtContainerVO25 dto) {
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerUnload】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectType", "【API：containerUnload】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", "【API：containerUnload】"));
        }

        List<String> types = new ArrayList<String>(Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER"));

        if (!types.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", types.toString(),
                                            "【API：containerUnload】"));
        }

        // 2. 容器装载事件
        String eventTypeCode = null;
        if ("EO".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_EO_UNLOAD";
        } else if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_MATERIAL_LOT_UNLOAD";
        } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
            eventTypeCode = "CONTAINER_UNLOAD";
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 新增容器卸载实绩
        if ("EO".equals(dto.getLoadObjectType()) && dto.getTrxUnloadQty() != null) {
            if (BigDecimal.valueOf(dto.getTrxUnloadQty()).compareTo(BigDecimal.ZERO) < 0) {
                throw new MtException("MT_MATERIAL_LOT_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                                "MATERIAL_LOT", "trxUnloadQty ", "【API：containerUnload】"));
            }

            MtContLoadDtlVO8 mtContLoadDtlVO8 = new MtContLoadDtlVO8();
            mtContLoadDtlVO8.setContainerId(dto.getContainerId());
            mtContLoadDtlVO8.setLocationRow(dto.getLocationRow());
            mtContLoadDtlVO8.setLocationColumn(dto.getLocationColumn());
            mtContLoadDtlVO8.setLoadObjectType(dto.getLoadObjectType());
            mtContLoadDtlVO8.setLoadObjectId(dto.getLoadObjectId());
            mtContLoadDtlVO8.setTrxLoadQty(-dto.getTrxUnloadQty());
            mtContLoadDtlVO8.setEventId(eventId);
            this.mtContainerLoadDetailRepository.containerLoadDetailUpdate(tenantId, mtContLoadDtlVO8);
        } else {
            // 删除容器装载实绩
            MtContLoadDtlVO9 mtContLoadDtlVO9 = new MtContLoadDtlVO9();
            mtContLoadDtlVO9.setContainerId(dto.getContainerId());
            mtContLoadDtlVO9.setLoadObjectType(dto.getLoadObjectType());
            mtContLoadDtlVO9.setLoadObjectId(dto.getLoadObjectId());
            mtContLoadDtlVO9.setLocationRow(dto.getLocationRow());
            mtContLoadDtlVO9.setLocationColumn(dto.getLocationColumn());
            mtContLoadDtlVO9.setEventId(eventId);
            this.mtContainerLoadDetailRepository.containerLoadDetailDelete(tenantId, mtContLoadDtlVO9);
        }

        // 4. 更新容器数据
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setLastUnloadTime(new Date());
        mtContainerVO12.setEventId(eventId);
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerBatchUnload(Long tenantId, MtContLoadDtlVO30 dto) {
        final String apiName = "【API:containerBatchUnload】";

        if (MtIdHelper.isIdNull(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0001", "MATERIAL_LOT","containerId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getUnLoadObjectList())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0001","MATERIAL_LOT", "unLoadObjectList", apiName));
        }
        if (dto.getUnLoadObjectList().stream().anyMatch(t -> StringUtils.isEmpty(t.getLoadObjectType()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0001","MATERIAL_LOT", "loadObjectType", apiName));
        }
        if (dto.getUnLoadObjectList().stream().anyMatch(t -> MtIdHelper.isIdNull(t.getLoadObjectId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0001", "MATERIAL_LOT","loadObjectId", apiName));
        }

        List<String> types = new ArrayList<>(Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER"));
        if (dto.getUnLoadObjectList().stream().anyMatch(t -> !types.contains(t.getLoadObjectType()))) {
            throw new MtException("MT_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0003", "MATERIAL_LOT","loadObjectType", types.toString(), apiName));
        }

        // 2. 按照是否传入为eo并且传入数量，划分输入参数集合
        List<MtContLoadDtlVO29> eoUnloadQtyInputList = dto.getUnLoadObjectList().stream().filter(
                t -> MtBaseConstants.LOAD_TYPE.EO.equals(t.getLoadObjectType()) && t.getTrxUnloadQty() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(eoUnloadQtyInputList)) {
            if (eoUnloadQtyInputList.stream()
                    .anyMatch(t -> BigDecimal.valueOf(t.getTrxUnloadQty()).compareTo(BigDecimal.ZERO) < 0)) {
                throw new MtException("MT_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_LOT_0007", "MATERIAL_LOT","trxUnloadQty ", apiName));
            }
        }

        // 3. 容器装载事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(MtBaseConstants.EVENT_TYPE.CONTAINER_UNLOAD);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 执行eo卸载
        if (CollectionUtils.isNotEmpty(eoUnloadQtyInputList)) {
            List<MtContLoadDtlVO16> mtContLoadDtlList = new ArrayList<>();
            for (MtContLoadDtlVO29 input : eoUnloadQtyInputList) {
                MtContLoadDtlVO16 contLoadDtl = new MtContLoadDtlVO16();
                contLoadDtl.setContainerId(dto.getContainerId());
                contLoadDtl.setLocationRow(dto.getLocationRow());
                contLoadDtl.setLocationColumn(dto.getLocationColumn());

                contLoadDtl.setLoadObjectType(input.getLoadObjectType());
                contLoadDtl.setLoadObjectId(input.getLoadObjectId());
                contLoadDtl.setTrxLoadQty(-input.getTrxUnloadQty());

                mtContLoadDtlList.add(contLoadDtl);
            }

            MtContLoadDtlVO12 detailBatchUpdateVO = new MtContLoadDtlVO12();
            detailBatchUpdateVO.setEventId(eventId);
            detailBatchUpdateVO.setMtContLoadDtlList(mtContLoadDtlList);
            this.mtContainerLoadDetailRepository.containerLoadDetailBatchUpdate(tenantId, detailBatchUpdateVO,
                    MtBaseConstants.NO);
        }

        // 5. 其余的执行卸载
        List<MtContLoadDtlVO29> othersInputList = dto.getUnLoadObjectList().stream()
                .filter(t -> !(MtBaseConstants.LOAD_TYPE.EO.equals(t.getLoadObjectType())
                        && t.getTrxUnloadQty() != null))
                .collect(Collectors.toList());

        List<MtContLoadDtlVO13> unloadObjectList = new ArrayList<>();
        for (MtContLoadDtlVO29 othersInput : othersInputList) {
            MtContLoadDtlVO13 unloadObject = new MtContLoadDtlVO13();
            unloadObject.setLoadObjectId(othersInput.getLoadObjectId());
            unloadObject.setLoadObjectType(othersInput.getLoadObjectType());
            unloadObjectList.add(unloadObject);
        }
        MtContLoadDtlVO31 loadDetailBatchDeleteVO = new MtContLoadDtlVO31();
        loadDetailBatchDeleteVO.setContainerId(dto.getContainerId());
        loadDetailBatchDeleteVO.setLocationRow(dto.getLocationRow());
        loadDetailBatchDeleteVO.setLocationColumn(dto.getLocationColumn());
        loadDetailBatchDeleteVO.setUnloadObjectList(unloadObjectList);
        loadDetailBatchDeleteVO.setEventId(eventId);
        mtContainerLoadDetailRepository.containerLoadDetailBatchDelete(tenantId, loadDetailBatchDeleteVO);

        // 4. 更新容器数据
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setLastUnloadTime(new Date());
        mtContainerVO12.setEventId(eventId);
        containerUpdate(tenantId, mtContainerVO12, MtBaseConstants.NO);
    }

    /**
     * containerUpdate-容器更新&新增并记录容器变更历史
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtContainerVO26 containerUpdate(Long tenantId, MtContainerVO12 dto, String fullUpdate) {
        MtContainerVO26 result = new MtContainerVO26();
        // 第一步：判断输入参数是否合规
        if (dto == null || StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：containerUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId())) {
            throw new MtException("MT_MATERIAL_LOT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0014", "MATERIAL_LOT", "ownerType", "ownerId", "【API：containerUpdate】"));

        }
        if (StringUtils.isNotEmpty(dto.getReservedObjectType()) && StringUtils.isEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "reservedObjectType", "reservedObjectId",
                                            "【API：containerUpdate】"));

        }

        // 2.第二步，根据输入参数判断为需新增数据或是更新数据
        MtContainer mtContainer = null;
        if (StringUtils.isNotEmpty(dto.getContainerId())) {
            mtContainer = new MtContainer();
            mtContainer.setTenantId(tenantId);
            mtContainer.setContainerId(dto.getContainerId());
            mtContainer = mtContainerMapper.selectOne(mtContainer);
            if (mtContainer == null) {
                throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerUpdate】"));
            }
        } else {
            if (StringUtils.isNotEmpty(dto.getContainerCode())) {
                mtContainer = new MtContainer();
                mtContainer.setContainerCode(dto.getContainerCode());
                mtContainer.setTenantId(tenantId);
                // 可能为空null
                mtContainer = mtContainerMapper.selectOne(mtContainer);
            }
        }

        if (mtContainer == null) {
            // 插入
            if (StringUtils.isEmpty(dto.getContainerTypeId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "containerId、containerTypeId",
                                                "【API：containerUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSiteId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "containerId、siteId", "【API：containerUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getLocatorId())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "containerId、locatorId", "【API：containerUpdate】"));
            }

            mtContainer = new MtContainer();
            BeanUtils.copyProperties(dto, mtContainer);
            // CONTAINER_CODE
            Map<String, String> codeMap = new HashMap<>(0);
            String containerTypeCode;
            String siteCode;
            String locatorCode;
            if (StringUtils.isNotEmpty(mtContainer.getContainerCode())) {
                mtContainer.setContainerCode(dto.getContainerCode());
            } else {
                String nextCode = mtContainerMapper.containerNextCodeGet(tenantId, dto.getSiteId());
                mtContainer.setContainerCode(nextCode);

                // 获取containerTypeCode
                MtContainerType type =
                                mtContainerTypeRepository.containerTypePropertyGet(tenantId, dto.getContainerTypeId());
                if (type != null && StringUtils.isNotEmpty(type.getContainerTypeCode())) {
                    containerTypeCode = type.getContainerTypeCode();
                    codeMap.put("containerTypeCode", containerTypeCode);
                }

                // 获取siteCode
                MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
                if (mtModSite != null && StringUtils.isNotEmpty(mtModSite.getSiteCode())) {
                    siteCode = mtModSite.getSiteCode();
                    codeMap.put("siteCode", siteCode);
                }

                if (StringUtils.isNotEmpty(dto.getOwnerType())) {
                    // 获取ownerType
                    codeMap.put("ownerType", dto.getOwnerType());
                }

                // 获取locatorCode
                MtModLocator mtModLocator =
                                mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getLocatorId());
                if (mtModLocator != null && StringUtils.isNotEmpty(mtModLocator.getLocatorCode())) {
                    locatorCode = mtModLocator.getLocatorCode();
                    codeMap.put("locatorCode", locatorCode);
                }

                // 调用API{ containerNextCodeGet }获取返回number，
                MtContainerVO33 containerVO33 = new MtContainerVO33();
                containerVO33.setSiteId(dto.getSiteId());
                containerVO33.setIncomingValueList(dto.getIncomingValueList());
                containerVO33.setOutsideNum(dto.getOutsideNum());
                containerVO33.setContainerPropertyList(codeMap);
                MtNumrangeVO5 mtNumrangeVO5 = mtContainerRepository.containerNextCodeGet(tenantId, containerVO33);

                if (null != mtNumrangeVO5) {
                    mtContainer.setContainerCode(mtNumrangeVO5.getNumber());
                }
            }


            if (StringUtils.isEmpty(mtContainer.getStatus())) {
                mtContainer.setStatus("NEW");
            }
            if (StringUtils.isNotEmpty(dto.getIdentification())) {
                mtContainer.setIdentification(dto.getIdentification());
            }
            if (StringUtils.isEmpty(mtContainer.getIdentification())) {
                // 存入为输入值identification，未输入则存入为输入的containerCode
                mtContainer.setIdentification(mtContainer.getContainerCode());
            }
            // update by peng.yuan 2019-11-11

            if (StringUtils.isNotEmpty(dto.getCurrentContainerId())) {
                mtContainer.setCurrentContainerId(dto.getCurrentContainerId());
            }
            if (dto.getTopContainerId() != null) {
                mtContainer.setTopContainerId(dto.getTopContainerId());
            } else {
                // 若topContainerId无值输入，判断currentContainerId是否有值输入
                if (dto.getCurrentContainerId() != null && "".equals(dto.getCurrentContainerId())) {
                    mtContainer.setTopContainerId("");
                }
                if (StringUtils.isNotEmpty(dto.getCurrentContainerId())) {
                    MtContainer container = new MtContainer();
                    container.setTenantId(tenantId);
                    container.setContainerId(dto.getCurrentContainerId());
                    container = mtContainerMapper.selectOne(container);
                    // 若获取到的topContainerId为空，则将TOP_CONTAINER_ID更新为输入参数currentContainerId
                    if (null == container || StringUtils.isEmpty(container.getTopContainerId())) {
                        mtContainer.setTopContainerId(dto.getCurrentContainerId());
                    } else {
                        // 若获取到的topContainerId不为空，则将TOP_CONTAINER_ID更新为获取到的topContainerId
                        mtContainer.setTopContainerId(container.getTopContainerId());
                    }
                }
                if (dto.getCurrentContainerId() == null) {
                    // 若未输入currentContainerId值则不存入
                    mtContainer.setTopContainerId(null);
                }

            }
            mtContainer.setTenantId(tenantId);
            self().insertSelective(mtContainer);

            // 记录历史
            MtContainerHis mtContainerHis = new MtContainerHis();

            BeanUtils.copyProperties(mtContainer, mtContainerHis);
            mtContainerHis.setEventId(dto.getEventId());
            mtContainerHis.setTenantId(tenantId);
            mtContainerHisRepository.insertSelective(mtContainerHis);

            // 主表维护最新历史字段
            mtContainer.setLatestHisId(mtContainerHis.getContainerHisId());
            self().updateByPrimaryKeySelective(mtContainer);

            result.setContainerId(mtContainer.getContainerId());
            result.setContainerHisId(mtContainerHis.getContainerHisId());

        } else {
            if (MtBaseConstants.YES.equals(fullUpdate)) {
                // 有输入则更新。没有输入则不更新
                dto.setContainerId(mtContainer.getContainerId());
                BeanUtils.copyProperties(dto, mtContainer);
            } else {
                if (dto.getContainerCode() != null) {
                    mtContainer.setContainerCode(dto.getContainerCode());
                }
                if (dto.getContainerTypeId() != null) {
                    mtContainer.setContainerTypeId(dto.getContainerTypeId());
                }
                if (dto.getContainerName() != null) {
                    mtContainer.setContainerName(dto.getContainerName());
                }
                if (dto.getDescription() != null) {
                    mtContainer.setDescription(dto.getDescription());
                }
                if (dto.getSiteId() != null) {
                    mtContainer.setSiteId(dto.getSiteId());
                }
                if (dto.getLocatorId() != null) {
                    mtContainer.setLocatorId(dto.getLocatorId());
                }
                if (dto.getStatus() != null) {
                    mtContainer.setStatus(dto.getStatus());
                }
                if (dto.getOwnerType() != null) {
                    mtContainer.setOwnerType(dto.getOwnerType());
                }
                if (dto.getOwnerId() != null) {
                    mtContainer.setOwnerId(dto.getOwnerId());
                }
                if (dto.getReservedFlag() != null) {
                    mtContainer.setReservedFlag(dto.getReservedFlag());
                }
                if (dto.getReservedObjectType() != null) {
                    mtContainer.setReservedObjectType(dto.getReservedObjectType());
                }
                if (dto.getReservedObjectId() != null) {
                    mtContainer.setReservedObjectId(dto.getReservedObjectId());
                }
                if (dto.getIdentification() != null) {
                    mtContainer.setIdentification(dto.getIdentification());
                }
                if (dto.getLastLoadTime() != null) {
                    mtContainer.setLastLoadTime(dto.getLastLoadTime());
                }
                if (dto.getLastUnloadTime() != null) {
                    mtContainer.setLastUnloadTime(dto.getLastUnloadTime());
                }
                if (dto.getCurrentContainerId() != null) {
                    mtContainer.setCurrentContainerId(dto.getCurrentContainerId());
                }
                if (dto.getTopContainerId() != null) {
                    mtContainer.setTopContainerId(dto.getTopContainerId());
                }
            }

            // update by peng.yuan 2019-11-11
            if (null == dto.getTopContainerId()) {
                if (null != dto.getCurrentContainerId() && "".equals(dto.getCurrentContainerId())) {
                    mtContainer.setTopContainerId("");
                }
                if (StringUtils.isNotEmpty(dto.getCurrentContainerId())) {
                    MtContainer container = new MtContainer();
                    container.setTenantId(tenantId);
                    container.setContainerId(dto.getCurrentContainerId());
                    container = mtContainerMapper.selectOne(container);
                    // 若获取到的topContainerId为空，则将TOP_CONTAINER_ID更新为输入参数currentContainerId
                    if (null == container || StringUtils.isEmpty(container.getTopContainerId())) {
                        mtContainer.setTopContainerId(dto.getCurrentContainerId());
                    } else {
                        // 若获取到的topContainerId不为空，则将TOP_CONTAINER_ID更新为获取到的topContainerId
                        mtContainer.setTopContainerId(container.getTopContainerId());
                    }
                }
                if (dto.getCurrentContainerId() == null) {
                    // 若未输入currentContainerId值则不存入
                    mtContainer.setTopContainerId(null);
                }
            }

            mtContainer.setTenantId(tenantId);

            // 记录历史
            MtContainerHis mtContainerHis = new MtContainerHis();
            BeanUtils.copyProperties(mtContainer, mtContainerHis);
            mtContainerHis.setEventId(dto.getEventId());
            mtContainerHis.setTenantId(tenantId);
            mtContainerHisRepository.insertSelective(mtContainerHis);

            // 主表维护最新历史字段
            mtContainer.setLatestHisId(mtContainerHis.getContainerHisId());

            if ("Y".equals(fullUpdate)) {
                // 将String类型为null的字段更新为""
                mtContainer = (MtContainer) ObjectFieldsHelper.setStringFieldsEmpty(mtContainer);
                self().updateByPrimaryKey(mtContainer);
            } else {
                self().updateByPrimaryKeySelective(mtContainer);
            }

            result.setContainerId(mtContainer.getContainerId());
            result.setContainerHisId(mtContainerHis.getContainerHisId());
        }

        return result;
    }

    @Override
    public MtContainerVO5 containerFirstAvailableLocationGet(Long tenantId, MtContainerVO4 dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API：containerFirstAvailableLocationGet】"));
        }
        // Step2 获取容器的行和列数
        String containerTypeId = containerLimitContainerTypeGet(tenantId, dto.getContainerId());
        if (StringUtils.isEmpty(containerTypeId)) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerFirstAvailableLocationGet】"));
        }

        // 2-b
        MtContainerType type = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId);
        if (type == null || StringUtils.isEmpty(type.getLocationEnabledFlag())
                        || "N".equals(type.getLocationEnabledFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0055", "MATERIAL_LOT", "【API：containerFirstAvailableLocationGet】"));
        }

        // Step3获取容器已装载的行和列
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        mtConLoadDetailVO.setContainerId(dto.getContainerId());
        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        if (CollectionUtils.isEmpty(loadDetails)) {
            MtContainerVO5 mtcontainerVO5 = new MtContainerVO5();
            mtcontainerVO5.setNextLoactionRow(1L);
            mtcontainerVO5.setNextLocationColumn(1L);
            return mtcontainerVO5;
        }

        // Step4获取位置最小的容器行
        List<MtContainerVO5> vo5List = new ArrayList<>();
        List<MtContainerVO5> vo5List2 = new ArrayList<>();
        for (int i = 1; i <= type.getLocationRow(); i++) {
            for (int j = 1; j <= type.getLocationColumn(); j++) {
                MtContainerVO5 vo5 = new MtContainerVO5();
                vo5.setNextLoactionRow(Long.valueOf(i));
                vo5.setNextLocationColumn(Long.valueOf(j));
                vo5List.add(vo5);
            }
        }

        loadDetails.stream().forEach(t -> {
            MtContainerVO5 vo5 = new MtContainerVO5();
            vo5.setNextLoactionRow(t.getLocationRow());
            vo5.setNextLocationColumn(t.getLocationColumn());
            vo5List2.add(vo5);
        });
        vo5List.removeAll(vo5List2);
        if (CollectionUtils.isEmpty(vo5List)) {
            return null;
        }

        if (vo5List.size() == 1) {
            return vo5List.get(0);
        }

        if (StringUtils.isEmpty(dto.getRowPriorityFlag()) || "Y".equals(dto.getRowPriorityFlag())) {
            vo5List.sort(Comparator
                            .comparingLong((MtContainerVO5 c) -> c.getNextLoactionRow() == null ? Long.valueOf(0L)
                                            : c.getNextLoactionRow())
                            .thenComparingLong(
                                            (MtContainerVO5 c) -> c.getNextLocationColumn() == null ? Long.valueOf(0L)
                                                            : c.getNextLocationColumn()));
        } else {
            vo5List.sort(Comparator
                            .comparingLong((MtContainerVO5 c) -> c.getNextLocationColumn() == null ? Long.valueOf(0L)
                                            : c.getNextLocationColumn())
                            .thenComparingLong((MtContainerVO5 c) -> c.getNextLoactionRow() == null ? Long.valueOf(0L)
                                            : c.getNextLoactionRow()));
        }
        return vo5List.get(0);
    }

    @Override
    public void containerMixedAllowVerify(Long tenantId, MtContainerVO21 dto) {
        // Step1判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerMixedAllowVerify】"));
        }
        // 1-b
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerMixedAllowVerify】"));

        }
        // 1-c
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                            "【API:containerMixedAllowVerify】"));

        }

        // Step2获取容器对应的容器类型
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());

        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerMixedAllowVerify】"));
        }

        // Step3获取容器对应容器类型的混合装载限制
        MtContainerType type =
                        mtContainerTypeRepository.containerTypePropertyGet(tenantId, mtContainer.getContainerTypeId());
        if (type == null) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
        }
        if (StringUtils.isEmpty(type.getMixedMaterialFlag()) && StringUtils.isEmpty(type.getMixedEoFlag())
                        && StringUtils.isEmpty(type.getMixedWoFlag()) && StringUtils.isEmpty(type.getMixedOwnerFlag())
                        || "Y".equals(type.getMixedMaterialFlag()) && "Y".equals(type.getMixedEoFlag())
                                        && "Y".equals(type.getMixedWoFlag()) && "Y".equals(type.getMixedOwnerFlag())) {
            return;
        }

        // Step4获取容器内所有已装载物料批列表
        List<String> loadMaterialLotId = new ArrayList<>();
        List<String> loadEoId = new ArrayList<>();
        MtContLoadDtlVO10 detailVO2 = new MtContLoadDtlVO10();
        detailVO2.setContainerId(dto.getContainerId());
        detailVO2.setAllLevelFlag("Y");
        List<MtContLoadDtlVO4> detailVO4List =
                        mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, detailVO2);
        loadMaterialLotId.addAll(detailVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId).distinct()
                        .collect(Collectors.toList()));

        // Step5获取容器内所有已装载的执行作业列表
        List<MtContLoadDtlVO1> detailVO1List =
                        mtContainerLoadDetailRepository.containerLimitEoQuery(tenantId, detailVO2);
        loadEoId.addAll(detailVO1List.stream().filter(t -> t.getEoId() != null).map(MtContLoadDtlVO1::getEoId)
                        .distinct().collect(Collectors.toList()));

        // Step6loadObjectType有输入，获取当前装载对象对应的物料和数量
        List<MtContLoadDtlVO4> detailVO4List2;
        List<MtContLoadDtlVO1> detailVO1List2;
        if (StringUtils.isNotEmpty(dto.getLoadObjectType())) {
            if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
                loadMaterialLotId.add(dto.getLoadObjectId());
            } else if ("EO".equals(dto.getLoadObjectType())) {
                loadEoId.add(dto.getLoadObjectId());
            } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
                detailVO2.setContainerId(dto.getLoadObjectId());
                detailVO4List2 = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, detailVO2);
                loadMaterialLotId.addAll(detailVO4List2.stream().map(MtContLoadDtlVO4::getMaterialLotId).distinct()
                                .collect(Collectors.toList()));
                detailVO1List2 = mtContainerLoadDetailRepository.containerLimitEoQuery(tenantId, detailVO2);
                loadEoId.addAll(detailVO1List2.stream().filter(t -> t.getEoId() != null).map(MtContLoadDtlVO1::getEoId)
                                .distinct().collect(Collectors.toList()));

            }
        }

        // 需要对eoId,materialLotId去重
        loadMaterialLotId = loadMaterialLotId.stream().distinct().collect(Collectors.toList());
        loadEoId = loadEoId.stream().distinct().collect(Collectors.toList());

        // Step7判断装载是否超出容器混合装载限制
        List<MtMaterialLot> lots = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(loadMaterialLotId)) {
            lots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, loadMaterialLotId);
        }

        // 7-b
        List<String> temp = lots.stream().filter(t -> t.getEoId() != null).map(MtMaterialLot::getEoId).distinct()
                        .collect(Collectors.toList());

        List<String> materialIds = lots.stream().filter(t -> t.getMaterialId() != null)
                        .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());

        if (StringUtils.isNotEmpty(type.getMixedEoFlag()) && "N".equals(type.getMixedEoFlag())) {
            List<String> adjustList = new ArrayList<>();
            // 取并集
            adjustList.addAll(loadEoId);
            adjustList.addAll(temp);

            // 去重
            adjustList = adjustList.stream().distinct().collect(Collectors.toList());

            // 结果大于1则报错，说明存在多个eo
            if (adjustList.size() > 1) {
                throw new MtException("MT_MATERIAL_LOT_0044", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0044", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
            }
        }

        // 7-c
        List<String> loadEoId2 = new ArrayList<>(loadEoId);
        loadEoId.addAll(temp);

        // 拼接所有eoId的EO信息（比较woId时用）
        List<MtEo> nonQtyEos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(loadEoId)) {
            nonQtyEos = mtEoRepository.eoPropertyBatchGet(tenantId, loadEoId);
        }

        // 只有loadEoId的EO信息（比较materialId时用）
        List<MtEo> loadEos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(loadEoId2)) {
            loadEos = mtEoRepository.eoPropertyBatchGet(tenantId, loadEoId2);
        }

        // 7-d
        List<String> materialIdTemp = loadEos.stream().filter(t -> t.getMaterialId() != null).map(MtEo::getMaterialId)
                        .distinct().collect(Collectors.toList());

        if (StringUtils.isNotEmpty(type.getMixedMaterialFlag()) && "N".equals(type.getMixedMaterialFlag())) {
            List<String> adjustList = new ArrayList<>();
            // 取并集
            adjustList.addAll(materialIds);
            adjustList.addAll(materialIdTemp);

            // 去重
            adjustList = adjustList.stream().distinct().collect(Collectors.toList());

            // 结果大于1则报错，说明存在多个material
            if (adjustList.size() > 1) {
                throw new MtException("MT_MATERIAL_LOT_0045", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0045", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
            }
        }

        // 7-d-ii
        if (StringUtils.isNotEmpty(type.getMixedOwnerFlag()) && "N".equals(type.getMixedOwnerFlag())) {
            if (CollectionUtils.isNotEmpty(loadEoId2) && CollectionUtils.isNotEmpty(lots) && lots.stream().anyMatch(
                            t -> StringUtils.isNotEmpty(t.getOwnerType()) || StringUtils.isNotEmpty(t.getOwnerId()))) {
                throw new MtException("MT_MATERIAL_LOT_0046", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0046", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
            }
            if (CollectionUtils.isEmpty(loadEoId2) && CollectionUtils.isNotEmpty(lots)) {
                // 判断7-a所有结果中ownerType、ownerId全部一致
                List<String> temp1 =
                                lots.stream().map(MtMaterialLot::getOwnerType).distinct().collect(Collectors.toList());
                List<String> temp2 =
                                lots.stream().map(MtMaterialLot::getOwnerId).distinct().collect(Collectors.toList());
                if (temp1.size() > 1 || temp2.size() > 1) {
                    throw new MtException("MT_MATERIAL_LOT_0046",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0046",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
                }
            }
        }

        // 7-d-iii判断容器是否允许混合装载不同生产指令的实物
        if (StringUtils.isNotEmpty(type.getMixedWoFlag()) && "N".equals(type.getMixedWoFlag())) {
            List<String> temp3 = lots.stream().map(MtMaterialLot::getEoId).distinct().collect(Collectors.toList());

            if (temp3.size() > 1 && temp3.stream().anyMatch(t -> StringUtils.isEmpty(t))) {
                throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
            }
            if (CollectionUtils.isNotEmpty(temp3) && temp3.stream().allMatch(t -> StringUtils.isEmpty(t))
                            && CollectionUtils.isNotEmpty(loadEoId2)) {
                throw new MtException("MT_MATERIAL_LOT_0047", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0047", "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
            }
            if (CollectionUtils.isNotEmpty(temp3) && temp3.stream().allMatch(t -> StringUtils.isNotEmpty(t))) {
                List<String> temp4 =
                                nonQtyEos.stream().map(MtEo::getWorkOrderId).distinct().collect(Collectors.toList());
                if (temp4.size() > 1) {
                    throw new MtException("MT_MATERIAL_LOT_0047",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0047",
                                                    "MATERIAL_LOT", "【API：containerMixedAllowVerify】"));
                }
            }
        }
    }

    @Override
    public void containerCapacityExcessVerify(Long tenantId, MtContainerVO2 dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerCapacityExcessVerify】"));
        }
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        // 1-b
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerCapacityExcessVerify】"));
        }
        // 1-c
        if (StringUtils.isNotEmpty(dto.getLoadObjectType())) {
            if (StringUtils.isEmpty(dto.getLoadObjectId())) {
                throw new MtException("MT_MATERIAL_LOT_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                                "【API:containerCapacityExcessVerify】"));
            }

            if ("EO".equals(dto.getLoadObjectType())) {
                if (dto.getLoadQty() == null || BigDecimal.valueOf(dto.getLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MATERIAL_LOT_0014",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0066",
                                                    "MATERIAL_LOT", "【API:containerCapacityExcessVerify】"));
                }
            }
        }

        // Step2获取容器对应的容器类型
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());

        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerCapacityExcessVerify】"));
        }
        // Step3获取容器对应容器类型的容量限制
        MtContainerType type =
                        mtContainerTypeRepository.containerTypePropertyGet(tenantId, mtContainer.getContainerTypeId());
        if (type == null) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API：containerCapacityExcessVerify】"));
        }
        if (type.getCapacityQty() == null) {
            return;
        }

        // Step4获取容器内所有已装载物料及物料数量
        MtContLoadDtlVO2 detailVO2 = new MtContLoadDtlVO2();
        detailVO2.setContainerId(dto.getContainerId());
        detailVO2.setAllLevelFlag("Y");
        List<MtContLoadDtlVO7> detailVO7List = new ArrayList<>();
        detailVO7List.addAll(mtContainerLoadDetailRepository.containerLimitMaterialQtyQuery(tenantId, detailVO2));

        // Step5获取容器内所有已装载的执行作业及执行作业物料和数
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(detailVO2.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag(detailVO2.getAllLevelFlag());
        List<MtContLoadDtlVO1> detailVO1List =
                        mtContainerLoadDetailRepository.containerLimitEoQuery(tenantId, mtContLoadDtlVO10);
        List<String> eoIds;
        if (CollectionUtils.isNotEmpty(detailVO1List)) {
            eoIds = detailVO1List.stream().map(MtContLoadDtlVO1::getEoId).distinct().collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);

            Map<String, MtEo> eoMap = mtEos.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

            detailVO1List.stream().forEach(t -> {
                MtEo mtEo = eoMap.get(t.getEoId());
                if (mtEo != null) {
                    MtContLoadDtlVO7 detailVO7 = new MtContLoadDtlVO7();
                    detailVO7.setMaterialId(mtEo.getMaterialId());
                    detailVO7.setPrimaryUomId(mtEo.getUomId());
                    detailVO7.setPrimaryUomQty(t.getLoadQty());
                    detailVO7List.add(detailVO7);
                }
            });
        }

        // Step6若loadObjectType有输入，获取当前装载对象对应的物料和数量
        if (StringUtils.isNotEmpty(dto.getLoadObjectType())) {
            if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
                MtMaterialLotVO11 mtMaterialLotVO11 = new MtMaterialLotVO11();
                mtMaterialLotVO11.setMaterialLotId(dto.getLoadObjectId());
                MtMaterialLotVO4 mtMaterialLotVO4 =
                                mtMaterialLotRepository.materialLotLimitMaterialQtyGet(tenantId, mtMaterialLotVO11);
                if (mtMaterialLotVO4 != null) {
                    MtContLoadDtlVO7 detailVO7 = new MtContLoadDtlVO7();
                    detailVO7.setMaterialId(mtMaterialLotVO4.getMaterialId());
                    detailVO7.setPrimaryUomId(mtMaterialLotVO4.getPrimaryUomId());
                    detailVO7.setPrimaryUomQty(mtMaterialLotVO4.getPrimaryUomQty());
                    detailVO7List.add(detailVO7);
                }
            } else if ("EO".equals(dto.getLoadObjectType())) {
                MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getLoadObjectId());
                if (mtEo != null) {
                    MtContLoadDtlVO7 detailVO7 = new MtContLoadDtlVO7();
                    detailVO7.setMaterialId(mtEo.getMaterialId());
                    detailVO7.setPrimaryUomId(mtEo.getUomId());
                    detailVO7.setPrimaryUomQty(dto.getLoadQty());
                    detailVO7List.add(detailVO7);
                }
            } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
                detailVO2.setContainerId(dto.getLoadObjectId());
                detailVO7List.addAll(
                                mtContainerLoadDetailRepository.containerLimitMaterialQtyQuery(tenantId, detailVO2));

                mtContLoadDtlVO10 = new MtContLoadDtlVO10();
                mtContLoadDtlVO10.setContainerId(detailVO2.getContainerId());
                mtContLoadDtlVO10.setAllLevelFlag(detailVO2.getAllLevelFlag());
                List<MtContLoadDtlVO1> detailVO1List1 =
                                mtContainerLoadDetailRepository.containerLimitEoQuery(tenantId, mtContLoadDtlVO10);

                List<String> loadEoIds = new ArrayList<>();
                loadEoIds.addAll(detailVO1List1.stream().map(MtContLoadDtlVO1::getEoId).distinct()
                                .collect(Collectors.toList()));

                if (CollectionUtils.isNotEmpty(loadEoIds)) {
                    List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, loadEoIds);

                    if (CollectionUtils.isNotEmpty(mtEoList)) {
                        Map<String, MtEo> eoMap = mtEoList.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

                        detailVO1List1.stream().forEach(t -> {
                            MtEo mtEo = eoMap.get(t.getEoId());
                            if (mtEo != null) {
                                MtContLoadDtlVO7 detailVO7 = new MtContLoadDtlVO7();
                                detailVO7.setMaterialId(mtEo.getMaterialId());
                                detailVO7.setPrimaryUomId(mtEo.getUomId());
                                detailVO7.setPrimaryUomQty(t.getLoadQty());
                                detailVO7List.add(detailVO7);
                            }
                        });
                    }
                }
            }
        }

        // Step7判断装载是否超出容器容量限制
        Map<String, Map<String, BigDecimal>> resultMap = detailVO7List.stream().collect(Collectors.groupingBy(
                        MtContLoadDtlVO7::getMaterialId,
                        Collectors.groupingBy(MtContLoadDtlVO7::getPrimaryUomId,
                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                        c.getPrimaryUomQty() == null ? 0.0D : c.getPrimaryUomQty())))));
        List<MtContLoadDtlVO7> list = new ArrayList<>();
        resultMap.entrySet().forEach(t -> {
            t.getValue().entrySet().forEach(tt -> {
                MtContLoadDtlVO7 detailVO7 = new MtContLoadDtlVO7();
                detailVO7.setMaterialId(t.getKey());
                detailVO7.setPrimaryUomId(tt.getKey());
                detailVO7.setPrimaryUomQty(tt.getValue().doubleValue());
                list.add(detailVO7);
            });
        });

        // 7-b
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        // 7-c
        Double sLoadQty = 0.0D;
        for (MtContLoadDtlVO7 t : list) {
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, t.getMaterialId());
            if (mtMaterialVO1 != null && StringUtils.isNotEmpty(mtMaterialVO1.getPrimaryUomId())
                            && mtMaterialVO1.getPrimaryUomId().equals(t.getPrimaryUomId())) {
                sLoadQty = new BigDecimal(sLoadQty.toString()).add(new BigDecimal(t.getPrimaryUomQty().toString()))
                                .doubleValue();
            } else if (mtMaterialVO1 != null && StringUtils.isNotEmpty(mtMaterialVO1.getPrimaryUomId())
                            && !mtMaterialVO1.getPrimaryUomId().equals(t.getPrimaryUomId())) {

                MtUomVO1 mtUomVO1 = new MtUomVO1();
                mtUomVO1.setSourceUomId(t.getPrimaryUomId());
                mtUomVO1.setSourceValue(t.getPrimaryUomQty());
                mtUomVO1.setTargetUomId(mtMaterialVO1.getPrimaryUomId());
                mtUomVO1 = mtUomRepository.uomConversion(tenantId, mtUomVO1);
                if (mtUomVO1 != null && mtUomVO1.getTargetValue() != null) {
                    sLoadQty = new BigDecimal(sLoadQty.toString())
                                    .add(new BigDecimal(mtUomVO1.getTargetValue().toString())).doubleValue();

                }
            }
        }

        if (new BigDecimal(sLoadQty.toString()).compareTo(new BigDecimal(type.getCapacityQty().toString())) == 1) {
            throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0043", "MATERIAL_LOT", "【API：containerCapacityExcessVerify】"));
        }
    }

    @Override
    public void containerLocationIsEmptyValidate(Long tenantId, MtContainerVO6 dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerLocationIsEmptyValidate】"));
        }

        if (dto.getLocationColumn() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "locationColumn",
                                            "【API:containerLocationIsEmptyValidate】"));
        }

        if (dto.getLocationRow() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "locationRow", "【API:containerLocationIsEmptyValidate】"));
        }

        // Step2根据输入参数containerId调用API{ containerPropertyGet }获取容器数据
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerLocationIsEmptyValidate】"));

        }

        // Step3获取容器下指定位置所有已装载的对象
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        BeanUtils.copyProperties(dto, mtConLoadDetailVO);
        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        if (CollectionUtils.isNotEmpty(loadDetails)) {
            throw new MtException("MT_MATERIAL_LOT_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0042", "MATERIAL_LOT", "【API:containerLocationIsEmptyValidate】"));
        }
    }

    @Override
    public void containerLocationIsEmptyBatchValidate(Long tenantId, List<MtContainerVO6> dto) {

        // 第一步，判断输入参数是否合规
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "inputList",
                                            "【API:containerLocationIsEmptyBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId",
                                            "【API:containerLocationIsEmptyBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> t.getLocationRow() == null)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "locationRow",
                                            "【API:containerLocationIsEmptyBatchValidate】"));
        }

        if (dto.stream().anyMatch(t -> t.getLocationColumn() == null)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "locationColumn",
                                            "【API:containerLocationIsEmptyBatchValidate】"));
        }

        // 第二步，根据输入参数containerId列表调用API{ containerPropertyBatchGet }获取容器数据
        List<String> idList = dto.stream().map(MtContainerVO6::getContainerId).collect(Collectors.toList());
        List<MtContainer> mtContainers = containerPropertyBatchGet(tenantId, idList);
        if (CollectionUtils.isEmpty(mtContainers)) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerLocationIsEmptyBatchValidate】"));
        }

        // 第三步，根据输入参数containerId、locationRow、locationColumn从表MT_CONTAINER_LOAD_DETAIL中获取数据
        for (MtContainerVO6 mtContainer : dto) {
            MtContainerLoadDetail loadDetail = new MtContainerLoadDetail();
            loadDetail.setTenantId(tenantId);
            loadDetail.setContainerId(mtContainer.getContainerId());
            loadDetail.setLocationColumn(mtContainer.getLocationColumn());
            loadDetail.setLocationRow(mtContainer.getLocationRow());
            List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper.select(loadDetail);
            if (CollectionUtils.isNotEmpty(loadDetails)) {
                throw new MtException("MT_MATERIAL_LOT_0042",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0042",
                                                "MATERIAL_LOT", "【API:containerLocationIsEmptyBatchValidate】"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerTransfer(Long tenantId, MtContainerVO7 dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerTransfer】"));
        }
        if (StringUtils.isEmpty(dto.getTargetLocatorId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "targetLocatorId", "【API:containerTransfer】"));
        }

        // Step2获取需转移容器的属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null || !"CANRELEASE".equals(mtContainer.getStatus())) {
            throw new MtException("MT_MATERIAL_LOT_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0033", "MATERIAL_LOT", "【API:containerTransfer】"));
        }

        // Step3获取事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        BeanUtils.copyProperties(dto, eventCreateVO);
        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !dto.getTargetSiteId().equals(mtContainer.getSiteId())) {
            eventCreateVO.setEventTypeCode("CONTAINER_SITE_TRANSFER_OUT");
        } else {
            eventCreateVO.setEventTypeCode("CONTAINER_LOCATOR_TRANSFER_OUT");
        }
        eventCreateVO.setLocatorId(mtContainer.getLocatorId());
        // 3-a
        String eventIdOut = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3-b
        if (StringUtils.isNotEmpty(dto.getTargetSiteId()) && !dto.getTargetSiteId().equals(mtContainer.getSiteId())) {
            eventCreateVO.setEventTypeCode("CONTAINER_SITE_TRANSFER_IN");
        } else {
            eventCreateVO.setEventTypeCode("CONTAINER_LOCATOR_TRANSFER_IN");
        }
        eventCreateVO.setParentEventId(eventIdOut);
        eventCreateVO.setLocatorId(dto.getTargetLocatorId());
        String eventIdIn = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 4获取当前容器下装载的所有容器
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag("Y");
        List<MtContLoadDtlVO3> mtContLoadDtlVO3s =
                        mtContainerLoadDetailRepository.containerLimitContainerQuery(tenantId, mtContLoadDtlVO10);

        // Step5根据第四步获取结果和输入参数，依次记录容器转出事件
        List<String> containerIds = mtContLoadDtlVO3s.stream().map(MtContLoadDtlVO3::getLoadContainerId)
                        .collect(Collectors.toList());
        containerIds.add(dto.getContainerId());

        for (String containerId : containerIds) {
            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setEventId(eventIdOut);
            mtContainerVO12.setContainerId(containerId);
            containerUpdate(tenantId, mtContainerVO12, "N");
        }

        // Step6获取容器装载的所有物料批和容器

        List<MtContLoadDtlVO4> detailVO4s =
                        mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);

        // Step7针对第六步获取结果中所有的数据，依次进行处理
        if (CollectionUtils.isNotEmpty(detailVO4s)) {
            // update 2019.10.10 使用批量的形式
            List<String> materialLotIds =
                            detailVO4s.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
            MtMaterialLotVO14 mtMaterialLotVO14 = new MtMaterialLotVO14();
            mtMaterialLotVO14.setMaterialLotIds(materialLotIds);
            mtMaterialLotVO14.setTargetSiteId(dto.getTargetSiteId());
            mtMaterialLotVO14.setTargetLocatorId(dto.getTargetLocatorId());
            mtMaterialLotVO14.setWorkcellId(dto.getWorkcellId());
            mtMaterialLotVO14.setEventRequestId(dto.getEventRequestId());
            mtMaterialLotVO14.setParentEventId(dto.getParentEventId());
            mtMaterialLotVO14.setShiftCode(dto.getShiftCode());
            mtMaterialLotVO14.setShiftDate(dto.getShiftDate());
            mtMaterialLotVO14.setInstructionDocId(dto.getInstructionDocId());
            mtMaterialLotRepository.materialLotBatchTransfer(tenantId, mtMaterialLotVO14);
        }

        // Step8
        for (String containerId : containerIds) {
            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setEventId(eventIdIn);
            mtContainerVO12.setContainerId(containerId);
            mtContainerVO12.setSiteId(dto.getTargetSiteId());
            mtContainerVO12.setLocatorId(dto.getTargetLocatorId());
            containerUpdate(tenantId, mtContainerVO12, "N");
        }
    }

    /**
     * containerIdentificationUpdate-更新容器标识
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerIdentificationUpdate(Long tenantId, MtContainerVO14 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerIdentificationUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getIdentification())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "identification", "【API:containerIdentificationUpdate】"));
        }

        // 2. 判断标识是否已被用于其他容器
        MtContainerVO13 limitVO = new MtContainerVO13();
        limitVO.setIdentification(dto.getIdentification());
        List<String> containerIds = propertyLimitContainerQuery(tenantId, limitVO);
        if (CollectionUtils.isNotEmpty(containerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0030", "MATERIAL_LOT", "【API:containerIdentificationUpdate】"));
        }

        // 3. 生成容器预留事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_IDENTIFICATION_CHANGE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 对容器进行预留
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setIdentification(dto.getIdentification());
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    /**
     * propertyLimitContainerQuery-根据属性限制获取容器
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Override
    public List<String> propertyLimitContainerQuery(Long tenantId, MtContainerVO13 dto) {
        return mtContainerMapper.propertyLimitQuery(tenantId, dto);
    }

    /**
     * containerRelease-容器下达
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerRelease(Long tenantId, MtContainerVO14 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerRelease】"));
        }

        // 2. 生成容器下达事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_RELEASE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 对容器进行预留
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setStatus("CANRELEASE");
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    /**
     * containerHold-容器保留
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerHold(Long tenantId, MtContainerVO15 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerHold】"));
        }

        // 2. 获取容器属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null || StringUtils.isEmpty(mtContainer.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerHold】"));
        }

        // 3. 生成容器保留事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_HOLD");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 对容器进行预留
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setStatus("HOLD");
        containerUpdate(tenantId, mtContainerVO12, "N");

        // 5. 创建保留实绩
        // 头数据
        MtHoldActual mtHoldActual = new MtHoldActual();
        mtHoldActual.setSiteId(mtContainer.getSiteId());
        mtHoldActual.setHoldReasonCode(dto.getHoldReasonCode());
        mtHoldActual.setComments(dto.getComments());
        mtHoldActual.setExpiredReleaseTime(dto.getExpiredReleaseTime());
        mtHoldActual.setHoldType("IMMEDIATE");

        // 行数据
        MtHoldActualDetail mtHoldActualDetail = new MtHoldActualDetail();
        mtHoldActualDetail.setObjectId(dto.getContainerId());
        mtHoldActualDetail.setObjectType("CONTAINER");
        mtHoldActualDetail.setOriginalStatus(mtContainer.getStatus());
        mtHoldActualDetail.setHoldEventId(eventId);

        List<MtHoldActualDetail> mtHoldActualDetails = new ArrayList<>();
        mtHoldActualDetails.add(mtHoldActualDetail);

        MtHoldActualVO mtHoldActualVo = new MtHoldActualVO();
        mtHoldActualVo.setMtHoldActual(mtHoldActual);
        mtHoldActualVo.setMtHoldActualDetails(mtHoldActualDetails);
        mtHoldActualRepository.holdCreate(tenantId, mtHoldActualVo);
    }

    /**
     * containerHoldCancel-容器保留取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerHoldCancel(Long tenantId, MtContainerVO16 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerHoldCancel】"));
        }

        // 2. 获取容器属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getContainerId());
        if (mtContainer == null || StringUtils.isEmpty(mtContainer.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerHoldCancel】"));
        }

        if (!"HOLD".equals(mtContainer.getStatus())) {
            throw new MtException("MT_MATERIAL_LOT_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0032", "MATERIAL_LOT", "【API:containerHoldCancel】"));
        }

        // 3. 生成容器取消事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_HOLD_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取并释放容器保留实绩
        MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();
        mtHoldActualDetailVo2.setObjectId(dto.getContainerId());
        mtHoldActualDetailVo2.setObjectType("CONTAINER");
        String holdDetailId = mtHoldActualDetailRepository.objectLimitHoldingDetailGet(tenantId, mtHoldActualDetailVo2);

        if (StringUtils.isNotEmpty(holdDetailId)) {
            MtHoldActualDetailVO4 mtHoldActualDetailVo4 = new MtHoldActualDetailVO4();
            mtHoldActualDetailVo4.setHoldDetailId(holdDetailId);
            mtHoldActualDetailVo4.setReleaseComment(dto.getReleaseComment());
            mtHoldActualDetailVo4.setReleaseReasonCode(dto.getReleaseReasonCode());
            mtHoldActualDetailVo4.setReleaseEventId(eventId);
            mtHoldActualDetailRepository.holdRelease(tenantId, mtHoldActualDetailVo4);
        }

        // 5. 对容器进行预留取消
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setStatus(StringUtils.isEmpty(dto.getTargetStatus()) ? "CANRELEASE" : dto.getTargetStatus());
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    /**
     * containerAbandon-容器废弃
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerAbandon(Long tenantId, MtContainerVO14 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerAbandon】"));
        }

        // 2. 生成容器取消事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_ABANDON");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 对容器进行预留
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setContainerId(dto.getContainerId());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setStatus("ABANDON");
        containerUpdate(tenantId, mtContainerVO12, "N");
    }

    /**
     * containerCopy-复制容器
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/8
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String containerCopy(Long tenantId, MtContainerVO17 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSourceContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "sourceContainerId", "【API:containerCopy】"));
        }

        // 新容器的编码
        String containerCode = "";

        if (StringUtils.isNotEmpty(dto.getTargetContainerCode())) {
            MtContainerVO13 mtContainerVO13 = new MtContainerVO13();
            mtContainerVO13.setContainerCode(dto.getTargetContainerCode());
            List<String> containerIds = propertyLimitContainerQuery(tenantId, mtContainerVO13);
            if (CollectionUtils.isNotEmpty(containerIds)) {
                throw new MtException("MT_MATERIAL_LOT_0031",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0031",
                                                "MATERIAL_LOT", dto.getTargetContainerCode(), "【API:containerCopy】"));
            }

            containerCode = dto.getTargetContainerCode();
        }

        // 2. 获取复制来源容器的属性
        MtContainer mtContainer = containerPropertyGet(tenantId, dto.getSourceContainerId());
        if (mtContainer == null || StringUtils.isEmpty(mtContainer.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerCopy】"));
        }

        // 3. 生成容器复制事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_COPY_CREATE");
        eventCreateVO.setLocatorId(mtContainer.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);


        // 4. 根据第2步和第3步获取到的结果新增目标容器
        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setStatus("ABANDON");
        mtContainerVO12.setContainerCode(containerCode);
        mtContainerVO12.setContainerTypeId(mtContainer.getContainerTypeId());
        mtContainerVO12.setContainerName(StringUtils.isNotEmpty(dto.getContainerName()) ? dto.getContainerName()
                        : mtContainer.getContainerName());
        mtContainerVO12.setDescription(StringUtils.isNotEmpty(dto.getDescription()) ? dto.getDescription()
                        : mtContainer.getDescription());
        mtContainerVO12.setSiteId(mtContainer.getSiteId());
        mtContainerVO12.setLocatorId(mtContainer.getLocatorId());
        mtContainerVO12.setStatus("NEW");
        mtContainerVO12.setOwnerType(mtContainer.getOwnerType());
        mtContainerVO12.setOwnerId(mtContainer.getOwnerId());
        mtContainerVO12.setReservedFlag(mtContainer.getReservedFlag());
        mtContainerVO12.setReservedObjectType(mtContainer.getReservedObjectType());
        mtContainerVO12.setReservedObjectId(mtContainer.getReservedObjectId());
        mtContainerVO12.setIdentification(dto.getIdentification());
        mtContainerVO12.setEventId(eventId);
        mtContainerVO12.setOutsideNum(dto.getOutsideNum());
        mtContainerVO12.setIncomingValueList(dto.getIncomingValueList());
        return containerUpdate(tenantId, mtContainerVO12, "N").getContainerId();
    }

    /**
     * containerNextCodeGet -获取下一个容器编码
     *
     * @Author lxs
     * @Date 2019/4/8
     * @Params [tenantId, siteId]
     * @Return java.lang.String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtNumrangeVO5 containerNextCodeGet(Long tenantId, MtContainerVO33 dto) {
        // 参数校验
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (mtModSite == null) {
                throw new MtException("MT_MATERIAL_LOT_0091", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0091", "GENERAL", "siteId", "【API:containerNextCodeGet】"));
            }
        }

        // 仅获取一条数据
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("CONTAINER_CODE");
        List<String> objectIdList =
                        mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIdList)) {
            throw new MtException("MT_MATERIAL_LOT_0093", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0093", "MATERIAL_LOT", "【API:materialLotNextCodeGet】"));
        }


        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectIdList.get(0));

        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);

        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)
                        && MapUtils.isNotEmpty(dto.getContainerPropertyList())) {
            List<String> objectColumnCode = mtNumrangeObjectColumns.stream().map(t -> t.getObjectColumnCode())
                            .collect(Collectors.toList());
            Map<String, String> tempMap = dto.getContainerPropertyList().entrySet().stream()
                            .filter(t -> objectColumnCode.contains(t.getKey()))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            Optional<MtNumrangeObjectColumn> first = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .findFirst();
            if (first.isPresent()) {
                createNum.setObjectTypeCode(first.get().getObjectColumnCode());
            }
            createNum.setCallObjectCodeList(tempMap);
        }
        createNum.setObjectCode("CONTAINER_CODE");
        createNum.setSiteId(dto.getSiteId());
        createNum.setOutsideNum(dto.getOutsideNum());
        createNum.setIncomingValueList(dto.getIncomingValueList());
        return mtNumrangeRepository.numrangeGenerate(tenantId, createNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerMaterialConsume(Long tenantId, MtContainerVO11 dto) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerMaterialConsume】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "materialId", "【API:containerMaterialConsume】"));
        }
        if (dto.getTrxPrimaryUomQty() == null) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "trxPrimaryUomQty", "【API:containerMaterialConsume】"));
        }
        if (new BigDecimal(dto.getTrxPrimaryUomQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_MATERIAL_LOT_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                            "MATERIAL_LOT", "trxPrimaryUomQty", "【API:containerMaterialConsume】"));
        }
        if (dto.getTrxSecondaryUomQty() != null
                        && new BigDecimal(dto.getTrxSecondaryUomQty().toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_MATERIAL_LOT_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                            "MATERIAL_LOT", "trxSecondaryUomQty ", "【API:containerMaterialConsume】"));
        }
        if (StringUtils.isNotEmpty(dto.getReservedObjectType()) && StringUtils.isEmpty(dto.getReservedObjectId())
                        || StringUtils.isEmpty(dto.getReservedObjectType())
                                        && StringUtils.isNotEmpty(dto.getReservedObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0010",
                                            "MATERIAL_LOT", "reservedObjectType 、reservedObjectId ",
                                            "【API:containerMaterialConsume】"));
        }
        if ("N".equals(dto.getAllConsume()) && null == dto.getTrxPrimaryUomQty()) {
            throw new MtException("MT_MATERIAL_LOT_0081", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0081", "MATERIAL_LOT", "【API:containerMaterialConsume】"));
        }

        // Step2获取消耗单位
        // 2-a获取消耗主计量数量单位c_primaryUomId和辅助计量数量单位c_secondaryUomId
        String cPrimaryUomId = null;
        String cSecondaryUomId = null;
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
        if (StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
            cPrimaryUomId = dto.getPrimaryUomId();
        } else if (StringUtils.isEmpty(dto.getPrimaryUomId()) && mtMaterialVO1 != null) {
            cPrimaryUomId = mtMaterialVO1.getPrimaryUomId();
        }

        if (StringUtils.isEmpty(dto.getSecondaryUomId()) && mtMaterialVO1 != null
                        && StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
            cSecondaryUomId = mtMaterialVO1.getSecondaryUomId();
        }

        if (StringUtils.isNotEmpty(dto.getSecondaryUomId()) && mtMaterialVO1 != null
                        && StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
            cSecondaryUomId = dto.getSecondaryUomId();
        }

        // step3获取容器内所有的物料批
        // update by xie.yiyang 2019/11/05
        // 调用API{ containerLimitMaterialLotLoadSequenceQuery }获取容器内装载的物料批以及装载顺序
        MtMaterialLotVO17 lotVO17 = new MtMaterialLotVO17();
        lotVO17.setContainerId(dto.getContainerId());
        lotVO17.setAllLevelFlag("Y");
        List<MtMaterialLotVO18> loadDetails =
                        mtMaterialLotRepository.containerLimitMaterialLotLoadSequenceQuery(tenantId, lotVO17);
        if (CollectionUtils.isEmpty(loadDetails)) {
            throw new MtException("MT_MATERIAL_LOT_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0062", "MATERIAL_LOT", "【API:containerMaterialConsume】"));
        }

        // Step4调用API{materialLotPropertyBatchGet}获取物料批信息
        // update by xie.yiyang 2019/11/05
        Map<String, Long> map = loadDetails.stream().collect(
                        Collectors.toMap(MtMaterialLotVO18::getMaterialLotId, MtMaterialLotVO18::getLoadSequence));
        List<String> materialLotIds = new ArrayList<>(map.keySet());
        List<MtMaterialLot> mtMaterialLots =
                        mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);

        // 获取输入参数中materialId、reservedObjectType 、reservedObjectId与获取结果中物料批物料、物料批预留信息一致的物料批ID
        List<MtMaterialLotVO16> vo16s = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtMaterialLots)) {
            vo16s = mtMaterialLots.stream()
                            .filter(t -> t.getMaterialId().equals(
                                            StringUtils.isNotEmpty(dto.getMaterialId()) ? dto.getMaterialId() : "")
                                            && t.getReservedObjectType()
                                                            .equals(StringUtils.isNotEmpty(dto.getReservedObjectType())
                                                                            ? dto.getReservedObjectType()
                                                                            : "")
                                            && t.getReservedObjectId()
                                                            .equals(StringUtils.isNotEmpty(dto.getReservedObjectId())
                                                                            ? dto.getReservedObjectId()
                                                                            : ""))
                            .map(t -> {
                                MtMaterialLotVO16 vo16 = new MtMaterialLotVO16();
                                vo16.setMaterialLotId(t.getMaterialLotId());
                                if ("N".equals(dto.getAllConsume())) {
                                    vo16.setSequence(map.get(t.getMaterialLotId()));
                                }
                                return vo16;
                            }).collect(Collectors.toList());
        }

        // 第五步，传入第四步获取的筛选后的物料批ID进行消耗
        // 调用{ sequenceLimitMaterialLotBatchConsume }进行消耗
        MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
        mtMaterialLotVO15.setMtMaterialLotSequenceList(vo16s);
        mtMaterialLotVO15.setPrimaryUomId(cPrimaryUomId);
        mtMaterialLotVO15.setTrxPrimaryUomQty(dto.getTrxPrimaryUomQty());
        mtMaterialLotVO15.setSecondaryUomId(cSecondaryUomId);
        mtMaterialLotVO15.setTrxSecondaryUomQty(dto.getTrxSecondaryUomQty());
        mtMaterialLotVO15.setAllConsume(dto.getAllConsume());
        mtMaterialLotVO15.setWorkcellId(dto.getWorkcellId());
        mtMaterialLotVO15.setParentEventId(dto.getParentEventId());
        mtMaterialLotVO15.setEventRequestId(dto.getEventRequestId());
        mtMaterialLotVO15.setShiftDate(dto.getShiftDate());
        mtMaterialLotVO15.setShiftCode(dto.getShiftCode());
        mtMaterialLotVO15.setInstructionDocId(dto.getInstructionDocId());
        mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);
    }

    /***
     * 递归调用容器的物料批进行消耗
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerEoMaterialConsume(List<MtContLoadDtlVO6> list, MtContainerVO23 dto, Long tenantId) {
        // 参数预处理reservedObjectId, reservedObjectType 未输入清空默认为空字符串
        dto.setReservedObjectId(dto.getReservedObjectId() == null ? "" : dto.getReservedObjectId());
        dto.setReservedObjectType(dto.getReservedObjectType() == null ? "" : dto.getReservedObjectType());

        // 筛选当前容器下的物料
        List<MtContLoadDtlVO6> contLoadDtlVO6s = list.stream()
                        .filter(t -> dto.getNeedConsumeContainerId().equals(t.getContainerId())
                                        && t.getLoadSequence().compareTo(dto.getCurrentSequence()) == 1)
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(contLoadDtlVO6s)) {

            contLoadDtlVO6s =
                            contLoadDtlVO6s.stream().sorted(Comparator.comparingLong(MtContLoadDtlVO6::getLoadSequence))
                                            .collect(Collectors.toList());
            // 最后一条数据
            MtContLoadDtlVO6 finalLoadDetailV = contLoadDtlVO6s.get(contLoadDtlVO6s.size() - 1);

            for (MtContLoadDtlVO6 t : contLoadDtlVO6s) {
                // 4-b
                Double avaPrimaryQty = 0.0D;
                Double avaSecondaryQty = 0.0D;
                // 4-c
                if ("EO".equals(t.getLoadObjectType())) {
                    dto.setCurrentSequence(t.getLoadSequence());
                    if (!t.equals(finalLoadDetailV)) {
                        continue;
                    }

                    if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                                    && (new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                                    .compareTo(BigDecimal.ZERO) == 1)
                                    || new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                                    .compareTo(BigDecimal.ZERO) == 1) {
                        throw new MtException("MT_MATERIAL_LOT_0062",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0062", "MATERIAL_LOT",
                                                        "【API:containerEoMaterialConsume】"));
                    }

                    if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                                    && new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                                    .compareTo(BigDecimal.ZERO) <= 0
                                    && new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                                    .compareTo(BigDecimal.ZERO) <= 0) {
                        return;
                    }
                    if (!dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())) {
                        // 获取父容器的containerId 若取到仅会取到一条数据
                        List<MtContLoadDtlVO6> temp = list.stream()
                                        .filter(tt -> "CONTAINER".equals(tt.getLoadObjectType())
                                                        && dto.getParentContainerId().equals(tt.getLoadObjectId()))
                                        .collect(Collectors.toList());

                        dto.setCurrentSequence(dto.getParentSequence());
                        dto.setNeedConsumeContainerId(dto.getParentContainerId());

                        if (CollectionUtils.isNotEmpty(temp)) {
                            dto.setParentContainerId(temp.get(0).getContainerId());
                            dto.setParentSequence(temp.get(0).getLoadSequence());
                        } else {
                            dto.setParentContainerId(null);
                            dto.setParentSequence(0L);
                        }
                        containerEoMaterialConsume(list, dto, tenantId);
                    }
                }
                if ("MATERIAL_LOT".equals(t.getLoadObjectType())) {
                    dto.setCurrentSequence(t.getLoadSequence());
                    MtMaterialLot lot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, t.getLoadObjectId());
                    if (lot != null && dto.getMaterialId().equals(lot.getMaterialId())
                                    && StringHelper.isSame(dto.getReservedObjectId(), lot.getReservedObjectId())
                                    && StringHelper.isSame(dto.getReservedObjectType(), lot.getReservedObjectType())) {
                        if (dto.getConsumePrimaryUomId().equals(lot.getPrimaryUomId())) {
                            avaPrimaryQty = lot.getPrimaryUomQty();
                        } else {
                            MtUomVO1 transferUomVO = new MtUomVO1();
                            transferUomVO.setSourceUomId(lot.getPrimaryUomId());
                            transferUomVO.setSourceValue(lot.getPrimaryUomQty());
                            transferUomVO.setTargetUomId(dto.getConsumePrimaryUomId());
                            transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                            if (transferUomVO != null && transferUomVO.getTargetValue() != null) {
                                avaPrimaryQty = transferUomVO.getTargetValue();
                            }
                        }

                        if (StringUtils.isNotEmpty(dto.getConsumeSecondaryUomId())
                                        && StringUtils.isNotEmpty(lot.getSecondaryUomId())) {
                            if (dto.getConsumeSecondaryUomId().equals(lot.getSecondaryUomId())) {
                                avaSecondaryQty = lot.getSecondaryUomQty();
                            } else {
                                MtUomVO1 transferUomVO = new MtUomVO1();
                                transferUomVO.setSourceUomId(lot.getSecondaryUomId());
                                transferUomVO.setSourceValue(lot.getSecondaryUomQty());
                                transferUomVO.setTargetUomId(dto.getConsumeSecondaryUomId());
                                transferUomVO = mtUomRepository.uomConversion(tenantId, transferUomVO);
                                if (transferUomVO != null && transferUomVO.getTargetValue() != null) {
                                    avaSecondaryQty = transferUomVO.getTargetValue();
                                }
                            }
                        }
                        // 判断是否满足消耗
                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        if (new BigDecimal(avaPrimaryQty.toString())
                                        .compareTo(new BigDecimal(dto.getNeedConsumePrimaryQty().toString())) >= 0
                                        && new BigDecimal(avaSecondaryQty.toString()).compareTo(new BigDecimal(
                                                        dto.getNeedConsumeSecondaryQty().toString())) >= 0) {

                            BeanUtils.copyProperties(dto, mtMaterialLotVO1);
                            mtMaterialLotVO1.setMaterialLotId(t.getLoadObjectId());
                            mtMaterialLotVO1.setPrimaryUomId(dto.getConsumePrimaryUomId());
                            mtMaterialLotVO1.setTrxPrimaryUomQty(dto.getNeedConsumePrimaryQty());
                            if (StringUtils.isNotEmpty(dto.getConsumeSecondaryUomId())) {
                                mtMaterialLotVO1.setSecondaryUomId(dto.getConsumeSecondaryUomId());
                                mtMaterialLotVO1.setTrxSecondaryUomQty(dto.getNeedConsumeSecondaryQty());
                            }
                        } else {
                            BeanUtils.copyProperties(dto, mtMaterialLotVO1);
                            mtMaterialLotVO1.setMaterialLotId(t.getLoadObjectId());
                            mtMaterialLotVO1.setPrimaryUomId(dto.getConsumePrimaryUomId());
                            mtMaterialLotVO1.setTrxPrimaryUomQty(
                                            Math.min(dto.getNeedConsumePrimaryQty(), avaPrimaryQty));
                            if (StringUtils.isNotEmpty(dto.getConsumeSecondaryUomId())) {
                                mtMaterialLotVO1.setSecondaryUomId(dto.getConsumeSecondaryUomId());
                                mtMaterialLotVO1.setTrxSecondaryUomQty(
                                                Math.min(dto.getNeedConsumeSecondaryQty(), avaSecondaryQty));
                            }
                        }
                        // 更新
                        mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                        Double primaryQty = Math.min(dto.getNeedConsumePrimaryQty(), avaPrimaryQty);
                        Double secondaryQty = Math.min(dto.getNeedConsumeSecondaryQty(), avaSecondaryQty);
                        dto.setNeedConsumePrimaryQty(new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                        .subtract(new BigDecimal(primaryQty.toString())).doubleValue());
                        dto.setNeedConsumeSecondaryQty(new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                        .subtract(new BigDecimal(secondaryQty.toString())).doubleValue());
                        if (t.equals(finalLoadDetailV)) {
                            if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                                            && (new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                                            .compareTo(BigDecimal.ZERO) == 1)
                                            || new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                                            .compareTo(BigDecimal.ZERO) == 1) {
                                throw new MtException("MT_MATERIAL_LOT_0062",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MATERIAL_LOT_0062", "MATERIAL_LOT",
                                                                "【API:containerEoMaterialConsume】"));

                            }
                            if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                                            && new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                                            .compareTo(BigDecimal.ZERO) <= 0
                                            && new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                                            .compareTo(BigDecimal.ZERO) <= 0) {
                                return;
                            }
                            if (!dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())) {
                                // 获取父容器的containerId 若取到仅会取到一条数据
                                List<MtContLoadDtlVO6> temp = list.stream()
                                                .filter(tt -> "CONTAINER".equals(tt.getLoadObjectType()) && dto
                                                                .getParentContainerId().equals(tt.getLoadObjectId()))
                                                .collect(Collectors.toList());
                                dto.setCurrentSequence(dto.getParentSequence());
                                dto.setNeedConsumeContainerId(dto.getParentContainerId());
                                if (CollectionUtils.isNotEmpty(temp)) {
                                    dto.setParentContainerId(temp.get(0).getContainerId());
                                    dto.setParentSequence(temp.get(0).getLoadSequence());
                                } else {
                                    dto.setParentContainerId(null);
                                    dto.setParentSequence(0L);
                                }
                                containerEoMaterialConsume(list, dto, tenantId);
                            }
                        }
                    }
                }
                if ("CONTAINER".equals(t.getLoadObjectType())) {

                    dto.setParentContainerId(dto.getNeedConsumeContainerId());
                    dto.setNeedConsumeContainerId(t.getLoadObjectId());
                    dto.setParentSequence(t.getLoadSequence());
                    dto.setCurrentSequence(0L);
                    containerEoMaterialConsume(list, dto, tenantId);
                }
            }
        } else {
            // 如果当前为空容器
            if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                            && (new BigDecimal(dto.getNeedConsumePrimaryQty().toString())
                                            .compareTo(BigDecimal.ZERO) == 1)
                            || new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                            .compareTo(BigDecimal.ZERO) == 1) {
                throw new MtException("MT_MATERIAL_LOT_0062", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0062", "MATERIAL_LOT", "【API:containerEoMaterialConsume】"));
            }
            if (dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())
                            && new BigDecimal(dto.getNeedConsumePrimaryQty().toString()).compareTo(BigDecimal.ZERO) <= 0
                            && new BigDecimal(dto.getNeedConsumeSecondaryQty().toString())
                                            .compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }
            if (!dto.getInputContainerId().equals(dto.getNeedConsumeContainerId())) {
                // 获取父容器的containerId 若取到仅会取到一条数据
                List<MtContLoadDtlVO6> temp = list.stream()
                                .filter(tt -> "CONTAINER".equals(tt.getLoadObjectType())
                                                && dto.getParentContainerId().equals(tt.getLoadObjectId()))
                                .collect(Collectors.toList());
                dto.setCurrentSequence(dto.getParentSequence());
                dto.setNeedConsumeContainerId(dto.getParentContainerId());
                if (CollectionUtils.isNotEmpty(temp)) {
                    dto.setParentContainerId(temp.get(0).getContainerId());
                    dto.setParentSequence(temp.get(0).getLoadSequence());
                } else {
                    dto.setParentContainerId(null);
                    dto.setParentSequence(0L);
                }
                containerEoMaterialConsume(list, dto, tenantId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void objectContainerTransfer(Long tenantId, MtContainerVO18 dto) {
        // Step1判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getTargetContainerId())) {

            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "targetContainerId", "【API:objectContainerTransfer】"));
        }
        if (StringUtils.isEmpty(dto.getSourceContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "sourceContainerId", "【API:objectContainerTransfer】"));
        }
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:objectContainerTransfer】"));
        }
        if (StringUtils.isNotEmpty(dto.getLoadObjectId()) && StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "loadObjectId", "loadObjectType",
                                            "【API:objectContainerTransfer】"));
        }
        // Step2判断目标容器是否启用位置管理
        String containerTypeId = containerLimitContainerTypeGet(tenantId, dto.getTargetContainerId());
        if (StringUtils.isEmpty(containerTypeId)) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:objectContainerTransfer】"));
        }
        MtContainerType type = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId);

        // Step3获取需转移的对象数据
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        BeanUtils.copyProperties(dto, mtConLoadDetailVO);
        mtConLoadDetailVO.setContainerId(dto.getSourceContainerId());
        mtConLoadDetailVO.setAllLevelFlag("N");
        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        if (CollectionUtils.isEmpty(loadDetails)) {
            throw new MtException("MT_MATERIAL_LOT_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0061", "MATERIAL_LOT", "【API:objectContainerTransfer】"));
        }

        if (StringUtils.isNotEmpty(dto.getLoadObjectId())) {
            loadDetails = loadDetails.stream().filter(t -> dto.getLoadObjectId().equals(t.getLoadObjectId()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(loadDetails)) {
                throw new MtException("MT_MATERIAL_LOT_0061", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0061", "MATERIAL_LOT", "【API:objectContainerTransfer】"));
            }

            if (dto.getLocationRow() != null && dto.getLocationColumn() != null) {
                loadDetails = loadDetails.stream().filter(t -> dto.getLocationRow().equals(t.getLocationRow()))
                                .collect(Collectors.toList());
            } else if (dto.getLocationColumn() != null) {
                loadDetails = loadDetails.stream().filter(t -> dto.getLocationColumn().equals(t.getLocationColumn()))
                                .collect(Collectors.toList());
            } else if (dto.getLocationRow() != null) {
                loadDetails = loadDetails.stream().filter(t -> dto.getLocationRow().equals(t.getLocationRow()))
                                .collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(loadDetails)) {
                throw new MtException("MT_MATERIAL_LOT_0061", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0061", "MATERIAL_LOT", "【API:objectContainerTransfer】"));
            }

            // 多条数据转移的时候不允许输入数量，必须全部转移
            if (loadDetails.size() > 1 && dto.getTransferQty() != null) {
                throw new MtException("MT_MATERIAL_LOT_0069",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0069 ",
                                                "MATERIAL_LOT", " locationRow、locationColumn",
                                                "【API:objectContainerTransfer】"));
            }

            // loadObjectType=EO时，需要判断每个结果中的数量都必须大于输入transferQty
            if ("EO".equals(dto.getLoadObjectType()) && dto.getTransferQty() != null) {
                for (MtContLoadDtlVO6 mtContLoadDtlVO6 : loadDetails) {
                    if (BigDecimal.valueOf(dto.getTransferQty())
                                    .compareTo(BigDecimal.valueOf(mtContLoadDtlVO6.getLoadQty())) > 0) {
                        throw new MtException("MT_MATERIAL_LOT_0070",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0070", "MATERIAL_LOT",
                                                        "【API:objectContainerTransfer】"));
                    }
                }
            }
        }

        // Step4将需转移的数据从来源容器中进行卸载并装载到目标容器中
        for (MtContLoadDtlVO6 t : loadDetails) {
            MtContainerVO25 mtContainerVO22 = new MtContainerVO25();
            BeanUtils.copyProperties(dto, mtContainerVO22);
            mtContainerVO22.setContainerId(t.getContainerId());
            mtContainerVO22.setLoadObjectId(t.getLoadObjectId());
            mtContainerVO22.setLoadObjectType(t.getLoadObjectType());
            mtContainerVO22.setLocationRow(t.getLocationRow());
            mtContainerVO22.setLocationColumn(t.getLocationColumn());
            mtContainerVO22.setTrxUnloadQty(dto.getTransferQty());
            containerUnload(tenantId, mtContainerVO22);

            // 验证当前判断对象是否可装在到目标容器：
            MtContainerVO9 mtContainerVO9 = new MtContainerVO9();
            mtContainerVO9.setContainerId(dto.getTargetContainerId());
            mtContainerVO9.setLoadObjectId(t.getLoadObjectId());
            mtContainerVO9.setLoadObjectType(t.getLoadObjectType());

            if (type != null && "Y".equals(type.getLocationEnabledFlag())) {
                MtContainerVO4 mtcontainerVO4 = new MtContainerVO4();
                mtcontainerVO4.setContainerId(dto.getTargetContainerId());
                MtContainerVO5 mtcontainerVO5 = containerFirstAvailableLocationGet(tenantId, mtcontainerVO4);
                if (mtcontainerVO5 != null) {
                    mtContainerVO9.setLocationRow(mtcontainerVO5.getNextLoactionRow());
                    mtContainerVO9.setLocationColumn(mtcontainerVO5.getNextLocationColumn());
                }
            }

            if ("EO".equals(t.getLoadObjectType()) && dto.getTransferQty() == null) {
                mtContainerVO9.setTrxLoadQty(t.getLoadQty());
            } else if ("EO".equals(t.getLoadObjectType()) && dto.getTransferQty() != null
                            && StringUtils.isNotEmpty(dto.getLoadObjectId())) {
                mtContainerVO9.setTrxLoadQty(dto.getTransferQty());
            }
            containerLoadVerify(tenantId, mtContainerVO9);

            // 验证通过
            MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
            BeanUtils.copyProperties(dto, mtContainerVO24);
            mtContainerVO24.setContainerId(dto.getTargetContainerId());
            mtContainerVO24.setLoadObjectId(mtContainerVO9.getLoadObjectId());
            mtContainerVO24.setLoadObjectType(mtContainerVO9.getLoadObjectType());
            if (type != null && "Y".equals(type.getLocationEnabledFlag())) {
                mtContainerVO24.setLocationRow(mtContainerVO9.getLocationRow());
                mtContainerVO24.setLocationColumn(mtContainerVO9.getLocationColumn());
            }

            mtContainerVO24.setTrxLoadQty(mtContainerVO9.getTrxLoadQty());
            containerLoad(tenantId, mtContainerVO24);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerAllObjectUnload(Long tenantId, MtContainerVO20 dto) {
        // Step1判断输入参数是否合规
        List<String> loadObjectTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API:containerAllObjectUnload】"));
        }
        if (StringUtils.isNotEmpty(dto.getLoadObjectType()) && !loadObjectTypes.contains(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                            "MATERIAL_LOT", "loadObjectType", "", "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]",
                                            "【API:containerAllObjectUnload】"));
        }

        // Step2获取容器内装载明细
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        BeanUtils.copyProperties(dto, mtConLoadDetailVO);

        List<MtContLoadDtlVO6> loadDetails =
                        mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);

        // Step3获取容器装载事
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        BeanUtils.copyProperties(dto, eventCreateVO);
        eventCreateVO.setEventTypeCode("CONTAINER_ALL_UNLOAD");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step4依次根据第二步获取结果删除容器装载实绩
        for (MtContLoadDtlVO6 t : loadDetails) {
            MtContLoadDtlVO9 mtContLoadDtlVO9 = new MtContLoadDtlVO9();
            mtContLoadDtlVO9.setContainerId(t.getContainerId());
            mtContLoadDtlVO9.setLoadObjectType(t.getLoadObjectType());
            mtContLoadDtlVO9.setLoadObjectId(t.getLoadObjectId());
            mtContLoadDtlVO9.setEventId(eventId);
            mtContLoadDtlVO9.setLocationColumn(t.getLocationColumn());
            mtContLoadDtlVO9.setLocationRow(t.getLocationRow());
            this.mtContainerLoadDetailRepository.containerLoadDetailDelete(tenantId, mtContLoadDtlVO9);
        }

        // Step5根据第二步获取结果中的containerId，去重后依次更新容器数据
        List<String> containerIds = loadDetails.stream().map(MtContLoadDtlVO6::getContainerId).distinct()
                        .collect(Collectors.toList());
        for (String t : containerIds) {
            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setContainerId(t);
            mtContainerVO12.setLastUnloadTime(new Date(System.currentTimeMillis()));
            mtContainerVO12.setEventId(eventId);
            containerUpdate(tenantId, mtContainerVO12, "N");
        }
    }

    @Override
    public List<String> containerIdentify(Long tenantId, String identification) {
        if (StringUtils.isEmpty(identification)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "identification", "【API:containerIdentify】"));
        }

        MtContainer mtContainer = new MtContainer();
        mtContainer.setIdentification(identification);
        mtContainer.setTenantId(tenantId);
        List<MtContainer> list = this.mtContainerMapper.select(mtContainer);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtContainer::getContainerId).collect(Collectors.toList());
    }

    @Override
    public List<MtExtendAttrVO> containerLimitAttrQuery(Long tenantId, MtContainerAttrVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLimitAttrQuery】"));
        }

        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_container_attr");
        extendVO.setKeyId(dto.getContainerId());
        extendVO.setAttrName(dto.getAttrName());
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerLimitAttrUpdate(Long tenantId, MtContainerAttrVO3 dto) {
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_container_attr", dto.getContainerId(),
                        dto.getEventId(), dto.getAttr());
    }

    @Override
    public List<String> attrLimitContainerQuery(Long tenantId, MtContainerAttrVO2 dto) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getAttr())
                        || dto.getAttr().stream().anyMatch(t -> StringUtils.isEmpty(t.getAttrName()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "attrName ", "【API:attrLimitContainerQuery】"));
        }

        Map<String, String> property = dto.getAttr().stream().collect(Collectors.toMap(t -> t.getAttrName(),
                        t -> t.getAttrValue() == null ? "" : t.getAttrValue(), (k1, k2) -> k1));
        List<String> ids = mtExtendSettingsRepository.attrBatchPropertyLimitKidQuery(tenantId, "mt_container_attr",
                        property);
        // 根据主键进行筛选
        if (StringUtils.isNotEmpty(dto.getContainerId())) {
            ids = ids.stream().filter(t -> dto.getContainerId().equals(t)).collect(Collectors.toList());
        }

        return ids;
    }

    @Override
    public List<MtContainerAttrHisVO1> containerAttrHisQuery(Long tenantId, MtContainerAttrHisVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventId()) && StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                            "MATERIAL_LOT", "containerId、eventId", "【API：containerAttrHisQuery】"));
        }

        MtExtendAttrHisVO2 query = new MtExtendAttrHisVO2();
        BeanUtils.copyProperties(dto, query);
        query.setKid(dto.getContainerId());
        List<MtContainerAttrHisVO1> result = new ArrayList<>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisQuery(tenantId, query, "mt_container_attr");
        attrHisList.stream().forEach(t -> {
            MtContainerAttrHisVO1 mtEoAttrHisVO1 = new MtContainerAttrHisVO1();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setContainerId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });
        return result;
    }

    @Override
    public List<MtContainerAttrHisVO1> eventLimitContainerAttrHisBatchQuery(Long tenantId, List<String> eventIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "eventId", "【API：eventLimitContainerAttrHisBatchQuery】"));
        }
        List<MtContainerAttrHisVO1> result = new ArrayList<MtContainerAttrHisVO1>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisBatchQuery(tenantId, eventIds, "mt_container_attr");
        attrHisList.stream().forEach(t -> {
            MtContainerAttrHisVO1 mtEoAttrHisVO1 = new MtContainerAttrHisVO1();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setContainerId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });

        return result;
    }

    @Override
    public List<MtContainerVO28> propertyLimitContainerPropertyQuery(Long tenantId, MtContainerVO27 dto) {
        List<MtContainerVO28> voList = mtContainerMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 获取到的站点 siteId 列表
        List<String> siteIdList = voList.stream().map(MtContainerVO28::getSiteId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(Collectors.toList());
        List<String> locatorIdList = voList.stream().map(MtContainerVO28::getLocatorId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(Collectors.toList());
        List<String> containerTypeIdList = voList.stream().map(MtContainerVO28::getContainerTypeId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, MtModSite> mtModSiteMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(siteIdList)) {
            List<MtModSite> mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
            if (CollectionUtils.isNotEmpty(mtModSites)) {
                mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }
        }

        Map<String, MtModLocator> mtModLocatorMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(locatorIdList)) {
            List<MtModLocator> mtModLocators =
                            mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
            if (CollectionUtils.isNotEmpty(mtModLocators)) {
                mtModLocatorMap = mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
            }
        }

        Map<String, MtContainerType> mtContainerTypeMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(containerTypeIdList)) {
            List<MtContainerType> mtContainerTypes =
                            mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIdList);
            if (CollectionUtils.isNotEmpty(mtContainerTypes)) {
                mtContainerTypeMap = mtContainerTypes.stream()
                                .collect(Collectors.toMap(MtContainerType::getContainerTypeId, t -> t));
            }
        }


        for (MtContainerVO28 vo28 : voList) {
            MtModSite site = mtModSiteMap.get(vo28.getSiteId());
            if (null != site) {
                vo28.setSiteCode(site.getSiteCode());
                vo28.setSiteName(site.getSiteName());
            }

            MtModLocator locator = mtModLocatorMap.get(vo28.getLocatorId());
            if (null != locator) {
                vo28.setLocatorCode(locator.getLocatorCode());
                vo28.setLocatorName(locator.getLocatorName());
            }


            MtContainerType mtContainerType = mtContainerTypeMap.get(vo28.getContainerTypeId());
            if (null != mtContainerType) {
                vo28.setContainerTypeCode(mtContainerType.getContainerTypeCode());
                vo28.setContainerTypeDescription(mtContainerType.getContainerTypeDescription());
            }
        }
        // mappper的sql里面已排序
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtContainerVO26> containerBatchUpdate(Long tenantId, MtContainerVO29 dto, String fullUpdate) {
        // 第一步：判断输入参数是否合规
        if (dto == null || StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：containerBatchUpdate】"));
        }
        if (CollectionUtils.isEmpty(dto.getContainerList())) {
            return Collections.emptyList();
        }

        List<String> containerIds =
                        dto.getContainerList().stream().filter(t -> StringUtils.isNotEmpty(t.getContainerId()))
                                        .map(MtContainerVO32::getContainerId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(containerIds)) {
            List<String> disContainerIds = containerIds.stream().distinct().collect(Collectors.toList());
            if (containerIds.size() != disContainerIds.size()) {
                throw new MtException("MT_MATERIAL_LOT_0086 MT_MATERIAL_LOT_0086 ",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0086 ",
                                                "MATERIAL_LOT", "【API：containerBatchUpdate】"));
            }
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());

        List<String> sqlList = new ArrayList<>();
        List<MtContainerVO26> result = new ArrayList<>();

        // 获取id
        List<String> newIds = this.customDbRepository.getNextKeys("mt_container_s", dto.getContainerList().size());

        // 获取cid
        List<String> newCids = this.customDbRepository.getNextKeys("mt_container_cid_s", dto.getContainerList().size());

        // 获取id
        List<String> newHisIds =
                        this.customDbRepository.getNextKeys("mt_container_his_s", dto.getContainerList().size());
        // 获取cid
        List<String> newHIsCids =
                        this.customDbRepository.getNextKeys("mt_container_his_cid_s", dto.getContainerList().size());
        dto.getContainerList().stream().forEach(k -> {
            if (StringUtils.isNotEmpty(k.getOwnerType()) && StringUtils.isEmpty(k.getOwnerId())) {
                throw new MtException("MT_MATERIAL_LOT_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                "MATERIAL_LOT", "ownerType", "ownerId", "【API：containerBatchUpdate】"));

            }
            if (StringUtils.isNotEmpty(k.getReservedObjectType()) && StringUtils.isEmpty(k.getReservedObjectId())) {
                throw new MtException("MT_MATERIAL_LOT_0014",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                "MATERIAL_LOT", "reservedObjectType", "reservedObjectId",
                                                "【API：containerBatchUpdate】"));

            }
            if (StringUtils.isEmpty(k.getContainerId()) && StringUtils.isEmpty(k.getContainerCode())) {
                throw new MtException("MT_MATERIAL_LOT_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                "MATERIAL_LOT", "containerId,containerCode",
                                                "【API：containerBatchUpdate】"));
            }

            MtContainerVO26 mtContainerVO26 = new MtContainerVO26();

            // 2.第二步，根据输入参数判断为需新增数据或是更新数据
            MtContainer mtContainer = new MtContainer();
            if (StringUtils.isNotEmpty(k.getContainerId())) {
                mtContainer.setTenantId(tenantId);
                mtContainer.setContainerId(k.getContainerId());
                mtContainer = mtContainerMapper.selectOne(mtContainer);
                if (mtContainer == null) {
                    throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerBatchUpdate】"));
                }
            } else {
                mtContainer.setContainerCode(k.getContainerCode());
                mtContainer.setTenantId(tenantId);
                // 可能为空null
                mtContainer = mtContainerMapper.selectOne(mtContainer);
            }

            if (mtContainer == null) {
                mtContainer = new MtContainer();
                // 插入
                if (StringUtils.isEmpty(k.getContainerTypeId())) {
                    throw new MtException("MT_MATERIAL_LOT_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                    "MATERIAL_LOT", "containerId、containerTypeId",
                                                    "【API：containerBatchUpdate】"));
                }
                if (StringUtils.isEmpty(k.getSiteId())) {
                    throw new MtException("MT_MATERIAL_LOT_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                    "MATERIAL_LOT", "containerId、siteId",
                                                    "【API：containerBatchUpdate】"));
                }
                if (StringUtils.isEmpty(k.getLocatorId())) {
                    throw new MtException("MT_MATERIAL_LOT_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                                    "MATERIAL_LOT", "containerId、locatorId",
                                                    "【API：containerBatchUpdate】"));
                }
                mtContainer.setContainerCode(k.getContainerCode());
                mtContainer.setContainerTypeId(k.getContainerTypeId());
                mtContainer.setContainerName(k.getContainerName());
                mtContainer.setDescription(k.getDescription());
                mtContainer.setSiteId(k.getSiteId());
                mtContainer.setLocatorId(k.getLocatorId());
                mtContainer.setOwnerType(k.getOwnerType());
                mtContainer.setOwnerId(k.getOwnerId());
                mtContainer.setReservedFlag(k.getReservedFlag());
                mtContainer.setReservedObjectId(k.getReservedObjectId());
                mtContainer.setReservedObjectType(k.getReservedObjectType());
                mtContainer.setLastLoadTime(k.getLastLoadTime());
                mtContainer.setLastUnloadTime(k.getLastUnloadTime());
                if (StringUtils.isEmpty(k.getStatus())) {
                    mtContainer.setStatus("NEW");
                } else {
                    mtContainer.setStatus(k.getStatus());
                }
                if (StringUtils.isEmpty(k.getIdentification())) {
                    mtContainer.setIdentification(k.getContainerCode());
                } else {
                    mtContainer.setIdentification(k.getIdentification());
                }
                mtContainer.setCurrentContainerId(k.getCurrentContainerId());

                if (null != k.getTopContainerId()) {
                    mtContainer.setTopContainerId(k.getTopContainerId());
                } else {
                    if (null != k.getCurrentContainerId() && "".equals(k.getCurrentContainerId())) {
                        mtContainer.setTopContainerId("");
                    } else if (StringUtils.isNotEmpty(k.getCurrentContainerId())) {
                        MtContainer mtC = new MtContainer();
                        mtC.setTenantId(tenantId);
                        mtC.setContainerId(k.getCurrentContainerId());
                        mtC = mtContainerMapper.selectOne(mtC);
                        if (null != mtC && StringUtils.isNotEmpty(mtC.getTopContainerId())) {
                            mtContainer.setTopContainerId(mtC.getTopContainerId());
                        } else {
                            mtContainer.setTopContainerId(k.getCurrentContainerId());
                        }
                    }
                }

                mtContainer.setTenantId(tenantId);
                mtContainer.setContainerId(newIds.get(result.size()));
                mtContainer.setCid(Long.valueOf(newCids.get(result.size())));
                mtContainer.setCreatedBy(userId);
                mtContainer.setCreationDate(now);
                mtContainer.setLastUpdateDate(now);
                mtContainer.setLastUpdatedBy(userId);

                // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
                mtContainer.setLatestHisId(newHisIds.get(result.size()));
                sqlList.addAll(customDbRepository.getInsertSql(mtContainer));
            } else {
                if (StringUtils.isNotEmpty(k.getContainerCode())) {
                    mtContainer.setContainerCode(k.getContainerCode());
                }
                if (StringUtils.isNotEmpty(k.getContainerTypeId())) {
                    mtContainer.setContainerTypeId(k.getContainerTypeId());
                }
                if (StringUtils.isNotEmpty(k.getContainerName())) {
                    mtContainer.setContainerName(k.getContainerName());
                }
                if (StringUtils.isNotEmpty(k.getDescription())) {
                    mtContainer.setDescription(k.getDescription());
                }
                if (StringUtils.isNotEmpty(k.getSiteId())) {
                    mtContainer.setSiteId(k.getSiteId());
                }
                if (StringUtils.isNotEmpty(k.getLocatorId())) {
                    mtContainer.setLocatorId(k.getLocatorId());
                }
                if (StringUtils.isNotEmpty(k.getStatus())) {
                    mtContainer.setStatus(k.getStatus());
                }
                if (StringUtils.isNotEmpty(k.getOwnerType())) {
                    mtContainer.setOwnerType(k.getOwnerType());
                }
                if (StringUtils.isNotEmpty(k.getOwnerId())) {
                    mtContainer.setOwnerId(k.getOwnerId());
                }
                if (StringUtils.isNotEmpty(k.getReservedFlag())) {
                    mtContainer.setReservedFlag(k.getReservedFlag());
                }
                if (StringUtils.isNotEmpty(k.getReservedObjectId())) {
                    mtContainer.setReservedObjectId(k.getReservedObjectId());
                }
                if (StringUtils.isNotEmpty(k.getReservedObjectType())) {
                    mtContainer.setReservedObjectType(k.getReservedObjectType());
                }
                if (StringUtils.isNotEmpty(k.getIdentification())) {
                    mtContainer.setIdentification(k.getIdentification());
                }
                if (k.getLastLoadTime() != null) {
                    mtContainer.setLastLoadTime(k.getLastLoadTime());
                }
                if (k.getLastUnloadTime() != null) {
                    mtContainer.setLastUnloadTime(k.getLastUnloadTime());
                }
                if (StringUtils.isNotEmpty(k.getCurrentContainerId())) {
                    mtContainer.setCurrentContainerId(k.getCurrentContainerId());
                }
                // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
                mtContainer.setLatestHisId(newHisIds.get(result.size()));
                // 更新
                if (null != k.getTopContainerId()) {
                    mtContainer.setTopContainerId(k.getTopContainerId());
                } else {
                    if (null != k.getCurrentContainerId() && "".equals(k.getCurrentContainerId())) {
                        mtContainer.setTopContainerId("");
                    } else if (StringUtils.isNotEmpty(k.getCurrentContainerId())) {
                        MtContainer mtC = new MtContainer();
                        mtC.setTenantId(tenantId);
                        mtC.setContainerId(k.getCurrentContainerId());
                        mtC = mtContainerMapper.selectOne(mtC);
                        if (null != mtC && StringUtils.isNotEmpty(mtC.getTopContainerId())) {
                            mtContainer.setTopContainerId(mtC.getTopContainerId());
                        } else {
                            mtContainer.setTopContainerId(k.getCurrentContainerId());
                        }
                    }
                }
                mtContainer.setTenantId(tenantId);
                mtContainer.setLastUpdateDate(now);
                mtContainer.setLastUpdatedBy(userId);
                mtContainer.setCid(Long.valueOf(newCids.get(result.size())));
                if ("Y".equals(fullUpdate)) {
                    mtContainer = (MtContainer) ObjectFieldsHelper.setStringFieldsEmpty(mtContainer);
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtContainer));
                } else {
                    sqlList.addAll(customDbRepository.getUpdateSql(mtContainer));
                }
            }
            // 记录历史
            MtContainerHis mtContainerHis = new MtContainerHis();
            mtContainerHis.setTenantId(tenantId);
            mtContainerHis.setContainerId(mtContainer.getContainerId());
            mtContainerHis.setContainerCode(mtContainer.getContainerCode());
            mtContainerHis.setContainerTypeId(mtContainer.getContainerTypeId());
            mtContainerHis.setContainerName(mtContainer.getContainerName());
            mtContainerHis.setDescription(mtContainer.getDescription());
            mtContainerHis.setSiteId(mtContainer.getSiteId());
            mtContainerHis.setLocatorId(mtContainer.getLocatorId());
            mtContainerHis.setStatus(mtContainer.getStatus());
            mtContainerHis.setOwnerType(mtContainer.getOwnerType());
            mtContainerHis.setOwnerId(mtContainer.getOwnerId());
            mtContainerHis.setReservedFlag(mtContainer.getReservedFlag());
            mtContainerHis.setReservedObjectType(mtContainer.getReservedObjectType());
            mtContainerHis.setReservedObjectId(mtContainer.getReservedObjectId());
            mtContainerHis.setIdentification(mtContainer.getIdentification());
            mtContainerHis.setLastLoadTime(mtContainer.getLastLoadTime());
            mtContainerHis.setLastUnloadTime(mtContainer.getLastUnloadTime());
            mtContainerHis.setCurrentContainerId(mtContainer.getCurrentContainerId());
            mtContainerHis.setTopContainerId(mtContainer.getCurrentContainerId());
            mtContainerHis.setEventId(dto.getEventId());
            mtContainerHis.setCreatedBy(userId);
            mtContainerHis.setCreationDate(now);
            mtContainerHis.setLastUpdateDate(now);
            mtContainerHis.setLastUpdatedBy(userId);
            mtContainerHis.setContainerHisId(newHisIds.get(result.size()));
            mtContainerHis.setCid(Long.valueOf(newHIsCids.get(result.size())));
            sqlList.addAll(customDbRepository.getInsertSql(mtContainerHis));
            mtContainerVO26.setContainerId(mtContainerHis.getContainerId());
            mtContainerVO26.setContainerHisId(mtContainerHis.getContainerHisId());
            result.add(mtContainerVO26);
        });

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerBatchLoad(Long tenantId, MtContainerVO30 dto) {

        if (dto == null || CollectionUtils.isEmpty(dto.getContainerLoadList())) {
            return;
        }
        // 校验参数
        MtContLoadDtlVO12 mtContLoadDtlVO12 = new MtContLoadDtlVO12();
        List<String> types = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
        List<MtContLoadDtlVO16> mtContLoadDtlList = new ArrayList<>();
        List<MtContainerVO32> containerIds = new ArrayList<>();
        Set<String> containerIdSet = new HashSet<String>();
        MtContainerVO32 mtContainer;
        MtContLoadDtlVO16 mtContLoadDtlVO16;
        Date now = new Date();
        for (MtContainerVO31 tuple : dto.getContainerLoadList()) {
            if (StringUtils.isEmpty(tuple.getContainerId())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "containerId", "【API：containerBatchLoad】"));
            }
            if (StringUtils.isEmpty(tuple.getLoadObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "loadObjectType", "【API：containerBatchLoad】"));
            }
            if (StringUtils.isEmpty(tuple.getLoadObjectId())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "loadObjectId", "【API：containerBatchLoad】"));
            }
            if (!types.contains(tuple.getLoadObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                                "MATERIAL_LOT", "loadObjectType", types.toString(),
                                                "【API：containerBatchLoad】"));
            }
            if (tuple.getTrxLoadQty() != null) {
                if (BigDecimal.valueOf(tuple.getTrxLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MATERIAL_LOT_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                                    "MATERIAL_LOT", "trxLoadQty ", "【API：containerBatchLoad】"));
                }
            }
            if (tuple.getLoadQty() != null) {
                if (BigDecimal.valueOf(tuple.getLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MATERIAL_LOT_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0007",
                                                    "MATERIAL_LOT", "loadQty ", "【API：containerBatchLoad】"));
                }
            }
            mtContLoadDtlVO16 = new MtContLoadDtlVO16();

            mtContLoadDtlVO16.setContainerId(tuple.getContainerId());
            mtContLoadDtlVO16.setLocationRow(tuple.getLocationRow());
            mtContLoadDtlVO16.setLocationColumn(tuple.getLocationColumn());
            mtContLoadDtlVO16.setLoadObjectId(tuple.getLoadObjectId());
            mtContLoadDtlVO16.setLoadObjectType(tuple.getLoadObjectType());
            mtContLoadDtlVO16.setTrxLoadQty(tuple.getTrxLoadQty());
            mtContLoadDtlVO16.setLoadQty(tuple.getLoadQty());
            mtContLoadDtlVO16.setLoadEoStepActualId(tuple.getLoadEoStepActualId());
            mtContLoadDtlList.add(mtContLoadDtlVO16);

            if (containerIdSet.add(tuple.getContainerId())){
                mtContainer = new MtContainerVO32();
                mtContainer.setContainerId(tuple.getContainerId());
                mtContainer.setLastLoadTime(now);
                containerIds.add(mtContainer);
            }
        }

        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("CONTAINER_BATCH_LOAD");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        mtContLoadDtlVO12.setEventId(eventId);

        // 新增容器装载实绩
        mtContLoadDtlVO12.setMtContLoadDtlList(mtContLoadDtlList);

        mtContainerLoadDetailRepository.containerLoadDetailBatchUpdate(tenantId, mtContLoadDtlVO12, MtBaseConstants.NO);

        // 更新容器数据
        MtContainerVO29 mtContainerVO29 = new MtContainerVO29();
        mtContainerVO29.setEventId(eventId);
        mtContainerVO29.setContainerList(Lists.newArrayList(containerIds.iterator()));
        containerBatchUpdate(tenantId, mtContainerVO29, "N");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "keyId ", "【API：containerAttrPropertyUpdate】"));
        }
        MtContainer container = new MtContainer();
        container.setTenantId(tenantId);
        container.setContainerId(mtExtendVO10.getKeyId());
        MtContainer mtContainer = mtContainerMapper.selectOne(container);
        if (null == mtContainer) {
            throw new MtException("MT_MATERIAL_LOT_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0089",
                                            "MATERIAL_LOT", mtExtendVO10.getKeyId(), "mt_container",
                                            "【API:containerAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_container_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }

    @Override
    public MtNumrangeVO8 containerBatchCodeGet(Long tenantId, MtContainerVO34 dto) {

        // 1. 验证参数有效性
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (null == mtModSite) {
                throw new MtException("MT_MATERIAL_LOT_0091",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0091",
                                                "MATERIAL_LOT", "siteId:" + dto.getSiteId(),
                                                "【API:containerBatchCodeGet】"));
            }
        }

        // 构造callObjectCodeList接收对象
        List<MtNumrangeVO10> numrangeVO10List = new ArrayList<>();
        // 接收objectTypeCode的值，用来后面判断
        List<String> objectTypeCodes = new ArrayList<>();

        // 2. 获取objectId编码对象ID
        MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("CONTAINER_CODE");
        List<String> objectIds = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_MATERIAL_LOT_0093", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0093", "MATERIAL_LOT", "【API:containerBatchCodeGet】"));
        }

        // 获取objectId编码对象ID,只有一条数据
        String objectId = objectIds.get(0);

        // 3.a 获取该对象所有属性列objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);

        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
            String typeCode = null;
            String objectColumnCode = null;

            // 获取的多行中TYPE_GROUP类型组不为空的列参数名
            // 只有一条
            List<MtNumrangeObjectColumn> columnList = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(columnList)) {
                objectColumnCode = columnList.get(0).getObjectColumnCode();
                typeCode = columnList.get(0).getTypeGroup();
            }

            // 转为Map数据
            Map<String, MtNumrangeObjectColumn> mtNumrangeObjectColumnMap = mtNumrangeObjectColumns.stream()
                            .collect(Collectors.toMap(MtNumrangeObjectColumn::getObjectColumnCode, t -> t));

            // 循环输入参数
            if (CollectionUtils.isNotEmpty(dto.getMaterialLotPropertyList())) {
                for (MtNumrangeVO10 mtNumrangeVO10 : dto.getMaterialLotPropertyList()) {
                    // 每循环一次构造一个map对象，用来存储
                    Map<String, String> callObjectCodeMap = new HashMap<>(0);

                    for (Map.Entry<String, String> entry : mtNumrangeVO10.getCallObjectCode().entrySet()) {
                        // 匹配定义的对象列
                        MtNumrangeObjectColumn mtNumrangeObjectColumn = mtNumrangeObjectColumnMap.get(entry.getKey());
                        if (mtNumrangeObjectColumn != null) {
                            callObjectCodeMap.put(entry.getKey(), entry.getValue());
                        }
                    }

                    if (MapUtils.isNotEmpty(callObjectCodeMap)) {
                        // 设置数据
                        MtNumrangeVO10 calObjectNumrange = new MtNumrangeVO10();
                        calObjectNumrange.setCallObjectCode(callObjectCodeMap);
                        calObjectNumrange.setSequence(mtNumrangeVO10.getSequence());
                        numrangeVO10List.add(calObjectNumrange);

                        if (StringUtils.isNotEmpty(typeCode)) {
                            objectTypeCodes.add(callObjectCodeMap.get(objectColumnCode));
                        }
                    }
                }
            }
        }

        // 3.b 将获取的objectTypeCode进行去重，假如出现多行则报错
        String objectTypeCode = null;
        List<String> distinctObjectTypeCodes = objectTypeCodes.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(distinctObjectTypeCodes)) {
            if (distinctObjectTypeCodes.size() > 1) {
                throw new MtException("MT_MATERIAL_LOT_0092", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0092", "MATERIAL_LOT", "【API:containerBatchCodeGet】"));
            } else {
                objectTypeCode = distinctObjectTypeCodes.get(0);
            }
        }

        // 4. 批量生成号码段数据
        mtNumrangeVO9.setObjectCode("CONTAINER_CODE");
        mtNumrangeVO9.setObjectTypeCode(objectTypeCode);
        mtNumrangeVO9.setSiteId(dto.getSiteId());
        mtNumrangeVO9.setOutsideNumList(dto.getOutsideNumList());
        mtNumrangeVO9.setCallObjectCodeList(numrangeVO10List);
        mtNumrangeVO9.setIncomingValueList(dto.getIncomingValueList());
        mtNumrangeVO9.setObjectNumFlag(dto.getObjectNumFlag());
        mtNumrangeVO9.setNumQty(dto.getNumQty());
        return mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
    }

    @Override
    public List<MtContainerVO36> objectLimitLoadingContainerBatchQuery(Long tenantId, List<MtContainerVO35> vo35List) {
        // 1.判断输入参数是否合规
        for (MtContainerVO35 mtContainerVO35 : vo35List) {
            if (StringUtils.isEmpty(mtContainerVO35.getLoadObjectId())
                            && StringUtils.isEmpty(mtContainerVO35.getLoadObjectType())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "loadObjectType、loadObjectId",
                                                "【API:objectLimitLoadingContainerBatchQuery】"));
            }
        }

        List<MtContainerVO36> resultList = new ArrayList<>();
        // 2.判断输入参数loadObjectType是否为EO
        for (MtContainerVO35 mtContainerVO35 : vo35List) {
            // 3.若loadObjectType=EO，则执行第三步操作
            if ("EO".equals(mtContainerVO35.getLoadObjectType())) {
                MtContainerVO36 vo36 = new MtContainerVO36();
                // 根据输入参数从MT_CONTAINER_LOAD_DETAIL中获取数据
                MtContainerLoadDetail detail = new MtContainerLoadDetail();
                detail.setTenantId(tenantId);
                detail.setLoadObjectType("EO");
                detail.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                List<MtContainerLoadDetail> detailList = mtContainerLoadDetailMapper.select(detail);
                // 获取containerid列表
                List<String> containerIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(detailList)) {
                    containerIdList = detailList.stream().map(MtContainerLoadDetail::getContainerId).distinct()
                                    .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
                }

                // 第四步
                if (CollectionUtils.isNotEmpty(containerIdList)) {
                    if (!"Y".equals(mtContainerVO35.getTopLevelFlag())) {
                        vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                        vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                        vo36.setContainerId(containerIdList);
                    } else {
                        List<MtContainerLoadDetail> container = mtContainerLoadDetailRepository.selectByCondition(
                                        Condition.builder(MtContainerLoadDetail.class).andWhere(Sqls.custom()
                                                        .andEqualTo(MtContainerLoadDetail.FIELD_TENANT_ID, tenantId)
                                                        .andEqualTo(MtContainerLoadDetail.FIELD_LOAD_OBJECT_TYPE,
                                                                        "CONTAINER")
                                                        .andIn(MtContainerLoadDetail.FIELD_LOAD_OBJECT_ID,
                                                                        containerIdList))
                                                        .build());

                        List<String> conIds = container.stream().map(MtContainerLoadDetail::getContainerId)
                                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

                        if (CollectionUtils.isEmpty(conIds)) {
                            vo36.setContainerId(containerIdList);
                            vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                            vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                        } else {
                            // 递归查找
                            vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                            vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                            vo36.setContainerId(findTopContainerIdRecursion(tenantId, conIds));
                        }
                    }

                } else {
                    vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                    vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                }

                // 添加返回参数结果
                resultList.add(vo36);

            } else {

                MtContainerVO36 vo36 = new MtContainerVO36();
                // 否则执行第五步操作
                // a) 若输入参数loadObjectType= CONTAINER
                if ("CONTAINER".equals(mtContainerVO35.getLoadObjectType())
                                && "Y".equals(mtContainerVO35.getTopLevelFlag())) {
                    MtContainer container = new MtContainer();
                    container.setTenantId(tenantId);
                    container.setContainerId(mtContainerVO35.getLoadObjectId());
                    MtContainer mtContainer = mtContainerMapper.selectOne(container);

                    vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                    vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                    if (null != mtContainer) {
                        vo36.setContainerId(Arrays.asList(mtContainer.getTopContainerId()));
                    }

                } else if ("CONTAINER".equals(mtContainerVO35.getLoadObjectType())
                                && !"Y".equals(mtContainerVO35.getTopLevelFlag())) {
                    MtContainer container = new MtContainer();
                    container.setTenantId(tenantId);
                    container.setContainerId(mtContainerVO35.getLoadObjectId());
                    MtContainer mtContainer = mtContainerMapper.selectOne(container);
                    vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                    vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                    if (null != mtContainer) {
                        vo36.setContainerId(Arrays.asList(mtContainer.getCurrentContainerId()));
                    }
                }

                // 若输入参数loadObjectType= MATERIAL_LOT，判断输入参数topLevelFlag是否为Y
                if ("MATERIAL_LOT".equals(mtContainerVO35.getLoadObjectType())
                                && "Y".equals(mtContainerVO35.getTopLevelFlag())) {
                    MtMaterialLot materialLot = new MtMaterialLot();
                    materialLot.setTenantId(tenantId);
                    materialLot.setMaterialLotId(mtContainerVO35.getLoadObjectId());
                    MtMaterialLot mtMaterialLot = mtMaterialLotMapper.selectOne(materialLot);

                    vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                    vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                    if (null != mtMaterialLot) {
                        vo36.setContainerId(Arrays.asList(mtMaterialLot.getTopContainerId()));
                    }

                } else if ("MATERIAL_LOT".equals(mtContainerVO35.getLoadObjectType())
                                && !"Y".equals(mtContainerVO35.getTopLevelFlag())) {
                    MtMaterialLot materialLot = new MtMaterialLot();
                    materialLot.setTenantId(tenantId);
                    materialLot.setMaterialLotId(mtContainerVO35.getLoadObjectId());
                    MtMaterialLot mtMaterialLot = mtMaterialLotMapper.selectOne(materialLot);
                    vo36.setLoadObjectId(mtContainerVO35.getLoadObjectId());
                    vo36.setLoadObjectType(mtContainerVO35.getLoadObjectType());
                    if (null != mtMaterialLot) {
                        vo36.setContainerId(Arrays.asList(mtMaterialLot.getCurrentContainerId()));
                    }
                }

                resultList.add(vo36);
            }

        }

        return resultList;
    }

    @Override
    public void containerAvailableBatchValidate(Long tenantId, List<String> containerIds) {
        // 1.验证参数
        if (CollectionUtils.isEmpty(containerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerIds", "【API:containerAvailableBatchValidate】"));
        }

        // 2.验证状态
        containerIds = containerIds.stream().distinct().collect(Collectors.toList());
        List<MtContainer> containerList = containerPropertyBatchGet(tenantId, containerIds);
        if (CollectionUtils.isEmpty(containerList) || containerList.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0083",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0083",
                                            "MATERIAL_LOT", "containerIds", "【API:containerAvailableBatchValidate】"));
        }

        for (MtContainer container : containerList) {
            if (!"CANRELEASE".equals(container.getStatus())) {
                throw new MtException("MT_MATERIAL_LOT_0099",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0099",
                                                "MATERIAL_LOT", container.getContainerId(),
                                                "【API:containerAvailableBatchValidate】"));
            }
        }

    }

    @Override
    public void containerPackingLevelBatchVerify(Long tenantId, List<MtContLoadDtlVO18> voList) {
        // 1.验证输入参数
        List<String> hasContainerIds = voList.stream().map(MtContLoadDtlVO18::getContainerId)
                        .filter(t -> StringUtils.isEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hasContainerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API:containerPackingLevelBatchVerify】"));
        }

        List<String> typeList = voList.stream().map(MtContLoadDtlVO18::getLoadObjectType)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(typeList)) {
            // 类型判断
            for (String type : typeList) {
                if (!LOAD_OBJECT_TYPE.contains(type)) {
                    throw new MtException("MT_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0003", "MATERIAL_LOT", "loadObjectType",
                                    "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]", "【API:containerPackingLevelBatchVerify】"));
                }
            }
        }

        // 2.
        List<String> containerIds = voList.stream().map(MtContLoadDtlVO18::getContainerId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        List<MtContainer> containerList = containerPropertyBatchGet(tenantId, containerIds);

        if (CollectionUtils.isEmpty(containerList) || containerList.size() != containerIds.size()) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API:containerPackingLevelBatchVerify】"));
        }

        List<String> containerTypeIds = containerList.stream().map(MtContainer::getContainerTypeId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        Map<String, MtContainer> containerIdAndTypeIdMap =
                        containerList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

        // 3.获取容器对应容器类型的包装等级要求
        List<MtContainerType> mtContainerTypes =
                        mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIds);
        if (CollectionUtils.isEmpty(mtContainerTypes)) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerPackingLevelBatchVerify】"));
        }

        // 3-ii-获取包装等级
        Map<String, MtContainerType> packingLevelMap =
                        mtContainerTypes.stream().collect(Collectors.toMap(t -> t.getContainerTypeId(), t -> t));
        List<String> packingLevels = mtContainerTypes.stream().map(MtContainerType::getPackingLevel)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        // Map<String, MtContLoadDtlVO18> containerIdMap =
        // voList.stream().collect(Collectors.toMap(t -> t.getContainerId(), t -> t));

        // 4.获取容器内所有已装载对象列表
        if (CollectionUtils.isNotEmpty(packingLevels)) {
            // 4-a-ii 获取loadObjectType列表
            List<MtContainerLoadDetail> loadDetails =
                            mtContainerLoadDetailMapper.selectByContainerId(tenantId, containerIds);
            // List<MtContLoadDtlVO19> mtContLoadDtlVO19s =
            // mtContainerLoadDetailRepository.containerLimitObjectBatchGet(tenantId, voList);

            Map<String, List<MtContainerLoadDetail>> loadContainerIdMap =
                            loadDetails.stream().collect(Collectors.groupingBy(MtContainerLoadDetail::getContainerId));
            for (MtContLoadDtlVO18 vo18 : voList) {
                String loadObjectType = vo18.getLoadObjectType();
                if (StringUtils.isEmpty(loadObjectType)) {
                    // 找到containerId对应的containerTypeId
                    MtContainer container = containerIdAndTypeIdMap.get(vo18.getContainerId());

                    // 找到containerTypeId对应的loadObjectType
                    MtContainerType containerType = packingLevelMap.get(container.getContainerId());
                    List<MtContainerLoadDetail> details = loadContainerIdMap.get(vo18.getContainerId());

                    if (CollectionUtils.isEmpty(details)) {
                        return;
                    }
                    // 一个containerId只会对应一个packingLevel，如果一个containerid对应多个loadObjectType则验证失败
                    if (CollectionUtils.isNotEmpty(details)) {
                        if (details.size() > 1) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }

                        String loadType = null;
                        if (CollectionUtils.isNotEmpty(details)) {
                            MtContainerLoadDetail loadDetail = details.get(0);
                            loadType = loadDetail.getLoadObjectType();
                        }

                        // packingLevel 非空，数据库默认值''
                        String packingLevel = containerType.getPackingLevel();

                        if (!packingLevel.equals(loadType)) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }
                    }

                } else {
                    // 找到containerId对应的containerTypeId
                    MtContainer container = containerIdAndTypeIdMap.get(vo18.getContainerId());

                    // 找到containerTypeId对应的loadObjectType
                    MtContainerType containerType = packingLevelMap.get(container.getContainerTypeId());

                    List<MtContainerLoadDetail> details = loadContainerIdMap.get(vo18.getContainerId());

                    if (CollectionUtils.isEmpty(details)) {
                        // packingLevel 非空，数据库默认值''
                        String packingLevel = containerType.getPackingLevel();

                        if (!packingLevel.equals(loadObjectType)) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }
                    } else {
                        if (details.size() > 1) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }

                        // packingLevel 非空，数据库默认值''
                        String packingLevel = containerType.getPackingLevel();

                        String loadType = null;
                        if (CollectionUtils.isNotEmpty(details)) {
                            MtContainerLoadDetail loadDetail = details.get(0);
                            loadType = loadDetail.getLoadObjectType();
                        }

                        // 因为loadObjectType不为空，如果其它为空就报错了
                        if (StringUtils.isEmpty(loadType) || StringUtils.isEmpty(packingLevel)) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }

                        if (!loadType.equals(packingLevel) || !loadType.equals(loadObjectType)
                                        || !loadObjectType.equals(packingLevel)) {
                            throw new MtException("MT_MATERIAL_LOT_0048",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0048", "MATERIAL_LOT",
                                                            "【API:containerPackingLevelBatchVerify】"));
                        }
                    }
                }
            }
        } else {
            return;
        }
    }

    /**
     * 递归查找顶层容器id列表
     *
     * @param tenantId :
     * @param conIds : 容器id列表
     * @return java.util.List<java.lang.String>
     * @Author peng.yuan
     * @Date 2020/2/5 14:48
     */
    private List<String> findTopContainerIdRecursion(Long tenantId, List<String> conIds) {

        List<MtContainerLoadDetail> container = mtContainerLoadDetailRepository.selectByCondition(Condition
                        .builder(MtContainerLoadDetail.class)
                        .andWhere(Sqls.custom().andEqualTo(MtContainerLoadDetail.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtContainerLoadDetail.FIELD_LOAD_OBJECT_TYPE, "CONTAINER")
                                        .andIn(MtContainerLoadDetail.FIELD_LOAD_OBJECT_ID, conIds))
                        .build());

        List<String> containerIdList = container.stream().map(MtContainerLoadDetail::getContainerId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        // 终止条件
        if (CollectionUtils.isEmpty(containerIdList)) {
            return conIds;
        } else {
            // 递归查找
            return findTopContainerIdRecursion(tenantId, conIds);
        }
    }

    @Override
    public void containerCapacityExcessBatchVerify(Long tenantId, List<MtContainerVO37> voList) {
        // 1. 第一步，判断输入参数是否合规
        for (MtContainerVO37 vo : voList) {
            if (StringUtils.isEmpty(vo.getContainerId())) {
                throw new MtException("MT_MATERIAL_LOT_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                "MATERIAL_LOT", "containerId",
                                                "【API：containerCapacityExcessBatchVerify】"));
            }
            if (StringUtils.isNotEmpty(vo.getLoadObjectType())) {
                if (!LOAD_OBJECT_TYPE.contains(vo.getLoadObjectType())) {
                    throw new MtException("MT_MATERIAL_LOT_0003",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0003",
                                                    "MATERIAL_LOT", "loadObjectType",
                                                    "【API：containerCapacityExcessBatchVerify】"));
                }
                if (StringUtils.isEmpty(vo.getLoadObjectId())) {
                    throw new MtException("MT_MATERIAL_LOT_0014",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                                    "MATERIAL_LOT", "loadObjectType", "loadObjectId",
                                                    "【API：containerCapacityExcessBatchVerify】"));
                }
                if ("EO".equals(vo.getLoadObjectType()) && (vo.getLoadQty() == null || vo.getLoadQty() < 0)) {
                    throw new MtException("MT_MATERIAL_LOT_0085",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0085",
                                                    "MATERIAL_LOT", "【API：containerCapacityExcessBatchVerify】"));
                }
            }
        }
        // 找出非EO类型的，又有预装载的数据
        List<MtContainerVO37> otherVoList = voList.stream().filter(
                        t -> StringUtils.isNotEmpty(t.getLoadObjectType()) && !"EO".equals(t.getLoadObjectType()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherVoList)) {
            // 要求除EO类型预装载的东西不能是相同的,loadObjectId和loadObjectType不能存在相同的数据
            if (otherVoList.stream().map(m -> m.getLoadObjectType() + ":" + m.getLoadObjectId()).distinct()
                            .count() != otherVoList.size()) {
                throw new MtException("MT_MATERIAL_LOT_0106",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0106",
                                                "MATERIAL_LOT", "【API：containerCapacityExcessBatchVerify】"));
            }
        }

        // 2. 第二步，根据输入参数containerId列表调用
        List<MtContainer> mtContainers = this.containerPropertyBatchGet(tenantId,
                        voList.stream().map(MtContainerVO37::getContainerId).collect(Collectors.toList()));
        if (voList.stream().anyMatch(t -> !mtContainers.stream().map(MtContainer::getContainerId)
                        .collect(Collectors.toList()).contains(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerCapacityExcessBatchVerify】"));
        }
        // 3. 第三步，获取容器对应容器类型的容量限制
        if (CollectionUtils.isNotEmpty(mtContainers)) {
            List<String> containerTypeIdList = mtContainers.stream().map(MtContainer::getContainerTypeId).distinct()
                            .collect(Collectors.toList());
            List<MtContainerType> mtContainerTypes =
                            mtContainerTypeRepository.containerTypePropertyBatchGet(tenantId, containerTypeIdList);
            if (containerTypeIdList.stream().anyMatch(t -> !mtContainerTypes.stream()
                            .map(MtContainerType::getContainerTypeId).collect(Collectors.toList()).contains(t))) {
                throw new MtException("MT_MATERIAL_LOT_0025",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0025",
                                                "MATERIAL_LOT", "【API：containerCapacityExcessBatchVerify】"));
            }
            // 将容器和容量保存记录
            Map<String, Double> containerMap = new HashMap<>(mtContainers.size());
            for (MtContainer ever : mtContainers) {
                Optional<MtContainerType> typeOp = mtContainerTypes.stream()
                                .filter(t -> ever.getContainerTypeId().equals(t.getContainerTypeId())).findFirst();
                if (typeOp.isPresent()) {
                    Double capacityQty = typeOp.get().getCapacityQty();
                    if (capacityQty != null) {
                        containerMap.put(ever.getContainerId(), capacityQty);
                    }
                }
            }
            if (containerMap.size() > 0) {
                // 4. 第四步，获取容器内所有已装载物料及物料数量
                Map<String, List<MtContainerVO38>> allQtyMap = new HashMap<>(containerMap.size());
                for (Map.Entry<String, Double> ever : containerMap.entrySet()) {
                    MtContLoadDtlVO2 detailVO2 = new MtContLoadDtlVO2();
                    detailVO2.setContainerId(ever.getKey());
                    detailVO2.setAllLevelFlag("Y");
                    List<MtContLoadDtlVO7> existQty =
                                    mtContainerLoadDetailRepository.containerLimitMaterialQtyQuery(tenantId, detailVO2);
                    List<MtContainerVO38> list = existQty.stream().map(m -> {
                        MtContainerVO38 one = new MtContainerVO38();
                        one.setLoadMaterialId(m.getMaterialId());
                        one.setLoadUomId(m.getPrimaryUomId());
                        one.setLoadUomQty(m.getPrimaryUomQty());
                        return one;
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(list)) {
                        allQtyMap.put(ever.getKey(), list);
                    } else {
                        allQtyMap.put(ever.getKey(), new ArrayList<>());
                    }
                }
                // 5. 第五步，获取容器内所有已装载的执行作业及执行作业物料和数量
                List<MtContLoadDtlVO> param = mtContainers.stream().map(m -> {
                    MtContLoadDtlVO one = new MtContLoadDtlVO();
                    one.setContainerId(m.getContainerId());
                    one.setLoadObjectType("EO");
                    one.setAllLevelFlag("Y");
                    return one;
                }).collect(Collectors.toList());
                List<MtContLoadDtlVO21> eoExistQtyList =
                                mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, param);
                List<String> eoIds = new ArrayList<>();
                for (MtContLoadDtlVO21 ever : eoExistQtyList) {
                    if (CollectionUtils.isNotEmpty(ever.getLoadDetailList())) {
                        eoIds.addAll(ever.getLoadDetailList().stream().map(MtContLoadDtlVO6::getLoadObjectId)
                                        .collect(Collectors.toList()));
                    }
                }
                if (CollectionUtils.isNotEmpty(eoIds)) {
                    List<MtEo> eoInfos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
                    // 拼接所有已装载物料
                    for (MtContLoadDtlVO21 ever : eoExistQtyList) {
                        if (CollectionUtils.isNotEmpty(ever.getLoadDetailList())) {
                            // 这是一句话，别看错了
                            List<MtContainerVO38> eoExist = ever.getLoadDetailList().stream().map(m -> {
                                Optional<MtEo> eoOp = eoInfos.stream()
                                                .filter(r -> m.getLoadObjectId().equals(r.getEoId())).findFirst();
                                if (eoOp.isPresent()) {
                                    MtContainerVO38 one = new MtContainerVO38();
                                    one.setLoadMaterialId(eoOp.get().getMaterialId());
                                    one.setLoadUomId(eoOp.get().getUomId());
                                    one.setLoadUomQty(m.getLoadQty());
                                    return one;
                                } else {
                                    return null;
                                }
                            }).filter(Objects::nonNull).collect(Collectors.toList());
                            // 到这里这句话才结束
                            if (allQtyMap.containsKey(ever.getContainerId())) {
                                allQtyMap.get(ever.getContainerId()).addAll(eoExist);
                            } else {
                                allQtyMap.put(ever.getContainerId(), eoExist);
                            }
                        }

                    }
                }

                // 6. 第六步，若loadObjectType有输入，获取当前装载对象对应的物料和数量：
                List<MtContainerVO37> materialList =
                                voList.stream().filter(t -> "MATERIAL_LOT".equals(t.getLoadObjectType())).distinct()
                                                .collect(Collectors.toList());
                List<MtContainerVO37> eoList = voList.stream().filter(t -> "EO".equals(t.getLoadObjectType()))
                                .collect(Collectors.toList());
                List<MtContainerVO37> containerList =
                                voList.stream().filter(t -> "CONTAINER".equals(t.getLoadObjectType())).distinct()
                                                .collect(Collectors.toList());
                // Map<String, List<MtContainerVO38>> preQty = new HashMap<>(containerMap.size());
                if (CollectionUtils.isNotEmpty(materialList)) {
                    List<MtMaterialLotVO5> materialLotVO5s =
                                    mtMaterialLotRepository.materialLotLimitMaterialQtyBatchGet(tenantId,
                                                    materialList.stream().map(MtContainerVO37::getLoadObjectId)
                                                                    .distinct().collect(Collectors.toList()));
                    for (MtContainerVO37 ever : materialList) {
                        List<MtContainerVO38> list = materialLotVO5s.stream()
                                        .filter(t -> ever.getLoadObjectId().equals(t.getMaterialLotId())).map(m -> {
                                            MtContainerVO38 one = new MtContainerVO38();
                                            one.setLoadMaterialId(m.getMaterialId());
                                            one.setLoadUomId(m.getPrimaryUomId());
                                            one.setLoadUomQty(m.getPrimaryUomQty());
                                            return one;
                                        }).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(list)) {
                            if (allQtyMap.containsKey(ever.getContainerId())) {
                                allQtyMap.get(ever.getContainerId()).addAll(list);
                            } else {
                                allQtyMap.put(ever.getContainerId(), list);
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(eoList)) {
                    List<MtEo> eos = mtEoRepository.eoPropertyBatchGet(tenantId, eoList.stream()
                                    .map(MtContainerVO37::getLoadObjectId).distinct().collect(Collectors.toList()));
                    for (MtContainerVO37 ever : eoList) {
                        Optional<MtEo> eoOp = eos.stream().filter(t -> ever.getLoadObjectId().equals(t.getEoId()))
                                        .findFirst();
                        if (eoOp.isPresent()) {
                            MtContainerVO38 one = new MtContainerVO38();
                            one.setLoadMaterialId(eoOp.get().getMaterialId());
                            one.setLoadUomId(eoOp.get().getUomId());
                            one.setLoadUomQty(ever.getLoadQty());
                            if (allQtyMap.containsKey(ever.getContainerId())) {
                                allQtyMap.get(ever.getContainerId()).add(one);
                            } else {
                                allQtyMap.put(ever.getContainerId(), Collections.singletonList(one));
                            }
                        }

                    }
                }
                if (CollectionUtils.isNotEmpty(containerList)) {
                    // 物料批
                    for (MtContainerVO37 ever : containerList) {
                        MtContLoadDtlVO2 detail = new MtContLoadDtlVO2();
                        detail.setContainerId(ever.getLoadObjectId());
                        detail.setAllLevelFlag("Y");
                        List<MtContLoadDtlVO7> existQty = mtContainerLoadDetailRepository
                                        .containerLimitMaterialQtyQuery(tenantId, detail);
                        List<MtContainerVO38> lotListIn = existQty.stream().map(m -> {
                            MtContainerVO38 one = new MtContainerVO38();
                            one.setLoadMaterialId(m.getMaterialId());
                            one.setLoadUomId(m.getPrimaryUomId());
                            one.setLoadUomQty(m.getPrimaryUomQty());
                            return one;
                        }).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(lotListIn)) {
                            if (allQtyMap.containsKey(ever.getContainerId())) {
                                allQtyMap.get(ever.getContainerId()).addAll(lotListIn);
                            } else {
                                allQtyMap.put(ever.getContainerId(), lotListIn);
                            }
                        }
                    }

                    // EO
                    List<MtContLoadDtlVO> paramIn =
                                    containerList.stream().map(MtContainerVO37::getLoadObjectId).distinct().map(m -> {
                                        MtContLoadDtlVO one = new MtContLoadDtlVO();
                                        one.setContainerId(m);
                                        one.setLoadObjectType("EO");
                                        one.setAllLevelFlag("Y");
                                        return one;
                                    }).collect(Collectors.toList());
                    List<MtContLoadDtlVO21> eoExistQtyListIn =
                                    mtContainerLoadDetailRepository.containerLimitObjectBatchQuery(tenantId, paramIn);
                    List<String> eoIdsIn = new ArrayList<>();
                    for (MtContLoadDtlVO21 everIn : eoExistQtyListIn) {
                        if (CollectionUtils.isNotEmpty(everIn.getLoadDetailList())) {
                            eoIdsIn.addAll(everIn.getLoadDetailList().stream().map(MtContLoadDtlVO6::getLoadObjectId)
                                            .collect(Collectors.toList()));
                        }
                    }
                    if (CollectionUtils.isNotEmpty(eoIdsIn)) {
                        List<MtEo> eoInfosIn = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdsIn);
                        // 拼接所有已装载物料
                        for (MtContLoadDtlVO21 ever : eoExistQtyListIn) {
                            if (CollectionUtils.isNotEmpty(ever.getLoadDetailList())) {
                                // 这是一句话
                                List<MtContainerVO38> eoExist = ever.getLoadDetailList().stream().map(m -> {
                                    Optional<MtEo> eoOp = eoInfosIn.stream()
                                                    .filter(r -> m.getLoadObjectId().equals(r.getEoId())).findFirst();
                                    if (eoOp.isPresent()) {
                                        MtContainerVO38 one = new MtContainerVO38();
                                        one.setLoadMaterialId(eoOp.get().getMaterialId());
                                        one.setLoadUomId(eoOp.get().getUomId());
                                        one.setLoadUomQty(m.getLoadQty());
                                        return one;
                                    } else {
                                        return null;
                                    }
                                }).filter(Objects::nonNull).collect(Collectors.toList());
                                // 这里这句话才结束
                                // 找到这个预装载的容器要装到哪个容器中
                                Optional<MtContainerVO37> containerOp = containerList.stream()
                                                .filter(t -> t.getLoadObjectId().equals(ever.getContainerId()))
                                                .findFirst();
                                if (containerOp.isPresent()) {
                                    String containerId = containerOp.get().getContainerId();
                                    if (allQtyMap.containsKey(containerId)) {
                                        allQtyMap.get(containerId).addAll(eoExist);
                                    } else {
                                        allQtyMap.put(containerId, eoExist);
                                    }
                                }

                            }
                        }
                    }
                }
                // 提前获取物料主单位
                List<String> materialIds = new ArrayList<>();
                for (List<MtContainerVO38> ever : allQtyMap.values()) {
                    materialIds.addAll(
                                    ever.stream().map(MtContainerVO38::getLoadMaterialId).collect(Collectors.toList()));
                }

                Map<String, String> materialUomMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(materialIds)) {
                    List<MtMaterialVO1> materialUomList =
                                    mtMaterialRepository.materialUomBatchGet(tenantId, materialIds);
                    materialUomMap = materialUomList.stream().collect(
                                    Collectors.toMap(MtMaterialVO1::getMaterialId, MtMaterialVO1::getPrimaryUomId));
                }

                // 7. 第七步，判断装载是否超出容器容量限制
                for (Map.Entry<String, List<MtContainerVO38>> ever : allQtyMap.entrySet()) {
                    List<MtContainerVO38> list = ever.getValue();
                    if (CollectionUtils.isNotEmpty(list)) {
                        Double capacityQty = 0.0D;
                        // 找到容器容量
                        if (containerMap.containsKey(ever.getKey())) {
                            capacityQty = containerMap.get(ever.getKey());
                        }
                        if (capacityQty == null) {
                            continue;
                        }
                        // 分组求和
                        Map<String, Map<String, BigDecimal>> collect = list.stream().collect(Collectors.groupingBy(
                                        MtContainerVO38::getLoadMaterialId,
                                        Collectors.groupingBy(MtContainerVO38::getLoadUomId,
                                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal
                                                                        .valueOf(c.getLoadUomQty() == null ? 0.0D
                                                                                        : c.getLoadUomQty())))));
                        // 分物料计算
                        BigDecimal needQty = BigDecimal.ZERO;
                        for (Map.Entry<String, Map<String, BigDecimal>> out : collect.entrySet()) {
                            for (Map.Entry<String, BigDecimal> in : out.getValue().entrySet()) {
                                // 获取当前物料主单位
                                if (materialUomMap.containsKey(out.getKey())) {
                                    if (!in.getKey().equals(materialUomMap.get(out.getKey()))) {
                                        MtUomVO1 uom = new MtUomVO1();
                                        uom.setSourceUomId(in.getKey());
                                        uom.setSourceValue(in.getValue().doubleValue());
                                        uom.setTargetUomId(in.getKey());
                                        MtUomVO1 uomNow = mtUomRepository.uomConversion(tenantId, uom);
                                        needQty = needQty.add(BigDecimal.valueOf(uomNow.getTargetValue()));
                                    } else {
                                        needQty = needQty.add(in.getValue());
                                    }
                                }
                            }
                        }
                        // 判断大小
                        if (needQty.compareTo(BigDecimal.valueOf(capacityQty)) > 0) {
                            throw new MtException("MT_MATERIAL_LOT_0043",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0043", "MATERIAL_LOT",
                                                            "【API：containerCapacityExcessBatchVerify】"));
                        }
                    }
                }

            }
        }
    }
}


/**
 * @author peng.yuan
 * @ClassName MyHashMap
 * @description key相同时，value会添加到集合，最后去重
 * @date 2020年02月20日 15:05
 */
class MyHashMap<K, V> {
    Map<K, Collection<V>> map = new HashMap<>();

    public MyHashMap() {}

    private Collection c;

    public void put(K key, V value) {
        c = map.get(key);
        if (c != null && c.size() > 0) {
            for (Object o : (Collection) value) {
                c.add((V) o);
            }
            map.put(key, c);
        } else {
            c = new ArrayList();
            for (Object o : (Collection) value) {
                c.add((V) o);
            }
            map.put(key, c);
        }
    }

    public V get(K key) {
        Collection<V> vs = map.get(key);
        if (vs == null || vs.size() == 0) {
            return null;
        }
        return (V) map.get(key).stream().distinct().collect(Collectors.toList());
    }
}
