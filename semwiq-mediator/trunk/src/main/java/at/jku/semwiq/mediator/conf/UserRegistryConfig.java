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
package at.jku.semwiq.mediator.conf;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import at.jku.semwiq.mediator.Constants;
import at.jku.semwiq.mediator.vocabulary.Config;
import at.jku.semwiq.mediator.vocabulary.VocabUtils;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class UserRegistryConfig {
	private static final Logger log = LoggerFactory.getLogger(UserRegistryConfig.class);

	private final String superUserPassword;
	
	/**
	 * @throws ConfigException
	 * 
	 */
	public UserRegistryConfig() throws ConfigException {
		this(null);
	}
	
	/**
	 * @throws ConfigException 
	 * 
	 */
	public UserRegistryConfig(Resource conf) throws ConfigException {
		VocabUtils.unknownPropertyWarnings(conf, Config.MediatorConfig.getModel(), log);
		if (conf != null) {
			
			// super user password
			Statement s = conf.getProperty(Config.superuserPassword);
			if (s != null && s.getObject().isLiteral())
				superUserPassword = s.getString();
			else {
				log.warn("WARNING! Using default super user password: " + Constants.DEFAULT_SUPERUSER_PASSWORD);
				superUserPassword = Constants.DEFAULT_SUPERUSER_PASSWORD;
			}
			
		} else {
			log.warn("WARNING! Using default super user password: " + Constants.DEFAULT_SUPERUSER_PASSWORD);
			superUserPassword = Constants.DEFAULT_SUPERUSER_PASSWORD;			
		}
	}
	
	/**
	 * @return the superUserPassword
	 */
	public String getSuperUserPassword() {
		return superUserPassword;
	}
}
