package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeTagPassRateLineService;
import com.ruike.hme.domain.entity.HmeTagPassRateHeader;
import com.ruike.hme.domain.entity.HmeTagPassRateHeaderHis;
import com.ruike.hme.domain.entity.HmeTagPassRateLine;
import com.ruike.hme.domain.entity.HmeTagPassRateLineHis;
import com.ruike.hme.domain.repository.HmeTagPassRateHeaderHisRepository;
import com.ruike.hme.domain.repository.HmeTagPassRateHeaderRepository;
import com.ruike.hme.domain.repository.HmeTagPassRateLineHisRepository;
import com.ruike.hme.domain.repository.HmeTagPassRateLineRepository;
import com.ruike.hme.domain.vo.HmeTagPassRateLineVO;
import com.ruike.hme.infra.mapper.HmeTagPassRateLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 偏振度和发散角良率维护行表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:37
 */
@Service
public class HmeTagPassRateLineServiceImpl extends BaseAppService implements HmeTagPassRateLineService {

    private final HmeTagPassRateLineMapper hmeTagPassRateLineMapper;
    private final HmeTagPassRateLineRepository hmeTagPassRateLineRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeTagPassRateHeaderRepository hmeTagPassRateHeaderRepository;
    private final HmeTagPassRateLineHisRepository hmeTagPassRateLineHisRepository;
    private final HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository;

    public HmeTagPassRateLineServiceImpl(HmeTagPassRateLineMapper hmeTagPassRateLineMapper,
                                         HmeTagPassRateLineRepository hmeTagPassRateLineRepository,
                                         MtErrorMessageRepository mtErrorMessageRepository,
                                         HmeTagPassRateHeaderRepository hmeTagPassRateHeaderRepository,
                                         HmeTagPassRateLineHisRepository hmeTagPassRateLineHisRepository, HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository) {
        this.hmeTagPassRateLineMapper = hmeTagPassRateLineMapper;
        this.hmeTagPassRateLineRepository = hmeTagPassRateLineRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeTagPassRateHeaderRepository = hmeTagPassRateHeaderRepository;
        this.hmeTagPassRateLineHisRepository = hmeTagPassRateLineHisRepository;
        this.hmeTagPassRateHeaderHisRepository = hmeTagPassRateHeaderHisRepository;
    }

