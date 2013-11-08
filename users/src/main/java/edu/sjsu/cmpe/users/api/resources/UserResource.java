package edu.sjsu.cmpe.users.api.resources;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
//import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.mongo.config.MongoConfig;
import edu.sjsu.cmpe.users.dao.MongoDao;
import edu.sjsu.cmpe.users.domain.UserDetails;
import edu.sjsu.cmpe.users.domain.EventDetails;

@Path("/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserResource {
	private int authenticated = 400;
	
	public UserResource()
	{
	}
	
	@POST
	@Path("/users/create")
	@Timed(name="create-user")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response user(UserDetails user) throws FileNotFoundException, IOException
	{
		MongoConfig connection = new MongoConfig();
		MongoDao client = new MongoDao();
		client.getDBConnection(connection.dbHostName, connection.dbPortNumber);
		client.getDB(connection.dbName);
		client.getCollection("usercollection");
		
		BasicDBObject doc = new BasicDBObject("sjsuId", user.getSjsuId()).append("firstname", user.getFirstName()).append("lastname", user.getLastName()).append("email", user.getEmail()).append("password", user.getPassword());
		client.insertData(doc);
		client.closeConnection();

		return Response.status(201).entity(user).build();
	}
	
	@POST
	@Path("/users/authenticate")
	@Timed(name="authenticate-user")
	public Response authenticateUser(UserDetails user) throws FileNotFoundException, IOException
	{
		MongoConfig connection = new MongoConfig();
		MongoDao client = new MongoDao();
		client.getDBConnection(connection.dbHostName, connection.dbPortNumber);
		client.getDB(connection.dbName);
		client.getCollection("userCollection");
		
		BasicDBObject authenticate = new BasicDBObject();
		List <BasicDBObject> authList = new ArrayList <BasicDBObject>();
		authList.add(new BasicDBObject("email", user.getEmail()));
		authList.add(new BasicDBObject("password", user.getPassword()));
		authenticate.put("$and", authList);
		
		DBCursor cursor = client.findData(authenticate);
		while(cursor.hasNext())
		{
			System.out.println(cursor.hasNext());
			authenticated =200;
		}
		return null;
	}
	
	@POST
	@Path("/events/create")
	@Timed(name="create-event")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response event(EventDetails event) throws FileNotFoundException, IOException 
	{
		MongoConfig connection = new MongoConfig();
		MongoDao client = new MongoDao();
		client.getDBConnection(connection.dbHostName, connection.dbPortNumber);
		client.getDB(connection.dbName);
		client.getCollection("eventcollection");
		
		BasicDBObject doc = new BasicDBObject("eventId", event.getEventId()).append("eventName", event.getEventName()).append("eventCategory", event.getEventCategory()).append("eventDescription", event.getEvenDescription()).append("eventDate", event.getEventDate()).append("eventTime", event.getEventTime());
		
		client.closeConnection();

		return Response.status(201).entity(event).build();
	}
	
	@GET
	@Path("/users")
	@Timed(name="get-all-users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public DBCollection getAllUsers() throws FileNotFoundException, IOException
	{
		MongoConfig connection = new MongoConfig();
		MongoDao client = new MongoDao();
		client.getDBConnection(connection.dbHostName, connection.dbPortNumber);
		client.getDB(connection.dbName);
		client.getCollection("userCollection");
		
		return client.getCollection("usercollection");
	}
	
	@GET
	@Path("/events")
	@Timed(name="get-all-events")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public DBCollection getAllEvents() throws FileNotFoundException, IOException
	{
		MongoConfig connection = new MongoConfig();
		MongoDao client = new MongoDao();
		client.getDBConnection(connection.dbHostName, connection.dbPortNumber);
		client.getDB(connection.dbName);
		client.getCollection("eventCollection");
		
		return client.getCollection("eventcollection");
	}

}
