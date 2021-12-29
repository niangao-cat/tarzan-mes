package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.mybatis.util.StringUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtMemberRoleService;
import tarzan.general.api.dto.MtEventTypeDTO;
import tarzan.general.api.dto.MtEventTypeDTO2;
import tarzan.general.app.service.MtEventTypeService;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventTypeVO;
import tarzan.general.infra.mapper.MtEventTypeMapper;

/**
 * 事件类型定义应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Service
public class MtEventTypeServiceImpl extends BaseServiceImpl<MtEventType> implements MtEventTypeService {

    @Autowired
    private MtEventTypeMapper mapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtMemberRoleService mtMemberRoleService;

    @Autowired
    private MtEventTypeRepository mtEventTypeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEventType saveMtEventType(Long tenantId, MtEventTypeDTO dto) {
        // 判断事件编码是否存在
        MtEventType mtEventType = new MtEventType();
        BeanUtils.copyProperties(dto, mtEventType);
        mtEventType.setTenantId(tenantId);

        //唯一性校验
        Criteria criteria = new Criteria(mtEventType);
        List<WhereField> whereFields2 = new ArrayList<WhereField>();
        if(StringUtils.isNotEmpty(dto.getEventTypeId())){
            whereFields2.add(new WhereField(MtEventType.FIELD_EVENT_TYPE_ID, Comparison.NOT_EQUAL));
        }
        whereFields2.add(new WhereField(MtEventType.FIELD_TENANT_ID,Comparison.EQUAL));
        whereFields2.add(new WhereField(MtEventType.FIELD_EVENT_TYPE_CODE, Comparison.EQUAL));
        criteria.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
        if (mapper.selectOptional(mtEventType, criteria).size() > 0) {
            // 事件类型编码重复，请确认
            throw new MtException("MT_EVENT_0012",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0012", "EVENT"));
        }

        if (StringUtil.isEmpty(mtEventType.getEventTypeId())) {
            this.insertSelective(mtEventType);
        } else {
            // 获取原始事件属性
            MtEventType oldEventType = new MtEventType();
            oldEventType.setTenantId(tenantId);
            oldEventType.setEventTypeId(mtEventType.getEventTypeId());
            oldEventType = mapper.selectOne(oldEventType);

            // 用户角色集合
            // 用户角色集合
            String allowUpdateRole =
                    profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                            DetailsHelper.getUserDetails().getRoleId(), "GEN_INITIALIZE_UPDATE_ROLE");

            // 通过用户ID获取对应角色
            List<String> roleList = new ArrayList<String>();
            ResponseEntity<List<MtRoleVO>> roleResEntity = mtMemberRoleService.listSelfRoles();
            if (null != roleResEntity) {
                List<MtRoleVO> roleVOs = roleResEntity.getBody();
                if (CollectionUtils.isNotEmpty(roleVOs)) {
                    roleList = roleVOs.stream().map(MtRoleVO::getViewCode).collect(Collectors.toList());
                }
            }

            if ("N".equals(oldEventType.getDefaultEventTypeFlag()) && "Y".equals(dto.getDefaultEventTypeFlag())
                            && allowUpdateRole != null) {
                if (!roleList.contains(allowUpdateRole)) {
                    // 仅xxx权限用户允许修改此标识，请确认
                    throw new MtException("MT_EVENT_0013", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_EVENT_0013", "EVENT", allowUpdateRole));
                }
            }
            if (StringUtils.isNotEmpty(oldEventType.getOnhandChangeFlag())
                            && "Y".equals(oldEventType.getOnhandChangeFlag()) && ("N".equals(dto.getOnhandChangeFlag())
                                            || StringUtils.isEmpty(dto.getOnhandChangeFlag()))) {
                mtEventType.setOnhandChangeType("");
            }

            this.updateByPrimaryKey(mtEventType);
        }

        return mtEventType;
    }

    @Override
    public Page<MtEventType> eventTypeQuery(Long tenantId, PageRequest pageRequest, MtEventTypeDTO2 dto) {
        return PageHelper.doPageAndSort(pageRequest, () -> mapper.selectEventType(tenantId, dto));
    }

    @Override
    public List<String> propertyLimitEventTypeQueryUi(Long tenantId, MtEventTypeVO dto) {
        return mtEventTypeRepository.propertyLimitEventTypeQuery(tenantId, dto);
    }
}
