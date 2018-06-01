package eu.wdaqua.dblp;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class NotNull implements IParameterValidator {
    public void validate(String name, String value)throws ParameterException {
            if(value.equals("")){
                throw new ParameterException("Parameter "+ name + " not be NULL");
            }
    }
}
