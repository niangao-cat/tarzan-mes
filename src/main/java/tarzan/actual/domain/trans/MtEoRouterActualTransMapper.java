package tarzan.actual.domain.trans;

import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.entity.MtEoRouterActualHis;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 16:30
 * @Description:
 */
public interface MtEoRouterActualTransMapper {

    /**
     * 构建历史
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtEoRouterActualHis mtEoRouterActualTransToHis(MtEoRouterActual dto);
}
