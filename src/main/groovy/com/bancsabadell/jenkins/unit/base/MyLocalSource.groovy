/**
 * My own local source for local libs.
 */
package com.bancsabadell.jenkins.unit.base

import com.lesfurets.jenkins.unit.global.lib.SourceRetriever
import groovy.transform.CompileStatic
import groovy.transform.Immutable

/**
 * @author loga
 *
 */

@Immutable 
@CompileStatic
class MyLocalSource implements SourceRetriever {

    String sourceURL

    @Override
    List<URL> retrieve(String repository, String branch, String targetPath) {
        def sourceDir = new File(sourceURL).toPath().resolve("$repository").toFile()
        if (sourceDir.exists()) {
            return [sourceDir.toURI().toURL()]
        }
        throw new IllegalStateException("Directory $sourceDir.path does not exists")
    }

    static MyLocalSource localSource(String source) {
        new MyLocalSource(source)
    }

    @Override
    String toString() {
        return "MyLocalSource{" +
                        "sourceURL='" + sourceURL + '\'' +
                        '}'
    }
}
