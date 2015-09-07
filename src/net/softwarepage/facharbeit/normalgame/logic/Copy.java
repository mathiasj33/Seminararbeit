package net.softwarepage.facharbeit.normalgame.logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Copy {

    public static Object copy(Object orig) {
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            System.out.println(e);
        }
        catch(ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
        return obj;
    }

}
