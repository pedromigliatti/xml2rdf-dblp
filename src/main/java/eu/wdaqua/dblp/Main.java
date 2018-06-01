package eu.wdaqua.dblp;

import com.beust.jcommander.JCommander;
import com.ctc.wstx.api.WstxInputProperties;
import eu.wdaqua.dblp.ontology.*;
import org.apache.commons.lang3.ObjectUtils;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static eu.wdaqua.dblp.ontology.Utility.createURI;

public class Main {
//    public static String outputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dump.nt";
//    public static String inputFile = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.xml";

    public static String vocabularyFile = "vocabulary.nt";
    public static String monthFile = "query.json";

//    Directory of tests on Pedro's computer
//    public static String outputFile = "/home/pedro/Documentos/WDAqua/dblpDocuments/dblp2.nt";
//    public static String inputFile = "/home/pedro/Documentos/WDAqua/dblpDocuments/dblp.xml.temp";

    public static void main(String[] argv) throws IOException, XMLStreamException, IllegalAccessException {

        Args args = new Args();

        JCommander.newBuilder().addObject(args).build().parse(argv);

        String outputFile = args.output;
        String inputFile = args.input;

        System.out.println("Input File: " + inputFile);
        System.out.println("Output File: " + outputFile);

        Map<String, String> month = Utility.readMonthJson(monthFile);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
        factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, Integer.valueOf(999999999));
        FileInputStream fileXML = new FileInputStream(inputFile);
        XMLEventReader reader = factory.createXMLEventReader(inputFile, fileXML);

        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(outputFile), RDFFormat.NTRIPLES);

        Map<String, String> persons = Persons.extractPersonRecords(inputFile);
        Utility.writeVocabulary(writer);

        boolean affiliation = false;

        List<String> path = new ArrayList<>();
        List<Node> published_in = new ArrayList<>();
        Node subject = null;
        String key = "";
        String journal = "";
        String booktitle = "";
        String href = "";
        int line = 0;
        while (reader.hasNext()) {
            line++;
            if (line % 100000 == 0) {
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
                            subject = createURI("https://dblp.org/pid/" + attribute.getValue().replace("homepages/", ""));
                        } else if (path.get(1).equals("book")) {
                            subject = createURI("https://dblp.org/db/" + attribute.getValue());
                        } else {
                            subject = createURI("https://dblp.org/rec/html/" + attribute.getValue());
                        }
                        //Map the classes
                        Map<String, ObjectUtils.Null> classes = Classes.getMappedTags();
                        if (classes.containsKey(path.get(1))) {
                            Node predicate = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                            for (Mapping c : Classes.getMapping(path.get(1))) {
                                Node object = NodeFactory.createURI(c.getPropertyUri());
                                Triple t = new Triple(subject, predicate, object);
                                writer.triple(t);
                            }
                        } else {
                            System.out.println("Unmapped class tag " + path.get(1));
                        }
                    } else if (attribute.getName().toString().equals("type")) {
                        if (attribute.getValue().equals("affiliation"))
                            affiliation = true;
                    } else if(attribute.getName().toString().equals("href")) {
                        if(event.asStartElement().getName().toString().equals("series")){
                            href = "http://dblp.uni-trier.de/" + attribute.getValue();
                        }
                    }
                }
            } else if (event.isCharacters()) {
                String tagEntry = event.asCharacters().getData();
                if (!tagEntry.equals("\n")) {
                    //Map the properties
                        if (path.size()>2) { // there are some bugs in the dump
//                            System.out.println(Properties.getMappedTags().toString());
                            for (Mapping mapping: Properties.getMappings()){
                                if (String.join("/", path).contains(mapping.getTag())){
                                    if (mapping.getType() != Type.CUSTOM) {
                                        Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, mapping);
                                        writer.triple(t);
                                    } else {
                                        if (path.get(2).equals("crossref")) {
                                            String[] crossref = tagEntry.split("/");
                                            Node predicate = createURI(mapping.getPropertyUri());
                                            Node object = createURI("http://dblp.uni-trier.de/db/" + crossref[0] + "/" + crossref[1]);
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (path.get(2).equals("series")) {
                                            for (Mapping p : Properties.getMapping("series")) {
                                                Node predicate = createURI(p.getPropertyUri());
                                                Triple t;
                                                if (!p.getPropertyUri().contains("#label")) {
                                                    Node object = createURI(href);
                                                    t = new Triple(subject, predicate, object);
                                                } else {
                                                    Node object = NodeFactory.createLiteral(tagEntry);
                                                    Node newSub = createURI(href);
                                                    t = new Triple(newSub, predicate, object);
                                                }
                                                writer.triple(t);
                                            }
                                        } else if (path.get(2).equals("month")) {
                                            Node predicate = createURI(mapping.getPropertyUri());
                                            Node object = createURI(month.get(tagEntry));
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (path.get(2).equals("booktitle")) {
                                            booktitle = tagEntry;
                                        } else if (path.get(2).equals("url")) {
                                            Node predicate = createURI(mapping.getPropertyUri());
                                            Node object;
                                            if (!tagEntry.contains("http"))
                                                object = createURI("http://dblp.uni-trier.de/" + tagEntry);
                                            else
                                                object = createURI(tagEntry);
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            if (!path.get(1).equals("www")) {
                                                for (Mapping p : Properties.getMapping("booktitle")) {
                                                    if (!p.getPropertyUri().contains("#label")) {
                                                        String newTagEntry = "";
                                                        predicate = createURI(p.getPropertyUri());
                                                        tagEntry = tagEntry.split("#")[0];
                                                        if(!journal.equals("")) {
                                                            String[] tagEntrySplited = tagEntry.split("/");
                                                            for (int i = 0; i < tagEntrySplited.length - 1; i++) {
                                                                if (!tagEntrySplited[i].equals(""))
                                                                    if (!newTagEntry.equals(""))
                                                                        newTagEntry = newTagEntry + "/" + tagEntrySplited[i];
                                                                    else
                                                                        newTagEntry = tagEntrySplited[i];
                                                            }
                                                        } else if(!booktitle.equals("")){
                                                            newTagEntry = tagEntry;
                                                        }
                                                        if (!tagEntry.contains("http"))
                                                            object = createURI("http://dblp.uni-trier.de/" + newTagEntry);
                                                        else
                                                            object = createURI(newTagEntry);
                                                        published_in.add(object);
                                                        t = new Triple(subject, predicate, object);
                                                        writer.triple(t);
                                                    }
                                                }
                                                for (Node p : published_in) {
                                                    predicate = createURI("http://www.w3.org/2000/01/rdf-schema#label");
                                                    if(!booktitle.equals(""))
                                                        object = NodeFactory.createLiteral(booktitle);
                                                    else if(!journal.equals(""))
                                                        object = NodeFactory.createLiteral(journal);
                                                    t = new Triple(p, predicate, object);
                                                    writer.triple(t);
                                                }
                                                published_in.clear();
                                                journal = "";
                                                booktitle = "";
                                            }
                                        } else if (path.get(2).equals("journal")) {
                                            journal = tagEntry;
                                        } else if (path.get(2).equals("author")) {
                                            Node predicate = createURI(mapping.getPropertyUri());
                                            Node object = createURI(persons.get(tagEntry));
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            if (path.get(1).equals("www")) {
                                                for (Mapping mappingName : Properties.getMapping("name")) {
                                                    String name = tagEntry.replaceAll("[0-9]", "");
                                                    name = name.replaceAll("\\s$", "");
                                                    t = eu.wdaqua.dblp.ontology.Utility.map(subject, name, mappingName);
                                                    writer.triple(t);
                                                }
                                            }
                                        } else if(path.get(2).equals("cite")){
                                            if(!tagEntry.contains("...")){
                                                Node predicate = createURI(mapping.getPropertyUri());
                                                Node object = createURI(persons.get(tagEntry));
                                                Triple t = new Triple(subject, predicate, object);
                                                writer.triple(t);
                                            }
                                        } else if(path.get(2).equals("note")) {
                                            if(affiliation){
                                                for(Mapping mappingAff : Properties.getMapping("affiliation")) {
                                                    Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, mappingAff);
                                                    writer.triple(t);
                                                    affiliation = false;
                                                }
                                            } else {
                                                Node predicate = createURI(mapping.getPropertyUri());
                                                Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, new Mapping("note","http://www.w3.org/2004/02/skos/core#note",Type.STRING));
                                                writer.triple(t);
                                            }
                                        } else {
                                            System.out.println("This tag is not mapped " + path.get(2));
                                        }
                                    }
                                }
                            }
                        }
                }
            } else if (event.isEndElement()) {
                path.remove(path.size() - 1);
            }
        }
        writer.finish();

        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(vocabularyFile));

        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            sb.append(sCurrentLine).append("\n");
        }

        Utility.writeStringBuffer(sb, outputFile, true);
    }
}
