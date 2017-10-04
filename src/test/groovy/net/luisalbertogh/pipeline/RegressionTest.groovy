/**
 * Test pipeline in Groovy.
 * 
 * Use -Dpipeline.stack.write=true to update the the previous callstack trace.
 */
package net.luisalbertogh.pipeline

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.lesfurets.jenkins.unit.*
import com.lesfurets.jenkins.unit.global.lib.*

import net.luisalbertogh.jenkins.unit.PipelineTest
import net.luisalbertogh.jenkins.unit.domain.Library

/**
 * @author loga
 *
 */
class RegressionTest extends PipelineTest {
    /**
     * Set up test.
     */
    @Override
    @Before
    public void setUp() {
        /* Register library */
        libs.add(new Library(gitUrl:'https://github.com/luisalbertogh/my-jenkins-lib', libName:'simple-lib', destination:'my-jenkins-lib', branch:'master'))

        /* Set up test */
        super.setUp()
    }

    /**
     * Test regression.
     * @throws Exception
     */
    @Test
    public void testRegression() throws Exception {
        /* Run the script */
        runScript('sh-libraries.pipeline')

        /* Print call stack */
        printCallStack()

        /* Assert step */
        assertTrue(helper.callStack.findAll({call -> call.methodName == "echo"}).any({call -> MethodCall.callArgsToString(call).contains("Hello Luis from simple lib")}))

        /* Test regression */
        super.testNonRegression('prev-output')

        /* Assert job status */
        assertJobStatusSuccess()
    }
}
