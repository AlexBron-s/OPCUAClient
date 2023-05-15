package org.example;

import java.io.*;
import java.util.ArrayList;

public class TagRepositoryFileImpl {
    private static final String FILE_NAME = "src/main/java/org/example/Tags.txt";

    static <o> void write (ArrayList<o> o){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Object object : o) {
                bw.write(object.toString() + "\n");
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
