package com.example.gestionpedidoscondao.domain;

import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public interface DAO<T> {

    public ArrayList<T> getAll();
    public T get(Long id);
    public T save(T data);
    public void update(T data);
    public void delete(T data);
}
