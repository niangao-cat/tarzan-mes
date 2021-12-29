package tarzan.inventory.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.inventory.domain.entity.*;
import tarzan.inventory.domain.repository.*;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtContainerLoadDetailMapper;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.inventory.infra.mapper.MtContainerTypeMapper;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;

/**
 * 容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Component
public class MtContainerLoadDetailRepositoryImpl extends BaseRepositoryImpl<MtContainerLoadDetail>
                implements MtContainerLoadDetailRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtContainerLoadDetailMapper mtContainerLoadDetailMapper;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtContainerLoadDetailHisRepository mtContainerLoadDetailHisRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtContainerTypeMapper mtContainerTypeMapper;

    @Autowired
    private MtContainerMapper mtContainerMapper;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    /**
     * containerLimitObjectQuery-获取指定容器下的装载对象
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailV>
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public List<MtContLoadDtlVO6> containerLimitObjectQuery(Long tenantId, MtContLoadDtlVO dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLimitObjectQuery】"));
        }

        // 获取系统参数：递归最大次数
        String recursionMaxCount =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "RECURSION_MAX_COUNT");
        if (StringUtils.isEmpty(recursionMaxCount)) {

            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0011", "GENERAL", "RECURSION_MAX_COUNT", "【API：containerLimitObjectQuery】"));
        }

        Long maxCount = Long.valueOf(recursionMaxCount);
        List<MtContainerLoadDetail> queryResults = getObjectDetail(tenantId, dto);

        Long curNum = 1L;

        if ("Y".equals(dto.getAllLevelFlag())) {
            // 4. 第3.1步，根据输入参数containerId从表MT_CONTAINER中获取容器的顶层容器topContainerId为表中TOP_CONTAINER_ID字段
            MtContainer container = new MtContainer();
            container.setContainerId(dto.getContainerId());
            container.setTenantId(tenantId);
            container = mtContainerRepository.selectOne(container);
            if (Objects.isNull(container) || StringUtils.isEmpty(container.getTopContainerId())) {
                MtContainer topContainer = new MtContainer();
                topContainer.setTopContainerId(dto.getContainerId());
                topContainer.setTenantId(tenantId);
                List<MtContainer> containerList = mtContainerRepository.select(topContainer);
                List<String> containerIdList =
                                containerList.stream().map(MtContainer::getContainerId).collect(Collectors.toList());

                // 增加非空判断
                if (CollectionUtils.isNotEmpty(containerIdList)) {
                    // 根据获取到的c_containerId从表MT_CONTAINER_LOAD_DETAIL中获取装载的物料批数据
                    List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper
                                    .batchGetContainerDetail(tenantId, dto.getLoadObjectType(), containerIdList);
                    queryResults.addAll(loadDetails);
                }
            } else {
                queryResults.addAll(getContainerLimitObjectDetail(tenantId, dto.getContainerId(),
                                dto.getLoadObjectType(), curNum, maxCount));
            }

        }

        List<MtContLoadDtlVO6> resultList = new ArrayList<>();

        // 结果数据填充
        if (CollectionUtils.isNotEmpty(queryResults)) {
            // 求并集去重
            for (MtContainerLoadDetail detail : queryResults) {
                MtContLoadDtlVO6 result = new MtContLoadDtlVO6();
                result.setContainerId(detail.getContainerId());
                result.setLoadObjectId(detail.getLoadObjectId());
                result.setLoadObjectType(detail.getLoadObjectType());
                result.setLoadSequence(detail.getLoadSequence());
                result.setLocationColumn(detail.getLocationColumn());
                result.setLocationRow(detail.getLocationRow());
                result.setLoadEoStepActualId(detail.getLoadEoStepActualId());
                result.setLoadQty(detail.getLoadQty());
                if (!resultList.contains(result)) {
                    resultList.add(result);
                }
            }
        }

        // 根据顺序字段从小到大排序
        resultList.sort(Comparator.comparingLong(MtContLoadDtlVO6::getLoadSequence).thenComparingDouble(
                        c -> Double.valueOf(StringUtils.isEmpty(c.getContainerId()) ? "0" : c.getContainerId())));

        return resultList;
    }

    /**
     * containerLimitObjectBatchQuery-获取指定批量容器下的装载对象
     *
     * @param tenantId
     * @param condition
     * @return
     */
    @Override
    public List<MtContLoadDtlVO21> containerLimitObjectBatchQuery(Long tenantId, List<MtContLoadDtlVO> condition) {

        List<MtContLoadDtlVO21> resultList = new ArrayList<>();

        // 第一步，判断输入参数是否合规
        if (CollectionUtils.isEmpty(condition)
                        || condition.stream().anyMatch(t -> StringUtils.isEmpty(t.getContainerId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API：containerLimitObjectBatchQuery】"));
        }

        // 获取系统参数：递归最大次数
        String recursionMaxCount =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "RECURSION_MAX_COUNT");
        if (StringUtils.isEmpty(recursionMaxCount)) {

            throw new MtException("MT_GENERAL_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0011", "GENERAL",
                                            "RECURSION_MAX_COUNT", "【API：containerLimitObjectBatchQuery】"));
        }

        Long maxCount = Long.valueOf(recursionMaxCount);

        for (MtContLoadDtlVO dto : condition) {
            MtContLoadDtlVO21 result = new MtContLoadDtlVO21();
            result.setContainerId(dto.getContainerId());

            List<MtContainerLoadDetail> queryResults = getObjectDetail(tenantId, dto);

            Long curNum = 1L;

            if ("Y".equals(dto.getAllLevelFlag())) {
                // 4. 第3.1步，根据输入参数containerId从表MT_CONTAINER中获取容器的顶层容器topContainerId为表中TOP_CONTAINER_ID字段
                MtContainer container = new MtContainer();
                container.setContainerId(dto.getContainerId());
                container.setTenantId(tenantId);
                container = mtContainerRepository.selectOne(container);
                if (Objects.isNull(container) || StringUtils.isEmpty(container.getTopContainerId())) {
                    MtContainer topContainer = new MtContainer();
                    topContainer.setTopContainerId(dto.getContainerId());
                    topContainer.setTenantId(tenantId);
                    List<MtContainer> containerList = mtContainerRepository.select(topContainer);
                    List<String> containerIdList = containerList.stream().map(MtContainer::getContainerId)
                                    .collect(Collectors.toList());

                    // 增加非空判断
                    if (CollectionUtils.isNotEmpty(containerIdList)) {
                        // 根据获取到的c_containerId从表MT_CONTAINER_LOAD_DETAIL中获取装载的物料批数据
                        List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper.batchGetContainerDetail(
                                        tenantId,
                                        StringUtils.isEmpty(dto.getLoadObjectType()) ? null : dto.getLoadObjectType(),
                                        containerIdList);
                        queryResults.addAll(loadDetails);
                    }
                } else {
                    queryResults.addAll(getContainerLimitObjectDetail(tenantId, dto.getContainerId(),
                                    dto.getLoadObjectType(), curNum, maxCount));
                }

            }

            // 结果数据填充
            if (CollectionUtils.isNotEmpty(queryResults)) {
                // 求并集去重
                List<MtContLoadDtlVO6> collect = queryResults.stream().distinct().map(t -> {
                    MtContLoadDtlVO6 vo6 = new MtContLoadDtlVO6();
                    vo6.setLoadObjectType(t.getLoadObjectType());
                    vo6.setLoadObjectId(t.getLoadObjectId());
                    vo6.setLocationRow(t.getLocationRow());
                    vo6.setLocationColumn(t.getLocationColumn());
                    vo6.setLoadSequence(t.getLoadSequence());
                    vo6.setContainerId(t.getContainerId());
                    vo6.setLoadQty(t.getLoadQty());
                    vo6.setLoadEoStepActualId(t.getLoadEoStepActualId());
                    return vo6;
                }).collect(Collectors.toList());
                // 根据顺序字段从小到大排序
                collect.sort(Comparator.comparingLong(MtContLoadDtlVO6::getLoadSequence).thenComparingDouble(c -> Double
                                .valueOf(StringUtils.isEmpty(c.getContainerId()) ? "0" : c.getContainerId())));
                result.setLoadDetailList(collect);
            }

            resultList.add(result);
        }

        return resultList;
    }

    /**
     * 获取Object明细-递归
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getContainerLimitObjectDetail(Long tenantId, String containerId,
                    String inputLoadObjectType, Long curNum, Long maxCount) {
        curNum++;
        if (curNum >= maxCount) {
            return Collections.emptyList();
        }
        List<MtContainerLoadDetail> result = new ArrayList<>();
        MtContainerLoadDetail query = new MtContainerLoadDetail();
        query.setContainerId(containerId);
        query.setLoadObjectType("CONTAINER");
        query.setTenantId(tenantId);
        List<MtContainerLoadDetail> next = mtContainerLoadDetailMapper.select(query);
        if (CollectionUtils.isNotEmpty(next)) {
            for (MtContainerLoadDetail detail : next) {
                MtContLoadDtlVO voTemp = new MtContLoadDtlVO();
                voTemp.setContainerId(detail.getLoadObjectId());
                voTemp.setLoadObjectType(inputLoadObjectType);
                voTemp.setAllLevelFlag("Y");
                result.addAll(getObjectDetail(tenantId, voTemp));
                result.addAll(getContainerLimitObjectDetail(tenantId, detail.getLoadObjectId(), inputLoadObjectType,
                                curNum, maxCount));
            }
        }
        return result;
    }


    /**
     * 获取Object明细
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getObjectDetail(Long tenantId, MtContLoadDtlVO dtlVO) {
        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setContainerId(dtlVO.getContainerId());
        if (StringUtils.isNotEmpty(dtlVO.getLoadObjectType())) {
            mtContainerLoadDetail.setLoadObjectType(dtlVO.getLoadObjectType());
        }

        if (!"Y".equals(dtlVO.getAllLevelFlag())) {
            mtContainerLoadDetail.setLocationColumn(dtlVO.getLocationColumn());
            mtContainerLoadDetail.setLocationRow(dtlVO.getLocationRow());
        }
        mtContainerLoadDetail.setTenantId(tenantId);
        return mtContainerLoadDetailMapper.select(mtContainerLoadDetail);
    }


    /**
     * 功能描述
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtConLoadDetailVO1>
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public List<MtContLoadDtlVO1> containerLimitEoQuery(Long tenantId, MtContLoadDtlVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLimitEoQuery】"));
        }


        // 获取系统参数：递归最大次数
        String recursionMaxCount =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "RECURSION_MAX_COUNT");
        if (StringUtils.isEmpty(recursionMaxCount)) {
            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0011", "GENERAL", "RECURSION_MAX_COUNT", "【API：containerLimitEoQuery】"));
        }

        Long maxCount = Long.valueOf(recursionMaxCount);
        List<MtContainerLoadDetail> queryResults = getEoDetail(tenantId, dto.getContainerId());

        Long curNum = 1L;

        if (!"N".equals(dto.getAllLevelFlag())) {
            // 第3.1步，根据输入参数containerId从表MT_CONTAINER中获取容器的顶层容器topContainerId为表中TOP_CONTAINER_ID字段
            MtContainer container = new MtContainer();
            container.setContainerId(dto.getContainerId());
            container.setTenantId(tenantId);
            container = mtContainerRepository.selectOne(container);
            if (Objects.isNull(container) || StringUtils.isEmpty(container.getTopContainerId())) {
                MtContainer topContainer = new MtContainer();
                topContainer.setTopContainerId(dto.getContainerId());
                topContainer.setTenantId(tenantId);
                List<MtContainer> containerList = mtContainerRepository.select(topContainer);
                List<String> containerIdList =
                                containerList.stream().map(MtContainer::getContainerId).collect(Collectors.toList());

                // 增加判空验证
                if (CollectionUtils.isNotEmpty(containerIdList)) {
                    // 根据获取到的c_containerId从表MT_CONTAINER_LOAD_DETAIL中获取装载的物料批数据
                    List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper
                                    .batchGetContainerDetail(tenantId, "EO", containerIdList);
                    queryResults.addAll(loadDetails);
                    queryResults.stream().distinct().collect(Collectors.toList());
                }
            } else {
                // 第四步
                queryResults.addAll(getContainerLimitEoDetail(tenantId, dto.getContainerId(), curNum, maxCount));
            }

        }

        List<MtContLoadDtlVO1> resultList = new ArrayList<>();

        // 结果数据填充
        if (CollectionUtils.isNotEmpty(queryResults)) {
            // 求并集去重
            for (MtContainerLoadDetail detail : queryResults) {
                MtContLoadDtlVO1 result = new MtContLoadDtlVO1();
                result.setContainerId(detail.getContainerId());
                result.setEoId(detail.getLoadObjectId());
                result.setLocationRow(detail.getLocationRow());
                result.setLocationColumn(detail.getLocationColumn());
                result.setLoadSequence(detail.getLoadSequence());
                result.setLoadQty(detail.getLoadQty());
                result.setLoadEoStepActualId(detail.getLoadEoStepActualId());
                if (!resultList.contains(result)) {
                    resultList.add(result);
                }
            }
        }

        // 根据顺序字段从小到大排序
        resultList.sort(Comparator.comparingLong(MtContLoadDtlVO1::getLoadSequence));

        return resultList;
    }

    /**
     * 获取EO明细-递归
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getContainerLimitEoDetail(Long tenantId, String containerId, Long curNum,
                    Long maxCount) {
        curNum++;
        if (curNum >= maxCount) {
            return Collections.emptyList();
        }
        List<MtContainerLoadDetail> result = new ArrayList<>();
        MtContainerLoadDetail query = new MtContainerLoadDetail();
        query.setContainerId(containerId);
        query.setLoadObjectType("CONTAINER");
        query.setTenantId(tenantId);
        List<MtContainerLoadDetail> next = mtContainerLoadDetailMapper.select(query);
        if (CollectionUtils.isNotEmpty(next)) {
            for (MtContainerLoadDetail detail : next) {
                result.addAll(getEoDetail(tenantId, detail.getLoadObjectId()));
                result.addAll(getContainerLimitEoDetail(tenantId, detail.getLoadObjectId(), curNum, maxCount));
            }
        }
        return result;
    }


    /**
     * 获取EO明细
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getEoDetail(Long tenantId, String containerId) {
        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setContainerId(containerId);
        mtContainerLoadDetail.setLoadObjectType("EO");
        mtContainerLoadDetail.setTenantId(tenantId);
        return mtContainerLoadDetailMapper.select(mtContainerLoadDetail);
    }


    /**
     * containerLimitContainerQuery-获取指定容器已装载的所有容器
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailVO3>
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public List<MtContLoadDtlVO3> containerLimitContainerQuery(Long tenantId, MtContLoadDtlVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API：containerLimitContainerQuery】"));
        }

        // 获取系统参数：递归最大次数
        String recursionMaxCount =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "RECURSION_MAX_COUNT");
        if (StringUtils.isEmpty(recursionMaxCount)) {

            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0011", "GENERAL", "RECURSION_MAX_COUNT", "【API：containerLimitContainerQuery】"));
        }

        Long maxCount = Long.valueOf(recursionMaxCount);
        List<MtContainerLoadDetail> queryResults = getContainerDetail(tenantId, dto.getContainerId());

        Long curNum = 1L;

        if (!"N".equals(dto.getAllLevelFlag())) {
            MtContainer mtContainer = new MtContainer();
            mtContainer.setTenantId(tenantId);
            mtContainer.setContainerId(dto.getContainerId());
            mtContainer = mtContainerMapper.selectOne(mtContainer);
            if (null != mtContainer) {
                if (StringUtils.isEmpty(mtContainer.getTopContainerId())) {
                    mtContainer = new MtContainer();
                    mtContainer.setTenantId(tenantId);
                    mtContainer.setTopContainerId(dto.getContainerId());
                    List<String> containerIds = mtContainerMapper.select(mtContainer).stream()
                                    .map(MtContainer::getContainerId).collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(containerIds)) {
                        List<MtContainerLoadDetail> details =
                                        batchGetContainerDetail(tenantId, "CONTAINER", containerIds);
                        queryResults.addAll(details);
                    }
                } else {
                    queryResults.addAll(
                                    getContainerLimitContainerDetail(tenantId, dto.getContainerId(), curNum, maxCount));
                }
            }
        }

        List<MtContLoadDtlVO3> resultList = new ArrayList<>();

        // 结果数据填充
        if (CollectionUtils.isNotEmpty(queryResults)) {
            // 求并集去重
            for (MtContainerLoadDetail detail : queryResults) {
                MtContLoadDtlVO3 result = new MtContLoadDtlVO3();
                result.setUpperContainerId(detail.getContainerId());
                result.setLoadContainerId(detail.getLoadObjectId());
                result.setLocationRow(detail.getLocationRow());
                result.setLocationColumn(detail.getLocationColumn());
                result.setLoadSequence(detail.getLoadSequence());
                if (!resultList.contains(result)) {
                    resultList.add(result);
                }
            }
        }

        // 根据顺序字段从小到大排序
        resultList.sort(Comparator.comparingLong(MtContLoadDtlVO3::getLoadSequence));

        return resultList;
    }

    /**
     * 获取容器明细-递归
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getContainerLimitContainerDetail(Long tenantId, String containerId, Long curNum,
                    Long maxCount) {
        curNum++;
        if (curNum >= maxCount) {
            return Collections.emptyList();
        }
        List<MtContainerLoadDetail> result = new ArrayList<>();
        MtContainerLoadDetail query = new MtContainerLoadDetail();
        query.setContainerId(containerId);
        query.setLoadObjectType("CONTAINER");
        query.setTenantId(tenantId);
        List<MtContainerLoadDetail> next = mtContainerLoadDetailMapper.select(query);
        if (CollectionUtils.isNotEmpty(next)) {
            for (MtContainerLoadDetail detail : next) {
                result.addAll(getContainerDetail(tenantId, detail.getLoadObjectId()));
                result.addAll(getContainerLimitContainerDetail(tenantId, detail.getLoadObjectId(), curNum, maxCount));
            }
        }
        return result;
    }


    /**
     * 获取容器明细
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getContainerDetail(Long tenantId, String containerId) {
        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setContainerId(containerId);
        mtContainerLoadDetail.setLoadObjectType("CONTAINER");
        mtContainerLoadDetail.setTenantId(tenantId);
        return mtContainerLoadDetailMapper.select(mtContainerLoadDetail);
    }

    /**
     * 批量获取容器明细
     *
     * @param tenantId
     * @param loadObjectType
     * @param loadObjectIds
     * @return
     */
    private List<MtContainerLoadDetail> batchGetContainerDetail(Long tenantId, String loadObjectType,
                    List<String> loadObjectIds) {
        return mtContainerLoadDetailMapper.selectByLoadObject(tenantId, loadObjectType, loadObjectIds);
    }

    /**
     * containerLimitMaterialLotQuery-获取指定容器已装载的物料批
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailVO4>
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public List<MtContLoadDtlVO4> containerLimitMaterialLotQuery(Long tenantId, MtContLoadDtlVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API：containerLimitMaterialLotQuery】"));
        }

        // 获取系统参数：递归最大次数
        String recursionMaxCount =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "RECURSION_MAX_COUNT");
        if (StringUtils.isEmpty(recursionMaxCount)) {
            throw new MtException("MT_GENERAL_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0011", "GENERAL",
                                            "RECURSION_MAX_COUNT", "【API：containerLimitMaterialLotQuery】"));
        }

        Long maxCount = Long.valueOf(recursionMaxCount);
        List<MtContainerLoadDetail> queryResults = getMaterialLotDetail(tenantId, dto.getContainerId());

        Long curNum = 1L;

        if (!"N".equals(dto.getAllLevelFlag())) {
            MtContainer mtContainer = new MtContainer();
            mtContainer.setTenantId(tenantId);
            mtContainer.setContainerId(dto.getContainerId());
            mtContainer = mtContainerMapper.selectOne(mtContainer);
            if (null != mtContainer) {
                if (StringUtils.isEmpty(mtContainer.getTopContainerId())) {
                    List<String> lotIds =
                                    mtMaterialLotRepository.selectLotIdByTopContainerId(tenantId, dto.getContainerId());
                    if (CollectionUtils.isNotEmpty(lotIds)) {
                        List<MtContainerLoadDetail> details = batchGetContainerDetail(tenantId, "MATERIAL_LOT", lotIds);
                        queryResults.addAll(details);
                    }
                } else {
                    queryResults.addAll(getContainerLimitMaterialLotDetail(tenantId, dto.getContainerId(), curNum,
                                    maxCount));
                }
            }
        }

        List<MtContLoadDtlVO4> resultList = new ArrayList<>();

        // 结果数据填充
        if (CollectionUtils.isNotEmpty(queryResults)) {
            // 求并集去重
            for (MtContainerLoadDetail detail : queryResults) {
                MtContLoadDtlVO4 result = new MtContLoadDtlVO4();
                result.setContainerId(detail.getContainerId());
                result.setMaterialLotId(detail.getLoadObjectId());
                result.setLocationRow(detail.getLocationRow());
                result.setLocationColumn(detail.getLocationColumn());
                result.setLoadSequence(detail.getLoadSequence());
                if (!resultList.contains(result)) {
                    resultList.add(result);
                }
            }
        }

        // 根据顺序字段从小到大排序
        resultList.sort(Comparator.comparingLong(MtContLoadDtlVO4::getLoadSequence));

        return resultList;
    }

    /**
     * 获取物料批明细-递归
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getContainerLimitMaterialLotDetail(Long tenantId, String containerId,
                    Long curNum, Long maxCount) {
        curNum++;
        if (curNum >= maxCount) {
            return Collections.emptyList();
        }
        List<MtContainerLoadDetail> result = new ArrayList<>();
        MtContainerLoadDetail query = new MtContainerLoadDetail();
        query.setContainerId(containerId);
        query.setLoadObjectType("CONTAINER");
        query.setTenantId(tenantId);
        List<MtContainerLoadDetail> next = mtContainerLoadDetailMapper.select(query);
        if (CollectionUtils.isNotEmpty(next)) {
            for (MtContainerLoadDetail detail : next) {
                result.addAll(getMaterialLotDetail(tenantId, detail.getLoadObjectId()));
                result.addAll(getContainerLimitMaterialLotDetail(tenantId, detail.getLoadObjectId(), curNum, maxCount));
            }
        }
        return result;
    }


    /**
     * 获取物料批明细
     *
     * @return
     * @author chuang.yang
     * @date 2019/4/16
     */
    private List<MtContainerLoadDetail> getMaterialLotDetail(Long tenantId, String containerId) {
        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setContainerId(containerId);
        mtContainerLoadDetail.setLoadObjectType("MATERIAL_LOT");
        mtContainerLoadDetail.setTenantId(tenantId);
        return mtContainerLoadDetailMapper.select(mtContainerLoadDetail);
    }


    /**
     * containerLimitMaterialQtyQuery-获取指定容器已装载的物料和物料数量
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtConLoadDetailVO7>
     * @author chuang.yang
     * @date 2019/4/3
     */
    @Override
    public List<MtContLoadDtlVO7> containerLimitMaterialQtyQuery(Long tenantId, MtContLoadDtlVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId", "【API：containerLimitMaterialQtyQuery】"));
        }

        // 2. 根据输入参数获取容器内已装载的物料批
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(dto.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag(dto.getAllLevelFlag());
        List<MtContLoadDtlVO4> mtContLoadDtlVO4List = containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);
        if (CollectionUtils.isEmpty(mtContLoadDtlVO4List)) {
            return Collections.emptyList();
        }

        List<String> materialLotIds = mtContLoadDtlVO4List.stream().map(MtContLoadDtlVO4::getMaterialLotId).distinct()
                        .collect(Collectors.toList());

        // 3. 根据第2步获取到的物料批列表获取物料批数量
        List<MtMaterialLotVO5> mtMaterialLotVO5s =
                        mtMaterialLotRepository.materialLotLimitMaterialQtyBatchGet(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(mtMaterialLotVO5s)) {
            return Collections.emptyList();
        }

        // 以materialId为key，转换成map数据，方便下面逻辑筛选
        Map<String, List<MtMaterialLotVO5>> materialLotMap =
                        mtMaterialLotVO5s.stream().collect(Collectors.groupingBy(MtMaterialLotVO5::getMaterialId));

        List<MtContLoadDtlVO7> resultList = new ArrayList<>();

        // 4. 根据输入参数获取结果并返回
        if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            MtContLoadDtlVO7 result = new MtContLoadDtlVO7();
            result.setMaterialId(dto.getMaterialId());

            // 4.1. 筛选第三步获取结果中materialId与输入参数materialId一致的所有数据
            List<MtMaterialLotVO5> matchMaterialLots = materialLotMap.get(dto.getMaterialId());
            if (CollectionUtils.isEmpty(matchMaterialLots)) {
                return Collections.emptyList();
            }

            // 返回主单位 primaryUomId 和主单位数量primaryUomQty
            Map<String, List<MtMaterialLotVO5>> primaryUomMaterialMap = matchMaterialLots.stream()
                            .collect(Collectors.groupingBy(MtMaterialLotVO5::getPrimaryUomId));
            if (primaryUomMaterialMap.size() > 1) {
                /*
                 * 分组结果如果大于1，表示有多个分组，说明 primaryUomId 不都一致 结果取 mPrimaryUomId、mPrimaryUomQty之和
                 */
                result.setPrimaryUomId(matchMaterialLots.get(0).getmPrimaryUomId());

                BigDecimal sumMPrimaryUomQty = matchMaterialLots.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getmPrimaryUomQty() == null ? 0.0D : c.getmPrimaryUomQty())));

                result.setPrimaryUomQty(sumMPrimaryUomQty.doubleValue());
            } else {
                /*
                 * 表示只有1个分组，说明 primaryUomId 都一致 结果取 primaryUomId、primaryUomQty之和
                 */
                result.setPrimaryUomId(matchMaterialLots.get(0).getPrimaryUomId());

                BigDecimal sumPrimaryUomQty = matchMaterialLots.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getPrimaryUomQty() == null ? 0.0D : c.getPrimaryUomQty())));

                if (sumPrimaryUomQty != null) {
                    result.setPrimaryUomQty(sumPrimaryUomQty.doubleValue());
                }
            }

            // 返回辅助单位 secondaryUomId 和辅助单位数量secondaryUomQty
            // 筛选辅助单位不为空字符串的数据
            List<MtMaterialLotVO5> secondUomIdAvailableMaterialLots = matchMaterialLots.stream()
                            .filter(t -> !"".equals(t.getSecondaryUomId())).collect(Collectors.toList());

            Map<String, List<MtMaterialLotVO5>> secondaryUomMaterialMap = secondUomIdAvailableMaterialLots.stream()
                            .collect(Collectors.groupingBy(MtMaterialLotVO5::getSecondaryUomId));
            if (secondaryUomMaterialMap.size() > 1) {
                /*
                 * 分组结果如果大于1，表示有多个分组，说明 secondaryUomId 不都一致 结果取 mSecondaryUomId、mSecondaryUomQty
                 */
                result.setSecondaryUomId(secondUomIdAvailableMaterialLots.get(0).getmSecondaryUomId());
                BigDecimal sumMSecondaryUmQty = secondUomIdAvailableMaterialLots.stream()
                                .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                c.getmSecondaryUomQty() == null ? 0.0D : c.getmSecondaryUomQty())));

                if (sumMSecondaryUmQty != null) {
                    result.setSecondaryUomQty(sumMSecondaryUmQty.doubleValue());
                }
            } else if (secondaryUomMaterialMap.size() == 1) {
                /*
                 * 表示只有1个分组，说明 secondaryUomId 都一致， 结果取 secondaryUomId、secondaryUomQty
                 */
                result.setSecondaryUomId(secondUomIdAvailableMaterialLots.get(0).getSecondaryUomId());
                BigDecimal sumSecondaryUomQty = secondUomIdAvailableMaterialLots.stream()
                                .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                c.getSecondaryUomQty() == null ? 0.0D : c.getSecondaryUomQty())));
                if (sumSecondaryUomQty != null) {
                    result.setSecondaryUomQty(sumSecondaryUomQty.doubleValue());
                }
            }

            resultList.add(result);
            return resultList;
        }

        // 未输入 materialId 的时候，整体判断返回结果
        for (Map.Entry<String, List<MtMaterialLotVO5>> entry : materialLotMap.entrySet()) {
            MtContLoadDtlVO7 result = new MtContLoadDtlVO7();
            result.setMaterialId(entry.getKey());

            List<MtMaterialLotVO5> materialLots = entry.getValue();

            // 返回主单位 primaryUomId 和主单位数量 primaryUomQty
            Map<String, List<MtMaterialLotVO5>> primaryUomMaterialMap =
                            materialLots.stream().collect(Collectors.groupingBy(MtMaterialLotVO5::getPrimaryUomId));
            if (primaryUomMaterialMap.size() > 1) {
                /*
                 * 分组结果如果大于1，表示有多个分组，说明 primaryUomId 不都一致 结果取 mPrimaryUomId、mPrimaryUomQty
                 */
                result.setPrimaryUomId(materialLots.get(0).getmPrimaryUomId());
                result.setPrimaryUomQty(materialLots.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getmPrimaryUomQty() == null ? 0.0D : c.getmPrimaryUomQty())))
                                .doubleValue());
            } else {
                /*
                 * 表示只有1个分组，说明 primaryUomId 都一致 结果取 primaryUomId、primaryUomQty
                 */
                result.setPrimaryUomId(materialLots.get(0).getPrimaryUomId());
                result.setPrimaryUomQty(materialLots.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getPrimaryUomQty() == null ? 0.0D : c.getPrimaryUomQty())))
                                .doubleValue());
            }

            // 返回辅助单位 secondaryUomId 和辅助单位数量secondaryUomQty
            // 筛选辅助单位不为空字符串的数据
            List<MtMaterialLotVO5> secondUomIdAvailableMaterialLots = materialLots.stream()
                            .filter(t -> !"".equals(t.getSecondaryUomId())).collect(Collectors.toList());

            Map<String, List<MtMaterialLotVO5>> secondaryUomMaterialMap = secondUomIdAvailableMaterialLots.stream()
                            .collect(Collectors.groupingBy(MtMaterialLotVO5::getSecondaryUomId));
            if (secondaryUomMaterialMap.size() > 1) {
                /*
                 * 分组结果如果大于1，表示有多个分组，说明 secondaryUomId 不都一致 结果取 mSecondaryUomId、mSecondaryUomQty
                 */
                result.setSecondaryUomId(secondUomIdAvailableMaterialLots.get(0).getmSecondaryUomId());
                result.setSecondaryUomQty(secondUomIdAvailableMaterialLots.stream()
                                .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                c.getmSecondaryUomQty() == null ? 0.0D : c.getmSecondaryUomQty())))
                                .doubleValue());
            } else if (secondaryUomMaterialMap.size() == 1) {
                /*
                 * 表示只有1个分组，说明 secondaryUomId 都一致， 结果取 secondaryUomId、secondaryUomQty
                 */
                result.setSecondaryUomId(secondUomIdAvailableMaterialLots.get(0).getSecondaryUomId());
                result.setSecondaryUomQty(secondUomIdAvailableMaterialLots.stream()
                                .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(
                                                c.getSecondaryUomQty() == null ? 0.0D : c.getSecondaryUomQty())))
                                .doubleValue());
            }
            resultList.add(result);
        }

        return resultList;
    }


    @Override
    public List<String> objectLimitLoadingContainerQuery(Long tenantId, MtContLoadDtlVO5 dto) {
        // 第一步，判断输入参数是否合规
        if (dto == null || StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectId", "【API：objectLimitLoadingContainerQuery】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectType",
                                            "【API：objectLimitLoadingContainerQuery】"));
        }
        List<String> result = new ArrayList<>();
        // 判断容器类型
        if ("EO".equalsIgnoreCase(dto.getLoadObjectType())) {
            // 第二步，根据输入参数获取直接装载容器
            MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
            mtContainerLoadDetail.setLoadObjectType(dto.getLoadObjectType());
            mtContainerLoadDetail.setLoadObjectId(dto.getLoadObjectId());
            mtContainerLoadDetail.setTenantId(tenantId);
            List<MtContainerLoadDetail> mtContainerLoadDetailList =
                            mtContainerLoadDetailMapper.select(mtContainerLoadDetail);
            if (CollectionUtils.isEmpty(mtContainerLoadDetailList)) {
                return result;
            }

            // 去重，获取containerId集合
            List<String> uniqueContainerIds = mtContainerLoadDetailList.stream()
                            .map(MtContainerLoadDetail::getContainerId).distinct().collect(Collectors.toList());

            if (!"Y".equals(dto.getTopLevelFlag())) {
                result.addAll(uniqueContainerIds);
            } else {
                // topContainerId = 第二步2-a-ii获取到的结果containerId
                List<String> resultList = new ArrayList<>();
                for (String topContainerId : uniqueContainerIds) {
                    MtContainerLoadDetail topDetail = new MtContainerLoadDetail();
                    topDetail.setLoadObjectType("CONTAINER");
                    topDetail.setLoadObjectId(topContainerId);
                    topDetail.setTenantId(tenantId);

                    String supContainerId = topContainerId;
                    boolean notFindFlag = true;
                    while (notFindFlag) {
                        topDetail = mtContainerLoadDetailMapper.selectOne(topDetail);
                        if (topDetail == null) {
                            // 查询上层container无果，则认为当前container即为topContainer
                            resultList.add(supContainerId);
                            notFindFlag = false;
                        } else {
                            // 查询上层container有值，则根据此上层container继续向上查找topContainer
                            supContainerId = topDetail.getContainerId();
                            topDetail = new MtContainerLoadDetail();
                            topDetail.setLoadObjectType("CONTAINER");
                            topDetail.setLoadObjectId(supContainerId);
                        }
                    }
                }

                result.addAll(resultList.stream().distinct().collect(Collectors.toList()));
            }
        } else {
            if ("CONTAINER".equalsIgnoreCase(dto.getLoadObjectType())) {
                MtContainer mtContainer = new MtContainer();
                mtContainer.setTenantId(tenantId);
                mtContainer.setContainerId(dto.getLoadObjectId());
                mtContainer = mtContainerRepository.selectOne(mtContainer);
                if (mtContainer != null) {
                    if ("Y".equalsIgnoreCase(dto.getTopLevelFlag())) {
                        result.add(mtContainer.getTopContainerId());
                    } else {
                        result.add(mtContainer.getCurrentContainerId());
                    }
                }
            }

            if ("MATERIAL_LOT".equalsIgnoreCase(dto.getLoadObjectType())) {
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setTenantId(tenantId);
                mtMaterialLot.setMaterialLotId(dto.getLoadObjectId());
                mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
                if (mtMaterialLot != null) {
                    if ("Y".equalsIgnoreCase(dto.getTopLevelFlag())) {
                        result.add(mtMaterialLot.getTopContainerId());
                    } else {
                        result.add(mtMaterialLot.getCurrentContainerId());
                    }
                }
            }
        }
        result.removeIf(StringUtils::isEmpty);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtContLoadDtlVO11 containerLoadDetailUpdate(Long tenantId, MtContLoadDtlVO8 dto) {
        String topContainerId = null;
        // 1.第一步：判断输入参数是否合规：
        if (dto == null || StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLoadDetailUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectType", "【API：containerLoadDetailUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", "【API：containerLoadDetailUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：containerLoadDetailUpdate】"));
        }
        if (dto.getLoadQty() != null && dto.getTrxLoadQty() != null) {
            throw new MtException("MT_MATERIAL_LOT_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                            "MATERIAL_LOT", "loadQty、trxLoadQty", "【API：containerLoadDetailUpdate】"));
        }

        // 获取容器对应的容器类型
        String containerTypeId = mtContainerRepository.containerLimitContainerTypeGet(tenantId, dto.getContainerId());
        if (StringUtils.isEmpty(containerTypeId)) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
        }

        // 获取容器类型启用位置管理标识locationEnableFlag、行数locationRow、列数
        MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId);
        String locationEnabledFlag = null;
        if (mtContainerType != null) {
            locationEnabledFlag = mtContainerType.getLocationEnabledFlag();
            if ("Y".equals(locationEnabledFlag)) {
                if (dto.getLocationColumn() == null || dto.getLocationRow() == null) {
                    throw new MtException("MT_MATERIAL_LOT_0027",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0027",
                                                    "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
                }

                if (mtContainerType.getLocationColumn() == null
                                || BigDecimal.ZERO.compareTo(new BigDecimal(dto.getLocationColumn().toString())) >= 0
                                || new BigDecimal(dto.getLocationColumn().toString()).compareTo(
                                                new BigDecimal(mtContainerType.getLocationColumn().toString())) > 0) {
                    throw new MtException("MT_MATERIAL_LOT_0038",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0038",
                                                    "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));

                }
                if (mtContainerType.getLocationRow() == null
                                || BigDecimal.ZERO.compareTo(new BigDecimal(dto.getLocationRow().toString())) >= 0
                                || new BigDecimal(dto.getLocationRow().toString()).compareTo(
                                                new BigDecimal(mtContainerType.getLocationRow().toString())) > 0) {
                    throw new MtException("MT_MATERIAL_LOT_0038",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0038",
                                                    "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
                }
            }
        }

        // 用来判断新增/更新逻辑
        MtContainerLoadDetail mtContainerLoadDetail = null;

        // 2.第二步，根据传入参数判断更新或创建容器装载明细
        boolean isEOType = "EO".equals(dto.getLoadObjectType());
        if (!isEOType) {
            // 判断装载象是否已被装载到其他容器上
            MtContainerLoadDetail detailCheck = new MtContainerLoadDetail();
            detailCheck.setLoadObjectId(dto.getLoadObjectId());
            detailCheck.setLoadObjectType(dto.getLoadObjectType());
            detailCheck.setTenantId(tenantId);
            List<MtContainerLoadDetail> mtContainerLoadDetailList = mtContainerLoadDetailMapper.select(detailCheck);
            if (CollectionUtils.isNotEmpty(mtContainerLoadDetailList)) {
                throw new MtException("MT_MATERIAL_LOT_0039", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0039", "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
            }
        } else {
            // 根据以下限制判断新增或更新模式
            mtContainerLoadDetail = new MtContainerLoadDetail();
            mtContainerLoadDetail.setLoadObjectId(dto.getLoadObjectId());
            mtContainerLoadDetail.setLoadObjectType(dto.getLoadObjectType());
            mtContainerLoadDetail.setContainerId(dto.getContainerId());
            if ("Y".equals(locationEnabledFlag)) {
                mtContainerLoadDetail.setLocationRow(dto.getLocationRow());
                mtContainerLoadDetail.setLocationColumn(dto.getLocationColumn());
            }

            mtContainerLoadDetail.setTenantId(tenantId);
            mtContainerLoadDetail = mtContainerLoadDetailMapper.selectOne(mtContainerLoadDetail);

            if (mtContainerLoadDetail == null) {
                // 若获取数据为空，loadQty和trxLoadQty参数必须有一个有值，且值必须大于0
                boolean loadAndTrxLoadEmptyFlag = dto.getTrxLoadQty() == null && dto.getLoadQty() == null;
                boolean loadAvailableFlag = dto.getLoadQty() != null
                                && BigDecimal.valueOf(dto.getLoadQty()).compareTo(BigDecimal.ZERO) <= 0;
                boolean trxLoadAvailableFlag = dto.getTrxLoadQty() != null
                                && BigDecimal.valueOf(dto.getTrxLoadQty()).compareTo(BigDecimal.ZERO) <= 0;
                if (loadAndTrxLoadEmptyFlag || loadAvailableFlag || trxLoadAvailableFlag) {
                    throw new MtException("MT_MATERIAL_LOT_0085",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0085",
                                                    "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
                }
            }

            // 若获取到数据，loadQty和trxLoadQty参数必须有一个有值
            if (dto.getTrxLoadQty() == null && dto.getLoadQty() == null) {
                throw new MtException("MT_MATERIAL_LOT_0067", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0067", "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
            }
        }

        // 第三步，根据当前装载对象创建容器装载明细
        MtContLoadDtlVO11 result = new MtContLoadDtlVO11();
        Double trxLoadQty = null;
        // 如果为null，则执行新增模式否则新增模式
        if (mtContainerLoadDetail == null) {
            // 根据当前装载对象创建容器装载明细
            mtContainerLoadDetail = new MtContainerLoadDetail();
            mtContainerLoadDetail.setContainerId(dto.getContainerId());
            if ("Y".equals(locationEnabledFlag)) {
                mtContainerLoadDetail.setLocationColumn(dto.getLocationColumn());
                mtContainerLoadDetail.setLocationRow(dto.getLocationRow());
            }
            mtContainerLoadDetail.setLoadObjectType(dto.getLoadObjectType());
            mtContainerLoadDetail.setLoadObjectId(dto.getLoadObjectId());

            // 获取最大序列
            Long sequence = mtContainerLoadDetailMapper.getMaxSequence(tenantId, dto.getContainerId());
            if (sequence == null) {
                sequence = Long.valueOf(10L);
            } else {
                sequence += Long.valueOf(10L);
            }
            mtContainerLoadDetail.setLoadSequence(sequence);

            if (isEOType) {
                mtContainerLoadDetail.setLoadQty(dto.getLoadQty() != null ? dto.getLoadQty() : dto.getTrxLoadQty());
                mtContainerLoadDetail.setLoadEoStepActualId(dto.getLoadEoStepActualId());
            }

            mtContainerLoadDetail.setTenantId(tenantId);
            self().insertSelective(mtContainerLoadDetail);
            result.setContainerLoadDetailId(mtContainerLoadDetail.getContainerLoadDetailId());

            // 记录历史
            MtContainerLoadDetailHis mtContainerLoadDetailHis = new MtContainerLoadDetailHis();
            BeanUtils.copyProperties(mtContainerLoadDetail, mtContainerLoadDetailHis);
            mtContainerLoadDetailHis.setEventId(dto.getEventId());
            mtContainerLoadDetailHis.setTrxLoadQty(mtContainerLoadDetail.getLoadQty());
            mtContainerLoadDetailHis.setTenantId(tenantId);
            mtContainerLoadDetailHisRepository.insertSelective(mtContainerLoadDetailHis);
            result.setContainerLoadDetailHisId(mtContainerLoadDetailHis.getContainerLoadDetailHisId());
        } else {
            // 第四步
            if (dto.getLoadQty() != null) {
                // 计算数量变更结果
                trxLoadQty = BigDecimal.valueOf(dto.getLoadQty())
                                .subtract(BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty())).doubleValue();
                if (isEOType) {
                    mtContainerLoadDetail.setLoadQty(dto.getLoadQty());
                }
            } else {
                // 计算数量变更
                trxLoadQty = dto.getTrxLoadQty();
                mtContainerLoadDetail.setLoadQty(BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty())
                                .add(BigDecimal.valueOf(dto.getTrxLoadQty())).doubleValue());
            }

            if (isEOType) {
                // LOAD_EO_STEP_ACTUAL_ID：存入为输入参数（仅当loadObjectType=EO时传入，否则不更新）
                mtContainerLoadDetail.setLoadEoStepActualId(dto.getLoadEoStepActualId());
            }

            mtContainerLoadDetail.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(mtContainerLoadDetail);

            // 更新后LOAD_QTY为负，则返回报错消息
            if (BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                throw new MtException("MT_MATERIAL_LOT_0084", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0084", "MATERIAL_LOT", "【API：containerLoadDetailUpdate】"));
            }

            result.setContainerLoadDetailId(mtContainerLoadDetail.getContainerLoadDetailId());

            // 记录历史
            MtContainerLoadDetailHis mtContainerLoadDetailHis = new MtContainerLoadDetailHis();
            BeanUtils.copyProperties(mtContainerLoadDetail, mtContainerLoadDetailHis);
            mtContainerLoadDetailHis.setEventId(dto.getEventId());
            mtContainerLoadDetailHis.setTrxLoadQty(trxLoadQty);
            mtContainerLoadDetailHis.setTenantId(tenantId);
            mtContainerLoadDetailHisRepository.insertSelective(mtContainerLoadDetailHis);
            result.setContainerLoadDetailHisId(mtContainerLoadDetailHis.getContainerLoadDetailHisId());

            // 更新数据后，QTY为0时，记录历史并删除该条数据
            if (BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty()).compareTo(BigDecimal.ZERO) == 0) {
                mtContainerLoadDetailMapper.delete(mtContainerLoadDetail);
            }
        }
        /**
         *
         * add by peng.yuan 2019/11/11 20:00
         */
        // 第4.1步，判断输入参数loadObjectType输入情况
        if ("MATERIAL_LOT".equalsIgnoreCase(dto.getLoadObjectType())) {
            MtMaterialLotVO2 vo2 = new MtMaterialLotVO2();
            vo2.setMaterialLotId(dto.getLoadObjectId());
            vo2.setEventId(dto.getEventId());
            vo2.setCurrentContainerId(dto.getContainerId());
            mtMaterialLotRepository.materialLotUpdate(tenantId, vo2, "N");
        } else if ("CONTAINER".equalsIgnoreCase(dto.getLoadObjectType())) {
            // i.调用API{ containerLimitContainerQuery }，查询该容器下的所有容器：
            MtContLoadDtlVO10 vo10 = new MtContLoadDtlVO10();
            vo10.setContainerId(dto.getLoadObjectId());
            List<MtContLoadDtlVO3> vo3s = this.containerLimitContainerQuery(tenantId, vo10);
            if (CollectionUtils.isNotEmpty(vo3s)) {
                // ii. 若获取到的loadContainerId列表不为空
                List<String> loadContainerIdList =
                                vo3s.stream().map(MtContLoadDtlVO3::getLoadContainerId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(loadContainerIdList)) {
                    MtContainer container = new MtContainer();
                    container.setContainerId(dto.getContainerId());
                    container.setTenantId(tenantId);
                    MtContainer mtContainer = mtContainerMapper.selectOne(container);
                    if (mtContainer != null) {
                        if (StringUtils.isNotEmpty(mtContainer.getTopContainerId())) {
                            topContainerId = mtContainer.getTopContainerId();
                        } else {
                            topContainerId = dto.getContainerId();
                        }
                    }
                }

                // 若获取到的loadContainerId列表不为空，则根据获取到的loadContainerId批量更新容器的顶层容器
                String finalTopContainerId = topContainerId;
                // 组装容器集合
                List<MtContainerVO32> containerList = vo3s.stream().map(t -> {
                    MtContainerVO32 container = new MtContainerVO32();
                    container.setContainerId(t.getLoadContainerId());
                    container.setTopContainerId(finalTopContainerId);
                    return container;
                }).collect(Collectors.toList());

                MtContainerVO29 containerVO29 = new MtContainerVO29();
                containerVO29.setEventId(dto.getEventId());
                containerVO29.setContainerList(containerList);
                // 批量更新
                mtContainerRepository.containerBatchUpdate(tenantId, containerVO29, "N");
            }

            // iii.调用API{containerLimitMaterialLotQuery}，查询该容器下的所有物料批：
            MtContLoadDtlVO10 loadDtlVO10 = new MtContLoadDtlVO10();
            loadDtlVO10.setContainerId(dto.getLoadObjectId());
            List<MtContLoadDtlVO4> loadDtlVO4s = this.containerLimitMaterialLotQuery(tenantId, loadDtlVO10);
            if (CollectionUtils.isNotEmpty(loadDtlVO4s)) {
                // vi.若获取到的materialLotId列表不为空，则根据输入参数containerId在表MT_CONTAINER中限制CONTAINER_ID字段获取到topContainerId为TOP_CONTAINER_ID字段
                List<String> materialLotIdList = loadDtlVO4s.stream().map(MtContLoadDtlVO4::getMaterialLotId)
                                .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    MtContainer container = new MtContainer();
                    container.setContainerId(dto.getContainerId());
                    container.setTenantId(tenantId);
                    MtContainer mtContainer = mtContainerMapper.selectOne(container);
                    if (mtContainer != null) {
                        if (StringUtils.isNotEmpty(mtContainer.getTopContainerId())) {
                            topContainerId = mtContainer.getTopContainerId();
                        } else {
                            topContainerId = dto.getContainerId();
                        }
                    }
                }

                // 若获取到的materialLotId列表不为空，则根据获取到的materialLotId批量更新物料批的顶层容器
                String finalTopContainerId1 = topContainerId;

                List<MtMaterialLotVO20> materialLotVO20s = loadDtlVO4s.stream().map(t -> {
                    MtMaterialLotVO20 lotVO20 = new MtMaterialLotVO20();
                    lotVO20.setMaterialLotId(t.getMaterialLotId());
                    lotVO20.setTopContainerId(finalTopContainerId1);
                    return lotVO20;
                }).collect(Collectors.toList());
                // 批量更新
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotVO20s, dto.getEventId(),
                                MtBaseConstants.NO);
            }

            // v.调用API{ containerUpdate }更新容器的当前容器和顶层容器：
            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setContainerId(dto.getLoadObjectId());
            mtContainerVO12.setEventId(dto.getEventId());
            mtContainerVO12.setCurrentContainerId(dto.getContainerId());
            mtContainerRepository.containerUpdate(tenantId, mtContainerVO12, "N");
        }

        return result;
    }

    /**
     * containerLoadDetailDelete -删除容器装载明细
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerLoadDetailDelete(Long tenantId, MtContLoadDtlVO9 dto) {

        if (dto == null || StringUtils.isEmpty(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerLoadDetailDelete】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", "【API：containerLoadDetailDelete】"));
        }
        if (StringUtils.isEmpty(dto.getLoadObjectType())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "loadObjectType", "【API：containerLoadDetailDelete】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：containerLoadDetailDelete】"));
        }

        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setLoadObjectId(dto.getLoadObjectId());
        mtContainerLoadDetail.setContainerId(dto.getContainerId());
        mtContainerLoadDetail.setLoadObjectType(dto.getLoadObjectType());
        mtContainerLoadDetail.setLocationRow(dto.getLocationRow());
        mtContainerLoadDetail.setLocationColumn(dto.getLocationColumn());
        mtContainerLoadDetail.setTenantId(tenantId);
        List<MtContainerLoadDetail> loadDetails =
                        mtContainerLoadDetailMapper.selectDeleteUnique(tenantId, mtContainerLoadDetail);
        if (CollectionUtils.isEmpty(loadDetails)) {
            throw new MtException("MT_MATERIAL_LOT_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0040", "MATERIAL_LOT", "【API：containerLoadDetailDelete】"));
        }
        // 2.1步判断输入参数loadObjectType输入情况
        if ("MATERIAL_LOT".equals(dto.getLoadObjectType())) {
            // 若loadObjectType=MATERIAL_LOT，则调用API{materialLotUpdate}
            MtMaterialLotVO2 vo2 = new MtMaterialLotVO2();
            vo2.setMaterialLotId(dto.getLoadObjectId());
            vo2.setEventId(dto.getEventId());
            vo2.setCurrentContainerId("");
            mtMaterialLotRepository.materialLotUpdate(tenantId, vo2, "N");
        } else if ("CONTAINER".equals(dto.getLoadObjectType())) {
            // 调用API{ containerLimitContainerQuery }，查询该容器下的所有容器
            MtContLoadDtlVO10 vo10 = new MtContLoadDtlVO10();
            vo10.setContainerId(dto.getLoadObjectId());
            List<MtContLoadDtlVO3> vo3s = this.containerLimitContainerQuery(tenantId, vo10);
            if (CollectionUtils.isNotEmpty(vo3s)) {
                // 若获取到的loadContainerId列表不为空，则根据获取到的loadContainerId依次调用API{ containerUpdate }更新容器的顶层容器
                List<MtContainerVO32> mtContainers = vo3s.stream().map(t -> {
                    MtContainerVO32 mtContainer = new MtContainerVO32();
                    mtContainer.setContainerId(t.getLoadContainerId());
                    mtContainer.setTopContainerId(dto.getLoadObjectId());
                    return mtContainer;
                }).collect(Collectors.toList());
                MtContainerVO29 mtContainerVO29 = new MtContainerVO29();
                mtContainerVO29.setEventId(dto.getEventId());
                mtContainerVO29.setContainerList(mtContainers);
                mtContainerRepository.containerBatchUpdate(tenantId, mtContainerVO29, "N");
            }
            // 调用API{containerLimitMaterialLotQuery}，查询该容器下的所有物料批
            MtContLoadDtlVO10 loadDtlVO10 = new MtContLoadDtlVO10();
            loadDtlVO10.setContainerId(dto.getLoadObjectId());
            List<MtContLoadDtlVO4> loadDtlVO4s = this.containerLimitMaterialLotQuery(tenantId, loadDtlVO10);
            if (CollectionUtils.isNotEmpty(loadDtlVO4s)) {
                // 若获取到的materialLotId列表不为空，则根据获取到的materialLotId依次调用API{ materialLotUpdate }，更新物料批的顶层容器
                List<MtMaterialLotVO20> mtMaterialLotVO20s = loadDtlVO4s.stream().map(t -> {
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(t.getMaterialLotId());
                    mtMaterialLotVO20.setTopContainerId(dto.getLoadObjectId());
                    return mtMaterialLotVO20;
                }).collect(Collectors.toList());

                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20s, dto.getEventId(),
                                MtBaseConstants.NO);
            }

            // v.调用API{ containerUpdate }更新当前容器和顶层容器
            MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
            mtContainerVO12.setContainerId(dto.getLoadObjectId());
            mtContainerVO12.setEventId(dto.getEventId());
            mtContainerVO12.setCurrentContainerId("");
            mtContainerVO12.setTopContainerId("");
            mtContainerRepository.containerUpdate(tenantId, mtContainerVO12, "N");
        }
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        loadDetails.forEach(t -> {
            // 添加删除主表数据sql
            t.setTenantId(tenantId);
            sqlList.addAll(customDbRepository.getDeleteSql(t));

            MtContainerLoadDetailHis mtContainerLoadDetailHis = new MtContainerLoadDetailHis();
            BeanUtils.copyProperties(t, mtContainerLoadDetailHis);
            mtContainerLoadDetailHis.setTenantId(tenantId);
            mtContainerLoadDetailHis.setContainerLoadDetailHisId(
                            customDbRepository.getNextKey("mt_container_load_detail_his_s"));
            mtContainerLoadDetailHis
                            .setCid(Long.valueOf(customDbRepository.getNextKey("mt_container_load_detail_his_cid_s")));
            mtContainerLoadDetailHis.setEventId(dto.getEventId());
            mtContainerLoadDetailHis.setCreatedBy(userId);
            mtContainerLoadDetailHis.setCreationDate(now);
            mtContainerLoadDetailHis.setLastUpdatedBy(userId);
            mtContainerLoadDetailHis.setLastUpdateDate(now);
            mtContainerLoadDetailHis.setObjectVersionNumber(1L);
            // 添加生成历史表数据sql
            sqlList.addAll(customDbRepository.getInsertSql(mtContainerLoadDetailHis));
        });

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void containerLoadDetailBatchDelete(Long tenantId, MtContLoadDtlVO31 dto) {
        final String apiName = "【API:containerLoadDetailBatchDelete】";

        // 1. 验证参数有效性
        if (dto == null || MtIdHelper.isIdNull(dto.getContainerId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getUnloadObjectList())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "unloadObjectList", apiName));
        }
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", apiName));
        }
        if (dto.getUnloadObjectList().stream().anyMatch(t -> MtIdHelper.isIdNull(t.getLoadObjectId()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectId", apiName));
        }
        if (dto.getUnloadObjectList().stream().anyMatch(t -> StringUtils.isEmpty(t.getLoadObjectType()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "loadObjectType", apiName));
        }

        // 2. 批量查询装载明细信息
        SecurityTokenHelper.close();
        List<MtContainerLoadDetail> loadDetails = mtContainerLoadDetailMapper.selectByCondition(Condition
                        .builder(MtContainerLoadDetail.class)
                        .andWhere(Sqls.custom().andEqualTo(MtContainerLoadDetail.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtContainerLoadDetail.FIELD_CONTAINER_ID, dto.getContainerId())
                                        .andEqualTo(MtContainerLoadDetail.FIELD_LOCATION_ROW, dto.getLocationRow())
                                        .andEqualTo(MtContainerLoadDetail.FIELD_LOCATION_COLUMN,
                                                        dto.getLocationColumn())
                                        .andIn(MtContainerLoadDetail.FIELD_LOAD_OBJECT_ID,
                                                        dto.getUnloadObjectList().stream()
                                                                        .map(MtContLoadDtlVO13::getLoadObjectId)
                                                                        .distinct().collect(Collectors.toList()))
                                        .andIn(MtContainerLoadDetail.FIELD_LOAD_OBJECT_TYPE,
                                                        dto.getUnloadObjectList().stream()
                                                                        .map(MtContLoadDtlVO13::getLoadObjectType)
                                                                        .distinct().collect(Collectors.toList())))
                        .build());
        if (CollectionUtils.isEmpty(loadDetails)) {
            throw new MtException("MT_MATERIAL_LOT_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0040", "MATERIAL_LOT", apiName));
        }

        // 3 筛选卸载对象为 materialLot/container 的数据
        List<MtContLoadDtlVO13> materialLotObjectList = dto.getUnloadObjectList().stream()
                        .filter(t -> MtBaseConstants.LOAD_TYPE.MATERIAL_LOT.equals(t.getLoadObjectType()))
                        .collect(Collectors.toList());

        List<MtContLoadDtlVO13> containerObjectList = dto.getUnloadObjectList().stream()
                        .filter(t -> MtBaseConstants.LOAD_TYPE.CONTAINER.equals(t.getLoadObjectType()))
                        .collect(Collectors.toList());

        // 3.1 卸载对象为 materialLot 的数据执行数据更新
        if (CollectionUtils.isNotEmpty(materialLotObjectList)) {
            List<MtMaterialLotVO20> materialLotList = new ArrayList<>(materialLotObjectList.size());
            for (MtContLoadDtlVO13 materialLotOject : materialLotObjectList) {
                MtMaterialLotVO20 materialLot = new MtMaterialLotVO20();
                materialLot.setMaterialLotId(materialLotOject.getLoadObjectId());
                materialLot.setCurrentContainerId(MtBaseConstants.LONG_SPECIAL);
                materialLotList.add(materialLot);
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, dto.getEventId(),
                            MtBaseConstants.NO);
        }

        // 3.2 处理卸载对象为 container 的数据
        if (CollectionUtils.isNotEmpty(containerObjectList)) {
            List<MtContainerVO32> containerBatchUpdateList = new ArrayList<>(containerObjectList.size());
            List<MtContLoadDtlVO> batchQuery = new ArrayList<>();
            for (MtContLoadDtlVO13 containerObject : containerObjectList) {
                // 执行container更新对象
                MtContainerVO32 containerUpdateVO = new MtContainerVO32();
                containerUpdateVO.setContainerId(containerObject.getLoadObjectId());
                containerUpdateVO.setCurrentContainerId(MtBaseConstants.LONG_SPECIAL);
                containerUpdateVO.setTopContainerId(MtBaseConstants.LONG_SPECIAL);
                containerBatchUpdateList.add(containerUpdateVO);

                // 查询该容器装载对象集合
                MtContLoadDtlVO query = new MtContLoadDtlVO();
                query.setContainerId(containerObject.getLoadObjectId());
                query.setLoadObjectType(MtBaseConstants.LOAD_TYPE.CONTAINER);
                query.setLocationRow(dto.getLocationRow());
                query.setLocationColumn(dto.getLocationColumn());
                query.setAllLevelFlag(MtBaseConstants.YES);
                batchQuery.add(query);

                query = new MtContLoadDtlVO();
                query.setContainerId(containerObject.getLoadObjectId());
                query.setLoadObjectType(MtBaseConstants.LOAD_TYPE.MATERIAL_LOT);
                query.setLocationRow(dto.getLocationRow());
                query.setLocationColumn(dto.getLocationColumn());
                query.setAllLevelFlag(MtBaseConstants.YES);
                batchQuery.add(query);
            }

            // 3.2.1 调用容器批量更新，更新当前容器和顶层容器
            MtContainerVO29 containerBatchUpdate = new MtContainerVO29();
            containerBatchUpdate.setEventId(dto.getEventId());
            containerBatchUpdate.setContainerList(containerBatchUpdateList);
            mtContainerRepository.containerBatchUpdate(tenantId, containerBatchUpdate, "N");

            // 3.2.2 查询装载的该容器下，装载的所有container和materialLot
            List<MtContLoadDtlVO21> containerLoadDetailList = containerLimitObjectBatchQuery(tenantId, batchQuery);
            if (CollectionUtils.isNotEmpty(containerLoadDetailList)) {
                // 对查询结果执行 materialLot/container 的更新
                List<MtContainerVO32> loadContainerBatchUpdateList = new ArrayList<>();
                List<MtMaterialLotVO20> materialLotBatchUpdateList = new ArrayList<>();
                for (MtContLoadDtlVO21 containerLoadDetail : containerLoadDetailList) {
                    List<MtContLoadDtlVO6> loadDetailList = containerLoadDetail.getLoadDetailList();
                    for (MtContLoadDtlVO6 loadDetail : loadDetailList) {
                        if (MtBaseConstants.LOAD_TYPE.CONTAINER.equals(loadDetail.getLoadObjectType())) {
                            MtContainerVO32 containerUpateVO = new MtContainerVO32();
                            containerUpateVO.setContainerId(loadDetail.getLoadObjectId());
                            containerUpateVO.setTopContainerId(loadDetail.getContainerId());
                            loadContainerBatchUpdateList.add(containerUpateVO);
                        }
                        if (MtBaseConstants.LOAD_TYPE.MATERIAL_LOT.equals(loadDetail.getLoadObjectType())) {
                            MtMaterialLotVO20 materialLotUpdateVO = new MtMaterialLotVO20();
                            materialLotUpdateVO.setMaterialLotId(loadDetail.getLoadObjectId());
                            materialLotUpdateVO.setTopContainerId(loadDetail.getContainerId());
                            materialLotBatchUpdateList.add(materialLotUpdateVO);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(loadContainerBatchUpdateList)) {
                    MtContainerVO29 mtContainerVO29 = new MtContainerVO29();
                    mtContainerVO29.setEventId(dto.getEventId());
                    mtContainerVO29.setContainerList(loadContainerBatchUpdateList);
                    mtContainerRepository.containerBatchUpdate(tenantId, mtContainerVO29, MtBaseConstants.NO);
                }
                if (CollectionUtils.isNotEmpty(materialLotBatchUpdateList)) {
                    mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotBatchUpdateList,
                                    dto.getEventId(), MtBaseConstants.NO);
                }
            }
        }

        // 4. 筛选满足传入参数的装载明细数据，执行删除
        List<MtContainerLoadDetail> matchInputLoadDetailList = loadDetails.stream()
                        .filter(t -> dto.getUnloadObjectList()
                                        .contains(new MtContLoadDtlVO13(t.getLoadObjectId(), t.getLoadObjectType())))
                        .collect(Collectors.toList());
        customDbRepository.batchDeleteTarzan(matchInputLoadDetailList.stream()
                        .map(MtContainerLoadDetail::getContainerLoadDetailId).collect(Collectors.toList()),
                        MtContainerLoadDetail.class);
    }

    /**
     * containerIsEmptyValidate -验证容器是否未装载任何对象
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return void
     */
    @Override
    public void containerIsEmptyValidate(Long tenantId, String containerId) {

        if (StringUtils.isEmpty(containerId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "containerId", "【API：containerIsEmptyValidate】"));
        }

        // 第二步 获取容器数据,校验容器是否存在
        MtContainer mtContainer = mtContainerRepository.containerPropertyGet(tenantId, containerId);
        if (mtContainer == null) {
            throw new MtException("MT_MATERIAL_LOT_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0029", "MATERIAL_LOT", "【API：containerIsEmptyValidate】"));
        }

        MtContLoadDtlVO mtContainerLoadDetailVO = new MtContLoadDtlVO();
        mtContainerLoadDetailVO.setContainerId(containerId);
        List<MtContLoadDtlVO6> mtContainerLoadDetails = containerLimitObjectQuery(tenantId, mtContainerLoadDetailVO);
        // 容器是否已经装载
        if (mtContainerLoadDetails != null && mtContainerLoadDetails.size() > 0) {
            throw new MtException("MT_MATERIAL_LOT_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0041", "MATERIAL_LOT", "【API：containerIsEmptyValidate】"));

        }

    }

    /**
     * eoLimitLoadingEoQtyGet-获取执行作业已装载数量
     *
     * @param tenantId
     * @param eoId
     * @return java.lang.Double
     * @author chuang.yang
     * @date 2019/6/13
     */
    @Override
    public Double eoLimitLoadingEoQtyGet(Long tenantId, String eoId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eoId", "【API：eoLimitLoadingEoQtyGet】"));
        }

        // 2. 获取指定执行作业的已装载数量
        MtContainerLoadDetail detail = new MtContainerLoadDetail();
        detail.setLoadObjectId(eoId);
        detail.setLoadObjectType("EO");
        detail.setTenantId(tenantId);
        List<MtContainerLoadDetail> mtContainerLoadDetails = mtContainerLoadDetailMapper.select(detail);
        if (CollectionUtils.isEmpty(mtContainerLoadDetails)) {
            return 0.0D;
        }

        // 3. 汇总数量输出
        BigDecimal sumLoadQty = mtContainerLoadDetails.stream().collect(CollectorsUtil
                        .summingBigDecimal(c -> BigDecimal.valueOf(c.getLoadQty() == null ? 0.0D : c.getLoadQty())));
        return sumLoadQty.doubleValue();
    }


    @Override
    public List<MtContLoadDtlVO20> eoLimitLoadingEoQtyBatchGet(Long tenantId, List<String> eoIdList) {
        List<MtContLoadDtlVO20> resultList = new ArrayList<>();
        // 第一步：判断输入参数是否合规
        if (CollectionUtils.isEmpty(eoIdList)) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eoId", "【API：eoLimitLoadingEoQtyBatchGet】"));
        }

        // 第二步，从MT_CONTAINER_LOAD_DETAIL表中获取指定执行作业的已装载数量
        List<MtContainerLoadDetail> loadDetails =
                        mtContainerLoadDetailMapper.selectByLoadObject(tenantId, "EO", eoIdList);

        Map<String, List<MtContainerLoadDetail>> listMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(loadDetails)) {
            listMap = loadDetails.stream().collect(Collectors.groupingBy(MtContainerLoadDetail::getLoadObjectId));
        }

        // 第三步，根据eoId汇总第二步所求结果LOAD_QTY传出为loadQty；若对应eoId第二步获取LOAD_QTY为空
        for (String eoId : eoIdList) {
            MtContLoadDtlVO20 result = new MtContLoadDtlVO20();
            result.setEoId(eoId);
            List<MtContainerLoadDetail> details = listMap.get(eoId);
            if (CollectionUtils.isEmpty(details)) {
                result.setLoadQty(0.0d);
            } else {
                BigDecimal sumLoadQty = details.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> BigDecimal.valueOf(c.getLoadQty() == null ? 0.0D : c.getLoadQty())));
                result.setLoadQty(sumLoadQty.doubleValue());
            }
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 根据转载id集合，批量查询容器装载数据
     *
     * @param tenantId
     * @param loadObjectType
     * @param loadObjectIds
     * @return java.util.List<hmes.container_load_detail.dto.MtContainerLoadDetail>
     * @author chuang.yang
     * @date 2019/10/14
     */
    @Override
    public List<MtContainerLoadDetail> loadObjectLimitBatchGet(Long tenantId, String loadObjectType,
                    List<String> loadObjectIds) {
        return mtContainerLoadDetailMapper.selectByLoadObject(tenantId, loadObjectType, loadObjectIds);
    }

    /**
     * 根据containerId装载的物料批和容器
     *
     * @param tenantId
     * @param containerId
     * @return java.util.List<hmes.container_load_detail.dto.MtContainerLoadDetail>
     * @author chuang.yang
     * @date 2019/10/15
     */
    @Override
    public List<MtContainerLoadDetail> containerLimitMaterialLotAndContainer(Long tenantId, String containerId) {
        return mtContainerLoadDetailMapper.selectMaterialLotAndContainerByContainer(tenantId, containerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtContLoadDtlVO11> containerLoadDetailBatchUpdate(Long tenantId, MtContLoadDtlVO12 dto,
                    String fullUpdate) {
        if (CollectionUtils.isEmpty(dto.getMtContLoadDtlList())) {
            return Collections.emptyList();
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "eventId", "【API：containerLoadDetailBatchUpdate】"));
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        List<String> sqlList = new ArrayList<>();
        List<MtContLoadDtlVO11> result = new ArrayList<>();

        // 获取containerId
        List<String> containerId = dto.getMtContLoadDtlList().stream().map(MtContLoadDtlVO16::getContainerId)
                        .collect(Collectors.toList());

        // 获取容器类型
        List<MtContainerTypeAttrVO4> mtContainerTypes =
                        mtContainerTypeMapper.selectByContainerId(tenantId, containerId);
        Map<String, List<MtContainerTypeAttrVO4>> mtContainerTypesMap = mtContainerTypes.stream()
                        .collect(Collectors.groupingBy(MtContainerTypeAttrVO4::getContainerId));

        // 获取装载对象
        List<MtContLoadDtlVO13> mtContLoadDtlVO13s = dto.getMtContLoadDtlList().stream()
                        .map(t -> new MtContLoadDtlVO13(t.getLoadObjectId(), t.getLoadObjectType()))
                        .collect(Collectors.toList());

        List<MtContainerLoadDetail> mtContainerLoadDetails =
                        mtContainerLoadDetailMapper.selectContainerLoadDetailList(tenantId, mtContLoadDtlVO13s);
        Map<MtContLoadDtlVO13, List<MtContainerLoadDetail>> loadDtlVO12ListMap =
                        mtContainerLoadDetails.stream().collect(Collectors.groupingBy(
                                        t -> new MtContLoadDtlVO13(t.getLoadObjectId(), t.getLoadObjectType())));

        Map<String, List<MtContLoadDtlVO16>> listMap = dto.getMtContLoadDtlList().stream()
                        .collect(Collectors.groupingBy(MtContLoadDtlVO16::getContainerId));

        // 获取装载顺序
        List<MtContLoadDtlVO15> batchMaxSequence =
                        mtContainerLoadDetailMapper.getBatchMaxSequence(tenantId, containerId);
        Map<String, List<MtContLoadDtlVO15>> sequenceMap =
                        batchMaxSequence.stream().collect(Collectors.groupingBy(MtContLoadDtlVO15::getContainerId));

        // 获取id
        List<String> newIds = this.customDbRepository.getNextKeys("mt_container_load_detail_s",
                        dto.getMtContLoadDtlList().size());
        // 获取cid
        List<String> newCids = this.customDbRepository.getNextKeys("mt_container_load_detail_cid_s",
                        dto.getMtContLoadDtlList().size());

        // 获取id
        List<String> newHisIds = this.customDbRepository.getNextKeys("mt_container_load_detail_his_s",
                        dto.getMtContLoadDtlList().size());
        // 获取cid
        List<String> newHIsCids = this.customDbRepository.getNextKeys("mt_container_load_detail_cid_s",
                        dto.getMtContLoadDtlList().size());
        for (Map.Entry<String, List<MtContLoadDtlVO16>> c : listMap.entrySet()) {
            // 获取最大序列
            Long sequence = 0L;
            if (MapUtils.isNotEmpty(sequenceMap) && CollectionUtils.isNotEmpty(sequenceMap.get(c.getKey()))) {
                sequence = sequenceMap.get(c.getKey()).get(0).getLoadSequence();
            }
            for (MtContLoadDtlVO16 t : c.getValue()) {
                // 1.第一步：判断输入参数是否合规：
                if (StringUtils.isEmpty(t.getContainerId())) {
                    throw new MtException("MT_MATERIAL_LOT_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                    "MATERIAL_LOT", "containerId",
                                                    "【API：containerLoadDetailBatchUpdate】"));
                }

                if (StringUtils.isEmpty(t.getLoadObjectType())) {
                    throw new MtException("MT_MATERIAL_LOT_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                    "MATERIAL_LOT", "loadObjectType",
                                                    "【API：containerLoadDetailBatchUpdate】"));
                }
                if (StringUtils.isEmpty(t.getLoadObjectId())) {
                    throw new MtException("MT_MATERIAL_LOT_0001",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                                    "MATERIAL_LOT", "loadObjectId",
                                                    "【API：containerLoadDetailBatchUpdate】"));
                }
                if (t.getLoadQty() != null && t.getTrxLoadQty() != null) {
                    throw new MtException("MT_MATERIAL_LOT_0009",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0009",
                                                    "MATERIAL_LOT", "loadQty、trxLoadQty",
                                                    "【API：containerLoadDetailBatchUpdate】"));
                }

                // 获取容器对应的容器类型
                MtContainerTypeAttrVO4 mtContainerTypeAttrVO4s = null;
                if (CollectionUtils.isNotEmpty(mtContainerTypesMap.get(t.getContainerId()))) {
                    mtContainerTypeAttrVO4s = mtContainerTypesMap.get(t.getContainerId()).get(0);
                }
                if (mtContainerTypeAttrVO4s == null
                                || StringUtils.isEmpty(mtContainerTypeAttrVO4s.getContainerTypeId())) {
                    throw new MtException("MT_MATERIAL_LOT_0029",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0029",
                                                    "MATERIAL_LOT", "【API：containerLoadDetailBatchUpdate】"));
                }
                MtContLoadDtlVO11 mtContLoadDtlVO11 = new MtContLoadDtlVO11();

                // 获取容器类型启用位置管理标识locationEnableFlag、行数locationRow、列数
                String locationEnabledFlag = mtContainerTypeAttrVO4s.getLocationEnabledFlag();
                if ("Y".equals(locationEnabledFlag)) {
                    if (t.getLocationColumn() == null || t.getLocationRow() == null) {
                        throw new MtException("MT_MATERIAL_LOT_0027",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0027", "MATERIAL_LOT",
                                                        "【API：containerLoadDetailBatchUpdate】"));
                    }

                    if (BigDecimal.ZERO.compareTo(new BigDecimal(t.getLocationColumn().toString())) >= 0
                                    || new BigDecimal(t.getLocationColumn().toString()).compareTo(new BigDecimal(
                                                    mtContainerTypeAttrVO4s.getLocationColumn().toString())) == 1) {
                        throw new MtException("MT_MATERIAL_LOT_0038",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0038", "MATERIAL_LOT",
                                                        "【API：containerLoadDetailBatchUpdate】"));

                    }
                    if (BigDecimal.ZERO.compareTo(new BigDecimal(t.getLocationRow().toString())) >= 0
                                    || new BigDecimal(t.getLocationRow().toString()).compareTo(new BigDecimal(
                                                    mtContainerTypeAttrVO4s.getLocationRow().toString())) == 1) {
                        throw new MtException("MT_MATERIAL_LOT_0038",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0038", "MATERIAL_LOT",
                                                        "【API：containerLoadDetailBatchUpdate】"));
                    }
                }
                // 用来判断新增/更新逻辑
                MtContainerLoadDetail mtContainerLoadDetail = null;

                // 2.第二步，根据传入参数判断更新或创建容器装载明细
                boolean isEOType = "EO".equals(t.getLoadObjectType());
                // 判断装载象是否已被装载到其他容器上
                MtContLoadDtlVO13 detailCheck = new MtContLoadDtlVO13(t.getLoadObjectId(), t.getLoadObjectType());
                List<MtContainerLoadDetail> mtContainerLoadDetailList = loadDtlVO12ListMap.get(detailCheck);
                if (!isEOType) {
                    if (CollectionUtils.isNotEmpty(mtContainerLoadDetailList)) {
                        throw new MtException("MT_MATERIAL_LOT_0039",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0039", "MATERIAL_LOT",
                                                        "【API：containerLoadDetailBatchUpdate】"));
                    }
                } else {
                    // 根据以下限制判断新增或更新模式
                    if (CollectionUtils.isNotEmpty(mtContainerLoadDetailList)) {
                        List<MtContainerLoadDetail> details = mtContainerLoadDetailList.stream()
                                        .filter(k -> t.getContainerId().equalsIgnoreCase(k.getContainerId()))
                                        .collect(Collectors.toList());

                        if ("Y".equals(locationEnabledFlag)) {
                            details = mtContainerLoadDetailList.stream()
                                            .filter(k -> t.getLocationRow().equals(k.getLocationRow())
                                                            && t.getLocationColumn().equals(k.getLocationColumn()))
                                            .collect(Collectors.toList());
                        }

                        Optional<MtContainerLoadDetail> first = details.stream().findFirst();
                        if (first.isPresent()) {
                            mtContainerLoadDetail = first.get();
                            // 若获取到数据，loadQty和trxLoadQty参数只能有一个有值
                            if (t.getTrxLoadQty() == null && t.getLoadQty() == null) {
                                throw new MtException("MT_MATERIAL_LOT_0067",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MATERIAL_LOT_0067", "MATERIAL_LOT",
                                                                "【API：containerLoadDetailBatchUpdate】"));
                            }
                        } else {
                            // 若获取数据为空，loadQty和trxLoadQty参数必须有一个有值，且值必须大于0
                            if (t.getTrxLoadQty() == null && t.getLoadQty() == null || t.getLoadQty() != null
                                            && BigDecimal.valueOf(t.getLoadQty()).compareTo(BigDecimal.ZERO) <= 0
                                            || t.getTrxLoadQty() != null && BigDecimal.valueOf(t.getTrxLoadQty())
                                                            .compareTo(BigDecimal.ZERO) <= 0) {
                                throw new MtException("MT_MATERIAL_LOT_0085",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MATERIAL_LOT_0085", "MATERIAL_LOT",
                                                                "【API：containerLoadDetailBatchUpdate】"));
                            }
                        }
                    } else {
                        if (t.getTrxLoadQty() == null && t.getLoadQty() == null || t.getLoadQty() != null
                                        && BigDecimal.valueOf(t.getLoadQty()).compareTo(BigDecimal.ZERO) <= 0
                                        || t.getTrxLoadQty() != null && BigDecimal.valueOf(t.getTrxLoadQty())
                                                        .compareTo(BigDecimal.ZERO) <= 0) {
                            throw new MtException("MT_MATERIAL_LOT_0085",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0085", "MATERIAL_LOT",
                                                            "【API：containerLoadDetailBatchUpdate】"));
                        }
                    }
                }
                // 如果为null，则执行新增模式否则新增模式
                Double trxLoadQty = 0.0D;
                if (mtContainerLoadDetail == null) {
                    // 根据当前装载对象创建容器装载明细
                    mtContainerLoadDetail = new MtContainerLoadDetail();
                    mtContainerLoadDetail.setContainerId(t.getContainerId());
                    if ("Y".equals(locationEnabledFlag)) {
                        mtContainerLoadDetail.setLocationColumn(t.getLocationColumn());
                        mtContainerLoadDetail.setLocationRow(t.getLocationRow());
                    }
                    mtContainerLoadDetail.setLoadObjectType(t.getLoadObjectType());
                    mtContainerLoadDetail.setLoadObjectId(t.getLoadObjectId());
                    sequence += Long.valueOf(10L);
                    mtContainerLoadDetail.setLoadSequence(sequence);
                    if (isEOType) {
                        mtContainerLoadDetail.setLoadQty(t.getLoadQty() != null ? t.getLoadQty() : t.getTrxLoadQty());
                        mtContainerLoadDetail.setLoadEoStepActualId(t.getLoadEoStepActualId());
                    }
                    mtContainerLoadDetail.setTenantId(tenantId);
                    mtContainerLoadDetail.setCreatedBy(userId);
                    mtContainerLoadDetail.setCreationDate(now);
                    mtContainerLoadDetail.setLastUpdateDate(now);
                    mtContainerLoadDetail.setLastUpdatedBy(userId);
                    mtContainerLoadDetail.setObjectVersionNumber(1L);
                    mtContainerLoadDetail.setContainerLoadDetailId(newIds.get(result.size()));
                    mtContainerLoadDetail.setCid(Long.valueOf(newCids.get(result.size())));
                    sqlList.addAll(customDbRepository.getInsertSql(mtContainerLoadDetail));
                    trxLoadQty = mtContainerLoadDetail.getLoadQty();

                } else {
                    if (isEOType) {
                        if (t.getLoadQty() != null) {
                            // 计算数量变更结果
                            trxLoadQty = BigDecimal.valueOf(t.getLoadQty())
                                            .subtract(BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty()))
                                            .doubleValue();
                            mtContainerLoadDetail.setLoadQty(t.getLoadQty());
                        } else {
                            // 计算数量变更
                            trxLoadQty = t.getTrxLoadQty();
                            mtContainerLoadDetail.setLoadQty(BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty())
                                            .add(BigDecimal.valueOf(t.getTrxLoadQty())).doubleValue());
                        }
                        if (BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty()).compareTo(BigDecimal.ZERO) < 0) {
                            throw new MtException("MT_MATERIAL_LOT_0084",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_MATERIAL_LOT_0084", "MATERIAL_LOT",
                                                            "【API：containerLoadDetailBatchUpdate】"));
                        }
                        mtContainerLoadDetail.setLoadEoStepActualId(t.getLoadEoStepActualId());
                        mtContainerLoadDetail.setTenantId(tenantId);
                        mtContainerLoadDetail.setLastUpdateDate(now);
                        mtContainerLoadDetail.setLastUpdatedBy(userId);
                        mtContainerLoadDetail.setObjectVersionNumber(1L);
                        mtContainerLoadDetail.setCid(Long.valueOf(newCids.get(result.size())));
                        if (BigDecimal.valueOf(mtContainerLoadDetail.getLoadQty()).compareTo(BigDecimal.ZERO) == 0) {
                            sqlList.addAll(customDbRepository.getDeleteSql(mtContainerLoadDetail));
                        } else {
                            if (MtBaseConstants.NO.equalsIgnoreCase(fullUpdate)) {
                                sqlList.addAll(customDbRepository.getUpdateSql(mtContainerLoadDetail));
                            } else {
                                mtContainerLoadDetail = (MtContainerLoadDetail) ObjectFieldsHelper
                                                .setStringFieldsEmpty(mtContainerLoadDetail);
                                sqlList.addAll(customDbRepository.getFullUpdateSql(mtContainerLoadDetail));
                            }
                        }
                    }
                }
                // 记录历史
                MtContainerLoadDetailHis mtContainerLoadDetailHis = new MtContainerLoadDetailHis();
                BeanUtils.copyProperties(mtContainerLoadDetail, mtContainerLoadDetailHis);
                mtContainerLoadDetailHis.setEventId(dto.getEventId());
                mtContainerLoadDetailHis.setTrxLoadQty(trxLoadQty);
                mtContainerLoadDetailHis.setCreatedBy(userId);
                mtContainerLoadDetailHis.setCreationDate(now);
                mtContainerLoadDetailHis.setLastUpdateDate(now);
                mtContainerLoadDetailHis.setLastUpdatedBy(userId);
                mtContainerLoadDetailHis.setObjectVersionNumber(1L);
                mtContainerLoadDetailHis.setContainerLoadDetailHisId(newHisIds.get(result.size()));
                mtContainerLoadDetailHis.setCid(Long.valueOf(newHIsCids.get(result.size())));
                sqlList.addAll(customDbRepository.getInsertSql(mtContainerLoadDetailHis));

                mtContLoadDtlVO11.setContainerLoadDetailId(mtContainerLoadDetailHis.getContainerLoadDetailId());
                mtContLoadDtlVO11.setContainerLoadDetailHisId(mtContainerLoadDetailHis.getContainerLoadDetailHisId());
                result.add(mtContLoadDtlVO11);


            }
        }

        // 新增逻辑记录当前容器I-2019-11-21
        List<MtMaterialLotVO20> mtMaterialLotVO20s =
                        Collections.synchronizedList(new ArrayList<>(dto.getMtContLoadDtlList().size()));
        List<MtContainerVO32> containerList =
                        Collections.synchronizedList(new ArrayList<>(dto.getMtContLoadDtlList().size()));
        List<String> containerIds = Collections.synchronizedList(new ArrayList<>());
        List<String> topContainerIds = Collections.synchronizedList(new ArrayList<>());

        Map<String, String> containerMap =
                        Collections.synchronizedMap(new HashMap<>(dto.getMtContLoadDtlList().size()));
        Map<String, String> topContainerMap =
                        Collections.synchronizedMap(new HashMap<>(dto.getMtContLoadDtlList().size()));

        dto.getMtContLoadDtlList().parallelStream().forEach(t -> {
            if ("MATERIAL_LOT".equalsIgnoreCase(t.getLoadObjectType())) {
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(t.getLoadObjectId());
                mtMaterialLotVO20.setCurrentContainerId(t.getContainerId());
                mtMaterialLotVO20s.add(mtMaterialLotVO20);
            } else if ("CONTAINER".equalsIgnoreCase(t.getLoadObjectType())) {
                containerIds.add(t.getContainerId());
                topContainerIds.add(t.getLoadObjectId());
                containerMap.put(t.getLoadObjectId(), t.getContainerId());
            }
        });

        if (CollectionUtils.isNotEmpty(containerIds)) {
            // 容器是否为顶层容器
            List<MtContainer> mtContainers = mtContainerMapper.selectByContainerIds(tenantId,
                            containerIds.stream().distinct().collect(Collectors.toList()));
            mtContainers.parallelStream().forEach(t -> {
                topContainerMap.put(t.getContainerId(), StringUtils.isEmpty(t.getTopContainerId()) ? t.getContainerId()
                                : t.getTopContainerId());
            });
        }
        if (CollectionUtils.isNotEmpty(topContainerIds)) {
            // loadContainer更新
            List<MtContainer> mtLoadContainer = mtContainerMapper.selectByContainerIds(tenantId,
                            topContainerIds.stream().distinct().collect(Collectors.toList()));
            mtLoadContainer.parallelStream().forEach(t -> {
                MtContainerVO32 mtContainerVO32 = new MtContainerVO32();
                mtContainerVO32.setContainerId(t.getContainerId());
                mtContainerVO32.setCurrentContainerId(containerMap.get(t.getContainerId()));
                containerList.add(mtContainerVO32);
            });

            // 容器下顶层容器
            List<MtContainer> mtContainers = mtContainerMapper.selectByTopContainerIds(tenantId,
                            topContainerIds.stream().distinct().collect(Collectors.toList()));
            mtContainers.parallelStream().forEach(t -> {
                String contarinerId = containerMap.get(t.getTopContainerId());
                MtContainerVO32 mtContainerVO32 = new MtContainerVO32();
                mtContainerVO32.setContainerId(t.getContainerId());
                mtContainerVO32.setTopContainerId(topContainerMap.get(contarinerId));
                containerList.add(mtContainerVO32);
            });
            // 获取顶层容器下物料批
            List<MtMaterialLot> mtMaterialLots = mtMaterialLotMapper.selectByTopContainerIds(tenantId,
                            topContainerIds.stream().distinct().collect(Collectors.toList()));

            mtMaterialLots.parallelStream().forEach(c -> {
                String contarinerId = containerMap.get(c.getTopContainerId());
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(c.getMaterialLotId());
                mtMaterialLotVO20.setTopContainerId(topContainerMap.get(contarinerId));
                mtMaterialLotVO20s.add(mtMaterialLotVO20);
            });
        }

        // 更新物料批当前容器ID
        if (CollectionUtils.isNotEmpty(containerList)) {
            MtContainerVO29 mtContainerVO29 = new MtContainerVO29();
            mtContainerVO29.setEventId(dto.getEventId());
            mtContainerVO29.setContainerList(containerList);
            mtContainerRepository.containerBatchUpdate(tenantId, mtContainerVO29, MtBaseConstants.NO);
        }

        if (CollectionUtils.isNotEmpty(mtMaterialLotVO20s)) {
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20s, dto.getEventId(),
                            MtBaseConstants.NO);
        }


        // 更新容器下的顶层容器
        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return result;
    }

    @Override
    public List<MtContLoadDtlVO17> selectByMaterialLodIds(Long tenantId, List<String> sourceMaterialLotId) {
        return mtContainerLoadDetailMapper.selectByMaterialLodIds(tenantId, sourceMaterialLotId);
    }

    @Override
    public List<MtContLoadDtlVO19> containerLimitObjectBatchGet(Long tenantId, List<MtContLoadDtlVO18> voList) {
        // 1.判断输入参数是否合规
        List<String> containerIds = voList.stream().map(MtContLoadDtlVO18::getContainerId)
                        .filter(t -> StringUtils.isEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(containerIds)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerId ", "【API：containerLimitObjectBatchGet】"));
        }

        List<String> typeList = voList.stream().map(MtContLoadDtlVO18::getLoadObjectType)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(typeList)) {
            // 类型判断
            List<String> targetTypes = Arrays.asList("EO", "MATERIAL_LOT", "CONTAINER");
            for (String type : typeList) {
                if (!targetTypes.contains(type)) {
                    throw new MtException("MT_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_LOT_0003", "MATERIAL_LOT", "loadObjectType",
                                    "[‘EO’、‘MATERIAL_LOT’、‘CONTAINER’]", "【API:containerLimitObjectBatchGet】"));
                }
            }
        }

        List<MtContLoadDtlVO19> resultList = new ArrayList<>();

        // 2.根据输入参数从表MT_CONTAINER_LOAD_DETAIL中获取数据
        for (MtContLoadDtlVO18 mtContLoadDtlVO18 : voList) {
            resultList.addAll(mtContainerLoadDetailMapper.selectByIdAndType(tenantId, mtContLoadDtlVO18));
        }
        return resultList;
    }
}
