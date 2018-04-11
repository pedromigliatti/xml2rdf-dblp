import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        String filename = "src/main/files/dblp.xml";
        FileInputStream fileXML = new FileInputStream(filename);

        XMLEventReader r = factory.createXMLEventReader
                (filename, fileXML);

        while (r.hasNext()) {
            XMLEvent e = r.nextEvent();

        }
    }
}
