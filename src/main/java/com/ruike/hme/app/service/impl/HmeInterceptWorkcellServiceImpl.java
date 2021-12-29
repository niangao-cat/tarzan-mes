package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeInterceptWorkcellService;
import com.ruike.hme.domain.entity.HmeInterceptInformation;
import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.entity.HmeInterceptWorkcell;
import com.ruike.hme.domain.repository.HmeInterceptInformationRepository;
import com.ruike.hme.domain.repository.HmeInterceptObjectRepository;
import com.ruike.hme.domain.repository.HmeInterceptWorkcellRepository;
import com.ruike.hme.domain.vo.HmeInterceptWorkcellVO;
import com.ruike.hme.infra.mapper.HmeInterceptInformationMapper;
import com.ruike.hme.infra.mapper.HmeInterceptWorkcellMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截工序表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:11
 */
@Service
public class HmeInterceptWorkcellServiceImpl extends BaseAppService implements HmeInterceptWorkcellService {

    private final HmeInterceptWorkcellMapper hmeInterceptWorkcellMapper;
    private final MtUserClient mtUserClient;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtModWorkcellRepository mtModWorkcellRepository;
    private final HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository;
    private final HmeInterceptInformationRepository hmeInterceptInformationRepository;
    private final HmeInterceptInformationMapper hmeInterceptInformationMapper;
    private final HmeInterceptObjectRepository hmeInterceptObjectRepository;

    public HmeInterceptWorkcellServiceImpl(HmeInterceptWorkcellMapper hmeInterceptWorkcellMapper,
                                           MtUserClient mtUserClient,
                                           MtErrorMessageRepository mtErrorMessageRepository,
                                           MtModWorkcellRepository mtModWorkcellRepository,
                                           HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository,
                                           HmeInterceptInformationRepository hmeInterceptInformationRepository,
                                           HmeInterceptInformationMapper hmeInterceptInformationMapper, HmeInterceptObjectRepository hmeInterceptObjectRepository) {
        this.hmeInterceptWorkcellMapper = hmeInterceptWorkcellMapper;
        this.mtUserClient = mtUserClient;
        this.mtErrorMessageRepository = mtErrorMessageRepository;

        this.mtModWorkcellRepository = mtModWorkcellRepository;
        this.hmeInterceptWorkcellRepository = hmeInterceptWorkcellRepository;
        this.hmeInterceptInformationRepository = hmeInterceptInformationRepository;
        this.hmeInterceptInformationMapper = hmeInterceptInformationMapper;
        this.hmeInterceptObjectRepository = hmeInterceptObjectRepository;
    }


