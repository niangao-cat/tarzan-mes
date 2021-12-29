package tarzan.order.domain.trans;

import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.vo.MtEoActualVO10;
import tarzan.actual.domain.vo.MtWorkOrderActualVO8;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.vo.MtEoVO38;
import tarzan.order.domain.vo.MtEoVO45;

public interface MtEoTransMapper {
    /**
     * MtEo ——> MtEoVO38
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtEoVO38 mtEoToMtEoVO38(MtEo dto);

    /**
     * MtEoActual ——> MtEoActualVO10
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtEoActualVO10 mEoActualToMtEoActualVO10(MtEoActual dto);

    /**
     * MtWorkOrderActual ——> MtWorkOrderActualVO8.ActualInfo
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtWorkOrderActualVO8.ActualInfo mtWorkOrderActualToActualInfo(MtWorkOrderActual dto);

    /**
     * MtEoVO45 ——> MtEo
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtEoVO45 mtEoToMtEoVO45(MtEo dto);

    /**
     * MtEoVO45 ——> MtEo
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtEo mtEoVO45ToMtEo(MtEoVO45 dto);

    /**
     * MtEoVO45 ——> MtEo
     *
     * @author guichuan.li
     * @date 2020/10/26
     */
    MtEoHis mtEoToMtEoHis(MtEo dto);

}
