package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TFT extends ListenerAdapter{

	public void onMessageReceived(MessageReceivedEvent cmd) {
		String prefix = "!";
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("List of Commands");
		embed.setColor(15844367); //Set color to gold
		embed.setThumbnail("https://pbs.twimg.com/media/Eg0q3PeWsAEzFUv.jpg");
		embed.addField("!stats", "Check the your own TFT stats.", true);
		embed.addField("!user [Name]", "Check a user's TFT stats.", true);
		embed.addField("!choncc", "Shows you a thicc choncc.", true);
		String[] message = cmd.getMessage().getContentRaw().split(" ");
		
		if(message[0].equalsIgnoreCase(prefix + "help") && message.length == 1) {
			cmd.getChannel().sendMessageEmbeds(embed.build()).queue();
		}
		
		EmbedBuilder userEmbed = new EmbedBuilder();
		String name = cmd.getMember().getUser().getName();
		userEmbed.setTitle(name + "'s TFT Stats");
		userEmbed.setColor(15158332); //Set color to red
		userEmbed.setThumbnail("https://static.wikia.nocookie.net/leagueoflegends/images/7/76/Little_Legend_Choncc_profileicon.png/revision/latest?cb=20201224030306");
		if(message[0].equalsIgnoreCase(prefix + "user") && message.length == 1) {
			cmd.getChannel().sendMessage("Invalid Input: Please type !user [Name]").queue();
		}
		else if(message[0].equalsIgnoreCase(prefix + "user") && message.length == 2) {
			if(message[1].equalsIgnoreCase(name)) {
				cmd.getChannel().sendMessageEmbeds(userEmbed.build()).queue();
			}
		}
	}
}