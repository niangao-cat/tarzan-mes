package tarzan.general.infra.repository.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.general.domain.entity.MtEventRequest;
import tarzan.general.domain.entity.MtEventRequestType;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventRequestVO1;
import tarzan.general.domain.vo.MtEventRequestVO2;
import tarzan.general.domain.vo.MtEventRequestVO3;
import tarzan.general.domain.vo.MtEventRequestVO4;
import tarzan.general.infra.mapper.MtEventRequestMapper;
import tarzan.general.infra.mapper.MtEventRequestTypeMapper;

/**
 * 事件请求记录 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@Component
public class MtEventRequestRepositoryImpl extends BaseRepositoryImpl<MtEventRequest>
        implements MtEventRequestRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRequestMapper mtEventRequestMapper;
    @Autowired
    private MtEventRequestTypeMapper mtEventRequestTypeMapper;

    @Override
    public MtEventRequestVO1 eventRequestGet(Long tenantId, String eventRequestId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eventRequestId)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0001", "EVENT", "eventRequestId", "【API：eventRequestGet】"));
        }

        MtEventRequestVO1 result = new MtEventRequestVO1();

        // 2. 获取EventRequest数据
        MtEventRequest mtEventRequest = new MtEventRequest();
        mtEventRequest.setTenantId(tenantId);
        mtEventRequest.setEventRequestId(eventRequestId);
        mtEventRequest = mtEventRequestMapper.selectOne(mtEventRequest);
        if (mtEventRequest != null) {
            result.setEventRequestId(mtEventRequest.getEventRequestId());
            result.setRequestBy(mtEventRequest.getRequestBy());
            result.setRequestTime(mtEventRequest.getRequestTime());

            MtEventRequestType mtEventRequestType = new MtEventRequestType();
            mtEventRequestType.setTenantId(tenantId);
            mtEventRequestType.setRequestTypeId(mtEventRequest.getRequestTypeId());
            mtEventRequestType = mtEventRequestTypeMapper.selectOne(mtEventRequestType);
            if (mtEventRequestType != null) {
                result.setRequestTypeCode(mtEventRequestType.getRequestTypeCode());
            }
        }

        return result;
    }

    @Override
    public List<String> propertyLimitEventRequestQuery(Long tenantId, MtEventRequestVO2 dto) {
        // 1. 验证参数有效性
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_EVENT_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0002", "EVENT", "【API：propertyLimitEventRequestQuery】"));
        }

        // 2. 获取 eventRequest
        List<MtEventRequest> mtEventRequestList = mtEventRequestMapper.selectByConditionCustom(tenantId, dto);

        if (CollectionUtils.isNotEmpty(mtEventRequestList)) {
            return mtEventRequestList.stream().map(MtEventRequest::getEventRequestId).distinct()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eventRequestCreate(Long tenantId, String requestTypeCode) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(requestTypeCode)) {
            throw new MtException("MT_EVENT_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_EVENT_0001", "EVENT", "requestTypeCode", "【API：eventRequestCreate】"));
        }

        // 2. 获取 requestTypeId
        MtEventRequestType mtEventRequestType = new MtEventRequestType();
        mtEventRequestType.setTenantId(tenantId);
        mtEventRequestType.setRequestTypeCode(requestTypeCode);
        mtEventRequestType = mtEventRequestTypeMapper.selectOne(mtEventRequestType);
        if (mtEventRequestType == null || !"Y".equals(mtEventRequestType.getEnableFlag())) {
            throw new MtException("MT_EVENT_0004",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_EVENT_0004", "EVENT",
                            "requestTypeCode:" + requestTypeCode, "【API：eventRequestCreate】"));
        }

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtEventRequest mtEventRequest = new MtEventRequest();
        mtEventRequest.setTenantId(tenantId);
        mtEventRequest.setRequestBy(userId);
        mtEventRequest.setRequestTime(new Date());
        mtEventRequest.setRequestTypeId(mtEventRequestType.getRequestTypeId());
        self().insertSelective(mtEventRequest);

        return mtEventRequest.getEventRequestId();
    }

    @Override
    public List<MtEventRequestVO4> propertyLimitEventRequestPropertyQuery(Long tenantId, MtEventRequestVO3 dto) {
        return mtEventRequestMapper.selectCondition(tenantId, dto);
    }
}
