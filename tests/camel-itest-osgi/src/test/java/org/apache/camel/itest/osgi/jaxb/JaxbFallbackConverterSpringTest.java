/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.itest.osgi.jaxb;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.itest.osgi.OSGiIntegrationSpringTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

import static org.ops4j.pax.exam.OptionUtils.combine;

@RunWith(JUnit4TestRunner.class)
public class JaxbFallbackConverterSpringTest extends OSGiIntegrationSpringTestSupport {

    @Override
    protected OsgiBundleXmlApplicationContext createApplicationContext() {
        return new OsgiBundleXmlApplicationContext(new String[]{"org/apache/camel/itest/osgi/jaxb/CamelContext.xml"});
    }
    
    @Test
    public void testSendMessage() throws Exception {
        MockEndpoint mock =  getMandatoryEndpoint("mock:bar", MockEndpoint.class);
        assertNotNull("The mock endpoint should not be null", mock);
        
        PersonType expected = new PersonType();
        expected.setFirstName("FOO");
        expected.setLastName("BAR");
        mock.expectedBodiesReceived(expected);
        mock.expectedHeaderReceived("foo", "bar");
        template.sendBodyAndHeader("direct:start", "<Person><firstName>FOO</firstName><lastName>BAR</lastName></Person>",
                                   "foo", "bar");
        assertMockEndpointsSatisfied();      
    }    
    
    @Configuration
    public static Option[] configure() {
        Option[] options = combine(
            getDefaultCamelKarafOptions(),
            // using the features to install the other camel components             
            loadCamelFeatures("camel-jaxb"));
        
        return options;
    }

}
