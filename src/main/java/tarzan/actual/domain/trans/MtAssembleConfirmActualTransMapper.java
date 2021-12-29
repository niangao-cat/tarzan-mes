package tarzan.actual.domain.trans;

import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.vo.MtAssembleConfirmActualTupleVO;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO25;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO26;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 17:52
 * @Description:
 */
public interface MtAssembleConfirmActualTransMapper {

    /**
     * MtAssembleConfirmActual -> MtAssembleConfirmActualTupleVO
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtAssembleConfirmActualTupleVO actualToActualTupleVO(MtAssembleConfirmActual dto);

    /**
     * MtAssembleConfirmActualVO25 -> MtAssembleConfirmActualTupleVO
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtAssembleConfirmActualTupleVO actualVO25ToTupleVO(MtAssembleConfirmActualVO25 dto);

    /**
     * MtAssembleConfirmActualVO25 -> MtAssembleConfirmActual
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtAssembleConfirmActual actualVO25ToActual(MtAssembleConfirmActualVO25 dto);

    /**
     * MtAssembleConfirmActualVO25 -> MtAssembleConfirmActualVO25
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtAssembleConfirmActualVO25 cloneActualVO25(MtAssembleConfirmActualVO25 dto);

    /**
     * MtAssembleConfirmActualVO25 -> MtAssembleConfirmActualVO25
     *
     * @author chuang.yang
     * @date 2020/10/23
     * @param dto
     */
    MtAssembleConfirmActualVO26 actualVO25ToActualVO26(MtAssembleConfirmActualVO25 dto);
}
