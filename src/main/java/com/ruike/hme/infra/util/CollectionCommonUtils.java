package com.ruike.hme.infra.util;

import com.ruike.hme.infra.util.functional.ToBigDecimalFunction;

import java.io.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * <p>
 * 集合类工具包
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/6 11:36
 */
public class CollectionCommonUtils {

    /**
     * 深度拷贝List对象
     *
     * @param src 源list
     * @return java.util.List<T>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/6 11:38:29
     */
    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private CollectionCommonUtils() {
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    /**
     * Simple implementation class for {@code Collector}.
     *
     * @param <T> the type of elements to be collected
     * @param <R> the type of the result
     */
    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Function<A, R> finisher, Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Collector.Characteristics> characteristics() {
            return characteristics;
        }
    }

    /**
     * 汇总BigDecimal的方法，用于groupingBy
     * 示例如下：
     * Map< String, BigDecimal > map = list.stream()
     * .collect(Collectors.groupingBy(dto::getType, CollectionCommonUtils.summingBigDecimal(dto::getNum)));
     *
     * @param mapper 汇总字段
     * @return java.util.stream.Collector<T, ?, java.math.BigDecimal>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/9 10:28:42
     */
    public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(() -> new BigDecimal[1], (nums, fun) -> {
            if (nums[0] == null) {
                nums[0] = BigDecimal.ZERO;
            }
            nums[0] = nums[0].add(mapper.applyAsBigDecimal(fun));
        }, (nums, adds) -> {
            nums[0] = nums[0].add(adds[0]);
            return nums;
        }, nums -> nums[0], CH_NOID);
    }
}
