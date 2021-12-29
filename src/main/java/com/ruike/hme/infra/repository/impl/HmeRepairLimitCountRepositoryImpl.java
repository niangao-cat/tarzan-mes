package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.entity.HmeRepairLimitCount;
import com.ruike.hme.domain.entity.HmeRepairLimitCountHis;
import com.ruike.hme.domain.repository.HmeRepairLimitCountHisRepository;
import com.ruike.hme.domain.repository.HmeRepairLimitCountRepository;
import com.ruike.hme.domain.vo.HmeRepairLimitCountHisVO;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import com.ruike.hme.infra.mapper.HmeRepairLimitCountMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:09
 */
@Component
public class HmeRepairLimitCountRepositoryImpl extends BaseRepositoryImpl<HmeRepairLimitCount> implements HmeRepairLimitCountRepository {

    @Autowired
    private HmeRepairLimitCountMapper repairLimitCountMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeRepairLimitCountHisRepository repairLimitCountHisRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    @ProcessLovValue
    public Page<HmeRepairLimitCountVO> queryRepairLimitCountList(Long tenantId, PageRequest pageRequest, HmeRepairLimitCountDTO dto) {
        return PageHelper.doPage(pageRequest,()->repairLimitCountMapper.queryRepairLimitCountList(tenantId,dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRepairLimitCountByIds(Long tenantId, List<String> list) {
        repairLimitCountMapper.deleteRepairLimitCountByIds(tenantId, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeRepairLimitCountDTO> createOrUpdateRepairLimitCount(Long tenantId, List<HmeRepairLimitCountDTO> dtoList) {
        List<HmeRepairLimitCountDTO> resultList = new ArrayList<>();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date now = new Date();
        for(HmeRepairLimitCountDTO repairLimitCountDTO : dtoList) {
            //根据租户Id、 物料Id、工序Id 查询记录
            HmeRepairLimitCount queryDTO = new HmeRepairLimitCount();
            queryDTO.setTenantId(tenantId);
            queryDTO.setMaterialId(repairLimitCountDTO.getMaterialId());
            queryDTO.setWorkcellId(repairLimitCountDTO.getWorkcellId());
            List<HmeRepairLimitCount> queryResult = this.select(queryDTO);
            String queryResId = null;
            if (CollectionUtils.isNotEmpty(queryResult)) {
                queryResId = queryResult.get(0).getRepairLimitCountId();
            }
            //主键为空， 且数据库中不存在租户Id、物料id、工序id相同的值时 插入数据
            if (StringUtils.isBlank(repairLimitCountDTO.getRepairLimitCountId()) && CollectionUtils.isEmpty(queryResult)){
                HmeRepairLimitCount insertLimit = new HmeRepairLimitCount();
                String ids = customDbRepository.getNextKey("hme_repair_limit_count_s");
                String cids = customDbRepository.getNextKey("hme_repair_limit_count_cid_s");
                insertLimit.setTenantId(tenantId);
                insertLimit.setRepairLimitCountId(ids);
                insertLimit.setMaterialId(repairLimitCountDTO.getMaterialId());
                //工序
                insertLimit.setWorkcellId(repairLimitCountDTO.getWorkcellId());
                insertLimit.setLimitCount(Long.parseLong(repairLimitCountDTO.getLimitCount()));
                insertLimit.setEnableFlag(repairLimitCountDTO.getEnableFlag());
                insertLimit.setCid(Long.valueOf(cids));
                insertLimit.setLastUpdatedBy(userId);
                insertLimit.setLastUpdateDate(now);
                insertLimit.setCreatedBy(userId);
                insertLimit.setCreationDate(now);
                this.insertSelective(insertLimit);
                // 记录历史
                HmeRepairLimitCountHis hisRecord = new HmeRepairLimitCountHis();
                BeanUtils.copyProperties(insertLimit, hisRecord);
                repairLimitCountHisRepository.insertSelective(hisRecord);
            } else if ( StringUtils.isNotBlank(repairLimitCountDTO.getRepairLimitCountId()) &&
                    ( CollectionUtils.isEmpty(queryResult) || repairLimitCountDTO.getRepairLimitCountId().equals(queryResId) ) ) {
                // 修改
                Long cids = Long.parseLong(customDbRepository.getNextKey("hme_repair_limit_count_cid_s"));
                HmeRepairLimitCount updateLimit = new HmeRepairLimitCount();
                updateLimit.setRepairLimitCountId(repairLimitCountDTO.getRepairLimitCountId());
                updateLimit.setTenantId(tenantId);
                updateLimit.setMaterialId(repairLimitCountDTO.getMaterialId());
                updateLimit.setWorkcellId(repairLimitCountDTO.getWorkcellId());
                updateLimit.setEnableFlag(repairLimitCountDTO.getEnableFlag());
                updateLimit.setLimitCount(Long.parseLong(repairLimitCountDTO.getLimitCount()));
                updateLimit.setCid(cids);
                updateLimit.setLastUpdateDate(now);
                updateLimit.setLastUpdatedBy(userId);
                repairLimitCountMapper.updateByPrimaryKeySelective(updateLimit);
                //记录历史
                HmeRepairLimitCountHis hisRecord = new HmeRepairLimitCountHis();
                BeanUtils.copyProperties(updateLimit, hisRecord);
                repairLimitCountHisRepository.insertSelective(hisRecord);
            } else {
                //物料、工序下已维护返修进站次数限制
                throw new MtException("HME_REPAIR_LIMIT_COUNT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_REPAIR_LIMIT_COUNT_001", "HME",repairLimitCountDTO.getMaterialCode(), repairLimitCountDTO.getWorkcellCode()));
            }
            resultList.add(repairLimitCountDTO);
        }
        return resultList;
    }
}
