package com.reminder.security;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenBlacklistService {

    private final Map<String, List<String>> userTokensMap = new HashMap<>();
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void addTokenToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void saveTokenForUser(String username, String token) {
        userTokensMap.computeIfAbsent(username, k -> new ArrayList<>()).add(token);
    }

    public List<String> getTokensByUsername(String username) {
        return userTokensMap.getOrDefault(username, Collections.emptyList());
    }

    //public void clearUserTokens(String username) {
    //    List<String> tokens = userTokensMap.remove(username);
    //    if (tokens != null) {
    //        blacklistedTokens.addAll(tokens);
    //    }
    //}

    public boolean isTokenInMap(String username, String token) {
        List<String> tokens = userTokensMap.get(username);
        return tokens != null && tokens.contains(token);
    }
}