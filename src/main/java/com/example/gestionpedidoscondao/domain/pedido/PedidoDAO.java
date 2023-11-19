package com.example.gestionpedidoscondao.domain.pedido;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz PedidoDAO para interactuar con la base de datos
 * y recuperar información sobre los pedidos.
 *
 * Esta implementación utiliza una base de datos SQL para realizar las operaciones CRUD.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 1.0
 */
public class PedidoDAO implements DAO<Pedido> {

    /**
     * Recupera todos los pedidos asociados a un ID de usuario específico.
     *
     * @param usuarioId el ID del usuario para el cual se buscan los pedidos.
     * @return una lista de objetos {@link Pedido}, cada uno representando un pedido
     *         asociado al usuario proporcionado.
     */
    public List<Pedido> findByUsuarioId(int usuarioId) {
        List<Pedido> pedidos = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Pedido> query = session.createQuery(
                    "from Pedido where usuario.id = :usuarioId", Pedido.class);
            query.setParameter("usuarioId", usuarioId);
            pedidos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }


    @Override
    public ArrayList<Pedido> getAll() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Pedido> query = ((org.hibernate.Session) session).createQuery("from Pedido", Pedido.class);
            pedidos = (ArrayList<Pedido>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }


    @Override
    public Pedido get(Long id) {
        Pedido pedido = null;
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            pedido = ((org.hibernate.Session) session).get(Pedido.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedido;
    }

    @Override
    public Pedido save(Pedido data) {
        Pedido salida = null;
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
    public void update(Pedido data) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Iniciar una transacción
            transaction = session.beginTransaction();

            // Actualizar el objeto Pedido en la base de datos
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
    public void delete(Pedido data) {

    }

    public void deleteByCodigo(String codigoPedido) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Eliminar todos los ItemPedido asociados con el Pedido
            Query itemPedidoDeleteQuery = session.createQuery("DELETE FROM ItemPedido ip WHERE ip.pedido.codigo = :codigoPedido");
            itemPedidoDeleteQuery.setParameter("codigoPedido", codigoPedido);
            itemPedidoDeleteQuery.executeUpdate();

            // Eliminar el Pedido si existe
            Query<Pedido> pedidoQuery = session.createQuery("FROM Pedido WHERE codigo = :codigo", Pedido.class);
            pedidoQuery.setParameter("codigo", codigoPedido);
            Pedido pedido = pedidoQuery.uniqueResult();
            if (pedido != null) {
                session.remove(pedido);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
