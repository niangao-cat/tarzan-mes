package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosSelectionCurrentDTO;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrentHis;
import com.ruike.hme.domain.repository.HmeCosSelectionCurrentHisRepository;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2;
import com.ruike.hme.infra.mapper.HmeCosSelectionCurrentMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrent;
import com.ruike.hme.domain.repository.HmeCosSelectionCurrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * COS筛选电流点维护表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
@Component
public class HmeCosSelectionCurrentRepositoryImpl extends BaseRepositoryImpl<HmeCosSelectionCurrent> implements HmeCosSelectionCurrentRepository {

    @Autowired
    private HmeCosSelectionCurrentMapper hmeCosSelectionCurrentMapper;
    @Autowired
    private HmeCosSelectionCurrentHisRepository hmeCosSelectionCurrentHisRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserClient userClient;

    @Override
    @ProcessLovValue
    public Page<HmeCosSelectionCurrentVO> cosSelectionCurrentPageQuery(Long tenantId, HmeCosSelectionCurrentDTO dto, PageRequest pageRequest) {
        Page<HmeCosSelectionCurrentVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeCosSelectionCurrentMapper.cosSelectionCurrentPageQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cosSelectionCurrentCreateOrUpdate(Long tenantId, HmeCosSelectionCurrentVO dto) {
        if(StringUtils.isEmpty(dto.getCosId())){
            //唯一性校验
            HmeCosSelectionCurrent hmeCosSelectionCurrentQuery = this.selectOne(new HmeCosSelectionCurrent() {{
                setTenantId(tenantId);
                setCosType(dto.getCosType());
            }});
            if(Objects.nonNull(hmeCosSelectionCurrentQuery)){
                throw new MtException("HME_SELECTION_CURRENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SELECTION_CURRENT_0001", "HME", dto.getCosType()));
            }
            //新增
            HmeCosSelectionCurrent hmeCosSelectionCurrent = new HmeCosSelectionCurrent();
            hmeCosSelectionCurrent.setTenantId(tenantId);
            BeanCopierUtil.copy(dto, hmeCosSelectionCurrent);
            this.insertSelective(hmeCosSelectionCurrent);
            //记录历史
            HmeCosSelectionCurrentHis hmeCosSelectionCurrentHis = new HmeCosSelectionCurrentHis();
            BeanCopierUtil.copy(hmeCosSelectionCurrent, hmeCosSelectionCurrentHis);
            hmeCosSelectionCurrentHisRepository.insertSelective(hmeCosSelectionCurrentHis);
        }else {
            //更新
            HmeCosSelectionCurrent hmeCosSelectionCurrent = this.selectByPrimaryKey(dto.getCosId());
            if(Objects.nonNull(hmeCosSelectionCurrent)){
                BeanCopierUtil.copy(dto, hmeCosSelectionCurrent);
                hmeCosSelectionCurrentMapper.updateByPrimaryKey(hmeCosSelectionCurrent);
                //记录历史
                HmeCosSelectionCurrentHis hmeCosSelectionCurrentHis = new HmeCosSelectionCurrentHis();
                BeanCopierUtil.copy(hmeCosSelectionCurrent, hmeCosSelectionCurrentHis);
                hmeCosSelectionCurrentHisRepository.insertSelective(hmeCosSelectionCurrentHis);
            }
        }
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosSelectionCurrentVO2> cosSelectionCurrentHisPageQuery(Long tenantId, String cosId, PageRequest pageRequest) {
        Page<HmeCosSelectionCurrentVO2> resultPage = PageHelper.doPage(pageRequest, () -> hmeCosSelectionCurrentMapper.cosSelectionCurrentHisPageQuery(tenantId, cosId));
        if(CollectionUtils.isNotEmpty(resultPage.getContent())){
            List<Long> userIdList = new ArrayList<>();
            userIdList.addAll(resultPage.getContent().stream().map(HmeCosSelectionCurrentVO2::getCreatedBy).distinct().collect(Collectors.toList()));
            userIdList.addAll(resultPage.getContent().stream().map(HmeCosSelectionCurrentVO2::getLastUpdatedBy).distinct().collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(userIdList)){
                userIdList = userIdList.stream().distinct().collect(Collectors.toList());
                Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, userIdList);
                resultPage.getContent().forEach(item -> {
                    item.setCreatedByName(userMap.getOrDefault(item.getCreatedBy(), new MtUserInfo()).getRealName());
                    item.setLastUpdatedByName(userMap.getOrDefault(item.getLastUpdatedBy(), new MtUserInfo()).getRealName());
                });
            }
        }
        return resultPage;
    }
}
