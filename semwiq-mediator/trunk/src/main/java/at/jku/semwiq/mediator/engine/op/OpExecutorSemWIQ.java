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
package at.jku.semwiq.mediator.engine.op;

import java.util.List;

import at.jku.semwiq.mediator.engine.iter.QueryIterBlocked;
import at.jku.semwiq.mediator.engine.iter.QueryIterBlockedOptionalIndex;
import at.jku.semwiq.mediator.engine.iter.QueryIterBlockedService;
import at.jku.semwiq.mediator.engine.iter.QueryIterBlockedUnion;

import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.algebra.op.OpConditional;
import com.hp.hpl.jena.sparql.algebra.op.OpExt;
import com.hp.hpl.jena.sparql.algebra.op.OpService;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.binding.BindingRoot;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterRoot;
import com.hp.hpl.jena.sparql.engine.iterator.QueryIterSingleton;
import com.hp.hpl.jena.sparql.engine.main.OpExecutor;
import com.hp.hpl.jena.sparql.engine.main.iterator.QueryIterJoin;
import com.hp.hpl.jena.sparql.engine.main.iterator.QueryIterOptionalIndex;
import com.hp.hpl.jena.sparql.engine.main.iterator.QueryIterService;
import com.hp.hpl.jena.sparql.engine.main.iterator.QueryIterUnion;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class OpExecutorSemWIQ extends OpExecutor {

	public OpExecutorSemWIQ(ExecutionContext cxt) {
		super(cxt);
	}
	
	@Override
	protected QueryIterator execute(OpService opService, QueryIterator input) {
		return new QueryIterBlockedService(new QueryIterBlocked(input, execCxt), opService, execCxt);
	}

	@Override
	protected QueryIterator execute(OpUnion opUnion, QueryIterator input) {
        List<Op> x = flattenUnion(opUnion);
        for (Op op : x) {
        	if (!(op instanceof OpService))
        		return super.execute(opUnion, input);
        }
        
        QueryIterator qIter = new QueryIterBlockedUnion(new QueryIterBlocked(input, execCxt), x, execCxt);		
        return qIter ;
	}
	
	@Override
	protected QueryIterator execute(OpConditional opCondition, QueryIterator input) {
//        QueryIterator left = executeOp(opCondition.getLeft(), input) ;
//        QueryIterator qIter = new QueryIterOptionalIndex(left, opCondition.getRight(), execCxt) ;
//        return qIter ;
		
		QueryIterator left = executeOp(opCondition.getLeft(), input);
		QueryIterator qIter = new QueryIterBlockedOptionalIndex(new QueryIterBlocked(left, execCxt), opCondition.getRight(), execCxt);
		return qIter;
	}
}
