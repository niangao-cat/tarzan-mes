package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeOpTagRelVO;
import com.ruike.hme.domain.vo.HmeOpTagRelVO2;
import com.ruike.hme.infra.mapper.HmeOpTagRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeOpTagRel;
import com.ruike.hme.domain.repository.HmeOpTagRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;

import java.util.*;

/**
 * 工艺数据项关系表 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-12-24 15:43:25
 */
@Component
public class HmeOpTagRelRepositoryImpl extends BaseRepositoryImpl<HmeOpTagRel> implements HmeOpTagRelRepository {

    @Autowired
    private HmeOpTagRelMapper hmeOpTagRelMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Override
    public Page<HmeOpTagRelVO2> hmeOpTagRelQuery(Long tenantId, String operationId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeOpTagRelMapper.hmeOpTagRelQuery(tenantId, operationId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeOpTagRel> saveOpTagRel(Long tenantId, List<HmeOpTagRel> hmeOpTagRelList) {
        for (HmeOpTagRel hmeOpTagRel : hmeOpTagRelList) {
            // 校验参数
            // 数据项
            if (StringUtils.isBlank(hmeOpTagRel.getTagId())) {
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "tagId"));
            }
            // 工艺
            if (StringUtils.isBlank(hmeOpTagRel.getOperationId())) {
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "operationId"));
            }
            // 校验唯一性
            List<HmeOpTagRel> queryHmeOpTagRelList = hmeOpTagRelMapper.select(new HmeOpTagRel() {{
                setTenantId(tenantId);
                setTagId(hmeOpTagRel.getTagId());
                setOperationId(hmeOpTagRel.getOperationId());
            }});
            if (StringUtils.isBlank(hmeOpTagRel.getOpTagRelId())) {
                // 新增
                if (CollectionUtils.isNotEmpty(queryHmeOpTagRelList)) {
                    MtTag mtTag = mtTagRepository.selectByPrimaryKey(hmeOpTagRel.getTagId());
                    throw new MtException("HME_OP_TAG_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_OP_TAG_001", "HME", mtTag != null ? mtTag.getTagCode() : ""));
                }
                hmeOpTagRel.setTenantId(tenantId);
                self().insertSelective(hmeOpTagRel);
            } else {
                if (CollectionUtils.isNotEmpty(queryHmeOpTagRelList) && !StringUtils.equals(queryHmeOpTagRelList.get(0).getOpTagRelId(), hmeOpTagRel.getOpTagRelId())) {
                    MtTag mtTag = mtTagRepository.selectByPrimaryKey(hmeOpTagRel.getTagId());
                    throw new MtException("HME_OP_TAG_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_OP_TAG_001", "HME", mtTag != null ? mtTag.getTagCode() : ""));
                }
                hmeOpTagRelMapper.updateByPrimaryKeySelective(hmeOpTagRel);
            }
        }
        return hmeOpTagRelList;
    }
    /**
     *
     * @Description 查询工艺下所有的采集项
     *
     * @author yuchao.wang
     * @date 2020/12/24 15:54
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpTagRelVO>
     *
     */
    @Override
    public List<HmeOpTagRelVO> queryTagInfoByOperationId(Long tenantId, String operationId) {
        return hmeOpTagRelMapper.queryTagInfoByOperationId(tenantId, operationId);
    }
}
