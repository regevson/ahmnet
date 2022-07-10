package at.ahmacademy.ahmnet.dtos;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ahmacademy.ahmnet.model.Club;
import at.ahmacademy.ahmnet.services.trainingGroup.ClubService;

@Component
public class ClubMapper {

  @Autowired
  private ClubService clubService;

  public ClubDto mapToClubDto(Club club) {
    ClubDto dto = new ClubDto();
    dto.setId(club.getId());
    return dto;
  }

  public Collection<ClubDto> mapToClubDto(Collection<Club> clubs) {
    Collection<ClubDto> dtos = new ArrayList<>();
    for(Club c: clubs)
      dtos.add(mapToClubDto(c));
    return dtos;
  }

  public Club mapFromClubDto(ClubDto dto) {
    Club club = this.clubService.loadClub(dto.getId());
    club.setId(dto.getId());
    return club;
  }

}
