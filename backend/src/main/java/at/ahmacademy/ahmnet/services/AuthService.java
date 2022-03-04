package at.ahmacademy.ahmnet.services;

import org.springframework.security.access.AccessDeniedException;

public class AuthService<T> {

  public static void authWhen(boolean auth) {
    if(!auth)
      throw new AccessDeniedException("Fuer diese Aktion hast du zu wenig Rechte!");
  }
  
}
