package test;

import resource.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 02.04.15.
 */
public class TestResource implements Serializable, Resource {
    private int success;

    public TestResource() {
    }

    public int getSuccess() { return success; }

    @Override
    public void checkState() {
    }
}
