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
package at.jku.semwiq.mediator.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.helpers.CyclicBuffer;

/**
 * @author dorgon
 *
 * caches the last n lines and forwards messages to other buffers
 */
public class LogBufferDispatcher {
	private static final Logger log = LoggerFactory.getLogger(LogBufferDispatcher.class);
	
	public static final int DEFAULT_CACHED_EVENTS = 1000;
	protected final List<LogBuffer> registeredBuffers;
	
	private static LogBufferDispatcher instance;
	
	/** cache last lines */
	private final CyclicBufferAppender<LoggingEvent> cache;
	
	public static void init(int cachedLines) {
		if (instance != null) {
			log.warn("Re-initializing log buffer dispatcher with " + cachedLines + " cached log evenets. Active log buffers will be interrupted!");
			instance.stop(); // and reinit (will inform and stop all active LogBuffers)
		} else
			log.info("Initializing log buffer dispatcher with " + cachedLines + " cached log events.");
			instance = new LogBufferDispatcher(cachedLines);
	}

	/** init the dispatcher (will always cache last lines)
	 * 
	 * @param cachedLines
	 */
	private LogBufferDispatcher(int cachedLines) {
		if (cachedLines < 0)
			cachedLines = DEFAULT_CACHED_EVENTS;
		cache = new CyclicBufferAppender<LoggingEvent>();
		cache.setMaxSize(cachedLines);
		cache.start();
		
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        WriterAppender<LoggingEvent> writerAppender = new WriterAppender<LoggingEvent>(); 
//        writerAppender.setContext(lc);        
//        writerAppender.setLayout(new EchoLayout<LoggingEvent>()); 
//        logStream = new ByteOutputStream();
//        writerAppender.setWriter(new OutputStreamWriter(logStream)); 
//        writerAppender.setImmediateFlush(false); 
//        writerAppender.start();
		
		ch.qos.logback.classic.Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
		root.addAppender(this.cache);

		registeredBuffers = new ArrayList<LogBuffer>();
	}

	/** create a new LogBuffer (will register itself at the dispatcher in order
	 * to receive future events)
	 * 
	 * @return
	 */
	public static LogBuffer createLogBuffer() {
		LogBuffer buf = new LogBuffer(instance);

		// send cached events
		for (int i = 0; i < instance.cache.getLength(); i++)
			buf.receiveEvent((LoggingEvent) instance.cache.get(i));
		
		return buf;
	}

	/**
	 * @param logBuffer
	 */
	protected void register(LogBuffer logBuffer) {
		registeredBuffers.add(logBuffer);
	}

	/**
	 * LogBuffers unregister themselves calling this method
	 * @param logBuffer
	 */
	protected void unregister(LogBuffer logBuffer) {
		registeredBuffers.remove(logBuffer);
	}

	protected void stop() {
		for (LogBuffer buf : registeredBuffers)
			buf.close();
		cache.stop();
	}
	
	/** modified version of ch.qos.logback.core.read.CyclicBufferAppender<E>
	 * 
	 * @author dorgon
	 *
	 * @param <E>
	 */
	public class CyclicBufferAppender<E> extends AppenderBase<E> {

		CyclicBuffer<E> cb;
		int maxSize = 512;

		public void start() {
			cb = new CyclicBuffer<E>(maxSize);
			super.start();
		}

		public void stop() {
			cb = null;
			super.stop();
		}

		@Override
		protected void append(E eventObject) {
			if (!isStarted())
				return;
			cb.add(eventObject);
			for (LogBuffer buf : registeredBuffers)
				buf.receiveEvent((LoggingEvent) eventObject);
		}

		public int getLength() {
			if (isStarted()) {
				return cb.length();
			} else {
				return 0;
			}
		}

		public Object get(int i) {
			if (isStarted()) {
				return cb.get(i);
			} else {
				return null;
			}
		}

		/**
		 * Set the size of the cyclic buffer.
		 */
		public int getMaxSize() {
			return maxSize;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize = maxSize;
		}

	}
}
