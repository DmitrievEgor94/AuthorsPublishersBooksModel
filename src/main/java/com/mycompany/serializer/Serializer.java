package com.mycompany.serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Serializer {
    void serializeObject(Object ob, String fileWithObjects) throws IOException, IllegalAccessException;

    Object deserializeObject(String fileWithObjects, Class obClass) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
