package com.duck.scores;

import lombok.*;

import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkourScore {

    private UUID uuid;
    private long time;

}
