package view.platforms.discord.logic.listeners

import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import view.botservices.database.DiscordDBO

class GuildListener : ListenerAdapter() {
    private val dbo = DiscordDBO()

    override fun onGuildJoin(event: GuildJoinEvent) {
        val guildClient = event.guild.idLong
        val text = "хай, я ржу бот, пиши -help и разбирайся че делать будем" //todo embed info

        event.guild.defaultChannel!!.sendMessage(text).queue()

        if (dbo.clientIsExists(guildClient))
            return

        dbo.addClient(guildClient)
    }
}