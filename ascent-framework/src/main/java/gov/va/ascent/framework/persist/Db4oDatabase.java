package gov.va.ascent.framework.persist;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.Db4oIOException;

/**
 * This class is a simple database client and possibly server for db4o.
 * 
 * All database startup/shutdown processing is here as well as the handle to the db client that local
 * services/simulators can use for persistence.
 * 
 * Numerous aspects of the database are configurable via the setters. The assumed usage pattern for this class is that
 * this will be a Spring managed bean with setters invoked via configurable properties where applicable and then Spring
 * will manage @PostConstruct and @PreDestory to startup and shutdown the database server and/or client appropriately.
 * 
 * This client/server database was developed with the intent to be used for local development to allow easy creation of
 * persistent simulations of remote services. This database is not designed/tested/intended to be used for other
 * purposes such as real persistence of real data.
 * 
 * @author jshrader
 */
//jshrader - multiple methods are synchronized at the method level to ensure we only initialize this database 1 time 
//	and don't have multiple instances getting instantiated and triggering initialization simultaneously.
@SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
public class Db4oDatabase {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(Db4oDatabase.class);

	/** The Constant LOG_DATABASE_CLIENT_INSTANTIATED. */
	private static final String LOG_DATABASE_CLIENT_INSTANTIATED = "database client instantiated.";

	/** The Constant ERROR_DELETING_OLD_DATABASE. */
	private static final String ERROR_DELETING_OLD_DATABASE = "ERROR deleting old database!";

	/**
	 * true/false to enable/disable this database in the event one desires to manage this via a property file instead of
	 * a Spring profile.
	 */
	private boolean enabled = true;

	/** true/false to clean/leave data from past runs of the database. */
	private boolean cleanData = true;

	/** The Database file name. */
	private String db4oFile = "datafile.db4o";
	
	/**
	 * configurable injected true/false to run in clientServerMode (necessary in a cluster, works fine either way in a
	 * single-server mode).
	 */
	private boolean clientServerMode = true;

	/** The database host, only relevant in client/server mode. */
	private String host = "localhost";

	/** The database port, only relevant in client/server mode. */
	//jshrader - this magic number is on purpose, this field is a default but easily overridden.
	//CHECKSTYLE:OFF
	private int port = 9999;
	//CHECKSTYLE:ON

	/** The database user, only relevant in client/server mode. */
	private String user = "db4o";

	/** The database password, only relevant in client/server mode. */
	private String pw = "password";

	/**
	 * The client/server update depth, easily overridden via the setter. 100 would provide terrible performance in a
	 * real app with a complex object graph. However in small local databases, such as for simulators, this will perform
	 * adequately and not encounter object depth issues. This property can be overridden.
	 */
	//jshrader - this magic number is on purpose, this field is a default but easily overridden.
	//CHECKSTYLE:OFF
	private Integer updateDepth = 100;
	//CHECKSTYLE:ON
	
	/**
	 * The client/server activation depth, easily overridden via the setter. 100 would provide terrible performance in a
	 * real app with a complex object graph. However in small local databases, such as for simulators, this will perform
	 * adequately and not encounter object depth issues. This property can be overridden.
	 */
	//jshrader - this magic number is on purpose, this field is a default but easily overridden.
	//CHECKSTYLE:OFF
	private Integer activationDepth = 100;
	//CHECKSTYLE:ON
	
	/** The db client and server components. */
	private ObjectServer server;

	/** The client. */
	private ObjectContainer client;
	
	/**
	 * Checks if is data init permitted for this instance.
	 * 
	 * If this is client/server mode and this is the client end, then
	 * don't re-init because the server side will take care of that.
	 *
	 * @return true, if is data init permitted for this instance
	 */
	public final boolean isDataInitPermittedForThisInstance() {
		return !clientServerMode || clientServerMode && server != null;
	}
	
	/**
	 * Save object.
	 *
	 * @param object the object
	 */
	public final void save(final Object object){
		client.store(object);
		client.commit();
	}
	
	/**
	 * Delete.
	 *
	 * @param object the object
	 */
	public final void delete(final Object object){
		client.delete(object);
		client.commit();
	}
	
