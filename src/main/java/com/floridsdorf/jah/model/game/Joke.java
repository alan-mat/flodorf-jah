package com.floridsdorf.jah.model.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Joke {

    private final String text;

    private Rating rating;


}
