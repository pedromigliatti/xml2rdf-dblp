package eu.wdaqua.dblp;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static String outputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.nt";
    public static String inputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.xml";

    public static void main(String[] args) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);

//        String outputFile = "src/main/files/output";
//        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        //remeber that is necessary put the dblp.xml in the directory "files"
        FileInputStream fileXML = new FileInputStream(inputFile);

        XMLEventReader reader = factory.createXMLEventReader
                (inputFile, fileXML);

//        String key = "";
        String elementName = "";
        String type = "";
//        String elementPattern = "";
        List recordsList = Arrays.asList("article", "www");
        Map<String,String> elements = new HashMap<String, String>();
        while (reader.hasNext()) {

            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if(recordsList.contains(event.asStartElement().getName().toString()))
                   type = event.asStartElement().getName().toString();
                elementName = event.asStartElement().getName().toString();
                Iterator attributes = event.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = (Attribute) attributes.next();
                    elements.put(attribute.getName().toString(), attribute.getValue());
                }
            }
            else if(event.isCharacters()){
                if(!event.asCharacters().getData().toString().equals("\n"))
                    elements.put(elementName.toString(), event.asCharacters().getData().toString());
            }
            else if(event.isEndElement()){
                if(event.asEndElement().getName().toString().contains(type)){
                    Manipulation.mapToRDF(type, elements);
                    elements.clear();
                }
            }
        }
//                StartElement element = event.asStartElement();
//                elementName = element.getName().toString();
//                Iterator attributes = element.getAttributes();
//
//                //Select the prefix of the subject
//                if(elementName.equals("article")){
//                    elementPattern = "<https://dblp.org/rec/html/";
//                } else if(elementName.equals("www")){
//                    elementPattern = "<https://dblp.org/pid/";
//                }
//
//                //Extract the key and the mdate
//                String mdate = "";
//                while (attributes.hasNext()) {
//                    Attribute attribute = (Attribute) attributes.next();
//                    String attributeValue = attribute.getValue();
//                    if (attribute.getName().toString().contains("mdate")) {
//                        mdate = attributeValue;
//                    }
//                    if (attribute.getName().toString().contains("key")) {
//                        key = attributeValue.replace("homepage/","");
//                    }
//                }
//
//                //Write the properties
//                if (Arrays.asList(resourcesArray).contains(elementName)) {
//                    writer.write(elementPattern + key + ">" +
//                            " <mdate> " +
//                            "\"" + mdate + "\"" +
//                            "^^<http://www.w3.org/2001/XMLSchema#date> ." +
//                            "\n");
//                } else if (elementName.equals("author")) {
//                    writer.write(elementPattern + key + ">");
//                            if(!elementName.equals("www"))
//                                writer.write(" <http://dbpedia.org/ontology/author> ");
//                            else
//                                writer.write(" <https://schema.org/name> ");
//                } else if (elementName.equals("title")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://purl.org/dc/elements/1.1/title> ");
//                } else if (elementName.equals("pages")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://swrc.ontoware.org/ontology#pages> ");
//                } else if (elementName.equals(  "year")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://purl.org/dc/terms/issued> ");
//                } else if (elementName.equals("volume")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://swrc.ontoware.org/ontology#volume> ");
//                } else if (elementName.equals("journal")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://swrc.ontoware.org/ontology#journal> ");
//                } else if (elementName.equals("number")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://swrc.ontoware.org/ontology#number> ");
//                } else if (elementName.equals("url")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://purl.org/dc/elements/1.1/identifier> ");
//                } else if (elementName.equals("ee")) {
//                    writer.write(elementPattern + key + ">" +
//                            " <http://www.w3.org/2002/07/owl#sameAs> ");
//                }
//            }
//
//            //Write the Objects
//            String[] characterArray = {"author", "title", "pages", "volume", "journal", "number",
//                    "url",};
//            if (event.isCharacters()) {
//                Characters character = event.asCharacters();
//                if (!character.getData().toString().equals("\n")) {
//                    if (Arrays.asList(characterArray).contains(elementName))
//                        writer.write("\"" + character.getData().toString() + "\"" + "^^<http://www.w3.org/2001/XMLSchema#string> ." + "\n");
//                    else if (elementName.equals("year"))
//                        writer.write("\"" + character.getData().toString() + "\"" + "^^<http://www.w3.org/2001/XMLSchema#gYear> ." + "\n");
//                    else if (elementName.equals("ee"))
//                        writer.write("<" + character.getData().toString() + "> ." + "\n");
//                }
//            }

//        }
    }
}
