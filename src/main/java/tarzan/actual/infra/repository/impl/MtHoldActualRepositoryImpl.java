package tarzan.actual.infra.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.repository.MtHoldActualDetailRepository;
import tarzan.actual.domain.repository.MtHoldActualRepository;
import tarzan.actual.domain.vo.MtHoldActualDetailVO2;
import tarzan.actual.domain.vo.MtHoldActualVO;
import tarzan.actual.domain.vo.MtHoldActualVO2;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 保留实绩 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtHoldActualRepositoryImpl extends BaseRepositoryImpl<MtHoldActual> implements MtHoldActualRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtHoldActualDetailRepository mtHoldActualDetailRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtHoldActualVO2 holdCreate(Long tenantId, MtHoldActualVO dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());
        // 1.校验头数据并保存
        MtHoldActual mtHoldActual = dto.getMtHoldActual();
        if (mtHoldActual == null || StringUtils.isEmpty(mtHoldActual.getSiteId())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "siteId", "【API:holdCreate】"));
        }

        if (StringUtils.isEmpty(mtHoldActual.getHoldType())) {
            throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0001", "HOLD", "holdType", "【API:holdCreate】"));
        }
        // 校验行数据
        List<MtHoldActualDetail> mtHoldActualDetails = dto.getMtHoldActualDetails();
        if (null != mtHoldActualDetails && mtHoldActualDetails.size() > 0) {

            for (MtHoldActualDetail mtHoldActualDetail : mtHoldActualDetails) {
                if (StringUtils.isEmpty(mtHoldActualDetail.getObjectType())) {
                    throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_HOLD_0001", "HOLD", "objectType", "【API:holdCreate】"));
                }
                if (StringUtils.isEmpty(mtHoldActualDetail.getObjectId())) {
                    throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_HOLD_0001", "HOLD", "objectId", "【API:holdCreate】"));
                }
                if (StringUtils.isEmpty(mtHoldActualDetail.getOriginalStatus())) {
                    throw new MtException("MT_HOLD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_HOLD_0001", "HOLD", "originalStatus", "【API:holdCreate】"));
                }
            }
        }

        // 2.校验参数合理性
        // 2.1 站点合理性
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtHoldActual.getSiteId());
        if (mtModSite == null || !"Y".equals(mtModSite.getEnableFlag())) {
            throw new MtException("MT_HOLD_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0002", "HOLD", "siteId", "【API:holdCreate】"));
        }

        // 2.2校验holdType合理性

        MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
        mtGenTypeVO2.setModule("HOLD");
        mtGenTypeVO2.setTypeGroup("HOLD_TYPE");
        List<MtGenType> bomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

        Boolean contaninerHoldTypeEqual = false;
        for (MtGenType type : bomTypes) {
            if (type.getTypeCode().equals(mtHoldActual.getHoldType())) {
                contaninerHoldTypeEqual = true;
                break;
            }
        }
        if (!contaninerHoldTypeEqual) {
            throw new MtException("MT_HOLD_0005 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_HOLD_0005 ", "HOLD", "holdType", "【API:holdCreate】"));

        }

        // 2.3校验objectType合理性
        if (CollectionUtils.isNotEmpty(mtHoldActualDetails)) {
            MtGenTypeVO2 mtGenTypeVO21 = new MtGenTypeVO2();
            mtGenTypeVO21.setModule("HOLD");
            mtGenTypeVO21.setTypeGroup("HOLD_OBJECT_TYPE");
            List<MtGenType> mtGenTypes1 = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO21);


            for (MtHoldActualDetail t : mtHoldActualDetails) {
                boolean containGenType = false;

                for (MtGenType type : mtGenTypes1) {
                    if (type.getTypeCode().equals(t.getObjectType())) {
                        containGenType = true;
                        break;
                    }
                }

                if (!containGenType) {
                    throw new MtException("MT_HOLD_0005 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_HOLD_0005 ", "HOLD", "objectType", "【API:holdCreate】"));
                }
            }
        }

        // 3.校验object是否已存在有效保留
        if ("IMMEDIATE".equals(mtHoldActual.getHoldType()) && CollectionUtils.isNotEmpty(mtHoldActualDetails)) {
            for (MtHoldActualDetail mtHoldActualDetail : mtHoldActualDetails) {
                MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();
                mtHoldActualDetailVo2.setObjectId(mtHoldActualDetail.getObjectId());
                mtHoldActualDetailVo2.setObjectType(mtHoldActualDetail.getObjectType());
                String detailId = mtHoldActualDetailRepository.objectLimitHoldingDetailGet(tenantId,
                                mtHoldActualDetailVo2);
                if (StringUtils.isNotEmpty(detailId)) {
                    throw new MtException("MT_HOLD_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_HOLD_0010", "HOLD", "【API:holdCreate】"));
                }
            }
        }

        List<String> sqlList = new ArrayList<>();

        MtHoldActualVO2 mtHoldActualVo2 = new MtHoldActualVO2();

        // 插入实绩表
        mtHoldActual.setTenantId(tenantId);
        mtHoldActual.setHoldBy(userId);
        mtHoldActual.setHoldTime(new Date());
        mtHoldActual.setCid(Long.valueOf(this.customSequence.getNextKey("mt_hold_actual_cid_s")));
        mtHoldActual.setHoldId(this.customSequence.getNextKey("mt_hold_actual_s"));
        mtHoldActual.setCreatedBy(userId);
        mtHoldActual.setCreationDate(now);
        mtHoldActual.setLastUpdateDate(now);
        mtHoldActual.setLastUpdatedBy(userId);
        mtHoldActual.setObjectVersionNumber(1L);

        sqlList.addAll(MtSqlHelper.getInsertSql(mtHoldActual));

        mtHoldActualVo2.setHoldId(mtHoldActual.getHoldId());

        // 插入明细表
        if (CollectionUtils.isNotEmpty(mtHoldActualDetails)) {
            List<String> listId = new ArrayList<>();
            for (MtHoldActualDetail detail : mtHoldActualDetails) {
                detail.setTenantId(tenantId);
                detail.setHoldId(mtHoldActual.getHoldId());
                detail.setCid(Long.valueOf(this.customSequence.getNextKey("mt_hold_actual_detail_cid_s")));
                detail.setHoldDetailId(this.customSequence.getNextKey("mt_hold_actual_detail_s"));
                detail.setCreatedBy(userId);
                detail.setCreationDate(now);
                detail.setLastUpdateDate(now);
                detail.setLastUpdatedBy(userId);
                detail.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(detail));
                listId.add(detail.getHoldDetailId());
            }
            mtHoldActualVo2.setHoldDetailId(listId);
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        return mtHoldActualVo2;
    }
}
