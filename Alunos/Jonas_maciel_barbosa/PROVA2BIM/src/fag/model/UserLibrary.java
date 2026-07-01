package fag.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UserLibrary {

    private final String username;
    private final Set<Integer> favoriteIds = new LinkedHashSet<>();
    private final Set<Integer> watchedIds = new LinkedHashSet<>();
    private final Set<Integer> wantToWatchIds = new LinkedHashSet<>();

    public UserLibrary(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Set<Integer> getIds(ListType type) {
        switch (type) {
            case FAVORITES:
                return favoriteIds;
            case WATCHED:
                return watchedIds;
            case WANT_TO_WATCH:
                return wantToWatchIds;
            default:
                throw new IllegalArgumentException("Tipo de lista desconhecido: " + type);
        }
    }

    public boolean contains(ListType type, int seriesId) {
        return getIds(type).contains(seriesId);
    }

    public boolean add(ListType type, int seriesId) {
        return getIds(type).add(seriesId);
    }

    public boolean remove(ListType type, int seriesId) {
        return getIds(type).remove(seriesId);
    }

    public boolean toggle(ListType type, int seriesId) {
        if (contains(type, seriesId)) {
            remove(type, seriesId);
            return false;
        } else {
            add(type, seriesId);
            return true;
        }
    }

    public List<Integer> getFavoriteIds() {
        return new ArrayList<>(favoriteIds);
    }

    public List<Integer> getWatchedIds() {
        return new ArrayList<>(watchedIds);
    }

    public List<Integer> getWantToWatchIds() {
        return new ArrayList<>(wantToWatchIds);
    }
}
