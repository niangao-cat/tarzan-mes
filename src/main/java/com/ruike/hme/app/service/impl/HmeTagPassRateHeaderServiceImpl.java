package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeTagPassRateHeaderDTO;
import com.ruike.hme.app.service.HmeTagPassRateHeaderService;
import com.ruike.hme.domain.entity.HmeTagPassRateHeader;
import com.ruike.hme.domain.entity.HmeTagPassRateHeaderHis;
import com.ruike.hme.domain.repository.HmeTagPassRateHeaderHisRepository;
import com.ruike.hme.domain.repository.HmeTagPassRateHeaderRepository;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderVO;
import com.ruike.hme.infra.mapper.HmeTagPassRateHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 偏振度和发散角良率维护头表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:35
 */
@Service
public class HmeTagPassRateHeaderServiceImpl extends BaseAppService implements HmeTagPassRateHeaderService {

    private final HmeTagPassRateHeaderMapper hmeTagPassRateHeaderMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeTagPassRateHeaderRepository tagPassRateHeaderRepository;
    private final HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository;

    public HmeTagPassRateHeaderServiceImpl(HmeTagPassRateHeaderMapper hmeTagPassRateHeaderMapper, MtErrorMessageRepository mtErrorMessageRepository, HmeTagPassRateHeaderRepository tagPassRateHeaderRepository, HmeTagPassRateHeaderHisRepository hmeTagPassRateHeaderHisRepository) {
        this.hmeTagPassRateHeaderMapper = hmeTagPassRateHeaderMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.tagPassRateHeaderRepository = tagPassRateHeaderRepository;
        this.hmeTagPassRateHeaderHisRepository = hmeTagPassRateHeaderHisRepository;
    }


    @Override
    @ProcessLovValue
    public Page<HmeTagPassRateHeaderVO> queryTagPassRateHeader(Long tenantId, HmeTagPassRateHeaderDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeTagPassRateHeaderMapper.queryTagPassRateHeader(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePassRateHeader(Long tenantId, HmeTagPassRateHeaderVO hmeTagPassRateHeaderVO) {
        if (StringUtils.isNotBlank(hmeTagPassRateHeaderVO.getHeaderId())) {
            //修改数据
            HmeTagPassRateHeader tagPassRateHeader = new HmeTagPassRateHeader();
            tagPassRateHeader.setCosType(hmeTagPassRateHeaderVO.getCosType());
            tagPassRateHeader.setTestObject(hmeTagPassRateHeaderVO.getTestObject());
            HmeTagPassRateHeader passRateHeader = tagPassRateHeaderRepository.selectOne(tagPassRateHeader);
            if (Objects.nonNull(passRateHeader)) {
                //检查【cos类型】+【测试对象】是否存在，若存在，报错“cos类型【${1}】的测试对象【${1}】已存在，请检查！
                if (!StringUtils.equals(hmeTagPassRateHeaderVO.getHeaderId(), passRateHeader.getHeaderId())) {
                    throw new MtException("HME_COS_TEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_TEST_002", "HME", passRateHeader.getCosType(), passRateHeader.getTestObject()));
                }
                //修改自己
                HmeTagPassRateHeader hmeTagPassRateHeader = this.setTagPassRateHeader(tenantId, hmeTagPassRateHeaderVO);
                hmeTagPassRateHeader.setHeaderId(hmeTagPassRateHeaderVO.getHeaderId());
                //更新表数据
                hmeTagPassRateHeaderMapper.updateByPrimaryKeySelective(hmeTagPassRateHeader);
                //将修改的数据插入历史头表
                HmeTagPassRateHeaderHis tagPassRateHeaderHis = this.setTagPassRateHeaderHis(tenantId, hmeTagPassRateHeaderVO);
                //设置头id
                tagPassRateHeaderHis.setHeaderId(hmeTagPassRateHeaderVO.getHeaderId());
                //历史表新增
                hmeTagPassRateHeaderHisRepository.insertSelective(tagPassRateHeaderHis);
            }
        } else {
            //新增数据
            HmeTagPassRateHeader tagPassRateHeader = new HmeTagPassRateHeader();
            tagPassRateHeader.setCosType(hmeTagPassRateHeaderVO.getCosType());
            tagPassRateHeader.setTestObject(hmeTagPassRateHeaderVO.getTestObject());
            HmeTagPassRateHeader passRateHeader = tagPassRateHeaderRepository.selectOne(tagPassRateHeader);
            if (Objects.nonNull(passRateHeader)) {
                //检查【cos类型】+【测试对象】是否存在，若存在，报错“cos类型【${1}】的测试对象【${1}】已存在，请检查！
                throw new MtException("HME_COS_TEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_TEST_002", "HME", passRateHeader.getCosType(), passRateHeader.getTestObject()));
            }
            HmeTagPassRateHeader hmeTagPassRateHeader = this.setTagPassRateHeader(tenantId, hmeTagPassRateHeaderVO);
            //插入数据在头表
            tagPassRateHeaderRepository.insertSelective(hmeTagPassRateHeader);
            //插入历史表
            HmeTagPassRateHeaderHis tagPassRateHeaderHis = this.setTagPassRateHeaderHis(tenantId, hmeTagPassRateHeaderVO);
            tagPassRateHeaderHis.setHeaderId(hmeTagPassRateHeader.getHeaderId());
            hmeTagPassRateHeaderHisRepository.insertSelective(tagPassRateHeaderHis);
        }
    }

