package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeInterceptObjectService;
import com.ruike.hme.domain.entity.HmeInterceptInformation;
import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.entity.HmeInterceptWorkcell;
import com.ruike.hme.domain.repository.HmeInterceptInformationRepository;
import com.ruike.hme.domain.repository.HmeInterceptObjectRepository;
import com.ruike.hme.domain.repository.HmeInterceptWorkcellRepository;
import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import com.ruike.hme.infra.mapper.HmeInterceptInformationMapper;
import com.ruike.hme.infra.mapper.HmeInterceptObjectMapper;
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
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截对象表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:08
 */
@Service
public class HmeInterceptObjectServiceImpl extends BaseAppService implements HmeInterceptObjectService {

    private final HmeInterceptObjectMapper hmeInterceptObjectMapper;
    private final MtUserClient mtUserClient;
    private final HmeInterceptInformationRepository hmeInterceptInformationRepository;
    private final HmeInterceptObjectRepository hmeInterceptObjectRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialRepository mtMaterialRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final HmeInterceptInformationMapper hmeInterceptInformationMapper;
    private final HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository;


    public HmeInterceptObjectServiceImpl(HmeInterceptObjectMapper hmeInterceptObjectMapper, MtUserClient mtUserClient, HmeInterceptInformationRepository hmeInterceptInformationRepository, HmeInterceptObjectRepository hmeInterceptObjectRepository, MtErrorMessageRepository mtErrorMessageRepository, MtMaterialRepository mtMaterialRepository, MtWorkOrderRepository mtWorkOrderRepository, MtMaterialLotRepository mtMaterialLotRepository, HmeInterceptInformationMapper hmeInterceptInformationMapper, HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository) {
        this.hmeInterceptObjectMapper = hmeInterceptObjectMapper;
        this.mtUserClient = mtUserClient;
        this.hmeInterceptInformationRepository = hmeInterceptInformationRepository;
        this.hmeInterceptObjectRepository = hmeInterceptObjectRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialRepository = mtMaterialRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.hmeInterceptInformationMapper = hmeInterceptInformationMapper;
        this.hmeInterceptWorkcellRepository = hmeInterceptWorkcellRepository;
    }


