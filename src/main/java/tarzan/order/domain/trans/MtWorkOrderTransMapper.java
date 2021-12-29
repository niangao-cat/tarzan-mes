package tarzan.order.domain.trans;

import org.mapstruct.Mapper;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.vo.MtWorkOrderVO36;
import tarzan.order.domain.vo.MtWorkOrderVO66;

/**
 * @Author: chuang.yang
 * @Date: 2020/9/25 17:42
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface MtWorkOrderTransMapper {

    /**
     * MtWorkOrder ——> MtWorkOrderVO36
     *
     * @author chuang.yang
     * @date 2020/9/25
     */
    MtWorkOrderVO36 mtWorkOrderToVO36(MtWorkOrder dto);

    /**
     * MtWorkOrderVO66 ——> MtWorkOrder
     *
     * @author chuang.yang
     * @date 2020/10/30
     */
    MtWorkOrder mtWorkOrderVO66ToMtWorkOrder(MtWorkOrderVO66 dto);

    /**
     * MtWorkOrder ——> MtWorkOrderVO66
     *
     * @author chuang.yang
     * @date 2020/10/30
     */
    MtWorkOrderVO66 mtWorkOrderToMtWorkOrderVO66(MtWorkOrder dto);

    /**
     * MtWorkOrder ——> MtWorkOrderHis
     *
     * @author chuang.yang
     * @date 2020/10/30
     */
    MtWorkOrderHis mtWorkOrderToMtWorkOrderHis(MtWorkOrder dto);
}
