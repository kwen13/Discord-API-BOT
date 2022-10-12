package Commands;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TFT extends ListenerAdapter{

	public void onMessageReceived(MessageReceivedEvent cmd) {
		String prefix = "#";
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("List of Commands");
		embed.setColor(15844367); //Set color to gold
		embed.setThumbnail("https://pbs.twimg.com/media/Eg0q3PeWsAEzFUv.jpg");
		embed.addField("#stats", "Check the your own TFT stats.", true);
		embed.addField("#user [Name]", "Check a user's TFT stats.", true);
		embed.addField("#leaderboard", "Displays TFT top 10 leaderboard.", true);
		embed.addField("#choncc", "Shows you a thicc choncc.", true);
		String[] message = cmd.getMessage().getContentRaw().split(" ");
		
		if(message[0].equalsIgnoreCase(prefix + "help") && message.length == 1) {
			cmd.getChannel().sendMessageEmbeds(embed.build()).queue();
		}
		
		else if(message[0].equalsIgnoreCase(prefix + "user") && message.length == 1) {
			if(!cmd.getMember().getUser().isBot()) {
				cmd.getChannel().sendMessage("Invalid Input: Please type !user [Name]").queue();
			}
		}
		
		else if(message[0].equalsIgnoreCase(prefix + "user") && message.length > 1) {
			EmbedBuilder userEmbed = new EmbedBuilder();
			String name = "";
			for(int i = 1; i < message.length; i++) {
				name += message[i];
			}
			userEmbed.setColor(15158332); //Set color to red
			userEmbed.setThumbnail("https://static.wikia.nocookie.net/leagueoflegends/images/7/76/Little_Legend_Choncc_profileicon.png/revision/latest?cb=20201224030306");
			userEmbed.setDescription("");
			if(!cmd.getMember().getUser().isBot()) {
				
				String summonerID = "";
				try {
					URL url = new URL("Riot API" + name + "Riot API");
						
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
						summonerID = (String) dataObj.get("id");
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					URL url = new URL("Riot API" + summonerID + "Riot API");
						
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
						JSONArray dataArr = (JSONArray) parse.parse(String.valueOf(infoString));
						for(Object data : dataArr) {
							JSONObject dataObj = (JSONObject) data;
							String summonerName = (String) dataObj.get("summonerName");
							String tier = (String) dataObj.get("tier");
							String rank = (String) dataObj.get("rank");
							long LP = (long) dataObj.get("leaguePoints");
							double top4 = ((double)(long) dataObj.get("wins") / ((double)(long)dataObj.get("wins") + (double)(long)dataObj.get("losses")))*100;
							top4 = Math.round(top4);
							
							userEmbed.setTitle(summonerName + "'s TFT Stats");
							userEmbed.addField("User", summonerName, true);
							userEmbed.addField("Rank", tier + " " + rank + " " + LP + "LP", true);
							userEmbed.addField("Top4 %", top4 + "%", true);
						}
						
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				cmd.getChannel().sendMessageEmbeds(userEmbed.build()).queue();
			}
		}
		
		else if(message[0].equalsIgnoreCase(prefix + "leaderboard") && message.length == 1) {
			EmbedBuilder leaderboardEmbed = new EmbedBuilder();
			leaderboardEmbed.setTitle("TFT Top 10 Leaderbaord");
			leaderboardEmbed.setColor(9031664); //Set color to Blue
			leaderboardEmbed.setThumbnail("https://static.wikia.nocookie.net/leagueoflegends/images/5/5e/Choncc_Atlantean_Tier_2.png/revision/latest/scale-to-width-down/512?cb=20211004011746");
			leaderboardEmbed.setDescription("");
			if(!cmd.getMember().getUser().isBot()) {
					
				String info = "";
				try {
					URL url = new URL("Riot API");
						
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
						JSONArray players = (JSONArray) dataObj.get("entries");
						JSONArray sortedByLP = new JSONArray();
						List<JSONObject> list = new ArrayList<JSONObject>();
					    for (int i = 0; i < players.size(); i++) {
					    	list.add((JSONObject)players.get(i));
					    }
						Collections.sort(list, new Comparator<JSONObject>() {

					        public int compare(JSONObject a, JSONObject b) {
					            long valA = 0;
					            long valB = 0;

					            try {
					                valA = (long) a.get("leaguePoints");
					                valB = (long) b.get("leaguePoints");
					            } 
					            catch (Exception e) {
					            	e.printStackTrace();
					            }
					           
					            return (int)-(valA - valB);
					        }
						});
						for (int i = 0; i < players.size(); i++) {
					        sortedByLP.add(list.get(i));
					    }
						for(int i = 0; i < 10; i++) {
							JSONObject player = (JSONObject) sortedByLP.get(i);
							String summonerName = (String) player.get("summonerName");
							long leaguePoints = (long) player.get("leaguePoints");
							info = (i+1) + ". " + summonerName + ": " + leaguePoints + " LP\n";
							leaderboardEmbed.appendDescription(info);
							
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				cmd.getChannel().sendMessageEmbeds(leaderboardEmbed.build()).queue();
			}
		}
	}
}
