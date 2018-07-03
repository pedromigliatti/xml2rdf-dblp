package eu.wdaqua.dblp;

import com.beust.jcommander.JCommander;
import com.ctc.wstx.api.WstxInputProperties;
import eu.wdaqua.dblp.ontology.*;
import eu.wdaqua.dblp.ontology.Properties;
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
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static eu.wdaqua.dblp.ontology.Utility.createURI;
import static eu.wdaqua.dblp.ontology.Utility.map;
import static eu.wdaqua.dblp.ontology.Utility.printLogger;

public class Main {

    public static String vocabularyFile = "vocabulary.nt";
    public static String monthFile = "query.json";

    public static Logger logger = Logger.getLogger("log");
    private static FileHandler fh;
    static {
        try {
            fh = new FileHandler("status.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) throws IOException, XMLStreamException, IllegalAccessException {

        Args args = new Args();

        JCommander.newBuilder().addObject(args).build().parse(argv);

        String outputFile = args.output;
        String inputFile = args.input;

        printLogger(Level.INFO, "Input File: " + inputFile);
        printLogger(Level.INFO, "Output File: " + outputFile);

        Map<String,List<String>> entry = new HashMap<String, List<String>>();
        Map<String, String> attributes = new HashMap<String, String>();
        logger.info("Extracting persons records");
        Map<String, String> persons = Persons.extractPersonRecords(inputFile);
        logger.info("Reading months file");
        Map<String, String> months = Utility.readMonthJson(monthFile);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_VALIDATING, true);
        factory.setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, 999999999);

        FileInputStream fileXML = new FileInputStream(inputFile);
        XMLEventReader reader = factory.createXMLEventReader(inputFile, fileXML);

        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(outputFile), RDFFormat.NTRIPLES);

//        Utility.writeVocabulary(writer);

        List<String> path = new ArrayList<>();
        Node subject = null;

