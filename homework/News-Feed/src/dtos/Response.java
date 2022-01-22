package dtos;

import entities.Article;
import entities.Status;

import java.util.List;

//check for code and message
public record Response(Status status, Long totalResults, List<Article> articles, String code, String message) {
}
