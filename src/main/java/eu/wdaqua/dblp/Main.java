package eu.wdaqua.dblp;

import com.ctc.wstx.api.WstxInputProperties;

import eu.wdaqua.dblp.ontology.Classes;
import eu.wdaqua.dblp.ontology.PropertyMapping;
import eu.wdaqua.dblp.ontology.Properties;
import eu.wdaqua.dblp.ontology.Type;
import eu.wdaqua.dblp.ontology.Utility;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static eu.wdaqua.dblp.ontology.Utility.createURI;

public class Main {
//    public static String outputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.nt";
//    public static String inputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.xml";

//    Directory of tests on Pedro's computer
    public static String outputFile = "/home/pedro/Documentos/WDAqua/dblpDocuments/dblp2.nt";
    public static String inputFile = "/home/pedro/Documentos/WDAqua/dblpDocuments/dblp.xml";

    public static void main(String[] args) throws IOException, XMLStreamException, IllegalAccessException {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
        factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, Integer.valueOf(999999999));
        FileInputStream fileXML = new FileInputStream(inputFile);
        XMLEventReader reader = factory.createXMLEventReader(inputFile, fileXML);

        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(outputFile), RDFFormat.NTRIPLES);

        Map<String,String> persons = Persons.extractPersonRecords();
        Utility.writeVocabulary(writer);

        List<String> path = new ArrayList<>();
        Node subject = null;
        String key = "";
        int line =0;
        while (reader.hasNext()) {
            line++;
            if (line%100000==0){
                System.out.println(line);
            }
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                path.add(event.asStartElement().getName().toString());
                //Extract the attributes
                Iterator attributes = event.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = (Attribute) attributes.next();
                    if(attribute.getName().toString().equals("key")) { //when a key is encountered then a new element is coming
                        key = attribute.getValue();
                        //generate the subject
                        //System.out.println("PATH "+path.get(1));
                        if (path.get(1).equals("www")) {
                            subject = createURI("https://dblp.org/pid/" + attribute.getValue());
                        } else if (path.get(1).equals("book")) {
                            subject = createURI("https://dblp.org/db/" + attribute.getValue());
                        } else {
                            subject = createURI("https://dblp.org/rec/html/" + attribute.getValue());
                        }
                        //Map the classes
                        Map<String, String> classes = Classes.getFields();
                        if (classes.containsKey(path.get(1))) {
                            Node predicate = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                            Node object = NodeFactory.createURI(classes.get(path.get(1)));
                            Triple t = new Triple(subject, predicate, object);
                            writer.triple(t);
                        } else {
                            System.out.println("Unmapped class tag " + path.get(1));
                        }
                    }
                }
            } else if(event.isCharacters()){
                String tagEntry = event.asCharacters().getData().toString();
                if (!tagEntry.equals("\n")) {
                    //Map the properties
//                    if (line>90200000){
//                        System.out.println(event.getLocation()+"  "+path.toString()+" "+key);
//                    }
                    List<PropertyMapping> propertyMappings = Properties.getMappings();
//                    for (PropertyMapping propertyMapping : propertyMappings){
                        if (path.size()>2) { // there are some bugs in the dump
//                            System.out.println(Properties.getMappedTags().toString());
                            if (Properties.getMappedTags().containsKey((path.get(2)))) {
                                for (PropertyMapping propertyMapping : Properties.getMapping(path.get(2))) {
                                    if (propertyMapping.getType() != Type.CUSTOM) {
                                        Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry,propertyMapping);
                                        writer.triple(t);
                                    } else {
                                        if (path.get(2).equals("crossref")) {
                                            String[] crossref = tagEntry.split("/");
                                            Node predicate = createURI(propertyMapping.getPropertyUri());
                                            Node object = createURI("http://dblp.uni-trier.de/db/" + crossref[0] + "/" + crossref[1]);
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (path.get(2).equals("url")) {
                                            Node predicate = createURI(propertyMapping.getPropertyUri());
                                            Node object = createURI("http://dblp.uni-trier.de/" + tagEntry);
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            //Use the tag for generating the booktitle uri
                                            for (PropertyMapping p : Properties.getMapping("booktitle")) {
                                                if (!p.getPropertyUri().contains("#label")) {
                                                    predicate = createURI(p.getPropertyUri());
                                                    object = createURI("http://dblp.uni-trier.de/" + tagEntry.split("#")[0]);
                                                    t = new Triple(subject, predicate, object);
                                                    writer.triple(t);
                                                }
                                            }
                                        } else if (path.get(2).equals("journal")) {
                                            Node predicate = createURI(propertyMapping.getPropertyUri());
                                            Node object = createURI("http://dblp.uni-trier.de/db/" + key.split("/")[0] + "/" + key.split("/")[1]);
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (path.get(2).equals("author")) {
                                            Node predicate = createURI(propertyMapping.getPropertyUri());
                                            Node object = createURI(persons.get(tagEntry));
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else {
                                            System.out.println("This tag is not mapped " + path.get(2));
                                        }
                                    }
                                }
                            }
                        }
//                    }
                }
            } else if(event.isEndElement()){
                path.remove(path.size()-1);
            }
        }
        writer.finish();
    }
}
