package com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher;

/**
 * Created by Ragabov on 4/2/2016.
 */
public abstract class Validator <OUTPUT, INPUT> {

    public Validator(String prompt)
    {
        this.prompt = prompt;
    }
    public String prompt;

    public abstract OUTPUT execute (INPUT input);
}
