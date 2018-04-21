package eu.wdaqua.dblp;

import com.ctc.wstx.api.WstxInputProperties;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import static eu.wdaqua.dblp.Main.outputFile;

public class Manipulation {

    public static void mapToRDF(String type, Map<String, String> elements, Map<String, String> persons, Map<String, String> types, Map<String, String> properties) throws IOException {

        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(outputFile), RDFFormat.NTRIPLES) ;



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
        Node subject = NodeFactory.createURI(finalElementPattern + finalKey);
        elements.entrySet().forEach((pair) -> {
            Node predicate;
            Node object;
            switch (pair.getValue()) {
                case ConstantList.MDATE:
                    predicate = NodeFactory.createURI(Properties.MDATE);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDdate);
                    Triple t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.AUTHOR:
                    if (!type.equals("www")) {
                        predicate = NodeFactory.createURI(Properties.AUTHOR);
                        object = NodeFactory.createURI(persons.get(pair.getKey()));
                        t = new Triple(subject,predicate,object);
                        writer.triple(t);
                    }
                    else {
                        predicate = NodeFactory.createURI(Properties.NAME);
                        object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                        t = new Triple(subject,predicate,object);
                        writer.triple(t);
                    }
                    break;
                case ConstantList.TITLE:
                    predicate = NodeFactory.createURI(Properties.TITLE);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.PAGES:
                    predicate = NodeFactory.createURI(Properties.PAGES);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.YEAR:
                    predicate = NodeFactory.createURI(Properties.YEAR);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDgYear);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.VOLUME:
                    predicate = NodeFactory.createURI(Properties.VOLUME);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDinteger);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.JOURNAL:
                    predicate = NodeFactory.createURI(Properties.JOURNAL);
                    object = NodeFactory.createURI(pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.NUMBER:
                    predicate = NodeFactory.createURI(Properties.NUMBER);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.URL:
                    if (!type.equals("www"))
                        predicate = NodeFactory.createURI(Properties.URL);
                    else
                        predicate = NodeFactory.createURI(Properties.URL_WWW);
                    object = NodeFactory.createURI("<http://dblp.uni-trier.de/" +  pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.EE:
                    predicate = NodeFactory.createURI(Properties.EE);
                    object = NodeFactory.createURI(pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.NOTE:
                    predicate = NodeFactory.createURI(Properties.NOTE);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.CITE:
                    predicate = NodeFactory.createURI(Properties.CITE);
                    object = NodeFactory.createURI("https://dblp.org/rec/html/" + pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.MONTH:
                    predicate = NodeFactory.createURI(Properties.MONTH);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDgMonth);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.ADDRESS:
                    predicate = NodeFactory.createURI(Properties.ADDRESS);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.EDITOR:
                    predicate = NodeFactory.createURI(Properties.EDITOR);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.PUBLISHER:
                    predicate = NodeFactory.createURI(Properties.PUBLISHER);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.BOOKTITLE:
                    predicate = NodeFactory.createURI(Properties.BOOKTITLE);
                    object = NodeFactory.createURI(pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.CDROM:
                    predicate = NodeFactory.createURI(Properties.BOOKTITLE);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.CROSSREF:
                    predicate = NodeFactory.createURI(Properties.CROSSREF);
                    object = NodeFactory.createURI(pair.getKey());
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.ISBN:
                    predicate = NodeFactory.createURI(Properties.ISBN);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.SERIES:
                    predicate = NodeFactory.createURI(Properties.SERIES);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.SCHOOL:
                    predicate = NodeFactory.createURI(Properties.SCHOOL);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.CHAPTER:
                    predicate = NodeFactory.createURI(Properties.CHAPTER);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
                case ConstantList.PUBLNR:
                    predicate = NodeFactory.createURI(Properties.PUBLNR);
                    object = NodeFactory.createLiteral(pair.getKey(), XSDDatatype.XSDstring);
                    t = new Triple(subject,predicate,object);
                    writer.triple(t);
                    break;
            }
        });


        if(types.containsKey(type.toUpperCase())){
            Node predicate = NodeFactory.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            Node object = NodeFactory.createLiteral(types.get(type.toUpperCase()), XSDDatatype.XSDstring);
            Triple t = new Triple(subject,predicate,object);
            writer.triple(t);
        }
    }

    public static void writeVocabulary(List<String> typeList, List<String> elementList, Map<String, String> types, Map<String, String> properties) throws IOException {
        StringBuffer writer = new StringBuffer();

        types.entrySet().forEach((pair) -> {
            writer.append(pair.getValue() + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2000/01/rdf-schema#Class> .\n");
        });

        properties.entrySet().forEach((pair) -> {
            writer.append(pair.getValue() + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> .\n");
        });

        Utility.writeStringBuffer(writer, outputFile, false);
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
