package fag.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppData {

    private final Map<String, UserLibrary> users = new LinkedHashMap<>();
    private final Map<Integer, Series> seriesCache = new LinkedHashMap<>();

    public Map<String, UserLibrary> getUsers() {
        return users;
    }

    public Map<Integer, Series> getSeriesCache() {
        return seriesCache;
    }

    public UserLibrary getOrCreateUser(String username) {
        return users.computeIfAbsent(username, UserLibrary::new);
    }

    public boolean hasUser(String username) {
        return users.containsKey(username);
    }

    public List<String> getUsernames() {
        return new ArrayList<>(users.keySet());
    }

    public void cacheSeries(Series series) {
        seriesCache.put(series.getId(), series);
    }

    public Series getCachedSeries(int id) {
        return seriesCache.get(id);
    }

    public List<Series> resolve(List<Integer> ids) {
        List<Series> result = new ArrayList<>();
        for (Integer id : ids) {
            Series s = seriesCache.get(id);
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }
}
