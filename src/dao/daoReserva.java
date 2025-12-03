package dao;

import model.Reserva;
import model.Espaco;
import exception.ErroAoSalvar;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class daoReserva implements Persistencia<Reserva> {
    
    private static final String ARQUIVO = "data/reservas.json";
    private List<Reserva> lista;
    private daoEspaco espacoDao;
    private Gson gson = new Gson();

    public daoReserva() {
        espacoDao = new daoEspaco();
        carregar();
    }

    private void salvar() {
        try {
            new File("data").mkdirs();
            
            JsonArray json = new JsonArray();
            
            for (Reserva reserva : lista) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", reserva.getId());
                obj.addProperty("espacoId", reserva.getEspaco().getId());
                obj.addProperty("inicio", reserva.getInicio().toString());
                obj.addProperty("fim", reserva.getFim().toString());
                obj.addProperty("status", reserva.getStatus().name());
                obj.addProperty("taxa", reserva.getTaxa());
                json.add(obj);
            }

            Writer writer = new FileWriter(ARQUIVO);
            gson.toJson(json, writer);
            writer.close();
            
        } catch (Exception e) {
            throw new ErroAoSalvar("Erro ao salvar reservas");
        }
    }

    private void carregar() {
        try {
            File arquivo = new File(ARQUIVO);
            
            if (!arquivo.exists()) {
                this.lista = new ArrayList<>();
                return;
            }
            
            Reader reader = new FileReader(ARQUIVO);
            JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();
            
            this.lista = new ArrayList<>();
            
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();

                int id = obj.get("id").getAsInt();
                int espacoId = obj.get("espacoId").getAsInt();
                LocalDateTime inicio = LocalDateTime.parse(obj.get("inicio").getAsString());
                LocalDateTime fim = LocalDateTime.parse(obj.get("fim").getAsString());
                Reserva.Status status = Reserva.Status.valueOf(obj.get("status").getAsString());
                double taxa = obj.get("taxa").getAsDouble();

                Espaco espaco = espacoDao.buscar(espacoId);
                if (espaco != null) {
                    Reserva reserva = new Reserva(id, espaco, inicio, fim);
                    reserva.setStatus(status);
                    reserva.setTaxa(taxa);
                    this.lista.add(reserva);
                }
            }
        } catch (Exception e) {
            this.lista = new ArrayList<>();
        }
    }

    private int novoId() {
        return lista.isEmpty() ? 1 : lista.get(lista.size() - 1).getId() + 1;
    }

    @Override
    public void salvar(Reserva obj) {
        obj.setId(novoId());
        lista.add(obj);
        salvar();
    }

    @Override
    public List<Reserva> listar() {
        return lista;
    }

    @Override
    public Reserva buscar(int id) {
        for (Reserva reserva : lista) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Reserva obj) {
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
        lista.removeIf(reserva -> reserva.getId() == id);
        salvar();
    }
}