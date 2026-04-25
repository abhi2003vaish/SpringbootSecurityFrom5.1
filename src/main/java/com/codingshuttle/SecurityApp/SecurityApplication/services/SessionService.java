package com.codingshuttle.SecurityApp.SecurityApplication.services;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.Session;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT=2;

//    1st method to create a new Session
    public void generateNewSession(User user,String refreshToken){
        List<Session> userSessions=sessionRepository.findByUser(user);
        if(userSessions.size()==SESSION_LIMIT){
//            first we sort the list of sessions
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastRecentlyUsedSession=userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

//        if we are here it mean SESSION_LIMIT<2
//        so now create a new Session
        Session newSession=Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

//        now save the session in db
        sessionRepository.save(newSession);
    }


//    2nd method is for session is valiad or not
    public void validateSession(String refreshToken){
        Session session=sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() ->new SessionAuthenticationException("Session not found for refresh token: "+refreshToken));

//        this method is call from the "/refresh" api so we need to update the LastUsedAt also
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
