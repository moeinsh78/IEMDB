package InterfaceServer;

import IEMDBClasses.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HTMLHandler {
    public String createMoviesHTML(HashMap<Integer, Movie> movies) throws IOException {
        File movies_page = new File("src/main/resources/movies.html");
        Document movies_page_doc = Jsoup.parse(movies_page, "UTF-8");
        addEmptyTableRows(movies_page_doc, movies.size() - 1);
        int row_number = 1;
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovieInfoToTable(movies_page_doc, movie.getValue(), row_number);
            row_number += 1;
        }
        return movies_page_doc.html();
    }

    public String createMovieInfoHTML(Movie movie) throws IOException {
        File movie_template_page = new File("src/main/resources/movie.html");
        Document movie_info_page_doc = Jsoup.parse(movie_template_page, "UTF-8");
        addMovieInfoToUnorderedList(movie_info_page_doc, movie);

        movie_info_page_doc.getElementById("ratingFormHidden").attr("value", String.valueOf(movie.getId()));
        movie_info_page_doc.getElementById("watchListFormHidden").attr("value", String.valueOf(movie.getId()));

        addEmptyTableRows(movie_info_page_doc, movie.getComments().size() - 1);
        int row_number = 1;
        for (Comment comment : movie.getComments()) {
            addCommentToCommentsTable(movie_info_page_doc, comment, movie.getId(), row_number);
            row_number += 1;
        }
        return movie_info_page_doc.html();
    }

    public String createActorHTML(Actor actor, HashMap<Integer, Movie> movies) throws IOException {
        File actor_page = new File("src/main/resources/actor.html");
        Document actor_page_doc = Jsoup.parse(actor_page, "UTF-8");
        addActorInfoToList(actor_page_doc, actor, String.valueOf(movies.size()));
        addEmptyTableRows(actor_page_doc, movies.size() - 1);
        int row_number = 1;
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovieInfoToActorTable(actor_page_doc, movie.getValue(), row_number);
            row_number += 1;
        }
        return actor_page_doc.html();
    }

    public String getResponseCodeHtml(String code) throws IOException {
        String template_add = "src/main/resources/" + code + ".html";
        File error_page = new File(template_add);
        Document error_page_doc = Jsoup.parse(error_page, "UTF-8");
        return error_page_doc.html();
    }

    private void addCommentToCommentsTable(Document movie_info_page_doc, Comment comment, Integer movie_id,
                                           int row_number) {
        Element comments_table = movie_info_page_doc.getElementsByTag("table").first();
        Element row = comments_table.getElementsByTag("tr").get(row_number);
        row.getElementsByTag("td").get(0).text("@" + comment.getUserNickname());
        row.getElementsByTag("td").get(1).text(comment.getText());

        Map<String, Integer> votes_counts = comment.getVotesCount();

        Element like_and_dislikes = row.getElementsByTag("td").get(2);

        like_and_dislikes.getElementsByTag("input").get(0).attr("value", String.valueOf(movie_id));

        like_and_dislikes.getElementsByTag("input").get(1).attr("value", String.valueOf(comment.getId()));

        like_and_dislikes.getElementsByTag("label").get(0).text(String.valueOf(votes_counts.get("like")));

        like_and_dislikes.getElementsByTag("label").get(1).text(String.valueOf(votes_counts.get("dislike")));
    }

    private void addMovieInfoToUnorderedList(Document movie_info_page_doc, Movie movie) {
        List<String> movie_info_ids = Arrays.asList("name", "summary", "releaseDate", "director", "writers",
                "genres", "cast", "imdbRate", "rating", "duration", "ageLimit");
        List<String> movie_info_items = getMovieInfoItemsList(movie);

        Element u_list = movie_info_page_doc.getElementsByTag("ul").first();
        int movie_item_index = 0;
        for (String element_id: movie_info_ids) {
            u_list.getElementById(element_id).append(movie_info_items.get(movie_item_index));
            movie_item_index += 1;
        }
    }

    private List<String> getMovieInfoItemsList(Movie movie) {
        String writers_str = convertListOfStringsToString(movie.getWriters());
        String genres_str = convertListOfStringsToString(movie.getGenres());
        String cast_str = convertListOfStringsToString(movie.getCastNames());
        String rating;
        try { rating = String.valueOf(movie.getRatingsAverage()); }
        catch (ArithmeticException e) { rating = "null"; }

        return Arrays.asList(movie.getName(), movie.getSummary(), movie.getReleaseDate(),
                movie.getDirector(), writers_str, genres_str, cast_str, String.valueOf(movie.getImdbRate()), rating,
                String.valueOf(movie.getDuration()), String.valueOf(movie.getAgeLimit())
        );
    }

    private void addMovieInfoToTable(Document movies_page_doc, Movie movie, int row_number) {
        List<String> movie_info_items = getMovieInfoItemsList(movie);
        String movie_link = "/movie/" + movie.getId();
        Element row = movies_page_doc.getElementsByTag("table").first()
                .getElementsByTag("tr").get(row_number);
        for (int i = 0; i < movie_info_items.size(); i++)
            row.getElementsByTag("td").get(i).text(movie_info_items.get(i));

        Element link_tag = row.getElementsByTag("td").get(movie_info_items.size());
        link_tag.getElementsByTag("a").first().attr("href", movie_link);
    }

    private void addMovieInfoToActorTable(Document actor_page_doc, Movie movie, int row_number) {
        String rating;
        String movie_link = "/movie/" + movie.getId();
        try { rating = String.valueOf(movie.getRatingsAverage()); }
        catch (ArithmeticException e) { rating = "null"; }

        List<String> movie_info_items = Arrays.asList(movie.getName(), String.valueOf(movie.getImdbRate()), rating);
        Element row = actor_page_doc.getElementsByTag("table").first()
                .getElementsByTag("tr").get(row_number);
        for (int i = 0; i < movie_info_items.size(); i++)
            row.getElementsByTag("td").get(i).text(movie_info_items.get(i));

        Element link_tag = row.getElementsByTag("td").get(movie_info_items.size());
        link_tag.getElementsByTag("a").first().attr("href", movie_link);
    }

    private void addEmptyTableRows(Document movies_page_doc, int empty_rows_count) {
        Element table = movies_page_doc.getElementsByTag("table").first();
        Element empty_row = table.getElementsByTag("tr").last();
        for (int i = 0; i < empty_rows_count; i++)
            table.append(empty_row.html());
    }

    private void addActorInfoToList(Document actor_page_doc, Actor actor, String tma) {
        Element list = actor_page_doc.getElementsByTag("ul").first();
        list.getElementById("name").append(actor.getName());
        list.getElementById("birthDate").append(actor.getBirthday());
        list.getElementById("nationality").append(actor.getNationality());
        list.getElementById("tma").append(tma);
    }

    private void addUserInfoToList(Document user_page_doc, User user) {
        Element list = user_page_doc.getElementsByTag("ul").first();
        list.getElementById("name").append(user.getName());
        list.getElementById("nickname").append(user.getNickname());
    }

    public String createWatchListHTML(User user, HashMap<Integer, Movie> movies) throws IOException {
        File user_page = new File("src/main/resources/watchlist.html");
        Document user_page_doc = Jsoup.parse(user_page, "UTF-8");
        addUserInfoToList(user_page_doc, user);
        addEmptyTableRows(user_page_doc, movies.size() - 1);
        int row_number = 1;
        for (HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovieInfoToUserTable(user_page_doc, movie.getValue(), user.getEmail(), row_number);
            row_number += 1;
        }
        return user_page_doc.html();
    }

    private void addMovieInfoToUserTable(Document user_page_doc, Movie movie, String user_id, int row_number) {
        String genres_str = convertListOfStringsToString(movie.getGenres());
        String rating;
        try { rating = String.valueOf(movie.getRatingsAverage()); }
        catch (ArithmeticException e) { rating = "null"; }
        List<String> movie_info_items = Arrays.asList(movie.getName(), movie.getReleaseDate(),
                movie.getDirector(), genres_str, String.valueOf(movie.getImdbRate()), rating,
                String.valueOf(movie.getDuration()));
        String movie_link = "/movie/" + movie.getId();
        Element row = user_page_doc.getElementsByTag("table").first()
                .getElementsByTag("tr").get(row_number);
        for (int i = 0; i < movie_info_items.size(); i++)
            row.getElementsByTag("td").get(i).text(movie_info_items.get(i));

        Element link_tag = row.getElementsByTag("td").get(movie_info_items.size());
        link_tag.getElementsByTag("a").first().attr("href", movie_link);
        Element remove_button_cell = row.getElementsByTag("td").last();
        remove_button_cell.getElementsByTag("input").first().attr("value", String.valueOf(movie.getId()));
        remove_button_cell.getElementsByTag("input").last().attr("value", user_id);
    }

    private String convertListOfStringsToString(ArrayList<String> list_of_items) {

        // In tabe vase vaqti ke list khaali bashe niaz be test dare :))

        StringBuilder items_str = new StringBuilder();
        for (String item: list_of_items)
            items_str.append(item).append(", ");

        if(items_str.length() > 0)
            items_str = new StringBuilder(items_str.substring(0, items_str.length() - 2));

        return items_str.toString();
    }
}
