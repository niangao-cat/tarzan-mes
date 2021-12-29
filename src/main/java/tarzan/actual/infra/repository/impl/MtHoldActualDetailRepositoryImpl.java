package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.actual.domain.entity.MtEoStepActualHis;
import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtHoldActualDetailMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO10;
import tarzan.order.domain.vo.MtEoVO28;

/**
 * 保留实绩明细 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtHoldActualDetailRepositoryImpl extends BaseRepositoryImpl<MtHoldActualDetail>
                implements MtHoldActualDetailRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtHoldActualDetailMapper mtHoldActualDetailMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtHoldActualRepository mtHoldActualRepository;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;


    @Override
    public MtHoldActualDetailVO holdDetailPropertyGet(Long tenantId, MtHoldActualDetailVO dto) {
        if (null == dto || StringUtils.isEmpty(dto.getHoldDetailId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "holdDetailId", "【API:holdDetailPropertyGet】"));
        }
        return mtHoldActualDetailMapper.holdDetailPropertyGet(tenantId, dto);
    }

    @Override
    public String objectLimitHoldingDetailGet(Long tenantId, MtHoldActualDetailVO2 dto) {
        if (null == dto || StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "objectType", "【API:objectLimitHoldingDetailGet】"));
        }
        if (StringUtils.isEmpty(dto.getObjectId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "objectId", "【API:objectLimitHoldingDetailGet】"));
        }
        // 根据对象获取正在保留的实绩明细
        List<MtHoldActualDetail> mtHoldActualDetailList =
                        mtHoldActualDetailMapper.checkSavingHoldActrualDetail(tenantId, dto);
        if (mtHoldActualDetailList == null || mtHoldActualDetailList.size() == 0) {
            return null;
        } else if (mtHoldActualDetailList.size() > 1) {
            throw new MtException("MT_HOLD_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0009", "HOLD", "【API:objectLimitHoldingDetailGet】"));
        } else {
            return mtHoldActualDetailList.get(0).getHoldDetailId();
        }
    }

    @Override
    public List<MtHoldActualDetailVO3> propertyLimitHoldDetailQuery(Long tenantId, MtHoldActualDetailVO dto) {
        // 校验参数是否都为空

        if (dto == null) {
            throw new MtException("MT_HOLD_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0004", "HOLD", "", "【API:propertyLimitHoldDetailQuery】"));
        }
        if (StringUtils.isEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getReleaseReasonCode())
                        && StringUtils.isEmpty(dto.getComment()) && null == dto.getExpiredReleaseTime()
                        && StringUtils.isEmpty(dto.getHoldType()) && StringUtils.isEmpty(dto.getHoldBy())
                        && null == dto.getHoldStartTime() && null == dto.getHoldEndTime()
                        && StringUtils.isEmpty(dto.getObjectType()) && StringUtils.isEmpty(dto.getObjectId())
                        && StringUtils.isEmpty(dto.getEoStepActualId()) && StringUtils.isEmpty(dto.getOriginalStatus())
                        && StringUtils.isEmpty(dto.getFutureHoldRouterStepId())
                        && StringUtils.isEmpty(dto.getFutureHoldStatus()) && StringUtils.isEmpty(dto.getHoldEventId())
                        && StringUtils.isEmpty(dto.getReleaseFlag()) && StringUtils.isEmpty(dto.getReleaseComment())
                        && null == dto.getReleaseStartTime() && null == dto.getReleaseEndTime()
                        && StringUtils.isEmpty(dto.getReleaseBy()) && StringUtils.isEmpty(dto.getReleaseReasonCode())
                        && StringUtils.isEmpty(dto.getReleaseEventId())) {
            throw new MtException("MT_HOLD_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0004", "HOLD", "", "【API:propertyLimitHoldDetailQuery】"));
        }
        return mtHoldActualDetailMapper.propertyLimitHoldDetailQuery(tenantId, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void holdRelease(Long tenantId, MtHoldActualDetailVO4 dto) {
        // 1.校验必输
        if (null == dto || StringUtils.isEmpty(dto.getHoldDetailId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "holdDetailId", "【API:holdRelease】"));
        }

        // 2.查找数据
        MtHoldActualDetail mtHoldActualDetail = new MtHoldActualDetail();
        mtHoldActualDetail.setHoldDetailId(dto.getHoldDetailId());
        mtHoldActualDetail.setTenantId(tenantId);
        mtHoldActualDetail = mtHoldActualDetailMapper.selectOne(mtHoldActualDetail);
        if (mtHoldActualDetail == null) {
            throw new MtException("MT_HOLD_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0003", "HOLD", "holdDetailId", "【API:holdRelease】"));
        }
        // 3.释放,更新表
        Long userId = DetailsHelper.getUserDetails().getUserId();
        mtHoldActualDetail.setTenantId(tenantId);
        mtHoldActualDetail.setReleaseFlag("Y");
        mtHoldActualDetail.setReleaseComment(dto.getReleaseComment());
        mtHoldActualDetail.setReleaseTime(new Date());
        mtHoldActualDetail.setReleaseBy(userId);
        mtHoldActualDetail.setReleaseReasonCode(dto.getReleaseReasonCode());
        mtHoldActualDetail.setReleaseEventId(dto.getReleaseEventId());
        self().updateByPrimaryKeySelective(mtHoldActualDetail);

    }

    @Override
    public void holdIsExpiredVerify(Long tenantId, MtHoldActualDetailVO3 dto) {
        // 1.校验必输
        if (null == dto || StringUtils.isEmpty(dto.getHoldDetailId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "holdDetailId", "【API:holdIsExpiredVerify】"));
        }
        MtHoldActualDetailVO5 detailVo5 = holdExpiredReleaseTimeGet(tenantId, dto);
        if (detailVo5 == null) {
            throw new MtException("MT_HOLD_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0003", "HOLD", "holdDetailId", "【API:holdIsExpiredVerify】"));
        }
        // 没有维护过期时间则不过期,确认过
        if (detailVo5.getExpiredReleaseTime() == null) {
            throw new MtException("MT_HOLD_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0008", "HOLD", "【API:holdIsExpiredVerify】"));
        }

        MtHoldActualDetail mtHoldActualDetail = new MtHoldActualDetail();
        mtHoldActualDetail.setHoldDetailId(dto.getHoldDetailId());
        mtHoldActualDetail.setTenantId(tenantId);
        mtHoldActualDetail = mtHoldActualDetailMapper.selectOne(mtHoldActualDetail);

        // 是否已经释放
        if ("Y".equals(mtHoldActualDetail.getReleaseFlag())) {
            throw new MtException("MT_HOLD_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0007", "HOLD", "【API:holdIsExpiredVerify】"));
        }

        /*
         * 2019/04/30 逻辑变更 ExpiredReleaseTime 变更为Date类型，直接比较当前日期，判断是否过期
         */

        // 如果保留日期大于当前系统时间则说明保留未到期
        if (detailVo5.getExpiredReleaseTime().compareTo(new Date()) > 0) {
            throw new MtException("MT_HOLD_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0008", "HOLD", "【API:holdIsExpiredVerify】"));
        }

    }

    @Override
    public MtHoldActualDetailVO5 holdExpiredReleaseTimeGet(Long tenantId, MtHoldActualDetailVO3 dto) {
        // 1.校验参数是否有传入
        if (null == dto) {
            throw new MtException("MT_HOLD_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0004", "HOLD", "【API:holdExpiredReleaseTimeGet】"));
        }

        if (StringUtils.isEmpty(dto.getHoldDetailId()) && StringUtils.isEmpty(dto.getHoldId())) {
            throw new MtException("MT_HOLD_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0004", "HOLD", "【API:holdExpiredReleaseTimeGet】"));
        }

        // 2.根据id取数据
        return mtHoldActualDetailMapper.holdExpiredReleaseTimeGet(tenantId, dto);
    }

    @Override
    public List<MtHoldActualDetailVO3> objectLimitAllHoldQuery(Long tenantId, MtHoldActualDetailVO2 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getObjectId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "objectId", "【API:objectLimitAllHoldQuery】"));
        }
        if (StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "objectType", "【API:objectLimitAllHoldQuery】"));
        }

        return mtHoldActualDetailMapper.objectLimitAllHoldQuery(tenantId, dto);
    }

    @Override
    public List<MtHoldActualDetailVO3> objectLimitFutureHoldDetailQuery(Long tenantId, MtHoldActualDetailVO2 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getObjectId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "objectId", "【API:objectLimitFutureHoldDetailQuery】"));
        }
        if (StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "bjectType", "【API:objectLimitFutureHoldDetailQuery】"));
        }
        return mtHoldActualDetailMapper.objectLimitFutureHoldDetailQuery(tenantId, dto);
    }

    @Override
    public void futureHoldVerify(Long tenantId, MtHoldActualDetailVO6 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "eoId", "【API:futureHoldVerify】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "routerStepId", "【API:futureHoldVerify】"));
        }
        MtHoldActualDetail mtHoldActualDetail = new MtHoldActualDetail();
        mtHoldActualDetail.setObjectType("EO");
        mtHoldActualDetail.setObjectId(dto.getEoId());
        mtHoldActualDetail.setFutureHoldRouterStepId(dto.getRouterStepId());
        mtHoldActualDetail.setFutureHoldStatus(dto.getStatus());
        mtHoldActualDetail.setTenantId(tenantId);

        List<MtHoldActualDetailVO3> list = mtHoldActualDetailMapper.futureHoldVerify(tenantId, mtHoldActualDetail);

        if (list == null || list.size() == 0) {
            throw new MtException("MT_HOLD_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0011", "HOLD", "【API:futureHoldVerify】"));
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtHoldActualDetailVO7> expiredReleaseTimeLimitHoldRelease(Long tenantId, MtHoldActualDetailVO8 dto) {
        // 1.校验参数有效性
        if (dto == null || StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "siteId", "【API:expiredReleaseTimeLimitHoldRelease】"));
        }
        // 2.校验objectType和objectId同时输入或者同时不输入
        if (StringUtils.isEmpty(dto.getObjectId()) && StringUtils.isNotEmpty(dto.getObjectType())
                        || StringUtils.isNotEmpty(dto.getObjectId()) && StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("MT_HOLD_0013 ",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_HOLD_0013 ", "HOLD",
                                            "objectId", "objectType", "【API:expiredReleaseTimeLimitHoldRelease】"));
        }

        // 2.2校验objectType合理性
        if (StringUtils.isNotEmpty(dto.getObjectType())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("HOLD");
            mtGenTypeVO2.setTypeGroup("HOLD_OBJECT_TYPE");
            List<MtGenType> bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            Boolean notContainerFlag = true;
            for (MtGenType type : bomTypes) {
                if (type.getTypeCode().equals(dto.getObjectType())) {
                    notContainerFlag = false;
                    break;
                }
            }
            if (notContainerFlag) {
                throw new MtException("MT_HOLD_0005 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_HOLD_0005 ", "HOLD", "objectType", "【API:expiredReleaseTimeLimitHoldRelease】"));

            }
        }

        // 3.校验站点有效性
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
            throw new MtException("MT_HOLD_0002 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0002 ", "HOLD", "siteId", "【API:expiredReleaseTimeLimitHoldRelease】"));
        }
        // 返回对象

        List<MtHoldActualDetailVO7> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getObjectId()) && StringUtils.isNotEmpty(dto.getObjectType())) {
            MtHoldActualDetailVO7 mtHoldActualDetailVo7 = new MtHoldActualDetailVO7();
            // 4两个非空
            // 4.1.1获取对象正在保留的实绩明细ID;
            MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();
            mtHoldActualDetailVo2.setObjectType(dto.getObjectType());
            mtHoldActualDetailVo2.setObjectId(dto.getObjectId());
            String holdDetailId = objectLimitHoldingDetailGet(tenantId, mtHoldActualDetailVo2);
            if (StringUtils.isEmpty(holdDetailId)) {
                return Collections.emptyList();
            }
            // 4.1.2获取保留实绩所属的站点siteId和初始状态originalStatus，
            // 校验siteId与传入的[input1]是否一致；
            MtHoldActualDetailVO mtHoldActualDetailVo = new MtHoldActualDetailVO();
            mtHoldActualDetailVo.setHoldDetailId(holdDetailId);
            mtHoldActualDetailVo = holdDetailPropertyGet(tenantId, mtHoldActualDetailVo);
            if (mtHoldActualDetailVo == null || !dto.getSiteId().equals(mtHoldActualDetailVo.getSiteId())) {
                throw new MtException("MT_HOLD_0012 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_HOLD_0012 ", "HOLD", "【API:expiredReleaseTimeLimitHoldRelease】"));
            }
            // 4.1.3校验保留是否到期；
            MtHoldActualDetailVO3 mtHoldActualDetailVo3 = new MtHoldActualDetailVO3();
            mtHoldActualDetailVo3.setHoldDetailId(holdDetailId);
            holdIsExpiredVerify(tenantId, mtHoldActualDetailVo3);
            // 4.1.4将保留进行释放；
            MtHoldActualDetailVO4 mtHoldActualDetailVo4 = new MtHoldActualDetailVO4();
            mtHoldActualDetailVo4.setHoldDetailId(holdDetailId);
            holdRelease(tenantId, mtHoldActualDetailVo4);
            mtHoldActualDetailVo7.setHoldDetailId(holdDetailId);
            mtHoldActualDetailVo7.setObjectId(dto.getObjectId());
            mtHoldActualDetailVo7.setObjectType(dto.getObjectType());
            mtHoldActualDetailVo7.setOriginalStatus(mtHoldActualDetailVo.getOriginalStatus());
            list.add(mtHoldActualDetailVo7);
        } else {
            // 4.2.1根据站点获取数据
            List<MtHoldActualDetailVO3> mtHoldActualDetailVo3s =
                            mtHoldActualDetailMapper.queryHoldActrualDetailBySiteId(tenantId, dto.getSiteId());
            for (MtHoldActualDetailVO3 mtHoldActualDetailVo3 : mtHoldActualDetailVo3s) {

                // 校验是否到期
                try {
                    holdIsExpiredVerify(tenantId, mtHoldActualDetailVo3);
                } catch (Exception ex) {
                    continue;
                }
                // 进行释放
                MtHoldActualDetailVO4 mtHoldActualDetailVo4 = new MtHoldActualDetailVO4();
                mtHoldActualDetailVo4.setHoldDetailId(mtHoldActualDetailVo3.getHoldDetailId());
                holdRelease(tenantId, mtHoldActualDetailVo4);
                MtHoldActualDetailVO mtHoldActualDetailVo = new MtHoldActualDetailVO();
                mtHoldActualDetailVo.setHoldDetailId(mtHoldActualDetailVo3.getHoldDetailId());
                mtHoldActualDetailVo = holdDetailPropertyGet(tenantId, mtHoldActualDetailVo);
                // 返回数据
                MtHoldActualDetailVO7 mtHoldActualDetailVo7 = new MtHoldActualDetailVO7();
                mtHoldActualDetailVo7.setHoldDetailId(mtHoldActualDetailVo3.getHoldDetailId());
                mtHoldActualDetailVo7.setObjectId(mtHoldActualDetailVo.getObjectId());
                mtHoldActualDetailVo7.setObjectType(mtHoldActualDetailVo.getObjectType());
                mtHoldActualDetailVo7.setOriginalStatus(mtHoldActualDetailVo.getOriginalStatus());
                list.add(mtHoldActualDetailVo7);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepFutureHold(Long tenantId, MtHoldActualDetailVO11 dto) {
        // 1.检查传入参数是否为空
        if (dto == null || StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepFutureHold】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoStepFutureHold】"));
        }
        if (StringUtils.isEmpty(dto.getFutureHoldRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "futureHoldRouterStepId", "【API:eoStepFutureHold】"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "【API:eoStepFutureHold】"));
        }
        if (StringUtils.isEmpty(dto.getFutureHoldStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "futureHoldStatus", "【API:eoStepFutureHold】"));
        }
        // 2.获取站点
        // 2.1 获取eoId P1
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoStepFutureHold】"));
        }
        // 获取siteId P2
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoStepActualVO1.getEoId());


        // 3.创建事件并记录将来保留 得到[P3] eventId
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_FUTURE_HOLD");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.2 保存数据
        MtHoldActualVO mtHoldActualVo = new MtHoldActualVO();

        MtHoldActual mtHoldActual = new MtHoldActual();// 头
        mtHoldActual.setSiteId(mtEo.getSiteId());
        mtHoldActual.setHoldType("FUTURE");
        mtHoldActualVo.setMtHoldActual(mtHoldActual);

        List<MtHoldActualDetail> mtHoldActualDetails = new ArrayList<>();// 行
        MtHoldActualDetail mtHoldActualDetail = new MtHoldActualDetail();
        mtHoldActualDetail.setObjectType("EO");
        mtHoldActualDetail.setObjectId(mtEoStepActualVO1.getEoId());
        mtHoldActualDetail.setEoStepActualId(dto.getEoStepActualId());
        mtHoldActualDetail.setOriginalStatus(dto.getSourceStatus());
        mtHoldActualDetail.setHoldEventId(eventId);
        mtHoldActualDetail.setFutureHoldRouterStepId(dto.getFutureHoldRouterStepId());
        mtHoldActualDetail.setFutureHoldStatus(dto.getFutureHoldStatus());
        mtHoldActualDetails.add(mtHoldActualDetail);
        mtHoldActualVo.setMtHoldActualDetails(mtHoldActualDetails);
        mtHoldActualRepository.holdCreate(tenantId, mtHoldActualVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepFutureHoldCancel(Long tenantId, MtHoldActualDetailVO9 dto) {
        // 1.检查传入参数是否为空
        if (dto == null || StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepFutureHoldCancel】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoStepFutureHoldCancel】"));
        }
        // 2.获取数量
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoStepFutureHoldCancel】"));
        }

        // 2.创建事件并记录将来保留
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_FUTURE_HOLD_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());

        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);
        MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();

        mtHoldActualDetailVo2.setObjectType("EO");
        mtHoldActualDetailVo2.setObjectId(mtEoStepActualVO1.getEoId());
        List<MtHoldActualDetailVO3> mtHoldActualDetailVo3s =
                        this.objectLimitFutureHoldDetailQuery(tenantId, mtHoldActualDetailVo2);

        for (MtHoldActualDetailVO3 t : mtHoldActualDetailVo3s) {
            MtHoldActualDetailVO4 mtHoldActualDetailVo4 = new MtHoldActualDetailVO4();
            mtHoldActualDetailVo4.setHoldDetailId(t.getHoldDetailId());
            mtHoldActualDetailVo4.setReleaseEventId(eventId);
            this.holdRelease(tenantId, mtHoldActualDetailVo4);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepHold(Long tenantId, MtHoldActualDetailVO10 dto) {
        // 1.检查传入参数是否为空
        if (dto == null || StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepHold】"));
        }
        // wkcId允许传递空字符串
        if (dto.getWorkcellId() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoStepHold】"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "【API:eoStepHold】"));
        }
        // 2.获取需要扣减数量的状态
        String status = null;
        if ("QUEUE".equals(dto.getSourceStatus())) {
            status = "queueQty";
        } else if ("WORKING".equals(dto.getSourceStatus())) {
            status = "workingQty";
        } else if ("COMPENDING".equals(dto.getSourceStatus())) {
            status = "completePendingQty";
        } else {
            throw new MtException("MT_MOVING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0026", "MOVING", "【API:eoStepHold】"));
        }
        // 3.获取数量 得到过程参数:P1,P6
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoStepHold】"));
        }
        mtEoStepActualVO1.setEoQty(
                        mtEoStepActualVO1.getEoQty() == null ? Double.valueOf(0.0D) : mtEoStepActualVO1.getEoQty());
        // 得到 过程参数：P9
        // MtEo mtEo = iMtEoService.eoPropertyGet(tenantId, mtEoStepActualVO1.getEoId());

        // 2. 第一步 创建事件 P2=parentEventId
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_HOLD");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String parentEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.第二步 步骤WKC实绩（含WIP）更新（新增步骤WIP保留数量，扣减WIP来源状态数量）
        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setHoldQty(mtEoStepActualVO1.getEoQty());
        mtEoStepWipVO3.setEventId(parentEventId);
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 生成一个子事件 得到P3=eventId
        mtEoStepActualVO16.setParentEventId(parentEventId);
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepWipVO3.setEventId(eventId);
        mtEoStepWipVO3.setHoldQty(null);
        switch (status) {
            case "queueQty":
                mtEoStepWipVO3.setQueueQty(-mtEoStepActualVO1.getEoQty());
                break;
            case "workingQty":
                mtEoStepWipVO3.setWorkingQty(-mtEoStepActualVO1.getEoQty());
                break;
            default:
                mtEoStepWipVO3.setCompletePendingQty(-mtEoStepActualVO1.getEoQty());
                break;
        }
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 第三步 步骤实绩更新（生产实绩：数量）（新增保留数量）
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setHoldQty(mtEoStepActualVO1.getEoQty());
        mtEoStepActualHis.setEventId(parentEventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 4.第四步 获取需要更新的状态 P4=eoStepActualStatus
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 5 第五步 步骤实绩更新（生产实绩：状态）
        // 再次生成一个子事件 P5=eventId2
        mtEoStepActualVO16.setParentEventId(parentEventId);
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        String eventId2 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualHis mtEoStepActualHis1 = new MtEoStepActualHis();
        mtEoStepActualHis1.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis1.setStatus(eoStepActualStatus);
        mtEoStepActualHis1.setEventId(eventId2);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis1);

        // 第六步 EO保留 获取输出参数shiftDate [P7]，输出参数shiftCode[P8]
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());

        MtEoVO10 mtEoVO10 = new MtEoVO10();
        mtEoVO10.setEoId(mtEoStepActualVO1.getEoId());
        mtEoVO10.setWorkcellId(dto.getWorkcellId());
        mtEoVO10.setParentEventId(parentEventId);
        mtEoVO10.setShiftCode(mtWkcShiftVO3.getShiftCode());
        mtEoVO10.setShiftDate(mtWkcShiftVO3.getShiftDate());
        mtEoVO10.setEventRequestId(dto.getEventRequestId());
        mtEoVO10.setEoStepActualId(dto.getEoStepActualId());
        mtEoRepository.eoHold(tenantId, mtEoVO10);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepHoldCancel(Long tenantId, MtHoldActualDetailVO10 dto) {
        // 检查传入参数是否为空
        if (dto == null || StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepHoldCancel】"));
        }

        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "【API:eoStepHoldCancel】"));
        }
        // 获取需要扣减数量的状态
        String status = null;
        if ("QUEUE".equals(dto.getSourceStatus())) {
            status = "queueQty";
        } else if ("WORKING".equals(dto.getSourceStatus())) {
            status = "workingQty";
        } else if ("COMPENDING".equals(dto.getSourceStatus())) {
            status = "completePendingQty";
        } else {
            throw new MtException("MT_MOVING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0026", "MOVING", "【API:eoStepHoldCancel】"));
        }

        // 获取数量 [P1]=eoQty,[P6]=eoId
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoStepHoldCancel】"));
        }
        mtEoStepActualVO1.setEoQty(
                        mtEoStepActualVO1.getEoQty() == null ? Double.valueOf(0.0D) : mtEoStepActualVO1.getEoQty());

        // 2.【第一步】创建事件 P2=parentEventId
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_HOLD_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String parentEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.【第二步】步骤WKC实绩（含WIP）更新（新增步骤WIP保留数量，扣减WIP来源状态数量）
        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setHoldQty(-mtEoStepActualVO1.getEoQty());
        mtEoStepWipVO3.setEventId(parentEventId);
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 生成一个子事件 得到P3=eventId
        mtEoStepActualVO16.setParentEventId(parentEventId);
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepWipVO3.setEventId(eventId);
        mtEoStepWipVO3.setHoldQty(null);
        switch (status) {
            case "queueQty":
                mtEoStepWipVO3.setQueueQty(mtEoStepActualVO1.getEoQty());
                break;
            case "workingQty":
                mtEoStepWipVO3.setWorkingQty(mtEoStepActualVO1.getEoQty());
                break;
            default:
                mtEoStepWipVO3.setCompletePendingQty(mtEoStepActualVO1.getEoQty());
                break;
        }
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 4.【第三步】步骤实绩更新（生产实绩：数量）（新增保留数量）
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setHoldQty(-mtEoStepActualVO1.getEoQty());
        mtEoStepActualHis.setEventId(parentEventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 5.【第四步】获取需要更新的状态 [P4]=eoStepActualStatus
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 6.【第五步】步骤实绩更新（生产实绩：状态） [P5]=eventId2
        mtEoStepActualVO16.setParentEventId(parentEventId);
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        String eventId2 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualHis mtEoStepActualHis1 = new MtEoStepActualHis();
        mtEoStepActualHis1.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis1.setStatus(eoStepActualStatus);
        mtEoStepActualHis1.setEventId(eventId2);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis1);

        // 7.【第六步】EO保留取消 [P7]=shiftDate,[P8]=shiftCode
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        MtEoVO28 mtEoVO28 = new MtEoVO28();
        mtEoVO28.setEoId(mtEoStepActualVO1.getEoId());
        mtEoVO28.setWorkcellId(dto.getWorkcellId());
        mtEoVO28.setParentEventId(parentEventId);
        mtEoVO28.setShiftCode(mtWkcShiftVO3.getShiftCode());
        mtEoVO28.setShiftDate(mtWkcShiftVO3.getShiftDate());
        mtEoVO28.setEventRequestId(dto.getEventRequestId());
        mtEoRepository.eoHoldCancel(tenantId, mtEoVO28);

    }



}
