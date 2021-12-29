package tarzan.actual.domain.trans;

import tarzan.actual.domain.entity.MtWorkOrderCompActualHis;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.vo.MtWoComponentActualTupleVO;
import tarzan.actual.domain.vo.MtWoComponentActualVO30;

/**
 * @Author: changbu 2020/10/31 14:48
 */
public interface MtWoComponentActualTransMapper {

    /**
     * MtWorkComponentActualVO30 -> MtWorkComponentActualVO31
     *
     * @param dto
     * @date 2020/10/31
     */
    MtWoComponentActualTupleVO woComponentActualVO30ToActualTupleVO(MtWoComponentActualVO30 dto);

    /**
     * MtWorkOrderComponentActual -> MtWorkOrderCompActualHis
     * 
     * @param dto
     * @return
     */
    MtWorkOrderCompActualHis woComponentActualToCompActualHis(MtWorkOrderComponentActual dto);

}
