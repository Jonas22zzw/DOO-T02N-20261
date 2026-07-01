package fag.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Series {

    private final int id;
    private String name;
    private String language;
    private List<String> genres;
    private Double rating;
    private SeriesStatus status;
    private String premiered;
    private String ended;
    private String network;
    private String summary;
    private String imageUrl;

    public Series(int id) {
        this.id = id;
        this.genres = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres != null ? genres : new ArrayList<>();
    }

    public String getGenresAsText() {
        return String.join(", ", genres);
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public SeriesStatus getStatus() {
        return status;
    }

    public void setStatus(SeriesStatus status) {
        this.status = status != null ? status : SeriesStatus.UNKNOWN;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSummaryPlainText() {
        if (summary == null) {
            return "";
        }
        return summary.replaceAll("<[^>]*>", "");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("language", language);
        map.put("genres", new ArrayList<Object>(genres));
        map.put("rating", rating);
        map.put("status", status != null ? status.name() : SeriesStatus.UNKNOWN.name());
        map.put("premiered", premiered);
        map.put("ended", ended);
        map.put("network", network);
        map.put("summary", summary);
        map.put("imageUrl", imageUrl);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Series fromMap(Map<String, Object> map) {
        int id = ((Number) map.get("id")).intValue();
        Series s = new Series(id);
        s.setName((String) map.get("name"));
        s.setLanguage((String) map.get("language"));
        Object genresObj = map.get("genres");
        List<String> genres = new ArrayList<>();
        if (genresObj instanceof List) {
            for (Object g : (List<Object>) genresObj) {
                genres.add(String.valueOf(g));
            }
        }
        s.setGenres(genres);
        Object ratingObj = map.get("rating");
        s.setRating(ratingObj != null ? ((Number) ratingObj).doubleValue() : null);
        Object statusObj = map.get("status");
        try {
            s.setStatus(statusObj != null ? SeriesStatus.valueOf(String.valueOf(statusObj)) : SeriesStatus.UNKNOWN);
        } catch (IllegalArgumentException ex) {
            s.setStatus(SeriesStatus.UNKNOWN);
        }
        s.setPremiered((String) map.get("premiered"));
        s.setEnded((String) map.get("ended"));
        s.setNetwork((String) map.get("network"));
        s.setSummary((String) map.get("summary"));
        s.setImageUrl((String) map.get("imageUrl"));
        return s;
    }

    @SuppressWarnings("unchecked")
    public static Series fromApiMap(Map<String, Object> show) {
        int id = ((Number) show.get("id")).intValue();
        Series s = new Series(id);
        s.setName((String) show.get("name"));
        s.setLanguage((String) show.get("language"));

        Object genresObj = show.get("genres");
        List<String> genres = new ArrayList<>();
        if (genresObj instanceof List) {
            for (Object g : (List<Object>) genresObj) {
                genres.add(String.valueOf(g));
            }
        }
        s.setGenres(genres);

        Object ratingObj = show.get("rating");
        if (ratingObj instanceof Map) {
            Object avg = ((Map<String, Object>) ratingObj).get("average");
            s.setRating(avg != null ? ((Number) avg).doubleValue() : null);
        }

        s.setStatus(SeriesStatus.fromApiValue((String) show.get("status")));
        s.setPremiered((String) show.get("premiered"));
        s.setEnded((String) show.get("ended"));

        String networkName = null;
        Object networkObj = show.get("network");
        if (networkObj instanceof Map) {
            networkName = (String) ((Map<String, Object>) networkObj).get("name");
        }
        if (networkName == null) {
            Object webChannelObj = show.get("webChannel");
            if (webChannelObj instanceof Map) {
                networkName = (String) ((Map<String, Object>) webChannelObj).get("name");
            }
        }
        s.setNetwork(networkName);

        s.setSummary((String) show.get("summary"));

        Object imageObj = show.get("image");
        if (imageObj instanceof Map) {
            Object medium = ((Map<String, Object>) imageObj).get("medium");
            s.setImageUrl(medium != null ? String.valueOf(medium) : null);
        }

        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Series)) return false;
        Series series = (Series) o;
        return id == series.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name != null ? name : ("Serie #" + id);
    }
}
