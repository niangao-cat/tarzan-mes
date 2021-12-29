package tarzan.inventory.domain.trans;

import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO13;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO17;

public interface MtInvOnhandQuantityTransferMapper {

    /**
     * 转换 MtInvOnhandQuantityVO13 -> MtInvOnhandQuantityVO
     * 
     * @author tangxiao
     * @param invOnhandQuantityVO13
     * @return MtInvOnhandQuantityVO
     */
    MtInvOnhandQuantityVO invOnhandQuantityVO13ToInvOnhandQuantityVO(MtInvOnhandQuantityVO13 invOnhandQuantityVO13);

    /**
     * 转换 MtInvOnhandQuantity -> MtInvOnhandQuantityVO
     *
     * @author tangxiao
     * @param invOnhandQuantity
     * @return MtInvOnhandQuantityVO
     */
    MtInvOnhandQuantityVO invOnhandQuantityToInvOnhandQuantityVO(MtInvOnhandQuantity invOnhandQuantity);

    /**
     * 转换 MtInvOnhandQuantity -> MtInvJournal
     * 
     * @author tangxiao
     * @param invOnhandQuantity
     * @return MtInvJournal
     */
    MtInvJournal invOnhandQuantityToInvJournal(MtInvOnhandQuantity invOnhandQuantity);

    /**
     * 转换 MtInvOnhandQuantityVO -> MtInvOnhandQuantity
     * 
     * @author tangxiao
     * @param invOnhandQuantityVO
     * @return MtInvOnhandQuantity
     */
    MtInvOnhandQuantity invOnhandQuantityVOToInvOnhandQuantity(MtInvOnhandQuantityVO invOnhandQuantityVO);

    /**
     * 转换 MtInvOnhandQuantityVO13 -> MtInvOnhandQuantityVO17
     * 
     * @author tangxiao
     * @param invOnhandQuantityVO13
     * @return MtInvOnhandQuantityVO17
     */
    MtInvOnhandQuantityVO17 invOnhandQuantityVO13ToInvOnhandQuantityVO17(MtInvOnhandQuantityVO13 invOnhandQuantityVO13);

    /**
     * 转换 MtInvOnhandQuantityVO17 -> MtInvOnhandQuantityVO13
     * 
     * @author tangxiao
     * @param invOnhandQuantityVO17
     * @return MtInvOnhandQuantityVO13
     */
    MtInvOnhandQuantityVO13 invOnhandQuantityVO17ToInvOnhandQuantityVO13(MtInvOnhandQuantityVO17 invOnhandQuantityVO17);

}
