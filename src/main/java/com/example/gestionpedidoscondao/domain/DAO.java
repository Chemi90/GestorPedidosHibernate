package com.example.gestionpedidoscondao.domain;

import java.util.ArrayList;

/**
 * Interfaz DAO.
 * Define las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para un tipo genérico T.
 * Esta interfaz es utilizada por clases DAO para implementar operaciones de base de datos estándar.
 *
 * @param <T> Tipo genérico que representa la entidad para la cual se implementa el DAO.
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
public interface DAO<T> {

    public ArrayList<T> getAll();
    public T get(Long id);
    public T save(T data);
    public void update(T data);
    public void delete(T data);
    boolean remove(T t);
}
