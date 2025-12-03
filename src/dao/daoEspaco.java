package dao;

import model.Espaco;
import model.SalaDeReuniao;
import model.CabineIndividual;
import model.Auditorio;
import exception.ErroAoSalvar;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public class daoEspaco implements Persistencia<Espaco> {
    
    private static final String ARQUIVO = "data/espacos.json";
    private List<Espaco> lista;

    public daoEspaco() {
        this.lista = new ArrayList<>();
        carregar();
    }

    private void salvar() {
        try {
            new File("data").mkdirs();
            
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();

            for (Espaco espaco : lista) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", espaco.getId());
                jsonObject.addProperty("nome", espaco.getNome());
                jsonObject.addProperty("capacidade", espaco.getCapacidade());
                jsonObject.addProperty("disponivel", espaco.isDisponivel());
                jsonObject.addProperty("precoPorHora", espaco.getPrecoPorHora());
                jsonObject.addProperty("tipo", espaco.getClass().getSimpleName());
                
                if (espaco instanceof SalaDeReuniao) {
                    SalaDeReuniao sala = (SalaDeReuniao) espaco;
                    jsonObject.addProperty("projetor", sala.isProjetor());
                }
                
                jsonArray.add(jsonObject);
            }

            FileWriter writer = new FileWriter(ARQUIVO);
            gson.toJson(jsonArray, writer);
            writer.close();

        } catch (Exception e) {
            throw new ErroAoSalvar("Erro ao salvar espaços");
        }
    }

    private void carregar() {
        try {
            File arquivo = new File(ARQUIVO);
            if (!arquivo.exists()) {
                return;
            }
            
            FileReader reader = new FileReader(ARQUIVO);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String tipo = obj.get("tipo").getAsString();
                
                int id = obj.get("id").getAsInt();
                String nome = obj.get("nome").getAsString();
                int capacidade = obj.get("capacidade").getAsInt();
                boolean disponivel = obj.get("disponivel").getAsBoolean();
                double precoPorHora = obj.get("precoPorHora").getAsDouble();

                Espaco espaco;
                switch (tipo) {
                    case "SalaDeReuniao":
                        boolean projetor = obj.get("projetor").getAsBoolean();
                        espaco = new SalaDeReuniao(id, nome, capacidade, disponivel, precoPorHora, projetor);
                        break;
                    case "CabineIndividual":
                        espaco = new CabineIndividual(id, nome, capacidade, disponivel, precoPorHora);
                        break;
                    case "Auditorio":
                        espaco = new Auditorio(id, nome, capacidade, disponivel, precoPorHora);
                        break;
                    default:
                        continue;
                }
                this.lista.add(espaco);
            }
        } catch (Exception e) {
            this.lista = new ArrayList<>();
        }
    }

    private int novoId() {
        if (lista.isEmpty()) return 1;
        int maxId = 0;
        for (Espaco espaco : lista) {
            if (espaco.getId() > maxId) {
                maxId = espaco.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public void salvar(Espaco obj) {
        obj.setId(novoId());
        lista.add(obj);
        salvar();
    }

    @Override
    public List<Espaco> listar() {
        return new ArrayList<>(lista);
    }

    @Override
    public Espaco buscar(int id) {
        for (Espaco espaco : lista) {
            if (espaco.getId() == id) {
                return espaco;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Espaco obj) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == obj.getId()) {
                lista.set(i, obj);
                salvar();
                return;
            }
        }
    }

    @Override
    public void deletar(int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == id) {
                lista.remove(i);
                salvar();
                return;
            }
        }
    }
}