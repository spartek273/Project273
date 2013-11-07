package edu.sjsu.cmpe.users.api.resources;



import java.util.HashMap;
import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;







//import com.sun.jersey.core.header.reader.HttpHeaderReader.Event;
import com.google.common.base.Optional;
//import com.yammer.dropwizard.jersey.params.IntParam;
import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.users.domain.UserDetails;
import edu.sjsu.cmpe.users.domain.EventDetails;

@Path("/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserResource {
	
	private static HashMap <Integer, UserDetails> userMap = new HashMap<Integer,UserDetails>();
	private static HashMap <Integer, EventDetails> eventMap = new HashMap<Integer,EventDetails>();
	private static HashMap <String, EventDetails> locMap = new HashMap <String, EventDetails>();
	public UserResource()
	{
	}
	
	@POST
	@Path("/users/create")
	@Timed(name="create-user")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response user(UserDetails user)
	{
		int num= userMap.size()+1;
		user.setId(num);
		userMap.put(num, user);		
		//System.out.println(" " +user.getTime());
		return Response.status(201).entity(user).build();
	}
	
	@POST
	@Path("/events/create")
	@Timed(name="create-event")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response event(EventDetails event)
	{
		int num= eventMap.size()+1;
		event.setId(num);
		eventMap.put(num, event);	
		locMap.put(event.getLatitude(), event);
		return Response.status(201).entity(event).build();
	}
	
	@GET
	@Path("/users/location/")
	@Timed(name="get-event-by-user-location")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getEvent(@QueryParam("latitude") Optional <String> latitude)
	{
		String eventName = "";
		
		//EventDetails tempEvent= new EventDetails();
		for(EventDetails iter: locMap.values())
		{
			if(iter.getLatitude()==latitude.get())
			{
				eventName=iter.getName();
				System.out.println(""+eventName);
			}
		}
		return eventName;
	}	
	
	@GET
	@Path("/users")
	@Timed(name="get-all-users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<Integer, UserDetails> getAllUsers()
	{
		return userMap;
	}
	
	@GET
	@Path("/events")
	@Timed(name="get-all-")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HashMap<Integer, EventDetails> getAllEvents()
	{
		return eventMap;
	}

}
