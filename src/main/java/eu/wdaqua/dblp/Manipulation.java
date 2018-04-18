package eu.wdaqua.dblp;

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
        String finalElementPattern = elementPattern;
        elements.entrySet().forEach((pair) -> {
            switch (pair.getKey()){
                case ConstantList.MDATE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.MDATE +
                            "\"" + elements.get("mdate") + "\"" +
                            "^^<http://www.w3.org/2001/XMLSchema#date> ." +
                            "\n");
                    break;
                case ConstantList.AUTHOR:
                    writer.append(finalElementPattern + elements.get("key") + ">");
                    if (!type.equals("www"))
                        writer.append(Utility.AUTHOR);
                    else
                        writer.append(Utility.NAME);
                    writer.append("\"" + elements.get("author") + "\""+"^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.TITLE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.TITLE + "\"" + elements.get("title") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PAGES:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.PAGES + "\"" + elements.get("pages") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.YEAR:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.YEAR + "\"" + elements.get("year") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#gYear> ." + "\n");
                    break;
                case ConstantList.VOLUME:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.VOLUME + "\"" + elements.get("volume") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.JOURNAL:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.JOURNAL + "\"" + elements.get("journal") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.NUMBER:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.NUMBER + "\"" + elements.get("number") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.URL:
                    writer.append(finalElementPattern + elements.get("key") + ">");
                    if(!type.equals("www"))
                        writer.append(Utility.URL);
                    else
                        writer.append(Utility.URL_WWW);
                    writer.append("\"" + elements.get("url") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.EE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.EE + "<" + elements.get("ee") + "> .\n");
                    break;
                case ConstantList.NOTE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.NOTE + "<" + elements.get("note") + "> .\n");
                    break;
                case ConstantList.CITE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.CITE + "<" + elements.get("cite") + "> .\n");
                    break;
                case ConstantList.MONTH:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.MONTH + "\"" + elements.get("month") + "\"" + "^^<https://www.w3.org/2001/XMLSchema#gMonth> .\n");
                    break;
                case ConstantList.ADDRESS:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.ADDRESS + "\"" + elements.get("address") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.EDITOR:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.EDITOR + "\"" + elements.get("editor") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PUBLISHER:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.PUBLISHER + "\"" + elements.get("publisher") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.BOOKTITLE:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.BOOKTITLE + "\"" + elements.get("booktitle") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CDROM:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.CDROM + "\"" + elements.get("cdrom") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CROSSREF:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.CROSSREF + "\"" + elements.get("crossref") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.ISBN:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.ISBN + "\"" + elements.get("isbn") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.SERIES:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.SERIES + "\"" + elements.get("series") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.SCHOOL:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.SCHOOL + "\"" + elements.get("school") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CHAPTER:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.CHAPTER + "\"" + elements.get("chapter") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PUBLNR:
                    writer.append(finalElementPattern + elements.get("key") + ">" +
                            Utility.PUBLNR + "\"" + elements.get("publnr") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                default:
                    break;
            }
        });


        Utility.writeStringBuffer(writer,Main.outputFile,true);
        writer.delete(0, writer.length());
    }
}