        logger.info("Starting convertion");
        int line = 0;
        while (reader.hasNext()) {
            line++;
            if (line % 1000000 == 0) {
                printLogger(Level.INFO,"Processed lines: " + line);
            }
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                path.add(event.asStartElement().getName().toString());
                if(path.size()>1) {
                    Iterator elementAttributes = event.asStartElement().getAttributes();
                    while (elementAttributes.hasNext()) {
                        Attribute attribute = (Attribute) elementAttributes.next();
                        attributes.put(attribute.getName().toString(), attribute.getValue());
                    }
                    if(path.size()==2) {
                        switch (path.get(1)) {
                            case "www":
                                subject = createURI("https://dblp.org/pid/" + attributes.get("key").replace("homepages/", ""));
                                break;
                            case "book":
                                subject = createURI("https://dblp.org/db/" + attributes.get("key"));
                                break;
                            default:
                                subject = createURI("https://dblp.org/rec/html/" + attributes.get("key"));

                        }
                        Map<String, ObjectUtils.Null> classes = Classes.getMappedTags();
                        if (classes.containsKey(path.get(1))) {
                            Node predicate = createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                            for (Mapping c : Classes.getMapping(path.get(1))) {
                                Node object = NodeFactory.createURI(c.getPropertyUri());
                                Triple t = new Triple(subject, predicate, object);
                                writer.triple(t);
                            }
                        } else {
                            printLogger(Level.WARNING, "Unmapped class tag: " + path.get(1));
                        }
                    }
                }

            } else if (event.isCharacters()) {
                String tagEntry = event.asCharacters().getData();
                if(path.size() > 2 ) {
                    List<String> listTagEntry = new ArrayList<>();
                    if(entry.containsKey(String.join("/", path))){
                        listTagEntry = entry.get(String.join("/", path));
                        listTagEntry.add(tagEntry);
                        entry.put(String.join("/", path), listTagEntry);
                    }
                    listTagEntry.add(tagEntry);
                    entry.put(String.join("/", path), listTagEntry);
                }
            } else if (event.isEndElement()) {
                path.remove(path.size() - 1);
                if(path.size()==1){
                    processEntry(entry, attributes, subject, writer, months, persons);
                    entry.clear();
                }
            }
        }
        writer.finish();
        logger.info("Finishing convertion");

        logger.info("Writing vocabulary");
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(vocabularyFile));

        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            sb.append(sCurrentLine).append("\n");
        }

        Utility.writeStringBuffer(sb, outputFile, true);
    }

    public static void processEntry(Map<String, List<String>> selects, Map<String, String> attributes, Node subject, StreamRDF writer, Map<String, String> months, Map<String, String> persons) throws IOException {
        for(Map.Entry<String, List<String>> entry : selects.entrySet()) {
            List<String> listTagEntry = entry.getValue();
            List<String> path = Arrays.asList(entry.getKey().split("/"));
            for(String tagEntry : listTagEntry) {
                if (!tagEntry.equals("\n") && !tagEntry.equals("...")) {
                    for (Mapping mapping : Properties.getMappings()) {
                        if (entry.getKey().contains(mapping.getTag())) {
                            if (mapping.getType() != Type.CUSTOM && mapping.getType() != Type.LABEL) {
                                Triple t = map(subject, tagEntry, mapping);
                                writer.triple(t);
                            } else if (mapping.getType() != Type.LABEL) {
                                if (path.get(2).equals("crossref")) {
                                    String[] crossref = tagEntry.split("/");
                                    Node predicate = createURI(mapping.getPropertyUri());
                                    Node object = createURI(crossref[0] + "/" + crossref[1]);
                                    Triple t = new Triple(subject, predicate, object);
                                    writer.triple(t);
                                } else if (path.get(2).equals("month")) {
                                    Node predicate = createURI(mapping.getPropertyUri());
                                    String month = months.get(tagEntry);
                                    if (month != null) {
                                        Node object = createURI(month);
                                        Triple t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    }
                                } else if (path.get(2).equals("author")) {
                                    Node predicate = createURI(mapping.getPropertyUri());
                                    Node object = createURI(persons.get(tagEntry));
                                    Triple t = new Triple(subject, predicate, object);
                                    writer.triple(t);
                                } else if (path.get(2).equals("series")) {
                                    if (attributes.containsKey("href")) {
                                        for (Mapping p : Properties.getMapping("/series")) {
                                            Node predicate = createURI(p.getPropertyUri());
                                            Triple t;
                                            if (!p.getPropertyUri().contains("#label")) {
                                                Node object = createURI(attributes.get("href"));
                                                t = new Triple(subject, predicate, object);
                                            } else {
                                                Node object = NodeFactory.createLiteral(tagEntry);
                                                Node newSub = createURI(attributes.get("href"));
                                                t = new Triple(newSub, predicate, object);
                                            }
                                            writer.triple(t);
                                        }
                                    }
                                } else if (path.get(2).equals("booktitle") || path.get(2).equals("journal")) {
                                    for (Mapping p : Properties.getMapping("/" + path.get(2))) {
                                        Node predicate = createURI(p.getPropertyUri());
                                        if (selects.containsKey(path.get(0) + "/" + path.get(1) + "/" + "url")) {
                                            List<String> listUrl = selects.get(path.get(0) + "/" + path.get(1) + "/" + "url");
                                            String url = "";
                                            for(String u : listUrl){
                                                url = u.split("#")[0];
                                            }
                                            Triple t;
                                            if (p.getPropertyUri().contains("#label")) {
                                                t = new Triple(createURI(url), predicate, NodeFactory.createLiteral(tagEntry));
                                            } else {
                                                Node object = createURI(url);
                                                t = new Triple(subject, predicate, object);
                                            }
                                            writer.triple(t);
                                        } else if (p.getPropertyUri().contains("#label")) {
                                            Triple t = new Triple(subject, predicate, NodeFactory.createLiteral(tagEntry));
                                            writer.triple(t);
                                        }
                                    }
                                } else if (path.get(2).equals("note")) {
                                    Node object = NodeFactory.createLiteral(tagEntry);
                                    if (attributes.containsKey("type")) {
                                        if (attributes.get("type").equals("affiliation")) {
                                            for (Mapping p : Properties.getMapping("/affiliation")) {
                                                Triple t = map(subject, tagEntry, p);
                                                writer.triple(t);
                                            }
                                            attributes.remove("type");
                                        }
                                    } else {
                                        for (Mapping p : Properties.getMapping("/note")) {
                                            Node predicate = createURI(p.getPropertyUri());
                                            Triple t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }
                                } else {

                                    printLogger(Level.WARNING, "Unmapped property tag: " + path.get(2));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