    @Override
    public Page<HmeTagPassRateLineVO> queryTagPassRateLine(Long tenantId, String heardId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeTagPassRateLineMapper.queryTagPassRateLine(tenantId, heardId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTagPassRateLine(Long tenantId, HmeTagPassRateLineVO hmeTagPassRateLineVO) {
        //拿到最新的头表历史id

        HmeTagPassRateHeaderHis hmeTagPassRateHeaderHis = new HmeTagPassRateHeaderHis();
        hmeTagPassRateHeaderHis.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
        List<HmeTagPassRateHeaderHis> tagPassRateHeaderHisList = hmeTagPassRateHeaderHisRepository.select(hmeTagPassRateHeaderHis);
        HmeTagPassRateHeaderHis rateHeaderHis = tagPassRateHeaderHisList.stream().max(Comparator.comparing(HmeTagPassRateHeaderHis::getCreationDate)).get();
        //拿到优先级
        Long priority = hmeTagPassRateLineVO.getPriority();

        if (StringUtils.isNotBlank(hmeTagPassRateLineVO.getLineId())) {
            //修改数据
            HmeTagPassRateLine tagPassRateLine = new HmeTagPassRateLine();
            tagPassRateLine.setPriority(hmeTagPassRateLineVO.getPriority());
            tagPassRateLine.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
            HmeTagPassRateLine hmeTagPassRateLine = hmeTagPassRateLineRepository.selectOne(tagPassRateLine);
            //1.检查【优先级】是否存在，若存在，报错“优先级【${1}】已存在，请检查！”
            if (Objects.nonNull(hmeTagPassRateLine) && !StringUtils.equals(hmeTagPassRateLineVO.getLineId(), hmeTagPassRateLine.getLineId())) {
                throw new MtException("HME_COS_TEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_TEST_003",
                        "HME", hmeTagPassRateLine.getPriority().toString()));
            }
            //检查优先级大的【测试总量】中填写的数值是否大于上一优先级中【测试总量】中的数值，是，生成数据，否，报错“测试总量数值填写有误，请检查!
            if (priority == 1L) {
                //优先级为1的，检查头表中【测试数量】的数值
                HmeTagPassRateHeader tagPassRateHeader = new HmeTagPassRateHeader();
                tagPassRateHeader.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
                HmeTagPassRateHeader passRateHeader = hmeTagPassRateHeaderRepository.selectOne(tagPassRateHeader);
                if (hmeTagPassRateLineVO.getTestSumQty() < passRateHeader.getTestQty()) {
                    throw new MtException("HME_COS_TEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_TEST_004", "HME"));
                }
            }
            for (Long i = 1L; i < priority; i++) {
                HmeTagPassRateLine line = new HmeTagPassRateLine();
                line.setPriority(--priority);
                line.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
                //找当前优先级上一优先级数据
                HmeTagPassRateLine passRateLine = hmeTagPassRateLineRepository.selectOne(line);
                if (Objects.nonNull(passRateLine)) {
                    //【测试总量】判断
                    if (hmeTagPassRateLineVO.getTestSumQty() < passRateLine.getTestSumQty()) {
                        throw new MtException("HME_COS_TEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_TEST_004", "HME"));
                    } else {
                        break;
                    }
                } else {
                    continue;
                }
            }
            //修改数据
            HmeTagPassRateLine passRateLine = this.setHmeTagPassRateLine(tenantId, hmeTagPassRateLineVO);
            passRateLine.setLineId(hmeTagPassRateLineVO.getLineId());
            //更新数据
            hmeTagPassRateLineMapper.updateByPrimaryKeySelective(passRateLine);
            //设置历史表值
            HmeTagPassRateLineHis tagPassRateLineHis = this.setHmeTagPassRateLineHis(tenantId, hmeTagPassRateLineVO);
            tagPassRateLineHis.setLineId(hmeTagPassRateLineVO.getLineId());
            tagPassRateLineHis.setHeaderHisId(rateHeaderHis.getHeaderHisId());
            //插入历史表
            hmeTagPassRateLineHisRepository.insertSelective(tagPassRateLineHis);

        } else {
            //新增数据
            HmeTagPassRateLine tagPassRateLine = new HmeTagPassRateLine();
            tagPassRateLine.setPriority(hmeTagPassRateLineVO.getPriority());
            tagPassRateLine.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
            HmeTagPassRateLine hmeTagPassRateLine = hmeTagPassRateLineRepository.selectOne(tagPassRateLine);
            //1.检查【优先级】是否存在，若存在，报错“优先级【${1}】已存在，请检查！”
            if (Objects.nonNull(hmeTagPassRateLine)) {
                throw new MtException("HME_COS_TEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_TEST_003",
                        "HME", hmeTagPassRateLine.getPriority().toString()));
            }
            //检查优先级大的【测试总量】中填写的数值是否大于上一优先级中【测试总量】中的数值，是，生成数据，否，报错“测试总量数值填写有误，请检查!
            if (priority == 1L) {
                //优先级为1的，检查头表中【测试数量】的数值
                HmeTagPassRateHeader tagPassRateHeader = new HmeTagPassRateHeader();
                tagPassRateHeader.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
                HmeTagPassRateHeader passRateHeader = hmeTagPassRateHeaderRepository.selectOne(tagPassRateHeader);
                if (hmeTagPassRateLineVO.getTestSumQty() < passRateHeader.getTestQty()) {
                    throw new MtException("HME_COS_TEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_TEST_004", "HME"));
                }
            }
            for (Long i = 1L; i < priority; i++) {
                HmeTagPassRateLine line = new HmeTagPassRateLine();
                line.setPriority(--priority);
                line.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
                //找当前优先级上一优先级数据
                HmeTagPassRateLine passRateLine = hmeTagPassRateLineRepository.selectOne(line);
                if (Objects.nonNull(passRateLine)) {
                    //【测试总量】判断
                    if (hmeTagPassRateLineVO.getTestSumQty() < passRateLine.getTestSumQty()) {
                        throw new MtException("HME_COS_TEST_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_TEST_004", "HME"));
                    } else {
                        break;
                    }
                } else {
                    continue;
                }
            }
            //行表插入数据
            HmeTagPassRateLine passRateLine = this.setHmeTagPassRateLine(tenantId, hmeTagPassRateLineVO);
            //更新数据
            hmeTagPassRateLineRepository.insertSelective(passRateLine);
            //设置历史表值
            HmeTagPassRateLineHis tagPassRateLineHis = this.setHmeTagPassRateLineHis(tenantId, hmeTagPassRateLineVO);
            tagPassRateLineHis.setLineId(passRateLine.getLineId());
            tagPassRateLineHis.setHeaderHisId(rateHeaderHis.getHeaderHisId());
            //插入历史表
            hmeTagPassRateLineHisRepository.insertSelective(tagPassRateLineHis);
        }

    }

    private HmeTagPassRateLineHis setHmeTagPassRateLineHis(Long tenantId, HmeTagPassRateLineVO hmeTagPassRateLineVO) {
        HmeTagPassRateLineHis hmeTagPassRateLineHis = new HmeTagPassRateLineHis();
        hmeTagPassRateLineHis.setTenantId(tenantId);
        hmeTagPassRateLineHis.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
        hmeTagPassRateLineHis.setLineId(hmeTagPassRateLineVO.getLineId());
        hmeTagPassRateLineHis.setAddPassRate(hmeTagPassRateLineVO.getAddPassRate());
        hmeTagPassRateLineHis.setTestSumQty(hmeTagPassRateLineVO.getTestSumQty());
        hmeTagPassRateLineHis.setPriority(hmeTagPassRateLineVO.getPriority());
        hmeTagPassRateLineHis.setRemark(hmeTagPassRateLineVO.getRemark());
        return hmeTagPassRateLineHis;
    }

    private HmeTagPassRateLine setHmeTagPassRateLine(Long tenantId, HmeTagPassRateLineVO hmeTagPassRateLineVO) {
        HmeTagPassRateLine passRateLine = new HmeTagPassRateLine();
        passRateLine.setTenantId(tenantId);
        passRateLine.setHeaderId(hmeTagPassRateLineVO.getHeaderId());
        passRateLine.setAddPassRate(hmeTagPassRateLineVO.getAddPassRate());
        passRateLine.setTestSumQty(hmeTagPassRateLineVO.getTestSumQty());
        passRateLine.setPriority(hmeTagPassRateLineVO.getPriority());
        passRateLine.setRemark(hmeTagPassRateLineVO.getRemark());
        return passRateLine;
    }
}
