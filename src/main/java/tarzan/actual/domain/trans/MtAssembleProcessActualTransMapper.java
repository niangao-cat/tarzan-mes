package tarzan.actual.domain.trans;

import tarzan.actual.domain.vo.*;

/**
 * Tangxiao
 */
public interface MtAssembleProcessActualTransMapper {


    /**
     * MtAssembleProcessActualVO16 -> MtAssembleProcessActualVO9
     *
     * @author Tangxiao
     * @date 2020/11/02
     * @param dto
     */
    MtAssembleProcessActualVO9 actualVO16ToActualVO9(MtAssembleProcessActualVO16 dto);

    /**
     * MtAssembleProcessActualVO16 -> MtEoComponentActualVO31
     * 
     * @param dto
     * @return
     */
    MtEoComponentActualVO31 actualVO16ToEoComponentActualVO31(MtAssembleProcessActualVO16 dto);

    /**
     * mtAssProAcVO12TOMtAssProActVO13
     */
    MtAssembleProcessActualVO13 mtAssProAcVO12TOMtAssProActVO13(MtAssembleProcessActualVO12.AssembleInfo dto);

}
