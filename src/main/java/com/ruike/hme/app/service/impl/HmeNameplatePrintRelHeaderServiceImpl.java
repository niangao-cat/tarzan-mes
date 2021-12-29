package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNameplatePrintRelHeaderDTO;
import com.ruike.hme.app.service.HmeNameplatePrintRelHeaderService;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeader;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeaderHis;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelHeaderHisRepository;
import com.ruike.hme.domain.repository.HmeNameplatePrintRelHeaderRepository;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderAndLineVO;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderVO;
import com.ruike.hme.infra.mapper.HmeNameplatePrintRelHeaderMapper;
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
import org.hzero.core.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 铭牌打印内部识别码对应关系头表应用服务默认实现
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:11
 */
@Service
public class HmeNameplatePrintRelHeaderServiceImpl extends BaseAppService implements HmeNameplatePrintRelHeaderService {

    private final HmeNameplatePrintRelHeaderRepository hmeNameplatePrintRelHeaderRepository;
    private final HmeNameplatePrintRelHeaderMapper hmeNameplatePrintRelHeaderMapper;
    private final MtUserClient mtUserClient;
    private final HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    public HmeNameplatePrintRelHeaderServiceImpl(HmeNameplatePrintRelHeaderRepository hmeNameplatePrintRelHeaderRepository,
                                                 HmeNameplatePrintRelHeaderMapper hmeNameplatePrintRelHeaderMapper,
                                                 MtUserClient mtUserClient,
                                                 HmeNameplatePrintRelHeaderHisRepository hmeNameplatePrintRelHeaderHisRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.hmeNameplatePrintRelHeaderRepository = hmeNameplatePrintRelHeaderRepository;
        this.hmeNameplatePrintRelHeaderMapper = hmeNameplatePrintRelHeaderMapper;
        this.mtUserClient = mtUserClient;
        this.hmeNameplatePrintRelHeaderHisRepository = hmeNameplatePrintRelHeaderHisRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    @ProcessLovValue
    public Page<HmeNameplatePrintRelHeaderVO> queryPrintRelHeader(Long tenantId, HmeNameplatePrintRelHeaderDTO hmeNameplatePrintRelHeaderDTO, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeNameplatePrintRelHeaderMapper.queryPrintRelHeader(tenantId, hmeNameplatePrintRelHeaderDTO));
    }

