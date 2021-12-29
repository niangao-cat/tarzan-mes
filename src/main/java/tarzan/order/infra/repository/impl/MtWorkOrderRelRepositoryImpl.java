package tarzan.order.infra.repository.impl;

import static java.util.stream.Collectors.toList;

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
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtSqlHelper;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderRel;
import tarzan.order.domain.repository.MtWorkOrderRelRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderRelVO;
import tarzan.order.infra.mapper.MtWorkOrderMapper;
import tarzan.order.infra.mapper.MtWorkOrderRelMapper;

/**
 * 生产指令关系,标识生产指令的父子关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@Component
public class MtWorkOrderRelRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderRel>
                implements MtWorkOrderRelRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWorkOrderRelMapper mtWorkOrderRelMapper;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtWorkOrderMapper mtWorkOrderMapper;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelDelete(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelDelete】"));
        }
        if (StringUtils.isEmpty(dto.getParentWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "parentWorkOrderId", "【API:woRelDelete】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelDelete】"));
        }

        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setTenantId(tenantId);
        tmp.setWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());
        tmp.setParentWorkOrderId(dto.getParentWorkOrderId());
        mtWorkOrderRelMapper.delete(tmp);
    }

    @Override
    public List<MtWorkOrderRelVO> woRelParentQuery(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelParentQuery】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelParentQuery】"));
        }
        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());
        return findParent(tenantId, tmp);
    }

    @Override
    public List<MtWorkOrderRelVO> woRelSubQuery(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getParentWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "parentWorkOrderId", "【API:woRelChildQuery】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelChildQuery】"));
        }
        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getParentWorkOrderId());
        tmp.setRelType(dto.getRelType());
        return findChild(tenantId, tmp);
    }

    private List<MtWorkOrderRelVO> findParent(Long tenantId, MtWorkOrderRel dto) {

        dto.setTenantId(tenantId);

        List<MtWorkOrderRel> ls = mtWorkOrderRelMapper.select(dto);
        List<MtWorkOrderRelVO> rs = new ArrayList<MtWorkOrderRelVO>();
        if (CollectionUtils.isNotEmpty(ls)) {
            for (int i = 0; i < ls.size(); i++) {
                MtWorkOrderRelVO r = new MtWorkOrderRelVO();
                r.setParentWorkOrderId(ls.get(i).getParentWorkOrderId());
                r.setWorkOrderId(ls.get(i).getWorkOrderId());
                rs.add(r);
                // 包含父亲的父亲
                MtWorkOrderRel newtmp = new MtWorkOrderRel();
                newtmp.setWorkOrderId(ls.get(i).getParentWorkOrderId());
                newtmp.setRelType(ls.get(i).getRelType());
                rs.addAll(findParent(tenantId, newtmp));
            }
        }
        return rs;
    }

    private List<MtWorkOrderRelVO> findChild(Long tenantId, MtWorkOrderRel dto) {

        dto.setTenantId(tenantId);

        List<MtWorkOrderRel> ls = mtWorkOrderRelMapper.select(dto);
        List<MtWorkOrderRelVO> rs = new ArrayList<MtWorkOrderRelVO>();
        if (CollectionUtils.isNotEmpty(ls)) {
            for (int i = 0; i < ls.size(); i++) {
                MtWorkOrderRelVO r = new MtWorkOrderRelVO();
                r.setParentWorkOrderId(ls.get(i).getParentWorkOrderId());
                r.setWorkOrderId(ls.get(i).getWorkOrderId());
                rs.add(r);
                // 包含父亲的父亲
                MtWorkOrderRel newtmp = new MtWorkOrderRel();
                newtmp.setParentWorkOrderId(ls.get(i).getWorkOrderId());
                newtmp.setRelType(ls.get(i).getRelType());
                rs.addAll(findChild(tenantId, newtmp));
            }
        }
        return rs;
    }

    @Override
    public List<MtWorkOrderRelVO> woRelTreeQuery(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelTreeQuery】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelTreeQuery】"));
        }
        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());
        List<MtWorkOrderRelVO> ls = new ArrayList<MtWorkOrderRelVO>();
        ls.addAll(findChild(tenantId, tmp));
        MtWorkOrderRel tmp2 = new MtWorkOrderRel();
        tmp2.setWorkOrderId(dto.getWorkOrderId());
        tmp2.setRelType(dto.getRelType());
        ls.addAll(findParent(tenantId, tmp2));
        return ls;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelLimitChildQtyUpdate(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelLimitChildQtyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelLimitChildQtyUpdate】"));
        }

        MtWorkOrder d = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (d == null || StringUtils.isEmpty(d.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woRelLimitChildQtyUpdate】"));
        }

        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());

        List<MtWorkOrderRelVO> ls = new ArrayList<MtWorkOrderRelVO>();
        ls.addAll(findChild(tenantId, tmp));
        List<String> wolist = ls.stream().map(MtWorkOrderRelVO::getWorkOrderId).collect(toList());

        if (CollectionUtils.isNotEmpty(wolist)) {
            List<String> sqlList = new ArrayList<String>();
            final Long userId = DetailsHelper.getUserDetails().getUserId();
            final Date currentDate = new Date(System.currentTimeMillis());

            wolist.forEach(c -> {
                MtWorkOrder mtWorkOrder = new MtWorkOrder();
                mtWorkOrder.setTenantId(tenantId);
                mtWorkOrder.setWorkOrderId(c);
                mtWorkOrder.setQty(d.getQty());
                mtWorkOrder.setLastUpdateDate(currentDate);
                mtWorkOrder.setLastUpdatedBy(userId);
                mtWorkOrder.setCid(Long.valueOf(this.customSequence.getNextKey("mt_work_order_cid_s")));
                sqlList.addAll(MtSqlHelper.getUpdateSql(mtWorkOrder));
            });

            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelStatusLimitChildQtyUpdate(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelStatusLimitChildQtyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelStatusLimitChildQtyUpdate】"));
        }

        MtWorkOrder d = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (d == null || StringUtils.isEmpty(d.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woRelStatusLimitChildQtyUpdate】"));
        }

        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());

        List<MtWorkOrderRelVO> ls = new ArrayList<MtWorkOrderRelVO>();
        ls.addAll(findChild(tenantId, tmp));
        List<String> wolist = ls.stream().map(MtWorkOrderRelVO::getWorkOrderId).collect(toList());

        if (CollectionUtils.isNotEmpty(wolist)) {
            List<MtWorkOrder> wols = mtWorkOrderMapper.selectByIdsCustom(tenantId, wolist);
            /* 挑选出NEW的 */
            if (CollectionUtils.isNotEmpty(wols)) {

                wols = wols.stream().filter((MtWorkOrder t) -> t.getStatus().equals("NEW")).collect(toList());
                wolist = wols.stream().map(MtWorkOrder::getWorkOrderId).collect(toList());

                if (CollectionUtils.isNotEmpty(wolist)) {
                    List<String> sqlList = new ArrayList<String>();
                    final Long userId = DetailsHelper.getUserDetails().getUserId();
                    final Date currentDate = new Date(System.currentTimeMillis());

                    wolist.forEach(c -> {
                        MtWorkOrder mtWorkOrder = new MtWorkOrder();
                        mtWorkOrder.setTenantId(tenantId);
                        mtWorkOrder.setWorkOrderId(c);
                        mtWorkOrder.setQty(d.getQty());
                        mtWorkOrder.setLastUpdateDate(currentDate);
                        mtWorkOrder.setLastUpdatedBy(userId);
                        mtWorkOrder.setCid(Long.valueOf(this.customSequence.getNextKey("mt_work_order_cid_s")));
                        sqlList.addAll(MtSqlHelper.getUpdateSql(mtWorkOrder));
                    });

                    this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woLimitSubRelDelete(Long tenantId, MtWorkOrderRel dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitChildRelDelete】"));
        }
        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woLimitChildRelDelete】"));
        }
        MtWorkOrder d = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (d == null || StringUtils.isEmpty(d.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woLimitChildRelDelete】"));
        }

        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());
        List<MtWorkOrderRelVO> ls = new ArrayList<MtWorkOrderRelVO>();
        ls.addAll(findChild(tenantId, tmp));
        mtWorkOrderRelMapper.batchDelete(tenantId, dto.getRelType(), ls);
    }

    /**
     * 暂挂功能
     *
     * @param tenantId
     * @param workOrderId
     * @param relType
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelStatusReleaseQtyLimitChildQtyUpdate(Long tenantId, String workOrderId, String relType) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRelStatusReleaseQtyLimitChildQtyUpdate】"));
        }
        if (StringUtils.isEmpty(relType)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API:woRelStatusReleaseQtyLimitChildQtyUpdate】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woRelStatusReleaseQtyLimitChildQtyUpdate】"));
        }

        // Double qty = mtWorkOrder.getQty() == null ? 0.0D :
        // mtWorkOrder.getQty();
        MtWorkOrderRel mtWorkOrderRel = new MtWorkOrderRel();
        mtWorkOrderRel.setParentWorkOrderId(workOrderId);
        mtWorkOrderRel.setRelType(relType);
        List<MtWorkOrderRelVO> mtWorkOrderRelVOs = woRelSubQuery(tenantId, mtWorkOrderRel);
        if (CollectionUtils.isEmpty(mtWorkOrderRelVOs)) {
            return;
        }

        List<String> workOrderIds = mtWorkOrderRelVOs.stream().map(MtWorkOrderRelVO::getWorkOrderId).collect(toList());
        List<MtWorkOrder> mtWorkOrders = this.mtWorkOrderRepository.woPropertyBatchGet(tenantId, workOrderIds);
        if (CollectionUtils.isEmpty(mtWorkOrders)) {
            return;
        }

        // List<MtWorkOrder> filterWorkOrders = mtWorkOrders.stream().filter(c
        // -> {
        // String status = c.getStatus();
        //
        // }).collect(toList());

    }
}
