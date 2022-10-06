package Events;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RespondEvent extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String reply = event.getMessage().getContentRaw();
		String name = event.getMember().getUser().getName();
		if(reply.equalsIgnoreCase("hi")) {
			if(!event.getMember().getUser().isBot()) {
				event.getChannel().sendMessage("Hey " + name).queue();
			}
		}
		else if(reply.equalsIgnoreCase("bye")) {
			if(!event.getMember().getUser().isBot()) {
				event.getChannel().sendMessage("Bye " + name).queue();
			}
		}
		else if(reply.equalsIgnoreCase("help")) {
			if(!event.getMember().getUser().isBot()) {
				event.getChannel().sendMessage("Do !help for help " + name + ".").queue();
			}
		}
		else if(reply.equalsIgnoreCase("joke")) {
			if(!event.getMember().getUser().isBot()) {
				String info = "";
				try {
					URL url = new URL("https://official-joke-api.appspot.com/random_joke");
					
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.connect();
					int responseCode = connection.getResponseCode();
					if(responseCode != 200) {
						throw new RuntimeException("HttpResponseCode: " + responseCode);
					}
					else {
						StringBuilder infoString = new StringBuilder();
						Scanner scanner = new Scanner(url.openStream());
						while(scanner.hasNext()) {
							infoString.append(scanner.nextLine());
						}
						scanner.close();
						
						JSONParser parse = new JSONParser();
						JSONObject dataObj = (JSONObject) parse.parse(String.valueOf(infoString));
						String joke = (String) dataObj.get("setup");
						String ans = (String) dataObj.get("punchline");
						info = joke + ", " + ans;
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				event.getChannel().sendMessage(info).queue();
			}
		}
	}
	
}