    @Override
    @ProcessLovValue
    public Page<HmeNameplatePrintRelHeaderAndLineVO> queryPrintRelHeaderAndLine(Long tenantId, String nameplateHeaderId, PageRequest pageRequest) {
        Page<HmeNameplatePrintRelHeaderAndLineVO> page = PageHelper.doPage(pageRequest, () -> hmeNameplatePrintRelHeaderMapper.queryPrintRelHeaderAndLine(tenantId, nameplateHeaderId));
        List<Long> userHeaderId = new ArrayList<>();
        List<Long> userLineId = new ArrayList<>();
        page.getContent().forEach(hmeNameplatePrintRelHeaderAndLineVO -> {
            //头表创建人
            userHeaderId.add(hmeNameplatePrintRelHeaderAndLineVO.getLastUpdatedByHeader());
            //行表创建人
            userLineId.add(hmeNameplatePrintRelHeaderAndLineVO.getLastUpdatedByLine());
        });
        //去重
        List<Long> distinctListHeader = userHeaderId.stream().distinct().collect(Collectors.toList());
        List<Long> distinctListLine = userLineId.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoHeaderMap = new HashMap<>();
        Map<Long, MtUserInfo> userInfoLineMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctListHeader)) {
            userInfoHeaderMap = mtUserClient.userInfoBatchGet(tenantId, distinctListHeader);
        }
        if (CollectionUtils.isNotEmpty(distinctListHeader)) {
            userInfoLineMap = mtUserClient.userInfoBatchGet(tenantId, distinctListLine);
        }
        for (HmeNameplatePrintRelHeaderAndLineVO hmeNameplatePrintRelHeaderAndLineVO : page.getContent()) {
            //设置头表更新人姓名
            hmeNameplatePrintRelHeaderAndLineVO.setLastUpdatedByHeaderName(userInfoHeaderMap.getOrDefault(hmeNameplatePrintRelHeaderAndLineVO.getLastUpdatedByHeader(), new MtUserInfo()).getRealName());
            //设置行表更新人姓名
            hmeNameplatePrintRelHeaderAndLineVO.setLastUpdatedByLineName(userInfoLineMap.getOrDefault(hmeNameplatePrintRelHeaderAndLineVO.getLastUpdatedByLine(), new MtUserInfo()).getRealName());
        }

        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrintRelHeader(Long tenantId, HmeNameplatePrintRelHeaderVO hmeNameplatePrintRelHeaderVO) {
        if (StringUtils.isNotEmpty(hmeNameplatePrintRelHeaderVO.getNameplateHeaderId())) {
            String oneByOrder = hmeNameplatePrintRelHeaderMapper.QueryOneByOrder(tenantId, hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            if (StringUtils.isNotEmpty(oneByOrder) && !StringUtils.equals(oneByOrder, hmeNameplatePrintRelHeaderVO.getNameplateHeaderId())) {
                throw new MtException("HME_NAMEPLATE_PRINT_REL_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NAMEPLATE_PRINT_REL_001", "HME"));
            }
            //修改数据
            HmeNameplatePrintRelHeader hmeNameplatePrintRelHeader = new HmeNameplatePrintRelHeader();
            hmeNameplatePrintRelHeader.setNameplateHeaderId(hmeNameplatePrintRelHeaderVO.getNameplateHeaderId());
            hmeNameplatePrintRelHeader.setNameplateOrder(hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            hmeNameplatePrintRelHeader.setType(hmeNameplatePrintRelHeaderVO.getType());
            hmeNameplatePrintRelHeader.setIdentifyingCode(hmeNameplatePrintRelHeaderVO.getIdentifyingCode());
            hmeNameplatePrintRelHeader.setEnableFlag(hmeNameplatePrintRelHeaderVO.getEnableFlag());
            //更新头表
            hmeNameplatePrintRelHeaderMapper.updateByPrimaryKeySelective(hmeNameplatePrintRelHeader);
            //头历史表新增数据
            HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis = new HmeNameplatePrintRelHeaderHis();
            //设置头
            hmeNameplatePrintRelHeaderHis.setNameplateHeaderId(hmeNameplatePrintRelHeaderVO.getNameplateHeaderId());
            hmeNameplatePrintRelHeaderHis.setNameplateOrder(hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            hmeNameplatePrintRelHeaderHis.setType(hmeNameplatePrintRelHeaderVO.getType());
            hmeNameplatePrintRelHeaderHis.setTenantId(tenantId);
            hmeNameplatePrintRelHeaderHis.setIdentifyingCode(hmeNameplatePrintRelHeaderVO.getIdentifyingCode());
            hmeNameplatePrintRelHeaderHis.setEnableFlag(hmeNameplatePrintRelHeaderVO.getEnableFlag());
            //历史表新增
            hmeNameplatePrintRelHeaderHisRepository.insertSelective(hmeNameplatePrintRelHeaderHis);
        } else {
            String oneByOrder = hmeNameplatePrintRelHeaderMapper.QueryOneByOrder(tenantId, hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            if (StringUtils.isNotEmpty(oneByOrder)) {
                throw new MtException("HME_NAMEPLATE_PRINT_REL_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NAMEPLATE_PRINT_REL_001", "HME"));
            }
            //新增操作
            HmeNameplatePrintRelHeader hmeNameplatePrintRelHeader = new HmeNameplatePrintRelHeader();
            hmeNameplatePrintRelHeader.setNameplateHeaderId(hmeNameplatePrintRelHeaderVO.getNameplateHeaderId());
            hmeNameplatePrintRelHeader.setNameplateOrder(hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            hmeNameplatePrintRelHeader.setType(hmeNameplatePrintRelHeaderVO.getType());
            hmeNameplatePrintRelHeader.setIdentifyingCode(hmeNameplatePrintRelHeaderVO.getIdentifyingCode());
            hmeNameplatePrintRelHeader.setEnableFlag(hmeNameplatePrintRelHeaderVO.getEnableFlag());
            hmeNameplatePrintRelHeaderRepository.insertSelective(hmeNameplatePrintRelHeader);
            //历史表新增
            HmeNameplatePrintRelHeaderHis hmeNameplatePrintRelHeaderHis = new HmeNameplatePrintRelHeaderHis();
            hmeNameplatePrintRelHeaderHis.setNameplateHeaderId(hmeNameplatePrintRelHeader.getNameplateHeaderId());
            hmeNameplatePrintRelHeaderHis.setType(hmeNameplatePrintRelHeaderVO.getType());
            hmeNameplatePrintRelHeaderHis.setNameplateOrder(hmeNameplatePrintRelHeaderVO.getNameplateOrder());
            hmeNameplatePrintRelHeaderHis.setTenantId(tenantId);
            hmeNameplatePrintRelHeaderHis.setIdentifyingCode(hmeNameplatePrintRelHeaderVO.getIdentifyingCode());
            hmeNameplatePrintRelHeaderHis.setEnableFlag(hmeNameplatePrintRelHeaderVO.getEnableFlag());
            //历史表新增
            hmeNameplatePrintRelHeaderHisRepository.insertSelective(hmeNameplatePrintRelHeaderHis);
        }
    }
}
