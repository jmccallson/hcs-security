package org.hcs.ws.v1.controllers;

import com.webcohesion.enunciate.metadata.Facet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Annotation interface used in conjunction with enunciate to mark certain endpoints as
 * internal (i.e. not documented).
 * <p/>
 * To use, add the following to your enunciate.xml:
 * <pre>
 * <docs splashPackage="org.familysearch.messaging.cmpusermsg">
 *    <facets>
 *        <exclude name="internal.endpoint"/>
 *    </facets>
 * </docs>
 * </pre>
 */
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Facet("internal.endpoint")
public @interface InternalController {

}
