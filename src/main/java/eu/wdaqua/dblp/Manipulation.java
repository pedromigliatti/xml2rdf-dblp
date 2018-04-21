package eu.wdaqua.dblp;

import com.ctc.wstx.api.WstxInputProperties;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Manipulation {

    public static void mapToRDF(String type, Map<String, String> elements, Map<String, String> persons, Map<String, String> types, Map<String, String> properties) throws IOException {

        StringBuffer writer = new StringBuffer();
        String key = "";

        for(String item : elements.keySet()){
            if(elements.get(item).equals("key")) {
                key = item.replace("homepages/", "");
            }
        }
        elements.remove(key);

        String elementPattern = "";
        if (type.equals("www")) {
            elementPattern = "<https://dblp.org/pid/";
        } else if(type.equals("book")) {
            elementPattern = "https://dblp.org/db/";
        } else{
            elementPattern = "<https://dblp.org/rec/html/";
        }
        String finalElementPattern = elementPattern;
        String finalKey = key;
        elements.entrySet().forEach((pair) -> {
            writer.append(finalElementPattern + finalKey + ">");
            switch (pair.getValue()) {
                case ConstantList.MDATE:
                    writer.append(
                            Properties.MDATE +
                                    "\"" + pair.getKey().replace("\"","\\\"") + "\"" +
                                    "^^<http://www.w3.org/2001/XMLSchema#date> ." +
                                    "\n");
                    break;
                case ConstantList.AUTHOR:
                    if (!type.equals("www")) {
                        writer.append(Properties.AUTHOR);
                        writer.append("<" + persons.get(pair.getKey().replace("\"","\\\"")) + "> .\n");
                    }
                    else {
                        writer.append(Properties.NAME);
                        writer.append("\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    }
                    break;
                case ConstantList.TITLE:
                    writer.append(
                            Properties.TITLE + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PAGES:
                    writer.append(
                            Properties.PAGES + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.YEAR:
                    writer.append(
                            Properties.YEAR + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#gYear> ." + "\n");
                    break;
                case ConstantList.VOLUME:
                    writer.append(
                            Properties.VOLUME + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#integer> .\n");
                    break;
                case ConstantList.JOURNAL:
                    writer.append(
                            Properties.JOURNAL + "<" + pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.NUMBER:
                    writer.append(
                            Properties.NUMBER + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.URL:
                    if (!type.equals("www"))
                        writer.append(Properties.URL);
                    else
                        writer.append(Properties.URL_WWW);
                    writer.append("<http://dblp.uni-trier.de/" +  pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.EE:
                    writer.append(
                            Properties.EE + "<" + pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.NOTE:
                    writer.append(
                            Properties.NOTE + "\"" + pair.getKey().replace("\"","\\\"") + "\"^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CITE:
                    writer.append(
                            Properties.CITE + "<" + "https://dblp.org/rec/html/" + pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.MONTH:
                    writer.append(
                            Properties.MONTH + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<https://www.w3.org/2001/XMLSchema#gMonth> .\n");
                    break;
                case ConstantList.ADDRESS:
                    writer.append(
                            Properties.ADDRESS + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.EDITOR:
                    writer.append(
                            Properties.EDITOR + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PUBLISHER:
                    writer.append(
                            Properties.PUBLISHER + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.BOOKTITLE:
                    writer.append(
                            Properties.BOOKTITLE + "<" + pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.CDROM:
                    writer.append(
                            Properties.CDROM + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CROSSREF:
                    writer.append(
                            Properties.CROSSREF + "<" + pair.getKey().replace("\"","\\\"") + "> .\n");
                    break;
                case ConstantList.ISBN:
                    writer.append(
                            Properties.ISBN + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.SERIES:
                    writer.append(
                            Properties.SERIES + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.SCHOOL:
                    writer.append(
                            Properties.SCHOOL + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.CHAPTER:
                    writer.append(
                            Properties.CHAPTER + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                case ConstantList.PUBLNR:
                    writer.append(
                            Properties.PUBLNR + "\"" + pair.getKey().replace("\"","\\\"") + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> .\n");
                    break;
                default:
                    int last = writer.lastIndexOf(finalElementPattern);
                    if (last >= 0) { writer.delete(last, writer.length()); }
                    break;
            }
        });


        if(types.containsKey(type.toUpperCase())){
            writer.append(finalElementPattern + finalKey + ">"+
                    " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " + types.get(type.toUpperCase()) + "\n");
        }

        Utility.writeStringBuffer(writer, Main.outputFile, true);
        writer.delete(0, writer.length());
    }

    public static void writeVocabulary(List<String> typeList, List<String> elementList, Map<String, String> types, Map<String, String> properties) throws IOException {
        StringBuffer writer = new StringBuffer();

        types.entrySet().forEach((pair) -> {
            writer.append(pair.getValue() + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2000/01/rdf-schema#Class> .\n");
        });

        properties.entrySet().forEach((pair) -> {
            writer.append(pair.getValue() + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> .\n");
        });

        Utility.writeStringBuffer(writer, Main.outputFile, false);
        writer.delete(0, writer.length());
    }

    public static Map<String, String> extractPersonRecords() throws XMLStreamException, FileNotFoundException {
                XMLInputFactory factory = XMLInputFactory.newInstance();

        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
        factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, Integer.valueOf(999999999));

        FileInputStream fileXML = new FileInputStream(Main.inputFile);

        XMLEventReader reader = factory.createXMLEventReader(Main.inputFile, fileXML);

        Map<String,String> persons = new HashMap<String, String>();

        String key = "";
        String personName;
        boolean extractName = false;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if(event.asStartElement().getName().toString().equals("www")){
                    key = event.asStartElement().getAttributeByName(new QName("key")).getValue();
                } else if((event.asStartElement().getName().toString().equals("author")) && key.contains("homepage")){
                    extractName = true;
                }
            } else if(event.isCharacters() && extractName) {
                if (!event.asCharacters().getData().toString().equals("\n")) {
                    personName = event.asCharacters().getData().toString();

                    persons.put(personName, key.replace("homepages/","https://dblp.org/pid/" ));
                    extractName = false;
                }
            } else if(event.isEndElement()){
            if(event.asEndElement().getName().toString().contains("www")){
                extractName = false;
                key = "";
            }

        }
        }
        return persons;
    }
}
