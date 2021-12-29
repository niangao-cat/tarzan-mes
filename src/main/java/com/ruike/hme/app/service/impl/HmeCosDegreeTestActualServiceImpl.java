package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosDegreeTestActualDTO;
import com.ruike.hme.app.service.HmeCosDegreeTestActualService;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActual;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActualHis;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeCosDegreeTestActualHisRepository;
import com.ruike.hme.domain.repository.HmeCosDegreeTestActualRepository;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO2;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosDegreeTestActualMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 偏振度和发散角测试结果应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
@Service
public class HmeCosDegreeTestActualServiceImpl implements HmeCosDegreeTestActualService {

    @Autowired
    private HmeCosDegreeTestActualRepository hmeCosDegreeTestActualRepository;
    @Autowired
    private HmeCosDegreeTestActualMapper hmeCosDegreeTestActualMapper;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private HmeCosDegreeTestActualHisRepository hmeCosDegreeTestActualHisRepository;

    @Override
    public void dopAndDivergenceComputeJob(Long tenantId) {
        //获取上次job的执行时间
        Date lastJobDate = hmeCosDegreeTestActualRepository.getLastJobDate(tenantId);
        //获取此次job需要回写的数据
        List<HmeCosDegreeTestActual> hmeCosDegreeTestActualList = hmeCosDegreeTestActualRepository.getCosDegreeTestActualData(tenantId, lastJobDate);
        //批量回写数据，并记录历史
        if(CollectionUtils.isNotEmpty(hmeCosDegreeTestActualList)){
            //2021
            hmeCosDegreeTestActualRepository.updateCosDegreeTestActualData(tenantId, hmeCosDegreeTestActualList);
        }
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosDegreeTestActualVO3> cosDegreeTestActualPageQuery(Long tenantId, HmeCosDegreeTestActualDTO dto, PageRequest pageRequest) {
        Page<HmeCosDegreeTestActualVO3> resultPage = PageHelper.doPage(pageRequest, () -> hmeCosDegreeTestActualMapper.cosDegreeTestActualPageQuery(tenantId, dto));
        if(CollectionUtils.isNotEmpty(resultPage.getContent())){
            List<Long> releaseByList = resultPage.getContent().stream()
                    .filter(item -> Objects.nonNull(item.getReleaseBy()))
                    .map(HmeCosDegreeTestActualVO3::getReleaseBy).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(releaseByList)){
                Map<Long, MtUserInfo> userInfoMap = mtUserClient.userInfoBatchGet(tenantId, releaseByList);
                for (HmeCosDegreeTestActualVO3 hmeCosDegreeTestActualVO3:resultPage.getContent()) {
                    if(Objects.nonNull(hmeCosDegreeTestActualVO3.getReleaseBy())){
                        MtUserInfo mtUserInfo = userInfoMap.get(hmeCosDegreeTestActualVO3.getReleaseBy());
                        if(Objects.nonNull(mtUserInfo)){
                            hmeCosDegreeTestActualVO3.setReleaseByName(mtUserInfo.getRealName());
                        }
                    }
                }
            }
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cosDegreeTestActualUpdate(Long tenantId, HmeCosDegreeTestActualVO3 dto) {
        HmeCosDegreeTestActual hmeCosDegreeTestActual = hmeCosDegreeTestActualRepository.selectByPrimaryKey(dto.getDegreeTestId());
        hmeCosDegreeTestActual.setTestStatus("RELEASE");
        Date nowDate = new Date();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmeCosDegreeTestActual.setReleaseBy(userId);
        hmeCosDegreeTestActual.setReleaseDate(nowDate);
        hmeCosDegreeTestActualMapper.updateByPrimaryKeySelective(hmeCosDegreeTestActual);
        //记录历史
        HmeCosDegreeTestActualHis hmeCosDegreeTestActualHis = new HmeCosDegreeTestActualHis();
        BeanCopierUtil.copy(hmeCosDegreeTestActual, hmeCosDegreeTestActualHis);
        hmeCosDegreeTestActualHisRepository.insertSelective(hmeCosDegreeTestActualHis);
    }
}
