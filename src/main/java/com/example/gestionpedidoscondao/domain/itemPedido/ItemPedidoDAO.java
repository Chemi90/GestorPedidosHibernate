package com.example.gestionpedidoscondao.domain.itemPedido;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link ItemPedidoDAO} para interactuar con la base de datos
 * y realizar operaciones CRUD para los {@code ItemPedido}.
 * <p>
 * Esta clase provee la funcionalidad para obtener los detalles de los ítems de un pedido específico
 * a través de la base de datos utilizando JDBC.
 * </p>
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 1.0
 */
public class ItemPedidoDAO implements DAO<ItemPedido> {

    /**
     * Busca y devuelve todos los ítems asociados a un código de pedido específico.
     * <p>
     * Esta implementación realiza una consulta a la base de datos para obtener los ítems del pedido,
     * incluyendo el nombre del producto y el precio total calculado en función de la cantidad.
     * </p>
     *
     * @param codPedido El código único del pedido cuyos ítems se desean recuperar.
     * @return Una lista de {@code ItemPedido} que contiene los ítems del pedido especificado.
     * @throws SQLException Si ocurre algún problema durante la ejecución de la consulta SQL.
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



    @Override
    public ArrayList<ItemPedido> getAll() {
        ArrayList<ItemPedido> items = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ItemPedido> query = session.createQuery("from ItemPedido", ItemPedido.class);
            items = (ArrayList<ItemPedido>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


    @Override
    public ItemPedido get(Long id) {
        ItemPedido item = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            item = session.get(ItemPedido.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }


    @Override
    public ItemPedido save(ItemPedido data) {
        ItemPedido salida = null;
        try( org.hibernate.Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction t = s.beginTransaction();
            s.persist(data);
            t.commit();
            salida = data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return salida;
    }

    @Override
    public void update(ItemPedido data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Iniciar una transacción
            transaction = session.beginTransaction();

            // Actualizar el objeto ItemPedido en la base de datos
            session.update(data);

            // Commit la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ItemPedido data) {

    }
}
