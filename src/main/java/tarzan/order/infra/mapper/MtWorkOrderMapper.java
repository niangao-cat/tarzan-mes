package tarzan.order.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.vo.MtWorkOrderVO26;
import tarzan.order.domain.vo.MtWorkOrderVO30;
import tarzan.order.domain.vo.MtWorkOrderVO38;
import tarzan.order.domain.vo.MtWorkOrderVO39;
import tarzan.order.domain.vo.MtWorkOrderVO42;
import tarzan.order.domain.vo.MtWorkOrderVO47;

/**
 * 生产指令Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderMapper extends BaseMapper<MtWorkOrder> {


    /**
     * limitWoQuery-获取满足指定属性限制的生产指令
     * 
     * @param dto
     * @return
     */
    List<String> limitWoQuery(@Param("tenantId") Long tenantId, @Param("dto") MtWorkOrderVO26 dto);

    /**
     * numberLimitWoGet-根据生产指令编码获取生产指令ID
     * 
     * @param workOrderNum
     * @return
     */
    String numberLimitWoGet(@Param("tenantId") Long tenantId, @Param("workOrderNum") String workOrderNum);

    void batchDelete(@Param("tenantId") Long tenantId, @Param(value = "orderids") List<String> orderids);

    List<MtWorkOrder> selectByIdsCustom(@Param("tenantId") Long tenantId, @Param(value = "woIds") List<String> woIds);

    /**
     * 获取生产指令组件装配周期/sen.luo 2018-03-18
     * 
     * @param tenantId
     * @return
     */
    String woNextNumberGet(@Param("tenantId") Long tenantId);

    /**
     * propertyLimitWoPropertyQuery-根据属性获取生产指令信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrder> propertyLimitWoPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "dto") MtWorkOrderVO30 dto);

    List<String> selectByPriority(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "status") List<String> status, @Param(value = "priority") Long priority);


    /**
     * select by wipEntityIds
     */
    List<MtWorkOrder> selectByWipEntityId(@Param("tenantId") Long tenantId,
                                          @Param(value = "wipEntityIds") String wipEntityIds);

    /**
     * select by wipEntityIds
     */
    List<MtWorkOrder> selectByWorkOrderNum(@Param("tenantId") Long tenantId,
                                           @Param("workOrderNum") String workOrderNum);

    /**
     * 获取生产指令列表
     */
    List<MtWorkOrderVO39> selectForUi(@Param("tenantId") Long tenantId,
                                      @Param("dto") MtWorkOrderVO38 dto, @Param("siteIds") List<String> siteIds);

    /**
     * 获取生产指令列表
     */
    List<MtWorkOrderVO42> selectBomComponent(@Param("tenantId") Long tenantId,
                                             @Param("dto") MtWorkOrderVO47 dto);

    /**
     * 获取装配清单
     */
    List<MtWorkOrderVO42> selectRKBomComponent(@Param("tenantId") Long tenantId,
                                             @Param("dto") MtWorkOrderVO47 dto);

}
