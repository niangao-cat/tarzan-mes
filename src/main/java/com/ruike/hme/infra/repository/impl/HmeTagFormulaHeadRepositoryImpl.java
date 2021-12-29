package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import com.ruike.hme.infra.mapper.HmeTagFormulaHeadMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据采集项公式头表 资源库实现
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
@Component
public class HmeTagFormulaHeadRepositoryImpl extends BaseRepositoryImpl<HmeTagFormulaHead> implements HmeTagFormulaHeadRepository {


    @Autowired
    private HmeTagFormulaHeadMapper hmeTagFormulaHeadMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;


    @Override
    public Page<HmeTagFormulaHeadVO> getHeadList(Long tenantId,String operationId, String tagGroupId, String tagId, PageRequest pageRequest) {
        Page<HmeTagFormulaHeadVO> page = PageHelper.doPage(pageRequest,() -> hmeTagFormulaHeadMapper.getTagHeadList(tenantId, operationId,  tagGroupId,  tagId));
        return page;
    }

    /**
     *
     * @Description 工序作业平台查询头数据
     *
     * @author yuchao.wang
     * @date 2020/9/24 10:52
     * @param headQuery 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     *
     */
    @Override
    public List<HmeTagFormulaHead> selectHeadForCalculation(HmeTagFormulaHead headQuery) {
        return hmeTagFormulaHeadMapper.selectHeadForCalculation(headQuery);
    }

    @Override
    public void createOrUpdateHead(Long tenantId, HmeTagFormulaHead hmeTagFormulaHead) {
        List<HmeTagFormulaHead> headList = hmeTagFormulaHeadMapper.queryHmeTagFormulaHead(tenantId, hmeTagFormulaHead.getOperationId(), hmeTagFormulaHead.getTagGroupId(), hmeTagFormulaHead.getTagId());

        if(StringUtils.isBlank(hmeTagFormulaHead.getTagFormulaHeadId())){
            //保存
            if(CollectionUtils.isNotEmpty(headList)){
                throw new MtException("HME_TAG_FORMULA_HEAD_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_HEAD_001", "HME"));
            }
            hmeTagFormulaHead.setTenantId(tenantId);
            self().insertSelective(hmeTagFormulaHead);
        }else {
            //更新
            if(CollectionUtils.isNotEmpty(headList) && !StringUtils.equals(headList.get(0).getTagFormulaHeadId(), hmeTagFormulaHead.getTagFormulaHeadId())){
                throw new MtException("HME_TAG_FORMULA_HEAD_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_HEAD_001", "HME"));
            }
            hmeTagFormulaHeadMapper.updateByPrimaryKeySelective(hmeTagFormulaHead);
        }

    }
}
