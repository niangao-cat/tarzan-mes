package tarzan.method.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomReferencePoint;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomHisRepository;
import tarzan.method.domain.repository.MtBomReferencePointRepository;
import tarzan.method.domain.vo.MtBomHisVO1;
import tarzan.method.domain.vo.MtBomReferencePointVO;
import tarzan.method.domain.vo.MtBomReferencePointVO2;
import tarzan.method.domain.vo.MtBomReferencePointVO3;
import tarzan.method.infra.mapper.MtBomReferencePointMapper;

/**
 * 装配清单行参考点关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomReferencePointRepositoryImpl extends BaseRepositoryImpl<MtBomReferencePoint>
                implements MtBomReferencePointRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtBomReferencePointMapper mtBomReferencePointMapper;

    @Override
    public MtBomReferencePoint bomReferencePointBasicGet(Long tenantId, String bomReferencePointId) {
        if (StringUtils.isEmpty(bomReferencePointId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomReferencePointId", "【API:bomReferencePointBasicGet】"));
        }
        MtBomReferencePoint referencePoint = new MtBomReferencePoint();
        referencePoint.setTenantId(tenantId);
        referencePoint.setBomReferencePointId(bomReferencePointId);
        return this.mtBomReferencePointMapper.selectOne(referencePoint);
    }

    @Override
    public List<Map<String, String>> componentLimitBomReferencePointQuery(Long tenantId, String bomComponentId) {
        if (StringUtils.isEmpty(bomComponentId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:componentLimitBomReferencePointQuery】"));
        }

        MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
        mtBomReferencePoint.setTenantId(tenantId);
        mtBomReferencePoint.setBomComponentId(bomComponentId);
        List<MtBomReferencePoint> mtBomReferencePoints = this.mtBomReferencePointMapper.select(mtBomReferencePoint);
        if (CollectionUtils.isEmpty(mtBomReferencePoints)) {
            return Collections.emptyList();
        }

        final List<Map<String, String>> list = new ArrayList<>();
        mtBomReferencePoints.stream().forEach(t -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bomComponentId", t.getBomComponentId());
            map.put("bomReferencePointId", t.getBomReferencePointId());
            list.add(map);
        });
        return list;
    }

    @Override
    public List<MtBomReferencePointVO2> propertyLimitBomReferencePointQuery(Long tenantId,
                    MtBomReferencePointVO condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_BOM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0002", "BOM", "【API:propertyLimitBomReferencePointQuery】"));
        }

        List<MtBomReferencePointVO2> list = this.mtBomReferencePointMapper.selectConditionCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list;
    }

    @Override
    public List<MtBomReferencePoint> bomReferencePointBasicBatchGet(Long tenantId, List<String> bomReferencePointIds) {
        if (CollectionUtils.isEmpty(bomReferencePointIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomReferencePointId", "【API:bomReferencePointBasicBatchGet】"));
        }
        return this.mtBomReferencePointMapper.selectByIdsCustom(tenantId, bomReferencePointIds);
    }

    @Override
    public List<MtBomReferencePoint> bomComponentReferencePointQuery(Long tenantId, MtBomReferencePointVO3 dto) {
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomComponentReferencePointQuery】"));
        }

        MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
        mtBomReferencePoint.setTenantId(tenantId);
        mtBomReferencePoint.setBomComponentId(dto.getBomComponentId());
        mtBomReferencePoint.setEnableFlag("Y");
        return mtBomReferencePointMapper.select(mtBomReferencePoint);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomReferencePointUpdate(Long tenantId, MtBomReferencePoint dto) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String bomReferencePointId = dto.getBomReferencePointId();
        if (StringUtils.isEmpty(bomReferencePointId)) {
            if (StringUtils.isEmpty(dto.getBomComponentId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomReferencePointUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getReferencePoint())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "referencePoint", "【API:bomReferencePointUpdate】"));
            }
            if (dto.getQty() == null) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "qty", "【API:bomReferencePointUpdate】"));
            } else {
                if (!NumberHelper.isSixDecimal(dto.getQty().toString())) {
                    throw new MtException("MT_BOM_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0047", "BOM", "【API:bomReferencePointUpdate】"));
                }
            }
            if (dto.getLineNumber() == null) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "lineNumber", "【API:bomReferencePointUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "enableFlag", "【API:bomReferencePointUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0053", "BOM", "【API:bomReferencePointUpdate】"));
            }

            MtBomComponent mtBomComponent =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, dto.getBomComponentId());
            if (null == mtBomComponent) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomReferencePointUpdate】"));
            }

            MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
            mtBomReferencePoint.setTenantId(tenantId);
            mtBomReferencePoint.setBomComponentId(dto.getBomComponentId());
            mtBomReferencePoint.setReferencePoint(dto.getReferencePoint());
            mtBomReferencePoint = this.mtBomReferencePointMapper.selectOne(mtBomReferencePoint);
            if (null != mtBomReferencePoint) {
                throw new MtException("MT_BOM_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0030", "BOM", "【API:bomReferencePointUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);

            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(mtBomComponent.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_REFERENCE_POINT_CREATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);

            bomReferencePointId = dto.getBomReferencePointId();
        } else {
            if (null != dto.getBomComponentId() && "".equals(dto.getBomComponentId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomReferencePointUpdate】"));
            }
            if (null != dto.getReferencePoint() && "".equals(dto.getReferencePoint())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "referencePoint", "【API:bomReferencePointUpdate】"));
            }
            if (null != dto.getQty() && !NumberHelper.isSixDecimal(dto.getQty().toString())) {
                throw new MtException("MT_BOM_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0047", "BOM", "【API:bomReferencePointUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "enableFlag", "【API:bomReferencePointUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_BOM_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0053", "BOM", "【API:bomReferencePointUpdate】"));
            }

            MtBomReferencePoint mtBomReferencePoint = bomReferencePointBasicGet(tenantId, dto.getBomReferencePointId());
            if (null == mtBomReferencePoint) {
                throw new MtException("MT_BOM_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0039", "BOM", "【API:bomReferencePointUpdate】"));
            }

            String bomComponentId = null == dto.getBomComponentId() ? mtBomReferencePoint.getBomComponentId()
                            : dto.getBomComponentId();
            MtBomReferencePoint existBomReferencePoint = new MtBomReferencePoint();
            existBomReferencePoint.setTenantId(tenantId);
            existBomReferencePoint.setBomReferencePointId(dto.getBomReferencePointId());
            existBomReferencePoint.setBomComponentId(bomComponentId);
            existBomReferencePoint = mtBomReferencePointMapper.selectOne(existBomReferencePoint);
            if (null == existBomReferencePoint) {
                throw new MtException("MT_BOM_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0049", "BOM", "【API:bomReferencePointUpdate】"));
            }

            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, bomComponentId);
            if (null == mtBomComponent) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomReferencePointUpdate】"));
            }

            mtBomReferencePoint.setTenantId(tenantId);
            mtBomReferencePoint.setReferencePoint(dto.getReferencePoint());
            mtBomReferencePoint.setQty(dto.getQty());
            mtBomReferencePoint.setLineNumber(dto.getLineNumber());
            mtBomReferencePoint.setEnableFlag(dto.getEnableFlag());
            self().updateByPrimaryKeySelective(mtBomReferencePoint);

            // 生成历史数据
            MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
            mtBomHisVO1.setBomId(mtBomComponent.getBomId());
            mtBomHisVO1.setEventTypeCode("BOM_REFERENCE_POINT_UPDATE");
            mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
        }

        return bomReferencePointId;
    }

    @Override
    public List<String> sourceLimitTargetBomReferencePointUpdateGet(Long tenantId, String sourceBomComponentId,
                    String targetBomComponentId, Date now) {
        MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
        mtBomReferencePoint.setTenantId(tenantId);
        mtBomReferencePoint.setBomComponentId(sourceBomComponentId);
        List<MtBomReferencePoint> sourceBomReferencePoints = mtBomReferencePointMapper.select(mtBomReferencePoint);

        mtBomReferencePoint = new MtBomReferencePoint();
        mtBomReferencePoint.setTenantId(tenantId);
        mtBomReferencePoint.setBomComponentId(targetBomComponentId);
        List<MtBomReferencePoint> targetBomReferencePoints = mtBomReferencePointMapper.select(mtBomReferencePoint);

        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sourceBomReferencePoints) && CollectionUtils.isEmpty(targetBomReferencePoints)) {
            return sqlList;
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 来源有，目标全无，则以来源数据新增
        if (CollectionUtils.isEmpty(targetBomReferencePoints)) {
            for (MtBomReferencePoint s : sourceBomReferencePoints) {
                MtBomReferencePoint newBomReferencePoint = new MtBomReferencePoint();
                newBomReferencePoint.setTenantId(tenantId);
                newBomReferencePoint.setBomReferencePointId(this.customSequence.getNextKey("mt_bom_reference_point_s"));
                newBomReferencePoint.setBomComponentId(targetBomComponentId);
                newBomReferencePoint.setReferencePoint(s.getReferencePoint());
                newBomReferencePoint.setQty(s.getQty());
                newBomReferencePoint.setLineNumber(s.getLineNumber());
                newBomReferencePoint.setEnableFlag(s.getEnableFlag());
                newBomReferencePoint.setCopiedFromPointId(s.getBomReferencePointId());
                newBomReferencePoint
                                .setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                newBomReferencePoint.setCreatedBy(userId);
                newBomReferencePoint.setCreationDate(now);
                newBomReferencePoint.setLastUpdatedBy(userId);
                newBomReferencePoint.setLastUpdateDate(now);
                newBomReferencePoint.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(newBomReferencePoint));
            }
        } else if (CollectionUtils.isEmpty(sourceBomReferencePoints)) {
            // 目标有，来源全无，则目标全部失效
            for (MtBomReferencePoint t : targetBomReferencePoints) {
                t.setTenantId(tenantId);
                t.setEnableFlag("N");
                t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                t.setLastUpdatedBy(userId);
                t.setLastUpdateDate(now);
                sqlList.addAll(MtSqlHelper.getUpdateSql(t));
            }
        } else {
            // 目标有，来源也有，则匹配 REFERENCE_POINT 相同的数据
            Map<String, MtBomReferencePoint> sourceBomReferencePointMap = sourceBomReferencePoints.stream()
                            .collect(Collectors.toMap(m -> m.getBomReferencePointId(), m -> m));

            for (MtBomReferencePoint t : targetBomReferencePoints) {
                // 对每一个目标组件筛选来源
                List<MtBomReferencePoint> result = sourceBomReferencePoints.stream()
                                .filter(s -> s.getReferencePoint().equals(t.getReferencePoint()))
                                .collect(Collectors.toList());

                // 如果无对应来源，则无效改目标
                if (CollectionUtils.isEmpty(result)) {
                    t.setTenantId(tenantId);
                    t.setEnableFlag("N");
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                    t.setLastUpdatedBy(userId);
                    t.setLastUpdateDate(now);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                } else {
                    MtBomReferencePoint tempSource = result.get(0);

                    // 剔除来源Map数据: 筛选结果唯一
                    sourceBomReferencePointMap.remove(tempSource.getBomReferencePointId());

                    // 根据筛选出来的来源更新该目标
                    t.setTenantId(tenantId);
                    t.setQty(tempSource.getQty());
                    t.setLineNumber(tempSource.getLineNumber());
                    t.setEnableFlag(tempSource.getEnableFlag());
                    t.setCopiedFromPointId(tempSource.getBomReferencePointId());
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                    t.setLastUpdatedBy(userId);
                    t.setLastUpdateDate(now);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                }
            }

            // 如果处理完所有目标后，还有来源未筛选到，则对这部分来源执行目标新增
            if (!MapUtils.isEmpty(sourceBomReferencePointMap)) {
                for (Map.Entry<String, MtBomReferencePoint> entry : sourceBomReferencePointMap.entrySet()) {
                    MtBomReferencePoint sourceBomReferencePoint = entry.getValue();

                    MtBomReferencePoint newBomReferencePoint = new MtBomReferencePoint();
                    newBomReferencePoint.setTenantId(tenantId);
                    newBomReferencePoint
                                    .setBomReferencePointId(this.customSequence.getNextKey("mt_bom_reference_point_s"));
                    newBomReferencePoint.setBomComponentId(targetBomComponentId);
                    newBomReferencePoint.setReferencePoint(sourceBomReferencePoint.getReferencePoint());
                    newBomReferencePoint.setQty(sourceBomReferencePoint.getQty());
                    newBomReferencePoint.setLineNumber(sourceBomReferencePoint.getLineNumber());
                    newBomReferencePoint.setEnableFlag(sourceBomReferencePoint.getEnableFlag());
                    newBomReferencePoint.setCopiedFromPointId(sourceBomReferencePoint.getBomReferencePointId());
                    newBomReferencePoint.setCid(
                                    Long.valueOf(this.customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                    newBomReferencePoint.setCreatedBy(userId);
                    newBomReferencePoint.setCreationDate(now);
                    newBomReferencePoint.setLastUpdatedBy(userId);
                    newBomReferencePoint.setLastUpdateDate(now);
                    sqlList.addAll(MtSqlHelper.getInsertSql(newBomReferencePoint));
                }
            }
        }
        return sqlList;
    }

}
