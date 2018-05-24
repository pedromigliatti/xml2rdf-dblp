package eu.wdaqua.dblp.ontology;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Properties {
    public static final ArrayList<PropertyMapping> mappings = new ArrayList<PropertyMapping>(Arrays.asList(
            new PropertyMapping("mdate","http://schema.org/dateModified",Type.DATE),
            new PropertyMapping("author","http://dbpedia.org/ontology/author",Type.CUSTOM),
            new PropertyMapping("name","https://schema.org/name",Type.STRING),
            new PropertyMapping("name","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new PropertyMapping("title","http://purl.org/dc/elements/1.1/title",Type.STRING),
            new PropertyMapping("title","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new PropertyMapping("pages","http://swrc.ontoware.org/ontology#pages",Type.STRING),
            new PropertyMapping("year","http://purl.org/dc/terms/issued",Type.YEAR),
            new PropertyMapping("volume","http://swrc.ontoware.org/ontology#volume",Type.INTEGER),
            new PropertyMapping("journal","http://swrc.ontoware.org/ontology#journal",Type.CUSTOM),
            new PropertyMapping("number","http://swrc.ontoware.org/ontology#number",Type.INTEGER),
            new PropertyMapping("url","http://purl.org/dc/elements/1.1/identifier",Type.CUSTOM),
            new PropertyMapping("url_www","http://schema.org/url",Type.URI),
            new PropertyMapping("ee","http://www.w3.org/2002/07/owl#sameAs",Type.URI),
            new PropertyMapping("note","http://www.w3.org/2004/02/skos/core#note",Type.CUSTOM),
            new PropertyMapping("affiliation","http://wdaqua.eu/affiliation",Type.STRING),
            new PropertyMapping("cite","http://www.w3.org/1999/xhtml/vocab#cite",Type.STRING),
            new PropertyMapping("editor","http://schema.org/editor",Type.STRING),
            new PropertyMapping("booktitle","http://lsdis.cs.uga.edu/projects/semdis/opus#book_title",Type.STRING), //"booktitle"; //I could not map
            new PropertyMapping("booktitle","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new PropertyMapping("address","http://schema.org/address",Type.STRING),
            new PropertyMapping("month","http://purl.org/dc/terms/issued",Type.DATE),
            new PropertyMapping("cdrom","http://lsdis.cs.uga.edu/projects/semdis/opus#cdrom",Type.STRING),
            new PropertyMapping("publisher","http://schema.org/publisher",Type.STRING),
            new PropertyMapping("crossref","http://data.europa.eu/eli/ontology#published_in",Type.URI), //"http://purl.org/net/nknouf/ns/bibtex#hasCrossref";
            new PropertyMapping("isbn","http://schema.org/isbn",Type.STRING),
            new PropertyMapping("series","http://lsdis.cs.uga.edu/projects/semdis/opus#in_series",Type.STRING),
            new PropertyMapping("series","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new PropertyMapping("school","http://lsdis.cs.uga.edu/projects/semdis/opus#School",Type.STRING),
            new PropertyMapping("school","http://www.w3.org/2000/01/rdf-schema#label",Type.STRING),
            new PropertyMapping("chapter","http://lsdis.cs.uga.edu/projects/semdis/opus#chapter",Type.INTEGER),
            new PropertyMapping("orcid","http://wdaqua.eu/P496",Type.URI))); //I could not map

    public static  List<PropertyMapping> getMappings(){
       return mappings;
    }

    public static Map<String,ObjectUtils.Null> getMappedTags(){
        Map<String,ObjectUtils.Null> map = new HashMap<String,ObjectUtils.Null>();
        for (PropertyMapping m : mappings){
            map.put(m.getTag(),null);
        }
        return map;
    }

    public static List<PropertyMapping> getMapping(String tag){
        List<PropertyMapping> properties = new ArrayList<PropertyMapping>();
        for (PropertyMapping m : mappings){
            if (m.getTag().equals(tag)){
                properties.add(m);
            }
        }

        return properties;
    }
}
