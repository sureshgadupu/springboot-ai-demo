package dev.fullstackcode.spring.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chatai")
public class ChatAIController {


    private final ChatClient chatClient;

    public ChatAIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/movieRecommendation")
    public String getMovieRecommendations(@RequestParam(value = "genre") String genre) {
        String userPrompt = "Recommend me a movie in %s genre".formatted(genre);
        ChatResponse response =
                chatClient.prompt(new Prompt(userPrompt)).call().chatResponse();
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/movieRecommendationWithSystemPrompt")
    public String getMovieRecommendationsWithSystemPrompt(@RequestParam(value = "genre") String genre) {
        String userPrompt = "Recommend me a movie in %s genre".formatted(genre);
        String systemPrompt = "You are a movie recommender, helping users discover new films " +
                "based on their preferences, moods, and interests. Offer personalized " +
                "recommendations, provide insights into the movies' plots, themes, and key " +
                "features, and suggest similar films that users may enjoy. Help users find their " +
                "next favorite movie experience.";
        ChatResponse response =
                chatClient.prompt().user(userPrompt).system(systemPrompt).call().chatResponse();
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/moviesRecommendationOfActor")
    public ActorMovies getMoviesRecommendationOfActor(@RequestParam(value = "actor") String actor) {
        String userPrompt = "Recommend block buster movies for actor %s".formatted(actor);
        return chatClient.prompt().user(userPrompt).call().entity(ActorMovies.class);
    }

    @GetMapping("/moviesRecommendationOfActors")
    public List<ActorMovies> getMoviesRecommendationOfActors() {
        String userPrompt = "Recommend block buster movies for actor %s and %s".formatted("pierce" +
                " brasnon", "johnny depp");
        return chatClient.prompt().user(userPrompt).call().entity(new ParameterizedTypeReference<List<ActorMovies>>() {});
    }

    @GetMapping("/bookRecommendation")
    public String getBookRecommendations(@RequestParam(value = "category") String category) {
        String userPrompt = "Recommend me a book in %s genre".formatted(category);
        return chatClient.prompt().user(userPrompt).call().content();
    }

    @GetMapping("/seriesRecommendation")
    public String getWebSeriesRecommendations(@RequestParam(value = "category") String category) {
        String userPrompt = "Recommend me a web series in %s genre".formatted(category);
        ChatResponse response =
                chatClient.prompt().user(userPrompt).call().chatResponse();
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/streamSeriesRecommendation")
    public Flux<String> getStreamOfWebSeriesRecommendations(@RequestParam(value = "category") String category) {
        String userPrompt = "Recommend me a web series in %s genre".formatted(category);
        return chatClient.prompt().user(userPrompt).stream().content();
    }

}
