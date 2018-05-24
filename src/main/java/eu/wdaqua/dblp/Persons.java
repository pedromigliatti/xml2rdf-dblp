package eu.wdaqua.dblp;

import com.ctc.wstx.api.WstxInputProperties;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Persons {
    //extracts person records from xml file
    public static Map<String, String> extractPersonRecords(){
        Map<String, String> persons = new HashMap<>();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
            factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, Integer.valueOf(999999999));
            FileInputStream fileXML = new FileInputStream(Main.inputFile);
            XMLEventReader reader = factory.createXMLEventReader(Main.inputFile, fileXML);

            String key = "";
            String personName;
            boolean extractName = false;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) { //identify start TAG <www>
                    if (event.asStartElement().getName().toString().equals("www")) {
                        key = event.asStartElement().getAttributeByName(new QName("key")).getValue();
                    } else if ((event.asStartElement().getName().toString().equals("author")) && key.contains("homepage")) {
                        extractName = true;
                    }
                } else if (event.isCharacters() && extractName) {
                    if (!event.asCharacters().getData().toString().equals("\n")) {
                        personName = event.asCharacters().getData();
                        persons.put(personName, key.replace("homepages/", "https://dblp.org/pid/"));
                        extractName = false;
                    }
                } else if (event.isEndElement()) { //identify end TAG </www>
                    if (event.asEndElement().getName().toString().contains("www")) {
                        extractName = false;
                        key = "";
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return persons;
    }
}