    private HmeTagPassRateHeaderHis setTagPassRateHeaderHis(Long tenantId, HmeTagPassRateHeaderVO hmeTagPassRateHeaderVO) {
        //此处的数据差一个头id
        HmeTagPassRateHeaderHis tagPassRateHeaderHis = new HmeTagPassRateHeaderHis();
        tagPassRateHeaderHis.setTenantId(tenantId);
        tagPassRateHeaderHis.setCosType(hmeTagPassRateHeaderVO.getCosType());
        tagPassRateHeaderHis.setTestQty(hmeTagPassRateHeaderVO.getTestQty());
        tagPassRateHeaderHis.setPassRate(hmeTagPassRateHeaderVO.getPassRate());
        tagPassRateHeaderHis.setEnableFlag(hmeTagPassRateHeaderVO.getEnableFlag());
        tagPassRateHeaderHis.setRemark(hmeTagPassRateHeaderVO.getRemark());
        tagPassRateHeaderHis.setTestType(hmeTagPassRateHeaderVO.getTestType());
        tagPassRateHeaderHis.setTestObject(hmeTagPassRateHeaderVO.getTestObject());
        return tagPassRateHeaderHis;
    }

    private HmeTagPassRateHeader setTagPassRateHeader(Long tenantId, HmeTagPassRateHeaderVO hmeTagPassRateHeaderVO) {
        //此处的数据差头id
        HmeTagPassRateHeader tagPassRateHeader = new HmeTagPassRateHeader();
        tagPassRateHeader.setTestType(hmeTagPassRateHeaderVO.getTestType());
        tagPassRateHeader.setTestObject(hmeTagPassRateHeaderVO.getTestObject());
        tagPassRateHeader.setTenantId(tenantId);
        tagPassRateHeader.setCosType(hmeTagPassRateHeaderVO.getCosType());
        tagPassRateHeader.setTestQty(hmeTagPassRateHeaderVO.getTestQty());
        tagPassRateHeader.setPassRate(hmeTagPassRateHeaderVO.getPassRate());
        tagPassRateHeader.setEnableFlag(hmeTagPassRateHeaderVO.getEnableFlag());
        tagPassRateHeader.setRemark(hmeTagPassRateHeaderVO.getRemark());
        return tagPassRateHeader;

    }


}