	/**
	 * Delete all data in the database
	 */
	public final void deleteAll(){
		final ObjectSet objects = client.queryByExample(null);
	    for (Object object : objects) {
	    	client.delete(object);
	    }
	}
	
	/**
	 * Query by example.
	 *
	 * @param objectExample the object example
	 * @return the object
	 */
	public final List<Object> queryByExample(final Object objectExample){
		final List<Object> foundEm = client.queryByExample(objectExample);

		for (Object entity : foundEm){
			//refresh our copy
			client.ext().refresh(entity, updateDepth);
		}
		return foundEm;
	}
	
	/**
	 * Query for unique.
	 *
	 * @param objectExample the object example
	 * @return the object
	 */
	public final Object queryForUnique(final Object objectExample) {
		final List<Object> foundEm = client.queryByExample(objectExample);

		if(foundEm.size() > 0){
			//refresh our copy
			client.ext().refresh(foundEm.get(0), updateDepth);
			return foundEm.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the objects of type.
	 *
	 * @param clazz the clazz
	 * @return the objects of type
	 */
	public final Object[] getObjectsOfType(final Class clazz){
		final Object[] foundEm = client.query(clazz).toArray();
		for(Object foundIt: foundEm){
			client.ext().refresh(foundIt, updateDepth);
		}
		return foundEm;
	}

	/**
	 * Post construction/setter injects initialization of the database server and/or client.
	 * 
	 * This method is synchronized to ensure we only initialize this data 1 time and don't have multiple instances
	 * getting instantiated and triggering initialization simultaneously.
	 */
	@PostConstruct
	public final synchronized void postConstruct() {
		LOGGER.debug("Db4oDatabase postConstruct");

		if (!enabled) {
			LOGGER.info("Db4oDatabase not enabled, not going to start the database");
			return;
		} else {
			LOGGER.warn("!!!Db4oDatabase enabled & initializing, ensure this is the intent! " + this);
		}

		if (client != null) {
			LOGGER.debug("db4o client already open and setup.");
			return;
		}

		if (clientServerMode) {
			LOGGER.debug("db4o configured to run in client/server mode.");
			startInClientServerMode();
		} else {
			LOGGER.debug("db4o configured to run in single client mode.");
			startInSingleClientMode();
		}
	}

	/**
	 * Predestroy closing down of the database server and/or client.
	 */
	@PreDestroy
	public final synchronized void preDestroy() {
		if (server != null) {
			try {
				LOGGER.info("closing db4o server.");
				server.close();
				server = null;
			} catch (final Db4oIOException e) {
				LOGGER.error("Exception occurred trying to close the db4o server, this might break future deployments"
						+ "...recommend restarting server and manuall deleting the db4o database file.", e);
			}
		}
		if (client != null) {
			try {
				LOGGER.info("closing db4o client.");
				client.close();
				client = null;
			} catch (final Db4oIOException e) {
				LOGGER.error("Exception occurred trying to close the db4o client, this might break future deployments"
						+ "...recommend restarting server.", e);
			}
		} else {
			LOGGER.debug("db4o client null or already closed.");
		}
	}

	/**
	 * Start in single client mode.
	 */
	private synchronized void startInSingleClientMode() {
		if (!enabled || client != null) {
			return;
		}

		if (cleanData) {
			LOGGER.info("cleanData==true and db isn't started, deleting old database prior to starting.");
			final boolean success = new File(db4oFile).delete();
			if (!success) {
				LOGGER.error(ERROR_DELETING_OLD_DATABASE);
			}
		}

		LOGGER.debug("trying to open db4o client in  embedded client mode.");
		final EmbeddedConfiguration clientConfig = Db4oEmbedded.newConfiguration();
		clientConfig.common().updateDepth(updateDepth);
		clientConfig.common().activationDepth(activationDepth);
		client = Db4oEmbedded.openFile(clientConfig, db4oFile);
		LOGGER.info(LOG_DATABASE_CLIENT_INSTANTIATED);
	}

	/**
	 * Start in client server mode, required if we want to run this in a cluster.
	 */
	// jshrader - this is a "lazy initializing" internal object database intended to be used in simulators, its not
	// going to be perfect elegant design. this method is synchronized, locks, catchs exceptions, etc. on puprose
	// to try our hardest to lazily open the client/server.  this isn't intended to be used at runtime in prod.
	//CHECKSTYLE:OFF
	@edu.umd.cs.findbugs.annotations.SuppressWarnings
	@SuppressWarnings("PMD.AvoidCatchingGenericException")
	private synchronized void startInClientServerMode() {
		if (!enabled || client != null) {
			return;
		}

		try {
			openClientInClientServerMode();
		} catch (final com.db4o.ext.Db4oIOException ioe) {
			LOGGER.warn("client failed to open, trying to start server.");
			try {
				openServerForClientServerMode();
			} catch (final Exception e) {
				LOGGER.warn("db4o server startup failed, assuming server started/cleaned on other server.");
				try {
					// pause, wait for server to start if its starting on another node in the cluster.					
					Thread.sleep(5000);
				} catch (final InterruptedException ie) {
					LOGGER.error("error sleeping trying to wait for db4o server to startup.", ie);
				}
			}
			openClientInClientServerMode(); // if an error occurs, raise it as, we are dead in the water at this point
		}
	}
	//CHECKSTYLE:ON

	/**
	 * Open server for client server mode.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void openServerForClientServerMode() throws IOException {
		// check if server is open yet, if not, delete the old db4o file and open it
		final ServerSocket srv = new ServerSocket(port);
		srv.close();
		if (cleanData) {
			LOGGER.info("cleanData==true and server isn't started,"
					+ " deleting old database prior to starting the server.");
			final boolean success = new File(db4oFile).delete();
			if (!success) {
				LOGGER.error(ERROR_DELETING_OLD_DATABASE);
			}
		}
		final ServerConfiguration serverConfig = Db4oClientServer.newServerConfiguration();
		serverConfig.common().updateDepth(updateDepth);
		serverConfig.common().activationDepth(activationDepth);
		server = Db4oClientServer.openServer(serverConfig, db4oFile, port);
		server.grantAccess(user, pw);
		LOGGER.info("database server instantiated.");
	}

	/**
	 * Open client in client server mode.
	 */
	private void openClientInClientServerMode() {
		LOGGER.debug("trying to open db4o client in client/server mode.");
		final ClientConfiguration clientConfig = Db4oClientServer.newClientConfiguration();
		clientConfig.common().updateDepth(updateDepth);
		clientConfig.common().activationDepth(activationDepth);
		client = Db4oClientServer.openClient(clientConfig, host, port, user, pw);
		LOGGER.info(LOG_DATABASE_CLIENT_INSTANTIATED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	/**
	 * Sets the update depth.
	 * 
	 * @param updateDepth the new update depth
	 */
	public final void setUpdateDepth(final Integer updateDepth) {
		this.updateDepth = updateDepth;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password the new password
	 */
	public final void setPw(final String password) {
		this.pw = password;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user the new user
	 */
	public final void setUser(final String user) {
		this.user = user;
	}

	/**
	 * Sets the host.
	 * 
	 * @param host the new host
	 */
	public final void setHost(final String host) {
		this.host = host;
	}

	/**
	 * Sets the activation depth.
	 * 
	 * @param activationDepth the new activation depth
	 */
	public final void setActivationDepth(final Integer activationDepth) {
		this.activationDepth = activationDepth;
	}

	/**
	 * Sets the db4o file.
	 * 
	 * @param db4oFile the new db4o file
	 */
	public final void setDb4oFile(final String db4oFile) {
		this.db4oFile = db4oFile;
	}

	/**
	 * Sets the client server mode.
	 * 
	 * @param clientServerMode the new client server mode
	 */
	public final void setClientServerMode(final boolean clientServerMode) {
		this.clientServerMode = clientServerMode;
	}

	/**
	 * Sets the port.
	 * 
	 * @param port the new port
	 */
	public final void setPort(final int port) {
		this.port = port;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @param enabled the new enabled
	 */
	public final void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Sets the clean data.
	 * 
	 * @param cleanData the new clean data
	 */
	public final void setCleanData(final boolean cleanData) {
		this.cleanData = cleanData;
	}
}
