package at.ahmacademy.ahmnet.controllers;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ahmacademy.ahmnet.dtos.ClubDto;
import at.ahmacademy.ahmnet.dtos.ClubMapper;
import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.services.club.ClubService;

@RequestMapping("/api")
@RestController
@Scope("application")
public class ClubController {

  @Autowired
  ClubService clubService;
  @Autowired
  ClubMapper mapper;

  @GetMapping("/clubs")
  public ResponseEntity<?> getAllClubs() {
    Collection<ClubDto> clubs = mapper.mapToClubDto(clubService.loadAllClubs());
    return ResponseEntity.status(HttpStatus.OK).body(clubs);
  }

  @GetMapping("/clubs/actions/count-groups")
  public ResponseEntity<?> getAllClubsWithGroupCount() {
    Collection<Club> clubs = clubService.loadAllClubs();
    Map<String, Integer> club_groupNum = clubService.getNumOfGroups(clubs);
    return ResponseEntity.status(HttpStatus.OK).body(club_groupNum);
  }

  @GetMapping("/clubs/actions/count-players")
  public ResponseEntity<?> getAllClubsWithPlayerCount() {
    Collection<Club> clubs = clubService.loadAllClubs();
    Map<String, Integer> club_groupNum = clubService.getNumOfPlayers(clubs);
    return ResponseEntity.status(HttpStatus.OK).body(club_groupNum);
  }

}
