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

import at.jku.semwiq.mediator.registry.model.MonitoringProfile;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class ExampleConstants {
	
	// for endpoints spawned by the semwiq-endpoint project, chose the defaultVoidProfile, otherwise the defaultCentralizedProfile
	public static final String testEndpoint = "http://vancouver:8900/sparql/endpoint8900"; //"http://ramses.faw.uni-linz.ac.at:8900/sparql/endpoint8900";
	public static final String testEndpoint2 = "http://vancouver:8901/sparql/endpoint8901"; //"http://ramses.faw.uni-linz.ac.at:8901/sparql/endpoint8901";
	public static MonitoringProfile monitoringProfile = MonitoringProfile.getDefaultVoidProfile();
	
//	public static final String testEndpoint = "http://data.semanticweb.org/sparql";
//	public static MonitoringProfile monitoringProfile = MonitoringProfile.getDefaultCentralizedProfile();
}
