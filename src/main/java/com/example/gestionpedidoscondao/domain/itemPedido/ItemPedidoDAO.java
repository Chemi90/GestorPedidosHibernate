package com.example.gestionpedidoscondao.domain.itemPedido;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase ItemPedidoDAO
 * Implementa la interfaz DAO para la gestión de objetos de tipo ItemPedido en la base de datos.
 * Permite realizar operaciones como buscar, guardar, actualizar y eliminar items de pedidos.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 */
public class ItemPedidoDAO implements DAO<ItemPedido> {

    /**
     * Busca y devuelve una lista de ItemPedido asociados con un código de Pedido específico.
     * Utiliza una consulta HQL para obtener los datos de la base de datos.
     * Maneja excepciones internamente y devuelve una lista vacía en caso de error.
     *
     * @param codPedido El código del Pedido para el cual buscar los items.
     * @return List<ItemPedido> Lista de objetos ItemPedido asociados con el código de Pedido dado.
     *         Retorna una lista vacía si no se encuentran resultados o si ocurre un error.
     */
    public List<ItemPedido> findItemsByPedidoCodigo(String codPedido) {
        List<ItemPedido> items = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ItemPedido> query = session.createQuery(
                    "SELECT ip FROM ItemPedido ip JOIN ip.pedido p JOIN ip.producto prod WHERE p.codigo = :codPedido", ItemPedido.class);
            query.setParameter("codPedido", codPedido);
            items = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Método no implementado para obtener todos los ItemPedido.
     *
     * @return ArrayList<ItemPedido> Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public ArrayList<ItemPedido> getAll() {return null;}

    /**
     * Método no implementado para obtener un ItemPedido por su ID.
     *
     * @param id El ID único del ItemPedido a obtener.
     * @return ItemPedido Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public ItemPedido get(Long id) {return null;}

    /**
     * Guarda un objeto ItemPedido en la base de datos.
     * Si el ItemPedido está asociado a un Pedido, actualiza el total de dicho Pedido.
     * Maneja transacciones y cierra la sesión al finalizar.
     * Lanza excepciones en caso de errores en el proceso de guardado.
     *
     * @param data El objeto ItemPedido a ser guardado.
     * @return ItemPedido El objeto ItemPedido guardado.
     * @throws Exception Si ocurre algún error durante el proceso de guardado.
     */
    @Override
    public ItemPedido save(ItemPedido data) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.persist(data);

            Pedido pedido = data.getPedido();
            if (pedido != null) {
                // Consulta para obtener el total actualizado
                Query<Double> query = session.createQuery(
                        "SELECT SUM(ip.cantidad * prod.precio) FROM ItemPedido ip " +
                                "JOIN ip.producto prod WHERE ip.pedido.id = :pedidoId", Double.class);
                query.setParameter("pedidoId", pedido.getId());
                Double nuevoTotal = query.getSingleResult();

                // Actualizar el total del Pedido
                pedido.setTotal(nuevoTotal != null ? nuevoTotal : 0.0);
                session.update(pedido);
            }

            transaction.commit();
            return data;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Método no implementado para actualizar un ItemPedido.
     *
     * @param itemPedido El objeto ItemPedido a actualizar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void update(ItemPedido itemPedido) {}

    /**
     * Método no implementado para eliminar un ItemPedido.
     *
     * @param data El objeto ItemPedido a eliminar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void delete(ItemPedido data) {}

    /**
     * Elimina un ItemPedido de la base de datos y actualiza el total del Pedido asociado.
     * Realiza la operación en una transacción y maneja posibles errores.
     *
     * @param item El ItemPedido a eliminar.
     * @return boolean Verdadero si el item fue eliminado con éxito, falso en caso contrario.
     * @throws RuntimeException Si ocurre un error durante la operación de eliminación.
     */
    @Override
    public boolean remove(ItemPedido item) {
        boolean salida = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ItemPedido itemToRemove = session.get(ItemPedido.class, item.getId());
            if (itemToRemove != null) {
                Pedido pedido = itemToRemove.getPedido();
                session.remove(itemToRemove);

                List<ItemPedido> itemsActuales = session.createQuery("FROM ItemPedido ip WHERE ip.pedido.id = :pedidoId AND ip.id != :itemId", ItemPedido.class)
                        .setParameter("pedidoId", pedido.getId())
                        .setParameter("itemId", itemToRemove.getId())
                        .getResultList();

                double nuevoTotal = itemsActuales.stream().mapToDouble(ItemPedido::getPrecioTotal).sum();
                pedido.setTotal(nuevoTotal);

                session.update(pedido);

                salida = true;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return salida;
    }
}
