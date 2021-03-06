/**
 * Copyright 2007-2008 Institute for Applied Knowledge Processing, Johannes Kepler University Linz
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.jku.semwiq.mediator.examples;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

import at.jku.semwiq.mediator.Mediator;
import at.jku.semwiq.mediator.MediatorException;
import at.jku.semwiq.mediator.MediatorImpl;
import at.jku.semwiq.mediator.conf.ConfigException;
import at.jku.semwiq.mediator.dataset.SemWIQDataset;
import at.jku.semwiq.mediator.registry.RegistryException;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class SemWIQDatasetExample {

	public static void main(String[] args) throws ConfigException, MediatorException, RegistryException {
		Mediator m = new MediatorImpl();
		SemWIQDataset ds = new SemWIQDataset(m);
		m.getDataSourceRegistry().getManager().register(ExampleConstants.testEndpoint, ExampleConstants.monitoringProfile);
		m.getDataSourceRegistry().getMonitor().waitUntilFinished(); // blocks until monitor has finished updating
		
		QueryExecution qe = QueryExecutionFactory.create("SELECT DISTINCT ?t WHERE { ?s a ?t }", ds);
		ResultSet r = qe.execSelect();
		ResultSetFormatter.out(r);
		
		ds.close();
	}
}
