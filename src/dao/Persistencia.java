package dao;

import java.util.List;

public interface Persistencia<T> {
    void salvar(T obj);
    List<T> listar();
    T buscar(int id);
    void atualizar(T obj);
    void deletar(int id);
}