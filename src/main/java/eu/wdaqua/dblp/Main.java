package eu.wdaqua.dblp;

import com.ctc.wstx.api.WstxInputProperties;

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
//    public static String outputFile = "/home/pedro/Documentos/WDAqua/dblp.nt";
//    public static String inputFile = "/home/pedro/Documentos/WDAqua/dblp.xml";

    public static void main(String[] args) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
        factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, Integer.valueOf(999999999));

        //remeber that is necessary put the dblp.xml in the directory "files"
        FileInputStream fileXML = new FileInputStream(inputFile);

        XMLEventReader reader = factory.createXMLEventReader(inputFile, fileXML);

        String elementName = "";
        String type = "";
        List typeList = Arrays.asList("article","i","sub","sup","proceedings","tt","inproceedings","incollection","book","phdthesis","mastersthesis","www");
        List<String> elementsList = Arrays.asList("author","editor","title","booktitle","pages","year","address","journal","volume","number","month","url","ee","cdrom","cite","publisher","note","crossref","isbn","series","school","chapter","publnr");

        Map<String,String> elements = new HashMap<String, String>();

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                if(typeList.contains(event.asStartElement().getName().toString()))
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
    }
}
