package com.floridsdorf.jah.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<String> parsePromptsJSON(String path) {
        List<String> prompts = new ArrayList<>();
        String text = readFileFromResources(path);
        JSONObject jsonObject = new JSONObject(text);
        JSONArray promptsJSON = jsonObject.getJSONArray("prompts");
        for(int i = 0; i < promptsJSON.length(); i++){
            JSONObject promptObject = promptsJSON.getJSONObject(i);
            //only add prompts with 1 blank
            if(promptObject.getInt("pick") == 1)
                prompts.add(promptObject.getString("text"));
        }
        return prompts;
    }

    public static String readFileFromResources(String fileName) {
        URL resource = GameHandler.class.getClassLoader().getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("File is not found!");

        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(resource.getFile())));){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

}
