package com.bancsabadell.pipeline;

import org.junit.Before;
import org.junit.Test;

import com.bancsabadell.jenkins.unit.PipelineTest;

/**
 * cycling-stats.pipeline test suite.
 */

/**
 * @author loga
 *
 */
public class CyclingStatsPipelineTest extends PipelineTest {

    /**
     * Set up test.
     */
    @Override
    @Before
    public void setUp() {
        /* Set up test */
        super.setUp();

        /* Set default pom */
        poms.put("pom.xml","pom.xml");
        
        /* Define variables */
        getBinding().setVariable("scm", "scm");
    }

    /**
     * Test execution.
     * @throws Exception
     */
    @Test
    public void testExecution() throws Exception {
        /* Run the script */
        runScript("cycling-stats.pipeline");

        /* Print call stack */
        printCallStack();

        /* Assert job status */
        assertJobStatusSuccess();
    }
}
