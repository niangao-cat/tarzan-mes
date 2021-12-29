package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtAssemblePointActual;
import tarzan.actual.domain.repository.MtAssembleGroupActualRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO3;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO4;
import tarzan.actual.domain.vo.MtAssemblePointActualVO;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO2;
import tarzan.actual.domain.vo.MtAssemblePointActualVO3;
import tarzan.actual.domain.vo.MtAssemblePointActualVO4;
import tarzan.actual.domain.vo.MtAssemblePointActualVO5;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO11;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO4;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtUomVO3;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.entity.MtAssemblePoint;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssemblePointVO;
import tarzan.method.domain.vo.MtAssemblePointVO1;
import tarzan.method.domain.vo.MtAssemblePointVO10;
import tarzan.method.domain.vo.MtAssemblePointVO2;
import tarzan.method.domain.vo.MtAssemblePointVO3;
import tarzan.method.domain.vo.MtAssemblePointVO4;
import tarzan.method.domain.vo.MtAssemblePointVO5;
import tarzan.method.domain.vo.MtAssemblePointVO6;
import tarzan.method.domain.vo.MtAssemblePointVO7;
import tarzan.method.domain.vo.MtAssemblePointVO9;
import tarzan.method.infra.mapper.MtAssembleGroupMapper;
import tarzan.method.infra.mapper.MtAssemblePointMapper;

