package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable{
    
    @FXML Pagination pagination;
    private int pagina = 1;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        pagination.setPageCount(42);
        pagination.setPageFactory(pag ->{
            pagina = pag + 1;
            return carregarPersonagem();
            
        });
    }


    public FlowPane carregarPersonagem() {
        try {
            var url = new URL("https://rickandmortyapi.com/api/character?page=" + pagina);
            var con = url.openConnection();

            con.connect();

            var is = con.getInputStream();

            var reader = new BufferedReader(new InputStreamReader(is));

            var json = reader.readLine();

            var lista = jsonParaLista(json);

            mostrarPersonagens(lista);
            
            return mostrarPersonagens(lista);

        } catch (IOException e) {
            e.printStackTrace();
               return null;
        }

    }

    private List<Personagem> jsonParaLista(String json) throws IOException {
        var mapper = new ObjectMapper();
        var results = mapper.readTree(json).get("results");
        List<Personagem> personagens = new ArrayList<>();
        
        

        results.forEach(personagem -> {
            try {
                var p = mapper.readValue(personagem.toString(), Personagem.class);
                personagens.add(p);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        return personagens;
    }

    private FlowPane mostrarPersonagens(List<Personagem> lista) {
        var flow = new FlowPane();
        flow.setVgap(10);
        flow.setHgap(10);
        lista.forEach(personagem -> {
            var image = new ImageView(new Image(personagem.getImage()));
            image.setFitWidth(200);
            image.setFitHeight(200);
            var labelName = new Label(personagem.getName());
            var labelEspecie = new Label(personagem.getSpecies());
            flow.getChildren().add(
                new VBox(image, labelName, labelEspecie)
            );
        });
        return flow;
    }
}
