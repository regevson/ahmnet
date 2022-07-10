package at.ahmacademy.ahmnet.repositories;

import org.springframework.stereotype.Repository;

import at.ahmacademy.ahmnet.model.Club;

@Repository
public interface ClubRepository extends AbstractRepository<Club, String> {

  Club findFirstById(String clubName);

}
