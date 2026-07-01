package fag.service;

import fag.exceptions.PersistenceException;
import fag.json.JsonParser;
import fag.json.JsonWriter;
import fag.model.AppData;
import fag.model.ListType;
import fag.model.Series;
import fag.model.SeriesStatus;
import fag.model.UserLibrary;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersistenceService {

    private final Path dataFile;

    public PersistenceService() {
        Path homeDir = Paths.get(System.getProperty("user.home"), ".tvtracker");
        this.dataFile = homeDir.resolve("dados.json");
    }

    public PersistenceService(Path customFile) {
        this.dataFile = customFile;
    }

    @SuppressWarnings("unchecked")
    public AppData load() throws PersistenceException {
        try {
            if (!Files.exists(dataFile)) {
                AppData seed = createSeedData();
                save(seed);
                return seed;
            }

            String content = new String(Files.readAllBytes(dataFile), StandardCharsets.UTF_8);
            if (content.trim().isEmpty()) {
                AppData seed = createSeedData();
                save(seed);
                return seed;
            }

            Object parsed = JsonParser.parse(content);
            if (!(parsed instanceof Map)) {
                throw new PersistenceException("O arquivo de dados esta corrompido (formato inesperado).");
            }
            return mapToAppData((Map<String, Object>) parsed);
        } catch (IOException ex) {
            throw new PersistenceException("Nao foi possivel ler o arquivo de dados em " + dataFile, ex);
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception ex) {
            throw new PersistenceException("O arquivo de dados esta corrompido e nao pode ser interpretado.", ex);
        }
    }

    public void save(AppData appData) throws PersistenceException {
        try {
            if (dataFile.getParent() != null) {
                Files.createDirectories(dataFile.getParent());
            }
            Map<String, Object> root = appDataToMap(appData);
            String json = JsonWriter.write(root);
            Files.write(dataFile, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new PersistenceException("Nao foi possivel salvar os dados em " + dataFile, ex);
        }
    }

    private Map<String, Object> appDataToMap(AppData appData) {
        Map<String, Object> root = new LinkedHashMap<>();

        Map<String, Object> seriesCacheMap = new LinkedHashMap<>();
        for (Series s : appData.getSeriesCache().values()) {
            seriesCacheMap.put(String.valueOf(s.getId()), s.toMap());
        }
        root.put("seriesCache", seriesCacheMap);

        Map<String, Object> usersMap = new LinkedHashMap<>();
        for (Map.Entry<String, UserLibrary> entry : appData.getUsers().entrySet()) {
            UserLibrary lib = entry.getValue();
            Map<String, Object> userMap = new LinkedHashMap<>();
            userMap.put("favorites", new ArrayList<Object>(lib.getFavoriteIds()));
            userMap.put("watched", new ArrayList<Object>(lib.getWatchedIds()));
            userMap.put("wantToWatch", new ArrayList<Object>(lib.getWantToWatchIds()));
            usersMap.put(entry.getKey(), userMap);
        }
        root.put("users", usersMap);

        return root;
    }

    @SuppressWarnings("unchecked")
    private AppData mapToAppData(Map<String, Object> root) {
        AppData appData = new AppData();

        Object seriesCacheObj = root.get("seriesCache");
        if (seriesCacheObj instanceof Map) {
            for (Object value : ((Map<String, Object>) seriesCacheObj).values()) {
                if (value instanceof Map) {
                    try {
                        Series s = Series.fromMap((Map<String, Object>) value);
                        appData.cacheSeries(s);
                    } catch (Exception ex) {

                    }
                }
            }
        }

        Object usersObj = root.get("users");
        if (usersObj instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) usersObj).entrySet()) {
                String username = entry.getKey();
                UserLibrary lib = appData.getOrCreateUser(username);
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> userMap = (Map<String, Object>) entry.getValue();
                    addIds(lib, ListType.FAVORITES, userMap.get("favorites"));
                    addIds(lib, ListType.WATCHED, userMap.get("watched"));
                    addIds(lib, ListType.WANT_TO_WATCH, userMap.get("wantToWatch"));
                }
            }
        }

        return appData;
    }

    @SuppressWarnings("unchecked")
    private void addIds(UserLibrary lib, ListType type, Object listObj) {
        if (listObj instanceof List) {
            for (Object idObj : (List<Object>) listObj) {
                if (idObj instanceof Number) {
                    lib.add(type, ((Number) idObj).intValue());
                }
            }
        }
    }

    private AppData createSeedData() {
        AppData appData = new AppData();

        Series breakingBad = new Series(169);
        breakingBad.setName("Breaking Bad");
        breakingBad.setLanguage("English");
        breakingBad.setGenres(Arrays.asList("Drama", "Crime", "Thriller"));
        breakingBad.setRating(9.4);
        breakingBad.setStatus(SeriesStatus.ENDED);
        breakingBad.setPremiered("2008-01-20");
        breakingBad.setEnded("2013-09-29");
        breakingBad.setNetwork("AMC");
        breakingBad.setSummary("Um professor de quimica diagnosticado com cancer entra no mundo da fabricacao de drogas.");

        Series got = new Series(82);
        got.setName("Game of Thrones");
        got.setLanguage("English");
        got.setGenres(Arrays.asList("Drama", "Adventure", "Fantasy"));
        got.setRating(9.0);
        got.setStatus(SeriesStatus.ENDED);
        got.setPremiered("2011-04-17");
        got.setEnded("2019-05-19");
        got.setNetwork("HBO");
        got.setSummary("Nove familias nobres lutam pelo controle das terras miticas de Westeros.");

        Series friends = new Series(431);
        friends.setName("Friends");
        friends.setLanguage("English");
        friends.setGenres(Arrays.asList("Comedy", "Romance"));
        friends.setRating(8.7);
        friends.setStatus(SeriesStatus.ENDED);
        friends.setPremiered("1994-09-22");
        friends.setEnded("2004-05-06");
        friends.setNetwork("NBC");
        friends.setSummary("Um grupo de amigos vive suas vidas e enfrenta os altos e baixos do dia a dia em Nova York.");

        Series strangerThings = new Series(2993);
        strangerThings.setName("Stranger Things");
        strangerThings.setLanguage("English");
        strangerThings.setGenres(Arrays.asList("Drama", "Horror", "Science-Fiction"));
        strangerThings.setRating(8.6);
        strangerThings.setStatus(SeriesStatus.RUNNING);
        strangerThings.setPremiered("2016-07-15");
        strangerThings.setEnded(null);
        strangerThings.setNetwork("Netflix");
        strangerThings.setSummary("Em uma pequena cidade, forcas sobrenaturais e experimentos secretos ameacam seus moradores.");

        Series bigBang = new Series(66);
        bigBang.setName("The Big Bang Theory");
        bigBang.setLanguage("English");
        bigBang.setGenres(Arrays.asList("Comedy"));
        bigBang.setRating(8.1);
        bigBang.setStatus(SeriesStatus.ENDED);
        bigBang.setPremiered("2007-09-24");
        bigBang.setEnded("2019-05-16");
        bigBang.setNetwork("CBS");
        bigBang.setSummary("Dois fisicos brilhantes e socialmente desajeitados dividem apartamento com uma aspirante a atriz.");

        for (Series s : Arrays.asList(breakingBad, got, friends, strangerThings, bigBang)) {
            appData.cacheSeries(s);
        }

        UserLibrary convidado = appData.getOrCreateUser("Convidado");
        convidado.add(ListType.FAVORITES, breakingBad.getId());
        convidado.add(ListType.FAVORITES, got.getId());
        convidado.add(ListType.WATCHED, breakingBad.getId());
        convidado.add(ListType.WATCHED, friends.getId());
        convidado.add(ListType.WANT_TO_WATCH, strangerThings.getId());
        convidado.add(ListType.WANT_TO_WATCH, bigBang.getId());

        return appData;
    }

    public Path getDataFile() {
        return dataFile;
    }
}
