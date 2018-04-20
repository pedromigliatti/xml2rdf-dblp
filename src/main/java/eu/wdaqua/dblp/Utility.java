package eu.wdaqua.dblp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utility {
    public static void writeStringBuffer(StringBuffer str, String path, boolean next) throws IOException {
        //write contents of StringBuffer to a file
        try (BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(path), next))) {
            //write contents of StringBuffer to a file
            bwr.write(str.toString());
            //flush the stream
            bwr.flush();
            //close the stream
        }
    }

}
