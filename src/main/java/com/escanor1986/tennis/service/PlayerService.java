package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerList;

@Service
public class PlayerService {

  public List<Player> getAllPlayers() {
    return PlayerList.ALL.stream()
            .sorted(Comparator.comparing(player -> player.rank().position()))
            .collect(Collectors.toList());
}

  public Player getPlayerByLastName(String lastName) {
    return PlayerList.ALL.stream()
        .filter(player -> player.lastName().equals(lastName))
        .findFirst()
        .orElseThrow(() -> new PlayerNotFoundException(lastName));
  }
}
