package view.platforms.discord.logic.commands.settings

import net.dv8tion.jda.api.EmbedBuilder
import view.botservices.DatabaseOperations
import view.botservices.DateFormatter
import view.platforms.discord.logic.services.commandmanagment.Command
import view.platforms.discord.logic.services.commandmanagment.CommandContext
import java.awt.Color

class GetConfig : Command {
    override fun handle(ctx: CommandContext) {
        val dbo = DatabaseOperations(ctx.sessionFactory)
        val configList = dbo.getClientDiscordProperties(ctx.event.guild.idLong)

        val stringBuilder = StringBuilder()
        for ((key, value) in configList) {
            stringBuilder.append("`$key`: $value\n".replace("none", "Отсутствует"))
        }

        val eb = EmbedBuilder().apply {
            setColor(Color(0xff52cb))
            setAuthor("Настройки ${ctx.event.guild.name}")
            setTitle("Для уточения значения настроек указывайте команду `config info`")
            addField("", stringBuilder.toString(), false)
            setFooter("Запрошено пользователем ${ctx.event.author.asTag} | ${DateFormatter.today()}")
        }

        ctx.event.channel.sendMessageEmbeds(eb.build()).queue()
    }

    override fun getName(): String =
        "config"
}