package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import tarzan.actual.domain.entity.MtAssembleGroupActual;
import tarzan.actual.domain.repository.MtAssembleGroupActualRepository;
import tarzan.actual.domain.repository.MtAssemblePointActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO1;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO2;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO4;
import tarzan.actual.domain.vo.MtAssembleGroupActualVO5;
import tarzan.actual.domain.vo.MtAssemblePointActualVO1;
import tarzan.actual.domain.vo.MtAssemblePointActualVO4;
import tarzan.actual.domain.vo.MtEoComponentActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.repository.MtAssembleControlRepository;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.repository.MtAssemblePointControlRepository;
import tarzan.method.domain.repository.MtAssemblePointRepository;
import tarzan.method.domain.vo.MtAssembleControlVO1;
import tarzan.method.domain.vo.MtAssembleGroupVO1;
import tarzan.method.domain.vo.MtAssembleGroupVO2;
import tarzan.method.domain.vo.MtAssembleGroupVO3;
import tarzan.method.domain.vo.MtAssembleGroupVO4;
import tarzan.method.domain.vo.MtAssembleGroupVO5;
import tarzan.method.domain.vo.MtAssemblePointControlVO4;
import tarzan.method.domain.vo.MtAssemblePointControlVO5;
import tarzan.method.domain.vo.MtAssemblePointVO4;
import tarzan.method.domain.vo.MtAssemblePointVO7;
import tarzan.method.infra.mapper.MtAssembleGroupMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 装配组，标识一个装载设备或一类装配关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@Component
public class MtAssembleGroupRepositoryImpl extends BaseRepositoryImpl<MtAssembleGroup>
                implements MtAssembleGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleGroupMapper mtAssembleGroupMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtAssembleGroupActualRepository mtAssembleGroupActualRepository;

    @Autowired
    private MtAssemblePointActualRepository mtAssemblePointActualRepository;

    @Autowired
    private MtAssembleControlRepository mtAssembleControlRepository;

    @Autowired
    private MtAssemblePointControlRepository mtAssemblePointControlRepository;

    @Autowired
    private MtAssemblePointRepository mtAssemblePointRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Override
    public List<String> propertyLimitAssembleGroupQuery(Long tenantId, MtAssembleGroup dto) {
        if (null == dto.getSiteId() && null == dto.getAssembleGroupCode() && null == dto.getDescription()
                        && null == dto.getAutoInstallPointFlag() && null == dto.getAssembleControlFlag()
                        && null == dto.getAssembleSequenceFlag() && null == dto.getAssembleGroupStatus()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0004",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0004",
                                            "ASSEMBLE_CONTROL", "【API:propertyLimitAssembleGroupQuery】"));
        }

        List<MtAssembleGroup> mtAssembleGroups = this.mtAssembleGroupMapper.mySelect(tenantId, dto);
        if (CollectionUtils.isEmpty(mtAssembleGroups)) {
            return Collections.emptyList();
        }
        return mtAssembleGroups.stream().map(MtAssembleGroup::getAssembleGroupId).collect(Collectors.toList());
    }

    @Override
    public MtAssembleGroup assembleGroupPropertyGet(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assembleGroupPropertyGet】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(assembleGroupId);
        return this.mtAssembleGroupMapper.selectOne(mtAssembleGroup);
    }

    /**
     * assembleGroupPropertyBatchGet-批量获取装配组属性
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param assembleGroupIds
     * @return java.util.List<hmes.assemble_group.dto.MtAssembleGroup>
     */
    @Override
    public List<MtAssembleGroup> assembleGroupPropertyBatchGet(Long tenantId, List<String> assembleGroupIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(assembleGroupIds)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupIds",
                                            "【API:assembleGroupPropertyBatchGet】"));
        }
        return mtAssembleGroupMapper.selectByIdsCustom(tenantId, assembleGroupIds);
    }

    @Override
    public void assembleGroupAvailableValidate(Long tenantId, String assembleGroupId) {
        if (StringUtils.isEmpty(assembleGroupId)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupAvailableValidate】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(assembleGroupId);
        mtAssembleGroup = this.mtAssembleGroupMapper.selectOne(mtAssembleGroup);
        if (null == mtAssembleGroup) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupAvailableValidate】"));
        }

        if (!("RELEASED".equals(mtAssembleGroup.getAssembleGroupStatus())
                        || "WORKING".equals(mtAssembleGroup.getAssembleGroupStatus()))) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0011",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0011",
                                            "ASSEMBLE_CONTROL", "【API:assembleGroupAvailableValidate】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assembleGroupUpdate(Long tenantId, MtAssembleGroup dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "siteId", "【API:assembleGroupUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleGroupCode())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupCode", "【API:assembleGroupUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleGroupStatus())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupStatus", "【API:assembleGroupUpdate】"));
        }

        List<String> standardFlag = Arrays.asList("", "Y", "N");
        if (null != dto.getAutoInstallPointFlag() && !standardFlag.contains(dto.getAutoInstallPointFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0020",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0020",
                                            "ASSEMBLE_CONTROL", "autoInstallPointFlag", "【API:assembleGroupUpdate】"));
        }
        if (null != dto.getAssembleControlFlag() && !standardFlag.contains(dto.getAssembleControlFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0020",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0020",
                                            "ASSEMBLE_CONTROL", "assembleControlFlag", "【API:assembleGroupUpdate】"));
        }
        if (null != dto.getAssembleSequenceFlag() && !standardFlag.contains(dto.getAssembleSequenceFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0020",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0020",
                                            "ASSEMBLE_CONTROL", "assembleSequenceFlag", "【API:assembleGroupUpdate】"));
        }

        MtModSite modSite = this.mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (null == modSite || !"Y".equals(modSite.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "siteId", "【API:assembleGroupUpdate】"));
        }

        MtGenStatus mtGenStatus = this.mtGenStatusRepository.getGenStatus(tenantId, "ASSEMBLE_CONTROL",
                        "ASSEMBLE_GROUP", dto.getAssembleGroupStatus());
        if (null == mtGenStatus) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assembleGroupStatus", "【API:assembleGroupUpdate】"));
        }

        // 传入空默认为 N
        if (StringUtils.isEmpty(dto.getAutoInstallPointFlag())) {
            dto.setAutoInstallPointFlag("N");
        }
        if (StringUtils.isEmpty(dto.getAssembleControlFlag())) {
            dto.setAssembleControlFlag("N");
        }
        if (StringUtils.isEmpty(dto.getAssembleSequenceFlag())) {
            dto.setAssembleSequenceFlag("N");
        }

        List<String> flag = new ArrayList<String>();
        flag.add(dto.getAutoInstallPointFlag());
        flag.add(dto.getAssembleControlFlag());
        flag.add(dto.getAssembleSequenceFlag());
        if (1 < flag.stream().filter("Y"::equals).count()) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0018", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "MT_ASSEMBLE_CONTROL_0018", "ASSEMBLE_CONTROL", "【API:assembleGroupUpdate】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setSiteId(dto.getSiteId());
        mtAssembleGroup.setAssembleGroupCode(dto.getAssembleGroupCode());
        mtAssembleGroup.setDescription(dto.getDescription());
        mtAssembleGroup.setAutoInstallPointFlag(dto.getAutoInstallPointFlag());
        mtAssembleGroup.setAssembleControlFlag(dto.getAssembleControlFlag());
        mtAssembleGroup.setAssembleSequenceFlag(dto.getAssembleSequenceFlag());
        mtAssembleGroup.setAssembleGroupStatus(dto.getAssembleGroupStatus());

        Criteria criteria = new Criteria(mtAssembleGroup);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtAssembleGroup.FIELD_TENANT_ID, Comparison.EQUAL));
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            whereFields.add(new WhereField(MtAssembleGroup.FIELD_ASSEMBLE_GROUP_CODE, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
            if (CollectionUtils.isNotEmpty(mtAssembleGroupMapper.selectOptional(mtAssembleGroup, criteria))) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0024",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0024",
                                                "ASSEMBLE_CONTROL", "MT_ASSEMBLE_GROUP", "ASSEMBLE_GROUP_CODE",
                                                "【API:assembleGroupUpdate】"));
            }
            self().insertSelective(mtAssembleGroup);
        } else {
            whereFields.add(new WhereField(MtAssembleGroup.FIELD_ASSEMBLE_GROUP_ID, Comparison.EQUAL));
            criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
            if (CollectionUtils.isEmpty(mtAssembleGroupMapper.selectOptional(mtAssembleGroup, criteria))) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assembleGroupUpdate】"));
            }
            mtAssembleGroup.setAssembleGroupId(dto.getAssembleGroupId());
            self().updateByPrimaryKeySelective(mtAssembleGroup);
        }
        return mtAssembleGroup.getAssembleGroupId();
    }

    /**
     * 将装配组从指定工作单元中取消安装/sen.luo 2018-03-21
     * 
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wkcAssembleGroupSetupCancel(Long tenantId, MtAssembleGroupVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assembleGroupId", "【API:wkcAssembleGroupSetupCancel】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:wkcAssembleGroupSetupCancel】"));
        }

        // 2. 获取装配组实绩assembleGroupActualId
        MtAssembleGroupActualVO1 mtAssembleGroupActualVO1 = new MtAssembleGroupActualVO1();
        mtAssembleGroupActualVO1.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroupActualVO1.setWorkcellId(dto.getWorkcellId());
        List<String> assembleGroupActualIds = this.mtAssembleGroupActualRepository
                        .propertyLimitAssembleGroupActualQuery(tenantId, mtAssembleGroupActualVO1);
        if (CollectionUtils.isEmpty(assembleGroupActualIds)) {
            throw new MtException("MT_ASSEMBLE_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0020", "ASSEMBLE", "【API:wkcAssembleGroupSetupCancel】"));
        }

        // 3. 获取安装取消事件并记录取消安装前装配组和装配点实绩
        // 3.1. 生成事件
        MtEventCreateVO MtEventCreateVO = new MtEventCreateVO();
        MtEventCreateVO.setEventTypeCode("ASSEMBLE_GROUP_SETUP_CANCEL");
        MtEventCreateVO.setParentEventId(dto.getParentEventId());
        MtEventCreateVO.setEventRequestId(dto.getEventRequestId());
        MtEventCreateVO.setWorkcellId(dto.getWorkcellId());
        MtEventCreateVO.setShiftCode(dto.getShiftCode());
        MtEventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, MtEventCreateVO);

        // 3.2. 记录装配组取消安装前装配点实绩历史
        MtAssemblePointActualVO1 assemblePointCondition = new MtAssemblePointActualVO1();
        assemblePointCondition.setAssembleGroupId(dto.getAssembleGroupId());
        assemblePointCondition.setWorkcellId(dto.getWorkcellId());
        List<String> assemblePointActualIds = this.mtAssemblePointActualRepository
                        .propertyLimitAssemblePointActualQuery(tenantId, assemblePointCondition);

        if (CollectionUtils.isNotEmpty(assemblePointActualIds)) {
            MtAssemblePointActualVO4 mtAssemblePointActualVO4 = new MtAssemblePointActualVO4();
            for (String assemblePointActualId : assemblePointActualIds) {
                mtAssemblePointActualVO4.setAssemblePointActualId(assemblePointActualId);
                mtAssemblePointActualVO4.setEventId(eventId);
                this.mtAssemblePointActualRepository.assemblePointActualUpdate(tenantId, mtAssemblePointActualVO4, "N");
            }
        } else {
            MtAssembleGroupActualVO5 mtAssembleGroupActualVO5 = new MtAssembleGroupActualVO5();
            for (String assembleGroupActualId : assembleGroupActualIds) {
                mtAssembleGroupActualVO5.setAssembleGroupActualId(assembleGroupActualId);
                mtAssembleGroupActualVO5.setEventId(eventId);
                this.mtAssembleGroupActualRepository.assembleGroupActualUpdate(tenantId, mtAssembleGroupActualVO5);
            }
        }

        // 4. 删除取消安装的装配组实绩
        for (String assembleGroupActualId : assembleGroupActualIds) {
            mtAssembleGroupActualRepository.assembleGroupActualDelete(tenantId, assembleGroupActualId);
        }
    }

    /**
     * 将装配组安装到工作单元/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wkcAssembleGroupSetup(Long tenantId, MtAssembleGroupVO1 dto) {
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "assembleGroupId", "【API:wkcAssembleGroupSetup】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:wkcAssembleGroupSetup】"));
        }

        MtAssembleGroupActualVO1 condition = new MtAssembleGroupActualVO1();
        condition.setAssembleGroupId(dto.getAssembleGroupId());
        List<String> list =
                        this.mtAssembleGroupActualRepository.propertyLimitAssembleGroupActualQuery(tenantId, condition);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new MtException("MT_ASSEMBLE_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0019", "ASSEMBLE", "【API:wkcAssembleGroupSetup】"));
        }

        MtEventCreateVO MtEventCreateVO = new MtEventCreateVO();
        MtEventCreateVO.setEventTypeCode("ASSEMBLE_GROUP_SETUP");
        MtEventCreateVO.setParentEventId(dto.getParentEventId());
        MtEventCreateVO.setEventRequestId(dto.getEventRequestId());
        MtEventCreateVO.setWorkcellId(dto.getWorkcellId());
        MtEventCreateVO.setShiftCode(dto.getShiftCode());
        MtEventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, MtEventCreateVO);

        MtAssembleGroupActualVO5 mtAssembleGroupActualVO5 = new MtAssembleGroupActualVO5();
        mtAssembleGroupActualVO5.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroupActualVO5.setWorkcellId(dto.getWorkcellId());
        mtAssembleGroupActualVO5.setEventId(eventId);
        this.mtAssembleGroupActualRepository.assembleGroupActualUpdate(tenantId, mtAssembleGroupActualVO5);
    }

    /**
     * 装配组物料消耗/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assembleGroupMaterialConsume(Long tenantId, MtAssembleGroupVO2 dto) {
        // 第一步，判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "workcellId", "【API:assembleGroupMaterialConsume】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_ASSEMBLE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0001", "ASSEMBLE", "qty", "【API:assembleGroupMaterialConsume】"));
        }
        // 第二步，获取工作单元上已安装的装配组
        List<MtAssembleGroupActualVO2> mtAssembleGroupActualVO2s = this.mtAssembleGroupActualRepository
                        .wkcLimitCurrentAssembleGroupQuery(tenantId, dto.getWorkcellId());

        if (CollectionUtils.isEmpty(mtAssembleGroupActualVO2s)) {
            throw new MtException("MT_ASSEMBLE_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0034", "ASSEMBLE", "【API:assembleGroupMaterialConsume】"));
        }
        // 第三步,获取装配组的控制属性
        List<String> assembleGroupIds = mtAssembleGroupActualVO2s.stream()
                        .map(MtAssembleGroupActualVO2::getAssembleGroupId).collect(Collectors.toList());
        List<MtAssembleGroup> mtAssembleGroups = assembleGroupPropertyBatchGet(tenantId, assembleGroupIds);
        // 新增报错
        if (CollectionUtils.isEmpty(mtAssembleGroups)) {
            throw new MtException("MT_ASSEMBLE_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0075", "ASSEMBLE", "【API:assembleGroupMaterialConsume】"));
        }
        for (MtAssembleGroup mtAssembleGroup : mtAssembleGroups) {
            if ("Y".equals(mtAssembleGroup.getAssembleControlFlag())) {
                MtAssembleGroupActualVO4 mtAssembleGroupActualVO4 = new MtAssembleGroupActualVO4();
                mtAssembleGroupActualVO4.setWorkcellId(dto.getWorkcellId());
                mtAssembleGroupActualVO4.setEoId(dto.getEoId());
                mtAssembleGroupActualVO4.setReferenceArea(dto.getReferenceArea());
                mtAssembleGroupActualVO4.setAssembleGroupId(mtAssembleGroup.getAssembleGroupId());
                this.mtAssembleGroupActualRepository.wkcAssembleGroupControlVerify(tenantId, mtAssembleGroupActualVO4);

                MtAssembleControlVO1 mtAssembleControlVO1 = new MtAssembleControlVO1();
                mtAssembleControlVO1.setObjectType("EO");
                mtAssembleControlVO1.setObjectId(dto.getEoId());
                mtAssembleControlVO1.setOrganizationType("WORKCELL");
                mtAssembleControlVO1.setOrganizationId(dto.getWorkcellId());
                mtAssembleControlVO1.setReferenceArea(dto.getReferenceArea());
                String assembleControlId =
                                this.mtAssembleControlRepository.assembleControlGet(tenantId, mtAssembleControlVO1);
                if (StringUtils.isEmpty(assembleControlId)) {
                    throw new MtException("MT_ASSEMBLE_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0035", "ASSEMBLE", "【API:assembleGroupMaterialConsume】"));
                }

                MtAssemblePointControlVO5 mtAssemblePointControlVO5 = new MtAssemblePointControlVO5();
                mtAssemblePointControlVO5.setAssembleControlId(assembleControlId);
                mtAssemblePointControlVO5.setAssembleGroupId(mtAssembleGroup.getAssembleGroupId());
                mtAssemblePointControlVO5.setMaterialId(dto.getMaterialId());
                List<MtAssemblePointControlVO4> mtAssemblePointControlVO4s = this.mtAssemblePointControlRepository
                                .assembleGroupLimitAssembleControlPropertyQuery(tenantId, mtAssemblePointControlVO5);
                if (CollectionUtils.isEmpty(mtAssemblePointControlVO4s)) {
                    throw new MtException("MT_ASSEMBLE_0035", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_ASSEMBLE_0035", "ASSEMBLE", "【API:assembleGroupMaterialConsume】"));
                }

                for (MtAssemblePointControlVO4 mtAssemblePointControlVO : mtAssemblePointControlVO4s) {
                    MtAssemblePointVO4 mtAssemblePointVO4 = new MtAssemblePointVO4();
                    mtAssemblePointVO4.setAssemblePointId(mtAssemblePointControlVO.getAssemblePointId());
                    mtAssemblePointVO4.setMaterialId(mtAssemblePointControlVO.getMaterialId());
                    mtAssemblePointVO4.setMaterialLotId(null);
                    Double unitQty = mtAssemblePointControlVO.getUnitQty() == null ? Double.valueOf(0.0D)
                                    : mtAssemblePointControlVO.getUnitQty();
                    mtAssemblePointVO4.setQty(BigDecimal.valueOf(dto.getQty()).multiply(BigDecimal.valueOf(unitQty))
                                    .doubleValue());

                    MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                    mtEoComponentVO.setEoId(dto.getEoId());
                    mtEoComponentVO.setMaterialId(mtAssemblePointControlVO.getMaterialId());
                    String locatorId = this.mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId,
                                    mtEoComponentVO);
                    mtAssemblePointVO4.setLocatorId(locatorId);

                    MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                    if (null != mtEo) {
                        mtAssemblePointVO4.setSiteId(mtEo.getSiteId());
                    }

                    mtAssemblePointVO4.setWorkcellId(dto.getWorkcellId());
                    mtAssemblePointVO4.setParentEventId(dto.getParentEventId());
                    mtAssemblePointVO4.setEventRequestId(dto.getEventRequestId());
                    mtAssemblePointVO4.setShiftDate(dto.getShiftDate());
                    mtAssemblePointVO4.setShiftCode(dto.getShiftCode());
                    this.mtAssemblePointRepository.assemblePointMaterialConsume(tenantId, mtAssemblePointVO4);
                }
            } else {
                MtAssemblePointVO7 mtAssemblePointVO = new MtAssemblePointVO7();
                mtAssemblePointVO.setAssembleGroupId(mtAssembleGroup.getAssembleGroupId());
                mtAssemblePointVO.setMaterialId(dto.getMaterialId());
                mtAssemblePointVO.setQty(dto.getQty());

                if (StringUtils.isNotEmpty(dto.getEoId())) {
                    MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                    mtEoComponentVO.setEoId(dto.getEoId());
                    mtEoComponentVO.setMaterialId(dto.getMaterialId());
                    String locatorId = this.mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId,
                                    mtEoComponentVO);
                    mtAssemblePointVO.setLocatorId(locatorId);

                    MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                    if (null != mtEo) {
                        mtAssemblePointVO.setSiteId(mtEo.getSiteId());
                    }
                }
                mtAssemblePointVO.setWorkcellId(dto.getWorkcellId());
                mtAssemblePointVO.setParentEventId(dto.getParentEventId());
                mtAssemblePointVO.setEventRequestId(dto.getEventRequestId());
                mtAssemblePointVO.setShiftDate(dto.getShiftDate());
                mtAssemblePointVO.setShiftCode(dto.getShiftCode());
                this.mtAssemblePointRepository.assemblePointMaterialSequenceConsume(tenantId, mtAssemblePointVO);
            }
        }
    }

    /**
     * assembleGroupStatusUpdate-装配组状态变更校验
     *
     * @Author lxs
     * @Date 2019/4/15
     * @Params [tenantId, dto]
     * @Return void
     */
    @Override
    public void assembleGroupStatusUpdateVerify(Long tenantId, MtAssembleGroupVO3 dto) {
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupStatusUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getTargetStatus())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "targetStatus",
                                            "【API:assembleGroupStatusUpdateVerify】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroup = mtAssembleGroupMapper.selectOne(mtAssembleGroup);
        if (mtAssembleGroup == null) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleGroupId",
                                            "【API:assembleGroupStatusUpdateVerify】"));
        }
        // 当前状态
        String currentStatus = mtAssembleGroup.getAssembleGroupStatus();

        // 校验API返回的statusCode中是否包含[input2]的值。
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("ASSEMBLE_CONTROL");
        mtGenStatusVO2.setStatusGroup("ASSEMBLE_GROUP");
        List<MtGenStatus> mtGenStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);

        if (mtGenStatuses.stream().filter(c -> c.getStatusCode().equals(dto.getTargetStatus())).count() == 0) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "targetStatus",
                                            "【API:assembleGroupStatusUpdateVerify】"));
        }

        List<String> validate1 = Arrays.asList("NEW", "WORKING", "HOLD");
        List<String> validate2 = Arrays.asList("RELEASED", "HOLD");
        List<String> validate3 = Arrays.asList("RELEASED", "WORKING");
        List<String> validate4 = Arrays.asList("RELEASED", "NEW");

        if ("NEW".equals(dto.getTargetStatus()) && !"CLOSED".equals(currentStatus)) {
            // 状态不满足新建要求，只有完成的装配组可以变更为新建状态
            throw new MtException("MT_ASSEMBLE_CONTROL_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0005",
                                            "ASSEMBLE_CONTROL", "【API:assembleGroupStatusUpdateVerify】"));
        }

        if ("RELEASED".equals(dto.getTargetStatus())) {
            // 5-1
            if (!validate1.contains(currentStatus)) {
                // 状态不满足下达要求，只有新建/运行/保留的装配组可以变更为下达状态
                throw new MtException("MT_ASSEMBLE_CONTROL_0006",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0006",
                                                "ASSEMBLE_CONTROL", "【API:assembleGroupStatusUpdateVerify】"));
            }
            // 5-2
            if ("WORKING".equals(currentStatus)) {
                MtAssembleGroupActual mtAssembleGroupActual = mtAssembleGroupActualRepository
                                .assembleGroupActualPropertyGet(tenantId, dto.getAssembleGroupId());
                if (null != mtAssembleGroupActual) {
                    // 装配组已绑定工作单元，无法修改为下达状态
                    throw new MtException("MT_ASSEMBLE_CONTROL_0010",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                    "MT_ASSEMBLE_CONTROL_0010", "ASSEMBLE_CONTROL",
                                                    "【API:assembleGroupStatusUpdateVerify】"));
                }
            }
        }
        if ("WORKING".equals(dto.getTargetStatus()) && !validate2.contains(currentStatus)) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0007",
                                            "ASSEMBLE_CONTROL", "【API:assembleGroupStatusUpdateVerify】"));

        }

        if ("HOLD".equals(dto.getTargetStatus()) && !validate3.contains(currentStatus)) {
            // 状态不满足保留要求，只有下达/运行的装配组可以变更为保留状态
            throw new MtException("MT_ASSEMBLE_CONTROL_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0008",
                                            "ASSEMBLE_CONTROL", "【API:assembleGroupStatusUpdateVerify】"));
        }

        if ("CLOSED".equals(dto.getTargetStatus()) && !validate4.contains(currentStatus)) {
            // 状态不满足关闭要求，只有新建/下达的装配组可以变更为关闭状态
            throw new MtException("MT_ASSEMBLE_CONTROL_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0009",
                                            "ASSEMBLE_CONTROL", "【API:assembleGroupStatusUpdateVerify】"));
        }
    }

    /**
     * assembleGroupStatusUpdate-装配组状态变更
     *
     * @Author lxs
     * @Date 2019/4/15
     * @Params [tenantId, dto]
     * @Return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assembleGroupStatusUpdate(Long tenantId, MtAssembleGroupVO3 dto) {
        assembleGroupStatusUpdateVerify(tenantId, dto);

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroup = mtAssembleGroupMapper.selectOne(mtAssembleGroup);
        mtAssembleGroup.setAssembleGroupStatus(dto.getTargetStatus());
        self().updateByPrimaryKeySelective(mtAssembleGroup);
    }

    @Override
    public List<MtAssembleGroupVO5> propertyLimitAssembleGroupPropertyQuery(Long tenantId, MtAssembleGroupVO4 dto) {
        List<MtAssembleGroupVO5> groupVO5List = new ArrayList<>();
        MtAssembleGroup group = new MtAssembleGroup();
        BeanUtils.copyProperties(dto, group);
        group.setTenantId(tenantId);
        List<MtAssembleGroup> groupList = mtAssembleGroupMapper.select(group);
        if (CollectionUtils.isEmpty(groupList)) {
            return Collections.emptyList();
        }
        List<String> siteIds = groupList.stream().map(MtAssembleGroup::getSiteId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(Collectors.toList());
        Map<String, MtModSite> mtModSiteMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(siteIds)) {
            // 获取站点编码和站点描述
            List<MtModSite> mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
            if (CollectionUtils.isNotEmpty(mtModSites)) {
                mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(t -> t.getSiteId(), t -> t));
            }
        }

        for (MtAssembleGroup assembleGroup : groupList) {
            MtAssembleGroupVO5 groupVO5 = new MtAssembleGroupVO5();
            BeanUtils.copyProperties(assembleGroup, groupVO5);
            groupVO5.setSiteName(null == mtModSiteMap.get(assembleGroup.getSiteId()) ? null
                            : mtModSiteMap.get(assembleGroup.getSiteId()).getSiteName());
            groupVO5.setSiteCode(null == mtModSiteMap.get(assembleGroup.getSiteId()) ? null
                            : mtModSiteMap.get(assembleGroup.getSiteId()).getSiteCode());
            groupVO5.setAssembleGroupDescription(assembleGroup.getDescription());
            groupVO5List.add(groupVO5);
        }
        return groupVO5List;
    }

}
