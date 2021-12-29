package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosTestPassRateDTO;
import com.ruike.hme.app.service.HmeCosTestPassRateService;
import com.ruike.hme.domain.entity.HmeCosTestPassRate;
import com.ruike.hme.domain.entity.HmeCosTestPassRateHis;
import com.ruike.hme.domain.repository.HmeCosTestPassRateHisRepository;
import com.ruike.hme.domain.repository.HmeCosTestPassRateRepository;
import com.ruike.hme.domain.vo.HmeCosTestPassRateVO;
import com.ruike.hme.infra.mapper.HmeCosTestPassRateHisMapper;
import com.ruike.hme.infra.mapper.HmeCosTestPassRateMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * COS测试良率维护表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:38
 */
@Service
public class HmeCosTestPassRateServiceImpl extends BaseAppService implements HmeCosTestPassRateService {

    private final HmeCosTestPassRateMapper hmeCosTestPassRateMapper;
    private final HmeCosTestPassRateRepository hmeCosTestPassRateRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeCosTestPassRateHisMapper hmeCosTestPassRateHisMapper;
    private final HmeCosTestPassRateHisRepository hmeCosTestPassRateHisRepository;

    public HmeCosTestPassRateServiceImpl(HmeCosTestPassRateMapper hmeCosTestPassRateMapper, HmeCosTestPassRateRepository hmeCosTestPassRateRepository, MtErrorMessageRepository mtErrorMessageRepository, HmeCosTestPassRateHisMapper hmeCosTestPassRateHisMapper, HmeCosTestPassRateHisRepository hmeCosTestPassRateHisRepository) {
        this.hmeCosTestPassRateMapper = hmeCosTestPassRateMapper;
        this.hmeCosTestPassRateRepository = hmeCosTestPassRateRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeCosTestPassRateHisMapper = hmeCosTestPassRateHisMapper;
        this.hmeCosTestPassRateHisRepository = hmeCosTestPassRateHisRepository;
    }


    @Override
    @ProcessLovValue
    public Page<HmeCosTestPassRateVO> queryCosTestPassRate(Long tenantId, HmeCosTestPassRateDTO hmeCosTestPassRateDTO, PageRequest pageRequest) {
        Page<HmeCosTestPassRateVO> page = PageHelper.doPage(pageRequest, () -> hmeCosTestPassRateMapper.queryCosTestPassRate(tenantId, hmeCosTestPassRateDTO));
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHmeCosTestPassRate(Long tenantId, HmeCosTestPassRateVO hmeCosTestPassRateVO) {
        if (StringUtils.isNotBlank(hmeCosTestPassRateVO.getTestId())) {
            HmeCosTestPassRate hmeCosTestPassRate = new HmeCosTestPassRate();
            //数据修改,用cos_type做校验
            hmeCosTestPassRate.setCosType(hmeCosTestPassRateVO.getCosType());
            HmeCosTestPassRate testPassRate = hmeCosTestPassRateRepository.selectOne(hmeCosTestPassRate);
            if (Objects.nonNull(testPassRate)) {
                if (!StringUtils.equals(hmeCosTestPassRateVO.getTestId(), testPassRate.getTestId())) {
                    //该COS类型【${1}】已维护良率,不可重复维护，请检查！
                    throw new MtException("HME_COS_TEST_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_TEST_001", "HME", hmeCosTestPassRate.getCosType()));
                }
            }
            hmeCosTestPassRate.setTestId(hmeCosTestPassRateVO.getTestId());
            hmeCosTestPassRate.setCosType(hmeCosTestPassRateVO.getCosType());
            hmeCosTestPassRate.setTargetPassRate(hmeCosTestPassRateVO.getTargetPassRate());
            hmeCosTestPassRate.setInputPassRate(hmeCosTestPassRateVO.getInputPassRate());
            hmeCosTestPassRate.setRemark(hmeCosTestPassRateVO.getRemark());
            hmeCosTestPassRate.setEnableFlag(hmeCosTestPassRateVO.getEnableFlag());
            HmeCosTestPassRateHis hmeCosTestPassRateHis = new HmeCosTestPassRateHis();
            hmeCosTestPassRateHis.setTenantId(tenantId);
            //设置历史表数据
            hmeCosTestPassRateHis.setTestId(hmeCosTestPassRateVO.getTestId());
            hmeCosTestPassRateHis.setCosType(hmeCosTestPassRateVO.getCosType());
            hmeCosTestPassRateHis.setTargetPassRate(hmeCosTestPassRateVO.getTargetPassRate());
            hmeCosTestPassRateHis.setInputPassRate(hmeCosTestPassRateVO.getInputPassRate());
            hmeCosTestPassRateHis.setRemark(hmeCosTestPassRateVO.getRemark());
            hmeCosTestPassRateHis.setEnableFlag(hmeCosTestPassRateVO.getEnableFlag());

            //更新主表
            hmeCosTestPassRateMapper.updateByPrimaryKeySelective(hmeCosTestPassRate);
            //插入历史表
            hmeCosTestPassRateHisRepository.insertSelective(hmeCosTestPassRateHis);
        } else {
            //数据保存，用cos_type做校验
            HmeCosTestPassRate hmeCosTestPassRate = new HmeCosTestPassRate();
            hmeCosTestPassRate.setCosType(hmeCosTestPassRateVO.getCosType());
            HmeCosTestPassRate testPassRate = hmeCosTestPassRateRepository.selectOne(hmeCosTestPassRate);
            if (Objects.nonNull(testPassRate)) {
                //该COS类型【${1}】已维护良率,不可重复维护，请检查！
                throw new MtException("HME_COS_TEST_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_TEST_001", "HME", hmeCosTestPassRate.getCosType()));

            }
            hmeCosTestPassRate.setTenantId(tenantId);
            hmeCosTestPassRate.setCosType(hmeCosTestPassRateVO.getCosType());
            hmeCosTestPassRate.setTargetPassRate(hmeCosTestPassRateVO.getTargetPassRate());
            hmeCosTestPassRate.setInputPassRate(hmeCosTestPassRateVO.getInputPassRate());
            hmeCosTestPassRate.setRemark(hmeCosTestPassRateVO.getRemark());
            hmeCosTestPassRate.setEnableFlag(hmeCosTestPassRateVO.getEnableFlag());
            //将数据插入到主表
            hmeCosTestPassRateRepository.insertSelective(hmeCosTestPassRate);
            //设置历史数据
            HmeCosTestPassRateHis hmeCosTestPassRateHis = new HmeCosTestPassRateHis();
            hmeCosTestPassRateHis.setCosType(hmeCosTestPassRateVO.getCosType());
            //对历史表设置主表主键
            hmeCosTestPassRateHis.setTestId(hmeCosTestPassRate.getTestId());
            hmeCosTestPassRateHis.setTenantId(tenantId);
            hmeCosTestPassRateHis.setTargetPassRate(hmeCosTestPassRateVO.getTargetPassRate());
            hmeCosTestPassRateHis.setInputPassRate(hmeCosTestPassRateVO.getInputPassRate());
            hmeCosTestPassRateHis.setRemark(hmeCosTestPassRateVO.getRemark());
            hmeCosTestPassRateHis.setEnableFlag(hmeCosTestPassRateVO.getEnableFlag());
            //将数据插入到历史表
            hmeCosTestPassRateHisRepository.insertSelective(hmeCosTestPassRateHis);
        }
    }
}
