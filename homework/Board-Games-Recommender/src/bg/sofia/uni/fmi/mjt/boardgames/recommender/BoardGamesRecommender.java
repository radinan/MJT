package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.BoardGameDistance;
import bg.sofia.uni.fmi.mjt.boardgames.BoardGameKeywordsCount;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BoardGamesRecommender implements Recommender {

    private final List<BoardGame> allGames;
    private final List<String> stopwords;
    private final Map<String, Integer> wordsToGamesIndexes;

    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {
        this.allGames = new ArrayList<>();
        this.stopwords = new ArrayList<>();
        this.wordsToGamesIndexes = new LinkedHashMap<>();

        try {
            ZipFile zipFile = new ZipFile(datasetZipFile.toString());
            ZipEntry zipEntry = zipFile.getEntry(datasetFileName);
            storeGamesFromFile(new InputStreamReader(zipFile.getInputStream(zipEntry)));
            storeStopwordsFromFile(new FileReader(stopwordsFile.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        this.allGames = new ArrayList<>();
        this.stopwords = new ArrayList<>();
        this.wordsToGamesIndexes = new LinkedHashMap<>();

        try {
            storeGamesFromFile(dataset);
            storeStopwordsFromFile(stopwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableCollection(allGames);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        List<BoardGameDistance> pairs = new ArrayList<>();

        for (BoardGame currentGame : allGames) {
            pairs.add(new BoardGameDistance(currentGame, getDistance(game, currentGame)));
        }

        return pairs.stream()
                .sorted(Comparator.comparing(BoardGameDistance::distance).reversed())
                .limit(n)
                .map(BoardGameDistance::boardGame)
                .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        Stream<String> streamKeywords = Arrays.stream(keywords)
                .filter(this.stopwords::contains);

        List<BoardGameKeywordsCount> pairs = new ArrayList<>();
        for (BoardGame boardGame : this.allGames) {
            List<String> str = Arrays.stream(boardGame.description().split
                            ("[\\p{IsPunctuation}\\p{IsWhite_Space}]+"))
                    .distinct()
                    .filter(this.stopwords::contains)
                    .toList();

            long matchCount = streamKeywords.filter(str::contains).count();

            pairs.add(new BoardGameKeywordsCount(boardGame, matchCount));
        }

        return pairs.stream()
                .sorted(Comparator.comparing(BoardGameKeywordsCount::count).reversed())
                .map(BoardGameKeywordsCount::boardGame)
                .toList();
    }

    @Override
    public void storeGamesIndex(Writer writer) {

    }

    private void storeGamesFromFile(Reader reader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String[] colNames = bufferedReader.readLine().split(";");

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(";");

                int id = Integer.parseInt(words[0]);
                String name = words[4];
                String description = words[8];
                int maxPlayers = Integer.parseInt(words[1]);
                int minAge = Integer.parseInt(words[2]);
                int minPlayers = Integer.parseInt(words[3]);
                int playingTimeMins = Integer.parseInt(words[5]);
                Collection<String> categories = Arrays.stream(words[6].split(",")).toList();
                Collection<String> mechanics = Arrays.stream(words[7].split(",")).toList();

                this.allGames.add(new BoardGame(id, name, description,
                        maxPlayers, minAge, minPlayers, playingTimeMins,
                        categories, mechanics));
            }
        }
    }

    private void storeStopwordsFromFile(Reader reader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String word = line.toString();

                this.stopwords.add(word);
            }
        }
    }

    private double getDistance(BoardGame boardGame1, BoardGame boardGame2) {
        double playingTime = Math.pow((boardGame1.playingTimeMins() - boardGame2.playingTimeMins()), 2);
        double maxPlayers = Math.pow((boardGame1.maxPlayers() - boardGame2.maxPlayers()), 2);
        double minAge = Math.pow((boardGame1.minAge() - boardGame2.minAge()), 2);
        double minPlayers = Math.pow((boardGame1.minPlayers() - boardGame2.minPlayers()), 2);

        double distanceForNumerical = Math.sqrt(playingTime + maxPlayers + minAge + minPlayers);


        double categoriesIntersectionPower = boardGame1.categories()
                .stream()
                .filter(boardGame2.categories()::contains)
                .count();

        double categoriesUnionPower = Stream.concat(boardGame1.categories().stream(), boardGame2.categories().stream())
                .distinct()
                .count();

        double mechanicsIntersectionPower = boardGame1.mechanics()
                .stream()
                .filter(boardGame2.categories()::contains)
                .count();

        double mechanicsUnionPower = Stream.concat(boardGame1.mechanics().stream(), boardGame2.mechanics().stream())
                .distinct()
                .count();

        double distanceForTyped = Math.sqrt(Math.pow((categoriesUnionPower - categoriesIntersectionPower), 2)
                + Math.pow((mechanicsUnionPower - mechanicsIntersectionPower), 2));

        return distanceForNumerical + distanceForNumerical;
    }
}
