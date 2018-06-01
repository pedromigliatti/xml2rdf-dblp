package eu.wdaqua.dblp;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names={"--input", "-i"})
    String input = "/home_expes/dd77474h/datasets/dblp_new/dump/dblp.xml" ;

    @Parameter(names={"--output", "-o"})
    String output = "/home_expes/dd77474h/datasets/dblp_new/dump/dump.nt";
}
