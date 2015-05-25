package test;

import resource.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 02.04.15.
 */
public class WrongTestResource implements Serializable, Resource {
    private int fail;

    public WrongTestResource() {
    }

    @Override
    public void checkState() {
    }
}