/**
 * 装配点，标识具体装配组下具体的装配位置 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@Component
public class MtAssemblePointRepositoryImpl extends BaseRepositoryImpl<MtAssemblePoint>
                implements MtAssemblePointRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssemblePointMapper mtAssemblePointMapper;

    @Autowired
    private MtAssembleGroupMapper mtAssembleGroupMapper;

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @Autowired
    private MtAssemblePointActualRepository mtAssemblePointActualRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtAssembleGroupActualRepository mtAssembleGroupActualRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Override
    public List<String> propertyLimitAssemblePointQuery(Long tenantId, MtAssemblePoint dto) {
        if (null == dto.getAssembleGroupId() && null == dto.getAssemblePointCode() && null == dto.getDescription()
                        && null == dto.getUniqueMaterialLotFlag() && null == dto.getSequence()
                        && null == dto.getMaxQty() && null == dto.getEnableFlag()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0004",
                                            "ASSEMBLE_CONTROL", "【API:propertyLimitAssemblePointQuery】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        BeanUtils.copyProperties(dto, mtAssemblePoint);
        mtAssemblePoint.setTenantId(tenantId);
        Criteria criteria = new Criteria(mtAssemblePoint);

        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtAssemblePoint.FIELD_TENANT_ID, Comparison.EQUAL));

        if (null != dto.getAssembleGroupId()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_ASSEMBLE_GROUP_ID, Comparison.EQUAL));
        }
        if (null != dto.getAssemblePointCode()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_ASSEMBLE_POINT_CODE, Comparison.EQUAL));
        }
        if (null != dto.getDescription()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_DESCRIPTION, Comparison.LIKE));
        }
        if (null != dto.getUniqueMaterialLotFlag()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_UNIQUE_MATERIAL_LOT_FLAG, Comparison.EQUAL));
        }
        if (null != dto.getSequence()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_SEQUENCE, Comparison.EQUAL));
        }
        if (null != dto.getMaxQty()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_MAX_QTY, Comparison.EQUAL));
        }
        if (null != dto.getEnableFlag()) {
            whereFields.add(new WhereField(MtAssemblePoint.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtAssemblePoint> mtAssemblePoints = this.mtAssemblePointMapper.selectOptional(mtAssemblePoint, criteria);
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return Collections.emptyList();
        }
        return mtAssemblePoints.stream().map(MtAssemblePoint::getAssemblePointId).collect(Collectors.toList());
    }

    @Override
    public MtAssemblePoint assemblePointPropertyGet(Long tenantId, String assemblePointId) {
        if (StringUtils.isEmpty(assemblePointId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointPropertyGet】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssemblePointId(assemblePointId);
        return this.mtAssemblePointMapper.selectOne(mtAssemblePoint);
    }

    @Override
    public List<Map<String, String>> assembleGroupLimitEnableAssemblePointQuery(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupLimitEnableAssemblePointQuery】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(assembleGroupId);
        mtAssemblePoint.setEnableFlag("Y");
        List<MtAssemblePoint> mtAssemblePoints = this.mtAssemblePointMapper.select(mtAssemblePoint);
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return Collections.emptyList();
        }

        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        mtAssemblePoints.stream().forEach(c -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("assemblePointId", c.getAssemblePointId());
            map.put("assemblePointCode", c.getAssemblePointCode());
            list.add(map);
        });
        return list;
    }

    @Override
    public void assemblePointIsEnabledValidate(Long tenantId, String assemblePointId) {
        if (StringUtils.isEmpty(assemblePointId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointIsEnabledValidate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssemblePointId(assemblePointId);
        mtAssemblePoint = this.mtAssemblePointMapper.selectOne(mtAssemblePoint);
        if (null == mtAssemblePoint || !"Y".equals(mtAssemblePoint.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointIsEnabledValidate】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assemblePointUpdate(Long tenantId, MtAssemblePoint dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssemblePointCode())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointCode", "【API:assemblePointUpdate】"));
        }
        if (null == dto.getSequence()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "sequence", "【API:assemblePointUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "enableFlag", "【API:assemblePointUpdate】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroup = this.mtAssembleGroupMapper.selectOne(mtAssembleGroup);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointUpdate】"));
        }
        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssemblePoint.setAssemblePointCode(dto.getAssemblePointCode());
        // 唯一性校验
        if (null != mtAssemblePointMapper.selectOne(mtAssemblePoint)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0024",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0024",
                                            "ASSEMBLE_CONTROL", "MT_ASSEMBLE_POINT",
                                            "【ASSEMBLE_GROUP_ID+ASSEMBLE_POINT_CODE】", "【API:assemblePointUpdate】"));
        }
        mtAssemblePoint.setDescription(dto.getDescription());
        mtAssemblePoint.setUniqueMaterialLotFlag(dto.getUniqueMaterialLotFlag());
        mtAssemblePoint.setSequence(dto.getSequence());
        mtAssemblePoint.setMaxQty(dto.getMaxQty());
        mtAssemblePoint.setEnableFlag(dto.getEnableFlag());
        MtAssemblePoint tmpMtAssemblePoint = new MtAssemblePoint();
        if (StringUtils.isEmpty(dto.getAssemblePointId())) {
            self().insertSelective(mtAssemblePoint);
        } else {
            tmpMtAssemblePoint.setTenantId(tenantId);
            tmpMtAssemblePoint.setAssemblePointId(dto.getAssemblePointId());
            tmpMtAssemblePoint = this.mtAssemblePointMapper.selectOne(tmpMtAssemblePoint);
            if (null == tmpMtAssemblePoint) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointUpdate】"));
            }

            if (!tmpMtAssemblePoint.getAssembleGroupId().equals(dto.getAssembleGroupId())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0012",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0012",
                                                "ASSEMBLE_CONTROL", tmpMtAssemblePoint.getAssemblePointCode(),
                                                mtAssembleGroup.getAssembleGroupCode(), "【API:assemblePointUpdate】"));
            }
            tmpMtAssemblePoint.setAssemblePointCode(
                            dto.getAssemblePointCode() == null ? tmpMtAssemblePoint.getAssemblePointCode()
                                            : dto.getAssemblePointCode());
            tmpMtAssemblePoint.setDescription(
                            dto.getDescription() == null ? tmpMtAssemblePoint.getDescription() : dto.getDescription());
            tmpMtAssemblePoint.setUniqueMaterialLotFlag(
                            dto.getUniqueMaterialLotFlag() == null ? tmpMtAssemblePoint.getUniqueMaterialLotFlag()
                                            : dto.getUniqueMaterialLotFlag());
            tmpMtAssemblePoint.setSequence(
                            dto.getSequence() == null ? tmpMtAssemblePoint.getSequence() : dto.getSequence());
            tmpMtAssemblePoint.setMaxQty(dto.getMaxQty());
            tmpMtAssemblePoint.setEnableFlag(
                            dto.getEnableFlag() == null ? tmpMtAssemblePoint.getEnableFlag() : dto.getEnableFlag());
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                self().updateByPrimaryKey(tmpMtAssemblePoint);
            } else {
                self().updateByPrimaryKeySelective(tmpMtAssemblePoint);
            }
        }
        return tmpMtAssemblePoint.getAssemblePointId();
    }

    @Override
    public String assemblePointCodeGenerate(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointCodeGenerate】"));
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointCodeGenerate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(assembleGroupId);
        List<MtAssemblePoint> mtAssemblePoints = this.mtAssemblePointMapper.select(mtAssemblePoint);
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return mtAssembleGroup.getAssembleGroupCode() + "-1";
        }
        // 新增校验若结果中仅对assemblePointCode的"-"数据处理
        mtAssemblePoints = mtAssemblePoints.stream().filter(t -> t.getAssemblePointCode().contains("-"))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return mtAssembleGroup.getAssembleGroupCode() + "-1";
        }
        MtAssemblePoint lastMtAssemblePoint = mtAssemblePoints.stream()
                        .sorted(Comparator.comparing(MtAssemblePoint::getAssemblePointCode, (x, y) -> {
                            int idx1 = x.lastIndexOf("-");
                            int idx2 = y.lastIndexOf("-");
                            int num1 = Integer.parseInt(x.substring(idx1 + 1));
                            int num2 = Integer.parseInt(y.substring(idx2 + 1));
                            return num2 - num1;
                        })).findFirst().get();

        int idx = lastMtAssemblePoint.getAssemblePointCode().lastIndexOf("-");
        int num = Integer.parseInt(lastMtAssemblePoint.getAssemblePointCode().substring(idx + 1)) + 1;
        return mtAssembleGroup.getAssembleGroupCode() + "-" + num;
    }

    @Override
    public Long assemblePointNextSequenceGenerate(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assemblePointNextSequenceGenerate】"));
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assemblePointNextSequenceGenerate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(assembleGroupId);
        List<MtAssemblePoint> mtAssemblePoints = this.mtAssemblePointMapper.select(mtAssemblePoint);
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return 1L;
        }

        MtAssemblePoint lastMtAssemblePoint = mtAssemblePoints.stream()
                        .sorted(Comparator.comparingLong(MtAssemblePoint::getSequence).reversed()).findFirst().get();
        return lastMtAssemblePoint.getSequence().longValue() + 1L;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assemblePointAutoCreate(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointAutoCreate】"));
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assemblePointAutoCreate】"));
        }
        if (!"Y".equals(mtAssembleGroup.getAutoInstallPointFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0013", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_ASSEMBLE_CONTROL_0013", "ASSEMBLE_CONTROL", "【API:assemblePointAutoCreate】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(assembleGroupId);
        mtAssemblePoint.setAssemblePointCode(assemblePointCodeGenerate(tenantId, assembleGroupId));
        mtAssemblePoint.setDescription(mtAssemblePoint.getAssemblePointCode());
        mtAssemblePoint.setUniqueMaterialLotFlag("N");
        mtAssemblePoint.setSequence(assemblePointNextSequenceGenerate(tenantId, assembleGroupId));
        mtAssemblePoint.setEnableFlag("Y");
        self().insertSelective(mtAssemblePoint);
        return mtAssemblePoint.getAssemblePointId();
    }

    @Override
    public List<MtAssemblePointVO> assembleGroupLimitAssemblePointOrderBySequenceSort(Long tenantId,
                                                                                      String assembleGroupId, String sortBy) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupLimitAssemblePointOrderBySequenceSort】"));
        }
        if (StringUtils.isNotEmpty(sortBy) && !Arrays.asList("", "ASC", "DESC").contains(sortBy)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0014",
                                            "ASSEMBLE_CONTROL",
                                            "【API:assembleGroupLimitAssemblePointOrderBySequenceSort】"));
        }

        MtAssemblePoint mtAssemblePoint = new MtAssemblePoint();
        mtAssemblePoint.setTenantId(tenantId);
        mtAssemblePoint.setAssembleGroupId(assembleGroupId);
        mtAssemblePoint.setEnableFlag("Y");
        List<MtAssemblePoint> mtAssemblePoints = this.mtAssemblePointMapper.select(mtAssemblePoint);
        if (CollectionUtils.isEmpty(mtAssemblePoints)) {
            return Collections.emptyList();
        }

        final List<MtAssemblePointVO> result = new ArrayList<MtAssemblePointVO>();
        if ("DESC".equals(sortBy)) {
            mtAssemblePoints.stream().sorted(Comparator.comparingLong(MtAssemblePoint::getSequence).reversed())
                            .forEach(c -> {
                                MtAssemblePointVO mtAssemblePointVO = new MtAssemblePointVO();
                                mtAssemblePointVO.setAssemblePointCode(c.getAssemblePointCode());
                                mtAssemblePointVO.setAssemblePointId(c.getAssemblePointId());
                                mtAssemblePointVO.setSequence(c.getSequence());
                                result.add(mtAssemblePointVO);
                            });
        } else {
            mtAssemblePoints.stream().sorted(Comparator.comparingLong(MtAssemblePoint::getSequence)).forEach(c -> {
                MtAssemblePointVO mtAssemblePointVO = new MtAssemblePointVO();
                mtAssemblePointVO.setAssemblePointCode(c.getAssemblePointCode());
                mtAssemblePointVO.setAssemblePointId(c.getAssemblePointId());
                mtAssemblePointVO.setSequence(c.getSequence());
                result.add(mtAssemblePointVO);
            });
        }
        return result;
    }

    @Override
    public List<MtAssemblePoint> assemblePointPropertyBatchGet(Long tenantId, List<String> assemblePointIds) {
        if (CollectionUtils.isEmpty(assemblePointIds)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId",
                                            "【API:assemblePointPropertyBatchGet】"));
        }

        return this.mtAssemblePointMapper.selectByIdsCustom(tenantId, assemblePointIds);
    }

    @Override
    public void assemblePointMaxQtyVerify(Long tenantId, MtAssemblePointVO1 condition) {
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointMaxQtyVerify】"));
        }
        if (null == condition.getQty()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "qty", "【API:assemblePointMaxQtyVerify】"));
        }

        MtAssemblePoint mtAssemblePoint = assemblePointPropertyGet(tenantId, condition.getAssemblePointId());
        if (null == mtAssemblePoint) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointMaxQtyVerify】"));
        }

        if (StringUtils.isEmpty(mtAssemblePoint.getEnableFlag()) || !"Y".equals(mtAssemblePoint.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assemblePointId", "【API:assemblePointMaxQtyVerify】"));
        }

        if (null != mtAssemblePoint.getMaxQty() && BigDecimal.valueOf(condition.getQty())
                        .compareTo(BigDecimal.valueOf(mtAssemblePoint.getMaxQty())) > 0) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0017",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0017",
                                            "ASSEMBLE_CONTROL", "【API:assemblePointMaxQtyVerify】"));
        }
    }

    /**
     * 装配点物料安装验证/sen.luo 2018-03-21
     *
     * @param tenantId
     * @param condition
     */
    @Override
    public void assemblePointMaterialLoadVerify(Long tenantId, MtAssemblePointVO2 condition) {
        if (StringUtils.isEmpty(condition.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assemblePointId", "【API:assemblePointMaterialLoadVerify】"));
        }

        String materialId = null;
        Double qty = null;
        if (StringUtils.isEmpty(condition.getMaterialLotId())) {
            if (StringUtils.isEmpty(condition.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、materialId",
                                                "【API:assemblePointMaterialLoadVerify】"));
            }
            if (condition.getTrxQty() == null) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、trxQty",
                                                "【API:assemblePointMaterialLoadVerify】"));
            }
            materialId = condition.getMaterialId();
            qty = condition.getTrxQty();
        } else {
            // 根据materialLotId 获取物料materialId和数量qty
            // 逻辑修改调用API{ materialLotLimitMaterialQtyGet}
            MtMaterialLotVO11 mtMaterialLotVO11 = new MtMaterialLotVO11();
            mtMaterialLotVO11.setMaterialLotId(condition.getMaterialLotId());
            mtMaterialLotVO11.setPrimaryFlag("Y");
            MtMaterialLotVO4 lot =
                            this.mtMaterialLotRepository.materialLotLimitMaterialQtyGet(tenantId, mtMaterialLotVO11);
            if (null == lot) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:assemblePointMaterialLoadVerify】"));
            }

            if (StringUtils.isNotEmpty(condition.getMaterialId())
                            && !condition.getMaterialId().equals(lot.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                "ASSEMBLE", "materialId", "materialLotId",
                                                "【API:assemblePointMaterialLoadVerify】"));
            }
            if (condition.getTrxQty() != null && BigDecimal
                            .valueOf(lot.getPrimaryUomQty() == null ? Double.valueOf(0.0D) : lot.getPrimaryUomQty())
                            .compareTo(BigDecimal.valueOf(condition.getTrxQty())) != 0) {
                throw new MtException("MT_ASSEMBLE_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                "ASSEMBLE", "trxQty", "materialLotId",
                                                "【API:assemblePointMaterialLoadVerify】"));
            }
            materialId = lot.getMaterialId();
            qty = lot.getPrimaryUomQty() == null ? Double.valueOf(0.0D) : lot.getPrimaryUomQty();
        }

        if (StringUtils.isNotEmpty(condition.getMaterialLotId())) {
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setMaterialLotId(condition.getMaterialLotId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
            if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
                throw new MtException("MT_ASSEMBLE_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0022", "ASSEMBLE", "【API:assemblePointMaterialLoadVerify】"));
            }

            MtAssemblePoint mtAssemblePoint = assemblePointPropertyGet(tenantId, condition.getAssemblePointId());
            if (null == mtAssemblePoint) {
                throw new MtException("MT_ASSEMBLE_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0013", "ASSEMBLE", "【API:assemblePointMaterialLoadVerify】"));
            }

            if ("Y".equals(mtAssemblePoint.getUniqueMaterialLotFlag())) {
                MtAssemblePointActualVO2 mtAssemblePointActualVO2 = new MtAssemblePointActualVO2();
                mtAssemblePointActualVO2.setAssemblePointId(condition.getAssemblePointId());
                Double loadingQty = this.mtAssemblePointActualRepository.assemblePointLoadingMaterialQtyGet(tenantId,
                                mtAssemblePointActualVO2);
                loadingQty = loadingQty == null ? Double.valueOf(0.0D) : loadingQty;

                if (BigDecimal.valueOf(loadingQty).compareTo(BigDecimal.ZERO) != 0) {
                    throw new MtException("MT_ASSEMBLE_0025", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0025", "ASSEMBLE", "【API:assemblePointMaterialLoadVerify】"));
                } else {
                    MtAssemblePointVO1 mtAssemblePointVO1 = new MtAssemblePointVO1();
                    mtAssemblePointVO1.setAssemblePointId(condition.getAssemblePointId());
                    mtAssemblePointVO1.setQty(qty);
                    assemblePointMaxQtyVerify(tenantId, mtAssemblePointVO1);
                    return;
                }
            }
        }

        MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
        mtAssemblePointActualVO1.setAssemblePointId(condition.getAssemblePointId());
        List<String> assemblePointActualIds1 = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);

        mtAssemblePointActualVO1.setAssemblePointId(condition.getAssemblePointId());
        mtAssemblePointActualVO1.setMaterialId(materialId);
        List<String> assemblePointActualIds2 = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);

        if (CollectionUtils.isEmpty(assemblePointActualIds1)) {
            MtAssemblePointVO1 mtAssemblePointVO1 = new MtAssemblePointVO1();
            mtAssemblePointVO1.setAssemblePointId(condition.getAssemblePointId());
            mtAssemblePointVO1.setQty(qty);
            assemblePointMaxQtyVerify(tenantId, mtAssemblePointVO1);
        } else {
            if (CollectionUtils.isEmpty(assemblePointActualIds2)) {
                throw new MtException("MT_ASSEMBLE_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0024", "ASSEMBLE", "【API:assemblePointMaterialLoadVerify】"));
            }

            MtAssemblePointVO1 mtAssemblePointVO1 = new MtAssemblePointVO1();
            mtAssemblePointVO1.setAssemblePointId(condition.getAssemblePointId());

            MtAssemblePointActualVO2 mtAssemblePointActualVO2 = new MtAssemblePointActualVO2();
            mtAssemblePointActualVO2.setAssemblePointId(condition.getAssemblePointId());
            mtAssemblePointActualVO2.setMaterialId(materialId);
            Double loadingQty = this.mtAssemblePointActualRepository.assemblePointLoadingMaterialQtyGet(tenantId,
                            mtAssemblePointActualVO2);
            loadingQty = loadingQty == null ? Double.valueOf(0.0D) : loadingQty;

            mtAssemblePointVO1.setQty((BigDecimal.valueOf(qty).add(BigDecimal.valueOf(loadingQty))).doubleValue());
            assemblePointMaxQtyVerify(tenantId, mtAssemblePointVO1);
        }
    }

    /**
     * 装配组物料按序上料装配/sen.luo 2018-03-21
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointMaterialSequenceLoad(Long tenantId, MtAssemblePointVO3 dto) {
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupId", "【API:assemblePointMaterialSequenceLoad】"));
        }

        String materialId = null;
        BigDecimal needQty = BigDecimal.ZERO;
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、materialId",
                                                "【API:assemblePointMaterialSequenceLoad】"));
            }
            if (null == dto.getQty()) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、qty",
                                                "【API:assemblePointMaterialSequenceLoad】"));
            }
            materialId = dto.getMaterialId();
            needQty = BigDecimal.valueOf(dto.getQty());
        } else {
            /*
             * 逻辑变更：获取物料和本次装配数量逻辑变更
             */
            MtMaterialLotVO11 mtMaterialLotVO11 = new MtMaterialLotVO11();
            mtMaterialLotVO11.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO11.setPrimaryFlag("Y");
            MtMaterialLotVO4 mtMaterialLot =
                            mtMaterialLotRepository.materialLotLimitMaterialQtyGet(tenantId, mtMaterialLotVO11);
            if (null == mtMaterialLot || StringUtils.isEmpty(mtMaterialLot.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:assemblePointMaterialSequenceLoad】"));
            }

            materialId = mtMaterialLot.getMaterialId();
            needQty = BigDecimal.valueOf(
                            mtMaterialLot.getPrimaryUomQty() == null ? 0.0D : mtMaterialLot.getPrimaryUomQty());

            // 如果输入了物料和消耗数量，需要和获取的数据一致
            if (StringUtils.isNotEmpty(dto.getMaterialId()) && !dto.getMaterialId().equals(materialId)) {
                throw new MtException("MT_ASSEMBLE_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                "ASSEMBLE", "materialId", "materialLotId",
                                                "【API:assemblePointMaterialSequenceLoad】"));
            }

            if (dto.getQty() != null && BigDecimal.valueOf(dto.getQty()).compareTo(needQty) != 0) {
                throw new MtException("MT_ASSEMBLE_0023",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                "ASSEMBLE", "qty", "materialLotId",
                                                "【API:assemblePointMaterialSequenceLoad】"));
            }
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, dto.getAssembleGroupId());
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:assemblePointMaterialSequenceLoad】"));
        }

        if ("Y".equals(mtAssembleGroup.getAssembleControlFlag())
                        || "Y".equals(mtAssembleGroup.getAutoInstallPointFlag())) {
            throw new MtException("MT_ASSEMBLE_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0027", "ASSEMBLE", "【API:assemblePointMaterialSequenceLoad】"));
        }

        MtEventCreateVO MtEventCreateVO = new MtEventCreateVO();
        MtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_SEQUENCE_LOAD");
        MtEventCreateVO.setParentEventId(dto.getParentEventId());
        MtEventCreateVO.setEventRequestId(dto.getEventRequestId());
        MtEventCreateVO.setWorkcellId(dto.getWorkcellId());
        MtEventCreateVO.setShiftCode(dto.getShiftCode());
        MtEventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, MtEventCreateVO);

        while (true) {
            MtAssembleGroupActualVO3 mtAssembleGroupActualVO3 = new MtAssembleGroupActualVO3();
            mtAssembleGroupActualVO3.setAssembleGroupId(dto.getAssembleGroupId());
            mtAssembleGroupActualVO3.setMaterialId(dto.getMaterialId());
            mtAssembleGroupActualVO3.setMaterialLotId(dto.getMaterialLotId());
            String assemblePointId = this.mtAssembleGroupActualRepository
                            .firstAvailableFeedingAssemblePointGet(tenantId, mtAssembleGroupActualVO3);
            if (StringUtils.isEmpty(assemblePointId)) {
                throw new MtException("MT_ASSEMBLE_0073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0073", "ASSEMBLE", "【API:assemblePointMaterialSequenceLoad】"));
            }
            // 新增逻辑若输入参数中materialLotId不为空，判断装配是否合理
            if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                MtAssemblePointVO2 mtAssemblePointVO2 = new MtAssemblePointVO2();
                mtAssemblePointVO2.setMaterialId(materialId);
                mtAssemblePointVO2.setMaterialLotId(dto.getMaterialLotId());
                mtAssemblePointVO2.setTrxQty(needQty.doubleValue());
                mtAssemblePointVO2.setAssemblePointId(assemblePointId);
                assemblePointMaterialLoadVerify(tenantId, mtAssemblePointVO2);
            }
            MtAssemblePointActualVO2 mtAssemblePointActualVO2 = new MtAssemblePointActualVO2();
            mtAssemblePointActualVO2.setAssemblePointId(assemblePointId);
            mtAssemblePointActualVO2.setMaterialId(materialId);
            Double loadingQty = this.mtAssemblePointActualRepository.assemblePointLoadingMaterialQtyGet(tenantId,
                            mtAssemblePointActualVO2);
            loadingQty = loadingQty == null ? Double.valueOf(0.0D) : loadingQty;

            MtAssemblePoint mtAssemblePoint = assemblePointPropertyGet(tenantId, assemblePointId);
            Double maxQty = null;
            if (null == mtAssemblePoint) {
                maxQty = Double.valueOf(0.0D);
            } else {
                maxQty = mtAssemblePoint.getMaxQty();
                if (null == maxQty) {
                    maxQty = needQty.add(BigDecimal.valueOf(loadingQty)).add(BigDecimal.valueOf(1D)).doubleValue();
                }
            }

            BigDecimal validateQty = BigDecimal.valueOf(maxQty).subtract(BigDecimal.valueOf(loadingQty));
            if (needQty.compareTo(validateQty) <= 0) {
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointId(assemblePointId);
                mtAssemblePointActualVO4.setMaterialId(materialId);
                mtAssemblePointActualVO4.setMaterialLotId(dto.getMaterialLotId());
                mtAssemblePointActualVO4.setTrxQty(needQty.doubleValue());
                mtAssemblePointActualVO4.setTrxFeedingQty(needQty.doubleValue());
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setAssemblePointId(assemblePointId);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                }
                return;
            } else {
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointId(assemblePointId);
                mtAssemblePointActualVO4.setMaterialLotId(dto.getMaterialLotId());
                mtAssemblePointActualVO4.setMaterialId(materialId);
                mtAssemblePointActualVO4.setTrxQty(validateQty.doubleValue());
                mtAssemblePointActualVO4.setTrxFeedingQty(validateQty.doubleValue());
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setAssemblePointId(assemblePointId);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                }
                needQty = needQty.subtract(validateQty);
            }
        }
    }

    /**
     * 装配点物料消耗/sen.luo 2018-03-21
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointMaterialConsume(Long tenantId, MtAssemblePointVO4 dto) {
        // 判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assemblePointId", "【API:assemblePointMaterialConsume】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:assemblePointMaterialConsume】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotId()) && StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "materialLotId、materialId", "【API:assemblePointMaterialConsume】"));
        }

        // 判断若输入参数materialLotId不为空
        if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {

            // 新增1.3

            MtMaterialLot mtMaterialLot =
                            this.mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if (null == mtMaterialLot) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:assemblePointMaterialConsume】"));
            }

            if (StringUtils.isNotEmpty(dto.getMaterialId())
                            && !dto.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0018", "ASSEMBLE", "【API:assemblePointMaterialConsume】"));

            }

            // 3.第二步，2.1 判断若输入参数materialLotId不为空
            // 校验[assemblePointHaveMaterialVerify]
            MtAssemblePointActualVO5 mtAssemblePointActualVO5 = new MtAssemblePointActualVO5();
            mtAssemblePointActualVO5.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO5.setMaterialLotId(dto.getMaterialLotId());
            mtAssemblePointActualVO5.setMaterialId(mtMaterialLot.getMaterialId());
            mtAssemblePointActualVO5.setQty(dto.getQty());
            this.mtAssemblePointActualRepository.assemblePointHaveMaterialVerify(tenantId, mtAssemblePointActualVO5);
            // 2.2 b
            // 调用API{propertyLimitAssemblePointActualQuery}获取装配实绩assemblePointActualId
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO1.setMaterialLotId(dto.getMaterialLotId());
            mtAssemblePointActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
            // 这三个确定唯一一条
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);

            if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
                // 2.cc)调用API{eventCreate}获取事件eventId：
                MtEventCreateVO MtEventCreateVO = new MtEventCreateVO();
                MtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_MATERIAL_CONSUME");
                MtEventCreateVO.setParentEventId(dto.getParentEventId());
                MtEventCreateVO.setEventRequestId(dto.getEventRequestId());
                MtEventCreateVO.setWorkcellId(dto.getWorkcellId());
                MtEventCreateVO.setLocatorId(dto.getLocatorId());
                MtEventCreateVO.setShiftCode(dto.getShiftCode());
                MtEventCreateVO.setShiftDate(dto.getShiftDate());
                String eventId = mtEventRepository.eventCreate(tenantId, MtEventCreateVO);

                // 2.d 调用API{assemblePointActualUpdate}
                String assemblePointActualId = assemblePointActualIds.get(0);
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActualVO4.setTrxQty(-dto.getQty());
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                // 2.d.iii.若数量更新后装配点实绩数量变为0，调用API{ assemblePointActualDelete }
                // 获取更新后数量
                MtAssemblePointActual mtAssemblePointActual = new MtAssemblePointActual();
                mtAssemblePointActual.setTenantId(tenantId);
                mtAssemblePointActual.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActual = this.mtAssemblePointActualRepository.selectOne(mtAssemblePointActual);
                if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtAssemblePointActual.getQty())) == 0) {
                    this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
                }

                // 2.e) 传入物料批和消耗数量调用API{ materialLotConsume }完成物料批消耗

                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();

                mtMaterialLotVO1.setMaterialLotId(dto.getMaterialLotId());

                // 2.e.1 获取primaryUomId
                MtMaterialVO1 mtMaterialVO1 =
                                this.mtMaterialRepository.materialUomGet(tenantId, mtMaterialLot.getMaterialId());
                if (null == mtMaterialVO1) {
                    throw new MtException("MT_ORDER_0137", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0137", "ORDER", "【API:assemblePointMaterialConsume】"));
                }
                // 2.e.2
                mtMaterialLotVO1.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
                mtMaterialLotVO1.setTrxPrimaryUomQty(dto.getQty());

                if (StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
                    mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());

                    if (mtMaterialVO1.getConversionRate() == null) {
                        mtMaterialLotVO1.setTrxSecondaryUomQty(Double.valueOf(0.0D));
                    } else {
                        MtUomVO3 transferUomVO = new MtUomVO3();
                        transferUomVO.setSourceUomId(mtMaterialLotVO1.getPrimaryUomId());
                        transferUomVO.setMaterialId(mtMaterialLot.getMaterialId());
                        transferUomVO.setSourceValue(mtMaterialLotVO1.getTrxPrimaryUomQty());
                        transferUomVO = mtUomRepository.materialUomConversion(tenantId, transferUomVO);
                        if (transferUomVO != null) {
                            mtMaterialLotVO1.setTrxSecondaryUomQty(transferUomVO.getTargetValue());
                        }
                    }
                }
                mtMaterialLotVO1.setParentEventId(dto.getParentEventId());
                mtMaterialLotVO1.setEventRequestId(dto.getEventRequestId());
                mtMaterialLotVO1.setWorkcellId(dto.getWorkcellId());
                mtMaterialLotVO1.setShiftCode(dto.getShiftCode());
                mtMaterialLotVO1.setShiftDate(dto.getShiftDate());
                this.mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
            }

        } else {
            // 4.第三步，判断若materialLotId未输入或输入为空：
            MtAssemblePointActualVO5 mtAssemblePointActualVO5 = new MtAssemblePointActualVO5();
            mtAssemblePointActualVO5.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO5.setMaterialId(dto.getMaterialId());
            mtAssemblePointActualVO5.setQty(dto.getQty());
            this.mtAssemblePointActualRepository.assemblePointHaveMaterialVerify(tenantId, mtAssemblePointActualVO5);
            // 4.b.ii)调用API{propertyLimitAssemblePointActualQuery}获取装配实绩assemblePointActualId列表
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO1.setMaterialId(dto.getMaterialId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);

            List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                            .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);

            if (CollectionUtils.isNotEmpty(mtAssemblePointActuals)) {
                MtEventCreateVO MtEventCreateVO = new MtEventCreateVO();
                MtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_MATERIAL_CONSUME");
                MtEventCreateVO.setParentEventId(dto.getParentEventId());
                MtEventCreateVO.setEventRequestId(dto.getEventRequestId());
                MtEventCreateVO.setWorkcellId(dto.getWorkcellId());
                MtEventCreateVO.setLocatorId(dto.getLocatorId());
                MtEventCreateVO.setShiftCode(dto.getShiftCode());
                MtEventCreateVO.setShiftDate(dto.getShiftDate());
                String eventId = mtEventRepository.eventCreate(tenantId, MtEventCreateVO);

                if (1 == mtAssemblePointActuals.size()) {
                    // 若获取结果只有一条
                    mtAssemblePointActualVO1 = mtAssemblePointActuals.get(0);
                    String assemblePointActualId = mtAssemblePointActualVO1.getAssmeblePointActualId();

                    MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                    mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                    mtAssemblePointActualVO4.setTrxQty(-dto.getQty());
                    mtAssemblePointActualVO4.setEventId(eventId);
                    this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4,
                                    "N");

                    MtAssemblePointActual mtAssemblePointActual = new MtAssemblePointActual();
                    mtAssemblePointActual.setTenantId(tenantId);
                    mtAssemblePointActual.setAssemblePointActualId(assemblePointActualId);
                    mtAssemblePointActual = this.mtAssemblePointActualRepository.selectOne(mtAssemblePointActual);
                    if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtAssemblePointActual.getQty())) == 0) {
                        this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
                    }

                    // ii.若第三步4-c[mtAssemblePointActualVO1]获取物料批不为空：
                    if (StringUtils.isNotEmpty(mtAssemblePointActualVO1.getMaterialLotId())) {
                        MtMaterialLot mtMaterialLot = this.mtMaterialLotRepository.materialLotPropertyGet(tenantId,
                                        mtAssemblePointActualVO1.getMaterialLotId());
                        if (null == mtMaterialLot) {
                            throw new MtException("MT_ASSEMBLE_0017",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_ASSEMBLE_0017", "ASSEMBLE",
                                                            "【API:assemblePointMaterialConsume】"));
                        }
                        MtMaterialVO1 mtMaterialVO1 = this.mtMaterialRepository.materialUomGet(tenantId,
                                        mtMaterialLot.getMaterialId());
                        if (null == mtMaterialVO1) {
                            throw new MtException("MT_ORDER_0137", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0137", "ORDER", "【API:assemblePointMaterialConsume】"));
                        }

                        MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                        mtMaterialLotVO1.setMaterialLotId(mtAssemblePointActualVO1.getMaterialLotId());
                        mtMaterialLotVO1.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
                        mtMaterialLotVO1.setTrxPrimaryUomQty(dto.getQty());

                        if (StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
                            mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());

                            if (mtMaterialVO1.getConversionRate() == null) {
                                mtMaterialLotVO1.setTrxSecondaryUomQty(Double.valueOf(0.0D));
                            } else {
                                MtUomVO3 transferUomVO = new MtUomVO3();
                                transferUomVO.setSourceUomId(mtMaterialLotVO1.getPrimaryUomId());
                                transferUomVO.setMaterialId(mtMaterialLot.getMaterialId());
                                transferUomVO.setSourceValue(mtMaterialLotVO1.getTrxPrimaryUomQty());
                                // 确认过是用materialUomConversion
                                transferUomVO = mtUomRepository.materialUomConversion(tenantId, transferUomVO);
                                mtMaterialLotVO1.setTrxSecondaryUomQty(transferUomVO.getTargetValue());
                            }
                        }
                        mtMaterialLotVO1.setParentEventId(dto.getParentEventId());
                        mtMaterialLotVO1.setEventRequestId(dto.getEventRequestId());
                        mtMaterialLotVO1.setWorkcellId(dto.getWorkcellId());
                        mtMaterialLotVO1.setShiftCode(dto.getShiftCode());
                        mtMaterialLotVO1.setShiftDate(dto.getShiftDate());
                        this.mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                    } else {
                        // 4.e.iii.若第三步4-c获取物料批为空：
                        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
                        updateOnhandVO.setSiteId(dto.getSiteId());
                        updateOnhandVO.setMaterialId(dto.getMaterialId());
                        updateOnhandVO.setLocatorId(dto.getLocatorId());
                        updateOnhandVO.setLotCode("");
                        updateOnhandVO.setChangeQuantity(dto.getQty());
                        updateOnhandVO.setEventId(eventId);
                        this.mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
                    }
                } else {
                    // 如果取到多条
                    BigDecimal needConsumeQty = BigDecimal.valueOf(dto.getQty());
                    mtAssemblePointActuals.stream().forEach(c -> {
                        if (null == c.getFeedingSequence()) {
                            c.setFeedingSequence(Long.valueOf(0L));
                        }
                        if (null == c.getFeedingMaterialLotSequence()) {
                            c.setFeedingMaterialLotSequence(Long.valueOf(0L));
                        }
                        if (null == c.getQty()) {
                            c.setQty(Double.valueOf(0.0D));
                        }
                    });
                    // 对获取到的数据按照装载顺序feedingSequence，
                    // 装载物料批顺序feedingMaterialLotSequence从小到大排序
                    mtAssemblePointActuals = mtAssemblePointActuals.stream().sorted(Comparator
                                    .comparingLong(MtAssemblePointActualVO1::getFeedingSequence)
                                    .thenComparing(Comparator.comparingLong(
                                                    MtAssemblePointActualVO1::getFeedingMaterialLotSequence)))
                                    .collect(Collectors.toList());

                    for (MtAssemblePointActualVO1 mtAssemblePointActualVO : mtAssemblePointActuals) {
                        String assemblePointActualId = mtAssemblePointActualVO.getAssmeblePointActualId();
                        BigDecimal qty = BigDecimal.valueOf(mtAssemblePointActualVO.getQty());

                        if (qty.compareTo(needConsumeQty) >= 0) {
                            // 若assemblePointActualId对应的qty ≥ needConsumeQty，
                            // 认为仅需卸载当前assemblePointActualId
                            MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                            mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                            mtAssemblePointActualVO4.setTrxQty(-needConsumeQty.doubleValue());
                            mtAssemblePointActualVO4.setEventId(eventId);
                            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId,
                                            mtAssemblePointActualVO4, "N");

                            // 若assemblePointActualId对应实绩数量被更新为0，调用API{
                            // assemblePointActualDelete }
                            MtAssemblePointActual mtAssemblePointActual = new MtAssemblePointActual();
                            mtAssemblePointActual.setTenantId(tenantId);
                            mtAssemblePointActual.setAssemblePointActualId(assemblePointActualId);
                            mtAssemblePointActual =
                                            this.mtAssemblePointActualRepository.selectOne(mtAssemblePointActual);
                            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtAssemblePointActual.getQty())) == 0) {
                                this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId,
                                                assemblePointActualId);
                            }

                            if (StringUtils.isNotEmpty(mtAssemblePointActualVO.getMaterialLotId())) {
                                // 若当前装配实绩ID对应的物料批（第三步4-c获取物料批）不为空，
                                // 传入第三步4-c获取物料批和消耗数量needConsumeQty
                                // 调用API{ materialLotConsume }完成物料批消耗
                                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                                mtMaterialLotVO1.setMaterialLotId(mtAssemblePointActualVO.getMaterialLotId());

                                MtMaterialLot mtMaterialLot = this.mtMaterialLotRepository.materialLotPropertyGet(
                                                tenantId, mtAssemblePointActualVO.getMaterialLotId());
                                if (null == mtMaterialLot) {
                                    throw new MtException("MT_ASSEMBLE_0017",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                    "MT_ASSEMBLE_0017", "ASSEMBLE",
                                                                    "【API:assemblePointMaterialConsume】"));
                                }

                                MtMaterialVO1 mtMaterialVO1 = this.mtMaterialRepository.materialUomGet(tenantId,
                                                mtMaterialLot.getMaterialId());
                                if (null == mtMaterialVO1) {
                                    throw new MtException("MT_ORDER_0137",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                    "MT_ORDER_0137", "ORDER",
                                                                    "【API:assemblePointMaterialConsume】"));
                                }

                                mtMaterialLotVO1.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
                                mtMaterialLotVO1.setTrxPrimaryUomQty(needConsumeQty.doubleValue());

                                if (StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
                                    mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());

                                    if (mtMaterialVO1.getConversionRate() == null) {
                                        mtMaterialLotVO1.setTrxSecondaryUomQty(Double.valueOf(0.0D));
                                    } else {
                                        MtUomVO3 transferUomVO = new MtUomVO3();
                                        transferUomVO.setSourceUomId(mtMaterialLotVO1.getPrimaryUomId());
                                        transferUomVO.setMaterialId(mtMaterialLot.getMaterialId());
                                        transferUomVO.setSourceValue(mtMaterialLotVO1.getTrxPrimaryUomQty());
                                        // 同上
                                        transferUomVO = mtUomRepository.materialUomConversion(tenantId, transferUomVO);
                                        mtMaterialLotVO1.setTrxSecondaryUomQty(transferUomVO.getTargetValue());
                                    }
                                }
                                mtMaterialLotVO1.setParentEventId(dto.getParentEventId());
                                mtMaterialLotVO1.setEventRequestId(dto.getEventRequestId());
                                mtMaterialLotVO1.setWorkcellId(dto.getWorkcellId());
                                mtMaterialLotVO1.setShiftCode(dto.getShiftCode());
                                mtMaterialLotVO1.setShiftDate(dto.getShiftDate());
                                this.mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                            } else {
                                MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
                                updateOnhandVO.setSiteId(dto.getSiteId());
                                updateOnhandVO.setMaterialId(dto.getMaterialId());
                                updateOnhandVO.setLocatorId(dto.getLocatorId());
                                updateOnhandVO.setLotCode("");
                                updateOnhandVO.setChangeQuantity(needConsumeQty.doubleValue());
                                updateOnhandVO.setEventId(eventId);
                                this.mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
                            }
                            break;
                        } else {
                            // 若assemblePointActualId对应的qty ＜ needConsumeQty，
                            // 则需要首先将当前装配点实绩卸载完成，然后继续判断下一装配点实绩
                            MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                            mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                            mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                            mtAssemblePointActualVO4.setEventId(eventId);
                            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId,
                                            mtAssemblePointActualVO4, "N");

                            this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId,
                                            assemblePointActualId);

                            if (StringUtils.isNotEmpty(mtAssemblePointActualVO.getMaterialLotId())) {
                                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                                mtMaterialLotVO1.setMaterialLotId(mtAssemblePointActualVO.getMaterialLotId());

                                MtMaterialLot mtMaterialLot = this.mtMaterialLotRepository.materialLotPropertyGet(
                                                tenantId, mtAssemblePointActualVO.getMaterialLotId());
                                if (null == mtMaterialLot) {
                                    throw new MtException("MT_ASSEMBLE_0017",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                    "MT_ASSEMBLE_0017", "ASSEMBLE",
                                                                    "【API:assemblePointMaterialConsume】"));
                                }

                                MtMaterialVO1 mtMaterialVO1 = this.mtMaterialRepository.materialUomGet(tenantId,
                                                mtMaterialLot.getMaterialId());
                                if (null == mtMaterialVO1) {
                                    throw new MtException("MT_ORDER_0137",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                    "MT_ORDER_0137", "ORDER",
                                                                    "【API:assemblePointMaterialConsume】"));
                                }

                                mtMaterialLotVO1.setPrimaryUomId(mtMaterialVO1.getPrimaryUomId());
                                mtMaterialLotVO1.setTrxPrimaryUomQty(mtAssemblePointActualVO.getQty());

                                if (StringUtils.isNotEmpty(mtMaterialVO1.getSecondaryUomId())) {
                                    mtMaterialLotVO1.setSecondaryUomId(mtMaterialVO1.getSecondaryUomId());

                                    if (mtMaterialVO1.getConversionRate() == null) {
                                        mtMaterialLotVO1.setTrxSecondaryUomQty(Double.valueOf(0.0D));
                                    } else {
                                        MtUomVO3 transferUomVO = new MtUomVO3();
                                        transferUomVO.setSourceUomId(mtMaterialLotVO1.getPrimaryUomId());
                                        transferUomVO.setMaterialId(mtMaterialLot.getMaterialId());
                                        transferUomVO.setSourceValue(mtMaterialLotVO1.getTrxPrimaryUomQty());
                                        transferUomVO = mtUomRepository.materialUomConversion(tenantId, transferUomVO);
                                        mtMaterialLotVO1.setTrxSecondaryUomQty(transferUomVO.getTargetValue());
                                    }
                                }
                                mtMaterialLotVO1.setParentEventId(dto.getParentEventId());
                                mtMaterialLotVO1.setEventRequestId(dto.getEventRequestId());
                                mtMaterialLotVO1.setWorkcellId(dto.getWorkcellId());
                                mtMaterialLotVO1.setShiftCode(dto.getShiftCode());
                                mtMaterialLotVO1.setShiftDate(dto.getShiftDate());
                                this.mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                            } else {
                                MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
                                updateOnhandVO.setSiteId(dto.getSiteId());
                                updateOnhandVO.setMaterialId(dto.getMaterialId());
                                updateOnhandVO.setLocatorId(dto.getLocatorId());
                                updateOnhandVO.setLotCode("");
                                updateOnhandVO.setChangeQuantity(mtAssemblePointActualVO.getQty());
                                updateOnhandVO.setEventId(eventId);
                                this.mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnhandVO);
                            }

                            needConsumeQty = needConsumeQty
                                            .subtract(BigDecimal.valueOf(mtAssemblePointActualVO.getQty()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 装配点物料装载/sen.luo 2018-03-21
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointMaterialLoad(Long tenantId, MtAssemblePointVO5 dto) {
        // 2.第一步，判断输入参数是否合规：
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、materialId",
                                                "【API:assemblePointMaterialLoad】"));
            }
            if (dto.getQty() == null) {
                throw new MtException("MT_ASSEMBLE_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                "ASSEMBLE", "materialLotId、qty", "【API:assemblePointMaterialLoad】"));
            }
        }

        if (StringUtils.isEmpty(dto.getAssembleGroupId()) && StringUtils.isEmpty(dto.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002", "ASSEMBLE",
                                            "assembleGroupId、assemblePointId", "【API:assemblePointMaterialLoad】"));
        }

        String assembleGroupId = dto.getAssembleGroupId();
        if (StringUtils.isNotEmpty(dto.getAssemblePointId())) {
            if (StringUtils.isNotEmpty(dto.getAssembleGroupId())) {
                MtAssemblePoint mtAssemblePoint = assemblePointPropertyGet(tenantId, dto.getAssemblePointId());
                if (mtAssemblePoint == null || !dto.getAssembleGroupId().equals(mtAssemblePoint.getAssembleGroupId())) {
                    throw new MtException("MT_ASSEMBLE_0023",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0023",
                                                    "ASSEMBLE", "assembleGroupId", "assemblePointId",
                                                    "【API:assemblePointMaterialLoad】"));
                }
                assembleGroupId = mtAssemblePoint.getAssembleGroupId();
            }
        }

        String materialId = null;
        Double qty = null;
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            materialId = dto.getMaterialId();
            qty = dto.getQty() == null ? Double.valueOf(0.0D) : dto.getQty();
        } else {
            MtMaterialLotVO11 mtMaterialLotVO11 = new MtMaterialLotVO11();
            mtMaterialLotVO11.setMaterialLotId(dto.getMaterialLotId());
            mtMaterialLotVO11.setPrimaryFlag("Y");
            MtMaterialLotVO4 mtMaterialLotVO4 =
                            this.mtMaterialLotRepository.materialLotLimitMaterialQtyGet(tenantId, mtMaterialLotVO11);
            if (null == mtMaterialLotVO4) {
                throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0017", "ASSEMBLE", "【API:assemblePointMaterialLoad】"));
            }

            materialId = mtMaterialLotVO4.getMaterialId();
            qty = mtMaterialLotVO4.getPrimaryUomQty() == null ? Double.valueOf(0.0D)
                            : mtMaterialLotVO4.getPrimaryUomQty();
        }

        // 根据输入assembleGroupId或第一步根据装配点获取到的assembleGroupId，
        // 获取装配组属性是否自动安装装配点
        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:assemblePointMaterialLoad】"));
        }

        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        String eventId = null;
        MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
        // 4-a
        if ("Y".equals(mtAssembleGroup.getAutoInstallPointFlag())) {
            // 装配上料时启用装配控制
            String autoAssemblePointId = this.assemblePointAutoCreate(tenantId, assembleGroupId);
            mtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_AUTO_INSTALL_LOAD");
            mtEventCreateVO.setParentEventId(dto.getParentEventId());
            mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
            mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
            mtEventCreateVO.setShiftCode(dto.getShiftCode());
            mtEventCreateVO.setShiftDate(dto.getShiftDate());
            eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            mtAssemblePointActualVO4.setAssemblePointId(autoAssemblePointId);
            mtAssemblePointActualVO4.setMaterialId(materialId);
            mtAssemblePointActualVO4.setMaterialLotId(dto.getMaterialLotId());
            // mtAssemblePointActualVO4.setQty(qty);
            // 新增插入参数
            mtAssemblePointActualVO4.setTrxFeedingQty(qty);
            mtAssemblePointActualVO4.setTrxQty(qty);
            mtAssemblePointActualVO4.setEventId(eventId);
            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

            // 新增逻辑2019/4/17
            if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setAssemblePointId(autoAssemblePointId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }
            return;
        }
        // 4-b
        if ("Y".equals(mtAssembleGroup.getAssembleControlFlag())) {
            MtAssembleGroupActualVO4 mtAssembleGroupActualVO4 = new MtAssembleGroupActualVO4();
            mtAssembleGroupActualVO4.setWorkcellId(dto.getWorkcellId());
            mtAssembleGroupActualVO4.setEoId(dto.getEoId());
            mtAssembleGroupActualVO4.setReferenceArea(dto.getReferenceArea());
            mtAssembleGroupActualVO4.setAssembleGroupId(assembleGroupId);
            this.mtAssembleGroupActualRepository.wkcAssembleGroupControlVerify(tenantId, mtAssembleGroupActualVO4);

            String controlAssemblePointId = null;
            if (StringUtils.isNotEmpty(dto.getAssemblePointId())) {
                MtAssemblePointActualVO3 mtAssemblePointActualVO3 = new MtAssemblePointActualVO3();
                mtAssemblePointActualVO3.setWorkcellId(dto.getWorkcellId());
                mtAssemblePointActualVO3.setEoId(dto.getEoId());
                mtAssemblePointActualVO3.setReferenceArea(dto.getReferenceArea());
                mtAssemblePointActualVO3.setMaterialLotId(dto.getMaterialLotId());
                mtAssemblePointActualVO3.setMaterialId(materialId);
                mtAssemblePointActualVO3.setAssemblePointId(dto.getAssemblePointId());
                this.mtAssemblePointActualRepository.materialAssemblePointControlVerify(tenantId,
                                mtAssemblePointActualVO3);
                controlAssemblePointId = dto.getAssemblePointId();
            } else {
                MtAssemblePointActualVO mtAssemblePointActualVO = new MtAssemblePointActualVO();
                mtAssemblePointActualVO.setWorkcellId(dto.getWorkcellId());
                mtAssemblePointActualVO.setEoId(dto.getEoId());
                mtAssemblePointActualVO.setReferenceArea(dto.getReferenceArea());
                mtAssemblePointActualVO.setMaterialLotId(dto.getMaterialLotId());
                mtAssemblePointActualVO.setMaterialId(materialId);
                List<String> assemblePointIds = this.mtAssemblePointActualRepository
                                .assembleControlLimitAssemblePointQuery(tenantId, mtAssemblePointActualVO);
                if (CollectionUtils.isEmpty(assemblePointIds)) {
                    throw new MtException("MT_ASSEMBLE_0028", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0028", "ASSEMBLE", "【API:assemblePointMaterialLoad】"));
                } else if (assemblePointIds.size() > 1) {
                    throw new MtException("MT_ASSEMBLE_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0002",
                                                    "ASSEMBLE", "assemblePointId", "【API:assemblePointMaterialLoad】"));
                }
                controlAssemblePointId = assemblePointIds.get(0);
            }

            MtAssemblePointVO2 mtAssemblePointVO2 = new MtAssemblePointVO2();
            mtAssemblePointVO2.setAssemblePointId(controlAssemblePointId);
            mtAssemblePointVO2.setMaterialId(materialId);
            mtAssemblePointVO2.setMaterialLotId(dto.getMaterialLotId());
            mtAssemblePointVO2.setTrxQty(qty);
            assemblePointMaterialLoadVerify(tenantId, mtAssemblePointVO2);

            mtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_CONTROL_LOAD");
            mtEventCreateVO.setParentEventId(dto.getParentEventId());
            mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
            mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
            mtEventCreateVO.setShiftCode(dto.getShiftCode());
            mtEventCreateVO.setShiftDate(dto.getShiftDate());
            eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            mtAssemblePointActualVO4.setAssemblePointId(controlAssemblePointId);
            mtAssemblePointActualVO4.setMaterialId(materialId);
            mtAssemblePointActualVO4.setMaterialLotId(dto.getMaterialLotId());
            // mtAssemblePointActualVO4.setQty(qty);
            // 新增传入参数FeedingQty取值与qty一致
            mtAssemblePointActualVO4.setTrxFeedingQty(qty);
            mtAssemblePointActualVO4.setTrxQty(qty);
            mtAssemblePointActualVO4.setEventId(eventId);
            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

            if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setAssemblePointId(controlAssemblePointId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
            }
            return;
        }
        // 4-c
        if ("Y".equals(mtAssembleGroup.getAssembleSequenceFlag())) {
            if (StringUtils.isNotEmpty(dto.getAssemblePointId())) {
                MtAssembleGroupActualVO3 mtAssembleGroupActualVO3 = new MtAssembleGroupActualVO3();
                mtAssembleGroupActualVO3.setAssembleGroupId(assembleGroupId);
                mtAssembleGroupActualVO3.setMaterialId(materialId);
                mtAssembleGroupActualVO3.setMaterialLotId(dto.getMaterialLotId());
                String avaAssemblePointId = this.mtAssembleGroupActualRepository
                                .firstAvailableFeedingAssemblePointGet(tenantId, mtAssembleGroupActualVO3);

                if (!dto.getAssemblePointId().equals(avaAssemblePointId)) {
                    throw new MtException("MT_ASSEMBLE_0029", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0029", "ASSEMBLE", "【API:assemblePointMaterialLoad】"));
                }
            }

            MtAssemblePointVO3 mtAssemblePointVO3 = new MtAssemblePointVO3();
            mtAssemblePointVO3.setAssembleGroupId(assembleGroupId);
            mtAssemblePointVO3.setMaterialId(materialId);
            mtAssemblePointVO3.setMaterialLotId(dto.getMaterialLotId());
            mtAssemblePointVO3.setQty(qty);
            mtAssemblePointVO3.setWorkcellId(dto.getWorkcellId());
            mtAssemblePointVO3.setEventRequestId(dto.getEventRequestId());
            mtAssemblePointVO3.setParentEventId(dto.getParentEventId());
            mtAssemblePointVO3.setShiftDate(dto.getShiftDate());
            mtAssemblePointVO3.setShiftCode(dto.getShiftCode());
            assemblePointMaterialSequenceLoad(tenantId, mtAssemblePointVO3);
        }
    }

    /**
     * 装配点物料卸载/sen.luo 2018-03-21
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointMaterialUnload(Long tenantId, MtAssemblePointVO6 dto) {
        if (StringUtils.isEmpty(dto.getAssemblePointId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assemblePointId", "【API:assemblePointMaterialUnload】"));
        }
        if (null != dto.getQty() && StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0030", "ASSEMBLE", "qty", "materialId", "【API:assemblePointMaterialUnload】"));
        }
        if (StringUtils.isNotEmpty(dto.getMaterialLotId()) && StringUtils.isNotEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0009", "ASSEMBLE",
                                            "materialLotId、materialId", "【API:assemblePointMaterialUnload】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialLotId()) && StringUtils.isEmpty(dto.getMaterialId())) {
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
            if (CollectionUtils.isEmpty(assemblePointActualIds)) {
                throw new MtException("MT_ASSEMBLE_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0031", "ASSEMBLE", "【API:assemblePointMaterialUnload】"));
            }

            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_MATERIAL_UNLOAD");
            mtEventCreateVO.setParentEventId(dto.getParentEventId());
            mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
            mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
            mtEventCreateVO.setShiftCode(dto.getShiftCode());
            mtEventCreateVO.setShiftDate(dto.getShiftDate());
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            for (String assemblePointActualId : assemblePointActualIds) {
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                mtAssemblePointActualVO4.setTrxFeedingQty(Double.valueOf(0.0D));
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                /*
                 * 2018/04/28 新增逻辑 若卸载的装配点实绩的物料批不为空，需更新物料批的装配点
                 */
                MtAssemblePointActualVO1 assemblePointActualProperty = mtAssemblePointActualRepository
                                .assemblePointActualPropertyGet(tenantId, assemblePointActualId);
                if (assemblePointActualProperty != null
                                && StringUtils.isNotEmpty(assemblePointActualProperty.getMaterialLotId())) {
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(assemblePointActualProperty.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setAssemblePointId(null);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                }

                this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
            }
        } else if (StringUtils.isNotEmpty(dto.getMaterialId())) {
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO1.setMaterialId(dto.getMaterialId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
            if (CollectionUtils.isEmpty(assemblePointActualIds)) {
                throw new MtException("MT_ASSEMBLE_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0031", "ASSEMBLE", "【API:assemblePointMaterialUnload】"));
            }

            MtAssemblePointActualVO5 mtAssemblePointActualVO5 = new MtAssemblePointActualVO5();
            mtAssemblePointActualVO5.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO5.setMaterialId(dto.getMaterialId());
            mtAssemblePointActualVO5.setQty(dto.getQty());
            this.mtAssemblePointActualRepository.assemblePointHaveMaterialVerify(tenantId, mtAssemblePointActualVO5);

            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_MATERIAL_UNLOAD");
            mtEventCreateVO.setParentEventId(dto.getParentEventId());
            mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
            mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
            mtEventCreateVO.setShiftCode(dto.getShiftCode());
            mtEventCreateVO.setShiftDate(dto.getShiftDate());
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            if (1 == assemblePointActualIds.size()) {
                String assemblePointActualId = assemblePointActualIds.get(0);
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                if (null != dto.getQty()) {
                    mtAssemblePointActualVO4.setTrxQty(-dto.getQty());
                    mtAssemblePointActualVO4.setQty(null);
                    mtAssemblePointActualVO4.setTrxFeedingQty(-dto.getQty());
                    mtAssemblePointActualVO4.setFeedingQty(null);
                } else {
                    mtAssemblePointActualVO4.setTrxQty(null);
                    mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                    mtAssemblePointActualVO4.setTrxFeedingQty(null);
                    mtAssemblePointActualVO4.setFeedingQty(Double.valueOf(0.0D));
                }
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                MtAssemblePointActualVO1 assemblePointActualProperty = mtAssemblePointActualRepository
                                .assemblePointActualPropertyGet(tenantId, assemblePointActualId);
                if (assemblePointActualProperty != null
                                && StringUtils.isNotEmpty(assemblePointActualProperty.getMaterialLotId())) {
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(assemblePointActualProperty.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setAssemblePointId(null);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                }

                MtAssemblePointActual mtAssemblePointActual = new MtAssemblePointActual();
                mtAssemblePointActual.setTenantId(tenantId);
                mtAssemblePointActual.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActual = this.mtAssemblePointActualRepository.selectOne(mtAssemblePointActual);
                if (null != mtAssemblePointActual
                                && BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtAssemblePointActual.getQty())) == 0) {
                    this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
                }
            } else {
                if (null == dto.getQty()) {
                    for (String assemblePointActualId : assemblePointActualIds) {
                        MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                        mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                        mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                        mtAssemblePointActualVO4.setEventId(eventId);
                        this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId,
                                        mtAssemblePointActualVO4, "N");

                        MtAssemblePointActualVO1 assemblePointActualProperty = mtAssemblePointActualRepository
                                        .assemblePointActualPropertyGet(tenantId, assemblePointActualId);
                        if (assemblePointActualProperty != null
                                        && StringUtils.isNotEmpty(assemblePointActualProperty.getMaterialLotId())) {
                            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                            mtMaterialLotVO2.setMaterialLotId(assemblePointActualProperty.getMaterialLotId());
                            mtMaterialLotVO2.setEventId(eventId);
                            mtMaterialLotVO2.setAssemblePointId(null);
                            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                        }
                        this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
                    }
                } else {
                    List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                                    .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);
                    mtAssemblePointActuals.stream().forEach(c -> {
                        if (null == c.getFeedingSequence()) {
                            c.setFeedingSequence(Long.valueOf(0L));
                        }
                        if (null == c.getFeedingMaterialLotSequence()) {
                            c.setFeedingMaterialLotSequence(Long.valueOf(0L));
                        }
                        if (null == c.getQty()) {
                            c.setQty(Double.valueOf(0.0D));
                        }
                    });

                    mtAssemblePointActuals.stream().sorted(Comparator
                                    .comparingLong(MtAssemblePointActualVO1::getFeedingSequence)
                                    .thenComparing(Comparator.comparingLong(
                                                    MtAssemblePointActualVO1::getFeedingMaterialLotSequence)))
                                    .collect(Collectors.toList());

                    BigDecimal needUnloadQty =
                                    BigDecimal.valueOf(dto.getQty() == null ? Double.valueOf(0.0D) : dto.getQty());
                    for (MtAssemblePointActualVO1 mtAssemblePointActualVO : mtAssemblePointActuals) {
                        BigDecimal actualQty = BigDecimal
                                        .valueOf(mtAssemblePointActualVO.getQty() == null ? Double.valueOf(0.0D)
                                                        : mtAssemblePointActualVO.getQty());

                        if (actualQty.compareTo(needUnloadQty) >= 0) {
                            MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                            mtAssemblePointActualVO4.setAssemblePointActualId(
                                            mtAssemblePointActualVO.getAssmeblePointActualId());
                            mtAssemblePointActualVO4.setTrxQty(-needUnloadQty.doubleValue());
                            mtAssemblePointActualVO4.setTrxFeedingQty(-needUnloadQty.doubleValue());
                            mtAssemblePointActualVO4.setEventId(eventId);
                            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId,
                                            mtAssemblePointActualVO4, "N");

                            if (actualQty.compareTo(needUnloadQty) == 0
                                            && StringUtils.isNotEmpty(mtAssemblePointActualVO.getMaterialLotId())) {
                                // 获取装配点装配的物料批,更新物料批装配点
                                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                                mtMaterialLotVO2.setMaterialLotId(mtAssemblePointActualVO.getMaterialLotId());
                                mtMaterialLotVO2.setEventId(eventId);
                                mtMaterialLotVO2.setAssemblePointId(null);
                                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                            }

                            MtAssemblePointActual mtAssemblePointActual = new MtAssemblePointActual();
                            mtAssemblePointActual.setTenantId(tenantId);
                            mtAssemblePointActual.setAssemblePointActualId(
                                            mtAssemblePointActualVO.getAssmeblePointActualId());
                            mtAssemblePointActual =
                                            this.mtAssemblePointActualRepository.selectOne(mtAssemblePointActual);
                            if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtAssemblePointActual.getQty())) == 0) {
                                this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId,
                                                mtAssemblePointActualVO.getAssmeblePointActualId());
                            }
                            return;
                        } else {
                            MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                            mtAssemblePointActualVO4.setAssemblePointActualId(
                                            mtAssemblePointActualVO.getAssmeblePointActualId());
                            mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                            mtAssemblePointActualVO4.setFeedingQty(Double.valueOf(0.0D));
                            mtAssemblePointActualVO4.setEventId(eventId);
                            this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId,
                                            mtAssemblePointActualVO4, "N");

                            if (StringUtils.isNotEmpty(mtAssemblePointActualVO.getMaterialLotId())) {
                                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                                mtMaterialLotVO2.setMaterialLotId(mtAssemblePointActualVO.getMaterialLotId());
                                mtMaterialLotVO2.setEventId(eventId);
                                mtMaterialLotVO2.setAssemblePointId(null);
                                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                            }

                            this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId,
                                            mtAssemblePointActualVO.getAssmeblePointActualId());
                            needUnloadQty = needUnloadQty.subtract(actualQty);
                        }
                    }
                }
            }
        } else if (StringUtils.isNotEmpty(dto.getMaterialLotId())) {
            MtAssemblePointActualVO1 mtAssemblePointActualVO1 = new MtAssemblePointActualVO1();
            mtAssemblePointActualVO1.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO1.setMaterialLotId(dto.getMaterialLotId());
            List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                            .propertyLimitAssemblePointActualQuery(tenantId, mtAssemblePointActualVO1);
            if (CollectionUtils.isEmpty(assemblePointActualIds)) {
                throw new MtException("MT_ASSEMBLE_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ASSEMBLE_0031", "ASSEMBLE", "【API:assemblePointMaterialUnload】"));
            }

            MtAssemblePointActualVO5 mtAssemblePointActualVO5 = new MtAssemblePointActualVO5();
            mtAssemblePointActualVO5.setAssemblePointId(dto.getAssemblePointId());
            mtAssemblePointActualVO5.setMaterialLotId(dto.getMaterialLotId());
            mtAssemblePointActualVO5.setQty(dto.getQty());
            this.mtAssemblePointActualRepository.assemblePointHaveMaterialVerify(tenantId, mtAssemblePointActualVO5);

            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("ASSEMBLE_POINT_MATERIAL_UNLOAD");
            mtEventCreateVO.setParentEventId(dto.getParentEventId());
            mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
            mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
            mtEventCreateVO.setShiftCode(dto.getShiftCode());
            mtEventCreateVO.setShiftDate(dto.getShiftDate());
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

            for (String assemblePointActualId : assemblePointActualIds) {
                MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
                mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActualVO4.setQty(Double.valueOf(0.0D));
                mtAssemblePointActualVO4.setFeedingQty(Double.valueOf(0.0D));
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");

                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
                mtMaterialLotVO2.setEventId(eventId);
                mtMaterialLotVO2.setAssemblePointId(null);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                this.mtAssemblePointActualRepository.assemblePointActualDelete(tenantId, assemblePointActualId);
            }
        }
    }

    /**
     * 获取装配组下已装载指定物料的第一个装配点/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param assembleGroupId
     * @param materialId
     * @return
     */
    @Override
    public String firstAvailableLoadingAssemblePointGet(Long tenantId, String assembleGroupId, String materialId) {
        String assemblePointId = null;
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupId", "【API:firstAvailableLoadingAssemblePointGet】"));
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "materialId", "【API:firstAvailableLoadingAssemblePointGet】"));
        }

        MtAssembleGroup mtAssembleGroup =
                        this.mtAssembleGroupRepository.assembleGroupPropertyGet(tenantId, assembleGroupId);
        if (mtAssembleGroup == null) {
            throw new MtException("MT_ASSEMBLE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0012", "ASSEMBLE", "【API:firstAvailableLoadingAssemblePointGet】"));
        }
        if (!"Y".equals(mtAssembleGroup.getAssembleSequenceFlag())) {
            throw new MtException("MT_ASSEMBLE_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0021", "ASSEMBLE", "【API:firstAvailableLoadingAssemblePointGet】"));
        }

        MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
        assemblePointCondition.setAssembleGroupId(assembleGroupId);
        assemblePointCondition.setMaterialId(materialId);
        List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

        if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
            List<MtAssemblePointActualVO1> mtAssemblePointActuals = this.mtAssemblePointActualRepository
                            .assemblePointActualPropertyBatchGet(tenantId, assemblePointActualIds);

            if (CollectionUtils.isNotEmpty(mtAssemblePointActuals)) {
                mtAssemblePointActuals = mtAssemblePointActuals.stream().filter(c -> {
                    if (null != c.getQty() && BigDecimal.valueOf(c.getQty()).compareTo(BigDecimal.ZERO) != 0) {
                        c.setFeedingMaterialLotSequence(c.getFeedingMaterialLotSequence() == null ? Long.valueOf(0L)
                                        : c.getFeedingMaterialLotSequence());
                        c.setFeedingSequence(
                                        c.getFeedingSequence() == null ? Long.valueOf(0L) : c.getFeedingSequence());
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

            }

            if (CollectionUtils.isNotEmpty(mtAssemblePointActuals)) {
                mtAssemblePointActuals = mtAssemblePointActuals.stream().sorted(Comparator
                                .comparingLong(MtAssemblePointActualVO1::getFeedingSequence)
                                .thenComparing(Comparator.comparingLong(
                                                MtAssemblePointActualVO1::getFeedingMaterialLotSequence)))
                                .collect(Collectors.toList());
                assemblePointId = mtAssemblePointActuals.get(0).getAssemblePointId();
            }
        }
        return assemblePointId;
    }

    /**
     * 顺序控制装配组物料消耗/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assemblePointMaterialSequenceConsume(Long tenantId, MtAssemblePointVO7 dto) {
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "assembleGroupId", "【API:assemblePointMaterialSequenceConsume】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_ASSEMBLE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_0001", "ASSEMBLE",
                                            "materialId", "【API:assemblePointMaterialSequenceConsume】"));
        }

        if (dto.getQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:assemblePointMaterialSequenceConsume】"));
        }

        Double sequenceNeedConsumeQty = dto.getQty();

        String firstAssemblePointId =
                        firstAvailableLoadingAssemblePointGet(tenantId, dto.getAssembleGroupId(), dto.getMaterialId());
        if (StringUtils.isEmpty(firstAssemblePointId)) {
            throw new MtException("MT_ASSEMBLE_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0032", "ASSEMBLE", "【API:assemblePointMaterialSequenceConsume】"));
        }

        Double loadingQty;
        MtAssemblePointActualVO2 mtAssemblePointActualVO2 = new MtAssemblePointActualVO2();
        mtAssemblePointActualVO2.setAssemblePointId(firstAssemblePointId);
        mtAssemblePointActualVO2.setMaterialId(dto.getMaterialId());
        loadingQty = this.mtAssemblePointActualRepository.assemblePointLoadingMaterialQtyGet(tenantId,
                        mtAssemblePointActualVO2);
        loadingQty = loadingQty == null ? Double.valueOf(0.0D) : loadingQty;

        MtAssemblePointVO4 mtAssemblePointVO4 = new MtAssemblePointVO4();
        mtAssemblePointVO4.setAssemblePointId(firstAssemblePointId);
        mtAssemblePointVO4.setMaterialId(dto.getMaterialId());
        mtAssemblePointVO4.setLocatorId(dto.getLocatorId());
        mtAssemblePointVO4.setSiteId(dto.getSiteId());
        mtAssemblePointVO4.setWorkcellId(dto.getWorkcellId());
        mtAssemblePointVO4.setParentEventId(dto.getParentEventId());
        mtAssemblePointVO4.setEventRequestId(dto.getEventRequestId());
        mtAssemblePointVO4.setShiftDate(dto.getShiftDate());
        mtAssemblePointVO4.setShiftCode(dto.getShiftCode());
        if (BigDecimal.valueOf(loadingQty).compareTo(BigDecimal.valueOf(sequenceNeedConsumeQty)) >= 0) {
            mtAssemblePointVO4.setQty(sequenceNeedConsumeQty);
            assemblePointMaterialConsume(tenantId, mtAssemblePointVO4);
        } else {
            mtAssemblePointVO4.setQty(loadingQty);
            assemblePointMaterialConsume(tenantId, mtAssemblePointVO4);

            MtAssemblePointVO7 mtAssemblePointVO7 = new MtAssemblePointVO7();
            BeanUtils.copyProperties(dto, mtAssemblePointVO7);
            mtAssemblePointVO7.setQty(
                            (BigDecimal.valueOf(sequenceNeedConsumeQty).subtract(BigDecimal.valueOf(loadingQty)))
                                            .doubleValue());
            assemblePointMaterialSequenceConsume(tenantId, mtAssemblePointVO7);// 递归调用
        }
    }

    @Override
    public List<MtAssemblePointVO10> propertyLimitAssemblePointPropertyQuery(Long tenantId, MtAssemblePointVO9 dto) {
        List<MtAssemblePointVO10> pointVO10List = new ArrayList<>();
        MtAssemblePoint point = new MtAssemblePoint();
        BeanUtils.copyProperties(dto, point);
        point.setTenantId(tenantId);
        List<MtAssemblePoint> pointList = mtAssemblePointMapper.select(point);

        if (CollectionUtils.isEmpty(pointList)) {
            return Collections.emptyList();
        }
        List<String> assembleGroupIds = pointList.stream().map(MtAssemblePoint::getAssembleGroupId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtAssembleGroup> assembleGroupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(assembleGroupIds)) {
            List<MtAssembleGroup> mtAssembleGroups =
                            mtAssembleGroupRepository.assembleGroupPropertyBatchGet(tenantId, assembleGroupIds);
            // 获取装配组描述和装配组编码编码：
            if (CollectionUtils.isNotEmpty(mtAssembleGroups)) {
                assembleGroupMap = mtAssembleGroups.stream()
                                .collect(Collectors.toMap(t -> t.getAssembleGroupId(), t -> t));
            }
        }

        for (MtAssemblePoint assemblePoint : pointList) {
            MtAssemblePointVO10 pointVO10 = new MtAssemblePointVO10();
            BeanUtils.copyProperties(assemblePoint, pointVO10);
            pointVO10.setAssembleGroupCode(null == assembleGroupMap.get(pointVO10.getAssembleGroupId()) ? null
                            : assembleGroupMap.get(pointVO10.getAssembleGroupId()).getAssembleGroupCode());
            pointVO10.setAssembleGroupDescription(null == assembleGroupMap.get(pointVO10.getAssembleGroupId()) ? null
                            : assembleGroupMap.get(pointVO10.getAssembleGroupId()).getDescription());
            pointVO10List.add(pointVO10);
        }
        return pointVO10List;
    }

}
