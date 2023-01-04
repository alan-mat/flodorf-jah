package com.floridsdorf.jah.model.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Player {

    private final String name;
    private final List<Joke> jokeList;

}
