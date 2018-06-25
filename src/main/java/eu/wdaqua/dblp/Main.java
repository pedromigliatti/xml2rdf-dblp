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

import static eu.wdaqua.dblp.ontology.Utility.createURI;

public class Main {

    public static String vocabularyFile = "vocabulary.nt";
    public static String monthFile = "query.json";

    public static void main(String[] argv) throws IOException, XMLStreamException, IllegalAccessException {

        Args args = new Args();

        JCommander.newBuilder().addObject(args).build().parse(argv);

        String outputFile = args.output;
        String inputFile = args.input;

        System.out.println("Input File: " + inputFile);
        System.out.println("Output File: " + outputFile);

        Map<String, String> months = Utility.readMonthJson(monthFile);

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

        String journal = "";
        String booktitle = "";

        Map<String, String> attributes = new HashMap<String, String>();

        int line = 0;
        while (reader.hasNext()) {
            line++;
            if (line % 1000000 == 0) {
                System.out.println(line);
            }
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                path.add(event.asStartElement().getName().toString());

                Iterator elementAttributes = event.asStartElement().getAttributes();
                while (elementAttributes.hasNext()) {
                    Attribute attribute = (Attribute) elementAttributes.next();
                    attributes.put(attribute.getName().toString(), attribute.getValue());
                }

                switch (path.get(1)){
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
                    System.out.println("Unmapped class tag " + path.get(1));
                }

            } else if (event.isCharacters()) {
                String tagEntry = event.asCharacters().getData();
                if (!tagEntry.equals("\n") && path.size() > 2 && !tagEntry.equals("...")) {
                        for (Mapping mapping : Properties.getMappings()) {
                            if (String.join("/", path).contains(mapping.getTag())) {
                                if (mapping.getType() != Type.CUSTOM) {
                                    Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, mapping);
                                    writer.triple(t);
                                } else {
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
                                            for (Mapping mappingName : Properties.getMapping("/name")) {
                                                String name = tagEntry.replaceAll("[0-9]", "");
                                                name = name.replaceAll("\\s$", "");
                                                Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, name, mappingName);
                                                writer.triple(t);
                                            }
                                    } else if (path.get(2).equals("series")) {
                                        if(!attributes.get("href").isEmpty()) {
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
                                        for(Mapping p : Properties.getMapping(path.get(2))) {
                                            if(p.getPropertyUri().contains("#label")) {
                                                Node predicate = createURI(p.getPropertyUri());
                                                String[] url = attributes.get("key").split("/");
                                                Node object = createURI("db/" + url[0] + url[1]);
                                                Triple t = new Triple(subject, predicate, object);
                                                writer.triple(t);
                                            } else {
                                                Node predicate = createURI(p.getPropertyUri());
                                                String[] url = attributes.get("key").split("/");
                                                Triple t = new Triple(createURI("db/" + url[0] + url[1]), predicate, NodeFactory.createLiteral(tagEntry));
                                                writer.triple(t);
                                            }
                                        }
                                    } else if (path.get(2).equals("note")) {
                                        if (affiliation) {
                                            for (Mapping mappingAff : Properties.getMapping("/affiliation")) {
                                                Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, mappingAff);
                                                writer.triple(t);
                                                affiliation = false;
                                            }
                                        } else {
                                            Node predicate = createURI(mapping.getPropertyUri());
                                            Triple t = eu.wdaqua.dblp.ontology.Utility.map(subject, tagEntry, new Mapping("note", "http://www.w3.org/2004/02/skos/core#note", Type.STRING));
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
