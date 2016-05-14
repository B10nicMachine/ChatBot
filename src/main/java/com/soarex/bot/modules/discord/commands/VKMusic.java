package com.soarex.bot.modules.discord.commands;

import com.soarex.bot.SoarexBot;
import com.soarex.bot.api.ICommand;
import com.soarex.bot.modules.discord.Discord;
import com.soarex.bot.modules.discord.MsgListener;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by shumaf on 14.05.16.
 */
public class VKMusic implements ICommand {
    @Override
    public String description() {
        return "включает в голосовом чате заданную или рандомную мелодию";
    }

    @Override
    public String usage() {
        return "!музыку или !музыку [название аудиозаписи]";
    }

    @Override
    public Permissions permission() {
        return Permissions.SEND_MESSAGES;
    }

    @Override
    public void execute(String... args) {
        String token = "c1f6f1e37cc8ce4e8d08351d6d3969e91d15b45c69b592e094120d969a4d41eebfcd075cd88b7ac313355";
        StringBuilder query = new StringBuilder();
        for (String s : args) {
            query.append(s);
            query.append(" ");
        }
        if (args.length == 0) {

        } else {
            try {
                URIBuilder builder = new URIBuilder();
                builder.setScheme("https").setHost("api.vk.com").setPath("/method/audio.search")
                        .setParameter("q", query.toString())
                        .setParameter("auto_complete", "1")
                        .setParameter("lyrics", "0")
                        .setParameter("performer_only", "0")
                        .setParameter("sort", "2")
                        .setParameter("search_own", "0")
                        .setParameter("offset", "0")
                        .setParameter("count", "1")
                        .setParameter("access_token", token);
                URI uri = builder.build();
                HttpGet httpget = new HttpGet(uri);

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpResponse response = httpClient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try(InputStream instream = entity.getContent()) {
                        String responseAsString = IOUtils.toString(instream);
                        SoarexBot.LOGGER.info("AUDIO|response = " + responseAsString);
                        parseAndPlay(responseAsString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseAndPlay(String responseAsString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(responseAsString);
        JSONArray mp3list = (JSONArray) jsonResponse.get("response");
        if (mp3list.size() > 1) {
            JSONObject mp3 = (JSONObject) mp3list.get(1);
            IVoiceChannel channel = Discord.discordClient.getVoiceChannelByID(Discord.musicChannel);
            if (!channel.isConnected()) {
                channel.join();
            }
            try {
                MsgListener.msgChannel.sendMessage("Добавлено в очередь: **" + mp3.get("title") + "**");
                channel.getAudioChannel().queueUrl((String) mp3.get("url"));
            } catch (DiscordException e) {
                e.printStackTrace();
            } catch (MissingPermissionsException e) {
                e.printStackTrace();
            } catch (HTTP429Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                MsgListener.msgChannel.sendMessage("Аудиозапись не найдена");
            } catch (MissingPermissionsException e) {
                e.printStackTrace();
            } catch (HTTP429Exception e) {
                e.printStackTrace();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }
}
