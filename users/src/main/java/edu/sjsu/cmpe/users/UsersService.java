package edu.sjsu.cmpe.users;
import org.codehaus.jackson.map.Module;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.Json;

import edu.sjsu.cmpe.users.config.CustomJson;
import edu.sjsu.cmpe.users.config.UserServiceConfiguration;
import edu.sjsu.cmpe.users.api.resources.UserResource;

public class UsersService extends Service<UserServiceConfiguration> {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new UsersService().run(args);
	}
	
	private UsersService()
	{
		super("users");
	}
	
	@Override
	protected void initialize(UserServiceConfiguration configuration, Environment environment) throws Exception
	{
		environment.addResource(UserResource.class);
		//environment.addResource(BookResource.class);
		//environment.addResource(ReviewResource.class);
		
	}
	
	@Override
	public Json getJson()
	{
		//System.out.println("Test");
		final CustomJson json=new CustomJson();
		for(Module module : getJacksonModules())
		{
			json.registerModule(module);
		
		}
		return json;
	}
}
