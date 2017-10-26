package com.mycompany.serializer;

import java.io.*;
import java.util.List;

public class JavaSerializer implements Serializer {

    @Override
    public void serializeObject(Object ob, String fileWithObjects) throws IOException {
        ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File(fileWithObjects)));

        out.writeObject(ob);
        out.flush();
        out.close();
    }

    @Override
    public Object deserializeObject(String fileWithObjects, Class obClass) throws IOException, ClassNotFoundException {
        ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(
                                new File(fileWithObjects)));

        Object ob = in.readObject();

        if (ob instanceof List) {
            List collection = (List) ob;

            for (Object o : collection) {
                if (o.getClass() == obClass) {
                    break;
                }
                else {
                    throw new ClassNotFoundException();
                }
            }
        }

        return ob;
    }
}
