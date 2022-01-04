package com.duck.utils;

import java.util.List;
import java.util.Random;

public final class RandomUtils {

    public static <T> T getRandomElementFromList(List<T> genericList){
        Random rand = new Random();

        return genericList.get(rand.nextInt(genericList.size()));
    }
}
