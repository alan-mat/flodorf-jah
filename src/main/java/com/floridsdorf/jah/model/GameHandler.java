package com.floridsdorf.jah.model;

import org.json.*;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {

    private List<String> prompts;

    private static final String promptsJSONPath = "com/floridsdorf/jah/prompts_from_cah.json";

    public GameHandler(){
        prompts = new ArrayList<>();
        try {
            parsePromptsJSON(promptsJSONPath);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    private void parsePromptsJSON(String path) throws IOException {
        String text = readFileFromResources(path);
        JSONObject jsonObject = new JSONObject(text);
        JSONArray prompts = jsonObject.getJSONArray("prompts");
        for(int i = 0; i < prompts.length(); i++){
            JSONObject promptObject = prompts.getJSONObject(i);
            //only add prompts with 1 blank
            if(promptObject.getInt("pick") == 1)
                this.prompts.add(promptObject.getString("text"));
        }
    }

    private static String readFileFromResources(String fileName) throws IOException {
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

    public List<String> getPrompts(){ return prompts; }

}