    @Override
    @ProcessLovValue
    public Page<HmeInterceptWorkcellVO> queryInterceptWorkcell(Long tenantId, PageRequest pageRequest, String interceptId) {
        Page<HmeInterceptWorkcellVO> page = PageHelper.doPage(pageRequest, () -> hmeInterceptWorkcellMapper.queryInterceptWorkcell(tenantId, interceptId));
        List<Long> userIdList = new ArrayList<>();
        for (HmeInterceptWorkcellVO hmeInterceptWorkcellVO : page.getContent()) {
            userIdList.add(hmeInterceptWorkcellVO.getReleaseBy());
        }
        List<Long> distinctUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctUserIdList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctUserIdList);
        }
        for (HmeInterceptWorkcellVO hmeInterceptWorkcellVO : page.getContent()) {
            //设置姓名
            hmeInterceptWorkcellVO.setReleaseByName(userInfoMap.getOrDefault(hmeInterceptWorkcellVO.getReleaseBy(), new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterceptWorkcell(Long tenantId, List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList, String interceptId) {
        //输入数据唯一性校验
        Map<String, List<HmeInterceptWorkcellVO>> workcellVOMap = hmeInterceptWorkcellVOList.stream().collect(Collectors.groupingBy(e -> this.splice(e)));
        if (workcellVOMap.size() != hmeInterceptWorkcellVOList.size()) {
            workcellVOMap.forEach((workcellKey, workcellValue) -> {
                if (workcellValue.size() > 1) {
                    throw new MtException("HME_INTERCEPT_RELEASE_003", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_INTERCEPT_RELEASE_003", "HME", workcellValue.get(0).getWorkcellCode()
                    ));
                }
            });
        }


        List<HmeInterceptWorkcell> interceptWorkcellList = new ArrayList<>();
        //当拦截工序输入时匹配表mt_mod_workcell中字段WORKCELL_TYPE=PROCESS为的WORKCELL_CODE，如果没有匹配到则报错，输入数据【${1}】非工序,请检查
        hmeInterceptWorkcellVOList.forEach(hmeInterceptWorkcellVO -> {
            MtModWorkcell mtModWorkcell = new MtModWorkcell();
            mtModWorkcell.setWorkcellCode(hmeInterceptWorkcellVO.getWorkcellCode());
            mtModWorkcell.setWorkcellType("PROCESS");
            MtModWorkcell workcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
            if (Objects.isNull(workcell)) {
                throw new MtException("HME_INTERCEPT_WORKCELL_001", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_INTERCEPT_WORKCELL_001", "HME", hmeInterceptWorkcellVO.getWorkcellCode())
                );
            } else {
                HmeInterceptWorkcell hmeInterceptWorkcell = new HmeInterceptWorkcell();
                hmeInterceptWorkcell.setInterceptId(interceptId);
                hmeInterceptWorkcell.setTenantId(tenantId);
                hmeInterceptWorkcell.setWorkcellId(workcell.getWorkcellId());
                hmeInterceptWorkcell.setStatus(hmeInterceptWorkcellVO.getStatus());
                //将数据装到集合里
                interceptWorkcellList.add(hmeInterceptWorkcell);
            }
        });
        //批量插入拦截工序表
        hmeInterceptWorkcellRepository.batchInsertSelective(interceptWorkcellList);
        //根据对象表和工序表的状态去更改信息头表
        HmeInterceptObject hmeInterceptObject = new HmeInterceptObject();
        hmeInterceptObject.setInterceptId(interceptId);
        List<HmeInterceptObject> interceptObjectList = hmeInterceptObjectRepository.select(hmeInterceptObject);
        if (CollectionUtils.isNotEmpty(interceptObjectList)) {
            //更新信息表状态
            HmeInterceptInformation interceptInformation = new HmeInterceptInformation();
            interceptInformation.setInterceptId(interceptId);
            interceptInformation.setStatus("INTERCEPT");
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(interceptInformation);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passInterceptWorkcell(Long tenantId, String interceptId, List<HmeInterceptWorkcellVO> hmeInterceptWorkcellVOList) {
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        hmeInterceptWorkcellVOList.forEach(hmeInterceptWorkcellVO -> {
            HmeInterceptWorkcell hmeInterceptWorkcell = new HmeInterceptWorkcell();
            //设置id
            hmeInterceptWorkcell.setInterceptWorkcellId(hmeInterceptWorkcellVO.getInterceptWorkcellId());
            //修改拦截工序所选数据状态为已放行
            hmeInterceptWorkcell.setStatus("RELEASED");
            //设置放行人
            hmeInterceptWorkcell.setReleaseBy(userId);
            //设置释放时间
            hmeInterceptWorkcell.setReleaseDate(date);
            //更新数据
            hmeInterceptWorkcellMapper.updateByPrimaryKeySelective(hmeInterceptWorkcell);
        });
        //查询所有该拦截id下的工序,判断该集合下工序状态是否一致
        List<HmeInterceptWorkcellVO> workcellVOList = hmeInterceptWorkcellMapper.queryInterceptWorkcell(tenantId, interceptId);
        //用状态为已拦截去筛选
        List<HmeInterceptWorkcellVO> interceptList = workcellVOList.stream().filter(e -> StringUtils.equals(e.getStatus(),
                "INTERCEPT")).collect(Collectors.toList());
        HmeInterceptInformation interceptInformation = new HmeInterceptInformation();
        //interceptList 不为空，说明还有已拦截数据，去修改拦截信息状态为部分拦截，否则状态改为已放行
        if (CollectionUtils.isNotEmpty(interceptList)) {
            interceptInformation.setStatus("PART_INTERCEPT");
            interceptInformation.setInterceptId(interceptId);
            //更新拦截信息表
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(interceptInformation);
        } else {
            //将拦截信息表状态更新为RELEASED 已释放
            interceptInformation.setStatus("RELEASED");
            interceptInformation.setInterceptId(interceptId);
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(interceptInformation);
        }
    }

    private String splice(HmeInterceptWorkcellVO hmeInterceptWorkcellVO) {
        //用工序code构建新对象
        StringBuffer sb = new StringBuffer();
        sb.append(hmeInterceptWorkcellVO.getWorkcellCode());
        return sb.toString();
    }


}
