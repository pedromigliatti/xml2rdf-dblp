import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        String outputFile = "src/main/files/output";
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        //remeber that is necessary put the dblp.xml in the directory "files"
        String inputFile = "src/main/files/dblp.xml";
        FileInputStream fileXML = new FileInputStream(inputFile);

        XMLEventReader reader = factory.createXMLEventReader
                (inputFile, fileXML);

        String key = "";
        String nameElement = "";
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if(event.isStartElement()) {
                StartElement element = event.asStartElement();
                nameElement = element.getName().toString();
                Iterator attributes = element.getAttributes();
                if(nameElement.equals("article")) {
                    String mdate = "";
                    while (attributes.hasNext()) {
                        String attribute = attributes.next().toString();
                        if (attribute.contains("mdate")) {
                            mdate = attribute.split("=")[1].replace("\'", "\"");
                        }
                        if (attribute.contains("key")) {
                            key = attribute.split("=")[1].replace("\'", "");
                        }
                    }
                    writer.write("<http://dblp.l3s.de/d2r/resource/publications/" + key + ">" +
                            " <mdate> " +
                            mdate + "^^<http://www.w3.org/2001/XMLSchema#date>." +
                            "\n");
                } else if(nameElement.equals("author")){
                    writer.write("<http://dblp.l3s.de/d2r/resource/publications/" + key + ">" +
                            " <http://dbpedia.org/ontology/author> ");
                }
            }
            if(event.isCharacters()){
                Characters character = event.asCharacters();
                if(!character.getData().toString().equals("\n")) {
                    if (nameElement.equals("author"))
                        writer.write("<" + character.getData().toString() + ">." + "\n");
                }
            }

        }
    }
}
