package com.melik.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

/*
 * Multi-thread ortamlarda bile sadece çağrıldığı zaman bir kere nesnesi oluşacak sonrasında başka instance oluşmadan aynı instance kullanılacak.
 * */
public class CollectionsUtil {

    private CollectionsUtil() {

    }

    private static class CollectionsUtilHolder {
        static final CollectionsUtil INSTANCE = new CollectionsUtil();
    }

    public static CollectionsUtil getInstance() {
        return CollectionsUtilHolder.INSTANCE;
    }

    public <T> List<T> getListFromIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
