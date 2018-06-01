package eu.wdaqua.dblp;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names={"--input", "-i"})
    String input;

    @Parameter(names={"--output", "-o"})
    String output;
}
