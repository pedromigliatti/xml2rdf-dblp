package eu.wdaqua.dblp.ontology;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Properties {
    public static final ArrayList<Mapping> mappings = new ArrayList<Mapping>(Arrays.asList(
            new Mapping("/mdate","http://schema.org/dateModified",Type.DATE),
            new Mapping("/name","https://schema.org/name",Type.STRING),
            new Mapping("/name","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/article/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/article/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),

            new Mapping("/www/author","http://dbpedia.org/ontology/author",Type.CUSTOM),
            new Mapping("/author","http://dbpedia.org/ontology/author",Type.STRING),

            new Mapping("/proceedings/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/proceedings/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/inproceedings/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/inproceedings/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/book/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/book/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/phdthesis/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/phdthesis/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/mastersthesis/title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new Mapping("/mastersthesis/title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING), // not title mapping except for www, i.e. persons


            new Mapping("/pages","http://swrc.ontoware.org/ontology#pages",Type.STRING),
            new Mapping("/year","http://purl.org/dc/terms/issued",Type.YEAR),
            new Mapping("/volume","http://swrc.ontoware.org/ontology#volume",Type.INTEGER),
            new Mapping("/journal","http://data.europa.eu/eli/ontology#published_in",Type.CUSTOM),
            new Mapping("/journal","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/number","http://swrc.ontoware.org/ontology#number",Type.INTEGER),
            new Mapping("/url","http://purl.org/dc/elements/1.1/identifier",Type.URI),
            new Mapping("/url_www","http://schema.org/url",Type.URI),
            new Mapping("/ee","http://www.w3.org/2002/07/owl#sameAs",Type.URI),
            new Mapping("/note","http://www.w3.org/2004/02/skos/core#note",Type.CUSTOM),
            new Mapping("/affiliation","http://wdaqua.eu/affiliation",Type.STRING),
            new Mapping("/cite","http://www.w3.org/1999/xhtml/vocab#cite",Type.URI),
            new Mapping("/editor","http://schema.org/editor",Type.STRING),
            new Mapping("/booktitle","http://data.europa.eu/eli/ontology#published_in",Type.CUSTOM),
            new Mapping("/booktitle","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/address","http://schema.org/address",Type.STRING),
            new Mapping("/month","http://purl.org/dc/terms/issued",Type.CUSTOM),
            new Mapping("/cdrom","http://lsdis.cs.uga.edu/projects/semdis/opus#cdrom",Type.STRING),
            new Mapping("/publisher","http://schema.org/publisher",Type.STRING),
            new Mapping("/crossref","http://data.europa.eu/eli/ontology#published_in",Type.URI), //"http://purl.org/net/nknouf/ns/bibtex#hasCrossref";
            new Mapping("/isbn","http://schema.org/isbn",Type.STRING),
            new Mapping("/series","http://lsdis.cs.uga.edu/projects/semdis/opus#in_series",Type.CUSTOM),
            new Mapping("/series","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/school","http://lsdis.cs.uga.edu/projects/semdis/opus#School",Type.STRING),
            new Mapping("/school","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new Mapping("/chapter","http://lsdis.cs.uga.edu/projects/semdis/opus#chapter",Type.INTEGER),
            new Mapping("/orcid","http://wdaqua.eu/P496",Type.URI))); //I could not map

    public static  List<Mapping> getMappings(){
       return mappings;
    }

    public static Map<String,ObjectUtils.Null> getMappedTags(){
        Map<String,ObjectUtils.Null> map = new HashMap<String,ObjectUtils.Null>();
        for (Mapping m : mappings){
            map.put(m.getTag(),null);
        }
        return map;
    }

    public static List<Mapping> getMapping(String tag){
        List<Mapping> properties = new ArrayList<Mapping>();
        for (Mapping m : mappings){
            if (m.getTag().equals(tag)){
                properties.add(m);
            }
        }

        return properties;
    }
}
