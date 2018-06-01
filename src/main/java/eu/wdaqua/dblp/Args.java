package eu.wdaqua.dblp;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class Args {
    @Parameter(names = {"--input", "-i"}, validateWith = NotNull.class, required = true)
    public String input;

    @Parameter(names = {"--output", "-o"}, validateWith = NotNull.class, required = true)
    public String output;
}
