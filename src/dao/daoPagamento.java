package dao;

import model.Pagamento;
import model.Reserva;
import exception.ErroAoSalvar;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class daoPagamento implements Persistencia<Pagamento> {
    
    private static final String ARQUIVO = "data/pagamentos.json";
    private List<Pagamento> lista;
    private daoReserva reservaDao;
    private Gson gson = new Gson();

    public daoPagamento() {
        reservaDao = new daoReserva();
        carregar();
    }

    private void salvar() {
        try {
            new File("data").mkdirs();
            
            JsonArray json = new JsonArray();
            
            for (Pagamento pagamento : lista) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id", pagamento.getId());
                obj.addProperty("reservaId", pagamento.getReserva().getId());
                obj.addProperty("valor", pagamento.getValor());
                obj.addProperty("data", pagamento.getData().toString());
                obj.addProperty("metodo", pagamento.getMetodo().name());
                json.add(obj);
            }

            Writer writer = new FileWriter(ARQUIVO);
            gson.toJson(json, writer);
            writer.close();
            
        } catch (Exception e) {
            throw new ErroAoSalvar("Erro ao salvar pagamentos");
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
                int reservaId = obj.get("reservaId").getAsInt();
                double valor = obj.get("valor").getAsDouble();
                LocalDateTime data = LocalDateTime.parse(obj.get("data").getAsString());
                Pagamento.Metodo metodo = Pagamento.Metodo.valueOf(obj.get("metodo").getAsString());

                Reserva reserva = reservaDao.buscar(reservaId);
                if (reserva != null) {
                    Pagamento pagamento = new Pagamento(id, reserva, valor, data, metodo);
                    this.lista.add(pagamento);
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
    public void salvar(Pagamento obj) {
        obj.setId(novoId());
        lista.add(obj);
        salvar();
    }

    @Override
    public List<Pagamento> listar() {
        return lista;
    }

    @Override
    public Pagamento buscar(int id) {
        for (Pagamento pagamento : lista) {
            if (pagamento.getId() == id) {
                return pagamento;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Pagamento obj) {
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
        lista.removeIf(pagamento -> pagamento.getId() == id);
        salvar();
    }
}