    @Override
    @ProcessLovValue
    public Page<HmeInterceptObjectVO> queryInterceptObject(Long tenantId, String interceptId, PageRequest pageRequest) {
        Page<HmeInterceptObjectVO> page = PageHelper.doPage(pageRequest, () -> hmeInterceptObjectMapper.queryInterceptObject(tenantId, interceptId));
        List<Long> userIdList = new ArrayList<>();
        page.getContent().forEach(hmeInterceptObjectVO -> {
            userIdList.add(hmeInterceptObjectVO.getReleaseBy());
        });
        //去重后的释放人id
        List<Long> distinctUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctUserIdList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctUserIdList);
        }
        for (HmeInterceptObjectVO hmeInterceptObjectVO : page.getContent()) {
            //设置释放人姓名
            hmeInterceptObjectVO.setReleaseByName(userInfoMap.getOrDefault(hmeInterceptObjectVO.getReleaseBy(), new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterceptObject(Long tenantId, List<HmeInterceptObjectVO> hmeInterceptObjectVOList, String interceptId) {

        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        HmeInterceptInformation interceptInformation = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);
        //
        if (!StringUtils.equals(interceptInformation.getDimension(), "LOT") || !StringUtils.equals(interceptInformation.getDimension(), "SUPPLIER_LOT")) {
            //输入数据唯一性校验
            Map<String, List<HmeInterceptObjectVO>> objectListMap = hmeInterceptObjectVOList.stream().collect(Collectors.groupingBy(e -> this.splice(e)));
            if (objectListMap.size() != hmeInterceptObjectVOList.size()) {
                objectListMap.forEach((objectKey, objectValue) -> {
                    if (objectValue.size() > 1) {
                        throw new MtException("HME_INTERCEPT_RELEASE_003", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_INTERCEPT_RELEASE_003", "HME", objectValue.get(0).getInterceptObject()
                        ));
                    }
                });
            }
        }

        //当拦截对象输入时检查拦截单信息表的字段拦截维度，如果拦截维度为工单，校验输入拦截对象信息是否是工单，否时报错，输入数据【${1}】非工单,请检查，消息值集（HME_INTERCEPT_OBJECT_001）
        if (StringUtils.equals(interceptInformation.getDimension(), "WO")) {
            hmeInterceptObjectVOList.forEach(hmeInterceptObjectVO -> {
                MtWorkOrder workOrder = new MtWorkOrder();
                workOrder.setWorkOrderNum(hmeInterceptObjectVO.getInterceptObject());
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(workOrder);
                //如果在mt_work_order表里找不到工单号就报错
                if (Objects.isNull(mtWorkOrder)) {
                    throw new MtException("HME_INTERCEPT_OBJECT_001", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_INTERCEPT_OBJECT_001", "HME", hmeInterceptObjectVO.getInterceptObject()
                    ));
                }
            });
        }
        //当拦截对象输入时检查拦截单信息表的字段拦截维度，如果拦截维度为序列号，校验输入拦截对象信息是否是序列号，否时报错，输入数据【${1}】非SN号,请检查，消息值集（HME_INTERCEPT_OBJECT_002）
        if (StringUtils.equals(interceptInformation.getDimension(), "SN")) {
            hmeInterceptObjectVOList.forEach(hmeInterceptObjectVO -> {
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setMaterialLotCode(hmeInterceptObjectVO.getInterceptObject());
                MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
                if (Objects.isNull(materialLot)) {
                    throw new MtException("HME_INTERCEPT_OBJECT_002", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_INTERCEPT_OBJECT_002", "HME", hmeInterceptObjectVO.getInterceptObject()
                    ));
                }
            });

        }
        List<HmeInterceptObject> hmeInterceptObjectList = new ArrayList<>();
        hmeInterceptObjectVOList.forEach(hmeInterceptObjectVO -> {

            //设置数据
            HmeInterceptObject hmeInterceptObject = new HmeInterceptObject();
            hmeInterceptObject.setInterceptObject(hmeInterceptObjectVO.getInterceptObject());
            //设置物料id
            hmeInterceptObject.setMaterialId(hmeInterceptObjectVO.getMaterialId());
            hmeInterceptObject.setStatus(hmeInterceptObjectVO.getStatus());
            //设置拦截对象id
            hmeInterceptObject.setInterceptId(interceptId);
            hmeInterceptObject.setTenantId(tenantId);
            hmeInterceptObjectList.add(hmeInterceptObject);

        });
        //批量插入
        hmeInterceptObjectRepository.batchInsertSelective(hmeInterceptObjectList);
        //根据对象表和工序表的状态去更改信息头表
        HmeInterceptWorkcell hmeInterceptWorkcell = new HmeInterceptWorkcell();
        hmeInterceptWorkcell.setInterceptId(interceptId);
        List<HmeInterceptWorkcell> interceptWorkcellList = hmeInterceptWorkcellRepository.select(hmeInterceptWorkcell);
        if (CollectionUtils.isNotEmpty(interceptWorkcellList)) {
            //工序表不为空,更新头表状态为已拦截
            HmeInterceptInformation information = new HmeInterceptInformation();
            information.setStatus("INTERCEPT");
            information.setInterceptId(interceptId);
            //更新状态
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(information);
        }


    }

    @Override
    public void passInterceptObject(Long tenantId, String interceptId, List<HmeInterceptObjectVO> hmeInterceptObjectVOList) {
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        //更新拦截对象状态
        HmeInterceptObject hmeInterceptObject = new HmeInterceptObject();
        hmeInterceptObjectVOList.forEach(hmeInterceptObjectVO -> {
            //设置id
            hmeInterceptObject.setInterceptObjectId(hmeInterceptObjectVO.getInterceptObjectId());
            //更新拦截对象状态为放行
            hmeInterceptObject.setStatus("RELEASED");
            //设置释放人
            hmeInterceptObject.setReleaseBy(userId);
            //设置释放时间
            hmeInterceptObject.setReleaseDate(date);
            //更新表
            hmeInterceptObjectMapper.updateByPrimaryKeySelective(hmeInterceptObject);
        });
        //查询拦截id下的所有拦截对象
        List<HmeInterceptObjectVO> objectVOList = hmeInterceptObjectMapper.queryInterceptObject(tenantId, interceptId);
        //用已拦截 INTERCEPT 去筛选objectVOList，如果筛选的集合里有数据，修改拦截信息表状态为部分拦截 PART_INTERCEPT
        List<HmeInterceptObjectVO> interceptObjectVOList = objectVOList.stream().filter(e ->
                StringUtils.equals(e.getStatus(), "INTERCEPT")).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(interceptObjectVOList)) {
            HmeInterceptInformation interceptInformation = new HmeInterceptInformation();
            interceptInformation.setStatus("PART_INTERCEPT");
            interceptInformation.setInterceptId(interceptId);
            //更新信息表
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(interceptInformation);
        } else {
            //如果筛选的集合里没有数据，修改拦截信息表状态为已释放 RELEASED
            HmeInterceptInformation interceptInformation = new HmeInterceptInformation();
            interceptInformation.setStatus("RELEASED");
            interceptInformation.setInterceptId(interceptId);
            //更新信息表
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(interceptInformation);
        }
    }

    private String splice(HmeInterceptObjectVO hmeInterceptObjectVO) {
        //用工序code构建新对象
        StringBuffer sb = new StringBuffer();
        sb.append(hmeInterceptObjectVO.getInterceptObject());
        sb.append(hmeInterceptObjectVO.getMaterialId());
        return sb.toString();
    }


}
