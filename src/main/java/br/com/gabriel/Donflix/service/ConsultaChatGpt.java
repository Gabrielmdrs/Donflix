package br.com.gabriel.Donflix.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGpt {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("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");


        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-1106")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();


        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }

}
