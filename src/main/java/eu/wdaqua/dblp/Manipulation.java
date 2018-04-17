package eu.wdaqua.dblp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Manipulation {

    public static void mapToRDF(String type, Map<String, String> elements) throws IOException {

        StringBuffer writer = new StringBuffer();

        String elementPattern = "";
        if (type.equals("article")) {
            elementPattern = "<https://dblp.org/rec/html/";
        } else if (type.equals("www")) {
            elementPattern = "<https://dblp.org/pid/";
        }
        if (elements.containsKey("mdate")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.MDATE +
                    "\"" + elements.get("mdate") + "\"" +
                    "^^<http://www.w3.org/2001/XMLSchema#date> ." +
                    "\n");
        }
        if (elements.containsKey("author")) {
            writer.append(elementPattern + elements.get("key") + ">");
            if (!type.equals("www"))
                writer.append(Utility.AUTHOR);
            else
                writer.append(Utility.NAME);
            writer.append("\"" + elements.get("author") + "\""+"^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("title") && !type.equals("www")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.TITLE + "\"" + elements.get("title") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("pages")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.PAGES + "\"" + elements.get("pages") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("year")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.YEAR + "\"" + elements.get("year") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#gYear> ." + "\n");
        }
        if (elements.containsKey("volume")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.VOLUME + "\"" + elements.get("volume") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("journal")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.JOURNAL + "\"" + elements.get("journal") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("number")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.NUMBER + "\"" + elements.get("number") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("url")) {
            writer.append(elementPattern + elements.get("key") + ">");
            if(!type.equals("www"))
                writer.append(Utility.URL);
            else
                writer.append(Utility.URL_WWW);
            writer.append("\"" + elements.get("url") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
        }
        if (elements.containsKey("ee")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.EE + "<" + elements.get("ee") + "> .\n");
        }
        if (elements.containsKey("note")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.NOTE + "<" + elements.get("note") + "> .\n");
        }
        if (elements.containsKey("cite")) {
            writer.append(elementPattern + elements.get("key") + ">" +
                    Utility.CITE + "<" + elements.get("cite") + "> .\n");
        }

        Utility.writeStringBuffer(writer,Main.outputFile,true);
    }
}
